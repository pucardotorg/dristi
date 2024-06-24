import { useQuery, useQueryClient } from "react-query";
import { ordersService } from "../services";

function useGetOrderDetails(reqData, params, key, enabled) {
  const client = useQueryClient();
  const { isLoading, data, isFetching, refetch, error } = useQuery(`GET_ORDER_DETAILS_${key}`, () => ordersService.updateOrder(reqData, params), {
    cacheTime: 5 * 60,
    enabled: Boolean(enabled),
  });

  return {
    isLoading,
    isFetching,
    data,
    refetch,
    revalidate: () => {
      data && client.invalidateQueries({ queryKey: `GET_ORDER_DETAILS_${key}` });
    },
    error,
  };
}

export default useGetOrderDetails;
