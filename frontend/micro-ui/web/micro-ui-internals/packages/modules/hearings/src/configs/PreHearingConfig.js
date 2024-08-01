const defaultSearchValues = {
  stage: "",
  type: "",
  caseNameOrId: "",
};

export const preHearingConfig = {
  label: "ES_COMMON_HEARING",
  type: "search",
  customHookName: "hearings.usePreHearingModalData",
  apiDetails: {
    serviceName: "/hearing/v1/search",
    requestParam: {
      tenantId: Digit.ULBService.getCurrentTenantId(),
    },
    requestBody: {
      criteria: {},
    },
    minParametersForSearchForm: 0,
    masterName: "commonUiConfig",
    moduleName: "PreHearingsConfig",
    searchFormJsonPath: "requestBody.criteria",
    filterFormJsonPath: "requestBody.criteria",
    tableFormJsonPath: "requestBody.criteria",
  },
  sections: {
    search: {
      uiConfig: {
        formClassName: "inbox-filter",
        primaryLabel: "ES_COMMON_SEARCH",
        secondaryLabel: "ES_COMMON_CLEAR_SEARCH",
        minReqFields: 0,
        defaultValues: defaultSearchValues,
        searchWrapperStyles: {
          marginLeft: "auto",
        },
        fields: [
          {
            label: "Type",
            isMandatory: false,
            key: "type",
            type: "dropdown",
            populators: {
              styles: { width: "150px" },
              name: "type",
              error: "Required",
              optionsKey: "type",
              options: [
                {
                  type: "NIA S138",
                },
                {
                  type: "CIA S138",
                },
              ],
            },
          },
          {
            label: "Stage",
            isMandatory: false,
            key: "stage",
            type: "dropdown",
            populators: {
              styles: { width: "150px" },
              name: "stage",
              error: "Required",
              optionsKey: "stage",
              options: [
                {
                  stage: ["Pre-Trial"],
                },
                {
                  stage: ["Inquiry"],
                },
              ],
            },
          },
          {
            label: "Search Case Name or ID",
            isMandatory: false,
            key: "caseNameOrId",
            type: "text",
            populators: {
              name: "caseNameOrId",
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
            jsonPath: "caseName",
          },
          {
            label: "Stage",
            jsonPath: "stage",
          },
          {
            label: "Case Type",
            jsonPath: "caseType",
          },
          {
            label: "Pending Tasks",
            jsonPath: "pendingTasks",
          },
          {
            label: "Actions",
            jsonPath: "",
            additionalCustomization: true,
          },
        ],
        enableColumnSort: true,
        resultsJsonPath: "items",
      },
      show: true,
    },
  },
};
