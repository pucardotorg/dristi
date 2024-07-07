import { useQuery, useQueryClient } from "react-query";

const usePreHearingModalData = ({ url, params, body, config = {}, plainAccessRequest, state, changeQueryName = "Random" }) => {
  const client = useQueryClient();

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

    const filingNumbers = hearingListResponse.HearingList.map((hearing) => hearing.filingNumber).flat();

    const caseDetailsPromises = filingNumbers.map((filingNumber) => {
      const caseBody = {
        tenantId: "pg",
        criteria: [
          {
            filingNumber: filingNumber,
          },
        ],
      };

      return Digit.CustomService.getResponse({
        url: "/case/case/v1/_search",
        params: { tenantId: Digit.ULBService.getCurrentTenantId() },
        body: caseBody,
        plainAccessRequest,
      }).then((response) => ({
        ...response,
      }));
    });

    const caseResponses = await Promise.all(caseDetailsPromises);

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

    const combinedData = hearingListResponse.HearingList.map((hearing) => {
      const caseDetail = caseResponses.find((caseResponse) =>
        caseResponse.criteria.some((caseItem) => caseItem.filingNumber === hearing.filingNumber[0])
      );
      const caseData = caseDetail ? caseDetail.criteria[0].responseList[0] : {};

      const pendingTaskDetail = pendingTaskResponses.find((taskResponse) => taskResponse.filingNumber === hearing.filingNumber[0]);
      const pendingTasksData = pendingTaskDetail ? pendingTaskDetail.data.length : 0;

      return {
        filingNumber: hearing.filingNumber[0],
        caseName: caseData.caseTitle || "",
        stage: caseData.stage || "",
        caseType: caseData.caseType || "",
        pendingTasks: pendingTasksData || "-",
      };
    });

    return { items: combinedData };
  };

  const { isLoading, data, isFetching, refetch, error } = useQuery("GET_PRE_HEARING_DATA", fetchCombinedData, {
    cacheTime: 0,
    ...config,
  });

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
