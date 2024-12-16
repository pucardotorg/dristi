import React, { useEffect, useMemo, useState } from "react";
import { submissionDocumentDetailsConfig } from "../../configs/submitDocumentConfig";
import { FormComposerV2, Header, Loader, Toast } from "@egovernments/digit-ui-react-components";
import { useTranslation } from "react-i18next";
import isEqual from "lodash/isEqual";
import ReviewDocumentSubmissionModal from "../../components/ReviewDocumentSubmissionModal";
import { combineMultipleFiles, getFilingType } from "@egovernments/digit-ui-module-dristi/src/Utils";
import SubmissionDocumentSuccessModal from "../../components/SubmissionDocumentSuccessModal";
import { getAdvocates } from "@egovernments/digit-ui-module-dristi/src/pages/citizen/FileCase/EfilingValidationUtils";
import { DRISTIService } from "@egovernments/digit-ui-module-dristi/src/services";
import { useHistory } from "react-router-dom/cjs/react-router-dom.min";
import useSearchEvidenceService from "../../hooks/submissions/useSearchEvidenceService";
import downloadPdfFromFile from "@egovernments/digit-ui-module-dristi/src/Utils/downloadPdfFromFile";
import { SubmissionDocumentWorkflowAction, SubmissionDocumentWorkflowState } from "../../utils/submissionDocumentsWorkflow";
import { Urls } from "../../hooks/services/Urls";

const fieldStyle = { marginRight: 0, width: "100%" };

const stateSla = {
  PENDINGESIGN_SUBMIT_DOCUMENT: 2 * 24 * 3600 * 1000,
};

const onDocumentUpload = async (fileData, filename) => {
  try {
    const fileUploadRes = await Digit.UploadServices.Filestorage("DRISTI", fileData, Digit.ULBService.getCurrentTenantId());
    return { file: fileUploadRes?.data, fileType: fileData.type, filename };
  } catch (error) {
    console.error("Failed to upload document:", error);
    throw error;
  }
};

