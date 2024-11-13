import React, { useState, useEffect } from "react";
import { FileAttachIcon } from "../icons/svgIndex";
import RenderFileUpload from "./RenderFileUpload";
import UploadDocument from "./UploadDocument";
import ESignSignatureModal from "./ESignSignatureModal";

const DragDropJSX = ({ t, currentValue, error }) => {
  return (
    <React.Fragment>
      <div style={{ marginTop: "10px", cursor: "pointer" }}>
        <div style={{ color: "#505A5F" }}>
          <FileAttachIcon />
        </div>
      </div>
    </React.Fragment>
  );
};

function SelectCustomDocUpload({ t, config, formUploadData = {}, setData, documentSubmission, showDocument, setShowDocument }) {
  const [upload, setUpload] = useState(false);
  const [eSignModal, setEsignModal] = useState(false);
  const [isSigned, setIsSigned] = useState(false);

  const handleOpenUploadModal = () => {
    setUpload(true);
  };

  const handleCancelUpload = () => {
    setUpload(false);
    setEsignModal(false);
  };

  const handleUploadProceed = () => {
    setUpload(false);
    setEsignModal(true);
  };

  const handleGoBackSignatureModal = () => {
    setUpload(true);
    setEsignModal(false);
  };

  const handleEsign = () => {
    localStorage.removeItem("docSubmission");
    localStorage.removeItem("formUploadData");
    localStorage.removeItem("EvidenceFile");
    const localStorageID = localStorage.getItem("fileStoreId");
    setData((prevData) => ({
      ...prevData,
      SelectUserTypeComponent: {
        ...prevData.SelectUserTypeComponent,
        doc: [
          [
            prevData.SelectUserTypeComponent.doc[0][0],
            {
              ...prevData.SelectUserTypeComponent.doc[0][1],
              fileStoreId: localStorageID ? localStorageID : prevData.SelectUserTypeComponent.doc[0][1].fileStoreId,
            },
          ],
        ],
      },
    }));
    localStorage.removeItem("fileStoreId");
    setShowDocument(true);
    setEsignModal(false);
  };

  const handleDeleteFile = () => {
    setShowDocument(false);
    setData({});
  };

  const loadFileFromLocalStorage = () => {
    const storedData = localStorage.getItem("EvidenceFile");

    if (storedData) {
      const storedObject = JSON.parse(storedData);

      // Ensure that SelectUserTypeComponent and doc are present
      if (storedObject.SelectUserTypeComponent?.doc?.[0]?.[1]?.fileData) {
        const fileData = storedObject.SelectUserTypeComponent.doc[0][1].fileData;
        const byteString = atob(fileData.base64String.split(",")[1]);
        const mimeType = fileData.base64String.split(",")[0].match(/:(.*?);/)[1];
        const byteArray = new Uint8Array(byteString.length);

        for (let i = 0; i < byteString.length; i++) {
          byteArray[i] = byteString.charCodeAt(i);
        }

        const restoredFile = new File([byteArray], fileData.fileName, { type: mimeType });

        // Return the restored object with updated file
        const restoredObject = {
          ...storedObject,
          SelectUserTypeComponent: {
            ...storedObject.SelectUserTypeComponent,
            doc: [
              [
                storedObject.SelectUserTypeComponent.doc[0][0], // Preserve the first element
                {
                  ...storedObject.SelectUserTypeComponent.doc[0][1],
                  file: restoredFile, // Update with the restored file
                },
              ],
            ],
          },
        };

        return restoredObject;
      }
    }

    return {}; // Return empty object if no valid data found
  };

  useEffect(() => {
    const isSignSuccess = localStorage.getItem("esignProcess");
    const formData = localStorage.getItem("formUploadData");
    const restoreFileData = loadFileFromLocalStorage();

    if (isSignSuccess) {
      if (formData) {
        const parsedFormData = JSON.parse(formData);

        // Ensure that `restoreFileData` contains the correct file structure
        if (restoreFileData.SelectUserTypeComponent?.doc?.[0]?.[1]?.file) {
          const restoredFile = restoreFileData.SelectUserTypeComponent.doc[0][1].file;

          // Update the file in `parsedFormData` with the restored file
          if (parsedFormData.SelectUserTypeComponent?.doc?.[0]?.[1]) {
            parsedFormData.SelectUserTypeComponent.doc[0][1].file = restoredFile;
          }
        }

        setData(parsedFormData);
      }

      setEsignModal(true);
      localStorage.removeItem("esignProcess");
    }
  }, []);

  return (
    <React.Fragment>
      {!showDocument && (
        <div className="file-uploader-div-main show-file-uploader">
          <button style={{ background: "none" }} onClick={handleOpenUploadModal}>
            <DragDropJSX />
          </button>
        </div>
      )}
      {upload && (
        <UploadDocument
          config={config}
          t={t}
          handleCancelUpload={handleCancelUpload}
          handleUploadProceed={handleUploadProceed}
          formUploadData={formUploadData}
          setData={setData}
        />
      )}
      {eSignModal && (
        <ESignSignatureModal
          t={t}
          saveOnsubmitLabel={t("Add Document")}
          doctype={formUploadData?.SelectUserTypeComponent?.selectIdType?.code}
          handleGoBackSignatureModal={handleGoBackSignatureModal}
          handleIssueOrder={handleEsign}
          documentSubmission={documentSubmission}
          formUploadData={formUploadData}
          isSigned={isSigned}
          setIsSigned={setIsSigned}
        />
      )}
      {showDocument && (
        <div className="drag-drop-visible-main">
          {formUploadData?.SelectUserTypeComponent?.doc?.map((fileData, index) => (
            <RenderFileUpload
              key={`${fileData[0]}-${index}`}
              index={index}
              fileData={fileData?.[1]?.file}
              fileStoreId={fileData?.[1]?.fileStoreId}
              handleDeleteFile={handleDeleteFile}
              t={t}
              displayName={`${config?.[0]?.body?.[0]?.populators?.inputs?.[0]?.options?.[0]?.name}`}
            />
          ))}
        </div>
      )}
    </React.Fragment>
  );
}

export default SelectCustomDocUpload;
