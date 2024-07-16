const defaultSearchValues = {
  filingNumber: "",
  caseType: "NIA S138",
  substage: "",
};

export const CaseWorkflowState = {
  CASE_RE_ASSIGNED: "CASE_RE_ASSIGNED",
  DRAFT_IN_PROGRESS: "DRAFT_IN_PROGRESS",
  UNDER_SCRUTINY: "UNDER_SCRUTINY",
  CASE_ADMITTED: "CASE_ADMITTED",
  PENDING_ADMISSION: "PENDING_ADMISSION",
};

export const subStageOptions = [
  "Filing",
  "Cognizance",
  "Inquiry",
  "Appearance",
  "Framing of charges",
  "Evidence",
  "Arguments",
  "Judgment",
  "Post-Judgement",
];
export const outcomesOptions = [
  "Withdrawn",
  "Settled",
  "Transferred",
  "Dismissed",
  "Allowed",
  "Partly allowed",
  "Convicted",
  "Partly convicted",
  "Abated",
];

export const userTypeOptions = [
  {
    code: "LITIGANT",
    name: "LITIGANT_TEXT",
    showBarDetails: false,
    isVerified: false,
    role: [
      "CASE_CREATOR",
      "CASE_EDITOR",
      "CASE_VIEWER",
      "DEPOSITION_CREATOR",
      "DEPOSITION_VIEWER",
      "APPLICATION_CREATOR",
      "APPLICATION_VIEWER",
      "HEARING_VIEWER",
      "ORDER_VIEWER",
      "SUBMISSION_CREATOR",
      "SUBMISSION_RESPONDER",
    ],
    subText: "LITIGANT_SUB_TEXT",
  },
  {
    code: "ADVOCATE",
    name: "ADVOCATE_TEXT",
    showBarDetails: true,
    isVerified: true,
    hasBarRegistrationNo: true,
    role: [
      "ADVOCATE_ROLE",
      "CASE_CREATOR",
      "CASE_EDITOR",
      "CASE_VIEWER",
      "DEPOSITION_CREATOR",
      "DEPOSITION_VIEWER",
      "APPLICATION_CREATOR",
      "APPLICATION_VIEWER",
      "HEARING_VIEWER",
      "ORDER_VIEWER",
      "SUBMISSION_CREATOR",
      "SUBMISSION_RESPONDER",
    ],
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
    role: [
      "ADVOCATE_CLERK_ROLE",
      "CASE_CREATOR",
      "CASE_EDITOR",
      "CASE_VIEWER",
      "DEPOSITION_CREATOR",
      "DEPOSITION_VIEWER",
      "APPLICATION_CREATOR",
      "APPLICATION_VIEWER",
      "HEARING_VIEWER",
      "ORDER_VIEWER",
      "SUBMISSION_CREATOR",
      "SUBMISSION_RESPONDER",
    ],
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
  moduleName: "homeLitigantUiConfig",
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
        moduleName: "homeLitigantUiConfig",
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
                label: "Case ID",
                type: "text",
                isMandatory: false,
                disable: false,
                populators: {
                  name: "filingNumber",
                  error: "BR_PATTERN_ERR_MSG",
                  style: { maxWidth: "250px", minWidth: "200px", width: "220px" },
                  validation: {
                    pattern: {},
                    minlength: 2,
                  },
                },
              },
              {
                label: "Case Type",
                isMandatory: false,
                key: "caseType",
                type: "dropdown",
                disable: false,
                populators: {
                  name: "caseType",
                  options: ["NIA S138"],
                  styles: {
                    maxWidth: "200px",
                    minWidth: "150px",
                  },
                  optionsCustomStyle: {
                    overflowX: "hidden",
                  },
                },
              },
              {
                label: "Stage",
                isMandatory: false,
                key: "stage",
                type: "dropdown",
                disable: false,
                populators: {
                  name: "substage",
                  options: subStageOptions,
                  styles: {
                    maxWidth: "250px",
                    minWidth: "200px",
                  },
                  optionsCustomStyle: {
                    overflowX: "hidden",
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
                jsonPath: "substage",
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
                label: "Filing Date",
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
        moduleName: "homeLitigantUiConfig",
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
                type: "component",
                component: "CustomSortComponent",
                isMandatory: false,
                disable: false,
                name: "Last Edited",
                key: "sortCaseListByDate",
                sortBy: "lastModifiedTime",
                showIcon: true,
                icon: "UpDownArrowIcon",
                populators: {},
              },
              {
                label: "Case ID",
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
                label: "Draft Name",
                jsonPath: "caseTitle",
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
                label: "Last Edited",
                jsonPath: "auditDetails.lastModifiedTime",
                additionalCustomization: true,
              },
            ],

            enableColumnSort: true,
            resultsJsonPath: "criteria[0].responseList",
          },
          show: true,
        },
      },
      additionalDetails: {
        sortBy: "sortCaseListByDate",
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
          criteria: [{ stage: "Post-Trial" }],
        },
        masterName: "commonUiConfig",
        moduleName: "homeLitigantUiConfig",
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
                label: "Case ID",
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
              {
                label: "Outcome",
                isMandatory: false,
                key: "outcome",
                type: "dropdown",
                disable: false,
                populators: {
                  name: "outcome",
                  options: outcomesOptions,
                  styles: {
                    maxWidth: "250px",
                    minWidth: "200px",
                  },
                  optionsCustomStyle: {
                    overflowX: "hidden",
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
                label: "OutCome",
                jsonPath: "outcome",
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
                label: "Filing Date",
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
  moduleName: "homeJudgeUIConfig",
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
          criteria: [
            {
              stage: "Pre-Trial",
            },
          ],
        },
        masterName: "commonUiConfig",
        moduleName: "homeJudgeUIConfig",
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
                label: "Case ID",
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
              {
                label: "Case Type",
                isMandatory: false,
                key: "caseType",
                type: "dropdown",
                disable: false,
                populators: {
                  name: "caseType",
                  options: ["NIA S138"],
                  styles: {
                    maxWidth: "200px",
                    minWidth: "150px",
                  },
                  optionsCustomStyle: {
                    overflowX: "hidden",
                  },
                },
              },
              {
                label: "Stage",
                isMandatory: false,
                key: "stage",
                type: "dropdown",
                disable: false,
                populators: {
                  name: "substage",
                  options: subStageOptions,
                  styles: {
                    maxWidth: "250px",
                    minWidth: "200px",
                  },
                  optionsCustomStyle: {
                    overflowX: "hidden",
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
                jsonPath: "substage",
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
                label: "Filing Date",
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
              stage: "Trial",
            },
          ],
        },
        masterName: "commonUiConfig",
        moduleName: "homeJudgeUIConfig",
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
                label: "Case ID",
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
              {
                label: "Case Type",
                isMandatory: false,
                key: "caseType",
                type: "dropdown",
                disable: false,
                populators: {
                  name: "caseType",
                  options: ["NIA S138"],
                  styles: {
                    maxWidth: "200px",
                    minWidth: "150px",
                  },
                  optionsCustomStyle: {
                    overflowX: "hidden",
                  },
                },
              },
              {
                label: "Stage",
                isMandatory: false,
                key: "stage",
                type: "dropdown",
                disable: false,
                populators: {
                  name: "substage",
                  options: subStageOptions,
                  styles: {
                    maxWidth: "250px",
                    minWidth: "200px",
                  },
                  optionsCustomStyle: {
                    overflowX: "hidden",
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
                jsonPath: "substage",
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
                label: "Filing Date",
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
          criteria: [
            {
              stage: "Post-Trial",
            },
          ],
        },
        masterName: "commonUiConfig",
        moduleName: "homeJudgeUIConfig",
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
                type: "component",
                component: "CustomSortComponent",
                isMandatory: false,
                disable: false,
                name: "Sort by",
                key: "sortCaseListByDate",
                sortBy: "createdtime",
                ascText: "First",
                descText: "Last",
                showAdditionalText: true,
                showIcon: true,
                icon: "UpDownArrowIcon",
                populators: {},
              },
              {
                label: "Case ID",
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
              {
                label: "Case Type",
                isMandatory: false,
                key: "caseType",
                type: "dropdown",
                disable: false,
                populators: {
                  name: "caseType",
                  options: ["NIA S138"],
                  styles: {
                    maxWidth: "200px",
                    minWidth: "150px",
                  },
                  optionsCustomStyle: {
                    overflowX: "hidden",
                  },
                },
              },
              {
                label: "Stage",
                isMandatory: false,
                key: "stage",
                type: "dropdown",
                disable: false,
                populators: {
                  name: "substage",
                  options: subStageOptions,
                  styles: {
                    maxWidth: "250px",
                    minWidth: "200px",
                  },
                  optionsCustomStyle: {
                    overflowX: "hidden",
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
                jsonPath: "substage",
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
                label: "Filing Date",
                jsonPath: "filingDate",
              },
            ],

            enableColumnSort: true,
            resultsJsonPath: "criteria[0].responseList",
          },
          show: true,
        },
        additionalDetails: {
          sortBy: "sortCaseListByDate",
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
          criteria: [
            {
              stage: "Post-Trial",
            },
          ],
        },
        masterName: "commonUiConfig",
        moduleName: "homeJudgeUIConfig",
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
                type: "component",
                component: "CustomSortComponent",
                isMandatory: false,
                disable: false,
                name: "Closed:",
                key: "sortCaseListByDate",
                sortBy: "createdtime",
                ascText: "new first",
                descText: "old first",
                showAdditionalText: true,
                showIcon: true,
                icon: "UpDownArrowIcon",
                populators: {},
              },
              {
                label: "Case ID",
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
              {
                label: "Case Type",
                isMandatory: false,
                key: "caseType",
                type: "dropdown",
                disable: false,
                populators: {
                  name: "caseType",
                  options: ["NIA S138"],
                  styles: {
                    maxWidth: "200px",
                    minWidth: "150px",
                  },
                  optionsCustomStyle: {
                    overflowX: "hidden",
                  },
                },
              },
              {
                label: "Outcome",
                isMandatory: false,
                key: "outcome",
                type: "dropdown",
                disable: false,
                populators: {
                  name: "outcome",
                  options: outcomesOptions,
                  styles: {
                    maxWidth: "250px",
                    minWidth: "200px",
                  },
                  optionsCustomStyle: {
                    overflowX: "hidden",
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
                label: "Outcome",
                jsonPath: "outcome",
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
                label: "Filing Date",
                jsonPath: "filingDate",
              },
            ],

            enableColumnSort: true,
            resultsJsonPath: "criteria[0].responseList",
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

export const TabFSOSearchConfig = {
  tenantId: "pg",
  moduleName: "homeFSOUiConfig",
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
          criteria: [
            {
              stage: "Pre-Trial",
            },
          ],
        },
        masterName: "commonUiConfig",
        moduleName: "homeFSOUiConfig",
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
                type: "component",
                component: "CustomSortComponent",
                isMandatory: false,
                disable: false,
                name: "Filed",
                key: "sortCaseListByDate",
                sortBy: "createdtime",
                ascText: "(new first)",
                descText: "(old first)",
                showAdditionalText: true,
                showIcon: true,
                icon: "UpDownArrowIcon",
                populators: {},
              },
              {
                label: "Case ID",
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
              {
                label: "Case Type",
                isMandatory: false,
                key: "caseType",
                type: "dropdown",
                disable: false,
                populators: {
                  name: "caseType",
                  options: ["NIA S138"],
                  styles: {
                    maxWidth: "200px",
                    minWidth: "150px",
                  },
                  optionsCustomStyle: {
                    overflowX: "hidden",
                  },
                },
              },
              {
                label: "Stage",
                isMandatory: false,
                key: "stage",
                type: "dropdown",
                disable: false,
                populators: {
                  name: "substage",
                  options: subStageOptions,
                  styles: {
                    maxWidth: "250px",
                    minWidth: "200px",
                  },
                  optionsCustomStyle: {
                    overflowX: "hidden",
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
                jsonPath: "substage",
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
                jsonPath: "auditDetails.createdTime",
                additionalCustomization: true,
              },
            ],

            enableColumnSort: true,
            resultsJsonPath: "criteria[0].responseList",
          },
          show: true,
        },
      },
      additionalDetails: {
        sortBy: "sortCaseListByDate",
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
        moduleName: "homeFSOUiConfig",
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
                type: "component",
                component: "CustomSortComponent",
                isMandatory: false,
                disable: false,
                name: "Filed",
                key: "sortCaseListByDate",
                sortBy: "createdtime",
                ascText: "(new first)",
                descText: "(old first)",
                showAdditionalText: true,
                showIcon: true,
                icon: "UpDownArrowIcon",
                populators: {},
              },
              {
                label: "Case ID",
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
              {
                label: "Case Type",
                isMandatory: false,
                key: "caseType",
                type: "dropdown",
                disable: false,
                populators: {
                  name: "caseType",
                  options: ["NIA S138"],
                  styles: {
                    maxWidth: "200px",
                    minWidth: "150px",
                  },
                  optionsCustomStyle: {
                    overflowX: "hidden",
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
                label: "Status",
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
                jsonPath: "auditDetails.createdTime",
                additionalCustomization: true,
              },
            ],

            enableColumnSort: true,
            resultsJsonPath: "criteria[0].responseList",
          },
          show: true,
        },
        additionalDetails: {
          sortBy: "sortCaseListByDate",
        },
      },
    },
  ],
};

