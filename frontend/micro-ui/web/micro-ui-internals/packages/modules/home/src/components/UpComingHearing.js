import React, { useMemo } from "react";
import { Button } from "@egovernments/digit-ui-components";
import { Link } from "react-router-dom";
import { Loader } from "@egovernments/digit-ui-react-components";

function timeInMillisToDateTime(timeInMillis) {
  if (!timeInMillis || typeof timeInMillis !== "number") {
    throw new Error("Invalid Time");
  }

  const date = new Date(timeInMillis);
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

class HearingSlot {
  hearings = [];
  slotName = "";
  slotStartTime = "";
  slotEndTime = "";
  constructor(slotName, slotStartTime, slotEndTime) {
    this.slotName = slotName;
    this.slotStartTime = slotStartTime;
    this.slotEndTime = slotEndTime;
  }

  addHearingIfApplicable(hearing) {
    const hearingTime = timeInMillisToDateTime(hearing.startTime).time;
    if (this.slotStartTime <= hearingTime && hearingTime <= this.slotEndTime) {
      this.hearings.push(hearing);
    }
  }
}

const UpcomingHearings = (props) => {
  const userName = Digit.SessionStorage.get("User");
  const tenantId = useMemo(() => window?.Digit.ULBService.getCurrentTenantId(), []);
  const userInfo = JSON.parse(window.localStorage.getItem("user-info"));
  const userType = useMemo(() => (userInfo.type === "CITIZEN" ? "citizen" : "employee"), [userInfo.type]);

  // Get the current date
  const today = useMemo(() => new Date(), []);

  // Format the date
  const dateOptions = { month: "short", day: "numeric" };
  const dayOptions = { weekday: "short" };
  const date = today.toLocaleDateString("en-US", dateOptions); // e.g., "Jun 15"
  const day = today.toLocaleDateString("en-US", dayOptions); // e.g., "Tue"
  const curHr = today.getHours();
  const dateRange = useMemo(
    () => ({
      start: today.toISOString().split("T")[0],
      end: today.toISOString().split("T")[0],
    }),
    [today]
  );
  const reqBody = useMemo(
    () => ({
      criteria: {
        tenantId,
        fromDate: dateRange.start,
        toDate: dateRange.end,
      },
    }),
    [dateRange.end, dateRange.start, tenantId]
  );

  const { data: hearingSlotsResponse } = Digit.Hooks.hearings.useGetHearingSlotMetaData(true);

  // const orderedHearings

  const { data: hearingResponse, isLoading } = Digit.Hooks.hearings.useGetHearings(
    reqBody,
    { applicationNumber: "", cnrNumber: "", tenantId },
    `${dateRange.start}-${dateRange.end}`,
    dateRange.start && dateRange.end
  );

  /**
   * @type {HearingSlot[]}
   */
  const hearingSlots = useMemo(() => {
    if (!hearingSlotsResponse || !hearingResponse) {
      return [];
    }
    console.debug({ hearingResponse, hearingSlotsResponse });
    const hearingSlots = hearingSlotsResponse.slots.map((slot) => new HearingSlot(slot.slotName, slot.slotStartTime, slot.slotEndTime)) || [];
    hearingResponse.HearingList.forEach((hearing) => {
      hearingSlots.forEach((slot) => slot.addHearingIfApplicable(hearing));
    });
    return hearingSlots;
  }, [hearingResponse, hearingSlotsResponse]);

  const latestHearing = hearingSlots.filter((slot) => slot.hearings.length).sort((a, b) => a.slotStartTime.localeCompare(b.slotStartTime))[0];

  const hearingCount = useMemo(() => hearingResponse?.TotalCount, [hearingResponse]);
  if (isLoading) {
    return <Loader />;
  }
  if (!latestHearing) {
    return null;
  }
  const hearingSearchParams = new URLSearchParams();
  hearingSearchParams.set("from-date", dateRange.start);
  hearingSearchParams.set("to-date", dateRange.end);
  hearingSearchParams.set("slot", latestHearing.slotName);
  return (
    <div className="upcoming-hearing-container">
      <div className="header">
        {curHr < 12 ? "Good Morning" : curHr < 18 ? "Good Afternoon" : "Good Evening"}, <span className="userName">{userName?.info?.name}</span>
      </div>
      <div className="hearingCard">
        <div style={{ display: "flex", alignItems: "center", gap: "20px" }}>
          <div className="hearingDate">
            <div className="dateText">{date.split(" ")[0]}</div>
            <div className="dateNumber">{date.split(" ")[1]}</div>
            <div className="dayText">{day}</div>
          </div>
          <div className="time-hearing-type">
            <div className="timeText">
              {latestHearing.slotName} - {latestHearing.slotStartTime} to {latestHearing.slotEndTime}
            </div>
            <Link
              className="hearingType"
              to={{
                pathname: `/${window.contextPath}/${userType}/hearings`,
                search: hearingSearchParams.toString(),
              }}
            >
              {latestHearing.hearings[0].hearingType} ({hearingCount})
            </Link>
          </div>
        </div>
        <Button className={"view-hearing-button"} label={"View Hearings"} variation={"primary"} onClick={props.handleNavigate} />
      </div>
    </div>
  );
};

export default UpcomingHearings;
