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
      label: "Pending",
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
                label: "Order Type",
                isMandatory: false,
                key: "orderType",
                type: "dropdown",
                disable: false,
                populators: {
                  name: "orderType",
                  optionsKey: "name",
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
                label: "E-Sign Status",
                isMandatory: false,
                key: "applicationStatus",
                type: "dropdown",
                disable: false,
                populators: {
                  name: "applicationStatus",
                  optionsKey: "name",
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
                label: "Search E-process",
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
                label: "E-process ID",
                jsonPath: "taskNumber",
              },
              {
                label: "Status",
                jsonPath: "documentStatus",
                additionalCustomization: true,
              },
              {
                label: "Case Name & ID",
                jsonPath: "filingNumber",
                additionalCustomization: true,
              },
              {
                label: "Order Type",
                jsonPath: "taskType",
                additionalCustomization: true,
              },
              {
                label: "Delivery Channel",
                jsonPath: "taskDetails",
                additionalCustomization: true,
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
                label: "Order Type",
                isMandatory: false,
                key: "orderType",
                type: "dropdown",
                disable: false,
                populators: {
                  name: "orderType",
                  optionsKey: "name",
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
                label: "Status",
                isMandatory: false,
                key: "completeStatus",
                type: "dropdown",
                disable: false,
                populators: {
                  name: "completeStatus",
                  optionsKey: "name",
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
                label: "Search E-process",
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
                label: "E-process ID",
                jsonPath: "taskNumber",
              },
              {
                label: "Status",
                jsonPath: "status",
                additionalCustomization: true,
              },
              {
                label: "Case Name & ID",
                jsonPath: "filingNumber",
                additionalCustomization: true,
              },
              {
                label: "Order Type",
                jsonPath: "taskType",
                additionalCustomization: true,
              },
              {
                label: "Delivery Channel",
                jsonPath: "taskDetails",
                additionalCustomization: true,
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
