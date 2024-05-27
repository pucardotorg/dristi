import { Button, ErrorIcon } from "@egovernments/digit-ui-react-components";
import React from "react";
import { ReactComponent as DeleteFileIcon } from "../images/delete.svg";
import { FileUploader } from "react-drag-drop-files";
import { ReactComponent as UploadFileIcon } from "../images/upload.svg";
import { CloseIconWhite, FileIcon } from "../icons/svgIndex";
function RenderFileCard({ handleChange, handleDeleteFile, fileData, t, input, index, uploadErrorInfo }) {
  return (
    <div className={`uploaded-file-div-main upload-${!!uploadErrorInfo ? "error" : "successful"}`}>
      <div className={`uploaded-file-div-sub ${!!uploadErrorInfo ? "error" : ""}`}>
        <div className="uploaded-file-div-icon-area">
          <div className="uploaded-file-icon">
            <FileIcon />
          </div>
          <h3>{fileData?.name}</h3>
        </div>
        <div className="reupload-or-delete-div">


          <div>
            <FileUploader
              handleChange={(data) => {
                handleChange(data, input, index);
              }}
              name="file"
              types={input?.fileTypes}
              children={
                <Button
                  icon={<div><UploadFileIcon /> </div>}
                  className="reupload-button"
                  label={t("CS_REUPLOAD")}
                />
              }
              key={`file ${index}`}
            />
          </div>
          <Button
            onButtonClick={() => {
              handleDeleteFile(input, index);
            }}
            icon={<div><DeleteFileIcon /> </div>}
            className="delete-button"
            label={t("Delete")}
          />
        </div>
      </div>

      <div className={`uploaded-file-div-sub-mobile`}>
        <img src="https://picsum.photos/200" alt="Description" className="image" />
        <div className="close-button" onClick={() => { handleDeleteFile(input, index); }}><CloseIconWhite /></div>
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
