import { CardLabel, CardLabelError, CardText, CloseSvg, Modal } from "@egovernments/digit-ui-react-components";
import React, { Fragment, useState } from "react";
import useInterval from "../../../hooks/useInterval";
import OTPInput from "../../../components/OTPInput";
import FormStep from "../../../components/FormStep";
import { Close } from "@egovernments/digit-ui-svg-components";
import { useHistory } from "react-router-dom/cjs/react-router-dom.min";

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
            <Close />{" "}
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

  return (
    <Modal
      headerBarEnd={<CloseBtn onClick={onCancel} isMobileView={false} />}
      actionSaveLabel={t("VERIFY")}
      actionSaveOnSubmit={onSelect}
      isDisabled={!(otp?.length === 6 && canSubmit)}
      formId="modal-action"
      headerBarMain={
        <div>
          <Heading label={isAdhaar ? t("Verify_Otp_Aadhaar") : t("Verify_Otp_MOBILE")} />
          <CardText style={{ marginLeft: "20px" }}>{cardText}</CardText>
        </div>
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
        {timeLeft > 0 ? (
          <CardText style={{ alignSelf: "flex-start" }}>{`${t("CS_RESEND_ANOTHER_OTP")} ${timeLeft} ${t("CS_RESEND_SECONDS")}`}</CardText>
        ) : (
          <p className="card-text" onClick={handleResendOtp} style={{ backgroundColor: "#fff", color: "#f47738", cursor: "pointer" }}>
            {t("CS_RESEND_OTP")}
          </p>
        )}
        {!error && <CardLabelError>{t("CS_INVALID_OTP")}</CardLabelError>}
      </FormStep>
    </Modal>
  );
};

export default SelectOtp;
