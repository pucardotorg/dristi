import { useQuery, useQueryClient } from "react-query";
import { ordersService } from "../services";

function useSearchOrdersService(reqData, params, key, enabled, cacheTime = 5 * 60) {
  const client = useQueryClient();
  const { isLoading, data, isFetching, refetch, error } = useQuery(`GET_ORDERS_DETAILS_${key}`, () => ordersService.searchOrder(reqData, params), {
    cacheTime: cacheTime,
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

export function useSearchOrdersServiceForForm(reqData, params, key, enabled, formConfigMap) {
  const client = useQueryClient();
  const { isLoading, data, isFetching, refetch, error } = useQuery(
    `GET_ORDERS_DETAILS_${key}`,
    async () => {
      const response = await ordersService.searchOrder(reqData, params);
      if (response.list) {
        const updatedResponseList = await Promise.all(
          response.list.map(async (order) => {
            const formConfig = formConfigMap[order.orderType];
            if (!formConfig) {
              return order;
            }
            const orderWithFormData = await Digit.Customizations.dristiOrders.OrderFormSchemaUtils.schemaToForm(order, formConfig);
            return orderWithFormData;
          })
        );
        return { ...response, list: updatedResponseList };
      }
      return response;
    },
    {
      cacheTime: 5 * 60,
      enabled: Boolean(enabled),
    }
  );

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
