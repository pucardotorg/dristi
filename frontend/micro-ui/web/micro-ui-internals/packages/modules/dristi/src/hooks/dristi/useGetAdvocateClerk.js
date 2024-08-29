import { useQuery } from "react-query";
import { DRISTIService } from "../../services";

function useGetAdvocateClerk(data, params, keys, enabled, url) {
  return useQuery(
    `GET_ADVOCATE_CLERK_${keys}`,
    () =>
      DRISTIService.searchAdvocateClerk(url, data, params)
        .then((data) => data)
        .catch(() => ({})),
    {
      enabled: Boolean(enabled),
    }
  );
}

export default useGetAdvocateClerk;
