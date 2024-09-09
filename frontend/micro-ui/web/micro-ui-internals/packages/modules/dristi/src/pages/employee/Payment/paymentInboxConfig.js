export const paymentTabInboxConfig = {
  tenantId: "pg",
  moduleName: "paymentInboxConfig",
  showTab: true,
  TabSearchConfig: [
    {
      label: "Pending",
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
                key: "sortOrder",
                paymentInbox: true,
                ascText: "Newest to Oldest",
                descText: "Oldest to Newest",
                showAdditionalText: true,
                showIcon: true,
                icon: "ArrowDownIcon",
                populators: {},
              },
              {
                label: "Case Type",
                isMandatory: false,
                key: "caseType",
                type: "dropdown",
                disable: false,
                populators: {
                  name: "caseType",
                  options: ["caseType"],
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
                label: "Payment Type",
                isMandatory: false,
                key: "paymentType",
                type: "dropdown",
                disable: false,
                populators: {
                  name: "paymentType",
                  options: ["paymentType"],
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
                label: "Case Name & ID",
                jsonPath: "businessObject.billDetails.caseTitleFilingNumber",
                additionalCustomization: true,
              },
              {
                label: "Amount Due",
                jsonPath: "businessObject.billDetails.amount",
                additionalCustomization: true,
              },
              {
                label: "Stage",
                jsonPath: "businessObject.billDetails.stage",
              },
              {
                label: "Case Type",
                jsonPath: "businessObject.billDetails.caseType",
              },
              {
                label: "Payment Type",
                jsonPath: "businessObject.billDetails.paymentType",
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
                key: "sortOrder",
                paymentInbox: true,
                ascText: "Newest to Oldest",
                descText: "Oldest to Newest",
                showAdditionalText: true,
                showIcon: true,
                icon: "ArrowDownIcon",
                populators: {},
              },
              {
                label: "Case Type",
                isMandatory: false,
                key: "caseType",
                type: "dropdown",
                disable: false,
                populators: {
                  name: "caseType",
                  options: ["caseType"],
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
                label: "Case Name & ID",
                jsonPath: "businessObject.billDetails.caseTitleFilingNumber",
                additionalCustomization: true,
              },
              {
                label: "Amount Due",
                jsonPath: "businessObject.billDetails.amount",
                additionalCustomization: true,
              },
              {
                label: "Stage",
                jsonPath: "businessObject.billDetails.stage",
              },
              {
                label: "Case Type",
                jsonPath: "businessObject.billDetails.caseType",
              },
              {
                label: "Payment Type",
                jsonPath: "businessObject.billDetails.paymentType",
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
