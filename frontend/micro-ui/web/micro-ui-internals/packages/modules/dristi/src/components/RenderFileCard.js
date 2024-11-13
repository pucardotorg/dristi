import { Button, ErrorIcon } from "@egovernments/digit-ui-react-components";
import React, { useEffect, useMemo, useRef, useState } from "react";
import { ReactComponent as DeleteFileIcon } from "../images/delete.svg";
import { FileUploader } from "react-drag-drop-files";
import { ReactComponent as UploadFileIcon } from "../images/upload.svg";
import { CloseIconWhite, FileIcon } from "../icons/svgIndex";
import ImageModal from "./ImageModal";
function RenderFileCard({
  handleChange,
  handleDeleteFile,
  fileData,
  t,
  input,
  index,
  uploadErrorInfo,
  isDisabled = false,
  disableUploadDelete = false,
}) {
  const [file, setFile] = useState(null);
  const popupAnchor = useRef();
  const [showImageModal, setShowImageModal] = useState(false);

  useEffect(() => {
    if (fileData.fileStore) {
      const draftFile = new File(["draft content"], fileData.documentName, {
        type: fileData.documentType,
      });
      setFile(draftFile);
    }
  }, [fileData]);

  const viewImageModal = useMemo(() => {
    return (
      <div>
        <ImageModal
          imageInfo={{
            data: {
              fileStore: fileData?.fileStore,
              fileName: fileData?.fileName,
              documentName: fileData?.documentName,
              docViewerStyle: { minWidth: "100%", height: "calc(100vh - 154px)" },
            },
          }}
          selectedDocs={[fileData]}
          t={t}
          anchorRef={popupAnchor}
          showFlag={!showImageModal}
          handleCloseModal={() => {
            if (showImageModal) {
              setShowImageModal(false);
            }
          }}
          isPrevScrutiny={false}
          disableScrutiny={false}
        />
      </div>
    );
  }, [fileData, showImageModal]);
  return (
    <div className={`uploaded-file-div-main upload-${!!uploadErrorInfo ? "error" : "successful"}`}>
      <div className={`uploaded-file-div-sub ${!!uploadErrorInfo ? "error" : ""}`}>
        <div className="uploaded-file-div-icon-area" onClick={() => setShowImageModal(true)}>
          <div className="uploaded-file-icon">
            <FileIcon />
          </div>
          <h3>{fileData.fileStore ? file?.name : fileData?.name}</h3>
        </div>
        <div className="reupload-or-delete-div">
          <div>
            <FileUploader
              handleChange={(data) => {
                handleChange(data, input, index);
              }}
              name="file"
              types={input?.fileTypes}
              disabled={isDisabled}
              children={
                <Button
                  isDisabled={disableUploadDelete}
                  onButtonClick={() => {
                    if (isDisabled) handleChange(input, index);
                  }}
                  icon={
                    <div>
                      <UploadFileIcon />{" "}
                    </div>
                  }
                  className="reupload-button"
                  label={t("CS_REUPLOAD")}
                />
              }
              key={`file ${index}`}
            />
          </div>
          <Button
            isDisabled={disableUploadDelete}
            onButtonClick={() => {
              handleDeleteFile(input, index);
            }}
            key={`Delete-${input.name}`}
            icon={
              <div>
                <DeleteFileIcon />{" "}
              </div>
            }
            className="delete-button"
            label={t("Delete")}
          />
        </div>
      </div>

      <div className={`uploaded-file-div-sub-mobile`}>
        <img src="https://picsum.photos/200" alt="Description" className="image" />
        <div
          className="close-button"
          onClick={() => {
            handleDeleteFile(input, index);
          }}
        >
          <CloseIconWhite />
        </div>
      </div>

      <div className={`uploaded-file-div-sub-mobile`}>
        <img src="https://picsum.photos/200" alt="Description" className="image" />
        <div
          className="close-button"
          onClick={() => {
            handleDeleteFile(input, index);
          }}
        >
          <CloseIconWhite />
        </div>
      </div>
      {!!uploadErrorInfo && (
        <div className="upload-error-div-main">
          <div className="upload-error-icon-container">
            <ErrorIcon />
            <h2> {t("CS_UPLOAD_FAILED")}</h2>
          </div>
          <div className="upload-error-info-div">
            <h1>{uploadErrorInfo}</h1>
          </div>
        </div>
      )}
      {showImageModal && viewImageModal}
    </div>
  );
}

export default RenderFileCard;
