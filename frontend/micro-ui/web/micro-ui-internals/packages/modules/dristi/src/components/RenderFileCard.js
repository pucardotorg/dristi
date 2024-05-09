import { ErrorIcon, FileIcon } from "@egovernments/digit-ui-react-components";
import React from "react";
import CustomUploadButton from "./CustomUploadButton";
import { ReactComponent as DeleteFileIcon } from "../images/delete.svg";
function RenderFileCard({ handleChange, fileInputRef, handleReupload, handleDeleteFile, uploadErrorInfo, fileData, t, input }) {
  return (
    <div className={`uploaded-file-div-main upload-${!!uploadErrorInfo ? "error" : "successful"}`}>
      <div className="uploaded-file-div-sub">
        <div className="uploaded-file-div-icon-area">
          <FileIcon className="icon uploaded-file-icon" />
          <h3>{fileData?.name}</h3>
        </div>
        <div className="reupload-or-delete-div">
          <div>
            <CustomUploadButton t={t} handleChange={handleChange} fileInputRef={fileInputRef} handleReupload={handleReupload} input={input} />
          </div>
          <div
            className="icon delete-uploaded-file"
            onClick={() => {
              handleDeleteFile(input);
            }}
          >
            <DeleteFileIcon />
            <h3> Delete </h3>
          </div>
        </div>
      </div>
      {!!uploadErrorInfo && (
        <div className="upload-error-div-main">
          <div className="upload-error-div-sub">
            <div className="upload-error-icon-container">
              <ErrorIcon />
              <h2> Uploaded Failed!</h2>
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
