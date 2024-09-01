import { Card, Header, Label, UploadFile } from "@egovernments/digit-ui-react-components";
import React, { Fragment, useState } from "react";
import DocViewer, { DocViewerRenderers } from "@cyntler/react-doc-viewer";
import { useTranslation } from "react-i18next";
import { Urls } from "../../hooks";
import { Link } from "react-router-dom";
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

const DocViewerWrapper = ({
  style,
  fileStoreId,
  tenantId,
  displayFilename,
  documentName,
  selectedDocs = [],
  docViewerCardClassName,
  showDownloadOption = true,
  docWidth = "262px",
  docHeight = "206px",
  preview,
}) => {
  const Digit = window?.Digit || {};
  const { t } = useTranslation();
  const { fileUrl, fileName } = Digit.Hooks.useQueryParams();
  // const [selectedDocs, setSelectedDocs] = useState([]);
  const uri = `${window.location.origin}${Urls.FileFetchById}?tenantId=${tenantId}&fileStoreId=${fileStoreId}`;
  const documents = fileStoreId
    ? [{ uri: uri || "", fileName: "fileName" }]
    : selectedDocs.map((file) => ({
        uri: window.URL.createObjectURL(file),
        fileName: file?.name || fileName,
      }));
  return (
    <div className="docviewer-wrapper" id="docviewer-id">
      <Card className={docViewerCardClassName}>
        {documents?.length != 0 && (
          <>
            <DocViewer
              className="docViewer-image"
              documents={documents}
              pluginRenderers={DocViewerRenderers}
              style={{ width: docWidth, height: docHeight, ...style }}
              theme={{
                primary: "#F47738",
                secondary: "#feefe7",
                tertiary: "#feefe7",
                textPrimary: "#0B0C0C",
                textSecondary: "#505A5F",
                textTertiary: "#00000099",
                disableThemeScrollbar: true,
              }}
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
                pdfVerticalScrollByDefault: !preview, // false as default
              }}
            />{" "}
          </>
        )}
      </Card>
      {showDownloadOption && (
        <Link
          to={uri}
          target="_blank"
          rel="noreferrer"
          style={{
            display: "flex",
            color: "#007e7e",
            width: 250,
            whiteSpace: "nowrap",
            overflow: "hidden",
            textOverflow: "ellipsis",
          }}
        >
          {t(displayFilename) || t("CS_CLICK_TO_DOWNLOAD")}
        </Link>
      )}
      {documentName && (
        <p
          style={{
            display: "flex",
            color: "#505A5F",
            textDecoration: "none",
            width: 250,
            whiteSpace: "nowrap",
            overflow: "hidden",
            textOverflow: "ellipsis",
            margin: 8,
          }}
        >
          {t(documentName)}
        </p>
      )}
    </div>
  );
};

export default DocViewerWrapper;
