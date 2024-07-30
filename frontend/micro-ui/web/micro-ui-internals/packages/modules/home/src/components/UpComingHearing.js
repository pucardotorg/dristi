import React, { useEffect, useMemo, useState } from "react";
import { Button } from "@egovernments/digit-ui-components";
import { Link } from "react-router-dom";
import { Loader } from "@egovernments/digit-ui-react-components";
import { CalenderIcon } from "../../homeIcon";

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

const UpcomingHearings = ({ t, userInfoType, ...props }) => {
  const userName = Digit.SessionStorage.get("User");
  const tenantId = useMemo(() => window?.Digit.ULBService.getCurrentTenantId(), []);
  const userInfo = JSON.parse(window.localStorage.getItem("user-info"));
  const userType = useMemo(() => (userInfo.type === "CITIZEN" ? "citizen" : "employee"), [userInfo.type]);
  const roles = Digit.UserService.getUser()?.info?.roles;
  const isFSO = roles.some((role) => role.code === "FSO_ROLE");
  const [hearingCaseList, setHearingCaseList] = useState([]);

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
        attendeeIndividualId: props?.attendeeIndividualId,
      },
    }),
    [dateRange.end, dateRange.start, props?.attendeeIndividualId, tenantId]
  );

  const { data: hearingSlotsResponse } = Digit.Hooks.hearings.useGetHearingSlotMetaData(true);

  const searchCase = async (HearingList) => {
    const hearingCaseList = await Promise.all(
      HearingList?.map(async (hearing) => {
        const response = await window?.Digit?.DRISTIService.searchCaseService(
          {
            criteria: [
              {
                filingNumber: hearing?.filingNumber?.[0],
              },
            ],
            tenantId,
          },
          {}
        );
        if (response?.criteria[0]?.responseList?.length === 1) {
          return {
            caseName: response?.criteria[0]?.responseList[0].caseTitle,
            filingNumber: response?.criteria[0]?.responseList[0].filingNumber,
          };
        }
      }) || []
    );
    setHearingCaseList(hearingCaseList);
  };

  const { data: hearingResponse, isLoading } = Digit.Hooks.hearings.useGetHearings(
    reqBody,
    { applicationNumber: "", cnrNumber: "", tenantId },
    `${dateRange.start}-${dateRange.end}`,
    dateRange.start && dateRange.end
  );

  useEffect(() => {
    searchCase(hearingResponse?.HearingList);
  }, [hearingResponse]);

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

  const hearingCount = useMemo(() => {
    return hearingResponse?.TotalCount;
  }, [hearingResponse]);

  if (isLoading) {
    return <Loader />;
  }
  return (
    <div className="upcoming-hearing-container">
      <div className="header">
        {curHr < 12 ? "Good Morning" : curHr < 18 ? "Good Afternoon" : "Good Evening"}, <span className="userName">{userName?.info?.name}</span>
      </div>
      {!isFSO && (
        <div className="hearingCard">
          {hearingCount > 0 ? (
            <React.Fragment>
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
                  <div style={{ display: "flex", gap: "8px" }}>
                    {userInfoType === "citizen" ? (
                      <React.Fragment>
                        {hearingCaseList?.map((hearing, index) => (
                          <React.Fragment>
                            {index < 2 && (
                              <React.Fragment>
                                <Link className="hearingType" to={`/${window.contextPath}/${userType}/hearings`}>
                                  {hearing?.caseName}
                                </Link>
                                {index !== hearingCaseList.length - 1 && <span>,</span>}
                              </React.Fragment>
                            )}
                            {index === 2 && (
                              <Link className="hearingType" to={`/${window.contextPath}/${userType}/hearings`}>
                                {`+ ${hearingCaseList?.length - 2} more`}
                              </Link>
                            )}
                          </React.Fragment>
                        ))}
                      </React.Fragment>
                    ) : (
                      <Link className="hearingType" to={`/${window.contextPath}/${userType}/hearings`}>
                        {hearingType} ({hearingCount})
                      </Link>
                    )}
                  </div>
                </div>
              </div>
              <Button className={"view-hearing-button"} label={t("VIEW_HEARINGS")} variation={"primary"} onClick={props.handleNavigate} />
            </React.Fragment>
          ) : (
            <div className="no-hearing">
              <CalenderIcon />
              <p>
                {t("YOU_DONT_HAVE_ANY")} <span>{t("HEARING_SCHEDULED")}</span>
              </p>
            </div>
          )}
        </div>
      )}
    </div>
  );
};

export default UpcomingHearings;
