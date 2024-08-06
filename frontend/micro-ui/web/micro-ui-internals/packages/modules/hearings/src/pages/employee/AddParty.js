import { Button, CloseSvg, FormComposerV2, Modal } from "@egovernments/digit-ui-react-components";
import React, { useCallback, useState } from "react";
import addPartyConfig from "../../configs/AddNewPartyConfig.js";
import { useTranslation } from "react-i18next";
import SelectCustomNote from "@egovernments/digit-ui-module-dristi/src/components/SelectCustomNote.js";
import { Urls } from "../../hooks/services/Urls.js";

const AddParty = ({ onCancel, onAddSuccess, caseData, tenantId, hearing, refetchHearing }) => {
  const { t } = useTranslation();
  const DRISTIService = Digit?.ComponentRegistryService?.getComponent("DRISTIService");
  const [formConfigs, setFormConfigs] = useState([addPartyConfig(1)]);
  const [aFormData, setFormData] = useState([{}]);

  const { mutateAsync: updateAttendees } = Digit.Hooks.useCustomAPIMutationHook({
    url: Urls.hearing.hearingUpdate,
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

  const validateFormData = (data) => {
    const errors = {};
    if (!data.partyName) errors.partyName = "Party name is required";
    if (!data.partyType) errors.partyType = "Party type is required";
    if (!data.phoneNumber || !/^\d+$/.test(data.phoneNumber)) errors.phoneNumber = "Phone number is invalid";
    if (!data.emailId || !/\S+@\S+\.\S+/.test(data.emailId)) errors.emailId = "Email is invalid";
    if (!data.address) errors.address = "Address is required";
    return errors;
  };

  const handleSubmit = (e) => {
    e?.stopPropagation();
    e?.preventDefault();
    const cleanedData = aFormData
      .map(({ data }) => {
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
        const errors = validateFormData(newData);
        if (Object.keys(errors).length > 0) {
          console.log("Validation errors:", errors);
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
      headerBarMain={<h1 className="heading-m">{t("ADD_NEW_PARTY")}</h1>}
      headerBarEnd={<CloseBtn onClick={onCancel} />}
      actionCancelLabel={t("HEARING_BACK")}
      actionCancelOnSubmit={onCancel}
      actionSaveLabel={t("HEARING_ADD")}
      actionSaveOnSubmit={handleSubmit}
    >
      <div style={{ padding: "16px 24px" }}>
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
      {formConfigs.map((config, index) => (
        <FormComposerV2
          key={index}
          config={[config]}
          onFormValueChange={(setValue, formData, formState, reset, setError, clearErrors, trigger, getValues) => {
            onFormValueChange(formData, index);
          }}
          fieldStyle={{ width: "100%" }}
        />
      ))}
      <div style={{ display: "flex", justifyContent: "space-between", marginBottom: "3rem" }}>
        <Button onButtonClick={handleAddParty} label={t("CASE_ADD_PARTY")} />
      </div>
    </Modal>
  );
};

export default AddParty;
