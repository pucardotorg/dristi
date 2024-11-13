export const modalConfig = [
  {
    headModal: "CS_SEND_CASE_BACK",
    wordCount: "50",
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
              infoText: "SEND_BACK_NOTE_INFO",
              infoTooltipMessage: "CS_NOTETOOLTIP_RESPONDENT_PERSONAL_DETAILS",
              type: "InfoComponent",
            },
          ],
        },
      },
      {
        type: "textarea",
        label: "CS_COMMENT_HEADER",
        populators: {
          name: "commentForLitigant",
        },
      },
    ],
  },
  {
    headModal: "ADMIT_CASE_HEADER",
    text: "ADMIT_SUB_HEADER",
    submitText: "SURE_TEXT",
  },
  {
    headModal: "CS_SCHEDULE_ADMISSION_HEARING",
    label: "PURPOSE_OF_HEARING",
    name: "purposeOfHearing",
  },
  {
    headModal: "CS_SELECT_CUSTOM_DATE",
    label: "CS_HEARINGS_SCHEDULED",
    showBottomBar: true,
    buttonText: "CS_COMMON_CONFIRM",
  },
];
export const selectParticipantConfig = {
  header: "CHOOSE_PARTICIPANT",
  checkBoxText: "HEADER_PARTICIPANT",
  checkBoxes: [
    {
      key: "Litigants",
      name: "CS_LITIGANTS",
      dependentText: "CS_LITIGANT_SELECT",
      dependentFields: [],
    },
    {
      key: "Witness",
      name: "CS_WITNESS",
      dependentText: "CS_WITNESS_SELECT",
      dependentFields: [],
    },
  ],
};

export const scheduleCaseSubmitConfig = {
  header: "CS_ADMISSION_SUCCESS",
  backButtonText: "BACK_TO_HOME",
  nextButtonText: "NEXT_CASE",
  isArrow: true,
  showTable: true,
};

export const admitCaseSubmitConfig = {
  header: "CS_ADMIT_SUCCESS",
  subHeader: "CASE_UPDATES_SENT_VIA_SMS_MESSAGE",
  backButtonText: "BACK_TO_HOME",
  nextButtonText: "SCHEDULE_NEXT_HEARING",
  isArrow: false,
  showTable: true,
};
export const sendBackCase = {
  header: "SEND_BACK_SUBMIT",
  subHeader: "CASE_UPDATES_SENT_VIA_SMS_MESSAGE",
  backButtonText: "BACK_TO_HOME",
  nextButtonText: "NEXT_CASE",
  isArrow: true,
  showCopytext: true,
};

export const registerCaseConfig = {
  header: "CS_REGISTER_SUCCESS",
  subHeader: "CASE_UPDATES_NOTICE_ISSUED",
  backButtonText: "BACK_TO_HOME",
  nextButtonText: "CS_SCHEDULE_ADMISSION_HEARING",
  isArrow: false,
  showTable: true,
};

export const scheduleCaseAdmissionConfig = {
  header: "CS_ADMISSION_SUCCESS",
  backButtonText: "BACK_TO_HOME",
  nextButtonText: "ISSUE_NOTICE",
  isArrow: true,
  showTable: true,
};
