import React, { useEffect, useMemo, useState } from "react";
import { useTranslation } from "react-i18next";
import { FormComposerV2, Header, Loader } from "@egovernments/digit-ui-react-components";
import {
  applicationTypeConfig,
  configsBailBond,
  configsCaseTransfer,
  configsCaseWithdrawal,
  configsCheckoutRequest,
  configsDocumentSubmission,
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
import usePaymentProcess from "../../../../home/src/hooks/usePaymentProcess";

const fieldStyle = { marginRight: 0, width: "100%" };

const stateSla = {
  RE_SCHEDULE: 2 * 24 * 3600 * 1000,
  CHECKOUT_REQUEST: 2 * 24 * 3600 * 1000,
  ESIGN_THE_SUBMISSION: 2 * 24 * 3600 * 1000,
  MAKE_PAYMENT_SUBMISSION: 2 * 24 * 3600 * 1000,
};

const SubmissionsCreate = ({ path }) => {
  const tenantId = Digit.ULBService.getCurrentTenantId();
  const { t } = useTranslation();
  const history = useHistory();
  const { orderNumber, filingNumber, applicationNumber, isExtension, hearingId, applicationType: applicationTypeUrl } = Digit.Hooks.useQueryParams();
  const [formdata, setFormdata] = useState({});
  const [showReviewModal, setShowReviewModal] = useState(false);
  const [showsignatureModal, setShowsignatureModal] = useState(false);
  const [showSuccessModal, setShowSuccessModal] = useState(false);
  const [makePaymentLabel, setMakePaymentLabel] = useState(false);
  const [loader, setLoader] = useState(false);
  const userInfo = Digit.UserService.getUser()?.info;
  const applicationTypeParam = useMemo(() => applicationTypeUrl, [applicationTypeUrl]);
  const userType = useMemo(() => (userInfo?.type === "CITIZEN" ? "citizen" : "employee"), [userInfo?.type]);
  const isCitizen = useMemo(() => userInfo?.type === "CITIZEN", [userInfo]);
  const [signedDoucumentUploadedID, setSignedDocumentUploadID] = useState("");
  const [paymentStatus, setPaymentStatus] = useState();
  const scenario = "applicationSubmission";
  const hasSubmissionRole = useMemo(
    () =>
      ["SUBMISSION_CREATOR", "SUBMISSION_RESPONDER"].reduce((result, current) => {
        if (!result) return result;
        result = userInfo?.roles?.includes(current);
        return result;
      }, false),
    [userInfo]
  );
  const todayDate = new Date().getTime();
  const { data: individualData } = window?.Digit.Hooks.dristi.useGetIndividualUser(
    {
      Individual: {
        userUuid: [userInfo?.uuid],
      },
    },
    { tenantId, limit: 1000, offset: 0 },
    "Home",
    "",
    userInfo?.uuid
  );
  const individualId = useMemo(() => individualData?.Individual?.[0]?.individualId, [individualData]);

  const submissionType = useMemo(() => {
    return formdata?.submissionType?.code;
  }, [formdata?.submissionType?.code]);

  const submissionFormConfig = useMemo(() => {
    const submissionConfigKeys = {
      APPLICATION: applicationTypeConfig,
    };
    if (Array.isArray(submissionConfigKeys[submissionType])) {
      if (orderNumber || hearingId || !isCitizen) {
        return submissionConfigKeys[submissionType]?.map((item) => {
          return {
            ...item,
            body: item?.body?.map((input) => {
              return { ...input, disable: true };
            }),
          };
        });
      } else {
        return submissionConfigKeys[submissionType];
      }
    }
    return [];
  }, [hearingId, orderNumber, submissionType, isCitizen]);

  const applicationType = useMemo(() => {
    return formdata?.applicationType?.type || applicationTypeUrl;
  }, [formdata?.applicationType?.type, applicationTypeUrl]);

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
    const applicationConfigKeysForEmployee = {
      DOCUMENT: configsDocumentSubmission,
    };
    let newConfig = isCitizen ? applicationConfigKeys?.[applicationType] || [] : applicationConfigKeysForEmployee?.[applicationType] || [];

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
  }, [applicationType, isCitizen]);

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

  const { data: hearingsData } = Digit.Hooks.hearings.useGetHearings(
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
  const onBehalfOfuuid = useMemo(() => Object.keys(allAdvocates)?.find((key) => allAdvocates[key].includes(userInfo?.uuid)), [
    allAdvocates,
    userInfo?.uuid,
  ]);
  const onBehalfOfLitigent = useMemo(() => caseDetails?.litigants?.find((item) => item.additionalDetails.uuid === onBehalfOfuuid), [
    caseDetails,
    onBehalfOfuuid,
  ]);
  const sourceType = useMemo(
    () => (onBehalfOfLitigent?.partyType?.toLowerCase()?.includes("complainant") ? "COMPLAINANT" : !isCitizen ? "COURT" : "ACCUSED"),
    [onBehalfOfLitigent, isCitizen]
  );

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
    } else if (!isCitizen && applicationTypeParam) {
      return {
        submissionType: {
          code: "APPLICATION",
          name: "APPLICATION",
        },
        ...(applicationTypeParam && {
          applicationType: {
            type: applicationTypeParam,
            name: `APPLICATION_TYPE_${applicationTypeParam}`,
          },
        }),
      };
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
        applicationDate: formatDate(new Date()),
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
      } else if (orderDetails?.orderType === orderTypes.WARRANT) {
        return {
          submissionType: {
            code: "APPLICATION",
            name: "APPLICATION",
          },
          applicationType: {
            type: "BAIL_BOND",
            name: "APPLICATION_TYPE_BAIL_BOND",
          },
          refOrderId: orderDetails?.orderNumber,
          applicationDate: formatDate(new Date()),
        };
      } else {
        return {
          submissionType: {
            code: "APPLICATION",
            name: "APPLICATION",
          },
          applicationDate: formatDate(new Date()),
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
        applicationDate: formatDate(new Date()),
      };
    }
  }, [applicationDetails, isCitizen, hearingId, hearingsData, orderNumber, applicationType, applicationTypeParam, orderDetails, isExtension]);

  const onFormValueChange = (setValue, formData, formState, reset, setError, clearErrors, trigger, getValues) => {
    if (applicationType && !["OTHERS", "DOCUMENT"].includes(applicationType) && !formData?.applicationDate) {
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
    let entityType = "application-voluntary-submission";
    if (orderNumber) {
      entityType =
        orderDetails?.additionalDetails?.formdata?.responseInfo?.isResponseRequired?.code === true
          ? "application-order-submission-feedback"
          : "application-order-submission-default";
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
      if (formdata?.submissionDocuments?.documents?.length > 0) {
        documentsList = [...documentsList, ...formdata?.submissionDocuments?.documents];
      }
      const bailDocuments =
        formdata?.additionalDetails?.submissionDocuments?.submissionDocuments?.map((item) => ({
          fileType: item?.document?.documentType,
          fileStore: item?.document?.fileStore,
          additionalDetails: item?.document?.additionalDetails,
        })) || [];
      const documentres = (await Promise.all(documentsList?.map((doc) => onDocumentUpload(doc, doc?.name)))) || [];
      let documents = [];
      let file = null;
      let evidenceReqBody = {};
      const uploadedDocumentList = [...(documentres || []), ...bailDocuments];
      uploadedDocumentList.forEach((res) => {
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
            sourceType,
            sourceID: individualId,
            additionalDetails: {
              uuid: userInfo?.uuid,
            },
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
          createdDate: new Date().getTime(),
          applicationType,
          status: caseDetails?.status,
          isActive: true,
          statuteSection: { tenantId },
          additionalDetails: {
            formdata,
            ...(orderDetails && { orderDate: formatDate(new Date(orderDetails?.auditDetails?.lastModifiedTime)) }),
            ...(orderDetails?.additionalDetails?.formdata?.documentName && { documentName: orderDetails?.additionalDetails?.formdata?.documentName }),
            onBehalOfName: onBehalfOfLitigent?.additionalDetails?.fullName,
            partyType: "complainant.primary",
            ...(orderDetails &&
              orderDetails?.additionalDetails?.formdata?.responseInfo?.isResponseRequired?.code === true && {
                respondingParty: orderDetails?.additionalDetails?.formdata?.responseInfo?.respondingParty,
              }),
            isResponseRequired:
              orderDetails && !isExtension ? orderDetails?.additionalDetails?.formdata?.responseInfo?.isResponseRequired?.code === true : true,
            ...(hearingId && { hearingId }),
          },
          documents,
          onBehalfOf: [isCitizen ? onBehalfOfuuid : userInfo?.uuid],
          comment: [],
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
      const localStorageID = localStorage.getItem("fileStoreId");
      const documents = Array.isArray(applicationDetails?.documents) ? applicationDetails.documents : [];
      const documentsFile =
        signedDoucumentUploadedID !== "" || localStorageID
          ? {
              documentType: "SIGNED",
              fileStore: signedDoucumentUploadedID || localStorageID,
            }
          : null;

      localStorage.removeItem("fileStoreId");
      const reqBody = {
        application: {
          ...applicationDetails,
          documents: documentsFile ? [...documents, documentsFile] : documents,
          workflow: { ...applicationDetails?.workflow, documents: [{}], action },
          tenantId,
        },
        tenantId,
      };

      await submissionService.updateApplication(reqBody, { tenantId });
      if (isCitizen) {
        await createPendingTask({ name: t("ESIGN_THE_SUBMISSION"), status: "ESIGN_THE_SUBMISSION", isCompleted: true });
        await createPendingTask({
          name: t("MAKE_PAYMENT_SUBMISSION"),
          status: "MAKE_PAYMENT_SUBMISSION",
          stateSla: todayDate + stateSla.MAKE_PAYMENT_SUBMISSION,
        });
      } else if (hasSubmissionRole) {
        await createPendingTask({
          name: t("ESIGN_THE_SUBMISSION"),
          status: "ESIGN_THE_SUBMISSION",
          isCompleted: true,
          isAssignedRole: true,
          assignedRole: ["SUBMISSION_CREATOR", "SUBMISSION_RESPONDER"],
        });
        await createPendingTask({
          name: t("MAKE_PAYMENT_SUBMISSION"),
          status: "MAKE_PAYMENT_SUBMISSION",
          isAssignedRole: true,
          assignedRole: ["SUBMISSION_CREATOR", "SUBMISSION_RESPONDER"],
          stateSla: todayDate + stateSla.MAKE_PAYMENT_SUBMISSION,
        });
      }
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
      if (isCitizen) {
        await createPendingTask({
          name: t("ESIGN_THE_SUBMISSION"),
          status: "ESIGN_THE_SUBMISSION",
          refId: newapplicationNumber,
          stateSla: todayDate + stateSla.ESIGN_THE_SUBMISSION,
        });
      } else if (hasSubmissionRole) {
        await createPendingTask({
          name: t("ESIGN_THE_SUBMISSION"),
          status: "ESIGN_THE_SUBMISSION",
          refId: newapplicationNumber,
          stateSla: todayDate + stateSla.ESIGN_THE_SUBMISSION,
          isAssignedRole: true,
          assignedRole: ["SUBMISSION_CREATOR", "SUBMISSION_RESPONDER"],
        });
      }
      history.push(
        orderNumber
          ? `?filingNumber=${filingNumber}&applicationNumber=${newapplicationNumber}&orderNumber=${orderNumber}`
          : `?filingNumber=${filingNumber}&applicationNumber=${newapplicationNumber}`
      );
    }
  };

  const handleBack = () => {
    if (!paymentLoader) {
      history.replace(`/digit-ui/${userType}/dristi/home/view-case?caseId=${caseDetails?.id}&filingNumber=${filingNumber}&tab=Submissions`);
    }
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

    if (!paymentLoader) {
      setMakePaymentLabel(true);
      setShowPaymentModal(false);
      setShowSuccessModal(true);
    }
  };
  let entityType = "application-voluntary-submission";
  let taxHeadMasterCode = "ASYNC_VOLUNTARY_SUNMISSION_ADVANCE_CARRYFORWARD";
  if (orderNumber) {
    entityType =
      orderDetails?.additionalDetails?.formdata?.isResponseRequired?.code === "Yes"
        ? "application-order-submission-feedback"
        : "application-order-submission-default";

    taxHeadMasterCode =
      orderDetails?.additionalDetails?.formdata?.isResponseRequired?.code === "Yes"
        ? "ASYNC_SUBMISSION_RESPONSE_ADVANCE_CARRYFORWARD"
        : "ASYNC_ORDER_SUBMISSION_ADVANCE_CARRYFORWARD";
  }

  const { fetchBill, openPaymentPortal, paymentLoader, showPaymentModal, setShowPaymentModal, billPaymentStatus } = usePaymentProcess({
    tenantId,
    consumerCode: applicationDetails?.applicationNumber,
    service: entityType,
    path,
    caseDetails,
    totalAmount: "4",
    scenario,
  });
  const { data: billResponse, isLoading: isBillLoading } = Digit.Hooks.dristi.useBillSearch(
    {},
    { tenantId, consumerCode: applicationDetails?.applicationNumber, service: entityType },
    "dristi",
    Boolean(applicationDetails?.applicationNumber)
  );

  const handleMakePayment = async (totalAmount) => {
    try {
      if (billResponse?.Bill?.length === 0) {
        await DRISTIService.createDemand({
          Demands: [
            {
              tenantId,
              consumerCode: applicationDetails?.applicationNumber,
              consumerType: entityType,
              businessService: entityType,
              taxPeriodFrom: Date.now().toString(),
              taxPeriodTo: Date.now().toString(),
              demandDetails: [
                {
                  taxHeadMasterCode: taxHeadMasterCode,
                  taxAmount: 4,
                  collectionAmount: 0,
                },
              ],
            },
          ],
        });
      }
      const bill = await fetchBill(applicationDetails?.applicationNumber, tenantId, entityType);
      if (bill?.Bill?.length) {
        const billPaymentStatus = await openPaymentPortal(bill);
        setPaymentStatus(billPaymentStatus);
        await applicationRefetch();
        console.log(billPaymentStatus);
        if (billPaymentStatus === true) {
          setMakePaymentLabel(false);
          setShowPaymentModal(false);
          setShowSuccessModal(true);
          await updateSubmission(SubmissionWorkflowAction.PAY);
          applicationType === "PRODUCTION_DOCUMENTS" &&
            orderNumber &&
            createPendingTask({
              refId: `${userInfo?.uuid}_${orderNumber}`,
              isCompleted: true,
              status: "Completed",
            });
          createPendingTask({ name: t("MAKE_PAYMENT_SUBMISSION"), status: "MAKE_PAYMENT_SUBMISSION", isCompleted: true });
        } else {
          setMakePaymentLabel(true);
          setShowPaymentModal(false);
          setShowSuccessModal(true);
        }
      }
    } catch (error) {
      console.error(error);
    }
  };

  const handleDownloadSubmission = () => {
    // history.push(`/digit-ui/${userType}/dristi/home/view-case?caseId=${caseDetails?.id}&filingNumber=${filingNumber}&tab=Submissions`);
  };
  if (!filingNumber) {
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
    <div className="citizen create-submission" style={{ width: "50%", ...(!isCitizen && { padding: "0 8px 24px 16px" }) }}>
      <Header> {t("CREATE_SUBMISSION")}</Header>
      <FormComposerV2
        label={t("REVIEW_SUBMISSION")}
        config={modifiedFormConfig}
        defaultValues={defaultFormValue}
        onFormValueChange={onFormValueChange}
        onSubmit={handleOpenReview}
        fieldStyle={fieldStyle}
        key={applicationType}
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
        <SubmissionSignatureModal
          t={t}
          handleProceed={handleAddSignature}
          handleCloseSignaturePopup={handleCloseSignaturePopup}
          setSignedDocumentUploadID={setSignedDocumentUploadID}
        />
      )}
      {showPaymentModal && (
        <PaymentModal
          t={t}
          handleClosePaymentModal={handleBack}
          handleSkipPayment={handleSkipPayment}
          handleMakePayment={handleMakePayment}
          tenantId={tenantId}
          consumerCode={applicationDetails?.applicationNumber}
          paymentLoader={paymentLoader}
          entityType={entityType}
        />
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
          paymentStatus={paymentStatus}
        />
      )}
    </div>
  );
};

export default SubmissionsCreate;
