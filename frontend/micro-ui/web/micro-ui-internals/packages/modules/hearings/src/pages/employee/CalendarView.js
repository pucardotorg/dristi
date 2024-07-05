import React, { useEffect, useMemo, useState, useContext } from "react";
import FullCalendar from "@fullcalendar/react";
import dayGridPlugin from "@fullcalendar/daygrid";
import timeGridPlugin from "@fullcalendar/timegrid";
import interactionPlugin from "@fullcalendar/interaction";
import useGetHearings from "../../hooks/hearings/useGetHearings";
import useGetHearingSlotMetaData from "../../hooks/services/useGetHearingSlotMetaData";
import PreHearingModal from "../../components/PreHearingModal";
import { DataContext } from "../../components/DataContext";

const tenantId = window.localStorage.getItem("tenant-id");

const MonthlyCalendar = () => {
  const { updateHearingData } = useContext(DataContext);
  const [currentMonth, setCurrentMonth] = useState(new Date());
  const [showModal, setShowModal] = useState(false);
  const [currentEvent, setCurrentEvent] = useState(null);

  const reqBody = {
    hearing: { tenantId },
    criteria: {},
  };

  const { data: hearingResponse, refetch: refetch } = useGetHearings(reqBody, { applicationNumber: "", cnrNumber: "", tenantId }, "dristi", true);
  const { data: AdvocateSlotsResponse, refetch: refetchGetHearingSlotMetaData } = useGetHearingSlotMetaData(true);
  const hearingDetails = useMemo(() => hearingResponse?.HearingList || [], [hearingResponse]);

  const events = useMemo(() => AdvocateSlotsResponse || [], [AdvocateSlotsResponse]);
  useEffect(() => {
    const fetchData = async () => {
      try {
        await refetch();
      } catch (error) {
        console.error("Error refetching Hearings data:", error);
      }
      try {
        await refetchGetHearingSlotMetaData();
      } catch (error) {
        console.error("Error refetching Advocate Slots data:", error);
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
          if (dateTimeObj.time >= slot.slotStartTime && dateTimeObj.time < slot.slotEndTime) {
            const eventKey = `${dateString}-${slot.slotName}`;

            if (!calendarEvents[eventKey]) {
              calendarEvents[eventKey] = {
                title: `${slot.slotName} Hearing`,
                start: `${dateString}T${slot.slotStartTime}`,
                end: `${dateString}T${slot.slotEndTime}`,
                extendedProps: {
                  hearings: [hearing],
                  count: 1,
                  date: new Date(dateString),
                  slot: slot.slotName,
                },
              };
            } else {
              calendarEvents[eventKey].extendedProps.count += 1;
              calendarEvents[eventKey].extendedProps.hearings.push(hearing);
            }
          }
        });
      }
    });

    const eventsArray = Object.values(calendarEvents);
    return eventsArray;
  }, [hearingDetails, events.slots]);

  const handleEventClick = (arg) => {
    setCurrentEvent(arg.event);
    updateHearingData({
      hearingDate: arg.event.extendedProps.date.toISOString().split("T")[0],
      hearingSlot: arg.event.extendedProps.slot,
    });
    setShowModal(true);
  };

  const closeModal = () => {
    setShowModal(false);
    setCurrentEvent(null);
  };

  const Close = () => (
    <svg width="24" height="24" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
      <g>
        <path d="M19 6.41L17.59 5L12 10.59L6.41 5L5 6.41L10.59 12L5 17.59L6.41 19L12 13.41L17.59 19L19 17.59L13.41 12L19 6.41Z" fill="#0A0A0A" />
      </g>
    </svg>
  );

  const CloseBtn = (props) => {
    return (
      <div style={{ padding: "10px" }} onClick={props.onClick}>
        <Close />
      </div>
    );
  };

  const Heading = (props) => {
    return <h1 className="heading-m">{props.label}</h1>;
  };

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
          return <div>{`${arg.event.extendedProps.slot} : ${arg.event.extendedProps.count}-hearings`}</div>;
        }}
        eventClick={handleEventClick}
      />

      {showModal && <PreHearingModal onCancel={closeModal} />}
    </div>
  );
};

export default MonthlyCalendar;
