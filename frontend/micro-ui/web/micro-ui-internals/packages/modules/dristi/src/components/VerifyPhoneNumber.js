import { CardLabel, CardLabelError, CloseSvg, FormComposerV2, LabelFieldPair, Modal, TextInput } from "@egovernments/digit-ui-react-components";
import React, { useMemo, useState } from "react";
import Button from "./Button";
import { verifyMobileNoConfig } from "../configs/component";
import useInterval from "../hooks/useInterval";
import { InfoIconRed } from "../icons/svgIndex";
const TYPE_REGISTER = { type: "register" };
const TYPE_LOGIN = { type: "login" };
const DEFAULT_USER = "digit-user";

const RoundedCheck = ({ className, height = "24", width = "24", style = {}, fill = "green", onClick = null }) => {
  return (
    <svg xmlns="http://www.w3.org/2000/svg" height={height} width={width} viewBox="0 0 24 24" fill={fill} className={className}>
      <path d="M0 0h24v24H0V0z" fill="none" />
      <path d="M12 2C6.48 2 2 6.48 2 12s4.48 10 10 10 10-4.48 10-10S17.52 2 12 2zM9.29 16.29L5.7 12.7c-.39-.39-.39-1.02 0-1.41.39-.39 1.02-.39 1.41 0L10 14.17l6.88-6.88c.39-.39 1.02-.39 1.41 0 .39.39.39 1.02 0 1.41l-7.59 7.59c-.38.39-1.02.39-1.41 0z" />
    </svg>
  );
};

const CloseBtn = (props) => {
  return (
    <div onClick={props?.onClick} style={{ height: "100%", display: "flex", alignItems: "center", paddingRight: "20px", cursor: "pointer" }}>
      <CloseSvg />
    </div>
  );
};

const Heading = (props) => {
  return <h1 className="heading-m">{props.label}</h1>;
};

