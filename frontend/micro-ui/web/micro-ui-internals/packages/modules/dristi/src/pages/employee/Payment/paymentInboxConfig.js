export const paymentTabInboxConfig = {
  tenantId: "pg",
  moduleName: "paymentInboxConfig",
  showTab: true,
  TabSearchConfig: [
    {
      label: "PENDING",
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
              sortOrder: "DESC",
            },
            fields: [
              {
                type: "component",
                component: "CustomSortComponent",
                isMandatory: false,
                disable: false,
                name: "SORT_BY",
                key: "sortOrder",
                paymentInbox: true,
                ascText: "OLDEST_TO_NEWEST",
                descText: "NEWEST_TO_OLDEST",
                showAdditionalText: true,
                showIcon: true,
                icon: "UpDownArrowIcon",
                populators: {},
              },
              {
                label: "CASE_TYPE",
                isMandatory: false,
                key: "caseType",
                type: "dropdown",
                disable: false,
                populators: {
                  name: "caseType",
                  options: ["Negotiable Instruments Act 1881"],
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
                label: "NYAY_PAYMENT_TYPE",
                isMandatory: false,
                key: "paymentType",
                type: "dropdown",
                disable: false,
                populators: {
                  name: "paymentType",
                  mdmsConfig: {
                    masterName: "paymentType",
                    moduleName: "payment",
                    select:
                      "(data) => {return data['payment'].paymentType?.filter((item) => item?.paymentType !== `Warrant Court Fee`).map((item) => {return item?.paymentType;});}",
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
                label: "CASE_ID_TITLE",
                type: "text",
                isMandatory: false,
                disable: false,
                populators: {
                  name: "caseTitleFilingNumber",
                  // placeholder: "CASE_ID_TITLE",
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
                label: "CASE_NAME_ID",
                jsonPath: "businessObject.billDetails.caseTitleFilingNumber",
                additionalCustomization: true,
              },
              {
                label: "AMOUNT_DUE",
                jsonPath: "businessObject.billDetails.amount",
                additionalCustomization: true,
              },
              {
                label: "CS_STAGE",
                jsonPath: "businessObject.billDetails.stage",
              },
              {
                label: "CASE_TYPE",
                jsonPath: "businessObject.billDetails.caseType",
              },
              {
                label: "NYAY_PAYMENT_TYPE",
                jsonPath: "businessObject.billDetails.paymentType",
              },
              {
                label: "ACTION",
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
      label: "COMPLETED",
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
              sortOrder: "DESC",
            },
            fields: [
              {
                type: "component",
                component: "CustomSortComponent",
                isMandatory: false,
                disable: false,
                name: "SORT_BY",
                key: "sortOrder",
                paymentInbox: true,
                ascText: "OLDEST_TO_NEWEST",
                descText: "NEWEST_TO_OLDEST",
                showAdditionalText: true,
                showIcon: true,
                icon: "UpDownArrowIcon",
                populators: {},
              },
              {
                label: "CASE_TYPE",
                isMandatory: false,
                key: "caseType",
                type: "dropdown",
                disable: false,
                populators: {
                  name: "caseType",
                  options: ["Negotiable Instruments Act 1881"],
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
                label: "NYAY_PAYMENT_TYPE",
                isMandatory: false,
                key: "paymentType",
                type: "dropdown",
                disable: false,
                populators: {
                  name: "paymentType",
                  mdmsConfig: {
                    masterName: "paymentType",
                    moduleName: "payment",
                    select: "(data) => {return data['payment'].paymentType?.map((item) => {return item?.paymentType;});}",
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
                label: "CASE_ID_TITLE",
                type: "text",
                isMandatory: false,
                disable: false,
                populators: {
                  name: "caseTitleFilingNumber",
                  // placeholder: "CASE_ID_TITLE",
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
                label: "CASE_NAME_ID",
                jsonPath: "businessObject.billDetails.caseTitleFilingNumber",
                additionalCustomization: true,
              },
              {
                label: "AMOUNT_DUE",
                jsonPath: "businessObject.billDetails.amount",
                additionalCustomization: true,
              },
              {
                label: "CS_STAGE",
                jsonPath: "businessObject.billDetails.stage",
              },
              {
                label: "CASE_TYPE",
                jsonPath: "businessObject.billDetails.caseType",
              },
              {
                label: "NYAY_PAYMENT_TYPE",
                jsonPath: "businessObject.billDetails.paymentType",
              },
              {
                label: "ACTION",
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
