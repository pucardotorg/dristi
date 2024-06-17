import { useQuery } from "react-query";
import { DRISTIService } from "../../services";

function useGetHearings(data, params, keys, enabled) {
  console.log(data, params, keys, enabled);
  return useQuery(`GET_ADVOCATE_CLERK_${keys}`, () => DRISTIService.searchHearings(data, params), {
    enabled: Boolean(enabled),
  });
}

export default useGetHearings;
