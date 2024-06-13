import React, { useState } from "react";
import { Calendar } from "react-date-range";
import { CalendarLeftArrow, CalendarRightArrow } from "../icons/svgIndex";
import { Button, CardHeader } from "@egovernments/digit-ui-react-components";

function CustomCalendar({ config, t, handleSelect, onCalendarConfirm, selectedCustomDate }) {
  const renderCustomDay = (date) => {
    const isToday = date.getDate() === new Date().getDate(); // Check if the date is today
    const isWeekend = date.getDay() === 0 || date.getDay() === 6; // Check if the date is a weekend

    return (
      // <div style={{ textAlign: "center", fontSize: "14px", paddingTop: "5px" }}>
      <div>
        <span class="rdrDayNumber">{date.getDate()}</span>
        {isToday && <div style={{ fontSize: "8px", color: "#931847", marginTop: "20px" }}>10 Hearings</div>}
      </div>
      //   {isWeekend && <div style={{ color: "red" }}>Weekend</div>}
      // </div>
    );
  };
  const navigatorRenderer = (currentDate, changeShownDate, props) => {
    return (
      <div className="custom-navigator">
        <span>{currentDate.toLocaleDateString("default", { month: "long", year: "numeric" })}</span>
        <span>
          <button onClick={() => changeShownDate(-1, "monthOffset")}>
            <CalendarLeftArrow />{" "}
          </button>
          <button onClick={() => changeShownDate(1, "monthOffset")}>
            <CalendarRightArrow />
          </button>
        </span>
      </div>
    );
  };
  const minDate = new Date(); // For example, today
  const maxDate = new Date(2025, 11, 31);

  return (
    <div>
      <Calendar
        date={selectedCustomDate}
        onChange={handleSelect}
        minDate={minDate}
        maxDate={maxDate}
        dayContentRenderer={renderCustomDay}
        navigatorRenderer={navigatorRenderer}
      />
      {config?.showBottomBar && (
        <div className="calendar-bottom-div">
          <CardHeader>12 {config?.label}</CardHeader>
          <Button variation="primary" onButtonClick={() => onCalendarConfirm()} label={t(config?.buttonText)}></Button>
        </div>
      )}
    </div>
  );
}

export default CustomCalendar;
