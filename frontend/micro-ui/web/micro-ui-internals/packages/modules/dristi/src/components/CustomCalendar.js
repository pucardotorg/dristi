import React from "react";
import { Calendar } from "react-date-range";
import { CalendarLeftArrow, CalendarRightArrow } from "../icons/svgIndex";
import { Button, CardHeader } from "@egovernments/digit-ui-react-components";

function CustomCalendar({ config, t, handleSelect, onCalendarConfirm, selectedCustomDate, hearingDetails }) {
  const hearingCounts = {};

  // Count the number of hearings for each date
  hearingDetails &&
    hearingDetails.forEach((hearing) => {
      const date = new Date(hearing.startTime).toLocaleDateString("en-CA"); // Format as YYYY-MM-DD
      if (hearingCounts[date]) {
        hearingCounts[date]++;
      } else {
        hearingCounts[date] = 1;
      }
    });

  const renderCustomDay = (date) => {
    const dateStr = date.toLocaleDateString("en-CA"); // Get the date string in YYYY-MM-DD format
    const hearingCount = hearingCounts[dateStr] || 0; // Get the count of hearings for the date

    return (
      <div>
        <span className="rdrDayNumber">{date.getDate()}</span>
        {hearingCount > 0 && (
          <div style={{ fontSize: "8px", color: "#931847", marginTop: "2px" }}>
            {hearingCount} {t("HEARINGS")}
          </div>
        )}
      </div>
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
        // minDate={minDate}
        maxDate={maxDate}
        dayContentRenderer={renderCustomDay}
        navigatorRenderer={navigatorRenderer}
      />
      {config?.showBottomBar && (
        <div className="calendar-bottom-div">
          <CardHeader>
            {hearingDetails?.length} {t(config?.label)}
          </CardHeader>
          <Button variation="primary" onButtonClick={() => onCalendarConfirm()} label={t(config?.buttonText)}></Button>
        </div>
      )}
    </div>
  );
}

export default CustomCalendar;
