import React from "react";
import Modal from "../../../dristi/src/components/Modal";
import CustomSubmitModal from "../../../dristi/src/pages/citizen/FileCase/admission/CustomSubmitModal";
import { FileDownloadIcon } from "../../../dristi/src/icons/svgIndex";
import CustomCopyTextDiv from "../../../dristi/src/components/CustomCopyTextDiv";
import { Banner, CardLabel } from "@egovernments/digit-ui-react-components";
import { useHistory } from "react-router-dom/cjs/react-router-dom.min";

function OrderSucessModal({ order, t, setShowSuccessModal }) {
  const history = useHistory();
  const searchParams = new URLSearchParams(history.location.search);
  searchParams.set("caseId", history.location.state.caseId);
  searchParams.set("tab", history.location.state.tab);

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
        key: "ORDER_ISSUE_DATE",
        value: getFormattedDate(),
        copyData: false,
      },
      {
        key: `${t("ORDER_ID")}:${t(order?.orderType)}`,
        value: order?.id,
        copyData: true,
      },
    ],
  };

  const handleDownloadOrders = () => {
    // setShowSuccessModal(false);
    // history.push(`/${window.contextPath}/employee/dristi/home/view-case?${searchParams.toString()}`, { from: "orderSuccessModal" });
  };

  const handleClose = () => {
    setShowSuccessModal(false);
    history.push(`/${window.contextPath}/employee/dristi/home/view-case?${searchParams.toString()}`, { from: "orderSuccessModal" });
  };

  return (
    <Modal
      actionCancelLabel={t("DOWNLOAD_ORDER")}
      actionCancelOnSubmit={handleDownloadOrders}
      actionSaveLabel={t("CS_COMMON_CLOSE")}
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
