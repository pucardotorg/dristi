import { useQuery } from "react-query";
import { DRISTIService } from "../../services/elements/DRISTI";
function useGetAdvocateClientServices(url, data, tenantId, keys, additionInfo, userService = false, enabled = true) {
  return useQuery(`GETADVOCATECLIENTSERVICES_${keys}`, () => DRISTIService.advocateClerkService(url, data, tenantId, userService, additionInfo), {
    enabled: Boolean(enabled),
  });
}

export default useGetAdvocateClientServices;
