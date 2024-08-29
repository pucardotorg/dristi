import { Button, ErrorIcon } from "@egovernments/digit-ui-react-components";
import React, { useEffect, useState } from "react";
import { ReactComponent as DeleteFileIcon } from "../images/delete.svg";
import { FileUploader } from "react-drag-drop-files";
import { ReactComponent as UploadFileIcon } from "../images/upload.svg";
import { CloseIconWhite, FileIcon } from "../icons/svgIndex";
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

  useEffect(() => {
    if (fileData.fileStore) {
      const draftFile = new File(["draft content"], fileData.documentName, {
        type: fileData.documentType,
      });
      setFile(draftFile);
    }
  }, [fileData]);
  return (
    <div className={`uploaded-file-div-main upload-${!!uploadErrorInfo ? "error" : "successful"}`}>
      <div className={`uploaded-file-div-sub ${!!uploadErrorInfo ? "error" : ""}`}>
        <div className="uploaded-file-div-icon-area">
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
    </div>
  );
}

export default RenderFileCard;
