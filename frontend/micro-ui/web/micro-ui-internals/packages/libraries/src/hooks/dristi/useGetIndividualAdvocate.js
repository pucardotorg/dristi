import { useQuery } from "react-query";
import { DRISTIService } from "../../services/elements/DRISTI";

function useGetIndividualAdvocate(data, params, moduleCode) {
  return useQuery(`GETINDIVIDUALADVOCATE_${moduleCode}`, () => DRISTIService.searchIndividualAdvocate(data, params));
}

export default useGetIndividualAdvocate;
