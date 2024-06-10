import { CloseSvg, Modal } from "@egovernments/digit-ui-react-components";
import React from "react";
import SelectCustomDragDrop from "./SelectCustomDragDrop";

function UploadSignatureModal({ t, setOpenUploadSignatureModal, config, onSelect, formData, name }) {
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
    setOpenUploadSignatureModal(false);
  };

  const onCancel = () => {
    setValue(null, name);
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
