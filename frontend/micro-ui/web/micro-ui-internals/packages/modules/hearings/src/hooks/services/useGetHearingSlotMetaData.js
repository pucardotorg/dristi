import { useQuery } from "react-query";

function useGetHearingSlotMetaData(enabled) {
  const fetchAdvocateSlots = () => {
    const events = {
      tenantId: "kl",
      moduleName: "court",
      slots: [
        {
          id: 1,
          slotName: "Slot 1",
          slotStartTime: "00:00:00",
          slotEndTime: "13:00:00",
          slotDuration: "180",
          unitOfMeasurement: "Minutes",
        },
        {
          id: 2,
          slotName: "Slot 2",
          slotStartTime: "13:00:00",
          slotEndTime: "23:59:59",
          slotDuration: "180",
          unitOfMeasurement: "Minutes",
        },
      ],
    };
    return events;
  };

  const { isLoading, data: slotsResponse, isFetching, refetch, error } = useQuery("GET_ADVOCATE_SLOTS", fetchAdvocateSlots, {
    cacheTime: 0,
    enabled: Boolean(enabled),
    retry: false, // Disable automatic retries to prevent flooding with requests
  });

  if (error) {
    console.error("Error fetching advocate slots:", error);
  }

  return {
    isLoading,
    isFetching,
    data: slotsResponse,
    refetch,
    error,
  };
}

export default useGetHearingSlotMetaData;
