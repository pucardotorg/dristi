import { useQuery } from "react-query";
import { DRISTIService } from "../../services";
function useSummonsPaymentBreakUp(data, params, keys, enabled) {
  const { isLoading, data: breakupResponse, isFetching, refetch, error } = useQuery(
    `GET_SUMMONS_PAYMENT_${keys}`,
    () => DRISTIService.getSummonsPaymentBreakup(data, params),
    {
      cacheTime: 0,
      enabled: Boolean(enabled),
      retry: false, // Disable automatic retries to prevent flooding the API with requests
    }
  );
  if (error) {
    console.error("Error fetching Summons Breakup:", error);
  }
  return {
    isLoading,
    isFetching,
    data: breakupResponse,
    refetch,
    error,
  };
}
export default useSummonsPaymentBreakUp;
