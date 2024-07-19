import { useQuery } from "react-query";
import { DRISTIService } from "../../services";

function useGetSubmissions(data, params, keys, enabled) {
  const { isLoading, data: submissionsResponse, isFetching, refetch, error } = useQuery(
    `GET_SUBMISSIONS_${keys}`,
    () => DRISTIService.searchSubmissions(data, params),
    {
      cacheTime: 0,
      enabled: Boolean(enabled),
      retry: false, // Disable automatic retries to prevent flooding the API with requests
    }
  );

  if (error) {
    console.error("Error fetching SUBMISSIONS:", error);
  }

  return {
    isLoading,
    isFetching,
    data: submissionsResponse,
    refetch,
    error,
  };
}

export default useGetSubmissions;
