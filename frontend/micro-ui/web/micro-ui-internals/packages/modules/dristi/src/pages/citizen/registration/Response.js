import { ActionBar, Banner, Card, CardText, SubmitBar, Header } from "@egovernments/digit-ui-react-components";
import React from "react";
import { Link } from "react-router-dom";
import { useTranslation } from "react-i18next";
import { useRouteMatch, useLocation } from "react-router-dom";

const Response = () => {
  const { t } = useTranslation();
  const location = useLocation();

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
        <Link to={`/${window?.contextPath}/citizen/dristi/home`}>
          <SubmitBar label={t("GO TO HOME")} />
        </Link>
      </ActionBar>
    </Card>
  );
};

export default Response;
