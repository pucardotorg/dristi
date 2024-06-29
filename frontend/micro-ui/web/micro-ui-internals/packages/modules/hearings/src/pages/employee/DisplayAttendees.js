import React from 'react';

const DisplayAttendees = ({parties_to_attend , offline_attendance ,online_attendance,handleAttendees }) => {


  return <div>
  <div style={{ textAlign: 'left', fontSize: "24px", backgroundColor: '#F7F5F3', padding: '16px 24px' }}>
  <div style={{display:'flex' ,gap:'16px'}}>
      <span>Parties to attend</span>   
      <span>{parties_to_attend}</span>
    </div>
    <div style={{display:'flex' ,gap:'16px'}}>
      <span>Parties in Attendance Online </span>  
      <span>{online_attendance}</span> 
    </div>
    <div style={{display:'flex' ,gap:'16px'}}>
      <span>Parties to Attendance Offline</span>    
       <span>{offline_attendance}</span>
    </div>
  </div>
  {online_attendance !== 0 && (
    <div style={{ borderRadius: '4px 0px 0px 0px', border: '1px solid #E8E8E8', marginTop: '12px' }}>
      Display online
      <br />
      hello
    </div>
  )}
  {offline_attendance !== 0 && (
    <div style={{ border: '1px solid #E8E8E8', marginTop: '12px', height: '100px' }}>
      Display offline
      <hr style={hrStyle} />
    </div>
  )}
  {(online_attendance + offline_attendance === 0) && (
    <div style={{ textAlign: 'left', marginTop: '12px' }}>
      There are currently no marked attendees for this hearing. Marked attendees will appear here.
    </div>
  )}
  <button style={{margin : '16px' ,color : 'blue'}} onClick={handleAttendees}>Add Attendees</button> </div>
};

export default DisplayAttendees;
