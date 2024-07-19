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
            minLength: 2,
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
              fileTypes: ["JPG", "PDF"],
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
            minLength: 2,
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
          validation: {
            max: new Date().toISOString().split("T")[0],
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
          validation: {
            pattern: {
              message: "CORE_COMMON_IFSC_INVALID",
              value: /^[A-Z]{4}0[A-Z0-9]{6}$/,
            },
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
          validation: {
            max: new Date().toISOString().split("T")[0],
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
              fileTypes: ["JPG", "PDF"],
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
              fileTypes: ["JPG", "PDF"],
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

export const chequeDetailsFormDataToSchemaMapping = {
  chequeSignatoryName: { key: "chequeSignatoryName", type: "string", formDataType: "string" },
  name: { key: "payeeName", type: "string", formDataType: "string" },
  chequeNumber: { key: "number", type: "string", formDataType: "string" },
  issuanceDate: { key: "issueDate", type: "string", formDataType: "string" },
  bankName: { key: "payerBank", type: "string", formDataType: "string" },
  ifsc: { key: "payerBankIFSC", type: "string", formDataType: "string" },
  chequeAmount: { key: "amount", type: "string", formDataType: "string" },
  depositDate: { key: "depositDate", type: "string", formDataType: "string" },
  bouncedChequeFileUpload: { key: "cheque", type: "string", formDataType: "document" },
  depositChequeFileUpload: { key: "deposit", type: "string", formDataType: "document" },
  returnMemoFileUpload: { key: "returnMemo", type: "string", formDataType: "document" },
};

export const chequeDetailsSchemaToFormDataMapping = {
  chequeSignatoryName: { key: "chequeSignatoryName", type: "string", formDataType: "string" },
  payeeName: { key: "name", type: "string", formDataType: "string" },
  number: { key: "chequeNumber", type: "string", formDataType: "string" },
  issueDate: { key: "issuanceDate", type: "string", formDataType: "string" },
  payerBank: { key: "bankName", type: "string", formDataType: "string" },
  payerBankIFSC: { key: "ifsc", type: "string", formDataType: "string" },
  amount: { key: "chequeAmount", type: "string", formDataType: "string" },
  depositDate: { key: "depositDate", type: "string", formDataType: "string" },
  cheque: { key: "bouncedChequeFileUpload", type: "string", formDataType: "document" },
  deposit: { key: "depositChequeFileUpload", type: "string", formDataType: "document" },
  returnMemo: { key: "returnMemoFileUpload", type: "string", formDataType: "document" },
};
