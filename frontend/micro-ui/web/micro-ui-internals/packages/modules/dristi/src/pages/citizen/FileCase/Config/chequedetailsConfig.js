export const chequeDetailsFormConfig = [
  {
    body: [
      {
        type: "text",
        label: "CS_DISHONOURED_CHEQUE_SIGNATORY_NAME",
        isMandatory: true,
        populators: {
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
              documentHeader: "Bounced Cheque",
              isOptional: "optional",
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
        type: "text",
        label: "CS_NAME_ON_CHEQUE",
        isMandatory: true,
        populators: {
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
          name: "ifsc",
        },
      },
    ],
  },
  {
    body: [
      {
        type: "amount",
        component: "CustomInput",
        label: "CS_CHEQUE_AMOUNT",
        isMandatory: true,
        populators: {
          componentInFront: "₹",
          name: "chequeAmount",
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
              documentHeader: "Proof of deposit of Cheque",
              isOptional: "optional",
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
              documentHeader: "Cheque Return Memo",
              isOptional: "optional",
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
        type: "textarea",
        label: "CS_CHEQUE_ADDITIONAL_DETAILS",
        isMandatory: true,
        populators: {
          name: "chequeAdditionalDetails",
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
  formItemName: "Cheque",
  className: "cheque",
};
