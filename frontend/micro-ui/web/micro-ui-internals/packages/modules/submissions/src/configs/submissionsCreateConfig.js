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
          error: "CORE_REQUIRED_FIELD_ERROR",
          styles: { maxWidth: "100%" },
          required: true,
          isMandatory: true,
          options: [
            {
              code: "APPLICATION",
              name: "APPLICATION",
            },
            {
              code: "DOCUMENT",
              name: "DOCUMENT",
            },
          ],
          customStyle: { display: "flex", flexDirection: "column", alignItems: "flex-start" },
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
          error: "CORE_REQUIRED_FIELD_ERROR",
          styles: { maxWidth: "100%" },
          required: true,
          isMandatory: true,
          mdmsConfig: {
            masterName: "ApplicationType",
            moduleName: "Application",
            localePrefix: "APPLICATION_TYPE",
            select:
              "(data) => {return data['Application'].ApplicationType?.filter((item)=>![`EXTENSION_SUBMISSION_DEADLINE`,`DOCUMENT`,`RE_SCHEDULE`,`CHECKOUT_REQUEST`].includes(item.type)).map((item) => {return { ...item, name: 'APPLICATION_TYPE_'+item.type };});}",
          },
          customStyle: { display: "flex", flexDirection: "column", alignItems: "flex-start" },
        },
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
        isMandatory: false,
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
        label: "DATE_OF_APPLICATION",
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
        label: "INITIAL_HEARING_DATE",
        disable: true,
        isMandatory: true,
        key: "initialHearingDate",
        schemaKeyPath: "applicationDetails.initialHearingDate",
        transformer: "date",
        type: "date",
        populators: {
          name: "initialHearingDate",
          error: "CORE_REQUIRED_FIELD_ERROR",
        },
      },
      {
        inline: true,
        label: "RESCHEDULING_REASON",
        isMandatory: true,
        key: "reschedulingReason",
        schemaKeyPath: "applicationDetails.reasonForApplication",
        transformer: "mdmsDropdown",
        type: "dropdown",
        populators: {
          name: "reschedulingReason",
          optionsKey: "name",
          error: "CORE_REQUIRED_FIELD_ERROR",
          styles: { maxWidth: "100%" },
          required: true,
          isMandatory: true,
          mdmsConfig: {
            moduleName: "Application",
            masterName: "ReschedulingReason",
            select: "(data) => {return data['Application'].ReschedulingReason?.map((item) => {return item;});}",
          },
        },
      },
      {
        inline: true,
        label: "PROPOSED_DATE",
        isMandatory: true,
        key: "changedHearingDate",
        schemaKeyPath: "applicationDetails.newHearingScheduledDate",
        transformer: "date",
        type: "date",
        populators: {
          name: "changedHearingDate",
          error: "CORE_REQUIRED_FIELD_ERROR",
          validation: {
            customValidationFn: {
              moduleName: "dristiSubmissions",
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
        inline: true,
        type: "component",
        component: "SelectCustomTextArea",
        key: "comments",
        schemaKeyPath: "applicationDetails.additionalComments",
        transformer: "customTextArea",
        isMandatory: false,
        populators: {
          inputs: [
            {
              name: "text",
              textAreaSubHeader: "COMMENTS",
              subHeaderClassName: "dristi-font-big-bold",
              placeholder: "TYPE_HERE_PLACEHOLDER",
              isOptional: true,
              type: "TextAreaComponent",
            },
          ],
          validation: {
            customValidationFn: {
              moduleName: "dristiSubmissions",
              masterName: "alphaNumericValidation",
            },
          },
        },
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
        isMandatory: false,
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
        label: "DATE_OF_APPLICATION",
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
        label: "INITIAL_HEARING_DATE",
        disable: true,
        isMandatory: true,
        schemaKeyPath: "applicationDetails.initialHearingDate",
        transformer: "date",
        key: "initialHearingDate",
        type: "date",
        populators: {
          name: "initialHearingDate",
          error: "CORE_REQUIRED_FIELD_ERROR",
        },
      },
      {
        inline: true,
        label: "RESCHEDULING_REASON",
        isMandatory: true,
        schemaKeyPath: "applicationDetails.reasonForApplication",
        transformer: "mdmsDropdown",
        key: "reschedulingReason",
        type: "dropdown",
        populators: {
          name: "reschedulingReason",
          optionsKey: "name",
          error: "CORE_REQUIRED_FIELD_ERROR",
          styles: { maxWidth: "100%" },
          required: true,
          isMandatory: true,
          mdmsConfig: {
            moduleName: "Application",
            masterName: "ReschedulingReason",
            select: "(data) => {return data['Application'].ReschedulingReason?.map((item) => {return item;});}",
          },
        },
      },
      {
        inline: true,
        label: "PROPOSED_DATE",
        isMandatory: true,
        schemaKeyPath: "applicationDetails.newHearingScheduledDate",
        transformer: "date",
        key: "changedHearingDate",
        type: "date",
        populators: {
          name: "changedHearingDate",
          error: "CORE_REQUIRED_FIELD_ERROR",
          validation: {
            customValidationFn: {
              moduleName: "dristiSubmissions",
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
        schemaKeyPath: "applicationDetails.additionalComments",
        transformer: "customTextArea",
        key: "comments",
        isMandatory: false,
        populators: {
          inputs: [
            {
              name: "text",
              textAreaSubHeader: "COMMENTS",
              subHeaderClassName: "dristi-font-big-bold",
              placeholder: "TYPE_HERE_PLACEHOLDER",
              isOptional: true,
              type: "TextAreaComponent",
            },
          ],
          validation: {
            customValidationFn: {
              moduleName: "dristiSubmissions",
              masterName: "alphaNumericValidation",
            },
          },
        },
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
        isMandatory: false,
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
        label: "DATE_OF_APPLICATION",
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
          optionsKey: "value",
          error: "CORE_REQUIRED_FIELD_ERROR",
          styles: { maxWidth: "100%" },
          required: true,
          isMandatory: true,
          mdmsConfig: {
            moduleName: "Submission",
            masterName: "DocumentType",
            localePrefix: "",
          },
        },
      },
      {
        inline: true,
        label: "SUBMISSION_DATE",
        isMandatory: true,
        disable: true,
        key: "initialSubmissionDate",
        schemaKeyPath: "applicationDetails.originalSubmissionDate",
        transformer: "date",
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
        schemaKeyPath: "applicationDetails.requestedExtensionDate",
        transformer: "date",
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
        schemaKeyPath: "applicationDetails.reasonForApplication",
        transformer: "mdmsDropdown",
        type: "dropdown",
        populators: {
          name: "extensionReason",
          optionsKey: "name",
          error: "CORE_REQUIRED_FIELD_ERROR",
          styles: { maxWidth: "100%" },
          required: true,
          isMandatory: true,
          mdmsConfig: {
            moduleName: "Application",
            masterName: "ExtensionReason",
            select: "(data) => {return data['Application'].ExtensionReason?.map((item) => {return item;});}",
          },
        },
      },
    ],
  },
  {
    body: [
      {
        inline: true,
        type: "component",
        component: "SelectCustomTextArea",
        key: "extensionBenefit",
        schemaKeyPath: "applicationDetails.benefitOfExtension",
        transformer: "customTextArea",
        isMandatory: true,
        populators: {
          inputs: [
            {
              name: "text",
              textAreaHeader: "EXTENSION_BENEFIT",
              headerClassName: "dristi-font-big-bold",
              placeholder: "TYPE_HERE_PLACEHOLDER",
              type: "TextAreaComponent",
            },
          ],
          validation: {
            customValidationFn: {
              moduleName: "dristiSubmissions",
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
        inline: true,
        type: "component",
        component: "SelectCustomTextArea",
        schemaKeyPath: "applicationDetails.additionalComments",
        transformer: "customTextArea",
        key: "comments",
        isMandatory: false,
        populators: {
          inputs: [
            {
              name: "text",
              textAreaSubHeader: "COMMENTS",
              subHeaderClassName: "dristi-font-big-bold",
              placeholder: "TYPE_HERE_PLACEHOLDER",
              isOptional: true,
              type: "TextAreaComponent",
            },
          ],
          validation: {
            customValidationFn: {
              moduleName: "dristiSubmissions",
              masterName: "alphaNumericValidation",
            },
          },
        },
      },
    ],
  },
];

export const configsDocumentSubmission = [
  {
    body: [
      {
        inline: true,
        label: "DOCUMENT_TYPE",
        isMandatory: true,
        type: "dropdown",
        key: "documentType",
        populators: {
          name: "documentType",
          optionsKey: "value",
          error: "CORE_REQUIRED_FIELD_ERROR",
          styles: { maxWidth: "100%" },
          required: true,
          isMandatory: true,
          mdmsConfig: {
            moduleName: "Submission",
            masterName: "DocumentType",
            localePrefix: "",
          },
          customStyle: { display: "flex", flexDirection: "column", alignItems: "flex-start" },
        },
      },

      {
        inline: true,
        label: "SUBMISSION_TITLE",
        isMandatory: true,
        key: "submissionTitle",
        type: "text",
        populators: {
          name: "submissionTitle",
          customStyle: { display: "flex", flexDirection: "column", alignItems: "flex-start" },
        },
      },
    ],
  },
  {
    body: [
      {
        inline: true,
        type: "component",
        component: "SelectCustomTextArea",
        key: "extensionBenefit",
        isMandatory: true,
        populators: {
          inputs: [
            {
              name: "text",
              textAreaHeader: "PURPOSE_FOR_DOCUMENT_SUBMISSION",
              placeholder: "TYPE_HERE_PLACEHOLDER",
              type: "TextAreaComponent",
              textAreaStyle: {
                fontSize: "16px",
                fontWeight: 400,
                marginBottom: 0,
              },
            },
          ],
          validation: {
            customValidationFn: {
              moduleName: "dristiSubmissions",
              masterName: "alphaNumericValidation",
            },
          },
          customStyle: { display: "flex", flexDirection: "column", alignItems: "flex-start" },
        },
      },
    ],
  },
  {
    body: [
      {
        type: "component",
        component: "SelectCustomDragDrop",
        key: "submissionDocuments",
        isMandatory: true,
        populators: {
          inputs: [
            {
              isMandatory: true,
              name: "documents",
              documentHeader: "DOCUMENT",
              documentHeaderStyle: { fontSize: "16px", fontWeight: 400, marginBottom: 0 },
              type: "DragDropComponent",
              maxFileSize: 25,
              maxFileErrorMessage: "CS_FILE_LIMIT_50_MB",
              fileTypes: ["TXT", "DOC", "PDF", "DOCX"],
              isMultipleUpload: false,
              uploadGuidelines: "UPLOAD_PDF_JPEG_50",
              headerClassName: "dristi-font-bold",
            },
          ],
          customStyle: { display: "flex", flexDirection: "column", alignItems: "flex-start" },
        },
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
        isMandatory: false,
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
        label: "DATE_OF_APPLICATION",
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
        type: "component",
        component: "AddSubmissionDocument",
        key: "submissionDocuments",
        schemaKeyPath: "applicationDetails.applicationDocuments",
        transformer: "applicationDocuments",
        inline: false,
        populators: {
          inputs: [
            {
              isMandatory: true,
              key: "documentType",
              type: "dropdown",
              label: "DOCUMENT_TYPE",
              name: "documentType",
              disable: false,
              populators: {
                name: "documentType",
                optionsKey: "name",
                required: true,
                mdmsConfig: {
                  moduleName: "Application",
                  masterName: "DocumentType",
                  select: "(data) => {return data['Application'].DocumentType?.map((item) => {return item;});}",
                },
              },
            },
            {
              label: "DOCUMENT_TITLE",
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
              label: "DOCUMENT_ATTACHMENT",
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
      {
        inline: true,
        type: "component",
        component: "SelectCustomTextArea",
        schemaKeyPath: "applicationDetails.reasonForApplication",
        transformer: "customTextArea",
        key: "reasonForApplication",
        isMandatory: true,
        populators: {
          inputs: [
            {
              name: "text",
              textAreaSubHeader: "REASON_FOR_APPLICATION",
              subHeaderClassName: "dristi-font-big-bold",
              placeholder: "TYPE_HERE_PLACEHOLDER",
              isOptional: false,
              type: "TextAreaComponent",
            },
          ],
          validation: {
            customValidationFn: {
              moduleName: "dristiSubmissions",
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
        inline: true,
        type: "component",
        component: "SelectCustomTextArea",
        schemaKeyPath: "applicationDetails.additionalComments",
        transformer: "customTextArea",
        key: "comments",
        isMandatory: false,
        populators: {
          inputs: [
            {
              name: "text",
              textAreaSubHeader: "COMMENTS",
              subHeaderClassName: "dristi-font-big-bold",
              placeholder: "TYPE_HERE_PLACEHOLDER",
              isOptional: true,
              type: "TextAreaComponent",
            },
          ],
          validation: {
            customValidationFn: {
              moduleName: "dristiSubmissions",
              masterName: "alphaNumericValidation",
            },
          },
        },
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
        isMandatory: false,
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
        label: "DATE_OF_APPLICATION",
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
        schemaKeyPath: "applicationDetails.reasonForWithdrawal",
        transformer: "mdmsDropdown",
        populators: {
          name: "reasonForWithdrawal",
          optionsKey: "name",
          error: "CORE_REQUIRED_FIELD_ERROR",
          required: true,
          isMandatory: true,
          styles: { maxWidth: "100%" },
          mdmsConfig: {
            moduleName: "Application",
            masterName: "ReasonForWithdrawal",
            select: "(data) => {return data['Application'].ReasonForWithdrawal?.map((item) => {return item;});}",
          },
        },
      },
    ],
  },
  {
    body: [
      {
        inline: true,
        type: "component",
        component: "SelectCustomTextArea",
        schemaKeyPath: "applicationDetails.additionalComments",
        transformer: "customTextArea",
        key: "comments",
        isMandatory: false,
        populators: {
          inputs: [
            {
              name: "text",
              textAreaSubHeader: "COMMENTS",
              subHeaderClassName: "dristi-font-big-bold",
              placeholder: "TYPE_HERE_PLACEHOLDER",
              isOptional: true,
              type: "TextAreaComponent",
            },
          ],
          validation: {
            customValidationFn: {
              moduleName: "dristiSubmissions",
              masterName: "alphaNumericValidation",
            },
          },
        },
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
        isMandatory: false,
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
        label: "DATE_OF_APPLICATION",
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
        schemaKeyPath: "applicationDetails.selectRequestedCourt",
        isMandatory: true,
        disable: true,
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
        schemaKeyPath: "applicationDetails.groundsForSeekingTransfer",
        populators: {
          name: "groundsForTransfer",
          error: "CS_ALPHANUMERIC_ALLOWED",
          validation: {
            customValidationFn: {
              moduleName: "dristiSubmissions",
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
        inline: true,
        type: "component",
        component: "SelectCustomTextArea",
        key: "comments",
        schemaKeyPath: "applicationDetails.additionalComments",
        transformer: "customTextArea",
        isMandatory: false,
        populators: {
          inputs: [
            {
              name: "text",
              textAreaSubHeader: "COMMENTS",
              subHeaderClassName: "dristi-font-big-bold",
              placeholder: "TYPE_HERE_PLACEHOLDER",
              isOptional: true,
              type: "TextAreaComponent",
            },
          ],
          validation: {
            customValidationFn: {
              moduleName: "dristiSubmissions",
              masterName: "alphaNumericValidation",
            },
          },
        },
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
        isMandatory: false,
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
        label: "DATE_OF_APPLICATION",
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
        inline: true,
        type: "component",
        component: "SelectCustomTextArea",
        schemaKeyPath: "applicationDetails.additionalComments",
        transformer: "customTextArea",
        key: "comments",
        isMandatory: false,
        populators: {
          inputs: [
            {
              name: "text",
              textAreaSubHeader: "COMMENTS",
              subHeaderClassName: "dristi-font-big-bold",
              placeholder: "TYPE_HERE_PLACEHOLDER",
              isOptional: true,
              type: "TextAreaComponent",
            },
          ],
          validation: {
            customValidationFn: {
              moduleName: "dristiSubmissions",
              masterName: "alphaNumericValidation",
            },
          },
        },
      },
    ],
  },
];

export const configsSurety = [
  {
    body: [
      {
        inline: true,
        label: "DATE_OF_APPLICATION",
        disable: true,
        isMandatory: true,
        key: "applicationDate",
        type: "date",
        populators: {
          name: "applicationDate",
        },
      },
      {
        type: "component",
        key: "reasonForApplication",
        isMandatory: true,
        inline: false,
        component: "SelectCustomTextArea",
        schemaKeyPath: "applicationDetails.reasonForApplication",
        transformer: "customTextArea",
        populators: {
          inputs: [
            {
              name: "text",
              textAreaSubHeader: "REASON_FOR_APPLICATION",
              subHeaderClassName: "dristi-font-big-bold",
              placeholder: "TYPE_HERE",
              type: "TextAreaComponent",
            },
          ],
          validation: {
            customValidationFn: {
              moduleName: "dristiSubmissions",
              masterName: "alphaNumericValidation",
            },
          },
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
              infoHeader: "SURETY_DOCUMENTS",
              infoText: "SURETY_DOCUMENTS_INFO_TEXT",
              infoTooltipMessage: "CS_NOTETOOLTIP_RESPONDENT_PERSONAL_DETAILS",
              type: "InfoComponent",
              linkText: "CLICK_HERE",
              modalHeading: "LIST_OF_SURETY_DOCUMENT",
              modalData: [],
            },
          ],
        },
      },
      {
        type: "component",
        component: "AddSubmissionDocument",
        key: "submissionDocuments",
        schemaKeyPath: "applicationDetails.applicationDocuments",
        transformer: "applicationDocuments",
        inline: false,
        populators: {
          inputs: [
            {
              isMandatory: true,
              key: "documentType",
              type: "dropdown",
              label: "DOCUMENT_TYPE",
              name: "documentType",
              disable: false,
              populators: {
                name: "documentType",
                optionsKey: "name",
                required: true,
                mdmsConfig: {
                  moduleName: "Application",
                  masterName: "DocumentType",
                  select: "(data) => {return data['Application'].DocumentType?.map((item) => {return item;});}",
                },
              },
            },
            {
              label: "DOCUMENT_TITLE",
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
              label: "DOCUMENT_ATTACHMENT",
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

export const configsBailBond = [
  {
    body: [
      {
        inline: true,
        label: "DATE_OF_APPLICATION",
        disable: true,
        isMandatory: true,
        key: "applicationDate",
        type: "date",
        populators: {
          name: "applicationDate",
        },
      },
      {
        type: "component",
        key: "reasonForApplication",
        isMandatory: true,
        inline: false,
        component: "SelectCustomTextArea",
        schemaKeyPath: "applicationDetails.reasonForApplication",
        transformer: "customTextArea",
        populators: {
          inputs: [
            {
              name: "text",
              textAreaSubHeader: "REASON_FOR_APPLICATION",
              subHeaderClassName: "dristi-font-big-bold",
              placeholder: "TYPE_HERE",
              type: "TextAreaComponent",
            },
          ],
          validation: {
            customValidationFn: {
              moduleName: "dristiSubmissions",
              masterName: "alphaNumericValidation",
            },
          },
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
              infoHeader: "BAIL_DOCUMENTS",
              infoText: "BAIL_DOCUMENTS_INFO_TEXT",
              infoTooltipMessage: "BAIL_DOCUMENTS_INFO_TOOLTIP_TEXT",
              type: "InfoComponent",
            },
          ],
        },
      },
      {
        type: "component",
        component: "AddSubmissionDocument",
        key: "submissionDocuments",
        schemaKeyPath: "applicationDetails.applicationDocuments",
        transformer: "applicationDocuments",
        inline: false,
        populators: {
          inputs: [
            {
              isMandatory: true,
              key: "documentType",
              type: "dropdown",
              label: "DOCUMENT_TYPE",
              name: "documentType",
              disable: false,
              populators: {
                name: "documentType",
                optionsKey: "name",
                required: true,
                mdmsConfig: {
                  moduleName: "Application",
                  masterName: "DocumentType",
                  select: "(data) => {return data['Application'].DocumentType?.map((item) => {return item;});}",
                },
              },
            },
            {
              label: "DOCUMENT_TITLE",
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
              label: "DOCUMENT_ATTACHMENT",
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

export const configsOthers = [
  {
    body: [
      {
        label: "APPLICATION_TITLE",
        isMandatory: true,
        key: "applicationTitle",
        schemaKeyPath: "applicationDetails.applicationTitle",
        type: "text",
        populators: {
          name: "applicationTitle",
          error: "CS_ALPHANUMERIC_ALLOWED",
          validation: {
            customValidationFn: {
              moduleName: "dristiSubmissions",
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
        component: "SelectCustomDragDrop",
        key: "othersDocument",
        isMandatory: true,
        populators: {
          inputs: [
            {
              isMandatory: true,
              name: "documents",
              documentHeader: "OTHERS_DOCUMENT",
              documentHeaderStyle: { fontSize: "19px", fontWeight: 700 },
              type: "DragDropComponent",
              maxFileSize: 50,
              maxFileErrorMessage: "CS_FILE_LIMIT_50_MB",
              fileTypes: ["PDF", "JPEG"],
              uploadGuidelines: "UPLOAD_PDF_JPEG_50",
              headerClassName: "dristi-font-bold",
            },
          ],
        },
      },
      {
        type: "component",
        component: "SelectCustomTextArea",
        schemaKeyPath: "applicationDetails.reasonForApplication",
        transformer: "customTextArea",
        key: "applicationDetails",
        isMandatory: true,
        populators: {
          inputs: [
            {
              name: "text",
              textAreaSubHeader: "DETAILS",
              subHeaderClassName: "dristi-font-big-bold",
              placeholder: "TYPE_HERE_PLACEHOLDER",
              type: "TextAreaComponent",
            },
          ],
          validation: {
            customValidationFn: {
              moduleName: "dristiSubmissions",
              masterName: "alphaNumericValidation",
            },
          },
        },
      },
    ],
  },
];
