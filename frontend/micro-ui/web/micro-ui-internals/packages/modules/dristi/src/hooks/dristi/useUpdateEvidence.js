import { useQuery } from "react-query";
import { DRISTIService } from "../../services";

function useUpdateEvidence(data, keys, enabled) {
  const { isLoading, data: updateEvidenceResponse, isFetching, refetch, error } = useQuery(
    `UPDATE_EVIDENCE_${keys}`,
    () => DRISTIService.updateEvidence(data),
    {
      cacheTime: 0,
      enabled: Boolean(enabled),
      retry: false, // Disable automatic retries to prevent flooding the API with requests
    }
  );

  if (error) {
    console.error("Error updating EVIDENCE:", error);
  }

  return {
    isLoading,
    isFetching,
    data: updateEvidenceResponse,
    refetch,
    error,
  };
}

export default useUpdateEvidence;
