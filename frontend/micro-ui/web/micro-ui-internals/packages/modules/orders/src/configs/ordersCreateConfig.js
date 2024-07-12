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
          validation: {
            customValidationFn: {
              moduleName: "dristiOrders",
              masterName: "alphaNumericValidation",
            },
          },
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
        isMandatory: false, // Change this to mandatory after system filled is implememnted
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
          error: "CORE_REQUIRED_FIELD_ERROR",
          required: true,
          isMandatory: true,
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
          error: "CORE_REQUIRED_FIELD_ERROR",
          required: true,
          isMandatory: true,
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
    ],
  },
  {
    body: [
      {
        type: "component",
        component: "SelectCustomTextArea",
        key: "lawSections",
        isMandatory: true,
        populators: {
          inputs: [
            {
              name: "text",
              textAreaSubHeader: "LAW_SECTIONS",
              placeholder: "TYPE_HERE_PLACEHOLDER",
              type: "TextAreaComponent",
            },
          ],
          validation: {
            customValidationFn: {
              moduleName: "dristiOrders",
              masterName: "alphaNumericValidation",
            },
          },
        },
      },
    ],
  },
  {
    body: [
      {
        label: "RESPONSE_REQUIRED_BY",
        isMandatory: true,
        key: "responseRequiredBy",
        type: "date",
        labelChildren: "OutlinedInfoIcon",
        tooltipValue: "ONLY_CURRENT_AND_FUTURE_DATES_ARE_ALLOWED",
        populators: {
          name: "responseRequiredBy",
          validation: {
            customValidationFn: {
              moduleName: "dristiOrders",
              masterName: "minTodayDateValidation",
            },
          },
        },
      },
      {
        label: "DATE_OF_ORDER",
        isMandatory: true,
        key: "dateOfOrder",
        type: "date",
        labelChildren: "OutlinedInfoIcon",
        tooltipValue: "ONLY_CURRENT_AND_PAST_DATES_ARE_ALLOWED",
        populators: {
          name: "dateOfOrder",
          hideInForm: true,
          validation: {
            customValidationFn: {
              moduleName: "dristiOrders",
              masterName: "maxTodayDateValidation",
            },
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
  {
    body: [
      {
        type: "component",
        component: "SelectCustomTextArea",
        key: "comments",
        isMandatory: false,
        populators: {
          inputs: [
            {
              name: "text",
              textAreaSubHeader: "COMMENTS",
              placeholder: "TYPE_HERE_PLACEHOLDER",
              isOptional: true,
              type: "TextAreaComponent",
            },
          ],
          validation: {
            customValidationFn: {
              moduleName: "dristiOrders",
              masterName: "alphaNumericValidation",
            },
          },
        },
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
        labelChildren: "OutlinedInfoIcon",
        tooltipValue: "ONLY_CURRENT_AND_PAST_DATES_ARE_ALLOWED",
        populators: {
          name: "dateOfOrder",
          hideInForm: true,
          validation: {
            customValidationFn: {
              moduleName: "dristiOrders",
              masterName: "maxTodayDateValidation",
            },
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
          error: "CORE_REQUIRED_FIELD_ERROR",
          required: true,
          isMandatory: true,
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
          error: "CORE_REQUIRED_FIELD_ERROR",
          required: true,
          isMandatory: true,
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
        labelChildren: "OutlinedInfoIcon",
        tooltipValue: "ONLY_CURRENT_AND_FUTURE_DATES_ARE_ALLOWED",
        populators: {
          name: "submissionDeadline",
          validation: {
            customValidationFn: {
              moduleName: "dristiOrders",
              masterName: "minTodayDateValidation",
            },
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
        key: "additionalComments",
        isMandatory: false,
        populators: {
          inputs: [
            {
              name: "text",
              textAreaSubHeader: "ADDITIONAL_COMMENTS",
              placeholder: "TYPE_HERE_PLACEHOLDER",
              isOptional: true,
              type: "TextAreaComponent",
            },
          ],
          validation: {
            customValidationFn: {
              moduleName: "dristiOrders",
              masterName: "alphaNumericValidation",
            },
          },
        },
      },
    ],
  },
  {
    body: [
      {
        label: "IS_RESPONSE_REQUIRED",
        isMandatory: true,
        key: "isResponseRequired",
        type: "radio",
        populators: {
          name: "isResponseRequired",
          optionsKey: "name",
          title: "",
          error: "CORE_REQUIRED_FIELD_ERROR",
          required: true,
          isMandatory: true,
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
      {
        label: "RESPONDING_PARTY",
        isMandatory: true,
        key: "respondingParty",
        type: "multiselectdropdown",
        populators: {
          name: "respondingParty",
          optionsKey: "name",
          error: "CORE_REQUIRED_FIELD_ERROR",
          required: true,
          isMandatory: true,
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
        labelChildren: "OutlinedInfoIcon",
        tooltipValue: "ONLY_CURRENT_AND_FUTURE_DATES_ARE_ALLOWED",
        populators: {
          name: "responseDeadline",
          validation: {
            customValidationFn: {
              moduleName: "dristiOrders",
              masterName: "minTodayDateValidation",
            },
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
        isMandatory: false, // Change this to mandatory after system filled is implememnted
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
        label: "EXTENSION_DOCUMENT_NAME",
        isMandatory: false,
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
        isMandatory: false, // Change this to mandatory after system filled is implememnted
        key: "originalSubmissionOrderDate",
        type: "date",
        populators: {
          name: "originalSubmissionOrderDate",
        },
      },
      {
        label: "ORIGINAL_DEADLINE",
        isMandatory: false, // Change this to mandatory after system filled is implememnted
        key: "originalDeadline",
        type: "date",
        populators: {
          name: "originalDeadline",
        },
      },
      {
        label: "PROPOSED_SUBMISSION_DATE",
        isMandatory: false, // Change this to mandatory after system filled is implememnted
        key: "proposedSubmissionDate",
        type: "date",
        populators: {
          name: "proposedSubmissionDate",
        },
      },
      {
        label: "NEW_SUBMISSION_DATE",
        isMandatory: true,
        key: "newSubmissionDate",
        type: "date",
        labelChildren: "OutlinedInfoIcon",
        tooltipValue: "ONLY_CURRENT_AND_FUTURE_DATES_ARE_ALLOWED",
        populators: {
          name: "newSubmissionDate",
          validation: {
            customValidationFn: {
              moduleName: "dristiOrders",
              masterName: "minTodayDateValidation",
            },
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
  {
    body: [
      {
        type: "component",
        component: "SelectCustomTextArea",
        key: "comments",
        isMandatory: false,
        populators: {
          inputs: [
            {
              name: "text",
              textAreaSubHeader: "COMMENTS",
              placeholder: "TYPE_HERE_PLACEHOLDER",
              isOptional: true,
              type: "TextAreaComponent",
            },
          ],
          validation: {
            customValidationFn: {
              moduleName: "dristiOrders",
              masterName: "alphaNumericValidation",
            },
          },
        },
      },
    ],
  },
];

export const configsOrderTranferToADR = [
  {
    body: [
      {
        label: "REF_APPLICATION_ID",
        isMandatory: false, // Change this to mandatory after system filled is implememnted
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
          error: "CORE_REQUIRED_FIELD_ERROR",
          required: true,
          isMandatory: true,
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
  {
    body: [
      {
        type: "component",
        component: "SelectCustomTextArea",
        key: "comments",
        isMandatory: false,
        populators: {
          inputs: [
            {
              name: "text",
              textAreaSubHeader: "COMMENTS",
              placeholder: "TYPE_HERE_PLACEHOLDER",
              isOptional: true,
              type: "TextAreaComponent",
            },
          ],
          validation: {
            customValidationFn: {
              moduleName: "dristiOrders",
              masterName: "alphaNumericValidation",
            },
          },
        },
      },
    ],
  },
];

export const configsScheduleHearingDate = [
  {
    body: [
      {
        label: "REF_APPLICATION_ID",
        isMandatory: false, // Change this to mandatory after system filled is implememnted
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
        label: "HEARING_PURPOSE",
        isMandatory: true,
        key: "hearingPurpose",
        type: "dropdown",
        populators: {
          name: "hearingPurpose",
          optionsKey: "name",
          error: "CORE_REQUIRED_FIELD_ERROR",
          required: true,
          isMandatory: true,
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
        labelChildren: "OutlinedInfoIcon",
        tooltipValue: "ONLY_CURRENT_AND_FUTURE_DATES_ARE_ALLOWED",
        populators: {
          name: "hearingDate",
          validation: {
            customValidationFn: {
              moduleName: "dristiOrders",
              masterName: "minTodayDateValidation",
            },
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
      {
        label: "NAMES_OF_PARTIES_REQUIRED",
        isMandatory: true,
        key: "namesOfPartiesRequired",
        type: "dropdown",
        populators: {
          name: "namesOfPartiesRequired",
          allowMultiSelect: true,
          optionsKey: "name",
          error: "CORE_REQUIRED_FIELD_ERROR",
          required: true,
          isMandatory: true,
          options: [
            {
              code: "PARTY_1",
              name: "PARTY_1",
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
        component: "SelectCustomTextArea",
        key: "comments",
        isMandatory: false,
        populators: {
          inputs: [
            {
              name: "text",
              textAreaSubHeader: "COMMENTS",
              placeholder: "TYPE_HERE_PLACEHOLDER",
              isOptional: true,
              type: "TextAreaComponent",
            },
          ],
          validation: {
            customValidationFn: {
              moduleName: "dristiOrders",
              masterName: "alphaNumericValidation",
            },
          },
        },
      },
    ],
  },
];

export const configsRejectRescheduleHeadingDate = [
  {
    body: [
      {
        label: "REF_APPLICATION_ID",
        isMandatory: false,
        key: "refApplicationId",
        type: "text",
        populators: { name: "refApplicationId" },
      },
      {
        label: "ORIGINAL_HEARING_DATE",
        isMandatory: false, // Change this to mandatory after system filled is implememnted
        key: "originalHearingDate",
        type: "date",
        populators: {
          name: "originalHearingDate",
        },
      },
    ],
  },
  {
    body: [
      {
        type: "component",
        component: "SelectCustomTextArea",
        key: "comments",
        isMandatory: false,
        populators: {
          inputs: [
            {
              name: "text",
              textAreaSubHeader: "COMMENTS",
              placeholder: "TYPE_HERE_PLACEHOLDER",
              isOptional: true,
              type: "TextAreaComponent",
            },
          ],
          validation: {
            customValidationFn: {
              moduleName: "dristiOrders",
              masterName: "alphaNumericValidation",
            },
          },
        },
      },
    ],
  },
];

export const configsRescheduleHearingDate = [
  {
    body: [
      {
        label: "REF_APPLICATION_ID",
        isMandatory: false, // Change this to mandatory after system filled is implememnted
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
          hideInForm: true,
        },
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
        isMandatory: false, // Change this to mandatory after system filled is implememnted
        key: "originalHearingDate",
        type: "date",
        populators: {
          name: "originalHearingDate",
        },
      },
      {
        label: "NEW_HEARING_DATE",
        isMandatory: true,
        key: "newHearingDate",
        type: "date",
        labelChildren: "OutlinedInfoIcon",
        tooltipValue: "ONLY_CURRENT_AND_FUTURE_DATES_ARE_ALLOWED",
        populators: {
          name: "newHearingDate",
          validation: {
            customValidationFn: {
              moduleName: "dristiOrders",
              masterName: "minTodayDateValidation",
            },
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
  {
    body: [
      {
        type: "component",
        component: "SelectCustomTextArea",
        key: "comments",
        isMandatory: false,
        populators: {
          inputs: [
            {
              name: "text",
              textAreaSubHeader: "COMMENTS",
              placeholder: "TYPE_HERE_PLACEHOLDER",
              isOptional: true,
              type: "TextAreaComponent",
            },
          ],
          validation: {
            customValidationFn: {
              moduleName: "dristiOrders",
              masterName: "alphaNumericValidation",
            },
          },
        },
      },
    ],
  },
];

export const configsAcceptSubmission = [
  {
    body: [
      {
        label: "SUBMISSION_ID",
        isMandatory: true,
        key: "submissionId",
        type: "dropdown",
        populators: {
          name: "settlementMechanism",
          optionsKey: "name",
          error: "CORE_REQUIRED_FIELD_ERROR",
          required: true,
          isMandatory: true,
          options: [
            {
              code: "0001",
              name: "0001",
            },
            {
              code: "0002",
              name: "0002",
            },
            {
              code: "0003",
              name: "0003",
            },
          ],
        },
      },
    ],
  },
];

export const configRejectSubmission = [
  {
    body: [
      {
        label: "SUBMISSION_ID",
        isMandatory: true,
        key: "submissionId",
        type: "dropdown",
        populators: {
          name: "submissionId",
          optionsKey: "name",
          error: "CORE_REQUIRED_FIELD_ERROR",
          required: true,
          isMandatory: true,
          options: [
            {
              code: "0001",
              name: "0001",
            },
            {
              code: "0002",
              name: "0002",
            },
            {
              code: "0003",
              name: "0003",
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
        component: "SelectCustomTextArea",
        key: "comments",
        isMandatory: false,
        populators: {
          inputs: [
            {
              name: "text",
              textAreaSubHeader: "REASON_FOR_REJECTION_SUBMISSION",
              placeholder: "TYPE_HERE_PLACEHOLDER",
              isOptional: true,
              type: "TextAreaComponent",
            },
          ],
          validation: {
            customValidationFn: {
              moduleName: "dristiOrders",
              masterName: "alphaNumericValidation",
            },
          },
        },
      },
    ],
  },
];
export const configsVoluntarySubmissionStatus = [
  {
    body: [
      {
        label: "REF_APPLICATION_ID",
        isMandatory: false, // Change this to mandatory after system filled is implememnted
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
    ],
  },
  {
    body: [
      {
        type: "component",
        component: "SelectCustomTextArea",
        key: "comments",
        isMandatory: false,
        populators: {
          inputs: [
            {
              name: "text",
              textAreaSubHeader: "COMMENTS",
              placeholder: "TYPE_HERE_PLACEHOLDER",
              isOptional: true,
              type: "TextAreaComponent",
            },
          ],
          validation: {
            customValidationFn: {
              moduleName: "dristiOrders",
              masterName: "alphaNumericValidation",
            },
          },
        },
      },
    ],
  },
  {
    body: [
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
        isMandatory: false, // Change this to mandatory after system filled is implememnted
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
        populators: {
          name: "transferSeekedTo",
          error: "CS_ALPHANUMERIC_ALLOWED",
          validation: {
            customValidationFn: {
              moduleName: "dristiOrders",
              masterName: "alphaNumericInputTextValidation",
            },
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
        key: "grounds",
        isMandatory: true,
        populators: {
          inputs: [
            {
              name: "text",
              textAreaSubHeader: "GROUNDS",
              placeholder: "TYPE_HERE_PLACEHOLDER",
              type: "TextAreaComponent",
            },
          ],
          validation: {
            customValidationFn: {
              moduleName: "dristiOrders",
              masterName: "alphaNumericValidation",
            },
          },
        },
      },
    ],
  },
  {
    body: [
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
        populators: {
          name: "caseTransferredTo",
          error: "CS_ALPHANUMERIC_ALLOWED",
          validation: {
            customValidationFn: {
              moduleName: "dristiOrders",
              masterName: "alphaNumericInputTextValidation",
            },
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
        key: "comments",
        isMandatory: false,
        populators: {
          inputs: [
            {
              name: "text",
              textAreaSubHeader: "COMMENTS",
              placeholder: "TYPE_HERE_PLACEHOLDER",
              isOptional: true,
              type: "TextAreaComponent",
            },
          ],
          validation: {
            customValidationFn: {
              moduleName: "dristiOrders",
              masterName: "alphaNumericValidation",
            },
          },
        },
      },
    ],
  },
  {
    body: [
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
        isMandatory: false, // Change this to mandatory after system filled is implememnted
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
        labelChildren: "OutlinedInfoIcon",
        tooltipValue: "ONLY_CURRENT_AND_PAST_DATES_ARE_ALLOWED",
        populators: {
          name: "dateOfOrder",
          validation: {
            customValidationFn: {
              moduleName: "dristiOrders",
              masterName: "maxTodayDateValidation",
            },
          },
          hideInForm: true,
        },
      },
      {
        label: "SETTLEMENT_AGREEMENT_DATE",
        isMandatory: true,
        key: "settlementAgreementDate",
        type: "date",
        populators: {
          name: "settlementAgreementDate",
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
          error: "CORE_REQUIRED_FIELD_ERROR",
          required: true,
          isMandatory: true,
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
        type: "radio",
        populators: {
          name: "settlementImplemented",
          optionsKey: "name",
          title: "",
          error: "CORE_REQUIRED_FIELD_ERROR",
          required: true,
          isMandatory: true,
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
    ],
  },
  {
    body: [
      {
        type: "component",
        component: "SelectCustomTextArea",
        key: "comments",
        isMandatory: false,
        populators: {
          inputs: [
            {
              name: "text",
              textAreaSubHeader: "COMMENTS",
              placeholder: "TYPE_HERE_PLACEHOLDER",
              isOptional: true,
              type: "TextAreaComponent",
            },
          ],
          validation: {
            customValidationFn: {
              moduleName: "dristiOrders",
              masterName: "alphaNumericValidation",
            },
          },
        },
      },
    ],
  },
  {
    body: [
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
        isMandatory: false, // Change this to mandatory after system filled is implememnted
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
    ],
  },
  {
    body: [
      {
        type: "component",
        component: "SelectCustomTextArea",
        key: "comments",
        isMandatory: false,
        populators: {
          inputs: [
            {
              name: "text",
              textAreaSubHeader: "COMMENTS",
              placeholder: "TYPE_HERE_PLACEHOLDER",
              isOptional: true,
              type: "TextAreaComponent",
            },
          ],
          validation: {
            customValidationFn: {
              moduleName: "dristiOrders",
              masterName: "alphaNumericValidation",
            },
          },
        },
      },
    ],
  },
  {
    body: [
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
        isMandatory: false, // Change this to mandatory after system filled is implememnted
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
        populators: {
          name: "reasonForWarrant",
          error: "CS_ALPHANUMERIC_ALLOWED",
          validation: {
            customValidationFn: {
              moduleName: "dristiOrders",
              masterName: "alphaNumericInputTextValidation",
            },
          },
        },
      },
      {
        label: "HEARING_DATE",
        isMandatory: true,
        key: "hearingDate",
        type: "date",
        populators: { name: "hearingDate", hideInForm: true },
      },
    ],
  },
  {
    body: [
      {
        type: "component",
        component: "SelectCustomTextArea",
        key: "comments",
        isMandatory: false,
        populators: {
          inputs: [
            {
              name: "text",
              textAreaSubHeader: "COMMENTS",
              placeholder: "TYPE_HERE_PLACEHOLDER",
              isOptional: true,
              type: "TextAreaComponent",
            },
          ],
          validation: {
            customValidationFn: {
              moduleName: "dristiOrders",
              masterName: "alphaNumericValidation",
            },
          },
        },
      },
    ],
  },
  {
    body: [
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
export const configsCaseWithdrawal = [
  {
    body: [
      {
        label: "REF_APPLICATION_ID",
        isMandatory: false, // Change this to mandatory after system filled is implememnted
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
        label: "APPLICATION_ON_BEHALF_OF",
        isMandatory: false, // Change this to mandatory after system filled is implememnted
        key: "applicationOnBehalfOf",
        type: "text",
        populators: { name: "applicationOnBehalfOf" },
      },
      {
        label: "PARTY_TYPE",
        isMandatory: false, // Change this to mandatory after system filled is implememnted
        key: "partyType",
        type: "text",
        populators: { name: "partyType" },
      },
      {
        label: "REASON_FOR_WITHDRAWAL",
        isMandatory: false, // Change this to mandatory after system filled is implememnted
        key: "reasonForWarrant",
        type: "text",
        populators: { name: "reasonForWithdrawal" },
      },
      {
        label: "APPLICATION_STATUS",
        isMandatory: false, // Change this to mandatory after system filled is implememnted
        key: "applicationStatus",
        type: "text",
        populators: { name: "applicationStatus" },
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
        populators: {
          name: "orderTitle",
          error: "MAX_15_WORDS_ARE_ALLOWED",
          validation: {
            customValidationFn: {
              moduleName: "dristiOrders",
              masterName: "orderTitleValidation",
            },
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
        key: "otherDetails",
        isMandatory: true,
        populators: {
          inputs: [
            {
              name: "text",
              textAreaHeader: "CS_DETAILS",
              placeholder: "TYPE_HERE_PLACEHOLDER",
              type: "TextAreaComponent",
            },
          ],
          validation: {
            customValidationFn: {
              moduleName: "dristiOrders",
              masterName: "alphaNumericValidation",
            },
          },
        },
      },
    ],
  },
];

export const configsBail = [
  {
    body: [
      {
        label: "REF_APPLICATION_ID",
        isMandatory: false, // Change this to mandatory after system filled is implememnted
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
        type: "dropdown",
        populators: {
          name: "bailOf",
          optionsKey: "name",
          error: "CORE_REQUIRED_FIELD_ERROR",
          required: true,
          isMandatory: true,
          options: [
            {
              code: "NAME_OF_PARTY",
              name: "Name of Party",
            },
          ],
        },
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
        isMandatory: false, // Change this to mandatory after system filled is implememnted
        key: "bailType",
        type: "dropdown",
        populators: {
          name: "bailType",
          optionsKey: "type",
          error: "CORE_REQUIRED_FIELD_ERROR",
          required: true,
          isMandatory: true,
          mdmsConfig: {
            masterName: "BailType",
            moduleName: "Order",
            localePrefix: "BAIL_TYPE"
          },
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
        populators: {
          name: "otherConditions",
          error: "CS_ALPHANUMERIC_ALLOWED",
          validation: {
            customValidationFn: {
              moduleName: "dristiOrders",
              masterName: "alphaNumericInputTextValidation",
            },
          },
        },
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

export const configsJudgement = [
  {
    body: [
      {
        label: "CASE_NUMBER",
        isMandatory: false, // Change this to mandatory after system filled is implememnted
        key: "caseNumber",
        type: "text",
        populators: { name: "caseNumber" },
      },
      {
        label: "DATE_OF_JUDGEMENT",
        isMandatory: false, // Change this to mandatory after system filled is implememnted
        key: "dateOfJudgement",
        type: "date",
        populators: { name: "dateOfJudgement" },
      },
      {
        label: "NAME_OF_JUDGE",
        isMandatory: false, // Change this to mandatory after system filled is implememnted
        key: "nameOfJudge",
        type: "text",
        populators: { name: "nameOfJudge" },
      },
      {
        label: "NAME_OF_COURT",
        isMandatory: false, // Change this to mandatory after system filled is implememnted
        key: "nameOfCourt",
        type: "text",
        populators: { name: "nameOfCourt" },
      },
      {
        label: "DESCRIPTION_OF_ACCUSED",
        isMandatory: false,
        key: "nameofRespondant",
        type: "text",
        populators: {
          name: "nameofRespondent",
          error: "CS_ALPHANUMERIC_ALLOWED",
          validation: {
            customValidationFn: {
              moduleName: "dristiOrders",
              masterName: "alphaNumericInputTextValidation",
            },
          },
        },
      },
      {
        label: "DESCRIPTION_OF_ACCUSED_RESIDENCE",
        isMandatory: false, // Change this to mandatory after system filled is implememnted
        key: "addressRespondant",
        type: "text",
        populators: { name: "addressRespondant" },
      },
      {
        label: "DATE_OF_OCCURENCE",
        isMandatory: false, // Change this to mandatory after system filled is implememnted
        key: "dateChequeReturnMemo",
        type: "date",
        populators: { name: "dateChequeReturnMemo" },
      },
      {
        label: "DATE_COMPLAINT",
        isMandatory: false, // Change this to mandatory after system filled is implememnted
        key: "dateFiling",
        type: "date",
        populators: { name: "dateFiling" },
      },
      {
        label: "DATE_OF_APPREHENSION",
        isMandatory: false, // Change this to mandatory after system filled is implememnted
        key: "dateApprehension",
        type: "date",
        populators: { name: "dateApprehension" },
      },
      {
        label: "DATE_OF_RELEASE_ON_BAIL",
        isMandatory: false, // Change this to mandatory after system filled is implememnted
        key: "dateofReleaseOnBail",
        type: "date",
        populators: { name: "dateofReleaseOnBail" },
      },
      {
        label: "DATE_OF_COMMENCEMENT_TRIAL",
        isMandatory: false, // Change this to mandatory after system filled is implememnted
        key: "dateofCommencementTrial",
        type: "date",
        populators: { name: "dateofCommencementTrial" },
      },
      {
        label: "DATE_OF_CLOSE_TRIAL",
        isMandatory: false, // Change this to mandatory after system filled is implememnted
        key: "dateofCloseTrial",
        type: "date",
        populators: { name: "dateofCloseTrial" },
      },
      {
        label: "DATE_OF_SENTENCE",
        isMandatory: false, // Change this to mandatory after system filled is implememnted
        key: "dateofSentence",
        type: "date",
        populators: { name: "dateofSentence" },
      },
      {
        label: "NAME_COMPLAINANT",
        isMandatory: false, // Change this to mandatory after system filled is implememnted
        key: "nameofComplainant",
        type: "text",
        populators: { name: "nameofComplainant" },
      },
      {
        label: "NAME_COMPLAINANT_ADVOCATE",
        isMandatory: false, // Change this to mandatory after system filled is implememnted
        key: "nameofComplainantAdvocate",
        type: "text",
        populators: { name: "nameofComplainantAdvocate" },
      },
      {
        label: "NAME_RESPONDANT_ADVOCATE",
        isMandatory: false, // Change this to mandatory after system filled is implememnted
        key: "nameofRespondantAdvocate",
        type: "text",
        populators: { name: "nameofRespondantAdvocate" },
      },
      {
        label: "OFFENSE",
        isMandatory: false, // Change this to mandatory after system filled is implememnted
        key: "offense",
        type: "text",
        populators: { name: "offense" },
      },
      {
        type: "radio",
        key: "plea",
        label: "PLEA",
        isMandatory: true,
        populators: {
          label: "PLEA",
          type: "radioButton",
          name: "plea",
          optionsKey: "name",
          error: "CORE_REQUIRED_FIELD_ERROR",
          required: true,
          isMandatory: true,
          options: [
            {
              code: "GUILTY",
              name: "GUILTY",
              isEnabled: true,
            },
            {
              code: "NOTGUILTY",
              name: "NOTGUILTY",
              isEnabled: true,
            },
          ],
        },
      },
      {
        type: "dropdown",
        key: "findings",
        label: "FINDING",
        isMandatory: true,
        populators: {
          label: "PLEA",
          type: "radioButton",
          name: "findings",
          optionsKey: "name",
          error: "CORE_REQUIRED_FIELD_ERROR",
          required: true,
          isMandatory: true,
          options: [
            {
              code: "GUILTY",
              name: "GUILTY",
              isEnabled: true,
            },
            {
              code: "NOTGUILTY",
              name: "NOTGUILTY",
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
        type: "component",
        component: "SelectCustomTextArea",
        key: "sentence",
        isMandatory: true,
        populators: {
          inputs: [
            {
              name: "text",
              textAreaSubHeader: "SENTENCE",
              placeholder: "TYPE_HERE_PLACEHOLDER",
              type: "TextAreaComponent",
            },
          ],
          validation: {
            customValidationFn: {
              moduleName: "dristiOrders",
              masterName: "alphaNumericValidation",
            },
          },
        },
      },
    ],
  },
];
