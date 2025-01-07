import { Button as ActionButton } from "@egovernments/digit-ui-components";
import { ActionBar, SubmitBar, Button, Header, InboxSearchComposer, Loader, Menu, Toast, CloseSvg } from "@egovernments/digit-ui-react-components";
import React, { useEffect, useMemo, useState } from "react";
import { useTranslation } from "react-i18next";
import { useHistory, useRouteMatch } from "react-router-dom";
import useSearchCaseService from "../../../hooks/dristi/useSearchCaseService";
import { CustomThreeDots, InfoIconRed } from "../../../icons/svgIndex";
import { CaseWorkflowState } from "../../../Utils/caseWorkflow";
import ViewCaseFile from "../scrutiny/ViewCaseFile";
import { TabSearchconfig } from "./AdmittedCasesConfig";
import CaseOverview from "./CaseOverview";
import EvidenceModal from "./EvidenceModal";
import ExtraComponent from "./ExtraComponent";
import "./tabs.css";
import { SubmissionWorkflowState } from "../../../Utils/submissionWorkflow";
import { OrderTypes, OrderWorkflowState } from "../../../Utils/orderWorkflow";
import ScheduleHearing from "./ScheduleHearing";
import ViewAllOrderDrafts from "./ViewAllOrderDrafts";
import PublishedOrderModal from "./PublishedOrderModal";
import ViewAllSubmissions from "./ViewAllSubmissions";
import { getAdvocates } from "../../citizen/FileCase/EfilingValidationUtils";
import useDownloadCasePdf from "../../../hooks/dristi/useDownloadCasePdf";
import HearingTranscriptModal from "./HearingTranscriptModal";
import AdmissionActionModal from "../admission/AdmissionActionModal";
import { DRISTIService } from "../../../services";
import { Urls } from "../../../hooks";
import {
  admitCaseSubmitConfig,
  scheduleCaseAdmissionConfig,
  scheduleCaseSubmitConfig,
  selectParticipantConfig,
  sendBackCase,
} from "../../citizen/FileCase/Config/admissionActionConfig";
import Modal from "../../../components/Modal";
import CustomCaseInfoDiv from "../../../components/CustomCaseInfoDiv";
import { getDate, removeInvalidNameParts } from "../../../Utils";
import useWorkflowDetails from "../../../hooks/dristi/useWorkflowDetails";
import useSearchOrdersService from "@egovernments/digit-ui-module-orders/src/hooks/orders/useSearchOrdersService";
import VoidSubmissionBody from "./VoidSubmissionBody";
import DocumentModal from "@egovernments/digit-ui-module-orders/src/components/DocumentModal";

const defaultSearchValues = {};

const stateSla = {
  SCHEDULE_HEARING: 3 * 24 * 3600 * 1000,
  NOTICE: 3 * 24 * 3600 * 1000,
};

const delayCondonationStylsMain = {
  padding: "6px 8px",
  borderRadius: "999px",
  backgroundColor: "#E9A7AA",
};

const delayCondonationTextStyle = {
  margin: "0px",
  fontFamily: "Roboto",
  fontSize: "14px",
  fontWeight: 400,
  lineHeight: "16.41px",
  color: "#231F20",
};

const casePrimaryActions = [
  { action: "REGISTER", label: "CS_REGISTER" },
  { action: "ADMIT", label: "CS_ADMIT_CASE" },
  { action: "SCHEDULE_ADMISSION_HEARING", label: "CS_SCHEDULE_ADMISSION_HEARING" },
  { action: "ISSUE_ORDER", label: "ISSUE_NOTICE" },
];
const caseSecondaryActions = [
  { action: "SEND_BACK", label: "SEND_BACK_FOR_CORRECTION" },
  { action: "REJECT", label: "CS_CASE_REJECT" },
];
const caseTertiaryActions = [];

const HearingWorkflowState = {
  OPTOUT: "OPT_OUT",
  INPROGRESS: "IN_PROGRESS",
  COMPLETED: "COMPLETED",
  ABATED: "ABATED",
  SCHEDULED: "SCHEDULED",
};

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

const relevantStatuses = [
  "CASE_ADMITTED",
  "ADMISSION_HEARING_SCHEDULED",
  "PENDING_ADMISSION_HEARING",
  "PENDING_NOTICE",
  "PENDING_RESPONSE",
  "PENDING_ADMISSION",
];
const judgeReviewStages = [
  "CASE_ADMITTED",
  "ADMISSION_HEARING_SCHEDULED",
  "PENDING_ADMISSION_HEARING",
  "PENDING_NOTICE",
  "PENDING_RESPONSE",
  "PENDING_ADMISSION",
  "CASE_DISMISSED",
];

const styles = {
  container: {
    display: "flex",
    gap: "8px",
    padding: "8px",
    borderRadius: "4px",
    backgroundColor: "#FCE8E8",
  },
  icon: {
    height: "20px",
    width: "20px",
  },
  text: {
    fontFamily: "Roboto",
    fontSize: "16px",
    fontWeight: "400",
    lineHeight: "18.75px",
    textAlign: "left",
    color: "#0a0a0a",
    margin: "0px",
  },
  link: {
    textDecoration: "underline",
    color: "#007e7e",
    cursor: "pointer",
  },
};

const formatDate = (date) => {
  if (date instanceof Date && !isNaN(date)) {
    const day = String(date.getDate()).padStart(2, "0");
    const month = String(date.getMonth() + 1).padStart(2, "0");
    const year = date.getFullYear();
    return `${day}-${month}-${year}`;
  }
  return "";
};

