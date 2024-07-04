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
      //   RequestInfo: {
      //     apiId: "Rainmaker",
      //     authToken: "035cede9-5323-4690-8ce0-16fd491994bb",
      //     msgId: "1717659830060|en_IN",
      //     plainAccessRequest: {},
      //   },
    },
    minParametersForSearchForm: 0,
    masterName: "commonUiConfig",
    moduleName: "PreHearingsConfig",
    searchFormJsonPath: "requestBody.inbox.moduleSearchCriteria",
    filterFormJsonPath: "requestBody.Criteria",
    tableFormJsonPath: "requestParam",
  },
  sections: {
    // search: {
    //   uiConfig: {
    //     formClassName: "custom-both-clear-search",
    //     primaryLabel: "ES_COMMON_SEARCH",
    //     secondaryLabel: "ES_COMMON_CLEAR_SEARCH",
    //     minReqFields: 0,
    //     defaultValues: defaultSearchValues, // Set default values for search fields
    //     fields: [
    //       // {
    //       //   label: "Type",
    //       //   isMandatory: false,
    //       //   key: "type",
    //       //   type: "dropdown",
    //       //   populators: {
    //       //     name: "type",
    //       //     error: "Required",
    //       //     validation: { pattern: /^[A-Za-z]+$/i },
    //       //   },
    //       // },
    //       // {
    //       //   label: "Stage",
    //       //   isMandatory: false,
    //       //   key: "stage",
    //       //   type: "dropdown",
    //       //   populators: {
    //       //     name: "stage",
    //       //     error: "Required",
    //       //     validation: { pattern: /^[A-Za-z]+$/i },
    //       //   },
    //       // },
    //       {
    //         label: "Search Case Id",
    //         isMandatory: false,
    //         key: "filingNumber",
    //         type: "text",
    //         populators: {
    //           name: "filingNumber",
    //           error: "Required",
    //           validation: { pattern: /^[A-Za-z]+$/i },
    //         },
    //       },
    //       // {
    //       //   label: "Phone number",
    //       //   isMandatory: false,
    //       //   key: "Phone number",
    //       //   type: "number",
    //       //   disable: false,
    //       //   populators: { name: "mobileNumber", error: "Hearings error message", validation: { min: 0, max: 999999999} },
    //       // },
    //       // {
    //       //   label: "Individual Id ",
    //       //   isMandatory: false,
    //       //   type: "text",
    //       //   disable: false,
    //       //   populators: {
    //       //     name: "individualId",
    //       //   },
    //       // },
    //     ],
    //   },

    //   show: true,
    // },
    filter: {
      uiConfig: {
        // type: "filter",
        // headerStyle: null,
        // // primaryLabel: "Filter",
        // secondaryLabel: "",
        // minReqFields: 1,
        // defaultValues: {
        //   state: "",
        //   ward: [],
        //   locality: [],
        //   assignee: {
        //     code: "ASSIGNED_TO_ALL",
        //     name: "EST_INBOX_ASSIGNED_TO_ALL",
        //   },
        // },
        fields: [
          {
            // label: "COMMON_WARD",
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
          {
            // label: "COMMON_WARD",
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
            // label: "COMMON_WARD",
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
        ],
      },
      //   label: "ES_COMMON_FILTERS",
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
            // additionalCustomization: true,
          },
          {
            label: "Stage",
            jsonPath: "stage",
            // additionalCustomization: true,
          },
          {
            label: "Case Type",
            jsonPath: "caseType",
            // additionalCustomization: true,
          },
          {
            label: "Pending Tasks",
            jsonPath: "pendingTasks",
            // additionalCustomization: true,
          },
          {
            label: "Actions",
            jsonPath: "caseDescription",
            additionalCustomization: true,
          },
        ],
        enableColumnSort: true,
        resultsJsonPath: "criteria[0].responseList",
      },
      // children: {},
      show: true,
    },
  },
};
