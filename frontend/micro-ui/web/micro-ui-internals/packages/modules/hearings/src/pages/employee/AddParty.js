import React, { useState, useCallback } from "react";
import { FormComposerV2, Modal, Button } from "@egovernments/digit-ui-react-components";
import { useTranslation } from "react-i18next";
import addPartyConfig from "../../configs/AddNewPartyConfig.js";

const AddParty = ({ onClose }) => {
  const { t } = useTranslation();
  const [formConfigs, setFormConfigs] = useState([addPartyConfig(1)]);
  const [aFormData, setFormData] = useState([{}]);

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

  const handleSubmit = () => {
    console.log("Form data:", aFormData);
  };

  const onFormValueChange = useCallback((formData, index) => {
    if (JSON.stringify(formData) !== JSON.stringify(aFormData[index].data)) {
      setFormData(prevData =>
        prevData.map((item, i) =>
          i === index ? { ...item, data: formData } : item
        )
      );
    }
  }, [aFormData]);

  return (
    <Modal
      headerBarMain={<h1>{t("Add New Parties")}</h1>}
      headerBarEnd={<Button onClick={onClose} label="Close" />}
      actionCancelLabel="Back"
      actionCancelOnSubmit={onClose}
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
      <div style={{ display: 'flex', justifyContent: 'space-between', marginBottom: "3rem" }}>
        <Button onButtonClick={handleAddParty} label="Add Party" />
        <Button onButtonClick={handleRemoveParty} label="Remove Party" />
      </div>
    </Modal>
  );
};

export default AddParty;
