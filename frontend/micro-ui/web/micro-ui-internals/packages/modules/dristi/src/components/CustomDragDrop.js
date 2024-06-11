import { ErrorIcon, FileIcon, UploadIcon } from "@egovernments/digit-ui-react-components";
import React, { useState, useMemo, useRef, useEffect } from "react";
import { ReactComponent as DeleteFileIcon } from "../images/delete.svg";
import { ReactComponent as InfoToolTipIcon } from "../images/Vector.svg";

import { FileUploader } from "react-drag-drop-files";
import CustomUploadButton from "./CustomUploadButton";

const CustomDragDrop = ({
  heading,
  isOptional = false,
  showInfoTooltip = false,
  note,
  uploadGuidelines,
  file = null,
  t,
  fileTypes = ["JPG", "PNG", "PDF"],
  fileValidator,
  onChange,
  ...props
}) => {
  const [fileData, setFileData] = useState(file);
  const [uploadErrorInfo, setUploadErrorInfo] = useState("");
  const [showReupload, setShowReupload] = useState(false);
  const [showDragDropArea, setShowDragDropArea] = useState(true);
  const maxFileSize = 1024 * 1024 * 50; // 2 MB limit
  const fileInputRef = useRef();

  useEffect(() => {
    setFileData(file);
  }, [file]);

  const handleChange = (data, chosenFromReupload) => {
    let file = data;
    if (chosenFromReupload) {
      file = data.target.files[0];
    }
    if (file.size > maxFileSize) {
      setUploadErrorInfo("Your file exceeded the 50mb limit.");
    } else setUploadErrorInfo("");
    setFileData(file);
    setShowReupload(true);
    setShowDragDropArea(false);
    onChange(file);
  };

  const dragDropJSX = (
    <div className="drag-drop-container">
      <UploadIcon />
      <p className="drag-drop-text">
        {t("WBH_DRAG_DROP")} <text className="browse-text">{t("WBH_BULK_BROWSE_FILES")}</text>
      </p>
    </div>
  );

  const handleReupload = () => {
    fileInputRef.current.click();
  };

  const handleDeleteFile = () => {
    setFileData(null);
    setShowDragDropArea(true);
  };

  const renderFileCard = useMemo(() => {
    return (
      <div className={`uploaded-file-div-main upload-${!!uploadErrorInfo ? "error" : "successful"}`}>
        <div className="uploaded-file-div-sub">
          <div className="uploaded-file-div-icon-area">
            <FileIcon className="icon uploaded-file-icon" />
            <h3>{fileData?.name}</h3>
          </div>
          <div className="reupload-or-delete-div">
            <div>
              <CustomUploadButton t={t} onChange={handleChange} fileInputRef={fileInputRef} handleReupload={handleReupload} />
            </div>
            <div className="icon delete-uploaded-file" onClick={handleDeleteFile}>
              <DeleteFileIcon></DeleteFileIcon>
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
  }, [fileData, uploadErrorInfo, showReupload]);

  return (
    <div>
      {showDragDropArea && (
        <div className="drag-drop-visible-main">
          <div className="drag-drop-heading-main">
            <div className="drag-drop-heading">
              <h1> {heading}</h1>
              {isOptional && <h3> {t("CS_IS_OPTIONAL")}</h3>}
              {showInfoTooltip && (
                <span>
                  <InfoToolTipIcon></InfoToolTipIcon>
                </span>
              )}
            </div>
            {<p>{note}</p>}
          </div>
          <div className="file-uploader-div-main">
            <FileUploader handleChange={handleChange} name="file" types={fileTypes} children={dragDropJSX} onTypeError={fileValidator} />
          </div>
          <div className="upload-guidelines-div">
            <p>{uploadGuidelines}</p>
          </div>
        </div>
      )}
      {!showDragDropArea && fileData && renderFileCard}
    </div>
  );
};

export default CustomDragDrop;
