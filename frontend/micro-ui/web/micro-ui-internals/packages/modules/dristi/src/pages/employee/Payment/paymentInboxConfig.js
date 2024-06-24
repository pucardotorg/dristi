export const paymentInboxConfig = {
  label: "ES_COMMON_INBOX",
  type: "inbox",
  apiDetails: {
    serviceName: "/case/case/v1/_search",
    requestParam: {},
    requestBody: {
      criteria: [
        {
          defaultValues: true,
          status: "PAYMENT_PENDING",
          filingNumber: "",
        },
      ],
    },
    minParametersForSearchForm: 1,
    masterName: "commonUiConfig",
    moduleName: "paymentInboxConfig",
    searchFormJsonPath: "requestBody.criteria[0]",
    tableFormJsonPath: "requestBody.inbox",
  },
  sections: {
    search: {
      uiConfig: {
        headerStyle: null,
        type: "registration-requests-table-search",
        primaryLabel: "ES_COMMON_SEARCH",
        secondaryLabel: "ES_COMMON_CLEAR_SEARCH",
        minReqFields: 1,
        defaultValues: {
          filingNumber: "",
          isActive: false,
        },
        fields: [
          {
            label: "CS_FILING_NO",
            type: "text",
            isMandatory: false,
            disable: false,
            populators: {
              name: "filingNumber",
              error: "BR_PATTERN_ERR_MSG",
              validation: {
                pattern: {},
                minlength: 2,
              },
            },
          },
        ],
      },
      children: {},
      show: true,
    },
    searchResult: {
      label: "",
      uiConfig: {
        columns: [
          {
            label: "Case ID",
            jsonPath: "filingNumber",
            additionalCustomization: true,
          },
          {
            label: "Case Name",
            jsonPath: "caseTitle",
          },
          {
            label: "Case Type",
            jsonPath: "caseType",
            additionalCustomization: true,
          },
          {
            label: "Stage",
            jsonPath: "",
            additionalCustomization: true,
          },

          {
            label: "Amount Due",
            jsonPath: "amountDue",
            additionalCustomization: true,
          },
          {
            label: "Action",
            jsonPath: "id",
            additionalCustomization: true,
          },
        ],
        enableGlobalSearch: false,
        enableColumnSort: true,
        resultsJsonPath: "criteria[0].responseList",
      },
      children: {},
      show: true,
    },
  },
  additionalSections: {},
  additionalDetails: "filingNumber",
};
