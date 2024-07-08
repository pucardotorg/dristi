import { useQuery } from "react-query";
function useGetAdvocateClientServices(url, data, tenantId, keys, additionInfo, userService = false, enabled = true) {
  return useQuery(
    `GET_ADVOCATE_CLIENT_SERVICES_${keys}`,
    () => window?.Digit.DRISTIService.advocateClerkService(url, data, tenantId, userService, additionInfo),
    {
      enabled: Boolean(enabled),
    }
  );
}

export default useGetAdvocateClientServices;
