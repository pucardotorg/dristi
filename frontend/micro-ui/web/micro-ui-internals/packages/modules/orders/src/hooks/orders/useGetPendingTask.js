import { useQuery } from "react-query";
import { ordersService } from "../services";

export const useGetPendingTask = ({ data, params, config = {}, key = "" }) => {
  return useQuery(
    ["Pending Task Details", key],
    () =>
      ordersService
        .getPendingTaskService(data, params)
        .then((data) => data)
        .catch(() => []),
    config
  );
};
