import { useQuery, useQueryClient } from "react-query";

function useGetIndividualUser(reqData, params, moduleCode, individualId, enabled) {
  const client = useQueryClient();
  const { isLoading, data, isFetching, refetch, error } = useQuery(
    `GET_INDIVIDUAL_USER_${moduleCode}_${individualId}`,
    () => window?.Digit.DRISTIService.searchIndividualUser(reqData, params),
    {
      cacheTime: 0,
      enabled: Boolean(enabled),
    }
  );

  return {
    isLoading,
    isFetching,
    data,
    refetch,
    revalidate: () => {
      data && client.invalidateQueries({ queryKey: `GET_INDIVIDUAL_USER_${moduleCode}_${individualId}` });
    },
    error,
  };
}

export default useGetIndividualUser;
