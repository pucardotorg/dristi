import { CardLabelError, CardText } from "@egovernments/digit-ui-react-components";
import React, { Fragment, useState } from "react";
import useInterval from "../../../hooks/useInterval";
import OTPInput from "../../../components/OTPInput";
import FormStep from "../../../components/FormStep";
import { useHistory } from "react-router-dom/cjs/react-router-dom.min";

const SelectOtp = ({ config, otp, onOtpChange, onResend, onSelect, t, error, userType = "citizen", canSubmit }) => {
  const history = useHistory();
  const token = window.localStorage.getItem("token");
  const isUserLoggedIn = Boolean(token);
  const [timeLeft, setTimeLeft] = useState(30);
  useInterval(
    () => {
      setTimeLeft(timeLeft - 1);
    },
    timeLeft > 0 ? 1000 : null
  );

  const handleResendOtp = () => {
    onResend();
    setTimeLeft(30);
  };

  if (isUserLoggedIn && !sessionStorage.getItem("Digit.aadharNumber")) {
    history.push(`/${window.contextPath}/citizen/dristi/home`);
  }

  if (
    sessionStorage.getItem("Digit.UploadedDocument") ||
    (sessionStorage.getItem("Digit.aadharNumber") && sessionStorage.getItem("Digit.isAadharNumberVerified") && isUserLoggedIn)
  ) {
    sessionStorage.removeItem("Digit.UploadedDocument");
    sessionStorage.removeItem("Digit.aadharNumber");
    sessionStorage.removeItem("Digit.isAadharNumberVerified");
    history.push(`/${window.contextPath}/citizen/dristi/home`);
  }

  if (userType === "employee") {
    return (
      <Fragment>
        <div style={{ width: "200px" }}>
          <OTPInput length={6} onChange={onOtpChange} value={otp} />
        </div>
        {timeLeft > 0 ? (
          <CardText>
            <span style={{ backgroundColor: "#fff" }}>{`${t("CS_RESEND_ANOTHER_OTP")} ${timeLeft} ${t("CS_RESEND_SECONDS")}`}</span>
          </CardText>
        ) : (
          <p className="card-text-button resend-otp" onClick={handleResendOtp}>
            {t("CS_RESEND_OTP")}
          </p>
        )}
        {!error && <CardLabelError>{t("CS_INVALID_OTP")}</CardLabelError>}
      </Fragment>
    );
  }

  return (
    <FormStep onSelect={onSelect} config={config} t={t} isDisabled={!(otp?.length === 6 && canSubmit)} cardStyle={{ minWidth: "100%" }}>
      <div style={{ display: "flex" }}>
        <OTPInput length={6} onChange={onOtpChange} value={otp} />
      </div>
      {timeLeft > 0 ? (
        <CardText>{`${t("CS_RESEND_ANOTHER_OTP")} ${timeLeft} ${t("CS_RESEND_SECONDS")}`}</CardText>
      ) : (
        <p className="card-text" onClick={handleResendOtp} style={{ backgroundColor: "#fff", color: "#f47738", cursor: "pointer" }}>
          {t("CS_RESEND_OTP")}
        </p>
      )}
      {!error && <CardLabelError>{t("CS_INVALID_OTP")}</CardLabelError>}
    </FormStep>
  );
};

export default SelectOtp;
