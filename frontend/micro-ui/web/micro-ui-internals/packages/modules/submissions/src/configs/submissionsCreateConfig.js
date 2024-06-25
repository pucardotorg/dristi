export const submissionConfig = [
  {
    head: "CREATE_SUBMISSION",
    body: [
      {
        inline: true,
        label: "SUBMISSION_ID",
        isMandatory: false,
        key: "filingNumber",
        type: "text",
        disable: true,
        populators: { name: "filingNumber" },
      },
      {
        isMandatory: true,
        key: "submissionType",
        type: "dropdown",
        label: "SUBMISSION_TYPE",
        disable: false,
        populators: {
          name: "submissionType",
          optionsKey: "type",
          error: "required ",
          options: [{ code: "APPLICATION", name: "Application" }],
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
        key: "applicationType",
        type: "dropdown",
        label: "APPLICATION_TYPE",
        disable: false,
        populators: {
          name: "applicationType",
          optionsKey: "type",
          error: "required ",
          mdmsConfig: {
            masterName: "ApplicationType",
            moduleName: "Application",
            localePrefix: "APPLICATION_TYPE",
          },
        },
      },
    ],
  },
];

export const RE_SCHEDULE_Config = [
  {
    head: "RESCHEDULE_REQUEST",
    body: [
      {
        inline: true,
        label: "REF_ORDER_ID",
        isMandatory: false,
        key: "refOrderId",
        type: "text",
        populators: { name: "refOrderId" },
      },
      {
        inline: true,
        label: "COURT_NAME",
        isMandatory: false,
        key: "courtName",
        type: "text",
        populators: { name: "courtName", hideInForm: true },
      },
      {
        inline: true,
        label: "CASE_NAME",
        isMandatory: false,
        key: "caseName",
        type: "text",
        populators: { name: "caseName", hideInForm: true },
      },
      {
        inline: true,
        label: "CNR_NUMBER",
        isMandatory: false,
        key: "cnrNumber",
        type: "text",
        populators: { name: "cnrNumber", hideInForm: true },
      },
      {
        inline: true,
        label: "FILING_NUMBER",
        isMandatory: false,
        key: "filingNumber",
        type: "text",
        populators: { name: "filingNumber", hideInForm: true },
      },
      {
        inline: true,
        label: "APPLICATION_DATE",
        isMandatory: false,
        key: "applicationDate",
        type: "text",
        populators: { name: "applicationDate" },
      },
      {
        inline: true,
        label: "APPLICANT_NAME",
        isMandatory: false,
        key: "applicantName",
        type: "text",
        populators: { name: "applicantName", hideInForm: true },
      },
      {
        inline: true,
        label: "PARTY_TYPE",
        isMandatory: false,
        type: "dropdown",
        key: "partyType",
        populators: {
          optionsKey: "name",
          hideInForm: true,
          options: [
            {
              code: "complainant",
              name: "Complainant",
            },
            {
              code: "respondant",
              name: "Respondant",
            },
          ],
        },
      },
      {
        inline: true,
        label: "REPRESENTED_BY",
        isMandatory: false,
        key: "representedBy",
        type: "text",
        populators: { name: "representedBy", hideInForm: true },
      },
      {
        inline: true,
        label: "HEARING_DATE",
        isMandatory: false,
        key: "initialHearingDate",
        type: "date",
        populators: { name: "initialHearingDate" },
      },
      {
        inline: true,
        label: "RESCHEDULING_REASON",
        isMandatory: false,
        key: "reschedulingReason",
        type: "dropdown",
        populators: {
          optionsKey: "name",
          options: [
            {
              code: "conflict",
              name: "Conflict",
            },
            {
              code: "illness",
              name: "Illness",
            },
            {
              code: "other",
              name: "Other",
            },
          ],
        },
      },
      {
        inline: true,
        label: "PROPOSED_DATE",
        isMandatory: false,
        key: "changedHearingDate",
        type: "date",
        populators: { name: "changedHearingDate" },
      },
      {
        inline: true,
        label: "COMMENTS",
        isMandatory: false,
        key: "comments",
        type: "textarea",
        populators: { name: "comments" },
      },
    ],
  },
];

export const SUBMISSION_EXTENSION_REQUESTConfig = [
  {
    head: "SUBMISSION_EXTENSION_REQUEST",
    body: [
      {
        inline: true,
        label: "REF_ORDER_ID",
        isMandatory: false,
        key: "refOrderId",
        type: "text",
        populators: { name: "refOrderId" },
      },
      {
        inline: true,
        label: "COURT_NAME",
        isMandatory: false,
        key: "courtName",
        type: "text",
        populators: { name: "courtName", hideInForm: true },
      },
      {
        inline: true,
        label: "CASE_NAME",
        isMandatory: false,
        key: "caseName",
        type: "text",
        populators: { name: "caseName", hideInForm: true },
      },
      {
        inline: true,
        label: "CNR_NUMBER",
        isMandatory: false,
        key: "cnrNumber",
        type: "text",
        populators: { name: "cnrNumber", hideInForm: true },
      },
      {
        inline: true,
        label: "FILING_NUMBER",
        isMandatory: false,
        key: "filingNumber",
        type: "text",
        populators: { name: "filingNumber", hideInForm: true },
      },
      {
        inline: true,
        label: "APPLICATION_DATE",
        isMandatory: false,
        key: "applicationDate",
        type: "text",
        populators: { name: "applicationDate" },
      },
      {
        inline: true,
        label: "APPLICANT_NAME",
        isMandatory: false,
        key: "applicantName",
        type: "text",
        populators: { name: "applicantName", hideInForm: true },
      },
      {
        inline: true,
        label: "PARTY_TYPE",
        isMandatory: false,
        type: "dropdown",
        key: "partyType",
        populators: {
          optionsKey: "name",
          hideInForm: true,
          options: [
            {
              code: "complainant",
              name: "Complainant",
            },
            {
              code: "respondant",
              name: "Respondant",
            },
          ],
        },
      },
      {
        inline: true,
        label: "REPRESENTED_BY",
        isMandatory: false,
        key: "representedBy",
        type: "text",
        populators: { name: "representedBy", hideInForm: true },
      },
      {
        inline: true,
        label: "SUBMISSION_TYPE",
        isMandatory: false,
        type: "dropdown",
        key: "submissionType",
        populators: {
          name: "submissionType",
          optionsKey: "type",
          mdmsConfig: {
            masterName: "ApplicationType",
            moduleName: "Application",
            localePrefix: "SUBMISSION_TYPE",
          },
        },
      },
      {
        inline: true,
        label: "SUBMISSION_DATE",
        isMandatory: false,
        key: "initialSubmissionDate",
        type: "date",
        populators: { name: "initialSubmissionDate" },
      },
      {
        inline: true,
        label: "REQUESTED_DATE",
        isMandatory: false,
        key: "changedSubmissionDate",
        type: "date",
        populators: { name: "changedSubmissionDate" },
      },
      {
        inline: true,
        label: "EXTENSION_REASON",
        isMandatory: false,
        key: "extensionReason",
        type: "dropdown",
        populators: {
          optionsKey: "name",
          options: [
            {
              code: "conflict",
              name: "Conflict",
            },
            {
              code: "illness",
              name: "Illness",
            },
            {
              code: "unavailability",
              name: "Unavailability",
            },
            {
              code: "other",
              name: "Other",
            },
          ],
        },
      },
      {
        inline: true,
        label: "EXTENSION_BENEFIT",
        isMandatory: false,
        key: "extensionBenefit",
        type: "textarea",
        populators: { name: "extensionBenefit" },
      },
      {
        inline: true,
        label: "COMMENTS",
        isMandatory: false,
        key: "comments",
        type: "textarea",
        populators: { name: "comments" },
      },
    ],
  },
];

export const configs = [
  {
    head: "CREATE_SUBMISSION",
    body: [
      {
        inline: true,
        label: "SUBMISSION_ID",
        isMandatory: false,
        key: "filingNumber",
        type: "text",
        disable: true,
        populators: { name: "filingNumber" },
      },
      {
        isMandatory: true,
        key: "submissionType",
        type: "dropdown",
        label: "SUBMISSION_TYPE",
        disable: false,
        populators: {
          name: "submissionType",
          optionsKey: "type",
          error: "required ",
          options: [{ code: "APPLICATION", name: "Application" }],
        },
      },
      {
        isMandatory: true,
        key: "applicationType",
        type: "dropdown",
        label: "APPLICATION_TYPE",
        disable: false,
        populators: {
          name: "applicationType",
          optionsKey: "type",
          error: "required ",
          mdmsConfig: {
            masterName: "ApplicationType",
            moduleName: "Application",
            localePrefix: "APPLICATION_TYPE",
          },
        },
      },
      {
        isMandatory: false,
        key: "referenceId",
        type: "dropdown",
        label: "ORDER",
        disable: false,
        populators: {
          name: "ORDER",
          optionsKey: "type",
          error: "required ",
        },
      },
      {
        inline: true,
        label: "DATE_PARTY_AVAILABLE",
        isMandatory: true,
        key: "datePartyAvailable",
        type: "date",
        disable: false,
        populators: { name: "datePartyAvailable", error: "Required" },
      },
      {
        inline: true,
        label: "DELAY_REASON",
        isMandatory: true,
        key: "delayReason",
        type: "textarea",
        disable: false,
        populators: { name: "delayReason", error: " Required ", validation: { pattern: /^[A-Za-z]+$/i } },
      },
      {
        inline: true,
        label: "SUPPORTING_DOCUMENTS_OPTIONAL",
        isMandatory: false,
        name: "documentUpload",
        type: "documentUpload",
        disable: false,
        module: "SUBMISSION",
        mdmsModuleName: "pucar-ui",
        localePrefix: "SUBMISSION",
        populators: { name: "documentUpload", error: "Required", validation: { pattern: /^[A-Za-z]+$/i } },
      },
    ],
  },
];
