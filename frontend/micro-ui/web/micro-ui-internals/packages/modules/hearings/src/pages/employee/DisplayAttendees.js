import { Button } from '@egovernments/digit-ui-react-components';
import React from 'react';
import { useTranslation } from 'react-i18next';

const DisplayAttendees = ({partiesToAttend, onlineAttendees =[], offlineAttendees =[], handleAttendees, handleModal }) => {
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
          <span>{onlineAttendees.length}</span> 
        </div>
        <div style={{display:'flex', gap:'16px'}}>
          <span>Parties in Attendance Offline</span>    
          <span>{offlineAttendees.length}</span>
        </div>
      </div>
      {onlineAttendees.length !== 0 && (
        <div style={{ borderRadius: '4px 0px 0px 0px', border: '1px solid #E8E8E8', marginTop: '12px',padding:'4px' }}>
           Online Attendees
          <hr style={{border: 'none', borderTop: '2px solid #E8E8E8', margin: '12px 0'}} />
                <ul>
              {onlineAttendees.map(attendee => (
                <li key={attendee.individualId}>
                  {attendee.name} 
                </li>
              ))}
            </ul>
        </div>
      )}
      {offlineAttendees.length !== 0 && (
        <div style={{ border: '1px solid #E8E8E8', marginTop: '12px', height: '100px' }}>
           Offline Attendees
          <hr style={{border: 'none', borderTop: '2px solid #E8E8E8', margin: '12px 0'}} />
          <ul>
        {offlineAttendees.map(attendee => (
          <li key={attendee.individualId}>
            {attendee.name} - {attendee.associatedWith}
          </li>
        ))}
      </ul>
        </div>
      )}
      {(onlineAttendees.length  + offlineAttendees.length === 0) && (
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
