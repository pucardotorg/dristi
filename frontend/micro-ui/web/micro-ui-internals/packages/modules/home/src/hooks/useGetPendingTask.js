import { useQuery } from "react-query";
import { DRISTIService } from "@egovernments/digit-ui-module-dristi/src/services";

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
