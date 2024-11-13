import { useQuery, useQueryClient } from "react-query";
import { DRISTIService } from "../../services";

function useBillSearch(data, params, keys, enabled) {
  const { isLoading, data: billResponse, isFetching, refetch, error } = useQuery(
    `GET_BILL_${keys}`,
    () => DRISTIService.callSearchBill(data, params),
    {
      cacheTime: 0,
      enabled: Boolean(enabled),
      retry: false, // Disable automatic retries to prevent flooding the API with requests
    }
  );

  if (error) {
    console.error("Error fetching payment:", error);
  }

  return {
    isLoading,
    isFetching,
    data: billResponse,
    refetch,
    error,
  };
}

export default useBillSearch;
