import React, { useState } from "react";
import FullCalendar from "@fullcalendar/react";
import dayGridPlugin from "@fullcalendar/daygrid";
import timeGridPlugin from "@fullcalendar/timegrid";
import interactionPlugin from "@fullcalendar/interaction";

const MonthlyCalendar = () => {
  const events = {
    tenantId: "kl",
    moduleName: "court",
    slots: [
      {
        id: 1,
        slotName: "Slot 1",
        slotStartTime: "10:00:00",
        slotEndTime: "13:00:00",
        slotDuration: "180",
        unitOfMeasurement: "Minutes",
      },
      {
        id: 2,
        slotName: "Slot 2",
        slotStartTime: "14:00:00",
        slotEndTime: "17:00:00",
        slotDuration: "180",
        unitOfMeasurement: "Minutes",
      },
    ],
  };
  return (
    <div>
      <FullCalendar
        plugins={[dayGridPlugin, timeGridPlugin, interactionPlugin]}
        initialView="dayGridMonth"
        events={[
          { title: "event 1", date: "2024-06-01" },
          { title: "event 2", date: "2024-06-02" },
        ]}
        headerToolbar={{
          start: "prev,next",
          center: "title",
          end: "dayGridMonth,timeGridWeek,timeGridDay"
        }}
        height={'80vh'}
      />
    </div>
  );
};

export default MonthlyCalendar;
