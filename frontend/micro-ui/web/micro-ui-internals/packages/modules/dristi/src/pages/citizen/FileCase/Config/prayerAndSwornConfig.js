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
          error: "sample required message",
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
              code: "Maybe",
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
              uploadGuidelines: "Upload .png",
              maxFileSize: 5,
              maxFileErrorMessage: "CS_FILE_LIMIT_50_MB",
              fileTypes: ["JPG", "PNG", "PDF"],
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
        populators: {
          inputs: [
            {
              name: "text",
              textAreaHeader: "CS_PRAYER_FOR_RELIEF_HEADER",
              textAreaSubHeader: "CS_PRAYER_FOR_RELIEF_SUBHEADER",
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
              uploadGuidelines: "Upload .png",
              maxFileSize: 50,
              maxFileErrorMessage: "CS_FILE_LIMIT_50_MB",
              fileTypes: ["JPG", "PNG", "PDF"],
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
        populators: {
          inputs: [
            {
              // isMandatory: true,
              isOptional: "CS_IS_OPTIONAL",
              name: "swornStatement",
              documentSubText: "CS_SWORN_STATEMENT_SUBTEXT",
              documentHeader: "CS_SWORN_STATEMENT_HEADER",
              type: "DragDropComponent",
              maxFileSize: 50,
              maxFileErrorMessage: "CS_FILE_LIMIT_50_MB",
              fileTypes: ["JPG", "PNG", "PDF"],
              isMultipleUpload: false,
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
              textAreaSubHeader: "CS_SWORN_PAGE_ADDITIONAL_DETAILS_SUBHEADER",
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
        component: "SelectCustomTextArea",
        key: "additionalActsSections",
        populators: {
          inputs: [
            {
              name: "text",
              textAreaHeader: "CS_SWORN_PAGE_ADDITIONAL_ACTS_SECTIONS_HEADER",
              type: "TextAreaComponent",
              isOptional: true,
              headerClassName: "dristi-font-bold",
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
              fileTypes: ["JPG", "PNG", "PDF"],
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
  subtext: "CS_PRAYER_AND_SWORN_STATEMENT_SUBTEXT",
};
