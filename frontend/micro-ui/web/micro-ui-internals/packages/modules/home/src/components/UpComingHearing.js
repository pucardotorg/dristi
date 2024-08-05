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
      start: today.getTime(),
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

  const reqBody = useMemo(
    () => ({
      criteria: {
        tenantId,
        fromDate: dateRange.start,
        attendeeIndividualId: props?.attendeeIndividualId,
      },
    }),
    [dateRange.start, props?.attendeeIndividualId, tenantId]
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
      const hearingCaseList = response.criteria.map((cases) => {
        return {
          caseName: cases[0].caseTitle,
          filingNumber: cases[0].filingNumber,
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
      setIsAdvocateLoading(false);
    }
  }, [tenantId, userInfo?.uuid]);

  const { data: hearingResponse, isLoading } = Digit.Hooks.hearings.useGetHearings(
    reqBody,
    { applicationNumber: "", cnrNumber: "", tenantId },
    `${dateRange.start}-${dateRange.end}`,
    Boolean(dateRange.start && dateRange.end && (individualUserType === "citizen" ? individualId : true)),
    false,
    individualUserType === "citizen" && individualId
  );

  const { data: monthlyHearingResponse, isLoadingMonthly } = Digit.Hooks.hearings.useGetHearings(
    reqBodyMonthly,
    { applicationNumber: "", cnrNumber: "", tenantId },
    `monthly-${dateRange.start}-${dateRange.end}`,
    Boolean(dateRange.start && dateRange.end && (individualUserType === "citizen" ? individualId : true)),
    false,
    individualUserType === "citizen" && individualId
  );

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

  useEffect(() => {
    if (latestHearing) {
      searchCase(latestHearing.hearings);
    }
    fetchBasicUserInfo();
  }, [fetchBasicUserInfo, hearingResponse, latestHearing, searchCase]);

  const hearingCountsByType = useMemo(() => {
    const hearingCountsByType = {};

    latestHearing?.hearings.forEach((hearing) => {
      hearingCountsByType[hearing.hearingType] = (hearingCountsByType[hearing.hearingType] || 0) + 1;
    });

    return Object.keys(hearingCountsByType)
      .map((hearingType) => {
        return `${hearingType} (${hearingCountsByType[hearingType]})`;
      })
      .join(", ");
  }, [latestHearing]);

  const hearingCount = useMemo(() => {
    return hearingResponse?.TotalCount;
  }, [hearingResponse]);

  const ongoingMonthHearingCount = useMemo(() => {
    return monthlyHearingResponse?.TotalCount;
  }, [monthlyHearingResponse]);

  if (isLoading || isLoadingMonthly || isAdvocateLoading || isCaseLoading) {
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
  hearingSearchParams.set("from-date", timeInMillisFromDateAndTime(today, latestHearing?.slotStartTime));
  hearingSearchParams.set("to-date", timeInMillisFromDateAndTime(today, latestHearing?.slotEndTime));
  hearingSearchParams.set("slot", latestHearing?.slotName);
  hearingSearchParams.set("count", latestHearing?.hearings.length);

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
                      {formatTimeTo12Hour(latestHearing.slotStartTime)} - {formatTimeTo12Hour(latestHearing.slotEndTime)}
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
