import { FileIcon, PrintIcon } from "@egovernments/digit-ui-react-components";
import React from "react";
import { Urls } from "../hooks/services/Urls";
import AuthenticatedLink from "@egovernments/digit-ui-module-dristi/src/Utils/authenticatedLink";

const DocumentPrintComponent = ({ documents }) => {
  const tenantId = window?.Digit.ULBService.getCurrentTenantId();

  return (
    <div className="print-documents-container" style={{ gap: 4, display: "flex", flexDirection: "column" }}>
      <div className="print-documents-heading" style={{ paddingLeft: 4, fontSize: 17 }}>{`Print Documents (${documents?.length})`}</div>
      {documents?.map((document) => (
        <div className="print-documents-box-div" style={{ display: "flex", flexDirection: "column" }}>
          <div className="print-documents-box-text" style={{ padding: 0 }}>
            <FileIcon styles={{ height: "20px", width: "20px" }} />
            <div style={{ marginLeft: "0.5rem" }}>{document?.fileName}</div>
          </div>
          <button className="print-button" style={{ padding: 4 }}>
            <PrintIcon />
            <AuthenticatedLink
              uri={`${window.location.origin}${Urls.FileFetchById}?tenantId=${tenantId}&fileStoreId=${document?.fileStore}`}
              t={t}
              displayFilename={"PRINT"}
            />
          </button>
        </div>
      ))}
    </div>
  );
};

export default DocumentPrintComponent;
