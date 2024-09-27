import { useQuery, useQueryClient } from "react-query";
import { DRISTIService } from "../../services";

function useRepondentPincodeDetails(data, params, keys, enabled) {
  const { isLoading, data: respondentPincode, isFetching, refetch, error } = useQuery(
    `GET_PINCODE_${keys}`,
    () => DRISTIService.getrepondentPincodeDetails(data, params),
    {
      cacheTime: 0,
      enabled: Boolean(enabled),
      retry: false, // Disable automatic retries to prevent flooding the API with requests
    }
  );

  if (error) {
    console.error("Error fetching Respondent Pincode:", error);
  }

  return {
    isLoading,
    isFetching,
    data: respondentPincode,
    refetch,
    error,
  };
}

export default useRepondentPincodeDetails;
