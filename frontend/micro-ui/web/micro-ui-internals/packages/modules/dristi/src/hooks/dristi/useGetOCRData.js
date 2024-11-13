import { useQuery, useQueryClient } from "react-query";
import { DRISTIService } from "../../services";

const useGetOCRData = (reqData, params, enabled = true) => {
  const queryClient = useQueryClient();
  const queryKey = ["GET_OCR_DATA", reqData, params];

  const { isLoading, data, isFetching, refetch, error } = useQuery(queryKey, () => window?.Digit.DRISTIService.getOCRData(reqData, params), {
    cacheTime: 0,
    enabled: Boolean(enabled),
  });

  const revalidate = () => {
    queryClient.invalidateQueries(queryKey);
  };

  return {
    isLoading,
    isFetching,
    data,
    refetch,
    revalidate,
    error,
  };
};

export default useGetOCRData;