const SubmissionDocuments = ({ path }) => {
  const tenantId = Digit.ULBService.getCurrentTenantId();
  const { t } = useTranslation();
  const history = useHistory();
  const { filingNumber, artifactNumber } = Digit.Hooks.useQueryParams();
  const userInfo = Digit.UserService.getUser()?.info;
  const userType = useMemo(() => (userInfo?.type === "CITIZEN" ? "citizen" : "employee"), [userInfo?.type]);
  const isCitizen = useMemo(() => userInfo?.type === "CITIZEN", [userInfo]);
  const [formdata, setFormdata] = useState({});
  const [isSubmitDisabled, setIsSubmitDisabled] = useState(false);
  const [showReviewModal, setShowReviewModal] = useState(false);
  const [showSubmissionSuccessModal, setShowSubmissionSuccessModal] = useState(false);
  const [showErrorToast, setShowErrorToast] = useState(null);
  const [combinedDocumentFile, setCombinedDocumentFile] = useState(null);
  const [combinedFileStoreId, setCombinedFileStoreId] = useState(null);
  const [signedDocumentUploadedID, setSignedDocumentUploadID] = useState(null);
  const [currentSubmissionStatus, setCurrentSubmissionStatus] = useState(null);
  const { downloadPdf } = Digit.Hooks.dristi.useDownloadCasePdf();
  const todayDate = new Date().getTime();
  const [loader, setLoader] = useState(false);
  const entityType = "voluntary-document-submission";

  const { data: filingTypeData, isLoading: isFilingTypeLoading } = Digit.Hooks.dristi.useGetStatuteSection("common-masters", [
    { name: "FilingType" },
  ]);

  const filingType = useMemo(() => getFilingType(filingTypeData?.FilingType, "Direct"), [filingTypeData?.FilingType]);

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
    `case-details-${filingNumber}`,
    filingNumber,
    Boolean(filingNumber)
  );

  const caseDetails = useMemo(() => {
    return caseData?.criteria?.[0]?.responseList?.[0];
  }, [caseData]);
  const allAdvocates = useMemo(() => getAdvocates(caseDetails), [caseDetails]);
  const onBehalfOfuuid = useMemo(() => Object.keys(allAdvocates)?.find((key) => allAdvocates[key].includes(userInfo?.uuid)), [
    allAdvocates,
    userInfo?.uuid,
  ]);
  const onBehalfOfLitigent = useMemo(() => caseDetails?.litigants?.find((item) => item?.additionalDetails?.uuid === onBehalfOfuuid), [
    caseDetails,
    onBehalfOfuuid,
  ]);
  const sourceType = useMemo(
    () => (onBehalfOfLitigent?.partyType?.toLowerCase()?.includes("complainant") ? "COMPLAINANT" : !isCitizen ? "COURT" : "ACCUSED"),
    [onBehalfOfLitigent, isCitizen]
  );

  const { data: evidenceData, isloading: isEvidenceLoading, refetch: evidenceRefetch } = useSearchEvidenceService(
    {
      criteria: {
        filingNumber,
        artifactNumber,
        tenantId,
      },
      tenantId,
    },
    {},
    artifactNumber,
    Boolean(artifactNumber)
  );

  const evidenceDetails = useMemo(() => evidenceData?.artifacts?.[0], [evidenceData]);
  const defaultFormValue = useMemo(() => {
    const formData = evidenceDetails?.additionalDetails?.formdata || {};
    const updatedFormData = {
      ...formData,
      submissionDocuments: {
        ...formData.submissionDocuments,
        uploadedDocs: evidenceDetails?.file ? [evidenceDetails?.file] : [],
      },
    };

    return updatedFormData;
  }, [evidenceDetails]);

  const formKey = useMemo(() => (evidenceDetails ? "default-values" : ""), [evidenceDetails]);

  useEffect(() => {
    if (evidenceDetails) {
      if (evidenceDetails?.status === SubmissionDocumentWorkflowState.PENDING_ESIGN) {
        setCombinedFileStoreId(evidenceDetails?.file.fileStore);
        setCurrentSubmissionStatus(evidenceDetails?.status);
        setShowReviewModal(true);
        return;
      }
      if (evidenceDetails?.status === SubmissionDocumentWorkflowState.SUBMITTED) {
        setCurrentSubmissionStatus(evidenceDetails?.status);
        setShowSubmissionSuccessModal(true);
        return;
      }
    }
  }, [evidenceDetails]);

  const closeToast = () => {
    setShowErrorToast(null);
  };

  useEffect(() => {
    if (showErrorToast) {
      const timer = setTimeout(() => {
        setShowErrorToast(null);
      }, 2000);
      return () => clearTimeout(timer);
    }
  }, [showErrorToast]);

  const handleNextSubmission = () => {
    localStorage.removeItem("fileStoreId");
    history.replace(`/digit-ui/citizen/submissions/submit-document?filingNumber=${filingNumber}`);
  };

  const handleSuccessDownloadSubmission = () => {
    const fileStoreId = localStorage.getItem("fileStoreId");
    downloadPdf(tenantId, signedDocumentUploadedID || fileStoreId);
  };

  const handleDownloadReviewModal = async () => {
    if ([SubmissionDocumentWorkflowState.PENDING_ESIGN].includes(currentSubmissionStatus)) {
      const fileStoreId = localStorage.getItem("fileStoreId");
      downloadPdf(tenantId, signedDocumentUploadedID || fileStoreId || evidenceDetails?.file?.fileStore);
    } else {
      await downloadPdfFromFile(combinedDocumentFile?.[0]);
    }
  };

  const createPendingTask = async ({
    name,
    status,
    isCompleted = false,
    refId = artifactNumber,
    stateSla = null,
    isAssignedRole = false,
    assignedRole = [],
  }) => {
    const assignes = !isAssignedRole ? [userInfo?.uuid] || [] : [];
    await DRISTIService.customApiService(Urls.application.pendingTask, {
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

  const handleGoToSign = async () => {
    try {
      let evidenceReqBody = {};
      let evidence = {};
      if (![SubmissionDocumentWorkflowState.PENDING_ESIGN, SubmissionDocumentWorkflowState.SUBMITTED].includes(currentSubmissionStatus)) {
        const documentFile = (await Promise.all(combinedDocumentFile?.map((doc) => onDocumentUpload(doc, doc?.name)))) || [];
        let file = null;
        for (let res of documentFile) {
          const fileStoreId = res?.file?.files?.[0]?.fileStoreId;
          file = {
            documentType: res?.fileType,
            fileStore: res?.fileStore || fileStoreId,
            additionalDetails: { name: res?.filename || res?.additionalDetails?.name },
          };
          evidenceReqBody = {
            artifact: {
              artifactType: formdata?.documentType?.code,
              caseId: caseDetails?.id,
              filingNumber,
              tenantId,
              comments: [],
              file,
              sourceType,
              sourceID: individualId,
              filingType: filingType,
              additionalDetails: {
                uuid: userInfo?.uuid,
                formdata,
              },
              workflow: {
                action: SubmissionDocumentWorkflowAction.CREATE,
              },
            },
          };
          evidence = await DRISTIService.createEvidence(evidenceReqBody);
          await createPendingTask({
            name: t("PENDINGESIGN_SUBMIT_DOCUMENT"),
            status: "PENDINGESIGN_SUBMIT_DOCUMENT",
            refId: evidence?.artifact?.artifactNumber,
            stateSla: todayDate + stateSla.PENDINGESIGN_SUBMIT_DOCUMENT,
          });
        }
        history.replace(
          `/digit-ui/citizen/submissions/submit-document?filingNumber=${filingNumber}&artifactNumber=${evidence?.artifact?.artifactNumber}`
        );
      } else {
        const localStorageID = localStorage.getItem("fileStoreId");
        const documentsFile =
          signedDocumentUploadedID !== "" || localStorageID
            ? {
                documentType: "SIGNED",
                fileStore: signedDocumentUploadedID || localStorageID,
              }
            : null;

        evidenceReqBody = {
          artifact: {
            ...evidenceDetails,
            file: documentsFile,
            workflow: {
              action: SubmissionDocumentWorkflowAction.E_SIGN,
            },
          },
        };
        evidence = await DRISTIService.updateEvidence(evidenceReqBody);
        await createPendingTask({
          name: t("PENDINGESIGN_SUBMIT_DOCUMENT"),
          status: "PENDINGESIGN_SUBMIT_DOCUMENT",
          isCompleted: true,
          isAssignedRole: true,
          assignedRole: ["EVIDENCE_CREATOR", "EVIDENCE_EDITOR"],
        });
        setShowReviewModal(false);
        setShowSubmissionSuccessModal(true);
      }
    } catch (error) {
      console.error("Error occured", error);
      setShowErrorToast({ label: t("SOMETHING_WENT_WRONG"), error: true });
    }
  };

  const handleOpenReview = async () => {
    try {
      if (![SubmissionDocumentWorkflowState.PENDING_ESIGN, SubmissionDocumentWorkflowState.SUBMITTED].includes(currentSubmissionStatus)) {
        const combinedDocumentFile = await combineMultipleFiles(
          formdata?.submissionDocuments?.uploadedDocs,
          `${t("COMBINED_DOC")}.pdf`,
          "submissionDocuments"
        );
        setCombinedDocumentFile(combinedDocumentFile);
      }
      setShowReviewModal(true);
    } catch (error) {
      console.error("Error occured", error);
      setShowErrorToast({ label: t("SOMETHING_WENT_WRONG"), error: true });
    }
  };

  const handleGoBack = async () => {
    if ([SubmissionDocumentWorkflowState.PENDING_ESIGN, SubmissionDocumentWorkflowState.SUBMITTED].includes(currentSubmissionStatus)) {
      history.replace(`/digit-ui/${userType}/dristi/home/view-case?caseId=${caseDetails?.id}&filingNumber=${filingNumber}&tab=Documents`);
    } else {
      setShowReviewModal(false);
    }
  };

  if (!artifactNumber && evidenceDetails) {
    handleGoBack();
  }

  const onFormValueChange = (setValue, formData, formState, reset, setError, clearErrors, trigger, getValues) => {
    if (formData?.submissionDocuments?.uploadedDocs?.length > 0 && Object.keys(formState?.errors).includes("uploadedDocs")) {
      clearErrors("uploadedDocs");
    } else if (
      formState?.submitCount &&
      !formData?.submissionDocuments?.uploadedDocs?.length &&
      !Object.keys(formState?.errors).includes("uploadedDocs")
    ) {
      setError("uploadedDocs", { message: t("CORE_REQUIRED_FIELD_ERROR") });
    }

    if (!isEqual(formdata, formData)) {
      setFormdata(formData);
    }

    if (Object.keys(formState?.errors).length) {
      setIsSubmitDisabled(true);
    } else {
      setIsSubmitDisabled(false);
    }
  };

  useEffect(() => {
    const esignProcess = localStorage.getItem("esignProcess");
    if (esignProcess) {
      setShowReviewModal(true);
      localStorage.removeItem("esignProcess");
      localStorage.removeItem("combineDocumentsPdf");
    }
  }, []);

  const modifiedFormConfig = useMemo(() => {
    const applyUiChanges = (config) => {
      return {
        ...config,
        body: config?.body.map((body) => {
          if (body?.labelChildren === "optional") {
            body.labelChildren = <span style={{ color: "#77787B" }}>&nbsp;{`${t("CS_IS_OPTIONAL")}`}</span>;
          }
          return {
            ...body,
          };
        }),
      };
    };

    if (!artifactNumber) {
      return submissionDocumentDetailsConfig.formConfig?.map(applyUiChanges);
    } else {
      const formConfig = JSON.parse(JSON.stringify(submissionDocumentDetailsConfig.formConfig));

      formConfig.forEach((config, index) => {
        if (config.body && Array.isArray(config.body)) {
          config.body.forEach((item) => {
            item.disable = true;
          });
        }
        formConfig[index] = applyUiChanges(config);
      });

      return formConfig;
    }
  }, [artifactNumber, t]);

  if (loader || isFilingTypeLoading || isEvidenceLoading) {
    return <Loader />;
  }
  return (
    <React.Fragment>
      <style>
        {`
          .formComposer .card {
            margin: 0px;
            padding: 0px;
            border: none;
          }

          .formComposer .card .label-field-pair h2.card-label {
            font-weight: 400;
            font-size : 16px;
            margin-bottom: 8px !important;
          }          
        `}
      </style>

      <div className="citizen create-submission" style={{ padding: "24px 24px 24px 40px" }}>
        {" "}
        <Header> {t(submissionDocumentDetailsConfig.header)}</Header>
        <div style={{ lineHeight: "24px" }}> {t(submissionDocumentDetailsConfig.subText1)}</div>
        <div style={{ marginBottom: "10px" }}> {t(submissionDocumentDetailsConfig.subText2)}</div>
        <div style={{ minHeight: "550px", overflowY: "auto", marginTop: "15px", width: "50%" }}>
          <FormComposerV2
            label={t("REVIEW_SUBMISSION_DOCS")}
            config={modifiedFormConfig}
            defaultValues={defaultFormValue}
            onFormValueChange={onFormValueChange}
            onSubmit={handleOpenReview}
            fieldStyle={fieldStyle}
            key={formKey}
            className={"formComposer"}
            isDisabled={isSubmitDisabled}
          />
        </div>
        {showReviewModal && (
          <ReviewDocumentSubmissionModal
            t={t}
            handleGoBack={handleGoBack}
            setSignedDocumentUploadID={setSignedDocumentUploadID}
            handleGoToSign={handleGoToSign}
            currentSubmissionStatus={currentSubmissionStatus}
            combinedDocumentFile={combinedDocumentFile?.[0]}
            combinedFileStoreId={combinedFileStoreId}
            handleDownloadReviewModal={handleDownloadReviewModal}
          />
        )}
        {showSubmissionSuccessModal && (
          <SubmissionDocumentSuccessModal
            t={t}
            handleClose={handleNextSubmission}
            handleSuccessDownloadSubmission={handleSuccessDownloadSubmission}
            documentSubmissionNumber={evidenceDetails?.artifactNumber}
          />
        )}
      </div>
      {showErrorToast && <Toast error={showErrorToast?.error} label={showErrorToast?.label} isDleteBtn={true} onClose={closeToast} />}
    </React.Fragment>
  );
};

export default SubmissionDocuments;
