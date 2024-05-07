import { Card, Header, Label, UploadFile } from "@egovernments/digit-ui-react-components";
import React, { Fragment, useState } from "react";
import DocViewer, { DocViewerRenderers } from "@cyntler/react-doc-viewer";
import { samplePDF } from "./SamplePdfFile";
import { useTranslation } from "react-i18next";
import { Urls } from "../../hooks";

const SUPPORTED_FILE_FORMATS = [
  ".pdf",
  ".bmp",
  ".xlsx",
  ".csv",
  ".doc",
  ".docx",
  ".gif",
  ".htm",
  ".html",
  ".jpg",
  ".jpeg",
  ".png",
  ".ppt",
  ".pptx",
  ".tiff",
  ".txt",
  ".xls",
];

const DocViewerWrapper = ({ fileStoreId, tenantId }) => {
  const Digit = window?.Digit || {};
  const { t } = useTranslation();
  const { fileUrl, fileName } = Digit.Hooks.useQueryParams();
  const [selectedDocs, setSelectedDocs] = useState([]);
  const uri = `${window.location.origin}${Urls.FileFetchById}?tenantId=${tenantId}&fileStoreId=${fileStoreId}`;
  const documents = fileStoreId
    ? [{ uri: uri || samplePDF, fileName: "fileName" }]
    : selectedDocs.map((file) => ({
        uri: window.URL.createObjectURL(file),
        fileName: file?.name || fileName,
      }));

  return (
    <div className="docviewer-wrapper" id="docviewer-id">
      <Card>
        {documents?.length != 0 && (
          <>
            <DocViewer
              documents={documents}
              pluginRenderers={DocViewerRenderers}
              style={{ width: 262, height: 206 }}
              config={{
                header: {
                  disableHeader: true,
                  disableFileName: true,
                  retainURLParams: true,
                },
                csvDelimiter: ",", // "," as default,
                pdfZoom: {
                  defaultZoom: 1.1, // 1 as default,
                  zoomJump: 0.2, // 0.1 as default,
                },
                pdfVerticalScrollByDefault: true, // false as default
              }}
            />{" "}
          </>
        )}
      </Card>
      <a href={uri} target="_blank" rel="noreferrer" style={{ color: "#505A5F", textDecoration: "none" }}>
        {t("DOWNLOAD_BAR_COUNCIL_ID")}
      </a>
    </div>
  );
};

export default DocViewerWrapper;
