import { useQuery, useQueryClient } from "react-query";
import { ordersService } from "../services";

function useSearchOrdersService(reqData, params, key, enabled) {
  const client = useQueryClient();
  const { isLoading, data, isFetching, refetch, error } = useQuery(`GET_ORDERS_DETAILS_${key}`, () => ordersService.searchOrder(reqData, params), {
    cacheTime: 5 * 60,
    enabled: Boolean(enabled),
  });

  return {
    isLoading,
    isFetching,
    data,
    refetch,
    revalidate: () => {
      data && client.invalidateQueries({ queryKey: `GET_ORDERS_DETAILS_${key}` });
    },
    error,
  };
}

export default useSearchOrdersService;
