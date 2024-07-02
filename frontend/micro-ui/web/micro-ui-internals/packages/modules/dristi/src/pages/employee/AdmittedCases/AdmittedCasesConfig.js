// default values of search input component
const defaultSearchValues = {
  individualName: "",
  mobileNumber: "",
  IndividualID: "",
};

//config for tab search sceeen
export const TabSearchconfig = {
  tenantId: "mz",
  moduleName: "commonCampaignUiConfig",
  showTab: true, // setting true will enable tab screen
  TabSearchconfig: [
    // all tab config should be added in json array
    {
      label: "Overview",
      type: "search",
    },
    {
      label: "Complaints",
      type: "search",
      // sections: {
      //   searchResult: {
      //     uiConfig: {},
      //   },
      //   show: true,
      // },
    },
    {
      label: "Hearings",
      type: "search",
      apiDetails: {
        serviceName: "/hearing/v1/search",
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
        filterFormJsonPath: "requestBody.HearingList",
        searchFormJsonPath: "requestBody.HearingList",
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
                label: "Type",
                isMandatory: false,
                key: "type",
                type: "dropdown",
                populators: {
                  name: "type",
                  optionsKey: "value",
                  mdmsConfig: {
                    masterName: "CaseType",
                    moduleName: "Case",
                    // localePrefix: "SUBMISSION_TYPE",
                  },
                },
              },
              {
                label: "Stage",
                isMandatory: false,
                key: "stage",
                type: "dropdown",
                populators: {
                  name: "stage",
                  optionsKey: "value",
                  mdmsConfig: {
                    masterName: "Stage",
                    moduleName: "case",
                    // localePrefix: "SUBMISSION_TYPE",
                  },
                },
              },
              {
                label: "Parties",
                isMandatory: false,
                key: "parties",
                type: "dropdown",
                populators: {
                  name: "parties",
                },
              },
              {
                label: "Order ID",
                isMandatory: false,
                key: "orderId",
                type: "text",
                populators: {
                  name: "orderId",
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
                label: "Hearing Type",
                jsonPath: "hearingType",
              },
              {
                label: "Stage",
                jsonPath: "",
              },
              {
                label: "Parties",
                jsonPath: "attendees",
                additionalCustomization: true,
              },
              {
                label: "Status",
                jsonPath: "status",
              },
              {
                label: "Date Added",
                jsonPath: "startTime",
                additionalCustomization: true,
              },
            ],
            enableColumnSort: true,
            resultsJsonPath: "HearingList",
          },
          show: true,
        },
      },
    },
    {
      label: "Orders",
      type: "search",
      apiDetails: {
        serviceName: "/order/order/v1/search",
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
                label: "Type",
                isMandatory: false,
                key: "type",
                type: "dropdown",
                populators: {
                  name: "type",
                  optionsKey: "value",
                  mdmsConfig: {
                    masterName: "CaseType",
                    moduleName: "Case",
                    // localePrefix: "SUBMISSION_TYPE",
                  },
                },
              },
              {
                label: "Stage",
                isMandatory: false,
                key: "stage",
                type: "dropdown",
                populators: {
                  name: "stage",
                  optionsKey: "value",
                  mdmsConfig: {
                    masterName: "Stage",
                    moduleName: "case",
                    // localePrefix: "SUBMISSION_TYPE",
                  },
                },
              },
              {
                label: "Status",
                isMandatory: false,
                key: "status",
                type: "dropdown",
                populators: {
                  name: "status",
                  optionsKey: "value",
                  mdmsConfig: {
                    masterName: "Status",
                    moduleName: "case",
                    // localePrefix: "SUBMISSION_TYPE",
                  },
                },
              },
              {
                label: "Parties",
                isMandatory: false,
                key: "parties",
                type: "dropdown",
                populators: {
                  name: "parties",
                },
              },
              {
                label: "Order ID",
                isMandatory: false,
                key: "orderId",
                type: "text",
                populators: {
                  name: "orderId",
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
                label: "Order Type",
                jsonPath: "orderType",
                additionalCustomization: true,
              },
              {
                label: "Order Id",
                jsonPath: "id",
              },
              {
                label: "Stage",
                jsonPath: "",
              },
              {
                label: "Parties",
                jsonPath: "",
              },
              {
                label: "Status",
                jsonPath: "status",
              },
              {
                label: "Date Added",
                jsonPath: "auditDetails.createdTime",
                additionalCustomization: true,
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
      label: "Submissions",
      type: "search",
      apiDetails: {
        serviceName: "/application/application/v1/search",
        requestParam: {
          tenantId: Digit.ULBService.getCurrentTenantId(),
        },
        requestBody: {
          apiOperation: "SEARCH",
          Individual: {
            tenantId: Digit.ULBService.getCurrentTenantId(),
          },
          criteria: {
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
                label: "Type",
                isMandatory: false,
                key: "type",
                type: "dropdown",
                populators: {
                  name: "type",
                  optionsKey: "value",
                  mdmsConfig: {
                    masterName: "CaseType",
                    moduleName: "Case",
                    // localePrefix: "SUBMISSION_TYPE",
                  },
                },
              },
              {
                label: "Stage",
                isMandatory: false,
                key: "stage",
                type: "dropdown",
                populators: {
                  name: "stage",
                  optionsKey: "value",
                  mdmsConfig: {
                    masterName: "Stage",
                    moduleName: "case",
                    // localePrefix: "SUBMISSION_TYPE",
                  },
                },
              },
              {
                label: "Status",
                isMandatory: false,
                key: "status",
                type: "dropdown",
                populators: {
                  name: "status",
                  optionsKey: "value",
                  mdmsConfig: {
                    masterName: "Status",
                    moduleName: "case",
                    // localePrefix: "SUBMISSION_TYPE",
                  },
                },
              },
              {
                label: "Owner",
                isMandatory: false,
                key: "owner",
                type: "dropdown",
                populators: {
                  name: "owner",
                },
              },
              {
                label: "Order ID",
                isMandatory: false,
                key: "orderId",
                type: "text",
                populators: {
                  name: "orderId",
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
                label: "Submission Name",
                jsonPath: "applicationType",
                additionalCustomization: true,
              },
              {
                label: "Submission Id",
                jsonPath: "id",
              },
              {
                label: "Stage",
                jsonPath: "",
              },
              {
                label: "Status",
                jsonPath: "workflow.action",
              },
              {
                label: "Owner",
                jsonPath: "",
              },
              {
                label: "Date Added",
                jsonPath: "auditDetails.createdTime",
                additionalCustomization: true,
              },
              {
                label: "Document",
                jsonPath: "documents",
                additionalCustomization: true,
              },
            ],

            enableColumnSort: true,
            resultsJsonPath: "applicationList",
          },
          show: true,
        },
      },
    },
    {
      label: "History",
      type: "search",
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
                label: "Applicant name ",
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
                label: "Phone number",
                isMandatory: false,
                key: "Phone number",
                type: "number",
                disable: false,
                populators: { name: "mobileNumber", error: "sample error message", validation: { min: 0, max: 999999999 } },
              },
              {
                label: "Individual Id ",
                isMandatory: false,
                type: "text",
                disable: false,
                populators: {
                  name: "individualId",
                },
              },
            ],
          },

          show: false,
        },
        searchResult: {
          tenantId: Digit.ULBService.getCurrentTenantId(),
          uiConfig: {
            columns: [
              {
                label: "IndividualID",
                jsonPath: "individualId",
              },
              {
                label: "Name",
                jsonPath: "name.givenName",
              },
              {
                label: "Address",
                jsonPath: "address.locality.code",
              },
            ],

            enableColumnSort: true,
            resultsJsonPath: "Individual",
          },
          show: true,
        },
      },
    },
    {
      label: "Parties",
      type: "search",
      apiDetails: {
        serviceName: "/case/case/v1/_search",
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
        moduleName: "PartiesConfig",
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
                label: "Applicant name ",
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
                label: "Phone number",
                isMandatory: false,
                key: "Phone number",
                type: "number",
                disable: false,
                populators: { name: "mobileNumber", error: "sample error message", validation: { min: 0, max: 999999999 } },
              },
              {
                label: "Individual Id ",
                isMandatory: false,
                type: "text",
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
                label: "Party Name",
                jsonPath: "name",
              },
              {
                label: "Type",
                jsonPath: "partyType",
              },
              {
                label: "Date Added",
                jsonPath: "auditDetails.createdTime",
                additionalCustomization: true,
              },
            ],

            enableColumnSort: true,
            resultsJsonPath: "criteria.responseList.parties",
          },
          show: true,
        },
      },
    },
  ],
};
