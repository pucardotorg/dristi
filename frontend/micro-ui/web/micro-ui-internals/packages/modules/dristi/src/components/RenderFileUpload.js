import React, { useEffect, useState } from "react";
import { CloseIcon, FileIcon } from "../icons/svgIndex";
import { Urls } from "../hooks";

function RenderFileUpload({ handleDeleteFile, fileData, index, disableUploadDelete = false, displayName, fileStoreId }) {
  console.log(fileStoreId);
  const [file, setFile] = useState(null);
  const tenantId = window?.Digit.ULBService.getCurrentTenantId();
  const uri = `${window.location.origin}${Urls.FileFetchById}?tenantId=${tenantId}&fileStoreId=${fileStoreId?.fileStoreId}`;

  useEffect(() => {
    if (fileData?.fileStore) {
      const draftFile = new File(["draft content"], fileData.documentName || "Unnamed File", {
        type: fileData.documentType || "application/octet-stream",
      });
      setFile(draftFile);
    }
  }, [fileData]);

  return (
    <div className={`uploaded-file-div-main upload-${fileData?.uploadErrorInfo ? "error" : "successful"}`} style={{ padding: "10px" }}>
      <div className={`uploaded-file-div-sub ${fileData?.uploadErrorInfo ? "error" : ""}`}>
        <div className="uploaded-file-div-icon-area">
          <div className="uploaded-file-icon">
            <a href={uri} target="_blank" rel="noreferrer">
              <FileIcon />
            </a>
          </div>
          <span style={{ margin: "2px", color: "#505A5F", fontSize: "14px" }}>
            {displayName || (fileData?.fileStore ? file?.name : fileData?.name) || "Unnamed File"}
          </span>
        </div>
        <div className="reupload-or-delete-div">
          <div
            style={{
              padding: "0",
              cursor: disableUploadDelete ? "not-allowed" : "pointer",
              opacity: disableUploadDelete ? 0.5 : 1,
            }}
            onClick={() => {
              if (!disableUploadDelete) {
                handleDeleteFile(index); // Pass the index to the handleDeleteFile function
              }
            }}
            className="delete-button"
          >
            <CloseIcon />
          </div>
        </div>
      </div>
    </div>
  );
}

export default RenderFileUpload;
