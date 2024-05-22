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
              allowedFileTypes: /(.*?)(png|jpeg|pdf)$/i,
              isMandatory: true,
              errorMessage: "CUSTOM_DOCUMENT_ERROR_MSG",
              disableFormValidation: false,
              uploadGuidelines: "Upload .png",
              maxFileSize: 50,
              maxFileErrorMessage: "CS_FILE_LIMIT_50_MB",
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
              label: "ENTER_AADHAR_NUMBER",
              type: "text",
              name: "aadharNumber",
              validation: {
                minlength: 12,
                maxlength: 12,
                patternType: "AadharNo",
                pattern: "[0-9]+",
                errMsg: "AADHAR_NUMBER_INVALID",
                title: "",
              },
              clearFields: { ID_Proof: [], selectIdTypeType: "" },
              clearFieldsType: { ID_Proof: "documentUpload" },
              disableMandatoryFieldFor: ["ID_Proof", "selectIdTypeType"],
              isMandatory: true,
            },
          ],
          validation: {},
        },
      },
    ],
  },
];
