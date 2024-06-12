import React from "react";
import Modal from "./Modal";
import { CloseSvg } from "@egovernments/digit-ui-react-components";

function SendCaseBackModal({ totalErrors, onCancel, onSubmit, t, heading, type }) {
  const Heading = (props) => {
    return <h1 className="heading-m">{props.label}</h1>;
  };
  const CloseBtn = (props) => {
    return (
      <div onClick={props?.onClick} style={{ height: "100%", display: "flex", alignItems: "center", paddingRight: "20px", cursor: "pointer" }}>
        <CloseSvg />
      </div>
    );
  };
  const errorText = `${t("CS_YOU_HAVE_MARKED")} ${totalErrors} ${t("CS_COMMON_ERRORS")} ${t("CS_IN_THIS_FILE")}`;
  return (
    <Modal
      headerBarEnd={<CloseBtn onClick={onCancel} />}
      actionCancelLabel={t("CS_COMMON_BACK")}
      actionCancelOnSubmit={onCancel}
      actionSaveLabel={t("CS_COMMON_CONFIRM")}
      actionSaveOnSubmit={onSubmit}
      formId="modal-action"
      headerBarMain={<Heading label={t(heading)} />}
      className="case-types"
    >
      <p style={{ padding: 20 }}>{type === "registerCase" ? t("CS_NO_ERROR_MARKED") : errorText}</p>
    </Modal>
  );
}

export default SendCaseBackModal;
