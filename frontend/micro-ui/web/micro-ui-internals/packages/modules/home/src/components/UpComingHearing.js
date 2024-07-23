import React, { useMemo } from "react";
import { Button } from "@egovernments/digit-ui-components";
import { Link } from "react-router-dom";
import { Loader } from "@egovernments/digit-ui-react-components";

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
  const time = "9:30am-12:00pm"; // Static time for demonstration purposes
  const hearingType = "Admission Hearings";
  const pendingTasks = 4;
  const upcomingHearings = 2;
  // [TODO: Time, Hearing Type, Pending Tasks, upcoming hearings need to be integrated with actual data]
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
  const { data: hearingResponse, isLoading } = Digit.Hooks.hearings.useGetHearings(
    reqBody,
    { applicationNumber: "", cnrNumber: "", tenantId },
    `${dateRange.start}-${dateRange.end}`,
    dateRange.start && dateRange.end
  );
  const hearingCount = useMemo(() => hearingResponse?.TotalCount, [hearingResponse]);
  if (isLoading) {
    return <Loader />;
  }
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
            <div className="timeText">{time}</div>
            <Link className="hearingType" to={`/${window.contextPath}/${userType}/hearings`}>
              {hearingType} ({hearingCount})
            </Link>
          </div>
        </div>
        <Button className={"view-hearing-button"} label={"View Hearing"} variation={"primary"} onClick={props.handleNavigate} />
      </div>
    </div>
  );
};

export default UpcomingHearings;
