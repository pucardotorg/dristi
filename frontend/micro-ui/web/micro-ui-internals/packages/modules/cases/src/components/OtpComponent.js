import OTPInput from "@egovernments/digit-ui-module-dristi/src/components/OTPInput";
import useInterval from "@egovernments/digit-ui-module-dristi/src/hooks/useInterval";
import { CardLabelError, CardText } from "@egovernments/digit-ui-react-components";
import React, { useState } from "react";

const otpComponentStyle = {
  padding: "0px 16px",
  display: "flex",
  flexDirection: "column",
  gap: "40px",
};

const otpCardTextStyle = {
  margin: "0px",
  fontFamily: "Roboto",
  fontSize: "14px",
  fontWeight: 400,
  lineHeight: "24px",
  textAlign: "left",
  color: "#0b0c0c",
};

const resendMessageStyle = {
  margin: "0px",
};

const resendLinkStyle = {
  cursor: "pointer",
};

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
    <div className="otp-component" style={otpComponentStyle}>
      <CardText className="otp-card-text" style={otpCardTextStyle}>{`${t(cardText)} ${
        mobileNumber ? " +91****" + mobileNumber.slice(-4) : ""
      }`}</CardText>
      <div className="otp-resend-wrapper">
        <OTPInput inputStyle={{ width: "78px" }} length={size} onChange={onOtpChange} value={otp} />
        <div className="message">
          <p className="resend-message" style={resendMessageStyle}>
            {timeLeft > 0 ? <span className="time-left">{`${t("CS_RESEND_ANOTHER_OTP")} ${timeLeft} ${t("CS_RESEND_SECONDS")}`} </span> : ""}
            <span className={`resend-link ${timeLeft > 0 ? "disabled" : ""}`} style={timeLeft <= 0 ? resendLinkStyle : {}} onClick={handleResendOtp}>
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
