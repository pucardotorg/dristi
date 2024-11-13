import React from "react";
import Modal from "./Modal";
import CustomSubmitModal from "./CustomSubmitModal";

function SubmissionSuccessModal({ t, handleBack }) {
  return (
    <Modal
      actionCancelLabel={t("BACK_TO_HOME")}
      actionSaveLabel={t("NEXT_PENDING_TASK")}
      actionCancelOnSubmit={handleBack}
      actionSaveOnSubmit={handleBack}
    >
      <CustomSubmitModal t={t} submitModalInfo={{ header: t("SUCCESS_REVIEW_SUBMISSION"), subHeader: t("SUBTEXT_SUCCESS_REVIEW_SUBMISSION") }} />
    </Modal>
  );
}

export default SubmissionSuccessModal;
