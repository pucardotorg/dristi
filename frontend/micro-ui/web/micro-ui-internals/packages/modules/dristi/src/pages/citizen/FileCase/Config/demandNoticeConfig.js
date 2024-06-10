const demandNoticeFormConfig = [
  {
    body: [
      {
        type: "component",
        component: "SelectUserTypeComponent",
        key: "SelectUserTypeComponent",
        isMandatory: true,
        withoutLabel: true,
        populators: {
          inputs: [
            {
              label: "CS_MODE_OF_DISPATCH_TYPE",
              type: "dropdown",
              name: "modeOfDispatchType",
              optionsKey: "name",
              error: "sample required message",
              required: false,
              isMandatory: true,
              options: [
                {
                  code: "POST",
                  name: "POST",
                  commonFields: true,
                  isEnabled: true,
                },
              ],
            },
          ],
        },
      },
    ],
  },
  {
    body: [
      {
        type: "date",
        label: "CS_DATE_OF_ISSUANCE_LDN",
        isMandatory: true,
        populators: {
          name: "dateOfIssuance",
        },
      },
    ],
  },
  {
    body: [
      {
        type: "date",
        label: "CS_DATE_OF_DISPATCH_LDN",
        isMandatory: true,
        populators: {
          name: "dateOfDispatch",
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
        withoutLabel: true,
        populators: {
          inputs: [
            {
              name: "legalDemandNoticeFileUpload",
              documentHeader: "LEGAL_DEMAND_NOTICE",
              type: "DragDropComponent",
              uploadGuidelines: "Upload .pdf or .jpg",
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
        key: "SelectCustomDragDrop",
        withoutLabel: true,
        populators: {
          inputs: [
            {
              name: "proofOfDispatchFileUpload",
              documentHeader: "PROOF_OF_DISPATCH_LDN",
              type: "DragDropComponent",
              uploadGuidelines: "Upload .pdf or .jpg",
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
        type: "radio",
        key: "proofOfService",
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
              showProofOfAcknowledgment: true,
              isEnabled: true,
            },
            {
              code: "NO",
              name: "NO",
              showProofOfAcknowledgment: false,
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
    dependentKey: { proofOfService: ["showProofOfAcknowledgment"] },
    body: [
      {
        type: "date",
        label: "CS_DATE_OF_SERVICE_LDN",
        isMandatory: true,
        populators: {
          name: "dateOfService",
        },
      },
    ],
  },
  {
    dependentKey: { proofOfService: ["showProofOfAcknowledgment"] },
    body: [
      {
        type: "component",
        component: "SelectCustomDragDrop",
        key: "SelectCustomDragDrop",
        populators: {
          inputs: [
            {
              name: "proofOfAcknowledgmentFileUpload",
              documentHeader: "LEGAL_DEMAND_NOTICE",
              type: "DragDropComponent",
              uploadGuidelines: "Upload .pdf or .jpg",
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
        type: "radio",
        key: "proofOfReply",
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
              showProofOfReply: true,
              isEnabled: true,
            },
            {
              code: "NO",
              name: "NO",
              showProofOfReply: false,
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
    dependentKey: { proofOfReply: ["showProofOfReply"] },
    body: [
      {
        type: "date",
        label: "CS_DATE_OF_REPLY_LDN",
        isMandatory: true,
        populators: {
          name: "dateOfReply",
        },
      },
    ],
  },
  {
    dependentKey: { proofOfReply: ["showProofOfReply"] },
    body: [
      {
        type: "component",
        component: "SelectCustomDragDrop",
        key: "SelectCustomDragDrop",
        populators: {
          inputs: [
            {
              name: "proofOfReplyFileUpload",
              documentHeader: "LEGAL_DEMAND_NOTICE",
              type: "DragDropComponent",
              uploadGuidelines: "Upload .pdf or .jpg",
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
        type: "date",
        label: "CS_DATE_OF_ACCRUAL_LDN",
        isMandatory: true,
        populators: {
          name: "dateOfAccrual",
        },
      },
    ],
  },
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
];

export const demandNoticeConfig = {
  formconfig: demandNoticeFormConfig,
  header: "CS_DEMAND_NOTICE_HEADING",
  subtext: "CS_DEMAND_NOTICE_SUBTEXT",
  isOptional: false,
  addFormText: "ADD_DEMAND_NOTICE",
  formItemName: "CS_DEMAND_NOTICE",
  className: "demand-notice",
};
