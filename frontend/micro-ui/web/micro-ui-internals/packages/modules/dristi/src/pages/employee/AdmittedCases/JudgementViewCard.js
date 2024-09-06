import { Button, Card } from "@egovernments/digit-ui-react-components";
import React, { useMemo } from "react";
import { useTranslation } from "react-i18next";
import { JudgementIcon } from "../../../icons/svgIndex";
const JudgementViewCard = ({ caseData, width }) => {
  const { t } = useTranslation();
  const handleButtonClick = () => {};
  const tenantId = Digit.ULBService.getCurrentTenantId();
  const { data: hearingsData } = Digit.Hooks.hearings.useGetHearings(
    { criteria: { tenantId }, tenantId },
    { applicationNumber: "", cnrNumber: "", tenantId },
    "dristi",
    true
  );
  const hearingsList = useMemo(() => hearingsData?.HearingList?.sort((a, b) => b.startTime - a.startTime), [hearingsData]);
  const formatDate = (date, format) => {
    const day = String(date.getDate()).padStart(2, "0");
    const month = String(date.getMonth() + 1).padStart(2, "0");
    const year = date.getFullYear();
    if (format === "DD-MM-YYYY") {
      return `${day}-${month}-${year}`;
    }
    return `${year}-${month}-${day}`;
  };
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
          display: "flex",
          alignItems: "center",
          gap: 15,
        }}
      >
        <JudgementIcon />
        {t("FINAL_JUDGEMENT")}
      </div>
      <hr style={{ border: "1px solid #FFF6E880" }} />
      <div style={{ display: "flex", justifyContent: "space-between", padding: "10px" }}>
        <div style={{ display: "flex", gap: "10px", justifyContent: "start", flexDirection: "column" }}>
          <div
            style={{
              fontWeight: 700,
              fontSize: "24px",
              lineHeight: "28.13px",
              color: "#231F20",
              marginTop: "5px",
            }}
          >
            {t(caseData?.case?.outcome)}
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
            {formatDate(new Date(hearingsList?.[hearingsList?.length - 1]?.startTime), "DD-MM-YYYY")}
          </div>
        </div>
        <Button variation={"outlined"} onButtonClick={handleButtonClick} label={t("VIEW_JUDGEMENT_ORDER")} />
      </div>
    </Card>
  );
};

export default JudgementViewCard;
