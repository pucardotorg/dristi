import { InfoCard } from "@egovernments/digit-ui-components";
import { FileUploadIcon } from "@egovernments/digit-ui-module-dristi/src/icons/svgIndex";
import { Button } from "@egovernments/digit-ui-react-components";
import React, { useEffect, useMemo, useState } from "react";
import useESign from "../hooks/orders/useESign";
import { Urls } from "../hooks/services/Urls";
import useDocumentUpload from "../hooks/orders/useDocumentUpload";

const AddSignatureComponent = ({ t, isSigned, handleSigned, rowData, setSignatureId, deliveryChannel }) => {
  const { handleEsign, checkSignStatus } = useESign();
  const { uploadDocuments } = useDocumentUpload();
  const [formData, setFormData] = useState({}); // storing the file upload data
  const [openUploadSignatureModal, setOpenUploadSignatureModal] = useState(false);
  const UploadSignatureModal = window?.Digit?.ComponentRegistryService?.getComponent("UploadSignatureModal");
  const [fileStoreId, setFileStoreId] = useState(rowData?.documents?.[0]?.fileStore || ""); // have to set the uploaded fileStoreID
  const [pageModule, setPageModule] = useState("en");
  const tenantId = window?.Digit.ULBService.getCurrentTenantId();
  const uri = `${window.location.origin}${Urls.FileFetchById}?tenantId=${tenantId}&fileStoreId=${fileStoreId}`;
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
            fileTypes: ["PDF"],
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
        setSignatureId(uploadedFileId?.[0]?.fileStoreId);
        handleSigned(true);
      }
    };

    upload();
  }, [formData]);

  useEffect(() => {
    checkSignStatus(name, formData, uploadModalConfig, onSelect, handleSigned);
  }, [checkSignStatus]);

  return (
    <div className="add-signature-main-div">
      {!openUploadSignatureModal ? (
        <div>
          <InfoCard
            variant={"default"}
            label={t("PLEASE_NOTE")}
            additionalElements={[
              <p key="note">
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
                    localStorage.setItem("ESignSummons", JSON.stringify(rowData));
                    localStorage.setItem("delieveryChannel", deliveryChannel);
                    handleEsign(name, pageModule, rowData?.documents?.[0]?.fileStore);
                  }}
                  className={"aadhar-sign-in"}
                  labelClassName={"aadhar-sign-in"}
                />
                <Button
                  icon={<FileUploadIcon />}
                  label={t("UPLOAD_DIGITAL_SIGN_CERTI")}
                  onButtonClick={() => {
                    setOpenUploadSignatureModal(true);
                  }}
                  className={"upload-signature"}
                  labelClassName={"upload-signature-label"}
                />
              </div>
              <div className="donwload-submission">
                <h2>{t("WANT_TO_DOWNLOAD")}</h2>
                <a href={uri} target="_blank" rel="noreferrer" style={{ color: "#007E7E", cursor: "pointer", textDecoration: "underline" }}>
                  {t("CLICK_HERE")}
                </a>
              </div>
            </div>
          ) : (
            <div className="signed">
              <h1>{t("YOUR_SIGNATURE")}</h1>
              <h2>{t("SIGNED")}</h2>
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
        />
      )}
    </div>
  );
};

export default AddSignatureComponent;
