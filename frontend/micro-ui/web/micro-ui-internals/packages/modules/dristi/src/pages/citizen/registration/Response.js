import { ActionBar, Banner, Button, Card, CardHeader, CardLabel, CardText, SubmitBar } from "@egovernments/digit-ui-react-components";
import React, { useEffect, useState } from "react";
import { useTranslation } from "react-i18next";
import { useLocation } from "react-router-dom";
import { useHistory } from "react-router-dom/cjs/react-router-dom.min";
import { useGetAccessToken } from "../../../hooks/useGetAccessToken";
import { SuccessIcon } from "../../../icons/svgIndex";
import JoinCaseHome from "../../../../../cases/src/pages/employee/JoinCaseHome";

const Response = ({ refetch, setHideBack }) => {
  const { t } = useTranslation();
  const location = useLocation();
  const history = useHistory();

  // const handleLogout = () => {
  //   Digit.UserService.logout();
  // };
  useGetAccessToken("citizen.refresh-token", true);

  useEffect(() => {
    setHideBack(true);
    return () => {
      setHideBack(false);
    };
  }, []);

  return (
    <React.Fragment>
      {location?.state?.response === "success" ? (
        <div className="response-main">
          <SuccessIcon />
          <CardHeader>{location?.state?.response === "success" ? t("CS_REGISTER_SUCCESS") : t("USER_REGISTRATION_BOTTOM_FAILURE_MSG")}</CardHeader>
          <CardText>
            {location?.state?.response === "success" ? t("CS_REGISTER_SUCCESS_SUB_TEXT") : t("USER_REGISTRATION_BOTTOM_FAILURE_MSG")}
          </CardText>
          {location?.state?.response === "success" && (
            <React.Fragment>
              <Button
                onButtonClick={() => {
                  refetch();
                  history.push(`/${window?.contextPath}/citizen/dristi/home/file-case`);
                }}
                label={t("File a case")}
                className={"file-a-case"}
              />
              <Button
                onButtonClick={() => {
                  refetch();
                  history.push(`/${window?.contextPath}/citizen/dristi/home`);
                }}
                label={t("Join a case")}
                className={"join-a-case"}
              />
            </React.Fragment>
          )}

          <ActionBar>
            {
              <SubmitBar
                label={t("GO TO HOME")}
                onSubmit={() => {
                  refetch();
                  history.push(`/${window?.contextPath}/citizen/dristi/home`);
                }}
              />
              // )
            }
          </ActionBar>
        </div>
      ) : (
        <Banner
          successful={location?.state?.response === "success"}
          message={t("USER_REGISTRATION_FAILURE_MSG")}
          info={t("USER_REGISTRATION_BOTTOM_FAILURE_MSG")}
        ></Banner>
      )}
    </React.Fragment>
  );
};

export default Response;
