import React, { useState } from "react";
import Modal from "../../../dristi/src/components/Modal";
import { CloseSvg } from "@egovernments/digit-ui-components";

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

function ReviewSubmissionModal({
  applicationType,
  submissionDate,
  sender,
  additionalDetails,
  setShowReviewModal,
  t,
  setShowsignatureModal,
  handleBack,
  documents,
}) {
  const DocViewerWrapper = window?.Digit?.ComponentRegistryService?.getComponent("DocViewerWrapper");

  const [documentList, setDocumentList] = useState([]);
  return (
    <Modal
      headerBarMain={<Heading label={t("REVIEW_SUBMISSION_APPLICATION_HEADING")} />}
      headerBarEnd={<CloseBtn onClick={() => setShowReviewModal(false)} />}
      actionCancelLabel={t("CS_COMMON_BACK")}
      actionCancelOnSubmit={handleBack}
      actionSaveLabel={t("ADD_SIGNATURE")}
      actionSaveOnSubmit={() => {
        setShowsignatureModal(true);
        setShowReviewModal(false);
      }}
      className={"review-submission-appl-modal"}
    >
      <div className="review-submission-appl-body-main">
        <div className="application-details">
          <div className="application-info">
            <div className="info-row">
              <div className="info-key">
                <h3>{t("APPLICATION_TYPE")}</h3>
              </div>
              <div className="info-value">
                <h3>{t(applicationType)}</h3>
              </div>
            </div>
            <div className="info-row">
              <div className="info-key">
                <h3>{t("SUBMISSION_DATE")}</h3>
              </div>
              <div className="info-value">
                <h3>{t(submissionDate)}</h3>
              </div>
            </div>
            <div className="info-row">
              <div className="info-key">
                <h3>{t("SENDER")}</h3>
              </div>
              <div className="info-value">
                <h3>{t(sender)}</h3>
              </div>
            </div>
            <div className="info-row">
              <div className="info-key">
                <h3>{t("ADDITIONAL_DETAILS")}</h3>
              </div>
              <div className="info-value">
                <h3>{t(additionalDetails)}</h3>
              </div>
            </div>
          </div>
          <div className="application-view">
            {documentList.map((docs) => (
              <DocViewerWrapper
                key={docs.fileStoreId}
                fileStoreId={docs.fileStoreId}
                displayFilename={docs.fileName}
                tenantId={docs.tenantId}
                docWidth="100%"
                docHeight="unset"
                showDownloadOption={false}
                documentName={docs.fileName}
              />
            ))}
          </div>
        </div>
      </div>
    </Modal>
  );
}

export default ReviewSubmissionModal;
