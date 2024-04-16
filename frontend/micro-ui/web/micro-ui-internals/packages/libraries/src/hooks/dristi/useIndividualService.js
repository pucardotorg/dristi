import { useQuery } from "react-query";
import { DRISTIService } from "../../services/elements/DRISTI";

const useIndividualService = (data, tenantId) => {
  return useQuery(`DSS_DASHBOARD_CONFIG_${moduleCode}`, () => DRISTIService.postIndividualService(data, tenantId));
};

export default useIndividualService;
