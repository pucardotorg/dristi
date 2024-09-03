import React from "react";
import Modal from "../../../dristi/src/components/Modal";
import { FileDownloadIcon } from "../../../dristi/src/icons/svgIndex";
import CustomCopyTextDiv from "../../../dristi/src/components/CustomCopyTextDiv";
import { Banner, CardLabel } from "@egovernments/digit-ui-react-components";

function OrderSucessModal({ order, t, handleDownloadOrders, handleClose, actionSaveLabel }) {
  const getFormattedDate = () => {
    const currentDate = new Date();
    const year = String(currentDate.getFullYear()).slice(-2);
    const month = String(currentDate.getMonth() + 1).padStart(2, "0");
    const day = String(currentDate.getDate()).padStart(2, "0");
    return `${month}/${day}/${year}`;
  };
  const orderModalInfo = {
    header: "CS_ORDER_SUCCESSFULLY_ISSUED",
    subHeader: "CS_ORDER_CREATED_SUBTEXT",
    caseInfo: [
      {
        key: t("ORDER_ISSUE_DATE"),
        value: getFormattedDate(),
        copyData: false,
      },
      {
        key: `${t("ORDER_ID")} : ${t("ORDER_TYPE_" + order?.orderType)}`,
        value: order?.orderNumber,
        copyData: true,
      },
    ],
  };

  return (
    <Modal
      actionCancelLabel={t("DOWNLOAD_ORDER")}
      actionCancelOnSubmit={handleDownloadOrders}
      actionSaveLabel={actionSaveLabel}
      actionSaveOnSubmit={handleClose}
      className={"orders-success-modal"}
      cancelButtonBody={<FileDownloadIcon></FileDownloadIcon>}
    >
      <div style={{ padding: "8px 24px" }}>
        <div>
          <Banner
            whichSvg={"tick"}
            successful={true}
            message={t(orderModalInfo?.header)}
            headerStyles={{ fontSize: "32px" }}
            style={{ minWidth: "100%", marginTop: "10px" }}
          ></Banner>
          {orderModalInfo?.subHeader && <CardLabel>{t(orderModalInfo?.subHeader)}</CardLabel>}
          {
            <CustomCopyTextDiv
              t={t}
              keyStyle={{ margin: "8px 0px" }}
              valueStyle={{ margin: "8px 0px", fontWeight: 700 }}
              data={orderModalInfo?.caseInfo}
            />
          }
        </div>
      </div>
    </Modal>
  );
}

export default OrderSucessModal;
