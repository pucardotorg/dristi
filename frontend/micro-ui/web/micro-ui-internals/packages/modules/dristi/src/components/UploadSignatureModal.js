import { CloseSvg, Modal } from "@egovernments/digit-ui-react-components";
import React, { useState } from "react";
import SelectCustomDragDrop from "./SelectCustomDragDrop";

function UploadSignatureModal({ t, setOpenUploadSignatureModal, config, onSelect, formData }) {
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

  const onSubmit = () => {
    setPage(1);
  };

  const onCancel = () => {
    setOpenUploadSignatureModal(false);
  };

  return (
    <Modal
      headerBarEnd={<CloseBtn onClick={onCancel} />}
      actionSaveLabel={t("CS_SUBMIT_SIGNATURE")}
      actionSaveOnSubmit={onSubmit}
      formId="modal-action"
      headerBarMain={<Heading label={t("CS_UPLOAD_SIGNATURE")} />}
      className="case-types"
    >
      <SelectCustomDragDrop config={config} t={t} onSelect={onSelect} formData={formData} />
    </Modal>
  );
}

export default UploadSignatureModal;
