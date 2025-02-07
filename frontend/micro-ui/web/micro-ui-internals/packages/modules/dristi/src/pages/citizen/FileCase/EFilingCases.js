import {
  ActionBar,
  Button,
  CloseSvg,
  EditIcon,
  FormComposerV2,
  Header,
  Loader,
  SubmitBar,
  TextInput,
  Toast,
} from "@egovernments/digit-ui-react-components";
import React, { useCallback, useEffect, useMemo, useRef, useState } from "react";
import { useTranslation } from "react-i18next";
import { useHistory } from "react-router-dom/cjs/react-router-dom.min";
import ReactTooltip from "react-tooltip";
import { CaseWorkflowState } from "../../../Utils/caseWorkflow";
import Accordion from "../../../components/Accordion";
import ConfirmCorrectionModal from "../../../components/ConfirmCorrectionModal";
import ConfirmCourtModal from "../../../components/ConfirmCourtModal";
import ErrorsAccordion from "../../../components/ErrorsAccordion";
import FlagBox from "../../../components/FlagBox";
import Modal from "../../../components/Modal";
import ScrutinyInfo from "../../../components/ScrutinyInfo";
import SelectCustomNote from "../../../components/SelectCustomNote";
import { useToast } from "../../../components/Toast/useToast";
import useGetAllCasesConfig from "../../../hooks/dristi/useGetAllCasesConfig";
import useSearchCaseService from "../../../hooks/dristi/useSearchCaseService";
import { ReactComponent as InfoIcon } from "../../../icons/info.svg";
import { CustomAddIcon, CustomArrowDownIcon, CustomDeleteIcon, RightArrow } from "../../../icons/svgIndex";
import { DRISTIService } from "../../../services";
import { formatDate } from "./CaseType";
import { sideMenuConfig } from "./Config";
import EditFieldsModal from "./EditFieldsModal";
import axios from "axios";
import {
  checkDuplicateMobileEmailValidation,
  checkIfscValidation,
  checkNameValidation,
  checkOnlyCharInCheque,
  chequeDateValidation,
  chequeDetailFileValidation,
  complainantValidation,
  debtLiabilityValidation,
  delayApplicationValidation,
  demandNoticeFileValidation,
  getAdvocatesAndPipRemainingFields,
  getAllAssignees,
  getComplainantName,
  getRespondentName,
  prayerAndSwornValidation,
  respondentValidation,
  showDemandNoticeModal,
  showToastForComplainant,
  signatureValidation,
  updateCaseDetails,
  validateDateForDelayApplication,
  witnessDetailsValidation,
} from "./EfilingValidationUtils";
import isEqual from "lodash/isEqual";
import isMatch from "lodash/isMatch";
import cloneDeep from "lodash/cloneDeep";
import CorrectionsSubmitModal from "../../../components/CorrectionsSubmitModal";
import { Urls } from "../../../hooks";
import useGetStatuteSection from "../../../hooks/dristi/useGetStatuteSection";
import { getFilingType, getSuffixByBusinessCode, getTaxPeriodByBusinessService } from "../../../Utils";
import useDownloadCasePdf from "../../../hooks/dristi/useDownloadCasePdf";
import DocViewerWrapper from "../../employee/docViewerWrapper";
import CaseLockModal from "./CaseLockModal";
import ConfirmCaseDetailsModal from "./ConfirmCaseDetailsModal";
import { DocumentUploadError } from "../../../Utils/errorUtil";
import ConfirmDcaSkipModal from "./ConfirmDcaSkipModal";
import ErrorDataModal from "./ErrorDataModal";

const OutlinedInfoIcon = () => (
  <svg width="19" height="19" viewBox="0 0 19 19" fill="none" xmlns="http://www.w3.org/2000/svg" style={{ position: "absolute", right: -22, top: 0 }}>
    <g clip-path="url(#clip0_7603_50401)">
      <path
        d="M8.70703 5.54232H10.2904V7.12565H8.70703V5.54232ZM8.70703 8.70898H10.2904V13.459H8.70703V8.70898ZM9.4987 1.58398C5.1287 1.58398 1.58203 5.13065 1.58203 9.50065C1.58203 13.8707 5.1287 17.4173 9.4987 17.4173C13.8687 17.4173 17.4154 13.8707 17.4154 9.50065C17.4154 5.13065 13.8687 1.58398 9.4987 1.58398ZM9.4987 15.834C6.00745 15.834 3.16536 12.9919 3.16536 9.50065C3.16536 6.0094 6.00745 3.16732 9.4987 3.16732C12.9899 3.16732 15.832 6.0094 15.832 9.50065C15.832 12.9919 12.9899 15.834 9.4987 15.834Z"
        fill="#3D3C3C"
      />
    </g>
    <defs>
      <clipPath id="clip0_7603_50401">
        <rect width="19" height="19" fill="white" />
      </clipPath>
    </defs>
  </svg>
);

function isEmptyValue(value) {
  if (!value) {
    return true;
  } else if (Array.isArray(value) || typeof value === "object") {
    return Object.keys(value).length === 0;
  } else if (typeof value === "string") {
    return value.trim().length === 0;
  } else {
    return false;
  }
}

const extractValue = (data, key) => {
  if (!key.includes(".") && data && typeof data === "object") {
    return data[key];
  }
  const keyParts = key.split(".");
  let value = data;
  keyParts.forEach((part) => {
    if (value && value.hasOwnProperty(part)) {
      value = value[part];
    } else {
      value = undefined;
    }
  });
  return value;
};

const Heading = (props) => {
  return <h1 className="heading-m">{props.label}</h1>;
};

const selectedArray = [
  "complainantDetails",
  "respondentDetails",
  "chequeDetails",
  "debtLiabilityDetails",
  "demandNoticeDetails",
  "delayApplications",
  "witnessDetails",
  "prayerSwornStatement",
  "advocateDetails",
];

const getTotalCountFromSideMenuConfig = (sideMenuConfig, selected) => {
  const countObj = { mandatory: 0, optional: 0 };
  for (let i = 0; i < sideMenuConfig.length; i++) {
    const childArray = sideMenuConfig[i]?.children;
    for (let j = 0; j < childArray.length; j++) {
      if (childArray[j].key === selected) {
        countObj.mandatory = childArray[j]?.initialMandatoryFieldCount;
        countObj.optional = childArray[j]?.initialOptionalFieldCount;
      }
    }
  }
  return countObj;
};

const extractCodeFromErrorMsg = (error) => {
  const statusCodeMatch = error?.message.match(/status code (\d+)/);
  const statusCode = statusCodeMatch ? parseInt(statusCodeMatch[1], 10) : null;
  return statusCode;
};

const stateSla = {
  PENDING_PAYMENT: 2,
};

const AccordionTabs = {
  REVIEW_CASE_FILE: "reviewCaseFile",
};

const dayInMillisecond = 24 * 3600 * 1000;

