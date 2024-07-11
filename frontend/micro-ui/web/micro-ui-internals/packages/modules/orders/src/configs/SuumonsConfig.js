const defaultSearchValues = {
  taskType: "",
  status: "",
  taskNumber: "",
};

export const SummonsTabsConfig = {
  tenantId: "pg",
  moduleName: "commonCampaignUiConfig",
  showTab: true,
  SummonsTabsConfig: [
    {
      label: "Pending",
      type: "search",
      apiDetails: {
        serviceName: "/task/v1/search",
        requestParam: {
          tenantId: Digit.ULBService.getCurrentTenantId(),
        },
        requestBody: {
          criteria: {},
        },
        masterName: "commonUiConfig",
        moduleName: "SearchIndividualConfig",
        minParametersForSearchForm: 0,
        tableFormJsonPath: "requestParam",
        filterFormJsonPath: "requestBody.criteria",
        searchFormJsonPath: "requestBody.criteria",
      },
      sections: {
        search: {
          uiConfig: {
            formClassName: "custom-both-clear-search",
            primaryLabel: "ES_COMMON_SEARCH",
            secondaryLabel: "ES_COMMON_CLEAR_SEARCH",
            minReqFields: 0,
            defaultValues: defaultSearchValues,
            fields: [
              {
                label: "Order Type",
                isMandatory: false,
                key: "taskType",
                type: "dropdown",
                disable: false,
                populators: {
                  name: "taskType",
                  options: ["Summon", "Warrant"],
                  optionsCustomStyle: {
                    overflowX: "hidden",
                  },
                },
              },
              {
                label: "E-Sign Status",
                isMandatory: false,
                key: "status",
                type: "dropdown",
                disable: false,
                populators: {
                  name: "status",
                  options: ["Signed", "Sign Pending"],
                  optionsCustomStyle: {
                    overflowX: "hidden",
                  },
                },
              },
              {
                label: "Seach E-process or Case ID",
                isMandatory: false,
                type: "text",
                key: "taskNumber",
                disable: false,
                populators: {
                  name: "taskNumber",
                },
              },
            ],
          },

          show: true,
        },
        searchResult: {
          tenantId: Digit.ULBService.getCurrentTenantId(),
          uiConfig: {
            columns: [
              {
                label: "E-process ID",
                jsonPath: "taskNumber",
              },
              {
                label: "Status",
                jsonPath: "status",
              },
              {
                label: "Case Name & ID",
                jsonPath: "filingNumber",
              },
              {
                label: "Order Type",
                jsonPath: "taskType",
              },
              {
                label: "Delivery Channel",
                jsonPath: "deliveryChannel",
              },
              {
                label: "Issued",
                jsonPath: "issued",
              },
            ],
            enableColumnSort: true,
            resultsJsonPath: "list",
          },
          show: true,
        },
      },
    },
    {
      label: "Sent",
      type: "search",
      apiDetails: {
        serviceName: "/task/v1/search",
        requestParam: {
          tenantId: Digit.ULBService.getCurrentTenantId(),
        },
        requestBody: {
          criteria: {},
        },
        masterName: "commonUiConfig",
        moduleName: "SearchIndividualConfig",
        minParametersForSearchForm: 0,
        tableFormJsonPath: "requestParam",
        filterFormJsonPath: "requestBody.criteria",
        searchFormJsonPath: "requestBody.criteria",
      },
      sections: {
        search: {
          uiConfig: {
            formClassName: "custom-both-clear-search",
            primaryLabel: "ES_COMMON_SEARCH",
            secondaryLabel: "ES_COMMON_CLEAR_SEARCH",
            minReqFields: 0,
            defaultValues: defaultSearchValues,
            fields: [
              {
                label: "Order Type",
                isMandatory: false,
                key: "taskType",
                type: "dropdown",
                disable: false,
                populators: {
                  name: "taskType",
                  options: ["Summon", "Warrant"],
                  optionsCustomStyle: {
                    overflowX: "hidden",
                  },
                },
              },
              {
                label: "E-Sign Status",
                isMandatory: false,
                key: "status",
                type: "dropdown",
                disable: false,
                populators: {
                  name: "status",
                  options: ["Signed", "Sign Pending"],
                  optionsCustomStyle: {
                    overflowX: "hidden",
                  },
                },
              },
              {
                label: "Seach E-process or Case ID",
                isMandatory: false,
                type: "text",
                key: "taskNumber",
                disable: false,
                populators: {
                  name: "taskNumber",
                },
              },
            ],
          },

          show: true,
        },
        searchResult: {
          tenantId: Digit.ULBService.getCurrentTenantId(),
          uiConfig: {
            columns: [
              {
                label: "E-process ID",
                jsonPath: "taskNumber",
              },
              {
                label: "Status",
                jsonPath: "status",
              },
              {
                label: "Case Name & ID",
                jsonPath: "filingNumber",
              },
              {
                label: "Order Type",
                jsonPath: "taskType",
              },
              {
                label: "Delivery Channel",
                jsonPath: "deliveryChannel",
              },
              {
                label: "Issued",
                jsonPath: "issued",
              },
            ],
            enableColumnSort: true,
            resultsJsonPath: "list",
          },
          show: true,
        },
      },
    },
  ],
};
