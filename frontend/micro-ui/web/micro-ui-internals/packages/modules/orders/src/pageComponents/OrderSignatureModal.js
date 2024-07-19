import { CloseSvg, InfoCard } from "@egovernments/digit-ui-components";
import React, { useState } from "react";
import Modal from "../../../dristi/src/components/Modal";
import { Button } from "@egovernments/digit-ui-react-components";
import { FileUploadIcon } from "../../../dristi/src/icons/svgIndex";

function OrderSignatureModal({ t, order, handleIssueOrder, handleGoBackSignatureModal }) {
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
      headerBarEnd={<CloseBtn onClick={handleGoBackSignatureModal} />}
      actionCancelLabel={t("CS_COMMON_BACK")}
      actionCancelOnSubmit={handleGoBackSignatureModal}
      actionSaveLabel={t("ISSUE_ORDER")}
      isDisabled={!isSigned}
      actionSaveOnSubmit={() => {
        handleIssueOrder();
      }}
      className={"add-signature-modal"}
    >
      <div className="add-signature-main-div">
        <InfoCard
          variant={"default"}
          label={t("PLEASE_NOTE")}
          additionalElements={[
            <p>
              {t("YOU_ARE_ADDING_YOUR_SIGNATURE_TO_THE")} <span style={{ fontWeight: "bold" }}>{t(order?.orderType)}</span>
            </p>,
          ]}
          inline
          textStyle={{}}
          className={`custom-info-card`}
        />

        {!isSigned ? (
          <div className="not-signed">
            <h1>{t("YOUR_SIGNATURE")}</h1>
            <div className="sign-button-wrap">
              <Button
                label={t("CS_ESIGN")}
                onButtonClick={() => {
                  // setOpenAadharModal(true);
                  setIsSigned(true);
                }}
                className={"aadhar-sign-in"}
                labelClassName={"aadhar-sign-in"}
              />
              <Button
                icon={<FileUploadIcon />}
                label={t("UPLOAD_DIGITAL_SIGN_CERTI")}
                onButtonClick={() => {
                  // setOpenUploadSignatureModal(true);
                  setIsSigned(true);
                }}
                className={"upload-signature"}
                labelClassName={"upload-signature-label"}
              />
            </div>
            <div className="donwload-submission">
              <h2>{t("WANT_TO_DOWNLOAD")}</h2>
              <span>
                <a href="">{t("CLICK_HERE")}</a>
              </span>
            </div>
          </div>
        ) : (
          <div className="signed">
            <h1>{t("YOUR_SIGNATURE")}</h1>
            <h2>{t("SIGNED")}</h2>
          </div>
        )}
      </div>
    </Modal>
  );
}

export default OrderSignatureModal;
