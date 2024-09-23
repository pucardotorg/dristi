import { Button } from "@egovernments/digit-ui-components";
import { Loader } from "@egovernments/digit-ui-react-components";
import React, { useCallback, useEffect, useMemo, useState } from "react";
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
  slotStartTime = 0;
  slotEndTime = 0;
  slotStartString = "";
  slotEndString = "";
  constructor(slotName, slotStartTime, slotEndTime, slotStartString, slotEndString) {
    this.slotName = slotName;
    this.slotStartTime = slotStartTime;
    this.slotEndTime = slotEndTime;
    this.slotStartString = slotStartString;
    this.slotEndString = slotEndString;
  }

  addHearingIfApplicable(hearing) {
    const hearingTime = hearing.startTime;
    if (this.slotStartTime <= hearingTime && hearingTime <= this.slotEndTime) {
      this.hearings.push(hearing);
      return true;
    }
    return false;
  }
}

function formatTimeTo12Hour(timeString) {
  let [hours, minutes] = timeString.split(":").map(Number);

  const suffix = hours >= 12 ? "pm" : "am";

  hours = hours % 12 || 12;

  const formattedHours = String(hours).padStart(2, "0");
  const formattedMinutes = String(minutes).padStart(2, "0");

  return `${formattedHours}:${formattedMinutes} ${suffix}`;
}

function timeInMillisFromDateAndTime(date, hhmmssms) {
  const startOfDate = new Date(date.getFullYear(), date.getMonth(), date.getDate());
  const [h, m, s, ms] = hhmmssms.split(":").map(Number);
  const millis = ((h || 0) * 60 * 60 + (m || 0) * 60 + (s || 0)) * 1000 + (ms || 0);
  return startOfDate.getTime() + millis;
}