export const rolesToConfigMapping = [
  {
    roles: [
      "CASE_CREATOR",
      "CASE_EDITOR",
      "CASE_VIEWER",
      "DEPOSITION_CREATOR",
      "DEPOSITION_VIEWER",
      "APPLICATION_CREATOR",
      "APPLICATION_VIEWER",
      "HEARING_VIEWER",
      "ORDER_VIEWER",
      "SUBMISSION_CREATOR",
      "SUBMISSION_RESPONDER",
    ],
    config: TabLitigantSearchConfig,
    isLitigant: true,
    showJoinFileOption: true,
    onRowClickRoute: {
      dependentUrl: "/dristi/home/file-case/case",
      urlDependentOn: "status",
      urlDependentValue: ["DRAFT_IN_PROGRESS", "CASE_RE_ASSIGNED"],
      params: [{ key: "caseId", value: "id" }],
    },
  },
  {
    roles: ["CASE_APPROVER"],
    config: TabJudgeSearchConfig,
    isJudge: true,
    onRowClickRoute: {
      dependentUrl: "/dristi/admission",
      urlDependentOn: "status",
      urlDependentValue: "PENDING_ADMISSION",
      params: [
        { key: "filingNumber", value: "filingNumber" },
        { key: "caseId", value: "id" },
      ],
    },
  },
  {
    roles: ["HEARING_CREATOR"],
    config: TabJudgeSearchConfig,
    isCourtOfficer: true,
    onRowClickRoute: {
      dependentUrl: "/dristi/admission",
      urlDependentOn: "status",
      urlDependentValue: "PENDING_ADMISSION",
      params: [
        { key: "filingNumber", value: "filingNumber" },
        { key: "caseId", value: "id" },
      ],
    },
  },
  {
    roles: ["CASE_REVIEWER"],
    config: TabFSOSearchConfig,
    isFSO: true,
    onRowClickRoute: {
      dependentUrl: "/dristi/case",
      urlDependentOn: "status",
      urlDependentValue: "UNDER_SCRUTINY",
      params: [{ key: "caseId", value: "id" }],
    },
  },
];

