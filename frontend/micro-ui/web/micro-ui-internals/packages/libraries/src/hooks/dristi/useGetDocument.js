import { useQuery } from "react-query";
import { DRISTIService } from "../../services/elements/DRISTI";

function useGetDocument(params, moduleCode, applicationNo, enabled) {
  return useQuery(`GETDOCUMENT_${moduleCode}_${applicationNo}`, () => DRISTIService.getDocument(params), {
    enabled: Boolean(enabled),
  });
}

export default useGetDocument;
