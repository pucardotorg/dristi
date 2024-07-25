import { CloseSvg, TextInput } from "@egovernments/digit-ui-react-components";
import React, { useMemo, useState } from "react";
import { useTranslation } from "react-i18next";
import { useHistory } from "react-router-dom/cjs/react-router-dom.min";
import CommentComponent from "../../../components/CommentComponent";
import ConfirmEvidenceAction from "../../../components/ConfirmEvidenceAction";
import ConfirmSubmissionAction from "../../../components/ConfirmSubmissionAction";
import Modal from "../../../components/Modal";
import SubmissionSuccessModal from "../../../components/SubmissionSuccessModal";
import { RightArrow } from "../../../icons/svgIndex";
import DocViewerWrapper from "../docViewerWrapper";
import { DRISTIService } from "../../../services";
import { Loader } from "@egovernments/digit-ui-components";
import { Urls } from "../../../hooks";
import { SubmissionWorkflowAction } from "../../../Utils/submissionWorkflow";

const EvidenceModal = ({ caseData, documentSubmission = [], setShow, userRoles, modalType, setUpdateCounter, showToast }) => {
  const [comments, setComments] = useState(documentSubmission[0]?.comments ? documentSubmission[0].comments : []);
  const [showConfirmationModal, setShowConfirmationModal] = useState(null);
  const [showSuccessModal, setShowSuccessModal] = useState(null);
  const [currentComment, setCurrentComment] = useState("");
  const history = useHistory();
  const filingNumber = useMemo(() => caseData?.filingNumber, [caseData]);
  const cnrNumber = useMemo(() => caseData.cnrNumber, [caseData]);
  const { t } = useTranslation();
  const tenantId = window?.Digit.ULBService.getCurrentTenantId();
  const OrderWorkflowAction = Digit.ComponentRegistryService.getComponent("OrderWorkflowActionEnum") || {};
  const ordersService = Digit.ComponentRegistryService.getComponent("OrdersService") || {};
  const user = Digit.UserService.getUser()?.info?.userName;

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
        <h3 className={props.isStatusRed ? "status-false" : "status"}>{props?.status}</h3>
      </div>
    );
  };
  const hideSubmit = useMemo(() => {
    return !userRoles.includes("JUDGE_ROLE") || userRoles.includes("CITIZEN");
  }, [userRoles]);

  const actionSaveLabel = useMemo(() => {
    let label = "";
    if (modalType === "Submissions") {
      label = t("Approve");
    } else {
      label = !documentSubmission?.[0]?.artifactList.isEvidence ? t("MARK_AS_EVIDENCE") : t("UNMARK_AS_EVIDENCE");
    }
    return label;
  }, [documentSubmission, modalType, t]);

  const actionCancelLabel = useMemo(() => {
    return userRoles.includes("WORKFLOW_ABANDON") && documentSubmission?.[0]?.status === "PENDINGREVIEW" && modalType === "Submissions"
      ? t("REJECT")
      : null;
  }, [documentSubmission, modalType, t, userRoles]);

  const reqCreate = {
    url: `/application/v1/update`,
    params: {},
    body: {},
    config: {
      enable: false,
    },
  };
  const reqEvidenceUpdate = {
    url: Urls.dristi.evidenceUpdate,
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
  //     application: documentSubmission[0]?.details.applicationId,
  //     isActive: true,
  //     isEvidence: true,
  //     status: documentSubmission[0]?.status,
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
  //     auditDetails: documentSubmission[0]?.details.auditDetails,
  //     workflow: {
  //       comments: documentSubmission[0]?.applicationList?.workflow.comments,
  //       documents: [{}],
  //       id: documentSubmission[0]?.applicationList?.workflow.id,
  //       status: documentSubmission[0]?.applicationList?.workflow?.status,
  //       action: "TYPE DEPOSITION",
  //     },
  //   },
  // };

  const acceptApplicationPayload = {
    ...documentSubmission?.[0]?.applicationList,
    workflow: {
      ...documentSubmission?.[0]?.applicationList?.workflow,
      action: SubmissionWorkflowAction.APPROVE,
    },
  };

  const rejectApplicationPayload = {
    ...documentSubmission?.[0]?.applicationList,
    workflow: {
      ...documentSubmission?.[0]?.applicationList?.workflow,
      action: SubmissionWorkflowAction.REJECT,
    },
  };

  const applicationCommentsPayload = (newComment) => {
    return {
      ...documentSubmission[0]?.applicationList,
      statuteSection: { ...documentSubmission[0]?.applicationList.statuteSection, tenantId: tenantId },
      comment: documentSubmission[0]?.applicationList.comment
        ? JSON.stringify([...documentSubmission[0]?.applicationList.comment, newComment])
        : JSON.stringify([newComment]),
      workflow: {
        ...documentSubmission[0]?.applicationList?.workflow,
        action: "RESPOND",
      },
    };
  };

  const onSuccess = async (result) => {
    setShow(false);
    const details = showToast({
      isError: false,
      message: documentSubmission?.[0].artifactList.isEvidence ? "SUCCESSFULLY_UNMARKED_MESSAGE" : "SUCCESSFULLY_MARKED_MESSAGE",
    });
    counterUpdate();
  };
  const onError = async (result) => {
    setShow(false);
    const details = showToast({
      isError: true,
      message: documentSubmission?.[0].artifactList.isEvidence ? "UNSUCCESSFULLY_UNMARKED_MESSAGE" : "UNSUCCESSFULLY_MARKED_MESSAGE",
    });
  };

  const counterUpdate = () => {
    setUpdateCounter((prevCount) => prevCount + 1);
  };

  const handleMarkEvidence = async () => {
    if (documentSubmission?.[0].artifactList.artifactType === "DEPOSITION") {
      await evidenceUpdateMutation.mutate(
        {
          url: Urls.dristi.evidenceUpdate,
          params: {},
          body: {
            artifact: {
              ...documentSubmission?.[0].artifactList,
              isEvidence: !documentSubmission?.[0].artifactList.isEvidence,
              workflow: {
                ...documentSubmission?.[0].artifactList.workflow,
                action: "SIGN DEPOSITION",
              },
            },
          },
          config: {
            enable: true,
          },
        },
        {
          onSuccess,
          onError,
        }
      );
    } else {
      await evidenceUpdateMutation.mutate(
        {
          url: Urls.dristi.evidenceUpdate,
          params: {},
          body: {
            artifact: {
              ...documentSubmission?.[0].artifactList,
              isEvidence: !documentSubmission?.[0].artifactList.isEvidence,
              filingNumber: filingNumber,
            },
          },
          config: {
            enable: true,
          },
        },
        {
          onSuccess,
          onError,
        }
      );
    }
  };

  const handleAcceptApplication = async () => {
    await mutation.mutate({
      url: Urls.dristi.submissionsUpdate,
      params: {},
      body: { application: acceptApplicationPayload },
      config: {
        enable: true,
      },
    });
    counterUpdate();
  };

  const handleRejectApplication = async () => {
    await mutation.mutate({
      url: Urls.dristi.submissionsUpdate,
      params: {},
      body: { application: rejectApplicationPayload },
      config: {
        enable: true,
      },
    });
    counterUpdate();
  };

  const submitCommentApplication = async (newComment) => {
    await mutation.mutate({
      url: Urls.dristi.submissionsUpdate,
      params: {},
      body: { application: applicationCommentsPayload(newComment) },
      config: {
        enable: true,
      },
    });
    counterUpdate();
  };

  const formatDate = (date) => {
    const day = String(date.getDate()).padStart(2, "0");
    const month = String(date.getMonth() + 1).padStart(2, "0");
    const year = date.getFullYear();
    return `${day}-${month}-${year}`;
  };

  const handleEvidenceAction = async () => {
    await handleMarkEvidence();
  };

  const handleApplicationAction = async (generateOrder) => {
    try {
      const formdata =
        showConfirmationModal?.type === "reject"
          ? {
              orderType: {
                code: "REJECT_VOLUNTARY_SUBMISSIONS",
                type: "REJECT_VOLUNTARY_SUBMISSIONS",
                name: "ORDER_TYPE_REJECT_VOLUNTARY_SUBMISSIONS",
              },
              refApplicationId: documentSubmission?.[0]?.applicationList?.applicationNumber,
            }
          : {
              orderType: {
                code: "APPROVE_VOLUNTARY_SUBMISSIONS",
                type: "APPROVE_VOLUNTARY_SUBMISSIONS",
                name: "ORDER_TYPE_APPROVE_VOLUNTARY_SUBMISSIONS",
              },
              refApplicationId: documentSubmission?.[0]?.applicationList?.applicationNumber,
            };
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
              action: OrderWorkflowAction.SAVE_DRAFT,
              comments: "Creating order",
              assignes: null,
              rating: null,
              documents: [{}],
            },
            documents: [],
            additionalDetails: { formdata },
          },
        };
        try {
          const res = await ordersService.createOrder(reqbody, { tenantId });
          history.push(
            `/${window.contextPath}/employee/orders/generate-orders?filingNumber=${filingNumber}&applicationNumber=${documentSubmission?.[0]?.applicationList?.applicationNumber}&orderNumber=${res?.order?.orderNumber}`
          );
        } catch (error) {}
      } else {
        if (showConfirmationModal.type === "reject") {
          await handleRejectApplication();
        }
        if (showConfirmationModal.type === "accept") {
          await handleAcceptApplication();
        }
        const name = showConfirmationModal.type === "reject" ? t("GENERATE_REJECTION_ORDER_APPLICATION") : t("GENERATE_ACCEPTANCE_ORDER_APPLICATION");
        DRISTIService.customApiService(Urls.dristi.pendingTask, {
          pendingTask: {
            name,
            entityType: "order",
            referenceId: documentSubmission?.[0]?.applicationList?.applicationNumber,
            status: "SAVE_DRAFT",
            assignedTo: [],
            assignedRole: ["JUDGE_ROLE"],
            cnrNumber,
            filingNumber,
            isCompleted: false,
            stateSla: null,
            additionalDetails: {},
            tenantId,
          },
        });
        setShowConfirmationModal(null);
      }
    } catch (error) {}
  };

  const handleSubmitComment = async (newComment) => {
    if (modalType === "Submissions") {
      await submitCommentApplication(newComment);
    }
  };

  if (isLoading) {
    return <Loader />;
  }

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
          headerBarMain={
            <Heading
              label={t("DOCUMENT_SUBMISSION")}
              status={
                modalType === "Documents"
                  ? documentSubmission?.[0]?.artifactList?.isEvidence
                    ? "Accepeted"
                    : "Action Pending"
                  : t(documentSubmission?.[0]?.status)
              }
              isStatusRed={modalType === "Documents" ? !documentSubmission?.[0]?.artifactList?.isEvidence : documentSubmission?.[0]?.status}
            />
          }
          className="evidence-modal"
        >
          <div className="evidence-modal-main">
            <div className={modalType === "Submissions" ? "application-details" : "evidence-details"}>
              {documentSubmission?.map((docSubmission, index) => (
                <div>
                  <div className="application-info">
                    <div className="info-row">
                      <div className="info-key">
                        <h3>{t("APPLICATION_TYPE")}</h3>
                      </div>
                      <div className="info-value">
                        <h3>{t(docSubmission?.details?.applicationType)}</h3>
                      </div>
                    </div>
                    <div className="info-row">
                      <div className="info-key">
                        <h3>{t("APPLICATION_SENT_ON")}</h3>
                      </div>
                      <div className="info-value">
                        <h3>{docSubmission.details.applicationSentOn}</h3>
                      </div>
                    </div>
                    <div className="info-row">
                      <div className="info-key">
                        <h3>{t("SENDER")}</h3>
                      </div>
                      <div className="info-value">
                        <h3>{docSubmission.details.sender}</h3>
                      </div>
                    </div>
                    <div className="info-row">
                      <div className="info-key">
                        <h3>{t("EVIDENCE_ADDITIONAL_DETAILS")}</h3>
                      </div>
                      <div className="info-value">
                        {/* <h3>{JSON.stringify(docSubmission.details.additionalDetails)}</h3> */}
                        <h3>N/A</h3>
                      </div>
                    </div>
                    {docSubmission.applicationContent && (
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
                    )}
                  </div>
                </div>
              ))}
            </div>
            {modalType === "Submissions" && (
              <div className="application-comment">
                <div className="comment-section">
                  <h1 className="comment-xyzoo">{t("DOC_COMMENTS")}</h1>
                  <div className="comment-main">
                    {comments?.map((comment, index) => (
                      <CommentComponent key={index} comment={comment} />
                    ))}
                  </div>
                </div>
                <div className="comment-send">
                  <div className="comment-input-wrapper">
                    <TextInput
                      placeholder={"Type here..."}
                      value={currentComment}
                      onChange={(e) => {
                        setCurrentComment(e.target.value);
                      }}
                    />
                    <div
                      className="send-comment-btn"
                      onClick={() => {
                        const newComment = {
                          text: currentComment,
                          author: user,
                          timestamp: new Date(Date.now()).toLocaleDateString("en-in", {
                            year: "numeric",
                            month: "long",
                            day: "numeric",
                          }),
                        };
                        setComments((prev) => [...prev, newComment]);
                        setCurrentComment("");
                        handleSubmitComment(newComment);
                      }}
                    >
                      <RightArrow />
                    </div>
                  </div>
                </div>
              </div>
            )}
          </div>
        </Modal>
      )}
      {showConfirmationModal && !showSuccessModal && modalType === "Submissions" && (
        <ConfirmSubmissionAction
          t={t}
          setShowConfirmationModal={setShowConfirmationModal}
          type={showConfirmationModal.type}
          setShow={setShow}
          handleAction={handleApplicationAction}
        />
      )}
      {showConfirmationModal && !showSuccessModal && modalType === "Documents" && (
        <ConfirmEvidenceAction
          t={t}
          setShowConfirmationModal={setShowConfirmationModal}
          type={showConfirmationModal.type}
          setShow={setShow}
          handleAction={handleEvidenceAction}
          isEvidence={documentSubmission?.[0].artifactList.isEvidence}
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
