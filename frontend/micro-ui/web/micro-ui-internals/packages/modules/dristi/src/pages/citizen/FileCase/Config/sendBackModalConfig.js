export const modalConfig = [
  {
    headModal: "Send Case Back",
    body: [
      {
        type: "component",
        component: "SelectCustomNote",
        key: "sendBackCaseNote",
        withoutLabel: true,
        populators: {
          inputs: [
            {
              infoHeader: "CS_COMMON_NOTE",
              infoText: "Please make sure you have reviewed all the necessary case details and documents.  ",
              infoTooltipMessage: "CS_NOTETOOLTIP_RESPONDENT_PERSONAL_DETAILS",
              type: "InfoComponent",
            },
          ],
        },
      },
      {
        type: "textarea",
        label: "Leave your comments for Litigants / Advocates to view",
        populators: {
          name: "commentForLitigant",
        },
      },
    ],
  },
];
