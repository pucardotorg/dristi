export const submissionTypeConfig = [
  {
    body: [
      {
        isMandatory: true,
        key: "submissionType",
        type: "dropdown",
        label: "SUBMISSION_TYPE",
        disable: true,
        populators: {
          name: "submissionType",
          optionsKey: "name",
          error: "required ",
          options: [
            {
              code: "APPLICATION_TYPE",
              name: "APPLICATION_TYPE",
            },
          ],
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
          optionsKey: "name",
          error: "required ",
          mdmsConfig: {
            masterName: "ApplicationType",
            moduleName: "Application",
            localePrefix: "APPLICATION_TYPE",
            select:
              "(data) => {return data['Application'].ApplicationType?.map((item) => {return { ...item, name: 'APPLICATION_TYPE_'+item.type };});}",
          },
        },
      },
    ],
  },
];

export const configs = [
  {
    head: "CREATE_SUBMISSION",
    body: [
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
          mdmsConfig: {
            //Used application type for timebeing since Submission type MDMS data is not defined
            masterName: "ApplicationType",
            moduleName: "Application",
            localePrefix: "SUBMISSION_TYPE",
          },
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
        disable: true,
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
        populators: {
          name: "datePartyAvailable",
          error: "Required",
        },
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

export const configsRescheduleRequest = [
  {
    body: [
      {
        inline: true,
        label: "REF_ORDER_ID",
        disable: true,
        key: "refOrderId",
        type: "text",
        populators: { name: "refOrderId", hideInForm: true },
      },
      {
        inline: true,
        label: "COURT_NAME",
        isMandatory: true,
        key: "courtName",
        type: "text",
        populators: { name: "courtName", hideInForm: true },
      },
      {
        inline: true,
        label: "CASE_NAME",
        isMandatory: true,
        key: "caseName",
        type: "text",
        populators: { name: "caseName", hideInForm: true },
      },
      {
        inline: true,
        label: "CNR_NUMBER",
        isMandatory: true,
        key: "cnrNumber",
        type: "text",
        populators: { name: "cnrNumber", hideInForm: true },
      },
      {
        inline: true,
        label: "FILING_NUMBER",
        isMandatory: true,
        key: "filingNumber",
        type: "text",
        populators: { name: "filingNumber", hideInForm: true },
      },
      {
        inline: true,
        label: "APPLICATION_DATE",
        disable: true,
        isMandatory: true,
        key: "applicationDate",
        type: "date",
        populators: { name: "applicationDate" },
      },
      {
        inline: true,
        label: "APPLICANT_NAME",
        isMandatory: true,
        key: "applicantName",
        type: "text",
        populators: { name: "applicantName", hideInForm: true },
      },
      {
        inline: true,
        label: "PARTY_TYPE",
        isMandatory: true,
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
        isMandatory: true,
        key: "representedBy",
        type: "text",
        populators: { name: "representedBy", hideInForm: true },
      },
      {
        inline: true,
        label: "HEARING_DATE",
        isMandatory: true,
        key: "initialHearingDate",
        type: "date",
        populators: {
          name: "initialHearingDate",
        },
      },
      {
        inline: true,
        label: "RESCHEDULING_REASON",
        isMandatory: true,
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
        isMandatory: true,
        key: "changedHearingDate",
        type: "date",
        populators: {
          name: "changedHearingDate",
        },
      },
      {
        inline: true,
        label: "COMMENTS",
        isMandatory: true,
        key: "comments",
        type: "textarea",
        populators: { name: "comments" },
      },
    ],
  },
];

export const configsCheckoutRequest = [
  {
    body: [
      {
        inline: true,
        label: "REF_ORDER_ID",
        disable: true,
        key: "refOrderId",
        type: "text",
        populators: { name: "refOrderId" },
      },
      {
        inline: true,
        label: "COURT_NAME",
        isMandatory: true,
        key: "courtName",
        type: "text",
        populators: { name: "courtName", hideInForm: true },
      },
      {
        inline: true,
        label: "CASE_NAME",
        isMandatory: true,
        key: "caseName",
        type: "text",
        populators: { name: "caseName", hideInForm: true },
      },
      {
        inline: true,
        label: "CNR_NUMBER",
        isMandatory: true,
        key: "cnrNumber",
        type: "text",
        populators: { name: "cnrNumber", hideInForm: true },
      },
      {
        inline: true,
        label: "FILING_NUMBER",
        isMandatory: true,
        key: "filingNumber",
        type: "text",
        populators: { name: "filingNumber", hideInForm: true },
      },
      {
        inline: true,
        label: "APPLICATION_DATE",
        disable: true,
        isMandatory: true,
        key: "applicationDate",
        type: "date",
        populators: { name: "applicationDate" },
      },
      {
        inline: true,
        label: "APPLICANT_NAME",
        isMandatory: true,
        key: "applicantName",
        type: "text",
        populators: { name: "applicantName", hideInForm: true },
      },
      {
        inline: true,
        label: "PARTY_TYPE",
        isMandatory: true,
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
        isMandatory: true,
        key: "representedBy",
        type: "text",
        populators: { name: "representedBy", hideInForm: true },
      },
      {
        inline: true,
        label: "HEARING_DATE",
        isMandatory: true,
        key: "initialHearingDate",
        type: "date",
        populators: {
          name: "initialHearingDate",
        },
      },
      {
        inline: true,
        label: "RESCHEDULING_REASON",
        isMandatory: true,
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
        isMandatory: true,
        key: "changedHearingDate",
        type: "date",
        populators: {
          name: "changedHearingDate",
        },
      },
      {
        inline: true,
        label: "COMMENTS",
        isMandatory: true,
        key: "comments",
        type: "textarea",
        populators: { name: "comments" },
      },
    ],
  },
];

export const configsExtensionSubmissionDeadline = [
  {
    body: [
      {
        inline: true,
        label: "REF_ORDER_ID",
        disable: true,
        key: "refOrderId",
        type: "text",
        populators: { name: "refOrderId" },
      },
      {
        inline: true,
        label: "COURT_NAME",
        isMandatory: true,
        key: "courtName",
        type: "text",
        populators: { name: "courtName", hideInForm: true },
      },
      {
        inline: true,
        label: "CASE_NAME",
        isMandatory: true,
        key: "caseName",
        type: "text",
        populators: { name: "caseName", hideInForm: true },
      },
      {
        inline: true,
        label: "CNR_NUMBER",
        isMandatory: true,
        key: "cnrNumber",
        type: "text",
        populators: { name: "cnrNumber", hideInForm: true },
      },
      {
        inline: true,
        label: "FILING_NUMBER",
        isMandatory: true,
        key: "filingNumber",
        type: "text",
        populators: { name: "filingNumber", hideInForm: true },
      },
      {
        inline: true,
        label: "APPLICATION_DATE",
        disable: true,
        isMandatory: true,
        key: "applicationDate",
        type: "date",
        populators: {
          name: "applicationDate",
        },
      },
      {
        inline: true,
        label: "APPLICANT_NAME",
        isMandatory: true,
        key: "applicantName",
        type: "text",
        populators: { name: "applicantName", hideInForm: true },
      },
      {
        inline: true,
        label: "PARTY_TYPE",
        isMandatory: true,
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
        isMandatory: true,
        key: "representedBy",
        type: "text",
        populators: { name: "representedBy", hideInForm: true },
      },
      {
        inline: true,
        label: "DOCUMENT_TYPE",
        isMandatory: true,
        disable: true,
        type: "dropdown",
        key: "documentType",
        populators: {
          name: "documentType",
          optionsKey: "name",
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
        inline: true,
        label: "SUBMISSION_DATE",
        isMandatory: true,
        disable: true,
        key: "initialSubmissionDate",
        type: "date",
        populators: {
          name: "initialSubmissionDate",
        },
      },
      {
        inline: true,
        label: "REQUESTED_DATE",
        isMandatory: true,
        key: "changedSubmissionDate",
        type: "date",
        populators: {
          name: "changedSubmissionDate",
        },
      },
      {
        inline: true,
        label: "EXTENSION_REASON",
        isMandatory: true,
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
        isMandatory: true,
        key: "extensionBenefit",
        type: "textarea",
        populators: { name: "extensionBenefit" },
      },
      {
        inline: true,
        label: "COMMENTS",
        isMandatory: true,
        key: "comments",
        type: "textarea",
        populators: { name: "comments" },
      },
    ],
  },
];

export const configsProductionOfDocuments = [
  {
    body: [
      {
        inline: true,
        label: "REF_ORDER_ID",
        disable: true,
        key: "refOrderId",
        type: "text",
        populators: { name: "refOrderId" },
      },
      {
        inline: true,
        label: "COURT_NAME",
        isMandatory: true,
        key: "courtName",
        type: "text",
        populators: { name: "courtName", hideInForm: true },
      },
      {
        inline: true,
        label: "CASE_NAME",
        isMandatory: true,
        key: "caseName",
        type: "text",
        populators: { name: "caseName", hideInForm: true },
      },
      {
        inline: true,
        label: "CNR_NUMBER",
        isMandatory: true,
        key: "cnrNumber",
        type: "text",
        populators: { name: "cnrNumber", hideInForm: true },
      },
      {
        inline: true,
        label: "FILING_NUMBER",
        isMandatory: true,
        key: "filingNumber",
        type: "text",
        populators: { name: "filingNumber", hideInForm: true },
      },
      {
        inline: true,
        label: "APPLICATION_DATE",
        disable: true,
        isMandatory: true,
        key: "applicationDate",
        type: "date",
        populators: {
          name: "applicationDate",
        },
      },
      {
        inline: true,
        label: "APPLICANT_NAME",
        isMandatory: true,
        key: "applicantName",
        type: "text",
        populators: { name: "applicantName", hideInForm: true },
      },
      {
        inline: true,
        label: "PARTY_TYPE",
        isMandatory: true,
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
        isMandatory: true,
        key: "representedBy",
        type: "text",
        populators: { name: "representedBy", hideInForm: true },
      },
    ],
  },
  {
    body: [
      {
        type: "component",
        component: "SelectCustomDragDrop",
        key: "listOfProducedDocuments",
        isMandatory: true,
        populators: {
          inputs: [
            {
              // isMandatory: true,
              // isOptional: "CS_IS_OPTIONAL",
              isMandatory: true,
              name: "documents",
              // documentSubText: "PRODUCED_DOCUMENTS",
              documentHeader: "PRODUCED_DOCUMENTS",
              type: "DragDropComponent",
              maxFileSize: 50,
              maxFileErrorMessage: "CS_FILE_LIMIT_50_MB",
              fileTypes: ["PDF", "JPEG"],
              isMultipleUpload: true,
              uploadGuidelines: "UPLOAD_PDF_50",
              headerClassName: "dristi-font-bold",
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
        key: "reasonForDocumentsSubmission",
        isMandatory: true,
        populators: {
          inputs: [
            {
              // isMandatory: true,
              // isOptional: "CS_IS_OPTIONAL",
              isMandatory: true,
              name: "documents",
              // documentSubText: "PRODUCED_DOCUMENTS",
              documentHeader: "REASON_FOR_DOCUMENT_SUBMISSION",
              type: "DragDropComponent",
              maxFileSize: 50,
              maxFileErrorMessage: "CS_FILE_LIMIT_50_MB",
              fileTypes: ["PDF", "JPEG"],
              isMultipleUpload: true,
              uploadGuidelines: "UPLOAD_PDF_50",
              headerClassName: "dristi-font-bold",
            },
          ],
        },
      },
    ],
  },
  {
    body: [
      {
        inline: true,
        label: "COMMENTS",
        isMandatory: true,
        key: "comments",
        type: "textarea",
        populators: { name: "comments" },
      },
    ],
  },
];

export const configsCaseWithdrawal = [
  {
    body: [
      {
        inline: true,
        label: "REF_ORDER_ID",
        disable: true,
        key: "refOrderId",
        type: "text",
        populators: { name: "refOrderId" },
      },
      {
        inline: true,
        label: "COURT_NAME",
        isMandatory: true,
        key: "courtName",
        type: "text",
        populators: { name: "courtName", hideInForm: true },
      },
      {
        inline: true,
        label: "CASE_NAME",
        isMandatory: true,
        key: "caseName",
        type: "text",
        populators: { name: "caseName", hideInForm: true },
      },
      {
        inline: true,
        label: "CNR_NUMBER",
        isMandatory: true,
        key: "cnrNumber",
        type: "text",
        populators: { name: "cnrNumber", hideInForm: true },
      },
      {
        inline: true,
        label: "FILING_NUMBER",
        isMandatory: true,
        key: "filingNumber",
        type: "text",
        populators: { name: "filingNumber", hideInForm: true },
      },
      {
        inline: true,
        label: "APPLICATION_DATE",
        disable: true,
        isMandatory: true,
        key: "applicationDate",
        type: "date",
        populators: {
          name: "applicationDate",
        },
      },
      {
        inline: true,
        label: "APPLICANT_NAME",
        isMandatory: true,
        key: "applicantName",
        type: "text",
        populators: { name: "applicantName", hideInForm: true },
      },
      {
        inline: true,
        label: "PARTY_TYPE",
        isMandatory: true,
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
        isMandatory: true,
        key: "representedBy",
        type: "text",
        populators: { name: "representedBy", hideInForm: true },
      },
      {
        inline: true,
        label: "REASON_FOR_WITHDRAWAL",
        isMandatory: true,
        type: "dropdown",
        key: "reasonForWithdrawal",
        populators: {
          name: "reasonForWithdrawal",
          optionsKey: "name",
          error: "required",
          options: [
            {
              code: "REASON_1",
              name: "REASON_1",
            },
            {
              code: "REASON_2",
              name: "REASON_2",
            },
          ],
        },
      },
      {
        inline: true,
        label: "COMMENTS",
        isMandatory: true,
        key: "comments",
        type: "textarea",
        populators: { name: "comments" },
      },
    ],
  },
];

export const configsCaseTransfer = [
  {
    body: [
      {
        inline: true,
        label: "REF_ORDER_ID",
        disable: true,
        key: "refOrderId",
        type: "text",
        populators: { name: "refOrderId" },
      },
      {
        inline: true,
        label: "COURT_NAME",
        isMandatory: true,
        key: "courtName",
        type: "text",
        populators: { name: "courtName", hideInForm: true },
      },
      {
        inline: true,
        label: "CASE_NAME",
        isMandatory: true,
        key: "caseName",
        type: "text",
        populators: { name: "caseName", hideInForm: true },
      },
      {
        inline: true,
        label: "CNR_NUMBER",
        isMandatory: true,
        key: "cnrNumber",
        type: "text",
        populators: { name: "cnrNumber", hideInForm: true },
      },
      {
        inline: true,
        label: "FILING_NUMBER",
        isMandatory: true,
        key: "filingNumber",
        type: "text",
        populators: { name: "filingNumber", hideInForm: true },
      },
      {
        inline: true,
        label: "APPLICATION_DATE",
        disable: true,
        isMandatory: true,
        key: "applicationDate",
        type: "date",
        populators: {
          name: "applicationDate",
        },
      },
      {
        inline: true,
        label: "APPLICANT_NAME",
        isMandatory: true,
        key: "applicantName",
        type: "text",
        populators: { name: "applicantName", hideInForm: true },
      },
      {
        inline: true,
        label: "PARTY_TYPE",
        isMandatory: true,
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
        isMandatory: true,
        key: "representedBy",
        type: "text",
        populators: { name: "representedBy", hideInForm: true },
      },
      {
        inline: true,
        label: "REQUESTED_COURT",
        isMandatory: true,
        key: "requestedCourt",
        type: "text",
        populators: { name: "requestedCourt" },
      },
      {
        inline: true,
        label: "GROUNDS_FOR_SEEKING_TRANSFER",
        isMandatory: true,
        key: "groundsForTransfer",
        type: "text",
        populators: { name: "groundsForTransfer" },
      },
      {
        inline: true,
        label: "COMMENTS",
        isMandatory: true,
        key: "comments",
        type: "textarea",
        populators: { name: "comments" },
      },
    ],
  },
];

export const configsSettlement = [
  {
    body: [
      {
        inline: true,
        label: "REF_ORDER_ID",
        disable: true,
        key: "refOrderId",
        type: "text",
        populators: { name: "refOrderId" },
      },
      {
        inline: true,
        label: "COURT_NAME",
        isMandatory: true,
        key: "courtName",
        type: "text",
        populators: { name: "courtName", hideInForm: true },
      },
      {
        inline: true,
        label: "CASE_NAME",
        isMandatory: true,
        key: "caseName",
        type: "text",
        populators: { name: "caseName", hideInForm: true },
      },
      {
        inline: true,
        label: "CNR_NUMBER",
        isMandatory: true,
        key: "cnrNumber",
        type: "text",
        populators: { name: "cnrNumber", hideInForm: true },
      },
      {
        inline: true,
        label: "FILING_NUMBER",
        isMandatory: true,
        key: "filingNumber",
        type: "text",
        populators: { name: "filingNumber", hideInForm: true },
      },
      {
        inline: true,
        label: "APPLICATION_DATE",
        disable: true,
        isMandatory: true,
        key: "applicationDate",
        type: "date",
        populators: {
          name: "applicationDate",
        },
      },
      {
        inline: true,
        label: "APPLICANT_NAME",
        isMandatory: true,
        key: "applicantName",
        type: "text",
        populators: { name: "applicantName", hideInForm: true },
      },
      {
        inline: true,
        label: "PARTY_TYPE",
        isMandatory: true,
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
        isMandatory: true,
        key: "representedBy",
        type: "text",
        populators: { name: "representedBy", hideInForm: true },
      },
      {
        inline: true,
        label: "REASON_FOR_SETTLEMENT",
        isMandatory: true,
        key: "reasonForSettlement",
        type: "text",
        populators: { name: "reasonForSettlement" },
      },
      {
        inline: true,
        label: "COMMENTS",
        isMandatory: true,
        key: "comments",
        type: "textarea",
        populators: { name: "comments" },
      },
    ],
  },
];

export const configsBailBond = [
  {
    body: [
      {
        inline: true,
        label: "REF_ORDER_ID",
        disable: true,
        key: "refOrderId",
        type: "text",
        populators: { name: "refOrderId" },
      },
      {
        inline: true,
        label: "COURT_NAME",
        isMandatory: true,
        key: "courtName",
        type: "text",
        populators: { name: "courtName", hideInForm: true },
      },
      {
        inline: true,
        label: "CASE_NAME",
        isMandatory: true,
        key: "caseName",
        type: "text",
        populators: { name: "caseName", hideInForm: true },
      },
      {
        inline: true,
        label: "CNR_NUMBER",
        isMandatory: true,
        key: "cnrNumber",
        type: "text",
        populators: { name: "cnrNumber", hideInForm: true },
      },
      {
        inline: true,
        label: "FILING_NUMBER",
        isMandatory: true,
        key: "filingNumber",
        type: "text",
        populators: { name: "filingNumber", hideInForm: true },
      },
      {
        inline: true,
        label: "APPLICATION_DATE",
        disable: true,
        isMandatory: true,
        key: "applicationDate",
        type: "date",
        populators: {
          name: "applicationDate",
        },
      },
      {
        inline: true,
        label: "APPLICANT_NAME",
        isMandatory: true,
        key: "applicantName",
        type: "text",
        populators: { name: "applicantName", hideInForm: true },
      },
      {
        inline: true,
        label: "PARTY_TYPE",
        isMandatory: true,
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
        isMandatory: true,
        key: "representedBy",
        type: "text",
        populators: { name: "representedBy", hideInForm: true },
      },
      {
        inline: true,
        label: "PLACE_OF_DETENTION",
        isMandatory: true,
        key: "placeOfDetention",
        type: "text",
        populators: { name: "placeOfDetention" },
      },
      {
        inline: true,
        label: "BAIL_AMOUNT",
        isMandatory: true,
        key: "bailAmount",
        type: "number",
        populators: { name: "bailAmount" },
      },
    ],
  },
];

export const configsSurety = [
  {
    body: [
      {
        inline: true,
        label: "REF_ORDER_ID",
        disable: true,
        key: "refOrderId",
        type: "text",
        populators: { name: "refOrderId" },
      },
      {
        inline: true,
        label: "COURT_NAME",
        isMandatory: true,
        key: "courtName",
        type: "text",
        populators: { name: "courtName", hideInForm: true },
      },
      {
        inline: true,
        label: "CASE_NAME",
        isMandatory: true,
        key: "caseName",
        type: "text",
        populators: { name: "caseName", hideInForm: true },
      },
      {
        inline: true,
        label: "CNR_NUMBER",
        isMandatory: true,
        key: "cnrNumber",
        type: "text",
        populators: { name: "cnrNumber", hideInForm: true },
      },
      {
        inline: true,
        label: "FILING_NUMBER",
        isMandatory: true,
        key: "filingNumber",
        type: "text",
        populators: { name: "filingNumber", hideInForm: true },
      },
      {
        inline: true,
        label: "APPLICATION_DATE",
        disable: true,
        isMandatory: true,
        key: "applicationDate",
        type: "date",
        populators: {
          name: "applicationDate",
        },
      },
      {
        inline: true,
        label: "APPLICANT_NAME",
        isMandatory: true,
        key: "applicantName",
        type: "text",
        populators: { name: "applicantName", hideInForm: true },
      },
      {
        inline: true,
        label: "PARTY_TYPE",
        isMandatory: true,
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
        isMandatory: true,
        key: "representedBy",
        type: "text",
        populators: { name: "representedBy", hideInForm: true },
      },
      {
        inline: true,
        label: "NAME_OF_SURETY",
        isMandatory: true,
        key: "nameOfSurety",
        type: "text",
        populators: { name: "nameOfSurety" },
      },
      {
        inline: true,
        label: "BAIL_AMOUNT",
        isMandatory: true,
        key: "bailAmount",
        type: "text",
        populators: { name: "bailAmount", validation: {} },
      },
    ],
  },
];

export const configsBail = [
  {
    body: [
      {
        inline: true,
        label: "REF_ORDER_ID",
        disable: true,
        key: "refOrderId",
        type: "text",
        populators: { name: "refOrderId" },
      },
      {
        inline: true,
        label: "COURT_NAME",
        isMandatory: true,
        key: "courtName",
        type: "text",
        populators: { name: "courtName", hideInForm: true },
      },
      {
        inline: true,
        label: "CASE_NAME",
        isMandatory: true,
        key: "caseName",
        type: "text",
        populators: { name: "caseName", hideInForm: true },
      },
      {
        inline: true,
        label: "CNR_NUMBER",
        isMandatory: true,
        key: "cnrNumber",
        type: "text",
        populators: { name: "cnrNumber", hideInForm: true },
      },
      {
        inline: true,
        label: "FILING_NUMBER",
        isMandatory: true,
        key: "filingNumber",
        type: "text",
        populators: { name: "filingNumber", hideInForm: true },
      },
      {
        inline: true,
        label: "APPLICATION_DATE",
        disable: true,
        isMandatory: true,
        key: "applicationDate",
        type: "date",
        populators: {
          name: "applicationDate",
        },
      },
      {
        inline: true,
        label: "APPLICANT_NAME",
        isMandatory: true,
        key: "applicantName",
        type: "text",
        populators: { name: "applicantName", hideInForm: true },
      },
      {
        inline: true,
        label: "PARTY_TYPE",
        isMandatory: true,
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
        isMandatory: true,
        key: "representedBy",
        type: "text",
        populators: { name: "representedBy", hideInForm: true },
      },
    ],
  },
  {
    body: [
      {
        type: "component",
        component: "SelectCustomDragDrop",
        key: "documentsListForBail",
        isMandatory: true,
        populators: {
          inputs: [
            {
              isMandatory: true,
              name: "documents",
              // documentSubText: "PRODUCED_DOCUMENTS",
              documentHeader: "LIST_OF_DOCUMENTS",
              type: "DragDropComponent",
              maxFileSize: 50,
              maxFileErrorMessage: "CS_FILE_LIMIT_50_MB",
              fileTypes: ["PDF", "JPEG"],
              isMultipleUpload: true,
              uploadGuidelines: "UPLOAD_PDF_50",
              headerClassName: "dristi-font-bold",
            },
          ],
        },
      },
    ],
  },
  {
    body: [
      {
        inline: true,
        label: "REASON_FOR_BAIL",
        isMandatory: true,
        key: "reasonForBail",
        type: "textarea",
        populators: { name: "reasonForBail" },
      },
      {
        inline: true,
        label: "COMMENTS",
        isMandatory: true,
        key: "comments",
        type: "textarea",
        populators: { name: "comments" },
      },
    ],
  },
];

export const configsOthers = [
  {
    body: [
      {
        label: "APPLICATION_TITLE",
        isMandatory: false,
        key: "applicationTitle",
        type: "text",
        populators: { name: "applicationTitle" },
      },
      {
        label: "DETAILS",
        isMandatory: false,
        key: "applicationDetails",
        type: "textarea",
        populators: { name: "applicationDetails" },
      },
    ],
  },
];
