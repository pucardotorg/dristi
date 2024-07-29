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
import { Urls } from "../../../hooks";
import { SubmissionWorkflowAction, SubmissionWorkflowState } from "../../../Utils/submissionWorkflow";

const EvidenceModal = ({ caseData, documentSubmission = [], setShow, userRoles, modalType, setUpdateCounter, showToast, caseId }) => {
  const [comments, setComments] = useState(documentSubmission[0]?.comments ? documentSubmission[0].comments : []);
  const [showConfirmationModal, setShowConfirmationModal] = useState(null);
  const [showSuccessModal, setShowSuccessModal] = useState(null);
  const [currentComment, setCurrentComment] = useState("");
  const history = useHistory();
  const filingNumber = useMemo(() => caseData?.filingNumber, [caseData]);
  const cnrNumber = useMemo(() => caseData?.cnrNumber, [caseData]);
  const { t } = useTranslation();
  const tenantId = window?.Digit.ULBService.getCurrentTenantId();
  const OrderWorkflowAction = Digit.ComponentRegistryService.getComponent("OrderWorkflowActionEnum") || {};
  const ordersService = Digit.ComponentRegistryService.getComponent("OrdersService") || {};
  const userInfo = Digit.UserService.getUser()?.info;
  const user = Digit.UserService.getUser()?.info?.name;
  const userType = useMemo(() => (userInfo.type === "CITIZEN" ? "citizen" : "employee"), [userInfo.type]);
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
    return (
      !userRoles.includes("JUDGE_ROLE") ||
      userRoles.includes("CITIZEN") ||
      ![SubmissionWorkflowState.PENDINGAPPROVAL, SubmissionWorkflowState.PENDINGREVIEW].includes(documentSubmission?.[0]?.status)
    );
  }, [documentSubmission, userRoles]);

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
    statuteSection: {
      ...documentSubmission?.[0]?.applicationList?.statuteSection,
      tenantId: tenantId,
    },
    workflow: {
      ...documentSubmission?.[0]?.applicationList?.workflow,
      action: SubmissionWorkflowAction.APPROVE,
    },
  };

  const rejectApplicationPayload = {
    ...documentSubmission?.[0]?.applicationList,
    statuteSection: {
      ...documentSubmission?.[0]?.applicationList?.statuteSection,
      tenantId: tenantId,
    },
    workflow: {
      ...documentSubmission?.[0]?.applicationList?.workflow,
      action: SubmissionWorkflowAction.REJECT,
    },
  };

  const applicationCommentsPayload = (newComment) => {
    return {
      ...documentSubmission[0]?.applicationList,
      statuteSection: { ...documentSubmission[0]?.applicationList?.statuteSection, tenantId: tenantId },
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
    const details = showToast({
      isError: false,
      message: documentSubmission?.[0].artifactList.isEvidence ? "SUCCESSFULLY_UNMARKED_MESSAGE" : "SUCCESSFULLY_MARKED_MESSAGE",
    });
    counterUpdate();
    handleBack();
  };
  const onError = async (result) => {
    if (modalType === "Documents") {
      const details = showToast({
        isError: true,
        message: documentSubmission?.[0].artifactList.isEvidence ? "UNSUCCESSFULLY_UNMARKED_MESSAGE" : "UNSUCCESSFULLY_MARKED_MESSAGE",
      });
    }
    handleBack();
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
    await mutation.mutate(
      {
        url: Urls.dristi.submissionsUpdate,
        params: {},
        body: { application: acceptApplicationPayload },
        config: {
          enable: true,
        },
      },
      {
        onSuccess,
        onError,
      }
    );
  };

  const handleRejectApplication = async () => {
    await mutation.mutate(
      {
        url: Urls.dristi.submissionsUpdate,
        params: {},
        body: { application: rejectApplicationPayload },
        config: {
          enable: true,
        },
      },
      {
        onSuccess,
        onError,
      }
    );
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

  const getOrderTypes = (applicationType, type) => {
    switch (applicationType) {
      case "RE_SCHEDULE":
        return type === "reject" ? "REJECTION_RESCHEDULE_REQUEST" : "APPROVAL_RESCHEDULE_REQUEST";
      case "WITHDRAWAL":
        return type === "reject" ? "REJECT_VOLUNTARY_SUBMISSIONS" : "WITHDRAWAL";
      case "TRANSFER":
        return type === "reject" ? "REJECT_VOLUNTARY_SUBMISSIONS" : "CASE_TRANSFER";
      case "SETTLEMENT":
        return type === "reject" ? "REJECT_VOLUNTARY_SUBMISSIONS" : "SETTLEMENT";
      case "BAIL_BOND":
        return type === "reject" ? "REJECT_VOLUNTARY_SUBMISSIONS" : "BAIL";
      case "SURETY":
        return type === "reject" ? "REJECT_VOLUNTARY_SUBMISSIONS" : "BAIL";
      case "CHECKOUT_REQUEST":
        return type === "reject" ? "REJECT_VOLUNTARY_SUBMISSIONS" : "APPROVAL_RESCHEDULE_REQUEST";
      case "EXTENSION_SUBMISSION_DEADLINE":
        return type === "reject" ? "REJECT_VOLUNTARY_SUBMISSIONS" : "APPROVAL_RESCHEDULE_REQUEST";
      default:
        return type === "reject" ? "REJECT_VOLUNTARY_SUBMISSIONS" : "APPROVE_VOLUNTARY_SUBMISSIONS";
    }
  };

  const getOrderActionName = (applicationType, type) => {
    switch (applicationType) {
      case "RE_SCHEDULE":
        return type === "reject" ? "REJECTION_ORDER_RESCHEDULE_REQUEST" : "APPROVAL_ORDER_RESCHEDULE_REQUEST";
      case "WITHDRAWAL":
        return type === "reject" ? "REJECT_ORDER_VOLUNTARY_SUBMISSIONS" : "ORDER_FOR_WITHDRAWAL";
      case "TRANSFER":
        return type === "reject" ? "REJECT_ORDER_VOLUNTARY_SUBMISSIONS" : "ORDER_FOR_CASE_TRANSFER";
      case "SETTLEMENT":
        return type === "reject" ? "REJECT_ORDER_VOLUNTARY_SUBMISSIONS" : "ORDER_FOR_SETTLEMENT";
      case "BAIL_BOND":
        return type === "reject" ? "REJECT_ORDER_VOLUNTARY_SUBMISSIONS" : "ORDER_FOR_BAIL";
      case "SURETY":
        return type === "reject" ? "REJECT_ORDER_VOLUNTARY_SUBMISSIONS" : "ORDER_FOR_BAIL";
      case "CHECKOUT_REQUEST":
        return type === "reject" ? "REJECT_ORDER_VOLUNTARY_SUBMISSIONS" : "APPROVAL_ORDER_RESCHEDULE_REQUEST";
      case "EXTENSION_SUBMISSION_DEADLINE":
        return type === "reject" ? "REJECT_ORDER_VOLUNTARY_SUBMISSIONS" : "APPROVAL_ORDER_RESCHEDULE_REQUEST";
      default:
        return type === "reject" ? "REJECT_ORDER_VOLUNTARY_SUBMISSIONS" : "APPROVE_ORDER_VOLUNTARY_SUBMISSIONS";
    }
  };

  const handleApplicationAction = async (generateOrder) => {
    try {
      let orderType = getOrderTypes(documentSubmission?.[0]?.applicationList?.applicationType, showConfirmationModal?.type);
      const formdata = {
        orderType: {
          code: orderType,
          type: orderType,
          name: `ORDER_TYPE_${orderType}`,
        },
        refApplicationId: documentSubmission?.[0]?.applicationList?.applicationNumber,
        ...(orderType === "BAIL" && { bailType: { type: documentSubmission?.[0]?.applicationList?.applicationType } }),
      };
      if (generateOrder) {
        const reqbody = {
          order: {
            createdDate: new Date().getTime(),
            tenantId,
            cnrNumber,
            filingNumber,
            statuteSection: {
              tenantId,
            },
            orderType,
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
        const name = getOrderActionName(documentSubmission?.[0]?.applicationList?.applicationType, showConfirmationModal.type);
        DRISTIService.customApiService(Urls.dristi.pendingTask, {
          pendingTask: {
            name: t(name),
            entityType: "order-managelifecycle",
            referenceId: `MANUAL_${documentSubmission?.[0]?.applicationList?.applicationNumber}`,
            status: "SAVE_DRAFT",
            assignedTo: [],
            assignedRole: ["JUDGE_ROLE"],
            cnrNumber,
            filingNumber,
            isCompleted: false,
            stateSla: null,
            additionalDetails: { orderType },
            tenantId,
          },
        });
        setShowSuccessModal(true);
        setShowConfirmationModal(null);
      }
    } catch (error) {}
  };

  const handleBack = () => {
    if (modalType === "Submissions") {
      history.push(`/${window.contextPath}/${userType}/dristi/home/view-case?caseId=${caseId}&filingNumber=${filingNumber}&tab=Submissions`);
    } else {
      setShow(false);
    }
  };

  const handleSubmitComment = async (newComment) => {
    if (modalType === "Submissions") {
      await submitCommentApplication(newComment);
    }
  };

  return (
    <React.Fragment>
      {!showConfirmationModal && !showSuccessModal && (
        <Modal
          headerBarEnd={<CloseBtn onClick={handleBack} />}
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
              <div>
                <div className="application-info">
                  <div className="info-row">
                    <div className="info-key">
                      <h3>{t("APPLICATION_TYPE")}</h3>
                    </div>
                    <div className="info-value">
                      <h3>{t(documentSubmission[0]?.details?.applicationType)}</h3>
                    </div>
                  </div>
                  <div className="info-row">
                    <div className="info-key">
                      <h3>{t("APPLICATION_SENT_ON")}</h3>
                    </div>
                    <div className="info-value">
                      <h3>{documentSubmission[0]?.details.applicationSentOn}</h3>
                    </div>
                  </div>
                  <div className="info-row">
                    <div className="info-key">
                      <h3>{t("SENDER")}</h3>
                    </div>
                    <div className="info-value">
                      <h3>{documentSubmission[0]?.details.sender}</h3>
                    </div>
                  </div>
                  <div className="info-row">
                    <div className="info-key">
                      <h3>{t("EVIDENCE_ADDITIONAL_DETAILS")}</h3>
                    </div>
                    <div className="info-value">
                      {/* <h3>{JSON.stringify(documentSubmission[0]?.details.additionalDetails)}</h3> */}
                      <h3>N/A</h3>
                    </div>
                  </div>
                  {documentSubmission?.map((docSubmission, index) => (
                    <React.Fragment>
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
                    </React.Fragment>
                  ))}
                </div>
              </div>
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
                            year: "2-digit",
                            month: "short",
                            day: "2-digit",
                            hour: "2-digit",
                            minute: "2-digit",
                            hour12: true,
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
      {showSuccessModal && modalType === "Submissions" && <SubmissionSuccessModal t={t} handleBack={handleBack} />}
    </React.Fragment>
  );
};

export default EvidenceModal;
