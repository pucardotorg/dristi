import { useQuery } from "react-query";
import { DRISTIService } from "../../services";

function useUpdateSubmissions(data, params, keys, enabled) {
  const { isLoading, data: updateSubmissionsResponse, isFetching, refetch, error } = useQuery(
    `UPDATE_SUBMISSIONS_${keys}`,
    () => DRISTIService.updateSubmissions(data, params),
    {
      cacheTime: 0,
      enabled: Boolean(enabled),
      retry: false, // Disable automatic retries to prevent flooding the API with requests
    }
  );

  if (error) {
    console.error("Error updating SUBMISSIONS:", error);
  }

  return {
    isLoading,
    isFetching,
    data: updateSubmissionsResponse,
    refetch,
    error,
  };
}

export default useUpdateSubmissions;
