import { CloseSvg, FormComposerV2, Modal } from "@egovernments/digit-ui-react-components";
import { isEqual } from "lodash";
import React, { useState } from "react";

const CloseBtn = (props) => {
  return (
    <div onClick={props?.onClick} style={{ height: "100%", display: "flex", alignItems: "center", paddingRight: "20px", cursor: "pointer" }}>
      <CloseSvg />
    </div>
  );
};

const Heading = (props) => {
  return <h1 className="heading-m">{props.label}</h1>;
};

function ConfirmCourtModal({ t, setOpenConfirmCourtModal, onSubmitCase }) {
  const onCancel = () => {
    setOpenConfirmCourtModal(false);
  };
  const [courtFormData, setCourtFormData] = useState({});

  const config = [
    {
      body: [
        {
          type: "component",
          component: "SelectCustomNote",
          key: "personalDetailsNote",
          withoutLabel: true,
          populators: {
            inputs: [
              {
                infoHeader: "CS_COMMON_NOTE",
                infoText: "CS_NOTETEXT_CONFIRM_COURT",
                infoTooltipMessage: "CS_NOTETOOLTIP_RESPONDENT_PERSONAL_DETAILS",
                type: "InfoComponent",
              },
            ],
          },
        },
        {
          type: "dropdown",
          key: "state",
          label: "STATE",
          isMandatory: true,
          populators: {
            label: "SELECT_RESPONDENT_TYPE",
            type: "radioButton",
            optionsKey: "name",
            error: "CORE_REQUIRED_FIELD_ERROR",
            required: false,
            isMandatory: true,
            clearFields: { stateOfRegistration: "", barRegistrationNumber: "", barCouncilId: [], stateRegnNumber: "" },
            options: [
              {
                code: "Kerala",
                name: "Kerala",
                isEnabled: true,
              },
            ],
          },
        },
        {
          type: "dropdown",
          key: "district",
          label: "DISTRICT",
          isMandatory: true,
          populators: {
            label: "SELECT_RESPONDENT_TYPE",
            type: "radioButton",
            optionsKey: "name",
            error: "CORE_REQUIRED_FIELD_ERROR",
            required: false,
            isMandatory: true,
            clearFields: { stateOfRegistration: "", barRegistrationNumber: "", barCouncilId: [], stateRegnNumber: "" },
            options: [
              {
                code: "Kollam",
                name: "Kollam",
                isEnabled: true,
              },
            ],
          },
        },
        {
          type: "dropdown",
          key: "court",
          label: "CS_COURT",
          isMandatory: true,
          populators: {
            label: "SELECT_RESPONDENT_TYPE",
            type: "radioButton",
            optionsKey: "name",
            error: "CORE_REQUIRED_FIELD_ERROR",
            required: false,
            isMandatory: true,
            clearFields: { stateOfRegistration: "", barRegistrationNumber: "", barCouncilId: [], stateRegnNumber: "" },
            mdmsConfig: {
              moduleName: "common-masters",
              masterName: "Court_Rooms",
              localePrefix: "COMMON_MASTERS_COURT_R00M",
            },
          },
        },
      ],
    },
  ];

  const onFormValueChange = (setValue, formData, formState) => {
    if (formData.court && !isEqual(courtFormData, formData)) {
      setCourtFormData(formData);
      setValue("state", {
        code: "Kerala",
        name: "Kerala",
        isEnabled: true,
      });
      setValue("district", {
        code: "Kollam",
        name: "Kollam",
        isEnabled: true,
      });
    }
  };

  return (
    <Modal
      headerBarEnd={<CloseBtn onClick={onCancel} />}
      actionSaveLabel={t("CS_COMMON_CONFIRM")}
      formId="modal-action"
      headerBarMain={<Heading label={t("CS_CONFIRM_COURT")} />}
      hideSubmit
    >
      <FormComposerV2
        label={t("CS_COMMON_CONFIRM")}
        config={config}
        onSubmit={onSubmitCase}
        defaultValues={{
          state: {
            code: "Kerala",
            name: "Kerala",
            isEnabled: true,
          },
          district: {
            code: "Kollam",
            name: "Kollam",
            isEnabled: true,
          },
        }}
        cardStyle={{ minWidth: "100%" }}
        onFormValueChange={onFormValueChange}
        submitInForm={true}
      ></FormComposerV2>
    </Modal>
  );
}

export default ConfirmCourtModal;
