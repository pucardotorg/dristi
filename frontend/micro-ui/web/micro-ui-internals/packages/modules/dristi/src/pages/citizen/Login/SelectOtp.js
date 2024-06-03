import { CardLabel, CardLabelError, CardText, CloseSvg, Modal } from "@egovernments/digit-ui-react-components";
import React, { Fragment, useEffect, useState } from "react";
import useInterval from "../../../hooks/useInterval";
import OTPInput from "../../../components/OTPInput";
import FormStep from "../../../components/FormStep";
import { Close } from "@egovernments/digit-ui-svg-components";
import { useHistory } from "react-router-dom/cjs/react-router-dom.min";
import { CloseIconWhite } from "../../../icons/svgIndex";

const SelectOtp = ({
  config,
  otp,
  onOtpChange,
  onResend,
  onSelect,
  t,
  error,
  userType = "citizen",
  canSubmit,
  params,
  setParams,
  path,
  isAdhaar,
  cardText,
  mobileNumber,
}) => {
  const history = useHistory();
  const token = window.localStorage.getItem("token");
  const isUserLoggedIn = Boolean(token);
  const [timeLeft, setTimeLeft] = useState(25);
  useInterval(
    () => {
      setTimeLeft(timeLeft - 1);
    },
    timeLeft > 0 ? 1000 : null
  );

  const handleResendOtp = () => {
    onResend();
    setTimeLeft(25);
  };
  const onCancel = () => {
    setParams({
      ...params,
      otp: "",
      aadharOtp: "",
    });

    history.goBack();
  };
  const Heading = (props) => {
    return <h1 className="heading-m">{props.label}</h1>;
  };
  const CloseBtn = (props) => {
    return (
      <div onClick={props?.onClick} style={props?.isMobileView ? { padding: 5 } : null}>
        {props?.isMobileView ? (
          <CloseSvg />
        ) : (
          <div className={"icon-bg-secondary"} style={{ backgroundColor: "#505A5F" }}>
            {" "}
            <CloseIconWhite />{" "}
          </div>
        )}
      </div>
    );
  };

  if (!params?.mobileNumber && !isAdhaar) {
    history.push(path);
  } else if (!params?.adhaarNumber && isAdhaar) {
    history.push(path);
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

  const handleKeyDown = (e) => {
    if (e.key === "Enter") {
      onSelect();
    }
  };

  const onModalSubmit = () => {
    onSelect();
  };

  useEffect(() => {
    window.addEventListener("keydown", handleKeyDown);
    return () => {
      window.removeEventListener("keydown", handleKeyDown);
    };
  }, []);

  return (
    <Modal
      headerBarEnd={<CloseBtn onClick={onCancel} isMobileView={false} />}
      actionSaveLabel={t("VERIFY")}
      actionSaveOnSubmit={onModalSubmit}
      isDisabled={!(otp?.length === 6 && canSubmit)}
      formId="modal-action"
      headerBarMain={
        <React.Fragment>
          <Heading label={isAdhaar ? t("Verify_Otp_Aadhaar") : t("Verify_Otp_MOBILE")} />
          <CardText>{`${cardText}${mobileNumber ? " +91****" + mobileNumber.slice(-4) : ""}`}</CardText>
        </React.Fragment>
      }
      popupStyles={{ width: "580px", alignItems: "center" }}
    >
      <FormStep
        onSelect={onSelect}
        config={config}
        t={t}
        isDisabled={!(otp?.length === 6 && canSubmit)}
        cardStyle={{ minWidth: "100%", alignItems: "center" }}
      >
        <div style={{ display: "flex" }}>
          <OTPInput length={6} onChange={onOtpChange} value={otp} />
        </div>
        <div className="message">
          <p>
            {timeLeft > 0 ? <span className="time-left">{`${t("CS_RESEND_ANOTHER_OTP")} ${timeLeft} ${t("CS_RESEND_SECONDS")}`} </span> : ""}
            <span className={`resend-link ${timeLeft > 0 ? "disabled" : ""}`} onClick={handleResendOtp}>
              {t("CS_RESEND_OTP")}
            </span>
          </p>
        </div>
        {!error && <CardLabelError>{t("CS_INVALID_OTP")}</CardLabelError>}
      </FormStep>
    </Modal>
  );
};

export default SelectOtp;