function VerifyPhoneNumber({ t, config, onSelect, formData = {}, errors, setError }) {
  const [{ showModal }, setState] = useState({
    showModal: false,
  });

  const handleCloseModal = () => {
    setState((prev) => ({
      ...prev,
      showModal: false,
    }));
  };

  const resendOtp = async (input) => {
    const data = {
      mobileNumber: formData[config.key]?.[input?.mobileNoKey],
      tenantId: stateCode,
      userType: getUserType(),
    };
    const [res, err] = await sendOtp({ otp: { ...data, ...TYPE_LOGIN } });
    if (!err) {
      return;
    } else {
      const [res, err] = await sendOtp({ otp: { ...data, name: DEFAULT_USER, ...TYPE_REGISTER } });
      return;
    }
  };

  const selectOtp = async (input) => {
    try {
      if (isUserRegistered) {
        const requestData = {
          username: formData[config.key]?.[input?.mobileNoKey],
          password: formData[config.key]?.[input?.name],
          tenantId: stateCode,
          userType: getUserType(),
        };
        const { ResponseInfo, UserRequest: info, ...tokens } = await window?.Digit.UserService.authenticate(requestData);
        if (window?.globalConfigs?.getConfig("ENABLE_SINGLEINSTANCE")) {
          info.tenantId = window?.Digit.ULBService.getStateId();
        }

        setUser({ info, ...tokens });
        onSelect(config?.key, { ...formData?.[config.key], userDetails: { info, ...tokens } });
      } else if (!isUserRegistered) {
        const requestData = {
          name: DEFAULT_USER,
          username: formData[config.key]?.[input?.mobileNoKey],
          otpReference: formData[config.key]?.[input?.name],
          tenantId: stateCode,
        };

        const { ResponseInfo, UserRequest: info, ...tokens } = await window?.Digit.UserService.registerUser(requestData, stateCode);

        if (window?.globalConfigs?.getConfig("ENABLE_SINGLEINSTANCE")) {
          info.tenantId = window?.Digit.ULBService.getStateId();
        }

        setUser({ info, ...tokens });
        onSelect(config?.key, { ...formData?.[config.key], userDetails: { info, ...tokens } });
      }
    } catch (err) { }
  };

  const input = useMemo(() => verifyMobileNoConfig?.[0]?.body?.[0]?.populators?.inputs?.[0], []);

  return (
    <React.Fragment>
      <LabelFieldPair style={{ width: "100%", display: "flex", alignItem: "center" }}>
        <CardLabel className="card-label-smaller">{t(config.label)}</CardLabel>
      </LabelFieldPair>
      <div style={{ display: "flex", justifyContent: "space-between", gap: 24 }}>
        <div className="field user-details-form-style" style={{ display: "flex", width: "100%" }}>
          {config?.componentInFront ? <span className="citizen-card-input citizen-card-input--front">{config?.componentInFront}</span> : null}
          <TextInput
            value={formData?.[config.key]?.[config.name]}
            name={config.name}
            minlength={config?.validation?.minLength}
            maxlength={config?.validation?.maxLength}
            validation={config?.validation}
            ValidationRequired={config?.validation}
            title={config?.validation?.title}
            disable={config?.disable ? config?.disable : false}
            // inputRef={register(config?.validation)}
            isMandatory={errors[config?.name]}
            onChange={(e) => {
              const { value } = e.target;
              if (value?.length >= config?.validation?.minLength && !value.match(config?.validation?.pattern)) {
                setError(config.key, { [config?.name]: config?.error });
              } else {
                setError(config.key, { [config?.name]: "" });
              }
              onSelect(config?.key, { ...formData?.[config.key], [config?.name]: value });
            }}
          />
        </div>
        <Button
          label={"VERIFY_OTP"}
          style={{ alignItems: "center" }}
          className={"secondary-button-selector"}
          labelClassName={"secondary-label-selector"}
          isDisabled={
            !formData?.[config.key]?.[config.name] ||
            errors?.[config?.key]?.[config.name] ||
            formData?.[config.key]?.[config.name]?.length < config?.validation?.minLength ||
            formData?.[config.key]?.[config.name]?.length > config?.validation?.maxLength
          }
          onButtonClick={() => {
            // handleAdd(value);
            setState((prev) => ({
              ...prev,
              showModal: true,
            }));
          }}
        />
      </div>
      <CardLabelError>{t(errors?.[config?.key]?.[config.name] ? "VERIFY_PHONE_ERROR_TEXT" : "VERIFY_PHONE_DEFAULT_TEXT")}</CardLabelError>
      {showModal && (
        <Modal
          headerBarEnd={<CloseBtn onClick={handleCloseModal} isMobileView={true} />}
          actionCancelOnSubmit={() => { }}
          actionSaveLabel={t("VERIFY")}
          actionSaveOnSubmit={() => {
            selectOtp(input)
              .then(() => {
                setState((prev) => ({
                  ...prev,
                  isUserVerified: true,
                }));
              })
              .catch(() => {
                setState((prev) => ({
                  ...prev,
                  isUserVerified: false,
                }));
              })
              .finally(() => {
                setState((prev) => ({
                  ...prev,
                  showModal: false,
                }));
              });
          }}
          formId="modal-action"
          headerBarMain={<Heading label={t("UPLOAD_ID_PROOF_HEADER")} />}
          submitTextClassName={"verification-button-text-modal"}
          className={'case-types'}
        >
          <div>
            <LabelFieldPair>
              <CardLabel className="card-label-smaller" style={{ display: "flex" }}>
                {t(input.label) +
                  `${input?.hasMobileNo
                    ? formData[config.key]?.[input?.mobileNoKey]
                      ? input?.isMobileSecret
                        ? input?.mobileCode
                          ? ` ${input?.mobileCode}-******${formData[config.key]?.[input?.mobileNoKey]?.substring(6)}`
                          : ` ${formData[config.key]?.[input?.mobileNoKey]?.substring(6)}`
                        : ` ${formData[config.key]?.[input?.mobileNoKey]}`
                      : ""
                    : ""
                  }`}
              </CardLabel>
              <div className="field">
                {input?.type === "text" && (
                  <TextInput
                    className="field desktop-w-full"
                    key={input.name}
                    value={formData && formData[config.key] ? formData[config.key][input.name] : undefined}
                    onChange={(e) => {
                      onSelect(config?.key, { ...formData?.[config.key], [input?.name]: e.target.value });
                    }}
                    disable={input.isDisabled}
                    defaultValue={undefined}
                    {...input.validation}
                  />
                )}
                {formData?.[config.key][input.name] &&
                  formData?.[config.key][input.name].length > 0 &&
                  !["documentUpload", "radioButton"].includes(input.type) &&
                  input.validation &&
                  !formData?.[config.key][input.name].match(
                    window?.Digit.Utils.getPattern(input.validation.patternType) || input.validation.pattern
                  ) && (
                    <CardLabelError style={{ width: "100%", marginTop: "-15px", fontSize: "16px", marginBottom: "12px" }}>
                      <span style={{ color: "#FF0000" }}> {t(input.validation?.errMsg || "CORE_COMMON_INVALID")}</span>
                    </CardLabelError>
                  )}
              </div>
            </LabelFieldPair>
            {input?.hasResendOTP && (
              <React.Fragment>
                {timeLeft > 0 ? (
                  <CardText>{`${t("CS_RESEND_ANOTHER_OTP")} ${timeLeft} ${t("CS_RESEND_SECONDS")}`}</CardText>
                ) : (
                  <p className="card-text" onClick={() => resendOtp(input)} style={{ backgroundColor: "#fff", color: "#007E7E", cursor: "pointer" }}>
                    {t("CS_RESEND_OTP")}
                  </p>
                )}
              </React.Fragment>
            )}
          </div>
        </Modal>
      )}
    </React.Fragment>
  );
}

export default VerifyPhoneNumber;
