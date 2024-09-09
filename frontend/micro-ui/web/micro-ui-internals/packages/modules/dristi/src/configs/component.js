export const idProofVerificationConfig = [
  {
    body: [
      {
        type: "component",
        component: "IdProofUploadComponent",
        key: "IdProofUploadComponent",
        withoutLabel: true,
        populators: {
          inputs: [
            {
              label: "CS_ID_TYPE",
              type: "dropdown",
              name: "selectIdTypeType",
              optionsKey: "type",
              error: "CORE_REQUIRED_FIELD_ERROR",
              validation: {},
              isMandatory: true,
              disableFormValidation: false,
              clearFields: { ID_Proof: [] },
              clearFieldsType: { ID_Proof: "documentUpload" },
              mdmsConfig: {
                masterName: "IdentifierType",
                moduleName: "User Registration",
                select: "(data) => {return data['User Registration'].IdentifierType?.map((item) => {return item;});}",
              },
              optionsCustomStyle: {
                top: "40px",
              },
            },
            {
              label: "Upload ID Proof",
              type: "documentUpload",
              name: "ID_Proof",
              validation: {},
              fileTypes: ["PDF", "DOCS", "text/plain"],
              acceptFiles: ".pdf,.txt,.doc,.docx",
              allowedFileTypes: /(.*?)(doc|docx|pdf|txt)$/i,
              isMandatory: true,
              errorMessage: "CUSTOM_DOCUMENT_ERROR_MSG",
              disableFormValidation: false,
              uploadGuidelines: "UPLOAD_DOC_50",
              maxFileSize: 25,
              allowedMaxSizeInMB: 25,
              maxFileErrorMessage: "CS_FILE_LIMIT_25_MB",
              noteMsg: "CS_DOCUMENT_BLURB",
              notSupportedError: "NOT_SUPPORTED_ERROR",
            },
          ],
          validation: {},
        },
      },
    ],
  },
];

export const verifyMobileNoConfig = [
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
              label: "CS_LOGIN_OTP_TEXT",
              type: "text",
              name: "otpNumber",
              hasMobileNo: true,
              isMobileSecret: true,
              mobileNoKey: "mobileNumber",
              hasResendOTP: true,
              resendOTPDuration: 10,
              mobileCode: "+91",
              validation: {
                minlength: 6,
                maxlength: 6,
                pattern: /[0-9]{6}$/i,
                errMsg: "INVALID_OTP_NUMBER",
                title: "",
              },
              isMandatory: true,
            },
          ],
          validation: {},
        },
      },
    ],
  },
];
