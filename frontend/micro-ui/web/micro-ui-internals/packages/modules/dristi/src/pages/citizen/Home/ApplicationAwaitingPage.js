import { Button } from "@egovernments/digit-ui-react-components";
import React from "react";
import { useTranslation } from "react-i18next";
import { useHistory } from "react-router-dom";
import { WaitIcon } from "../../../icons/svgIndex";

function ApplicationAwaitingPage({ individualId }) {
  const { t } = useTranslation();
  const history = useHistory();
  return (
    <div style={{ display: "flex", flexDirection: "column", width: "90vw", height: "52vh", background: "white", alignItems: "center" }}>
      <div style={{ maxHeight: "40vh" }}>
        <WaitIcon />
      </div>
      <div style={{ "text-align": "center" }}>
        <h2> {t("APPROVAL_WAITING")}</h2>
      </div>
      <div style={{ "text-align": "center" }}>
        <h3> {t("APPROVAL_WAITING_SUBTEXT")}</h3>
      </div>
      <div>
        <Button
          onButtonClick={() => {
            history.push(`/digit-ui/citizen/dristi/home/application-details?individualId=${individualId}`);
          }}
          label={t("View My Application")}
          style={{
            flex: 1,
            width: "20vw",
            boxShadow: "none",
          }}
        ></Button>
      </div>
    </div>
  );
}

export default ApplicationAwaitingPage;
