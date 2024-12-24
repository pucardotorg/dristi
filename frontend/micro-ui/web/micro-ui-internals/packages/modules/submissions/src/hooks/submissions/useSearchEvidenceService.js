import { useQuery, useQueryClient } from "react-query";
import { submissionService } from "../services";

function useSearchEvidenceService(reqData, params, key, enabled) {
  const client = useQueryClient();
  const { isLoading, data, isFetching, refetch, error } = useQuery(
    `GET_SUBMISSION_DETAILS_${key}`,
    () => submissionService.searchEvidence(reqData, params),
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
      data && client.invalidateQueries({ queryKey: `GET_SUBMISSION_DETAILS_${key}` });
    },
    error,
  };
}

export default useSearchEvidenceService;
