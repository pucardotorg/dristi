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
              clearFields: { aadharNumber: "" },
              isMandatory: true,
              disableMandatoryFieldFor: ["aadharNumber"],
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
              clearFields: { aadharNumber: "" },
              allowedFileTypes: /(.*?)(png|jpeg|pdf)$/i,
              isMandatory: true,
              disableMandatoryFieldFor: ["aadharNumber"],
              errorMessage: "CUSTOM_DOCUMENT_ERROR_MSG",
              disableFormValidation: false,
            },
          ],
          validation: {},
        },
      },
    ],
  },
];
