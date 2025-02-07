import React, { useEffect, useMemo, useState } from "react";
import { ActionBar, SubmitBar, Loader, Button, CloseSvg } from "@egovernments/digit-ui-react-components";
import { useHistory } from "react-router-dom/cjs/react-router-dom.min";
import DocViewerWrapper from "../../employee/docViewerWrapper";
import { FileUploadIcon } from "../../../icons/svgIndex";
import { ReactComponent as InfoIcon } from "../../../icons/info.svg";
import { useTranslation } from "react-i18next";
import useSearchCaseService from "../../../hooks/dristi/useSearchCaseService";
import useDownloadCasePdf from "../../../hooks/dristi/useDownloadCasePdf";
import { DRISTIService } from "../../../services";
import { getSuffixByBusinessCode, getTaxPeriodByBusinessService } from "../../../Utils";
import UploadSignatureModal from "../../../components/UploadSignatureModal";
import { getAllAssignees } from "./EfilingValidationUtils";
import { Urls } from "../../../hooks";
import { useToast } from "../../../components/Toast/useToast";
import Modal from "../../../components/Modal";

const getStyles = () => ({
  container: { display: "flex", flexDirection: "row", marginBottom: "50px" },
  leftPanel: {
    flex: 1,
    padding: "24px 16px 16px 16px",
    display: "flex",
    flexDirection: "column",
    justifyContent: "flex-start",
  },
  caseDetails: {
    border: "1px solid #9E400A24",
    borderRadius: "4px",
    padding: "12px 16px",
    backgroundColor: "#F7F5F3",
    marginBottom: "16px",
  },
  infoRow: { display: "flex", alignItems: "center" },
  infoText: { marginLeft: "8px" },
  detailsSection: {
    display: "flex",
    flexDirection: "column",
    gap: "12px",
  },
  advocateDetails: {
    marginTop: "8px",
    display: "flex",
    justifyContent: "space-between",
    alignItems: "center",
  },
  litigantDetails: {
    display: "flex",
    justifyContent: "space-between",
    alignItems: "center",
  },
  signedLabel: {
    padding: "6px 8px",
    borderRadius: "999px",
    color: "#00703C",
    backgroundColor: "#E4F2E4",
    fontSize: "12px",
    fontWeight: 400,
  },
  unSignedLabel: {
    padding: "6px 8px",
    borderRadius: "999px",
    color: "#9E400A",
    backgroundColor: "#FFF6E8",
    fontSize: "12px",
    fontWeight: 400,
  },
  centerPanel: { flex: 3, padding: "24px 16px 16px 16px", border: "1px solid #e0e0e0" },
  header: { height: "72px", gap: "8px", display: "flex", flexDirection: "column", justifyContent: "space-between" },
  title: { width: "584px", height: "38px", color: "#0A0A0A", fontSize: "32px", fontWeight: 700 },
  downloadButton: {
    background: "none",
    border: "none",
    color: "#007E7E",
    fontSize: "16px",
    fontWeight: 700,
    display: "flex",
    alignItems: "center",
    cursor: "pointer",
    whiteSpace: "nowrap",
  },
  details: { color: "#231F20", fontWeight: 700, fontSize: "20px" },
  description: { color: "#77787B", fontSize: "16px", fontWeight: 400 },
  docViewer: { marginTop: "24px", border: "1px solid #e0e0e0", display: "flex", overflow: "hidden" },
  rightPanel: { flex: 1, padding: "24px 16px 24px 24px", borderLeft: "1px solid #ccc" },
  signaturePanel: { display: "flex", flexDirection: "column" },
  signatureTitle: { fontSize: "24px", fontWeight: 700, color: "#3D3C3C" },
  signatureDescription: { fontWeight: "400", fontSize: "16px", color: "#3D3C3C", marginBottom: 0 },
  esignButton: {
    height: "40px",
    alignItems: "center",
    fontWeight: 700,
    fontSize: "16px",
    color: "#FFFFFF",
    display: "flex",
    justifyContent: "center",
    backgroundColor: "#007E7E",
    border: "none",
    cursor: "pointer",
    padding: "0 20px",
    marginTop: "20px",
  },
  uploadButton: {
    marginBottom: "16px",
    marginTop: "8px",
    height: "40px",
    fontWeight: 700,
    fontSize: "16px",
    display: "flex",
    justifyContent: "center",
    alignItems: "center",
    background: "none",
    border: "none",
    cursor: "pointer",
    padding: "0 16px",
    color: "#007E7E",
  },
  actionBar: { display: "flex", justifyContent: "flex-end", width: "100%" },
  submitButton: { backgroundColor: "#008080", color: "#fff", fontWeight: "bold", cursor: "pointer" },
  editCaseButton: { backgroundColor: "#fff", border: "#007E7E solid", color: "#007E7E", cursor: "pointer" },
});

