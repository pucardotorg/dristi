import React, { useState } from 'react';
import { Button, CloseSvg, Modal } from "@egovernments/digit-ui-react-components";
import DisplayAttendees from './DisplayAttendees';
import AddAttendees from './AddAttendees';

const MarkAttendance = ({ handleModal }) => {
  const partiesToAttend = 12;
  const onlineAttendance = 0;
  const offlineAttendance = 0;
  const [isAddingAttendees, setIsAddingAttendee] = useState(false);

  const handleAttendees = () => {
    setIsAddingAttendee(!isAddingAttendees);
  }


  return (
    <Modal
      popupStyles={{
        width: "40%",
        minWidth: '300px',
        position: "absolute",
        height: '350px',
        top: "50%",
        left: "50%",
        transform: "translate(-50%, -50%)",
        padding: '12px 24px',
        justify: 'space-between',
      }}
      popupModuleActionBarStyles={{
        display: "none",
      }}
      popupModuleMianStyles={{
        padding: "18px",
        margin: '0px',
        height: '300px',
        height: 'calc(100% - 54px)', 
        overflowY: 'auto',
      }}
      headerBarMain={<h1 className="heading-m" style={{ textAlign: 'left' }}>Add Attendance</h1>}
      headerBarEnd={<CloseSvg onClick={handleModal} />}
      formId="modal-action"
    >
      <div style={{ width: '100%', padding: '16px', textAlign: 'left' }}>
        {isAddingAttendees ? 
          <AddAttendees /> : 
          <DisplayAttendees 
            partiesToAttend={partiesToAttend} 
            offlineAttendance={offlineAttendance} 
            onlineAttendance={onlineAttendance} 
            handleAttendees={handleAttendees} 
            handleModal={handleModal} 
          />
        }
      </div>
    </Modal>
  );
}

export default MarkAttendance;
