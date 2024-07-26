import { Card } from "@egovernments/digit-ui-react-components";
import React, { useState } from "react";
import { useTranslation } from "react-i18next";
import useGetSubmissions from "../../../hooks/dristi/useGetSubmissions";
import { CustomArrowOut } from "../../../icons/svgIndex";
import EvidenceModal from "./EvidenceModal";

const SubmissionReview = ({ caseData, setUpdateCounter }) => {
  const { t } = useTranslation();
  const filingNumber = caseData.filingNumber;
  const cnr = caseData.cnrNumber;
  const tenantId = window?.Digit.ULBService.getCurrentTenantId();
  const [show, setShow] = useState(false);
  const [documentSubmission, setDocumentSubmission] = useState();
  const [comment, setComment] = useState("");
  const user = localStorage.getItem("user-info");
  const userRoles = JSON.parse(user).roles.map((role) => role.code);

  const getDate = (value) => {
    const date = new Date(value);
    const day = date.getDate().toString().padStart(2, "0");
    const month = (date.getMonth() + 1).toString().padStart(2, "0"); // Month is zero-based
    const year = date.getFullYear();
    const formattedDate = `${day}-${month}-${year}`;
    return formattedDate;
  };

  const docSetFunc = (application) => {
    const docObj = application?.documents?.map((doc) => {
      return {
        status: application.workflow.action,
        details: {
          applicationType: application.applicationType,
          applicationSentOn: getDate(parseInt(application.auditDetails.createdTime)),
          sender: application.createdBy,
          additionalDetails: application.additionalDetails,
          applicationId: application.id,
          auditDetails: application.auditDetails,
        },
        applicationContent: {
          tenantId: application.tenantId,
          fileStoreId: doc.fileStore,
          id: doc.id,
          documentType: doc.documentType,
          documentUid: doc.documentUid,
          additionalDetails: doc.additionalDetails,
        },
        comments: [],
        applicationList: application,
      };
    });
    setDocumentSubmission(docObj);
    console.log(docObj);
    setShow(true);
  };

  const { data: applicationRes, refetch: refetchApplicationData, isLoading: isApplicationLoading } = useGetSubmissions(
    {
      criteria: {
        filingNumber: filingNumber,
        tenantId: tenantId,
      },
    },
    {},
    cnr + filingNumber,
    true
  );

  console.log(applicationRes);

  return (
    <React.Fragment>
      <Card
        style={{
          width: "100%",
          marginTop: "10px",
        }}
      >
        <div
          style={{
            fontWeight: 700,
            fontSize: "24px",
            lineHeight: "28.8px",
            color: "#231F20",
          }}
        >
          Submissions To Review ({applicationRes?.applicationList?.filter((application) => application.workflow.action === "PENDINGREVIEW").length})
        </div>
        <div style={{ display: "flex", gap: "16px", marginTop: "10px" }}>
          {applicationRes?.applicationList
            ?.filter((application) => application.workflow.action === "PENDINGREVIEW")
            .slice(0, 5)
            .map((app) => (
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
