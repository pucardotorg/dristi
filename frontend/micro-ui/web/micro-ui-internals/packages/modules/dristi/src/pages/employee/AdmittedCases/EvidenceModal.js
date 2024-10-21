import { CloseSvg, TextInput } from "@egovernments/digit-ui-react-components";
import React, { useEffect, useMemo, useState } from "react";
import { useTranslation } from "react-i18next";
import { useHistory } from "react-router-dom/cjs/react-router-dom.min";
import CommentComponent from "../../../components/CommentComponent";
import ConfirmEvidenceAction from "../../../components/ConfirmEvidenceAction";
import ConfirmSubmissionAction from "../../../components/ConfirmSubmissionAction";
import Modal from "../../../components/Modal";
import SubmissionSuccessModal from "../../../components/SubmissionSuccessModal";
import { Urls } from "../../../hooks";
import { RightArrow } from "../../../icons/svgIndex";
import { DRISTIService } from "../../../services";
import { SubmissionWorkflowAction, SubmissionWorkflowState } from "../../../Utils/submissionWorkflow";
import { getAdvocates } from "../../citizen/FileCase/EfilingValidationUtils";
import DocViewerWrapper from "../docViewerWrapper";
import SelectCustomDocUpload from "../../../components/SelectCustomDocUpload";
import ESignSignatureModal from "../../../components/ESignSignatureModal";
import useDownloadCasePdf from "../../../hooks/dristi/useDownloadCasePdf";
import { removeInvalidNameParts } from "../../../Utils";
const stateSla = {
  DRAFT_IN_PROGRESS: 2,
};

const dayInMillisecond = 24 * 3600 * 1000;

