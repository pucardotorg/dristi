import React from "react";
import { FactCheckIcon } from "../icons/svgIndex";
import { SubmissionWorkflowState } from "../Utils/submissionWorkflow";

export const OwnerColumn = ({ rowData, colData, value = "", showAsHeading = false, t }) => {
  const userInfo = Digit.UserService.getUser()?.info;
  const userRoles = userInfo?.roles?.map((role) => role.code);
  const getDate = (value) => {
    const date = new Date(value);
    const day = date.getDate().toString().padStart(2, "0");
    const month = (date.getMonth() + 1).toString().padStart(2, "0"); // Month is zero-based
    const year = date.getFullYear();
    const formattedDate = `${day}-${month}-${year}`;
    return formattedDate;
  };

  const defaultObj = {
    status: rowData?.status,
    details: {
      applicationType: rowData?.applicationType,
      applicationSentOn: getDate(parseInt(rowData?.auditDetails.createdTime)),
      sender: rowData?.owner,
      additionalDetails: rowData?.additionalDetails,
      applicationId: rowData?.id,
      auditDetails: rowData?.auditDetails,
      referenceId: rowData?.referenceId,
    },
    applicationContent: null,
    comments: rowData?.comment || [],
    applicationList: rowData,
  };
  const docObj = rowData?.documents?.map((doc) => {
    return {
      status: rowData?.status,
      details: {
        applicationType: rowData?.applicationType,
        applicationSentOn: getDate(parseInt(rowData?.auditDetails.createdTime)),
        sender: rowData?.owner,
        additionalDetails: rowData?.additionalDetails,
        applicationId: rowData?.id,
        auditDetails: rowData?.auditDetails,
        referenceId: rowData?.referenceId,
      },
      applicationContent: {
        tenantId: rowData?.tenantId,
        fileStoreId: doc.fileStore,
        id: doc.id,
        documentType: doc.documentType,
        documentUid: doc.documentUid,
        additionalDetails: doc.additionalDetails,
      },
      comments: rowData?.comment || [],
      applicationList: rowData,
    };
  }) || [defaultObj];

  const createdByUuid = rowData.statuteSection?.auditdetails?.createdBy;
  const respondingUuids = rowData?.additionalDetails?.respondingParty?.map((party) => party?.uuid.map((uuid) => uuid)).flat();

  const showDoc =
    ([
      SubmissionWorkflowState.PENDINGREVIEW,
      SubmissionWorkflowState.PENDINGAPPROVAL,
      SubmissionWorkflowState.COMPLETED,
      SubmissionWorkflowState.PENDINGRESPONSE,
    ].includes(rowData?.status) &&
      userRoles.includes("JUDGE_ROLE")) ||
    userInfo?.uuid === createdByUuid ||
    (![SubmissionWorkflowState.PENDINGPAYMENT, SubmissionWorkflowState.PENDINGESIGN, SubmissionWorkflowState.PENDINGSUBMISSION].includes(
      rowData?.status
    ) &&
      respondingUuids?.includes(userInfo?.uuid));

  return (
    <React.Fragment>
      <div className="fack-check-icon" onClick={() => (showDoc ? colData?.clickFunc(docObj) : null)}>
        {showAsHeading ? (
          <div
            style={{
              cursor: "pointer",
              textDecoration: "underline",
            }}
          >
            {t(value)}
          </div>
        ) : (
          showDoc && <FactCheckIcon />
        )}
      </div>
    </React.Fragment>
  );
};
