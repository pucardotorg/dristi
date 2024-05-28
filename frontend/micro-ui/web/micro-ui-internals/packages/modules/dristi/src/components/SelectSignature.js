import React, { useMemo, useState } from "react";
import SignatureCard from "./SignatureCard";
import EsignAdharModal from "./EsignAdharModal";
import UploadSignatureModal from "./UploadSignatureModal";

function SelectSignature({ t, config, onSelect, formData = {}, errors }) {
  const inputs = useMemo(
    () =>
      config?.populators?.inputs || [
        {
          key: "complaintDetails",
          label: "CS_COMPLAINT_DETAILS",
          icon: "LitigentIcon",
          config: [{ type: "title", value: "name" }],
          data: [{ name: "Sheetal Arora" }, { name: "Mehul Das" }],
        },
      ],
    [config?.populators?.inputs]
  );
  const [openAadharModal, setOpenAadharModal] = useState(false);
  const [openUploadSignatureModal, setOpenUploadSignatureModal] = useState(false);

  const uploadModalConfig = {
    key: "litigentsignature",
    populators: {
      inputs: [
        {
          name: "signature",
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
  return (
    <div>
      {inputs.map((input, inputIndex) => (
        <div>
          <div style={{ paddingTop: "10px", paddingBottom: "10px", fontWeight: 700, fontSize: "24px", color: "#3D3C3C" }}>{config?.label}</div>
          {input.data.map((item, itemIndex) => (
            <SignatureCard
              key={inputIndex + itemIndex}
              data={item}
              input={input}
              setOpenAadharModal={setOpenAadharModal}
              setOpenUploadSignatureModal={setOpenUploadSignatureModal}
              t={t}
            />
          ))}
        </div>
      ))}
      {openAadharModal && <EsignAdharModal t={t} setOpenAadharModal={setOpenAadharModal} />}
      {openUploadSignatureModal && (
        <UploadSignatureModal
          t={t}
          setOpenUploadSignatureModal={setOpenUploadSignatureModal}
          onSelect={onSelect}
          config={uploadModalConfig}
          formData={formData}
        />
      )}
    </div>
  );
}

export default SelectSignature;
