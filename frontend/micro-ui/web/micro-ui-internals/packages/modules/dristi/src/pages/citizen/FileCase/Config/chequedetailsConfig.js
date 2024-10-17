export const chequeDetailsFormConfig = [
  {
    body: [
      {
        type: "text",
        label: "CS_DISHONOURED_CHEQUE_SIGNATORY_NAME",
        isMandatory: true,
        populators: {
          validation: {
            pattern: {
              message: "CORE_COMMON_APPLICANT_NAME_INVALID",
              value: /^[^{0-9}^\$\"<>?\\\\~!@#$%^()+={}\[\]*,/_:;“”‘’]{1,100}$/i,
            },
            patternType: "Name",
            minLength: 1,
          },
          error: "FIRST_LAST_NAME_MANDATORY_MESSAGE",
          name: "chequeSignatoryName",
        },
      },
    ],
  },
  {
    body: [
      {
        type: "component",
        component: "SelectCustomDragDrop",
        key: "bouncedChequeFileUpload",
        populators: {
          inputs: [
            {
              name: "document",
              isMandatory: true,
              documentHeader: "CS_BOUNCED_CHEQUE",
              type: "DragDropComponent",
              uploadGuidelines: "UPLOAD_DOC_50",
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
        type: "text",
        label: "CS_NAME_ON_CHEQUE",
        isMandatory: true,
        populators: {
          validation: {
            pattern: {
              message: "CORE_COMMON_APPLICANT_NAME_INVALID",
              value: /^[^{0-9}^\$\"<>?\\\\~!@#$%^()+={}\[\]*,/_:;“”‘’]{1,50}$/i,
            },
            patternType: "Name",
            minLength: 1,
          },
          error: "FIRST_LAST_NAME_MANDATORY_MESSAGE",
          name: "name",
        },
      },
    ],
  },
  {
    body: [
      {
        type: "text",
        label: "CS_CHEQUE_NUMBER",
        isMandatory: true,
        populators: {
          error: "CORE_REQUIRED_FIELD_ERROR",
          validation: {
            minLength: 6,
          },
          name: "chequeNumber",
        },
      },
    ],
  },
  {
    body: [
      {
        type: "date",
        label: "CS_DATE_OF_ISSUANCE",
        isMandatory: true,
        populators: {
          name: "issuanceDate",
          error: "CORE_REQUIRED_FIELD_ERROR",
          validation: {
            max: {
              patternType: "date",
              masterName: "commonUiConfig",
              moduleName: "maxDateValidation",
            },
          },
        },
      },
    ],
  },
  {
    body: [
      {
        type: "text",
        label: "CS_BANK_NAME",
        isMandatory: true,
        populators: {
          error: "FIRST_LAST_NAME_MANDATORY_MESSAGE",
          name: "bankName",
        },
      },
    ],
  },
  {
    body: [
      {
        type: "text",
        label: "CS_IFSC_CODE",
        isMandatory: true,
        populators: {
          error: "CORE_REQUIRED_FIELD_ERROR",
          validation: {
            pattern: {
              patternType: "ifsc",
              masterName: "commonUiConfig",
              moduleName: "patternValidation",
            },
            minLength: 11,
          },
          name: "ifsc",
        },
      },
    ],
  },
  {
    body: [
      {
        type: "amount",
        label: "CS_CHEQUE_AMOUNT",
        isMandatory: true,
        populators: {
          error: "CORE_REQUIRED_FIELD_ERROR",
          componentInFront: "₹",
          name: "chequeAmount",
          prefix: "",
        },
      },
    ],
  },
  {
    body: [
      {
        type: "date",
        label: "CS_DATE_OF_CHEQUE_DEPOSIT",
        isMandatory: true,
        populators: {
          name: "depositDate",
          error: "CORE_REQUIRED_FIELD_ERROR",
          validation: {
            max: {
              patternType: "date",
              masterName: "commonUiConfig",
              moduleName: "maxDateValidation",
            },
          },
        },
      },
    ],
  },
  {
    body: [
      {
        type: "component",
        component: "SelectCustomDragDrop",
        key: "depositChequeFileUpload",
        withoutLabel: true,
        populators: {
          inputs: [
            {
              name: "document",
              documentHeader: "CS_PROOF_DEPOSIT_CHEQUE",
              infoTooltipMessage: "Tooltip",
              type: "DragDropComponent",
              uploadGuidelines: "UPLOAD_DOC_50",
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
        key: "returnMemoFileUpload",
        withoutLabel: true,
        populators: {
          inputs: [
            {
              name: "document",
              documentHeader: "CS_CHEQUE_RETURN_MEMO",
              infoTooltipMessage: "Tooltip",
              type: "DragDropComponent",
              uploadGuidelines: "UPLOAD_DOC_50",
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
        component: "SelectCustomTextArea",
        key: "chequeAdditionalDetails",
        populators: {
          inputs: [
            {
              textAreaSubHeader: "CS_CHEQUE_ADDITIONAL_DETAILS",
              isOptional: true,
              type: "TextAreaComponent",
            },
          ],
        },
      },
    ],
  },
];

export const chequeDetailsConfig = {
  formconfig: chequeDetailsFormConfig,
  header: "CS_CHEQUE_DETAILS_HEADING",
  subtext: "CS_CHEQUE_DETAILS_SUBTEXT",
  isOptional: false,
  addFormText: "ADD_CHEQUE",
  formItemName: "CS_CHEQUE",
  className: "cheque",
  selectDocumentName: {
    bouncedChequeFileUpload: "CS_BOUNCED_CHEQUE",
    depositChequeFileUpload: "CS_PROOF_DEPOSIT_CHEQUE",
    returnMemoFileUpload: "CS_CHEQUE_RETURN_MEMO",
  },
};
