import { Button, ErrorIcon, FileIcon } from "@egovernments/digit-ui-react-components";
import React from "react";
import { ReactComponent as DeleteFileIcon } from "../images/delete.svg";
import { FileUploader } from "react-drag-drop-files";
import { ReactComponent as UploadFileIcon } from "../images/upload.svg";
function RenderFileCard({ handleChange, handleDeleteFile, fileData, t, input, index, uploadErrorInfo }) {
  return (
    <div className={`uploaded-file-div-main upload-${!!uploadErrorInfo ? "error" : "successful"}`}>
      <div className="uploaded-file-div-sub">
        <div className="uploaded-file-div-icon-area">
          <FileIcon className="icon uploaded-file-icon" />
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
              children={<Button label={t("CS_REUPLOAD")} icon={<UploadFileIcon></UploadFileIcon>} />}
              key={`file ${index}`}
            />
          </div>
          <div
            className="icon delete-uploaded-file"
            onClick={() => {
              handleDeleteFile(input, index);
            }}
          >
            <DeleteFileIcon />
            <h3> {t("Delete")} </h3>
          </div>
        </div>
      </div>
      {!!uploadErrorInfo && (
        <div className="upload-error-div-main">
          <div className="upload-error-div-sub">
            <div className="upload-error-icon-container">
              <ErrorIcon />
              <h2> {t("CS_UPLOAD_FAILED")}</h2>
            </div>
            <div className="upload-error-info-div">
              <h1>{uploadErrorInfo}</h1>
            </div>
          </div>
        </div>
      )}
    </div>
  );
}

export default RenderFileCard;
