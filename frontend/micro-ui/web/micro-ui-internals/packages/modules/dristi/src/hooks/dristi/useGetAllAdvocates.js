import { useQuery } from "react-query";
import { DRISTIService } from "../../services";

function useGetAllAdvocates(data, params, keys, enabled = true, url) {
  return useQuery(
    `GET_ADVOCATE_CLERK_${keys}`,
    () =>
      DRISTIService.searchAllAdvocates(url, data, params)
        .then((data) => data)
        .catch(() => ({})),
    {
      enabled: Boolean(enabled),
      cacheTime: 0,
    }
  );
}

export default useGetAllAdvocates;
