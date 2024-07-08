import { CloseSvg, TextInput } from "@egovernments/digit-ui-react-components";
import React, { useMemo, useState } from "react";
import { useTranslation } from "react-i18next";
import Modal from "../../../components/Modal";
import DocViewerWrapper from "../docViewerWrapper";
import { RightArrow } from "../../../icons/svgIndex";
import CommentComponent from "../../../components/CommentComponent";
import ConfirmSubmissionAction from "../../../components/ConfirmSubmissionAction";
import { useHistory } from "react-router-dom/cjs/react-router-dom.min";
import { ordersService } from "../../../../../orders/src/hooks/services";
import { CaseWorkflowAction } from "../../../../../orders/src/utils/caseWorkflow";
import SubmissionSuccessModal from "../../../components/SubmissionSuccessModal";
import ConfirmEvidenceAction from "../../../components/ConfirmEvidenceAction";

const EvidenceModal = ({ documentSubmission, setShow, comment, setComment, userRoles, modalType, setUpdateCounter, showToast }) => {
  const [showConfirmationModal, setShowConfirmationModal] = useState(null);
  const [showSuccessModal, setShowSuccessModal] = useState(null);
  const history = useHistory();
  const urlParams = new URLSearchParams(window.location.search);
  const filingNumber = urlParams.get("filingNumber");
  const cnrNumber = urlParams.get("cnrNumber");
  const { t } = useTranslation();
  const tenantId = window?.Digit.ULBService.getCurrentTenantId();

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
  const hideSubmit = useMemo(() => {
    return userRoles.includes("APPLICATION_RESPONDER") && documentSubmission.status === "PENDINGREVIEW";
  }, [documentSubmission.status, userRoles]);

  const actionSaveLabel = useMemo(() => {
    return userRoles.includes("APPLICATION_RESPONDER") && documentSubmission[0].status === "PENDINGREVIEW" && modalType === "Submissions"
      ? t("Approve")
      : (userRoles.includes("APPLICATION_RESPONDER") ||
          userRoles.includes("DEPOSITION_CREATOR") ||
          userRoles.includes("DEPOSITION_ESIGN") ||
          userRoles.includes("DEPOSITION_PUBLISHER")) &&
        documentSubmission[0].status !== "PENDINGREVIEW" &&
        modalType === "Documents"
      ? !documentSubmission[0]?.artifactList.isEvidence
        ? t("Mark as Evidence")
        : documentSubmission[0]?.artifactList.artifactType !== "DEPOSITION"
        ? t("Unmark as Evidence")
        : null
      : null;
  }, [documentSubmission, modalType, t, userRoles]);

  const actionCancelLabel = useMemo(() => {
    return userRoles.includes("WORKFLOW_ABANDON") && documentSubmission[0].status === "PENDINGREVIEW" && modalType === "Submissions"
      ? t("Reject")
      : null;
  }, [documentSubmission, modalType, t, userRoles]);

  const reqCreate = {
    url: `/application/application/v1/update`,
    params: {},
    body: {},
    config: {
      enable: false,
    },
  };
  const reqEvidenceUpdate = {
    url: `/evidence/artifacts/v1/_update`,
    params: {},
    body: {},
    config: {
      enable: false,
    },
  };

  const mutation = Digit.Hooks.useCustomAPIMutationHook(reqCreate);
  const evidenceUpdateMutation = Digit.Hooks.useCustomAPIMutationHook(reqEvidenceUpdate);

  // const markAsReadPayload = {
  //   tenantId: tenantId,
  //   artifact: {
  //     tenantId: tenantId,
  //     caseId: caseId,
  //     artifactType: "AFFIDAVIT",
  //     sourceType: "COURT",
  //     application: documentSubmission[0].details.applicationId,
  //     isActive: true,
  //     isEvidence: true,
  //     status: documentSubmission[0].status,
  //     file: documentSubmission.map((doc) => {
  //       return {
  //         id: doc?.applicationContent?.id,
  //         documentType: doc?.applicationContent?.documentType,
  //         fileStore: doc?.applicationContent?.fileStoreId,
  //         documentUid: doc?.applicationContent?.documentUid,
  //         additionalDetails: doc?.applicationContent?.additionalDetails,
  //       };
  //     }),
  //     comments: [],
  //     auditDetails: documentSubmission[0].details.auditDetails,
  //     workflow: {
  //       comments: documentSubmission[0]?.applicationList?.workflow.comments,
  //       documents: [{}],
  //       id: documentSubmission[0]?.applicationList?.workflow.id,
  //       status: documentSubmission[0]?.applicationList?.workflow.status,
  //       action: "TYPE DEPOSITION",
  //     },
  //   },
  // };

  const acceptApplicationPayload = {
    ...documentSubmission[0].applicationList,
    workflow: {
      ...documentSubmission[0].applicationList?.workflow,
      action: "REQUESTFILERESPONSE",
    },
  };

  const rejectApplicationPayload = {
    ...documentSubmission[0].applicationList,
    workflow: {
      ...documentSubmission[0].applicationList?.workflow,
      action: "ABANDON",
    },
  };
  const handleRejectApplication = async () => {
    await mutation.mutate({
      url: `/application/application/v1/update`,
      params: {},
      body: { application: rejectApplicationPayload },
      config: {
        enable: true,
      },
    });
    setUpdateCounter((prevCount) => prevCount + 1);
  };

  const handleAcceptApplication = async () => {
    if (
      (userRoles.includes("APPLICATION_RESPONDER") ||
        userRoles.includes("DEPOSITION_CREATOR") ||
        userRoles.includes("DEPOSITION_ESIGN") ||
        userRoles.includes("DEPOSITION_PUBLISHER")) &&
      documentSubmission[0].status !== "PENDINGREVIEW" &&
      modalType === "Documents"
    ) {
      if (documentSubmission[0].artifactList.artifactType === "DEPOSITION") {
        await evidenceUpdateMutation.mutate({
          url: `/evidence/artifacts/v1/_update`,
          params: {},
          body: {
            artifact: {
              ...documentSubmission[0].artifactList,
              isEvidence: !documentSubmission[0].artifactList.isEvidence,
              workflow: {
                ...documentSubmission[0].artifactList.workflow,
                action: "SIGN DEPOSITION",
              },
            },
          },
          config: {
            enable: true,
          },
        });
      } else {
        await evidenceUpdateMutation.mutate({
          url: `/evidence/artifacts/v1/_update`,
          params: {},
          body: {
            artifact: {
              ...documentSubmission[0].artifactList,
              isEvidence: !documentSubmission[0].artifactList.isEvidence,
            },
          },
          config: {
            enable: true,
          },
        });
      }
    } else if (userRoles.includes("APPLICATION_RESPONDER") && documentSubmission[0].status === "PENDINGREVIEW" && modalType === "Submissions") {
      await mutation.mutate({
        url: `/application/application/v1/update`,
        params: {},
        body: { application: acceptApplicationPayload },
        config: {
          enable: true,
        },
      });
    }
    setUpdateCounter((prevCount) => prevCount + 1);
  };

  const formatDate = (date) => {
    const day = String(date.getDate()).padStart(2, "0");
    const month = String(date.getMonth() + 1).padStart(2, "0");
    const year = date.getFullYear();
    return `${day}-${month}-${year}`;
  };

  const handleAction = async (generateOrder) => {
    try {
      if (showConfirmationModal.type === "reject") {
        await handleRejectApplication();
      }
      if (showConfirmationModal.type === "accept" || showConfirmationModal.type === "documents-confirmation") {
        await handleAcceptApplication();
      }
      if (!generateOrder) {
        setShowConfirmationModal(null);
        if (modalType !== "Documents") {
          setShowSuccessModal(true);
        } else {
          setShow(false);
          showToast();
        }
      }
      if (generateOrder) {
        const reqbody = {
          order: {
            createdDate: formatDate(new Date()),
            tenantId,
            cnrNumber,
            filingNumber,
            statuteSection: {
              tenantId,
            },
            orderType: showConfirmationModal?.type === "reject" ? "REJECT_VOLUNTARY_SUBMISSIONS" : "APPROVE_VOLUNTARY_SUBMISSIONS",
            status: "",
            isActive: true,
            workflow: {
              action: CaseWorkflowAction.SAVE_DRAFT,
              comments: "Creating order",
              assignes: null,
              rating: null,
              documents: [{}],
            },
            documents: [],
            additionalDetails: {},
          },
        };
        ordersService
          .createOrder(reqbody, { tenantId })
          .then(() => {
            history.push(
              `/${window.contextPath}/employee/orders/generate-orders?filingNumber=${filingNumber}&applicationNumber=${documentSubmission?.[0]?.applicationList?.applicationNumber}`
            );
          })
          .catch((err) => {});
      }
    } catch (error) {}
  };
  return (
    <React.Fragment>
      {!showConfirmationModal && !showSuccessModal && (
        <Modal
          headerBarEnd={<CloseBtn onClick={() => setShow(false)} />}
          actionSaveLabel={actionSaveLabel}
          actionSaveOnSubmit={() => {
            modalType === "Documents" ? setShowConfirmationModal({ type: "documents-confirmation" }) : setShowConfirmationModal({ type: "accept" });
          }}
          hideSubmit={hideSubmit}
          actionCancelLabel={actionCancelLabel}
          actionCancelOnSubmit={() => {
            setShowConfirmationModal({ type: "reject" });
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
              <div className="application-comment">
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
              </div>
            </div>
          ))}
        </Modal>
      )}
      {showConfirmationModal && !showSuccessModal && modalType === "Submissions" && (
        <ConfirmSubmissionAction
          t={t}
          setShowConfirmationModal={setShowConfirmationModal}
          type={showConfirmationModal.type}
          setShow={setShow}
          handleAction={handleAction}
        />
      )}
      {showConfirmationModal && !showSuccessModal && modalType === "Documents" && (
        <ConfirmEvidenceAction
          t={t}
          setShowConfirmationModal={setShowConfirmationModal}
          type={showConfirmationModal.type}
          setShow={setShow}
          handleAction={handleAction}
          isEvidence={documentSubmission[0].artifactList.isEvidence}
        />
      )}
      {showSuccessModal && modalType === "Submissions" && (
        <SubmissionSuccessModal
          t={t}
          handleBack={() => {
            setShow(false);
          }}
        />
      )}
    </React.Fragment>
  );
};

export default EvidenceModal;
