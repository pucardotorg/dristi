import dayGridPlugin from "@fullcalendar/daygrid";
import interactionPlugin from "@fullcalendar/interaction";
import FullCalendar from "@fullcalendar/react";
import timeGridPlugin from "@fullcalendar/timegrid";
import React, { useEffect, useMemo, useState } from "react";
import { useHistory } from "react-router-dom/";
import PreHearingModal from "../../components/PreHearingModal";
import useGetHearings from "../../hooks/hearings/useGetHearings";
import useGetHearingSlotMetaData from "../../hooks/useGetHearingSlotMetaData";

const tenantId = window.localStorage.getItem("tenant-id");

const MonthlyCalendar = () => {
  const history = useHistory();

  const [dateRange, setDateRange] = useState({});

  const search = window.location.search;
  const { fromDate, toDate, slot } = useMemo(() => {
    const searchParams = new URLSearchParams(search);
    const fromDate = searchParams.get("from-date") || null;
    const toDate = searchParams.get("to-date") || null;
    const slot = searchParams.get("slot") || null;
    return { fromDate, toDate, slot };
  }, [search]);

  const reqBody = {
    criteria: { tenantId, fromDate: dateRange.start?.toISOString().split("T")[0], toDate: dateRange.end?.toISOString().split("T")[0] },
  };

  const { data: hearingResponse, refetch: refetch } = useGetHearings(
    reqBody,
    { applicationNumber: "", cnrNumber: "", tenantId },
    `${dateRange.start?.toISOString()}-${dateRange.end?.toISOString()}`,
    dateRange.start && dateRange.end
  );
  const { data: hearingSlots } = useGetHearingSlotMetaData(true);
  const hearingDetails = useMemo(() => hearingResponse?.HearingList || [], [hearingResponse]);

  const events = useMemo(() => hearingSlots || [], [hearingSlots]);

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
    const fromDate = new Date(arg.event.extendedProps.date);
    const toDate = new Date(fromDate);
    toDate.setDate(fromDate.getDate() + 1);

    const searchParams = new URLSearchParams(search);
    searchParams.set("from-date", fromDate.toISOString().split("T")[0]);
    searchParams.set("to-date", toDate.toISOString().split("T")[0]);
    searchParams.set("slot", arg.event.extendedProps.slot);

    history.replace({ search: searchParams.toString() });
  };

  const closeModal = () => {
    const searchParams = new URLSearchParams(search);
    searchParams.delete("from-date");
    searchParams.delete("to-date");
    searchParams.delete("slot");
    history.replace({ search: searchParams.toString() });
  };

  const Close = () => (
    <svg width="24" height="24" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
      <g>
        <path d="M19 6.41L17.59 5L12 10.59L6.41 5L5 6.41L10.59 12L5 17.59L6.41 19L12 13.41L17.59 19L19 17.59L13.41 12L19 6.41Z" fill="#0A0A0A" />
      </g>
    </svg>
  );

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
        datesSet={(dateInfo) => {
          setDateRange({ start: dateInfo.start, end: dateInfo.end });
        }}
      />

      {fromDate && toDate && slot && <PreHearingModal onCancel={closeModal} hearingData={{ fromDate, toDate, slot }} />}
    </div>
  );
};

export default MonthlyCalendar;
