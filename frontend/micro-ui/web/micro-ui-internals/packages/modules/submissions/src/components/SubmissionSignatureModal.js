import { Button, CloseSvg } from "@egovernments/digit-ui-components";
import React, { useState } from "react";
import Modal from "../../../dristi/src/components/Modal";

function SubmissionSignatureModal({ t, handleProceed, handleCloseSignaturePopup }) {
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
      headerBarEnd={<CloseBtn onClick={() => handleCloseSignaturePopup()} />}
      actionCancelLabel={t("BACK")}
      actionCancelOnSubmit={() => handleCloseSignaturePopup()}
      actionSaveLabel={t("PROCEED")}
      isDisabled={!isSigned}
      actionSaveOnSubmit={() => {
        handleProceed();
      }}
      className={"submission-add-signature-modal"}
    >
      <div className="add-signature-main-div">
        {!isSigned ? (
          <div className="not-signed">
            <h1>{t("YOUR_SIGNATURE")}</h1>
            <div className="buttons-div">
              <Button
                // icon={<FileUploadIcon />}
                label={t("CS_UPLOAD_ESIGNATURE")}
                onClick={() => {
                  // setOpenUploadSignatureModal(true);
                  setIsSigned(true);
                }}
                className={"upload-signature"}
                labelClassName={"submission-upload-signature-label"}
              ></Button>
              <Button
                label={t("CS_ESIGN_AADHAR")}
                onClick={() => {
                  // setOpenAadharModal(true);
                  setIsSigned(true);
                }}
                className={"aadhar-sign-in"}
                labelClassName={"submission-aadhar-sign-in"}
              ></Button>
            </div>
            <div className="click-for-download">
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

export default SubmissionSignatureModal;
