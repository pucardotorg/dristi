import { CloseSvg, InfoCard } from "@egovernments/digit-ui-components";
import React, { useState, useMemo, useEffect } from "react";
import Modal from "../../../dristi/src/components/Modal";
import { Button } from "@egovernments/digit-ui-react-components";
import { FileUploadIcon } from "../../../dristi/src/icons/svgIndex";
import useESign from "../hooks/orders/useESign";
import { Urls } from "../hooks/services/Urls";
import useDocumentUpload from "../hooks/orders/useDocumentUpload";

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

function OrderSignatureModal({
  t,
  order,
  handleIssueOrder,
  handleGoBackSignatureModal,
  saveOnsubmitLabel,
  setSignedDocumentUploadID,
  orderPdfFileStoreID,
}) {
  const [isSigned, setIsSigned] = useState(false);
  const { handleEsign, checkSignStatus } = useESign();
  const [formData, setFormData] = useState({}); // storing the file upload data
  const [openUploadSignatureModal, setOpenUploadSignatureModal] = useState(false);
  const UploadSignatureModal = window?.Digit?.ComponentRegistryService?.getComponent("UploadSignatureModal");
  const [pageModule, setPageModule] = useState("en");
  const tenantId = window?.Digit.ULBService.getCurrentTenantId();
  const uri = `${window.location.origin}${Urls.FileFetchById}?tenantId=${tenantId}&fileStoreId=${orderPdfFileStoreID}`;
  const { uploadDocuments } = useDocumentUpload();
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

  // check the data from upload
  useEffect(() => {
    const upload = async () => {
      if (formData?.uploadSignature?.Signature?.length > 0) {
        const uploadedFileId = await uploadDocuments(formData?.uploadSignature?.Signature, tenantId);
        setSignedDocumentUploadID(uploadedFileId?.[0]?.fileStoreId);
        setIsSigned(true);
      }
    };

    upload();
  }, [formData]);

  useEffect(() => {
    checkSignStatus(name, formData, uploadModalConfig, onSelect, setIsSigned);
  }, [checkSignStatus]);

  return !openUploadSignatureModal ? (
    <Modal
      headerBarMain={<Heading label={t("ADD_SIGNATURE")} />}
      headerBarEnd={<CloseBtn onClick={handleGoBackSignatureModal} />}
      actionCancelLabel={t("CS_COMMON_BACK")}
      actionCancelOnSubmit={handleGoBackSignatureModal}
      actionSaveLabel={t(saveOnsubmitLabel)}
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
                  // setIsSigned(true);
                  handleEsign(name, pageModule, orderPdfFileStoreID);
                }}
                className={"aadhar-sign-in"}
                labelClassName={"aadhar-sign-in"}
              />
              <Button
                icon={<FileUploadIcon />}
                label={t("UPLOAD_DIGITAL_SIGN_CERTI")}
                onButtonClick={() => {
                  // setOpenUploadSignatureModal(true);
                  // setIsSigned(true);
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

export default OrderSignatureModal;
