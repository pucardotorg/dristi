import { Button } from "@egovernments/digit-ui-components";
import React from "react";
import { useTranslation } from "react-i18next";

const HearingSideCard = ({ hearingId, caseId, filingNumber }) => {
  const { t } = useTranslation();
  const handleNavigate = (path, extraSearchParams) => {
    const userInfo = JSON.parse(window.localStorage.getItem("user-info"));
    const userType = userInfo?.type === "CITIZEN" ? "citizen" : "employee";
    const searchParams = new URLSearchParams({ hearingId, caseId, filingNumber, ...extraSearchParams });
    window.open(`${window.location.origin}/${window.contextPath}/${userType}/${path}?${searchParams.toString()}`, "_blank");
  };

  return (
    <div className="hearing-side-card" style={{ width: "auto" }}>
      <div className="hearing-details">
        <div className="hearing-title">{t("CASE_DETAILS")}</div>
        <div className="hearing-buttons">
          <Button
            label={t("ORDERS")}
            variation={"default"}
            style={{
              border: "1px solid",
              borderColor: "#E8E8E8",
              width: "100%",
              justifyContent: "center",
              backgroundColor: "#fff",
            }}
            textStyles={{
              fontFamily: "Roboto",
              fontSize: "16px",
              fontWeight: 700,
              lineHeight: "18.75px",
              textAlign: "center",
              color: "#3D3C3C",
            }}
            className={"take-action-btn-class"}
            icon={"File"}
            iconFill={"#1C1B1F"}
            onClick={() => handleNavigate("dristi/home/view-case", { tab: "Orders" })}
          />
          <Button
            label={t("SUBMISSIONS")}
            variation={"default"}
            style={{
              border: "1px solid",
              borderColor: "#E8E8E8",
              width: "100%",
              justifyContent: "center",
              backgroundColor: "#fff",
            }}
            textStyles={{
              fontFamily: "Roboto",
              fontSize: "16px",
              fontWeight: 700,
              lineHeight: "18.75px",
              textAlign: "center",
              color: "#3D3C3C",
            }}
            className={"take-action-btn-class"}
            icon={"SendAndArchive"}
            iconFill={"#1C1B1F"}
            onClick={() => handleNavigate("dristi/home/view-case", { tab: "Submissions" })}
          />
          {/* <Button
            label={"Case History"}
            variation={"default"}
            style={{
              border: "1px solid",
              borderColor: "#E8E8E8",
              width: "100%",
              justifyContent: "center",
              backgroundColor: "#fff",
            }}
            textStyles={{
              fontFamily: "Roboto",
              fontSize: "16px",
              fontWeight: 700,
              lineHeight: "18.75px",
              textAlign: "center",
              color: "#3D3C3C",
            }}
            className={"take-action-btn-class"}
            icon={"History"}
            iconFill={"#1C1B1F"}
            onClick={() => handleNavigate("dristi/home/view-case", { tab: "History" })}
          /> */}
          <Button
            label={t("PARTIES")}
            variation={"default"}
            style={{
              border: "1px solid",
              borderColor: "#E8E8E8",
              width: "100%",
              justifyContent: "center",
              backgroundColor: "#fff",
            }}
            textStyles={{
              fontFamily: "Roboto",
              fontSize: "16px",
              fontWeight: 700,
              lineHeight: "18.75px",
              textAlign: "center",
              color: "#3D3C3C",
            }}
            className={"take-action-btn-class"}
            icon={"Person"}
            iconFill={"#1C1B1F"}
            onClick={() => handleNavigate("dristi/home/view-case", { tab: "Parties" })}
          />
        </div>
      </div>
    </div>
  );
};
export default HearingSideCard;
