import React, { useEffect, useMemo, useState } from "react";
import { useTranslation } from "react-i18next";
import { FormComposerV2, Header, Loader } from "@egovernments/digit-ui-react-components";
import {
  applicationTypeConfig,
  configsBailBond,
  configsCaseTransfer,
  configsCaseWithdrawal,
  configsCheckoutRequest,
  configsExtensionSubmissionDeadline,
  configsOthers,
  configsProductionOfDocuments,
  configsRescheduleRequest,
  configsSettlement,
  configsSurety,
  submissionTypeConfig,
} from "../../configs/submissionsCreateConfig";
import ReviewSubmissionModal from "../../components/ReviewSubmissionModal";
import SubmissionSignatureModal from "../../components/SubmissionSignatureModal";
import PaymentModal from "../../components/PaymentModal";
import SuccessModal from "../../components/SuccessModal";
import { DRISTIService } from "../../../../dristi/src/services";
import { submissionService } from "../../hooks/services";
import { useHistory } from "react-router-dom/cjs/react-router-dom.min";
import isEqual from "lodash/isEqual";
import { orderTypes } from "../../utils/orderTypes";
import { SubmissionWorkflowAction, SubmissionWorkflowState } from "../../../../dristi/src/Utils/submissionWorkflow";
import { Urls } from "../../hooks/services/Urls";
import { getAdvocates } from "@egovernments/digit-ui-module-dristi/src/pages/citizen/FileCase/EfilingValidationUtils";

const fieldStyle = { marginRight: 0 };

const stateSla = {
  RE_SCHEDULE: 2 * 24 * 3600 * 1000,
  CHECKOUT_REQUEST: 2 * 24 * 3600 * 1000,
  ESIGN_THE_SUBMISSION: 2 * 24 * 3600 * 1000,
  MAKE_PAYMENT_SUBMISSION: 2 * 24 * 3600 * 1000,
};

