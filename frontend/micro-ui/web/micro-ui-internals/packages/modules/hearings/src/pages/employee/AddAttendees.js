import React, { useEffect, useMemo, useState } from "react";
import { FormComposerV2 } from "@egovernments/digit-ui-react-components";

const AddAttendees = ({ attendees=[],setAttendees ,refetch ,handleAttendees, hearingData , setUpdatedHearingDetails}) => {

    const [formError, setFormError] = useState("");
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
    try {
      const details = { ...hearingData, attendees: updatedAttendees };
      console.log(details)
      setUpdatedHearingDetails({hearing: {...details}});
    } catch (error) {
      console.error("Error updating hearing:", error);
    }
    refetch();
    setAttendees(updatedAttendees);
    handleAttendees()
    setFormError("");
  };
  const formConfig =[{
    isMandatory: true,
    key: "onlineAttendees",
    type: "multiselectdropdown",
    inline: false,
    disable: false,
    label: `Select online Attendees`,
    defaultValue: [],
    populators: {
      name: "onlineAttendees",
      optionsKey: "label",
      defaultText: "select attendees",
      selectedText: "attendees",
      options: attendees.map((attendee) => ({
        value: attendee.individualId,
        label: attendee.name,
      })),
      error: "Required",
    },
  },
  {
    isMandatory: true,
    key: "offlineAttendees",
    type: "multiselectdropdown",
    inline: false,
    disable: false,
    label: `Select Offline Attendees`,
    defaultValue: [],
    populators: {
      name: "offlineAttendees",
      optionsKey: "label",
      defaultText: "select attendees",
      selectedText: "attendees",
      options: attendees.map((attendee) => ({
        value: attendee.individualId,
        label: attendee.name,
      })),
      error: "Required",
    },
  }];

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
