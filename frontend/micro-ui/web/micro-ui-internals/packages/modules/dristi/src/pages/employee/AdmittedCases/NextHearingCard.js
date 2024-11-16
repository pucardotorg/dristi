import { Button, Card, Loader } from "@egovernments/digit-ui-react-components";
import React from "react";
import { useTranslation } from "react-i18next";
import { useHistory } from "react-router-dom/cjs/react-router-dom.min";
import { DRISTIService } from "../../../services";
import { HearingWorkflowState } from "@egovernments/digit-ui-module-orders/src/utils/hearingWorkflow";

function timeInMillisFromDateAndTime(date, hhmmssms) {
  const startOfDate = new Date(date.getFullYear(), date.getMonth(), date.getDate());
  const [h, m, s, ms] = hhmmssms.split(":").map(Number);
  const millis = ((h || 0) * 60 * 60 + (m || 0) * 60 + (s || 0)) * 1000 + (ms || 0);
  return startOfDate.getTime() + millis;
}

const NextHearingCard = ({ caseData, width }) => {
  const filingNumber = caseData.filingNumber;
  const cnr = caseData.cnrNumber;
  const tenantId = window?.Digit.ULBService.getCurrentTenantId();
  const history = useHistory();
  const { t } = useTranslation();
  const userRoles = Digit.UserService.getUser()?.info?.roles.map((role) => role.code);
  const isCourtRoomManager = userRoles.includes("COURT_ROOM_MANAGER");

  const { data: hearingRes, isLoading: isHearingsLoading } = Digit.Hooks.hearings.useGetHearings(
    {
      criteria: {
        fromDate: timeInMillisFromDateAndTime(new Date(), "00:00:00:00"),
        filingNumber: filingNumber,
        tenantId: tenantId,
      },
    },
    {},
    cnr + filingNumber,
    true
  );

  const scheduledHearing = hearingRes?.HearingList?.filter(
    (hearing) => ![HearingWorkflowState.COMPLETED, HearingWorkflowState?.OPTOUT, HearingWorkflowState?.ABATED].includes(hearing?.status)
  ).sort((hearing1, hearing2) => hearing1.startTime - hearing2.startTime)[0];

  const formattedTime = () => {
    const date1 = new Date(scheduledHearing?.startTime);
    const date2 = new Date(scheduledHearing?.endTime);
    const formattedDate = `
    ${date1.toLocaleTimeString("en-in", {
      hour: "2-digit",
      minute: "2-digit",
    })}
     - 
     ${date2.toLocaleTimeString("en-in", {
       hour: "2-digit",
       minute: "2-digit",
     })}`;
    return formattedDate;
  };

  const formattedDate = `${new Date(scheduledHearing?.startTime).toLocaleDateString("en-in", {
    month: "long",
    day: "numeric",
  })}`;

  const day = new Date(scheduledHearing?.startTime).toLocaleDateString("en-in", { weekday: "short" });

  const handleButtonClick = () => {
    const userInfo = JSON.parse(window.localStorage.getItem("user-info"));
    const userType = userInfo?.type === "CITIZEN" ? "citizen" : "employee";
    const searchParams = new URLSearchParams();
    searchParams.set("hearingId", scheduledHearing?.hearingId);
    if (userType === "citizen") {
      history.push(`/${window.contextPath}/${userType}/hearings/inside-hearing?${searchParams.toString()}`);
    } else if (scheduledHearing?.status === "SCHEDULED") {
      DRISTIService.startHearing({ hearing: scheduledHearing }).then(() => {
        window.location.href = `/${window.contextPath}/${userType}/hearings/inside-hearing?${searchParams.toString()}`;
      });
    } else {
      window.location.href = `/${window.contextPath}/${userType}/hearings/inside-hearing?${searchParams.toString()}`;
    }
  };

  if (isHearingsLoading) {
    return <Loader />;
  }

  if (!scheduledHearing) {
    return null;
  }

  return (
    <Card
      style={{
        width: width,
        marginTop: "10px",
      }}
    >
      <div
        style={{
          fontWeight: 700,
          fontSize: "16px",
          lineHeight: "18.75px",
          color: "#231F20",
        }}
      >
        {t("NEXT_HEARING")}
      </div>
      <hr style={{ border: "1px solid #FFF6E880" }} />
      <div style={{ display: "flex", justifyContent: "space-between", padding: "10px" }}>
        <div style={{ display: "flex", gap: "10px", alignItems: "center" }}>
          <div className="hearingCard">
            <div className="hearingDate">
              <div className="dateText">{formattedDate.split(" ")[1]}</div>
              <div className="dateNumber">{formattedDate.split(" ")[0]}</div>
              <div className="dayText">{day}</div>
            </div>
          </div>
          <div>
            <div
              style={{
                fontWeight: 700,
                fontSize: "24px",
                lineHeight: "28.13px",
                color: "#231F20",
                marginTop: "5px",
              }}
            >
              {formattedTime()}
            </div>
            <div
              style={{
                fontWeight: 400,
                fontSize: "14px",
                lineHeight: "16.41px",
                color: "#3D3C3C",
                marginTop: "5px",
              }}
            >
              {`${t(scheduledHearing?.hearingType)} Hearing`}
            </div>
          </div>
        </div>
        <Button
          variation={"outlined"}
          onButtonClick={handleButtonClick}
          isDisabled={isCourtRoomManager || (userRoles.includes("CITIZEN") && scheduledHearing?.status === "SCHEDULED")}
          label={
            userRoles.includes("CITIZEN")
              ? scheduledHearing?.status === "SCHEDULED"
                ? t("AWAIT_START_HEARING")
                : t("JOIN_HEARING")
              : scheduledHearing?.status === "SCHEDULED"
              ? t("START_NOW")
              : t("JOIN_HEARING")
          }
        />
      </div>
    </Card>
  );
};

export default NextHearingCard;
