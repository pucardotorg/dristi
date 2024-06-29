import React, { useState } from 'react';
import { CardText, CloseSvg, Modal } from "@egovernments/digit-ui-react-components";
import { Card } from '@egovernments/digit-ui-components';
import DisplayAttendees from './DisplayAttendees';
import AddAttendees from './AddAttendees';

const MarkAttendance = ({ closeModal }) => {

  const parties_to_attend = 12;
  const online_attendance = 0;
  const offline_attendance = 0;
  const [isAddingAttendees ,setIsAddingAttendee] = useState(false);
  const hrStyle = {
    border: 'none',
    borderTop: '2px solid #E8E8E8',
    margin: '12px 0'
  };

  const handleAttendees = () =>{
    setIsAddingAttendee(!isAddingAttendees)
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
        display: "flex",
        flex: 1,
        justifyContent: "end",
        padding: "18px",
      }}
      popupModuleMianStyles={{
        padding: "18px",
        margin: '0px',
        height: 'calc(100% - 54px)', // Adjust height for the action bar
        overflowY: 'auto', // Make the content scrollable
      }}
      headerBarMain={<h1 className="heading-m" style={{ textAlign: 'left' }}>Add Attendance</h1>}
      headerBarEnd={<CloseSvg onClick={closeModal} />}
      actionCancelLabel={"Share Link"}
      actionCancelOnSubmit={closeModal}
      actionSaveLabel={"Done"}
      actionSaveOnSubmit={() => {
        closeModal();
      }}
      formId="modal-action"
    >

      <div style={{ width: '100%', padding: '16px', textAlign: 'left' }}>
        {isAddingAttendees ?<AddAttendees /> :
        <DisplayAttendees parties_to_attend = {parties_to_attend} offline_attendance  = {offline_attendance} online_attendance = {online_attendance} handleAttendees = {handleAttendees} />}
      </div>
    </Modal>
  );
}

export default MarkAttendance;
