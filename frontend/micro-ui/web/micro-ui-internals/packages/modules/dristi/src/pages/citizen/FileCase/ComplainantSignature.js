import React, { useMemo, useState } from "react";
import { ActionBar, SubmitBar, Loader } from "@egovernments/digit-ui-react-components";
import { useHistory } from "react-router-dom/cjs/react-router-dom.min";
import DocViewerWrapper from "../../employee/docViewerWrapper";
import { FileUploadIcon } from "../../../icons/svgIndex";
import { ReactComponent as InfoIcon } from "../../../icons/info.svg";
import { useTranslation } from "react-i18next";
import useSearchCaseService from "../../../hooks/dristi/useSearchCaseService";
import useDownloadCasePdf from "../../../hooks/dristi/useDownloadCasePdf";

const getStyles = () => ({
  container: { display: "flex", flexDirection: "row", height: "100vh" },
  leftPanel: {
    flex: 1,
    padding: "24px 16px 16px 16px",
    display: "flex",
    flexDirection: "column",
    justifyContent: "flex-start",
  },
  caseDetails: {
    border: "1px solid #9E400A24",
    borderRadius: "4px",
    padding: "12px 16px",
    backgroundColor: "#F7F5F3",
    marginBottom: "16px",
  },
  infoRow: { display: "flex", alignItems: "center" },
  infoText: { marginLeft: "8px" },
  detailsSection: {
    display: "flex",
    flexDirection: "column",
    gap: "12px",
  },
  advocateDetails: {
    marginTop: "8px",
    display: "flex",
    justifyContent: "space-between",
    alignItems: "center",
  },
  litigantDetails: {
    display: "flex",
    justifyContent: "space-between",
    alignItems: "center",
  },
  signedLabel: {
    padding: "6px 8px",
    borderRadius: "999px",
    color: "#00703C",
    backgroundColor: "#E4F2E4",
    fontSize: "14px",
    fontWeight: 400,
  },
  centerPanel: { flex: 3, padding: "24px 16px 16px 16px", border: "1px solid #e0e0e0" },
  header: { height: "72px", gap: "8px", display: "flex", flexDirection: "column", justifyContent: "space-between" },
  title: { width: "584px", height: "38px", color: "#0A0A0A", fontSize: "32px", fontWeight: 700 },
  downloadButton: {
    background: "none",
    border: "none",
    color: "#007E7E",
    fontSize: "16px",
    fontWeight: 700,
    display: "flex",
    alignItems: "center",
    cursor: "pointer",
  },
  details: { color: "#231F20", fontWeight: 700, fontSize: "18px" },
  description: { color: "#77787B", fontSize: "16px", fontWeight: 400 },
  docViewer: { marginTop: "24px", border: "1px solid #e0e0e0", display: "flex", overflow: "hidden" },
  rightPanel: { flex: 1, padding: "24px 16px 24px 24px", borderLeft: "1px solid #ccc" },
  signaturePanel: { display: "flex", flexDirection: "column" },
  signatureTitle: { fontSize: "24px", fontWeight: 700, color: "#3D3C3C" },
  signatureDescription: { fontWeight: "400", fontSize: "16px", color: "#3D3C3C" },
  esignButton: {
    height: "40px",
    alignItems: "center",
    fontWeight: 700,
    fontSize: "16px",
    color: "#FFFFFF",
    display: "flex",
    justifyContent: "center",
    backgroundColor: "#007E7E",
    border: "none",
    cursor: "pointer",
    padding: "0 20px",
  },
  uploadButton: {
    marginBottom: "16px",
    height: "40px",
    fontWeight: 700,
    fontSize: "16px",
    display: "flex",
    justifyContent: "center",
    alignItems: "center",
    background: "none",
    border: "none",
    cursor: "pointer",
    padding: "0 16px",
    color: "#007E7E",
  },
  actionBar: { display: "flex", justifyContent: "flex-end", width: "100%" },
  submitButton: { backgroundColor: "#008080", color: "#fff", fontWeight: "bold", cursor: "pointer" },
});

const RightArrow = () => (
  <svg style={{ marginLeft: "8px" }} width="16" height="16" viewBox="0 0 16 16" fill="none" xmlns="http://www.w3.org/2000/svg">
    <path d="M8 0L6.59 1.41L12.17 7H0V9H12.17L6.59 14.59L8 16L16 8L8 0Z" fill="white" />
  </svg>
);

const FileDownloadIcon = () => (
  <svg width="16" height="16" viewBox="0 0 16 16" fill="none" xmlns="http://www.w3.org/2000/svg">
    <path d="M12.6693 6H10.0026V2H6.0026V6H3.33594L8.0026 10.6667L12.6693 6ZM3.33594 12V13.3333H12.6693V12H3.33594Z" fill="#007E7E" />
  </svg>
);

const caseType = {
  category: "Criminal",
  act: "Negotiable Instruments Act",
  section: "138",
  courtName: "Kollam S-138 Special Court",
  href: "https://districts.ecourts.gov.in/sites/default/files/study%20circles.pdf",
};

