import { useQuery } from "react-query";
import { DRISTIService } from "../../services/elements/DRISTI";

function useGetIndividualAdvocate(data, params, moduleCode, applicationNo, enabled) {
  return useQuery(`GETINDIVIDUALADVOCATE_${moduleCode}_${applicationNo}`, () => DRISTIService.searchIndividualAdvocate(data, params), {
    enabled: Boolean(enabled),
  });
}

export default useGetIndividualAdvocate;
