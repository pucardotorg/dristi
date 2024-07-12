import React from "react";
import { CloseSvg, FileIcon, Modal, PrintIcon } from "@egovernments/digit-ui-react-components";
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

const PrintAndSendDocumentModal = ({ handleClose }) => {
  const { t } = useTranslation();
  return (
    <div>
      <Modal
        popupStyles={popUpStyle}
        headerBarMain={<Heading label={t("Print & Send Documents")} />}
        headerBarEnd={<CloseBtn onClick={handleClose} />}
        actionSaveLabel={t("Mark As Sent")}
      >
        <div style={{ height: "310px" }}>
          <div className="print-documents-container">
            <div className="print-documents-heading">Print Documents</div>
            <div className="print-documents-box">
              <div className="print-documents-box-text">
                <FileIcon />
                <div style={{ marginLeft: "0.5rem" }}>Summons Document</div>
              </div>
              <button className="print-button">
                <PrintIcon />
                <div style={{ marginLeft: "0.5rem" }}>Print</div>
              </button>
            </div>
          </div>
          <div className="document-details-container">
            <div className="document-details-row">
              <div className="document-details-row-heading">Issued to</div>
              <div>Vikram Singh</div>
              <div className="view-order-right">View Order</div>
            </div>
            <div className="document-details-rows">
              <div className="document-details-row-heading">Issued Date</div>
              <div>23/04/2024</div>
            </div>
            <div className="document-details-rows">
              <div className="document-details-row-heading">Next Hearing Date</div>
              <div>04/07/2024</div>
            </div>
            <div className="document-details-rows">
              <div className="document-details-row-heading">Channel Details</div>
              <div>Physical Post </div>
            </div>
          </div>
        </div>
      </Modal>
    </div>
  );
};

export default PrintAndSendDocumentModal;
