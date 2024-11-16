import { useEffect } from "react";
import { useQuery, useQueryClient } from "react-query";
import useGetSubmissions from "./useGetSubmissions";
import { DRISTIService } from "../../services";

const useApplicationDetails = ({ url, params, body, config = {}, plainAccessRequest, state, changeQueryName = "Random" }) => {
  const client = useQueryClient();

  const { searchForm } = state;
  const { stage, type, caseNameOrId } = searchForm;

  const fetchCombinedData = async () => {
    //need to filter this hearing list response based on slot
    const res = await DRISTIService.searchSubmissions(body, params, plainAccessRequest, true);
    // .then((response) => {
    //   console.log(response, "RESPONSE");
    // });
    const owenrList = res.applicationList.map((application) => application.auditDetails.createdBy);
    const owners =
      owenrList.length > 0
        ? await DRISTIService.searchIndividualUser(
            {
              Individual: {
                userUuid: [...new Set(owenrList)],
              },
            },
            { ...params, limit: 1000, offset: 0 },
            plainAccessRequest,
            true
          )
        : [];
    return {
      ...res,
      applicationList: res.applicationList.map((application) => {
        return {
          ...application,
          owner: `${owners.Individual.find((individual) => application.auditDetails.createdBy === individual.userUuid)?.name.givenName} ${
            owners.Individual.find((individual) => application.auditDetails.createdBy === individual.userUuid)?.name.familyName
          }`,
        };
      }),
    };
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

export default useApplicationDetails;
