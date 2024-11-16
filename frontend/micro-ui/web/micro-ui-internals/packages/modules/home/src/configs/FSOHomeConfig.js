const defaultSearchValues = {
  filingNumber: "",
  caseType: "NIA S138",
  substage: "",
};

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
      "EVIDENCE_CREATOR",
      "EVIDENCE_VIEWER",
      "APPLICATION_CREATOR",
      "APPLICATION_VIEWER",
      "HEARING_VIEWER",
      "ORDER_VIEWER",
      "SUBMISSION_CREATOR",
      "SUBMISSION_RESPONDER",
      "SUBMISSION_DELETE",
      "TASK_VIEWER",
      "ADVOCATE_VIEWER",
      "PENDING_TASK_CREATOR",
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
      "EVIDENCE_CREATOR",
      "EVIDENCE_VIEWER",
      "APPLICATION_CREATOR",
      "APPLICATION_VIEWER",
      "HEARING_VIEWER",
      "ORDER_VIEWER",
      "SUBMISSION_CREATOR",
      "SUBMISSION_RESPONDER",
      "SUBMISSION_DELETE",
      "TASK_VIEWER",
      "ADVOCATE_VIEWER",
      "ADVOCATE_APPLICATION_VIEWER",
      "PENDING_TASK_CREATOR",
    ],
    apiDetails: {
      serviceName: "/advocate/v1/_create",
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
      "EVIDENCE_CREATOR",
      "EVIDENCE_VIEWER",
      "APPLICATION_CREATOR",
      "APPLICATION_VIEWER",
      "HEARING_VIEWER",
      "ORDER_VIEWER",
      "SUBMISSION_CREATOR",
      "SUBMISSION_RESPONDER",
      "SUBMISSION_DELETE",
      "TASK_VIEWER",
      "ADVOCATE_VIEWER",
      "PENDING_TASK_CREATOR",
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
      label: "CS_ALL",
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
                label: "CASE_TYPE",
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
                label: "CS_STAGE",
                isMandatory: false,
                key: "stage",
                type: "dropdown",
                disable: false,
                populators: {
                  name: "substage",
                  optionsKey: "code",
                  mdmsConfig: {
                    masterName: "SubStage",
                    moduleName: "case",
                    select: "(data) => {return data['case'].SubStage?.map((item) => {return item});}",
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
                label: "CS_CASE_ID",
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
                label: "CS_CASE_NAME",
                jsonPath: "caseTitle",
              },
              {
                label: "CS_STAGE",
                jsonPath: "substage",
              },
              {
                label: "CS_CASE_ID",
                jsonPath: "filingNumber",
              },
              {
                label: "CASE_TYPE",
                jsonPath: "",
                additionalCustomization: true,
              },
              {
                label: "CS_DAYS_FILING",
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
      label: "CS_SCRUTINY_DUE",
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
                label: "CASE_TYPE",
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
                label: "CS_STAGE",
                isMandatory: false,
                key: "stage",
                type: "dropdown",
                disable: false,
                populators: {
                  name: "substage",
                  optionsKey: "code",
                  mdmsConfig: {
                    masterName: "SubStage",
                    moduleName: "case",
                    select: "(data) => {return data['case'].SubStage?.map((item) => {return item});}",
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
                label: "CS_CASE_ID",
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
                label: "CS_CASE_NAME",
                jsonPath: "caseTitle",
              },
              {
                label: "CS_SCRUTINY_STATUS",
                jsonPath: "status",
                additionalCustomization: true,
              },
              {
                label: "CS_CASE_ID",
                jsonPath: "filingNumber",
              },
              {
                label: "CASE_TYPE",
                jsonPath: "",
                additionalCustomization: true,
              },
              {
                label: "CS_DAYS_FILING",
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
  ],
};
