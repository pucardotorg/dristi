import React from "react";
import DocumentPrintComponent from "./DocumentPrintComponent";
import ApplicationInfoComponent from "./ApplicationInfoComponent";

const PrintAndSendDocumentComponent = ({ infos, links, documents }) => {
  return (
    <div style={{ width: "616px" }} className="print-and-send-document">
      <DocumentPrintComponent documents={documents} />
      <ApplicationInfoComponent infos={infos} links={links} />
    </div>
  );
};

export default PrintAndSendDocumentComponent;