export const pendingTaskCaseActions = {
  PAYMENT_PENDING: {
    actorName: ["LITIGANT/ADVOCATE"],
    actionName: "Make Payment",
    redirectDetails: {
      url: "/home/home-pending-task/e-filing-payment-breakdown",
    },
  },
  UNDER_SCRUTINY: {
    actorName: ["FSO"],
    actionName: "Case Filed and ready for FSO to review",
    redirectDetails: {
      url: "/dristi/case",
      params: [{ key: "caseId", value: "id" }],
    },
  },
  CASE_RE_ASSIGNED: {
    actorName: ["LITIGANT/ADVOCATE"],
    actionName: "Case Sent Back for Edit",
    redirectDetails: {
      url: "/dristi/home/file-case/case",
      params: [{ key: "caseId", value: "id" }],
    },
  },
  PENDING_ADMISSION: {
    actorName: ["JUDGE"],
    actionName: "Case Approved from Scrutiny",
    redirectDetails: {
      url: "/dristi/admission",
      params: [
        { key: "filingNumber", value: "filingNumber" },
        { key: "caseId", value: "id" },
      ],
    },
  },
  SCHEDULE_ADMISSION_HEARING: {
    actorName: ["JUDGE"],
    actionName: "Schedule admission hearing",
    redirectDetails: {
      url: "/orders/generate-orders",
      params: [{ key: "filingNumber", value: "filingNumber" }],
    },
  },
  ADMISSION_HEARING_SCHEDULED: {
    actorName: ["JUDGE"],
    actionName: "Admission hearing scheduled - Admit Case",
    redirectDetails: {
      url: "/orders/generate-orders",
      params: [{ key: "filingNumber", value: "filingNumber" }],
    },
  },
  CASE_ADMITTED: {
    actorName: ["JUDGE"],
    actionName: "Schedule admission hearing",
    redirectDetails: {
      url: "/orders/generate-orders",
      params: [{ key: "filingNumber", value: "filingNumber" }],
    },
  },
};

export const pendingTaskSubmissionActions = {};