const Heading = (props) => {
  return <h1 className="heading-m">{props.label}</h1>;
};

const CloseBtn = (props) => {
  return (
    <div
      onClick={props?.onClick}
      style={{
        height: "100%",
        display: "flex",
        alignItems: "center",
        paddingRight: "20px",
        cursor: "pointer",
        ...(props?.backgroundColor && { backgroundColor: props.backgroundColor }),
      }}
    >
      <CloseSvg />
    </div>
  );
};

const RightArrow = () => (
  <svg style={{ marginLeft: "8px" }} width="16" height="16" viewBox="0 0 16 16" fill="none" xmlns="http://www.w3.org/2000/svg">
    <path d="M8 0L6.59 1.41L12.17 7H0V9H12.17L6.59 14.59L8 16L16 8L8 0Z" fill="white" />
  </svg>
);

const FileDownloadIcon = () => (
  <svg width="16" height="16" viewBox="0 0 16 16" fill="none" xmlns="http://www.w3.org/2000/svg">
    <path d="M12.6693 6H10.0026V2H6.0026V6H3.33594L8.0026 10.6667L12.6693 6ZM3.33594 12V13.3333H12.6693V12H3.33594Z" fill="#007E7E" />
  </svg>
);

const caseType = {
  category: "Criminal",
  act: "Negotiable Instruments Act",
  section: "138",
  courtName: "Kollam S-138 Special Court",
  href: "https://districts.ecourts.gov.in/sites/default/files/study%20circles.pdf",
};

const complainantWorkflowACTION = {
  UPLOAD_DOCUMENT: "UPLOAD",
  ESIGN: "E-SIGN",
  EDIT_CASE: "EDIT_CASE",
};

const complainantWorkflowState = {
  PENDING_ESIGN: "PENDING_E-SIGN",
  PENDING_ESIGN_SCRUTINY: "PENDING_RE_E-SIGN",
  UPLOAD_SIGN_DOC: "PENDING_SIGN",
  UPLOAD_SIGN_DOC_SCRUTINY: "PENDING_RE_SIGN",
  DRAFT_IN_PROGRESS: "DRAFT_IN_PROGRESS",
  CASE_REASSIGNED: "CASE_REASSIGNED",
};

const stateSla = {
  PENDING_PAYMENT: 2,
};

const dayInMillisecond = 24 * 3600 * 1000;

