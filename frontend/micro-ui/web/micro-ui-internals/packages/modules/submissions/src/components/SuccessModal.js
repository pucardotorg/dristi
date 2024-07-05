import React from "react";
import Modal from "../../../dristi/src/components/Modal";
import { CloseSvg } from "@egovernments/digit-ui-components";
import SelectCustomNote from "../../../dristi/src/components/SelectCustomNote";
import { Banner } from "@egovernments/digit-ui-react-components";
import CustomCopyTextDiv from "../../../dristi/src/components/CustomCopyTextDiv";

const customNoteConfig = {
  populators: {
    inputs: [
      {
        infoHeader: "PLEASE_NOTE",
        infoText: "YOU_HAVE_TO_MAKE_PAYMENT",
        // infoTooltipMessage: "CS_NOTE_TOOLTIP",
        showTooltip: true,
      },
    ],
  },
};

function SuccessModal({ t, handleDownloadSubmission, isPaymentDone = false, handleCloseSuccessModal, handlePendingPayment }) {
  const submissionData = [
    { key: "SUBMISSION_DATE", value: "04/04/24", copyData: false },
    { key: "SUBMISSION_ID", value: "KA454894943", copyData: true },
  ];
  return (
    <Modal
      actionCancelLabel={t("SKIP")}
      actionCancelOnSubmit={() => handleDownloadSubmission()}
      actionSaveLabel={t("CS_MAKE_PAYMENT")}
      actionSaveOnSubmit={
        isPaymentDone
          ? () => {
              handleCloseSuccessModal();
            }
          : () => {
              handlePendingPayment();
            }
      }
      className={"submission-success-modal"}
    >
      <div className="submission-success-modal-body-main">
        <Banner
          whichSvg={"tick"}
          successful={true}
          message={t("SUBMISSION_SUCCESSFUL")}
          headerStyles={{ fontSize: "32px" }}
          style={{ minWidth: "100%", marginTop: "10px" }}
        ></Banner>
        {isPaymentDone && <SelectCustomNote t={t} config={customNoteConfig} />}
        <CustomCopyTextDiv
          t={t}
          keyStyle={{ margin: "8px 0px" }}
          valueStyle={{ margin: "8px 0px", fontWeight: 700 }}
          data={submissionData}
          tableDataClassName={"e-filing-table-data-style"}
          tableValueClassName={"e-filing-table-value-style"}
        />
      </div>
    </Modal>
  );
}

export default SuccessModal;
