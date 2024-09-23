import { useEffect } from "react";
import { useQuery, useQueryClient } from "react-query";
import useGetSubmissions from "./useGetSubmissions";
import { DRISTIService } from "../../services";

const useEvidenceDetails = ({ url, params, body, config = {}, plainAccessRequest, state, changeQueryName = "Random" }) => {
  const client = useQueryClient();

  const { searchForm } = state;
  const { stage, type, caseNameOrId } = searchForm;

  const getIndividual = async (individualIdsToSearch) => {
    return await Promise.all(
      individualIdsToSearch.map(
        async (id) =>
          await DRISTIService.searchIndividualUser(
            {
              Individual: {
                individualId: id,
              },
            },
            { ...params, limit: 1000, offset: 0 },
            plainAccessRequest,
            true
          )
      )
    );
  };

  const fetchCombinedData = async () => {
    //need to filter this hearing list response based on slot
    const res = await DRISTIService.searchEvidence(body, params, plainAccessRequest, true);
    // .then((response) => {
    //   console.log(response, "RESPONSE");
    // });
    const owenrList = res.artifacts?.map((artifact) => artifact.sourceID);
    const individualIdsToSearch = [...new Set(owenrList)];
    const owners = individualIdsToSearch?.length > 0 ? await getIndividual(individualIdsToSearch) : [];
    const owner = { Individual: owners.map((owner) => owner.Individual[0]) };
    return {
      ...res,
      artifacts: res.artifacts.map((artifact) => {
        return {
          ...artifact,
          owner: `${owner.Individual.find((individual) => artifact.sourceID === individual.individualId)?.name.givenName} ${
            owner.Individual.find((individual) => artifact.sourceID === individual.individualId)?.name.familyName || ""
          }`.trim(),
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

export default useEvidenceDetails;
