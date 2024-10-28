const prayerAndSwornFormConfig = [
  {
    body: [
      {
        type: "component",
        component: "SelectCustomNote",
        key: "prayerAndSwornNote",
        populators: {
          inputs: [
            {
              infoHeader: "CS_COMMON_NOTE",
              infoText: "CS_NOTETEXT_PRAYER_AND_SWORN",
              infoTooltipMessage: "CS_NOTETEXT_PRAYER_AND_SWORN",
              type: "InfoComponent",
            },
          ],
        },
      },
    ],
  },
  {
    body: [
      {
        type: "radio",
        key: "prayerAndSwornStatementType",
        label: "CS_PRAYER_AND_SWORN_STATEMENT_TYPE",
        isMandatory: false,
        populators: {
          name: "prayerAndSwornStatementType",
          label: "SELECT_PRAYER_AND_SWORN_STATEMENT_TYPE",
          type: "radioButton",
          optionsKey: "name",
          error: "CORE_REQUIRED_FIELD_ERROR",
          required: false,
          isMandatory: false,
          isDependent: true,
          options: [
            {
              code: "YES",
              name: "YES",
            },
            {
              code: "NO",
              name: "NO",
            },
            {
              code: "MAYBE",
              name: "Maybe",
            },
          ],
        },
      },
    ],
  },
  {
    body: [
      {
        type: "component",
        component: "SelectCustomTextArea",
        key: "memorandumOfComplaint",
        isMandatory: true,
        populators: {
          inputs: [
            {
              name: "text",
              textAreaHeader: "CS_MEMORANDUM_OF_COMPLAINT_HEADER",
              type: "TextAreaComponent",
            },
          ],
        },
      },
    ],
  },
  {
    body: [
      {
        type: "component",
        component: "SelectCustomDragDrop",
        label: "CS_SWORN_STATEMENT_HEADER",
        withoutLabel: true,
        key: "swornStatement",
        isMandatory: true,
        populators: {
          inputs: [
            {
              isOptional: false,
              name: "document",
              documentSubText: "CS_SWORN_STATEMENT_SUBTEXT",
              documentHeader: "CS_SWORN_STATEMENT_HEADER",
              type: "DragDropComponent",
              maxFileSize: 50,
              maxFileErrorMessage: "CS_FILE_LIMIT_50_MB",
              fileTypes: ["JPG", "PDF"],
              isMultipleUpload: true,
              uploadGuidelines: "UPLOAD_DOC_50",
            },
          ],
        },
      },
    ],
  },
  {
    body: [
      {
        type: "component",
        component: "SelectCustomTextArea",
        key: "additionalDetails",
        label: "CS_ADDITIONAL_DETAILS",
        withoutLabel: true,
        populators: {
          inputs: [
            {
              name: "text",
              textAreaSubHeader: "CS_ADDITIONAL_DETAILS",
              type: "TextAreaComponent",
              isOptional: true,
            },
          ],
        },
      },
    ],
  },
  {
    body: [
      {
        type: "component",
        component: "SelectUploadDocWithName",
        key: "SelectUploadDocWithName",
        withoutLabel: true,
        populators: {
          inputs: [
            {
              label: "DOCUMENT_LABEL_NAME",
              type: "text",
              name: "docName",
              validation: {
                pattern: {
                  patternType: "docName",
                  masterName: "commonUiConfig",
                  moduleName: "patternValidation",
                },
                errMsg: "CORE_COMMON_DOCUMENT_NAME_INVALID",
                title: "",
                patternType: "Name",
                isRequired: true,
              },
              isMandatory: true,
            },
            {
              isMandatory: true,
              name: "document",
              documentHeader: "CS_SWORN_ADDITIONAL_ACTS_DOCUMENT_HEADER",
              type: "DragDropComponent",
              maxFileSize: 50,
              maxFileErrorMessage: "CS_FILE_LIMIT_50_MB",
              fileTypes: ["JPG", "PDF"],
              uploadGuidelines: "UPLOAD_DOC_50",
              isMultipleUpload: false,
            },
          ],
          validation: {},
        },
      },
    ],
  },
];

export const prayerAndSwornConfig = {
  formconfig: prayerAndSwornFormConfig,
  header: "CS_PRAYER_AND_SWORN_STATEMENT_HEADING",
  // addFormText: "ADD_DOCUMENT",
  className: "prayer-and-sworm",
  selectDocumentName: {
    memorandumOfComplaint: "CS_MEMORANDUM_OF_COMPLAINT_HEADER",
    prayerForRelief: "CS_PRAYER_FOR_RELIEF_HEADER",
    swornStatement: "CS_SWORN_STATEMENT_HEADER",
  },
};
