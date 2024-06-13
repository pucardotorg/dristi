import React from "react";
import Modal from "./Modal";
import { CloseSvg } from "@egovernments/digit-ui-react-components";

function SendCaseBackModal({ totalErrors, onCancel, onSubmit, t, heading, type, actionCancelLabel, actionSaveLabel, handleCloseModal }) {
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
  const subtexts = {
    registerCase: t("CS_NO_ERROR_MARKED"),
    sendCaseBack: `${t("CS_YOU_HAVE_MARKED")} ${totalErrors} ${t("CS_COMMON_ERRORS")} ${t("CS_IN_THIS_FILE")}`,
    sendCaseBackPotential: `${t("CS_CONFIRMING")} ${totalErrors} ${t("CS_POTENTIAL_ERRORS")} ${t("CS_FLAGGED_BY_SYSTEM")}`,
  };

  return (
    <Modal
      headerBarEnd={
        <CloseBtn
          onClick={() => {
            handleCloseModal ? handleCloseModal() : onCancel();
          }}
        />
      }
      actionCancelLabel={t(actionCancelLabel)}
      actionCancelOnSubmit={onCancel}
      actionSaveLabel={t(actionSaveLabel)}
      actionSaveOnSubmit={onSubmit}
      formId="modal-action"
      headerBarMain={<Heading label={t(heading)} />}
      className="case-types"
    >
      <p style={{ padding: 20 }}>{subtexts[type]}</p>
    </Modal>
  );
}

export default SendCaseBackModal;
