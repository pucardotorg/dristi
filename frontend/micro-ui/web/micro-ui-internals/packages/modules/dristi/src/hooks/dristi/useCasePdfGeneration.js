import { useQuery, useQueryClient } from "react-query";
import { DRISTIService } from "../../services";
import React from "react";

function useCasePdfGeneration(reqData, params, moduleCode, caseId, enabled) {
  const client = useQueryClient();
  const { isLoading, data, isFetching, refetch, error } = useQuery(
    `GET_CASE_PDF_DETAILS_${moduleCode}_${caseId}`,
    () => DRISTIService.generateCasePdf(reqData, params),
    {
      cacheTime: 0,
      enabled: Boolean(enabled),
    }
  );

  return {
    isLoading,
    isFetching,
    data,
    refetch,
    revalidate: () => {
      data && client.invalidateQueries({ queryKey: `GET_CASE_DETAILS_${moduleCode}_${caseId}` });
    },
    error,
  };
}

export default useCasePdfGeneration;
