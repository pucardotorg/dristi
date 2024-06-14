import React from "react";
import Modal from "./Modal";
import { FlagIcon, LeftArrow } from "../icons/svgIndex";
import { CloseSvg } from "@egovernments/digit-ui-react-components";
import DocViewerWrapper from "../pages/employee/docViewerWrapper";

function ImageModal({ imageInfo, handleCloseModal, handleOpenPopup, t }) {
  const tenantId = window?.Digit.ULBService.getCurrentTenantId();
  const Heading = (props) => {
    return (
      <div className="heading-main">
        <div className="heading-back-error" onClick={handleCloseModal}>
          <LeftArrow />
        </div>
        <div className="heading-title">
          <h1 className="heading-m">{props.label}</h1>
          <p>{`cheque_bounced.jpeg  |  2MB`}</p>
        </div>
      </div>
    );
  };
  const HeaderBarEnd = () => {
    return (
      <React.Fragment>
        <div
          className="flag-icon"
          onClick={(e) => {
            handleOpenPopup(null, imageInfo.configKey, imageInfo.name, imageInfo.dataIndex, imageInfo.fieldName);
          }}
        >
          <FlagIcon />
        </div>
        <div className="close-icon" onClick={handleCloseModal}>
          <CloseSvg />
        </div>
      </React.Fragment>
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
      <DocViewerWrapper fileStoreId={imageInfo.data.fileStore} tenantId={tenantId} docWidth="100%" docHeight={"100%"} showDownloadOption={false} />
    </Modal>
  );
}

export default ImageModal;
