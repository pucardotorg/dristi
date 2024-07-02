import { useQuery } from "react-query";
import { hearingService } from "../services";

function useGetHearings(data, params, keys, enabled, refetchInterval = false) {
  const { isLoading, data: hearingResponse, isFetching, refetch, error } = useQuery(
    `GET_HEARING_${keys}`,
    () => hearingService.searchHearings(data, params),
    {
      cacheTime: 0,
      enabled: Boolean(enabled),
      retry: false, // Disable automatic retries to prevent flooding the API with requests
      refetchInterval,
    }
  );

  if (error) {
    console.error("Error fetching hearings:", error);
  }

  return {
    isLoading,
    isFetching,
    data: hearingResponse,
    refetch,
    error,
  };
}

export default useGetHearings;
