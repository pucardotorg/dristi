import React from "react";
import Modal from "../../../dristi/src/components/Modal";
import CustomSubmitModal from "../../../dristi/src/pages/citizen/FileCase/admission/CustomSubmitModal";

function OrderSucessModal({ order, t }) {
  const getFormattedDate = () => {
    const currentDate = new Date();
    const year = String(currentDate.getFullYear()).slice(-2);
    const month = String(currentDate.getMonth() + 1).padStart(2, "0");
    const day = String(currentDate.getDate()).padStart(2, "0");
    return `${month}/${day}/${year}`;
  };
  const orderModalInfo = {
    header: t("CS_ORDER_SUCCESSFULLY_ISSUED"),
    subHeader: "CS_ORDER_CREATED_SUBTEXT",
    caseInfo: [
      {
        key: "ORDER_ISSUE_DATE",
        value: getFormattedDate(),
      },
      {
        key: `${t("ORDER_ID")}:${t(order.orderType)}`,
        value: order.id,
        showCopy: true,
      },
    ],
    isArrow: false,
    showCopytext: true,
  };

  return (
    <Modal
      actionCancelLabel={t("DOWNLOAD_ORDER")}
      actionCancelOnSubmit={() => {}}
      actionSaveLabel={t("CLOSE")}
      actionSaveOnSubmit={() => {}}
      className={"orders-success-modal"}
    >
      <CustomSubmitModal t={t} submitModalInfo={orderModalInfo} />
    </Modal>
  );
}

export default OrderSucessModal;
