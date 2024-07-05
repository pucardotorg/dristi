import React, { useMemo, useState } from "react";
import { useTranslation } from "react-i18next";
import { FormComposerV2, Header } from "@egovernments/digit-ui-react-components";
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

const fieldStyle = { marginRight: 0 };

const SubmissionsCreate = () => {
  const defaultValue = {};
  const tenantId = Digit.ULBService.getCurrentTenantId();
  const { t } = useTranslation();
  const urlParams = new URLSearchParams(window.location.search);
  const filingNumber = urlParams.get("filingNumber");
  const [formdata, setFormdata] = useState({});
  const [showReviewModal, setShowReviewModal] = useState(false);
  const [showsignatureModal, setShowsignatureModal] = useState(false);
  const [showPaymentModal, setShowPaymentModal] = useState(false);
  const [showSuccessModal, setShowSuccessModal] = useState(false);

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

  const createSubmission = () => {
    const reqbody = {
      tenantId,
      application: {
        tenantId,
        filingNumber,
        cnrNumber: caseDetails?.cnrNumber,
        caseId: caseDetails?.id,
        referenceId: "db3b2f72-ec26-4a5e-976a-6e42c6b6f06d",
        createdDate: formatDate(new Date()),
        applicationType,
        // applicationNumber: "example_application_number",
        // issuedBy: null,
        status: caseDetails?.status,
        // comment: "example_comment",
        isActive: true,
        statuteSection: {
          tenantId: tenantId,
        },
        additionalDetails: { formdata: formdata },
        documents: [{}],
        workflow: {
          id: "workflow123",
          action: "CREATE",
          status: "in_progress",
          comments: "Workflow comments",
          documents: [{}],
        },
      },
    };
    submissionService
      .createApplication(reqbody, { tenantId })
      .then((res) => {})
      .catch(() => {});
  };

  const handleBack = () => {
    setShowReviewModal(false);
  };

  const handleProceed = () => {
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

  return (
    <div>
      <Header> {t("CREATE_SUBMISSION")}</Header>

      <FormComposerV2
        label={t("SUBMIT_BUTTON")}
        config={modifiedFormConfig}
        defaultValues={defaultValue}
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
