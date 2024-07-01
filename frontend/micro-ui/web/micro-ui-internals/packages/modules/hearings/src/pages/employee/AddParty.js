import React, { useState , useMemo} from "react";
import { FormComposer, Modal, Button, FormComposerV2 } from "@egovernments/digit-ui-react-components";
import { useTranslation } from "react-i18next";
import addNewPartiesConfig from "../../configs/AddNewPartyConfig";

const AddParty = ({ onClose }) => {
  const { t } = useTranslation();
  const [partyFields, setPartyFields] = useState([
    { partyType: "Witness", partyName: "", phoneNumber: "", emailId: "", address: "", additionalDetails: "" },
  ]);

  const handleAddParty = () => {
    setPartyFields([...partyFields, { partyType: "Witness", partyName: "", phoneNumber: "", emailId: "", address: "", additionalDetails: "" }]);
  };

  const handleSubmit = (data) => {
    console.log("Form data:", data);
  };

  const config = addNewPartiesConfig();
  console.log(config,"config");

  const simpleConfig = {
    label: "Simple Form",
    form: [
      {
        head: "Section 1",
        body: [
          {
            label: "Field 1",
            type: "text",
            key: "field1",
            isMandatory: true,
            populators: {
              name: "field1",
            },
          },
        ],
      },
    ],
  };
  console.log(simpleConfig,"simpleconfig")

  return (
    <Modal
      headerBarMain={<h1>{t("Add New Parties")}</h1>}
      headerBarEnd={<Button onClick={onClose} label="Close" />}
      actionCancelLabel="Back"
      actionCancelOnSubmit={onClose}
      actionSaveLabel="Add"
      actionSaveOnSubmit={handleSubmit}
    >
      <FormComposerV2
        config={simpleConfig}
        onSubmit={handleSubmit}
        inlineChildren
      ></FormComposerV2>
    </Modal>
  );
};

export default AddParty;