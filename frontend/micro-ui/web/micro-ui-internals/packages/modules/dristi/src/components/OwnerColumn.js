import React from "react";
import { FactCheckIcon } from "../icons/svgIndex";

export const OwnerColumn = ({ rowData, colData, value = "", showAsHeading = false, t }) => {

  const getDate = (value) => {
    const date = new Date(value);
    const day = date.getDate().toString().padStart(2, "0");
    const month = (date.getMonth() + 1).toString().padStart(2, "0"); // Month is zero-based
    const year = date.getFullYear();
    const formattedDate = `${day}-${month}-${year}`;
    return formattedDate;
  }

  const docObj = rowData.documents.map((doc) => {
    return {
      status: rowData.workflow.action,
      details: {
        applicationType: rowData.applicationType,
        applicationSentOn: getDate(parseInt(rowData.auditDetails.createdTime)),
        sender: rowData.createdBy,
        additionalDetails: rowData.additionalDetails,
        applicationId: rowData.id,
      },
      applicationContent: {
        tenantId: rowData.tenantId,
        fileStoreId: doc.fileStore,
        fileName: doc.documentName,
      },
      comments: []
    }
  })

  
  return (
    <React.Fragment>
      <div className="fack-check-icon" onClick={() => colData?.clickFunc(docObj)}>
        {showAsHeading ? <div style={{ fontWeight: "bold", textDecoration: "underline" }}>{value}</div> : <FactCheckIcon />}
      </div>
    </React.Fragment>
  );
};