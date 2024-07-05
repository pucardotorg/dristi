
import React, { useState } from "react";
import { Button, CloseSvg, Modal } from "@egovernments/digit-ui-react-components";
import DisplayAttendees from "./DisplayAttendees";
import AddAttendees from "./AddAttendees";

const MarkAttendance = ({ handleModal, attendees = [], setAttendees, hearing = {} ,setAddPartyModal}) => {
  const partiesToAttend = attendees.length;
  const onlineAttendees = attendees.filter(attendee => attendee.type === 'ONLINE');
  const offlineAttendees = attendees.filter(attendee => attendee.type === 'OFFLINE');
  const [isAddingAttendees, setIsAddingAttendee] = useState(false);

  const handleAttendees = () => {
    console.log(attendees);
    setIsAddingAttendee(!isAddingAttendees);
  };

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
      popupModuleActionBarStyles={{
        display: "none",
      }}
      popupModuleMianStyles={{
        padding: "18px",
        margin: "0px",
        height: "300px",
        height: "calc(100% - 54px)",
        overflowY: "auto",
      }}
      headerBarMain={
        <h1 className="heading-m" style={{ textAlign: "left" }}>
          Add Attendance
        </h1>
      }
      headerBarEnd={<CloseSvg onClick={handleModal} />}
      formId="modal-action"
    >
      <div style={{ width: "100%", padding: "16px", textAlign: "left" }}>
        {isAddingAttendees ? (
          <AddAttendees
            attendees={attendees}
            setAttendees={setAttendees}
            handleAttendees={handleAttendees}
            hearingData={hearing}
            setAddPartyModal= {setAddPartyModal}
            handleModal= {handleModal}
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
