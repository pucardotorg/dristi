import { Button, CloseSvg } from "@egovernments/digit-ui-components";
import React, { useEffect, useMemo, useState } from "react";
import Modal from "../../../dristi/src/components/Modal";
import { Urls } from "../hooks/services/Urls";
import { FileUploadIcon } from "../../../dristi/src/icons/svgIndex";

function SubmissionSignatureModal({ t, handleProceed, handleCloseSignaturePopup, setSignedDocumentUploadID, applicationPdfFileStoreId }) {
  const [isSigned, setIsSigned] = useState(false);
  const { handleEsign, checkSignStatus } = Digit.Hooks.orders.useESign();
  const { uploadDocuments } = Digit.Hooks.orders.useDocumentUpload();
  const [formData, setFormData] = useState({}); // storing the file upload data
  const [pageModule, setPageModule] = useState("ci");
  const [openUploadSignatureModal, setOpenUploadSignatureModal] = useState(false);

  const UploadSignatureModal = window?.Digit?.ComponentRegistryService?.getComponent("UploadSignatureModal");
  const tenantId = window?.Digit.ULBService.getCurrentTenantId();
  const uri = `${window.location.origin}${Urls.FileFetchById}?tenantId=${tenantId}&fileStoreId=${applicationPdfFileStoreId}`;
  const name = "Signature";

  const uploadModalConfig = useMemo(() => {
    return {
      key: "uploadSignature",
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
  }, [name]);

  const onSelect = (key, value) => {
    if (value === null) {
      setFormData({});
    } else {
      setFormData((prevData) => ({
        ...prevData,
        [key]: value,
      }));
    }
  };

  useEffect(() => {
    const upload = async () => {
      if (formData?.uploadSignature?.Signature?.length > 0) {
        const uploadedFileId = await uploadDocuments(formData?.uploadSignature?.Signature, tenantId);
        setSignedDocumentUploadID(uploadedFileId[0]?.fileStoreId);
        setIsSigned(true);
      }
    };

    upload();
  }, [formData]);

  useEffect(() => {
    checkSignStatus(name, formData, uploadModalConfig, onSelect, setIsSigned);
  }, [checkSignStatus]);

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

  return !openUploadSignatureModal ? (
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
            <h1 style={{ color: "#3d3c3c", fontSize: "24px", fontWeight: "bold" }}>{t("YOUR_SIGNATURE")}</h1>
            <div className="buttons-div">
              <Button
                label={t("CS_ESIGN_AADHAR")}
                onClick={() => {
                  // setOpenAadharModal(true);
                  // setIsSigned(true);
                  handleEsign(name, pageModule, applicationPdfFileStoreId);
                }}
                className={"aadhar-sign-in"}
                labelClassName={"submission-aadhar-sign-in"}
              ></Button>
              <Button
                icon={<FileUploadIcon />}
                label={t("CS_UPLOAD_ESIGNATURE")}
                onClick={() => {
                  // setOpenUploadSignatureModal(true);
                  // setIsSigned(true);
                  setOpenUploadSignatureModal(true);
                }}
                className={"upload-signature"}
                labelClassName={"submission-upload-signature-label"}
              ></Button>
            </div>
            <div className="click-for-download">
              <h2>{t("WANT_TO_DOWNLOAD")}</h2>
              <a href={uri} target="_blank" rel="noreferrer" style={{ color: "#007E7E", cursor: "pointer", textDecoration: "underline" }}>
                {t("CLICK_HERE")}
              </a>
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
  ) : (
    <UploadSignatureModal
      t={t}
      key={name}
      name={name}
      setOpenUploadSignatureModal={setOpenUploadSignatureModal}
      onSelect={onSelect}
      config={uploadModalConfig}
      formData={formData}
    />
  );
}

export default SubmissionSignatureModal;
