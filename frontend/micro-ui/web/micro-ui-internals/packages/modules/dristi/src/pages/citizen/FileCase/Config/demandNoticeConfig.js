const demandNoticeFormConfig = [
  {
    body: [
      {
        type: "component",
        component: "SelectUserTypeComponent",
        key: "modeOfDispatchType",
        isMandatory: true,
        withoutLabel: true,
        populators: {
          inputs: [
            {
              label: "CS_MODE_OF_DISPATCH_TYPE",
              type: "dropdown",
              name: "modeOfDispatchType",
              optionsKey: "name",
              error: "CORE_REQUIRED_FIELD_ERROR",
              required: false,
              isMandatory: true,
              mdmsConfig: {
                masterName: "DispatchMode",
                moduleName: "case",
                select: "(data) => {return data['case'].DispatchMode?.map((item) => {return item;});}",
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
        type: "date",
        label: "CS_DATE_OF_ISSUANCE_LDN",
        labelChildren: "OutlinedInfoIcon",
        isMandatory: true,
        populators: {
          name: "dateOfIssuance",
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
        type: "date",
        label: "CS_DATE_OF_DISPATCH_LDN",
        labelChildren: "OutlinedInfoIcon",
        isMandatory: true,
        populators: {
          name: "dateOfDispatch",
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
        key: "legalDemandNoticeFileUpload",
        withoutLabel: true,
        populators: {
          inputs: [
            {
              name: "document",
              documentHeader: "LEGAL_DEMAND_NOTICE",
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
        key: "proofOfDispatchFileUpload",
        withoutLabel: true,
        populators: {
          inputs: [
            {
              name: "document",
              documentHeader: "PROOF_OF_DISPATCH_LDN",
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
        type: "radio",
        key: "proofOfService",
        label: "CS_DELAY_APPLICATION_TYPE",
        isMandatory: true,
        populators: {
          name: "proofOfService",
          label: "CS_DELAY_APPLICATION_TYPE",
          type: "radioButton",
          optionsKey: "name",
          error: "CORE_REQUIRED_FIELD_ERROR",
          required: false,
          isMandatory: true,
          isDependent: true,
          clearFields: {
            stateOfRegistration: "",
            barRegistrationNumber: "",
            barCouncilId: [],
            stateRegnNumber: "",
          },
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
    body: [
      {
        type: "date",
        label: "CS_DATE_OF_SERVICE_LDN",
        isMandatory: true,
        populators: {
          name: "dateOfService",
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
        key: "proofOfAcknowledgmentFileUpload",
        isDocDependentOn: "proofOfService",
        isDocDependentKey: "showProofOfAcknowledgment",
        populators: {
          inputs: [
            {
              name: "document",
              documentHeader: "PROOF_LEGAL_DEMAND_NOTICE",
              type: "DragDropComponent",
              uploadGuidelines: "UPLOAD_DOC_50",
              maxFileSize: 50,
              maxFileErrorMessage: "CS_FILE_LIMIT_50_MB",
              fileTypes: ["JPG", "PNG", "PDF"],
              isMultipleUpload: true,
              documentHeaderStyle: { textAlign: "left" },
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
        label: "CS_REPLY_NOTICE",
        isMandatory: true,
        populators: {
          name: "proofOfReply",
          label: "CS_REPLY_NOTICE",
          type: "radioButton",
          optionsKey: "name",
          error: "CORE_REQUIRED_FIELD_ERROR",
          required: false,
          isMandatory: true,
          isDependent: true,
          clearFields: {
            stateOfRegistration: "",
            barRegistrationNumber: "",
            barCouncilId: [],
            stateRegnNumber: "",
          },
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
    dependentKey: {
      proofOfReply: ["showProofOfReply"],
    },
    body: [
      {
        type: "date",
        label: "CS_DATE_OF_REPLY_LDN",
        isMandatory: true,
        populators: {
          name: "dateOfReply",
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
        key: "proofOfReplyFileUpload",
        isDocDependentOn: "proofOfReply",
        isDocDependentKey: "showProofOfReply",
        populators: {
          inputs: [
            {
              name: "document",
              documentHeader: "CS_PROOF_TO_REPLY_DEMAND_NOTICE",
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
        type: "date",
        label: "CS_DATE_OF_ACCRUAL_LDN",
        labelChildren: "OutlinedInfoIcon",
        isMandatory: true,
        populators: {
          name: "dateOfAccrual",
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
        type: "radio",
        key: "delayApplicationType",
        label: "CS_PAYER_FAIL_TO_PAY",
        isMandatory: true,
        populators: {
          name: "delayApplicationType",
          label: "CS_PAYER_FAIL_TO_PAY",
          type: "radioButton",
          optionsKey: "name",
          error: "CORE_REQUIRED_FIELD_ERROR",
          required: false,
          isMandatory: true,
          isDependent: true,
          clearFields: {
            stateOfRegistration: "",
            barRegistrationNumber: "",
            barCouncilId: [],
            stateRegnNumber: "",
          },
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
  selectDocumentName: {
    proofOfDispatchFileUpload: "PROOF_OF_DISPATCH_LDN",
    proofOfAcknowledgmentFileUpload: "PROOF_LEGAL_DEMAND_NOTICE",
    proofOfReplyFileUpload: "CS_PROOF_TO_REPLY_DEMAND_NOTICE",
  },
};
