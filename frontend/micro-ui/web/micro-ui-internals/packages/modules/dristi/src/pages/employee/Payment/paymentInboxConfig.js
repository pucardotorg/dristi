export const paymentTabInboxConfig = {
  tenantId: "pg",
  moduleName: "paymentInboxConfig",
  showTab: true,
  TabSearchConfig: [
    {
      label: "Completed",
      type: "search",
      apiDetails: {
        serviceName: "/inbox/v2/index/_search",
        requestParam: {},
        requestBody: {
          inbox: {
            processSearchCriteria: {
              businessService: ["billing"],
              moduleName: "Billing service",
            },
            moduleSearchCriteria: {
              billStatus: "PAID",
            },
            limit: 10,
            offset: 0,
          },
        },
        minParametersForSearchForm: 0,
        masterName: "commonUiConfig",
        moduleName: "paymentInboxConfig",
        searchFormJsonPath: "requestBody.inbox.moduleSearchCriteria",
        filterFormJsonPath: "requestBody.inbox.moduleSearchCriteria",
        tableFormJsonPath: "requestBody.inbox",
      },
      sections: {
        search: {
          uiConfig: {
            type: "registration-requests-table-search",
            primaryLabel: "ES_COMMON_SEARCH",
            secondaryLabel: "ES_COMMON_CLEAR_SEARCH",
            minReqFields: 0,
            defaultValues: {
              caseTitleFilingNumber: "",
            },
            fields: [
              {
                type: "component",
                component: "CustomSortComponent",
                isMandatory: false,
                disable: false,
                name: "Sort by",
                key: "sortCaseListByDate",
                sortBy: "createdtime",
                ascText: "Newest to Oldest",
                descText: "Oldest to Newest",
                showAdditionalText: true,
                showIcon: true,
                icon: "ArrowDownIcon",
                populators: {},
              },
              {
                label: "Stage",
                isMandatory: false,
                key: "stage",
                type: "dropdown",
                disable: false,
                populators: {
                  name: "substage",
                  mdmsConfig: {
                    masterName: "SubStage",
                    moduleName: "case",
                    select: "(data) => {return data['case'].SubStage?.map((item) => {return item.subStage;});}",
                  },
                  styles: {
                    maxWidth: "300px",
                    minWidth: "200px",
                  },
                  optionsCustomStyle: {
                    overflowX: "hidden",
                  },
                },
              },
              {
                label: "Case ID and Title",
                type: "text",
                isMandatory: false,
                disable: false,
                populators: {
                  name: "caseTitleFilingNumber",
                  placeholder: "Case ID and Title",
                  error: "BR_PATTERN_ERR_MSG",
                  validation: {
                    pattern: {},
                    minlength: 1,
                  },
                },
              },
            ],
          },
          children: {},
          show: true,
        },
        searchResult: {
          tenantId: Digit.ULBService.getCurrentTenantId(),
          label: "",
          uiConfig: {
            columns: [
              {
                label: "Case ID",
                jsonPath: "businessObject.billDetails.caseFilingNumber",
                additionalCustomization: true,
              },
              {
                label: "Case Name",
                jsonPath: "businessObject.billDetails.caseTitleFilingNumber",
                additionalCustomization: true,
              },
              {
                label: "Case Type",
                jsonPath: "businessObject.billDetails.caseType",
              },
              {
                label: "Stage",
                jsonPath: "businessObject.billDetails.stage",
              },

              {
                label: "Amount Due",
                jsonPath: "businessObject.billDetails.amount",
                additionalCustomization: true,
              },
              {
                label: "Action",
                jsonPath: "businessObject.billDetails.id",
                additionalCustomization: true,
              },
            ],
            enableColumnSort: true,
            resultsJsonPath: "items",
          },
          show: true,
        },
      },
    },
    {
      label: "Payment Due",
      type: "search",
      apiDetails: {
        serviceName: "/inbox/v2/index/_search",
        requestParam: {},
        requestBody: {
          inbox: {
            processSearchCriteria: {
              businessService: ["billing"],
              moduleName: "Billing service",
            },
            moduleSearchCriteria: {
              billStatus: "ACTIVE",
            },
            limit: 10,
            offset: 0,
          },
        },
        minParametersForSearchForm: 0,
        masterName: "commonUiConfig",
        moduleName: "paymentInboxConfig",
        searchFormJsonPath: "requestBody.inbox.moduleSearchCriteria",
        tableFormJsonPath: "requestBody.inbox",
      },
      sections: {
        search: {
          uiConfig: {
            type: "registration-requests-table-search",
            primaryLabel: "ES_COMMON_SEARCH",
            secondaryLabel: "ES_COMMON_CLEAR_SEARCH",
            minReqFields: 0,
            defaultValues: {
              caseTitleFilingNumber: "",
            },
            fields: [
              {
                type: "component",
                component: "CustomSortComponent",
                isMandatory: false,
                disable: false,
                name: "Sort by",
                key: "sortCaseListByDate",
                sortBy: "createdtime",
                ascText: "Newest to Oldest",
                descText: "Oldest to Newest",
                showAdditionalText: true,
                showIcon: true,
                icon: "ArrowDownIcon",
                populators: {},
              },
              {
                label: "Stage",
                isMandatory: false,
                key: "stage",
                type: "dropdown",
                disable: false,
                populators: {
                  name: "substage",
                  mdmsConfig: {
                    masterName: "SubStage",
                    moduleName: "case",
                    select: "(data) => {return data['case'].SubStage?.map((item) => {return item.subStage;});}",
                  },
                  styles: {
                    maxWidth: "300px",
                    minWidth: "200px",
                  },
                  optionsCustomStyle: {
                    overflowX: "hidden",
                  },
                },
              },
              {
                label: "Case ID and Title",
                type: "text",
                isMandatory: false,
                disable: false,
                populators: {
                  name: "caseTitleFilingNumber",
                  placeholder: "Case ID and Title",
                  error: "BR_PATTERN_ERR_MSG",
                  validation: {
                    pattern: {},
                    minlength: 1,
                  },
                },
              },
            ],
          },
          children: {},
          show: true,
        },
        searchResult: {
          tenantId: Digit.ULBService.getCurrentTenantId(),
          label: "",
          uiConfig: {
            columns: [
              {
                label: "Case ID",
                jsonPath: "businessObject.billDetails.caseFilingNumber",
                additionalCustomization: true,
              },
              {
                label: "Case Name",
                jsonPath: "businessObject.billDetails.caseTitleFilingNumber",
                additionalCustomization: true,
              },
              {
                label: "Case Type",
                jsonPath: "businessObject.billDetails.caseType",
              },
              {
                label: "Stage",
                jsonPath: "businessObject.billDetails.stage",
              },

              {
                label: "Amount Due",
                jsonPath: "businessObject.billDetails.amount",
                additionalCustomization: true,
              },
              {
                label: "Action",
                jsonPath: "businessObject.billDetails.id",
                additionalCustomization: true,
              },
            ],
            enableColumnSort: true,
            resultsJsonPath: "items",
          },
          show: true,
        },
      },
    },
  ],
};
