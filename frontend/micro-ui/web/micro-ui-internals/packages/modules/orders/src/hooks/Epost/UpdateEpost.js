import { useQuery } from "react-query";
import { EpostService } from "../services";

function useUpdateEpost(data, params, keys, enabled) {
  const { isLoading, data: EpostResponse, isFetching, refetch, error } = useQuery(
    `UPDATE_EPOST_${keys}`,
    () => EpostService.EpostUpdate(data, params),
    {
      cacheTime: 0,
      enabled: Boolean(enabled),
      retry: false, // Disable automatic retries to prevent flooding the API with requests
    }
  );

  if (error) {
    console.error("Error Updating Epost:", error);
  }

  return {
    isLoading,
    isFetching,
    data: EpostResponse,
    refetch,
    error,
  };
}

export default useUpdateEpost;