const ComplainantSignature = ({ path }) => {
  const { t } = useTranslation();
  const history = useHistory();
  const urlParams = new URLSearchParams(window.location.search);
  const caseId = urlParams.get("caseId");
  const [litigantSign, setLitigantSign] = useState(false);
  const [advocateSign, setAdvocateSign] = useState(false);
  const tenantId = window?.Digit.ULBService.getCurrentTenantId();
  const fileStoreId = "2234aee6-be86-4e22-8850-5196245a8e82";
  const styles = getStyles();
  const { downloadPdf } = useDownloadCasePdf();

  const { data: caseData, refetch: refetchCaseData, isLoading } = useSearchCaseService(
    {
      criteria: [
        {
          caseId: caseId,
          defaultFields: false,
        },
      ],
      tenantId,
    },
    {},
    "dristi",
    caseId,
    caseId
  );

  const caseDetails = useMemo(
    () => ({
      ...caseData?.criteria?.[0]?.responseList?.[0],
    }),
    [caseData]
  );

  const DocumentFileStoreId = useMemo(() => {
    return caseDetails?.additionalDetails?.signedCaseDocument;
  });

  const advocateDetails = useMemo(() => {
    const advocateData = caseDetails?.additionalDetails?.advocateDetails?.formdata?.[0]?.data;
    if (advocateData?.isAdvocateRepresenting?.code === "YES") {
      return advocateData;
    }
    return null;
  }, [caseDetails]);

  const litigants = useMemo(() => {
    return caseDetails?.litigants?.filter((litigant) => litigant.partyType === "complainant.primary")?.[0];
  }, [caseDetails]);

  const handleCasePdf = () => {
    downloadPdf(tenantId, DocumentFileStoreId || fileStoreId);
  };

  const handleEsign = () => {
    setLitigantSign(true);
  };

  const handleUploadFile = () => {
    setAdvocateSign(true);
  };

  const SubmitLabel = useMemo(() => {
    if (advocateDetails && !advocateSign) {
      return t("CS_ADVOCATE_SIGN");
    }
    return t("CS_SUBMIT_CASE");
  }, [advocateSign, advocateDetails]);

  const handleSubmit = () => {
    history.push(`${path}/e-filing-payment?caseId=${caseId}`, { state: { calculationResponse: 4 } });
  };

  const isSubmitEnabled = () => {
    return advocateDetails ? advocateDetails && litigantSign : litigantSign;
  };

  if (isLoading) {
    return <Loader />;
  }

  return (
    <div style={styles.container}>
      <div style={styles.leftPanel}>
        <div style={styles.caseDetails}>
          <div style={styles.infoRow}>
            <InfoIcon />
            <span style={styles.infoText}>
              <b>{t("CS_YOU_ARE_FILING_A_CASE")}</b>
            </span>
          </div>
          <div>
            <p>
              {t("CS_UNDER")}{" "}
              <a href={caseType?.href} target="_blank" rel="noreferrer" style={{ color: "#3d3c3c" }}>{`S-${caseType.section}, ${caseType.act}`}</a>{" "}
              {t("CS_IN")}
              <span style={{ fontWeight: 700 }}>{` ${caseType.courtName}.`}</span>
            </p>
          </div>
        </div>

        <div style={styles.detailsSection}>
          <div style={styles.litigantDetails}>
            <div style={styles.details}>
              <div>{t("COMPLAINT_SIGN")}:</div>
              <div style={{ marginTop: "5px" }}>{litigants?.additionalDetails?.fullName}</div>
            </div>
            {litigantSign && <span style={styles.signedLabel}>{t("SIGNED")}</span>}
          </div>
          {advocateDetails && (
            <div style={styles.advocateDetails}>
              <div style={styles.details}>
                <div>{t("ADVOCATE_SIGN")}:</div>
                <div style={{ marginTop: "5px" }}>{advocateDetails?.advocateName}</div>
              </div>
              {advocateSign && <span style={styles.signedLabel}>{t("SIGNED")}</span>}
            </div>
          )}
        </div>
      </div>

      <div style={styles.centerPanel}>
        <div style={styles.header}>
          <div style={{ display: "flex", justifyContent: "space-between", alignItems: "center" }}>
            <div style={styles.title}>{t("SIGN_COMPLAINT")}</div>
            <button style={styles.downloadButton} onClick={handleCasePdf}>
              <span style={{ marginRight: "8px", display: "flex", alignItems: "center" }}>
                <FileDownloadIcon />
              </span>
              <span>{t("DOWNLOAD_PDF")}</span>
            </button>
          </div>
          <div style={styles.description}>{t("CS_REVIEW_CASE_FILE_SUBTEXT")}</div>
        </div>
        <div style={styles.docViewer}>
          <DocViewerWrapper
            docWidth={"100vh"}
            docHeight={"70vh"}
            fileStoreId={DocumentFileStoreId || fileStoreId}
            tenantId={tenantId}
            docViewerCardClassName={"doc-card"}
            showDownloadOption={false}
          />
        </div>
      </div>

      <div style={styles.rightPanel}>
        {!litigantSign && (
          <div style={styles.signaturePanel}>
            <div style={styles.signatureTitle}>{t("ADD_SIGNATURE")}</div>
            <p style={styles.signatureDescription}>{t("EITHER_ESIGN_UPLOAD")}</p>
            <button style={styles.esignButton} onClick={handleEsign}>
              {t("CS_ESIGN")}
            </button>
            <button style={styles.uploadButton} onClick={handleUploadFile}>
              <FileUploadIcon />
              <span style={{ marginLeft: "8px" }}>{t("UPLOAD_SIGNED_PDF")}</span>
            </button>
          </div>
        )}
      </div>

      <ActionBar>
        <div style={styles.actionBar}>
          <SubmitBar
            label={
              <div style={{ display: "flex", alignItems: "center", justifyContent: "center", width: "100%" }}>
                <span>{SubmitLabel}</span>
                <RightArrow />
              </div>
            }
            onSubmit={handleSubmit}
            style={styles.submitButton}
            disabled={!isSubmitEnabled()}
          />
        </div>
      </ActionBar>
    </div>
  );
};

export default ComplainantSignature;
