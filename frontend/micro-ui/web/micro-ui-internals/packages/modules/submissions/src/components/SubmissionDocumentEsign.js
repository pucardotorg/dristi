import React, { useState, useMemo, useEffect } from "react";
import useESign from "@egovernments/digit-ui-module-orders/src/hooks/orders/useESign";
import useDocumentUpload from "@egovernments/digit-ui-module-orders/src/hooks/orders/useDocumentUpload";
import { FileUploadIcon } from "@egovernments/digit-ui-module-dristi/src/icons/svgIndex";
import { Urls } from "../hooks/services/Urls";
import Button from "@egovernments/digit-ui-module-dristi/src/components/Button";

function SubmissionDocumentEsign({ t, setSignedId, setIsSignedHeading, setSignedDocumentUploadID, combinedFileStoreId }) {
  const [isSigned, setIsSigned] = useState(false);
  const { handleEsign, checkSignStatus } = useESign();
  const [formData, setFormData] = useState({}); // storing the file upload data
  const [openUploadSignatureModal, setOpenUploadSignatureModal] = useState(false);
  const UploadSignatureModal = window?.Digit?.ComponentRegistryService?.getComponent("UploadSignatureModal");
  const [pageModule, setPageModule] = useState("ci");
  const tenantId = window?.Digit.ULBService.getCurrentTenantId();
  const uri = `${window.location.origin}${Urls.FileFetchById}?tenantId=${tenantId}&fileStoreId=${combinedFileStoreId}`;
  const { uploadDocuments } = useDocumentUpload();
  const name = "Signature";
  const userInfo = Digit.UserService.getUser()?.info;
  const isAdvocate = userInfo?.roles?.some((role) => ["ADVOCATE_CLERK_ROLE", "ADVOCATE_ROLE"].includes(role.code));

  const uploadModalConfig = useMemo(() => {
    return {
      key: "uploadSignature",
      populators: {
        inputs: [
          {
            name: name,
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
    if (value?.[name] === null) {
      setFormData({});
      setIsSigned(false);
      setIsSignedHeading(false);
      setSignedDocumentUploadID(null);
      setSignedId(null);
    } else {
      setFormData((prevData) => ({
        ...prevData,
        [key]: value,
      }));
    }
  };

  const cleanString = (input) => {
    if (typeof input !== "string") return "";
    return input
      .replace(/\b(null|undefined)\b/g, "")
      .replace(/\s+/g, " ")
      .trim();
  };

  const onSubmit = async () => {
    if (formData?.uploadSignature?.Signature?.length > 0) {
      try {
        const uploadedFileId = await uploadDocuments(formData?.uploadSignature?.Signature, tenantId);
        setSignedDocumentUploadID(uploadedFileId?.[0]?.fileStoreId);
        setSignedId(uploadedFileId?.[0]?.fileStoreId);
        setIsSigned(true);
        setIsSignedHeading(true);
        localStorage.setItem("formData", JSON.stringify(formData));
        setOpenUploadSignatureModal(false);
      } catch (error) {
        console.error("error", error);
        setFormData({});
      }
    }
  };

  useEffect(() => {
    checkSignStatus(name, formData, uploadModalConfig, onSelect, setIsSigned, setIsSignedHeading);
  }, [checkSignStatus]);

  return !openUploadSignatureModal ? (
    <div style={{ padding: "30px 30px 5px 30px", width: "80%" }}>
      {!isSigned ? (
        <div>
          <h1 style={{ fontFamily: "Roboto", fontSize: "24px", fontWeight: 700, lineHeight: "28.13px", textAlign: "left" }}>
            {t("SUBMISSION_DOCUMENT_ADD_SIGNATURE")}
          </h1>
          <div>
            <h2 style={{ fontFamily: "Roboto", fontSize: "16px", fontWeight: 400, lineHeight: "18.75px", textAlign: "left" }}>
              {t("SUBMISSION_DOCUMENT_SIGNATURE_SUBTEXT")}
            </h2>
          </div>
          <div style={{ display: "flex" }}>
            <Button
              label={""}
              onButtonClick={() => {
                localStorage.setItem("combineDocumentsPdf", combinedFileStoreId);
                handleEsign(name, pageModule, combinedFileStoreId);
              }}
              style={{ boxShadow: "none", backgroundColor: "#007E7E", border: "none", padding: "20px 30px", maxWidth: "fit-content" }}
              textStyles={{
                width: "unset",
              }}
            >
              <h1
                style={{
                  fontFamily: "Roboto",
                  fontSize: "16px",
                  fontWeight: 700,
                  lineHeight: "18.75px",
                  textAlign: "center",
                  color: "#FFFFFF",
                }}
              >
                {t("CS_ESIGN")}
              </h1>
            </Button>
            <Button
              icon={<FileUploadIcon />}
              label={""}
              onButtonClick={() => {
                setOpenUploadSignatureModal(true);
              }}
              style={{ boxShadow: "none", background: "none", border: "none", padding: "20px 10px", maxWidth: "fit-content" }}
              textStyles={{
                width: "unset",
              }}
            >
              <h1
                style={{
                  fontFamily: "Roboto",
                  fontSize: "16px",
                  fontWeight: 700,
                  lineHeight: "18.75px",
                  textAlign: "center",
                  color: "#007E7E",
                }}
              >
                {t("UPLOAD_DIGITAL_SIGN_CERTI")}
              </h1>
            </Button>
          </div>
        </div>
      ) : (
        <div style={{ background: "#ECF3FD", padding: "8px 16px", borderRadius: "4px" }}>
          <div
            style={{
              paddingBottom: "20px",
              fontFamily: "Roboto",
              fontSize: "16px",
              fontWeight: 400,
              lineHeight: "18.75px",
              textAlign: "left",
              color: "#3D3C3C",
            }}
          >
            {t("E_SIGNED_BY")}
          </div>
          <div
            style={{
              fontFamily: "Roboto",
              fontSize: "20px",
              fontWeight: 500,
              lineHeight: "23.44px",
              textAlign: "left",
              color: "#000000",
              fontStyle: "italic",
              paddingBottom: "10px",
            }}
          >
            {cleanString(userInfo?.name)}
          </div>
          {isAdvocate && <div>{t("ADVOCATE_KERALA_HIGH_COURT")}</div>}
        </div>
      )}
    </div>
  ) : (
    <UploadSignatureModal
      t={t}
      key={name}
      name={name}
      setOpenUploadSignatureModal={setOpenUploadSignatureModal}
      onSelect={onSelect}
      config={uploadModalConfig}
      formData={formData}
      onSubmit={onSubmit}
    />
  );
}

export default SubmissionDocumentEsign;
