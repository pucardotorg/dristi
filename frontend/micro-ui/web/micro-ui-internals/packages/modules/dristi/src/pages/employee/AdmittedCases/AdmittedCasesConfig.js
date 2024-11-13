// default values of search input component
const defaultSearchValues = {
  owner: {},
  parties: {},
  hearingType: {},
  orderType: {},
  status: {},
  orderNumber: "",
  applicationType: {},
  applicationNumber: "",
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
      type: "search",
    },
    {
      label: "Complaint",
      type: "search",
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
            defaultValues: defaultSearchValues,
            fields: [
              {
                label: "Type",
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
                label: "Hearing Type",
                jsonPath: "hearingType",
                additionalCustomization: true,
              },
              // {
              //   label: "Stage",
              //   jsonPath: "",
              // },
              {
                label: "Parties",
                jsonPath: "attendees",
                additionalCustomization: true,
              },
              {
                label: "Status",
                jsonPath: "status",
                additionalCustomization: true,
              },
              {
                label: "Date",
                jsonPath: "startTime",
                additionalCustomization: true,
              },
              // {
              //   label: "Date Added",
              //   jsonPath: "auditDetails.createdTime",
              //   additionalCustomization: true,
              // },
              {
                label: "Actions",
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
                label: "Type",
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
                label: "Status",
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
                label: "Search Order Id",
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
                label: "Order Type",
                jsonPath: "orderType",
                additionalCustomization: true,
              },
              {
                label: "Order Id",
                jsonPath: "orderNumber",
              },
              // {
              //   label: "Stage",
              //   jsonPath: "",
              // },
              {
                label: "Parties",
                jsonPath: "orderDetails.parties",
                additionalCustomization: true,
              },
              {
                label: "Status",
                jsonPath: "status",
                additionalCustomization: true,
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
                label: "Type",
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
                label: "Status",
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
                label: "Search Submission Id",
                isMandatory: false,
                key: "applicationNumber",
                type: "text",
                populators: {
                  name: "applicationNumber",
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
                label: "Submission Type",
                jsonPath: "applicationType",
                additionalCustomization: true,
              },
              {
                label: "Submission Id",
                jsonPath: "applicationNumber",
              },
              // {
              //   label: "Stage",
              //   jsonPath: "",
              // },
              {
                label: "Status",
                jsonPath: "status",
                additionalCustomization: true,
              },
              {
                label: "Owner",
                jsonPath: "owner",
                additionalCustomization: true,
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
      label: "Documents",
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
                label: "Type",
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
                      "(data) => {return data['Evidence'].EvidenceType?.map((item) => {return { ...item, name: item.subtype && item.subtype !== '' ? `${item.type} (${item.subtype})` : item.type };});}",
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
                label: "Search Artifact Number",
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
                label: "Document Type",
                jsonPath: "artifactType",
                additionalCustomization: true,
              },
              {
                label: "Artifact Number",
                jsonPath: "artifactNumber",
              },
              {
                label: "Evidence Number",
                jsonPath: "evidenceNumber",
              },
              {
                label: "Source",
                jsonPath: "sourceType",
                additionalCustomization: true,
              },
              {
                label: "Owner",
                jsonPath: "owner",
              },
              {
                label: "Date Added",
                jsonPath: "auditdetails.createdTime",
                additionalCustomization: true,
              },
              {
                label: "File",
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
                label: "Party Name",
                jsonPath: "name",
                additionalCustomization: true,
              },
              {
                label: "Party Type",
                jsonPath: "partyType",
                additionalCustomization: true,
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
