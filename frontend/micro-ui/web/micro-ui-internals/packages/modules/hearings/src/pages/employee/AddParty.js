import React, { useEffect, useState, useCallback } from "react";
import { FormComposerV2, Modal, Button } from "@egovernments/digit-ui-react-components";
import { useTranslation } from "react-i18next";
import addPartyConfig from "../../configs/AddNewPartyConfig.js";
import { hearingService } from "../../hooks/services/index.js";
import _ from "lodash";

const AddParty = ({ onCancel, onDismiss, hearing, tenantId, hearingId }) => {
  const { t } = useTranslation();
  const [formConfigs, setFormConfigs] = useState([addPartyConfig(1)]);
  const [aFormData, setFormData] = useState([{}]);
  const [partyHearing, setPartyHearing] = useState(() => _.cloneDeep(hearing));

  const Close = () => (
    <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="#FFFFFF">
      <path d="M0 0h24v24H0V0z" fill="none" />
      <path d="M19 6.41L17.59 5 12 10.59 6.41 5 5 6.41 10.59 12 5 17.59 6.41 19 12 13.41 17.59 19 19 17.59 13.41 12 19 6.41z" />
    </svg>
  );

  const CloseBtn = (props) => {
    return (
      <div onClick={props?.onClick} style={props?.isMobileView ? { padding: 5 } : null}>
        <div className={"icon-bg-secondary"} style={{ backgroundColor: "#505A5F" }}>
          {" "}
          <Close />{" "}
        </div>
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

  const handleSubmit = () => {
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
        newData.deposition = "";
        newData.isSigned = false;

        const errors = validateFormData(newData);
        if (Object.keys(errors).length > 0) {
          console.log("Validation errors:", errors);
          return null;
        }
        return newData;
      })
      .filter(Boolean);

    if (cleanedData.length === aFormData.length) {
      console.log(cleanedData, "dfddfd");
      onAdd(cleanedData);
      onDismiss();
    }
  };

  const onAdd = (cleanedData) => {
    const updatedHearing = { ...partyHearing };
    const updatedAdditionalDetails = { ...partyHearing.additionalDetails };

    if (updatedAdditionalDetails.witnesses) {
      updatedAdditionalDetails.witnesses = [...updatedAdditionalDetails.witnesses, ...cleanedData];
    } else {
      updatedAdditionalDetails.witnesses = cleanedData;
    }

    updatedHearing.additionalDetails = updatedAdditionalDetails;
    hearingService.updateHearing(
      { tenantId, hearing: updatedHearing, status: hearing.status, hearingType: hearing.hearingType },
      { applicationNumber: "", cnrNumber: "", hearingId }
    );
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
      headerBarMain={<h1 className="heading-m">Add New Party</h1>}
      headerBarEnd={<CloseBtn onClick={onDismiss} />}
      actionCancelLabel="Back"
      actionCancelOnSubmit={onCancel}
      actionSaveLabel="Add"
      actionSaveOnSubmit={handleSubmit}
    >
      {formConfigs.map((config, index) => (
        <FormComposerV2
          key={index}
          config={[config]}
          onFormValueChange={(setValue, formData, formState, reset, setError, clearErrors, trigger, getValues) => {
            onFormValueChange(formData, index);
          }}
        />
      ))}
      <div style={{ display: "flex", justifyContent: "space-between", marginBottom: "3rem" }}>
        <Button onButtonClick={handleAddParty} label="Add Party" />
        <Button onButtonClick={handleRemoveParty} label="Remove Party" />
      </div>
    </Modal>
  );
};

export default AddParty;
