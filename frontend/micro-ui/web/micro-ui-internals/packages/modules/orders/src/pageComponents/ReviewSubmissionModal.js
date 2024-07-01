import React from "react";
import Modal from "../../../dristi/src/components/Modal";
import { CloseSvg } from "@egovernments/digit-ui-components";
function ReviewSubmissionModal({ setShowReviewModal, t, setShowsignatureModal, handleBack }) {
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
        <div className="review-submission-appl-modal-info-div">
          <div className="keys-div">
            <h2> {t("APPLICATION_TYPE")}</h2>
            <h2> {t("SUBMISSION_DATE")}</h2>
            <h2> {t("SENDER")}</h2>
            <h2> {t("ADDITIONAL_DETAILS")}</h2>
          </div>
          <div className="values-div">
            <h2> {"Extension Request"}</h2>
            <h2> {"28 Mar’ 24, 09:15am"}</h2>
            <h2> {"Sukeerth Reddy on behalf of Aparna B."}</h2>
            <h2> {"This can help strengthen the case against the accused."}</h2>
          </div>
        </div>
        <div className="review-submission-appl-modal-doc-div">/// document hereee</div>
      </div>
    </Modal>
  );
}

export default ReviewSubmissionModal;