const EvidenceModal = ({ caseData, documentSubmission = [], setShow, userRoles, modalType, setUpdateCounter, showToast, caseId }) => {
  const [comments, setComments] = useState(documentSubmission[0]?.comments ? documentSubmission[0].comments : []);
  const [showConfirmationModal, setShowConfirmationModal] = useState(null);
  const [showSuccessModal, setShowSuccessModal] = useState(null);
  const [currentComment, setCurrentComment] = useState("");
  const history = useHistory();
  const filingNumber = useMemo(() => caseData?.filingNumber, [caseData]);
  const cnrNumber = useMemo(() => caseData?.cnrNumber, [caseData]);
  const allAdvocates = useMemo(() => getAdvocates(caseData?.case), [caseData]);
  const createdBy = useMemo(() => documentSubmission?.[0]?.details?.auditDetails?.createdBy, [documentSubmission]);
  const applicationStatus = useMemo(() => documentSubmission?.[0]?.status, [documentSubmission]);
  const { t } = useTranslation();
  const tenantId = window?.Digit.ULBService.getCurrentTenantId();
  const OrderWorkflowAction = Digit.ComponentRegistryService.getComponent("OrderWorkflowActionEnum") || {};
  const ordersService = Digit.ComponentRegistryService.getComponent("OrdersService") || {};
  const userInfo = Digit.UserService.getUser()?.info;
  const user = Digit.UserService.getUser()?.info?.name;
  const isLitigent = useMemo(() => !userInfo?.roles?.some((role) => ["ADVOCATE_ROLE", "ADVOCATE_CLERK"].includes(role?.code)), [userInfo?.roles]);
  const userType = useMemo(() => (userInfo?.type === "CITIZEN" ? "citizen" : "employee"), [userInfo?.type]);
  const todayDate = new Date().getTime();
  const [formData, setFormData] = useState({});
  const [showFileIcon, setShowFileIcon] = useState(false);
  const { downloadPdf } = useDownloadCasePdf();
  const setData = (data) => {
    setFormData(data);
  };

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
        {props.showStatus && <h3 className={props.isStatusRed ? "status-false" : "status"}>{props?.status}</h3>}
      </div>
    );
  };
  const respondingUuids = useMemo(() => {
    return documentSubmission?.[0]?.details?.additionalDetails?.respondingParty?.map((party) => party?.uuid?.map((uuid) => uuid))?.flat() || [];
  }, [documentSubmission]);

  const showSubmit = useMemo(() => {
    if (userType === "employee") {
      if (modalType === "Documents") {
        return true;
      }
      return (
        userRoles.includes("SUBMISSION_APPROVER") &&
        [SubmissionWorkflowState.PENDINGAPPROVAL, SubmissionWorkflowState.PENDINGREVIEW].includes(applicationStatus)
      );
    } else {
      if (modalType === "Documents") {
        return false;
      }
      if (userInfo?.uuid === createdBy) {
        return [SubmissionWorkflowState.DELETED].includes(applicationStatus) ? false : true;
      }
      if (isLitigent && [...allAdvocates?.[userInfo?.uuid], userInfo?.uuid]?.includes(createdBy)) {
        return [SubmissionWorkflowState.DELETED].includes(applicationStatus) ? false : true;
      }
      if (!isLitigent && allAdvocates?.[createdBy]?.includes(userInfo?.uuid)) {
        return true;
      }
      if (!isLitigent || (isLitigent && allAdvocates?.[userInfo?.uuid]?.includes(userInfo?.uuid))) {
        return [SubmissionWorkflowState?.PENDINGREVIEW, SubmissionWorkflowState.PENDINGRESPONSE].includes(applicationStatus);
      }
      return false;
    }
  }, [userType, modalType, userRoles, applicationStatus, userInfo?.uuid, createdBy, isLitigent, allAdvocates]);

  const actionSaveLabel = useMemo(() => {
    let label = "";
    if (modalType === "Submissions") {
      if (userType === "employee") {
        label = t("Approve");
      } else {
        if (userInfo?.uuid === createdBy) {
          label = t("DOWNLOAD_SUBMISSION");
        } else if (isLitigent && [...allAdvocates?.[userInfo?.uuid], userInfo?.uuid]?.includes(createdBy)) {
          label = t("DOWNLOAD_SUBMISSION");
        } else if (
          (respondingUuids?.includes(userInfo?.uuid) || !documentSubmission?.[0]?.details?.referenceId) &&
          [SubmissionWorkflowState.PENDINGRESPONSE, SubmissionWorkflowState.PENDINGREVIEW].includes(applicationStatus)
        ) {
          label = t("ADD_COMMENT");
        }
      }
    } else {
      label = !documentSubmission?.[0]?.artifactList?.isEvidence ? t("MARK_AS_EVIDENCE") : t("UNMARK_AS_EVIDENCE");
    }
    return label;
  }, [allAdvocates, applicationStatus, createdBy, documentSubmission, isLitigent, modalType, respondingUuids, t, userInfo?.uuid, userType]);

  const actionCancelLabel = useMemo(() => {
    if (
      userRoles.includes("SUBMISSION_APPROVER") &&
      [SubmissionWorkflowState.PENDINGAPPROVAL, SubmissionWorkflowState.PENDINGREVIEW].includes(applicationStatus) &&
      modalType === "Submissions"
    ) {
      return t("REJECT");
    }
    if (userType === "citizen") {
      if (isLitigent && !allAdvocates?.[userInfo?.uuid]?.includes(userInfo?.uuid)) {
        return null;
      }
      if (
        userInfo?.uuid === createdBy &&
        userRoles?.includes("SUBMISSION_DELETE") &&
        !documentSubmission?.[0]?.details?.referenceId &&
        ![
          SubmissionWorkflowState.COMPLETED,
          SubmissionWorkflowState.DELETED,
          SubmissionWorkflowState.ABATED,
          SubmissionWorkflowState.REJECTED,
        ].includes(applicationStatus)
      ) {
        return t("CANCEL_SUBMISSION");
      }
    }
    return null;
  }, [allAdvocates, applicationStatus, createdBy, documentSubmission, isLitigent, modalType, t, userInfo?.uuid, userRoles, userType]);

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
  const addSubmissionComment = {
    url: Urls.dristi.addSubmissionComment,
    params: {},
    body: {},
    config: {
      enable: false,
    },
  };

  const addEvidenceComment = {
    url: Urls.dristi.addEvidenceComment,
    params: {},
    body: {},
    config: {
      enable: false,
    },
  };

  const mutation = Digit.Hooks.useCustomAPIMutationHook(reqCreate);
  const evidenceUpdateMutation = Digit.Hooks.useCustomAPIMutationHook(reqEvidenceUpdate);
  const submissionComment = Digit.Hooks.useCustomAPIMutationHook(addSubmissionComment);
  const evidenceComment = Digit.Hooks.useCustomAPIMutationHook(addEvidenceComment);

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

  const respondApplicationPayload = {
    ...documentSubmission?.[0]?.applicationList,
    statuteSection: {
      ...documentSubmission?.[0]?.applicationList?.statuteSection,
      tenantId: tenantId,
    },
    workflow: {
      ...documentSubmission?.[0]?.applicationList?.workflow,
      action: SubmissionWorkflowAction.RESPOND,
    },
  };

  const deleteApplicationPayload = {
    ...documentSubmission?.[0]?.applicationList,
    statuteSection: {
      ...documentSubmission?.[0]?.applicationList?.statuteSection,
      tenantId: tenantId,
    },
    workflow: {
      ...documentSubmission?.[0]?.applicationList?.workflow,
      action: SubmissionWorkflowAction.DELETE,
    },
  };

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
      comment: documentSubmission[0]?.applicationList.comment ? [...documentSubmission[0]?.applicationList.comment, newComment] : [newComment],
      workflow: {
        ...documentSubmission[0]?.applicationList?.workflow,
        action: "RESPOND",
      },
    };
  };

  const onSuccess = async () => {
    let message = "";
    if (modalType === "Documents") {
      message = documentSubmission?.[0].artifactList?.isEvidence ? "SUCCESSFULLY_UNMARKED_MESSAGE" : "SUCCESSFULLY_MARKED_MESSAGE";
    } else {
      if (showConfirmationModal?.type === "reject") {
        message = "SUCCESSFULLY_REJECTED_APPLICATION_MESSAGE";
      }
      if (showConfirmationModal?.type === "accept") {
        message = "SUCCESSFULLY_ACCEPTED_APPLICATION_MESSAGE";
      }
      if (actionSaveLabel === t("ADD_COMMENT")) {
        message = "SUCCESSFULLY_RESPONDED_APPLICATION_MESSAGE";
      } else {
        message = "";
      }
    }
    showToast({
      isError: false,
      message,
    });
    counterUpdate();
    handleBack();
  };

  const onError = async (result) => {
    if (modalType === "Documents") {
      showToast({
        isError: true,
        message: documentSubmission?.[0].artifactList?.isEvidence ? "UNSUCCESSFULLY_UNMARKED_MESSAGE" : "UNSUCCESSFULLY_MARKED_MESSAGE",
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
              isEvidence: !documentSubmission?.[0]?.artifactList?.isEvidence,
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
              isEvidence: !documentSubmission?.[0]?.artifactList?.isEvidence,
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

  const handleRespondApplication = async () => {
    await mutation.mutate(
      {
        url: Urls.dristi.submissionsUpdate,
        params: {},
        body: {
          application: {
            ...respondApplicationPayload,
            comment: comments,
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
  };

  const handleDeleteApplication = async () => {
    await mutation.mutate(
      {
        url: Urls.dristi.submissionsUpdate,
        params: {},
        body: { application: deleteApplicationPayload },
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
    await submissionComment.mutate({
      url: Urls.dristi.addSubmissionComment,
      params: {},
      body: { applicationAddComment: newComment },
      config: {
        enable: true,
      },
    });
    counterUpdate();
  };

  const submitCommentEvidence = async (newComment) => {
    await evidenceComment.mutate({
      url: Urls.dristi.addEvidenceComment,
      params: {},
      body: { evidenceAddComment: newComment },
      config: {
        enable: true,
      },
    });
    counterUpdate();
  };

  const handleEvidenceAction = async () => {
    await handleMarkEvidence();
  };

  const getOrderTypes = (applicationType, type) => {
    switch (applicationType) {
      case "RE_SCHEDULE":
        return type === "reject" ? "REJECTION_RESCHEDULE_REQUEST" : "INITIATING_RESCHEDULING_OF_HEARING_DATE";
      case "WITHDRAWAL":
        return "WITHDRAWAL";
      case "TRANSFER":
        return "CASE_TRANSFER";
      case "SETTLEMENT":
        return "SETTLEMENT";
      case "BAIL_BOND":
        return "BAIL";
      case "SURETY":
        return "BAIL";
      case "EXTENSION_SUBMISSION_DEADLINE":
        return "EXTENSION_OF_DOCUMENT_SUBMISSION_DATE";
      case "CHECKOUT_REQUEST":
        return type === "reject" ? "CHECKOUT_REJECT" : "CHECKOUT_ACCEPTANCE";
      default:
        return type === "reject" ? "REJECT_VOLUNTARY_SUBMISSIONS" : "APPROVE_VOLUNTARY_SUBMISSIONS";
    }
  };

  const getOrderActionName = (applicationType, type) => {
    switch (applicationType) {
      case "RE_SCHEDULE":
        return type === "reject" ? "REJECTION_ORDER_RESCHEDULE_REQUEST" : "ORDER_FOR_INITIATING_RESCHEDULING_OF_HEARING_DATE";
      case "WITHDRAWAL":
        return "ORDER_FOR_WITHDRAWAL";
      case "TRANSFER":
        return "ORDER_FOR_CASE_TRANSFER";
      case "SETTLEMENT":
        return "ORDER_FOR_SETTLEMENT";
      case "BAIL_BOND":
        return "ORDER_FOR_BAIL";
      case "SURETY":
        return "ORDER_FOR_BAIL";
      case "EXTENSION_SUBMISSION_DEADLINE":
        return "ORDER_EXTENSION_SUBMISSION_DEADLINE";
      case "CHECKOUT_REQUEST":
        return type === "reject" ? "REJECT_CHECKOUT_REQUEST" : "ACCEPT_CHECKOUT_REQUEST";
      default:
        return type === "reject" ? "REJECT_ORDER_VOLUNTARY_SUBMISSIONS" : "APPROVE_ORDER_VOLUNTARY_SUBMISSIONS";
    }
  };
  const isMandatoryOrderCreation = useMemo(() => {
    const applicationType = documentSubmission?.[0]?.applicationList?.applicationType;
    const type = showConfirmationModal?.type;
    const acceptedApplicationTypes = [
      "RE_SCHEDULE",
      "WITHDRAWAL",
      "TRANSFER",
      "SETTLEMENT",
      "BAIL_BOND",
      "SURETY",
      "EXTENSION_SUBMISSION_DEADLINE",
      "CHECKOUT_REQUEST",
    ];
    if (type === "reject") {
      return false;
    } else {
      return acceptedApplicationTypes.includes(applicationType);
    }
  }, [documentSubmission, showConfirmationModal?.type]);

  const showDocument = useMemo(() => {
    return (
      <React.Fragment>
        {documentSubmission?.map((docSubmission, index) => (
          <React.Fragment>
            {docSubmission.applicationContent && (
              <div className="application-view">
                <DocViewerWrapper
                  key={docSubmission.applicationContent.fileStoreId}
                  fileStoreId={docSubmission.applicationContent.fileStoreId}
                  displayFilename={docSubmission.applicationContent.fileName}
                  tenantId={docSubmission.applicationContent.tenantId}
                  docWidth={"calc(80vw* 62/ 100)"}
                  docHeight={"60vh"}
                  showDownloadOption={false}
                  documentName={docSubmission.applicationContent.fileName}
                />
              </div>
            )}
          </React.Fragment>
        ))}
      </React.Fragment>
    );
  }, [documentSubmission]);
  const handleApplicationAction = async (generateOrder, type) => {
    try {
      const orderType = getOrderTypes(documentSubmission?.[0]?.applicationList?.applicationType, type);
      const formdata = {
        orderType: {
          code: orderType,
          type: orderType,
          name: `ORDER_TYPE_${orderType}`,
        },
        refApplicationId: documentSubmission?.[0]?.applicationList?.applicationNumber,
        applicationStatus: type === "accept" ? t("APPROVED") : t("REJECTED"),
      };
      const linkedOrderNumber = documentSubmission?.[0]?.applicationList?.additionalDetails?.formdata?.refOrderId;
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
            additionalDetails: {
              formdata,
              applicationStatus: type === "accept" ? t("APPROVED") : t("REJECTED"),
            },
            ...(documentSubmission?.[0]?.applicationList?.additionalDetails?.onBehalOfName && {
              orderDetails: { parties: [{ partyName: documentSubmission?.[0]?.applicationList?.additionalDetails?.onBehalOfName }] },
            }),
            ...(["INITIATING_RESCHEDULING_OF_HEARING_DATE", "CHECKOUT_ACCEPTANCE"].includes(orderType) && {
              hearingNumber: documentSubmission?.[0]?.applicationList?.additionalDetails?.hearingId,
            }),
            ...(linkedOrderNumber && { linkedOrderNumber }),
          },
        };
        try {
          const res = await ordersService.createOrder(reqbody, { tenantId });
          const name = getOrderActionName(documentSubmission?.[0]?.applicationList?.applicationType, showConfirmationModal.type);
          DRISTIService.customApiService(Urls.dristi.pendingTask, {
            pendingTask: {
              name: t(name),
              entityType: "order-default",
              referenceId: `MANUAL_${res?.order?.orderNumber}`,
              status: "DRAFT_IN_PROGRESS",
              assignedTo: [],
              assignedRole: ["JUDGE_ROLE"],
              cnrNumber,
              filingNumber,
              isCompleted: false,
              stateSla: stateSla.DRAFT_IN_PROGRESS * dayInMillisecond + todayDate,
              additionalDetails: { orderType },
              tenantId,
            },
          });
          history.push(`/${window.contextPath}/employee/orders/generate-orders?filingNumber=${filingNumber}&orderNumber=${res?.order?.orderNumber}`);
        } catch (error) {}
      } else {
        if (showConfirmationModal.type === "reject") {
          await handleRejectApplication();
        }
        if (showConfirmationModal.type === "accept") {
          await handleAcceptApplication();
        }
        counterUpdate();
        setShowSuccessModal(true);
        setShowConfirmationModal(null);
      }
    } catch (error) {}
  };

  const handleBack = () => {
    if (modalType === "Submissions" && history.location?.state?.applicationDocObj) {
      history.push(`/${window.contextPath}/${userType}/dristi/home/view-case?caseId=${caseId}&filingNumber=${filingNumber}&tab=Submissions`);
    } else {
      setShow(false);
      setShowSuccessModal(false);
      counterUpdate();
    }
  };

  const handleSubmitComment = async (newComment) => {
    if (modalType === "Submissions") {
      await submitCommentApplication(newComment);
      setShowFileIcon(false);
    } else {
      await submitCommentEvidence(newComment);
    }
  };

  const signedSubmission = useMemo(() => {
    return documentSubmission?.filter((item) => item?.applicationContent?.documentType === "SIGNED")?.[0] || {};
  }, [documentSubmission]);

  const actionSaveOnSubmit = async () => {
    if (actionSaveLabel === t("DOWNLOAD_SUBMISSION") && signedSubmission?.applicationContent?.fileStoreId) {
      downloadPdf(tenantId, signedSubmission?.applicationContent?.fileStoreId);
      return;
    }
    if (userType === "employee") {
      modalType === "Documents" ? setShowConfirmationModal({ type: "documents-confirmation" }) : setShowConfirmationModal({ type: "accept" });
    } else {
      if (actionSaveLabel === t("ADD_COMMENT")) {
        try {
        } catch (error) {}
        await handleRespondApplication();
      }
      ///show a toast message
      counterUpdate();
      setShow(false);
      counterUpdate();
      history.replace(`/${window.contextPath}/${userType}/dristi/home/view-case?caseId=${caseId}&filingNumber=${filingNumber}&tab=Submissions`);
    }
  };

  const actionCancelOnSubmit = async () => {
    if (userType === "employee") {
      setShowConfirmationModal({ type: "reject" });
    } else {
      try {
        await handleDeleteApplication();
        setShow(false);
        counterUpdate();
      } catch (error) {}
    }
  };

  const documentUploaderConfig = useMemo(
    () => [
      {
        body: [
          {
            type: "component",
            component: "SelectUserTypeComponent",
            key: "SelectUserTypeComponent",
            withoutLabel: true,
            populators: {
              inputs: [
                {
                  label: "Document Type",
                  type: "dropdown",
                  name: "selectIdType",
                  optionsKey: "name",
                  error: "CORE_REQUIRED_FIELD_ERROR",
                  validation: {},
                  isMandatory: true,
                  disableMandatoryFieldFor: ["aadharNumber"],
                  disableFormValidation: false,
                  options: [
                    {
                      code: "EVIDENCE",
                      name: "Evidence",
                    },
                  ],
                  optionsCustomStyle: {
                    top: "40px",
                  },
                },
                {
                  label: "Upload Document",
                  type: "documentUpload",
                  name: "doc",
                  validation: {},
                  allowedFileTypes: /(.*?)(png|jpg|pdf)$/i,
                  isMandatory: true,
                  disableMandatoryFieldFor: ["aadharNumber"],
                  errorMessage: "CUSTOM_DOCUMENT_ERROR_MSG",
                  disableFormValidation: false,
                },
              ],
              validation: {},
            },
          },
        ],
      },
    ],
    []
  );

  const onDocumentUpload = async (fileData, filename, tenantId) => {
    if (fileData?.fileStore) return fileData;
    const fileUploadRes = await window?.Digit.UploadServices.Filestorage("DRISTI", fileData, tenantId);
    return { file: fileUploadRes?.data, fileType: fileData.type, filename };
  };

  return (
    <React.Fragment>
      {!showConfirmationModal && !showSuccessModal && (
        <Modal
          headerBarEnd={<CloseBtn onClick={handleBack} />}
          actionSaveLabel={actionSaveLabel}
          actionSaveOnSubmit={actionSaveOnSubmit}
          hideSubmit={!showSubmit}
          actionCancelLabel={actionCancelLabel}
          actionCancelOnSubmit={actionCancelOnSubmit}
          formId="modal-action"
          headerBarMain={
            <Heading
              label={t("DOCUMENT_SUBMISSION")}
              status={
                modalType === "Documents"
                  ? documentSubmission?.[0]?.artifactList?.isEvidence
                    ? "Accepeted"
                    : "Action Pending"
                  : t(applicationStatus)
              }
              showStatus={modalType === "Documents" ? false : true}
              isStatusRed={modalType === "Documents" ? !documentSubmission?.[0]?.artifactList?.isEvidence : applicationStatus}
            />
          }
          className="evidence-modal"
        >
          <div className="evidence-modal-main">
            <div className={"application-details"}>
              <div style={{ display: "flex", flexDirection: "column" }}>
                <div className="application-info" style={{ display: "flex", flexDirection: "column" }}>
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
                      <h3>{removeInvalidNameParts(documentSubmission[0]?.details.sender)}</h3>
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
                </div>
                <div style={{ display: "flex", flexDirection: "column" }}>{showDocument}</div>
              </div>
            </div>
            {(userRoles.includes("SUBMISSION_RESPONDER") || userRoles.includes("JUDGE_ROLE")) && (
              <div className="application-comment">
                <div className="comment-section">
                  <h1 className="comment-xyzoo">{t("DOC_COMMENTS")}</h1>
                  <div className="comment-main">
                    {comments?.map((comment, index) => (
                      <CommentComponent key={index} comment={comment} />
                    ))}
                  </div>
                </div>
                {((modalType === "Submissions" &&
                  [
                    SubmissionWorkflowState.PENDINGAPPROVAL,
                    SubmissionWorkflowState.PENDINGREVIEW,
                    SubmissionWorkflowState.PENDINGRESPONSE,
                    SubmissionWorkflowState.COMPLETED,
                    SubmissionWorkflowState.REJECTED,
                  ].includes(applicationStatus)) ||
                  modalType === "Documents") && (
                  <div className="comment-send">
                    <div className="comment-input-wrapper">
                      <div style={{ display: "flex" }}>
                        <TextInput
                          placeholder={"Type here..."}
                          value={currentComment}
                          onChange={(e) => {
                            setCurrentComment(e.target.value);
                          }}
                        />
                        <div
                          className="send-comment-btn"
                          onClick={async () => {
                            if (currentComment !== "") {
                              let newComment =
                                modalType === "Submissions"
                                  ? {
                                      tenantId,
                                      comment: [
                                        {
                                          tenantId,
                                          comment: currentComment,
                                          individualId: "",
                                          commentDocumentId: "",
                                          commentDocumentName: "",
                                          additionalDetails: {
                                            author: user,
                                            timestamp: new Date(Date.now()).toLocaleDateString("en-in", {
                                              year: "2-digit",
                                              month: "short",
                                              day: "2-digit",
                                              hour: "2-digit",
                                              minute: "2-digit",
                                              hour12: true,
                                            }),
                                          },
                                        },
                                      ],
                                      applicationNumber: documentSubmission?.[0]?.applicationList?.applicationNumber,
                                    }
                                  : {
                                      tenantId,
                                      comment: [
                                        {
                                          tenantId,
                                          comment: currentComment,
                                          individualId: "",
                                          commentDocumentId: "",
                                          commentDocumentName: "",
                                          artifactId: documentSubmission?.[0]?.artifactList?.id,
                                          additionalDetails: {
                                            author: user,
                                            timestamp: new Date(Date.now()).toLocaleDateString("en-in", {
                                              year: "2-digit",
                                              month: "short",
                                              day: "2-digit",
                                              hour: "2-digit",
                                              minute: "2-digit",
                                              hour12: true,
                                            }),
                                          },
                                        },
                                      ],
                                      artifactNumber: documentSubmission?.[0]?.artifactList?.artifactNumber,
                                    };
                              if (formData) {
                                if (formData?.SelectUserTypeComponent?.doc?.length > 0) {
                                  newComment = {
                                    ...newComment,
                                    comment: [
                                      {
                                        ...newComment.comment[0],
                                        additionalDetails: {
                                          ...newComment.comment[0].additionalDetails,
                                          commentDocumentId: formData?.SelectUserTypeComponent?.doc?.[0]?.[1]?.fileStoreId?.fileStoreId,
                                          commentDocumentName: documentUploaderConfig?.[0]?.body?.[0]?.populators?.inputs?.[0]?.options?.[0]?.code,
                                        },
                                      },
                                    ],
                                  };
                                }
                              }
                              setComments((prev) => [...prev, ...newComment.comment]);
                              setCurrentComment("");
                              setFormData({});
                              handleSubmitComment(newComment);
                            }
                          }}
                        >
                          <RightArrow />
                        </div>
                      </div>
                      <div style={{ display: "flex" }}>
                        <SelectCustomDocUpload
                          t={t}
                          formUploadData={formData}
                          config={[documentUploaderConfig?.[0]]}
                          setData={setData}
                          documentSubmission={documentSubmission}
                          showDocument={showFileIcon}
                          setShowDocument={setShowFileIcon}
                        />
                      </div>
                    </div>
                  </div>
                )}
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
          disableCheckBox={isMandatoryOrderCreation}
        />
      )}
      {showConfirmationModal && !showSuccessModal && modalType === "Documents" && (
        <ConfirmEvidenceAction
          t={t}
          setShowConfirmationModal={setShowConfirmationModal}
          type={showConfirmationModal.type}
          setShow={setShow}
          handleAction={handleEvidenceAction}
          isEvidence={documentSubmission?.[0]?.artifactList?.isEvidence}
        />
      )}
      {showSuccessModal && modalType === "Submissions" && <SubmissionSuccessModal t={t} handleBack={handleBack} />}
    </React.Fragment>
  );
};

export default EvidenceModal;