const AdmittedCases = () => {
  const { t } = useTranslation();
  const { path } = useRouteMatch();
  const urlParams = new URLSearchParams(window.location.search);
  const { hearingId, taskOrderType } = Digit.Hooks.useQueryParams();
  const caseId = urlParams.get("caseId");
  const roles = Digit.UserService.getUser()?.info?.roles;
  const isFSO = roles.some((role) => role.code === "FSO_ROLE");
  const isCourtRoomManager = roles.some((role) => role.code === "COURT_ROOM_MANAGER");
  const activeTab = isFSO ? "Complaints" : urlParams.get("tab") || "Overview";
  const filingNumber = urlParams.get("filingNumber");
  const userRoles = Digit.UserService.getUser()?.info?.roles.map((role) => role.code);
  const tenantId = window?.Digit.ULBService.getCurrentTenantId();

  const [show, setShow] = useState(false);
  const [openAdmitCaseModal, setOpenAdmitCaseModal] = useState(true);
  const [documentSubmission, setDocumentSubmission] = useState();
  const [showOrderReviewModal, setShowOrderReviewModal] = useState(false);
  const [showHearingTranscriptModal, setShowHearingTranscriptModal] = useState(false);
  const [currentOrder, setCurrentOrder] = useState();
  const [currentHearing, setCurrentHearing] = useState();
  const [showMenu, setShowMenu] = useState(false);
  const [toast, setToast] = useState(false);
  const [orderDraftModal, setOrderDraftModal] = useState(false);
  const [submissionsViewModal, setSubmissionsViewModal] = useState(false);
  const [draftOrderList, setDraftOrderList] = useState([]);
  const [submissionsViewList, setSubmissionsViewList] = useState([]);
  const [showModal, setShowModal] = useState(false);
  const [modalInfo, setModalInfo] = useState(null);
  const [submitModalInfo, setSubmitModalInfo] = useState(null);
  const [caseAdmitLoader, setCaseAdmitLoader] = useState(false);
  const [createAdmissionOrder, setCreateAdmissionOrder] = useState(false);
  const [updatedCaseDetails, setUpdatedCaseDetails] = useState({});
  const [showDismissCaseConfirmation, setShowDismissCaseConfirmation] = useState(false);
  const [showPendingDelayApplication, setShowPendingDelayApplication] = useState(false);
  const [toastStatus, setToastStatus] = useState({ alreadyShown: false });
  const [showVoidModal, setShowVoidModal] = useState(false);
  const [downloadCasePdfLoading, setDownloadCasePdfLoading] = useState(false);
  const [voidReason, setVoidReason] = useState("");
  const [isDelayApplicationPending, setIsDelayApplicationPending] = useState(false);
  const [isOpenDCA, setIsOpenDCA] = useState(false);
  const [isOpenFromPendingTask, setIsOpenFromPendingTask] = useState(false);

  const history = useHistory();
  const isCitizen = userRoles.includes("CITIZEN");
  const OrderWorkflowAction = Digit.ComponentRegistryService.getComponent("OrderWorkflowActionEnum") || {};
  const ordersService = Digit.ComponentRegistryService.getComponent("OrdersService") || {};
  const OrderReviewModal = Digit.ComponentRegistryService.getComponent("OrderReviewModal") || {};
  const SummonsAndWarrantsModal = Digit.ComponentRegistryService.getComponent("SummonsAndWarrantsModal") || <React.Fragment></React.Fragment>;
  const userInfo = Digit.UserService.getUser()?.info;
  const userType = useMemo(() => (userInfo?.type === "CITIZEN" ? "citizen" : "employee"), [userInfo?.type]);
  const isJudge = userInfo?.roles?.some((role) => role.code === "JUDGE_ROLE");
  const todayDate = new Date().getTime();
  const { downloadPdf } = useDownloadCasePdf();

  const reqEvidenceUpdate = {
    url: Urls.dristi.evidenceUpdate,
    params: {},
    body: {},
    config: {
      enable: false,
    },
  };

  const evidenceUpdateMutation = Digit.Hooks.useCustomAPIMutationHook(reqEvidenceUpdate);

  const { data: caseData, isLoading, refetch: refetchCaseData, isFetching: isCaseFetching } = useSearchCaseService(
    {
      criteria: [
        {
          caseId: caseId,
        },
      ],
      tenantId,
    },
    {},
    `dristi-${caseId}`,
    caseId,
    Boolean(caseId)
  );
  const caseDetails = useMemo(() => caseData?.criteria?.[0]?.responseList?.[0] || {}, [caseData]);
  const delayCondonationData = useMemo(() => caseDetails?.caseDetails?.delayApplications?.formdata?.[0]?.data, [caseDetails]);

  const cnrNumber = useMemo(() => caseDetails?.cnrNumber || "", [caseDetails]);

  const showTakeAction = useMemo(() => userRoles.includes("ORDER_CREATOR") && !isCitizen && relevantStatuses.includes(caseDetails?.status), [
    caseDetails?.status,
    userRoles,
    isCitizen,
  ]);

  const {
    isLoading: isWorkFlowLoading,
    data: workFlowDetails,
    revalidate: revalidateWorkflow = () => {},
    isFetching: isWorkFlowFetching,
  } = useWorkflowDetails({
    tenantId,
    id: caseDetails?.filingNumber,
    moduleCode: "case-default",
    config: {
      enabled: Boolean(caseDetails?.filingNumber && tenantId),
    },
  });

  const nextActions = useMemo(() => workFlowDetails?.nextActions || [{}], [workFlowDetails]);

  const primaryAction = useMemo(() => {
    return casePrimaryActions?.find((action) => nextActions?.some((data) => data.action === action?.action)) || { action: "", label: "" };
  }, [nextActions]);

  const secondaryAction = useMemo(() => {
    return caseSecondaryActions?.find((action) => nextActions?.some((data) => data.action === action?.action)) || { action: "", label: "" };
  }, [nextActions]);

  const tertiaryAction = useMemo(
    () => caseTertiaryActions?.find((action) => nextActions?.some((data) => data.action === action?.action)) || { action: "", label: "" },
    [nextActions]
  );

  const isDelayCondonationApplicable = useMemo(
    () => caseDetails?.caseDetails?.delayApplications?.formdata[0]?.data?.delayCondonationType?.code === "NO" || undefined,
    [caseDetails]
  );

  const statue = useMemo(() => {
    const statutesAndSections = caseDetails?.statutesAndSections;
    if (!statutesAndSections?.length) return "";
    const section = statutesAndSections?.[0]?.sections?.[0];
    const subsection = statutesAndSections?.[0]?.subsections?.[0];

    if (!section || !subsection) return "";

    return section && subsection
      ? `${section
          ?.split(" ")
          ?.map((splitString) => splitString.charAt(0))
          ?.join("")} S${subsection}`
      : "";
  }, [caseDetails?.statutesAndSections]);

  const litigants = caseDetails?.litigants?.length > 0 ? caseDetails?.litigants : [];
  const finalLitigantsData = litigants?.map((litigant) => {
    return {
      ...litigant,
      name: removeInvalidNameParts(litigant.additionalDetails?.fullName),
    };
  });
  const reps = caseDetails?.representatives?.length > 0 ? caseDetails?.representatives : [];
  const finalRepresentativesData = reps.map((rep) => {
    return {
      ...rep,
      name: removeInvalidNameParts(rep.additionalDetails?.advocateName),
      partyType: `Advocate (for ${rep.representing?.map((client) => removeInvalidNameParts(client?.additionalDetails?.fullName))?.join(", ")})`,
    };
  });

  const allAdvocates = useMemo(() => getAdvocates(caseDetails), [caseDetails]);
  const listAllAdvocates = useMemo(() => Object.values(allAdvocates || {}).flat(), [allAdvocates]);
  const isAdvocatePresent = useMemo(() => listAllAdvocates?.includes(userInfo?.uuid), [listAllAdvocates, userInfo?.uuid]);

  const onBehalfOfuuid = useMemo(() => Object.keys(allAdvocates)?.find((key) => allAdvocates[key].includes(userInfo?.uuid)), [
    allAdvocates,
    userInfo?.uuid,
  ]);
  const { data: applicationData, isLoading: isApplicationLoading } = Digit.Hooks.submissions.useSearchSubmissionService(
    {
      criteria: {
        filingNumber,
        tenantId,
      },
      tenantId,
    },
    {},
    filingNumber + "allApplications",
    filingNumber
  );
  const extensionApplications = useMemo(
    () =>
      applicationData?.applicationList?.filter(
        (item) =>
          item?.applicationType === "EXTENSION_SUBMISSION_DEADLINE" &&
          item?.onBehalfOf?.includes(onBehalfOfuuid) &&
          ![
            SubmissionWorkflowState.COMPLETED,
            SubmissionWorkflowState.DELETED,
            SubmissionWorkflowState.REJECTED,
            SubmissionWorkflowState.ABATED,
          ].includes(item?.status)
      ) || [],
    [applicationData, onBehalfOfuuid]
  );

  const productionOfDocumentApplications = useMemo(
    () =>
      applicationData?.applicationList?.filter(
        (item) =>
          item?.applicationType === "PRODUCTION_DOCUMENTS" &&
          item?.onBehalfOf?.includes(onBehalfOfuuid) &&
          ![SubmissionWorkflowState.DELETED, SubmissionWorkflowState.ABATED].includes(item?.status)
      ) || [],
    [applicationData, onBehalfOfuuid]
  );

  useMemo(() => {
    setIsDelayApplicationPending(
      Boolean(
        applicationData?.applicationList?.some(
          (item) =>
            item?.applicationType === "DELAY_CONDONATION" &&
            [SubmissionWorkflowState.PENDINGAPPROVAL, SubmissionWorkflowState.PENDINGREVIEW].includes(item?.status)
        )
      )
    );
  }, [applicationData]);

  const isDelayApplicationCompleted = useMemo(
    () =>
      Boolean(
        applicationData?.applicationList?.some(
          (item) => item?.applicationType === "DELAY_CONDONATION" && [SubmissionWorkflowState.COMPLETED].includes(item?.status)
        )
      ),
    [applicationData]
  );

  const caseRelatedData = useMemo(
    () => ({
      caseId,
      filingNumber,
      cnrNumber,
      title: caseDetails?.caseTitle || "",
      stage: caseDetails?.stage,
      parties: [...finalLitigantsData, ...finalRepresentativesData],
      case: caseDetails,
      statue: statue,
    }),
    [caseDetails, caseId, cnrNumber, filingNumber, statue]
  );

  const caseStatus = useMemo(() => caseDetails?.status || "", [caseDetails]);
  const showMakeSubmission = useMemo(() => {
    return (
      isAdvocatePresent &&
      userRoles?.includes("SUBMISSION_CREATOR") &&
      [
        CaseWorkflowState.PENDING_ADMISSION_HEARING,
        CaseWorkflowState.ADMISSION_HEARING_SCHEDULED,
        CaseWorkflowState.PENDING_NOTICE,
        CaseWorkflowState.PENDING_RESPONSE,
        CaseWorkflowState.PENDING_ADMISSION,
        CaseWorkflowState.CASE_ADMITTED,
      ].includes(caseStatus)
    );
  }, [userRoles, caseStatus, isAdvocatePresent]);

  const openDraftModal = (orderList) => {
    setDraftOrderList(orderList);
    setOrderDraftModal(true);
  };

  const openSubmissionViewModal = (submissionList, openEvidenceModalFunc) => {
    setSubmissionsViewList({ list: submissionList, func: openEvidenceModalFunc });
    setSubmissionsViewModal(true);
  };

  const handleTakeAction = () => {
    setShowMenu(!showMenu);
    setShowOtherMenu(false);
  };

  const onSuccess = async (response, data) => {
    showToast({
      isError: false,
      message: !data?.body?.artifact?.isEvidence ? t("SUCCESSFULLY_UNMARKED_MESSAGE") : t("SUCCESSFULLY_MARKED_MESSAGE"),
    });
    refetchCaseData();
  };

  const onError = async (error, data) => {
    showToast({
      isError: true,
      message: !data?.body?.artifact?.isEvidence ? t("UNSUCCESSFULLY_UNMARKED_MESSAGE") : t("UNSUCCESSFULLY_MARKED_MESSAGE"),
    });
  };

  const handleMarkEvidence = async (documentSubmission, isEvidence) => {
    await evidenceUpdateMutation.mutate(
      {
        url: Urls.dristi.evidenceUpdate,
        params: {},
        body: {
          artifact: {
            ...documentSubmission?.[0].artifactList,
            isEvidence: !isEvidence,
            isVoid: false,
            reason: "",
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
  };

  const caseInfo = useMemo(
    () => [
      {
        key: "CASE_NUMBER",
        value: caseDetails?.filingNumber,
      },
      {
        key: "CASE_CATEGORY",
        value: caseDetails?.caseCategory,
      },
      {
        key: "CASE_TYPE",
        value: "NIA S138",
      },
      {
        key: "COURT_NAME",
        value: t(`COMMON_MASTERS_COURT_R00M_${caseDetails?.courtId}`),
      },
      {
        key: "SUBMITTED_ON",
        value: formatDate(new Date(caseDetails?.filingDate)),
      },
    ],
    [caseDetails?.caseCategory, caseDetails?.courtId, caseDetails?.filingDate, caseDetails?.filingNumber, t]
  );

  const configList = useMemo(() => {
    const docSetFunc = (docObj) => {
      const applicationNumber = docObj?.[0]?.applicationList?.applicationNumber;
      const status = docObj?.[0]?.applicationList?.status;
      const createdByUuid = docObj?.[0]?.applicationList?.statuteSection?.auditdetails?.createdBy;
      const documentCreatedByUuid = docObj?.[0]?.artifactList?.auditdetails?.createdBy;
      const artifactNumber = docObj?.[0]?.artifactList?.artifactNumber;
      const documentStatus = docObj?.[0]?.artifactList?.status;
      if (isCitizen) {
        if (documentStatus === "PENDING_E-SIGN" && documentCreatedByUuid === userInfo?.uuid) {
          history.push(`/digit-ui/citizen/submissions/submit-document?filingNumber=${filingNumber}&artifactNumber=${artifactNumber}`);
        }
        if (
          [SubmissionWorkflowState.PENDINGPAYMENT, SubmissionWorkflowState.PENDINGESIGN, SubmissionWorkflowState.PENDINGSUBMISSION].includes(status)
        ) {
          if (createdByUuid === userInfo?.uuid) {
            history.push(
              `/digit-ui/${
                isCitizen ? "citizen" : "employee"
              }/submissions/submissions-create?filingNumber=${filingNumber}&applicationNumber=${applicationNumber}`
            );
          }
        } else {
          setDocumentSubmission(docObj);
          setShow(true);
        }
      } else {
        if (
          ![SubmissionWorkflowState.PENDINGPAYMENT, SubmissionWorkflowState.PENDINGESIGN, SubmissionWorkflowState.PENDINGSUBMISSION].includes(status)
        ) {
          setDocumentSubmission(docObj);
          setShow(true);
        }
      }
    };

    const orderSetFunc = (order) => {
      if (isCitizen) {
        // for citizen, only those orders should be visible which are published
        setCurrentOrder(order);
        setShowOrderReviewModal(true);
      } else {
        if (order?.status === OrderWorkflowState.DRAFT_IN_PROGRESS) {
          history.push(`/${window.contextPath}/employee/orders/generate-orders?filingNumber=${filingNumber}&orderNumber=${order?.orderNumber}`);
        } else {
          setCurrentOrder(order);
          setShowOrderReviewModal(true);
        }
      }
    };

    const takeActionFunc = (hearingData) => {
      setCurrentHearing(hearingData);
      setShowHearingTranscriptModal(true);
    };

    const handleFilingAction = async (history, column, row, item) => {
      const docObj = [
        {
          itemType: item.id,
          status: row.workflow?.action,
          details: {
            applicationType: row.artifactType,
            applicationSentOn: getDate(parseInt(row.auditdetails.createdTime)),
            sender: row.owner,
            additionalDetails: row.additionalDetails,
            applicationId: row.id,
            auditDetails: row.auditDetails,
          },
          applicationContent: {
            tenantId: row.tenantId,
            fileStoreId: row.file?.fileStore,
            id: row.file?.id,
            documentType: row.file?.documentType,
            documentUid: row.file?.documentUid,
            additionalDetails: row.file?.additionalDetails,
          },
          comments: row.comments,
          artifactList: row,
        },
      ];
      if ("mark_as_evidence" === item.id || "unmark_as_evidence" === item.id) {
        await handleMarkEvidence(docObj, row?.isEvidence);
      } else if ("mark_as_void" === item.id || "view_reason_for_voiding" === item.id) {
        setDocumentSubmission(docObj);
        setVoidReason(row?.reason);
        setShowVoidModal(true);
      } else if ("download_filing" === item.id) {
        downloadPdf(tenantId, row?.file?.fileStore);
      }
    };

    return TabSearchconfig?.TabSearchconfig.map((tabConfig) => {
      return tabConfig.label === "Parties"
        ? {
            ...tabConfig,
            apiDetails: {
              ...tabConfig.apiDetails,
              requestBody: {
                ...tabConfig.apiDetails.requestBody,
                criteria: [
                  {
                    filingNumber: filingNumber,
                  },
                ],
              },
            },
          }
        : tabConfig.label === "Orders"
        ? {
            ...tabConfig,
            apiDetails: {
              ...tabConfig.apiDetails,
              requestBody: {
                ...tabConfig.apiDetails.requestBody,
                criteria: {
                  filingNumber: filingNumber,
                  tenantId: tenantId,
                },
              },
            },
            sections: {
              ...tabConfig.sections,
              search: {
                ...tabConfig.sections.search,
                uiConfig: {
                  ...tabConfig.sections.search.uiConfig,
                  fields: [
                    {
                      label: "PARTIES",
                      isMandatory: false,
                      key: "parties",
                      type: "dropdown",
                      populators: {
                        name: "parties",
                        optionsKey: "name",
                        options: caseRelatedData.parties.map((party) => {
                          return { code: removeInvalidNameParts(party.name), name: removeInvalidNameParts(party.name) };
                        }),
                      },
                    },
                    ...tabConfig.sections.search.uiConfig.fields,
                  ],
                },
              },
              searchResult: {
                ...tabConfig.sections.searchResult,
                uiConfig: {
                  ...tabConfig.sections.searchResult.uiConfig,
                  columns: tabConfig.sections.searchResult.uiConfig.columns.map((column) => {
                    return column.label === "ORDER_TYPE"
                      ? {
                          ...column,
                          clickFunc: orderSetFunc,
                        }
                      : column;
                  }),
                },
              },
            },
          }
        : tabConfig.label === "Hearings"
        ? {
            ...tabConfig,
            apiDetails: {
              ...tabConfig.apiDetails,
              requestBody: {
                ...tabConfig.apiDetails.requestBody,
                criteria: {
                  filingNumber: filingNumber,
                  tenantId: tenantId,
                },
              },
            },
            sections: {
              ...tabConfig.sections,
              search: {
                ...tabConfig.sections.search,
                uiConfig: {
                  ...tabConfig.sections.search.uiConfig,
                  fields: [
                    {
                      label: "PARTIES",
                      isMandatory: false,
                      key: "parties",
                      type: "dropdown",
                      populators: {
                        name: "parties",
                        optionsKey: "name",
                        options: caseRelatedData.parties.map((party) => {
                          return { code: removeInvalidNameParts(party.name), name: removeInvalidNameParts(party.name) };
                        }),
                      },
                    },
                    ...tabConfig.sections.search.uiConfig.fields,
                  ],
                },
              },
              searchResult: {
                ...tabConfig.sections.searchResult,
                uiConfig: {
                  ...tabConfig.sections.searchResult.uiConfig,
                  columns: tabConfig.sections.searchResult.uiConfig.columns.map((column) => {
                    return column.label === "CS_ACTIONS"
                      ? {
                          ...column,
                          clickFunc: takeActionFunc,
                        }
                      : column;
                  }),
                },
              },
            },
          }
        : tabConfig.label === "History"
        ? {
            ...tabConfig,
            apiDetails: {
              ...tabConfig.apiDetails,
              requestBody: {
                ...tabConfig.apiDetails.requestBody,
                filingNumber: filingNumber,
                tenantId: tenantId,
              },
            },
          }
        : tabConfig.label === "Documents"
        ? {
            ...tabConfig,
            apiDetails: {
              ...tabConfig.apiDetails,
              requestBody: {
                ...tabConfig.apiDetails.requestBody,
                criteria: {
                  caseId: caseId,
                  filingNumber: filingNumber,
                  tenantId: tenantId,
                },
              },
            },
            sections: {
              ...tabConfig.sections,
              search: {
                ...tabConfig.sections.search,
                uiConfig: {
                  ...tabConfig.sections.search.uiConfig,
                  fields: [
                    {
                      label: "OWNER",
                      isMandatory: false,
                      key: "owner",
                      type: "dropdown",
                      populators: {
                        name: "owner",
                        optionsKey: "name",
                        options: caseRelatedData.parties.map((party) => {
                          return { code: removeInvalidNameParts(party.name), name: removeInvalidNameParts(party.name), value: party.individualId };
                        }),
                      },
                    },
                    ...tabConfig.sections.search.uiConfig.fields,
                  ],
                },
              },
              searchResult: {
                ...tabConfig.sections.searchResult,
                uiConfig: {
                  ...tabConfig.sections.searchResult.uiConfig,
                  columns: tabConfig.sections.searchResult.uiConfig.columns.map((column) => {
                    switch (column.label) {
                      case "FILE":
                      case "FILING_NAME": {
                        return {
                          ...column,
                          clickFunc: docSetFunc,
                        };
                      }
                      case "CS_ACTIONS": {
                        return {
                          ...column,
                          clickFunc: handleFilingAction,
                        };
                      }
                      default: {
                        return column;
                      }
                    }
                  }),
                },
              },
            },
          }
        : tabConfig.label === "Submissions"
        ? {
            ...tabConfig,
            apiDetails: {
              ...tabConfig.apiDetails,
              requestBody: {
                ...tabConfig.apiDetails.requestBody,
                criteria: {
                  filingNumber: filingNumber,
                  tenantId: tenantId,
                },
              },
            },
            sections: {
              ...tabConfig.sections,
              search: {
                ...tabConfig.sections.search,
                uiConfig: {
                  ...tabConfig.sections.search.uiConfig,
                  fields: [
                    {
                      label: "OWNER",
                      isMandatory: false,
                      key: "owner",
                      type: "dropdown",
                      populators: {
                        name: "owner",
                        optionsKey: "name",
                        options: caseRelatedData.parties.map((party) => {
                          return {
                            code: removeInvalidNameParts(party.name),
                            name: removeInvalidNameParts(party.name),
                            value: party.additionalDetails?.uuid,
                          };
                        }),
                      },
                    },
                    ...tabConfig.sections.search.uiConfig.fields,
                  ],
                },
              },
              searchResult: {
                ...tabConfig.sections.searchResult,
                uiConfig: {
                  ...tabConfig.sections.searchResult.uiConfig,
                  columns: tabConfig.sections.searchResult.uiConfig.columns.map((column) => {
                    return column.label === "DOCUMENT_TEXT" || column.label === "SUBMISSION_TYPE"
                      ? {
                          ...column,
                          clickFunc: docSetFunc,
                        }
                      : column.label === "OWNER"
                      ? {
                          ...column,
                          parties: caseRelatedData.parties,
                        }
                      : column;
                  }),
                },
              },
            },
          }
        : {
            ...tabConfig,
            apiDetails: {
              ...tabConfig.apiDetails,
              requestParam: {
                ...tabConfig.apiDetails?.requestParam,
                filingNumber: filingNumber,
                cnrNumber,
                applicationNumber: "",
              },
            },
          };
    });
  }, [caseId, caseRelatedData, cnrNumber, filingNumber, history, isCitizen, tenantId, userInfo]);

  const newTabSearchConfig = {
    ...TabSearchconfig,
    TabSearchconfig: configList,
  };

  const indexOfActiveTab = newTabSearchConfig?.TabSearchconfig?.findIndex((tabData) => tabData.label === activeTab);

  const [defaultValues, setDefaultValues] = useState(defaultSearchValues); // State to hold default values for search fields
  const config = useMemo(() => {
    return newTabSearchConfig?.TabSearchconfig?.[indexOfActiveTab];
  }, [indexOfActiveTab, newTabSearchConfig?.TabSearchconfig]); // initially setting first index config as default from jsonarray

  const voidModalConfig = useMemo(() => {
    if (!showVoidModal) return {};

    const onSuccess = async (response, data) => {
      showToast({
        isError: false,
        message: !data?.body?.artifact?.isVoid ? "SUCCESSFULLY_UNMARKED_AS_VOID_MESSAGE" : "SUCCESSFULLY_MARKED_AS_VOID_MESSAGE",
      });
      refetchCaseData();
      setShowVoidModal(false);
    };

    const onError = async (error, data) => {
      showToast({
        isError: true,
        message: !data?.body?.artifact?.isVoid ? "UNSUCCESSFULLY_UNMARKED_AS_VOID_MESSAGE" : "UNSUCCESSFULLY_MARKED_AS_VOID_MESSAGE",
      });
    };

    const handleMarkAsVoid = async (documentSubmission, isVoid) => {
      await evidenceUpdateMutation.mutate(
        {
          url: Urls.dristi.evidenceUpdate,
          params: {},
          body: {
            artifact: {
              ...documentSubmission?.[0].artifactList,
              filingNumber: filingNumber,
              isVoid,
              isEvidence: false,
              reason: isVoid ? voidReason : "",
              workflow: null,
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

    const handleClose = () => {
      refetchCaseData();
      setShowVoidModal(false);
    };

    return {
      handleClose: handleClose,
      heading: {
        label:
          "view_reason_for_voiding" === documentSubmission?.[0]?.itemType
            ? t("REASON_FOR_VOIDING")
            : "unmark_void_submission" === documentSubmission?.[0]?.itemType
            ? t("ARE_YOU_SURE_TO_UNMARK_AS_VOID")
            : t("ARE_YOU_SURE_TO_MARK_AS_VOID"),
      },
      isStepperModal: true,
      actionSaveLabel:
        userType === "citizen"
          ? undefined
          : "view_reason_for_voiding" === documentSubmission?.[0]?.itemType
          ? t("UNMARK_AS_VOID")
          : "unmark_void_submission" === documentSubmission?.[0]?.itemType
          ? t("MARK_VOID_CONFIRM")
          : t("MARK_AS_VOID"),
      actionCancelLabel: userType === "citizen" ? t("VOID_BACK") : t("MARK_VOID_CANCEL"),
      steps: [
        {
          actionCancelOnSubmit: handleClose,
          actionSaveLableType: "mark_as_void" === documentSubmission?.[0]?.itemType ? "WARNING" : null,
          modalBody: (
            <VoidSubmissionBody
              t={t}
              documentSubmission={documentSubmission}
              setVoidReason={setVoidReason}
              voidReason={voidReason}
              disabled={"view_reason_for_voiding" === documentSubmission[0].itemType || "unmark_void_submission" === documentSubmission[0].itemType}
            />
          ),
          async: true,
          isDisabled: !Boolean(voidReason),
          actionSaveOnSubmit: async () => {
            if (documentSubmission[0].itemType === "unmark_void_submission") {
              await handleMarkAsVoid(documentSubmission, false);
            } else if (documentSubmission[0].itemType === "view_reason_for_voiding") {
              setDocumentSubmission(
                documentSubmission?.map((item) => {
                  return { ...item, itemType: "unmark_void_submission" };
                })
              );
            } else {
              await handleMarkAsVoid(documentSubmission, true);
            }
          },
        },
      ],
    };
  }, [documentSubmission, evidenceUpdateMutation, filingNumber, refetchCaseData, showVoidModal, t, userType, voidReason]);

  const dcaConfirmModalConfig = useMemo(() => {
    if (!isDelayCondonationApplicable) return;
    return {
      handleClose: () => {
        setIsOpenFromPendingTask(false);
        setIsOpenDCA(false);
      },
      heading: { label: "" },
      actionSaveLabel: "",
      isStepperModal: true,
      actionSaveOnSubmit: () => {},
      steps: [
        {
          heading: { label: isDelayApplicationPending ? t("DELAY_CONDONATION_APPLICATION_OPEN") : t("DCA_NOT_FILED") },
          ...(isDelayCondonationApplicable &&
            !isDelayApplicationPending && {
              actionSaveLabel: t("DCA_PROCEED_ANYWAY"),
              actionSaveOnSubmit: () => {
                setIsOpenDCA(false);
                setSubmitModalInfo({ ...admitCaseSubmitConfig, caseInfo: caseInfo });
                setModalInfo({ type: "admitCase", page: 0 });
                setShowModal(true);
              },
            }),
          modalBody: (
            <div style={{ width: "527px", padding: "12px 16px" }}>
              <p style={delayCondonationTextStyle}>
                {isDelayApplicationPending ? t("DELAY_CONDONATION_APPLICATION_OPEN_MESSAGE") : t("DCA_NOT_FILED_MESSAGE")}
              </p>
            </div>
          ),

          actionCancelLabel: "BACK",
          actionCancelOnSubmit: () => {
            setIsOpenDCA(false);
            if (isOpenFromPendingTask) {
              setIsOpenFromPendingTask(false);
              window.history.back();
            }
          },
        },
      ],
    };
  }, [caseInfo, isDelayApplicationPending, isDelayCondonationApplicable, isOpenFromPendingTask, t]);

  const tabData = useMemo(() => {
    return newTabSearchConfig?.TabSearchconfig?.map((configItem, index) => ({
      key: index,
      label: configItem.label,
      active: index === indexOfActiveTab ? true : false,
      displayLabel: configItem?.displayLabel,
    }));
  }, [indexOfActiveTab, newTabSearchConfig?.TabSearchconfig]); // setting number of tab component and making first index enable as default
  const [updateCounter, setUpdateCounter] = useState(0);
  const [toastDetails, setToastDetails] = useState({});
  const [showOtherMenu, setShowOtherMenu] = useState(false);
  const [showScheduleHearingModal, setShowScheduleHearingModal] = useState(false);

  const isTabDisabled = useMemo(() => {
    return isFSO ? true : !relevantStatuses.includes(caseDetails?.status);
  }, [caseDetails?.status, config?.label, isFSO]);

  const isCaseAdmitted = useMemo(() => {
    return caseDetails?.status === "CASE_ADMITTED";
  }, [caseDetails?.status]);

  useEffect(() => {
    if (
      history?.location?.state?.triggerAdmitCase &&
      openAdmitCaseModal &&
      isDelayCondonationApplicable !== undefined &&
      isDelayApplicationCompleted !== undefined
    ) {
      if (isDelayCondonationApplicable && !isDelayApplicationCompleted) {
        setIsOpenFromPendingTask(true);
        setIsOpenDCA(true);
        setShowModal(false);
        setOpenAdmitCaseModal(false);
      } else {
        setSubmitModalInfo({ ...admitCaseSubmitConfig, caseInfo: caseInfo });
        setModalInfo({ type: "admitCase", page: 0 });
        setShowModal(true);
        setOpenAdmitCaseModal(false);
      }
    }
  }, [caseInfo, history?.location, isDelayApplicationCompleted, isDelayCondonationApplicable, openAdmitCaseModal]);

  useEffect(() => {
    if (history?.location?.state?.from === "orderSuccessModal" && !toastStatus?.alreadyShown) {
      showToast(true);
      setToastDetails({
        isError: false,
        message: "ORDER_SUCCESSFULLY_ISSUED",
      });
    }
  }, [history.location]);

  useEffect(() => {
    if (history.location?.state?.orderObj && !showOrderReviewModal) {
      setCurrentOrder(history.location?.state?.orderObj);
      setShowOrderReviewModal(true);
    }
  }, [history.location?.state?.orderObj, OrderReviewModal, showOrderReviewModal]);

  useEffect(() => {
    if (history.location?.state?.applicationDocObj && !show) {
      setDocumentSubmission(history.location?.state?.applicationDocObj);
      setShow(true);
    }
  }, [history.location?.state?.applicationDocObj, show]);

  useEffect(() => {
    // Set default values when component mounts
    setDefaultValues(defaultSearchValues);
    const isSignSuccess = localStorage.getItem("esignProcess");
    const doc = JSON.parse(localStorage.getItem("docSubmission"));
    if (isSignSuccess) {
      if (doc) {
        setDocumentSubmission(doc);
      }
      setShow(true);
    }
  }, []);

  const onTabChange = (n) => {
    history.replace(`${path}?caseId=${caseId}&filingNumber=${filingNumber}&tab=${newTabSearchConfig?.TabSearchconfig?.[n].label}`);
    // urlParams.set("tab", newTabSearchConfig?.TabSearchconfig?.[n].label);
  };

  const handleIssueNotice = async (hearingDate, hearingNumber) => {
    try {
      const orderBody = {
        createdDate: null,
        tenantId,
        cnrNumber: caseDetails?.cnrNumber,
        filingNumber,
        statuteSection: {
          tenantId,
        },
        orderType: "NOTICE",
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
        ...(hearingNumber && { hearingNumber }),
        additionalDetails: {
          formdata: {
            orderType: {
              code: "NOTICE",
              type: "NOTICE",
              name: "ORDER_TYPE_NOTICE",
            },
            hearingDate,
          },
        },
      };
      return DRISTIService.customApiService(Urls.dristi.ordersCreate, { order: orderBody }, { tenantId })
        .then((res) => {
          DRISTIService.customApiService(Urls.dristi.pendingTask, {
            pendingTask: {
              name: t("DRAFT_IN_PROGRESS_ISSUE_NOTICE"),
              entityType: "order-default",
              referenceId: `MANUAL_${res?.order?.orderNumber}`,
              status: "DRAFT_IN_PROGRESS",
              assignedTo: [],
              assignedRole: ["JUDGE_ROLE"],
              cnrNumber: updatedCaseDetails?.cnrNumber,
              filingNumber: caseDetails?.filingNumber,
              isCompleted: false,
              stateSla: todayDate + stateSla.NOTICE,
              additionalDetails: {},
              tenantId,
            },
          });
          history.push(`/digit-ui/employee/orders/generate-orders?filingNumber=${caseDetails?.filingNumber}&orderNumber=${res.order.orderNumber}`, {
            caseId: caseDetails?.id,
            tab: "Orders",
          });
        })
        .catch((error) => {
          console.error("Error while creating order", error);
          showToast({ isError: true, message: "ORDER_CREATION_FAILED" });
        });
    } catch (error) {
      console.error("Error while fetching Hearing Data", error);
      showToast({ isError: true, message: "ERROR_WHILE_FETCH_HEARING_DETAILS" });
    }
  };

  const getDefaultValue = (value) => value || "N.A.";
  const formatDateOrDefault = (date) => (date ? formatDate(new Date(date)) : "N.A.");

  const caseBasicDetails = useMemo(() => {
    return [
      {
        key: "CS_FILING_NO",
        value: getDefaultValue(caseDetails?.filingNumber),
      },
      {
        key: "CS_COMPLAINT_NO",
        value: getDefaultValue(caseDetails?.cmpNumber),
      },
      {
        key: "CS_CNR",
        value: getDefaultValue(caseDetails?.cnrNumber),
      },
      {
        key: "CS_CCST",
        value: getDefaultValue(caseDetails?.courtCaseNumber),
      },
      {
        key: "SUBMITTED_ON",
        value: formatDateOrDefault(caseDetails?.filingDate),
      },
      {
        key: "REGISTERED_ON",
        value: formatDateOrDefault(caseDetails?.registrationDate),
      },
    ];
  }, [caseDetails]);

  const updateCaseDetails = async (action, data = {}) => {
    let respondentDetails = caseDetails?.additionalDetails?.respondentDetails;
    let witnessDetails = caseDetails?.additionalDetails?.witnessDetails;
    const newcasedetails = {
      ...caseDetails,
      additionalDetails: { ...caseDetails.additionalDetails, respondentDetails, witnessDetails, judge: data },
    };

    return DRISTIService.caseUpdateService(
      {
        cases: {
          ...newcasedetails,
          linkedCases: caseDetails?.linkedCases ? caseDetails?.linkedCases : [],
          workflow: {
            ...caseDetails?.workflow,
            action,
            ...(action === "SEND_BACK" && { assignes: [caseDetails.auditDetails.createdBy] || [] }),
          },
        },
        tenantId,
      },
      tenantId
    ).then(async (response) => {
      await refetchCaseData();
      revalidateWorkflow();
      setUpdatedCaseDetails(response?.cases?.[0]);
    });
  };

  const handleSendCaseBack = (props) => {
    updateCaseDetails("SEND_BACK", { comment: props?.commentForLitigant }).then((res) => {
      setModalInfo({ ...modalInfo, page: 1 });
    });
  };

  const handleAdmitDismissCaseOrder = async (generateOrder, type) => {
    try {
      const orderType = "ADMIT_DISMISS_CASE";
      const formdata = {
        orderType: {
          code: orderType,
          type: orderType,
          name: `ORDER_TYPE_${orderType}`,
        },
        isCaseAdmittedOrDismissed: {
          code: type === "reject" ? "DISMISSED" : type === "accept" ? "ADMITTED" : null,
          name: type === "reject" ? "DISMISSED" : type === "accept" ? "ADMITTED" : null,
        },
      };
      if (generateOrder) {
        const reqbody = {
          order: {
            createdDate: null,
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
            },
            ...(documentSubmission?.[0]?.applicationList?.additionalDetails?.onBehalOfName && {
              orderDetails: { parties: [{ partyName: documentSubmission?.[0]?.applicationList?.additionalDetails?.onBehalOfName }] },
            }),
          },
        };
        try {
          const res = await ordersService.createOrder(reqbody, { tenantId });
          const name = "ADMIT_DISMISS_CASE";
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
              // stateSla: stateSla.DRAFT_IN_PROGRESS * dayInMillisecond + todayDate,
              additionalDetails: { orderType },
              tenantId,
            },
          });
          history.push(`/${window.contextPath}/employee/orders/generate-orders?filingNumber=${filingNumber}&orderNumber=${res?.order?.orderNumber}`);
        } catch (error) {}
      }
    } catch (error) {}
  };

  const handleAdmitCase = async () => {
    setCaseAdmitLoader(true);
    setOpenAdmitCaseModal(false);
    await handleAdmitDismissCaseOrder(true, "accept");
  };

  const scheduleHearing = async ({ purpose, participant, date }) => {
    return DRISTIService.createHearings(
      {
        hearing: {
          tenantId: tenantId,
          filingNumber: [caseDetails.filingNumber],
          hearingType: purpose,
          status: true,
          attendees: [
            ...Object.values(participant)
              .map((val) => val.attendees.map((attendee) => JSON.parse(attendee)))
              .flat(Infinity),
          ],
          startTime: Date.parse(
            `${date
              .split(" ")
              .map((date, i) => (i === 0 ? date.slice(0, date.length - 2) : date))
              .join(" ")}`
          ),
          endTime: Date.parse(
            `${date
              .split(" ")
              .map((date, i) => (i === 0 ? date.slice(0, date.length - 2) : date))
              .join(" ")}`
          ),
          workflow: {
            action: "CREATE",
            assignes: [],
            comments: "Create new Hearing",
            documents: [{}],
          },
          documents: [],
        },
        tenantId,
      },
      { tenantId: tenantId }
    );
  };

  const handleScheduleCase = async (props) => {
    const hearingData = await scheduleHearing({ purpose: "ADMISSION", date: props.date, participant: props.participant });
    setSubmitModalInfo({
      ...scheduleCaseAdmissionConfig,
      caseInfo: [
        ...caseInfo,
        {
          key: "CS_ISSUE_NOTICE",
          value: props.date,
        },
        {
          hearingNumber: hearingData?.hearing?.hearingNumber,
        },
      ],
    });
    updateCaseDetails("SCHEDULE_ADMISSION_HEARING", props).then((res) => {
      setModalInfo({ ...modalInfo, page: 2 });
      DRISTIService.customApiService(Urls.dristi.pendingTask, {
        pendingTask: {
          name: "Schedule Admission Hearing",
          entityType: "case-default",
          referenceId: `MANUAL_${caseDetails?.filingNumber}`,
          status: "PENDING_ADMISSION_HEARING",
          assignedTo: [],
          assignedRole: ["JUDGE_ROLE"],
          cnrNumber: updatedCaseDetails?.cnrNumber,
          filingNumber: caseDetails?.filingNumber,
          isCompleted: true,
          stateSla: todayDate + stateSla.SCHEDULE_HEARING,
          additionalDetails: {},
          tenantId,
        },
      });
    });
  };

  const handleScheduleNextHearing = () => {
    const reqBody = {
      order: {
        createdDate: null,
        tenantId,
        cnrNumber: updatedCaseDetails?.cnrNumber || caseDetails?.cnrNumber,
        filingNumber: caseDetails?.filingNumber,
        statuteSection: {
          tenantId,
        },
        orderType: OrderTypes.SCHEDULE_OF_HEARING_DATE,
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
          formdata: {
            orderType: {
              code: OrderTypes.SCHEDULE_OF_HEARING_DATE,
              type: OrderTypes.SCHEDULE_OF_HEARING_DATE,
              name: `ORDER_TYPE_${OrderTypes.SCHEDULE_OF_HEARING_DATE}`,
            },
          },
        },
      },
    };
    DRISTIService.customApiService(Urls.dristi.ordersCreate, reqBody, { tenantId })
      .then((res) => {
        history.push(`/digit-ui/employee/orders/generate-orders?filingNumber=${caseDetails?.filingNumber}&orderNumber=${res.order.orderNumber}`, {
          caseId: caseId,
          tab: "Orders",
        });
        DRISTIService.customApiService(Urls.dristi.pendingTask, {
          pendingTask: {
            name: "Schedule Hearing",
            entityType: "case-default",
            referenceId: `MANUAL_${caseDetails?.filingNumber}`,
            status: "SCHEDULE_HEARING",
            assignedTo: [],
            assignedRole: ["JUDGE_ROLE"],
            cnrNumber: updatedCaseDetails?.cnrNumber,
            filingNumber: caseDetails?.filingNumber,
            isCompleted: true,
            stateSla: todayDate + stateSla.SCHEDULE_HEARING,
            additionalDetails: {},
            tenantId,
          },
        });
      })
      .catch((error) => {
        console.error("Error while creating order", error);
        showToast({ isError: true, message: "ORDER_CREATION_FAILED" });
      });
  };

  const updateConfigWithCaseDetails = (config, caseDetails) => {
    const litigantsNames = caseDetails.litigants?.map((litigant) => {
      return { name: litigant.additionalDetails.fullName, individualId: litigant.individualId };
    });

    config.checkBoxes.forEach((checkbox) => {
      if (checkbox.key === "Litigants") {
        checkbox.dependentFields = litigantsNames;
      }
    });

    return config;
  };

  const updatedConfig = caseDetails && updateConfigWithCaseDetails(selectParticipantConfig, caseDetails);

  const onSendBack = async () => {
    switch (secondaryAction.action) {
      case "SEND_BACK":
        setSubmitModalInfo({
          ...sendBackCase,
          caseInfo: [{ key: "CASE_FILE_NUMBER", value: caseDetails?.filingNumber }],
        });
        setShowModal(true);
        setModalInfo({ type: "sendCaseBack", page: 0 });
        break;

      case "REJECT":
        await handleAdmitDismissCaseOrder(true, "reject");
        break;

      default:
        break;
    }
  };

  const { data: hearingDetails } = Digit.Hooks.hearings.useGetHearings(
    {
      hearing: { tenantId },
      criteria: {
        tenantID: tenantId,
        filingNumber: filingNumber,
      },
    },
    {},
    filingNumber,
    Boolean(filingNumber)
  );

  const isDcaHearingScheduled = useMemo(() => {
    const isDcaHearingScheduled = Boolean(
      hearingDetails?.HearingList?.find(
        (hearing) =>
          ["DELAY_CONDONATION_HEARING", "DELAY_CONDONATION_AND_ADMISSION"].includes(hearing?.hearingType) &&
          [HearingWorkflowState?.INPROGRESS, HearingWorkflowState?.SCHEDULED].includes(hearing?.status)
      )
    );
    return isDcaHearingScheduled;
  }, [hearingDetails]);

  const currentHearingId = useMemo(
    () =>
      hearingDetails?.HearingList?.find((list) => list?.hearingType === "ADMISSION" && !(list?.status === "COMPLETED" || list?.status === "ABATED"))
        ?.hearingId,
    [hearingDetails?.HearingList]
  );

  const currentHearingStatus = useMemo(
    () =>
      hearingDetails?.HearingList?.find(
        (list) =>
          list?.hearingType === "ADMISSION" && !(list?.status === HearingWorkflowState.COMPLETED || list?.status === HearingWorkflowState.ABATED)
      )?.status,
    [hearingDetails?.HearingList]
  );

  const { data: ordersData } = useSearchOrdersService(
    { criteria: { tenantId: tenantId, filingNumber } },
    { tenantId },
    filingNumber + currentHearingId,
    Boolean(filingNumber),
    0
  );

  const orderListFiltered = useMemo(() => {
    if (!ordersData?.list) return [];

    const filteredOrders = ordersData?.list?.filter(
      (item) => item.orderType === "NOTICE" && item?.status === "PUBLISHED" && item?.hearingNumber === currentHearingId
    );

    const sortedOrders = filteredOrders?.sort((a, b) => {
      return new Date(b.auditDetails.createdTime) - new Date(a.auditDetails.createdTime);
    });

    return sortedOrders;
  }, [currentHearingId, ordersData]);

  const noticeFailureCount = useMemo(() => (isCaseAdmitted ? 0 : orderListFiltered?.length - 1), [isCaseAdmitted, orderListFiltered?.length]);

  const getHearingData = async () => {
    try {
      const { HearingList = [] } = await Digit.HearingService.searchHearings({
        hearing: { tenantId },
        criteria: {
          tenantID: tenantId,
          filingNumber: filingNumber,
        },
      });
      const { startTime: hearingDate, hearingId: hearingNumber } = HearingList?.find(
        (list) => list?.hearingType === "ADMISSION" && !(list?.status === "COMPLETED" || list?.status === "ABATED")
      ) || { startTime: null, hearingId: null };

      if (!(hearingDate || hearingNumber)) {
        showToast(
          {
            isError: true,
            message: "NO_ADMISSION_HEARING_SCHEDULED",
          },
          3000
        );
      }
      return { hearingDate, hearingNumber };
    } catch (error) {
      console.error("Error while fetching Hearing Data", error);
      showToast({ isError: true, message: "ERROR_WHILE_FETCH_HEARING_DETAILS" }, 3000);
      return { hearingDate: null, hearingNumber: null };
    }
  };

  const onSubmit = async () => {
    switch (primaryAction.action) {
      case "REGISTER":
        break;
      case "ADMIT":
        if (caseDetails?.status === "ADMISSION_HEARING_SCHEDULED") {
          const { hearingDate, hearingNumber } = await getHearingData();
          if (hearingNumber) {
            const {
              list: [orderData],
            } = await Digit.ordersService.searchOrder({
              tenantId,
              criteria: {
                filingNumber,
                applicationNumber: "",
                cnrNumber,
                status: OrderWorkflowState.DRAFT_IN_PROGRESS,
                hearingNumber: hearingNumber,
              },
              pagination: { limit: 1, offset: 0 },
            });
            if (orderData?.orderType === "NOTICE") {
              history.push(
                `/digit-ui/employee/orders/generate-orders?filingNumber=${caseDetails?.filingNumber}&orderNumber=${orderData.orderNumber}`,
                {
                  caseId: caseId,
                  tab: "Orders",
                }
              );
              await updateCaseDetails("ADMIT");
            } else {
              await handleIssueNotice(hearingDate, hearingNumber);
              await updateCaseDetails("ADMIT");
            }
          }
        } else {
          if ((isDelayApplicationPending || isDelayCondonationApplicable) && !isDelayApplicationCompleted) {
            setIsOpenDCA(true);
          } else {
            setSubmitModalInfo({ ...admitCaseSubmitConfig, caseInfo: caseInfo });
            setModalInfo({ type: "admitCase", page: 0 });
            setShowModal(true);
          }
        }
        break;
      case "ISSUE_ORDER":
        const { hearingDate, hearingNumber } = await getHearingData();
        if (hearingNumber) {
          const {
            list: [orderData],
          } = await Digit.ordersService.searchOrder({
            tenantId,
            criteria: { filingNumber, applicationNumber: "", cnrNumber, status: OrderWorkflowState.DRAFT_IN_PROGRESS, hearingNumber: hearingNumber },
            pagination: { limit: 1, offset: 0 },
          });
          if (orderData?.orderType === "NOTICE") {
            history.push(`/digit-ui/employee/orders/generate-orders?filingNumber=${caseDetails?.filingNumber}&orderNumber=${orderData.orderNumber}`, {
              caseId: caseId,
              tab: "Orders",
            });
          } else {
            handleIssueNotice(hearingDate, hearingNumber);
          }
        }
        break;
      case "SCHEDULE_ADMISSION_HEARING":
        setShowScheduleHearingModal(true);
        setCreateAdmissionOrder(true);
        break;

      default:
        break;
    }
  };

  const onSaveDraft = async () => {
    if (
      [CaseWorkflowState.ADMISSION_HEARING_SCHEDULED, CaseWorkflowState.PENDING_NOTICE, CaseWorkflowState.PENDING_RESPONSE].includes(
        caseDetails?.status
      )
    ) {
      const { hearingDate, hearingNumber } = await getHearingData();
      if (hearingNumber) {
        const date = new Date(hearingDate);
        const requestBody = {
          order: {
            createdDate: null,
            tenantId: tenantId,
            hearingNumber: hearingNumber,
            filingNumber: filingNumber,
            cnrNumber: cnrNumber,
            statuteSection: {
              tenantId: tenantId,
            },
            orderType: "INITIATING_RESCHEDULING_OF_HEARING_DATE",
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
              formdata: {
                orderType: {
                  type: "INITIATING_RESCHEDULING_OF_HEARING_DATE",
                  isactive: true,
                  code: "INITIATING_RESCHEDULING_OF_HEARING_DATE",
                  name: "ORDER_TYPE_INITIATING_RESCHEDULING_OF_HEARING_DATE",
                },
                originalHearingDate: `${date.getFullYear()}-${date.getMonth() < 9 ? `0${date.getMonth() + 1}` : date.getMonth() + 1}-${
                  date.getDate() < 10 ? `0${date.getDate()}` : date.getDate()
                }`,
              },
            },
          },
        };
        ordersService
          .createOrder(requestBody, { tenantId: Digit.ULBService.getCurrentTenantId() })
          .then((res) => {
            history.push(`/${window.contextPath}/employee/orders/generate-orders?filingNumber=${filingNumber}&orderNumber=${res.order.orderNumber}`, {
              caseId: caseDetails?.id,
              tab: "Orders",
            });
          })
          .catch((err) => {
            console.error("Error while creating order", err);
            showToast({ isError: true, message: "ORDER_CREATION_FAILED" });
          });
      }
    } else {
      setShowModal(true);
      setSubmitModalInfo({
        ...scheduleCaseSubmitConfig,
        caseInfo: [...caseInfo],
        shortCaseInfo: [
          {
            key: "CASE_NUMBER",
            value: caseDetails?.caseNumber,
          },
          {
            key: "COURT_NAME",
            value: t(`COMMON_MASTERS_COURT_R00M_${caseDetails?.courtId}`),
          },
          {
            key: "CASE_TYPE",
            value: "NIA S138",
          },
        ],
      });
      setModalInfo({ type: "schedule", page: 0 });
    }
  };

  const handleMakeSubmission = () => {
    history.push(`/digit-ui/citizen/submissions/submissions-create?filingNumber=${filingNumber}`);
  };

  const handleCitizenAction = (option) => {
    if (option.value === "RAISE_APPLICATION") {
      history.push(`/digit-ui/citizen/submissions/submissions-create?filingNumber=${filingNumber}`);
    } else if (option.value === "SUBMIT_DOCUMENTS") {
      history.push(`/digit-ui/citizen/submissions/submit-document?filingNumber=${filingNumber}`);
    }
  };

  const handleSelect = (option) => {
    if (option === t("MAKE_SUBMISSION")) {
      history.push(`/digit-ui/employee/submissions/submissions-create?filingNumber=${filingNumber}&applicationType=DOCUMENT`);
      return;
    }
    if (option === t("SCHEDULE_HEARING")) {
      openHearingModule();
      return;
    } else if (option === t("REFER_TO_ADR")) {
      const reqBody = {
        order: {
          createdDate: null,
          tenantId,
          cnrNumber,
          filingNumber: filingNumber,
          statuteSection: {
            tenantId,
          },
          orderType: "REFERRAL_CASE_TO_ADR",
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
            formdata: {
              orderType: {
                type: "REFERRAL_CASE_TO_ADR",
                code: "REFERRAL_CASE_TO_ADR",
                name: "ORDER_TYPE_REFERRAL_CASE_TO_ADR",
              },
            },
          },
        },
      };
      ordersService
        .createOrder(reqBody, { tenantId })
        .then((res) => {
          history.push(`/${window.contextPath}/employee/orders/generate-orders?filingNumber=${filingNumber}&orderNumber=${res.order.orderNumber}`, {
            caseId: caseId,
            tab: activeTab,
          });
        })
        .catch((err) => {
          showToast({ isError: true, message: "ORDER_CREATION_FAILED" });
        });
      return;
    } else if (option === t("MANDATORY_SUBMISSIONS_RESPONSES")) {
      const reqBody = {
        order: {
          createdDate: null,
          tenantId,
          cnrNumber,
          filingNumber: filingNumber,
          statuteSection: {
            tenantId,
          },
          orderType: "MANDATORY_SUBMISSIONS_RESPONSES",
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
            formdata: {
              orderType: {
                type: "MANDATORY_SUBMISSIONS_RESPONSES",
                code: "MANDATORY_SUBMISSIONS_RESPONSES",
                name: "ORDER_TYPE_MANDATORY_SUBMISSIONS_RESPONSES",
              },
            },
          },
        },
      };
      ordersService
        .createOrder(reqBody, { tenantId })
        .then((res) => {
          history.push(`/${window.contextPath}/employee/orders/generate-orders?filingNumber=${filingNumber}&orderNumber=${res.order.orderNumber}`, {
            caseId: caseId,
            tab: activeTab,
          });
        })
        .catch((err) => {
          showToast({ isError: true, message: "ORDER_CREATION_FAILED" });
        });
      return;
    }
    history.push(`/${window.contextPath}/employee/orders/generate-orders?filingNumber=${filingNumber}`, { caseId: caseId, tab: "Orders" });
  };

  const showToast = (details, duration = 5000) => {
    setToast(true);
    setToastDetails(details);
    setTimeout(() => {
      setToast(false);
      setToastStatus({ alreadyShown: true });
    }, duration);
  };

  const handleDownload = (filestoreId) => {
    if (filestoreId) {
      downloadPdf(tenantId, filestoreId);
    }
  };
  const handleOrdersTab = () => {
    if (history.location?.state?.orderObj) {
      history.push(`/${window.contextPath}/${userType}/dristi/home/view-case?caseId=${caseId}&filingNumber=${filingNumber}&tab=Orders`);
    } else {
      setShowOrderReviewModal(false);
    }
  };

  const handleExtensionRequest = (orderNumber) => {
    history.push(`/digit-ui/citizen/submissions/submissions-create?filingNumber=${filingNumber}&orderNumber=${orderNumber}&isExtension=true`);
  };
  const handleSubmitDocument = (orderNumber) => {
    history.push(`/digit-ui/citizen/submissions/submissions-create?filingNumber=${filingNumber}&orderNumber=${orderNumber}`);
  };

  const openHearingModule = () => {
    setShowScheduleHearingModal(true);
    if (!isCaseAdmitted) {
      setCreateAdmissionOrder(true);
    }
  };

  const handleActionModal = () => {
    updateCaseDetails("REJECT").then(() => {
      history.push(`/${window.contextPath}/employee/home/home-pending-task`);
    });
  };

  const caseAdmittedSubmit = (data) => {
    const dateArr = data.date.split(" ").map((date, i) => (i === 0 ? date.slice(0, date.length - 2) : date));
    const date = new Date(dateArr.join(" "));
    const reqBody = {
      order: {
        createdDate: null,
        tenantId,
        cnrNumber,
        filingNumber: filingNumber,
        statuteSection: {
          tenantId,
        },
        orderType: "SCHEDULE_OF_HEARING_DATE",
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
          formdata: {
            hearingDate: formatDate(date).split("-").reverse().join("-"),
            hearingPurpose: data.purpose,
            orderType: {
              code: "SCHEDULE_OF_HEARING_DATE",
              type: "SCHEDULE_OF_HEARING_DATE",
              name: "ORDER_TYPE_SCHEDULE_OF_HEARING_DATE",
            },
          },
        },
      },
    };
    ordersService
      .createOrder(reqBody, { tenantId })
      .then(async (res) => {
        await DRISTIService.customApiService(Urls.dristi.pendingTask, {
          pendingTask: {
            name: `Draft in Progress for ${t(data.purpose?.code)} Hearing Order`,
            entityType: "order-default",
            referenceId: `MANUAL_${res.order.orderNumber}`,
            status: "DRAFT_IN_PROGRESS",
            assignedTo: [],
            assignedRole: ["JUDGE_ROLE"],
            cnrNumber: updatedCaseDetails?.cnrNumber,
            filingNumber: caseDetails?.filingNumber,
            isCompleted: false,
            stateSla: todayDate + stateSla.SCHEDULE_HEARING,
            additionalDetails: {},
            tenantId,
          },
        });
        await DRISTIService.customApiService(Urls.dristi.pendingTask, {
          pendingTask: {
            name: "Pending Response",
            entityType: "case-default",
            referenceId: `MANUAL_${caseDetails?.filingNumber}`,
            status: "PENDING_RESPONSE",
            assignedRole: ["CASE_RESPONDER"],
            cnrNumber: caseDetails?.cnrNumber,
            filingNumber: caseDetails?.filingNumber,
            isCompleted: true,
            tenantId,
          },
        });
        refetchCaseData();
        revalidateWorkflow();
        history.push(`/${window.contextPath}/employee/orders/generate-orders?filingNumber=${filingNumber}&orderNumber=${res.order.orderNumber}`);
      })
      .catch((err) => {
        showToast({ isError: true, message: "ORDER_CREATION_FAILED" });
      });
  };

  const citizenActionOptions = useMemo(
    () => [
      {
        value: "RAISE_APPLICATION",
        label: "Raise Application",
      },
      {
        value: "SUBMIT_DOCUMENTS",
        label: "Submit Documents",
      },
    ],
    []
  );

  const takeActionOptions = useMemo(
    () => [
      ...(userRoles?.includes("SUBMISSION_CREATOR") ? [t("MAKE_SUBMISSION")] : []),
      t("GENERATE_ORDER_HOME"),
      t("SCHEDULE_HEARING"),
      t("REFER_TO_ADR"),
    ],
    [t, userRoles]
  );

  const showActionBar = useMemo(
    () =>
      (primaryAction.action ||
        secondaryAction.action ||
        tertiaryAction.action ||
        ([CaseWorkflowState.ADMISSION_HEARING_SCHEDULED, CaseWorkflowState.PENDING_NOTICE, CaseWorkflowState.PENDING_RESPONSE].includes(
          caseDetails?.status
        ) &&
          !isCitizen)) &&
      !isCourtRoomManager,
    [caseDetails, primaryAction.action, secondaryAction.action, tertiaryAction.action, isCitizen]
  );

  const handleOpenSummonNoticeModal = async () => {
    if (currentHearingId) {
      history.push(`${path}?filingNumber=${filingNumber}&caseId=${caseId}&taskOrderType=NOTICE&hearingId=${currentHearingId}&tab=${config?.label}`);
    }
  };

  const handleDownloadPDF = async () => {
    const caseId = caseDetails?.id;
    try {
      setDownloadCasePdfLoading(true);
      if (!caseId) {
        throw new Error("Case ID is not available.");
      }
      const response = await DRISTIService.downloadCaseBundle({ tenantId, caseId }, { tenantId });
      const fileStoreId = response?.fileStoreId?.toLowerCase();
      if (!fileStoreId || ["null", "undefined"].includes(fileStoreId)) {
        throw new Error("Invalid fileStoreId received in the response.");
      }
      downloadPdf(tenantId, response?.fileStoreId);
    } catch (error) {
      console.error("Error downloading PDF: ", error.message || error);
      showToast({
        isError: true,
        message: "UNABLE_CASE_PDF",
      });
    } finally {
      setDownloadCasePdfLoading(false);
    }
  };

  if (isLoading || isWorkFlowLoading || isApplicationLoading || isCaseFetching) {
    return <Loader />;
  }
  if (
    (userRoles?.includes("JUDGE_ROLE") || userRoles?.includes("BENCH_CLERK") || userRoles?.includes("COURT_ROOM_MANAGER")) &&
    !judgeReviewStages.includes(caseData?.criteria?.[0]?.responseList?.[0]?.status)
  ) {
    history.push(`/${window.contextPath}/employee/home/home-pending-task`);
  }

  return (
    <div className="admitted-case" style={{ position: "absolute", width: "100%" }}>
      {downloadCasePdfLoading && (
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
      <div
        className="admitted-case-header"
        style={{ position: "sticky", top: "72px", width: "100%", height: "100%", zIndex: 150, background: "white" }}
      >
        <div className="admitted-case-details" style={{ display: "flex", justifyContent: "space-between", alignItems: "center", padding: "10px" }}>
          <div className="case-details-title" style={{ display: "flex", alignItems: "center", gap: "12px" }}>
            <Header>{caseDetails?.caseTitle || ""}</Header>
            {statue && (
              <React.Fragment>
                <hr className="vertical-line" />
                <div className="sub-details-text">{statue}</div>
              </React.Fragment>
            )}
            <hr className="vertical-line" />
            <div className="sub-details-text">{t(caseDetails?.stage)}</div>
            <hr className="vertical-line" />
            <div className="sub-details-text">{t(caseDetails?.substage)}</div>
            <hr className="vertical-line" />
            {caseDetails?.outcome && (
              <React.Fragment>
                <div className="sub-details-text">{t(caseDetails?.outcome)}</div>
                <hr className="vertical-line" />
              </React.Fragment>
            )}
            <div className="sub-details-text">Code: {caseDetails?.accessCode}</div>
            {delayCondonationData?.delayCondonationType?.code === "NO" && isJudge && (
              <div className="delay-condonation-chip" style={delayCondonationStylsMain}>
                <p style={delayCondonationTextStyle}>
                  {(delayCondonationData?.isDcaSkippedInEFiling?.code === "NO" && isDelayApplicationPending) ||
                  isDelayApplicationPending ||
                  isDelayApplicationCompleted
                    ? t("DELAY_CONDONATION_FILED")
                    : t("DELAY_CONDONATION_NOT_FILED")}
                </p>
              </div>
            )}
          </div>
          <div className="make-submission-action" style={{ display: "flex", gap: 20, justifyContent: "space-between", alignItems: "center" }}>
            {isCitizen && (
              <Button
                variation={"outlined"}
                label={t("DOWNLOAD_CASE_FILE")}
                isDisabled={!caseDetails?.additionalDetails?.signedCaseDocument}
                onButtonClick={handleDownloadPDF}
              />
            )}
            {showMakeSubmission && (
              <div className="evidence-header-wrapper">
                <div className="evidence-hearing-header" style={{ background: "transparent" }}>
                  <div className="evidence-actions" style={{ ...(isTabDisabled ? { pointerEvents: "none" } : {}) }}>
                    <ActionButton
                      variation={"primary"}
                      label={t("CS_CASE_MAKE_FILINGS")}
                      icon={showMenu ? "ExpandLess" : "ExpandMore"}
                      isSuffix={true}
                      onClick={handleTakeAction}
                      className={"take-action-btn-class"}
                    ></ActionButton>
                    {showMenu && (
                      <Menu
                        t={t}
                        optionKey={"label"}
                        localeKeyPrefix={"CS_CASE"}
                        options={citizenActionOptions}
                        onSelect={(option) => handleCitizenAction(option)}
                      ></Menu>
                    )}
                  </div>
                </div>
              </div>
            )}
          </div>
          {showTakeAction && (
            <div className="judge-action-block" style={{ display: "flex" }}>
              {!isCourtRoomManager && (
                <div className="evidence-header-wrapper">
                  <div className="evidence-hearing-header" style={{ background: "transparent" }}>
                    <div className="evidence-actions" style={{ ...(isTabDisabled ? { pointerEvents: "none" } : {}) }}>
                      <ActionButton
                        variation={"primary"}
                        label={t("TAKE_ACTION_LABEL")}
                        icon={showMenu ? "ExpandLess" : "ExpandMore"}
                        isSuffix={true}
                        onClick={handleTakeAction}
                        className={"take-action-btn-class"}
                      ></ActionButton>
                      {showMenu && <Menu options={takeActionOptions} onSelect={(option) => handleSelect(option)}></Menu>}
                    </div>
                  </div>
                </div>
              )}

              <div className="evidence-header-wrapper">
                <div className="evidence-hearing-header" style={{ background: "transparent" }}>
                  <div className="evidence-actions">
                    <div
                      className="custom-icon-wrapper"
                      onClick={() => {
                        setShowOtherMenu((prev) => !prev);
                        setShowMenu(false);
                      }}
                    >
                      <CustomThreeDots />
                      {showOtherMenu && <Menu options={[t("DOWNLOAD_CASE_FILE")]} onSelect={handleDownloadPDF}></Menu>}
                    </div>
                  </div>
                </div>
              </div>
            </div>
          )}
        </div>
        {noticeFailureCount > 0 && !isCaseAdmitted && isJudge && (
          <div className="notice-failed-notification" style={styles.container}>
            <div className="notice-failed-icon" style={styles.icon}>
              <InfoIconRed style={styles.icon} />
            </div>
            <p className="notice-failed-text" style={styles.text}>
              {`${t("NOTICE_FAILED")} ${noticeFailureCount} ${t("TIMES_VIEW_STATUS")} `}
              <span onClick={() => handleOpenSummonNoticeModal()} className="click-here" style={styles.link}>
                {t("NOTICE_CLICK_HERE")}
              </span>
            </p>
          </div>
        )}

        <CustomCaseInfoDiv t={t} data={caseBasicDetails} column={6} />
        <div className="search-tabs-container">
          <div>
            {tabData?.map((i, num) => (
              <button
                className={i?.active === true ? "search-tab-head-selected" : "search-tab-head"}
                onClick={() => {
                  onTabChange(num);
                }}
                disabled={["Complaint", "Overview"].includes(i?.label) ? false : isTabDisabled}
              >
                {t(i?.displayLabel)}
              </button>
            ))}
          </div>
        </div>
      </div>
      {config?.label !== "Overview" && (
        <ExtraComponent
          caseData={caseRelatedData}
          setUpdateCounter={setUpdateCounter}
          tab={config?.label}
          setOrderModal={openDraftModal}
          openSubmissionsViewModal={openSubmissionViewModal}
        />
      )}
      {config?.label !== "Overview" && config?.label !== "Complaint" && config?.label !== "History" && (
        <div style={{ width: "100%", background: "white", padding: "10px", display: "flex", justifyContent: "space-between" }}>
          <div style={{ fontWeight: 700, fontSize: "24px", lineHeight: "28.8px" }}>{t(`All_${config?.label.toUpperCase()}_TABLE_HEADER`)}</div>
          {/* {(!userRoles.includes("CITIZENS") || userRoles.includes("ADVOCATE_ROLE")) &&
            (config?.label === "Hearings" || config?.label === "Documents") && (
              <div style={{ fontWeight: 500, fontSize: "16px", lineHeight: "20px", color: "#0A5757", cursor: "pointer" }}>
                {t("DOWNLOAD_ALL_LINK")}
              </div>
            )} */}
          {userRoles.includes("ORDER_CREATOR") && config?.label === "Orders" && (
            <div style={{ display: "flex", gap: "10px" }}>
              <div
                onClick={() => handleSelect(t("GENERATE_ORDER_HOME"))}
                style={{ fontWeight: 500, fontSize: "16px", lineHeight: "20px", color: "#0A5757", cursor: "pointer" }}
              >
                {t("GENERATE_ORDERS_LINK")}
              </div>
              {/* <div style={{ fontWeight: 500, fontSize: "16px", lineHeight: "20px", color: "#0A5757", cursor: "pointer" }}>
                {t("DOWNLOAD_ALL_LINK")}
              </div> */}
            </div>
          )}
          {userRoles.includes("ORDER_CREATOR") && config?.label === "Submissions" && (
            <div style={{ display: "flex", gap: "10px" }}>
              <div
                onClick={() => handleSelect(t("MANDATORY_SUBMISSIONS_RESPONSES"))}
                style={{ fontWeight: 500, fontSize: "16px", lineHeight: "20px", color: "#0A5757", cursor: "pointer" }}
              >
                {t("REQUEST_DOCUMENTS_LINK")}
              </div>
              {/* <div style={{ fontWeight: 500, fontSize: "16px", lineHeight: "20px", color: "#0A5757", cursor: "pointer" }}>
                {t("DOWNLOAD_ALL_LINK")}
              </div> */}
            </div>
          )}
          {isCitizen && config?.label === "Submissions" && (
            <div style={{ display: "flex", gap: "10px" }}>
              {showMakeSubmission && (
                <div
                  onClick={handleMakeSubmission}
                  style={{ fontWeight: 500, fontSize: "16px", lineHeight: "20px", color: "#0A5757", cursor: "pointer" }}
                >
                  {t("MAKE_SUBMISSION")}
                </div>
              )}

              {/* <div style={{ fontWeight: 500, fontSize: "16px", lineHeight: "20px", color: "#0A5757", cursor: "pointer" }}>
                {t("DOWNLOAD_ALL_LINK")}
              </div> */}
            </div>
          )}
        </div>
      )}
      <div className="inbox-search-wrapper" style={showActionBar && !isWorkFlowFetching ? { marginBottom: "56px" } : {}}>
        {/* Pass defaultValues as props to InboxSearchComposer */}
        <InboxSearchComposer
          key={`${config?.label}-${updateCounter}`}
          configs={config}
          defaultValues={defaultValues}
          showTab={false}
          tabData={tabData}
          onTabChange={onTabChange}
        ></InboxSearchComposer>
      </div>
      {tabData?.filter((tab) => tab.label === "Overview")?.[0]?.active && (
        <div className="case-overview-wrapper">
          <CaseOverview
            handleDownload={handleDownload}
            handleExtensionRequest={handleExtensionRequest}
            handleSubmitDocument={handleSubmitDocument}
            openHearingModule={openHearingModule}
            caseData={caseRelatedData}
            setUpdateCounter={setUpdateCounter}
            showToast={showToast}
            t={t}
            order={currentOrder}
            caseStatus={caseStatus}
            extensionApplications={extensionApplications}
            productionOfDocumentApplications={productionOfDocumentApplications}
          />
        </div>
      )}
      {tabData?.filter((tab) => tab.label === "Complaint")?.[0]?.active && (
        <div className="view-case-file-wrapper">
          <ViewCaseFile t={t} inViewCase={true} />
        </div>
      )}
      {show && (
        <EvidenceModal
          documentSubmission={documentSubmission}
          show={show}
          setShow={setShow}
          userRoles={userRoles}
          modalType={tabData?.filter((tab) => tab.active)?.[0]?.label}
          setUpdateCounter={setUpdateCounter}
          showToast={showToast}
          caseData={caseRelatedData}
          caseId={caseId}
          setIsDelayApplicationPending={setIsDelayApplicationPending}
        />
      )}
      {showOrderReviewModal && (
        <PublishedOrderModal
          t={t}
          order={currentOrder}
          handleDownload={handleDownload}
          handleRequestLabel={handleExtensionRequest}
          handleSubmitDocument={handleSubmitDocument}
          extensionApplications={extensionApplications}
          productionOfDocumentApplications={productionOfDocumentApplications}
          caseStatus={caseStatus}
          handleOrdersTab={handleOrdersTab}
        />
      )}
      {showHearingTranscriptModal && (
        <HearingTranscriptModal t={t} hearing={currentHearing} setShowHearingTranscriptModal={setShowHearingTranscriptModal} />
      )}
      {showScheduleHearingModal && (
        <ScheduleHearing
          setUpdateCounter={setUpdateCounter}
          showToast={showToast}
          tenantId={tenantId}
          caseData={caseRelatedData}
          setShowModal={setShowScheduleHearingModal}
          caseAdmittedSubmit={caseAdmittedSubmit}
          isCaseAdmitted={isCaseAdmitted}
          createAdmissionOrder={createAdmissionOrder}
          delayCondonationData={delayCondonationData}
          hearingDetails={hearingDetails}
          isDelayApplicationPending={isDelayApplicationPending}
          isDelayApplicationCompleted={isDelayApplicationCompleted}
        />
      )}
      {orderDraftModal && <ViewAllOrderDrafts t={t} setShow={setOrderDraftModal} draftOrderList={draftOrderList} filingNumber={filingNumber} />}
      {submissionsViewModal && (
        <ViewAllSubmissions
          t={t}
          setShow={setSubmissionsViewModal}
          submissionList={submissionsViewList.list}
          openEvidenceModal={submissionsViewList.func}
          filingNumber={filingNumber}
        />
      )}
      {toast && toastDetails && (
        <Toast error={toastDetails?.isError} label={t(toastDetails?.message)} onClose={() => setToast(false)} style={{ maxWidth: "670px" }} />
      )}
      {showActionBar && !isWorkFlowFetching && (
        <ActionBar className={"e-filing-action-bar"} style={{ justifyContent: "space-between" }}>
          <div style={{ width: "fit-content", display: "flex", gap: 20 }}>
            {currentHearingStatus === HearingWorkflowState.SCHEDULED &&
              (tertiaryAction.action ||
                [CaseWorkflowState.ADMISSION_HEARING_SCHEDULED, CaseWorkflowState.PENDING_NOTICE, CaseWorkflowState.PENDING_RESPONSE].includes(
                  caseDetails?.status
                )) && (
                <Button
                  className="previous-button"
                  variation="secondary"
                  label={
                    [CaseWorkflowState.ADMISSION_HEARING_SCHEDULED, CaseWorkflowState.PENDING_NOTICE, CaseWorkflowState.PENDING_RESPONSE].includes(
                      caseDetails?.status
                    ) && !isCitizen
                      ? t("RESCHEDULE_ADMISSION_HEARING")
                      : t(tertiaryAction.label)
                  }
                  onButtonClick={onSaveDraft}
                />
              )}
            {primaryAction.action && !isDcaHearingScheduled && (
              <SubmitBar
                label={t(
                  [CaseWorkflowState.ADMISSION_HEARING_SCHEDULED].includes(caseDetails?.status) && primaryAction?.action === "ADMIT"
                    ? "ISSUE_NOTICE"
                    : primaryAction?.label
                )}
                submit="submit"
                disabled={""}
                onSubmit={onSubmit}
              />
            )}
          </div>
          {secondaryAction.action && (
            <Button
              className="previous-button"
              variation="secondary"
              style={{
                border: "none",
                marginLeft: 0,
                fontSize: 16,
                fontWeight: 700,
                color: secondaryAction.action === "REJECT" && "#BB2C2F",
              }}
              label={t(secondaryAction.label)}
              onButtonClick={onSendBack}
            />
          )}
        </ActionBar>
      )}
      {isOpenDCA && <DocumentModal config={dcaConfirmModalConfig} />}

      {showModal && (
        <AdmissionActionModal
          t={t}
          setShowModal={setShowModal}
          setSubmitModalInfo={setSubmitModalInfo}
          submitModalInfo={submitModalInfo}
          modalInfo={modalInfo}
          setModalInfo={setModalInfo}
          handleSendCaseBack={handleSendCaseBack}
          handleAdmitCase={handleAdmitCase}
          path={path}
          handleScheduleCase={handleScheduleCase}
          updatedConfig={updatedConfig}
          tenantId={tenantId}
          handleScheduleNextHearing={handleScheduleNextHearing}
          caseAdmitLoader={caseAdmitLoader}
          caseDetails={caseDetails}
          isAdmissionHearingAvailable={Boolean(currentHearingId)}
          setOpenAdmitCaseModal={setOpenAdmitCaseModal}
          delayCondonationData={delayCondonationData}
          hearingDetails={hearingDetails}
          isDelayApplicationPending={isDelayApplicationPending}
          isDelayApplicationCompleted={isDelayApplicationCompleted}
        ></AdmissionActionModal>
      )}
      {showDismissCaseConfirmation && (
        <Modal
          headerBarMain={<Heading label={t("DISMISS_CASE_CONFIRMATION")} />}
          headerBarEnd={
            <CloseBtn
              onClick={() => {
                setShowDismissCaseConfirmation(false);
              }}
            />
          }
          actionSaveLabel={t("CS_DISMISS")}
          actionCancelLabel={t("CS_BACK")}
          actionCancelOnSubmit={() => {
            setShowDismissCaseConfirmation(false);
          }}
          style={{
            backgroundColor: "#BB2C2F",
          }}
          children={<div style={{ margin: "16px 0px" }}>{t("DISMISS_CASE_CONFIRMATION_TEXT")}</div>}
          actionSaveOnSubmit={() => {
            handleActionModal();
          }}
        ></Modal>
      )}
      {showPendingDelayApplication && (
        <Modal
          headerBarMain={<Heading label={t("PENDING_DELAY_CONDONATION_HEADER")} />}
          headerBarEnd={
            <CloseBtn
              onClick={() => {
                setShowPendingDelayApplication(false);
              }}
            />
          }
          actionSaveLabel={t("CS_CLOSE")}
          children={<div style={{ margin: "16px 0px" }}>{t("PENDING_DELAY_CONDONATION_APPLICATION_TEXT")}</div>}
          actionSaveOnSubmit={() => {
            setShowPendingDelayApplication(false);
          }}
        ></Modal>
      )}
      {taskOrderType && hearingId && (
        <SummonsAndWarrantsModal
          handleClose={() => {
            history.push(`${path}?filingNumber=${filingNumber}&caseId=${caseId}&tab=${config?.label}`);
          }}
        />
      )}
      {showVoidModal && <DocumentModal config={voidModalConfig} />}
    </div>
  );
};

export default AdmittedCases;
