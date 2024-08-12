import React from "react";
import Modal from "./Modal";
import { CloseSvg } from "@egovernments/digit-ui-react-components";

function DeleteCommentModal({ onCancel, onSelect, t }) {
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
  return (
    <Modal
      headerBarEnd={<CloseBtn onClick={onCancel} />}
      actionCancelLabel={t("CORE_LOGOUT_CANCEL")}
      actionCancelOnSubmit={onCancel}
      actionSaveLabel={t("CS_COMMON_DELETE")}
      actionSaveOnSubmit={onSelect}
      formId="modal-action"
      headerBarMain={<Heading label={t("CS_DELETE_COMMENT")} />}
      className="case-types"
    >
      <p style={{ padding: 20 }}>{t("CS_DELETE_COMMENT_TEXT")}</p>
    </Modal>
  );
}

export default DeleteCommentModal;
