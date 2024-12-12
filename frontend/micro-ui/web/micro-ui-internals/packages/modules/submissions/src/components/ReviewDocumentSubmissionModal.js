import React, { useEffect, useMemo, useState } from "react";
import Modal from "../../../dristi/src/components/Modal";
import { CloseSvg } from "@egovernments/digit-ui-components";
import { Toast } from "@egovernments/digit-ui-react-components";
import SubmissionDocumentEsign from "./SubmissionDocumentEsign";
import Button from "@egovernments/digit-ui-module-dristi/src/components/Button";
import { FileDownloadIcon } from "@egovernments/digit-ui-module-dristi/src/icons/svgIndex";
import { SubmissionDocumentWorkflowState } from "../utils/submissionDocumentsWorkflow";

const downloadSvgStyle = {
  margin: "0px 12px 0px 0px",
  height: "16px",
  width: "16px",
};

const downloadPathStyle = {
  fill: "#007e7e",
};

const Heading = (props) => {
  return <h1 className="heading-m">{props.label}</h1>;
};

const CloseBtn = (props) => {
  return (
    <div style={{ display: "flex" }}>
      <Button
        icon={<FileDownloadIcon svgStyle={downloadSvgStyle} pathStyle={downloadPathStyle} />}
        label={""}
        onButtonClick={() => {
          props.handleDownload();
        }}
        style={{ boxShadow: "none", background: "none", border: "none", padding: "20px 10px", maxWidth: "fit-content" }}
        textStyles={{
          width: "unset",
        }}
      >
        <h1
          style={{
            fontFamily: "Roboto",
            fontSize: "16px",
            fontWeight: 700,
            lineHeight: "18.75px",
            textAlign: "center",
            color: "#007E7E",
          }}
        >
          {props.t("SUBMISSION_DOCUMENT_DOWNLOAD")}
        </h1>
      </Button>
      <div onClick={props?.onClick} style={{ maxHeight: "100%", display: "flex", alignItems: "center", paddingRight: "20px", cursor: "pointer" }}>
        <CloseSvg />
      </div>
    </div>
  );
};

function ReviewDocumentSubmissionModal({
  t,
  combinedFileStoreId,
  handleGoToSign,
  handleGoBack,
  setSignedDocumentUploadID,
  currentSubmissionStatus,
  combinedDocumentFile,
  handleDownloadReviewModal,
}) {
  const DocViewerWrapper = window?.Digit?.ComponentRegistryService?.getComponent("DocViewerWrapper");
  const [showErrorToast, setShowErrorToast] = useState(null);
  const [isSignedHeading, setIsSignedHeading] = useState(false);
  const [signedId, setSignedId] = useState(null);
  const tenantId = Digit.ULBService.getCurrentTenantId();
  const signedDisplayFileStoreId = useMemo(() => signedId || localStorage.getItem("fileStoreId"), [signedId]);

  const closeToast = () => {
    setShowErrorToast(null);
  };
  useEffect(() => {
    if (showErrorToast) {
      const timer = setTimeout(() => {
        setShowErrorToast(null);
      }, 2000);
      return () => clearTimeout(timer);
    }
  }, [showErrorToast]);

  const showDocument = useMemo(() => {
    return (
      <React.Fragment>
        {combinedDocumentFile || combinedFileStoreId || signedId ? (
          <DocViewerWrapper
            docWidth={"100%"}
            docHeight={"fit-content"}
            tenantId={tenantId}
            showDownloadOption={false}
            docViewerStyle={{ maxWidth: "100%", width: "100%" }}
            fileStoreId={
              currentSubmissionStatus === SubmissionDocumentWorkflowState.PENDING_ESIGN && (signedDisplayFileStoreId || combinedFileStoreId)
            }
            selectedDocs={[combinedDocumentFile]}
          />
        ) : (
          <h2>{t("PREVIEW_DOC_NOT_AVAILABLE")}</h2>
        )}
      </React.Fragment>
    );
  }, [combinedDocumentFile, combinedFileStoreId, currentSubmissionStatus, signedDisplayFileStoreId, signedId, t, tenantId]);

  return (
    <Modal
      headerBarMain={<Heading label={!isSignedHeading ? t("REVIEW_SUBMISSION_DOCUMENT_HEADING") : t("VIEW_SIGNED_SUBMISSION")} />}
      headerBarEnd={<CloseBtn t={t} onClick={handleGoBack} handleDownload={handleDownloadReviewModal} />}
      actionCancelLabel={currentSubmissionStatus !== SubmissionDocumentWorkflowState.PENDING_ESIGN && t("SUBMISSION_DOCUMENT_BACK")}
      actionCancelOnSubmit={handleGoBack}
      actionSaveLabel={
        !isSignedHeading
          ? currentSubmissionStatus !== SubmissionDocumentWorkflowState.PENDING_ESIGN
            ? t("SUBMISSION_DOCUMENT_SIGN")
            : t("SUBMISSION_DOCUMENT_NEXT")
          : t("SUBMISSION_DOCUMENT_FINISH")
      }
      isDisabled={currentSubmissionStatus === SubmissionDocumentWorkflowState.PENDING_ESIGN && !isSignedHeading}
      actionSaveOnSubmit={() => {
        handleGoToSign();
      }}
      className={"review-submission-appl-modal"}
      textStyle={{ color: "#FFFFFF", margin: 0 }}
      style={{ backgroundColor: "#007E7E" }}
    >
      <div className="review-submission-appl-body-main" style={{ display: "flex", justifyContent: "space-between", flexDirection: "column" }}>
        <div className="application-details" style={{ alignItems: "center" }}>
          <div className="application-view" style={{ width: "100%" }}>
            {showDocument}
          </div>
        </div>
        {currentSubmissionStatus === SubmissionDocumentWorkflowState.PENDING_ESIGN && (
          <SubmissionDocumentEsign
            t={t}
            setSignedDocumentUploadID={setSignedDocumentUploadID}
            combinedFileStoreId={combinedFileStoreId}
            setSignedId={setSignedId}
            setIsSignedHeading={setIsSignedHeading}
          />
        )}
      </div>
      {showErrorToast && <Toast error={showErrorToast?.error} label={showErrorToast?.label} isDleteBtn={true} onClose={closeToast} />}
    </Modal>
  );
}

export default ReviewDocumentSubmissionModal;
