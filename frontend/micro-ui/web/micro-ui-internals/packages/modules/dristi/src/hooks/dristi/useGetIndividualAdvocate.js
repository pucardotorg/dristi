import { useQuery } from "react-query";

function useGetIndividualAdvocate(data, params, moduleCode, applicationNo, enabled) {
  return useQuery(
    `GET_INDIVIDUAL_ADVOCATE_${moduleCode}_${applicationNo}`,
    () => window?.Digit.DRISTIService.searchIndividualAdvocate(data, params),
    {
      enabled: Boolean(enabled),
    }
  );
}

export default useGetIndividualAdvocate;
