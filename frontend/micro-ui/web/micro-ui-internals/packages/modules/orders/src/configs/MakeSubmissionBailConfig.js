export const submissionTypeConfig = [
  {
    body: [
      {
        isMandatory: true,
        key: "submissionID",
        type: "text",
        label: "Submission Id",
        disable: true,
        inline: false,
        populators: {
          name: "submissionID",
          optionsKey: "id",
          error: "required ",
        },
      },
    ],
  },
];

export const applicationTypeConfig = [
  {
    body: [
      {
        isMandatory: true,
        key: "submissionType",
        type: "dropdown",
        label: "Submission Type",
        disable: false,
        inline: false,
        populators: {
          name: "submissionType",
          optionsKey: "name",
          error: " ",
          required: true,
          options: [
            {
              code: "Application",
              name: "Application",
            },
          ],
        },
      },
    ],
  },
];

// this is the config for submission Form
export const configsBailBond = [
  {
    body: [
      // {
      //   isMandatory: true,
      //   key: "applicationType",
      //   type: "dropdown",
      //   label: "Application Type",
      //   disable: false,
      //   inline: false,
      //   populators: {
      //     name: "applicationType",
      //     optionsKey: "name",
      //     required: true,
      //     options: [
      //       {
      //         code: "requestForBail",
      //         name: "Request for Bail - Surety",
      //       },
      //     ],
      //   },
      // },
      {
        label: "Add any information to support your application",
        placeholder: "Type Here.....",
        key: "additionalInfo",
        isMandatory: true,
        type: "textarea",
        inline: false,
        populators: {
          name: "additionalInfo",
        },
      },
      {
        type: "component",
        component: "SelectCustomNote",
        key: "info",
        inline: false,
        isMandatory: false,
        populators: {
          inputs: [
            {
              infoHeader: "Info",
              infoText: "If you would like to submit document(s) for bail bond, kindly make a separate submission for the same.",
              infoTooltipMessage: "CS_NOTETOOLTIP_RESPONDENT_PERSONAL_DETAILS",
              type: "InfoComponent",
            },
          ],
        },
      },
      {
        type: "component",
        component: "CustomInfo",
        key: "suretyDocuments",
        inline: false,
        isMandatory: false,
        populators: {
          inputs: [
            {
              infoHeader: "Surety documents",
              infoText: "To understand what kind of documents you can upload as surety,",
              infoTooltipMessage: "CS_NOTETOOLTIP_RESPONDENT_PERSONAL_DETAILS",
              type: "InfoComponent",
              linkText: "click here",
              modalHeading: "List of surety documents",
              modalData: [
                {
                  title: "Tax Records",
                  description: "PAN Card, Aadhar card, Passport, Driving license, Voter ID, Ration card or Bank passbook",
                  hint: "Upload .pdf or .jpg. Maximum upload size of 5MB",
                },
                {
                  title: "Salary Receipts",
                  description: "A copy of the bounced chequeon the  basis which this case is being filed",
                  hint: "Upload .pdf or .jpg. Maximum upload size of 5MB",
                },
              ],
            },
          ],
        },
      },
      {
        type: "component",
        component: "AddSubmissionDocument",
        key: "submissionDocuments",
        inline: false,
        populators: {
          inputs: [
            {
              isMandatory: true,
              key: "documentType",
              type: "dropdown",
              label: "Document Type",
              name: "documentType",
              disable: false,
              populators: {
                name: "documentType",
                optionsKey: "name",
                required: true,
                options: [
                  {
                    code: "taxRecords",
                    name: "Tax Records",
                  },
                  {
                    code: "salaryReciepts",
                    name: "Salary Reciepts",
                  },
                  // Add more options as needed
                ],
              },
            },
            {
              label: "Document Title",
              type: "text",
              name: "documentTitle",
              validation: {
                isRequired: true,
                pattern: /^[0-9A-Z/]{0,20}$/,
                errMsg: "",
              },
              isMandatory: true,
            },
            {
              label: "Attachment",
              type: "documentUpload",
              name: "document",
              validation: {
                isRequired: true,
              },
              isMandatory: true,
              allowedFileTypes: /(.*?)(png|jpeg|jpg|pdf)$/i,
            },
          ],
        },
      },
    ],
  },
];
