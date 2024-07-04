import { useQuery } from "react-query";

const usePreHearingModalData = ({ url, params, body, config = {}, plainAccessRequest, state, changeQueryName = "Random" }) => {
  const { isLoading, data, isFetching, refetch, error } = useQuery(
    "GET_PRE_HEARING_DATA",
    () =>
      Digit.CustomService.getResponse({
        url,
        params,
        body,
        plainAccessRequest,
      }).then((response) => {
        return {
          ...response,
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
    revalidate: () => {
      data && client.invalidateQueries({ queryKey: [url, changeQueryName] });
    },

    refetch,
    error,
  };
};

export default usePreHearingModalData;
