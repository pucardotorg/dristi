// const defaultSearchValues = {
//   filingNumber: "",
//   cnrNumber: "",
//   applicationNumber:""
// };

export const summonsConfig = ({ filingNumber, limit, offset }) => {
  return {
    label: "1(Summons)",
    type: "search",
    apiDetails: {
      serviceName: "/order/v1/search",
      requestParam: {
        tenantId: Digit.ULBService.getCurrentTenantId(),
        limit: 2,
        offset: 0,
      },
      requestBody: {
        apiOperation: "SEARCH",
        criteria: {
          // tenantId: Digit.ULBService.getCurrentTenantId(),
          filingNumber: filingNumber,
        },
      },
      masterName: "commonUiConfig",
      moduleName: "SearchIndividualConfig",
      minParametersForSearchForm: 0,
      tableFormJsonPath: "requestParam",
      filterFormJsonPath: "requestBody.criteria",
      searchFormJsonPath: "requestBody.criteria",
    },
    sections: {
      // search: {
      //   uiConfig: {
      //     fields: [],
      //   },
      //   show: true,
      // },
      searchResult: {
        tenantId: Digit.ULBService.getCurrentTenantId(),
        uiConfig: {
          columns: [
            {
              label: "Delivery Channels",
              jsonPath: "individualId",
            },

            {
              label: "Channel Details",
              jsonPath: "name.givenName",
            },
            {
              label: "Status",
              jsonPath: "status",
            },
            {
              label: "Remark",
              jsonPath: "comments",
            },
          ],

          enableColumnSort: true,
          resultsJsonPath: "list",
        },
        show: true,
      },
    },
  };
};
