export const newconfigAdvocate = {
  label: "ES_COMMON_INBOX",
  type: "inbox",
  apiDetails: {
    serviceName: "/inbox/v2/_search",
    requestParam: {},
    requestBody: {
      inbox: {
        processSearchCriteria: {
          businessService: ["advocate"],
          moduleName: "Advocate services",
          tenantId: "pg",
        },
        moduleSearchCriteria: {
          tenantId: "pg",
        },
        tenantId: "pg",
        limit: 10,
        offset: 0,
      },
    },
    minParametersForSearchForm: 1,
    masterName: "commonUiConfig",
    moduleName: "registrationRequestsConfig",
    searchFormJsonPath: "requestBody",
    tableFormJsonPath: "requestBody.inbox",
  },
  sections: {
    search: {
      uiConfig: {
        headerStyle: null,
        type: "registration-requests-table-search",
        primaryLabel: "ES_COMMON_SEARCH",
        secondaryLabel: "ES_COMMON_CLEAR_SEARCH",
        minReqFields: 1,
        defaultValues: {
          applicationNumber: "",
        },
        fields: [
          {
            label: "Application No",
            type: "text",
            isMandatory: false,
            disable: false,
            populators: {
              name: "applicationNumber",
              error: "BR_PATTERN_ERR_MSG",
              validation: {
                pattern: {},
                minlength: 2,
              },
            },
          },
        ],
      },
      label: "",
      children: {},
      show: true,
    },
    searchResult: {
      label: "",
      uiConfig: {
        columns: [
          {
            label: "Application No",
            jsonPath: "businessObject.advocateDetails.applicationNumber",
            additionalCustomization: true,
          },
          {
            label: "User Name",
            jsonPath: "businessObject.advocateDetails.additionalDetails.username",
          },
          {
            label: "User Type",
            jsonPath: "ProcessInstance.businessService",
            additionalCustomization: true,
          },
          {
            label: "Date Created",
            jsonPath: "businessObject.auditDetails.createdTime",
            additionalCustomization: true,
          },
          {
            label: "Due Since (no of days)",
            jsonPath: "dueSince",
            additionalCustomization: true,
          },
          { label: "Action", jsonPath: "businessObject.individual.individualId", additionalCustomization: true },
        ],
        enableGlobalSearch: false,
        enableColumnSort: true,
        resultsJsonPath: "items",
      },
      children: {},
      show: true,
    },
  },
  additionalSections: {},
};

export const newconfigClerk = {
  label: "ES_COMMON_INBOX",
  type: "inbox",
  apiDetails: {
    serviceName: "/inbox/v2/_search",
    requestParam: {},
    requestBody: {
      inbox: {
        processSearchCriteria: {
          businessService: ["advocateclerk"],
          moduleName: "Advocate Clerk Service",
          tenantId: "pg",
        },
        moduleSearchCriteria: {
          tenantId: "pg",
        },
        tenantId: "pg",
        limit: 10,
        offset: 0,
      },
    },
    minParametersForSearchForm: 1,
    masterName: "commonUiConfig",
    moduleName: "registrationRequestsConfig",
    searchFormJsonPath: "requestBody",
    tableFormJsonPath: "requestBody.inbox",
  },
  sections: {
    search: {
      uiConfig: {
        headerStyle: null,
        type: "registration-requests-table-search",
        primaryLabel: "ES_COMMON_SEARCH",
        secondaryLabel: "ES_COMMON_CLEAR_SEARCH",
        minReqFields: 1,
        defaultValues: {
          applicationNumber: "",
        },
        fields: [
          {
            label: "Application No",
            type: "text",
            isMandatory: false,
            disable: false,
            populators: {
              name: "applicationNumber",
              error: "BR_PATTERN_ERR_MSG",
              validation: {
                pattern: {},
                minlength: 2,
              },
            },
          },
        ],
      },
      label: "",
      children: {},
      show: true,
    },
    searchResult: {
      label: "",
      uiConfig: {
        columns: [
          {
            label: "Application No",
            jsonPath: "businessObject.clerkDetails.applicationNumber",
            additionalCustomization: true,
          },
          {
            label: "User Name",
            jsonPath: "businessObject.clerkDetails.additionalDetails.username",
          },
          {
            label: "User Type",
            jsonPath: "ProcessInstance.businessService",
            additionalCustomization: true,
          },
          {
            label: "Date Created",
            jsonPath: "businessObject.auditDetails.createdTime",
            additionalCustomization: true,
          },
          {
            label: "Due Since (no of days)",
            jsonPath: "dueSince",
            additionalCustomization: true,
          },
          { label: "Action", jsonPath: "businessObject.individual.individualId", additionalCustomization: true },
        ],
        enableGlobalSearch: false,
        enableColumnSort: true,
        resultsJsonPath: "items",
      },
      children: {},
      show: true,
    },
  },
  additionalSections: {},
};

export const dropdownConfig = {
  label: "CS_ID_TYPE",
  type: "dropdown",
  name: "selectIdTypeType",
  optionsKey: "name",
  validation: {},
  isMandatory: true,
  options: [
    {
      code: "advocate",
      name: "Advocate",
    },
    {
      code: "clerk",
      name: "Clerk",
    },
  ],
  styles: {
    width: "200px",
  },
};
