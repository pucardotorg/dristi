import React from "react";
import Modal from "./Modal";
import { FlagIcon } from "../icons/svgIndex";
import { CloseSvg } from "@egovernments/digit-ui-react-components";
import DocViewerWrapper from "../pages/employee/docViewerWrapper";

function ImageModal({ imageInfo, handleCloseModal, handleOpenPopup, t }) {
  const tenantId = window?.Digit.ULBService.getCurrentTenantId();
  const Heading = (props) => {
    return <h1 className="heading-m">{props.label}</h1>;
  };
  const HeaderBarEnd = () => {
    return (
      <div style={{ display: "flex", gap: 20, alignItems: "center", justifyContent: "end" }}>
        <div
          onClick={(e) => {
            handleOpenPopup(e, imageInfo.configKey, imageInfo.name, imageInfo.dataIndex, imageInfo.fieldName);
          }}
          style={{ height: "100%", display: "flex", alignItems: "center", paddingRight: "20px", cursor: "pointer" }}
        >
          <FlagIcon />
        </div>
        <div onClick={handleCloseModal} style={{ height: "100%", display: "flex", alignItems: "center", paddingRight: "20px", cursor: "pointer" }}>
          <CloseSvg />
        </div>
      </div>
    );
  };
  return (
    <Modal
      headerBarEnd={<HeaderBarEnd />}
      formId="modal-action"
      headerBarMain={<Heading label={t("CS_BOUNCED_CHEQUE")} />}
      className="view-image-modal"
      hideSubmit
      style={{ height: "100%", width: "100%" }}
    >
      <DocViewerWrapper fileStoreId={imageInfo.fileStore} tenantId={tenantId} docWidth="250px" showDownloadOption={false} />
    </Modal>
  );
}

export default ImageModal;
