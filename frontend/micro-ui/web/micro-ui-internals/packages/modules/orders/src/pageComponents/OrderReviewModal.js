import React, { useState } from "react";
import Modal from "../../../dristi/src/components/Modal";
import { CloseSvg } from "@egovernments/digit-ui-components";
function OrderReviewModal({ setShowReviewModal, t, orderList, setSignatureIndex }) {
  const [selectedOrder, setSelectedOrder] = useState(0);
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
      headerBarMain={<Heading label={t("REVIEW_ORDERS_HEADING")} />}
      headerBarEnd={<CloseBtn onClick={() => setShowReviewModal(false)} />}
      actionCancelLabel={t("SAVE_DRAFT")}
      actionCancelOnSubmit={() => {}}
      actionSaveLabel={t("ADD_SIGNATURE")}
      actionSaveOnSubmit={() => {
        setSignatureIndex(0);
      }}
      className={"review-order-modal"}
    >
      <div className="review-order-body-main">
        <div className="review-order-modal-list-div">
          <div>
            {orderList?.map((order, index) => {
              return (
                <div
                  style={{ cursor: "pointer", ...(selectedOrder === index ? { background: "#E8E8E8" } : {}) }}
                  onClick={() => setSelectedOrder(index)}
                >
                  <h1> {order.orderType}</h1>
                </div>
              );
            })}
          </div>
        </div>
        <div className="review-order-modal-document-div">/// document here</div>
      </div>
    </Modal>
  );
}

export default OrderReviewModal;
