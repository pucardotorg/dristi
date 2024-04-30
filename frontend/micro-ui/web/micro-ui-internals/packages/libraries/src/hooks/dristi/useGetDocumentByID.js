import { useQuery } from "react-query";
import { DRISTIService } from "../../services/elements/DRISTI";

function useGetDocumentByID(params, moduleCode, applicationNo, enabled) {
  return useQuery(`GETDOCUMENT_${moduleCode}_${applicationNo}`, () => DRISTIService.getDocument(params), {
    enabled: Boolean(enabled),
  });
}

export default useGetDocumentByID;
