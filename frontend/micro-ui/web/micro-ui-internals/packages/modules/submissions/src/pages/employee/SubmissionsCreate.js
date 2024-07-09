import React, { useMemo, useState } from "react";
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
import { submissionService } from "../../hooks/services";
import ReviewSubmissionModal from "../../components/ReviewSubmissionModal";
import SubmissionSignatureModal from "../../components/SubmissionSignatureModal";
import PaymentModal from "../../components/PaymentModal";
import SuccessModal from "../../components/SuccessModal";
import { configsCaseSettlement } from "../../../../orders/src/configs/ordersCreateConfig";
import { DRISTIService } from "../../../../dristi/src/services";

const fieldStyle = { marginRight: 0 };

const SubmissionsCreate = () => {
  const tenantId = Digit.ULBService.getCurrentTenantId();
  const { t } = useTranslation();
  const urlParams = new URLSearchParams(window.location.search);
  const filingNumber = urlParams.get("filingNumber");
  const [formdata, setFormdata] = useState({});
  const [showReviewModal, setShowReviewModal] = useState(false);
  const [showsignatureModal, setShowsignatureModal] = useState(false);
  const [showPaymentModal, setShowPaymentModal] = useState(false);
  const [showSuccessModal, setShowSuccessModal] = useState(false);
  const [loader, setLoader] = useState(false);
  const submissionType = useMemo(() => {
    return formdata?.submissionType?.code;
  }, [formdata?.submissionType?.code]);

  const submissionFormConfig = useMemo(() => {
    const submissionConfigKeys = {
      APPLICATION_TYPE: applicationTypeConfig,
    };
    return submissionConfigKeys[submissionType] || [];
  }, [submissionType]);

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

  const modifiedFormConfig = useMemo(() => {
    return [...submissionTypeConfig, ...submissionFormConfig, ...applicationFormConfig];
  }, [submissionFormConfig, applicationFormConfig]);

  const defaultFormValue = {
    submissionType: {
      code: "APPLICATION_TYPE",
      name: "APPLICATION_TYPE",
    },
  };

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

  const onFormValueChange = (setValue, formData, formState, reset, setError, clearErrors, trigger, getValues) => {
    if (JSON.stringify(formData) !== JSON.stringify(formdata)) {
      setFormdata(formData);
    }
  };
  const formatDate = (date) => {
    const day = String(date.getDate()).padStart(2, "0");
    const month = String(date.getMonth() + 1).padStart(2, "0");
    const year = date.getFullYear();
    return `${day}-${month}-${year}`;
  };

  const onDocumentUpload = async (fileData, filename) => {
    if (fileData?.fileStore) return fileData;
    const fileUploadRes = await window?.Digit.UploadServices.Filestorage("DRISTI", fileData, tenantId);
    return { file: fileUploadRes?.data, fileType: fileData.type, filename };
  };

  const createSubmission = async (data) => {
    try {
      let documents = [];
      if (applicationType === "PRODUCTION_DOCUMENTS") {
        const documentres = await Promise.all([
          onDocumentUpload(formdata?.listOfProducedDocuments?.documents?.[0], formdata?.listOfProducedDocuments?.documents?.[0]?.name),
          onDocumentUpload(formdata?.reasonForDocumentsSubmission?.documents?.[0], formdata?.reasonForDocumentsSubmission?.documents?.[0]?.name),
        ]);
        documentres.forEach((res) => {
          documents.push({
            documentType: res?.fileType,
            fileStore: res?.file?.files?.[0]?.fileStoreId,
            additionalDetails: { name: res?.filename },
          });
        });

        const file = {
          documentType: documentres[0]?.fileType,
          fileStore: documentres[0]?.file?.files?.[0]?.fileStoreId,
          additionalDetails: {
            name: documentres[0]?.filename,
          },
        };

        const evidenceReqBody = {
          artifact: {
            artifactType: "AFFIDAVIT",
            caseId: caseDetails?.id,
            tenantId,
            comments: [],
            file,
          },
        };
        DRISTIService.createEvidence(evidenceReqBody);
      }
      if (applicationType === "BAIL_BOND") {
        const docres = await onDocumentUpload(formdata?.documentsListForBail?.documents?.[0], formdata?.documentsListForBail?.documents?.[0]?.name);
        documents.push({
          documentType: docres?.fileType,
          fileStore: docres?.file?.files?.[0]?.fileStoreId,
          additionalDetails: { name: docres?.filename },
        });
      }

      const applicationReqBody = {
        tenantId,
        application: {
          tenantId,
          filingNumber,
          cnrNumber: caseDetails?.cnrNumber,
          caseId: caseDetails?.id,
          referenceId: "db3b2f72-ec26-4a5e-976a-6e42c6b6f06d",
          createdDate: formatDate(new Date()),
          applicationType,
          status: caseDetails?.status,
          isActive: true,
          statuteSection: { tenantId },
          additionalDetails: { formdata },
          documents,
          workflow: {
            id: "workflow123",
            action: "CREATE",
            status: "in_progress",
            comments: "Workflow comments",
            documents: [{}],
          },
        },
      };
      await submissionService.createApplication(applicationReqBody, { tenantId });
      setLoader(false);
    } catch (error) {}
  };

  const handleBack = () => {
    setShowReviewModal(false);
  };

  const handleProceed = () => {
    setLoader(true);
    setShowsignatureModal(false);
    setShowPaymentModal(true);
    createSubmission();
  };

  const handleCloseSignaturePopup = () => {
    setShowsignatureModal(false);
    setShowReviewModal(true);
  };

  const handleClosePaymentModal = () => {
    setShowPaymentModal(false);
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
    ///
  };

  const handleCloseSuccessModal = () => {
    //
  };

  const handlePendingPayment = () => {
    //
  };

  if (loader) {
    return <Loader />;
  }
  return (
    <div>
      <Header> {t("CREATE_SUBMISSION")}</Header>
      <FormComposerV2
        label={t("SUBMIT_BUTTON")}
        config={modifiedFormConfig}
        defaultValues={defaultFormValue}
        onFormValueChange={onFormValueChange}
        onSubmit={() => setShowReviewModal(true)}
        fieldStyle={fieldStyle}
      />
      {showReviewModal && (
        <ReviewSubmissionModal
          t={t}
          applicationType={formdata?.applicationType?.name}
          submissionDate={formatDate(new Date())}
          sender={caseDetails?.additionalDetails?.payerName}
          additionalDetails={"additional-details"}
          setShowReviewModal={setShowReviewModal}
          setShowsignatureModal={setShowsignatureModal}
          handleBack={handleBack}
        />
      )}
      {showsignatureModal && <SubmissionSignatureModal t={t} handleProceed={handleProceed} handleCloseSignaturePopup={handleCloseSignaturePopup} />}
      {showPaymentModal && (
        <PaymentModal
          t={t}
          handleClosePaymentModal={handleClosePaymentModal}
          handleSkipPayment={handleSkipPayment}
          handleMakePayment={handleMakePayment}
        />
      )}
      {showSuccessModal && (
        <SuccessModal
          t={t}
          handleDownloadSubmission={handleDownloadSubmission}
          // isPaymentDone={true}
          handleCloseSuccessModal={handleCloseSuccessModal}
          handlePendingPayment={handlePendingPayment}
        />
      )}
    </div>
  );
};

export default SubmissionsCreate;
