import { useQuery } from "react-query";
import { sideMenuConfig } from "../../pages/citizen/FileCase/Config";

function useGetAllCasesConfig() {
  const { isLoading, data: getAllConfig, isFetching, refetch, error } = useQuery(
    `GET_ALL_CONFIGS`,
    async () => {
      const tenantId = Digit.ULBService.getCurrentTenantId();
      const configData = await Promise.all(
        sideMenuConfig.map(async (data) => {
          const children = await Promise.all(
            data.children.map(async (child) => {
              let configData = [];
              if (Array.isArray(child.pageConfig)) {
                configData = await window?.Digit.MDMSService.call(tenantId, {
                  moduleDetails: child.pageConfig,
                });
                return { ...child, pageConfig: configData?.MdmsRes?.commonUiConfig?.[child?.pageConfig?.[0]?.masterDetails?.[0]?.name]?.[0] };
              } else return child;
            })
          );
          return {
            ...data,
            children,
          };
        })
      );
      return configData;
    },
    {
      cacheTime: Infinity,
      staleTime: Infinity,
      enabled: true,
      retry: false, // Disable automatic retries to prevent flooding the API with requests
    }
  );

  if (error) {
    console.error("Error fetching configs:", error);
  }

  return {
    isLoading,
    isFetching,
    data: getAllConfig,
    refetch,
    error,
  };
}

export default useGetAllCasesConfig;
