const defaultSearchValues = {
  individualName: "",
  mobileNumber: "",
  IndividualID: "",
};

export const userTypeOptions = [
  {
    code: "LITIGANT",
    name: "LITIGANT_TEXT",
    showBarDetails: false,
    isVerified: false,
    role: ["CASE_CREATOR", "CASE_EDITOR", "CASE_VIEWER", "DEPOSITION_CREATOR", "DEPOSITION_EDITOR", "DEPOSITION_VIEWER"],
    subText: "LITIGANT_SUB_TEXT",
  },
  {
    code: "ADVOCATE",
    name: "ADVOCATE_TEXT",
    showBarDetails: true,
    isVerified: true,
    hasBarRegistrationNo: true,
    role: ["ADVOCATE_ROLE", "CASE_CREATOR", "CASE_EDITOR", "CASE_VIEWER", "DEPOSITION_CREATOR", "DEPOSITION_EDITOR", "DEPOSITION_VIEWER"],
    apiDetails: {
      serviceName: "/advocate/advocate/v1/_create",
      requestKey: "advocate",
      AdditionalFields: ["barRegistrationNumber"],
    },
    subText: "ADVOCATE_SUB_TEXT",
  },
  {
    code: "ADVOCATE_CLERK",
    name: "ADVOCATE_CLERK_TEXT",
    showBarDetails: true,
    hasStateRegistrationNo: true,
    isVerified: true,
    role: ["ADVOCATE_CLERK_ROLE", "CASE_CREATOR", "CASE_EDITOR", "CASE_VIEWER", "DEPOSITION_CREATOR", "DEPOSITION_EDITOR", "DEPOSITION_VIEWER"],
    apiDetails: {
      serviceName: "/advocate/clerk/v1/_create",
      requestKey: "clerk",
      AdditionalFields: ["stateRegnNumber"],
    },

    subText: "ADVOCATE_CLERK_SUB_TEXT",
  },
];

export const TabLitigantSearchConfig = {
  tenantId: "pg",
  moduleName: "homeHearingUIConfig",
  showTab: true,
  TabSearchConfig: [
    {
      label: "All",
      type: "search",
      apiDetails: {
        serviceName: "/case/case/v1/_search",
        requestParam: {},
        requestBody: {
          tenantId: "pg",
          criteria: [{}],
        },
        masterName: "commonUiConfig",
        moduleName: "homeHearingUIConfig",
        minParametersForSearchForm: 0,
        tableFormJsonPath: "requestParam",
        filterFormJsonPath: "requestBody",
        searchFormJsonPath: "requestBody",
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
                label: "CS_FILING_NO",
                type: "text",
                isMandatory: false,
                disable: false,
                populators: {
                  name: "filingNumber",
                  error: "BR_PATTERN_ERR_MSG",
                  validation: {
                    pattern: {},
                    minlength: 2,
                  },
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
                label: "Case Name",
                jsonPath: "caseTitle",
              },
              {
                label: "Stage",
                jsonPath: "status",
                additionalCustomization: true,
              },
              {
                label: "Case ID",
                jsonPath: "filingNumber",
              },
              {
                label: "Case Type",
                jsonPath: "",
                additionalCustomization: true,
              },
              {
                label: "Days Since Filing",
                jsonPath: "filingDate",
              },
            ],

            enableColumnSort: true,
            resultsJsonPath: "criteria[0].responseList",
          },
          show: true,
        },
      },
    },
    {
      label: "Drafts",
      type: "search",
      apiDetails: {
        serviceName: "/case/case/v1/_search",
        requestParam: {},
        requestBody: {
          tenantId: "pg",
          criteria: [
            {
              status: "DRAFT_IN_PROGRESS",
            },
          ],
        },
        masterName: "commonUiConfig",
        moduleName: "homeHearingUIConfig",
        minParametersForSearchForm: 0,
        tableFormJsonPath: "requestParam",
        filterFormJsonPath: "requestBody",
        searchFormJsonPath: "requestBody",
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
                label: "CS_FILING_NO",
                type: "text",
                isMandatory: false,
                disable: false,
                populators: {
                  name: "filingNumber",
                  error: "BR_PATTERN_ERR_MSG",
                  validation: {
                    pattern: {},
                    minlength: 2,
                  },
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
                label: "Case Name",
                jsonPath: "caseTitle",
              },
              {
                label: "Stage",
                jsonPath: "status",
                additionalCustomization: true,
              },
              {
                label: "Case ID",
                jsonPath: "filingNumber",
              },
              {
                label: "Case Type",
                jsonPath: "",
                additionalCustomization: true,
              },
              {
                label: "Days Since Filing",
                jsonPath: "filingDate",
              },
            ],

            enableColumnSort: true,
            resultsJsonPath: "criteria[0].responseList",
          },
          show: true,
        },
      },
    },
    {
      label: "Closed",
      type: "search",
      apiDetails: {
        serviceName: "/case/case/v1/_search",
        requestParam: {},
        requestBody: {
          tenantId: "pg",
          criteria: [{}],
        },
        masterName: "commonUiConfig",
        moduleName: "homeHearingUIConfig",
        minParametersForSearchForm: 0,
        tableFormJsonPath: "requestParam",
        filterFormJsonPath: "requestBody",
        searchFormJsonPath: "requestBody",
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
                label: "CS_FILING_NO",
                type: "text",
                isMandatory: false,
                disable: false,
                populators: {
                  name: "filingNumber",
                  error: "BR_PATTERN_ERR_MSG",
                  validation: {
                    pattern: {},
                    minlength: 2,
                  },
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
                label: "Case Name",
                jsonPath: "caseTitle",
              },
              {
                label: "Stage",
                jsonPath: "status",
                additionalCustomization: true,
              },
              {
                label: "Case ID",
                jsonPath: "filingNumber",
              },
              {
                label: "Case Type",
                jsonPath: "",
                additionalCustomization: true,
              },
              {
                label: "Days Since Filing",
                jsonPath: "filingDate",
              },
            ],

            enableColumnSort: true,
            resultsJsonPath: "criteria[0].responseList",
          },
          show: true,
        },
      },
    },
  ],
};

