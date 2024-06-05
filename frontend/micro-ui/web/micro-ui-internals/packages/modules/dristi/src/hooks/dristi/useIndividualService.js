import { useQuery, useQueryClient } from "react-query";

const useIndividualService = (reqData, tenantId, moduleCode) => {
  const client = useQueryClient();
  const { isLoading, data, isFetching, refetch, error } = useQuery(
    `CREATE_INDIVIDUAL_USER_${moduleCode}`,
    () => window?.Digit.DRISTIService.postIndividualService(reqData, tenantId),
    {
      cacheTime: 0,
    }
  );

  return {
    isLoading,
    isFetching,
    data,
    refetch,
    revalidate: () => {
      data && client.invalidateQueries({ queryKey: `CREATE_INDIVIDUAL_USER_${moduleCode}` });
    },
    error,
  };
};

export default useIndividualService;
