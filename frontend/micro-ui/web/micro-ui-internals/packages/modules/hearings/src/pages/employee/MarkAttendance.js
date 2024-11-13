import React, { useMemo, useState } from "react";
import { CloseSvg, Modal } from "@egovernments/digit-ui-react-components";
import DisplayAttendees from "./DisplayAttendees";
import AddAttendees from "./AddAttendees";
import { hearingService } from "../../hooks/services";
import { useTranslation } from "react-i18next";

const MarkAttendance = ({ handleModal, attendees = [], hearingData = {}, setAddPartyModal, refetchHearing }) => {
  const partiesToAttend = attendees.length;
  const { t } = useTranslation();
  const onlineAttendees = attendees.filter((attendee) => attendee.isOnline && attendee.wasPresent);
  const offlineAttendees = attendees.filter((attendee) => !attendee.isOnline && attendee.wasPresent);

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
      setFormError(t("BOTH_CANT_BE_SELECTED"));
      return;
    }

    const updatedAttendees = attendees.map((attendee) => {
      if (onlineIds.includes(attendee.individualId || attendee.name)) {
        return { ...attendee, isOnline: true, wasPresent: true };
      }
      if (offlineIds.includes(attendee.individualId || attendee.name)) {
        return { ...attendee, isOnline: false, wasPresent: true };
      }
      return { ...attendee, wasPresent: false };
    });
    try {
      const hearing = { ...hearingData, attendees: updatedAttendees };
      hearingService.updateHearingTranscript({ tenantId, hearing, hearingType: "", status: "" }, "").then(() => refetchHearing());
    } catch (error) {
      refetchHearing();
      console.error("Error updating hearing:", error);
    }
    handleAttendees();
    setFormError("");
  };
  const style = useMemo(() => {
    return isAddingAttendees ? {} : { display: "none" };
  }, [isAddingAttendees]);

  return (
    <Modal
      popupStyles={{
        width: "50vw",
        minWidth: "600px",
        position: "absolute",
        top: "50%",
        left: "50%",
        transform: "translate(-50%, -50%)",
        justify: "space-between",
      }}
      popupModuleActionBarStyles={style}
      popupModuleActionBarClass={"hearing-secondary-button"}
      popupModuleMianStyles={{
        padding: 0,
        margin: "0px",
        height: "calc(100% - 54px)",
        overflowY: "auto",
      }}
      headerBarMain={
        <h1 className="heading-m" style={{ textAlign: "left" }}>
          {isAddingAttendees ? t("ADD_ATTENDEES_LABEL") : t("MARK_ATTENDANCE")}
        </h1>
      }
      headerBarEnd={
        <h2 style={{ padding: "5px 10px" }}>
          <CloseSvg onClick={handleModal} />
        </h2>
      }
      formId="modal-action"
      actionCancelLabel={t("HEARING_BACK")}
      actionCancelOnSubmit={() => setIsAddingAttendee(!isAddingAttendees)}
      actionSaveLabel={t("HEARING_ADD")}
      actionSaveOnSubmit={() => onFormSubmit(form)}
      isDisabled={isDisabled}
    >
      <div style={{ width: "100%", padding: "16px 0px", textAlign: "left" }}>
        {isAddingAttendees ? (
          <AddAttendees
            attendees={attendees}
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
