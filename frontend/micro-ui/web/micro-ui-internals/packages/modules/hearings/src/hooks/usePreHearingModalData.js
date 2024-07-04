import { useQuery, useQueryClient } from "react-query";

const usePreHearingModalData = ({ url, params, body, config = {}, plainAccessRequest, state, changeQueryName = "Random" }) => {
  const client = useQueryClient();

  const fetchCombinedData = async () => {
    const [caseResponse, pendingTaskResponse] = await Promise.all([
      Digit.CustomService.getResponse({
        url,
        params,
        body,
        plainAccessRequest,
      }).then((response) => {
        return {
          ...response,
        };
      }),

      //Need to call pending task api here so we can populate that data into the pending tasks

      // Digit.CustomService.getResponse({
      //   url: "/inbox/v2/_getFields",
      //   params: { tenantId: Digit.ULBService.getCurrentTenantId() },
      //   body: {
      //     SearchCriteria: {
      //       tenantId: "pg",
      //       moduleName: "Pending Tasks Service",
      //       moduleSearchCriteria: {
      //         entityType: "case",
      //       },
      //       limit: 10,
      //       offset: 0,
      //     },
      //   },
      //   plainAccessRequest,
      // }).then((response) => {
      //   return {
      //     ...response,
      //   };
      // }),
    ]);

    return { caseResponse, pendingTaskResponse };
  };

  const { isLoading, data, isFetching, refetch, error } = useQuery("GET_PRE_HEARING_DATA", fetchCombinedData, {
    cacheTime: 0,
    ...config,
  });

  return {
    isLoading,
    isFetching,
    data,
    revalidate: () => {
      data && client.invalidateQueries({ queryKey: [url, changeQueryName] });
    },
    refetch,
    error,
  };
};

export default usePreHearingModalData;
