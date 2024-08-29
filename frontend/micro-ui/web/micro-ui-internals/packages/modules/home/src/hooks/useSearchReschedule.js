import { useQuery } from "react-query";
import { HomeService } from "./services";

function useSearchReschedule(data, params, keys, enabled) {
  const { isLoading, data: searchRescheduleResponse, isFetching, refetch, error } = useQuery(
    `GET_SEARCH_RESCHEDULE${keys}`,
    () => HomeService.searchReschedule(data, params),
    {
      cacheTime: 0,
      enabled: Boolean(enabled),
      retry: false, // Disable automatic retries to prevent flooding the API with requests
    }
  );

  if (error) {
    console.error("Error fetching search reschedule:", error);
  }

  return {
    isLoading,
    isFetching,
    data: searchRescheduleResponse,
    refetch,
    error,
  };
}

export default useSearchReschedule;
