import { Button, Card, CardHeader, CardText } from "@egovernments/digit-ui-react-components";
import React from "react";
import { useTranslation } from "react-i18next";
import { useHistory } from "react-router-dom";
import { WaitIcon } from "../../../icons/svgIndex";

function ApplicationAwaitingPage({ individualId }) {
  const { t } = useTranslation();
  const history = useHistory();
  return (
    <Card className="response-main">
      <div style={{ maxHeight: "40vh" }}>
        <WaitIcon />
      </div>
      <div style={{ "text-align": "center" }}>
        <CardHeader> {t("APPROVAL_WAITING")}</CardHeader>
      </div>
      <div style={{ "text-align": "center", maxWidth: "50%" }}>
        <CardText>
          {`Your registration (ID: ${individualId}) is in progress. It takes 2-3 days for verification. You'll get an SMS when it's done.`}
        </CardText>
      </div>
      <div>
        <Button
          onButtonClick={() => {
            history.push(`/digit-ui/citizen/dristi/home/application-details?individualId=${individualId}`);
          }}
          label={t("View My Application")}
          style={{
            flex: 1,
            boxShadow: "none",
          }}
        ></Button>
      </div>
    </Card>
  );
}

export default ApplicationAwaitingPage;
