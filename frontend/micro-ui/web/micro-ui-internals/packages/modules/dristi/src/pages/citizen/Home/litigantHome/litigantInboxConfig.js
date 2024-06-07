export const litigantInboxConfig = {
  label: "ES_COMMON_INBOX",
  type: "inbox",
  apiDetails: {
    serviceName: "/case/case/v1/_search",
    requestParam: {},
    requestBody: {
      tenantId: "pg",
      criteria: [{}],
    },
    minParametersForSearchForm: 1,
    masterName: "commonUiConfig",
    moduleName: "registrationRequestsConfig",
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
          applicationNumber: "",
          isActive: false,
          tenantId: "pg",
        },
        fields: [
          {
            label: "Filing No",
            type: "text",
            isMandatory: false,
            disable: false,
            populators: {
              name: "applicationNumber",
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
            label: "Case Name",
            jsonPath: "",
          },
          {
            label: "Stage",
            jsonPath: "",
          },
          {
            label: "Case ID",
            jsonPath: "caseNumber",
          },
          {
            label: "Case Type",
            jsonPath: "",
          },
          {
            label: "Days Since Filing",
            jsonPath: "filingDate",
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
};
