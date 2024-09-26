import { Card } from "@egovernments/digit-ui-react-components";
import React, { useState } from "react";
import { useTranslation } from "react-i18next";
import useGetSubmissions from "../../../hooks/dristi/useGetSubmissions";
import { CustomArrowOut } from "../../../icons/svgIndex";
import EvidenceModal from "./EvidenceModal";
import { useGetPendingTask } from "../../../../../home/src/hooks/useGetPendingTask";
import { useHistory } from "react-router-dom";

const SubmissionReview = ({ caseData, setUpdateCounter, openSubmissionsViewModal }) => {
  const { t } = useTranslation();
  const filingNumber = caseData.filingNumber;
  const tenantId = window?.Digit.ULBService.getCurrentTenantId();
  const [show, setShow] = useState(false);
  const [documentSubmission, setDocumentSubmission] = useState();
  const [comment, setComment] = useState("");
  const userInfo = Digit.UserService.getUser()?.info;
  const userRoles = userInfo?.roles.map((role) => role.code);
  const { caseId } = Digit.Hooks.useQueryParams();
  const history = useHistory();
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
      comments: application?.comment ? application?.comment : [],
      applicationList: application,
    };
    const docObj = application?.documents?.map((doc) => {
      return {
        status: application?.status,
        details: {
          applicationType: application?.applicationType,
          applicationSentOn: getDate(parseInt(application?.auditDetails.createdTime)),
          sender: caseData?.case?.additionalDetails?.payerName,
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
        comments: application?.comment ? application?.comment : [],
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
          entityType: "application-order-submission-feedback",
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
          entityType: "application-order-submission-default",
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

  let applicationsPending = [];
  if (pendingTaskDetails && pendingTaskDetailsWithout) {
    applicationsPending = [
      pendingTaskDetails.data?.map((app) =>
        app.fields.reduce(
          (fieldObj, item) =>
            item.key === "name"
              ? {
                  ...fieldObj,
                  applicationType: item.value,
                }
              : {
                  ...fieldObj,
                  [item.key]: item.value,
                },
          {}
        )
      ),
      pendingTaskDetailsWithout.data?.map((app) =>
        app.fields.reduce(
          (fieldObj, item) => ({
            ...fieldObj,
            [item.key]: item.value,
          }),
          {}
        )
      ),
    ].flat(Infinity);
  }
  const applicationListToShow = userRoles.includes("CITIZEN")
    ? applicationsPending
    : applicationRes?.applicationList?.filter((application) => application.status === "PENDINGREVIEW");

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
              onClick={() => {
                userRoles.includes("CITIZEN")
                  ? history.push(
                      `/digit-ui/citizen/submissions/submissions-create?filingNumber=${filingNumber}&orderNumber=${app.referenceId.split("_").pop()}`
                    )
                  : docSetFunc(app);
              }}
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
                  {t(app?.applicationType || app?.name)}
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
                    marginLeft: "2px",
                  }}
                >
                  {app?.stateSla
                    ? new Date(app?.stateSla).toLocaleDateString("en-in", {
                        year: "numeric",
                        month: "long",
                        day: "numeric",
                      })
                    : "N/A"}
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
          caseId={caseId}
        />
      )}
    </React.Fragment>
  );
};

export default SubmissionReview;
