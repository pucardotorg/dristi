export const preHearingConfig = {
  label: "ES_COMMON_HEARING",
  type: "inbox",
  customHookName: "hearings.usePreHearingModalData",
  apiDetails: {
    serviceName: "/hearing/v1/search",
    requestParam: {
      tenantId: Digit.ULBService.getCurrentTenantId(),
    },
    requestBody: {
      //need to add criteria to get hearings based on particular day
      criteria: {
        limit: 5,
      },
    },
    minParametersForSearchForm: 0,
    masterName: "commonUiConfig",
    moduleName: "PreHearingsConfig",
    searchFormJsonPath: "requestBody.inbox.moduleSearchCriteria",
    filterFormJsonPath: "requestBody.Criteria",
    tableFormJsonPath: "requestParam",
  },
  sections: {
    filter: {
      uiConfig: {
        fields: [
          {
            type: "locationdropdown",
            isMandatory: false,
            disable: false,
            populators: {
              name: "ward",
              type: "ward",
              optionsKey: "i18nKey",
              defaultText: "Type",
              selectedText: "COMMON_SELECTED",
              allowMultiSelect: true,
            },
          },
          {
            type: "locationdropdown",
            isMandatory: false,
            disable: false,
            populators: {
              name: "ward",
              type: "ward",
              optionsKey: "i18nKey",
              defaultText: "Stage",
              selectedText: "COMMON_SELECTED",
              allowMultiSelect: true,
            },
          },
          {
            type: "text",
            isMandatory: false,
            disable: false,
            populators: {
              name: "ward",
              type: "ward",
              optionsKey: "i18nKey",
              defaultText: "Stage",
              selectedText: "COMMON_SELECTED",
              allowMultiSelect: true,
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
