import { CloseSvg } from "@egovernments/digit-ui-react-components";
import React from "react";
import { useTranslation } from "react-i18next";
import Modal from "../../../components/Modal";
import useGetEvidence from "../../../hooks/dristi/useGetEvidence";
import DocViewerWrapper from "../docViewerWrapper";

const EvidenceModal = ({ documentSubmission, setShow, comment, setComment, userRoles }) => {
  const { t } = useTranslation();
  const searchParams = new URLSearchParams(location.search);
  const filingNumber = searchParams.get("filingNumber");
  const cnr = searchParams.get("cnr");
  const title = searchParams.get("title");
  const caseId = searchParams.get("caseId");

  const CloseBtn = (props) => {
    return (
      <div onClick={props?.onClick} style={{ height: "100%", display: "flex", alignItems: "center", paddingRight: "20px", cursor: "pointer" }}>
        <CloseSvg />
      </div>
    );
  };
  const Heading = (props) => {
    return (
      <div className="evidence-title">
        <h1 className="heading-m">{props.label}</h1>
        <h3 className="status">{props.status}</h3>
      </div>
    );
  };

  console.log(documentSubmission);

  const tenantId = window?.Digit.ULBService.getCurrentTenantId();

  const { data: caseFetchResponse, refetch: refetchCaseData, isLoading } = useGetEvidence(
    {
      criteria: {
        applicationId: documentSubmission[0].details.applicationId,
      },
      tenantId: tenantId,
    },
    cnr + filingNumber,
    true
  );

  console.log(documentSubmission);
  const markAsReadPayload = {
    artifact: {
      tenantId: tenantId,
      caseId: caseId,
      application: documentSubmission[0].details.applicationId,
      isActive: true,
      isEvidence: true,
      status: documentSubmission[0].status,
      file: documentSubmission.map((doc) => {
        return {
          id: doc?.applicationContent?.id,
          documentType: doc?.applicationContent?.documentType,
          fileStore: doc?.applicationContent?.fileStoreId,
          documentUid: doc?.applicationContent?.documentUid,
          additionalDetails: doc?.applicationContent?.additionalDetails,
        };
      }),
      comments: [],
      auditDetails: documentSubmission[0].details.auditDetails,
      workflow: {
        comments: "Workflow comments",
        documents: [{}],
        assignes: null,
        rating: null,
        action: "TYPE DEPOSITION",
      },
    },
  };
  console.log(markAsReadPayload);

  return (
    <Modal
      headerBarEnd={<CloseBtn onClick={() => setShow(false)} />}
      actionSaveLabel={
        userRoles.indexOf("APPLICATION_APPROVER") !== -1 && documentSubmission[0].status === "PENDINGREVIEW"
          ? t("Approve")
          : (userRoles.indexOf("APPLICATION_APPROVER") !== -1 ||
              userRoles.indexOf("DEPOSITION_CREATOR") !== -1 ||
              userRoles.indexOf("DEPOSITION_ESIGN") !== -1 ||
              userRoles.indexOf("DEPOSITION_PUBLISHER") !== -1) &&
            documentSubmission[0].status !== "PENDINGREVIEW"
          ? t("Mark as Evidence")
          : null
      }
      actionSaveOnSubmit={() => {
        if (
          (userRoles.indexOf("APPLICATION_APPROVER") !== -1 ||
            userRoles.indexOf("DEPOSITION_CREATOR") !== -1 ||
            userRoles.indexOf("DEPOSITION_ESIGN") !== -1 ||
            userRoles.indexOf("DEPOSITION_PUBLISHER") !== -1) &&
          documentSubmission[0].status !== "PENDINGREVIEW"
        ) {
        }
        setShow(false);
      }}
      hideSubmit={userRoles.indexOf("APPLICATION_APPROVER") !== -1 && documentSubmission.status === "PENDINGREVIEW"}
      actionCancelLabel={userRoles.indexOf("APPLICATION_APPROVER") !== -1 && documentSubmission[0].status === "PENDINGREVIEW" ? t("Reject") : null}
      actionCancelOnSubmit={() => {
        setShow(false);
      }}
      formId="modal-action"
      headerBarMain={<Heading label={t("Document Submission")} status={documentSubmission.status} />}
      className="evidence-modal"
    >
      {documentSubmission.map((docSubmission) => (
        <div className="evidence-modal-main">
          <div className="application-details">
            <div className="application-info">
              <div className="info-row">
                <div className="info-key">
                  <h3>Application Type</h3>
                </div>
                <div className="info-value">
                  <h3>{docSubmission?.details?.applicationType}</h3>
                </div>
              </div>
              <div className="info-row">
                <div className="info-key">
                  <h3>Application Sent On</h3>
                </div>
                <div className="info-value">
                  <h3>{docSubmission.details.applicationSentOn}</h3>
                </div>
              </div>
              <div className="info-row">
                <div className="info-key">
                  <h3>Sender</h3>
                </div>
                <div className="info-value">
                  <h3>{docSubmission.details.sender}</h3>
                </div>
              </div>
              <div className="info-row">
                <div className="info-key">
                  <h3>Additional Details</h3>
                </div>
                <div className="info-value">
                  <h3>{JSON.stringify(docSubmission.details.additionalDetails)}</h3>
                </div>
              </div>
            </div>
            <div className="application-view">
              <DocViewerWrapper
                key={docSubmission.applicationContent.fileStoreId}
                fileStoreId={docSubmission.applicationContent.fileStoreId}
                displayFilename={docSubmission.applicationContent.fileName}
                tenantId={docSubmission.applicationContent.tenantId}
                docWidth="100%"
                docHeight="unset"
                showDownloadOption={false}
                documentName={docSubmission.applicationContent.fileName}
              />
            </div>
          </div>
          {/* <div className="application-comment">
        <div className="comment-section">
          <h1 className="comment-xyzoo">Comments</h1>
          <div className="comment-main">
            {docSubmission.comments.map((comment, index) => (
              <CommentComponent key={index} comment={comment} />
            ))}
          </div>
        </div>
        <div className="comment-send">
          <div className="comment-input-wrapper">
            <TextInput
              placeholder={"Type here..."}
              value={comment}
              onChange={(e) => {
                setComment(e.target.value);
              }}
            />
            <div className="send-comment-btn">
              <RightArrow />
            </div>
          </div>
        </div>
      </div> */}
        </div>
      ))}
    </Modal>
  );
};

export default EvidenceModal;
