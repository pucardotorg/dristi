import React, { useEffect, useMemo, useState } from "react";
import { Button, FormComposerV2 } from "@egovernments/digit-ui-react-components";
import { hearingService } from "../../../../hearings/src/hooks/services/index";
import AddParty from "./AddParty";
const AddAttendees = ({ attendees = [], setAttendees, handleAttendees, hearingData, setAddPartyModal, handleModal }) => {
  const [formError, setFormError] = useState("");
  const tenantId = window?.Digit.ULBService.getCurrentTenantId();

  const onClickAddWitness = () => {
    handleModal();
    setAddPartyModal(true);
  };

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
        return { ...attendee, type: "ONLINE", wasPresent: true };
      }
      if (offlineIds.includes(attendee.individualId)) {
        return { ...attendee, type: "OFFLINE", wasPresent: true };
      }
      return attendee;
    });
    try {
      const hearing = { ...hearingData, attendees: updatedAttendees };
      hearingService.updateHearing({ tenantId, hearing, hearingType: "", status: "" }, "");
    } catch (error) {
      console.error("Error updating hearing:", error);
    }
    setAttendees(updatedAttendees);
    handleAttendees();
    setFormError("");
  };

  const formConfig = [
    {
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
    },
  ];

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
      <Button label={"Add Party"} onButtonClick={onClickAddWitness}></Button>
    </div>
  );
};

export default AddAttendees;
