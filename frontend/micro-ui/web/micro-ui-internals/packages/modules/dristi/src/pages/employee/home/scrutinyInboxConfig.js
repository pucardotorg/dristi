export const scrutinyInboxConfig = {
  label: "ES_COMMON_INBOX",
  type: "inbox",
  apiDetails: {
    serviceName: "/case/v1/_search",
    requestParam: {},
    requestBody: {
      criteria: [
        {
          defaultValues: true,
          status: ["UNDER_SCRUTINY"],
          filingNumber: "",
        },
      ],
    },
    minParametersForSearchForm: 1,
    masterName: "commonUiConfig",
    moduleName: "scrutinyInboxConfig",
    searchFormJsonPath: "requestBody.inbox.moduleSearchCriteria",
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
          tenantId: "pg",
        },
        fields: [
          {
            label: "CS_CASE_NO",
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
      label: "CS_YOUR_CASE",
      children: {},
      show: true,
    },
    searchResult: {
      label: "",
      uiConfig: {
        columns: [
          {
            label: "Case Name",
            jsonPath: "caseTitle",
          },
          {
            label: "Stage",
            jsonPath: "",
            additionalCustomization: true,
          },
          {
            label: "Case ID",
            jsonPath: "filingNumber",
          },
          {
            label: "Case Type",
            jsonPath: "",
            additionalCustomization: true,
          },
          {
            label: "Days Since Filing",
            jsonPath: "filingDate",
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