const ComplainantSignature = ({ path }) => {
  const { t } = useTranslation();
  const history = useHistory();
  const toast = useToast();
  const Digit = window.Digit || {};
  const { filingNumber } = Digit.Hooks.useQueryParams();
  const todayDate = new Date().getTime();
  const [Loading, setLoader] = useState(false);
  const [isEsignSuccess, setEsignSuccess] = useState(false);
  const [uploadDoc, setUploadDoc] = useState(false);
  const [isDocumentUpload, setDocumentUpload] = useState(false);
  const [isEditCaseModal, setEditCaseModal] = useState(false);
  const [formData, setFormData] = useState({});
  const tenantId = window?.Digit.ULBService.getCurrentTenantId();
  const styles = getStyles();
  const roles = Digit.UserService.getUser()?.info?.roles;
  const isAdvocateFilingCase = roles?.some((role) => role.code === "ADVOCATE_ROLE");
  const userInfo = Digit?.UserService?.getUser()?.info;
  const userInfoType = useMemo(() => (userInfo?.type === "CITIZEN" ? "citizen" : "employee"), [userInfo]);
  const [signatureDocumentId, setSignatureDocumentId] = useState(null);
  const { downloadPdf } = useDownloadCasePdf();
  const { handleEsign } = Digit.Hooks.orders.useESign();
  const { uploadDocuments } = Digit.Hooks.orders.useDocumentUpload();
  const name = "Signature";
  const complainantPlaceholder = "Complainant Signature";
  const advocatePlaceholder = "Advocate Signature";

  const uploadModalConfig = useMemo(() => {
    return {
      key: "uploadSignature",
      populators: {
        inputs: [
          {
            name: name,
            type: "DragDropComponent",
            uploadGuidelines: "Ensure the image is not blurry and under 5MB.",
            maxFileSize: 5,
            maxFileErrorMessage: "CS_FILE_LIMIT_5_MB",
            fileTypes: ["JPG", "PNG", "JPEG", "PDF"],
            isMultipleUpload: false,
          },
        ],
        validation: {},
      },
    };
  }, [name]);

  const onSelect = (key, value) => {
    if (value?.[name] === null) {
      setFormData({});
      setSignatureDocumentId(null);
      setUploadDoc(false);
    } else {
      setFormData((prevData) => ({
        ...prevData,
        [key]: value,
      }));
    }
  };

  useEffect(() => {
    const upload = async () => {
      if (formData?.uploadSignature?.Signature?.length > 0) {
        const uploadedFileId = await uploadDocuments(formData?.uploadSignature?.Signature, tenantId);
        setSignatureDocumentId(uploadedFileId[0]?.fileStoreId);
        setUploadDoc(true);
      }
    };

    upload();
  }, [formData, tenantId, uploadDocuments]);

  const { data: caseData, refetch: refetchCaseData, isLoading } = useSearchCaseService(
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

  const caseDetails = useMemo(
    () => ({
      ...caseData?.criteria?.[0]?.responseList?.[0],
    }),
    [caseData]
  );
  const caseId = useMemo(() => caseDetails?.id, [caseDetails]);

  const DocumentFileStoreId = useMemo(() => {
    return caseDetails?.additionalDetails?.signedCaseDocument;
  }, [caseDetails]);

  const advocateDetails = useMemo(() => {
    const advocateData = caseDetails?.additionalDetails?.advocateDetails?.formdata?.[0]?.data;
    if (advocateData?.isAdvocateRepresenting?.code === "YES") {
      return advocateData;
    }
    return null;
  }, [caseDetails]);

  const advocateUuid = useMemo(() => {
    return advocateDetails?.advocateBarRegNumberWithName?.[0]?.advocateUuid || "";
  }, [advocateDetails]);

  const litigants = useMemo(() => {
    return caseDetails?.litigants
      ?.filter((litigant) => litigant.partyType.includes("complainant"))
      ?.map((litigant) => ({
        ...litigant,
        representatives:
          caseDetails?.representatives?.filter((rep) =>
            rep?.representing?.some((complainant) => complainant?.individualId === litigant?.individualId)
          ) || [],
      }));
  }, [caseDetails]);

  const isFilingParty = useMemo(() => {
    return caseDetails?.auditDetails?.createdBy === userInfo?.uuid;
  }, [caseDetails?.auditDetails?.createdBy, userInfo?.uuid]);

  const isCurrentLitigantSigned = useMemo(() => {
    return litigants?.some((lit) => lit?.hasSigned && lit?.additionalDetails?.uuid === userInfo?.uuid);
  }, [litigants, userInfo?.uuid]);

  const isCurrentAdvocateSigned = useMemo(() => {
    return caseDetails?.representatives?.some((advocate) => advocate?.hasSigned && advocate?.additionalDetails?.uuid === userInfo?.uuid);
  }, [caseDetails?.representatives, userInfo?.uuid]);

  const state = useMemo(() => caseDetails?.status, [caseDetails]);
  const isSelectedEsign = useMemo(() => {
    const esignStates = [complainantWorkflowState.PENDING_ESIGN, complainantWorkflowState.PENDING_ESIGN_SCRUTINY];

    return esignStates.includes(state);
  }, [state]);

  const isSelectedUploadDoc = useMemo(
    () => [complainantWorkflowState.UPLOAD_SIGN_DOC, complainantWorkflowState.UPLOAD_SIGN_DOC_SCRUTINY].includes(state),
    [state]
  );

  const closePendingTask = async ({ status, assignee, closeUploadDoc }) => {
    const entityType = "case-default";
    const filingNumber = caseDetails?.filingNumber;
    await DRISTIService.customApiService(Urls.dristi.pendingTask, {
      pendingTask: {
        entityType,
        status,
        referenceId: closeUploadDoc ? `MANUAL_${filingNumber}` : `MANUAL_${filingNumber}_${assignee}`,
        cnrNumber: caseDetails?.cnrNumber,
        filingNumber: filingNumber,
        isCompleted: true,
        additionalDetails: {},
        tenantId,
      },
    });
  };

  const handleEditCase = async () => {
    setLoader(true);
    setEditCaseModal(false);
    try {
      await DRISTIService.caseUpdateService(
        {
          cases: {
            ...caseDetails,
            additionalDetails: {
              ...caseDetails?.additionalDetails,
              signedCaseDocument: null,
            },
            documents: caseDetails?.documents?.filter(
              (doc) => doc?.documentType !== "case.complaint.signed" && doc?.documentType !== "case.complaint.unsigned"
            ),
            workflow: {
              ...caseDetails?.workflow,
              action: complainantWorkflowACTION.EDIT_CASE,
              assignes: [],
            },
          },
          tenantId,
        },
        tenantId
      ).then(async (res) => {
        if ([complainantWorkflowState.CASE_REASSIGNED, complainantWorkflowState.DRAFT_IN_PROGRESS].includes(res?.cases?.[0]?.status)) {
          const promises = [
            ...(Array.isArray(caseDetails?.litigants)
              ? caseDetails?.litigants?.map(async (litigant) => {
                  return closePendingTask({
                    status: state,
                    assignee: litigant?.additionalDetails?.uuid,
                  });
                })
              : []),
            ...(Array.isArray(caseDetails?.representatives)
              ? caseDetails?.representatives?.map(async (advocate) => {
                  return closePendingTask({
                    status: state,
                    assignee: advocate?.additionalDetails?.uuid,
                  });
                })
              : []),
          ];
          await Promise.all(promises);
          history.replace(
            `/${window?.contextPath}/${userInfoType}/dristi/home/file-case/case?caseId=${res?.cases?.[0]?.id}&selected=complainantDetails`
          );
        }
      });
    } catch (error) {
      console.error("Error:", error);
      toast.error(t("SOMETHING_WENT_WRONG"));
      setLoader(false);
    }
  };

  function getOtherAdvocatesForClosing() {
    // Step 1: Find the representative with current uuid
    const currentAdvocate = caseDetails?.representatives?.find((rep) => rep?.additionalDetails?.uuid === userInfo?.uuid);

    if (!currentAdvocate) {
      return [];
    }

    // Extract the representing objects for the target representative
    const currentAdvocateLitigants = currentAdvocate?.representing;

    // Step 2: Iterate through other representatives to compare
    const matchingRepresentatives = caseDetails?.representatives?.filter((rep) => {
      // Skip the target representative itself
      if (rep?.additionalDetails?.uuid === userInfo?.uuid) return true;

      const otherAdvocateLitigants = rep?.representing;

      // If otherAdvocateLitigants has one item, check if it's in the currentAdvocateLitigants
      if (otherAdvocateLitigants?.length === 1) {
        return otherAdvocateLitigants?.some((item) =>
          currentAdvocateLitigants?.some((targetItem) => targetItem?.individualId === item?.individualId)
        );
      }

      // If otherAdvocateLitigants has multiple items, check if all are in the currentAdvocateLitigants
      if (otherAdvocateLitigants?.length > 1) {
        return currentAdvocateLitigants?.every((targetItem) =>
          otherAdvocateLitigants?.some((item) => item?.individualId === targetItem?.individualId)
        );
      }

      return false;
    });

    return matchingRepresentatives;
  }

  const handleCasePdf = () => {
    downloadPdf(tenantId, signatureDocumentId ? signatureDocumentId : DocumentFileStoreId);
  };

  const getPlaceholder = () => {
    if (isAdvocateFilingCase) {
      const advocate = caseDetails?.representatives?.find((advocate) => advocate?.additionalDetails?.uuid === userInfo?.uuid);
      return `Advocate ${advocate?.representing?.[0]?.additionalDetails?.currentPosition} Signature`;
    } else {
      const litigant = litigants?.find((litigant) => litigant?.additionalDetails?.uuid === userInfo?.uuid);
      return `Complainant ${litigant?.additionalDetails?.currentPosition} Signature`;
    }
  };

  const handleEsignAction = async () => {
    setLoader(true);
    try {
      const caseLockStatus = await DRISTIService.getCaseLockStatus(
        {},
        {
          uniqueId: caseDetails?.filingNumber,
          tenantId: tenantId,
        }
      );
      if (caseLockStatus?.Lock?.isLocked) {
        toast.error(t("SOMEONEELSE_IS_ESIGNING_CURRENTLY"));
        setLoader(false);
        return;
      } else {
        await DRISTIService.setCaseLock({ Lock: { uniqueId: caseDetails?.filingNumber, tenantId: tenantId, lockType: "ESIGN" } }, {}).then(() => {
          setLoader(false);
          handleEsign(name, "ci", DocumentFileStoreId, getPlaceholder());
        });
      }
    } catch (error) {
      console.error("Error:", error);
      toast.error(t("SOMETHING_WENT_WRONG"));
      setLoader(false);
    }
  };

  const handleUploadFile = () => {
    setDocumentUpload(true);
  };

  const isDelayCondonation = useMemo(() => {
    const dcaData = caseDetails?.caseDetails?.["delayApplications"]?.formdata[0]?.data;
    if (
      dcaData?.delayCondonationType?.code === "YES" ||
      (dcaData?.delayCondonationType?.code === "NO" && dcaData?.isDcaSkippedInEFiling?.code === "YES")
    ) {
      return false;
    }
    return true;
  }, [caseDetails]);

  const chequeDetails = useMemo(() => {
    const debtLiability = caseDetails?.caseDetails?.debtLiabilityDetails?.formdata?.[0]?.data;
    if (debtLiability?.liabilityType?.code === "PARTIAL_LIABILITY") {
      return {
        totalAmount: debtLiability?.totalAmount,
      };
    } else {
      const chequeData = caseDetails?.caseDetails?.chequeDetails?.formdata || [];
      const totalAmount = chequeData.reduce((sum, item) => {
        return sum + parseFloat(item.data.chequeAmount);
      }, 0);
      return {
        totalAmount: totalAmount.toString(),
      };
    }
  }, [caseDetails]);

  const { data: paymentTypeData, isLoading: isPaymentTypeLoading } = Digit.Hooks.useCustomMDMS(
    Digit.ULBService.getStateId(),
    "payment",
    [{ name: "paymentType" }],
    {
      select: (data) => {
        return data?.payment?.paymentType || [];
      },
    }
  );

  const { data: taxPeriodData, isLoading: taxPeriodLoading } = Digit.Hooks.useCustomMDMS(
    Digit.ULBService.getStateId(),
    "BillingService",
    [{ name: "TaxPeriod" }],
    {
      select: (data) => {
        return data?.BillingService?.TaxPeriod || [];
      },
    }
  );

  const callCreateDemandAndCalculation = async (caseDetails, tenantId, caseId) => {
    const suffix = getSuffixByBusinessCode(paymentTypeData, "case-default");
    const taxPeriod = getTaxPeriodByBusinessService(taxPeriodData, "case-default");
    const calculationResponse = await DRISTIService.getPaymentBreakup(
      {
        EFillingCalculationCriteria: [
          {
            checkAmount: chequeDetails?.totalAmount,
            numberOfApplication: 1,
            tenantId: tenantId,
            caseId: caseId,
            isDelayCondonation: isDelayCondonation,
            filingNumber: caseDetails?.filingNumber,
          },
        ],
      },
      {},
      "dristi",
      Boolean(chequeDetails?.totalAmount && chequeDetails.totalAmount !== "0")
    );

    await DRISTIService.createDemand({
      Demands: [
        {
          tenantId,
          consumerCode: caseDetails?.filingNumber + `_${suffix}`,
          consumerType: "case-default",
          businessService: "case-default",
          taxPeriodFrom: taxPeriod?.fromDate,
          taxPeriodTo: taxPeriod?.toDate,
          demandDetails: [
            {
              taxHeadMasterCode: "CASE_ADVANCE_CARRYFORWARD",
              taxAmount: calculationResponse?.Calculation?.[0]?.totalAmount,
              collectionAmount: 0,
              isDelayCondonation: isDelayCondonation,
            },
          ],
          additionalDetails: {
            filingNumber: caseDetails?.filingNumber,
            chequeDetails: chequeDetails,
            cnrNumber: caseDetails?.cnrNumber,
            payer: caseDetails?.litigants?.[0]?.additionalDetails?.fullName,
            payerMobileNo: caseDetails?.additionalDetails?.payerMobileNo,
            isDelayCondonation: isDelayCondonation,
          },
        },
      ],
    });

    return calculationResponse;
  };

  const updateSignedDocInCaseDoc = () => {
    const tempDocList = structuredClone(caseDetails?.documents || []);
    const index = tempDocList.findIndex((doc) => doc?.documentType === "case.complaint.signed");
    const signedDoc = {
      documentType: "case.complaint.signed",
      fileStore: signatureDocumentId ? signatureDocumentId : DocumentFileStoreId,
      fileName: "case Complaint Signed Document",
    };
    if (index > -1) {
      tempDocList.splice(index, 1);
    }
    tempDocList.push(signedDoc);
    return tempDocList;
  };

  const handleSubmit = async (state) => {
    setLoader(true);

    let calculationResponse = {};

    const caseDocList = updateSignedDocInCaseDoc();

    try {
      await DRISTIService.caseUpdateService(
        {
          cases: {
            ...caseDetails,
            additionalDetails: {
              ...caseDetails?.additionalDetails,
              signedCaseDocument: signatureDocumentId ? signatureDocumentId : DocumentFileStoreId,
            },
            documents: caseDocList,
            workflow: {
              ...caseDetails?.workflow,
              action: isSelectedUploadDoc ? complainantWorkflowACTION.UPLOAD_DOCUMENT : complainantWorkflowACTION.ESIGN,
              assignes: [],
            },
          },
          tenantId,
        },
        tenantId
      )
        .then(async (res) => {
          if (isAdvocateFilingCase && isSelectedUploadDoc) {
            await closePendingTask({
              status: state,
              assignee: userInfo?.uuid,
              closeUploadDoc: true,
            });
          }
          if (isSelectedEsign) {
            if (!isAdvocateFilingCase) {
              await closePendingTask({
                status: state,
                assignee: userInfo?.uuid,
              });
            } else {
              const advocates = getOtherAdvocatesForClosing();
              const promises = advocates?.map(async (advocate) => {
                return closePendingTask({
                  status: state,
                  assignee: advocate?.additionalDetails?.uuid,
                });
              });
              await Promise.all(promises);
            }
          }
          if (res?.cases?.[0]?.status === "PENDING_PAYMENT") {
            // Extract UUIDs of litigants and representatives if available
            const uuids = [
              ...(Array.isArray(caseDetails?.litigants)
                ? caseDetails?.litigants?.map((litigant) => ({
                    uuid: litigant?.additionalDetails?.uuid,
                  }))
                : []),
              ...(Array.isArray(caseDetails?.representatives)
                ? caseDetails?.representatives?.map((advocate) => ({
                    uuid: advocate?.additionalDetails?.uuid,
                  }))
                : []),
            ];
            await DRISTIService.customApiService(Urls.dristi.pendingTask, {
              pendingTask: {
                name: "Pending Payment",
                entityType: "case-default",
                referenceId: `MANUAL_${caseDetails?.filingNumber}`,
                status: "PENDING_PAYMENT",
                assignedTo: uuids,
                assignedRole: ["CASE_CREATOR"],
                cnrNumber: null,
                filingNumber: caseDetails?.filingNumber,
                isCompleted: false,
                stateSla: stateSla.PENDING_PAYMENT * dayInMillisecond + todayDate,
                additionalDetails: {},
                tenantId,
              },
            });
            calculationResponse = await callCreateDemandAndCalculation(caseDetails, tenantId, caseId);
            setLoader(false);
            history.replace(`${path}/e-filing-payment?caseId=${caseId}`, { state: { calculationResponse } });
          } else {
            setLoader(false);
            history.replace(`/${window?.contextPath}/${userInfoType}/dristi/landing-page`);
          }
        })
        .catch((error) => {
          toast.error(t("SOMETHING_WENT_WRONG"));
          throw error;
        });
    } catch (error) {
      console.error("Error:", error);
      toast.error(t("SOMETHING_WENT_WRONG"));
      setLoader(false);
    }
  };

  const isSubmitEnabled = () => {
    return isEsignSuccess || uploadDoc;
  };

  useEffect(() => {
    const handleCaseUnlocking = async () => {
      await DRISTIService.setCaseUnlock({}, { uniqueId: caseDetails?.filingNumber, tenantId: tenantId });
    };

    const isSignSuccess = localStorage.getItem("isSignSuccess");
    const storedESignObj = localStorage.getItem("signStatus");
    const parsedESignObj = JSON.parse(storedESignObj);
    const esignProcess = localStorage.getItem("esignProcess");

    if (isSignSuccess) {
      const matchedSignStatus = parsedESignObj?.find((obj) => obj.name === name && obj.isSigned === true);
      if (isSignSuccess === "success" && matchedSignStatus) {
        const fileStoreId = localStorage.getItem("fileStoreId");
        setSignatureDocumentId(fileStoreId);
        setEsignSuccess(true);
      }
    }
    if (esignProcess && caseDetails?.filingNumber) {
      handleCaseUnlocking();
      localStorage.removeItem("esignProcess");
    }

    localStorage.removeItem("isSignSuccess");
    localStorage.removeItem("signStatus");
    localStorage.removeItem("fileStoreId");
  }, [caseDetails?.filingNumber, tenantId]);

  const isRightPannelEnable = () => {
    if (isAdvocateFilingCase) {
      return !(isCurrentAdvocateSigned || isEsignSuccess || uploadDoc);
    }
    return !(isCurrentLitigantSigned || isEsignSuccess);
  };

  if (isLoading) {
    return <Loader />;
  }

  return (
    <div style={styles.container}>
      {Loading && (
        <div
          style={{
            width: "100vw",
            height: "100vh",
            zIndex: "9999",
            position: "fixed",
            right: "0",
            display: "flex",
            top: "0",
            background: "rgb(234 234 245 / 50%)",
            alignItems: "center",
            justifyContent: "center",
          }}
          className="submit-loader"
        >
          <Loader />
        </div>
      )}
      <div style={styles.leftPanel}>
        <div style={styles.caseDetails}>
          <div style={styles.infoRow}>
            <InfoIcon />
            <span style={styles.infoText}>
              <b>{t("CS_YOU_ARE_FILING_A_CASE")}</b>
            </span>
          </div>
          <div>
            <p>
              {t("CS_UNDER")}{" "}
              <a href={caseType?.href} target="_blank" rel="noreferrer" style={{ color: "#3d3c3c" }}>{`S-${caseType.section}, ${caseType.act}`}</a>{" "}
              {t("CS_IN")}
              <span style={{ fontWeight: 700 }}>{` ${caseType.courtName}.`}</span>
            </p>
          </div>
        </div>

        <div style={styles.detailsSection}>
          <div style={styles.details}>
            <div>{t("COMPLAINT_SIGN")}:</div>
            {litigants?.map((litigant, index) => (
              <div key={index} style={{ ...styles.litigantDetails, marginTop: "5px", fontSize: "15px" }}>
                {litigant?.additionalDetails?.fullName}
                {litigant?.hasSigned || (litigant?.additionalDetails?.uuid === userInfo?.uuid && (isEsignSuccess || uploadDoc)) ? (
                  <span style={{ ...styles.signedLabel, alignItems: "right" }}>{t("SIGNED")}</span>
                ) : (
                  <span style={{ ...styles.unSignedLabel, alignItems: "right" }}>{t("PENDING")}</span>
                )}
              </div>
            ))}
          </div>
          {Array.isArray(caseDetails?.representatives) && caseDetails?.representatives?.length > 0 && (
            <div style={{ ...styles.details, marginTop: "15px" }}>
              <div>{t("ADVOCATE_SIGN")}:</div>
              {litigants?.map(
                (litigant) =>
                  litigant?.representatives?.length > 0 && (
                    <div style={{ ...styles.advocateDetails, marginTop: "5px", fontSize: "15px" }}>
                      {`${t("ADVOCATE_FOR")} ${litigant?.additionalDetails?.fullName}`}
                      {litigant.representatives.some((rep) => rep?.hasSigned) ||
                      (litigant.representatives.some((rep) => rep?.additionalDetails?.uuid === userInfo?.uuid) && (isEsignSuccess || uploadDoc)) ? (
                        <span style={styles.signedLabel}>{t("SIGNED")}</span>
                      ) : (
                        <span style={styles.unSignedLabel}>{t("PENDING")}</span>
                      )}
                    </div>
                  )
              )}
            </div>
          )}
        </div>
      </div>
      <div style={styles.centerPanel}>
        <div style={styles.header}>
          <div style={{ display: "flex", justifyContent: "space-between", alignItems: "center" }}>
            <div style={styles.title}>{t("SIGN_COMPLAINT")}</div>
            <button style={styles.downloadButton} onClick={handleCasePdf}>
              <span style={{ marginRight: "8px", display: "flex", alignItems: "center" }}>
                <FileDownloadIcon />
              </span>
              <span>{t("DOWNLOAD_PDF")}</span>
            </button>
          </div>
          <div style={styles.description}>{t("CS_REVIEW_CASE_FILE_SUBTEXT")}</div>
        </div>
        <div style={styles.docViewer}>
          {signatureDocumentId || DocumentFileStoreId ? (
            <DocViewerWrapper
              docWidth={"100vh"}
              docHeight={"70vh"}
              fileStoreId={signatureDocumentId ? signatureDocumentId : DocumentFileStoreId}
              tenantId={tenantId}
              docViewerCardClassName={"doc-card"}
              showDownloadOption={false}
            />
          ) : (
            <h2>{t("PREVIEW_DOC_NOT_AVAILABLE")}</h2>
          )}
        </div>
      </div>
      <div style={styles.rightPanel}>
        {isRightPannelEnable() && (
          <div style={styles.signaturePanel}>
            <div style={styles.signatureTitle}>{t("ADD_SIGNATURE")}</div>
            {isSelectedUploadDoc && isAdvocateFilingCase && isFilingParty && <p style={styles.signatureDescription}>{t("EITHER_ESIGN_UPLOAD")}</p>}
            {isSelectedUploadDoc && !isAdvocateFilingCase && <p style={styles.signatureDescription}>{t("ONLY_ADVOCATES_CAN_UPLOAD_SIGNED_COPY")}</p>}
            {isSelectedEsign && (
              <button style={styles.esignButton} onClick={handleEsignAction}>
                {t("CS_ESIGN")}
              </button>
            )}

            {isSelectedUploadDoc && (
              <button
                style={{
                  ...styles.uploadButton,
                  opacity: isAdvocateFilingCase ? 1 : 0.5,
                  cursor: isAdvocateFilingCase ? "pointer" : "default",
                }}
                onClick={handleUploadFile}
                disabled={!isAdvocateFilingCase}
              >
                <FileUploadIcon />
                <span style={{ marginLeft: "8px" }}>{t("UPLOAD_SIGNED_PDF")}</span>
              </button>
            )}
          </div>
        )}
      </div>
      <ActionBar>
        <div style={styles.actionBar}>
          {isFilingParty && (
            <Button
              label={t("EDIT_A_CASE")}
              variation={"secondary"}
              onButtonClick={() => {
                setEditCaseModal(true);
              }}
              style={{ boxShadow: "none", backgroundColor: "#fff", padding: "10px", width: "240px", marginRight: "20px" }}
              textStyles={{
                fontFamily: "Roboto",
                fontSize: "16px",
                fontWeight: 700,
                lineHeight: "18.75px",
                textAlign: "center",
                color: "#007E7E",
              }}
            />
          )}
          <SubmitBar
            label={
              <div style={{ display: "flex", alignItems: "center", justifyContent: "center", width: "100%" }}>
                <span>{t("CS_SUBMIT_CASE")}</span>
                <RightArrow />
              </div>
            }
            onSubmit={() => handleSubmit(state)}
            style={styles.submitButton}
            disabled={!isSubmitEnabled()}
          />
        </div>
      </ActionBar>

      {isDocumentUpload && (
        <UploadSignatureModal
          t={t}
          key={name}
          name={name}
          setOpenUploadSignatureModal={setDocumentUpload}
          onSelect={onSelect}
          config={uploadModalConfig}
          formData={formData}
          showWarning={true}
          warningText={t("UPLOAD_SIGNED_DOC_WARNING")}
        />
      )}
      {isEditCaseModal && (
        <Modal
          headerBarMain={<Heading label={t("CS_CONFIRM_EDIT")} />}
          headerBarEnd={
            <CloseBtn
              onClick={() => {
                setEditCaseModal(false);
              }}
            />
          }
          actionSaveLabel={t("EDIT_CONFIRM")}
          actionCancelLabel={t("CS_EDIT_BACK")}
          actionCancelOnSubmit={() => {
            setEditCaseModal(false);
          }}
          style={{
            backgroundColor: "#007E7E",
          }}
          children={<div style={{ margin: "16px 0px" }}>{t("CS_CONFIRM_EDIT_TEXT")}</div>}
          actionSaveOnSubmit={() => {
            handleEditCase();
          }}
        ></Modal>
      )}
    </div>
  );
};

export default ComplainantSignature;
