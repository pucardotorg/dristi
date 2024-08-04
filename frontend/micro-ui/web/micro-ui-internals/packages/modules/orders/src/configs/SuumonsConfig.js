const defaultSearchValues = {
  taskType: "",
  status: "",
  taskNumber: "",
};

export const SummonsTabsConfig = {
  tenantId: "pg",
  moduleName: "reviewSummonWarrantNotice",
  showTab: true,
  SummonsTabsConfig: [
    {
      label: "Pending",
      type: "search",
      apiDetails: {
        serviceName: "/task/v1/search",
        requestParam: {
          tenantId: Digit.ULBService.getCurrentTenantId(),
          limit: 10,
          offset: 0,
        },
        requestBody: {
          apiOperation: "SEARCH",
          criteria: {},
        },
        masterName: "commonUiConfig",
        moduleName: "reviewSummonWarrantNotice",
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
                type: "component",
                component: "CustomSortComponent",
                isMandatory: false,
                disable: false,
                name: "Issue Date",
                key: "sortCaseListByDate",
                sortBy: "createdDate",
                showIcon: true,
                icon: "UpDownArrowIcon",
                populators: {},
              },
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
                  styles: {
                    maxWidth: "200px",
                    minWidth: "150px",
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
                  options: ["Signed", "Sign Pending", "Sent", "Delivered", "Not Delivered"],
                  optionsCustomStyle: {
                    overflowX: "hidden",
                  },
                  styles: {
                    maxWidth: "200px",
                    minWidth: "150px",
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
                jsonPath: "createdDate",
                additionalCustomization: true,
              },
            ],
            enableColumnSort: true,
            resultsJsonPath: "list",
          },
          show: true,
        },
      },
      additionalDetails: {
        sortBy: "sortCaseListByDate",
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
        moduleName: "reviewSummonWarrantNotice",
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
                type: "component",
                component: "CustomSortComponent",
                isMandatory: false,
                disable: false,
                name: "Issue Date",
                key: "sortCaseListByDate",
                sortBy: "createdDate",
                showIcon: true,
                icon: "UpDownArrowIcon",
                populators: {},
              },
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
                  styles: {
                    maxWidth: "200px",
                    minWidth: "150px",
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
                  options: ["Signed", "Sign Pending", "Sent", "Delivered", "Not Delivered"],
                  optionsCustomStyle: {
                    overflowX: "hidden",
                  },
                  styles: {
                    maxWidth: "200px",
                    minWidth: "150px",
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
                jsonPath: "createdDate",
                additionalCustomization: true,
              },
            ],
            enableColumnSort: true,
            resultsJsonPath: "list",
          },
          show: true,
        },
      },
      additionalDetails: {
        sortBy: "sortCaseListByDate",
      },
    },
  ],
};
