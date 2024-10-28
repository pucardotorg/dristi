import { Section } from "./formComposerTypes";

/**
 * @type {Section[]}
 */
export const chequeDetailsConfig = [
  {
    body: [
      {
        type: "text",
        label: "CS_DISHONOURED_CHEQUE_SIGNATORY_NAME",
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
              documentHeader: "BOUNCED_CHEQUE",
              isOptional: "CS_IS_OPTIONAL",
              infoTooltipMessage: "BOUNCED_CHEQUE",
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
        key: "bouncedChequeFileUpload",
        populators: {
          inputs: [
            {
              name: "document",
              documentHeader: "CS_PROOF_DEPOSIT_CHEQUE",
              isOptional: "CS_IS_OPTIONAL",
              infoTooltipMessage: "CS_PROOF_DEPOSIT_CHEQUE",
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
        key: "bouncedChequeFileUpload",
        populators: {
          inputs: [
            {
              name: "document",
              documentHeader: "CS_CHEQUE_RETURN_MEMO",
              isOptional: "CS_IS_OPTIONAL",
              infoTooltipMessage: "CS_CHEQUE_RETURN_MEMO",
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
        populators: {
          name: "chequeAdditionalDetails",
        },
      },
    ],
  },
];
