import { Button } from "@egovernments/digit-ui-react-components";
import React from "react";
import { useTranslation } from "react-i18next";
import { useHistory } from "react-router-dom";
import { ReactComponent as RegisterImage } from "./ImageUpload/image/register.svg";
import { ReactComponent as RightArrow } from "./ImageUpload/image/arrow_forward.svg";

function TakeUserToRegistration({ message, isRejected, data, userType }) {
  const { t } = useTranslation();
  const history = useHistory();
  return (
    <div className="take-user-to-registration">
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
            sessionStorage.removeItem("Digit.isAadharNumberVerified");
            !isRejected
              ? history.push(`/digit-ui/citizen/dristi/home/registration/user-name`)
              : history.push(`/digit-ui/citizen/dristi/home/registration/user-type`, { newParams: data, userType: userType, isRejected: isRejected });
          }}
          label={t("Register")}
        >
          <div className="svg-div">
            <RightArrow />
          </div>
        </Button>
      </div>
    </div>
  );
}

export default TakeUserToRegistration;
