import { CloseSvg, Modal, TextInput } from "@egovernments/digit-ui-react-components";
import React, { useState } from "react";

function EsignAdharModal({ t, modalconfig = { name: "Enter Aadhar to Esign" } }) {
  const [page, setPage] = useState(0);
  const [aadharNumber, setAadharNumber] = useState();
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

  const onSelect = () => {
    setPage(1);
  };

  const onCancel = () => {};
  return (
    <Modal
      headerBarEnd={<CloseBtn onClick={onCancel} />}
      actionCancelOnSubmit={onCancel}
      actionSaveLabel={t("CS_SEND_OTP")}
      actionSaveOnSubmit={onSelect}
      formId="modal-action"
      headerBarMain={<Heading label={t("CS_ESIGN_AADHAR")} />}
      style={{ height: "3vh" }}
      className="case-types"
    >
      <div>
        <div>Enter Adhar No. to E-sign</div>
        <div style={{ display: "flex", width: "100%" }}>
          <TextInput
            value={aadharNumber}
            onChange={(event) => {
              const { value } = event.target;
              setAadharNumber(value);
            }}
            name={modalconfig.name}
            minlength={modalconfig?.validation?.minLength}
            maxlength={modalconfig?.validation?.maxLength}
            validation={modalconfig?.validation}
            ValidationRequired={modalconfig?.validation}
            title={modalconfig?.validation?.title}
          />
        </div>
      </div>
    </Modal>
  );
}

export default EsignAdharModal;
