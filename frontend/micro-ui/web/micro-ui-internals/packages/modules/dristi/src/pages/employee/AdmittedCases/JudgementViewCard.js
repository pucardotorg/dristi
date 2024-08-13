import { Button, Card } from "@egovernments/digit-ui-react-components";
import React from "react";
import { useTranslation } from "react-i18next";
import { JudgementIcon } from "../../../icons/svgIndex";
const JudgementViewCard = ({ caseData, width }) => {
  const { t } = useTranslation();
  const handleButtonClick = () => {};

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
        <div style={{ display: "flex", gap: "10px", alignItems: "center" }}>
          <div className="hearingCard">
            <div className="hearingDate">{t(caseData?.case?.outcome)}</div>
          </div>
        </div>
        <Button variation={"outlined"} onButtonClick={handleButtonClick} label={t("VIEW_JUDGEMENT_ORDER")} />
      </div>
    </Card>
  );
};

export default JudgementViewCard;
