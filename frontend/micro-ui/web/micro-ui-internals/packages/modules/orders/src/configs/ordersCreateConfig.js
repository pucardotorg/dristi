export const applicationTypeConfig = [
  {
    body: [
      {
        isMandatory: true,
        key: "orderType",
        type: "dropdown",
        label: "ORDER_TYPE",
        disable: false,
        populators: {
          name: "orderType",
          optionsKey: "name",
          error: "required ",
          mdmsConfig: {
            moduleName: "Order",
            masterName: "OrderType",
            localePrefix: "ORDER_TYPE",
          },
        },
      },
    ],
  },
];
export const configs = [
  {
    body: [
      {
        isMandatory: true,
        key: "orderType",
        type: "dropdown",
        label: "ORDER_TYPE",
        disable: false,
        populators: {
          name: "orderType",
          optionsKey: "name",
          error: "required ",
          mdmsConfig: {
            moduleName: "Order",
            masterName: "OrderType",
            localePrefix: "ORDER_TYPE",
          },
        },
      },
      {
        isMandatory: true,
        key: "documentType",
        type: "dropdown",
        label: "DOCUMENT_TYPE",
        disable: false,
        populators: {
          name: "DocumentType",
          optionsKey: "name",
          error: "required ",
          mdmsConfig: {
            moduleName: "Order",
            masterName: "DocumentType",
            localePrefix: "",
          },
        },
      },
      {
        isMandatory: true,
        key: "partyToMakeSubmission",
        type: "dropdown",
        label: "PARTIES_TO_MAKE_SUBMISSION",
        disable: false,
        populators: {
          name: "genders",
          optionsKey: "name",
          error: "required ",
          mdmsConfig: {
            moduleName: "Order",
            masterName: "SubmissionName",
            localePrefix: "",
          },
        },
      },
      {
        label: "DEADLINE_FOR_SUBMISSION",
        isMandatory: false,
        key: "deadlineForSubmission",
        type: "date",
        disable: false,
        populators: {
          name: "submissionDeadlineDate",
          error: "Required",
          mdmsConfig: {
            moduleName: "Order",
            masterName: "DeadlineForSubmission",
            localePrefix: "",
          },
          validation: {
            max: new Date().toISOString().split("T")[0],
          },
        },
      },
    ],
  },
  {
    body: [
      {
        type: "component",
        component: "SelectCustomTextArea",
        key: "orderAdditionalNotes",
        populators: {
          inputs: [
            {
              textAreaSubHeader: "CS_ORDER_ADDITIONAL_NOTES",
              type: "TextAreaComponent",
              isOptional: true,
            },
          ],
          mdmsConfig: {
            moduleName: "Order",
            masterName: "", // TO DO: ADD CONFIG IN MDMS
            localePrefix: "",
          },
        },
      },
    ],
  },
  {
    body: [
      {
        type: "radio",
        key: "isResponseRequired",
        label: "IS_RESPONSE_REQUIRED",
        isMandatory: true,
        populators: {
          label: "IS_RESPONSE_REQUIRED",
          type: "radioButton",
          optionsKey: "name",
          error: "CORE_REQUIRED_FIELD_ERROR",
          required: false,
          isMandatory: true,
          // isDependent: true,
          options: [
            {
              code: "YES",
              name: "YES",
              showForm: true,
              isEnabled: true,
            },
            {
              code: "NO",
              name: "NO",
              showForm: false,
              // isVerified: true,
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
        isMandatory: true,
        key: "partiesToRespond",
        type: "dropdown",
        label: "PARTIES_TO_RESPOND",
        disable: false,
        populators: {
          name: "genders",
          optionsKey: "name",
          error: "required ",
          mdmsConfig: {
            moduleName: "Order",
            masterName: "PartyToRespond",
            localePrefix: "",
          },
        },
      },
      {
        label: "DEADLINE_TO_RESPOND",
        isMandatory: false,
        key: "deadlineToRespond",
        type: "date",
        disable: false,
        populators: {
          name: "respondDeadlineDate",
          error: "Required",
          mdmsConfig: {
            moduleName: "Order",
            masterName: "", // TO DO: ADD MDMS CONFIG
            localePrefix: "",
          },
          validation: {
            max: new Date().toISOString().split("T")[0],
          },
        },
      },
    ],
  },
];

export const configsOrderSection202CRPC = [
  {
    body: [
      {
        label: "REF_APPLICATION_ID",
        isMandatory: true,
        key: "refApplicationId",
        type: "text",
        populators: { name: "refApplicationId" },
      },
      {
        label: "COURT_NAME",
        isMandatory: true,
        key: "courtName",
        type: "text",
        populators: { name: "courtName", hideInForm: true },
      },
      {
        label: "CASE_NAME",
        isMandatory: true,
        key: "caseName",
        type: "text",
        populators: { name: "caseName", hideInForm: true },
      },
      {
        label: "JUDGE_DESIGNATION",
        isMandatory: true,
        key: "judgeDesignation",
        type: "text",
        populators: { name: "judgeDesignation", hideInForm: true },
      },
      {
        label: "DISTRICT",
        isMandatory: true,
        key: "district",
        type: "text",
        populators: { name: "district", hideInForm: true },
      },
      {
        label: "STATE",
        isMandatory: true,
        key: "state",
        type: "text",
        populators: { name: "state", hideInForm: true },
      },
      {
        label: "CNR_NUMBER",
        isMandatory: true,
        key: "cnrNumber",
        type: "text",
        populators: { name: "cnrNumber", hideInForm: true },
      },
      {
        label: "FILING_YEAR",
        isMandatory: true,
        key: "filingYear",
        type: "text",
        populators: { name: "filingYear", hideInForm: true },
      },
      {
        label: "APPLICATION_FILLED_BY",
        isMandatory: true,
        key: "applicationFilledBy",
        type: "radio",
        populators: {
          name: "applicationFilledBy",
          optionsKey: "name",
          error: "Error!",
          required: true,
          options: [
            {
              code: "COMPLAINANT_1",
              name: "COMPLAINANT_1",
            },
            {
              code: "COMPLAINANT_2",
              name: "COMPLAINANT_2",
            },
            {
              code: "COMPLAINANT_3",
              name: "COMPLAINANT_3",
            },
          ],
        },
      },
      {
        label: "DETAILS_SEEKED_OF",
        isMandatory: true,
        key: "detailsSeekedOf",
        type: "radio",
        populators: {
          name: "detailsSeekedOf",
          optionsKey: "name",
          error: "Error!",
          required: true,
          options: [
            {
              code: "RESPONDANT_1",
              name: "RESPONDANT_1",
            },
            {
              code: "RESPONDANT_2",
              name: "RESPONDANT_2",
            },
            {
              code: "RESPONDANT_3",
              name: "RESPONDANT_3",
            },
          ],
        },
      },
      {
        label: "LAW_SECTIONS",
        isMandatory: true,
        key: "lawSections",
        type: "textarea",
        populators: { name: "lawSections" },
      },
      {
        label: "RESPONSE_REQUIRED_BY",
        isMandatory: true,
        key: "responseRequiredBy",
        type: "date",
        populators: {
          name: "responseRequiredBy",
          validation: {
            max: new Date().toISOString().split("T")[0],
          },
        },
      },
      {
        label: "DATE_OF_ORDER",
        isMandatory: true,
        key: "dateOfOrder",
        type: "date",
        populators: {
          name: "dateOfOrder",
          hideInForm: true,
          validation: {
            max: new Date().toISOString().split("T")[0],
          },
        },
      },
      {
        label: "JUDGE_NAME",
        isMandatory: true,
        key: "judgeName",
        type: "text",
        populators: { name: "judgeName", hideInForm: true },
      },
    ],
  },
];

export const configsOrderMandatorySubmissions = [
  {
    body: [
      {
        label: "COURT_NAME",
        isMandatory: true,
        key: "courtName",
        type: "text",
        populators: { name: "courtName", hideInForm: true },
      },
      {
        label: "CASE_NAME",
        isMandatory: true,
        key: "caseName",
        type: "text",
        populators: { name: "caseName", hideInForm: true },
      },
      {
        label: "CNR_NUMBER",
        isMandatory: true,
        key: "cnrNumber",
        type: "text",
        populators: { name: "cnrNumber", hideInForm: true },
      },
      {
        label: "FILING_NUMBER",
        isMandatory: true,
        key: "filingNumber",
        type: "text",
        populators: { name: "filingNumber", hideInForm: true },
      },
      {
        label: "JUDGE_NAME",
        isMandatory: true,
        key: "judgeName",
        type: "text",
        populators: { name: "judgeName", hideInForm: true },
      },
      {
        label: "DATE_OF_ORDER",
        isMandatory: true,
        key: "dateOfOrder",
        type: "date",
        populators: {
          name: "dateOfOrder",
          hideInForm: true,
          validation: {
            max: new Date().toISOString().split("T")[0],
          },
        },
      },
      {
        label: "DOCUMENT_TYPE",
        isMandatory: true,
        key: "documentType",
        type: "dropdown",
        populators: {
          name: "documentType",
          optionsKey: "name",
          error: "Error!",
          required: true,
          options: [
            {
              code: "DOCUMENT_TYPE_1",
              name: "DOCUMENT_TYPE_1",
            },
            {
              code: "DOCUMENT_TYPE_2",
              name: "DOCUMENT_TYPE_2",
            },
            {
              code: "DOCUMENT_TYPE_3",
              name: "DOCUMENT_TYPE_3",
            },
          ],
        },
      },
      {
        label: "DOCUMENT_NAME",
        isMandatory: true,
        key: "documentName",
        type: "text",
        populators: { name: "documentName" },
      },
      {
        label: "SUBMISSION_PARTY",
        isMandatory: true,
        key: "submissionParty",
        type: "multiselectdropdown",
        populators: {
          name: "submissionParty",
          optionsKey: "name",
          error: "Error!",
          required: true,
          options: [
            {
              code: "PARTY_1",
              name: "PARTY_1",
            },
            {
              code: "PARTY_2",
              name: "PARTY_2",
            },
            {
              code: "PARTY_3",
              name: "PARTY_3",
            },
          ],
        },
      },
      {
        label: "SUBMISSION_DEADLINE",
        isMandatory: true,
        key: "submissionDeadline",
        type: "date",
        populators: {
          name: "submissionDeadline",
          validation: {
            max: new Date().toISOString().split("T")[0],
          },
        },
      },
      {
        label: "ADDITIONAL_COMMENTS",
        isMandatory: false,
        key: "additionalComments",
        type: "textarea",
        populators: { name: "additionalComments" },
      },
      {
        label: "IS_RESPONSE_REQUIRED",
        isMandatory: true,
        key: "isResponseRequired",
        type: "checkbox",
        populators: { name: "isResponseRequired", title: "" },
      },
      {
        label: "RESPONDING_PARTY",
        isMandatory: true,
        key: "respondingParty",
        type: "multiselectdropdown",
        populators: {
          name: "respondingParty",
          optionsKey: "name",
          error: "Error!",
          required: true,
          options: [
            {
              code: "PARTY_1",
              name: "PARTY_1",
            },
            {
              code: "PARTY_2",
              name: "PARTY_2",
            },
            {
              code: "PARTY_3",
              name: "PARTY_3",
            },
          ],
        },
      },
      {
        label: "RESPONSE_DEADLINE",
        isMandatory: true,
        key: "responseDeadline",
        type: "date",
        populators: {
          name: "responseDeadline",
          validation: {
            min: new Date().toISOString().split("T")[0],
          },
        },
      },
      {
        label: "JUDGE_DESIGNATION",
        isMandatory: true,
        key: "judgeDesignation",
        type: "text",
        populators: { name: "judgeDesignation", hideInForm: true },
      },
    ],
  },
];

export const configsOrderSubmissionExtension = [
  {
    body: [
      {
        label: "REF_APPLICATION_ID",
        isMandatory: true,
        key: "refApplicationId",
        type: "text",
        populators: { name: "refApplicationId" },
      },
      {
        label: "COURT_NAME",
        isMandatory: true,
        key: "courtName",
        type: "text",
        populators: { name: "courtName", hideInForm: true },
      },
      {
        label: "CASE_NAME",
        isMandatory: true,
        key: "caseName",
        type: "text",
        populators: { name: "caseName", hideInForm: true },
      },
      {
        label: "CNR_NUMBER",
        isMandatory: true,
        key: "cnrNumber",
        type: "text",
        populators: { name: "cnrNumber", hideInForm: true },
      },
      {
        label: "FILING_NUMBER",
        isMandatory: true,
        key: "filingNumber",
        type: "text",
        populators: { name: "filingNumber", hideInForm: true },
      },
      {
        label: "JUDGE_NAME",
        isMandatory: true,
        key: "judgeName",
        type: "text",
        populators: { name: "judgeName", hideInForm: true },
      },
      {
        label: "DATE_OF_ORDER",
        isMandatory: true,
        key: "dateOfOrder",
        type: "date",
        populators: { name: "dateOfOrder", hideInForm: true },
      },
      {
        label: "DOCUMENT_NAME",
        isMandatory: true,
        key: "documentName",
        type: "text",
        populators: { name: "documentName" },
      },
      {
        label: "ADVOCATE_NAME",
        isMandatory: true,
        key: "advocateName",
        type: "text",
        populators: { name: "advocateName", hideInForm: true },
      },
      {
        label: "APPLICATION_DATE",
        isMandatory: true,
        key: "applicationDate",
        type: "date",
        populators: { name: "applicationDate", hideInForm: true },
      },
      {
        label: "ORIGINAL_SUBMISSION_ORDER_DATE",
        isMandatory: true,
        key: "originalSubmissionOrderDate",
        type: "date",
        populators: {
          name: "originalSubmissionOrderDate",
          validation: {
            min: new Date().toISOString().split("T")[0],
          },
        },
      },
      {
        label: "ORIGINAL_DEADLINE",
        isMandatory: true,
        key: "originalDeadline",
        type: "date",
        populators: {
          name: "originalDeadline",
          validation: {
            max: new Date().toISOString().split("T")[0],
          },
        },
      },
      {
        label: "PROPOSED_SUBMISSION_DATE",
        isMandatory: true,
        key: "proposedSubmissionDate",
        type: "date",
        populators: {
          name: "proposedSubmissionDate",
          validation: {
            max: new Date().toISOString().split("T")[0],
          },
        },
      },
      {
        label: "NEW_SUBMISSION_DATE",
        isMandatory: true,
        key: "newSubmissionDate",
        type: "date",
        populators: {
          name: "newSubmissionDate",
          validation: {
            max: new Date().toISOString().split("T")[0],
          },
        },
      },
      {
        label: "JUDGE_DESIGNATION",
        isMandatory: true,
        key: "judgeDesignation",
        type: "text",
        populators: { name: "judgeDesignation", hideInForm: true },
      },
    ],
  },
];

export const configsOrderTranferToADR = [
  {
    body: [
      {
        label: "REF_APPLICATION_ID",
        isMandatory: true,
        key: "refApplicationId",
        type: "text",
        populators: { name: "refApplicationId" },
      },
      {
        label: "COURT_NAME",
        isMandatory: true,
        key: "courtName",
        type: "text",
        populators: { name: "courtName", hideInForm: true },
      },
      {
        label: "CASE_NAME",
        isMandatory: true,
        key: "caseName",
        type: "text",
        populators: { name: "caseName", hideInForm: true },
      },
      {
        label: "CNR_NUMBER",
        isMandatory: true,
        key: "cnrNumber",
        type: "text",
        populators: { name: "cnrNumber", hideInForm: true },
      },
      {
        label: "FILING_NUMBER",
        isMandatory: true,
        key: "filingNumber",
        type: "text",
        populators: { name: "filingNumber", hideInForm: true },
      },
      {
        label: "JUDGE_NAME",
        isMandatory: true,
        key: "judgeName",
        type: "text",
        populators: { name: "judgeName", hideInForm: true },
      },
      {
        label: "DATE_OF_ORDER",
        isMandatory: true,
        key: "dateOfOrder",
        type: "date",
        populators: { name: "dateOfOrder", hideInForm: true },
      },
      {
        label: "ADR_MODE",
        isMandatory: true,
        key: "ADRMode",
        type: "dropdown",
        populators: {
          name: "ADRMode",
          optionsKey: "name",
          error: "Error!",
          required: true,
          options: [
            {
              code: "MODE_1",
              name: "MODE_1",
            },
            {
              code: "MODE_2",
              name: "MODE_2",
            },
            {
              code: "MODE_3",
              name: "MODE_3",
            },
          ],
        },
      },
      {
        label: "JUDGE_DESIGNATION",
        isMandatory: true,
        key: "judgeDesignation",
        type: "text",
        populators: { name: "judgeDesignation", hideInForm: true },
      },
    ],
  },
];

export const configsScheduleHearingDate = [
  {
    body: [
      {
        label: "REF_APPLICATION_ID",
        isMandatory: true,
        key: "refApplicationId",
        type: "text",
        populators: { name: "refApplicationId" },
      },
      {
        label: "COURT_NAME",
        isMandatory: true,
        key: "courtName",
        type: "text",
        populators: { name: "courtName", hideInForm: true },
      },
      {
        label: "CASE_NAME",
        isMandatory: true,
        key: "caseName",
        type: "text",
        populators: { name: "caseName", hideInForm: true },
      },
      {
        label: "CNR_NUMBER",
        isMandatory: true,
        key: "cnrNumber",
        type: "text",
        populators: { name: "cnrNumber", hideInForm: true },
      },
      {
        label: "JUDGE_NAME",
        isMandatory: true,
        key: "judgeName",
        type: "text",
        populators: { name: "judgeName", hideInForm: true },
      },
      {
        label: "DATE_OF_ORDER",
        isMandatory: true,
        key: "dateOfOrder",
        type: "date",
        populators: { name: "dateOfOrder", hideInForm: true },
      },
      {
        label: "HEARING_PURPOSE",
        isMandatory: true,
        key: "hearingPurpose",
        type: "dropdown",
        populators: {
          name: "hearingPurpose",
          optionsKey: "name",
          error: "Error!",
          required: true,
          hideInForm: true,
          options: [
            {
              code: "HEARING_PURPOSE_1",
              name: "HEARING_PURPOSE_1",
            },
            {
              code: "HEARING_PURPOSE_2",
              name: "HEARING_PURPOSE_2",
            },
            {
              code: "HEARING_PURPOSE_3",
              name: "HEARING_PURPOSE_3",
            },
          ],
        },
      },
      {
        label: "HEARING_DATE",
        isMandatory: true,
        key: "hearingDate",
        type: "date",
        populators: {
          name: "hearingDate",
          validation: {
            max: new Date().toISOString().split("T")[0],
          },
        },
      },
      {
        label: "JUDGE_DESIGNATION",
        isMandatory: true,
        key: "judgeDesignation",
        type: "text",
        populators: { name: "judgeDesignation", hideInForm: true },
      },
    ],
  },
];

export const configsRescheduleHearingDate = [
  {
    body: [
      {
        label: "REF_APPLICATION_ID",
        isMandatory: true,
        key: "refApplicationId",
        type: "text",
        populators: { name: "refApplicationId" },
      },
      {
        label: "COURT_NAME",
        isMandatory: true,
        key: "courtName",
        type: "text",
        populators: { name: "courtName", hideInForm: true },
      },
      {
        label: "CASE_NAME",
        isMandatory: true,
        key: "caseName",
        type: "text",
        populators: { name: "caseName", hideInForm: true },
      },
      {
        label: "CNR_NUMBER",
        isMandatory: true,
        key: "cnrNumber",
        type: "text",
        populators: { name: "cnrNumber", hideInForm: true },
      },
      {
        label: "DATE_OF_ORDER",
        isMandatory: true,
        key: "dateOfOrder",
        type: "date",
        populators: { name: "dateOfOrder", hideInForm: true },
      },
      {
        label: "APPLICANT_NAME",
        isMandatory: true,
        key: "applicantName",
        type: "text",
        populators: { name: "applicantName", hideInForm: true },
      },
      {
        label: "RESCHEDULING_REASON",
        isMandatory: true,
        key: "reschedulingReason",
        type: "text",
        populators: { name: "reschedulingReason", hideInForm: true },
      },
      {
        label: "APPLICTION_STATUS",
        isMandatory: true,
        key: "applicationStatus",
        type: "text",
        disable: true,
        populators: { name: "applicationStatus", hideInForm: true },
      },
      {
        label: "ORIGINAL_HEARING_DATE",
        isMandatory: true,
        key: "originalHearingDate",
        type: "date",
        populators: {
          name: "originalHearingDate",
          validation: {
            max: new Date().toISOString().split("T")[0],
          },
        },
      },
      {
        label: "NEW_HEARING_DATE",
        isMandatory: true,
        key: "newHearingDate",
        type: "date",
        populators: {
          name: "newHearingDate",
          validation: {
            max: new Date().toISOString().split("T")[0],
          },
        },
      },
      {
        label: "JUDGE_NAME",
        isMandatory: true,
        key: "judgeName",
        type: "text",
        populators: { name: "judgeName", hideInForm: true },
      },
      {
        label: "JUDGE_DESIGNATION",
        isMandatory: true,
        key: "judgeDesignation",
        type: "text",
        populators: { name: "judgeDesignation", hideInForm: true },
      },
    ],
  },
];

export const configsVoluntarySubmissionStatus = [
  {
    body: [
      {
        label: "REF_APPLICATION_ID",
        isMandatory: true,
        key: "refApplicationId",
        type: "text",
        populators: { name: "refApplicationId" },
      },
      {
        label: "COURT_NAME",
        isMandatory: true,
        key: "courtName",
        type: "text",
        populators: { name: "courtName", hideInForm: true },
      },
      {
        label: "CASE_NAME",
        isMandatory: true,
        key: "caseName",
        type: "text",
        populators: { name: "caseName", hideInForm: true },
      },
      {
        label: "CNR_NUMBER",
        isMandatory: true,
        key: "cnrNumber",
        type: "text",
        populators: { name: "cnrNumber", hideInForm: true },
      },
      {
        label: "DATE_OF_ORDER",
        isMandatory: true,
        key: "dateOfOrder",
        type: "date",
        populators: { name: "dateOfOrder", hideInForm: true },
      },
      {
        label: "APPLICANT_NAME",
        isMandatory: true,
        key: "applicantName",
        type: "text",
        populators: { name: "applicantName", hideInForm: true },
      },
      {
        label: "SUBMISSION_DATE",
        isMandatory: true,
        key: "submissionDate",
        type: "date",
        populators: { name: "submissionDate", hideInForm: true },
      },
      {
        label: "SUBMISSION_ID",
        isMandatory: true,
        key: "submissionID",
        type: "text",
        populators: { name: "submissionID", hideInForm: true },
      },
      {
        label: "SUBMISSION_TYPE",
        isMandatory: true,
        key: "submissionType",
        type: "date",
        populators: { name: "submissionType", hideInForm: true },
      },
      {
        label: "APPROVAL_STATUS",
        isMandatory: false,
        key: "approvalStatus",
        type: "text",
        disable: true,
        populators: { name: "approvalStatus" },
      },
      {
        label: "COMMENTS",
        isMandatory: false,
        key: "comments",
        type: "textarea",
        populators: { name: "comments" },
      },
      {
        label: "JUDGE_NAME",
        isMandatory: true,
        key: "judgeName",
        type: "text",
        populators: { name: "judgeName", hideInForm: true },
      },
      {
        label: "JUDGE_DESIGNATION",
        isMandatory: true,
        key: "judgeDesignation",
        type: "text",
        populators: { name: "judgeDesignation", hideInForm: true },
      },
    ],
  },
];

export const configsCaseTransfer = [
  {
    body: [
      {
        label: "REF_APPLICATION_ID",
        isMandatory: true,
        key: "refApplicationId",
        type: "text",
        populators: { name: "refApplicationId" },
      },
      {
        label: "COURT_NAME",
        isMandatory: true,
        key: "courtName",
        type: "text",
        populators: { name: "courtName", hideInForm: true },
      },
      {
        label: "CASE_NAME",
        isMandatory: true,
        key: "caseName",
        type: "text",
        populators: { name: "caseName", hideInForm: true },
      },
      {
        label: "CNR_NUMBER",
        isMandatory: true,
        key: "cnrNumber",
        type: "text",
        populators: { name: "cnrNumber", hideInForm: true },
      },
      {
        label: "DATE_OF_ORDER",
        isMandatory: true,
        key: "dateOfOrder",
        type: "date",
        populators: { name: "dateOfOrder", hideInForm: true },
      },
      {
        label: "COMPLAINANT_NAME",
        isMandatory: true,
        key: "complainantName",
        type: "textarea",
        populators: { name: "complainantName", hideInForm: true },
      },
      {
        label: "COMPLAINANT_ADDRESS",
        isMandatory: true,
        key: "complainantAddress",
        type: "text",
        populators: { name: "complainantAddress", hideInForm: true },
      },
      {
        label: "TRANSFER_SEEKED_TO",
        isMandatory: true,
        key: "transferSeekedTo",
        type: "text",
        populators: { name: "transferSeekedTo" },
      },
      {
        label: "GROUNDS",
        isMandatory: true,
        key: "grounds",
        type: "textarea",
        populators: { name: "grounds" },
      },
      {
        label: "APPROVAL_STATUS",
        isMandatory: false,
        key: "approvalStatus",
        type: "text",
        disable: true,
        populators: { name: "approvalStatus" },
      },
      {
        label: "CASE_TRANSFERRED_TO",
        isMandatory: true,
        key: "caseTransferredTo",
        type: "text",
        populators: { name: "caseTransferredTo" },
      },
      {
        label: "COMMENTS",
        isMandatory: false,
        key: "comments",
        type: "textarea",
        populators: { name: "comments" },
      },
      {
        label: "JUDGE_NAME",
        isMandatory: true,
        key: "judgeName",
        type: "text",
        populators: { name: "judgeName", hideInForm: true },
      },
      {
        label: "JUDGE_DESIGNATION",
        isMandatory: true,
        key: "judgeDesignation",
        type: "text",
        populators: { name: "judgeDesignation", hideInForm: true },
      },
    ],
  },
];

export const configsCaseSettlement = [
  {
    body: [
      {
        label: "REF_APPLICATION_ID",
        isMandatory: true,
        key: "refApplicationId",
        type: "text",
        populators: { name: "refApplicationId" },
      },
      {
        label: "COURT_NAME",
        isMandatory: true,
        key: "courtName",
        type: "text",
        populators: { name: "courtName", hideInForm: true },
      },
      {
        label: "CASE_NAME",
        isMandatory: true,
        key: "caseName",
        type: "text",
        populators: { name: "caseName", hideInForm: true },
      },
      {
        label: "CNR_NUMBER",
        isMandatory: true,
        key: "cnrNumber",
        type: "text",
        populators: { name: "cnrNumber", hideInForm: true },
      },
      {
        label: "DATE_OF_ORDER",
        isMandatory: true,
        key: "dateOfOrder",
        type: "date",
        populators: {
          name: "dateOfOrder",
          validation: {
            max: new Date().toISOString().split("T")[0],
          },
        },
      },
      {
        label: "SETTLEMENT_AGREEMENT_DATE",
        isMandatory: true,
        key: "settlementAgreementDate",
        type: "date",
        populators: {
          name: "settlementAgreementDate",
          validation: {
            max: new Date().toISOString().split("T")[0],
          },
        },
      },
      {
        label: "SETTLEMENT_MECHANISM",
        isMandatory: true,
        key: "settlementMechanism",
        type: "dropdown",
        populators: {
          name: "settlementMechanism",
          optionsKey: "name",
          error: "Error!",
          required: true,
          options: [
            {
              code: "MECHANISM_1",
              name: "MECHANISM_1",
            },
            {
              code: "MECHANISM_2",
              name: "MECHANISM_2",
            },
            {
              code: "MECHANISM_3",
              name: "MECHANISM_3",
            },
          ],
        },
      },
      {
        label: "SETTLEMENT_IMPLEMETED",
        isMandatory: true,
        key: "settlementImplemented",
        type: "checkbox",
        populators: { name: "settlementImplemented", title: "" },
      },
      {
        label: "COMMENTS",
        isMandatory: false,
        key: "comments",
        type: "textarea",
        populators: { name: "comments", hideInForm: true },
      },
      {
        label: "JUDGE_NAME",
        isMandatory: true,
        key: "judgeName",
        type: "text",
        populators: { name: "judgeName", hideInForm: true },
      },
      {
        label: "JUDGE_DESIGNATION",
        isMandatory: true,
        key: "judgeDesignation",
        type: "text",
        populators: { name: "judgeDesignation", hideInForm: true },
      },
    ],
  },
];

export const configsIssueSummons = [
  {
    body: [
      {
        label: "REF_APPLICATION_ID",
        isMandatory: true,
        key: "refApplicationId",
        type: "text",
        populators: { name: "refApplicationId" },
      },
      {
        label: "COURT_NAME",
        isMandatory: true,
        key: "courtName",
        type: "text",
        populators: { name: "courtName", hideInForm: true },
      },
      {
        label: "CASE_NAME",
        isMandatory: true,
        key: "caseName",
        type: "text",
        populators: { name: "caseName", hideInForm: true },
      },
      {
        label: "CNR_NUMBER",
        isMandatory: true,
        key: "cnrNumber",
        type: "text",
        populators: { name: "cnrNumber", hideInForm: true },
      },
      {
        label: "DATE_OF_ORDER",
        isMandatory: true,
        key: "dateOfOrder",
        type: "date",
        populators: { name: "dateOfOrder", hideInForm: true },
      },
      {
        label: "ISSUE_SUMMONS_TO",
        isMandatory: true,
        key: "issueSummonsTo",
        type: "text",
        populators: { name: "issueSummonsTo", hideInForm: true },
      },
      {
        label: "HEARING_DATE",
        isMandatory: true,
        key: "hearingDate",
        type: "date",
        populators: { name: "hearingDate", hideInForm: true },
      },
      {
        label: "COMMENTS",
        isMandatory: true,
        key: "comments",
        type: "textarea",
        populators: { name: "comments", hideInForm: true },
      },
      {
        label: "JUDGE_NAME",
        isMandatory: true,
        key: "judgeName",
        type: "text",
        populators: { name: "judgeName", hideInForm: true },
      },
      {
        label: "JUDGE_DESIGNATION",
        isMandatory: true,
        key: "judgeDesignation",
        type: "text",
        populators: { name: "judgeDesignation", hideInForm: true },
      },
    ],
  },
];

export const configsIssueOfWarrants = [
  {
    body: [
      {
        label: "REF_APPLICATION_ID",
        isMandatory: true,
        key: "refApplicationId",
        type: "text",
        populators: { name: "refApplicationId" },
      },
      {
        label: "COURT_NAME",
        isMandatory: true,
        key: "courtName",
        type: "text",
        populators: { name: "courtName", hideInForm: true },
      },
      {
        label: "CASE_NAME",
        isMandatory: true,
        key: "caseName",
        type: "text",
        populators: { name: "caseName", hideInForm: true },
      },
      {
        label: "CNR_NUMBER",
        isMandatory: true,
        key: "cnrNumber",
        type: "text",
        populators: { name: "cnrNumber", hideInForm: true },
      },
      {
        label: "DATE_OF_ORDER",
        isMandatory: true,
        key: "dateOfOrder",
        type: "date",
        populators: { name: "dateOfOrder", hideInForm: true },
      },
      {
        label: "ISSUE_WARRANTS_FOR",
        isMandatory: true,
        key: "issueWarrantsFor",
        type: "text",
        populators: { name: "issueWarrantsFor", hideInForm: true },
      },
      {
        label: "REASON_FOR_WARRANT",
        isMandatory: true,
        key: "reasonForWarrant",
        type: "text",
        populators: { name: "reasonForWarrant" },
      },
      {
        label: "HEARING_DATE",
        isMandatory: true,
        key: "hearingDate",
        type: "date",
        populators: { name: "hearingDate", hideInForm: true },
      },
      {
        label: "COMMENTS",
        isMandatory: true,
        key: "comments",
        type: "textarea",
        populators: { name: "comments", hideInForm: true },
      },
      {
        label: "JUDGE_NAME",
        isMandatory: true,
        key: "judgeName",
        type: "text",
        populators: { name: "judgeName", hideInForm: true },
      },
      {
        label: "JUDGE_DESIGNATION",
        isMandatory: true,
        key: "judgeDesignation",
        type: "text",
        populators: { name: "judgeDesignation", hideInForm: true },
      },
    ],
  },
];

export const configsOthers = [
  {
    body: [
      {
        label: "ORDER_TITLE",
        isMandatory: true,
        key: "orderTitle",
        type: "text",
        populators: { name: "orderTitle" },
      },
      {
        label: "DETAILS",
        isMandatory: true,
        key: "otherDetails",
        type: "textarea",
        populators: { name: "otherDetails" },
      },
    ],
  },
];

export const configsBail = [
  {
    body: [
      {
        label: "REF_APPLICATION_ID",
        isMandatory: true,
        key: "refApplicationId",
        type: "text",
        populators: { name: "refApplicationId" },
      },
      {
        label: "COURT_NAME",
        isMandatory: true,
        key: "courtName",
        type: "text",
        populators: { name: "courtName", hideInForm: true },
      },
      {
        label: "CASE_NAME",
        isMandatory: true,
        key: "caseName",
        type: "text",
        populators: { name: "caseName", hideInForm: true },
      },
      {
        label: "CNR_NUMBER",
        isMandatory: true,
        key: "cnrNumber",
        type: "text",
        populators: { name: "cnrNumber", hideInForm: true },
      },
      {
        label: "DATE_OF_ORDER",
        isMandatory: true,
        key: "dateOfOrder",
        type: "date",
        populators: { name: "dateOfOrder", hideInForm: true },
      },
      {
        label: "BAIL_OF",
        isMandatory: true,
        key: "bailOf",
        type: "text",
        populators: { name: "bailOf" },
      },
      {
        label: "SUMMARY",
        isMandatory: true,
        key: "summary",
        type: "textarea",
        populators: { name: "summary", hideInForm: true },
      },
      {
        label: "ATTACHED_DOCUMENTS",
        isMandatory: true,
        key: "attachedDocuments",
        type: "textarea",
        populators: { name: "attachedDocuments", hideInForm: true },
      },
      {
        label: "BAIL_TYPE",
        isMandatory: true,
        key: "bailType",
        type: "dropdown",
        populators: {
          name: "bailType",
          optionsKey: "name",
          error: "Error!",
          required: true,
          options: [
            {
              code: "SURETY",
              name: "SURETY",
            },
            {
              code: "BAIL_BOND",
              name: "BAIL_BOND",
            },
            {
              code: "CASH",
              name: "CASH",
            },
          ],
        },
      },
      {
        label: "BAIL_AMOUNT",
        isMandatory: true,
        key: "bailAmount",
        type: "number",
        populators: { name: "bailAmount" },
      },
      {
        label: "OTHER_CONDITIONS",
        isMandatory: true,
        key: "otherConditions",
        type: "text",
        populators: { name: "otherConditions" },
      },
    ],
  },
];

export const configsCreateOrderSchedule = [
  {
    defaultValues: {
      orderType: {
        id: 8,
        type: "NEXT_HEARING",
        isactive: true,
        code: "NEXT_HEARING",
      },
    },
    body: [
      {
        isMandatory: true,
        key: "Order Type",
        type: "dropdown",
        label: "ORDER_TYPE",
        disable: true,
        populators: {
          name: "orderType",
          optionsKey: "code",
          error: "required ",
          mdmsConfig: {
            masterName: "OrderType",
            moduleName: "Order",
            localePrefix: "ORDER_TYPE",
          },
        },
      },
      {
        isMandatory: true,
        key: "Hearing Type",
        type: "dropdown",
        label: "HEARING_TYPE",
        disable: false,
        populators: {
          name: "hearingType",
          optionsKey: "code",
          error: "required ",
          mdmsConfig: {
            masterName: "HearingType",
            moduleName: "Hearing",
            localePrefix: "HEARING_TYPE",
          },
        },
      },
      {
        isMandatory: true,
        key: "Parties to Attend",
        type: "dropdown",
        label: "PARTIES_TO_ATTEND",
        disable: false,
        populators: {
          name: "hearingType",
          optionsKey: "code",
          error: "required ",
          mdmsConfig: {
            masterName: "HearingType",
            moduleName: "Hearing",
            localePrefix: "HEARING_TYPE",
          },
        },
      },
      {
        label: "DATE_OF_HEARING",
        isMandatory: true,
        key: "doh",
        type: "date",
        disable: false,
        populators: {
          name: "doh",
          error: "Required",
          validation: {
            max: new Date().toISOString().split("T")[0],
          },
        },
      },
      {
        label: "Purpose of Hearing",
        isMandatory: true,
        description: "",
        type: "textarea",
        disable: false,
        populators: { name: "purpose", error: "Error!" },
      },
      {
        label: "Additional notes (optional)",
        isMandatory: true,
        description: "",
        type: "textarea",
        disable: false,
        populators: { name: "additionalNotes", error: "Error!" },
      },
    ],
  },
];

export const configsCreateOrderWarrant = [
  {
    defaultValues: {
      orderType: {
        id: 5,
        type: "WARRANT",
        isactive: true,
        code: "WARRANT",
      },
    },
    body: [
      {
        isMandatory: true,
        key: "Order Type",
        type: "dropdown",
        label: "ORDER_TYPE",
        disable: true,
        populators: {
          name: "orderType",
          optionsKey: "code",
          error: "required ",
          mdmsConfig: {
            masterName: "OrderType",
            moduleName: "Order",
            localePrefix: "ORDER_TYPE",
          },
        },
      },
      {
        label: "DATE_OF_HEARING",
        isMandatory: true,
        key: "doh",
        type: "date",
        disable: false,
        populators: {
          name: "doh",
          error: "Required",
          validation: {
            max: new Date().toISOString().split("T")[0],
          },
        },
      },
      {
        isMandatory: true,
        key: "Warrant For",
        type: "dropdown",
        label: "WARRANT_FOR_PARTY",
        disable: false,
        populators: {
          name: "warrantFor",
          optionsKey: "code",
          error: "required ",
          mdmsConfig: {
            masterName: "OrderType",
            moduleName: "Order",
            localePrefix: "ORDER_TYPE",
          },
        },
      },
      {
        isMandatory: true,
        key: "Warrant Type",
        type: "dropdown",
        label: "WARRANT_TYPE",
        disable: false,
        populators: {
          name: "warrantType",
          optionsKey: "code",
          error: "required ",
          mdmsConfig: {
            masterName: "OrderType",
            moduleName: "Order",
            localePrefix: "ORDER_TYPE",
          },
        },
      },
      {
        isMandatory: true,
        type: "radio",
        key: "bailable",
        label: "Is this a bailable warrant?",
        disable: false,
        populators: {
          name: "bailable",
          optionsKey: "name",
          error: "Error!",
          required: false,
          options: [
            {
              code: "Yes",
              name: "ES_COMMON_YES",
            },
            {
              code: "No",
              name: "ES_COMMON_NO",
            },
          ],
        },
      },
      // {
      //   isMandatory: true,
      //   key: "Document Type",
      //   type: "dropdown",
      //   label: "document type",
      //   disable: false,
      //   populators: {
      //     name: "genders",
      //     optionsKey: "name",
      //     error: "required ",
      //     mdmsConfig: {
      //       masterName: "GenderType",
      //       moduleName: "common-masters",
      //       localePrefix: "COMMON_GENDER",
      //     },
      //   },
      // },
      // {
      //   isMandatory: true,
      //   key: "Party / parties to make submission",
      //   type: "dropdown",
      //   label: "Order for document Submission",
      //   disable: false,
      //   populators: {
      //     name: "genders",
      //     optionsKey: "name",
      //     error: "required ",
      //     mdmsConfig: {
      //       masterName: "GenderType",
      //       moduleName: "common-masters",
      //       localePrefix: "COMMON_GENDER",
      //     },
      //   },
      // },
      // {
      //
      //   label: "deadline for submission",
      //   isMandatory: false,
      //   key: "dob",
      //   type: "date",
      //   disable: false,
      //   populators: { name: "dob", error: "Required"},
      // },

      //   {
      //     label: "Additional notes",
      //     isMandatory: true,
      //     key: "phno",
      //     type: "number",
      //     disable: false,
      //     populators: { name: "phno", error: "Required", validation: { min: 0, max: 9999999999 } },
      //   },
    ],
  },
];

export const configsCreateOrderSummon = [
  {
    defaultValues: {
      orderType: {
        id: 4,
        type: "SUMMONS",
        isactive: true,
        code: "SUMMONS",
      },
    },
    body: [
      {
        isMandatory: true,
        key: "Order Type",
        type: "dropdown",
        label: "ORDER_TYPE",
        disable: true,
        populators: {
          name: "orderType",
          optionsKey: "code",
          error: "required ",
          mdmsConfig: {
            masterName: "OrderType",
            moduleName: "Order",
            localePrefix: "ORDER_TYPE",
          },
        },
      },
      {
        label: "DATE_OF_HEARING",
        isMandatory: true,
        key: "doh",
        type: "date",
        disable: false,
        populators: {
          name: "doh",
          error: "Required",
          validation: {
            max: new Date().toISOString().split("T")[0],
          },
        },
      },
      {
        isMandatory: true,
        key: "Parties to SUMMON",
        type: "dropdown",
        label: "PARTIES_TO_SUMMON",
        disable: false,
        populators: {
          name: "partyToSummon",
          optionsKey: "code",
          error: "required ",
          mdmsConfig: {
            masterName: "HearingType",
            moduleName: "Hearing",
            localePrefix: "HEARING_TYPE",
          },
        },
      },
      {
        isMandatory: false,
        key: "deliveryChannels",
        type: "component", // for custom component
        component: "DeliveryChannels", // name of the component as per component registry
        withoutLabel: true,
        disable: false,
        customProps: {},
        populators: {
          name: "deliveryChannels",
          required: true,
        },
      },
    ],
  },
];

export const configsCreateOrderReIssueSummon = [
  {
    body: [
      {
        isMandatory: true,
        key: "Order Type",
        type: "dropdown",
        label: "order type",
        disable: false,
        populators: {
          name: "genders",
          optionsKey: "name",
          error: "required ",
          mdmsConfig: {
            masterName: "OrderType",
            moduleName: "Order",
            localePrefix: "ORDER_TYPE",
          },
        },
      },
      // {
      //   isMandatory: true,
      //   key: "Document Type",
      //   type: "dropdown",
      //   label: "document type",
      //   disable: false,
      //   populators: {
      //     name: "genders",
      //     optionsKey: "name",
      //     error: "required ",
      //     mdmsConfig: {
      //       masterName: "GenderType",
      //       moduleName: "common-masters",
      //       localePrefix: "COMMON_GENDER",
      //     },
      //   },
      // },
      // {
      //   isMandatory: true,
      //   key: "Party / parties to make submission",
      //   type: "dropdown",
      //   label: "Order for document Submission",
      //   disable: false,
      //   populators: {
      //     name: "genders",
      //     optionsKey: "name",
      //     error: "required ",
      //     mdmsConfig: {
      //       masterName: "GenderType",
      //       moduleName: "common-masters",
      //       localePrefix: "COMMON_GENDER",
      //     },
      //   },
      // },
      //   {
      //
      //     label: "deadline for submission",
      //     isMandatory: false,
      //     key: "dob",
      //     type: "date",
      //     disable: false,
      //     populators: { name: "dob", error: "Required"},
      //   },

      //   {
      //     label: "Additional notes",
      //     isMandatory: true,
      //     key: "phno",
      //     type: "number",
      //     disable: false,
      //     populators: { name: "phno", error: "Required", validation: { min: 0, max: 9999999999 } },
      //   },
    ],
  },
];
