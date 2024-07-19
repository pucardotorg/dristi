import { useQuery } from "react-query";
import { DRISTIService } from "../../services";

function useGetOrders(data, params, keys, enabled) {
  const { isLoading, data: ordersResponse, isFetching, refetch, error } = useQuery(
    `GET_ORDERS_${keys}`,
    () => DRISTIService.searchOrders(data, params),
    {
      cacheTime: 0,
      enabled: Boolean(enabled),
      retry: false, // Disable automatic retries to prevent flooding the API with requests
    }
  );

  if (error) {
    console.error("Error fetching ORDERS:", error);
  }

  return {
    isLoading,
    isFetching,
    data: ordersResponse,
    refetch,
    error,
  };
}

export default useGetOrders;
