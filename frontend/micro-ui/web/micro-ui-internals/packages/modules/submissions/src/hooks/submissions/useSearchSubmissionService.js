import { useQuery, useQueryClient } from "react-query";
import { submissionService } from "../services";

function useSearchSubmissionService(reqData, params, key, enabled) {
  const client = useQueryClient();
  const { isLoading, data, isFetching, refetch, error } = useQuery(
    `GET_SUBMISSION_DETAILS_${key}`,
    () => submissionService.searchApplication(reqData, params),
    {
      cacheTime: 5 * 60,
      enabled: Boolean(enabled),
    }
  );

  return {
    isLoading,
    isFetching,
    data,
    refetch,
    revalidate: () => {
      data && client.invalidateQueries({ queryKey: `GET_SUBMISSION_DETAILS_${key}` });
    },
    error,
  };
}

export default useSearchSubmissionService;
