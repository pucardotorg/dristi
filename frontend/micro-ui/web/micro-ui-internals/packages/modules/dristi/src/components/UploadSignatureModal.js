import { CloseSvg } from "@egovernments/digit-ui-react-components";
import React, { useState } from "react";
import SelectCustomDragDrop from "./SelectCustomDragDrop";
import Modal from "./Modal";
import { useToast } from "./Toast/useToast";

function UploadSignatureModal({ t, setOpenUploadSignatureModal, config, onSelect, formData, name }) {
  const toast = useToast();
  const [error, setError] = useState({});
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
    localStorage.setItem("formData", JSON.stringify(formData));
    toast.success(t("CS_E_SIGN_VERIFIED"));
    setOpenUploadSignatureModal(false);
  };

  const onCancel = () => {
    setValue(null, name);
    setOpenUploadSignatureModal(false);
  };

  const clearError = (key) => {
    if (!key) return;
    const updatedError = { ...error };
    delete updatedError[key];
    setError(updatedError);
  };

  const setErrors = (key, errorMsg) => {
    if (!key) return;
    setError((prevErrors) => ({ ...prevErrors, [key]: errorMsg }));
  };

  return (
    <Modal
      headerBarEnd={<CloseBtn onClick={onCancel} />}
      actionSaveLabel={t("CS_SUBMIT_SIGNATURE")}
      actionSaveOnSubmit={onSubmit}
      formId="modal-action"
      isDisabled={!formData?.[config.key] || Boolean(Object.keys(error).length)}
      headerBarMain={<Heading label={t("CS_UPLOAD_SIGNATURE")} />}
      className="upload-signature-modal"
      submitTextClassName="upload-signature-button"
    >
      <div className="upload-signature-modal-main">
        <SelectCustomDragDrop
          config={config}
          t={t}
          onSelect={onSelect}
          formData={formData}
          errors={error}
          setError={setErrors}
          clearErrors={clearError}
        />
      </div>
    </Modal>
  );
}

export default UploadSignatureModal;
