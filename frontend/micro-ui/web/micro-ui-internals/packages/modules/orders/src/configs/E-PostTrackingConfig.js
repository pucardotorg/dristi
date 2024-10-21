import { sortBy } from "lodash";

export const EpostTrackingConfig = ({ inboxFilters, outboxFilters }) => {
  const defaultSearchValues = {
    pagination: {
      sortBy: "",
      order: "",
    },
    deliveryStatusList: {},
    processNumber: "",
  };
  const TabSearchConfig = [
    {
      label: "All",
      type: "search",
      moduleName: "EpostTrackingUiConfig",
      apiDetails: {
        serviceName: "/epost-tracker/epost/v1/_getEPost",
        requestParam: {
          tenantId: Digit.ULBService.getCurrentTenantId(),
          limit: 10,
          offset: 0,
        },
        requestBody: {
          apiOperation: "SEARCH",
          Individual: {
            tenantId: Digit.ULBService.getCurrentTenantId(),
          },
          ePostTrackerSearchCriteria: {
            processNumber: "",
            deliveryStatusList: [],
            pagination: {
              orderBy: "",
              sortBy: "",
            },
          },
        },
        masterName: "commonUiConfig",
        moduleName: "EpostTrackingUiConfig",
        minParametersForSearchForm: 0,
        tableFormJsonPath: "requestParam",
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
                type: "component",
                component: "CustomSortComponent",
                isMandatory: false,
                disable: false,
                name: "Date Received",
                key: "pagination",
                sortBy: "receivedDate",
                ascText: "ASC",
                descText: "DESC",
                showAdditionalText: true,
                showIcon: true,
                icon: "UpDownArrowIcon",
                populators: {},
              },
              {
                isMandatory: false,
                type: "text",
                disable: false,
                showIcon: true,
                icon: "SearchIcon",
                populators: {
                  name: "processNumber",
                  placeholder: "Search Eprocess No",
                },
              },

              {
                type: "dropdown",
                isMandatory: false,
                disable: false,
                populators: {
                  defaultText: "Status",
                  styles: { width: "250px" },
                  name: "deliveryStatusList.selected",
                  options: ["IN_TRANSIT", "NOT_UPDATED", "DELIVERED", "NOT_DELIVERED"],
                },
              },
              // {
              //   type: "dropdown",
              //   isMandatory: false,
              //   disable: false,
              //   populators: {
              //     placeholder: "Type",
              //     styles: { width: "250px" },
              //     name: "type",
              //     options: [
              //       "IN_TRANSIT",
              //       "NOT_UPDATED",
              //     ],
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
                label: "E-Process No.",
                jsonPath: "processNumber",
              },
              {
                label: "Bar Code",
                jsonPath: "trackingNumber",
              },
              {
                label: "Pincode",
                jsonPath: "pinCode",
              },
              {
                label: "Address",
                jsonPath: "address",
              },
              {
                label: "Delivery Status",
                jsonPath: "deliveryStatus",
                additionalCustomization: true,
              },
              {
                label: "Remarks",
                jsonPath: "remarks",
              },
            ],
            enableColumnSort: true,
            resultsJsonPath: "EPostTracker",
          },
          show: true,
        },
      },
    },
    {
      label: "Inbox",
      type: "search",
      moduleName: "EpostTrackingUiConfig",
      apiDetails: {
        serviceName: "/epost-tracker/epost/v1/_getEPost",
        requestParam: {
          tenantId: Digit.ULBService.getCurrentTenantId(),
          limit: 10,
          offset: 0,
        },
        requestBody: {
          apiOperation: "SEARCH",
          Individual: {
            tenantId: Digit.ULBService.getCurrentTenantId(),
          },
          ePostTrackerSearchCriteria: {
            processNumber: "",
            deliveryStatusList: inboxFilters,
            pagination: {
              orderBy: "",
              sortBy: "",
            },
          },
        },
        masterName: "commonUiConfig",
        moduleName: "EpostTrackingUiConfig",
        minParametersForSearchForm: 0,
        tableFormJsonPath: "requestParam",
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
                type: "component",
                component: "CustomSortComponent",
                isMandatory: false,
                disable: false,
                name: "Date Received",
                key: "pagination",
                sortBy: "receivedDate",
                ascText: "ASC",
                descText: "DESC",
                showAdditionalText: true,
                showIcon: true,
                icon: "UpDownArrowIcon",
                populators: {},
              },
              {
                isMandatory: false,
                type: "text",
                key: "caseId",
                // label: "Process Number",
                disable: false,
                populators: {
                  name: "processNumber",
                  placeholder: "Search Eprocess No",
                },
              },
              {
                type: "dropdown",
                isMandatory: false,
                disable: false,
                populators: {
                  styles: { width: "250px" },
                  name: "deliveryStatusList.selected",
                  options: inboxFilters,
                },
              },
              // {
              //   type: "dropdown",
              //   isMandatory: false,
              //   disable: false,
              //   populators: {
              //     placeholder: "Type",
              //     styles: { width: "250px" },
              //     name: "type",
              //     options: [
              //       "IN_TRANSIT",
              //       "NOT_UPDATED",
              //     ],
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
                label: "E-Process No.",
                jsonPath: "processNumber",
              },
              {
                label: "Bar Code",
                jsonPath: "trackingNumber",
              },
              {
                label: "Pincode",
                jsonPath: "pinCode",
              },
              {
                label: "Address",
                jsonPath: "address",
              },
              {
                label: "Delivery Status",
                jsonPath: "deliveryStatus",
                additionalCustomization: true,
              },
              {
                label: "Remarks",
                jsonPath: "remarks",
              },
            ],
            enableColumnSort: true,
            resultsJsonPath: "EPostTracker",
          },
          show: true,
        },
      },
    },
    {
      label: "Outbox",
      type: "search",
      moduleName: "EpostTrackingUiConfig",
      apiDetails: {
        serviceName: "/epost-tracker/epost/v1/_getEPost",
        requestParam: {
          tenantId: Digit.ULBService.getCurrentTenantId(),
          limit: 10,
          offset: 0,
        },
        requestBody: {
          apiOperation: "SEARCH",
          Individual: {
            tenantId: Digit.ULBService.getCurrentTenantId(),
          },
          ePostTrackerSearchCriteria: {
            processNumber: "",
            deliveryStatusList: outboxFilters,
            pagination: {
              orderBy: "",
              sortBy: "",
            },
          },
        },
        masterName: "commonUiConfig",
        moduleName: "EpostTrackingUiConfig",
        minParametersForSearchForm: 0,
        tableFormJsonPath: "requestParam",
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
                type: "component",
                component: "CustomSortComponent",
                isMandatory: false,
                disable: false,
                name: "Date Received",
                key: "pagination",
                sortBy: "receivedDate",
                ascText: "ASC",
                descText: "DESC",
                showAdditionalText: true,
                showIcon: true,
                icon: "UpDownArrowIcon",
                populators: {},
              },
              {
                isMandatory: false,
                type: "text",
                key: "caseId",
                // label: "Process Number",
                disable: false,
                populators: {
                  name: "processNumber",
                  placeholder: "Search Eprocess No",
                },
              },
              {
                type: "dropdown",
                isMandatory: false,
                disable: false,
                populators: {
                  styles: { width: "250px" },
                  name: "deliveryStatusList.selected",
                  options: outboxFilters,
                },
              },
              // {
              //   type: "dropdown",
              //   isMandatory: false,
              //   disable: false,
              //   populators: {
              //     placeholder: "Type",
              //     styles: { width: "250px" },
              //     name: "type",
              //     options: [
              //       "IN_TRANSIT",
              //       "NOT_UPDATED",
              //     ],
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
                label: "E-Process No.",
                jsonPath: "processNumber",
              },
              {
                label: "Bar Code",
                jsonPath: "trackingNumber",
              },
              {
                label: "Pincode",
                jsonPath: "pinCode",
              },
              {
                label: "Address",
                jsonPath: "address",
              },
              {
                label: "Delivery Status",
                jsonPath: "deliveryStatus",
                additionalCustomization: true,
              },
              {
                label: "Remarks",
                jsonPath: "remarks",
              },
            ],
            enableColumnSort: true,
            resultsJsonPath: "EPostTracker",
          },
          show: true,
        },
      },
    },
  ];

  return TabSearchConfig;
};
