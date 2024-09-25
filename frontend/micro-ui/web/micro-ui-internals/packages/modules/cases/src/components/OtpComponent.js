import useInterval from "@egovernments/digit-ui-module-dristi/src/hooks/useInterval";
import { CardLabelError, CardText, FormStep, OTPInput } from "@egovernments/digit-ui-react-components";
import React, { useEffect, useState } from "react";

const OtpComponent = ({
  t,
  otp,
  onOtpChange,
  error,
  onResend,
  size,
  mobileNumber,
  cardText = "Enter the OTP sent to Respondent’s phone number",
  otpEnterTime,
}) => {
  const [timeLeft, setTimeLeft] = useState(otpEnterTime);

  useInterval(
    () => {
      setTimeLeft(timeLeft - 1);
    },
    timeLeft > 0 ? 1000 : null
  );

  const handleResendOtp = () => {
    onResend();
    setTimeLeft(otpEnterTime);
  };
  return (
    <div className="otp-component">
      <CardText className="otp-card-text">{`${t(cardText)} ${mobileNumber ? " +91****" + mobileNumber.slice(-4) : ""}`}</CardText>
      <div className="otp-resend-wrapper">
        <OTPInput length={size} onChange={onOtpChange} value={otp} />
        <div className="message">
          <p className="resend-message">
            {timeLeft > 0 ? <span className="time-left">{`${t("CS_RESEND_ANOTHER_OTP")} ${timeLeft} ${t("CS_RESEND_SECONDS")}`} </span> : ""}
            <span className={`resend-link ${timeLeft > 0 ? "disabled" : ""}`} onClick={handleResendOtp}>
              {t("CS_RESEND_OTP")}
            </span>
          </p>
        </div>
      </div>
      {error && <CardLabelError>{error}</CardLabelError>}
    </div>
  );
};

export default OtpComponent;
