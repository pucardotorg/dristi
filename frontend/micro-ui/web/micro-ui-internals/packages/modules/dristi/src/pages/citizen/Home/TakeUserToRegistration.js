import { Button } from "@egovernments/digit-ui-react-components";
import React from "react";
import { useTranslation } from "react-i18next";
import { useHistory } from "react-router-dom";
import { ReactComponent as RegisterImage } from "./ImageUpload/image/register.svg";
import { ReactComponent as RightArrow } from "./ImageUpload/image/arrow_forward.svg";

function TakeUserToRegistration({ message }) {
  const { t } = useTranslation();
  const history = useHistory();
  return (
    <div
      style={{
        display: "flex",
        flexDirection: "column",
        width: "90vw",
        height: "52vh",
        background: "white",
        alignItems: "center",
        marginLeft: "10px",
      }}
    >
      <div style={{ maxHeight: "40vh" }}>
        <RegisterImage></RegisterImage>
      </div>
      <div style={{ textAlign: "center" }}>
        <h2> {t(message)} </h2>
      </div>
      <div>
        <Button
          onButtonClick={() => {
            sessionStorage.removeItem("Digit.UploadedDocument");
            sessionStorage.removeItem("Digit.aadharNumber");
            history.push(`/digit-ui/citizen/dristi/home/login/id-verification`);
          }}
          label={t("Register")}
          style={{
            flex: 1,
            maxHeight: "7vh",
            width: "20vw",
            background: "none",
            color: "#F47738",
            boxShadow: "none",
          }}
        >
          <RightArrow />
        </Button>
      </div>
    </div>
  );
}

export default TakeUserToRegistration;
