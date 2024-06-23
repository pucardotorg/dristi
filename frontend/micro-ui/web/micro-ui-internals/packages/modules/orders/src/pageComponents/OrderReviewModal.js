import React from "react";
import Modal from "../../../dristi/src/components/Modal";
import { CloseSvg } from "@egovernments/digit-ui-components";
function OrderReviewModal({ setShowReviewModal, formdata, t, currentSelectedOrderIndex }) {
  const orderTypes = [{ name: "order for doc submission" }, { name: "order for summons" }];
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
      actionSaveOnSubmit={() => {}}
      className={"review-order-modal"}
    >
      <div className="review-order-body-main">
        <div className="review-order-modal-list-div">
          <div>
            {orderTypes.map((orderType, index) => {
              return (
                <div
                  style={{ cursor: "pointer", ...(currentSelectedOrderIndex === index ? { background: "#E8E8E8" } : {}) }}
                  onClick={() => updateCurrentSelectedOrderInReview(index)}
                >
                  <h1> {orderType?.name}</h1>
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
