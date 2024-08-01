import { useEffect } from "react";
import { useQuery, useQueryClient } from "react-query";
import { Urls } from "../../../dristi/src/hooks";

const usePreHearingModalData = ({ url, params, body, config = {}, plainAccessRequest, state, changeQueryName = "Random" }) => {
  const client = useQueryClient();

  const { searchForm } = state;
  const { stage, type, caseNameOrId } = searchForm;

  const idPattern = /^F-C\.\d{4}\.\d{3}-\d{4}-\d{6}$/;

  if (caseNameOrId && idPattern.test(caseNameOrId)) {
    body.criteria = {
      ...body.criteria,
      filingNumber: caseNameOrId,
    };
  }

  const fetchCombinedData = async () => {
    //need to filter this hearing list response based on slot
    const hearingListResponse = await Digit.CustomService.getResponse({
      url,
      params,
      body,
      plainAccessRequest,
    }).then((response) => ({
      ...response,
    }));

    const filingNumbers = [];
    const filingNumberToHearing = new Map();
    for (const hearing of hearingListResponse.HearingList) {
      const filingNumber = hearing.filingNumber[0];
      filingNumbers.push(filingNumber);
      filingNumberToHearing.set(filingNumber, hearing);
    }

    const caseBody = {
      tenantId: "pg",
      criteria: filingNumbers.map((filingNumber) => ({
        filingNumber: filingNumber,
        ...(stage && { stage: stage.stage }),
        ...(type && { caseType: type.type }),
      })),
    };

    const caseDetailsResponse = await Digit.CustomService.getResponse({
      url: Urls.dristi.caseSearch,
      params: { tenantId: Digit.ULBService.getCurrentTenantId() },
      body: caseBody,
      plainAccessRequest,
    }).then((response) => ({
      ...response,
    }));

    const caseDetailsMap = new Map();
    caseDetailsResponse.criteria.forEach((caseDetail) => {
      caseDetailsMap.set(caseDetail.filingNumber, caseDetail.responseList[0]);
    });

    const pendingTaskPromises = filingNumbers.map((filingNumber) => {
      return Digit.CustomService.getResponse({
        url: "/inbox/v2/_getFields",
        params: { tenantId: Digit.ULBService.getCurrentTenantId() },
        body: {
          SearchCriteria: {
            tenantId: Digit.ULBService.getCurrentTenantId(),
            moduleName: "Pending Tasks Service",
            moduleSearchCriteria: {
              referenceId: filingNumber,
            },
            limit: 10,
            offset: 0,
          },
        },
        plainAccessRequest,
      }).then((response) => ({ filingNumber, data: response.data }));
    });

    const pendingTaskResponses = await Promise.all(pendingTaskPromises);

    const combinedData = Array.from(caseDetailsMap.entries())
      .filter(([filingNumber, caseData]) => caseData)
      .map(([filingNumber, caseData]) => {
        const pendingTaskDetail = pendingTaskResponses.find((taskResponse) => taskResponse.filingNumber === filingNumber);
        const pendingTasksData = pendingTaskDetail ? pendingTaskDetail.data.length : 0;

        return {
          caseId: caseData.id,
          filingNumber,
          caseName: caseData?.caseTitle || "",
          cnrNumber: caseData.cnrNumber,
          stage: caseData?.stage || "",
          caseType: caseData?.caseType || "",
          pendingTasks: pendingTasksData || "-",
          hearingId: filingNumberToHearing.get(filingNumber).hearingId,
          hearing: filingNumberToHearing.get(filingNumber),
        };
      });

    return { items: combinedData };
  };

  const { isLoading, data, isFetching, refetch, error } = useQuery("GET_PRE_HEARING_DATA", fetchCombinedData, {
    cacheTime: 0,
    enabled: state.searchForm && (state.searchForm.stage || state.searchForm.type || state.searchForm.filingNumber),
    ...config,
  });

  useEffect(() => {
    refetch();
  }, [state]);

  return {
    isLoading,
    isFetching,
    data,
    revalidate: () => {
      data && client.invalidateQueries({ queryKey: [url, changeQueryName] });
    },
    refetch,
    error,
  };
};

export default usePreHearingModalData;
