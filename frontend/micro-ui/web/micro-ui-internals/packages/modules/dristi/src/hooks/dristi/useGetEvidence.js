import { useQuery } from "react-query";
import { DRISTIService } from "../../services";

function useGetEvidence(data, keys, enabled) {
  const { isLoading, data: evidenceResponse, isFetching, refetch, error } = useQuery(
    `GET_EVIDENCE_${keys}`,
    () => DRISTIService.searchEvidence(data),
    {
      cacheTime: 0,
      enabled: Boolean(enabled),
      retry: false, // Disable automatic retries to prevent flooding the API with requests
    }
  );

  if (error) {
    console.error("Error fetching EVIDENCE:", error);
  }

  return {
    isLoading,
    isFetching,
    data: evidenceResponse,
    refetch,
    error,
  };
}

export default useGetEvidence;
