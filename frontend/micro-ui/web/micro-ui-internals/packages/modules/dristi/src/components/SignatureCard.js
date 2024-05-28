import React from "react";
import { AdvocateIcon, LitigentIcon } from "../icons/svgIndex";
import { Button } from "@egovernments/digit-ui-react-components";

function SignatureCard({ input, data, setOpenAadharModal, setOpenUploadSignatureModal, t }) {
  const Icon = ({ icon }) => {
    switch (icon) {
      case "LitigentIcon":
        return <LitigentIcon />;
      case "AdvocateIcon":
        return <AdvocateIcon />;
      default:
        return <LitigentIcon />;
    }
  };

  return (
    <div style={{ display: "flex", alignItems: "center", justifyContent: "space-between" }}>
      <div
        style={{
          display: "flex",
          alignItems: "center",
          justifyContent: "flex-start",
          gap: "20px",
          paddingTop: "10px",
          paddingBottom: "10px",
        }}
      >
        {input?.icon && <Icon icon={input?.icon} />}
        {data?.[input?.config?.title]}
      </div>
      <div style={{ display: "flex", gap: "20px", alignItems: "center", justifyContent: "space-between" }}>
        <Button
          label={t("CS_UPLOAD_ESIGNATURE")}
          onButtonClick={() => {
            setOpenUploadSignatureModal(true);
          }}
        ></Button>
        <Button
          label={t("CS_ESIGN_AADHAR")}
          onButtonClick={() => {
            setOpenAadharModal(true);
          }}
        ></Button>
      </div>
    </div>
  );
}

export default SignatureCard;