const SubmissionsCreate = () => {
  const tenantId = Digit.ULBService.getCurrentTenantId();
  const { t } = useTranslation();
  const history = useHistory();
  const urlParams = new URLSearchParams(window.location.search);
  const filingNumber = urlParams.get("filingNumber");
  const orderNumber = urlParams.get("orderNumber");
  const applicationNumber = urlParams.get("applicationNumber");
  const isExtension = urlParams.get("isExtension");
  const [formdata, setFormdata] = useState({});
  const [showReviewModal, setShowReviewModal] = useState(false);
  const [showsignatureModal, setShowsignatureModal] = useState(false);
  const [showPaymentModal, setShowPaymentModal] = useState(false);
  const [showSuccessModal, setShowSuccessModal] = useState(false);
  const [makePaymentLabel, setMakePaymentLabel] = useState(false);
  const hearingId = urlParams.get("hearingId");
  const [loader, setLoader] = useState(false);
  const userInfo = Digit.UserService.getUser()?.info;
  const userType = useMemo(() => (userInfo.type === "CITIZEN" ? "citizen" : "employee"), [userInfo.type]);
  const todayDate = new Date().getTime();
  const submissionType = useMemo(() => {
    return formdata?.submissionType?.code;
  }, [formdata?.submissionType?.code]);

  const submissionFormConfig = useMemo(() => {
    const submissionConfigKeys = {
      APPLICATION: applicationTypeConfig,
    };
    if (Array.isArray(submissionConfigKeys[submissionType])) {
      if (orderNumber || hearingId) {
        return submissionConfigKeys[submissionType]?.map((item) => {
          return {
            ...item,
            body: item?.body?.map((input) => {
              return { ...input, disable: true };
            }),
          };
        });
      } else {
        return submissionConfigKeys[submissionType]?.map((item) => {
          return {
            ...item,
            body: item?.body?.map((input) => {
              return {
                ...input,
                populators: {
                  ...input?.populators,
                  mdmsConfig: {
                    ...input?.populators?.mdmsConfig,
                    select:
                      "(data) => {return data['Application'].ApplicationType?.filter((item)=>![`EXTENSION_SUBMISSION_DEADLINE`,`RE_SCHEDULE`,`CHECKOUT_REQUEST`].includes(item.type)).map((item) => {return { ...item, name: 'APPLICATION_TYPE_'+item.type };});}",
                  },
                },
              };
            }),
          };
        });
      }
    }
    return [];
  }, [orderNumber, submissionType]);

  const applicationType = useMemo(() => {
    return formdata?.applicationType?.type || urlParams.get("applicationType");
  }, [formdata?.applicationType?.type]);

  const applicationFormConfig = useMemo(() => {
    const applicationConfigKeys = {
      RE_SCHEDULE: configsRescheduleRequest,
      EXTENSION_SUBMISSION_DEADLINE: configsExtensionSubmissionDeadline,
      PRODUCTION_DOCUMENTS: configsProductionOfDocuments,
      WITHDRAWAL: configsCaseWithdrawal,
      TRANSFER: configsCaseTransfer,
      SETTLEMENT: configsSettlement,
      BAIL_BOND: configsBailBond,
      SURETY: configsSurety,
      CHECKOUT_REQUEST: configsCheckoutRequest,
      OTHERS: configsOthers,
    };
    let newConfig = applicationConfigKeys?.[applicationType] || [];

    if (newConfig.length > 0) {
      const updatedConfig = newConfig.map((config) => {
        return {
          ...config,
          body: config?.body.map((body) => {
            if (body?.populators?.validation?.customValidationFn) {
              const customValidations =
                Digit.Customizations[body.populators.validation.customValidationFn.moduleName][
                  body.populators.validation.customValidationFn.masterName
                ];

              if (customValidations) {
                body.populators.validation = {
                  ...body.populators.validation,
                  ...customValidations(),
                };
              }
            }
            return {
              ...body,
            };
          }),
        };
      });
      return updatedConfig;
    } else {
      return [];
    }
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
    applicationNumber,
    applicationNumber
  );

  const { data: hearingsData, refetch } = Digit.Hooks.hearings.useGetHearings(
    {
      hearing: { tenantId },
      criteria: {
        tenantID: tenantId,
        filingNumber: filingNumber,
        hearingId: hearingId,
      },
    },
    { applicationNumber: "", cnrNumber: "" },
    "dristi",
    true
  );

  const applicationDetails = useMemo(() => applicationData?.applicationList?.[0], [applicationData]);
  useEffect(() => {
    if (applicationDetails) {
      if ([SubmissionWorkflowState.PENDINGESIGN, SubmissionWorkflowState.PENDINGSUBMISSION].includes(applicationDetails?.status)) {
        setShowReviewModal(true);
        return;
      }
      if (applicationDetails?.status === SubmissionWorkflowState.PENDINGPAYMENT) {
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
  const allAdvocates = useMemo(() => getAdvocates(caseDetails), [caseDetails]);
  const onBehalfOf = useMemo(() => Object.keys(allAdvocates)?.find((key) => allAdvocates[key].includes(userInfo?.uuid)), [
    allAdvocates,
    userInfo?.uuid,
  ]);
  const { data: orderData, isloading: isOrdersLoading } = Digit.Hooks.orders.useSearchOrdersService(
    { tenantId, criteria: { filingNumber, applicationNumber: "", cnrNumber: caseDetails?.cnrNumber, orderNumber: orderNumber } },
    { tenantId },
    filingNumber + caseDetails?.cnrNumber,
    Boolean(filingNumber && caseDetails?.cnrNumber && orderNumber)
  );
  const orderDetails = useMemo(() => orderData?.list?.[0], [orderData]);

  const defaultFormValue = useMemo(() => {
    if (applicationDetails?.additionalDetails?.formdata) {
      return applicationDetails?.additionalDetails?.formdata;
    } else if (hearingId && hearingsData?.HearingList?.[0]?.startTime) {
      return {
        submissionType: {
          code: "APPLICATION",
          name: "APPLICATION",
        },
        applicationType: {
          type: "RE_SCHEDULE",
          isactive: true,
          name: "APPLICATION_TYPE_RE_SCHEDULE",
        },
      };
    } else if (orderNumber) {
      if (orderDetails?.orderType === orderTypes.MANDATORY_SUBMISSIONS_RESPONSES) {
        if (isExtension) {
          return {
            submissionType: {
              code: "APPLICATION",
              name: "APPLICATION",
            },
            applicationType: {
              type: "EXTENSION_SUBMISSION_DEADLINE",
              isactive: true,
              name: "APPLICATION_TYPE_EXTENSION_SUBMISSION_DEADLINE",
            },
            refOrderId: orderDetails?.orderNumber,
            applicationDate: formatDate(new Date()),
            documentType: orderDetails?.additionalDetails?.formdata?.documentType,
            initialSubmissionDate: orderDetails?.additionalDetails?.formdata?.submissionDeadline,
          };
        } else {
          return {
            submissionType: {
              code: "APPLICATION",
              name: "APPLICATION",
            },
            applicationType: {
              type: "PRODUCTION_DOCUMENTS",
              isactive: true,
              name: "APPLICATION_TYPE_PRODUCTION_DOCUMENTS",
            },
            refOrderId: orderDetails?.orderNumber,
            applicationDate: formatDate(new Date()),
          };
        }
      } else {
        return {
          submissionType: {
            code: "APPLICATION",
            name: "APPLICATION",
          },
        };
      }
    } else if (applicationType) {
      return {
        submissionType: {
          code: "APPLICATION",
          name: "APPLICATION",
        },
        applicationType: {
          type: applicationType,
          name: `APPLICATION_TYPE_${applicationType}`,
          isActive: true,
        },
        applicationDate: formatDate(new Date()),
      };
    } else {
      return {
        submissionType: {
          code: "APPLICATION",
          name: "APPLICATION",
        },
      };
    }
  }, [applicationDetails?.additionalDetails?.formdata, isExtension, orderDetails, orderNumber, hearingId, hearingsData]);

  const onFormValueChange = (setValue, formData, formState, reset, setError, clearErrors, trigger, getValues) => {
    if (applicationType && !["OTHERS"].includes(applicationType) && !formData?.applicationDate) {
      setValue("applicationDate", formatDate(new Date()));
    }
    if (applicationType && applicationType === "TRANSFER" && !formData?.requestedCourt) {
      setValue("requestedCourt", caseDetails?.courtId ? t(`COMMON_MASTERS_COURT_R00M_${caseDetails?.courtId}`) : "");
    }
    if (applicationType && hearingId && ["CHECKOUT_REQUEST", "RE_SCHEDULE"].includes(applicationType) && !formData?.initialHearingDate) {
      setValue("initialHearingDate", formatDate(new Date(hearingsData?.HearingList?.[0]?.startTime)));
    }
    if (
      applicationType &&
      ["CHECKOUT_REQUEST", "RE_SCHEDULE"].includes(applicationType) &&
      formData?.initialHearingDate &&
      formData?.changedHearingDate
    ) {
      if (new Date(formData?.initialHearingDate).getTime() >= new Date(formData?.changedHearingDate).getTime()) {
        setValue("changedHearingDate", "");
        setError("changedHearingDate", { message: t("PROPOSED_DATE_CAN_NOT_BE_BEFORE_INITIAL_DATE") });
      } else if (Object.keys(formState?.errors).includes("changedHearingDate")) {
        setValue("changedHearingDate", formData?.changedHearingDate);
        clearErrors("changedHearingDate");
      }
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

  const createPendingTask = async ({
    name,
    status,
    isCompleted = false,
    refId = applicationNumber,
    stateSla = null,
    isAssignedRole = false,
    assignedRole = [],
  }) => {
    let entityType = "async-voluntary-submission-managelifecycle";
    if (orderNumber) {
      entityType =
        orderDetails?.additionalDetails?.formdata?.isResponseRequired?.code === "Yes"
          ? "async-submission-with-response-managelifecycle"
          : "async-order-submission-managelifecycle";
    }
    const assignes = !isAssignedRole ? [userInfo?.uuid] || [] : [];
    await submissionService.customApiService(Urls.application.pendingTask, {
      pendingTask: {
        name,
        entityType,
        referenceId: `MANUAL_${refId}`,
        status,
        assignedTo: assignes?.map((uuid) => ({ uuid })),
        assignedRole: assignedRole,
        cnrNumber: caseDetails?.cnrNumber,
        filingNumber: filingNumber,
        isCompleted,
        stateSla,
        additionalDetails: {},
        tenantId,
      },
    });
  };

  const createSubmission = async () => {
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
            filingNumber,
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
          referenceId: isExtension ? null : orderDetails?.id || null,
          createdDate: formatDate(new Date(), "DD-MM-YYYY"),
          applicationType,
          status: caseDetails?.status,
          isActive: true,
          statuteSection: { tenantId },
          additionalDetails: {
            formdata,
            ...(orderDetails && { orderDate: formatDate(new Date(orderDetails?.auditDetails?.lastModifiedTime)) }),
            ...(orderDetails?.additionalDetails?.formdata?.documentName && { documentName: orderDetails?.additionalDetails?.formdata?.documentName }),
            onBehalOfName: userInfo.name,
            partyType: "complainant.primary",
            ...(orderDetails &&
              orderDetails?.additionalDetails?.formdata?.isResponseRequired?.code === "Yes" && {
                respondingParty: orderDetails?.additionalDetails?.formdata?.respondingParty,
              }),
            isResponseRequired: orderDetails && !isExtension ? orderDetails?.additionalDetails?.formdata?.isResponseRequired?.code === "Yes" : true,
          },
          documents,
          onBehalfOf: [userInfo?.uuid],
          workflow: {
            id: "workflow123",
            action: SubmissionWorkflowAction.CREATE,
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
      setLoader(false);
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
      createPendingTask({ name: t("ESIGN_THE_SUBMISSION"), status: "ESIGN_THE_SUBMISSION", isCompleted: true });
      createPendingTask({
        name: t("MAKE_PAYMENT_SUBMISSION"),
        status: "MAKE_PAYMENT_SUBMISSION",
        stateSla: todayDate + stateSla.MAKE_PAYMENT_SUBMISSION,
      });
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
    !isExtension &&
      orderNumber &&
      createPendingTask({
        refId: orderNumber,
        isCompleted: true,
        status: "Completed",
      });
    createPendingTask({
      name: t("ESIGN_THE_SUBMISSION"),
      status: "ESIGN_THE_SUBMISSION",
      refId: newapplicationNumber,
      stateSla: todayDate + stateSla.ESIGN_THE_SUBMISSION,
    });
    if (newapplicationNumber) {
      history.push(
        orderNumber
          ? `?filingNumber=${filingNumber}&applicationNumber=${newapplicationNumber}&orderNumber=${orderNumber}`
          : `?filingNumber=${filingNumber}&applicationNumber=${newapplicationNumber}`
      );
    }
  };

  const handleBack = () => {
    history.replace(`/digit-ui/${userType}/dristi/home/view-case?caseId=${caseDetails?.id}&filingNumber=${filingNumber}&tab=Submissions`);
  };

  const handleAddSignature = () => {
    setLoader(true);
    updateSubmission(SubmissionWorkflowAction.ESIGN);
  };

  const handleCloseSignaturePopup = () => {
    setShowsignatureModal(false);
    setShowReviewModal(true);
  };

  const handleSkipPayment = () => {
    setMakePaymentLabel(true);
    setShowPaymentModal(false);
    setShowSuccessModal(true);
  };

  const handleMakePayment = () => {
    setMakePaymentLabel(false);
    setShowPaymentModal(false);
    setShowSuccessModal(true);
    createPendingTask({ name: t("MAKE_PAYMENT_SUBMISSION"), status: "MAKE_PAYMENT_SUBMISSION", isCompleted: true });
  };

  const handleDownloadSubmission = () => {
    // history.push(`/digit-ui/${userType}/dristi/home/view-case?caseId=${caseDetails?.id}&filingNumber=${filingNumber}&tab=Submissions`);
  };
  if (
    applicationDetails?.status &&
    ![SubmissionWorkflowState.PENDINGSUBMISSION, SubmissionWorkflowState.PENDINGESIGN, SubmissionWorkflowState.PENDINGPAYMENT].includes(
      applicationDetails?.status
    ) &&
    caseDetails?.id
  ) {
    handleBack();
  }
  if (
    loader ||
    isOrdersLoading ||
    isApplicationLoading ||
    (applicationNumber ? !applicationDetails?.additionalDetails?.formdata : false) ||
    (orderNumber ? !orderDetails?.orderType : false) ||
    (hearingId ? (hearingsData?.HearingList?.[0]?.startTime ? false : true) : false)
  ) {
    return <Loader />;
  }
  return (
    <div className="create-submission">
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
          isPaymentDone={applicationDetails?.status === SubmissionWorkflowState.PENDINGPAYMENT}
          handleCloseSuccessModal={makePaymentLabel ? handleMakePayment : handleBack}
          actionCancelLabel={"DOWNLOAD_SUBMISSION"}
          actionCancelOnSubmit={handleDownloadSubmission}
          applicationNumber={applicationNumber}
          createdDate={applicationDetails?.createdDate}
          makePayment={makePaymentLabel}
        />
      )}
    </div>
  );
};

export default SubmissionsCreate;
