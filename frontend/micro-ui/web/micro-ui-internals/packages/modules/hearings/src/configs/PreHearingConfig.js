export const preHearingConfig = {
  label: "ES_COMMON_HEARING",
  type: "inbox",
  customHookName: "hearings.usePreHearingModalData",
  apiDetails: {
    serviceName: "/case/case/v1/_search",
    requestParam: {},
    requestBody: {
      tenantId: Digit.ULBService.getCurrentTenantId(),
      criteria: [
        {
          status: "CASE_ADMITTED",
          pagination: { limit: 5, offSet: 0 },
        },
      ],
      inbox: {
        moduleSearchCriteria: {},
        limit: 5,
        offset: 0,
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
            jsonPath: "caseTitle",
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
        resultsJsonPath: "caseResponse.criteria[0].responseList",
      },
      show: true,
    },
  },
};
