const debtLiabilityFromconfig = [
  {
    body: [
      {
        type: "dropdown",
        key: "liabilityNature",
        label: "Nature of debt / liability for which cheque(s) was/were received (e.g. loan, sales agreement etc)",
        isMandatory: true,
        populators: {
          label: "SELECT_RESPONDENT_TYPE",
          name: "liabilityNature",
          type: "radioButton",
          optionsKey: "name",
          error: "sample required message",
          required: false,
          isMandatory: true,
          clearFields: { stateOfRegistration: "", barRegistrationNumber: "", barCouncilId: [], stateRegnNumber: "" },
          options: [
            {
              code: "LOAN",
              name: "Loan",
              showAmountCovered: false,
              isEnabled: true,
            },
            {
              code: "SALES",
              name: "Sales Agreement",
              showAmountCovered: true,
              isVerified: true,
              hasBarRegistrationNo: true,
              isEnabled: true,
              apiDetails: {
                serviceName: "/advocate/advocate/v1/_create",
                requestKey: "advocates",
                AdditionalFields: ["barRegistrationNumber"],
              },
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
        key: "liabilityType",
        label: "What was the total amount the cheque(s) covered?",
        isMandatory: true,
        populators: {
          label: "SELECT_RESPONDENT_TYPE",
          name: "liabilityType",
          type: "radioButton",
          optionsKey: "name",
          error: "sample required message",
          required: false,
          isMandatory: true,
          isDependent: true,
          clearFields: { stateOfRegistration: "", barRegistrationNumber: "", barCouncilId: [], stateRegnNumber: "" },
          options: [
            {
              code: "FULL_LIABILITY",
              name: "Full Liability",
              showAmountCovered: false,
              isEnabled: true,
            },
            {
              code: "PARTIAL_LIABILITY",
              name: "Partial Liability",
              showAmountCovered: true,
              isVerified: true,
              hasBarRegistrationNo: true,
              isEnabled: true,
              apiDetails: {
                serviceName: "/advocate/advocate/v1/_create",
                requestKey: "advocates",
                AdditionalFields: ["barRegistrationNumber"],
              },
            },
          ],
        },
      },
    ],
  },
  {
    dependentKey: { liabilityType: ["showAmountCovered"] },
    body: [
      {
        type: "text",
        key: "totalAmount",
        label: "What was the total amount the cheque(s) covered? ",
        populators: {
          validation: {},
          title: "FIRST_TERMS_AND_CONDITIONS",
          name: "Terms_Conditions",
        },
      },
    ],
  },
  {
    body: [
      {
        type: "component",
        component: "SelectCustomNote",
        key: "addressDetailsNote",
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
    body: [
      {
        type: "component",
        component: "SelectCustomDragDrop",
        key: "condonationFileUpload",
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
        key: "delayApplicationReason",
        populators: {
          inputs: [
            {
              textAreaHeader: "CS_TEXTAREA_HEADER_DELAY_REASON",
              type: "TextAreaComponent",
            },
          ],
        },
      },
    ],
  },
];
export const debtliabilityconfig = {
  formconfig: debtLiabilityFromconfig,
  header: "CS_DEBT_LIABILITY_HEADING",
  subtext: "CS_DEBT_LIABILITY_SUBTEXT",
  isOptional: false,
  className: "debt-liability",
};
