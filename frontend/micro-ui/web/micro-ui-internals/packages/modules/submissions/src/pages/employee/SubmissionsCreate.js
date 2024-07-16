import React, { useEffect, useMemo, useState } from "react";
import { useTranslation } from "react-i18next";
import { FormComposerV2, Header, Loader } from "@egovernments/digit-ui-react-components";
import {
  applicationTypeConfig,
  configsBail,
  configsCaseTransfer,
  configsCaseWithdrawal,
  configsCheckoutRequest,
  configsExtensionSubmissionDeadline,
  configsOthers,
  configsProductionOfDocuments,
  configsRescheduleRequest,
  configsSurety,
  submissionTypeConfig,
} from "../../configs/submissionsCreateConfig";
import ReviewSubmissionModal from "../../components/ReviewSubmissionModal";
import SubmissionSignatureModal from "../../components/SubmissionSignatureModal";
import PaymentModal from "../../components/PaymentModal";
import SuccessModal from "../../components/SuccessModal";
import { configsCaseSettlement } from "../../../../orders/src/configs/ordersCreateConfig";
import { DRISTIService } from "../../../../dristi/src/services";
import { submissionService } from "../../hooks/services";
import { CaseWorkflowAction, CaseWorkflowState } from "../../../../dristi/src/Utils/caseWorkflow";
import { useHistory } from "react-router-dom/cjs/react-router-dom.min";
import isEqual from "lodash/isEqual";
import { orderTypes } from "../../utils/orderTypes";

const fieldStyle = { marginRight: 0 };

