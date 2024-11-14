const defaultSearchValues = {
  caseSearchText: "",
  caseType: "NIA S138",
  substage: "",
};

export const CaseWorkflowState = {
  CASE_REASSIGNED: "CASE_REASSIGNED",
  DRAFT_IN_PROGRESS: "DRAFT_IN_PROGRESS",
  UNDER_SCRUTINY: "UNDER_SCRUTINY",
  CASE_ADMITTED: "CASE_ADMITTED",
  PENDING_ADMISSION: "PENDING_ADMISSION",
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
      label: "CS_ONGOING",
      type: "search",
      apiDetails: {
        serviceName: "/case/v1/_search",
        requestParam: {},
        requestBody: {
          tenantId: "pg",
          criteria: [{}],
        },
        masterName: "commonUiConfig",
        moduleName: "homeLitigantUiConfig",
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
            defaultValues: defaultSearchValues,
            fields: [
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
                label: "CS_CASE_ID",
                type: "text",
                isMandatory: false,
                disable: false,
                populators: {
                  name: "caseSearchText",
                  error: "BR_PATTERN_ERR_MSG",
                  style: { maxWidth: "250px", minWidth: "200px", width: "220px" },
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
                additionalCustomization: true,
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
            ],

            enableColumnSort: true,
            resultsJsonPath: "criteria[0].responseList",
          },
          show: true,
        },
      },
    },
    {
      label: "CS_PENDING_SUBMISSION",
      type: "search",
      apiDetails: {
        serviceName: "/case/v1/_search",
        requestParam: {},
        requestBody: {
          tenantId: "pg",
          criteria: [
            {
              status: ["DRAFT_IN_PROGRESS", "PENDING_E-SIGN", "PENDING_E-SIGN-2"],
            },
          ],
        },
        masterName: "commonUiConfig",
        moduleName: "homeLitigantUiConfig",
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
            },
            fields: [
              {
                label: "CS_CASE_ID",
                type: "text",
                isMandatory: false,
                disable: false,
                populators: {
                  name: "caseSearchText",
                  error: "BR_PATTERN_ERR_MSG",
                  style: { maxWidth: "250px", minWidth: "200px", width: "220px" },
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
                additionalCustomization: true,
              },
              {
                label: "CS_STAGE",
                jsonPath: "status",
                additionalCustomization: true,
              },
              {
                label: "CASE_TYPE",
                jsonPath: "",
                additionalCustomization: true,
              },
              {
                label: "CS_LAST_EDITED",
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
      label: "CS_CLOSED",
      type: "search",
      apiDetails: {
        serviceName: "/case/v1/_search",
        requestParam: {},
        requestBody: {
          tenantId: "pg",
          criteria: [{ outcome: [] }],
        },
        masterName: "commonUiConfig",
        moduleName: "homeLitigantUiConfig",
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
            defaultValues: defaultSearchValues,
            fields: [
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
                label: "CS_OUTCOME",
                isMandatory: false,
                key: "outcome",
                type: "dropdown",
                disable: false,
                populators: {
                  name: "outcome",
                  optionsKey: "outcome",
                  mdmsConfig: {
                    masterName: "OutcomeType",
                    moduleName: "case",
                    select:
                      "(data) => {return data['case'].OutcomeType?.flatMap((item) => {return item.judgementList && item.judgementList.length > 0 ? item.judgementList.map(it => ({outcome: it})) : [item];});}",
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
                  name: "caseSearchText",
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
                jsonPath: "outcome",
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
                label: "CS_FILING_DATE",
                jsonPath: "filingDate",
                additionalCustomization: true,
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
