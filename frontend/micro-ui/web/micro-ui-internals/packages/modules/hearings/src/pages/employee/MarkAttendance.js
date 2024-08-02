import React, { useMemo, useState } from "react";
import { CloseSvg, Modal } from "@egovernments/digit-ui-react-components";
import DisplayAttendees from "./DisplayAttendees";
import AddAttendees from "./AddAttendees";
import { hearingService } from "../../hooks/services";

const MarkAttendance = ({ handleModal, attendees = [], setAttendees, hearingData = {}, setAddPartyModal }) => {
  const partiesToAttend = attendees.length;
  const onlineAttendees = attendees.filter((attendee) => attendee.isOnline && attendee.wasPresent);
  const offlineAttendees = attendees.filter((attendee) => !attendee.isOnline && attendee.wasPresent );
  
  const [isAddingAttendees, setIsAddingAttendee] = useState(false);
  const [formError, setFormError] = useState("");
  const tenantId = window?.Digit.ULBService.getCurrentTenantId();
  const [form, setForm] = useState();
  const [isDisabled, setIsDisabled] = useState(false);
  const handleAttendees = () => {
    setIsAddingAttendee(!isAddingAttendees);
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
        return { ...attendee, isOnline: true, wasPresent: true };
      }
      if (offlineIds.includes(attendee.individualId)) {
        return { ...attendee, isOnline: false, wasPresent: true };
      }
      return { ...attendee, wasPresent: false };
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
  const style = useMemo(() => {
    return isAddingAttendees ? {} : { display: "none" };
  }, [isAddingAttendees]);

  return (
    <Modal
      popupStyles={{
        width: "40%",
        minWidth: "300px",
        position: "absolute",
        height: "400px",
        top: "50%",
        left: "50%",
        transform: "translate(-50%, -50%)",
        padding: "12px 24px",
        justify: "space-between",
      }}
      popupModuleActionBarStyles={style}
      popupModuleMianStyles={{
        padding: "18px",
        margin: "0px",
        height: "300px",
        height: "calc(100% - 54px)",
        overflowY: "auto",
      }}
      headerBarMain={
        <h1 className="heading-m" style={{ textAlign: "left" }}>
          {isAddingAttendees ? "Add Attendees" : "Mark Attendance"}
        </h1>
      }
      headerBarEnd={<CloseSvg onClick={handleModal} />}
      formId="modal-action"
      actionCancelLabel={"Back"}
      actionCancelOnSubmit={() => setIsAddingAttendee(!isAddingAttendees)}
      actionSaveLabel={"Add"}
      actionSaveOnSubmit={() => onFormSubmit(form)}
      isDisabled={isDisabled}
    >
      <div style={{ width: "100%", padding: "16px", textAlign: "left" }}>
        {isAddingAttendees ? (
          <AddAttendees
            attendees={attendees}
            setAttendees={setAttendees}
            handleAttendees={handleAttendees}
            setAddPartyModal={setAddPartyModal}
            handleModal={handleModal}
            setFormError={setFormError}
            formError={formError}
            form={form}
            setForm={setForm}
            setIsDisabled={setIsDisabled}
          />
        ) : (
          <DisplayAttendees
            partiesToAttend={partiesToAttend}
            onlineAttendees={onlineAttendees}
            offlineAttendees={offlineAttendees}
            handleAttendees={handleAttendees}
            handleModal={handleModal}
          />
        )}
      </div>
    </Modal>
  );
};

export default MarkAttendance;
