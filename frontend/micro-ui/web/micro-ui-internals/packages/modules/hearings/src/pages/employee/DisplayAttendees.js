import { Button } from '@egovernments/digit-ui-react-components';
import React from 'react';
import { useTranslation } from 'react-i18next';

const DisplayAttendees = ({partiesToAttend, offlineAttendance, onlineAttendance, handleAttendees, handleModal }) => {
  const {t} = useTranslation();


  return (
    <div>
      <div style={{ textAlign: 'left', fontSize: "24px", backgroundColor: '#F7F5F3', padding: '16px 24px' }}>
        <div style={{display:'flex', gap:'16px'}}>
          <span>Parties to attend</span>   
          <span>{partiesToAttend}</span>
        </div>
        <div style={{display:'flex', gap:'16px'}}>
          <span>Parties in Attendance Online </span>  
          <span>{onlineAttendance}</span> 
        </div>
        <div style={{display:'flex', gap:'16px'}}>
          <span>Parties in Attendance Offline</span>    
          <span>{offlineAttendance}</span>
        </div>
      </div>
      {onlineAttendance !== 0 && (
        <div style={{ borderRadius: '4px 0px 0px 0px', border: '1px solid #E8E8E8', marginTop: '12px' }}>
          Display online
          <br />
          hello
        </div>
      )}
      {offlineAttendance !== 0 && (
        <div style={{ border: '1px solid #E8E8E8', marginTop: '12px', height: '100px' }}>
          Display offline
          <hr style={{border: 'none', borderTop: '2px solid #E8E8E8', margin: '12px 0'}} />
        </div>
      )}
      {(onlineAttendance + offlineAttendance === 0) && (
        <div style={{ textAlign: 'left', marginTop: '12px' }}>
          {t("NO_MARKED_ATTENDEES")}
        </div>
      )}
      <button style={{ margin: '16px', color: 'blue' }} onClick={handleAttendees}>Add Attendees</button> 
      <div style={{display:'flex' ,justifyContent:"flex-end", gap:"16px"}}>
        <Button label={"Share Link"}></Button>
        <Button label={"Done"} onButtonClick={handleModal}></Button>
      </div>
    </div>
  );
};

export default DisplayAttendees;
