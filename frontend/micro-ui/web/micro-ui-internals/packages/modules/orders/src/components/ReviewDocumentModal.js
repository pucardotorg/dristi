import React from "react";
import { CardText, CloseSvg, Modal } from "@egovernments/digit-ui-react-components";
import { useTranslation } from "react-i18next";

const Heading = (props) => {
  return <h1 className="heading-m">{props.label}</h1>;
};

const CloseBtn = (props) => {
  return (
    <div onClick={props.onClick} className="close-button-style">
      <CloseSvg />
    </div>
  );
};

const popUpStyle = {
  width: "fit-content",
  height: "fit-content",
  borderRadius: "0.3rem",
};

const ReviewDocumentModal = ({ handleClose }) => {
  const { t } = useTranslation();
  return (
    <div>
      <Modal
        popupStyles={popUpStyle}
        headerBarMain={<Heading label={"Review Document: Summons Document"} />}
        headerBarEnd={<CloseBtn onClick={handleClose} />}
        actionSaveLabel={t("E-Sign")}
      >
        <div style={{ height: "620px" }}>
          <div className="document-info-container">
            <div className="document-info-row">
              <div className="document-info-heading">Issued to</div>
              <div>Vikram Singh</div>
              <div className="view-order-right">View Order</div>
            </div>
            <div className="document-info-row">
              <div className="document-info-heading">Issued Date</div>
              <div>23/04/2024</div>
            </div>
            <div className="document-info-row">
              <div className="document-info-heading">Next Hearing Date</div>
              <div>04/07/2024</div>
            </div>
            <div className="document-info-row">
              <div className="document-info-heading">Amount Paid</div>
              <div>Rs. 15/-</div>
            </div>
            <div className="document-info-row">
              <div className="document-info-heading">Channel Details</div>
              <div>Physical Post </div>
            </div>
          </div>
          <div className="document-preview">Document preview Space</div>
        </div>
      </Modal>
    </div>
  );
};

export default ReviewDocumentModal;
