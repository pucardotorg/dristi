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
              infoTooltipMessage: "Tooltip",
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
        isMandatory: true,
        populators: {
          label: "SELECT_PRAYER_AND_SWORN_STATEMENT_TYPE",
          type: "radioButton",
          optionsKey: "name",
          error: "CORE_REQUIRED_FIELD_ERROR",
          required: false,
          isMandatory: true,
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
        key: "caseSettlementCondition",
        withoutLabel: true,
        populators: {
          inputs: [
            {
              name: "text",
              textAreaSubHeader: "CS_CASE_SETTLEMENT_CONDITION_SUBHEADER",
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
        component: "SelectUploadFiles",
        key: "memorandumOfComplaint",
        isMandatory: true,
        populators: {
          inputs: [
            {
              name: "text",
              textAreaHeader: "CS_MEMORANDUM_OF_COMPLAINT_HEADER",
              textAreaSubHeader: "CS_MEMORANDUM_OF_COMPLAINT_SUBHEADER",
              type: "TextAreaComponent",
              isOptional: false,
              headerClassName: "dristi-font-bold",
            },
            {
              name: "document",
              documentHeader: "CS_MEMORANDUM_OF_COMPLAINT_HEADER",
              // isOptional: "optional",
              // infoTooltipMessage: "Tooltip",
              type: "DragDropComponent",
              label: "CS_MEMORANDUM_OF_COMPLAINT_HEADER",
              uploadGuidelines: "CS_DOCUMENT_BLURB",
              allowedFileTypes: /(.*?)(doc|docx|pdf|txt)$/i,
              maxFileSize: 25,
              maxFileErrorMessage: "CS_FILE_LIMIT_25_MB",
              allowedMaxSizeInMB: 25,
              noteMsg: "CS_DOCUMENT_BLURB",
              notSupportedError: "NOT_SUPPORTED_ERROR",
              fileTypes: ["PDF"],
              isMultipleUpload: true,
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
        component: "SelectUploadFiles",
        key: "prayerForRelief",
        isMandatory: true,
        populators: {
          inputs: [
            {
              name: "text",
              textAreaHeader: "CS_PRAYER_FOR_RELIEF_HEADER",
              textAreaSubHeader: "CS_MEMORANDUM_OF_COMPLAINT_SUBHEADER",
              type: "TextAreaComponent",
              isOptional: false,
              headerClassName: "dristi-font-bold",
            },
            {
              name: "document",
              documentHeader: "CS_PRAYER_FOR_RELIEF_HEADER",
              // isOptional: "optional",
              // infoTooltipMessage: "Tooltip",
              type: "DragDropComponent",
              label: "CS_PRAYER_FOR_RELIEF_HEADER",
              uploadGuidelines: "CS_DOCUMENT_BLURB",
              allowedFileTypes: /(.*?)(doc|docx|pdf|txt)$/i,
              maxFileSize: 25,
              allowedMaxSizeInMB: 25,
              maxFileErrorMessage: "CS_FILE_LIMIT_25_MB",
              noteMsg: "CS_DOCUMENT_BLURB",
              notSupportedError: "NOT_SUPPORTED_ERROR",
              fileTypes: ["PDF"],
              isMultipleUpload: true,
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
        key: "SelectCustomDragDrop",
        isMandatory: true,
        populators: {
          inputs: [
            {
              isMandatory: true,
              name: "swornStatement",
              documentSubText: "CS_SWORN_STATEMENT_SUBTEXT",
              documentHeader: "CS_SWORN_STATEMENT_HEADER",
              type: "DragDropComponent",
              maxFileSize: 50,
              maxFileErrorMessage: "CS_FILE_LIMIT_50_MB",
              fileTypes: ["PDF"],
              isMultipleUpload: false,
              uploadGuidelines: "UPLOAD_PDF_50",
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
        populators: {
          inputs: [
            {
              name: "text",
              textAreaSubHeader: "CS_ADDITIONAL_DETAILS",
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
        component: "SelectCustomTextArea",
        key: "additionalActsSections",
        populators: {
          inputs: [
            {
              name: "text",
              textAreaSubHeader: "CS_SWORN_PAGE_ADDITIONAL_ACTS_SECTIONS_HEADER",
              type: "TextAreaComponent",
              isOptional: true,
              headerClassName: "dristi-font-bold",
              subHeaderClassName: "subheader-bold-class",
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
                pattern: /^[^{0-9}^\$\"<>?\\\\~!@#$%^()+={}\[\]*,/_:;“”‘’]{1,50}$/i,
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
              maxFileSize: 5,
              maxFileErrorMessage: "CS_FILE_LIMIT_50_MB",
              fileTypes: ["PDF"],
              uploadGuidelines: "UPLOAD_PDF_50",
              isMultipleUpload: false,
              uploadGuidelines: "UPLOAD_MAX_PDF_DOCUMENT_SIZE",
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
