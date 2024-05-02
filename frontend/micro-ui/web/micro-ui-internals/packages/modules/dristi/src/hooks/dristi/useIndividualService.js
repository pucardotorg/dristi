import { useQuery } from "react-query";
import { DRISTIService } from "../../services/elements/DRISTI";

const useIndividualService = (data, tenantId, moduleCode) => {
  return useQuery(`CREATEINDIVIDUALUSER_${moduleCode}`, () => DRISTIService.postIndividualService(data, tenantId));
};

export default useIndividualService;
