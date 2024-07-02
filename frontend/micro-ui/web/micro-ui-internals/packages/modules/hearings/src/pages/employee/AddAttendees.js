import React, { useEffect, useMemo, useState } from "react";
import { FormComposerV2 } from "@egovernments/digit-ui-react-components";
import useGetHearings from "../../../../dristi/src/hooks/dristi/useGetHearings";
import useUpdateHearing from "../../hooks/useUpdateHearing";

const AddAttendees = ({ hearingId }) => {
//   const tenantId = window?.Digit.ULBService.getCurrentTenantId();
//   const { data: hearingResponse, refetch: refetch } = useGetHearings(
//     { tenantId: tenantId },
//     { applicationNumber: "", cnrNumber: "", hearingId: hearingId },
//     "dristi",
//     true
//   );
//   const [attendees, setAttendees] = useState([]);
//   const [formError, setFormError] = useState("");
//   const [updatedHearingDetails, setUpdatedHearingDetails] = useState({});

//   useEffect(() => {
//     if (hearingResponse) {
//       const hearingData = hearingResponse?.HearingList[0];
//       if (hearingData) {
//         setAttendees(hearingData.attendees || []);
//         setUpdatedHearingDetails(hearingData || []);
//       }
//     }
//   }, [hearingResponse]);

  const questions = [
    {
      uuid: "onlineAttendees",
      questionStatement: "online",
      required: true,
    },
    {
      uuid: "offlineAttendees",
      questionStatement: "offline",
      required: true,
    },
  ];

  //   const { data: updateHearingResponse, updateRefetch } = useUpdateHearing(
  //     { hearing: { tenantId, hearing } },
  //     { applicationNumber: "", cnrNumber: "" },
  //     "dristi",
  //     true
  //   );

  const { data: updatehearingResponse, refetch: updaterefetch } = useUpdateHearing( updatedHearingDetails, "dristi", true);

  const onFormSubmit = async (data) => {
    const onlineAttendees = data.onlineAttendees || [];
    const offlineAttendees = data.offlineAttendees || [];

    const onlineIds = onlineAttendees.map((a) => a.value);
    const offlineIds = offlineAttendees.map((a) => a.value);
    const duplicateIds = onlineIds.filter((id) => offlineIds.includes(id));

    if (duplicateIds.length > 0) {
      setFormError("Attendees cannot be selected for both online and offline.");
      return;
    }

    const updatedAttendees = attendees.map((attendee) => {
      if (onlineIds.includes(attendee.individualId)) {
        return { ...attendee, type: "ONLINE" };
      }
      if (offlineIds.includes(attendee.individualId)) {
        return { ...attendee, type: "OFFLINE" };
      }
      return attendee;
    });
    console.log("hiiiiiiiii");
    try {
      const details = { ...hearingResponse.HearingList[0], attendees: updatedAttendees };
      setUpdatedHearingDetails({hearing: {...details}});
    } catch (error) {
      console.error("Error updating hearing:", error);
    }
    updaterefetch();
    setAttendees(updatedAttendees);
    setFormError("");
  };
  const formConfig = questions.map((question) => ({
    isMandatory: question.required,
    key: question.uuid,
    type: "multiselectdropdown",
    inline: false,
    disable: false,
    label: `Select ${question.questionStatement} Attendees`,
    defaultValue: [],
    populators: {
      name: question.uuid,
      optionsKey: "label",
      defaultText: "select attendees",
      selectedText: "attendees",
      options: attendees.map((attendee) => ({
        value: attendee.individualId,
        label: attendee.name,
      })),
      required: question.required,
      error: "Required",
    },
  }));

  return (
    <div>
      {formError && <p style={{ color: "red" }}>{formError}</p>}
      <FormComposerV2
        inline={false}
        config={[
          {
            body: formConfig,
          },
        ]}
        onSubmit={onFormSubmit}
        label="Submit"
      />
    </div>
  );
};

export default AddAttendees;
