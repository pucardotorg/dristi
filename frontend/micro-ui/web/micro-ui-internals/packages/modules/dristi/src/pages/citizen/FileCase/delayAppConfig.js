export const configRadioOptions = [
  { name: "respondentDetails", configName: "respondentconfig" },
  { name: "delayApplication", configName: "delayAppConfig" },
];

export const delayAppConfig = [
  {
    body: [
      {
        type: "component",
        component: "SelectUserTypeComponent",
        key: "eligibleForDelayApplicationType",
        withoutLabel: true,
        populators: {
          inputs: [
            {
              label: "ELIGIBLE_FOR_DELAY_APPLICATION_TYPE",
              type: "radioButton",
              name: "selectEligibilityType",
              optionsKey: "name",
              error: "CORE_REQUIRED_FIELD_ERROR",
              //   clearFields: { stateOfRegistration: "", barRegistrationNumber: "", barCouncilId: [], stateRegnNumber: "" },
              options: [
                { code: "YES", name: "YES", configName: "de" },
                { code: "NO", name: "NO" },
              ],
            },
          ],
        },
      },
    ],
  },
  {
    body: [
      // {
      //   type: "component",
      //   component: "SelectCustomTextArea",
      //   key: "delayApplicationReason",
      //   populators: {
      //     inputs: [
      //       {
      //         textAreaHeader: "CS_TEXTAREA_HEADER_DELAY_REASON",
      //         type: "TextAreaComponent",
      //       },
      //     ],
      //   },
      // },
      {
        type: "component",
        component: "SelectTranscriptTextArea",
        key: "delayApplicationReason",
        withoutLabel: true,
        populators: {
          input: {
            textAreaHeader: "CS_TEXTAREA_HEADER_DELAY_REASON",
            type: "TranscriptionTextAreaComponent",
          },
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
              isOptional: "CS_IS_OPTIONAL",
              infoTooltipMessage: "Tooltip",
              type: "DragDropComponent",
              uploadGuidelines: "UPLOAD_DOC_50",
              maxFileSize: 50,
              maxFileErrorMessage: "CS_FILE_LIMIT_1_MB",
              fileTypes: ["JPG", "PNG", "PDF"],
            },
          ],
        },
      },
    ],
  },
];

export const mainConfig = [
  { configName: "delayAppConfig", value: delayAppConfig },
  { configName: "respondentconfig", value: delayAppConfig },
];
