import { Button, CardLabelError, CardText, CloseSvg, Modal } from "@egovernments/digit-ui-react-components";
import React, { Fragment, useEffect, useState } from "react";
import useInterval from "../../../hooks/useInterval";
import OTPInput from "../../../components/OTPInput";
import FormStep from "../../../components/FormStep";
import { useHistory } from "react-router-dom/cjs/react-router-dom.min";
import { Close } from "@egovernments/digit-ui-svg-components";

const SelectOtp = ({ config, otp, onOtpChange, onResend, onSelect, t, error, userType = "citizen", canSubmit, params }) => {
  const history = useHistory();
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
  const onCancel = () => {
    history.push("/digit-ui/citizen/dristi/home");
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

  if (!params?.mobileNumber) {
    history.push("/digit-ui/citizen/dristi/home/login");
  }
  // console.log(userType);
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
      formId="modal-action"
      headerBarMain={<Heading label={t("Verify Otp")} />}
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
          <CardText>{`${t("CS_RESEND_ANOTHER_OTP")} ${timeLeft} ${t("CS_RESEND_SECONDS")}`}</CardText>
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
