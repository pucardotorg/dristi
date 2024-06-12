import React, { useMemo, useState } from "react";
import { AdvocateIcon, LitigentIcon } from "../icons/svgIndex";
import { Button } from "@egovernments/digit-ui-react-components";
import EsignAdharModal from "./EsignAdharModal";
import UploadSignatureModal from "./UploadSignatureModal";

function SignatureCard({ input, data, t, index, onSelect, formData, configKey }) {
  const [openUploadSignatureModal, setOpenUploadSignatureModal] = useState(false);
  const [openAadharModal, setOpenAadharModal] = useState(false);
  const name = `${data?.[input?.config?.title]} ${index}`;
  const uploadModalConfig = useMemo(() => {
    return {
      key: configKey,
      populators: {
        inputs: [
          {
            name: name,
            documentHeader: "Signature",
            type: "DragDropComponent",
            uploadGuidelines: "Ensure the image is not blurry and under 5MB.",
            maxFileSize: 5,
            maxFileErrorMessage: "CS_FILE_LIMIT_50_MB",
            fileTypes: ["JPG", "PNG", "PDF"],
            isMultipleUpload: false,
          },
        ],
        validation: {},
      },
    };
  }, [configKey, name]);

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
  const currentValue = (formData && formData[configKey] && formData[configKey][name]) || [];
  const isSigned = currentValue.length > 0;
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
      {isSigned && <div style={{ width: "inherit", borderRadius: "30px", background: "#E4F2E4", color: "#00703C", padding: "10px" }}>Signed</div>}
      {!isSigned && (
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
      )}
      {openUploadSignatureModal && (
        <UploadSignatureModal
          t={t}
          key={name}
          name={name}
          setOpenUploadSignatureModal={setOpenUploadSignatureModal}
          onSelect={onSelect}
          config={uploadModalConfig}
          formData={formData}
        />
      )}
      {openAadharModal && (
        <EsignAdharModal
          t={t}
          setOpenAadharModal={setOpenAadharModal}
          key={name}
          name={name}
          onSelect={onSelect}
          config={uploadModalConfig}
          formData={formData}
        />
      )}
    </div>
  );
}

export default SignatureCard;
