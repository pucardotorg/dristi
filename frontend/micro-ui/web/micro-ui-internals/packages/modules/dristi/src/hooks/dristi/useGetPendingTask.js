import { useQuery } from "react-query";
import { DRISTIService } from "../../services";

export const useGetPendingTask = ({ data, params, config = {}, key = "" }) => {
  return useQuery(
    ["Pending Task Details", key],
    () =>
      DRISTIService.getPendingTaskService(data, params)
        .then((data) => data)
        .catch(() => []),
    config
  );
};
