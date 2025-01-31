import { useQuery, useQueryClient } from "react-query";
import { DRISTIService } from "../../services";

function useSearchADiaryService(reqData, params, moduleCode, date, enabled, isCacheTimeEnabled = true) {
  const client = useQueryClient();
  const { isLoading, data, isFetching, refetch, error } = useQuery(
    `GET_A_DIARY_DETAILS_${moduleCode}_${date}`,
    () =>
      DRISTIService.aDiaryEntrySearch(reqData, params)
        .then((data) => data)
        .catch(() => ({})),
    {
      ...(isCacheTimeEnabled && { cacheTime: 0 }),
      enabled: Boolean(enabled),
    }
  );

  return {
    isLoading,
    isFetching,
    data,
    refetch,
    revalidate: () => {
      data && client.invalidateQueries({ queryKey: `GET_A_DIARY_DETAILS_${moduleCode}_${date}` });
    },
    error,
  };
}

export default useSearchADiaryService;
