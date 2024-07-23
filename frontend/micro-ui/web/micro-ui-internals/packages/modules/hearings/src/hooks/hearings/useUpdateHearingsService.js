import { useQuery } from "react-query";
import { hearingService } from "../services";

function useUpdateHearingsService(data, params, keys, enabled) {
  const { isLoading, data: hearingResponse, isFetching, refetch, error } = useQuery(
    `GET_HEARING_${keys}`,
    () => hearingService.updateHearing(data, params),
    {
      cacheTime: 0,
      enabled: Boolean(enabled),
      retry: false, // Disable automatic retries to prevent flooding the API with requests
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

export default useUpdateHearingsService;
