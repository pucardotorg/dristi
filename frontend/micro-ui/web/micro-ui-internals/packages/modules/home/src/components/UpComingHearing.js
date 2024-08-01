import { Button } from "@egovernments/digit-ui-components";
import { Loader } from "@egovernments/digit-ui-react-components";
import React, { useEffect, useMemo, useState } from "react";
import { Link } from "react-router-dom";
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
  const [isAdvocateLoading, setIsAdvocateLoading] = useState(false);
  const [isCaseLoading, setIsCaseLoading] = useState(false);
  const [isAdvocate, setIsAdvocate] = useState(false);

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
      end: new Date(new Date().setDate(today.getDate() + 1)).toISOString().split("T")[0],
    }),
    [today]
  );
  const dayLeftInOngoingMonthRange = useMemo(() => {
    const year = today.getFullYear();
    const month = today.getMonth();
    const endOfMonth = new Date(year, month + 1, 0);
    const formatDate = (date) => date.toISOString().split("T")[0];
    return {
      fromDate: formatDate(today),
      toDate: formatDate(endOfMonth),
    };
  }, [today]);

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

  const reqBodyMonthly = useMemo(
    () => ({
      criteria: {
        tenantId,
        ...dayLeftInOngoingMonthRange,
        attendeeIndividualId: props?.attendeeIndividualId,
      },
    }),
    [dayLeftInOngoingMonthRange, props?.attendeeIndividualId, tenantId]
  );

  const { data: hearingSlotsResponse } = Digit.Hooks.hearings.useGetHearingSlotMetaData(true);

  const searchCase = async (HearingList) => {
    setIsCaseLoading(false);
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
    setIsCaseLoading(true);
  };

  const fetchBasicUserInfo = async () => {
    setIsAdvocateLoading(false);
    const individualData = await window?.Digit.DRISTIService.searchIndividualUser(
      {
        Individual: {
          userUuid: [userInfo?.uuid],
        },
      },
      { tenantId, limit: 1000, offset: 0 },
      "",
      userInfo?.uuid
    );

    const advocateResponse = await window?.Digit.DRISTIService.searchIndividualAdvocate(
      {
        criteria: [
          {
            individualId: individualData?.Individual?.[0]?.individualId,
          },
        ],
        tenantId,
      },
      {}
    );

    if (advocateResponse?.advocates[0]?.responseList?.length === 1) {
      setIsAdvocate(true);
      setIsAdvocateLoading(true);
    }
  };

  const { data: hearingResponse, isLoading } = Digit.Hooks.hearings.useGetHearings(
    reqBody,
    { applicationNumber: "", cnrNumber: "", tenantId },
    `${dateRange.start}-${dateRange.end}`,
    dateRange.start && dateRange.end
  );

  const { data: monthlyHearingResponse, isLoadingMonthly } = Digit.Hooks.hearings.useGetHearings(
    reqBodyMonthly,
    { applicationNumber: "", cnrNumber: "", tenantId },
    `${dateRange.start}-${dateRange.end}`,
    dateRange.start && dateRange.end
  );

  useEffect(() => {
    searchCase(hearingResponse?.HearingList);
    fetchBasicUserInfo();
  }, [hearingResponse]);

  /**
   * @type {HearingSlot[]}
   */
  const hearingSlots = useMemo(() => {
    if (!hearingSlotsResponse || !hearingResponse) {
      return [];
    }
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

  const ongoingMonthHearingCount = useMemo(() => {
    return monthlyHearingResponse?.TotalCount;
  }, [monthlyHearingResponse]);

  if (isLoading && isLoadingMonthly && isAdvocateLoading && isCaseLoading) {
    return <Loader />;
  }

  if (!latestHearing) {
    return (
      <div className="upcoming-hearing-container">
        <div className="header">
          {curHr < 12 ? "Good Morning" : curHr < 18 ? "Good Afternoon" : "Good Evening"}, <span className="userName">{userName?.info?.name}</span>
        </div>
        <div className="hearingCard">
          <div className="no-hearing">
            <CalenderIcon />
            <p>
              {t("YOU_DONT_HAVE_ANY")} <span>{t("HEARING_SCHEDULED")}</span>
            </p>
          </div>
        </div>
      </div>
    );
  }

  const hearingSearchParams = new URLSearchParams();
  hearingSearchParams.set("from-date", dateRange.start);
  hearingSearchParams.set("to-date", dateRange.end);
  hearingSearchParams.set("slot", latestHearing?.slotName);

  return (
    <div className="upcoming-hearing-container">
      <div className="header">
        {curHr < 12 ? "Good Morning" : curHr < 18 ? "Good Afternoon" : "Good Evening"}, <span className="userName">{userName?.info?.name}</span>
      </div>
      {!isFSO && (
        <div className="hearing-card-wrapper">
          <div className="hearingCard">
            {hearingCount > 0 && (
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
                      <Link
                        className="hearingType"
                        to={{ pathname: `/${window.contextPath}/${userType}/hearings`, search: hearingSearchParams.toString() }}
                      >
                        {userInfoType === "citizen"
                          ? hearingCaseList
                              .slice(0, 2)
                              .map((hearing) => hearing.caseName)
                              .join(", ") + (hearingCaseList.length >= 2 ? ` +${hearingCaseList.length - 2} more` : "")
                          : `${latestHearing.hearings[0].hearingType} ${hearingCount}`}
                      </Link>
                    </div>
                  </div>
                </div>
                <Button className={"view-hearing-button"} label={t("VIEW_HEARINGS")} variation={"primary"} onClick={props.handleNavigate} />
              </React.Fragment>
            )}
          </div>
          {ongoingMonthHearingCount > 0 && userInfoType === "citizen" && isAdvocate && (
            <div className="ongoing-month-hearing">
              <p>
                {t("YOU_HAVE_TEXT")} <span>{`${ongoingMonthHearingCount} ${t("UPCOMING_HEARINGS_TEXT")}`}</span> {t("THIS_MONTH_TEXT")}
              </p>
            </div>
          )}
        </div>
      )}
    </div>
  );
};

export default UpcomingHearings;
