import React from "react";
import Modal from "../../../dristi/src/components/Modal";
import CustomCopyTextDiv from "../../../dristi/src/components/CustomCopyTextDiv";
import { FileDownloadIcon } from "../../../dristi/src/icons/svgIndex";
import { Banner, CardLabel } from "@egovernments/digit-ui-react-components";

function SubmissionDocumentSuccessModal({ documentSubmissionNumber, t, handleSuccessDownloadSubmission, handleClose }) {
  const getFormattedDate = () => {
    const currentDate = new Date();
    const year = String(currentDate.getFullYear()).slice(-2);
    const month = String(currentDate.getMonth() + 1).padStart(2, "0");
    const day = String(currentDate.getDate()).padStart(2, "0");
    return `${month}/${day}/${year}`;
  };
  const submissionModalInfo = {
    header: "DOCUMENT_SUBMISSION_SUCCESSFUL",
    subHeader: "",
    caseInfo: [
      {
        key: t("DOCUMENT_SUBMISSION_DATE"),
        value: getFormattedDate(),
        copyData: false,
      },
      {
        key: `${t("DOCUMENT_SUBMISSION_FILING_ID")}`,
        value: documentSubmissionNumber,
        copyData: true,
      },
    ],
  };

  return (
    <Modal
      actionCancelLabel={t("DOWNLOAD_DOCUMENT_SUBMISSION")}
      actionCancelOnSubmit={handleSuccessDownloadSubmission}
      actionSaveLabel={t("DOCUMENT_SUBMISSION_NEXT")}
      actionSaveOnSubmit={handleClose}
      className={"orders-success-modal"}
      popupStyles={{ width: "50%" }}
      cancelButtonBody={<FileDownloadIcon></FileDownloadIcon>}
    >
      <div style={{ padding: "0px 0px 45px 0px" }}>
        <div>
          <Banner
            whichSvg={"tick"}
            successful={true}
            message={t(submissionModalInfo?.header)}
            headerStyles={{ fontSize: "32px" }}
            style={{ minWidth: "100%" }}
          ></Banner>
          {submissionModalInfo?.subHeader && <CardLabel>{t(submissionModalInfo?.subHeader)}</CardLabel>}
          {
            <CustomCopyTextDiv
              t={t}
              keyStyle={{ margin: "8px 0px" }}
              valueStyle={{ margin: "8px 0px", fontWeight: 700 }}
              data={submissionModalInfo?.caseInfo}
            />
          }
        </div>
      </div>
    </Modal>
  );
}

export default SubmissionDocumentSuccessModal;
