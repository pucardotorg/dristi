import React from "react";
import { FactCheckIcon, FactCrossIcon } from "../icons/svgIndex";
import ReactTooltip from "react-tooltip";

export const Evidence = ({ rowData, colData, value = "", showAsHeading = false, t, userRoles }) => {
  const getDate = (value) => {
    const date = new Date(value);
    const day = date.getDate().toString().padStart(2, "0");
    const month = (date.getMonth() + 1).toString().padStart(2, "0"); // Month is zero-based
    const year = date.getFullYear();
    const formattedDate = `${day}-${month}-${year}`;
    return formattedDate;
  };

  const docObj = [
    {
      status: rowData.workflow?.action,
      details: {
        applicationType: rowData.artifactType,
        applicationSentOn: getDate(parseInt(rowData.auditdetails.createdTime)),
        sender: rowData.owner,
        additionalDetails: rowData.additionalDetails,
        applicationId: rowData.id,
        auditDetails: rowData.auditDetails,
      },
      applicationContent: {
        tenantId: rowData.tenantId,
        fileStoreId: rowData.file?.fileStore,
        id: rowData.file?.id,
        documentType: rowData.file?.documentType,
        documentUid: rowData.file?.documentUid,
        additionalDetails: rowData.file?.additionalDetails,
      },
      comments: rowData.comments,
      artifactList: rowData,
    },
  ];

  // const message = () => ;

  return (
    <React.Fragment>
      <div className="fack-check-icon" onClick={() => colData?.clickFunc(docObj)}>
        {userRoles?.includes("JUDGE_ROLE") && (
          <ReactTooltip id={`mark-unmark-tooltip-${rowData.artifactNumber}`} place="left">
            {t(rowData.isEvidence ? "UNMARK_EVIDENCE_TOOLTIP" : "MARK_EVIDENCE_TOOLTIP")}
          </ReactTooltip>
        )}
        {showAsHeading ? (
          <div style={{ textDecoration: "underline", cursor: "pointer" }}>{t(value)}</div>
        ) : rowData.isEvidence ? (
          <span data-tip data-for={`mark-unmark-tooltip-${rowData.artifactNumber}`} style={{ cursor: "pointer" }}>
            <FactCrossIcon />
          </span>
        ) : (
          <span data-tip data-for={`mark-unmark-tooltip-${rowData.artifactNumber}`} style={{ cursor: "pointer" }}>
            <FactCheckIcon />
          </span>
        )}
      </div>
    </React.Fragment>
  );
};
