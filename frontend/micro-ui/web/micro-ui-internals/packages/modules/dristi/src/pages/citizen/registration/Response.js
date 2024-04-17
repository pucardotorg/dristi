import { ActionBar, Banner, Card, CardText, SubmitBar, Header } from "@egovernments/digit-ui-react-components";
import React from "react";
import { Link } from "react-router-dom";
import { useTranslation } from "react-i18next";
import { useRouteMatch } from "react-router-dom";

const Response = (props) => {
  const { t } = useTranslation();
  const { path, url } = useRouteMatch();

  return (
    <Card>
      <CardText>
        <Header>{props?.location?.state === "success" ? t("USER_REGISTRATION_SUCCESS_MSG") : t("USER_REGISTRATION_FAILURE_MSG")}</Header>
      </CardText>
      <Banner successful={props?.location?.state === "success"}></Banner>
      <ActionBar>
        <Link to={`${path}/citizen/home`}>
          <SubmitBar label={t("GO TO HOME")} />
        </Link>
      </ActionBar>
    </Card>
  );
};

export default Response;
