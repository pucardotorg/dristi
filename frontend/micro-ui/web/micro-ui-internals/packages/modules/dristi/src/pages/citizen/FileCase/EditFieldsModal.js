import { CloseSvg, Modal } from "@egovernments/digit-ui-react-components";
import React from "react";

function EditFieldsModal({ t, config, setOpenConfigurationModal, selected, handlePageChange }) {
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
  const onCancel = () => {
    setOpenConfigurationModal(false);
  };
  const onSubmit = () => {
    handlePageChange(selected, true);
    setOpenConfigurationModal(false);
  };
  return (
    <Modal
      headerBarEnd={<CloseBtn onClick={onCancel} />}
      actionCancelLabel={t(config?.actionCancelLabel)}
      actionCancelOnSubmit={onCancel}
      actionSaveLabel={t(config?.actionSaveLabel)}
      actionSaveOnSubmit={onSubmit}
      formId="modal-action"
      headerBarMain={<Heading label={t(config?.headerBarMain)} />}
      className="case-types"
    >
      <div style={{ paddingTop: "10px", paddingBottom: "10px" }}>{t(config?.modalText)}</div>
    </Modal>
  );
}

export default EditFieldsModal;
