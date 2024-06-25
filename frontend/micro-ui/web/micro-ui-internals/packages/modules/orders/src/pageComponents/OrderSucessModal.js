import React, { useMemo } from "react";
import Modal from "../../../dristi/src/components/Modal";
import CustomSubmitModal from "../../../dristi/src/pages/citizen/FileCase/admission/CustomSubmitModal";

function OrderSucessModal({ orderList, t }) {
  const getFormattedDate = () => {
    const currentDate = new Date();
    const year = String(currentDate.getFullYear()).slice(-2);
    const month = String(currentDate.getMonth() + 1).padStart(2, "0");
    const day = String(currentDate.getDate()).padStart(2, "0");
    return `${month}/${day}/${year}`;
  };
  const dateinfo = [
    {
      key: "ORDER_ISSUE_DATE",
      value: getFormattedDate(),
    },
  ];
  const orderinfo = useMemo(
    () =>
      orderList?.map((order, index) => {
        return {
          key: `${t("ORDER_ID")} ${index + 1}: ${t(order.orderType)}`,
          value: order.id,
          showCopy: true,
        };
      }) || [],
    [orderList]
  );

  const mockSubmitModalInfo = {
    header: `${t("CS_ORDER_SUCCESSFULLY_ISSUED")} ${orderList?.length || 0} ${t("CS_ORDERS")}`,
    subHeader: "CS_ORDER_CREATED_SUBTEXT",
    caseInfo: [...dateinfo, ...orderinfo],
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
      <CustomSubmitModal t={t} submitModalInfo={mockSubmitModalInfo} />
    </Modal>
  );
}

export default OrderSucessModal;
