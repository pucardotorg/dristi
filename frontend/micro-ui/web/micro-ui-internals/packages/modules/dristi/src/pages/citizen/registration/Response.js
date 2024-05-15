import { ActionBar, Banner, Button, Card, CardHeader, CardLabel, CardText, SubmitBar } from "@egovernments/digit-ui-react-components";
import React, { useState } from "react";
import { useTranslation } from "react-i18next";
import { useLocation } from "react-router-dom";
import { useHistory } from "react-router-dom/cjs/react-router-dom.min";
import { useGetAccessToken } from "../../../hooks/useGetAccessToken";
// import useInterval from "../../../hooks/useInterval";
import { SuccessIcon } from "../../../icons/svgIndex";

const Response = ({ refetch }) => {
  const { t } = useTranslation();
  const location = useLocation();
  const history = useHistory();

  // const handleLogout = () => {
  //   Digit.UserService.logout();
  // };

  // const [timeLeft, setTimeLeft] = useState(5);
  // useInterval(
  //   () => {
  //     setTimeLeft(timeLeft - 1);
  //     if (timeLeft === 1 && location?.state?.createType !== "LITIGANT") {
  //       handleLogout();
  //     }
  //   },
  //   timeLeft > 0 ? 1000 : null
  // );
  useGetAccessToken("citizen.refresh-token");

  return (
    <Card style={{ minWidth: "97%", marginLeft: "24px", alignItems: "center" }}>
      {location?.state?.response === "success" ? (
        <SuccessIcon />
      ) : (
        <Banner
          successful={location?.state?.response === "success"}
          message={t("USER_REGISTRATION_FAILURE_MSG")}
          style={{ minWidth: "100%" }}
        ></Banner>
      )}

      <CardHeader style={{ margin: 0, fontSize: "20px" }}>
        {/* {"You’ve been registered successfully!"} */}
        {location?.state?.response === "success" ? t("CS_REGISTER_SUCCESS") : t("USER_REGISTRATION_BOTTOM_FAILURE_MSG")}
      </CardHeader>
      <CardText style={{ marginBottom: "50px" }}>
        {/* {"You can now proceed to file a case or join an on-going case"} */}
        {location?.state?.response === "success" ? t("CS_REGISTER_SUCCESS_SUB_TEXT") : t("USER_REGISTRATION_BOTTOM_FAILURE_MSG")}
      </CardText>
      <Button
        onButtonClick={() => {
          history.push(`/digit-ui/citizen/dristi/home/login`);
        }}
        label={t("File a case")}
        style={{
          flex: 1,
          maxHeight: "7vh",
          width: "20vw",
          margin: "4px",
        }}
      ></Button>
      <Button
        onButtonClick={() => {
          history.push(`/digit-ui/citizen/dristi/home/login`);
        }}
        label={t("Join a case")}
        style={{
          flex: 1,
          maxHeight: "7vh",
          width: "20vw",
          margin: "4px",
          background: "none",
          color: "#007E7E",
        }}
      ></Button>
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
    </Card>
  );
};

export default Response;
