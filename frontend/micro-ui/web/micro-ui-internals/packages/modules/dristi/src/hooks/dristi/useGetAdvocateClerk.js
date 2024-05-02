import { useQuery } from "react-query";
import { DRISTIService } from "../../services/elements/DRISTI";

function useGetAdvocateClerk(data, params, keys, enabled, url) {
  return useQuery(`GET_ADVOCATE_CLERK_${keys}`, () => DRISTIService.searchAdvocateClerk(url, data, params), {
    enabled: Boolean(enabled),
  });
}

export default useGetAdvocateClerk;
