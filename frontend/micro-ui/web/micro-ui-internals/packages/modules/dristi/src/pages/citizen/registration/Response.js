import { ActionBar, Banner, Card, CardText, SubmitBar } from "@egovernments/digit-ui-react-components";
import React from "react";
import { useTranslation } from "react-i18next";
import { useLocation } from "react-router-dom";
import { useHistory } from "react-router-dom/cjs/react-router-dom.min";

const Response = ({ refetch }) => {
  const { t } = useTranslation();
  const location = useLocation();
  const history = useHistory();
  return (
    <Card style={{ minWidth: "100%" }}>
      <Banner
        successful={location?.state === "success"}
        message={location?.state === "success" ? t("USER_REGISTRATION_SUCCESS_MSG") : t("USER_REGISTRATION_FAILURE_MSG")}
        style={{ minWidth: "100%" }}
      ></Banner>
      <CardText style={{ margin: 0 }}>
        {location?.state === "success" ? t("USER_REGISTRATION_BOTTOM_SUCCESS_MSG") : t("USER_REGISTRATION_BOTTOM_FAILURE_MSG")}
      </CardText>
      <ActionBar>
        <SubmitBar
          label={t("GO TO HOME")}
          onSubmit={() => {
            refetch();
            history.push(`/${window?.contextPath}/citizen/dristi/home`);
          }}
        />
      </ActionBar>
    </Card>
  );
};

export default Response;
