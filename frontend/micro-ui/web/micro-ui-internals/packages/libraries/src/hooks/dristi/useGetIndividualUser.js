import { useQuery } from "react-query";
import { DRISTIService } from "../../services/elements/DRISTI";

function useGetIndividualUser(data, params, moduleCode, enabled) {
  return useQuery(`GETINDIVIDUALUSER_${moduleCode}`, () => DRISTIService.searchIndividualUser(data, params), { enabled: Boolean(enabled) });
}

export default useGetIndividualUser;
