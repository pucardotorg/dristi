import { CloseSvg, TextInput } from "@egovernments/digit-ui-react-components";
import React, { useMemo, useState } from "react";
import { ErrorInfoIcon } from "../icons/svgIndex";
import Modal from "./Modal";
import { useToast } from "./Toast/useToast";

function EsignAdharModal({ t, setOpenAadharModal, name, onSelect, config, formData }) {
  const toast = useToast();
  function setValue(value, input) {
    if (Array.isArray(input)) {
      onSelect(config.key, {
        ...formData[config.key],
        ...input.reduce((res, curr) => {
          res[curr] = value[curr];
          return res;
        }, {}),
      });
    } else onSelect(config.key, { ...formData[config.key], [input]: value });
  }

  const [page, setPage] = useState(0);
  const [aadharNumber, setAadharNumber] = useState("");
  const [otp, setOtp] = useState("");
  const [errorMessage, setErrorMessage] = useState(null);
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

  const onSubmit = () => {
    if (page === 0) {
      setPage(1);
      return;
    }
    if (page === 1) {
      setValue(["aadharsignature"], name);
      setOpenAadharModal(false);
    }
    toast.success(t("CS_E_SIGN_VERIFIED"));
  };

  const onCancel = () => {
    setOpenAadharModal(false);
  };

  const textfieldTitle = useMemo(() => {
    return page === 0 ? "Enter Aadhar to Esign" : "Enter OTP Sent to ";
  }, [page]);

  const minLength = useMemo(() => {
    return page === 0 ? 12 : 6;
  }, [page]);

  const maxLength = useMemo(() => {
    return page === 0 ? 12 : 6;
  }, [page]);

  const bottomText = useMemo(() => {
    if (errorMessage) {
      return errorMessage;
    }
    if (page === 0) {
      return "Make sure to add the Aadhar of person E-Signing.";
    }
    if (page === 1) {
      return "Haven’t received OTP?";
    }
  }, [page, errorMessage]);

  const isDisabled = useMemo(() => {
    return (page === 0 && aadharNumber.length < minLength) || errorMessage || (page === 1 && otp.length < minLength) ? true : false;
  }, [aadharNumber, errorMessage, minLength, otp.length, page]);

  return (
    <Modal
      headerBarEnd={<CloseBtn onClick={onCancel} />}
      actionSaveLabel={page === 0 ? t("CS_SEND_OTP") : t("CS_VERIFY_AADHAR")}
      actionSaveOnSubmit={onSubmit}
      formId="modal-action"
      headerBarMain={<Heading label={t("CS_ESIGN_AADHAR")} />}
      className="e-sign-aadhar-modal"
      isDisabled={isDisabled}
      submitTextClassName="e-sign-aadhar-button"
    >
      <div className="e-sign-aadhar-main">
        <h3 className="e-sign-aadhar-title">{textfieldTitle}</h3>
        <div style={{ width: "100%" }}>
          <TextInput
            value={page === 0 ? aadharNumber : otp}
            onChange={(event) => {
              const { value } = event.target;
              page === 0 ? setAadharNumber(value) : setOtp(value);
              const isDigitsOnly = /^\d+$/.test(value);
              setErrorMessage(isDigitsOnly ? "" : t("CS_NO_ALPHABETICAL_VALUES"));
            }}
            minlength={minLength}
            maxlength={maxLength}
          />
          <div style={{ display: "flex", justifyContent: "flex-start", alignItems: "center", gap: "3px" }}>
            {errorMessage && <ErrorInfoIcon />}
            <div style={{ color: errorMessage ? "#BB2C2F" : "black" }}>{bottomText}</div>
            {page === 1 && <div style={{ borderBottom: "1px", color: "#007E7E", textDecoration: "underline", cursor: "pointer" }}>Resend</div>}
          </div>
        </div>
      </div>
    </Modal>
  );
}

export default EsignAdharModal;
