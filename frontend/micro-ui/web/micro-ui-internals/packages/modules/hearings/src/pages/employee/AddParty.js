import { Button, CloseSvg, FormComposerV2, Modal } from "@egovernments/digit-ui-react-components";
import React, { useCallback, useRef, useState } from "react";
import addPartyConfig from "../../configs/AddNewPartyConfig.js";
import { useTranslation } from "react-i18next";
import SelectCustomNote from "@egovernments/digit-ui-module-dristi/src/components/SelectCustomNote.js";
import { Urls } from "../../hooks/services/Urls.js";

const AddParty = ({ onCancel, onAddSuccess, caseData, tenantId, hearing, refetchHearing }) => {
  const { t } = useTranslation();
  const DRISTIService = Digit?.ComponentRegistryService?.getComponent("DRISTIService");
  const [formConfigs, setFormConfigs] = useState([addPartyConfig(1)]);
  const [aFormData, setFormData] = useState([{}]);
  const setFormErrors = useRef([]);

  const { mutateAsync: updateAttendees } = Digit.Hooks.useCustomAPIMutationHook({
    url: Urls.hearing.hearingUpdateTranscript,
    params: { applicationNumber: "", cnrNumber: "" },
    body: { tenantId, hearingType: "", status: "" },
    config: {
      mutationKey: "addAttendee",
    },
  });

  const CloseBtn = (props) => {
    return (
      <div onClick={props.onClick} style={{ height: "100%", display: "flex", alignItems: "center", paddingRight: "20px", cursor: "pointer" }}>
        <CloseSvg />
      </div>
    );
  };

  const handleAddParty = () => {
    const newConfig = addPartyConfig(formConfigs.length + 1);
    setFormConfigs([...formConfigs, newConfig]);
    setFormData((prev) => [...prev, {}]);
  };

  const handleRemoveParty = () => {
    if (formConfigs.length > 1) {
      setFormConfigs(formConfigs.slice(0, -1));
    }
    if (aFormData.length > 1) {
      setFormData(aFormData.slice(0, -1));
    }
  };

  const validateFormData = (data, index) => {
    const errors = {};
    if (!data["partyName" + index] || !/^[a-zA-Z\s]+$/.test(data["partyName" + index])) errors["partyName" + index] = "Party name is required";
    if (!data["partyType" + index]) errors["partyType" + index] = "Party type is required";
    if (!data["phoneNumber" + index] || !/^\d{10}$/.test(data["phoneNumber" + index])) errors["phoneNumber" + index] = "Phone number is invalid";
    if (!data["emailId" + index] || !/\S+@\S+\.\S+/.test(data["emailId" + index])) errors["emailId" + index] = "Email is invalid";
    if (!data["address" + index]) errors["address" + index] = "Address is required";
    return errors;
  };

  const handleSubmit = (e) => {
    e?.stopPropagation();
    e?.preventDefault();
    const cleanedData = aFormData
      .map(({ data }, index) => {
        const newData = {};
        Object.keys(data).forEach((key) => {
          const newKey = key.replace(/\d+$/, "");
          if (newKey === "partyType") {
            newData[newKey] = data[key].name;
          } else {
            newData[newKey] = data[key];
          }
        });
        newData.uuid = generateUUID();
        const errors = validateFormData(data, index + 1);
        if (Object.keys(errors).length > 0) {
          Object.entries(errors).forEach(([errorKey, value]) => {
            setFormErrors.current[index](errorKey, value);
          });
          return null;
        }
        return newData;
      })
      .filter(Boolean);

    if (cleanedData.length === aFormData.length) {
      onAdd(cleanedData)
        .catch(console.error)
        .then(() => {
          onAddSuccess();
          onCancel();
        });
    }
  };
  const generateUUID = () => {
    return "xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx".replace(/[xy]/g, function (c) {
      const r = (Math.random() * 16) | 0;
      const v = c === "x" ? r : (r & 0x3) | 0x8;
      return v.toString(16);
    });
  };
  const onAdd = async (cleanedData) => {
    const newWitnesses = cleanedData.map((data) => {
      return {
        isenabled: true,
        displayindex: 0,
        data: {
          emails: { emailId: [data.emailId], textFieldValue: "" },
          firstName: data.partyName,
          lastName: "",
          phonenumbers: {
            mobileNumber: [data.phoneNumber],
            textFieldValue: "",
          },
          addressDetails: [{ addressDetails: data?.address }],
          witnessAdditionalDetails: {
            text: data.additionalDetails,
          },
          uuid: data.uuid,
        },
      };
    });

    const caseDetails = {
      ...caseData?.criteria?.[0]?.responseList?.[0],
    };
    const witnessDetails = caseDetails.additionalDetails?.witnessDetails
      ? [...caseDetails.additionalDetails?.witnessDetails?.formdata, ...newWitnesses]
      : [...newWitnesses];

    await DRISTIService.addWitness(
      {
        tenantId,
        caseFilingNumber: caseDetails.filingNumber,
        additionalDetails: {
          ...caseDetails.additionalDetails,
          witnessDetails: {
            formdata: witnessDetails,
          },
        },
      },
      tenantId
    );

    if (hearing) {
      const updatedHearing = structuredClone(hearing);
      updatedHearing.attendees = updatedHearing.attendees || [];
      updatedHearing.attendees.push(
        ...newWitnesses.map((witness) => {
          return {
            name: [witness.data.firstName, witness.data.lastName].join(" "),
            type: "Witness",
            wasPresent: false,
            isOnline: false,
          };
        })
      );

      await updateAttendees({ body: { hearing: updatedHearing } });
      refetchHearing?.();
    }
  };

  const onFormValueChange = useCallback(
    (formData, index) => {
      if (JSON.stringify(formData) !== JSON.stringify(aFormData[index].data)) {
        setFormData((prevData) => prevData.map((item, i) => (i === index ? { ...item, data: formData } : item)));
      }
    },
    [aFormData]
  );

  return (
    <Modal
      popupStyles={{
        width: "60%",
        minWidth: "600px",
        position: "absolute",
        height: "calc(100% - 100px)",
        top: "50%",
        left: "50%",
        transform: "translate(-50%, -50%)",
        justify: "space-between",
      }}
      popupModuleMianStyles={{
        padding: 0,
        margin: "0px",
        height: "calc(100% - 100px)",
        overflowY: "auto",
      }}
      headerBarMain={<h1 className="heading-m">{t("ADD_NEW_PARTY")}</h1>}
      headerBarEnd={<CloseBtn onClick={onCancel} />}
      // actionCancelLabel={t("HEARING_BACK")}
      // actionCancelOnSubmit={onCancel}
      actionSaveLabel={t("HEARING_ADD")}
      actionSaveOnSubmit={handleSubmit}
    >
      <div style={{ padding: "16px 0px 24px 0px" }}>
        <SelectCustomNote
          config={{
            populators: {
              inputs: [
                {
                  infoHeader: "CS_PLEASE_COMMON_NOTE",
                  infoText: "NEW_PARTY_NOTE",
                  infoTooltipMessage: "Tooltip",
                  type: "InfoComponent",
                },
              ],
            },
          }}
          t={t}
        />
      </div>
      <div className="add-party">
        {formConfigs.map((config, index) => (
          <FormComposerV2
            key={index}
            config={[config]}
            onFormValueChange={(setValue, formData, formState, reset, setError, clearErrors, trigger, getValues) => {
              onFormValueChange(formData, index);
              if (!setFormErrors.current.hasOwnProperty(index)) {
                setFormErrors.current[index] = setError;
              }
              if (JSON.stringify(formData) !== JSON.stringify(aFormData[index].data)) {
                if (formData && Object.keys(formData).length !== 0) {
                  const errors = validateFormData(formData, index + 1);
                  for (const key of Object.keys(formData)) {
                    if (formData[key] && !errors.hasOwnProperty(key)) {
                      clearErrors(key);
                    }
                  }
                }
              }
            }}
            fieldStyle={{ width: "100%" }}
          />
        ))}
      </div>
      <div style={{ display: "flex", justifyContent: "space-between", marginBottom: "3rem" }}>
        <Button
          onButtonClick={handleAddParty}
          label={t("ADD_PARTY")}
          style={{
            border: "none",
            boxShadow: "none",
            marginTop: "10px",
            borderColor: "#007E7E",
            width: "28%",
            backgroundColor: "#fff",
          }}
          textStyles={{
            fontFamily: "Roboto",
            fontSize: "16px",
            fontWeight: 700,
            lineHeight: "18.75px",
            textAlign: "start",
            color: "#007E7E",
          }}
        />
        <Button
          onButtonClick={handleRemoveParty}
          label={t("REMOVE_PARTY")}
          style={{
            border: "none",
            boxShadow: "none",
            marginTop: "10px",
            borderColor: "#007E7E",
            width: "28%",
            backgroundColor: "#fff",
          }}
          textStyles={{
            fontFamily: "Roboto",
            fontSize: "16px",
            fontWeight: 700,
            lineHeight: "18.75px",
            textAlign: "end",
            color: "#007E7E",
          }}
        />
      </div>
    </Modal>
  );
};

export default AddParty;
