import { useQuery } from "react-query";
import { DRISTIService } from "../../services";

function useJudgeAvailabilityDates(data, params, keys, enabled) {
  const { isLoading, data: judgeAvailabilityResponse, isFetching, refetch, error } = useQuery(
    `GET_JUDGE_AVAILABILITY_DATES${keys}`,
    () => DRISTIService.judgeAvailabilityDates(data, params),
    {
      cacheTime: 0,
      enabled: Boolean(enabled),
      retry: false, // Disable automatic retries to prevent flooding the API with requests
    }
  );

  if (error) {
    console.error("Error fetching judge availability dates:", error);
  }

  return {
    isLoading,
    isFetching,
    data: judgeAvailabilityResponse,
    refetch,
    error,
  };
}

export default useJudgeAvailabilityDates;
