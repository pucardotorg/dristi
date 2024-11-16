const defaultSearchValues = {
  searchText: "",
  applicationStatus: "",
  orderType: null,
};

export const SummonsTabsConfig = {
  tenantId: "pg",
  moduleName: "reviewSummonWarrantNotice",
  showTab: true,
  SummonsTabsConfig: [
    {
      label: "PENDING",
      type: "search",
      apiDetails: {
        serviceName: "/task/v1/table/search",
        requestParam: {
          tenantId: Digit.ULBService.getCurrentTenantId(),
          limit: 10,
          offset: 0,
        },
        requestBody: {
          apiOperation: "SEARCH",
          criteria: {
            completeStatus: ["ISSUE_SUMMON", "ISSUE_NOTICE", "ISSUE_WARRANT"], // have to do changes
          },
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
                label: "ORDER_TYPE",
                isMandatory: false,
                key: "orderType",
                type: "dropdown",
                disable: false,
                populators: {
                  name: "orderType",
                  optionsKey: "code",
                  mdmsConfig: {
                    moduleName: "Order",
                    masterName: "CourtStaffOrderType",
                    select: "(data) => {return data['Order'].CourtStaffOrderType?.map((item) => {return item;});}",
                  },
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
                label: "E_SIGN_STATUS",
                isMandatory: false,
                key: "applicationStatus",
                type: "dropdown",
                disable: false,
                populators: {
                  name: "applicationStatus",
                  optionsKey: "code",
                  mdmsConfig: {
                    moduleName: "Order",
                    masterName: "ESignPendingStatus",
                    select: "(data) => {return data['Order'].ESignPendingStatus?.map((item) => {return item;});}",
                  },
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
                label: "SEARCH_E_PROCESS",
                isMandatory: false,
                type: "text",
                key: "searchText", // seach text
                disable: false,
                populators: {
                  name: "searchText",
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
                label: "E_PROCESS_ID",
                jsonPath: "taskNumber",
              },
              {
                label: "STATUS",
                jsonPath: "documentStatus",
                additionalCustomization: true,
              },
              {
                label: "CASE_NAME_ID",
                jsonPath: "filingNumber",
                additionalCustomization: true,
              },
              {
                label: "ORDER_TYPE",
                jsonPath: "taskType",
                additionalCustomization: true,
              },
              {
                label: "DELIEVERY_CHANNEL",
                jsonPath: "taskDetails",
                additionalCustomization: true,
              },
              {
                label: "ISSUED",
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
      label: "SENT",
      type: "search",
      apiDetails: {
        serviceName: "/task/v1/table/search",
        requestParam: {
          tenantId: Digit.ULBService.getCurrentTenantId(),
        },
        requestBody: {
          criteria: {
            completeStatus: [],
          },
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
                label: "ORDER_TYPE",
                isMandatory: false,
                key: "orderType",
                type: "dropdown",
                disable: false,
                populators: {
                  name: "orderType",
                  optionsKey: "code",
                  mdmsConfig: {
                    moduleName: "Order",
                    masterName: "CourtStaffOrderType",
                    select: "(data) => {return data['Order'].CourtStaffOrderType?.map((item) => {return item;});}",
                  },
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
                label: "STATUS",
                isMandatory: false,
                key: "completeStatus",
                type: "dropdown",
                disable: false,
                populators: {
                  name: "completeStatus",
                  optionsKey: "code",
                  mdmsConfig: {
                    moduleName: "Order",
                    masterName: "SentStatus",
                    select: "(data) => {return data['Order'].SentStatus?.map((item) => {return item;});}",
                  },
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
                label: "SEARCH_E_PROCESS",
                isMandatory: false,
                type: "text",
                key: "searchText", // seach text
                disable: false,
                populators: {
                  name: "searchText",
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
                label: "E_PROCESS_ID",
                jsonPath: "taskNumber",
              },
              {
                label: "STATUS",
                jsonPath: "status",
                additionalCustomization: true,
              },
              {
                label: "CASE_NAME_ID",
                jsonPath: "filingNumber",
                additionalCustomization: true,
              },
              {
                label: "ORDER_TYPE",
                jsonPath: "taskType",
                additionalCustomization: true,
              },
              {
                label: "DELIEVERY_CHANNEL",
                jsonPath: "taskDetails",
                additionalCustomization: true,
              },
              {
                label: "ISSUED",
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
