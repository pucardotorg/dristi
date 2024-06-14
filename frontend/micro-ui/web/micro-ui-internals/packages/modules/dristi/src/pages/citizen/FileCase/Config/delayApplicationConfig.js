const delayApplicationFormConfig = [
  {
    body: [
      {
        type: "radio",
        key: "delayApplicationType",
        label: "CS_DELAY_APPLICATION_TYPE",
        isMandatory: true,
        populators: {
          label: "CS_DELAY_APPLICATION_TYPE",
          type: "radioButton",
          optionsKey: "name",
          error: "sample required message",
          required: false,
          isMandatory: true,
          isDependent: true,
          clearFields: { stateOfRegistration: "", barRegistrationNumber: "", barCouncilId: [], stateRegnNumber: "" },
          options: [
            {
              code: "YES",
              name: "YES",
              showForm: false,
              isEnabled: true,
            },
            {
              code: "NO",
              name: "NO",
              showForm: true,
              isVerified: true,
              hasBarRegistrationNo: true,
              isEnabled: true,
            },
          ],
        },
      },
    ],
  },
  {
    dependentKey: { delayApplicationType: ["showForm"] },
    body: [
      {
        type: "component",
        component: "SelectCustomTextArea",
        key: "delayApplicationReason",
        withoutLabel: true,
        populators: {
          inputs: [
            {
              textAreaHeader: "CS_TEXTAREA_HEADER_DELAY_REASON",
              type: "TextAreaComponent",
              headerClassName: "text-area-header",
            },
          ],
        },
      },
    ],
  },
  {
    dependentKey: { delayApplicationType: ["showForm"] },
    body: [
      {
        type: "component",
        component: "SelectCustomNote",
        key: "addressDetailsNote",
        withoutLabel: true,
        populators: {
          inputs: [
            {
              infoHeader: "CS_COMMON_NOTE",
              infoText: "CS_NOTETEXT_RESPONDENT_ADDRESS",
              infoTooltipMessage: "CS_NOTETOOLTIP_RESPONDENT_ADDRESS",
              type: "InfoComponent",
            },
          ],
        },
      },
    ],
  },
  {
    dependentKey: { delayApplicationType: ["showForm"] },
    body: [
      {
        type: "component",
        component: "SelectCustomDragDrop",
        key: "condonationFileUpload",
        withoutLabel: true,
        populators: {
          inputs: [
            {
              name: "document",
              documentHeader: "Aadhar",
              isOptional: "optional",
              infoTooltipMessage: "Tooltip",
              type: "DragDropComponent",
              uploadGuidelines: "Upload .png",
              maxFileSize: 50,
              maxFileErrorMessage: "CS_FILE_LIMIT_1_MB",
              fileTypes: ["JPG", "PNG", "PDF"],
            },
          ],
        },
      },
    ],
  },
];

export const delayApplicationConfig = {
  formconfig: delayApplicationFormConfig,
  header: "CS_RESPONDENT_DELAY_APPLICATION_HEADING",
  subtext: "CS_RESPONDENT_DELAY_APPLICATION_SUBTEXT",
  className: "delay-application",
};
