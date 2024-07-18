import React from "react";
import { FactCheckIcon, FactCrossIcon } from "../icons/svgIndex";

export const Evidence = ({ rowData, colData, value = "", showAsHeading = false, t }) => {
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
        sender: rowData.auditdetails.createdBy,
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
      comments: [],
      artifactList: rowData,
    },
  ];

  return (
    <React.Fragment>
      <div className="fack-check-icon" onClick={() => colData?.clickFunc(docObj)}>
        {showAsHeading ? (
          <div style={{ fontWeight: "bold", textDecoration: "underline" }}>{value}</div>
        ) : rowData.isEvidence ? (
          <FactCrossIcon />
        ) : (
          <FactCheckIcon />
        )}
      </div>
    </React.Fragment>
  );
};
