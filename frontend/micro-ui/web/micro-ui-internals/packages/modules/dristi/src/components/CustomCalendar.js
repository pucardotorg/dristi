import React, { useState, useEffect, useMemo } from "react";
import { Calendar } from "react-date-range";
import { CalendarLeftArrow, CalendarRightArrow } from "../icons/svgIndex";
import { Button, CardHeader } from "@egovernments/digit-ui-react-components";

function CustomCalendar({ config, t, handleSelect, onCalendarConfirm, selectedCustomDate, tenantId }) {
  const [currentMonth, setCurrentMonth] = useState(new Date()); // State to track the current month
  const { data: hearingResponse, refetch: refetch } = Digit.Hooks.hearings.useGetHearings(
    { criteria: { tenantId }, tenantId },
    { applicationNumber: "", cnrNumber: "", tenantId },
    "dristi",
    true
  );

  const hearingDetails = useMemo(() => hearingResponse?.HearingList || null, [hearingResponse]);
  useEffect(() => {
    const fetchData = async () => {
      try {
        await refetch(); // Call your refetch function from useGetHearings hook
      } catch (error) {
        console.error("Error refetching data:", error);
      }
    };

    fetchData();
  }, [currentMonth, refetch]);

  const hearingCounts = useMemo(() => {
    const counts = {};
    if (!hearingDetails) return counts;
    const filteredHearings = hearingDetails.filter((hearing) => {
      const hearingDate = new Date(hearing.startTime);
      return hearingDate.getMonth() === currentMonth.getMonth() && hearingDate.getFullYear() === currentMonth.getFullYear();
    });

    filteredHearings.forEach((hearing) => {
      const date = new Date(hearing.startTime).toLocaleDateString("en-CA");
      counts[date] = counts[date] ? counts[date] + 1 : 1;
    });

    return counts;
  }, [currentMonth, hearingDetails]);

  const monthlyCount = useMemo(() => {
    return Object.values(hearingCounts).reduce((sum, value) => sum + value, 0);
  }, [hearingCounts]);

  const renderCustomDay = (date) => {
    const dateStr = date.toLocaleDateString("en-CA");
    const hearingCount = hearingCounts[dateStr] || 0;
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

  const minDate = new Date();
  const maxDate = new Date(2025, 11, 31);

  return (
    <div>
      <div>
        <Calendar
          date={selectedCustomDate}
          onChange={handleSelect}
          // minDate={minDate}
          maxDate={maxDate}
          dayContentRenderer={renderCustomDay}
          navigatorRenderer={navigatorRenderer}
          onShownDateChange={(date) => {
            setCurrentMonth(date);
          }}
        />
      </div>
      {config?.showBottomBar && (
        <div className="calendar-bottom-div">
          <CardHeader>
            {monthlyCount} {t(config?.label)}
          </CardHeader>
          <Button variation="primary" onButtonClick={() => onCalendarConfirm()} label={t(config?.buttonText)}></Button>
        </div>
      )}
    </div>
  );
}

export default CustomCalendar;
