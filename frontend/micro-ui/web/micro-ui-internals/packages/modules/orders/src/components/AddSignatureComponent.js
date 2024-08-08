import { InfoCard } from "@egovernments/digit-ui-components";
import { FileUploadIcon } from "@egovernments/digit-ui-module-dristi/src/icons/svgIndex";
import { Button } from "@egovernments/digit-ui-react-components";
import React from "react";

const AddSignatureComponent = ({ t, isSigned, handleSigned }) => {
  return (
    <div className="add-signature-main-div">
      <InfoCard
        variant={"default"}
        label={t("PLEASE_NOTE")}
        additionalElements={[
          <p>
            {t("YOU_ARE_ADDING_YOUR_SIGNATURE_TO_THE")} <span style={{ fontWeight: "bold" }}>{t("Summons Document")}</span>
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
                handleSigned(true);
              }}
              className={"aadhar-sign-in"}
              labelClassName={"aadhar-sign-in"}
            />
            <Button
              icon={<FileUploadIcon />}
              label={t("UPLOAD_DIGITAL_SIGN_CERTI")}
              onButtonClick={() => {
                // setOpenUploadSignatureModal(true);
                handleSigned(true);
              }}
              className={"upload-signature"}
              labelClassName={"upload-signature-label"}
            />
          </div>
          <div className="donwload-submission">
            <h2>{t("WANT_TO_DOWNLOAD")}</h2>
            <span style={{ color: "#007E7E", cursor: "pointer", textDecoration: "underline" }}>{t("CLICK_HERE")}</span>
          </div>
        </div>
      ) : (
        <div className="signed">
          <h1>{t("YOUR_SIGNATURE")}</h1>
          <h2>{t("SIGNED")}</h2>
        </div>
      )}
    </div>
  );
};

export default AddSignatureComponent;
