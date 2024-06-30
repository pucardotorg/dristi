import React, { useEffect, useMemo, useState } from "react";
import FullCalendar from "@fullcalendar/react";
import dayGridPlugin from "@fullcalendar/daygrid";
import timeGridPlugin from "@fullcalendar/timegrid";
import interactionPlugin from "@fullcalendar/interaction";
import useGetHearings from "../hooks/dristi/useGetHearings";

const MonthlyCalendar = () => {
  const tenantId = "pg";
  const [currentMonth, setCurrentMonth] = useState(new Date()); // State to track the current month
  const { data: hearingResponse, refetch: refetch } = useGetHearings(
    { hearing: { tenantId }, tenantId },
    { applicationNumber: "", cnrNumber: "", tenantId },
    "dristi",
    true
  );
  const hearingDetails = useMemo(() => hearingResponse?.HearingList || [], [hearingResponse]);

  const events = {
    tenantId: "kl",
    moduleName: "court",
    slots: [
      {
        id: 1,
        slotName: "Slot 1",
        slotStartTime: "00:00:00",
        slotEndTime: "13:00:00",
        slotDuration: "180",
        unitOfMeasurement: "Minutes",
      },
      {
        id: 2,
        slotName: "Slot 2",
        slotStartTime: "13:00:00",
        slotEndTime: "23:59:59",
        slotDuration: "180",
        unitOfMeasurement: "Minutes",
      },
    ],
  };

  useEffect(() => {
    const fetchData = async () => {
      try {
        await refetch();
      } catch (error) {
        console.error("Error refetching data:", error);
      }
    };

    fetchData();
  }, [currentMonth, refetch]);

  function epochToDateTimeObject(epochTime) {
    if (!epochTime || typeof epochTime !== "number") {
      return null;
    }

    const date = new Date(epochTime);
    const year = date.getFullYear();
    const month = String(date.getMonth() + 1).padStart(2, "0");
    const day = String(date.getDate()).padStart(2, "0");
    const hours = String(date.getHours()).padStart(2, "0");
    const minutes = String(date.getMinutes()).padStart(2, "0");
    const seconds = String(date.getSeconds()).padStart(2, "0");
    const dateTimeObject = {
      date: `${year}-${month}-${day}`,
      time: `${hours}:${minutes}:${seconds}`,
    };

    return dateTimeObject;
  }

  const Calendar_events = useMemo(() => {
    const calendarEvents = {};
  
    hearingDetails.forEach((hearing) => {
      const dateTimeObj = epochToDateTimeObject(hearing.startTime);
  
      if (dateTimeObj) {
        const dateString = dateTimeObj.date; 
        events.slots.forEach((slot) => {
          if (dateTimeObj.time>= slot.slotStartTime && dateTimeObj.time< slot.slotEndTime) {
            const eventKey = `${dateString}-${slot.slotName}`;
  
            if (!calendarEvents[eventKey]) {
              calendarEvents[eventKey] = {
                title: `${slot.slotName} Hearing`,
                start: `${dateString}T${slot.slotStartTime}`,
                end: `${dateString}T${slot.slotEndTime}`, 
                extendedProps: {
                  hearings: [hearing],
                  count:1,
                  date: new Date(dateString),
                  slot: slot.slotName,
                },
              };
            } else {
              calendarEvents[eventKey].extendedProps.count  += 1;
              calendarEvents[eventKey].extendedProps.hearings.push(hearing);
            }
          }
        });
      }
    });
  
    const eventsArray = Object.values(calendarEvents);
  
    console.log('calendarEvents', eventsArray);
    return eventsArray;
  }, [hearingDetails, events.slots]);
  
  
  
  

  return (
    <div>
      <FullCalendar
        plugins={[dayGridPlugin, timeGridPlugin, interactionPlugin]}
        initialView="dayGridMonth"
        headerToolbar={{
          start: "prev",
          center: "title",
          end: "next, dayGridMonth,timeGridWeek,timeGridDay",
        }}
        height={"85vh"}
        events={Calendar_events}
        eventContent={(arg) => {
          console.log('argEventStart',arg);
          return <div>{`${arg.event.extendedProps.slot} : ${arg.event.extendedProps.count}-hearings`}</div>;
        }}
      />
    </div>
  );
};

export default MonthlyCalendar;
