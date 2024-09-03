import { FileIcon, PrintIcon } from "@egovernments/digit-ui-react-components";
import React from "react";

const DocumentPrintComponent = ({ documents }) => {
  return (
    <div className="print-documents-container">
      <div className="print-documents-heading">Print Documents</div>
      {documents?.map((document) => (
        <div className="print-documents-box">
          <div className="print-documents-box-text">
            <FileIcon />
            <div style={{ marginLeft: "0.5rem" }}>{document?.fileName}</div>
          </div>
          <button className="print-button">
            <PrintIcon />
            <div style={{ marginLeft: "0.5rem" }}>Print</div>
          </button>
        </div>
      ))}
    </div>
  );
};

export default DocumentPrintComponent;