export const TabJudgeSearchConfig = {
  tenantId: "pg",
  moduleName: "homeHearingUIConfig",
  showTab: true,
  TabSearchConfig: [
    {
      label: "All",
      type: "search",
      apiDetails: {
        serviceName: "/case/case/v1/_search",
        requestParam: {},
        requestBody: {
          tenantId: "pg",
          criteria: [{}],
        },
        masterName: "commonUiConfig",
        moduleName: "homeHearingUIConfig",
        minParametersForSearchForm: 0,
        tableFormJsonPath: "requestParam",
        filterFormJsonPath: "requestBody",
        searchFormJsonPath: "requestBody",
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
                label: "CS_FILING_NO",
                type: "text",
                isMandatory: false,
                disable: false,
                populators: {
                  name: "filingNumber",
                  error: "BR_PATTERN_ERR_MSG",
                  validation: {
                    pattern: {},
                    minlength: 2,
                  },
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
                label: "Case Name",
                jsonPath: "caseTitle",
              },
              {
                label: "Stage",
                jsonPath: "status",
                additionalCustomization: true,
              },
              {
                label: "Case ID",
                jsonPath: "filingNumber",
              },
              {
                label: "Case Type",
                jsonPath: "",
                additionalCustomization: true,
              },
              {
                label: "Days Since Filing",
                jsonPath: "filingDate",
              },
            ],

            enableColumnSort: true,
            resultsJsonPath: "criteria[0].responseList",
          },
          show: true,
        },
      },
    },
    {
      label: "Ongoing",
      type: "search",
      apiDetails: {
        serviceName: "/case/case/v1/_search",
        requestParam: {},
        requestBody: {
          tenantId: "pg",
          criteria: [
            {
              status: "DRAFT_IN_PROGRESS",
            },
          ],
        },
        masterName: "commonUiConfig",
        moduleName: "homeHearingUIConfig",
        minParametersForSearchForm: 0,
        tableFormJsonPath: "requestParam",
        filterFormJsonPath: "requestBody",
        searchFormJsonPath: "requestBody",
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
                label: "CS_FILING_NO",
                type: "text",
                isMandatory: false,
                disable: false,
                populators: {
                  name: "filingNumber",
                  error: "BR_PATTERN_ERR_MSG",
                  validation: {
                    pattern: {},
                    minlength: 2,
                  },
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
                label: "Case Name",
                jsonPath: "caseTitle",
              },
              {
                label: "Stage",
                jsonPath: "status",
                additionalCustomization: true,
              },
              {
                label: "Case ID",
                jsonPath: "filingNumber",
              },
              {
                label: "Case Type",
                jsonPath: "",
                additionalCustomization: true,
              },
              {
                label: "Days Since Filing",
                jsonPath: "filingDate",
              },
            ],

            enableColumnSort: true,
            resultsJsonPath: "criteria[0].responseList",
          },
          show: true,
        },
      },
    },
    {
      label: "Registered",
      type: "search",
      apiDetails: {
        serviceName: "/case/case/v1/_search",
        requestParam: {},
        requestBody: {
          tenantId: "pg",
          criteria: [{}],
        },
        masterName: "commonUiConfig",
        moduleName: "homeHearingUIConfig",
        minParametersForSearchForm: 0,
        tableFormJsonPath: "requestParam",
        filterFormJsonPath: "requestBody",
        searchFormJsonPath: "requestBody",
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
                label: "CS_FILING_NO",
                type: "text",
                isMandatory: false,
                disable: false,
                populators: {
                  name: "filingNumber",
                  error: "BR_PATTERN_ERR_MSG",
                  validation: {
                    pattern: {},
                    minlength: 2,
                  },
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
                label: "Case Name",
                jsonPath: "caseTitle",
              },
              {
                label: "Stage",
                jsonPath: "status",
                additionalCustomization: true,
              },
              {
                label: "Case ID",
                jsonPath: "filingNumber",
              },
              {
                label: "Case Type",
                jsonPath: "",
                additionalCustomization: true,
              },
              {
                label: "Days Since Filing",
                jsonPath: "filingDate",
              },
            ],

            enableColumnSort: true,
            resultsJsonPath: "criteria[0].responseList",
          },
          show: true,
        },
      },
    },
    {
      label: "Closed",
      type: "search",
      apiDetails: {
        serviceName: "/case/case/v1/_search",
        requestParam: {},
        requestBody: {
          tenantId: "pg",
          criteria: [{}],
        },
        masterName: "commonUiConfig",
        moduleName: "homeHearingUIConfig",
        minParametersForSearchForm: 0,
        tableFormJsonPath: "requestParam",
        filterFormJsonPath: "requestBody",
        searchFormJsonPath: "requestBody",
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
                label: "CS_FILING_NO",
                type: "text",
                isMandatory: false,
                disable: false,
                populators: {
                  name: "filingNumber",
                  error: "BR_PATTERN_ERR_MSG",
                  validation: {
                    pattern: {},
                    minlength: 2,
                  },
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
                label: "Case Name",
                jsonPath: "caseTitle",
              },
              {
                label: "Stage",
                jsonPath: "status",
                additionalCustomization: true,
              },
              {
                label: "Case ID",
                jsonPath: "filingNumber",
              },
              {
                label: "Case Type",
                jsonPath: "",
                additionalCustomization: true,
              },
              {
                label: "Days Since Filing",
                jsonPath: "filingDate",
              },
            ],

            enableColumnSort: true,
            resultsJsonPath: "criteria[0].responseList",
          },
          show: true,
        },
      },
    },
  ],
};

