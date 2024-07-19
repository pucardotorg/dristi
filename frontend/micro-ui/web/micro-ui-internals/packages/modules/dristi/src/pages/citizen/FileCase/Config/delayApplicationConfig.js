const delayApplicationFormConfig = [
  {
    body: [
      {
        type: "radio",
        key: "delayCondonationType",
        label: "CS_QUESTION_DELAY_APPLICATION",
        isMandatory: true,
        populators: {
          label: "CS_QUESTION_DELAY_APPLICATION",
          type: "radioButton",
          optionsKey: "name",
          error: "CORE_REQUIRED_FIELD_ERROR",
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
    dependentKey: { delayCondonationType: ["showForm"] },
    body: [
      {
        type: "component",
        component: "SelectCustomTextArea",
        key: "delayApplicationReason",
        withoutLabel: true,
        populators: {
          inputs: [
            {
              name: "reasonForDelay",
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
    dependentKey: { delayCondonationType: ["showForm"] },
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
              infoText: "CS_NOTE_DELAY_APPLICATION",
              infoTooltipMessage: "Tooltip",
              type: "InfoComponent",
            },
          ],
        },
      },
    ],
  },
  {
    dependentKey: { delayCondonationType: ["showForm"] },
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
              documentHeader: "CS_DELAY_CONDONATION_APPLICATION",
              infoTooltipMessage: "Tooltip",
              type: "DragDropComponent",
              uploadGuidelines: "UPLOAD_DOC_50",
              maxFileSize: 50,
              maxFileErrorMessage: "CS_FILE_LIMIT_1_MB",
              fileTypes: ["JPG", "PDF"],
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
  selectDocumentName: {
    condonationFileUpload: "CS_DELAY_CONDONATION_APPLICATION",
  },
};

export const demandNoticeDetailsFormDataToSchemaMapping = {
  delayCondonationType: {
    key: "isDelayed",
    type: "boolean",
    formDataType: "dropdown",
    dropdownKey: "code",
  },
  delayApplicationReason: { key: "reason", type: "string", formDataType: "text", textKey: "reasonForDelay" },
  condonationFileUpload: { key: "document", type: "string", formDataType: "document" },
};

export const demandNoticeDetailsSchemaToFormDataMapping = {
  isDelayed: {
    key: "delayCondonationType",
    type: "boolean",
    formDataType: "dropdown",
    dropdownKey: "code",
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
  reason: { key: "delayApplicationReason", type: "string", formDataType: "text", textKey: "reasonForDelay" },
  document: { key: "condonationFileUpload", type: "string", formDataType: "document" },
};
