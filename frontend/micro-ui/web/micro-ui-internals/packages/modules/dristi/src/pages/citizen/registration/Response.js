import { ActionBar, Banner, Card, CardText, SubmitBar } from "@egovernments/digit-ui-react-components";
import React, { useState } from "react";
import { useTranslation } from "react-i18next";
import { useLocation } from "react-router-dom";
import { useHistory } from "react-router-dom/cjs/react-router-dom.min";
import useInterval from "../../../hooks/useInterval";

const Response = ({ refetch }) => {
  const Digit = window.Digit || {};
  const { t } = useTranslation();
  const location = useLocation();
  const history = useHistory();

  const handleLogout = () => {
    Digit.UserService.logout();
  };

  const [timeLeft, setTimeLeft] = useState(5);
  useInterval(
    () => {
      setTimeLeft(timeLeft - 1);
      if (timeLeft === 1 && location?.state?.createType !== "LITIGANT") {
        handleLogout();
      }
    },
    timeLeft > 0 ? 1000 : null
  );

  return (
    <Card style={{ minWidth: "97%", marginLeft: "24px" }}>
      <Banner
        successful={location?.state?.response === "success"}
        message={location?.state?.response === "success" ? t("USER_REGISTRATION_SUCCESS_MSG") : t("USER_REGISTRATION_FAILURE_MSG")}
        style={{ minWidth: "100%" }}
      ></Banner>
      <CardText style={{ margin: 0 }}>
        {location?.state?.response === "success" ? t("USER_REGISTRATION_BOTTOM_SUCCESS_MSG") : t("USER_REGISTRATION_BOTTOM_FAILURE_MSG")}
      </CardText>
      <ActionBar>
        {location?.state?.createType !== "LITIGANT" ? (
          <span
            style={{ display: "flex", alignItems: "center", fontSize: "16px", color: "#505A5F", height: "40px", justifyContent: "flex-end" }}
          >{`${t("FORCE_LOGOUT_MSG")} ${timeLeft} ${t("CS_RESEND_SECONDS")}`}</span>
        ) : (
          <SubmitBar
            label={t("GO TO HOME")}
            onSubmit={() => {
              refetch();
              history.push(`/${window?.contextPath}/citizen/dristi/home`);
            }}
          />
        )}
      </ActionBar>
    </Card>
  );
};

export default Response;
