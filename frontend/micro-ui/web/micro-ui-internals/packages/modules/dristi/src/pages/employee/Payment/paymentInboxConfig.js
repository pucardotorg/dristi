export const paymentInboxConfig = {
  label: "ES_COMMON_INBOX",
  type: "inbox",
  apiDetails: {
    serviceName: "/case/v1/_search",
    requestParam: {},
    requestBody: {
      criteria: [
        {
          defaultValues: true,
          status: ["PAYMENT_PENDING"],
          filingNumber: "",
        },
      ],
    },
    minParametersForSearchForm: 1,
    masterName: "commonUiConfig",
    moduleName: "paymentInboxConfig",
    searchFormJsonPath: "requestBody.criteria[0]",
    tableFormJsonPath: "requestBody.inbox",
  },
  sections: {
    search: {
      uiConfig: {
        headerStyle: null,
        type: "registration-requests-table-search",
        primaryLabel: "ES_COMMON_SEARCH",
        secondaryLabel: "ES_COMMON_CLEAR_SEARCH",
        minReqFields: 0,
        defaultValues: {
          filingNumber: "",
          isActive: false,
          stage: [],
        },
        fields: [
          {
            type: "component",
            component: "CustomSortComponent",
            isMandatory: false,
            disable: false,
            name: "Sort by",
            key: "sortCaseListByDate",
            sortBy: "createdtime",
            ascText: "Newest to Oldest",
            descText: "Oldest to Newest",
            showAdditionalText: true,
            showIcon: true,
            icon: "ArrowDownIcon",
            populators: {},
          },
          {
            label: "Stage",
            isMandatory: false,
            key: "stage",
            type: "dropdown",
            disable: false,
            populators: {
              name: "substage",
              options: ["Filing", "Cognizance", "Inquiry", "Appearance", "Framing of charges", "Evidence", "Arguments", "Judgment", "Post-Judgement"],
              styles: {
                maxWidth: "300px",
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
              placeholder: "Case ID",
              error: "BR_PATTERN_ERR_MSG",
              validation: {
                pattern: {},
                minlength: 2,
              },
            },
          },
        ],
      },
      children: {},
      show: true,
    },
    searchResult: {
      label: "",
      uiConfig: {
        columns: [
          {
            label: "Case ID",
            jsonPath: "filingNumber",
            additionalCustomization: true,
          },
          {
            label: "Case Name",
            jsonPath: "caseTitle",
          },
          {
            label: "Case Type",
            jsonPath: "caseType",
            additionalCustomization: true,
          },
          {
            label: "Stage",
            jsonPath: "substage",
          },
          // {
          //   label: "Amount Due",
          //   jsonPath: "amountDue",
          //   additionalCustomization: true,
          // },
          {
            label: "Action",
            jsonPath: "id",
            additionalCustomization: true,
          },
        ],
        enableGlobalSearch: false,
        enableColumnSort: true,
        resultsJsonPath: "criteria[0].responseList",
      },
      children: {},
      show: true,
    },
  },
  additionalSections: {},
  additionalDetails: { filingNumber: "", stage: "", sortBy: "sortCaseListByDate" },
};
