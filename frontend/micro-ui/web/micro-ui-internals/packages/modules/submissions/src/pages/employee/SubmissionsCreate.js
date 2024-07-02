import React, { useMemo, useState } from "react";
import { useTranslation } from "react-i18next";
import { useHistory } from "react-router-dom";
import { FormComposerV2, Header } from "@egovernments/digit-ui-react-components";
import { applicationTypeConfig, configsRescheduleRequest, submissionTypeConfig } from "../../configs/submissionsCreateConfig";
import { transformCreateData } from "../../utils/createUtils";
import { submissionService } from "../../hooks/services";
import ReviewSubmissionModal from "../../components/ReviewSubmissionModal";
import SubmissionSignatureModal from "../../components/SubmissionSignatureModal";
import PaymentModal from "../../components/PaymentModal";
import SuccessModal from "../../components/SuccessModal";

const fieldStyle = { marginRight: 0 };

const SubmissionsCreate = () => {
  const defaultValue = {};
  const tenantId = Digit.ULBService.getCurrentTenantId();
  const { t } = useTranslation();
  const history = useHistory();
  const urlParams = new URLSearchParams(window.location.search);
  const filingNumber = urlParams.get("filingNumber");
  const [formdata, setFormdata] = useState({});
  const [showReviewModal, setShowReviewModal] = useState(false);
  const [showsignatureModal, setShowsignatureModal] = useState(false);
  const [showPaymentModal, setShowPaymentModal] = useState(false);
  const [showSuccessModal, setShowSuccessModal] = useState(false);

  const reqCreate = {
    url: `/application/application/v1/create`,
    params: {},
    body: {},
    config: {
      enable: false,
    },
  };

  const applicationConfigKeys = {
    RE_SCHEDULE: configsRescheduleRequest,
  };
  const submissionConfigKeys = {
    APPLICATION_TYPE: applicationTypeConfig,
  };

  const submissionType = useMemo(() => {
    return formdata?.submissionType?.code;
  }, [formdata?.submissionType?.code]);

  const submissionFormConfig = useMemo(() => {
    return submissionConfigKeys[submissionType] || [];
  }, [submissionType]);

  const applicationType = useMemo(() => {
    return formdata?.applicationType?.type;
  }, [formdata?.applicationType?.type]);

  const applicationFormConfig = useMemo(() => {
    return applicationConfigKeys?.[applicationType] || [];
  }, [applicationType]);

  const modifiedFormConfig = useMemo(() => {
    return [...submissionTypeConfig, ...submissionFormConfig, ...applicationFormConfig];
  }, [submissionFormConfig, applicationFormConfig]);

  const { data: caseData, isLoading: isCaseDetailsLoading } = Digit.Hooks.dristi.useSearchCaseService(
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
  console.log("caseDetails", caseDetails);

  const mutation = Digit.Hooks.useCustomAPIMutationHook(reqCreate);
  const onError = (resp) => {
    history.push(`/${window.contextPath}/employee/submissions/submissions-response?isSuccess=${false}`, { message: "SUBMISSION_CREATION_FAILED" });
  };

  const onSuccess = (resp) => {
    history.push(`/${window.contextPath}/employee/submissions/submissions-response?appNo=${"NEW-NO-1"}&isSuccess=${true}`, {
      message: "SUBMISSION_CREATION_SUCCESS",
      showID: true,
      label: "SUBMISSION_ID",
    });
  };

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

  const createOrder = () => {
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
        // status: "example_status",
        // comment: "example_comment",
        // isActive: true,
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
      .then((res) => {
        console.debug(res);
      })
      .catch();
  };

  const onSubmit = async (data) => {
    console.log(data, "data");
    await mutation.mutate(
      {
        url: `application/application/v1/create`,
        params: { tenantId },
        body: transformCreateData(data),
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

  const handleBack = () => {};

  const handleProceed = () => {};

  const handleCloseSignaturePopup = () => {
    setShowsignatureModal(false);
    setShowReviewModal(true);
  };

  const handleClosePaymentModal = () => {
    setShowPaymentModal(false);
    //
  };

  const handleSkipPayment = () => {
    setShowPaymentModal(false);
    setShowSuccessModal(true);
    //
  };

  const handleMakePayment = () => {
    setShowPaymentModal(false);
    setShowSuccessModal(true);
    //
    //
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
        onSubmit={createOrder}
        fieldStyle={fieldStyle}
      />
      {showReviewModal && (
        <ReviewSubmissionModal
          t={t}
          // order={currentOrder}
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
          isPaymentDone={true}
          handleCloseSuccessModal={handleCloseSuccessModal}
          handlePendingPayment={handlePendingPayment}
        />
      )}
    </div>
  );
};

export default SubmissionsCreate;
