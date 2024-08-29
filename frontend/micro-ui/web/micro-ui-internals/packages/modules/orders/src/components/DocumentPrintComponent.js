import { FileIcon, PrintIcon } from "@egovernments/digit-ui-react-components";
import React from "react";
import { Urls } from "../hooks/services/Urls";

const DocumentPrintComponent = ({ documents }) => {
  const tenantId = window?.Digit.ULBService.getCurrentTenantId();

  return (
    <div className="print-documents-container">
      <div className="print-documents-heading">{`Print Documents (${documents?.length})`}</div>
      {documents?.map((document) => (
        <div className="print-documents-box-div">
          <div className="print-documents-box-text">
            <FileIcon />
            <div style={{ marginLeft: "0.5rem" }}>{document?.fileName}</div>
          </div>
          <button className="print-button">
            <PrintIcon />
            <a
              href={`${window.location.origin}${Urls.FileFetchById}?tenantId=${tenantId}&fileStoreId=${document?.fileStore}`}
              target="_blank"
              rel="noreferrer"
              style={{ marginLeft: "0.5rem", color: "#007E7E" }}
            >
              Print
            </a>
          </button>
        </div>
      ))}
    </div>
  );
};

export default DocumentPrintComponent;
