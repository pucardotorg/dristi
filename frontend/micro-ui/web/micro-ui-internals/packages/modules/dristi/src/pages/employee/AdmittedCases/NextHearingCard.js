import { Button, Card } from "@egovernments/digit-ui-react-components";
import React from "react";
import { useTranslation } from "react-i18next";
import { useHistory } from "react-router-dom/cjs/react-router-dom.min";
import { DRISTIService } from "../../../services";

const NextHearingCard = ({ caseData, width }) => {
  const filingNumber = caseData.filingNumber;
  const cnr = caseData.cnrNumber;
  const tenantId = window?.Digit.ULBService.getCurrentTenantId();
  const history = useHistory();
  const { t } = useTranslation();
  const userRoles = Digit.UserService.getUser()?.info?.roles.map((role) => role.code);
  const { data: hearingRes, refetch: refetchHearingsData, isLoading: isHearingsLoading } = Digit.Hooks.hearings.useGetHearings(
    {
      criteria: {
        filingNumber: filingNumber,
        tenantId: tenantId,
      },
    },
    {},
    cnr + filingNumber,
    true
  );
  const scheduledHearing = hearingRes?.HearingList?.filter((hearing) => hearing.startTime > Date.now()).sort(
    (hearing1, hearing2) => hearing1.startTime - hearing2.startTime
  )[0];

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
    const userType = userInfo.type === "CITIZEN" ? "citizen" : "employee";
    const searchParams = new URLSearchParams();
    searchParams.set("hearingId", scheduledHearing.hearingId);
    if (userType === "citizen") {
      history.push(`/${window.contextPath}/${userType}/hearings/inside-hearing?${searchParams.toString()}`);
    } else {
      DRISTIService.startHearing({ hearing: scheduledHearing }).then(() => {
        window.location.href = `/${window.contextPath}/${userType}/hearings/inside-hearing?${searchParams.toString()}`;
      });
    }
  };

  return (
    !isHearingsLoading &&
    hearingRes?.HearingList?.filter((hearing) => hearing.startTime > Date.now()).length !== 0 && (
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
          Next Hearing
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
                {`${scheduledHearing?.hearingType.charAt(0).toUpperCase()}${scheduledHearing?.hearingType.slice(1).toLowerCase()} Hearing`}
              </div>
            </div>
          </div>
          <Button
            variation={"outlined"}
            onButtonClick={handleButtonClick}
            label={userRoles.includes("CITIZEN") ? t("JOIN_HEARING") : t("START_NOW")}
          />
        </div>
      </Card>
    )
  );
};

export default NextHearingCard;
