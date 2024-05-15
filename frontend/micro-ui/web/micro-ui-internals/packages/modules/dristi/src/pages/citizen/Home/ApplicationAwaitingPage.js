import { Button } from "@egovernments/digit-ui-react-components";
import React from "react";
import { useTranslation } from "react-i18next";
import { useHistory } from "react-router-dom";
import { ReactComponent as WaitImage } from "./ImageUpload/image/AppAwait.svg";
import { ReactComponent as RightArrow } from "./ImageUpload/image/arrow_forward.svg";

function ApplicationAwaitingPage({ individualId }) {
  const { t } = useTranslation();
  const history = useHistory();
  return (
    <div style={{ display: "flex", flexDirection: "column", width: "90vw", height: "52vh", background: "white", alignItems: "center" }}>
      <div style={{ maxHeight: "40vh" }}>
        <WaitImage></WaitImage>
      </div>
      <div style={{ "text-align": "center" }}>
        <h2> your application is awaiting approval.....!</h2>
      </div>
      <div>
        <Button
          onButtonClick={() => {
            history.push(`/digit-ui/citizen/dristi/home/application-details?individualId=${individualId}`);
          }}
          label={t("View My Application")}
          style={{
            flex: 1,
            maxHeight: "7vh",
            width: "20vw",
            background: "none",
            color: "#007E7E",
            boxShadow: "none",
          }}
        >
          <RightArrow />
        </Button>
      </div>
    </div>
  );
}

export default ApplicationAwaitingPage;