const SubmissionsCreate = () => {
  const tenantId = Digit.ULBService.getCurrentTenantId();
  const { t } = useTranslation();
  const history = useHistory();
  const urlParams = new URLSearchParams(window.location.search);
  const filingNumber = urlParams.get("filingNumber");
  const orderId = urlParams.get("orderId");
  const applicationNumber = urlParams.get("applicationNumber");
  const isExtension = urlParams.get("isExtension");
  const [formdata, setFormdata] = useState({});
  const [showReviewModal, setShowReviewModal] = useState(false);
  const [showsignatureModal, setShowsignatureModal] = useState(false);
  const [showPaymentModal, setShowPaymentModal] = useState(false);
  const [showSuccessModal, setShowSuccessModal] = useState(false);
  const [loader, setLoader] = useState(false);
  const userInfo = JSON.parse(window.localStorage.getItem("user-info"));
  const userType = useMemo(() => (userInfo.type === "CITIZEN" ? "citizen" : "employee"), [userInfo.type]);
  const submissionType = useMemo(() => {
    return formdata?.submissionType?.code;
  }, [formdata?.submissionType?.code]);

  const submissionFormConfig = useMemo(() => {
    const submissionConfigKeys = {
      APPLICATION_TYPE: applicationTypeConfig,
    };
    if (orderId && Array.isArray(submissionConfigKeys[submissionType])) {
      return submissionConfigKeys[submissionType]?.map((item) => {
        return {
          ...item,
          body: item?.body?.map((input) => {
            return { ...input, disable: true };
          }),
        };
      });
    }
    return submissionConfigKeys[submissionType] || [];
  }, [orderId, submissionType]);

  const applicationType = useMemo(() => {
    return formdata?.applicationType?.type;
  }, [formdata?.applicationType?.type]);

  const applicationFormConfig = useMemo(() => {
    const applicationConfigKeys = {
      RE_SCHEDULE: configsRescheduleRequest,
      EXTENSION_SUBMISSION_DEADLINE: configsExtensionSubmissionDeadline,
      PRODUCTION_DOCUMENTS: configsProductionOfDocuments,
      WITHDRAWAL: configsCaseWithdrawal,
      TRANSFER: configsCaseTransfer,
      SETTLEMENT: configsCaseSettlement,
      BAIL_BOND: configsBail,
      SURETY: configsSurety,
      CHECKOUT_REQUEST: configsCheckoutRequest,
      OTHERS: configsOthers,
    };
    return applicationConfigKeys?.[applicationType] || [];
  }, [applicationType]);

  const formatDate = (date, format) => {
    const day = String(date.getDate()).padStart(2, "0");
    const month = String(date.getMonth() + 1).padStart(2, "0");
    const year = date.getFullYear();
    if (format === "DD-MM-YYYY") {
      return `${day}-${month}-${year}`;
    }
    return `${year}-${month}-${day}`;
  };

  const modifiedFormConfig = useMemo(() => {
    return [...submissionTypeConfig, ...submissionFormConfig, ...applicationFormConfig];
  }, [submissionFormConfig, applicationFormConfig]);

  const { data: applicationData, isloading: isApplicationLoading, refetch: applicationRefetch } = Digit.Hooks.submissions.useSearchSubmissionService(
    {
      criteria: {
        filingNumber,
        applicationNumber,
        tenantId,
      },
      tenantId,
    },
    {},
    "dristi",
    applicationNumber,
    applicationNumber
  );

  const applicationDetails = useMemo(() => applicationData?.applicationList?.[0], [applicationData]);
  useEffect(() => {
    if (applicationDetails) {
      if ([CaseWorkflowState.PENDINGESIGN, CaseWorkflowState.PENDINGSUBMISSION].includes(applicationDetails?.status)) {
        setShowReviewModal(true);
        return;
      }
      if (applicationDetails?.status === CaseWorkflowState.PENDINGPAYMENT) {
        setShowPaymentModal(true);
        return;
      }
    }
  }, [applicationDetails]);

  const { data: caseData } = Digit.Hooks.dristi.useSearchCaseService(
    {
      criteria: [
        {
          filingNumber: filingNumber,
        },
      ],
      tenantId,
    },
    {},
    "dristi",
    filingNumber,
    filingNumber
  );

  const caseDetails = useMemo(() => {
    return caseData?.criteria?.[0]?.responseList?.[0];
  }, [caseData]);

  const { data: orderData, isloading: isOrdersLoading } = Digit.Hooks.orders.useSearchOrdersService(
    { tenantId, criteria: { filingNumber, applicationNumber: "", cnrNumber: caseDetails?.cnrNumber, id: orderId } },
    { tenantId },
    filingNumber + caseDetails?.cnrNumber,
    Boolean(filingNumber && caseDetails?.cnrNumber && orderId)
  );
  const orderDetails = useMemo(() => orderData?.list?.[0], [orderData]);

  const defaultFormValue = useMemo(() => {
    if (applicationDetails?.additionalDetails?.formdata) {
      return applicationDetails?.additionalDetails?.formdata;
    } else if (orderId) {
      if (orderDetails?.orderType === orderTypes.MANDATORY_SUBMISSIONS_RESPONSES) {
        if (isExtension) {
          return {
            submissionType: {
              code: "APPLICATION_TYPE",
              name: "APPLICATION_TYPE",
            },
            applicationType: {
              type: "EXTENSION_SUBMISSION_DEADLINE",
              isactive: true,
              name: "APPLICATION_TYPE_undefined",
            },
            refOrderId: orderDetails?.orderNumber,
            applicationDate: formatDate(new Date()),
            documentType: orderDetails?.additionalDetails?.formdata?.documentType,
            initialSubmissionDate: orderDetails?.additionalDetails?.formdata?.submissionDeadline,
          };
        } else {
          return {
            submissionType: {
              code: "APPLICATION_TYPE",
              name: "APPLICATION_TYPE",
            },
            applicationType: {
              type: "PRODUCTION_DOCUMENTS",
              isactive: true,
              name: "APPLICATION_TYPE_undefined",
            },
            refOrderId: orderDetails?.orderNumber,
            applicationDate: formatDate(new Date()),
          };
        }
      } else {
        return {
          submissionType: {
            code: "APPLICATION_TYPE",
            name: "APPLICATION_TYPE",
          },
        };
      }
    } else {
      return {
        submissionType: {
          code: "APPLICATION_TYPE",
          name: "APPLICATION_TYPE",
        },
      };
    }
  }, [applicationDetails?.additionalDetails?.formdata, isExtension, orderDetails, orderId]);

  const onFormValueChange = (setValue, formData, formState, reset, setError, clearErrors, trigger, getValues) => {
    if (applicationType && !["OTHERS", "SETTLEMENT"].includes(applicationType) && !formData?.applicationDate) {
      setValue("applicationDate", formatDate(new Date()));
    }
    if (!isEqual(formdata, formData)) {
      setFormdata(formData);
    }
  };
  const onDocumentUpload = async (fileData, filename) => {
    if (fileData?.fileStore) return fileData;
    const fileUploadRes = await window?.Digit.UploadServices.Filestorage("DRISTI", fileData, tenantId);
    return { file: fileUploadRes?.data, fileType: fileData.type, filename };
  };

  const createSubmission = async (data) => {
    try {
      let documentsList = [];
      if (formdata?.listOfProducedDocuments?.documents?.length > 0) {
        documentsList = [...documentsList, ...formdata?.listOfProducedDocuments?.documents];
      }
      if (formdata?.reasonForDocumentsSubmission?.documents?.length > 0) {
        documentsList = [...documentsList, ...formdata?.reasonForDocumentsSubmission?.documents];
      }
      if (formdata?.documentsListForBail?.documents) {
        documentsList = [...documentsList, ...formdata?.documentsListForBail?.documents];
      }
      const documentres = await Promise.all(documentsList?.map((doc) => onDocumentUpload(doc, doc?.name)));
      let documents = [];
      let file = null;
      let evidenceReqBody = {};
      documentres.forEach((res) => {
        file = {
          documentType: res?.fileType,
          fileStore: res?.file?.files?.[0]?.fileStoreId,
          additionalDetails: { name: res?.filename },
        };
        documents.push(file);
        evidenceReqBody = {
          artifact: {
            artifactType: "DOCUMENTARY",
            caseId: caseDetails?.id,
            tenantId,
            comments: [],
            file,
            sourceType: "COMPLAINANT",
            //ACCUSED // COURT - if respondant is uplading submission
          },
        };
        DRISTIService.createEvidence(evidenceReqBody);
      });

      const applicationReqBody = {
        tenantId,
        application: {
          tenantId,
          filingNumber,
          cnrNumber: caseDetails?.cnrNumber,
          caseId: caseDetails?.id,
          referenceId: orderId || null,
          createdDate: formatDate(new Date(), "DD-MM-YYYY"),
          applicationType,
          status: caseDetails?.status,
          isActive: true,
          statuteSection: { tenantId },
          additionalDetails: { formdata },
          documents,
          // onBehalfOf: "",
          // partyType: "",
          workflow: {
            id: "workflow123",
            action: CaseWorkflowAction.CREATE,
            status: "in_progress",
            comments: "Workflow comments",
            documents: [{}],
          },
        },
      };
      const res = await submissionService.createApplication(applicationReqBody, { tenantId });
      setLoader(false);
      return res;
    } catch (error) {
      return null;
    }
  };

  const updateSubmission = async (action) => {
    try {
      const reqBody = {
        application: {
          ...applicationDetails,
          workflow: { ...applicationDetails?.workflow, documents: [{}], action },

          tenantId,
        },
        tenantId,
      };
      await submissionService.updateApplication(reqBody, { tenantId });
      applicationRefetch();
      setShowPaymentModal(true);
    } catch (error) {
      setShowReviewModal(true);
    }
    setShowsignatureModal(false);
    setLoader(false);
  };

  const handleOpenReview = async () => {
    setLoader(true);
    const res = await createSubmission();
    const newapplicationNumber = res?.application?.applicationNumber;
    if (newapplicationNumber) {
      history.push(
        orderId
          ? `?filingNumber=${filingNumber}&applicationNumber=${newapplicationNumber}&orderId=${orderId}`
          : `?filingNumber=${filingNumber}&applicationNumber=${newapplicationNumber}`
      );
    }
  };

  const handleBack = () => {
    history.push(`/digit-ui/${userType}/dristi/home/view-case?caseId=${caseDetails?.id}&filingNumber=${filingNumber}&tab=Submissions`);
  };

  const handleAddSignature = () => {
    setLoader(true);
    updateSubmission(CaseWorkflowAction.ESIGN);
  };

  const handleCloseSignaturePopup = () => {
    setShowsignatureModal(false);
    setShowReviewModal(true);
  };

  const handleSkipPayment = () => {
    setShowPaymentModal(false);
    setShowSuccessModal(true);
  };

  const handleMakePayment = () => {
    setShowPaymentModal(false);
    setShowSuccessModal(true);
  };

  const handleDownloadSubmission = () => {
    history.push(`/digit-ui/${userType}/dristi/home/view-case?caseId=${caseDetails?.id}&filingNumber=${filingNumber}&tab=Submissions`);
  };
  if ([CaseWorkflowState.PENDINGREVIEW, CaseWorkflowState.ABATED, CaseWorkflowState.COMPLETED].includes(applicationDetails?.status)) {
    history.push(`/digit-ui/${userType}/dristi/home/view-case?caseId=${caseDetails?.id}&filingNumber=${filingNumber}&tab=Submissions`);
  }
  if (
    loader ||
    isOrdersLoading ||
    isApplicationLoading ||
    (applicationNumber ? !applicationDetails?.additionalDetails?.formdata : false) ||
    (orderId ? !orderDetails?.orderType : false)
  ) {
    return <Loader />;
  }
  return (
    <div>
      <Header> {t("CREATE_SUBMISSION")}</Header>
      <FormComposerV2
        label={t("REVIEW_SUBMISSION")}
        config={modifiedFormConfig}
        defaultValues={defaultFormValue}
        onFormValueChange={onFormValueChange}
        onSubmit={handleOpenReview}
        fieldStyle={fieldStyle}
      />
      {showReviewModal && (
        <ReviewSubmissionModal
          t={t}
          applicationType={applicationDetails?.applicationType}
          submissionDate={applicationDetails?.createdDate}
          sender={caseDetails?.additionalDetails?.payerName}
          setShowReviewModal={setShowReviewModal}
          setShowsignatureModal={setShowsignatureModal}
          handleBack={handleBack}
          documents={applicationDetails?.documents || []}
        />
      )}
      {showsignatureModal && (
        <SubmissionSignatureModal t={t} handleProceed={handleAddSignature} handleCloseSignaturePopup={handleCloseSignaturePopup} />
      )}
      {showPaymentModal && (
        <PaymentModal t={t} handleClosePaymentModal={handleBack} handleSkipPayment={handleSkipPayment} handleMakePayment={handleMakePayment} />
      )}
      {showSuccessModal && (
        <SuccessModal
          t={t}
          isPaymentDone={applicationDetails?.status === CaseWorkflowState.PENDINGPAYMENT}
          handleCloseSuccessModal={handleBack}
          actionCancelLabel={"DOWNLOAD_SUBMISSION"}
          actionCancelOnSubmit={handleDownloadSubmission}
          applicationNumber={applicationNumber}
          createdDate={applicationDetails?.createdDate}
        />
      )}
    </div>
  );
};

export default SubmissionsCreate;