export const TabFSOSearchConfig = {
  tenantId: "pg",
  moduleName: "homeHearingUIConfig",
  showTab: true,
  TabSearchConfig: [
    {
      label: "All",
      type: "search",
      apiDetails: {
        serviceName: "/case/case/v1/_search",
        requestParam: {},
        requestBody: {
          tenantId: "pg",
          criteria: [{}],
        },
        masterName: "commonUiConfig",
        moduleName: "homeHearingUIConfig",
        minParametersForSearchForm: 0,
        tableFormJsonPath: "requestParam",
        filterFormJsonPath: "requestBody",
        searchFormJsonPath: "requestBody",
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
                label: "CS_FILING_NO",
                type: "text",
                isMandatory: false,
                disable: false,
                populators: {
                  name: "filingNumber",
                  error: "BR_PATTERN_ERR_MSG",
                  validation: {
                    pattern: {},
                    minlength: 2,
                  },
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
                label: "Case Name",
                jsonPath: "caseTitle",
              },
              {
                label: "Stage",
                jsonPath: "status",
                additionalCustomization: true,
              },
              {
                label: "Case ID",
                jsonPath: "filingNumber",
              },
              {
                label: "Case Type",
                jsonPath: "",
                additionalCustomization: true,
              },
              {
                label: "Days Since Filing",
                jsonPath: "filingDate",
              },
            ],

            enableColumnSort: true,
            resultsJsonPath: "criteria[0].responseList",
          },
          show: true,
        },
      },
    },
    {
      label: "Scrutiny Due",
      type: "search",
      apiDetails: {
        serviceName: "/case/case/v1/_search",
        requestParam: {},
        requestBody: {
          tenantId: "pg",
          criteria: [
            {
              status: "UNDER_SCRUTINY",
            },
          ],
        },
        masterName: "commonUiConfig",
        moduleName: "homeHearingUIConfig",
        minParametersForSearchForm: 0,
        tableFormJsonPath: "requestParam",
        filterFormJsonPath: "requestBody",
        searchFormJsonPath: "requestBody",
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
                label: "CS_FILING_NO",
                type: "text",
                isMandatory: false,
                disable: false,
                populators: {
                  name: "filingNumber",
                  error: "BR_PATTERN_ERR_MSG",
                  validation: {
                    pattern: {},
                    minlength: 2,
                  },
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
                label: "Case Name",
                jsonPath: "caseTitle",
              },
              {
                label: "Stage",
                jsonPath: "status",
                additionalCustomization: true,
              },
              {
                label: "Case ID",
                jsonPath: "filingNumber",
              },
              {
                label: "Case Type",
                jsonPath: "",
                additionalCustomization: true,
              },
              {
                label: "Days Since Filing",
                jsonPath: "filingDate",
              },
            ],

            enableColumnSort: true,
            resultsJsonPath: "criteria[0].responseList",
          },
          show: true,
        },
      },
    },
  ],
};

const rolesToConfigMapping = [
  {
    roles: ["CASE_CREATOR", "CASE_EDITOR", "CASE_VIEWER", "DEPOSITION_CREATOR", "DEPOSITION_EDITOR", "DEPOSITION_VIEWER"],
    config: TabLitigantSearchConfig,
  },
  {
    role: [],
  },
];
