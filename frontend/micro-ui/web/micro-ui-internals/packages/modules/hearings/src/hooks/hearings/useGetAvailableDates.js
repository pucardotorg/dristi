import { useQuery } from "react-query";

function useGetAvailableDates(enabled) {
  const fetchNextFiveDays = () => {
    const today = new Date();
    const options = { day: "numeric", month: "long", year: "numeric" };
    const nextFiveDays = Array.from({ length: 5 }, (_, i) => {
      const nextDay = new Date(today);
      nextDay.setDate(today.getDate() + i + 1);
      return nextDay.toLocaleDateString("en-GB", options); // Format: "4th July 2024"
    });
    return nextFiveDays;
  };

  const { isLoading, data: datesResponse, isFetching, refetch, error } = useQuery("GET_NEXT_FIVE_DAYS", fetchNextFiveDays, {
    cacheTime: 0,
    enabled: Boolean(enabled),
    retry: false, // Disable automatic retries to prevent flooding with requests
  });

  if (error) {
    console.error("Error fetching dates:", error);
  }

  return {
    isLoading,
    isFetching,
    data: datesResponse,
    refetch,
    error,
  };
}

export default useGetAvailableDates;
