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
        <div className="heading-back-error">
          <LeftArrow />
        </div>
        <div className="heading-title">
          <h1 className="heading-m">{props.label}</h1>
          <p>{`cheque_bounced.jpeg  |  2MB`}</p>
        </div>
      </div>
    )

  };
  const HeaderBarEnd = () => {
    return (
      <React.Fragment>
        <div className="flag-icon"
          onClick={(e) => {
            handleOpenPopup(e, imageInfo.configKey, imageInfo.name, imageInfo.dataIndex, imageInfo.fieldName);
          }}
        >
          <FlagIcon />
        </div>
        <div className="close-icon">
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
      <DocViewerWrapper fileStoreId={imageInfo.fileStore} tenantId={tenantId} docWidth="250px" showDownloadOption={false} />
    </Modal>
  );
}

export default ImageModal;
