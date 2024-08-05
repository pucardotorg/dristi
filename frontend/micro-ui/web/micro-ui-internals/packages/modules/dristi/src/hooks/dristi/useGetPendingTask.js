import { useQuery } from "react-query";
import { HomeService } from "./services";

export const useGetPendingTask = ({ data, params, config = {}, key = "" }) => {
  return useQuery(
    ["Pending Task Details", key],
    () =>
      HomeService.getPendingTaskService(data, params)
        .then((data) => data)
        .catch(() => []),
    config
  );
};
