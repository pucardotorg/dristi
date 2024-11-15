// default values of search input component
const defaultSearchValues = {
  owner: {},
  parties: {},
  hearingType: {},
  orderType: {},
  status: {},
  orderNumber: "",
  applicationType: {},
  applicationCMPNumber: "",
  artifactType: {},
  artifactNumber: "",
};

//config for tab search sceeen
export const TabSearchconfig = {
  tenantId: "mz",
  moduleName: "commonCampaignUiConfig",
  showTab: true,
  TabSearchconfig: [
    {
      label: "Overview",
      displayLabel: "OVERVIEW_TAB",
      type: "search",
    },
    {
      label: "Complaint",
      displayLabel: "COMPLAINT_TAB",
      type: "search",
    },
    {
      label: "Hearings",
      displayLabel: "HEARINGS_TAB",
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
            defaultValues: defaultSearchValues,
            fields: [
              {
                label: "TYPE",
                isMandatory: false,
                key: "hearingType",
                type: "dropdown",
                populators: {
                  name: "hearingType",
                  optionsKey: "type",
                  mdmsConfig: {
                    masterName: "HearingType",
                    moduleName: "Hearing",
                    // localePrefix: "SUBMISSION_TYPE",
                  },
                },
              },
              // {
              //   label: "Stage",
              //   isMandatory: false,
              //   key: "stage",
              //   type: "dropdown",
              //   populators: {
              //     name: "stage",
              //     optionsKey: "value",
              //     mdmsConfig: {
              //       masterName: "Stage",
              //       moduleName: "case",
              //       // localePrefix: "SUBMISSION_TYPE",
              //     },
              //   },
              // },
              // {
              //   label: "Parties",
              //   isMandatory: false,
              //   key: "parties",
              //   type: "dropdown",
              //   populators: {
              //     name: "parties",
              //   },
              // },
              // {
              //   label: "Order ID",
              //   isMandatory: false,
              //   key: "orderNumber",
              //   type: "text",
              //   populators: {
              //     name: "orderNumber",
              //   },
              // },
            ],
          },
          show: true,
        },
        searchResult: {
          tenantId: Digit.ULBService.getCurrentTenantId(),
          uiConfig: {
            columns: [
              {
                label: "HEARING_TYPE",
                jsonPath: "hearingType",
                additionalCustomization: true,
              },
              // {
              //   label: "Stage",
              //   jsonPath: "",
              // },
              {
                label: "PARTIES",
                jsonPath: "attendees",
                additionalCustomization: true,
              },
              {
                label: "STATUS",
                jsonPath: "status",
                additionalCustomization: true,
              },
              {
                label: "DATE",
                jsonPath: "startTime",
                additionalCustomization: true,
              },
              // {
              //   label: "Date Added",
              //   jsonPath: "auditDetails.createdTime",
              //   additionalCustomization: true,
              // },
              {
                label: "CS_ACTIONS",
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
      displayLabel: "ORDERS_TAB",
      type: "search",
      apiDetails: {
        serviceName: "/order/v1/search",
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
            defaultValues: defaultSearchValues,
            fields: [
              {
                label: "TYPE",
                isMandatory: false,
                key: "orderType",
                type: "dropdown",
                populators: {
                  name: "orderType",
                  optionsKey: "type",
                  mdmsConfig: {
                    masterName: "OrderType",
                    moduleName: "Order",
                    // localePrefix: "SUBMISSION_TYPE",
                  },
                },
              },
              // {
              //   label: "Stage",
              //   isMandatory: false,
              //   key: "stage",
              //   type: "dropdown",
              //   populators: {
              //     name: "stage",
              //     optionsKey: "value",
              //     mdmsConfig: {
              //       masterName: "Stage",
              //       moduleName: "case",
              //       // localePrefix: "SUBMISSION_TYPE",
              //     },
              //   },
              // },
              {
                label: "STATUS",
                isMandatory: false,
                key: "status",
                type: "dropdown",
                populators: {
                  name: "status",
                  optionsKey: "type",
                  mdmsConfig: {
                    masterName: "OrderStatus",
                    moduleName: "Order",
                    // localePrefix: "SUBMISSION_TYPE",
                  },
                },
              },
              {
                label: "SEARCH_ORDER_ID",
                isMandatory: false,
                key: "orderNumber",
                type: "text",
                populators: {
                  name: "orderNumber",
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
                label: "ORDER_TYPE",
                jsonPath: "orderType",
                additionalCustomization: true,
              },
              {
                label: "ORDER_ID",
                jsonPath: "orderNumber",
              },
              // {
              //   label: "Stage",
              //   jsonPath: "",
              // },
              {
                label: "PARTIES",
                jsonPath: "orderDetails.parties",
                additionalCustomization: true,
              },
              {
                label: "STATUS",
                jsonPath: "status",
                additionalCustomization: true,
              },
              {
                label: "DATE_ADDED",
                jsonPath: "createdDate",
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
      displayLabel: "SUBMISSIONS_TAB",
      type: "search",
      customHookName: "dristi.useApplicationDetails",
      apiDetails: {
        serviceName: "/application/v1/search",
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
            defaultValues: defaultSearchValues,
            fields: [
              {
                label: "TYPE",
                isMandatory: false,
                key: "applicationType",
                type: "dropdown",
                populators: {
                  name: "applicationType",
                  optionsKey: "type",
                  mdmsConfig: {
                    masterName: "ApplicationType",
                    moduleName: "Application",
                    // localePrefix: "SUBMISSION_TYPE",
                  },
                },
              },
              // {
              //   label: "Stage",
              //   isMandatory: false,
              //   key: "stage",
              //   type: "dropdown",
              //   populators: {
              //     name: "stage",
              //     optionsKey: "value",
              //     mdmsConfig: {
              //       masterName: "Stage",
              //       moduleName: "case",
              //       // localePrefix: "SUBMISSION_TYPE",
              //     },
              //   },
              // },
              {
                label: "STATUS",
                isMandatory: false,
                key: "status",
                type: "dropdown",
                populators: {
                  name: "status",
                  optionsKey: "type",
                  mdmsConfig: {
                    masterName: "ApplicationStatus",
                    moduleName: "Application",
                    // localePrefix: "SUBMISSION_TYPE",
                  },
                },
              },
              {
                label: "SEARCH_SUBMISSION_ID",
                isMandatory: false,
                key: "applicationCMPNumber",
                type: "text",
                populators: {
                  name: "applicationCMPNumber",
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
                label: "SUBMISSION_TYPE",
                jsonPath: "applicationType",
                additionalCustomization: true,
              },
              {
                label: "SUBMISSION_ID",
                jsonPath: "applicationCMPNumber",
                additionalCustomization: true,
              },
              // {
              //   label: "Stage",
              //   jsonPath: "",
              // },
              {
                label: "STATUS",
                jsonPath: "status",
                additionalCustomization: true,
              },
              {
                label: "OWNER",
                jsonPath: "owner",
                additionalCustomization: true,
              },
              {
                label: "DATE_ADDED",
                jsonPath: "auditDetails.createdTime",
                additionalCustomization: true,
              },
              {
                label: "DOCUMENT_TEXT",
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
      label: "Documents",
      displayLabel: "DOCUMENTS_TAB",
      type: "search",
      customHookName: "dristi.useEvidenceDetails",
      apiDetails: {
        serviceName: "/evidence/v1/_search",
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
            defaultValues: defaultSearchValues,
            fields: [
              {
                label: "TYPE",
                isMandatory: false,
                key: "artifactType",
                type: "dropdown",
                populators: {
                  name: "artifactType",
                  optionsKey: "name",
                  mdmsConfig: {
                    masterName: "EvidenceType",
                    moduleName: "Evidence",
                    localePrefix: "EVIDENCE_TYPE",
                    select:
                      "(data) => {return data['Evidence'].EvidenceType?.map((item) => {return { ...item, name: item.subtype && item.subtype !== '' ? `${item.type}_${item.subtype}` : item.type };});}",
                    // localePrefix: "SUBMISSION_TYPE",
                  },
                },
              },
              // customDefaultPagination: {
              //   searchForm: {},
              //   filterForm: {},
              //   tableForm: {
              //     limit: 10,
              //     offset: 0,
              //   },
              // },
              // {
              //   label: "Stage",
              //   isMandatory: false,
              //   key: "stage",
              //   type: "dropdown",
              //   populators: {
              //     name: "stage",
              //     optionsKey: "value",
              //     mdmsConfig: {
              //       masterName: "Stage",
              //       moduleName: "case",
              //       // localePrefix: "SUBMISSION_TYPE",
              //     },
              //   },
              // },
              // {
              //   label: "Status",
              //   isMandatory: false,
              //   key: "status",
              //   type: "dropdown",
              //   populators: {
              //     name: "status",
              //     optionsKey: "value",
              //     mdmsConfig: {
              //       masterName: "Status",
              //       moduleName: "case",
              //       // localePrefix: "SUBMISSION_TYPE",
              //     },
              //   },
              // },
              {
                label: "SEARCH_ARTIFACT_NUMBER",
                isMandatory: false,
                key: "artifactNumber",
                type: "text",
                populators: {
                  name: "artifactNumber",
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
                label: "DOCUMENT_TYPE",
                jsonPath: "artifactType",
                additionalCustomization: true,
              },
              {
                label: "ARTIFACT_NUMBER",
                jsonPath: "artifactNumber",
              },
              {
                label: "EVIDENCE_NUMBER",
                jsonPath: "evidenceNumber",
              },
              {
                label: "SOURCE",
                jsonPath: "sourceType",
                additionalCustomization: true,
              },
              {
                label: "OWNER",
                jsonPath: "owner",
              },
              {
                label: "DATE_ADDED",
                jsonPath: "auditdetails.createdTime",
                additionalCustomization: true,
              },
              {
                label: "FILE",
                jsonPath: "file",
                additionalCustomization: true,
              },
            ],

            enableColumnSort: true,
            resultsJsonPath: "artifacts",
          },
          show: true,
        },
      },
    },
    // {
    //   label: "History",
    //   type: "search",
    //   apiDetails: {
    //     serviceName: "/casemanagement/casemanager/case/v1/_history",
    //     requestParam: {
    //       tenantId: Digit.ULBService.getCurrentTenantId(),
    //     },
    //     requestBody: {
    //       criteria: {
    //         tenantId: Digit.ULBService.getCurrentTenantId(),
    //       },
    //     },
    //     masterName: "commonUiConfig",
    //     moduleName: "HistoryConfig",
    //     minParametersForSearchForm: 0,
    //     tableFormJsonPath: "requestParam",
    //     filterFormJsonPath: "requestBody.Individual",
    //     searchFormJsonPath: "requestBody.Individual",
    //   },
    //   sections: {
    //     // search: {
    //     //   uiConfig: {
    //     //     formClassName: "custom-both-clear-search",
    //     //     primaryLabel: "ES_COMMON_SEARCH",
    //     //     secondaryLabel: "ES_COMMON_CLEAR_SEARCH",
    //     //     minReqFields: 0,
    //     //     defaultValues: defaultSearchValues,
    //     //     fields: [
    //     //       // {
    //     //       //   label: "Stage",
    //     //       //   isMandatory: false,
    //     //       //   key: "stage",
    //     //       //   type: "dropdown",
    //     //       //   populators: {
    //     //       //     name: "stage",
    //     //       //     optionsKey: "value",
    //     //       //     mdmsConfig: {
    //     //       //       masterName: "Stage",
    //     //       //       moduleName: "case",
    //     //       //       // localePrefix: "SUBMISSION_TYPE",
    //     //       //     },
    //     //       //   },
    //     //       // },
    //     //       {
    //     //         label: "Owner",
    //     //         isMandatory: false,
    //     //         key: "owner",
    //     //         type: "dropdown",
    //     //         populators: {
    //     //           name: "owner",
    //     //         },
    //     //       },
    //     //     ],
    //     //   },
    //     //   show: false,
    //     // },
    //     searchResult: {
    //       tenantId: Digit.ULBService.getCurrentTenantId(),
    //       uiConfig: {
    //         columns: [
    //           {
    //             label: "Instance",
    //             jsonPath: "instance",
    //             additionalCustomization: true,
    //           },
    //           {
    //             label: "Date",
    //             jsonPath: "date",
    //             additionalCustomization: true,
    //           },
    //           // {
    //           //   label: "Stage",
    //           //   jsonPath: "stage",
    //           // },
    //           {
    //             label: "Status",
    //             jsonPath: "status",
    //             additionalCustomization: true,
    //           },
    //         ],
    //         enableColumnSort: true,
    //         resultsJsonPath: "history",
    //       },
    //       show: true,
    //     },
    //   },
    // },
    {
      label: "Parties",
      displayLabel: "PARTIES_TAB",
      type: "search",
      apiDetails: {
        serviceName: "/case/v1/_search",
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
          uiConfig: {},
          show: false,
        },
        searchResult: {
          tenantId: Digit.ULBService.getCurrentTenantId(),
          uiConfig: {
            columns: [
              {
                label: "PARTY_NAME",
                jsonPath: "name",
                additionalCustomization: true,
              },
              {
                label: "PARTY_TYPE",
                jsonPath: "partyType",
                additionalCustomization: true,
              },
              {
                label: "DATE_ADDED",
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
