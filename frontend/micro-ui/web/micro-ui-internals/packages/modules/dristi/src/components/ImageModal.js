import React from "react";
import Modal from "./Modal";
import { FlagIcon, LeftArrow } from "../icons/svgIndex";
import { CloseSvg } from "@egovernments/digit-ui-react-components";
import DocViewerWrapper from "../pages/employee/docViewerWrapper";

function ImageModal({ imageInfo, handleCloseModal, handleOpenPopup, t, anchorRef, showFlag, isPrevScrutiny, selectedDocs }) {
  let showFlagNew = !imageInfo?.disableScrutiny && showFlag;

  if (isPrevScrutiny && !imageInfo?.disableScrutiny) {
    showFlagNew = imageInfo?.inputlist?.some((key) => {
      return Boolean(imageInfo?.dataError?.[key]?.FSOError);
    });
  }
  const tenantId = window?.Digit.ULBService.getCurrentTenantId();
  const Heading = (props) => {
    return (
      <div className="heading-main">
        <div className="heading-back-error" onClick={handleCloseModal}>
          <LeftArrow />
        </div>
        <div className="heading-title">
          <h1 className="heading-m">{props.label}</h1>
          <p>{props.fileName}</p>
        </div>
      </div>
    );
  };
  const HeaderBarEnd = () => {
    return (
      <React.Fragment>
        {showFlagNew && (
          <div
            ref={anchorRef}
            className="flag-icon"
            onClick={(e) => {
              handleOpenPopup(
                null,
                imageInfo?.configKey,
                imageInfo?.name,
                imageInfo?.index,
                imageInfo?.fieldName,
                imageInfo?.inputlist,
                imageInfo?.data?.fileName
              );
            }}
          >
            <FlagIcon />
          </div>
        )}
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
      headerBarMain={
        <Heading
          label={imageInfo?.data?.fileName ? t(imageInfo?.data?.fileName) : selectedDocs?.[0]?.name}
          fileName={imageInfo?.data?.documentName}
        />
      }
      className="view-image-modal"
      hideSubmit
      style={{ height: "100%", width: "100%" }}
    >
      <DocViewerWrapper
        fileStoreId={imageInfo?.data?.fileStore}
        selectedDocs={selectedDocs}
        tenantId={tenantId}
        docWidth="100%"
        docViewerStyle={imageInfo?.data?.docViewerStyle}
        docHeight={"100%"}
        showDownloadOption={false}
      />
    </Modal>
  );
}

export default ImageModal;
