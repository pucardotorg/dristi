const defaultSearchValues = {
  filingNumber: "",
  caseType: "NIA S138",
  substage: "",
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
      "SUBMISSION_DELETE",
      "TASK_VIEWER",
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
      "SUBMISSION_DELETE",
      "TASK_VIEWER",
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
      "SUBMISSION_DELETE",
      "TASK_VIEWER",
    ],
    apiDetails: {
      serviceName: "/advocate/clerk/v1/_create",
      requestKey: "clerk",
      AdditionalFields: ["stateRegnNumber"],
    },

    subText: "ADVOCATE_CLERK_SUB_TEXT",
  },
];

export const TabFSOSearchConfig = {
  tenantId: "pg",
  moduleName: "homeFSOUiConfig",
  showTab: true,
  TabSearchConfig: [
    {
      label: "All",
      type: "search",
      apiDetails: {
        serviceName: "/case/v1/_search",
        requestParam: {},
        requestBody: {
          tenantId: "pg",
          criteria: [
            {
              status: ["UNDER_SCRUTINY"],
            },
          ],
        },
        masterName: "commonUiConfig",
        moduleName: "homeFSOUiConfig",
        minParametersForSearchForm: 0,
        tableFormJsonPath: "requestBody",
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
            defaultValues: {
              ...defaultSearchValues,
              sortCaseListByDate: {
                sortBy: "createdtime",
                order: "desc",
              },
            },
            fields: [
              {
                type: "component",
                component: "CustomSortComponent",
                isMandatory: false,
                disable: false,
                name: "Filed",
                key: "sortCaseListByDate",
                sortBy: "createdtime",
                ascText: "(old first)",
                descText: "(new first)",
                showAdditionalText: true,
                showIcon: true,
                icon: "UpDownArrowIcon",
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
                label: "Status",
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
                    maxWidth: "250px",
                    minWidth: "200px",
                  },
                  optionsCustomStyle: {
                    overflowX: "hidden",
                  },
                },
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
        serviceName: "/case/v1/_search",
        requestParam: {},
        requestBody: {
          tenantId: "pg",
          criteria: [
            {
              status: ["UNDER_SCRUTINY"],
            },
          ],
        },
        masterName: "commonUiConfig",
        moduleName: "homeFSOUiConfig",
        minParametersForSearchForm: 0,
        tableFormJsonPath: "requestBody",
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
                label: "Status",
                isMandatory: false,
                key: "stage",
                type: "dropdown",
                disable: false,
                populators: {
                  name: "substage",
                  options: [],
                  styles: {
                    maxWidth: "250px",
                    minWidth: "200px",
                  },
                  optionsCustomStyle: {
                    overflowX: "hidden",
                  },
                },
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
                label: "Case Name",
                jsonPath: "caseTitle",
              },
              {
                label: "Scrutiny Status",
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
