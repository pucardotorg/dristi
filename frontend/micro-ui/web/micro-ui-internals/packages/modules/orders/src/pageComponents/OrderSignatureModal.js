import { Button, CloseSvg } from "@egovernments/digit-ui-components";
import React from "react";
import Modal from "../../../dristi/src/components/Modal";

function OrderSignatureModal({ t }) {
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
      headerBarMain={<Heading label={`${t("ADD_SIGNATURE")} (${1})`} />}
      headerBarEnd={<CloseBtn onClick={() => handleCloseSignaturePopup()} />}
      actionCancelLabel={t("BACK")}
      actionCancelOnSubmit={() => handleCloseSignaturePopup()}
      actionSaveLabel={false ? t("ISSUE_ORDERS") : t("NEXT")}
      actionSaveOnSubmit={() => {}}
      className={"add-signature-modal"}
    >
      <div className="add-signature-main-div">
        <div className="note-div">
          <div className="icon-div">
            {/* <SmallInfoIcon></SmallInfoIcon> */}
            <span>PLEASE_NOTE</span>
          </div>
          <h2>YOU_ARE_ADDING_YOUR_SIGNATURE_TO_THE</h2> <span style={{ fontWeight: "bold" }}>{"order type here"}</span>
        </div>
        {true ? (
          <div className="not-signed">
            <h1>YOUR_SIGNATURE</h1>
            <div className="signature-button-div">
              <Button label={t("E_SIGN")} style={{ alignItems: "center" }} onButtonClick={() => {}} />
              <div>
                <span> {/* <UploadIcon></UploadIcon> */}</span>
                <h1>{t("UPLOAD_DIGITAL_SIGNATURE_CERTIFICATE")}</h1>
              </div>
            </div>
            <div>
              <h2>{t("WANT_TO_DOWNLOAD")}</h2>
              <span>
                <a href="">{t("CLICK_HERE")}</a>
              </span>
            </div>
          </div>
        ) : (
          <div className="signed">
            <h1>{t("YOUR_SIGNATURE")}</h1>
            <span>{t("SIGNED")}</span>
          </div>
        )}
      </div>
    </Modal>
  );
}

export default OrderSignatureModal;
