import React, { useMemo, useState } from "react";
import { AdvocateIcon, FileUploadIcon, LitigentIcon } from "../icons/svgIndex";
import EsignAdharModal from "./EsignAdharModal";
import UploadSignatureModal from "./UploadSignatureModal";
import Button from "./Button";

function SignatureCard({ input, data, t, index, onSelect, formData, configKey, handleAadharClick }) {
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
            maxFileErrorMessage: "CS_FILE_LIMIT_5_MB",
            fileTypes: ["JPG", "PNG", "JPEG", "PDF"],
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
    <div className="signature-body">
      <div className="icon-and-title">
        {input?.icon && <Icon icon={input?.icon} />}
        <h3 className="signature-title">{data?.[input?.config?.title]}</h3>
      </div>
      {isSigned && <span className="signed">{t("SIGNED")}</span>}
      {!isSigned && (
        <div className="signed-button-group">
          <Button
            icon={<FileUploadIcon />}
            label={t("CS_UPLOAD_ESIGNATURE")}
            onButtonClick={() => {
              setOpenUploadSignatureModal(true);
            }}
            className={"upload-signature"}
            labelClassName={"upload-signature-label"}
          ></Button>
          <Button
            label={t("CS_ESIGN_AADHAR")}
            onButtonClick={() => {
              handleAadharClick(data, name);
            }}
            className={"aadhar-sign-in"}
            labelClassName={"aadhar-sign-in"}
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
