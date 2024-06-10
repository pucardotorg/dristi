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
              optionsKey: "name",
              error: "sample required message",
              validation: {},
              isMandatory: true,
              disableFormValidation: false,
              options: [
                {
                  code: "AADHAR",
                  name: "AADHAR",
                },
                {
                  code: "PAN",
                  name: "PAN",
                },
              ],
              optionsCustomStyle: {
                top: "40px",
              },
            },
            {
              label: "Upload ID Proof",
              type: "documentUpload",
              name: "ID_Proof",
              validation: {},
              fileTypes: ["JPG", "PNG", "PDF"],
              allowedFileTypes: /(.*?)(doc|docx|pdf|txt)$/i,
              isMandatory: true,
              errorMessage: "CUSTOM_DOCUMENT_ERROR_MSG",
              disableFormValidation: false,
              uploadGuidelines: "UPLOAD_DOC_50",
              maxFileSize: 25,
              maxFileErrorMessage: "CS_FILE_LIMIT_25_MB",
              noteMsg: "CS_DOCUMENT_BLURB",
              notSupportedError: "NOT_SUPPORTED_ERROR"
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
              label: "ENTER_OTP_SENT_TO",
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
                errMsg: "MOBILE_NUMBER_INVALID",
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
