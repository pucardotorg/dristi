export const preHearingConfig = {
  label: "ES_COMMON_HEARING",
  type: "search",
  postProcessResult: true,
  customHookName: "hearings.usePreHearingModalData",
  apiDetails: {
    serviceName: "/hearing/v1/search",
    requestParam: {
      tenantId: Digit.ULBService.getCurrentTenantId(),
    },
    requestBody: {
      //need to add criteria to get hearings based on particular day
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
    filter: {
      uiConfig: {
        fields: [
          {
            type: "text",
            isMandatory: false,
            disable: false,
            populators: {
              name: "caseTitle",
              placeholder: "Newest First",
              selectedText: "COMMON_SELECTED",
            },
          },
          {
            type: "dropdown",
            isMandatory: false,
            disable: false,
            populators: {
              name: "type",
              optionsKey: "name",
              defaultText: "Type",
              selectedText: "COMMON_SELECTED",
              options: [
                {
                  code: "nias138",
                  name: "NIA S138",
                },
                {
                  code: "cias138",
                  name: "CIA S138",
                },
              ],
            },
          },
          {
            type: "dropdown",
            isMandatory: false,
            disable: false,
            populators: {
              name: "stage",
              optionsKey: "name",
              defaultText: "Stage",
              selectedText: "COMMON_SELECTED",
              options: [
                {
                  code: "Pre Trial",
                  name: "Pre Trial",
                },
                {
                  code: "Inquiry",
                  name: "Inquiry",
                },
              ],
            },
          },
          {
            type: "text",
            isMandatory: false,
            disable: false,
            populators: {
              name: "caseTitle",
              placeholder: "Search Case Name or ID",
              selectedText: "COMMON_SELECTED",
            },
          },
        ],
      },
      show: true,
    },
    searchResult: {
      tenantId: Digit.ULBService.getCurrentTenantId(),
      label: "",
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
            jsonPath: "caseDescription",
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
