import { useQuery, useQueryClient } from "react-query";

const useInboxCustomHook = ({ url, params, body, config = {}, plainAccessRequest, changeQueryName = "Random" }) => {
  const client = useQueryClient();
  const tenantId = Digit?.ULBService?.getCurrentTenantId() || "pg";
  // const userType = body?.inbox?.moduleSearchCriteria?.userType || {
  //   code: "advocate",
  //   name: "Advocate",
  //   additionalDetails: {
  //     businessService: ["advocate"],
  //     moduleName: "Advocate services",
  //   },
  // };
  delete body?.inbox?.moduleSearchCriteria?.userType;

  const requestBody = {
    inbox: {
      ...body?.inbox,
      moduleSearchCriteria: {
        ...body?.inbox?.moduleSearchCriteria,
        isActive: false,
        tenantId,
      },
      processSearchCriteria: {
        ...body?.inbox?.processSearchCriteria,
        tenantId,
      },
      tenantId,
    },
  };
  const { isLoading, data, isFetching, refetch, error } = useQuery(
    [url, changeQueryName],
    () =>
      Digit.CustomService.getResponse({
        url,
        params,
        body: requestBody,
        plainAccessRequest,
      }).then((response) => {
        return {
          ...response,
          items:
            response?.items?.map((data) => ({
              ...data,
              applicationNumber: data?.businessObject?.clerkDetails?.applicationNumber || data?.businessObject?.advocateDetails?.applicationNumber,
              username:
                data?.businessObject?.advocateDetails?.additionalDetails?.username || data?.businessObject?.clerkDetails?.additionalDetails?.username,
            })) || [],
        };
      }),
    {
      cacheTime: 0,
      ...config,
    }
  );

  return {
    isLoading,
    isFetching,
    data,
    refetch,
    revalidate: () => {
      data && client.invalidateQueries({ queryKey: [url, changeQueryName] });
    },
    error,
  };
};

export default useInboxCustomHook;
