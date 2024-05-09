import { useQuery } from "react-query";

function useGetAdvocateClerk(data, params, keys, enabled, url) {
  return useQuery(`GET_ADVOCATE_CLERK_${keys}`, () => window?.Digit.DRISTIService.searchAdvocateClerk(url, data, params), {
    enabled: Boolean(enabled),
  });
}

export default useGetAdvocateClerk;
