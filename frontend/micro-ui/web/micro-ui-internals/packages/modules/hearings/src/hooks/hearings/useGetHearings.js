import { useQuery } from "react-query";
import { hearingService } from "../services";

/**
 * @param {string} keys
 * @param {boolean} enabled
 * @param {number} refetchInterval
 * @returns data
 */
function useGetHearings(data, params, keys, enabled, refetchInterval = false, attendeeIndividualId = "") {
  const { isLoading, data: hearingResponse, isFetching, refetch, error } = useQuery(
    `GET_HEARING_${keys}`,
    () =>
      hearingService
        .searchHearings(
          {
            ...data,
            criteria: {
              ...data?.criteria,
              attendeeIndividualId: attendeeIndividualId || undefined,
            },
          },
          params
        )
        .then((data) => data)
        .catch(() => null),
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
