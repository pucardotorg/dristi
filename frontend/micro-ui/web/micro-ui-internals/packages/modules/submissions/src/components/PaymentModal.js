import React from "react";
import Modal from "../../../dristi/src/components/Modal";
import { CloseSvg } from "@egovernments/digit-ui-components";
import SelectCustomNote from "../../../dristi/src/components/SelectCustomNote";

const customNoteConfig = {
  populators: {
    inputs: [
      {
        infoHeader: "INFO",
        infoText: "VISIT_NYAYMITRA_FOR_OFFLINE_PAYMENT",
        // infoTooltipMessage: "CS_NOTE_TOOLTIP",
        showTooltip: true,
      },
    ],
  },
};

function PaymentModal({ t, handleClosePaymentModal, handleSkipPayment, handleMakePayment }) {
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
      headerBarMain={<Heading label={t("SUBMISSION_APPLICATION_PAYMENT")} />}
      headerBarEnd={<CloseBtn onClick={handleClosePaymentModal} />}
      actionCancelLabel={t("SKIP")}
      actionCancelOnSubmit={() => handleSkipPayment()}
      actionSaveLabel={t("CS_MAKE_PAYMENT")}
      actionSaveOnSubmit={() => {
        handleMakePayment();
      }}
      className={"submission-payment-modal"}
    >
      <div className="submission-payment-modal-body-main">
        <div className="note-div">
          <SelectCustomNote t={t} config={customNoteConfig}></SelectCustomNote>
        </div>
        <div className="submission-payment-modal-amount-div">
          <div className="amount-div">
            <div className="keys-div">
              <h2> {t("AMOUNT_DUE")}</h2>
              <h2> {t("COURT_FEES")}</h2>
              <h2> {t("ADVOCATE_FEES")}</h2>
            </div>
            <div className="values-div">
              <h2> {"Rs 600"}</h2>
              <h2> {"Rs 400"}</h2>
              <h2> {"Rs 1000"}</h2>
            </div>
          </div>
          <div className="total-amount-div">
            <h1>{t("TOTAL_FEES")}</h1>
            <h2>{"Rs 2000"}</h2>
          </div>
        </div>
      </div>
    </Modal>
  );
}

export default PaymentModal;