const UpcomingHearings = ({ t, userInfoType, ...props }) => {
  const userName = Digit.SessionStorage.get("User");
  const token = window.localStorage.getItem("token");
  const isUserLoggedIn = Boolean(token);
  const tenantId = useMemo(() => window?.Digit.ULBService.getCurrentTenantId(), []);
  const userInfo = Digit.UserService.getUser()?.info;
  const userType = useMemo(() => (userInfo?.type === "CITIZEN" ? "citizen" : "employee"), [userInfo?.type]);
  const roles = userInfo?.roles;
  const isFSO = roles?.some((role) => role?.code === "FSO_ROLE");
  const [hearingCaseList, setHearingCaseList] = useState([]);
  const [isAdvocateLoading, setIsAdvocateLoading] = useState(false);
  const [isCaseLoading, setIsCaseLoading] = useState(false);
  const [isAdvocate, setIsAdvocate] = useState(false);

  // Get the current date
  // eslint-disable-next-line react-hooks/exhaustive-deps
  const today = new Date();

  // Format the date
  const dateOptions = { month: "short", day: "numeric" };
  const dayOptions = { weekday: "short" };
  const date = today.toLocaleDateString("en-US", dateOptions); // e.g., "Jun 15"
  const day = today.toLocaleDateString("en-US", dayOptions); // e.g., "Tue"
  const curHr = today.getHours();
  const dateRange = useMemo(
    () => ({
      start: timeInMillisFromDateAndTime(today, "00:00:00:000"),
      end: new Date(today.getFullYear(), today.getMonth(), today.getDate() + 1).getTime(),
    }),
    [today]
  );
  const dayLeftInOngoingMonthRange = useMemo(() => {
    const year = today.getFullYear();
    const month = today.getMonth();
    const endOfMonth = new Date(year, month + 1, 0);
    return {
      fromDate: today.getTime(),
      toDate: endOfMonth.getTime(),
    };
  }, [today]);

  const reqBody = useMemo(() => {
    today.setHours(23, 59, 59, 999);
    return {
      criteria: {
        tenantId,
        fromDate: dateRange.start,
        attendeeIndividualId: props?.attendeeIndividualId,
        toDate: today.getTime(),
      },
    };
  }, [dateRange.start, props?.attendeeIndividualId, tenantId]);

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

  const { data: individualData } = window?.Digit.Hooks.dristi.useGetIndividualUser(
    {
      Individual: {
        userUuid: [userInfo?.uuid],
      },
    },
    { tenantId, limit: 1000, offset: 0 },
    "Home",
    "",
    userInfo?.uuid && isUserLoggedIn
  );

  const individualId = useMemo(() => {
    return individualData?.Individual?.[0]?.individualId;
  }, [individualData]);

  const individualUserType = Digit.UserService.getType();

  const { data: hearingSlotsResponse } = Digit.Hooks.hearings.useGetHearingSlotMetaData(true);

  const searchCase = useCallback(
    async (hearings) => {
      setIsCaseLoading(true);

      const filingNumbers = [
        ...new Set(
          hearings.map((hearing) => {
            return hearing.filingNumber[0];
          })
        ),
      ];

      const criteria = filingNumbers.map((filingNumber) => {
        return { filingNumber };
      });

      if (!criteria.length) {
        setIsCaseLoading(false);
        return;
      }

      const response = await window?.Digit?.DRISTIService.searchCaseService(
        {
          criteria,
          tenantId,
        },
        {}
      );
      const hearingCaseList = response.criteria.map((res) => {
        return {
          caseName: res.responseList[0]?.caseTitle,
          filingNumber: res.responseList[0]?.filingNumber,
        };
      });

      setHearingCaseList(hearingCaseList);
      setIsCaseLoading(false);
    },
    [tenantId]
  );

  const fetchBasicUserInfo = useCallback(async () => {
    setIsAdvocateLoading(true);
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
    }
    setIsAdvocateLoading(false);
  }, [tenantId, userInfo?.uuid]);

  const { data: hearingResponse, isLoading } = Digit.Hooks.hearings.useGetHearings(
    reqBody,
    { applicationNumber: "", cnrNumber: "", tenantId },
    `${dateRange.start}-${dateRange.end}`,
    Boolean(dateRange.start && dateRange.end && (individualUserType === "citizen" ? individualId : true)),
    false,
    individualUserType === "citizen" && individualId
  );

  const { data: monthlyHearingResponse, isLoading: isLoadingMonthly } = Digit.Hooks.hearings.useGetHearings(
    reqBodyMonthly,
    { applicationNumber: "", cnrNumber: "", tenantId },
    `monthly-${dateRange.start}-${dateRange.end}`,
    Boolean(dateRange.start && dateRange.end && (individualUserType === "citizen" ? individualId : true)),
    false,
    individualUserType === "citizen" && individualId
  );

  const earliestHearing = useMemo(() => {
    if (!hearingResponse) {
      return null;
    }
    let earliestHearing = hearingResponse.HearingList[0];
    for (const hearing of hearingResponse.HearingList) {
      if (hearing.startTime < earliestHearing.startTime) {
        earliestHearing = hearing;
      }
    }
    return earliestHearing;
  }, [hearingResponse]);

  const earliestHearingSlot = useMemo(() => {
    if (!hearingSlotsResponse || !hearingResponse || !earliestHearing) {
      return null;
    }
    const slot = hearingSlotsResponse.slots.find((slot) => {
      const hearingTime = timeInMillisToDateTime(earliestHearing.startTime).time;
      return slot.slotStartTime <= hearingTime && hearingTime <= slot.slotEndTime;
    });
    const hearingSlotObj = new HearingSlot(
      slot.slotName,
      timeInMillisFromDateAndTime(new Date(earliestHearing.startTime), slot.slotStartTime),
      timeInMillisFromDateAndTime(new Date(earliestHearing.startTime), slot.slotEndTime),
      slot.slotStartTime,
      slot.slotEndTime
    );
    hearingResponse.HearingList.forEach((hearing) => {
      hearingSlotObj.addHearingIfApplicable(hearing);
    });
    return hearingSlotObj;
  }, [earliestHearing, hearingResponse, hearingSlotsResponse]);

  useEffect(() => {
    if (earliestHearingSlot) {
      searchCase(earliestHearingSlot.hearings);
    }
    fetchBasicUserInfo();
  }, [fetchBasicUserInfo, hearingResponse, earliestHearingSlot, searchCase]);

  const hearingCountsByType = useMemo(() => {
    const hearingCountsByType = {};

    earliestHearingSlot?.hearings.forEach((hearing) => {
      hearingCountsByType[hearing.hearingType] = (hearingCountsByType[hearing.hearingType] || 0) + 1;
    });

    return Object.keys(hearingCountsByType)
      .map((hearingType) => {
        return `${hearingType} (${hearingCountsByType[hearingType]})`;
      })
      .join(", ");
  }, [earliestHearingSlot]);

  const hearingCount = useMemo(() => {
    return hearingResponse?.TotalCount;
  }, [hearingResponse]);

  const ongoingMonthHearingCount = useMemo(() => {
    return monthlyHearingResponse?.TotalCount;
  }, [monthlyHearingResponse]);
  if (isLoading || isLoadingMonthly || isAdvocateLoading || isCaseLoading) {
    return <Loader />;
  }
  const name = userName?.info?.name
    ?.split(" ")
    .filter((part) => part && !["null", "undefined"].includes(part.toLowerCase()))
    .join(" ");

  const hearingSearchParams = new URLSearchParams();
  hearingSearchParams.set("from-date", earliestHearingSlot?.slotStartTime);
  hearingSearchParams.set("to-date", earliestHearingSlot?.slotEndTime);
  hearingSearchParams.set("slot", earliestHearingSlot?.slotName);
  hearingSearchParams.set("count", earliestHearingSlot?.hearings.length);

  return (
    <div className="upcoming-hearing-container">
      <div className="header">
        {curHr < 12 ? t("GOOD_MORNING") : curHr < 18 ? t("GOOD_AFTERNOON") : t("GOOD_EVENING")}, <span className="userName">{name}</span>
      </div>
      {!isFSO && (
        <div className="hearing-card-wrapper">
          <div className="hearingCard">
            <React.Fragment>
              <div style={{ display: "flex", alignItems: "center", gap: "20px" }}>
                <div className="hearingDate">
                  <div className="dateText">{date.split(" ")[0]}</div>
                  <div className="dateNumber">{date.split(" ")[1]}</div>
                  <div className="dayText">{day}</div>
                </div>
                {!earliestHearingSlot && (
                  <div className="no-hearing">
                    <p>
                      {t("YOU_DONT_HAVE_ANY")} <span>{t("HEARING_SCHEDULED")}</span>
                    </p>
                  </div>
                )}

                {hearingCount > 0 && (
                  <div className="time-hearing-type">
                    <div className="timeText">
                      {formatTimeTo12Hour(earliestHearingSlot.slotStartString)} - {formatTimeTo12Hour(earliestHearingSlot.slotEndString)}
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
                              .join(", ") + (hearingCaseList.length > 2 ? ` +${hearingCaseList.length - 2} more` : "")
                          : hearingCountsByType}
                      </Link>
                    </div>
                  </div>
                )}
              </div>
              <Button className={"view-hearing-button"} label={t("VIEW_HEARINGS")} variation={"primary"} onClick={props.handleNavigate} />
            </React.Fragment>
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
