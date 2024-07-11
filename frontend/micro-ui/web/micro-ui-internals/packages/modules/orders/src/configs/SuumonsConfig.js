const defaultSearchValues = {
  id: "",
  individualId: "",
  individualName: "",
};

export const statusOptions = ["Sign Pending", "Signed", "Sent", "Delivered", "Not Delivered"];

export const SummonsTabsConfig = {
  tenantId: "pg",
  moduleName: "commonCampaignUiConfig",
  showTab: true, // setting true will enable tab screen
  SummonsTabsConfig: [
    {
      // customHookName: "orders.useSummonsNoticeWarrant",
      label: "Pending",
      type: "search",
      apiDetails: {
        serviceName: "/task/v1/search",
        requestParam: {
          tenantId: Digit.ULBService.getCurrentTenantId(),
        },
        requestBody: {
          apiOperation: "SEARCH",
          Individual: {
            tenantId: Digit.ULBService.getCurrentTenantId(),
          },
          criteria: {},
        },
        masterName: "commonUiConfig",
        moduleName: "SearchIndividualConfig",
        minParametersForSearchForm: 0,
        tableFormJsonPath: "requestParam",
        filterFormJsonPath: "requestBody.Individual",
        searchFormJsonPath: "requestBody.Individual",
      },
      sections: {
        search: {
          uiConfig: {
            formClassName: "custom-both-clear-search",
            primaryLabel: "ES_COMMON_SEARCH",
            secondaryLabel: "ES_COMMON_CLEAR_SEARCH",
            minReqFields: 0,
            defaultValues: defaultSearchValues, // Set default values for search fields
            fields: [
              {
                type: "component",
                component: "CustomSortComponent",
                isMandatory: false,
                disable: false,
                name: "Issue Date",
                key: "sortCaseListByDate",
                sortBy: "createdtime",
                // ascText: "First",
                // descText: "Last",
                showAdditionalText: true,
                showIcon: true,
                icon: "UpDownArrowIcon",
                populators: {},
              },
              {
                label: "Order Type",
                isMandatory: false,
                key: "orderType",
                type: "dropdown",
                populators: {
                  tyles: { width: "150px" },
                  name: "orderType",
                  optionsKey: "orderType",
                  // showIcon: true,
                  // isSearchable: true,
                  options: [
                    {
                      orderType: "Summon",
                    },
                    {
                      orderType: "Warrant",
                    },
                  ],
                },
              },
              {
                label: "E-Sign Status",
                isMandatory: false,
                key: "eSignStatus",
                type: "dropdown",
                disable: false,
                populators: {
                  name: "eSignStatus",
                  optionsKey: "eSignStatus",
                  // showIcon: true,
                  // isSearchable: true,
                  options: [
                    {
                      eSignStatus: "Signed",
                    },
                    {
                      eSignStatus: "Sign Pending",
                    },
                  ],
                  // styles: {
                  //   maxWidth: "250px",
                  //   minWidth: "200px",
                  // },
                  // optionsCustomStyle: {
                  //   overflowX: "hidden",
                  // },
                },
              },
              {
                label: "Seach E-process or Case ID",
                isMandatory: false,
                type: "text",
                key: "eprocess",
                disable: false,
                populators: {
                  name: "id",
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
      customHookName: "",
      apiDetails: {
        serviceName: "/individual/v1/_search",
        requestParam: {
          tenantId: Digit.ULBService.getCurrentTenantId(),
        },
        requestBody: {
          apiOperation: "SEARCH",
          Individual: {
            tenantId: Digit.ULBService.getCurrentTenantId(),
          },
        },
        masterName: "commonUiConfig",
        moduleName: "SearchIndividualConfig",
        minParametersForSearchForm: 0,
        tableFormJsonPath: "requestParam",
        filterFormJsonPath: "requestBody.Individual",
        searchFormJsonPath: "requestBody.Individual",
      },
      sections: {
        search: {
          uiConfig: {
            formClassName: "custom-both-clear-search",
            primaryLabel: "ES_COMMON_SEARCH",
            secondaryLabel: "ES_COMMON_CLEAR_SEARCH",
            minReqFields: 0,
            defaultValues: defaultSearchValues, // Set default values for search fields
            fields: [
              {
                label: "E Process Id",
                isMandatory: false,
                type: "text",
                key: "eprocess",
                disable: false,
                populators: {
                  name: "id",
                },
              },
              {
                label: "Case Name",
                isMandatory: false,
                key: "individualName",
                type: "text",
                populators: {
                  name: "individualName",
                  error: "Required",
                  validation: { pattern: /^[A-Za-z]+$/i },
                },
              },
              {
                label: "Case ID",
                isMandatory: false,
                type: "text",
                key: "IndividualId",
                disable: false,
                populators: {
                  name: "individualId",
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
                jsonPath: "id",
              },
              {
                label: "Status",
                jsonPath: "status",
              },
              {
                label: "Case Name",
                jsonPath: "name.givenName",
              },
              {
                label: "Case ID",
                jsonPath: "individualId",
              },
              {
                label: "Order Type",
                jsonPath: "address.type",
              },
              {
                label: "Delivery Channel",
                jsonPath: "vcLink",
              },
              {
                label: "Issued",
                jsonPath: "isActive",
              },
            ],
            enableColumnSort: true,
            resultsJsonPath: "Individual",
          },
          show: true,
        },
      },
    },
  ],
};
