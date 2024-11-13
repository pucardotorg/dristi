import { useQuery } from "react-query";
import { DRISTIService } from "../../services";

function useCreateEvidence(data, keys, enabled) {
  const { isLoading, data: createEvidenceResponse, isFetching, refetch, error } = useQuery(
    `CREATE_EVIDENCE_${keys}`,
    () => DRISTIService.createEvidence(data),
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
    data: createEvidenceResponse,
    refetch,
    error,
  };
}

export default useCreateEvidence;
