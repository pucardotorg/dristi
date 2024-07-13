import { Button, CloseSvg, Stepper } from "@egovernments/digit-ui-components";
import React, { useState } from "react";
import Modal from "../../../dristi/src/components/Modal";
const SmallInfoIcon = () => (
  <svg width="16" height="16" viewBox="0 0 20 20" fill="none" xmlns="http://www.w3.org/2000/svg">
    <path d="M10 0C4.48 0 0 4.48 0 10C0 15.52 4.48 20 10 20C15.52 20 20 15.52 20 10C20 4.48 15.52 0 10 0ZM11 15H9V9H11V15ZM11 7H9V5H11V7Z" fill="#0F3B8C"/>
  </svg>
);
function OrderSignatureModal({ t, order, handleIssueOrder, closeModal, handleStepper, stepper }) {
  const [isSigned, setIsSigned] = useState(false);
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
      headerBarMain={<Heading label={t("ADD_SIGNATURE")} />}
      headerBarEnd={<CloseBtn onClick={() => closeModal()} />}
      actionCancelLabel={t("BACK")}
      actionCancelOnSubmit={() => handleStepper(-1)}
      actionSaveLabel={stepper ===1?t("Next"): t("ISSUE_ORDER")}
      isDisabled={!isSigned}
      actionSaveOnSubmit={() => {
        debugger;
        if(stepper ===2)
        handleIssueOrder();
        handleStepper(1);
      }}
      className={"add-signature-modal"}
    >
      <div className="add-signature-main-div">
          <div className="note-div" style={{ backgroundColor: "#ECF3FD", padding: "10px" }}> 
          <div className="icon-div" style={{display : 'flex',flexDirection :'row' ,gap:"5px", alignItems: 'center'}}>
            <SmallInfoIcon />
            <h2>
              <strong>{t("PLEASE_NOTE")}</strong>
            </h2>
          </div>
          <h2>
            {t("You are adding your signature to the order for")} <strong>{order?.orderType}</strong>
          </h2>
        </div>
        {stepper === 1 ? (
          <div className="not-signed">
            <h1>
              <strong>{t("YOUR_SIGNATURE")}</strong>
            </h1>
            <div className="sign-div " style={{ display: "flex", flexDirection: "row", justifyContent: "space-between" }}>
              <Button
                // icon={<FileUploadIcon />}
                label={t("CS_UPLOAD_ESIGNATURE")}
                onClick={() => {
                  // setOpenUploadSignatureModal(true);
                  setIsSigned(true);
                }}
                className={"upload-signature"}
                labelClassName={"upload-signature-label"}
              ></Button>
              <Button
                label={t("CS_ESIGN_AADHAR")}
                onClick={() => {
                  // setOpenAadharModal(true);
                  setIsSigned(true);
                }}
                className={"aadhar-sign-in"}
                labelClassName={"aadhar-sign-in"}
              ></Button>
            </div>
            <div>
              <h2>
                {t("WANT_TO_DOWNLOAD")}{" "}
                <span>
                  <a href="">{t("CLICK_HERE")}</a>
                </span>
              </h2>
            </div>
          </div>
        ) : (
          <div className="signed">
            <h1><strong>{t("YOUR_SIGNATURE")}</strong></h1>
            <span style={{color:"green"}}>{t("SIGNED")}</span>
          </div>
        )}
      </div>
    </Modal>
  );
}

export default OrderSignatureModal;
