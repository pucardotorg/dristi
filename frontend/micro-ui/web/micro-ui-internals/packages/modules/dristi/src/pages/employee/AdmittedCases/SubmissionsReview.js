import { Card } from "@egovernments/digit-ui-react-components";
import React, { useState } from "react";
import { useTranslation } from "react-i18next";
import useGetSubmissions from "../../../hooks/dristi/useGetSubmissions";
import { CustomArrowOut } from "../../../icons/svgIndex";
import EvidenceModal from "./EvidenceModal";
import { useGetPendingTask } from "../../../../../home/src/hooks/useGetPendingTask";

const SubmissionReview = ({ caseData, setUpdateCounter, openSubmissionsViewModal }) => {
  const { t } = useTranslation();
  const filingNumber = caseData.filingNumber;
  const tenantId = window?.Digit.ULBService.getCurrentTenantId();
  const [show, setShow] = useState(false);
  const [documentSubmission, setDocumentSubmission] = useState();
  const [comment, setComment] = useState("");
  const userInfo = Digit.UserService.getUser()?.info;
  const userRoles = userInfo?.roles.map((role) => role.code);

  const getDate = (value) => {
    const date = new Date(value);
    const day = date.getDate().toString().padStart(2, "0");
    const month = (date.getMonth() + 1).toString().padStart(2, "0"); // Month is zero-based
    const year = date.getFullYear();
    const formattedDate = `${day}-${month}-${year}`;
    return formattedDate;
  };

  const docSetFunc = (application) => {
    const defaultObj = {
      status: application?.status,
      details: {
        applicationType: application?.applicationType,
        applicationSentOn: getDate(parseInt(application?.auditDetails.createdTime)),
        sender: application?.createdBy,
        additionalDetails: application?.additionalDetails,
        applicationId: application?.id,
        auditDetails: application?.auditDetails,
      },
      applicationContent: null,
      comments: application?.comment ? JSON.parse(application?.comment) : [],
      applicationList: application,
    };
    const docObj = application?.documents?.map((doc) => {
      return {
        status: application?.status,
        details: {
          applicationType: application?.applicationType,
          applicationSentOn: getDate(parseInt(application?.auditDetails.createdTime)),
          sender: application?.createdBy,
          additionalDetails: application?.additionalDetails,
          applicationId: application?.id,
          auditDetails: application?.auditDetails,
        },
        applicationContent: {
          tenantId: application?.tenantId,
          fileStoreId: doc.fileStore,
          id: doc.id,
          documentType: doc.documentType,
          documentUid: doc.documentUid,
          additionalDetails: doc.additionalDetails,
        },
        comments: application?.comment ? JSON.parse(application?.comment) : [],
        applicationList: application,
      };
    }) || [defaultObj];
    // const docObj = application?.documents?.map((doc) => {
    //   return {
    //     status: application.workflow.action,
    //     details: {
    //       applicationType: application.applicationType,
    //       applicationSentOn: getDate(parseInt(application.auditDetails.createdTime)),
    //       sender: application.createdBy,
    //       additionalDetails: application.additionalDetails,
    //       applicationId: application.id,
    //       auditDetails: application.auditDetails,
    //     },
    //     applicationContent: {
    //       tenantId: application.tenantId,
    //       fileStoreId: doc.fileStore,
    //       id: doc.id,
    //       documentType: doc.documentType,
    //       documentUid: doc.documentUid,
    //       additionalDetails: doc.additionalDetails,
    //     },
    //     comments: [],
    //     applicationList: application,
    //   };
    // });
    setDocumentSubmission(docObj);
    console.log(docObj);
    setShow(true);
  };

  const { data: applicationRes } = useGetSubmissions(
    {
      criteria: {
        filingNumber: filingNumber,
        tenantId: tenantId,
      },
    },
    {},
    filingNumber,
    filingNumber
  );

  const { data: pendingTaskDetails = [] } = useGetPendingTask({
    data: {
      SearchCriteria: {
        tenantId,
        moduleName: "Pending Tasks Service",
        moduleSearchCriteria: {
          entityType: "asynsubmissionwithresponse",
          filingNumber: filingNumber,
          isCompleted: false,
          assignedTo: userInfo?.uuid,
        },
        limit: 10000,
        offset: 0,
      },
    },
    params: { tenantId },
    key: "With",
    config: { enable: true },
  });
  const { data: pendingTaskDetailsWithout = [] } = useGetPendingTask({
    data: {
      SearchCriteria: {
        tenantId,
        moduleName: "Pending Tasks Service",
        moduleSearchCriteria: {
          entityType: "asyncsubmissionwithoutresponse",
          filingNumber: filingNumber,
          isCompleted: false,
          assignedTo: userInfo?.uuid,
        },
        limit: 10000,
        offset: 0,
      },
    },
    params: { tenantId },
    key: "Without",
    config: { enable: true },
  });

  const applicationListToShow = userRoles.includes("CITIZEN")
    ? applicationRes?.applicationList?.filter((application) => application.status === "PENDINGSUBMISSION")
    : applicationRes?.applicationList?.filter((application) => application.status === "PENDINGREVIEW");

  console.log(applicationListToShow);

  return (
    <React.Fragment>
      <Card
        style={{
          width: "100%",
          marginTop: "10px",
        }}
      >
        <div style={{ display: "flex", justifyContent: "space-between", alignItems: "center" }}>
          <div
            style={{
              fontWeight: 700,
              fontSize: "24px",
              lineHeight: "28.8px",
              color: "#231F20",
            }}
          >
            {userRoles.includes("CITIZEN") ? t("PENDING_SUBMISSIONS_HEADER") : t("REVIEW_SUBMISSIONS_HEADER")} ({applicationListToShow?.length})
          </div>
          <div
            onClick={() => openSubmissionsViewModal(applicationListToShow, docSetFunc)}
            style={{ cursor: "pointer", fontWeight: 500, fontSize: "16px", lineHeight: "20px", color: "#0A5757" }}
          >
            {t("VIEW_ALL_LINK")}
          </div>
        </div>
        <div style={{ display: "flex", gap: "16px", marginTop: "10px" }}>
          {applicationListToShow?.slice(0, 5).map((app) => (
            <div
              style={{
                padding: "12px 16px",
                borderRadius: "4px",
                width: "300px",
                cursor: "pointer",
                background: "#ECF3FD66",
              }}
              onClick={() => docSetFunc(app)}
            >
              <div style={{ width: "100%", display: "flex", justifyContent: "space-between" }}>
                <div
                  style={{
                    fontWeight: 700,
                    fontSize: "16px",
                    lineHeight: "18.75px",
                    color: "#101828",
                  }}
                >
                  {app?.applicationType?.charAt(0).toUpperCase()}
                  {app?.applicationType?.slice(1).toLowerCase()}
                </div>
                <CustomArrowOut />
              </div>
              <div
                style={{
                  fontWeight: 600,
                  fontSize: "14px",
                  lineHeight: "20px",
                  color: "#101828",
                  marginTop: "12px",
                }}
              >
                Date:
                <span
                  style={{
                    fontWeight: 500,
                    fontSize: "14px",
                    lineHeight: "20px",
                  }}
                >
                  {new Date(app?.auditDetails?.createdTime).toLocaleDateString("en-in", {
                    year: "numeric",
                    month: "long",
                    day: "numeric",
                  })}
                </span>
              </div>
            </div>
          ))}
        </div>
      </Card>
      {show && (
        <EvidenceModal
          documentSubmission={documentSubmission}
          show={show}
          setShow={setShow}
          comment={comment}
          setComment={setComment}
          userRoles={userRoles}
          modalType={"Submissions"}
          setUpdateCounter={setUpdateCounter}
          caseData={caseData}
        />
      )}
    </React.Fragment>
  );
};

export default SubmissionReview;
