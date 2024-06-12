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
        type: "component",
        component: "SelectUserTypeComponent",
        key: "SelectUserTypeComponent",
        withoutLabel: true,
        populators: {
          inputs: [
            {
              label: "CS_DATE_OF_DISPATCH_LDN",
              type: "date",
              name: "dateOfDispatch",
              inputFieldClassName: "user-details-form-style",
              validation: {
                errMsg: "CORE_COMMON_APPLICANT_NAME_INVALID",
                title: "",
                patternType: "date",
                isRequired: true,
              },
              isMandatory: true,
            },
          ],
          validation: {},
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
              name: "legalDemandNotice",
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
              name: "proofOfDispatch",
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
        type: "component",
        component: "SelectUserTypeComponent",
        key: "SelectUserTypeComponent",
        withoutLabel: true,
        populators: {
          inputs: [
            {
              label: "CS_DATE_OF_SERVICE_LDN",
              type: "date",
              name: "dateOfService",
              inputFieldClassName: "user-details-form-style",
              validation: {
                errMsg: "CORE_COMMON_APPLICANT_NAME_INVALID",
                title: "",
                patternType: "date",
                isRequired: true,
              },
              isMandatory: true,
            },
          ],
          validation: {},
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
              name: "legalDemandNotice",
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
        type: "component",
        component: "SelectUserTypeComponent",
        key: "SelectUserTypeComponent",
        withoutLabel: true,
        populators: {
          inputs: [
            {
              label: "CS_DATE_OF_DISPATCH_LDN",
              type: "date",
              name: "dateOfDispatch",
              inputFieldClassName: "user-details-form-style",
              validation: {
                errMsg: "CORE_COMMON_APPLICANT_NAME_INVALID",
                title: "",
                patternType: "date",
                isRequired: true,
              },
              isMandatory: true,
            },
          ],
          validation: {},
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
              name: "legalDemandNotice",
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
        component: "SelectUserTypeComponent",
        key: "SelectUserTypeComponent",
        withoutLabel: true,
        populators: {
          inputs: [
            {
              label: "CS_DATE_OF_DISPATCH_LDN",
              type: "date",
              name: "dateOfDispatch",
              inputFieldClassName: "user-details-form-style",
              validation: {
                errMsg: "CORE_COMMON_APPLICANT_NAME_INVALID",
                title: "",
                patternType: "date",
                isRequired: true,
              },
              isMandatory: true,
            },
          ],
          validation: {},
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
  header: "CS_RESPONDENT_DETAIL_HEADING",
  subtext: "CS_RESPONDENT_DETAIL_SUBTEXT",
  isOptional: false,
  addFormText: "ADD_DEMAND_NOTICE",
  formItemName: "Respondent",
  className: "demand-notice",
};