function EFilingCases({ path }) {
  const [params, setParmas] = useState({});
  const { t } = useTranslation();
  const toast = useToast();
  const history = useHistory();
  const [showErrorToast, setShowErrorToast] = useState(false);
  const [isDisabled, setIsDisabled] = useState(false);
  const [isSubmitDisabled, setIsSubmitDisabled] = useState(false);
  const todayDate = new Date().getTime();
  const userInfo = Digit?.UserService?.getUser()?.info;
  const roles = Digit.UserService.getUser()?.info?.roles;
  const isAdvocateFilingCase = roles?.some((role) => role.code === "ADVOCATE_ROLE");
  const moduleCode = "DRISTI";
  const token = window.localStorage.getItem("token");
  const isUserLoggedIn = Boolean(token);

  const setFormErrors = useRef(null);
  const setFormState = useRef(null);
  const resetFormData = useRef(null);
  const setFormDataValue = useRef(null);
  const clearFormDataErrors = useRef(null);

  const urlParams = new URLSearchParams(window.location.search);
  const selected = urlParams.get("selected") || sideMenuConfig?.[0]?.children?.[0]?.key;
  const caseId = urlParams.get("caseId");
  const [formdata, setFormdata] = useState(selected === "witnessDetails" ? [{}] : [{ isenabled: true, data: {}, displayindex: 0 }]);
  const [advPageData, setAdvPageData] = useState([]);
  const [errorCaseDetails, setErrorCaseDetails] = useState(null);
  const tenantId = window?.Digit.ULBService.getCurrentTenantId();
  const [parentOpen, setParentOpen] = useState(sideMenuConfig.findIndex((parent) => parent.children.some((child) => child.key === selected)));

  const [openConfigurationModal, setOpenConfigurationModal] = useState(false);
  const [openConfirmCourtModal, setOpenConfirmCourtModal] = useState(false);
  const [openConfirmCorrectionModal, setOpenConfirmCorrectionModal] = useState(false);
  const [serviceOfDemandNoticeModal, setServiceOfDemandNoticeModal] = useState(false);
  const [confirmDeleteModal, setConfirmDeleteModal] = useState(false);
  const [showConfirmMandatoryModal, setShowConfirmMandatoryModal] = useState(false);
  const [showConfirmOptionalModal, setShowConfirmOptionalModal] = useState(false);
  const [showReviewCorrectionModal, setShowReviewCorrectionModal] = useState(false);
  const [showReviewConfirmationModal, setShowReviewConfirmationModal] = useState(false);
  const [showCaseLockingModal, setShowCaseLockingModal] = useState(false);
  const [showConfirmDcaSkipModal, setShowConfirmDcaSkipModal] = useState(false);
  const [shouldShowConfirmDcaModal, setShouldShowConfirmDcaModal] = useState(false);
  const [prevIsDcaSkipped, setPrevIsDcaSkipped] = useState("");
  const [showErrorDataModal, setShowErrorDataModal] = useState({ page: "", showModal: false, errorData: [] });

  const [showConfirmCaseDetailsModal, setShowConfirmCaseDetailsModal] = useState(false);

  const [caseResubmitSuccess, setCaseResubmitSuccess] = useState(false);
  const [prevSelected, setPrevSelected] = useState("");
  const [errorMsg, setErrorMsg] = useState("");
  const homepagePath = "/digit-ui/citizen/dristi/home";
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [isLoader, setIsLoader] = useState(false);
  const [pdfDetails, setPdfDetails] = useState(null);
  const { downloadPdf } = useDownloadCasePdf();
  const [newCaseName, setNewCaseName] = useState("");
  const [showEditCaseNameModal, setShowEditCaseNameModal] = useState(false);
  const [modalCaseName, setModalCaseName] = useState("");
  const [isFilingParty, setIsFilingParty] = useState(false);

  const [{ showSuccessToast, successMsg }, setSuccessToast] = useState({
    showSuccessToast: false,
    successMsg: "",
  });
  const [deleteFormIndex, setDeleteFormIndex] = useState(null);
  const setFieldsRemainingInitially = () => {
    const array = [];
    for (let i = 0; i < selectedArray.length; i++) {
      const selected = selectedArray[i];
      array.push({
        selectedPage: selected,
        mandatoryTotalCount: getTotalCountFromSideMenuConfig(sideMenuConfig, selected)?.mandatory,
        optionalTotalCount: getTotalCountFromSideMenuConfig(sideMenuConfig, selected)?.optional,
      });
    }
    return array;
  };
  const [fieldsRemaining, setFieldsRemaining] = useState(() => setFieldsRemainingInitially());

  const checkAndGetMandatoryFieldLeftPages = useMemo(() => {
    const mandatoryRemainingPages = fieldsRemaining.filter((page) => page.mandatoryTotalCount !== 0) || [];
    return mandatoryRemainingPages;
  }, [fieldsRemaining]);

  const checkAndGetOptionalFieldLeftPages = useMemo(() => {
    const optionalRemainingPages = fieldsRemaining.filter((page) => page.optionalTotalCount !== 0) || [];
    return optionalRemainingPages;
  }, [fieldsRemaining]);

  const mandatoryFieldsLeftTotalCount = useMemo(() => {
    let count = 0;
    for (let i = 0; i < fieldsRemaining.length; i++) {
      count = count + fieldsRemaining[i].mandatoryTotalCount;
    }
    return count;
  }, [fieldsRemaining]);

  const optionalFieldsLeftTotalCount = useMemo(() => {
    let count = 0;
    for (let i = 0; i < fieldsRemaining.length; i++) {
      count = count + fieldsRemaining[i].optionalTotalCount;
    }
    return count;
  }, [fieldsRemaining]);

  const showMandatoryFieldsRemainingModal = useMemo(() => {
    if (selected === "reviewCaseFile") {
      if (mandatoryFieldsLeftTotalCount > 0) {
        setShowConfirmMandatoryModal(true);
        return true;
      } else return false;
    }
    return false;
  }, [selected, mandatoryFieldsLeftTotalCount]);

  const showOptionalFieldsRemainingModal = useMemo(() => {
    if (selected === "reviewCaseFile") {
      if (checkAndGetOptionalFieldLeftPages.length !== 0) {
        setShowConfirmOptionalModal(true);
        return true;
      } else return false;
    }
    return false;
  }, [selected, checkAndGetOptionalFieldLeftPages]);

  const { data: caseData, refetch: refetchCaseData, isLoading } = useSearchCaseService(
    {
      criteria: [
        {
          caseId: caseId,
          defaultFields: false,
        },
      ],
      tenantId,
    },
    {},
    `dristi-${caseId}`,
    caseId,
    Boolean(caseId)
  );

  const { data: individualData, isIndividualLoading, refetch } = window?.Digit.Hooks.dristi.useGetIndividualUser(
    {
      Individual: {
        userUuid: [userInfo?.uuid],
      },
    },
    { tenantId, limit: 1000, offset: 0 },
    moduleCode,
    "HOME",
    userInfo?.uuid && isUserLoggedIn
  );

  const userType = useMemo(() => individualData?.Individual?.[0]?.additionalFields?.fields?.find((obj) => obj.key === "userType")?.value, [
    individualData?.Individual,
  ]);

  const { data: filingTypeData, isLoading: isFilingTypeLoading } = Digit.Hooks.dristi.useGetStatuteSection("common-masters", [
    { name: "FilingType" },
  ]);

  const filingType = useMemo(() => getFilingType(filingTypeData?.FilingType, "CaseFiling"), [filingTypeData?.FilingType]);

  const getAllKeys = useMemo(() => {
    const keys = [];
    sideMenuConfig.forEach((parent) => {
      parent.children.forEach((child) => {
        keys.push(child.key);
      });
    });
    return keys;
  }, []);

  const deleteWarningText = useCallback((pageName) => {
    return (
      <div className="delete-warning-text">
        <h3>{`${t("CONFIRM_DELETE_FIRST_HALF")} ${pageName?.toLowerCase()} ${t("CONFIRM_DELETE_SECOND_HALF")}`}</h3>
      </div>
    );
  }, []);

  const mandatoryFieldsRemainingText = useCallback(() => {
    return (
      <div>
        <h3>{t("ENSURE_ALL_MANDATORY_ARE_FILLED")}</h3>
      </div>
    );
  }, []);

  const optionalFieldsRemainingText = useCallback((count) => {
    return (
      <div>
        <h3>{`${t("MORE_INFO_HELPS_FIRST_HALF")} ${count} ${t("MORE_INFO_HELPS_SECOND_HALF")}`}</h3>
      </div>
    );
  }, []);

  const pagesLabels = useMemo(() => {
    let keyValuePairs = {};
    sideMenuConfig.forEach((parent) => {
      if (parent.children && parent.children.length > 0) {
        parent.children.forEach((child) => {
          keyValuePairs[child.key] = child.label;
        });
      }
    });
    return keyValuePairs;
  }, [sideMenuConfig]);

  const nextSelected = useMemo(() => {
    const index = getAllKeys.indexOf(selected);
    if (index !== -1 && index + 1 < getAllKeys.length) {
      return getAllKeys[index + 1];
    } else {
      return null;
    }
  }, [getAllKeys, selected]);

  const caseDetails = useMemo(
    () => ({
      ...caseData?.criteria?.[0]?.responseList?.[0],
    }),
    [caseData]
  );

  const prevCaseDetails = useMemo(() => structuredClone(caseDetails), [caseDetails]);

  const scrutinyObj = useMemo(() => {
    return caseDetails?.additionalDetails?.scrutiny?.data || {};
  }, [caseDetails]);
  const judgeObj = useMemo(() => {
    return caseDetails?.additionalDetails?.judge || null;
  }, [caseDetails]);

  const countSectionErrors = (section) => {
    let total = 0;
    let sectionErrors = 0;
    let inputErrors = 0;
    let pages = new Set();
    Object.keys(section)?.forEach((key) => {
      let pageErrorCount = 0;
      if (section[key]) {
        if (section[key]?.scrutinyMessage?.FSOError) {
          total++;
          sectionErrors++;
          pageErrorCount++;
        }
        section[key]?.form?.forEach((item) => {
          Object.keys(item)?.forEach((field) => {
            if (item[field]?.FSOError && field != "image" && field != "title") {
              total++;
              inputErrors++;
              pageErrorCount++;
            }
          });
        });
      }
      if (pageErrorCount) {
        pages.add({ key, label: pagesLabels[key], errorCount: pageErrorCount });
      }
    });

    return { total, inputErrors, sectionErrors, pages: [...pages] };
  };

  const scrutinyErrors = useMemo(() => {
    const errorCount = {};
    for (const key in scrutinyObj) {
      if (typeof scrutinyObj[key] === "object" && scrutinyObj[key] !== null) {
        if (!errorCount[key]) {
          errorCount[key] = { total: 0, sectionErrors: 0, inputErrors: 0 };
        }
        const temp = countSectionErrors(scrutinyObj[key]);
        errorCount[key] = {
          total: errorCount[key].total + temp.total,
          sectionErrors: errorCount[key].sectionErrors + temp.sectionErrors,
          inputErrors: errorCount[key].inputErrors + temp.inputErrors,
          pages: temp.pages,
        };
      }
    }
    return errorCount;
  }, [scrutinyObj]);

  const errorPages = useMemo(() => {
    const pages = Object.values(scrutinyErrors)
      ?.flatMap((val) => val?.pages)
      ?.filter((val) => val !== undefined);
    return pages.sort((a, b) => {
      const keyA = a.key;
      const keyB = b.key;
      return getAllKeys.indexOf(keyA) - getAllKeys.indexOf(keyB);
    });
  }, [scrutinyErrors]);

  const sectionWiseErrors = useMemo(() => {
    let obj = {};
    Object.values(scrutinyObj || {}).forEach((item) => {
      Object.keys(item || {}).forEach((key) => {
        if (item[key]?.scrutinyMessage?.FSOError) {
          obj[key] = item[key]?.scrutinyMessage?.FSOError;
        }
      });
    });
    return obj;
  }, [scrutinyObj]);

  const totalErrors = useMemo(() => {
    let total = 0;
    let sectionErrors = 0;
    let inputErrors = 0;

    for (const key in scrutinyErrors) {
      total += scrutinyErrors[key].total || 0;
      sectionErrors += scrutinyErrors[key].sectionErrors || 0;
      inputErrors += scrutinyErrors[key].inputErrors || 0;
    }

    return {
      total,
      sectionErrors,
      inputErrors,
    };
  }, [scrutinyErrors]);

  const state = useMemo(() => caseDetails?.status, [caseDetails]);

  const isCaseReAssigned = useMemo(() => state === CaseWorkflowState.CASE_REASSIGNED, [state]);
  const isPendingReESign = useMemo(() => state === CaseWorkflowState.PENDING_RE_E_SIGN, [state]);
  const isPendingESign = useMemo(() => state === CaseWorkflowState.PENDING_E_SIGN, [state]);
  const isDisableAllFieldsMode = !(
    state === CaseWorkflowState.CASE_REASSIGNED ||
    state === CaseWorkflowState.DRAFT_IN_PROGRESS ||
    state === CaseWorkflowState.PENDING_E_SIGN ||
    state === CaseWorkflowState.PENDING_RE_E_SIGN
  );
  const isDraftInProgress = state === CaseWorkflowState.DRAFT_IN_PROGRESS;
  const { data: courtRoomDetails, isLoading: isCourtIdsLoading } = useGetStatuteSection("common-masters", [{ name: "Court_Rooms" }]);
  const courtRooms = useMemo(() => courtRoomDetails?.Court_Rooms || [], [courtRoomDetails]);

  useEffect(() => {
    const isDcaSkipped = caseDetails?.caseDetails?.["delayApplications"]?.formdata?.[0]?.data?.isDcaSkippedInEFiling?.code;
    if (isDcaSkipped !== prevIsDcaSkipped) {
      setPrevIsDcaSkipped(isDcaSkipped);
    }
  }, [caseDetails, prevIsDcaSkipped]);

  useEffect(() => {
    setParentOpen(sideMenuConfig.findIndex((parent) => parent.children.some((child) => child.key === selected)));
  }, [selected]);

  useEffect(() => {
    if (Object.keys(caseDetails).length !== 0) {
      const fieldsRemainingCopy = structuredClone(fieldsRemaining);
      const additionalDetailsArray = ["complainantDetails", "respondentDetails", "witnessDetails", "prayerSwornStatement", "advocateDetails"];
      const caseDetailsArray = ["chequeDetails", "debtLiabilityDetails", "demandNoticeDetails", "delayApplications"];
      for (const key of additionalDetailsArray) {
        if (caseDetails?.additionalDetails?.[key]) {
          const index = fieldsRemainingCopy.findIndex((fieldsRemainingCopy) => fieldsRemainingCopy.selectedPage === key);
          fieldsRemainingCopy[index] = setMandatoryAndOptionalRemainingFields(caseDetails?.additionalDetails?.[key]?.formdata, key);
        }
      }
      for (const key of caseDetailsArray) {
        if (caseDetails?.caseDetails?.[key]) {
          const index = fieldsRemainingCopy.findIndex((fieldsRemainingCopy) => fieldsRemainingCopy.selectedPage === key);
          fieldsRemainingCopy[index] = setMandatoryAndOptionalRemainingFields(caseDetails?.caseDetails?.[key]?.formdata, key);
        }
      }
      if (isDraftInProgress) {
        setFieldsRemaining(fieldsRemainingCopy);
      } else {
        setFieldsRemaining([{ mandatoryTotalCount: 0, optionalTotalCount: 0 }]);
      }
    }
  }, [caseDetails]);

  useEffect(() => {
    const filingParty = caseDetails?.auditDetails?.createdBy === userInfo?.uuid;
    setIsFilingParty(filingParty);

    if (caseDetails && !filingParty && !isLoading) {
      history.replace(`?caseId=${caseId}&selected=${AccordionTabs.REVIEW_CASE_FILE}`);
    }
  }, [caseDetails, caseId, history, isFilingParty, isLoading, userInfo?.uuid]);

  const completedComplainants = useMemo(() => {
    // check TODO: apply filter for formdata which is enabled and completed
    return caseDetails?.additionalDetails?.["complainantDetails"]?.formdata;
  }, [caseDetails]);

  useEffect(() => {
    const data =
      caseDetails?.additionalDetails?.[selected]?.formdata ||
      caseDetails?.caseDetails?.[selected]?.formdata ||
      (selected === "witnessDetails" ? [{}] : [{ isenabled: true, data: {}, displayindex: 0 }]);
    if (selected === "advocateDetails") {
      const newAdvData = []; //  we will update the advocate data in the same order as order of complainant forms
      const advData = caseDetails?.additionalDetails?.[selected]?.formdata || [];
      for (let i = 0; i < completedComplainants?.length; i++) {
        const complainantIndividualId = completedComplainants?.[i]?.data?.complainantVerification?.individualDetails?.individualId;
        let isAdvDataFound = false;
        for (let j = 0; j < advData?.length; j++) {
          // we are checking if already an advocate data exist for this complaiant
          if (advData?.[j]?.data?.multipleAdvocatesAndPip?.boxComplainant?.individualId === complainantIndividualId) {
            isAdvDataFound = true;
            const newObj = structuredClone(advData[j]);
            newObj.data.multipleAdvocatesAndPip.boxComplainant = { ...newObj.data.multipleAdvocatesAndPip.boxComplainant, index: i };
            newAdvData.push(newObj);
            break;
          }
        }
        // if no adv data is found corr to this complaint, add a new adv data form with this complainant details.
        if (!isAdvDataFound) {
          newAdvData.push({
            isenabled: true,
            data: {
              multipleAdvocatesAndPip: {
                boxComplainant: {
                  firstName: completedComplainants?.[i]?.data?.firstName || "",
                  middleName: completedComplainants?.[i]?.data?.middleName || "",
                  lastName: completedComplainants?.[i]?.data?.lastName || "",
                  individualId: completedComplainants?.[i]?.data?.complainantVerification?.individualDetails?.individualId || "",
                  mobileNumber: completedComplainants?.[i]?.data?.complainantVerification?.mobileNumber || "",
                  index: i,
                },
                isComplainantPip: {
                  code: "NO",
                  name: "No",
                  isEnabled: true,
                },
                showAffidavit: false,
              },
            },
            displayindex: 0,
          });
        }
      }
      setFormdata(newAdvData);
      setAdvPageData(newAdvData);
    }
    if (selected !== "advocateDetails") {
      setFormdata(data);
    }
    if (selected === "addSignature" && !caseDetails?.additionalDetails?.signedCaseDocument && !isLoading) {
      setShowReviewCorrectionModal(true);
    }
    if (selected === "addSignature" && !caseDetails?.additionalDetails?.["reviewCaseFile"]?.isCompleted && !isLoading) {
      setShowReviewCorrectionModal(true);
    }
  }, [selected, caseDetails, isLoading, completedComplainants]);

  const closeToast = () => {
    setShowErrorToast(false);
    setErrorMsg("");
    setSuccessToast((prev) => ({
      ...prev,
      showSuccessToast: false,
      successMsg: "",
    }));
  };

  useEffect(() => {
    let timer;
    if (showErrorToast || showSuccessToast) {
      timer = setTimeout(() => {
        closeToast();
      }, 2000);
    }
    return () => clearTimeout(timer);
  }, [showErrorToast, showSuccessToast]);

  useEffect(() => {
    if (!errorCaseDetails && isCaseReAssigned) {
      setErrorCaseDetails(caseDetails);
    }
  }, [caseDetails, errorCaseDetails]);

  const isDelayCondonation = useMemo(() => {
    return caseDetails?.caseDetails?.["demandNoticeDetails"]?.formdata?.some((data) => {
      const dateObj = new Date(data?.data?.dateOfAccrual);
      const currentDate = new Date();
      const monthDifference = currentDate.getMonth() - dateObj.getMonth() + (currentDate.getFullYear() - dateObj.getFullYear()) * 12;
      if (monthDifference > 1) {
        return true;
      } else if (monthDifference === 0) {
        return false;
      } else if (currentDate.getDate() > dateObj.getDate()) {
        return true;
      } else {
        return false;
      }
    });
  }, [caseDetails]);

  const getDefaultValues = useCallback(
    (index) => {
      if (isCaseReAssigned && errorCaseDetails) {
        if (selected === "reviewCaseFile") {
          return scrutinyObj;
        }
        return (
          errorCaseDetails?.additionalDetails?.[selected]?.formdata?.[index]?.data ||
          errorCaseDetails?.caseDetails?.[selected]?.formdata?.[index]?.data ||
          formdata[index]?.data
        );
      }

      if (caseDetails?.status === "DRAFT_IN_PROGRESS" && selected === "delayApplications") {
        if (isDelayCondonation) {
          const data = {
            ...caseDetails?.caseDetails?.[selected]?.formdata?.[index]?.data,
            delayCondonationType: {
              code: "NO",
              name: "NO",
              showForm: true,
              isEnabled: true,
            },

            isDcaSkippedInEFiling: caseDetails?.caseDetails?.[selected]?.formdata?.[index]?.data?.isDcaSkippedInEFiling
              ? caseDetails?.caseDetails?.[selected]?.formdata?.[index]?.data?.isDcaSkippedInEFiling
              : {
                  code: "NO",
                  name: "NO",
                  showDcaFileUpload: true,
                },
          };
          return data;
        } else {
          return {
            ...caseDetails?.caseDetails?.[selected]?.formdata?.[index]?.data,
            delayCondonationType: {
              code: "YES",
              name: "YES",
              showForm: false,
              isEnabled: true,
            },
          };
        }
      }

      if (caseDetails?.status === "DRAFT_IN_PROGRESS" && selected === "advocateDetails") {
        return advPageData[index]?.data?.multipleAdvocatesAndPip;
      }

      return (
        caseDetails?.additionalDetails?.[selected]?.formdata?.[index]?.data ||
        caseDetails?.caseDetails?.[selected]?.formdata?.[index]?.data ||
        formdata[index]?.data
      );
    },
    [
      caseDetails?.status,
      caseDetails?.additionalDetails,
      caseDetails?.caseDetails,
      errorCaseDetails,
      formdata,
      isCaseReAssigned,
      selected,
      scrutinyObj,
      userType,
      completedComplainants,
      isDelayCondonation,
      advPageData,
    ]
  );

  const accordion = useMemo(() => {
    return sideMenuConfig.map((parent, pIndex) => ({
      ...parent,
      isOpen: pIndex === parentOpen,
      children: parent.children.map((child, cIndex) => ({
        ...child,
        checked: child.key === selected,
        isCompleted: caseDetails?.additionalDetails?.[child.key]?.isCompleted || caseDetails?.caseDetails?.[child.key]?.isCompleted,
      })),
    }));
  }, [caseDetails, parentOpen, selected]);

  const { data: caseDetailsConfig, isLoading: isGetAllCasesLoading } = useGetAllCasesConfig();

  const pageConfig = useMemo(() => {
    if (!caseDetailsConfig) {
      return {
        formconfig: [],
      };
    }
    return caseDetailsConfig
      .find((parent) => parent.children.some((child) => child.key === selected))
      ?.children?.find((child) => child.key === selected)?.pageConfig;
  }, [caseDetailsConfig, selected]);

  const formConfig = useMemo(() => {
    return pageConfig?.formconfig;
  }, [pageConfig?.formconfig]);
  const multiUploadList = useMemo(
    () =>
      formConfig?.flatMap((config) =>
        config.body
          .filter((item) => ["SelectCustomDragDrop"].includes(item.component))
          .map((item) => {
            const { key } = item;
            const fieldType = item?.populators?.inputs?.[0]?.name;
            return { key, fieldType };
          })
      ),
    [formConfig]
  );
  if (!getAllKeys.includes(selected) || !formConfig) {
    setPrevSelected(selected);
    history.push(`?caseId=${caseId}&selected=${getAllKeys[0]}`);
  }

  const confirmModalConfig = useMemo(() => {
    return pageConfig?.confirmmodalconfig;
  }, [pageConfig?.confirmmodalconfig]);

  const CloseBtn = (props) => {
    return (
      <div onClick={props?.onClick} style={{ height: "100%", display: "flex", alignItems: "center", paddingRight: "20px", cursor: "pointer" }}>
        <CloseSvg />
      </div>
    );
  };

  const isDependentEnabled = useMemo(() => {
    let result = false;
    formConfig.forEach((config) => {
      if (config?.body && Array.isArray(config?.body)) {
        config?.body?.forEach((bodyItem) => {
          if (bodyItem?.populators?.isDependent) {
            result = true;
          }
        });
      }
    });
    return result;
  }, [formConfig]);

  const modifiedFormConfig = useMemo(() => {
    let modifiedFormData = formdata;
    if (!isDependentEnabled) {
      modifiedFormData = modifiedFormData.map(() => {
        if (selected === "reviewCaseFile") {
          return formConfig.map((config) => {
            return {
              ...config,
              body: config?.body?.map((body) => {
                return {
                  ...body,
                  populators: {
                    inputs: body?.populators?.inputs?.map((input) => {
                      let dataobj =
                        input?.key === "advocateDetails"
                          ? caseDetails?.additionalDetails?.[input?.key]?.formdata?.[0]?.data?.advocateName
                            ? caseDetails?.additionalDetails?.[input?.key]?.formdata
                            : [
                                {
                                  data: {
                                    advocateName: "",
                                    barRegistrationNumber: "",
                                    vakalatnamaFileUpload: {},
                                    isAdvocateRepresenting: {
                                      code: "NO",
                                      name: "No",
                                      showForm: true,
                                      isEnabled: true,
                                    },
                                  },
                                },
                              ]
                          : caseDetails?.additionalDetails?.[input?.key]?.formdata || caseDetails?.caseDetails?.[input?.key]?.formdata || {};
                      if (isCaseReAssigned) {
                        dataobj =
                          input?.key === "advocateDetails"
                            ? errorCaseDetails?.additionalDetails?.[input?.key]?.formdata?.[0]?.data?.advocateName
                              ? errorCaseDetails?.additionalDetails?.[input?.key]?.formdata
                              : [
                                  {
                                    data: {
                                      advocateName: "",
                                      barRegistrationNumber: "",
                                      vakalatnamaFileUpload: {},
                                      isAdvocateRepresenting: {
                                        code: "NO",
                                        name: "No",
                                        showForm: true,
                                        isEnabled: true,
                                      },
                                    },
                                  },
                                ]
                            : errorCaseDetails?.additionalDetails?.[input?.key]?.formdata ||
                              errorCaseDetails?.caseDetails?.[input?.key]?.formdata ||
                              {};
                      }
                      return {
                        ...input,
                        data: dataobj,
                        isFilingParty: isFilingParty,
                      };
                    }),
                  },
                };
              }),
            };
          });
        }
        if (selected === "addSignature") {
          return formConfig.map((config) => {
            return {
              ...config,
              body: config?.body
                ?.filter((data) => {
                  if (
                    data.dependentOn &&
                    !extractValue(caseDetails?.additionalDetails?.[data.dependentOn]?.formdata?.[0]?.data, data?.dependentKey)
                  ) {
                    return false;
                  }
                  return true;
                })
                .map((body) => {
                  return {
                    ...body,
                    populators: {
                      inputs: body?.populators?.inputs?.map((input) => {
                        return {
                          ...input,
                          data:
                            input.key === "advocateDetails"
                              ? [
                                  {
                                    name:
                                      caseDetails?.additionalDetails?.[input.key]?.formdata?.[0]?.data?.advocateBarRegNumberWithName?.[0]
                                        ?.advocateName,
                                  },
                                ] || []
                              : caseDetails?.additionalDetails?.[input.key]?.formdata?.map((data) => ({
                                  name: `${data?.data?.firstName || ""} ${data?.data?.middleName || ""} ${data?.data?.lastName || ""}`,
                                })),
                        };
                      }),
                    },
                  };
                }),
            };
          });
        }
        return formConfig.map((config) => {
          return {
            ...config,
            body: config?.body.map((body) => {
              if (body?.labelChildren === "optional" && Object.keys(caseDetails?.additionalDetails?.scrutiny?.data || {}).length === 0) {
                body.labelChildren = <span style={{ color: "#77787B" }}>&nbsp;{`${t("CS_IS_OPTIONAL")}`}</span>;
              }

              if (body?.labelChildren === "OutlinedInfoIcon" && Object.keys(caseDetails?.additionalDetails?.scrutiny?.data || {}).length === 0) {
                body.labelChildren = (
                  <React.Fragment>
                    <span style={{ color: "#77787B", position: "relative" }} data-tip data-for={`${body.label}-tooltip`}>
                      {" "}
                      <OutlinedInfoIcon />
                    </span>
                    <ReactTooltip id={`${body.label}-tooltip`} place="bottom" content={body?.tooltipValue || ""}>
                      {t(body?.tooltipValue || body.label)}
                    </ReactTooltip>
                  </React.Fragment>
                );
              }

              if ("inputs" in body?.populators && Array.isArray(body?.populators.inputs)) {
                return {
                  ...body,
                  populators: {
                    inputs: body?.populators.inputs.map((input) => {
                      if (input?.validation) {
                        if (
                          input?.validation?.pattern &&
                          input?.validation?.pattern?.moduleName &&
                          input?.validation?.pattern?.masterName &&
                          input?.validation?.pattern?.patternType
                        ) {
                          input.validation = {
                            ...input.validation,
                            pattern: Digit?.Customizations?.[input?.validation?.pattern?.masterName]?.[input?.validation?.pattern?.moduleName](
                              input?.validation?.pattern?.patternType
                            ),
                          };
                        }
                      }
                      return {
                        ...input,
                      };
                    }),
                  },
                };
              } else if ("populators" in body) {
                const validationUpdate = {};
                if (
                  body?.populators?.validation &&
                  body?.populators?.validation?.pattern &&
                  body?.populators?.validation?.pattern?.moduleName &&
                  body?.populators?.validation?.pattern?.masterName &&
                  body?.populators?.validation?.pattern?.patternType
                ) {
                  validationUpdate.pattern = {
                    value: Digit?.Customizations?.[body?.populators?.validation?.pattern?.masterName]?.[
                      body?.populators?.validation?.pattern?.moduleName
                    ](body?.populators?.validation?.pattern?.patternType),
                    message: body?.populators?.validation?.pattern?.message,
                  };
                }

                if (
                  body?.populators?.validation &&
                  body?.populators?.validation?.max &&
                  body?.populators?.validation?.max?.moduleName &&
                  body?.populators?.validation?.max?.masterName &&
                  body?.populators?.validation?.max?.patternType
                ) {
                  validationUpdate.max = Digit?.Customizations?.[body?.populators?.validation?.max?.masterName]?.[
                    body?.populators?.validation?.max?.moduleName
                  ](body?.populators?.validation?.max?.patternType);
                }

                return {
                  ...body,
                  populators: {
                    ...body?.populators,
                    validation: {
                      ...body?.populators?.validation,
                      ...validationUpdate,
                    },
                  },
                };
              }
              return {
                ...body,
              };
            }),
          };
        });
      });
      if ((!isCaseReAssigned && !isPendingESign && !isPendingReESign) || selected === "addSignature" || selected === "reviewCaseFile") {
        return modifiedFormData;
      }
    }
    return modifiedFormData.map(({ data }, index) => {
      let disableConfigFields = [];
      formConfig.forEach((config) => {
        config.body.forEach((body) => {
          if ("disableConfigFields" in body && "disableConfigKey" in body && "key" in body) {
            if (!!data?.[body.key]?.[body.disableConfigKey]) {
              disableConfigFields = [...disableConfigFields, ...body.disableConfigFields];
            }
          }
        });
      });
      return formConfig
        .filter((config) => {
          const dependentKeys = config?.dependentKey;
          if (!dependentKeys) {
            return config;
          }
          let show = true;
          for (const key in dependentKeys) {
            const nameArray = dependentKeys[key];
            for (const name of nameArray) {
              if (Array.isArray(data?.[key]?.[name]) && data?.[key]?.[name]?.length === 0) {
                show = false;
              } else show = show && Boolean(data?.[key]?.[name]);
            }
          }
          return show && config;
        })
        .map((config) => {
          if (config.updateLabelOn && config.updateLabel.key && config.defaultLabel.key) {
            if (extractValue(data, config.updateLabelOn)) {
              config[config.updateLabel.key] = config.updateLabel.value;
            } else {
              config[config.defaultLabel.key] = config.defaultLabel.value;
            }
          }
          return {
            ...config,
            body: config?.body.map((body) => {
              body.state = state;
              if (body?.addUUID && body?.uuid !== index) {
                body.uuid = index;
                body.isUserVerified = disableConfigFields.some((field) => {
                  return field === body?.key;
                });
              }

              //isMandatory
              if (
                body?.isDocDependentOn &&
                body?.isDocDependentKey &&
                data?.[body?.isDocDependentOn]?.[body?.isDocDependentKey] &&
                body?.component === "SelectCustomDragDrop"
              ) {
                body.isMandatory = true;
              } else if (body?.isDocDependentOn && body?.isDocDependentKey && body?.component === "SelectCustomDragDrop") {
                body.isMandatory = false;
              }

              //withoutLabelFieldPair
              if (body?.isDocDependentOn && body?.isDocDependentKey && !data?.[body?.isDocDependentOn]?.[body?.isDocDependentKey]) {
                body.withoutLabelFieldPair = true;
              } else {
                body.withoutLabelFieldPair = false;
              }
              if (selected === "delayApplications") {
                if (isDelayCondonation && body?.key === "delayCondonationType") {
                  body.disable = true;
                }
              }

              if (body?.labelChildren === "optional" && Object.keys(caseDetails?.additionalDetails?.scrutiny?.data || {}).length === 0) {
                body.labelChildren = <span style={{ color: "#77787B" }}>&nbsp;{`${t("CS_IS_OPTIONAL")}`}</span>;
              }

              if (body?.labelChildren === "OutlinedInfoIcon" && Object.keys(caseDetails?.additionalDetails?.scrutiny?.data || {}).length === 0) {
                body.labelChildren = (
                  <React.Fragment>
                    <span style={{ color: "#77787B", position: "relative" }} data-tip data-for={`${body.label}-tooltip`}>
                      {" "}
                      <OutlinedInfoIcon />
                    </span>
                    <ReactTooltip id={`${body.label}-tooltip`} place="bottom" content={body?.tooltipValue || ""}>
                      {t(body?.tooltipValue || body.label)}
                    </ReactTooltip>
                  </React.Fragment>
                );
              }
              if (
                body?.validation?.pattern &&
                body?.validation?.pattern?.moduleName &&
                body?.validation?.pattern?.masterName &&
                body?.validation?.pattern?.patternType
              ) {
                body.validation = {
                  ...body.validation,
                  pattern: Digit?.Customizations?.[body?.validation?.pattern?.masterName]?.[body?.validation?.pattern?.moduleName](
                    body?.validation?.pattern?.patternType
                  ),
                };
              }

              if (body.updateLabelOn && body.updateLabel.key && body.defaultLabel.key) {
                if (extractValue(data, body.updateLabelOn)) {
                  body[body.updateLabel.key] = body.updateLabel.value;
                } else {
                  body[body.defaultLabel.key] = body.defaultLabel.value;
                }
              }

              if ("inputs" in body?.populators && Array.isArray(body?.populators.inputs)) {
                return {
                  ...body,
                  populators: {
                    inputs: body?.populators.inputs.map((input) => {
                      if (input.updateLabelOn && input.updateLabel.key && input.defaultLabel.key) {
                        if (extractValue(data, input.updateLabelOn)) {
                          input[input.updateLabel.key] = input.updateLabel.value;
                        } else {
                          input[input.defaultLabel.key] = input.defaultLabel.value;
                        }
                      }

                      if (input?.validation) {
                        if (
                          input?.validation?.pattern &&
                          input?.validation?.pattern?.moduleName &&
                          input?.validation?.pattern?.masterName &&
                          input?.validation?.pattern?.patternType
                        ) {
                          input.validation = {
                            ...input.validation,
                            pattern: Digit?.Customizations?.[input?.validation?.pattern?.masterName]?.[input?.validation?.pattern?.moduleName](
                              input?.validation?.pattern?.patternType
                            ),
                          };
                        }
                      }

                      if (
                        disableConfigFields.some((field) => {
                          if (Array.isArray(input?.name)) return field === input?.key;
                          return field === input?.name;
                        })
                      ) {
                        return {
                          ...input,
                          disable: input?.shouldBeEnabled ? false : true,
                          isDisabled: input?.shouldBeEnabled ? false : true,
                        };
                      }

                      // 225 Inquiry Affidavit Validation in respondent details
                      if (selected === "respondentDetails") {
                        if (
                          Array.isArray(data?.addressDetails) &&
                          data?.addressDetails?.some(
                            (address) =>
                              ((address?.addressDetails?.pincode !==
                                caseDetails?.additionalDetails?.["complainantDetails"]?.formdata?.[0]?.data?.addressDetails?.pincode &&
                                caseDetails?.additionalDetails?.["complainantDetails"]?.formdata?.[0]?.data?.complainantType?.code ===
                                  "INDIVIDUAL") ||
                                (address?.addressDetails?.pincode !==
                                  caseDetails?.additionalDetails?.["complainantDetails"]?.formdata?.[0]?.data?.addressCompanyDetails?.pincode &&
                                  caseDetails?.additionalDetails?.["complainantDetails"]?.formdata?.[0]?.data?.complainantType?.code ===
                                    "REPRESENTATIVE")) &&
                              body?.key === "inquiryAffidavitFileUpload"
                          )
                        ) {
                          // delete input.isOptional;
                          body.isMandatory = false;
                          return {
                            ...input,
                            hideDocument: false,
                          };
                        } else if (body?.key === "inquiryAffidavitFileUpload") {
                          delete body.isMandatory;
                          return {
                            ...input,
                            isOptional: "CS_IS_OPTIONAL",
                            hideDocument: false,
                          };
                        } else {
                          return {
                            ...input,
                          };
                        }
                      }
                      return {
                        ...input,
                      };
                    }),
                  },
                };
              } else if ("populators" in body) {
                const validationUpdate = {};
                if (
                  body?.populators?.validation &&
                  body?.populators?.validation?.pattern &&
                  body?.populators?.validation?.pattern?.moduleName &&
                  body?.populators?.validation?.pattern?.masterName &&
                  body?.populators?.validation?.pattern?.patternType
                ) {
                  validationUpdate.pattern = {
                    value: Digit?.Customizations?.[body?.populators?.validation?.pattern?.masterName]?.[
                      body?.populators?.validation?.pattern?.moduleName
                    ](body?.populators?.validation?.pattern?.patternType),
                    message: body?.populators?.validation?.pattern?.message,
                  };
                }
                if (
                  body?.populators?.validation &&
                  body?.populators?.validation?.max &&
                  body?.populators?.validation?.max?.moduleName &&
                  body?.populators?.validation?.max?.masterName &&
                  body?.populators?.validation?.max?.patternType
                ) {
                  validationUpdate.max = Digit?.Customizations?.[body?.populators?.validation?.max?.masterName]?.[
                    body?.populators?.validation?.max?.moduleName
                  ](body?.populators?.validation?.max?.patternType);
                }

                let disableDelayCondonationType = false;

                if (selected === "delayApplications") {
                  if (body?.key === "delayCondonationType") {
                    disableDelayCondonationType = true;
                  }
                  if (body?.key === "isDcaSkippedInEFiling" && !isDelayCondonation) {
                    return {};
                  }
                }
                return {
                  ...body,
                  disable: disableConfigFields.some((field) => field === body?.populators?.name) || disableDelayCondonationType,
                  populators: {
                    ...body?.populators,
                    validation: {
                      ...body?.populators?.validation,
                      ...validationUpdate,
                    },
                  },
                };
              }
              return {
                ...body,
                disable: disableConfigFields.some((field) => field === body?.name),
              };
            }),
          };
        })
        .map((config) => {
          const scrutiny = {};
          Object.keys(scrutinyObj).forEach((item) => {
            Object.keys(scrutinyObj[item]).forEach((key) => {
              scrutiny[key] = scrutinyObj[item][key];
            });
          });
          let updatedBody = [];
          if (Object.keys(scrutinyObj).length > 0 || isPendingESign || isPendingReESign) {
            updatedBody = config.body
              .map((formComponent) => {
                let key = formComponent.key || formComponent.populators?.name;
                if (formComponent.type === "component") {
                  if (
                    ["SelectCustomDragDrop", "SelectBulkInputs", "SelectCustomTextArea", "SelectUploadFiles", "SelectUserTypeComponent"].includes(
                      formComponent.component
                    )
                  ) {
                    key = formComponent.key + "." + formComponent.populators?.inputs?.[0]?.name;
                  }
                  if (formComponent.component === "VerifyPhoneNumber") {
                    key = formComponent.key + "." + formComponent?.name;
                  }
                }
                if (selected === "demandNoticeDetails" && formComponent.component === "SelectUserTypeComponent") {
                  key =
                    formComponent.key + "." + formComponent.populators?.inputs?.[0]?.name + "." + formComponent.populators?.inputs?.[0]?.optionsKey;
                }
                if (["debtLiabilityDetails", "delayApplications"].includes(selected) && ["dropdown", "radio"].includes(formComponent.type)) {
                  key = formComponent.key + "." + formComponent?.populators?.optionsKey;
                }
                if (selected === "delayApplications" && formComponent.component === "CustomRadioInfoComponent") {
                  key = formComponent.key + "." + formComponent?.populators?.optionsKey;
                }
                if (selected === "complainantDetails" && formComponent.component === "CustomRadioInfoComponent") {
                  key = formComponent.key + "." + formComponent?.populators?.optionsKey;
                }
                if (selected === "complainantDetails" && formComponent.component === "VerificationComponent") {
                  key = "complainantVerification.individualDetails.document";
                }
                const modifiedFormComponent = cloneDeep(formComponent);
                if (modifiedFormComponent?.labelChildren === "optional") {
                  modifiedFormComponent.labelChildren = <span style={{ color: "#77787B" }}>&nbsp;{`${t("CS_IS_OPTIONAL")}`}</span>;
                }
                modifiedFormComponent.state = state;
                if (
                  modifiedFormComponent?.labelChildren === "OutlinedInfoIcon" &&
                  Object.keys(caseDetails?.additionalDetails?.scrutiny?.data || {}).length === 0
                ) {
                  modifiedFormComponent.labelChildren = (
                    <React.Fragment>
                      <span style={{ color: "#77787B", position: "relative" }} data-tip data-for={`${modifiedFormComponent.label}-tooltip`}>
                        {" "}
                        <OutlinedInfoIcon />
                      </span>
                      <ReactTooltip id={`${modifiedFormComponent.label}-tooltip`} place="bottom" content={modifiedFormComponent?.tooltipValue || ""}>
                        {t(modifiedFormComponent?.tooltipValue || modifiedFormComponent.label)}
                      </ReactTooltip>
                    </React.Fragment>
                  );
                }

                modifiedFormComponent.disable = scrutiny?.[selected]?.scrutinyMessage?.FSOError || (judgeObj && !isPendingReESign) ? false : true;

                if (scrutiny?.[selected] && scrutiny?.[selected]?.form?.[index]) {
                  if (formComponent.component == "SelectUploadFiles") {
                    if (formComponent.key + "." + formComponent.populators?.inputs?.[0]?.name in scrutiny?.[selected]?.form?.[index]) {
                      key = formComponent.key + "." + formComponent.populators?.inputs?.[0]?.name;
                    }
                    if (formComponent.key + "." + formComponent.populators?.inputs?.[1]?.name in scrutiny?.[selected]?.form?.[index]) {
                      key = formComponent.key + "." + formComponent.populators?.inputs?.[1]?.name;
                    }
                  }
                  if (
                    selected === "debtLiabilityDetails" &&
                    formComponent.component === "CustomInput" &&
                    scrutiny?.[selected]?.form?.[index]?.["liabilityType.name"]?.FSOError
                  ) {
                    modifiedFormComponent.disable = false;
                  }
                  if (selected === "chequeDetails" && key === "policeStation") {
                    key = key + "." + formComponent?.populators?.optionsKey;
                  }
                  if (selected === "delayApplications" && key === "delayCondonationType.name") {
                    modifiedFormComponent.disable = true;
                  }
                  if (key in scrutiny?.[selected]?.form?.[index] && scrutiny?.[selected]?.form?.[index]?.[key]?.FSOError) {
                    if (key === "complainantVerification.individualDetails.document") {
                      modifiedFormComponent.isScrutiny = true;
                    }
                    modifiedFormComponent.disable = false;
                    modifiedFormComponent.withoutLabel = true;
                    modifiedFormComponent.disableScrutinyHeader = true;
                    return [
                      {
                        type: "component",
                        component: "ScrutinyInfo",
                        key: `${key}Scrutiny`,
                        label: modifiedFormComponent.label,
                        populators: {
                          scrutinyMessage: scrutiny?.[selected].form[index][key].FSOError,
                        },
                      },
                      modifiedFormComponent,
                    ];
                  }
                }
                return modifiedFormComponent;
              })
              .flat();
          } else {
            updatedBody = config.body.map((formComponent) => {
              return formComponent;
            });
          }
          return {
            ...config,
            body: updatedBody,
          };
        });
    });
  }, [
    formdata,
    isDependentEnabled,
    isCaseReAssigned,
    selected,
    formConfig,
    caseDetails?.additionalDetails,
    caseDetails?.caseDetails,
    t,
    scrutinyObj,
    isPendingESign,
    isPendingReESign,
  ]);

  const activeForms = useMemo(() => {
    return formdata.filter((item) => item.isenabled === true).length;
  }, [formdata]);

  const handleAddForm = () => {
    setFormdata([...formdata, { isenabled: true, data: {}, displayindex: activeForms }]);
  };

  // const handleDeleteForm = (index) => {
  //   const newArray = formdata.map((item, i) => ({
  //     ...item,
  //     isenabled: index === i ? false : item.isenabled,
  //     displayindex: i < index ? item.displayindex : i === index ? -Infinity : item.displayindex - 1,
  //   }));
  //   setConfirmDeleteModal(true);
  //   setFormdata(newArray);
  // };

  const handleSkip = () => {
    setShowConfirmOptionalModal(false);
  };

  const createPendingTask = async ({
    name,
    status,
    assignee,
    assignees,
    isCompleted = false,
    stateSla = null,
    isAssignedRole = false,
    assignedRole = [],
  }) => {
    const entityType = "case-default";
    const filingNumber = caseDetails?.filingNumber;
    await DRISTIService.customApiService(Urls.dristi.pendingTask, {
      pendingTask: {
        name,
        entityType,
        referenceId: assignees ? `MANUAL_${filingNumber}` : `MANUAL_${filingNumber}_${assignee || userInfo?.uuid}`,
        status,
        assignedTo: assignees ? assignees : [{ uuid: assignee || userInfo?.uuid }],
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

  const closePendingTask = async ({ status }) => {
    const entityType = "case-default";
    const filingNumber = caseDetails?.filingNumber;
    await DRISTIService.customApiService(Urls.dristi.pendingTask, {
      pendingTask: {
        entityType,
        status,
        referenceId: `MANUAL_${filingNumber}`,
        cnrNumber: caseDetails?.cnrNumber,
        filingNumber: filingNumber,
        isCompleted: true,
        additionalDetails: {},
        tenantId,
      },
    });
  };

  const onFormValueChange = (setValue, formData, formState, reset, setError, clearErrors, trigger, getValues, index, currentDisplayIndex) => {
    checkIfscValidation({ formData, setValue, selected });
    checkNameValidation({ formData, setValue, selected, formdata, index, reset, clearErrors, formState });
    checkOnlyCharInCheque({ formData, setValue, selected });
    if (!isEqual(formData, formdata[index].data)) {
      chequeDateValidation({ formData, setError, clearErrors, selected });
      showDemandNoticeModal({
        setValue,
        formData,
        setError,
        clearErrors,
        index,
        caseDetails,
        selected,
        setServiceOfDemandNoticeModal,
      });
      checkDuplicateMobileEmailValidation({
        formData,
        setValue,
        selected,
        formdata,
        index,
        reset,
        setError,
        clearErrors,
        caseDetails,
        currentDisplayIndex,
      });
      validateDateForDelayApplication({
        formData,
        setValue,
        caseDetails,
        selected,
        toast,
        t,
        history,
        caseId,
        setShowConfirmDcaSkipModal,
        showConfirmDcaSkipModal,
        shouldShowConfirmDcaModal,
        setShouldShowConfirmDcaModal,
        prevIsDcaSkipped,
        setPrevIsDcaSkipped,
      });
      showToastForComplainant({ formData, setValue, selected, setSuccessToast, formState, clearErrors });
      setFormdata(
        formdata.map((item, i) => {
          return i === index
            ? {
                ...item,
                data: formData,
              }
            : item;
        })
      );
    }
    if (selected === "advocateDetails") {
      if (
        !formdata[index]?.data?.multipleAdvocatesAndPip &&
        advPageData[index]?.data?.multipleAdvocatesAndPip &&
        !isEqual(formdata[index].data, advPageData[index].data)
      ) {
        setValue("multipleAdvocatesAndPip", advPageData[index].data?.multipleAdvocatesAndPip);
      }
    }

    setFormErrors.current = setError;
    setFormState.current = formState;
    resetFormData.current = reset;
    setFormDataValue.current = setValue;
    clearFormDataErrors.current = clearErrors;

    if (Object.keys(formState?.errors).length) {
      setIsSubmitDisabled(true);
    } else {
      setIsSubmitDisabled(false);
    }
  };

  const handleAccordionClick = (index) => {
    setParentOpen((prevParentOpen) => (prevParentOpen === index ? -1 : index));
  };

  const setMandatoryAndOptionalRemainingFields = (currentPageData, currentSelected) => {
    let totalMandatoryLeft = 0;
    let totalOptionalLeft = 0;

    if (currentPageData?.length === 0) {
      // this case is specially for witness details page (which is optional),
      // so there might not be any witness at all.
      totalMandatoryLeft = 0;
      totalOptionalLeft = 1;
    } else {
      for (let i = 0; i < currentPageData?.length; i++) {
        const currentIndexData = currentPageData[i];
        const currentPageMandatoryFields = [];
        const currentPageOptionalFields = [];
        let currentPage = {};
        for (const obj of sideMenuConfig) {
          const foundPage = obj?.children.find((o) => o?.key === currentSelected);
          if (foundPage) {
            currentPage = foundPage;
            break;
          }
        }

        currentPageMandatoryFields.push(...(currentPage?.mandatoryFields || []));
        currentPageOptionalFields.push(...(currentPage?.optionalFields || []));

        const currentPageMandatoryDependentFields = (currentPage?.dependentMandatoryFields || [])
          .filter((obj) => {
            return currentIndexData?.data?.[obj?.dependentOn]?.[obj?.dependentOnKey] === true;
          })
          .map((obj) => {
            return obj?.field;
          });
        currentPageMandatoryFields.push(...currentPageMandatoryDependentFields);

        const currentPageOptionalDependentFields = (currentPage?.dependentOptionalFields || [])
          .filter((obj) => {
            return currentIndexData?.data?.[obj?.dependentOn]?.[obj?.dependentOnKey] === true;
          })
          .map((obj) => {
            return obj?.field;
          });
        currentPageOptionalFields.push(...currentPageOptionalDependentFields);

        if (currentPageMandatoryFields.length !== 0) {
          for (let i = 0; i < currentPageMandatoryFields.length; i++) {
            const value = extractValue(currentIndexData?.data, currentPageMandatoryFields[i]);
            const isValueEmpty = isEmptyValue(value);
            if (isValueEmpty) {
              totalMandatoryLeft++;
            }
          }
        }

        if ("ifMultipleAddressLocations" in currentPage) {
          const arrayValue = currentIndexData?.data[currentPage?.ifMultipleAddressLocations?.dataKey] || [];
          for (let i = 0; i < arrayValue.length; i++) {
            const mandatoryFields = currentPage?.ifMultipleAddressLocations?.mandatoryFields || [];
            for (let j = 0; j < mandatoryFields.length; j++) {
              const value = extractValue(arrayValue[i], mandatoryFields[j]);
              const isValueEmpty = isEmptyValue(value);
              if (isValueEmpty) {
                totalMandatoryLeft++;
              }
            }
          }
        }

        if ("anyOneOfTheseMandatoryFields" in currentPage) {
          const fieldsArray = currentPage.anyOneOfTheseMandatoryFields;
          for (let i = 0; i < fieldsArray.length; i++) {
            const currentChildArray = fieldsArray[i];
            let count = 0;
            for (let j = 0; j < currentChildArray.length; j++) {
              const value = extractValue(currentIndexData?.data, currentChildArray[j]);
              const isValueEmpty = isEmptyValue(value);
              if (isValueEmpty) {
                count++;
              }
            }
            if (count === 2) {
              totalMandatoryLeft++;
            }
          }
        }

        if (currentPageOptionalFields.length !== 0) {
          let optionalLeft = 0;
          for (let i = 0; i < currentPageOptionalFields.length; i++) {
            const value = extractValue(currentIndexData?.data, currentPageOptionalFields[i]);
            const isValueEmpty = isEmptyValue(value);
            if (isValueEmpty) {
              optionalLeft++;
            }
          }
          totalOptionalLeft += optionalLeft;
        }
      }
    }
    const obj = {
      selectedPage: currentSelected,
      mandatoryTotalCount: totalMandatoryLeft,
      optionalTotalCount: totalOptionalLeft,
    };
    return obj;
  };
  const onDocumentUpload = async (fileData, filename) => {
    try {
      const fileUploadRes = await Digit.UploadServices.Filestorage("DRISTI", fileData, Digit.ULBService.getCurrentTenantId());
      return { file: fileUploadRes?.data, fileType: fileData.type, filename };
    } catch (error) {
      console.error("Failed to upload document:", error);
      throw error; // or handle error appropriately
    }
  };

  const onSubmit = async (action, isCaseLocked = false) => {
    if (isDisableAllFieldsMode) {
      history.push(homepagePath);
    }
    if (!Array.isArray(formdata)) {
      return;
    }
    if (
      formdata
        .filter((data) => data.isenabled)
        .some((data) =>
          respondentValidation({
            setErrorMsg,
            t,
            formData: data?.data,
            caseDetails,
            selected,
            setShowErrorToast,
            toast,
            setFormErrors: setFormErrors.current,
            clearFormDataErrors: clearFormDataErrors.current,
          })
        )
    ) {
      return;
    }
    if (
      formdata
        .filter((data) => data.isenabled)
        .some((data) =>
          demandNoticeFileValidation({
            formData: data?.data,
            selected,
            setShowErrorToast,
            setFormErrors: setFormErrors.current,
          })
        )
    ) {
      return;
    }
    if (
      formdata
        .filter((data) => data.isenabled)
        .some((data) => chequeDetailFileValidation({ formData: data?.data, selected, setShowErrorToast, setFormErrors: setFormErrors.current }))
    ) {
      return;
    }
    if (selected === "advocateDetails") {
      const advocatesAndPipErrors = getAdvocatesAndPipRemainingFields(formdata, t);
      if (advocatesAndPipErrors?.length > 0) {
        setShowErrorDataModal({ page: "advocateDetails", show: true, errorData: advocatesAndPipErrors });
        return;
      }
    }
    if (
      formdata
        .filter((data) => data.isenabled)
        .some((data) =>
          complainantValidation({
            formData: data?.data,
            t,
            caseDetails,
            selected,
            setShowErrorToast,
            toast,
            setFormErrors: setFormErrors.current,
            formState: setFormState.current,
            clearFormDataErrors: clearFormDataErrors.current,
          })
        )
    ) {
      return;
    }
    if (
      formdata
        .filter((data) => data.isenabled)
        .some((data) =>
          debtLiabilityValidation({
            formData: data?.data,
            t,
            caseDetails,
            selected,
            setShowErrorToast,
            toast,
            setFormErrors: setFormErrors.current,
          })
        )
    ) {
      return;
    }
    if (
      formdata
        .filter((data) => data.isenabled)
        .some((data) =>
          delayApplicationValidation({
            formData: data?.data,
            t,
            caseDetails,
            selected,
            setShowErrorToast,
            toast,
            setFormErrors: setFormErrors.current,
          })
        )
    ) {
      return;
    }
    if (
      formdata
        .filter((data) => data.isenabled)
        .some((data) =>
          witnessDetailsValidation({
            formData: data?.data,
            t,
            caseDetails,
            selected,
            setShowErrorToast,
            toast,
            setFormErrors: setFormErrors.current,
            clearFormDataErrors: clearFormDataErrors.current,
          })
        )
    ) {
      return;
    }
    if (
      formdata
        .filter((data) => data.isenabled)
        .some((data) =>
          prayerAndSwornValidation({
            t,
            formData: data?.data,
            selected,
            setShowErrorToast,
            setErrorMsg,
            toast,
            setFormErrors: setFormErrors.current,
            clearFormDataErrors: clearFormDataErrors.current,
          })
        )
    ) {
      return;
    }
    if (
      formdata
        .filter((data) => data.isenabled)
        .some((data) =>
          signatureValidation({
            formData: data?.data,
            selected,
            setShowErrorToast,
            setErrorMsg,
            caseDetails: modifiedFormConfig?.[0].reduce((res, curr) => {
              if (curr?.body && Array.isArray(curr.body) && curr.body?.length === 0) return res;
              else res[curr?.body?.[0]?.key] = curr?.body?.[0]?.populators?.inputs?.[0]?.data;
              return res;
            }, {}),
          })
        )
    ) {
      return;
    }
    if (selected === "reviewCaseFile" && isCaseReAssigned && !openConfirmCorrectionModal && !isCaseLocked) {
      setOpenConfirmCorrectionModal(true);
      return;
    }

    if (selected === "reviewCaseFile" && !showCaseLockingModal && isDraftInProgress) {
      setShowCaseLockingModal(true);
      return;
    }

    //check- include below commented code for signing process changes.

    // if (selected === "addSignature" && (isPendingESign || isPendingReESign)) {
    //   if (courtRooms?.length === 1) {
    //     onSubmitCase({ court: courtRooms[0], action: CaseWorkflowAction.E_SIGN });
    //     return;
    //   } else {
    //     setOpenConfirmCourtModal(true);
    //   }
    // }
    else {
      let caseComplaintDocument = {};
      try {
        if (isCaseLocked) {
          setIsDisabled(true);
          const caseObject = isCaseReAssigned && errorCaseDetails ? errorCaseDetails : caseDetails;
          const response = await axios.post(
            "/dristi-case-pdf/v1/fetchCaseComplaintPdf",
            {
              cases: caseObject,
              RequestInfo: {
                authToken: Digit.UserService.getUser().access_token,
                userInfo: Digit.UserService.getUser()?.info,
                msgId: `${Date.now()}|${Digit.StoreData.getCurrentLanguage()}`,
                apiId: "Rainmaker",
              },
            },
            { responseType: "blob" } // Important: Set responseType to handle binary data
          );
          const contentDisposition = response.headers["content-disposition"];
          const filename = contentDisposition ? contentDisposition.split("filename=")[1]?.replace(/['"]/g, "") : "caseCompliantDetails.pdf";
          const pdfFile = new File([response?.data], filename, { type: "application/pdf" });
          let document = {};
          try {
            document = await onDocumentUpload(pdfFile, pdfFile?.name);
          } catch (error) {
            throw error;
          }
          const fileStoreId = document?.file?.files?.[0]?.fileStoreId;

          if (fileStoreId) {
            caseComplaintDocument = {
              documentType: "case.complaint.unsigned",
              fileStore: fileStoreId,
              fileName: filename,
            };
          } else {
            throw new Error("FILE_STORE_ID_MISSING");
          }
        }
        const newCaseDetails = {
          ...caseDetails,
          additionalDetails: {
            ...caseDetails.additionalDetails,
            modifiedCaseTitle: newCaseName || caseDetails?.additionalDetails?.modifiedCaseTitle,
          },
        };
        await updateCaseDetails({
          t,
          isCompleted: true,
          caseDetails: isCaseReAssigned && errorCaseDetails ? errorCaseDetails : newCaseDetails,
          prevCaseDetails: prevCaseDetails,
          formdata,
          pageConfig,
          selected,
          setIsDisabled,
          tenantId,
          setFormDataValue: setFormDataValue.current,
          action,
          setErrorCaseDetails,
          isCaseSignedState: isPendingESign || isPendingReESign,
          isSaveDraftEnabled: isCaseReAssigned || isPendingReESign || isPendingESign,
          ...(caseComplaintDocument && { caseComplaintDocument: caseComplaintDocument }),
          multiUploadList,
          scrutinyObj,
          filingType: filingType,
        });

        if (resetFormData.current) {
          resetFormData.current();
          setIsDisabled(false);
        }

        await refetchCaseData();
        const caseData =
          caseDetails?.additionalDetails?.[nextSelected]?.formdata ||
          caseDetails?.caseDetails?.[nextSelected]?.formdata ||
          (nextSelected === "witnessDetails" ? [{}] : [{ isenabled: true, data: {}, displayindex: 0 }]);

        setFormdata(caseData);
        setIsDisabled(false);
        setPrevSelected(selected);

        if (selected !== "reviewCaseFile") {
          history.push(`?caseId=${caseId}&selected=${nextSelected}`);
        }
      } catch (error) {
        let message = t("SOMETHING_WENT_WRONG");
        if (error instanceof DocumentUploadError) {
          message = `${t("DOCUMENT_FORMAT_DOES_NOT_MATCH")} : ${error?.documentType}`;
        } else if (extractCodeFromErrorMsg(error) === 413) {
          message = t("FAILED_TO_UPLOAD_FILE");
        }
        toast.error(message);
        setIsDisabled(false);
        console.error("An error occurred:", error);
        return { error };
      }
    }
  };

  const onSaveDraft = (props) => {
    setParmas({ ...params, [pageConfig.key]: formdata });
    const newCaseDetails = {
      ...caseDetails,
      additionalDetails: {
        ...caseDetails.additionalDetails,
        modifiedCaseTitle: newCaseName || caseDetails?.additionalDetails?.modifiedCaseTitle,
      },
    };
    updateCaseDetails({
      t,
      caseDetails: newCaseDetails,
      prevCaseDetails: prevCaseDetails,
      formdata,
      setFormDataValue: setFormDataValue.current,
      pageConfig,
      selected,
      setIsDisabled,
      tenantId,
      setErrorCaseDetails,
      multiUploadList,
      scrutinyObj,
      filingType: filingType,
    })
      .then(() => {
        refetchCaseData().then(() => {
          const caseData = caseDetails?.additionalDetails?.[nextSelected]?.formdata ||
            caseDetails?.caseDetails?.[nextSelected]?.formdata || [{ isenabled: true, data: {}, displayindex: 0 }];
          setFormdata(caseData);
          setIsDisabled(false);
        });
      })
      .then(() => {
        toast.success(t("CS_SUCCESSFULLY_SAVED_DRAFT"));
      })
      .catch(async (error) => {
        if (error instanceof DocumentUploadError) {
          toast.error(`${t("DOCUMENT_FORMAT_DOES_NOT_MATCH")} : ${error?.documentType}`);
        } else if (extractCodeFromErrorMsg(error) === 413) {
          toast.error(t("FAILED_TO_UPLOAD_FILE"));
        } else {
          console.error("Error:", error);
          toast.error(t("SOMETHING_WENT_WRONG"));
        }
        setIsDisabled(false);
      });
  };

  const onErrorCorrectionSubmit = async () => {
    setOpenConfirmCorrectionModal(false);
    // onSubmit(CaseWorkflowAction.EDIT_CASE);
    // await createPendingTask({ name: t("PENDING_E_SIGN_FOR_CASE"), status: "PENDING_E-SIGN" });
    setShowCaseLockingModal(true);
  };

  const handlePageChange = (key, isConfirm) => {
    if (key === selected) {
      return;
    }
    // if (!isConfirm) {
    //   setOpenConfigurationModal(key);
    //   return;
    // }
    setParmas({ ...params, [pageConfig.key]: formdata });
    setFormdata([{ isenabled: true, data: {}, displayindex: 0 }]);
    if (resetFormData.current) {
      resetFormData.current();
      setIsDisabled(false);
    }
    setIsOpen(false);
    const isDrafted =
      caseDetails?.additionalDetails?.[selected]?.isCompleted || caseDetails?.caseDetails?.[selected]?.isCompleted
        ? isMatch(
            JSON.parse(
              JSON.stringify(
                caseDetails?.additionalDetails?.[selected]?.formdata ||
                  caseDetails?.caseDetails?.[selected]?.formdata || [{ isenabled: true, data: {}, displayindex: 0 }]
              )
            ),
            JSON.parse(JSON.stringify(formdata.filter((data) => data.isenabled)))
          )
        : false;
    const newCaseDetails = {
      ...caseDetails,
      additionalDetails: {
        ...caseDetails.additionalDetails,
        modifiedCaseTitle: newCaseName || caseDetails?.additionalDetails?.modifiedCaseTitle,
      },
    };
    updateCaseDetails({
      t,
      isCompleted: isDrafted,
      caseDetails: isCaseReAssigned && errorCaseDetails ? errorCaseDetails : newCaseDetails,
      prevCaseDetails: prevCaseDetails,
      formdata,
      setFormDataValue: setFormDataValue.current,
      pageConfig,
      selected,
      setIsDisabled,
      tenantId,
      setErrorCaseDetails,
      isSaveDraftEnabled: isCaseReAssigned || isPendingReESign || isPendingESign,
      multiUploadList,
      scrutinyObj,
      filingType: filingType,
    })
      .then(() => {
        if (!isCaseReAssigned) {
          refetchCaseData().then(() => {
            const caseData =
              caseDetails?.additionalDetails?.[nextSelected]?.formdata ||
              caseDetails?.caseDetails?.[nextSelected]?.formdata ||
              (nextSelected === "witnessDetails" ? [{}] : [{ isenabled: true, data: {}, displayindex: 0 }]);
            setFormdata(caseData);
            setIsDisabled(false);
          });
        } else {
          setIsDisabled(false);
        }
      })
      .catch(async (error) => {
        if (error instanceof DocumentUploadError) {
          toast.error(`${t("DOCUMENT_FORMAT_DOES_NOT_MATCH")} : ${error?.documentType}`);
        } else if (extractCodeFromErrorMsg(error) === 413) {
          toast.error(t("FAILED_TO_UPLOAD_FILE"));
        } else {
          console.error("Error:", error);
          toast.error(t("SOMETHING_WENT_WRONG"));
        }
        setIsDisabled(false);
      });
    setPrevSelected(selected);
    if (!isFilingParty) {
      history.replace(`?caseId=${caseId}&selected=${key}`);
    } else {
      history.push(`?caseId=${caseId}&selected=${key}`);
    }
  };

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
              taxAmount: 4, // amount to be replaced with calculationResponse
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
  const onSubmitCase = async (data) => {
    setOpenConfirmCourtModal(false);
    setIsDisabled(true);
    let calculationResponse = {};
    const assignees = getAllAssignees(caseDetails);
    const fileStoreId = localStorage.getItem("fileStoreId");
    await DRISTIService.caseUpdateService(
      {
        cases: {
          ...caseDetails,
          ...(fileStoreId && {
            additionalDetails: {
              ...caseDetails?.additionalDetails,
              signedCaseDocument: fileStoreId,
            },
          }),
          caseTitle:
            `${getComplainantName(caseDetails?.additionalDetails?.complainantDetails?.formdata || {}, t)} vs ${getRespondentName(
              caseDetails?.additionalDetails?.respondentDetails?.formdata || {},
              t
            )}` || caseDetails?.caseTitle,
          courtId: "KLKM52" || data?.court?.code,
          workflow: {
            ...caseDetails?.workflow,
            action: data?.action || "E-SIGN",
            assignes: [],
          },
        },
        tenantId,
      },
      tenantId
    ).then(async (res) => {
      await closePendingTask({ status: "PENDING_PAYMENT" });
      if (res?.cases?.[0]?.status === "PENDING_PAYMENT") {
        await DRISTIService.customApiService(Urls.dristi.pendingTask, {
          pendingTask: {
            name: "Pending Payment",
            entityType: "case-default",
            referenceId: `MANUAL_${caseDetails?.filingNumber}`,
            status: "PENDING_PAYMENT",
            assignedTo: [...assignees?.map((uuid) => ({ uuid }))],
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
      }
      if (isPendingReESign) setCaseResubmitSuccess(true);
      setIsDisabled(false);
      return;
    });

    setPrevSelected(selected);
    if (isPendingESign) {
      history.push(`${path}/e-filing-payment?caseId=${caseId}`, { state: { calculationResponse: calculationResponse } });
    }
  };

  const getFormClassName = useCallback(() => {
    if (formdata && formdata?.[0]?.data?.advocateBarRegNumberWithName?.[0]?.isDisable) {
      return "disable-form";
    }
    return "";
  }, [formdata]);

  const handleConfirmDeleteForm = () => {
    const index = deleteFormIndex;
    const newArray = formdata.map((item, i) => ({
      ...item,
      isenabled: index === i ? false : item.isenabled,
      displayindex: i < index ? item.displayindex : i === index ? -Infinity : item.displayindex - 1,
    }));
    setConfirmDeleteModal(false);
    setFormdata(newArray);
  };

  const actionName = useMemo(
    () =>
      selected === "reviewCaseFile"
        ? isPendingESign
          ? ""
          : isCaseReAssigned
          ? t("CS_COMMONS_NEXT")
          : isDraftInProgress
          ? t("CS_CONFIRM_DETAILS")
          : isPendingReESign
          ? t("CS_COMMON_CONTINUE")
          : t("CS_GO_TO_HOME")
        : selected === "addSignature"
        ? isPendingESign || isPendingReESign
          ? t("CS_SUBMIT_CASE")
          : t("CS_COMMON_CONTINUE")
        : isDisableAllFieldsMode
        ? t("CS_GO_TO_HOME")
        : isCaseReAssigned
        ? t("CS_COMMONS_NEXT")
        : isPendingESign
        ? ""
        : t("CS_COMMON_CONTINUE"),
    [isCaseReAssigned, isDisableAllFieldsMode, isPendingESign, selected, t, isDraftInProgress, isPendingReESign]
  );

  // show action bar only after all mandatory details are filed
  const showActionsLabels = useMemo(() => {
    return !isFilingParty ? !mandatoryFieldsLeftTotalCount && !isDisableAllFieldsMode : true;
  }, [isFilingParty, mandatoryFieldsLeftTotalCount, isDisableAllFieldsMode]);

  const [isOpen, setIsOpen] = useState(false);
  if (isLoading || isGetAllCasesLoading || isCourtIdsLoading || isLoader || isIndividualLoading || isFilingTypeLoading) {
    return <Loader />;
  }

  const caseType = {
    cateogry: "Criminal",
    act: "Negotiable Instruments Act",
    section: "138",
    courtName: "Kollam S-138 Special Court",
    href: "https://www.indiacode.nic.in/bitstream/123456789/2189/1/a1881-26.pdf",
  };

  const takeUserToRemainingMandatoryFieldsPage = () => {
    const firstPageInTheListWhichHasMandatoryFieldsLeft = checkAndGetMandatoryFieldLeftPages?.[0];
    const selectedPage = firstPageInTheListWhichHasMandatoryFieldsLeft?.selectedPage;
    setPrevSelected(selected);
    history.push(`?caseId=${caseId}&selected=${selectedPage}`);
    setShowConfirmMandatoryModal(false);
  };

  const takeUserToRemainingOptionalFieldsPage = () => {
    const firstPageInTheListWhichHasOptionalFieldsLeft = checkAndGetOptionalFieldLeftPages?.[0];
    const selectedPage = firstPageInTheListWhichHasOptionalFieldsLeft?.selectedPage;
    setPrevSelected(selected);
    history.push(`?caseId=${caseId}&selected=${selectedPage}`);
    setShowConfirmOptionalModal(false);
  };

  const handleGoToPage = (key) => {
    if (!isFilingParty) {
      history.replace(`?caseId=${caseId}&selected=${AccordionTabs.REVIEW_CASE_FILE}`);
    } else {
      history.push(`?caseId=${caseId}&selected=${key}`);
    }
  };
  const handleGoToHome = () => {
    history.push(homepagePath);
  };

  if (isDisableAllFieldsMode && selected !== "reviewCaseFile" && caseDetails) {
    setPrevSelected(selected);
    history.push(`?caseId=${caseId}&selected=reviewCaseFile`);
  }

  if (isCaseReAssigned && !errorPages.some((item) => item.key === selected) && selected !== "reviewCaseFile" && selected !== "addSignature") {
    if (!judgeObj) {
      history.push(`?caseId=${caseId}&selected=${nextSelected}`);
    }
  }

  const handleDownload = async (blob) => {
    const url = window.URL.createObjectURL(blob);
    const link = document.createElement("a");
    link.href = url;
    link.setAttribute("download", `${caseDetails?.filingNumber || "CasePdf"}.pdf`);
    document.body.appendChild(link);
    link.click();
    link.parentNode.removeChild(link);
    window.URL.revokeObjectURL(url);
  };

  const handleViewCasePdf = async () => {
    setIsLoader(true);
    try {
      const caseObject = isCaseReAssigned && errorCaseDetails ? errorCaseDetails : caseDetails;
      const response = await axios.post(
        "/dristi-case-pdf/v1/generateCasePdf",
        {
          cases: caseObject,
          RequestInfo: {
            authToken: Digit.UserService.getUser().access_token,
            userInfo: Digit.UserService.getUser()?.info,
            msgId: `${Date.now()}|${Digit.StoreData.getCurrentLanguage()}`,
            apiId: "Rainmaker",
          },
        },
        { responseType: "blob" } // Important: Set responseType to handle binary data
      );
      setPdfDetails(response?.data);
      setIsModalOpen(true);
    } catch (error) {
      console.error("Error generating case PDF:", error);
      toast.error(t("CASE_PDF_GENERATION_ERROR"));
    } finally {
      setIsLoader(false);
    }
  };

  const customStyles = `
  .action-bar-wrap.e-filing-action-bar header {
    margin-top:0 !important;
  }
`;

  return (
    <div className="file-case">
      <style>{customStyles}</style>
      <div className="file-case-side-stepper">
        {isDraftInProgress && (
          <div className="side-stepper-info">
            <div className="header">
              <InfoIcon />
              <span>
                <b>{t("CS_YOU_ARE_FILING_A_CASE")}</b>
              </span>
            </div>
            <p>
              {t("CS_UNDER")}{" "}
              <a href={caseType?.href} target="_blank" rel="noreferrer" className="act-name">{`S-${caseType.section}, ${caseType.act}`}</a>{" "}
              {t("CS_IN")}
              <span className="place-name">{` ${caseType.courtName}.`}</span>
            </p>
          </div>
        )}
        {isCaseReAssigned && (
          <div className="side-stepper-error-count">
            {judgeObj ? (
              <FlagBox t={t} judgeObj={judgeObj} />
            ) : (
              <ErrorsAccordion
                t={t}
                totalErrorCount={totalErrors.total}
                pages={errorPages}
                handlePageChange={handlePageChange}
                showConfirmModal={confirmModalConfig ? true : false}
                handleGoToPage={handleGoToPage}
                selected={selected}
              />
            )}
            <div className="total-error-note">
              <div className="header">
                <InfoIcon />
                <span>
                  <b>{t("CS_COMMON_NOTE")}</b>
                </span>
              </div>
              <p>{t("CS_FSO_ERROR_NOTE")}</p>
            </div>
          </div>
        )}
        {isOpen && (
          <Modal
            headerBarEnd={
              <CloseBtn
                onClick={() => {
                  setIsOpen(false);
                }}
              />
            }
            hideSubmit={true}
            className={"case-types"}
          >
            <div style={{ padding: "8px 16px" }}>
              {accordion.map((item, index) => (
                <Accordion
                  t={t}
                  title={item.title}
                  handlePageChange={handlePageChange}
                  handleAccordionClick={() => {
                    handleAccordionClick(isFilingParty ? index : accordion.length - 1);
                  }}
                  key={index}
                  children={item.children}
                  parentIndex={index}
                  isOpen={item.isOpen}
                  errorCount={scrutinyErrors?.[item.key]?.total || 0}
                  isCaseReAssigned={isCaseReAssigned}
                  isDraftInProgress={isDraftInProgress}
                  isFilingParty={isFilingParty}
                  AccordionTabs={AccordionTabs}
                />
              ))}
            </div>
          </Modal>
        )}

        <div className="file-case-select-form-section">
          {accordion.map((item, index) => (
            <Accordion
              t={t}
              title={item.title}
              handlePageChange={handlePageChange}
              handleAccordionClick={() => {
                handleAccordionClick(isFilingParty ? index : accordion.length - 1);
              }}
              key={index}
              children={item.children}
              parentIndex={index}
              isOpen={item.isOpen}
              showConfirmModal={confirmModalConfig ? true : false}
              errorCount={scrutinyErrors?.[item.key]?.total || 0}
              isCaseReAssigned={isCaseReAssigned}
              isDraftInProgress={isDraftInProgress}
              isFilingParty={isFilingParty}
              AccordionTabs={AccordionTabs}
            />
          ))}
        </div>
      </div>

      <div className="file-case-form-section">
        <div className="employee-card-wrapper">
          <div className="header-content">
            {selected === "reviewCaseFile" && (
              <div style={{ display: "flex", justifyContent: "flex-end" }}>
                <Button className="border-none dristi-font-bold" onButtonClick={handleViewCasePdf} label={t("CS_VIEW_PDF")} variation={"secondary"} />
              </div>
            )}
            <div className="header-details">
              <div
                className="header-title-icon"
                style={{
                  display: "flex",
                  flexDirection: selected === "reviewCaseFile" ? "column" : "row",
                  gap: "10px",
                  alignItems: "center",
                  justifyContent: "space-between",
                }}
              >
                <div style={{ display: "flex", alignItems: "center", gap: "10px", flexWrap: "wrap" }}>
                  <Header>
                    {t(pageConfig.header)}
                    {pageConfig?.showOptionalInHeader && <span style={{ color: "#77787B", fontWeight: 100 }}>&nbsp;(optional)</span>}
                    {selected === "reviewCaseFile" && (
                      <React.Fragment>
                        <span>: {newCaseName?.trim() ? newCaseName : caseDetails?.caseTitle}</span>
                      </React.Fragment>
                    )}
                  </Header>
                  {selected === "reviewCaseFile" && !isCaseReAssigned && isFilingParty && (
                    <div className="case-edit-icon" onClick={() => setShowEditCaseNameModal(true)} style={{ cursor: "pointer" }}>
                      <span style={{ position: "relative" }} data-tip data-for="Click">
                        <EditIcon style={{ display: "block", position: "relative" }} />
                      </span>
                      <ReactTooltip id="Click" place="bottom" content={t("CS_CLICK_TO_EDIT") || ""}>
                        {t("CS_CLICK_TO_EDIT")}
                      </ReactTooltip>
                    </div>
                  )}
                </div>
                <div className="header-icon" onClick={() => setIsOpen(true)}>
                  <CustomArrowDownIcon />
                </div>
              </div>
            </div>

            <p>{t(pageConfig.subtext || "")}</p>
          </div>
          {isCaseReAssigned && selected === "reviewCaseFile" && (
            <SelectCustomNote
              t={t}
              config={{
                populators: {
                  inputs: [
                    {
                      infoHeader: "Note",
                      infoText: `${t("CS_YOU_MADE")} ${totalErrors?.total} ${t("CS_REVIEW_CAREFULLY")}`,
                      type: "InfoComponent",
                    },
                  ],
                },
              }}
            />
          )}
          {sectionWiseErrors?.[selected] && <ScrutinyInfo t={t} config={{ populators: { scrutinyMessage: sectionWiseErrors?.[selected] } }} />}
          {modifiedFormConfig.map((config, index) => {
            return formdata[index].isenabled ? (
              <div key={`${selected}-${index}`} className="form-wrapper-d">
                {pageConfig?.addFormText && (
                  <div className="form-item-name">
                    <h1>{`${t(pageConfig?.formItemName)} ${formdata[index]?.displayindex + 1}`}</h1>
                    {(activeForms > 1 || t(pageConfig?.formItemName) === "Witness" || pageConfig?.isOptional) &&
                      isDraftInProgress &&
                      // check- TODO: instedd of comparison from mobile number, compare with individualId
                      completedComplainants?.[index]?.data?.complainantVerification?.mobileNumber !== userInfo?.mobileNumber && (
                        <span
                          style={{ cursor: "pointer" }}
                          onClick={() => {
                            setConfirmDeleteModal(true);
                            setDeleteFormIndex(index);
                          }}
                        >
                          <CustomDeleteIcon />
                        </span>
                      )}
                  </div>
                )}
                <FormComposerV2
                  label={showActionsLabels && actionName}
                  config={config}
                  onSubmit={() => onSubmit("SAVE_DRAFT")}
                  onSecondayActionClick={onSaveDraft}
                  defaultValues={getDefaultValues(index)}
                  onFormValueChange={(setValue, formData, formState, reset, setError, clearErrors, trigger, getValues) => {
                    onFormValueChange(
                      setValue,
                      formData,
                      formState,
                      reset,
                      setError,
                      clearErrors,
                      trigger,
                      getValues,
                      index,
                      formdata[index].displayindex
                    );
                  }}
                  isDisabled={isSubmitDisabled}
                  cardStyle={{ minWidth: "100%" }}
                  cardClassName={`e-filing-card-form-style ${pageConfig.className}`}
                  secondaryLabel={t("CS_SAVE_DRAFT")}
                  showSecondaryLabel={isDraftInProgress}
                  actionClassName="e-filing-action-bar"
                  className={`${pageConfig.className} ${getFormClassName()}`}
                  noBreakLine
                  submitIcon={<RightArrow />}
                />
              </div>
            ) : null;
          })}
          {confirmDeleteModal && (
            <Modal
              headerBarMain={<Heading label={t("Are you sure?")} />}
              headerBarEnd={<CloseBtn onClick={() => setConfirmDeleteModal(false)} />}
              actionCancelLabel="Cancel"
              actionCancelOnSubmit={() => setConfirmDeleteModal(false)}
              actionSaveLabel={`Remove ${t(pageConfig?.formItemName)}`}
              children={deleteWarningText(`${t(pageConfig?.formItemName).toLowerCase()}`)}
              actionSaveOnSubmit={handleConfirmDeleteForm}
              className={"confirm-delete-modal"}
            ></Modal>
          )}
          {serviceOfDemandNoticeModal && (
            <Modal
              headerBarMain={<Heading label={t("CS_IMPORTANT_NOTICE")} />}
              headerBarEnd={
                <CloseBtn
                  onClick={() => {
                    setFormDataValue.current?.("dateOfService", "");
                    clearFormDataErrors.current?.("dateOfService");
                    setServiceOfDemandNoticeModal(false);
                  }}
                />
              }
              actionCancelOnSubmit={() => {
                setFormDataValue.current?.("dateOfService", "");
                clearFormDataErrors.current?.("dateOfService");
                setServiceOfDemandNoticeModal(false);
              }}
              actionSaveLabel={t("CS_SAVE_DRAFT")}
              children={<div style={{ padding: "16px 0" }}>{t("CS_SAVE_AS_DRAFT_TEXT")}</div>}
              actionSaveOnSubmit={async () => {
                await DRISTIService.caseUpdateService(
                  {
                    cases: {
                      ...caseDetails,
                      litigants: !caseDetails?.litigants ? [] : caseDetails?.litigants,
                      workflow: {
                        ...caseDetails?.workflow,
                        action: "SAVE_DRAFT",
                      },
                    },
                    tenantId,
                  },
                  tenantId
                );
                history.push(`/${window?.contextPath}/citizen/dristi/home`);
              }}
            ></Modal>
          )}
          {/* show this modal only for filingParty */}
          {isFilingParty && showMandatoryFieldsRemainingModal && showConfirmMandatoryModal && (
            <Modal
              headerBarMain={<Heading label={`${mandatoryFieldsLeftTotalCount} ${t("MANDATORY_FIELDS_REMAINING")}`} />}
              headerBarEnd={<CloseBtn onClick={() => takeUserToRemainingMandatoryFieldsPage()} />}
              actionSaveLabel={t("CONTINUE_FILLING")}
              children={mandatoryFieldsRemainingText()}
              actionSaveOnSubmit={() => takeUserToRemainingMandatoryFieldsPage()}
            ></Modal>
          )}
          {showOptionalFieldsRemainingModal && showConfirmOptionalModal && !mandatoryFieldsLeftTotalCount && !isDisableAllFieldsMode && (
            <Modal
              headerBarMain={<Heading label={t("TIPS_FOR_STRONGER_CASE")} />}
              headerBarEnd={<CloseBtn onClick={() => setShowConfirmOptionalModal(false)} />}
              actionCancelLabel={t("SKIP_AND_CONTINUE")}
              actionCancelOnSubmit={handleSkip}
              actionSaveLabel={isFilingParty && t("FILL_NOW")}
              children={optionalFieldsRemainingText(optionalFieldsLeftTotalCount)}
              actionSaveOnSubmit={() => takeUserToRemainingOptionalFieldsPage()}
            ></Modal>
          )}
          {showReviewCorrectionModal && isDraftInProgress && (
            <Modal
              headerBarMain={<Heading label={t("REVIEW_CASE_HEADER")} />}
              headerBarEnd={
                <CloseBtn
                  onClick={() => {
                    setPrevSelected(selected);
                    history.push(`?caseId=${caseId}&selected=${prevSelected}`);
                    setShowReviewCorrectionModal(false);
                  }}
                />
              }
              actionSaveLabel={t("REVIEW_CASE")}
              children={<div style={{ margin: "16px 0px" }}>{t("PLEASE_REVIEW_CASE_TEXT")}</div>}
              actionSaveOnSubmit={() => {
                setPrevSelected(selected);
                history.push(`?caseId=${caseId}&selected=reviewCaseFile`);
                setShowReviewCorrectionModal(false);
              }}
            ></Modal>
          )}
          {pageConfig?.addFormText && (
            <Button
              variation="secondary"
              onButtonClick={handleAddForm}
              className="add-new-form"
              icon={<CustomAddIcon />}
              label={t(pageConfig.addFormText)}
              isDisabled={!isDraftInProgress}
            ></Button>
          )}
          {openConfigurationModal && (
            <EditFieldsModal
              t={t}
              config={confirmModalConfig}
              setOpenConfigurationModal={setOpenConfigurationModal}
              selected={openConfigurationModal}
              handlePageChange={handlePageChange}
            />
          )}
          {showErrorToast && (
            <Toast
              error={true}
              label={t(errorMsg ? errorMsg : "ES_COMMON_PLEASE_ENTER_ALL_MANDATORY_FIELDS")}
              isDleteBtn={true}
              onClose={closeToast}
            />
          )}
          {showSuccessToast && <Toast label={t(successMsg)} isDleteBtn={true} onClose={closeToast} />}
        </div>
      </div>
      {openConfirmCourtModal && <ConfirmCourtModal setOpenConfirmCourtModal={setOpenConfirmCourtModal} t={t} onSubmitCase={onSubmitCase} />}
      {openConfirmCorrectionModal && (
        <ConfirmCorrectionModal onCorrectionCancel={() => setOpenConfirmCorrectionModal(false)} onSubmit={onErrorCorrectionSubmit} />
      )}
      {caseResubmitSuccess && (
        <CorrectionsSubmitModal
          t={t}
          filingNumber={caseDetails?.filingNumber}
          handleGoToHome={handleGoToHome}
          downloadPdf={downloadPdf}
          tenantId={tenantId}
          caseDetails={caseDetails}
        />
      )}
      {selected === "witnessDetails" && !isPendingESign && Object.keys(formdata.filter((data) => data.isenabled)?.[0] || {}).length === 0 && (
        <ActionBar className={"e-filing-action-bar"}>
          <SubmitBar
            label={t(isCaseReAssigned ? "CS_COMMONS_NEXT" : "CS_COMMON_CONTINUE")}
            submit="submit"
            disabled={isDisabled}
            submitIcon={<RightArrow />}
            onSubmit={() => onSubmit("SAVE_DRAFT")}
          />
          {!(isCaseReAssigned || isPendingReESign) && (
            <Button className="previous-button" variation="secondary" label={t("CS_SAVE_DRAFT")} onButtonClick={onSaveDraft} />
          )}
        </ActionBar>
      )}

      {isDisabled && (
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
      {isModalOpen && (
        <Modal
          headerBarEnd={<CloseBtn onClick={() => setIsModalOpen(false)} />}
          actionSaveLabel={t("DOWNLOAD_PDF")}
          actionSaveOnSubmit={() => handleDownload(pdfDetails)}
          actionCancelLabel={t("DOWNLOAD_CS_BACK")}
          actionCancelOnSubmit={() => setIsModalOpen(false)}
          formId="modal-action"
          headerBarMain={<Heading label={t("REVIEW_YOUR_COMPLAINT")} />}
          className={"review-order-modal"}
          style={{
            border: "1px solid #007E7E",
            backgroundColor: "white",
            fontFamily: "Roboto",
            fontSize: "16px",
            fontWeight: 700,
            lineHeight: "18.75px",
            textAlign: "center",
            width: "190px",
          }}
          textStyle={{ margin: "0px", color: "#007E7E" }}
          popupStyles={{ maxWidth: "60%" }}
          popUpStyleMain={{ zIndex: "1000" }}
          isDisabled={isDisabled}
        >
          {<DocViewerWrapper docWidth={"calc(93vw* 62/ 100)"} docHeight={"60vh"} selectedDocs={[pdfDetails]} showDownloadOption={false} />}
        </Modal>
      )}

      {showCaseLockingModal && (
        <CaseLockModal
          t={t}
          path={path}
          setShowCaseLockingModal={setShowCaseLockingModal}
          setShowConfirmCaseDetailsModal={setShowConfirmCaseDetailsModal}
          isAdvocateFilingCase={isAdvocateFilingCase}
          onSubmit={onSubmit}
          createPendingTask={createPendingTask}
          setPrevSelected={setPrevSelected}
          selected={selected}
          caseDetails={caseDetails}
          state={state}
        ></CaseLockModal>
      )}
      {showConfirmCaseDetailsModal && (
        <ConfirmCaseDetailsModal t={t} setShowConfirmCaseDetailsModal={setShowConfirmCaseDetailsModal}></ConfirmCaseDetailsModal>
      )}
      {showErrorDataModal?.page === selected && showErrorDataModal?.show === true && (
        <ErrorDataModal
          t={t}
          setIsSubmitDisabled={setIsSubmitDisabled}
          showErrorDataModal={showErrorDataModal}
          setShowErrorDataModal={setShowErrorDataModal}
        ></ErrorDataModal>
      )}
      {showConfirmDcaSkipModal && selected === "delayApplications" && (
        // This modal asks to confirm if the user wants to skip submitting Delay condonation Application.
        <ConfirmDcaSkipModal
          t={t}
          setFormDataValue={setFormDataValue.current}
          setShowConfirmDcaSkipModal={setShowConfirmDcaSkipModal}
          prevIsDcaSkipped={prevIsDcaSkipped}
          setPrevIsDcaSkipped={setPrevIsDcaSkipped}
        ></ConfirmDcaSkipModal>
      )}
      {showEditCaseNameModal && (
        <Modal
          headerBarEnd={
            <CloseBtn
              onClick={() => {
                setShowEditCaseNameModal(false);
              }}
            />
          }
          actionCancelOnSubmit={() => setShowEditCaseNameModal(false)}
          actionSaveLabel={t("CS_COMMON_CONFIRM")}
          actionSaveOnSubmit={() => {
            setNewCaseName(modalCaseName);
            setShowEditCaseNameModal(false);
          }}
          formId="modal-action"
          headerBarMain={<Heading label={t("CS_CHANGE_CASE_NAME")} />}
          className="edit-case-name-modal"
        >
          <h3 className="input-label">{t("CS_CASE_NAME")}</h3>
          <TextInput defaultValue={newCaseName || caseDetails?.caseTitle} type="text" onChange={(e) => setModalCaseName(e.target.value)} />
        </Modal>
      )}
    </div>
  );
}

export default EFilingCases;
