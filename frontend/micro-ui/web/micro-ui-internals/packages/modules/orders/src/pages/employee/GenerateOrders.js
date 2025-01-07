import React, { useEffect, useMemo, useRef, useState } from "react";
import { useTranslation } from "react-i18next";
import { useHistory } from "react-router-dom";
import ReactTooltip from "react-tooltip";
import { Header, FormComposerV2, Toast } from "@egovernments/digit-ui-react-components";
import {
  applicationTypeConfig,
  configCheckout,
  configRejectSubmission,
  configsAssignDateToRescheduledHearing,
  configsAssignNewHearingDate,
  configsBail,
  configsCaseSettlement,
  configsCaseTransfer,
  configsCaseWithdrawal,
  configsCreateOrderWarrant,
  configsInitiateRescheduleHearingDate,
  configsIssueNotice,
  configsIssueSummons,
  configsJudgement,
  configsOrderMandatorySubmissions,
  configsOrderSection202CRPC,
  configsOrderSubmissionExtension,
  configsOrderTranferToADR,
  configsOthers,
  configsRejectCheckout,
  configsRejectRescheduleHeadingDate,
  configsRescheduleHearingDate,
  configsScheduleHearingDate,
  configsScheduleNextHearingDate,
  configsVoluntarySubmissionStatus,
  configsIssueBailAcceptance,
  configsIssueBailReject,
  configsSetTermBail,
  configsAcceptRejectDelayCondonation,
  configsAdmitDismissCase,
} from "../../configs/ordersCreateConfig";
import { CustomDeleteIcon, WarningInfoIconYellow } from "../../../../dristi/src/icons/svgIndex";
import OrderReviewModal from "../../pageComponents/OrderReviewModal";
import OrderSignatureModal from "../../pageComponents/OrderSignatureModal";
import OrderDeleteModal from "../../pageComponents/OrderDeleteModal";
import { ordersService, schedulerService } from "../../hooks/services";
import { Loader } from "@egovernments/digit-ui-components";
import OrderSucessModal from "../../pageComponents/OrderSucessModal";
import { applicationTypes } from "../../utils/applicationTypes";
import isEqual from "lodash/isEqual";
import { OrderWorkflowAction, OrderWorkflowState } from "../../utils/orderWorkflow";
import { Urls } from "../../hooks/services/Urls";
import { SubmissionWorkflowAction, SubmissionWorkflowState } from "../../utils/submissionWorkflow";
import { getAdvocates, getuuidNameMap } from "../../utils/caseUtils";
import { HearingWorkflowAction, HearingWorkflowState } from "../../utils/hearingWorkflow";
import _ from "lodash";
import { useGetPendingTask } from "../../hooks/orders/useGetPendingTask";
import useSearchOrdersService from "../../hooks/orders/useSearchOrdersService";
import { DRISTIService } from "@egovernments/digit-ui-module-dristi/src/services";
import { constructFullName, removeInvalidNameParts } from "../../utils";

const configKeys = {
  SECTION_202_CRPC: configsOrderSection202CRPC,
  MANDATORY_SUBMISSIONS_RESPONSES: configsOrderMandatorySubmissions,
  EXTENSION_OF_DOCUMENT_SUBMISSION_DATE: configsOrderSubmissionExtension,
  REFERRAL_CASE_TO_ADR: configsOrderTranferToADR,
  SCHEDULE_OF_HEARING_DATE: configsScheduleHearingDate,
  SCHEDULING_NEXT_HEARING: configsScheduleNextHearingDate,
  RESCHEDULE_OF_HEARING_DATE: configsRescheduleHearingDate,
  CHECKOUT_ACCEPTANCE: configCheckout,
  CHECKOUT_REJECT: configsRejectCheckout,
  REJECTION_RESCHEDULE_REQUEST: configsRejectRescheduleHeadingDate,
  INITIATING_RESCHEDULING_OF_HEARING_DATE: configsInitiateRescheduleHearingDate,
  ASSIGNING_DATE_RESCHEDULED_HEARING: configsAssignDateToRescheduledHearing,
  ASSIGNING_NEW_HEARING_DATE: configsAssignNewHearingDate,
  CASE_TRANSFER: configsCaseTransfer,
  SETTLEMENT: configsCaseSettlement,
  SUMMONS: configsIssueSummons,
  NOTICE: configsIssueNotice,
  BAIL: configsBail,
  WARRANT: configsCreateOrderWarrant,
  WITHDRAWAL: configsCaseWithdrawal,
  OTHERS: configsOthers,
  APPROVE_VOLUNTARY_SUBMISSIONS: configsVoluntarySubmissionStatus,
  REJECT_VOLUNTARY_SUBMISSIONS: configRejectSubmission,
  JUDGEMENT: configsJudgement,
  REJECT_BAIL: configsIssueBailReject,
  ACCEPT_BAIL: configsIssueBailAcceptance,
  SET_BAIL_TERMS: configsSetTermBail,
  ACCEPTANCE_REJECTION_DCA: configsAcceptRejectDelayCondonation,
  ADMIT_DISMISS_CASE: configsAdmitDismissCase,
};

function applyMultiSelectDropdownFix(setValue, formData, keys) {
  keys.forEach((key) => {
    if (formData[key] && Array.isArray(formData[key]) && formData[key].length === 0) {
      setValue(key, undefined);
    }
  });
}

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

const stateSlaMap = {
  SECTION_202_CRPC: 3,
  MANDATORY_SUBMISSIONS_RESPONSES: 3,
  EXTENSION_OF_DOCUMENT_SUBMISSION_DATE: 3,
  REFERRAL_CASE_TO_ADR: 3,
  SCHEDULE_OF_HEARING_DATE: 3,
  RESCHEDULE_OF_HEARING_DATE: 3,
  REJECTION_RESCHEDULE_REQUEST: 3,
  APPROVAL_RESCHEDULE_REQUEST: 3,
  INITIATING_RESCHEDULING_OF_HEARING_DATE: 1,
  ASSIGNING_DATE_RESCHEDULED_HEARING: 3,
  ASSIGNING_NEW_HEARING_DATE: 3,
  CASE_TRANSFER: 3,
  SETTLEMENT: 3,
  SUMMONS: 3,
  NOTICE: 3,
  BAIL: 3,
  WARRANT: 3,
  WITHDRAWAL: 3,
  OTHERS: 3,
  APPROVE_VOLUNTARY_SUBMISSIONS: 3,
  REJECT_VOLUNTARY_SUBMISSIONS: 3,
  REJECT_BAIL: 3,
  ACCEPT_BAIL: 3,
  SET_BAIL_TERMS: 3,
  JUDGEMENT: 3,
  CHECKOUT_ACCEPTANCE: 1,
  CHECKOUT_REJECT: 1,
};

const channelTypeEnum = {
  "e-Post": { code: "POST", type: "Post" },
  "Registered Post": { code: "RPAD", type: "RPAD" },
  SMS: { code: "SMS", type: "SMS" },
  "Via Police": { code: "POLICE", type: "Police" },
  "E-mail": { code: "EMAIL", type: "Email" },
};

const dayInMillisecond = 24 * 3600 * 1000;

const GenerateOrders = () => {
  const { t } = useTranslation();
  const { orderNumber, filingNumber } = Digit.Hooks.useQueryParams();
  const tenantId = Digit.ULBService.getCurrentTenantId();
  const [selectedOrder, _setSelectedOrder] = useState(0);
  const [deleteOrderIndex, setDeleteOrderIndex] = useState(null);
  const [showReviewModal, setShowReviewModal] = useState(false);
  const [showsignatureModal, setShowsignatureModal] = useState(null);
  const [showSuccessModal, setShowSuccessModal] = useState(false);
  const [formList, setFormList] = useState([]);
  const [prevOrder, setPrevOrder] = useState();
  const [isSubmitDisabled, setIsSubmitDisabled] = useState(false);
  const [showErrorToast, setShowErrorToast] = useState(null);
  const [loader, setLoader] = useState(false);
  const [createdHearing, setCreatedHearing] = useState({});
  const [signedDoucumentUploadedID, setSignedDocumentUploadID] = useState("");
  const [newHearingNumber, setNewHearingNumber] = useState(null);
  const [createdSummon, setCreatedSummon] = useState(null);
  const [createdNotice, setCreatedNotice] = useState(null);
  const [orderPdfFileStoreID, setOrderPdfFileStoreID] = useState(null);
  const history = useHistory();
  const todayDate = new Date().getTime();
  const setFormErrors = useRef(null);
  const [currentFormData, setCurrentFormData] = useState(null);
  const roles = Digit.UserService.getUser()?.info?.roles;
  const canESign = roles?.some((role) => role.code === "ORDER_ESIGN");
  const { downloadPdf } = Digit.Hooks.dristi.useDownloadCasePdf();
  const judgeName = window?.globalConfigs?.getConfig("JUDGE_NAME");
  const setSelectedOrder = (orderIndex) => {
    _setSelectedOrder(orderIndex);
  };

  const closeToast = () => {
    setShowErrorToast(null);
  };
  const { data: courtRoomDetails, isLoading: isCourtIdsLoading } = Digit.Hooks.dristi.useGetStatuteSection("common-masters", [
    { name: "Court_Rooms" },
  ]);
  const courtRooms = useMemo(() => courtRoomDetails?.Court_Rooms || [], [courtRoomDetails]);

  const { data: courtFeeAmount, isLoading: isLoadingCourtFeeData } = Digit.Hooks.useCustomMDMS(
    Digit.ULBService.getStateId(),
    "payment",
    [{ name: "courtFeePayment" }],
    {
      select: (data) => {
        return data?.payment?.courtFeePayment || [];
      },
    }
  );
  const summonsCourtFee = useMemo(() => courtFeeAmount?.find((p) => p?.paymentCode === "SUMMONS_COURT_FEE")?.amount || 0, [courtFeeAmount]);

  const { data: caseData, isLoading: isCaseDetailsLoading, refetch: refetchCaseData } = Digit.Hooks.dristi.useSearchCaseService(
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
  const userInfo = Digit.UserService.getUser()?.info;

  const caseDetails = useMemo(
    () => ({
      ...caseData?.criteria?.[0]?.responseList?.[0],
    }),
    [caseData]
  );

  const { data: courtRoomData } = Digit.Hooks.useCustomMDMS(Digit.ULBService.getStateId(), "common-masters", [{ name: "Court_Rooms" }], {
    select: (data) => {
      let newData = {};
      [{ name: "Court_Rooms" }]?.forEach((master) => {
        const optionsData = _.get(data, `${"common-masters"}.${master?.name}`, []);
        newData = {
          ...newData,
          [master.name]: optionsData.filter((opt) => (opt?.hasOwnProperty("active") ? opt.active : true)).map((opt) => ({ ...opt })),
        };
      });
      return newData;
    },
  });

  const { data: orderTypeData } = Digit.Hooks.useCustomMDMS(Digit.ULBService.getStateId(), "Order", [{ name: "OrderType" }], {
    select: (data) => {
      return _.get(data, "Order.OrderType", [])
        .filter((opt) => (opt?.hasOwnProperty("isactive") ? opt.isactive : true))
        .map((opt) => ({ ...opt }));
    },
  });

  const cnrNumber = useMemo(() => caseDetails?.cnrNumber, [caseDetails]);
  const allAdvocates = useMemo(() => getAdvocates(caseDetails), [caseDetails]);
  const uuidNameMap = useMemo(() => getuuidNameMap(caseDetails), [caseDetails]);
  const isCaseAdmitted = useMemo(() => {
    return caseDetails?.status === "CASE_ADMITTED";
  }, [caseDetails?.status]);

  const complainants = useMemo(() => {
    return (
      caseDetails?.litigants
        ?.filter((item) => item?.partyType?.includes("complainant"))
        .map((item) => {
          const fullName = removeInvalidNameParts(item?.additionalDetails?.fullName);
          return {
            code: fullName,
            name: `${fullName} (Complainant)`,
            uuid: allAdvocates[item?.additionalDetails?.uuid],
            individualId: item?.individualId,
            isJoined: true,
            partyType: "complainant",
          };
        }) || []
    );
  }, [caseDetails, allAdvocates]);

  const respondents = useMemo(() => {
    return (
      caseDetails?.litigants
        ?.filter((item) => item?.partyType?.includes("respondent"))
        .map((item) => {
          const fullName = removeInvalidNameParts(item?.additionalDetails?.fullName);
          return {
            code: fullName,
            name: `${fullName} (Accused)`,
            uuid: allAdvocates[item?.additionalDetails?.uuid],
            isJoined: true,
            partyType: "respondent",
          };
        }) || []
    );
  }, [caseDetails, allAdvocates]);

  const unJoinedLitigant = useMemo(() => {
    return (
      caseDetails?.additionalDetails?.respondentDetails?.formdata
        ?.filter((data) => !data?.data?.respondentVerification?.individualDetails?.individualId)
        ?.map((data) => {
          const fullName = constructFullName(data?.data?.respondentFirstName, data?.data?.respondentMiddleName, data?.data?.respondentLastName);
          return { code: fullName, name: `${fullName} (Accused)`, uuid: data?.data?.uuid, isJoined: false, partyType: "respondent" };
        }) || []
    );
  }, [caseDetails]);

  const witnesses = useMemo(() => {
    return (
      caseDetails?.additionalDetails?.witnessDetails?.formdata?.map((data) => {
        const fullName = constructFullName(data?.data?.firstName, data?.data?.middleName, data?.data?.lastName);
        return { code: fullName, name: `${fullName} (Witness)`, uuid: data?.data?.uuid, partyType: "witness" };
      }) || []
    );
  }, [caseDetails]);

  const allParties = useMemo(() => [...complainants, ...respondents, ...unJoinedLitigant, ...witnesses], [
    complainants,
    respondents,
    unJoinedLitigant,
    witnesses,
  ]);

  const { data: ordersData, refetch: refetchOrdersData, isLoading: isOrdersLoading, isFetching: isOrdersFetching } = useSearchOrdersService(
    {
      tenantId,
      criteria: { filingNumber, applicationNumber: "", cnrNumber, status: OrderWorkflowState.DRAFT_IN_PROGRESS },
      pagination: { limit: 1000, offset: 0 },
    },
    { tenantId },
    filingNumber + OrderWorkflowState.DRAFT_IN_PROGRESS,
    Boolean(filingNumber)
  );

  const { data: noticeOrdersData } = useSearchOrdersService(
    {
      tenantId,
      criteria: { filingNumber, applicationNumber: "", cnrNumber, orderType: "NOTICE", status: "PUBLISHED" },
      pagination: { limit: 1000, offset: 0 },
    },
    { tenantId },
    filingNumber,
    Boolean(filingNumber)
  );

  const isDCANoticeGenerated = useMemo(
    () => noticeOrdersData?.list?.some((notice) => "DCA Notice" === notice?.additionalDetails?.formdata?.noticeType.code),
    [noticeOrdersData]
  );

  const { data: publishedOrdersData, isLoading: isPublishedOrdersLoading } = useSearchOrdersService(
    {
      tenantId,
      criteria: { filingNumber, applicationNumber: "", cnrNumber, status: OrderWorkflowState.PUBLISHED },
      pagination: { limit: 1000, offset: 0 },
    },
    { tenantId },
    filingNumber + OrderWorkflowState.PUBLISHED,
    Boolean(filingNumber && cnrNumber)
  );
  const publishedBailOrder = useMemo(() => publishedOrdersData?.list?.find((item) => item?.orderType === "BAIL") || {}, [publishedOrdersData]);
  const advocateIds = caseDetails.representatives?.map((representative) => {
    return {
      id: representative.advocateId,
    };
  });

  const { data: advocateDetails } = Digit.Hooks.dristi.useGetIndividualAdvocate(
    {
      criteria: advocateIds,
    },
    { tenantId: tenantId },
    "DRISTI",
    cnrNumber + filingNumber,
    true
  );

  const defaultIndex = useMemo(() => {
    return formList.findIndex((order) => order?.orderNumber === orderNumber);
  }, [formList, orderNumber]);

  const defaultOrderData = useMemo(
    () => ({
      createdDate: null,
      tenantId,
      cnrNumber,
      filingNumber,
      statuteSection: {
        tenantId,
      },
      status: "",
      isActive: true,
      workflow: {
        action: OrderWorkflowAction.SAVE_DRAFT,
        comments: "Creating order",
        assignes: [],
        rating: null,
        documents: [{}],
      },
      documents: [],
      additionalDetails: { formdata: {} },
    }),
    [cnrNumber, filingNumber, tenantId]
  );
  const formatDate = (date, format) => {
    const day = String(date.getDate()).padStart(2, "0");
    const month = String(date.getMonth() + 1).padStart(2, "0");
    const year = date.getFullYear();
    if (format === "DD-MM-YYYY") {
      return `${day}-${month}-${year}`;
    }
    return `${year}-${month}-${day}`;
  };
  useEffect(() => {
    if (!ordersData?.list || ordersData?.list.length < 1) {
      setFormList([defaultOrderData]);
    } else {
      setFormList([...(ordersData?.list || [])].reverse());
    }
  }, [ordersData, defaultOrderData]);

  useEffect(() => {
    if (Boolean(filingNumber && cnrNumber)) {
      refetchOrdersData();
    }
  }, [cnrNumber, filingNumber, refetchOrdersData]);

  useEffect(() => {
    if (showErrorToast) {
      const timer = setTimeout(() => {
        setShowErrorToast(null);
      }, 2000);
      return () => clearTimeout(timer);
    }
  }, [showErrorToast]);

  useEffect(() => {
    if (defaultIndex && defaultIndex !== -1 && orderNumber && defaultIndex !== selectedOrder) {
      setSelectedOrder(defaultIndex);
    }
    const isSignSuccess = localStorage.getItem("esignProcess");
    const savedOrderPdf = localStorage.getItem("orderPDF");
    if (isSignSuccess) {
      setShowsignatureModal(true);
      setOrderPdfFileStoreID(savedOrderPdf);
      localStorage.removeItem("esignProcess");
      localStorage.removeItem("orderPDF");
    }
  }, [defaultIndex]);

  const currentOrder = useMemo(() => formList?.[selectedOrder], [formList, selectedOrder]);
  const orderType = useMemo(() => currentOrder?.orderType || {}, [currentOrder]);
  const referenceId = useMemo(() => currentOrder?.additionalDetails?.formdata?.refApplicationId, [currentOrder]);
  const hearingNumber = useMemo(() => currentOrder?.hearingNumber || currentOrder?.additionalDetails?.hearingId || "", [currentOrder]);

  const { data: pendingTaskData = [], isLoading: pendingTasksLoading } = useGetPendingTask({
    data: {
      SearchCriteria: {
        tenantId,
        moduleName: "Pending Tasks Service",
        moduleSearchCriteria: {
          filingNumber,
          isCompleted: false,
        },
        limit: 10000,
        offset: 0,
      },
    },
    params: { tenantId },
    key: filingNumber,
  });

  const pendingTaskDetails = useMemo(() => pendingTaskData?.data || [], [pendingTaskData]);
  const mandatorySubmissionTasks = useMemo(() => {
    const pendingtask = pendingTaskDetails?.filter((obj) =>
      obj.fields.some((field) => field.key === "referenceId" && field.value?.includes(currentOrder?.linkedOrderNumber))
    );
    if (pendingtask?.length > 0) {
      return pendingtask?.map((item) =>
        item?.fields.reduce((acc, field) => {
          if (field.key.startsWith("assignedTo[")) {
            const indexMatch = field.key.match(/assignedTo\[(\d+)\]\.uuid/);
            if (indexMatch) {
              const index = parseInt(indexMatch[1], 10);
              acc.assignedTo = acc.assignedTo || [];
              acc.assignedTo[index] = { uuid: field.value };
            }
          } else {
            acc[field.key] = field.value;
          }
          return acc;
        }, {})
      );
    }
    return [];
  }, [currentOrder?.linkedOrderNumber, pendingTaskDetails]);

  const { data: applicationData, isLoading: isApplicationDetailsLoading } = Digit.Hooks.submissions.useSearchSubmissionService(
    {
      criteria: {
        filingNumber: filingNumber,
        tenantId: tenantId,
        applicationNumber: referenceId,
      },
      tenantId,
    },
    {},
    referenceId,
    referenceId
  );

  const { data: completeApplicationData, isLoading: iscompleteApplicationDetailsLoading } = Digit.Hooks.submissions.useSearchSubmissionService(
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
  const applicationDetails = useMemo(() => applicationData?.applicationList?.[0], [applicationData]);

  const isDelayApplicationPending = useMemo(
    () =>
      Boolean(
        completeApplicationData?.applicationList?.some(
          (item) =>
            item?.applicationType === "DELAY_CONDONATION" &&
            [SubmissionWorkflowState.PENDINGAPPROVAL, SubmissionWorkflowState.PENDINGREVIEW].includes(item?.status)
        )
      ),
    [completeApplicationData]
  );

  const delayCondonationData = useMemo(() => caseDetails?.caseDetails?.delayApplications?.formdata?.[0]?.data, [caseDetails]);
  const hearingId = useMemo(() => currentOrder?.hearingNumber || applicationDetails?.additionalDetails?.hearingId || "", [
    applicationDetails,
    currentOrder,
  ]);
  const { data: hearingsData, isLoading: isHearingLoading } = Digit.Hooks.hearings.useGetHearings(
    {
      hearing: { tenantId },
      criteria: {
        tenantID: tenantId,
        filingNumber: filingNumber,
        hearingId: hearingId || hearingNumber,
      },
    },
    { applicationNumber: "", cnrNumber: "" },
    hearingId || hearingNumber,
    true
  );
  const hearingDetails = useMemo(() => hearingsData?.HearingList?.[0], [hearingsData]);
  const hearingsList = useMemo(() => hearingsData?.HearingList?.sort((a, b) => b.startTime - a.startTime), [hearingsData]);

  const isHearingScheduled = useMemo(() => {
    const isPresent = (hearingsData?.HearingList || []).some((hearing) => hearing?.status === HearingWorkflowState.SCHEDULED);
    return isPresent;
  }, [hearingsData]);

  const isHearingOptout = useMemo(() => {
    const isPresent = (hearingsData?.HearingList || []).some((hearing) => hearing?.status === HearingWorkflowState.OPTOUT);
    return isPresent;
  }, [hearingsData]);

  const modifiedFormConfig = useMemo(() => {
    let newConfig = currentOrder?.orderNumber
      ? applicationTypeConfig?.map((item) => ({ body: item.body.map((input) => ({ ...input, disable: true })) }))
      : structuredClone(applicationTypeConfig);
    if (orderType && configKeys.hasOwnProperty(orderType)) {
      let orderTypeForm = configKeys[orderType];
      if (orderType === "SECTION_202_CRPC") {
        orderTypeForm = orderTypeForm?.map((section) => {
          return {
            ...section,
            body: section.body.map((field) => {
              if (field.key === "applicationFilledBy") {
                return {
                  ...field,
                  populators: {
                    ...field.populators,
                    options: [...complainants, ...respondents],
                  },
                };
              }
              if (field.key === "detailsSeekedOf") {
                return {
                  ...field,
                  populators: {
                    ...field.populators,
                    options: [...complainants, ...respondents],
                  },
                };
              }
              return field;
            }),
          };
        });
      }
      if (["SCHEDULE_OF_HEARING_DATE", "SCHEDULING_NEXT_HEARING"].includes(orderType)) {
        orderTypeForm = orderTypeForm?.map((section) => {
          return {
            ...section,
            body: section.body.map((field) => {
              if (field.key === "namesOfPartiesRequired") {
                return {
                  ...field,
                  populators: {
                    ...field.populators,
                    options: [...complainants, ...respondents, ...unJoinedLitigant, ...witnesses],
                  },
                };
              }
              if (field.key === "hearingPurpose") {
                return {
                  ...field,
                  ...(currentOrder?.additionalDetails?.formdata?.hearingPurpose?.type === "ADMISSION" &&
                    !isCaseAdmitted && {
                      disable: true,
                    }),
                };
              }
              if (field.key === "unjoinedPartiesNote") {
                const parties = [...unJoinedLitigant, ...witnesses];
                return {
                  ...field,
                  populators: {
                    ...field.populators,
                    inputs: [
                      {
                        ...field.populators.inputs[0],
                        children: (
                          <React.Fragment>
                            {parties.map((party, index) => (
                              <div className="list-div" key={index}>
                                <p style={{ margin: "0px", fontSize: "14px" }}>
                                  {index + 1}. {party?.name}
                                </p>
                              </div>
                            ))}
                          </React.Fragment>
                        ),
                      },
                    ],
                  },
                };
              }
              return field;
            }),
          };
        });
      }
      if (orderType === "MANDATORY_SUBMISSIONS_RESPONSES") {
        orderTypeForm = orderTypeForm?.map((section) => {
          return {
            ...section,
            body: section.body.map((field) => {
              if (field.key === "submissionParty") {
                return {
                  ...field,
                  populators: {
                    ...field.populators,
                    options: [...complainants, ...respondents],
                  },
                };
              }
              if (field?.populators?.inputs?.some((input) => input?.name === "respondingParty")) {
                return {
                  ...field,
                  populators: {
                    ...field?.populators,
                    inputs: field?.populators?.inputs.map((input) =>
                      input.name === "respondingParty"
                        ? {
                            ...input,
                            options: [...complainants, ...respondents],
                          }
                        : input
                    ),
                  },
                };
              }
              return field;
            }),
          };
        });
      }
      if (orderType === "WARRANT") {
        orderTypeForm = orderTypeForm?.map((section) => {
          return {
            ...section,
            body: section.body.map((field) => {
              if (field.key === "warrantFor") {
                return {
                  ...field,
                  ...(!currentOrder?.additionalDetails?.warrantFor && {
                    disable: false,
                  }),
                  populators: {
                    ...field.populators,
                    options: [
                      ...(currentOrder?.additionalDetails?.warrantFor
                        ? [currentOrder?.additionalDetails?.warrantFor]
                        : [...respondents, ...unJoinedLitigant].map((data) => data?.name || "")),
                    ],
                  },
                };
              }
              return field;
            }),
          };
        });
      }
      if (orderType === "JUDGEMENT") {
        orderTypeForm = orderTypeForm?.map((section) => {
          return {
            ...section,
            body: section.body.map((field) => {
              if (field.key === "witnessNote" || field.key === "evidenceNote") {
                return {
                  ...field,
                  populators: {
                    ...field.populators,
                    inputs: [
                      {
                        ...field.populators.inputs[0],
                        caseId: caseDetails?.id,
                        filingNumber: caseDetails?.filingNumber,
                        tab: field?.key === "witnessNote" ? "Complaint" : field?.key === "evidenceNote" ? "Documents" : "Overview",
                        customFunction: () => handleSaveDraft({ showReviewModal: false }),
                      },
                    ],
                  },
                };
              }
              return field;
            }),
          };
        });
      }
      if (orderType === "EXTENSION_OF_DOCUMENT_SUBMISSION_DATE") {
        orderTypeForm = orderTypeForm?.map((section) => {
          return {
            ...section,
            body: section.body.filter((field) => {
              const isRejected = currentOrder?.additionalDetails?.applicationStatus === t("REJECTED");
              return !(field.key === "newSubmissionDate" && isRejected);
            }),
          };
        });
      }
      newConfig = [...newConfig, ...orderTypeForm];
    }
    const updatedConfig = newConfig.map((config) => {
      return {
        ...config,
        body: config?.body.map((body) => {
          if (body?.labelChildren === "OutlinedInfoIcon") {
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

          if (body?.populators?.validation?.customValidationFn) {
            const customValidations =
              Digit.Customizations[body.populators.validation.customValidationFn.moduleName][
                body.populators.validation.customValidationFn.masterName
              ];

            body.populators.validation = {
              ...body.populators.validation,
              ...customValidations(),
            };
          }
          return {
            ...body,
          };
        }),
      };
    });
    return updatedConfig;
  }, [caseDetails, complainants, currentOrder, orderType, respondents, t, unJoinedLitigant, witnesses]);
  const multiSelectDropdownKeys = useMemo(() => {
    const foundKeys = [];
    modifiedFormConfig.forEach((config) => {
      config.body.forEach((field) => {
        if (field.type === "dropdown" && field.populators.allowMultiSelect) {
          foundKeys.push(field.key);
        }
      });
    });
    return foundKeys;
  }, [modifiedFormConfig]);

  const generateAddress = ({
    pincode = "",
    district = "",
    city = "",
    state = "",
    coordinates = { longitude: "", latitude: "" },
    locality = "",
    address = "",
  } = {}) => {
    if (address) {
      return address;
    }
    return `${locality ? `${locality},` : ""} ${district ? `${district},` : ""} ${city ? `${city},` : ""} ${state ? `${state},` : ""} ${
      pincode ? `- ${pincode}` : ""
    }`.trim();
  };

  const defaultValue = useMemo(() => {
    if (currentOrder?.orderType && !currentOrder?.additionalDetails?.formdata) {
      return {
        orderType: {
          ...orderTypeData?.find((item) => item.code === currentOrder?.orderType),
        },
      };
    }
    let updatedFormdata = structuredClone(currentOrder?.additionalDetails?.formdata || {});
    if (orderType === "JUDGEMENT") {
      const complainantPrimary = caseDetails?.litigants?.filter((item) => item?.partyType?.includes("complainant.primary"))?.[0];
      const respondentPrimary = caseDetails?.litigants?.filter((item) => item?.partyType?.includes("respondent.primary"))?.[0];
      updatedFormdata.nameofComplainant = complainantPrimary?.additionalDetails?.fullName;
      updatedFormdata.nameofRespondent = respondentPrimary?.additionalDetails?.fullName;
      updatedFormdata.nameofComplainantAdvocate = uuidNameMap?.[allAdvocates?.[complainantPrimary?.additionalDetails?.uuid]] || "";
      updatedFormdata.nameofRespondentAdvocate = uuidNameMap?.[allAdvocates?.[respondentPrimary?.additionalDetails?.uuid]] || "";
      updatedFormdata.caseNumber = caseDetails?.courtCaseNumber;
      updatedFormdata.nameOfCourt = courtRooms.find((room) => room.code === caseDetails?.courtId)?.name;
      updatedFormdata.addressRespondant = generateAddress(
        caseDetails?.additionalDetails?.respondentDetails?.formdata?.[0]?.data?.addressDetails?.map((data) => data?.addressDetails)?.[0]
      );
      updatedFormdata.dateChequeReturnMemo = formatDate(new Date(caseDetails?.caseDetails?.chequeDetails?.formdata?.[0]?.data?.depositDate));
      updatedFormdata.dateFiling = formatDate(new Date(caseDetails?.filingDate));
      updatedFormdata.dateApprehension = formatDate(new Date(publishedBailOrder?.auditDetails?.lastModifiedTime)) || "";
      updatedFormdata.dateofReleaseOnBail = formatDate(new Date(publishedBailOrder?.auditDetails?.lastModifiedTime)) || "";
      updatedFormdata.dateofCommencementTrial = formatDate(new Date(publishedBailOrder?.auditDetails?.lastModifiedTime)) || "";
      updatedFormdata.dateofCloseTrial = formatDate(new Date(hearingsList?.[hearingsList?.length - 2]?.startTime));
      updatedFormdata.dateofSentence = formatDate(new Date(hearingsList?.[hearingsList?.length - 1]?.startTime));
      updatedFormdata.offense = "Section 138 of Negotiable Instruments Act";
    }
    if (orderType === "BAIL") {
      updatedFormdata.bailType = { type: applicationDetails?.applicationType };
      updatedFormdata.submissionDocuments = applicationDetails?.additionalDetails?.formdata?.submissionDocuments;
      updatedFormdata.bailOf = applicationDetails?.additionalDetails?.onBehalOfName;
    }
    if (orderType === "SET_BAIL_TERMS") {
      updatedFormdata.partyId = applicationDetails?.createdBy;
    }
    if (orderType === "ACCEPT_BAIL" || orderType === "REJECT_BAIL") {
      updatedFormdata.bailParty = applicationDetails?.additionalDetails?.onBehalOfName;
      updatedFormdata.submissionDocuments = {
        uploadedDocs:
          applicationDetails?.additionalDetails?.formdata?.supportingDocuments?.flatMap((doc) => doc.submissionDocuments?.uploadedDocs || []) || [],
      };
    }
    // if (orderType === "CASE_TRANSFER") {
    //   updatedFormdata.caseTransferredTo = applicationDetails?.applicationDetails?.selectRequestedCourt;
    //   updatedFormdata.grounds = { text: applicationDetails?.applicationDetails?.groundsForSeekingTransfer };
    // }
    if (orderType === "WITHDRAWAL") {
      if (applicationDetails?.applicationType === applicationTypes.WITHDRAWAL) {
        updatedFormdata.applicationOnBehalfOf = applicationDetails?.additionalDetails?.onBehalOfName;
        updatedFormdata.partyType = t(applicationDetails?.additionalDetails?.partyType);
        updatedFormdata.reasonForWithdrawal = t(applicationDetails?.additionalDetails?.formdata?.reasonForWithdrawal?.code);
      }
    }
    if (orderType === "EXTENSION_OF_DOCUMENT_SUBMISSION_DATE") {
      if (applicationDetails?.applicationType === applicationTypes.EXTENSION_SUBMISSION_DEADLINE) {
        updatedFormdata.documentName = applicationDetails?.additionalDetails?.formdata?.documentType?.value;
        updatedFormdata.originalDeadline = applicationDetails.additionalDetails?.formdata?.initialSubmissionDate;
        updatedFormdata.proposedSubmissionDate = applicationDetails.additionalDetails?.formdata?.changedSubmissionDate;
        updatedFormdata.originalSubmissionOrderDate = applicationDetails.additionalDetails?.orderDate;
      }
    }
    if (orderType === "SUMMONS") {
      if (hearingDetails?.startTime) {
        updatedFormdata.dateForHearing = formatDate(new Date(hearingDetails?.startTime));
      }
      if (currentOrder?.additionalDetails?.selectedParty && currentOrder?.additionalDetails?.selectedParty?.uuid) {
        updatedFormdata.SummonsOrder = {
          party: caseDetails?.additionalDetails?.respondentDetails?.formdata
            ?.filter((data) => data?.data?.uuid === currentOrder?.additionalDetails?.selectedParty?.uuid)
            ?.map((item) => ({
              ...item,
              data: {
                ...item.data,
                firstName: item?.data?.respondentFirstName,
                lastName: item?.data?.respondentLastName,
                address: item?.data?.addressDetails.map((address) => ({
                  locality: address?.addressDetails?.locality,
                  city: address.addressDetails.city,
                  district: address?.addressDetails?.district,
                  pincode: address?.addressDetails?.pincode,
                })),
                partyType: "Respondent",
                phone_numbers: item?.data?.phonenumbers?.mobileNumber || [],
                email: item?.data?.emails?.emailId,
              },
            }))?.[0],
          selectedChannels: currentOrder?.additionalDetails?.formdata?.SummonsOrder?.selectedChannels,
        };
      }
    }
    if (orderType === "NOTICE") {
      if (hearingDetails?.startTime) {
        updatedFormdata.dateForHearing = formatDate(new Date(hearingDetails?.startTime));
      }
      if (currentOrder?.additionalDetails?.selectedParty && currentOrder?.additionalDetails?.selectedParty?.uuid) {
        updatedFormdata.noticeOrder = {
          party: caseDetails?.additionalDetails?.respondentDetails?.formdata
            ?.filter((data) => data?.data?.uuid === currentOrder?.additionalDetails?.selectedParty?.uuid)
            ?.map((item) => ({
              ...item,
              data: {
                ...item.data,
                firstName: item.data.respondentFirstName,
                lastName: item.data.respondentLastName,
                address: item.data.addressDetails.map((address) => ({
                  locality: address.addressDetails.locality,
                  city: address.addressDetails.city,
                  district: address?.addressDetails?.district,
                  pincode: address?.addressDetails?.pincode,
                })),
                partyType: "Respondent",
                phone_numbers: item?.data?.phonenumbers?.mobileNumber || [],
                email: item?.data?.emails?.emailId,
              },
            }))?.[0],
          selectedChannels: currentOrder?.additionalDetails?.formdata?.noticeOrder?.selectedChannels,
        };
      }
    }
    if (orderType === "WARRANT") {
      if (hearingDetails?.startTime) {
        updatedFormdata.dateOfHearing = formatDate(new Date(hearingDetails?.startTime));
      }
    }
    if (
      [
        "RESCHEDULE_OF_HEARING_DATE",
        "REJECTION_RESCHEDULE_REQUEST",
        "APPROVAL_RESCHEDULE_REQUEST",
        "INITIATING_RESCHEDULING_OF_HEARING_DATE",
        "CHECKOUT_ACCEPTANCE",
        "CHECKOUT_REJECT",
      ].includes(orderType)
    ) {
      updatedFormdata.originalHearingDate =
        applicationDetails?.additionalDetails?.formdata?.initialHearingDate || currentOrder.additionalDetails?.formdata?.originalHearingDate || "";
      if (!isCaseAdmitted) {
        updatedFormdata.hearingPurpose = {
          code: "ADMISSION",
        };
      }
    }
    setCurrentFormData(updatedFormdata);
    return updatedFormdata;
  }, [currentOrder, orderType, applicationDetails, t, hearingDetails, caseDetails, orderTypeData]);
  const onFormValueChange = (setValue, formData, formState, reset, setError, clearErrors, trigger, getValues) => {
    applyMultiSelectDropdownFix(setValue, formData, multiSelectDropdownKeys);

    if (orderType && ["MANDATORY_SUBMISSIONS_RESPONSES"].includes(orderType)) {
      if (formData?.submissionDeadline && formData?.responseInfo?.responseDeadline) {
        if (new Date(formData?.submissionDeadline).getTime() >= new Date(formData?.responseInfo?.responseDeadline).getTime()) {
          setValue("responseInfo", {
            ...formData.responseInfo,
            responseDeadline: "",
          });
          setError("responseDeadline", { message: t("PROPOSED_DATE_CAN_NOT_BE_BEFORE_SUBMISSION_DEADLINE") });
        } else if (Object.keys(formState?.errors).includes("responseDeadline")) {
          setValue("responseInfo", formData?.responseInfo);
          clearErrors("responseDeadline");
        }
      }
      if (formData?.responseInfo?.isResponseRequired && Object.keys(formState?.errors).includes("isResponseRequired")) {
        clearErrors("isResponseRequired");
      } else if (
        formState?.submitCount &&
        !formData?.responseInfo?.isResponseRequired &&
        !Object.keys(formState?.errors).includes("isResponseRequired")
      ) {
        setError("isResponseRequired", { message: t("CORE_REQUIRED_FIELD_ERROR") });
      }
      if (
        formData?.responseInfo?.responseDeadline &&
        new Date(formData?.submissionDeadline).getTime() < new Date(formData?.responseInfo?.responseDeadline).getTime() &&
        Object.keys(formState?.errors).includes("responseDeadline")
      ) {
        clearErrors("responseDeadline");
      } else if (formData?.responseInfo?.isResponseRequired?.code === false && Object.keys(formState?.errors).includes("responseDeadline")) {
        clearErrors("responseDeadline");
      } else if (
        formState?.submitCount &&
        !formData?.responseInfo?.responseDeadline &&
        formData?.responseInfo?.isResponseRequired?.code === true &&
        !Object.keys(formState?.errors).includes("responseDeadline")
      ) {
        setError("responseDeadline", { message: t("PROPOSED_DATE_CAN_NOT_BE_BEFORE_SUBMISSION_DEADLINE") });
      }
      if (formData?.responseInfo?.respondingParty?.length > 0 && Object.keys(formState?.errors).includes("respondingParty")) {
        clearErrors("respondingParty");
      } else if (formData?.responseInfo?.isResponseRequired?.code === false && Object.keys(formState?.errors).includes("respondingParty")) {
        clearErrors("respondingParty");
      } else if (
        formState?.submitCount &&
        (!formData?.responseInfo?.respondingParty || formData?.responseInfo?.respondingParty?.length === 0) &&
        formData?.responseInfo?.isResponseRequired?.code === true &&
        !Object.keys(formState?.errors).includes("respondingParty")
      ) {
        setError("respondingParty", { message: t("CORE_REQUIRED_FIELD_ERROR") });
      }
    }

    if (orderType && ["WARRANT"].includes(orderType)) {
      if (formData?.bailInfo?.isBailable && Object.keys(formState?.errors).includes("isBailable")) {
        clearErrors("isBailable");
      } else if (formState?.submitCount && !formData?.bailInfo?.isBailable && !Object.keys(formState?.errors).includes("isBailable")) {
        setError("isBailable", { message: t("CORE_REQUIRED_FIELD_ERROR") });
      }
      if (formData?.bailInfo?.noOfSureties && Object.keys(formState?.errors).includes("noOfSureties")) {
        clearErrors("noOfSureties");
      } else if (formData?.bailInfo?.isBailable?.code === false && Object.keys(formState?.errors).includes("noOfSureties")) {
        clearErrors("noOfSureties");
      } else if (
        formState?.submitCount &&
        !formData?.bailInfo?.noOfSureties &&
        formData?.bailInfo?.isBailable?.code === true &&
        !Object.keys(formState?.errors).includes("noOfSureties")
      ) {
        setError("noOfSureties", { message: t("CORE_REQUIRED_FIELD_ERROR") });
      }
      if (
        formData?.bailInfo?.bailableAmount &&
        formData?.bailInfo?.bailableAmount?.slice(-1) !== "." &&
        Object.keys(formState?.errors).includes("bailableAmount")
      ) {
        clearErrors("bailableAmount");
      } else if (formData?.bailInfo?.isBailable?.code === false && Object.keys(formState?.errors).includes("bailableAmount")) {
        clearErrors("bailableAmount");
      } else if (
        formState?.submitCount &&
        formData?.bailInfo?.isBailable?.code === true &&
        formData?.bailInfo?.bailableAmount?.slice(-1) === "." &&
        !Object.keys(formState?.errors).includes("bailableAmount")
      ) {
        setError("bailableAmount", { message: t("CS_VALID_AMOUNT_DECIMAL") });
      } else if (
        formState?.submitCount &&
        !formData?.bailInfo?.bailableAmount &&
        formData?.bailInfo?.isBailable?.code === true &&
        !Object.keys(formState?.errors).includes("bailableAmount")
      ) {
        setError("bailableAmount", { message: t("CS_VALID_AMOUNT_DECIMAL") });
      }
    }

    if (formData?.orderType?.code && !isEqual(formData, currentOrder?.additionalDetails?.formdata)) {
      const updatedFormData =
        currentOrder?.additionalDetails?.formdata?.orderType?.code !== formData?.orderType?.code ? { orderType: formData.orderType } : formData;
      setFormList((prev) => {
        return prev?.map((item, index) => {
          return index !== selectedOrder
            ? item
            : {
                ...item,
                comments:
                  formData?.comments?.text ||
                  formData?.additionalComments?.text ||
                  formData?.otherDetails?.text ||
                  formData?.sentence?.text ||
                  formData?.briefSummary ||
                  "",
                orderType: formData?.orderType?.code,
                additionalDetails: { ...item?.additionalDetails, formdata: updatedFormData },
              };
        });
      });
      setCurrentFormData(formData);
    }
    setFormErrors.current = setError;
    if (Object.keys(formState?.errors).length) {
      setIsSubmitDisabled(true);
    } else {
      setIsSubmitDisabled(false);
    }
  };

  const getParties = (type, orderSchema) => {
    let parties = [];
    if (["SCHEDULE_OF_HEARING_DATE", "SCHEDULING_NEXT_HEARING"].includes(type)) {
      parties = orderSchema?.orderDetails.partyName;
    } else if (type === "MANDATORY_SUBMISSIONS_RESPONSES") {
      parties = [...orderSchema?.orderDetails?.partyDetails?.partiesToRespond, ...orderSchema?.orderDetails?.partyDetails?.partyToMakeSubmission];
    } else if (["WARRANT", "SUMMONS", "NOTICE"].includes(type)) {
      parties = orderSchema?.orderDetails?.respondentName ? [orderSchema?.orderDetails?.respondentName] : [];
    } else if (type === "SECTION_202_CRPC") {
      parties = [orderSchema?.orderDetails.soughtOfDetails];
    } else if (
      orderSchema?.orderDetails?.parties?.length > 0 &&
      ["BAIL", "REJECT_VOLUNTARY_SUBMISSIONS", "APPROVE_VOLUNTARY_SUBMISSIONS", "REJECTION_RESCHEDULE_REQUEST", "CHECKOUT_REJECT"].includes(type)
    ) {
      parties = orderSchema?.orderDetails?.parties?.map((party) => party?.partyName);
    } else {
      parties = allParties?.map((party) => ({ partyName: party.name, partyType: party?.partyType }));
      return parties;
    }
    parties = parties?.map((party) => {
      const matchingParty = allParties.find((p) => p.name === party);
      if (matchingParty) {
        return {
          partyName: matchingParty.name,
          partyType: matchingParty.partyType,
        };
      } else {
        return {
          partyName: party,
          partyType: "witness",
        };
      }
    });
    return parties;
  };

  const updateOrder = async (order, action) => {
    try {
      const localStorageID = localStorage.getItem("fileStoreId");
      const documents = Array.isArray(order?.documents) ? order.documents : [];
      const documentsFile =
        signedDoucumentUploadedID !== "" || localStorageID
          ? {
              documentType: "SIGNED",
              fileStore: signedDoucumentUploadedID || localStorageID,
            }
          : null;
      let orderSchema = {};
      try {
        let orderTypeDropDownConfig = order?.orderNumber
          ? applicationTypeConfig?.map((item) => ({ body: item.body.map((input) => ({ ...input, disable: true })) }))
          : structuredClone(applicationTypeConfig);
        let orderFormConfig = configKeys.hasOwnProperty(order?.orderType) ? configKeys[order?.orderType] : [];
        const modifiedPlainFormConfig = [...orderTypeDropDownConfig, ...orderFormConfig];
        orderSchema = Digit.Customizations.dristiOrders.OrderFormSchemaUtils.formToSchema(order.additionalDetails.formdata, modifiedPlainFormConfig);
      } catch (error) {
        console.error("error :>> ", error);
      }

      const parties = getParties(order?.orderType, {
        ...orderSchema,
        orderDetails: { ...orderSchema?.orderDetails, ...(order?.orderDetails || {}) },
      });
      orderSchema = { ...orderSchema, orderDetails: { ...orderSchema?.orderDetails, parties: parties } };
      return await ordersService.updateOrder(
        {
          order: {
            ...order,
            ...orderSchema,
            documents: documentsFile ? [...documents, documentsFile] : documents,
            workflow: { ...order.workflow, action, documents: [{}] },
          },
        },
        { tenantId }
      );
    } catch (error) {
      return null;
    }
  };

  const createOrder = async (order) => {
    try {
      let orderSchema = {};
      try {
        let orderTypeDropDownConfig = order?.orderNumber
          ? applicationTypeConfig?.map((item) => ({ body: item.body.map((input) => ({ ...input, disable: true })) }))
          : structuredClone(applicationTypeConfig);
        let orderFormConfig = configKeys.hasOwnProperty(order?.orderType) ? configKeys[order?.orderType] : [];
        const modifiedPlainFormConfig = [...orderTypeDropDownConfig, ...orderFormConfig];
        orderSchema = Digit.Customizations.dristiOrders.OrderFormSchemaUtils.formToSchema(order.additionalDetails.formdata, modifiedPlainFormConfig);
      } catch (error) {
        console.error("error :>> ", error);
      }
      const parties = getParties(order?.orderType, {
        ...orderSchema,
        orderDetails: { ...orderSchema?.orderDetails, ...(order?.orderDetails || {}) },
      });
      orderSchema = { ...orderSchema, orderDetails: { ...orderSchema?.orderDetails, parties: parties } };

      return await ordersService.createOrder(
        {
          order: {
            ...order,
            ...orderSchema,
          },
        },
        { tenantId }
      );
    } catch (error) {
      console.error(error);
    }
  };

  const handleAddOrder = () => {
    setFormList((prev) => {
      return [...prev, defaultOrderData];
    });
    setSelectedOrder(formList?.length);
  };

  const createPendingTask = async ({
    order,
    refId = null,
    orderEntityType = null,
    isAssignedRole = false,
    createTask = false,
    taskStatus = "CREATE_SUBMISSION",
    taskName = "",
    channel = "",
    orderType = "",
  }) => {
    const formdata = order?.additionalDetails?.formdata;
    let create = createTask;
    let name = taskName;
    let assignees = [];
    let referenceId = order?.orderNumber;
    let assignedRole = [];
    let additionalDetails = {};
    let entityType = orderEntityType
      ? orderEntityType
      : formdata?.isResponseRequired?.code === "Yes"
      ? "application-order-submission-feedback"
      : "application-order-submission-default";
    let status = taskStatus;
    let stateSla = stateSlaMap?.[order?.orderType] * dayInMillisecond + todayDate;
    if (order?.orderType === "MANDATORY_SUBMISSIONS_RESPONSES") {
      create = true;
      name = t("MAKE_MANDATORY_SUBMISSION");
      assignees = formdata?.submissionParty?.map((party) => party?.uuid.map((uuid) => ({ uuid }))).flat();
      stateSla = new Date(formdata?.submissionDeadline).getTime();
      status = "CREATE_SUBMISSION";
      const promises = assignees.map(async (assignee) => {
        return ordersService.customApiService(Urls.orders.pendingTask, {
          pendingTask: {
            name,
            entityType,
            referenceId: `MANUAL_${assignee?.uuid}_${order?.orderNumber}`,
            status,
            assignedTo: [assignee],
            assignedRole,
            cnrNumber: cnrNumber,
            filingNumber: filingNumber,
            isCompleted: false,
            stateSla,
            additionalDetails: { ...additionalDetails },
            tenantId,
          },
        });
      });
      return await Promise.all(promises);
    }
    if (order?.orderType === "EXTENSION_OF_DOCUMENT_SUBMISSION_DATE") {
      stateSla = new Date(formdata?.newSubmissionDate).getTime();
      const promises = mandatorySubmissionTasks?.map(async (task) => {
        return ordersService.customApiService(Urls.orders.pendingTask, {
          pendingTask: { ...task, stateSla, tenantId },
        });
      });
      return await Promise.all(promises);
    }
    if (order?.orderType === "INITIATING_RESCHEDULING_OF_HEARING_DATE") {
      create = true;
      status = "OPTOUT";
      assignees = Object.values(allAdvocates)
        ?.flat()
        ?.map((uuid) => ({ uuid }));
      name = t("CHOOSE_DATES_FOR_RESCHEDULE_OF_HEARING_DATE");
      entityType = "hearing-default";
      const promises = assignees.map(async (assignee) => {
        return ordersService.customApiService(Urls.orders.pendingTask, {
          pendingTask: {
            name,
            entityType,
            referenceId: `MANUAL_${assignee?.uuid}_${order?.orderNumber}`,
            status,
            assignedTo: [assignee],
            assignedRole,
            cnrNumber: cnrNumber,
            filingNumber: filingNumber,
            isCompleted: false,
            stateSla,
            additionalDetails,
            tenantId,
          },
        });
      });
      return await Promise.all(promises);
    }
    if ((order?.orderType === "SUMMONS" || orderType === "SUMMONS") && refId) {
      const assignee = [...complainants?.map((data) => data?.uuid[0])];
      const advocateUuid = Object.keys(allAdvocates)
        .filter((data) => assignee?.includes(allAdvocates?.[data]?.[0]))
        ?.flat();
      assignees = [...assignee, ...advocateUuid]?.map((uuid) => ({ uuid }));
      entityType = "order-default";
      return ordersService.customApiService(Urls.orders.pendingTask, {
        pendingTask: {
          name: t(`MAKE_PAYMENT_FOR_SUMMONS_${channelTypeEnum?.[channel?.type]?.code}`),
          entityType,
          referenceId: `MANUAL_${refId}`,
          status: `PAYMENT_PENDING_${channelTypeEnum?.[channel?.type]?.code}`,
          assignedTo: assignees,
          assignedRole,
          cnrNumber: cnrNumber,
          filingNumber: filingNumber,
          isCompleted: false,
          stateSla: stateSlaMap?.[order?.orderType || orderType] * dayInMillisecond + todayDate,
          additionalDetails: { ...additionalDetails, applicationNumber: order?.additionalDetails?.formdata?.refApplicationId },
          tenantId,
        },
      });
    }
    if ((order?.orderType === "NOTICE" || orderType === "NOTICE") && refId) {
      const assignee = [...complainants?.map((data) => data?.uuid[0])];
      const advocateUuid = Object.keys(allAdvocates)
        .filter((data) => assignee?.includes(allAdvocates?.[data]?.[0]))
        ?.flat();
      assignees = [...assignee, ...advocateUuid]?.map((uuid) => ({ uuid }));
      entityType = "order-default";
      return ordersService.customApiService(Urls.orders.pendingTask, {
        pendingTask: {
          name: t(`MAKE_PAYMENT_FOR_NOTICE_${channelTypeEnum?.[channel?.type]?.code}`),
          entityType,
          referenceId: `MANUAL_${refId}`,
          status: `PAYMENT_PENDING_${channelTypeEnum?.[channel?.type]?.code}`,
          assignedTo: assignees,
          assignedRole,
          cnrNumber: cnrNumber,
          filingNumber: filingNumber,
          isCompleted: false,
          stateSla: stateSlaMap?.[order?.orderType || orderType] * dayInMillisecond + todayDate,
          additionalDetails: { ...additionalDetails, applicationNumber: order?.additionalDetails?.formdata?.refApplicationId },
          tenantId,
        },
      });
    }
    if ((order?.orderType === "WARRANT" || orderType === "WARRANT") && refId) {
      const assignee = [...complainants?.map((data) => data?.uuid[0])];
      const advocateUuid = Object.keys(allAdvocates)
        .filter((data) => assignee?.includes(allAdvocates?.[data]?.[0]))
        ?.flat();
      assignees = [...assignee, ...advocateUuid]?.map((uuid) => ({ uuid }));
      entityType = "order-default";
      return ordersService.customApiService(Urls.orders.pendingTask, {
        pendingTask: {
          name: t(`PAYMENT_PENDING_FOR_WARRANT`),
          entityType,
          referenceId: `MANUAL_${refId}`,
          status: `PAYMENT_PENDING_POLICE`,
          assignedTo: assignees,
          assignedRole,
          cnrNumber: cnrNumber,
          filingNumber: filingNumber,
          isCompleted: false,
          stateSla: stateSlaMap?.[order?.orderType || orderType] * dayInMillisecond + todayDate,
          additionalDetails: { ...additionalDetails, applicationNumber: order?.additionalDetails?.formdata?.refApplicationId },
          tenantId,
        },
      });
    }

    if (order?.orderType === "SET_BAIL_TERMS") {
      create = true;
      status = "CREATE_SUBMISSION";
      name = t("SUBMIT_BAIL_DOCUMENTS");
      entityType = "voluntary-application-submission-bail-documents";
      const assigneeUuid = order?.additionalDetails?.formdata?.partyId;
      return ordersService.customApiService(Urls.orders.pendingTask, {
        pendingTask: {
          name,
          entityType,
          referenceId: `MANUAL_${assigneeUuid}_${order?.orderNumber}`,
          status,
          assignedTo: [{ uuid: assigneeUuid }],
          assignedRole,
          cnrNumber: cnrNumber,
          filingNumber: filingNumber,
          isCompleted: false,
          stateSla,
          additionalDetails,
          tenantId,
        },
      });
    }

    if (isAssignedRole) {
      assignees = [];
      assignedRole = ["JUDGE_ROLE"];
      if (order?.orderType === "SCHEDULE_OF_HEARING_DATE" && refId && isCaseAdmitted) {
        referenceId = refId;
        create = true;
        status = "CREATE_SUMMONS_ORDER";
        name = t("CREATE_ORDERS_FOR_SUMMONS");
        entityType = "order-default";
        additionalDetails = { ...additionalDetails, orderType: "SUMMONS", hearingID: order?.hearingNumber };
      }

      if (order?.orderType === "SCHEDULE_OF_HEARING_DATE" && refId && !isCaseAdmitted) {
        referenceId = refId;
        create = true;
        status = "CREATE_NOTICE_ORDER";
        name = t("CREATE_ORDERS_FOR_NOTICE");
        entityType = "order-default";
        additionalDetails = { ...additionalDetails, orderType: "NOTICE", hearingID: order?.hearingNumber };
      }
    }

    create &&
      (await ordersService.customApiService(Urls.orders.pendingTask, {
        pendingTask: {
          name,
          entityType,
          referenceId: `MANUAL_${referenceId}`,
          status,
          assignedTo: assignees,
          assignedRole,
          cnrNumber: cnrNumber,
          filingNumber: filingNumber,
          isCompleted: false,
          stateSla: stateSlaMap?.[order?.orderType] * dayInMillisecond + todayDate,
          additionalDetails: additionalDetails,
          tenantId,
        },
      }));
    return;
  };

  const closeManualPendingTask = async (refId) => {
    try {
      await ordersService.customApiService(Urls.orders.pendingTask, {
        pendingTask: {
          name: "Completed",
          entityType: "order-default",
          referenceId: `MANUAL_${refId}`,
          status: "DRAFT_IN_PROGRESS",
          assignedTo: [],
          assignedRole: [],
          cnrNumber: cnrNumber,
          filingNumber: filingNumber,
          isCompleted: true,
          stateSla: null,
          additionalDetails: {},
          tenantId,
        },
      });
    } catch (error) {}
  };

  const handleSaveDraft = async ({ showReviewModal }) => {
    if (showReviewModal) {
      setLoader(true);
    }
    let count = 0;
    const promises = formList.map(async (order) => {
      if (order?.orderType) {
        count += 1;
        if (order?.orderNumber) {
          return updateOrder(order, OrderWorkflowAction.SAVE_DRAFT);
        } else {
          return createOrder(order);
        }
      } else {
        return Promise.resolve();
      }
    });
    const responsesList = await Promise.all(promises);
    if (showReviewModal) {
      setLoader(false);
    }
    setFormList(
      responsesList.map((res) => {
        return res?.order;
      })
    );
    if (!showReviewModal) {
      setShowErrorToast({ label: t("DRAFT_SAVED_SUCCESSFULLY"), error: false });
    }
    if (selectedOrder >= count) {
      setSelectedOrder(0);
    }
    if (showReviewModal) {
      setShowReviewModal(true);
    }
  };

  const applicationStatusType = (Type) => {
    switch (Type) {
      case "APPROVED":
        return SubmissionWorkflowAction.APPROVE;
      case "SET_TERM_BAIL":
        return SubmissionWorkflowAction.SET_TERM_BAIL;
      default:
        return SubmissionWorkflowAction.REJECT;
    }
  };

  const handleApplicationAction = async (order) => {
    try {
      return await ordersService.customApiService(
        `/application/v1/update`,
        {
          application: {
            ...applicationDetails,
            cmpNumber: caseDetails?.cmpNumber,
            workflow: {
              ...applicationDetails.workflow,
              action: applicationStatusType(order?.additionalDetails?.applicationStatus),
            },
          },
        },
        { tenantId }
      );
    } catch (error) {
      return false;
    }
  };

  const { revalidate: revalidateWorkflow = () => {} } = window?.Digit.Hooks.useWorkflowDetails({
    tenantId,
    id: caseDetails?.filingNumber,
    moduleCode: "case-default",
    config: {
      cacheTime: 0,
      enabled: Boolean(caseDetails?.filingNumber && tenantId),
    },
  });

  const handleUpdateHearing = async ({ startTime, endTime, action }) => {
    await ordersService.updateHearings(
      {
        hearing: {
          ...hearingDetails,
          ...(startTime && { startTime }),
          ...(endTime && { endTime }),
          documents: hearingDetails?.documents || [],
          workflow: {
            action: action,
            assignes: [],
            comments: "Update Hearing",
            documents: [{}],
          },
        },
      },
      { tenantId }
    );
  };
  const handleRescheduleHearing = async ({ hearingType, hearingBookingId, rescheduledRequestId, comments, requesterId, date }) => {
    await schedulerService.RescheduleHearing(
      {
        RescheduledRequest: [
          {
            rescheduledRequestId: rescheduledRequestId,
            hearingBookingId: hearingBookingId,
            tenantId: tenantId,
            judgeId: "super",
            caseId: filingNumber,
            hearingType: "ADMISSION",
            requesterId: requesterId,
            reason: comments,
            availableAfter: date,
            rowVersion: 1,
            suggestedDates: null,
            availableDates: null,
            scheduleDate: null,
          },
        ],
      },
      {}
    );
  };

  const getFormData = (orderType, order) => {
    const formDataKeyMap = {
      SUMMONS: "SummonsOrder",
      WARRANT: "warrantFor",
      NOTICE: "noticeOrder",
    };
    const formDataKey = formDataKeyMap[orderType];
    return order?.additionalDetails?.formdata?.[formDataKey];
  };

  const getOrderData = (orderType, orderFormData) => {
    return ["SUMMONS", "NOTICE"].includes(orderType) ? orderFormData?.party?.data : orderFormData;
  };

  const getCourtFee = async (channelId, receiverPincode, taskType) => {
    try {
      const breakupResponse = await DRISTIService.getSummonsPaymentBreakup(
        {
          Criteria: [
            {
              channelId: channelId,
              receiverPincode: receiverPincode,
              tenantId: tenantId,
              taskType: taskType,
            },
          ],
        },
        {}
      );
      return breakupResponse?.Calculation?.[0]?.breakDown?.filter((data) => data?.type === "Court Fee").reduce((sum, fee) => (sum += fee.amount), 0);
    } catch (error) {
      console.error("error", error);
      return 0;
    }
  };

  const createTask = async (orderType, caseDetails, orderDetails) => {
    let payload = {};
    const { litigants } = caseDetails;
    const complainantIndividualId = litigants?.find((item) => item?.partyType === "complainant.primary")?.individualId;
    const individualDetail = await Digit.DRISTIService.searchIndividualUser(
      {
        Individual: {
          individualId: complainantIndividualId,
        },
      },
      { tenantId, limit: 1000, offset: 0 }
    );

    const orderData = orderDetails?.order;
    const orderFormData = getFormData(orderType, orderData);
    const orderFormValue = orderDetails?.order?.additionalDetails?.formdata;
    const respondentNameData = getOrderData(orderType, orderFormData);
    const selectedChannel = orderData?.additionalDetails?.formdata?.[orderType === "NOTICE" ? "noticeOrder" : "SummonsOrder"]?.selectedChannels;
    const noticeType = orderData?.additionalDetails?.formdata?.noticeType?.type;
    const respondentAddress = orderFormData?.addressDetails
      ? orderFormData?.addressDetails?.map((data) => ({ ...data?.addressDetails }))
      : caseDetails?.additionalDetails?.respondentDetails?.formdata?.[0]?.data?.addressDetails?.map((data) => data?.addressDetails);
    const respondentName =
      constructFullName(respondentNameData?.firstName, respondentNameData?.middleName, respondentNameData?.lastName) || respondentNameData;

    const respondentPhoneNo = orderFormData?.party?.data?.phone_numbers || [];
    const respondentEmail = orderFormData?.party?.data?.email || [];
    const complainantDetails = individualDetail?.Individual?.[0];
    const addressLine1 = complainantDetails?.address[0]?.addressLine1 || "";
    const addressLine2 = complainantDetails?.address[0]?.addressLine2 || "";
    const buildingName = complainantDetails?.address[0]?.buildingName || "";
    const street = complainantDetails?.address[0]?.street || "";
    const city = complainantDetails?.address[0]?.city || "";
    const pincode = complainantDetails?.address[0]?.pincode || "";
    const latitude = complainantDetails?.address[0]?.latitude || "";
    const longitude = complainantDetails?.address[0]?.longitude || "";
    const doorNo = complainantDetails?.address[0]?.doorNo || "";
    const complainantName = `${complainantDetails?.name?.givenName || ""}${
      complainantDetails?.name?.otherNames ? " " + complainantDetails?.name?.otherNames + " " : " "
    }${complainantDetails?.name?.familyName || ""}`;
    const address = `${doorNo ? doorNo + "," : ""} ${buildingName ? buildingName + "," : ""} ${street}`.trim();
    const complainantAddress = {
      pincode: pincode,
      district: addressLine2,
      city: city,
      state: addressLine1,
      coordinate: {
        longitude: longitude,
        latitude: latitude,
      },
      locality: address,
    };
    const courtDetails = courtRoomData?.Court_Rooms?.find((data) => data?.code === caseDetails?.courtId);

    const respondentDetails = {
      name: respondentName,
      address: respondentAddress?.[0],
      phone: respondentPhoneNo[0] || "",
      email: respondentEmail[0] || "",
      age: "",
      gender: "",
    };
    const caseRespondent = {
      name: caseDetails?.additionalDetails?.respondentDetails?.formdata?.[0]?.data?.respondentFirstName || "",
      address: caseDetails?.additionalDetails?.respondentDetails?.formdata?.[0]?.data?.addressDetails?.[0]?.addressDetails,
      phone: caseDetails?.additionalDetails?.respondentDetails?.formdata?.[0]?.data?.phonenumbers?.mobileNumber?.[0] || "",
      email: caseDetails?.additionalDetails?.respondentDetails?.formdata?.[0]?.data?.emails?.emailId?.[0] || "",
      age: caseDetails?.additionalDetails?.respondentDetails?.formdata?.[0]?.data?.respondentAge,
      gender: "",
    };

    switch (orderType) {
      case "SUMMONS":
        payload = {
          summonDetails: {
            issueDate: orderData?.auditDetails?.lastModifiedTime,
            caseFilingDate: caseDetails?.filingDate,
            docSubType: orderFormData?.party?.data?.partyType === "Witness" ? "WITNESS" : "ACCUSED",
          },
          respondentDetails: orderFormData?.party?.data?.partyType === "Witness" ? caseRespondent : respondentDetails,
          ...(orderFormData?.party?.data?.partyType === "Witness" && { witnessDetails: respondentDetails }),
          complainantDetails: {
            name: complainantName,
            address: complainantAddress,
          },
          caseDetails: {
            caseTitle: caseDetails?.caseTitle,
            year: new Date(caseDetails).getFullYear(),
            hearingDate: new Date(orderData?.additionalDetails?.formdata?.dateForHearing || "").getTime(),
            courtName: courtDetails?.name,
            courtAddress: courtDetails?.address,
            courtPhone: courtDetails?.phone,
            courtId: caseDetails?.courtId,
            hearingNumber: orderData?.hearingNumber,
            judgeName: judgeName,
          },
          deliveryChannels: {
            channelName: "",
            status: "",
            statusChangeDate: "",
            fees: 0,
            feesStatus: "pending",
          },
        };
        break;
      case "NOTICE":
        payload = {
          noticeDetails: {
            issueDate: orderData?.auditDetails?.lastModifiedTime,
            caseFilingDate: caseDetails?.filingDate,
            noticeType,
            docSubType: orderFormData?.party?.data?.partyType === "Witness" ? "WITNESS" : "ACCUSED",
          },
          respondentDetails: orderFormData?.party?.data?.partyType === "Witness" ? caseRespondent : respondentDetails,
          ...(orderFormData?.party?.data?.partyType === "Witness" && { witnessDetails: respondentDetails }),
          complainantDetails: {
            name: complainantName,
            address: complainantAddress,
          },
          caseDetails: {
            caseTitle: caseDetails?.caseTitle,
            year: new Date(caseDetails).getFullYear(),
            hearingDate: new Date(orderData?.additionalDetails?.formdata?.dateForHearing || "").getTime(),
            courtName: courtDetails?.name,
            courtAddress: courtDetails?.address,
            courtPhone: courtDetails?.phone,
            courtId: caseDetails?.courtId,
            hearingNumber: orderData?.hearingNumber,
            judgeName: judgeName,
          },
          deliveryChannels: {
            channelName: "",
            status: "",
            statusChangeDate: "",
            fees: 0,
            feesStatus: "pending",
          },
        };
        break;
      case "WARRANT":
        payload = {
          warrantDetails: {
            issueDate: orderData?.auditDetails?.lastModifiedTime,
            caseFilingDate: caseDetails?.filingDate,
            docType: orderFormValue.warrantType?.code,
            docSubType: orderFormValue.bailInfo?.isBailable?.code ? "BAILABLE" : "NON_BAILABLE",
            surety: orderFormValue.bailInfo?.noOfSureties?.code,
            bailableAmount: orderFormValue.bailInfo?.bailableAmount,
          },
          respondentDetails: {
            name: respondentName,
            address: { ...respondentAddress?.[0], coordinate: respondentAddress?.[0]?.coordinates },
            phone: respondentPhoneNo?.[0] || "",
            email: respondentEmail?.[0] || "",
            age: "",
            gender: "",
          },
          caseDetails: {
            caseTitle: caseDetails?.caseTitle,
            year: new Date(caseDetails).getFullYear(),
            hearingDate: new Date(orderData?.additionalDetails?.formdata?.dateOfHearing || "").getTime(),
            judgeName: judgeName,
            courtName: courtDetails?.name,
            courtAddress: courtDetails?.address,
            courtPhone: courtDetails?.phone,
            courtId: caseDetails?.courtId,
          },
          deliveryChannels: {
            channelName: "Police",
            name: "",
            address: "",
            phone: "",
            email: "",
            status: "",
            statusChangeDate: "",
            fees: await getCourtFee("POLICE", respondentAddress?.[0]?.pincode, orderType),
            feesStatus: "",
          },
        };
        break;
      case "BAIL":
        payload = {
          respondentDetails: {
            name: respondentName,
            address: respondentAddress?.[0],
            phone: respondentPhoneNo?.[0] || "",
            email: respondentEmail?.[0] || "",
            age: "",
            gender: "",
          },
          caseDetails: {
            title: caseDetails?.caseTitle,
            year: new Date(caseDetails).getFullYear(),
            hearingDate: new Date(orderData?.additionalDetails?.formdata?.date || "").getTime(),
            judgeName: "",
            courtName: courtDetails?.name,
            courtAddress: courtDetails?.address,
            courtPhone: courtDetails?.phone,
            courtId: caseDetails?.courtId,
          },
        };
        break;
      default:
        break;
    }
    if (Object.keys(payload || {}).length > 0 && Array.isArray(selectedChannel)) {
      const channelMap = new Map();
      selectedChannel.forEach(async (item) => {
        let courtFees = await getCourtFee(item?.code, payload?.respondentDetails?.address?.pincode, orderType);

        if (channelMap.get(item?.type)) {
          channelMap.set(item?.type, channelMap.get(item?.type) + 1);
        } else {
          channelMap.set(item?.type, 1);
        }
        if ("deliveryChannels" in payload) {
          payload.deliveryChannels = {
            ...payload.deliveryChannels,
            channelName: channelTypeEnum?.[item?.type]?.type,
            fees: courtFees,
          };

          const address = ["e-Post", "Via Police", "Registered Post"].includes(item?.type)
            ? respondentAddress[channelMap.get(item?.type) - 1]
            : respondentAddress[0];
          const sms = ["SMS"].includes(item?.type) ? respondentPhoneNo[channelMap.get(item?.type) - 1] : respondentPhoneNo[0];
          const email = ["E-mail"].includes(item?.type) ? respondentEmail[channelMap.get(item?.type) - 1] : respondentEmail[0];

          payload.respondentDetails = {
            ...payload.respondentDetails,
            address: ["e-Post", "Via Police", "Registered Post"].includes(item?.type)
              ? {
                  ...address,
                  locality: item?.value?.locality || address?.locality,
                  coordinate: item?.value?.coordinates || address?.coordinates,
                }
              : { ...address, coordinate: address?.coordinates } || "",
            phone: ["SMS"].includes(item?.type) ? item?.value : sms || "",
            email: ["E-mail"].includes(item?.type) ? item?.value : email || "",
            age: "",
            gender: "",
          };
        }
        if ("deliveryChannel" in payload) {
          const channelDetailsEnum = {
            SMS: "phone",
            "E-mail": "email",
            "e-Post": "address",
            "Via Police": "address",
            "Registered Post": "address",
          };
          payload.deliveryChannel = {
            ...payload.deliveryChannel,
            channelName: channelTypeEnum?.[item?.type]?.type,
            [channelDetailsEnum?.[item?.type]]: item?.value || "",
          };

          const address = respondentAddress[channelMap.get(item?.type) - 1];

          const sms = respondentPhoneNo[channelMap.get(item?.type) - 1];
          const email = respondentEmail[channelMap.get(item?.type) - 1];

          payload.respondentDetails = {
            ...payload.respondentDetails,
            address: ["e-Post", "Via Police", "Registered Post"].includes(item?.type) ? item?.value : address || "",
            phone: ["SMS"].includes(item?.type) ? item?.value : sms || "",
            email: ["E-mail"].includes(item?.type) ? item?.value : email || "",
            age: "",
            gender: "",
          };
        }
        await ordersService
          .customApiService(Urls.orders.taskCreate, {
            task: {
              taskDetails: payload,
              workflow: {
                action: "CREATE",
                comments: orderType,
                documents: [
                  {
                    documentType: null,
                    fileStore: null,
                    documentUid: null,
                    additionalDetails: {},
                  },
                ],
                assignes: null,
                rating: null,
              },
              createdDate: new Date().getTime(),
              orderId: orderData?.id,
              filingNumber,
              cnrNumber,
              taskType: orderType,
              status: "INPROGRESS",
              tenantId,
              amount: {
                type: "FINE",
                status: "DONE",
                amount: summonsCourtFee,
              },
            },
            tenantId,
          })
          .then(async (data) => {
            if (["SUMMONS", "NOTICE"].includes(orderType)) {
              await createPendingTask({ refId: data?.task?.taskNumber, channel: item, orderType: orderType });
            }
          });
      });
    } else if (Object.keys(payload || {}).length > 0) {
      await ordersService
        .customApiService(Urls.orders.taskCreate, {
          task: {
            taskDetails: payload,
            workflow: {
              action: "CREATE",
              comments: orderType,
              documents: [
                {
                  documentType: null,
                  fileStore: null,
                  documentUid: null,
                  additionalDetails: {},
                },
              ],
              assignes: null,
              rating: null,
            },
            createdDate: new Date().getTime(),
            orderId: orderData?.id,
            filingNumber,
            cnrNumber,
            taskType: orderType,
            status: "INPROGRESS",
            tenantId,
            amount: {
              type: "FINE",
              status: "DONE",
              amount: summonsCourtFee,
            },
          },
          tenantId,
        })
        .then(async (data) => {
          if (["WARRANT"].includes(orderType)) {
            await createPendingTask({ refId: data?.task?.taskNumber, orderType: orderType });
          }
        });
    }
  };

  const handleIssueSummons = async (hearingDate, hearingNumber) => {
    try {
      const orderbody = {
        createdDate: null,
        tenantId,
        cnrNumber,
        filingNumber,
        statuteSection: {
          tenantId,
        },
        orderType: "SUMMONS",
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
              code: "SUMMONS",
              type: "SUMMONS",
              name: "ORDER_TYPE_SUMMONS",
            },
            hearingDate,
          },
        },
      };
      const summonsArray = currentOrder?.additionalDetails?.isReIssueSummons
        ? [{}]
        : currentOrder?.additionalDetails?.formdata?.namesOfPartiesRequired?.filter((data) => data?.partyType === "respondent");
      const promiseList = summonsArray?.map((data) =>
        ordersService.createOrder(
          {
            order: {
              ...orderbody,
              additionalDetails: {
                ...orderbody?.additionalDetails,
                selectedParty: data,
              },
            },
          },
          { tenantId }
        )
      );
      const resList = await Promise.all(promiseList);
      setCreatedSummon(resList[0]?.order?.orderNumber);
      await Promise.all(
        resList.forEach((res) =>
          createPendingTask({
            order: res?.order,
            isAssignedRole: true,
            createTask: true,
            taskStatus: "DRAFT_IN_PROGRESS",
            taskName: t("DRAFT_IN_PROGRESS_ISSUE_SUMMONS"),
            orderEntityType: "order-default",
          })
        )
      );
    } catch (error) {}
  };

  const updateCaseDetails = async (action) => {
    return await DRISTIService.caseUpdateService(
      {
        cases: {
          ...caseDetails,
          linkedCases: caseDetails?.linkedCases ? caseDetails?.linkedCases : [],
          workflow: {
            ...caseDetails?.workflow,
            action,
          },
        },
        tenantId,
      },
      tenantId
    ).then(() => {
      refetchCaseData();
      revalidateWorkflow();
    });
  };

  const handleIssueNotice = async (hearingDate, hearingNumber) => {
    try {
      const orderbody = {
        createdDate: null,
        tenantId,
        cnrNumber,
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
      const summonsArray = currentOrder?.additionalDetails?.isReIssueNotice
        ? [{}]
        : currentOrder?.additionalDetails?.formdata?.namesOfPartiesRequired?.filter((data) => data?.partyType === "respondent");
      const promiseList = summonsArray?.map((data) =>
        ordersService.createOrder(
          {
            order: {
              ...orderbody,
              additionalDetails: {
                ...orderbody?.additionalDetails,
                selectedParty: data,
              },
            },
          },
          { tenantId }
        )
      );

      const resList = await Promise.all(promiseList);
      setCreatedNotice(resList[0]?.order?.orderNumber);
      await Promise.all(
        resList.forEach((res) =>
          createPendingTask({
            order: res?.order,
            isAssignedRole: true,
            createTask: true,
            taskStatus: "DRAFT_IN_PROGRESS",
            taskName: t("DRAFT_IN_PROGRESS_ISSUE_NOTICE"),
            orderEntityType: "order-default",
          })
        )
      );
    } catch (error) {}
  };

  const handleIssueOrder = async () => {
    try {
      setLoader(true);
      let newhearingId = "";
      setPrevOrder(currentOrder);
      if (["SCHEDULE_OF_HEARING_DATE", "SCHEDULING_NEXT_HEARING"].includes(orderType)) {
        const advocateData = advocateDetails.advocates.map((advocate) => {
          return {
            individualId: advocate.responseList[0].individualId,
            name: advocate.responseList[0].additionalDetails.username,
            type: "Advocate",
          };
        });
        const hearingres = await ordersService.createHearings(
          {
            hearing: {
              tenantId: tenantId,
              filingNumber: [filingNumber],
              cnrNumbers: cnrNumber ? [cnrNumber] : [],
              hearingType: currentOrder?.additionalDetails?.formdata?.hearingPurpose?.type,
              status: true,
              attendees: [
                ...currentOrder?.additionalDetails?.formdata?.namesOfPartiesRequired.map((attendee) => {
                  return { name: attendee.name, individualId: attendee.individualId, type: attendee.partyType };
                }),
                ...advocateData,
              ],
              startTime: Date.parse(currentOrder?.additionalDetails?.formdata?.hearingDate),
              endTime: Date.parse(currentOrder?.additionalDetails?.formdata?.hearingDate),
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
        newhearingId = hearingres?.hearing?.hearingId;
        setNewHearingNumber(newhearingId);
      }
      if (orderType === "RESCHEDULE_OF_HEARING_DATE") {
        await handleUpdateHearing({
          action: HearingWorkflowAction.BULK_RESCHEDULE,
          startTime: Date.parse(currentOrder?.additionalDetails?.formdata?.newHearingDate),
          endTime: Date.parse(currentOrder?.additionalDetails?.formdata?.newHearingDate),
        });
      }
      if (orderType === "CHECKOUT_ACCEPTANCE") {
        await handleUpdateHearing({
          action: HearingWorkflowAction.BULK_RESCHEDULE,
          startTime: Date.parse(currentOrder?.additionalDetails?.formdata?.newHearingDate),
          endTime: Date.parse(currentOrder?.additionalDetails?.formdata?.newHearingDate),
        });
      }
      if (orderType === "INITIATING_RESCHEDULING_OF_HEARING_DATE") {
        const dateObject = new Date(
          applicationDetails?.additionalDetails?.formdata?.changedHearingDate || currentOrder?.additionalDetails?.formdata?.originalHearingDate
        );
        let date = dateObject && dateObject?.getTime();
        if (isNaN(date)) {
          date = Date.now();
        }
        const requesterId = "";
        const comments = currentOrder?.comments || "";
        const hearingBookingId = currentOrder?.hearingNumber;
        const rescheduledRequestId = currentOrder?.orderNumber;
        await handleUpdateHearing({
          action: HearingWorkflowAction.RESCHEDULE,
          startTime: Date.parse(currentOrder?.additionalDetails?.formdata?.newHearingDate),
          endTime: Date.parse(currentOrder?.additionalDetails?.formdata?.newHearingDate),
        });
        await handleRescheduleHearing({ hearingBookingId, rescheduledRequestId, comments, requesterId, date });
      }
      if (orderType === "ASSIGNING_DATE_RESCHEDULED_HEARING") {
        await handleUpdateHearing({
          action: HearingWorkflowAction.SETDATE,
          startTime: Date.parse(currentOrder?.additionalDetails?.formdata?.newHearingDate),
          endTime: Date.parse(currentOrder?.additionalDetails?.formdata?.newHearingDate),
        });
      }
      referenceId && (await handleApplicationAction(currentOrder));
      const orderResponse = await updateOrder(
        {
          ...currentOrder,
          ...((newhearingId || hearingNumber || hearingDetails?.hearingId) && {
            hearingNumber: newhearingId || hearingNumber || hearingDetails?.hearingId,
          }),
        },
        OrderWorkflowAction.ESIGN
      );
      await createPendingTask({
        order: {
          ...currentOrder,
          ...((newhearingId || hearingNumber || hearingDetails?.hearingId) && {
            hearingNumber: newhearingId || hearingNumber || hearingDetails?.hearingId,
          }),
        },
      });
      currentOrder?.additionalDetails?.formdata?.refApplicationId && closeManualPendingTask(currentOrder?.orderNumber);
      if (orderType === "SCHEDULE_OF_HEARING_DATE") {
        closeManualPendingTask(filingNumber);
        if (currentOrder?.additionalDetails?.formdata?.hearingPurpose?.code === "ADMISSION" && !isCaseAdmitted) {
          updateCaseDetails("SCHEDULE_ADMISSION_HEARING");
        }
      }
      closeManualPendingTask(currentOrder?.orderNumber);
      if (orderType === "SUMMONS") {
        closeManualPendingTask(currentOrder?.hearingNumber || hearingDetails?.hearingId);
      }
      if (orderType === "NOTICE") {
        closeManualPendingTask(currentOrder?.hearingNumber || hearingDetails?.hearingId);
        if (caseDetails?.status === "ADMISSION_HEARING_SCHEDULED") {
          try {
            await updateCaseDetails("ADMIT");
          } catch (error) {
            console.error("Error during ADMIT case update:", error);
          } finally {
            try {
              await refetchCaseData();
              await updateCaseDetails("ISSUE_ORDER");
            } catch (finalError) {
              console.error("Error during final steps:", finalError);
            }
          }
        } else {
          try {
            await updateCaseDetails("ISSUE_ORDER");
            const caseDetails = await refetchCaseData();
            const caseData = caseDetails?.data?.criteria?.[0]?.responseList?.[0];
            const respondent = caseData?.litigants?.find((litigant) => litigant?.partyType?.includes("respondent"));
            const advocate = caseData?.representatives?.find((representative) =>
              representative?.representing?.some((represent) => respondent && represent?.individualId === respondent?.individualId)
            );

            const assignees = [];
            if (respondent) assignees.push({ uuid: respondent?.additionalDetails?.uuid });
            if (advocate) assignees.push({ uuid: advocate?.additionalDetails?.uuid });

            if (respondent && assignees?.length > 0) {
              try {
                await DRISTIService.customApiService(Urls.orders.pendingTask, {
                  pendingTask: {
                    name: "Pending Response",
                    entityType: "case-default",
                    referenceId: `MANUAL_${caseData?.filingNumber}`,
                    status: "PENDING_RESPONSE",
                    assignedTo: assignees,
                    assignedRole: ["CASE_RESPONDER"],
                    cnrNumber: caseData?.cnrNumber,
                    filingNumber: caseData?.filingNumber,
                    isCompleted: false,
                    stateSla: todayDate + 20 * 24 * 60 * 60 * 1000,
                    additionalDetails: { individualId: respondent?.individualId, caseId: caseData?.id },
                    tenantId,
                  },
                });
              } catch (err) {
                console.error("err :>> ", err);
              }
            }
          } catch (error) {
            console.error("error :>> ", error);
          }
        }
      }
      if (orderType === "ADMIT_DISMISS_CASE") {
        updateCaseDetails(currentOrder.additionalDetails?.formdata?.isCaseAdmittedOrDismissed?.code === "DISMISSED" ? "REJECT" : "ADMIT");
      }
      createTask(orderType, caseDetails, orderResponse);
      setLoader(false);
      setShowSuccessModal(true);
    } catch (error) {
      setShowErrorToast({ label: t("INTERNAL_ERROR_OCCURRED"), error: true });
      setLoader(false);
    }
  };

  const handleDeleteOrder = async () => {
    try {
      if (formList[deleteOrderIndex]?.orderNumber) {
        await updateOrder(formList[deleteOrderIndex], OrderWorkflowAction.DELETE);
        closeManualPendingTask(formList[deleteOrderIndex]?.orderNumber);
      }
      setFormList((prev) => prev.filter((_, i) => i !== deleteOrderIndex));
      if (orderNumber) {
        history.push(`?filingNumber=${filingNumber}`);
      }
      setSelectedOrder((prev) => {
        return deleteOrderIndex <= prev ? (prev - 1 >= 0 ? prev - 1 : 0) : prev;
      });
    } catch (error) {
      //show toast of API failed
      // setShowErrorToast()
    }
    setDeleteOrderIndex(null);
  };
  const successModalActionSaveLabel = useMemo(() => {
    if (
      (prevOrder?.orderType === "RESCHEDULE_OF_HEARING_DATE" ||
        (currentOrder?.orderType === "SCHEDULE_OF_HEARING_DATE" &&
          currentOrder?.additionalDetails?.formdata?.namesOfPartiesRequired?.some((data) => data?.partyType?.includes("respondent")))) &&
      isCaseAdmitted
    ) {
      if (currentOrder?.additionalDetails?.isReIssueNotice) {
        return t("ISSUE_NOTICE_BUTTON");
      }
      return t("ISSUE_SUMMONS_BUTTON");
    } else if ((prevOrder?.orderType === "RESCHEDULE_OF_HEARING_DATE" || currentOrder?.orderType === "SCHEDULE_OF_HEARING_DATE") && !isCaseAdmitted) {
      return t("ISSUE_NOTICE_BUTTON");
    }
    return t("CS_COMMON_CLOSE");
  }, [currentOrder, prevOrder?.orderType, t, isCaseAdmitted]);

  const handleGoBackSignatureModal = () => {
    setShowsignatureModal(false);
    setShowReviewModal(true);
  };
  const handleOrderChange = (index) => {
    setSelectedOrder(index);
  };
  const handleDownloadOrders = () => {
    const fileStoreId = localStorage.getItem("fileStoreId");
    downloadPdf(tenantId, signedDoucumentUploadedID || fileStoreId);
    // setShowSuccessModal(false);
    // history.push(`/${window.contextPath}/employee/dristi/home/view-case?tab=${"Orders"}&caseId=${caseDetails?.id}&filingNumber=${filingNumber}`, {
    //   from: "orderSuccessModal",
    // });
  };

  const handleReviewOrderClick = () => {
    if (
      referenceId &&
      "ACCEPTANCE_REJECTION_DCA" === orderType &&
      [SubmissionWorkflowState.COMPLETED, SubmissionWorkflowState.REJECTED].includes(applicationDetails?.status)
    ) {
      setShowErrorToast({
        label: applicationDetails?.status === SubmissionWorkflowState.COMPLETED ? t("DCA_APPLICATION_ACCEPTED") : t("DCA_APPLICATION_REJECTED"),
        error: true,
      });
      return;
    }
    if ("ADMIT_DISMISS_CASE" === orderType && ["CASE_DISMISSED", "CASE_ADMITTED"].includes(caseDetails?.status)) {
      setShowErrorToast({
        label: "CASE_ADMITTED" === caseDetails?.status ? t("CASE_ALREADY_ADMITTED") : t("CASE_ALREADY_REJECTED"),
        error: true,
      });
      return;
    }

    if (["SCHEDULE_OF_HEARING_DATE", "SCHEDULING_NEXT_HEARING"].includes(orderType) && (isHearingScheduled || isHearingOptout)) {
      setShowErrorToast({
        label: isHearingScheduled ? t("HEARING_IS_ALREADY_SCHEDULED_FOR_THIS_CASE") : t("CURRENTLY_A_HEARING_IS_IN_OPTOUT_STATE"),
        error: true,
      });
      return;
    }
    if (["INITIATING_RESCHEDULING_OF_HEARING_DATE"].includes(orderType) && !isHearingScheduled) {
      setShowErrorToast({
        label: t("CURRENTLY_NO_HEARING_IS_IN_SCHEDULED_STATE"),
        error: true,
      });
      return;
    }
    if (["ASSIGNING_DATE_RESCHEDULED_HEARING"].includes(orderType) && !isHearingOptout) {
      setShowErrorToast({
        label: t("CURRENTLY_NO_HEARING_IS_IN_OPTOUT_STATE"),
        error: true,
      });
      return;
    }
    if (orderType && ["MANDATORY_SUBMISSIONS_RESPONSES"].includes(orderType)) {
      if (!currentFormData?.responseInfo?.responseDeadline && currentFormData?.responseInfo?.isResponseRequired?.code === true) {
        setFormErrors.current("responseDeadline", { message: t("PROPOSED_DATE_CAN_NOT_BE_BEFORE_SUBMISSION_DEADLINE") });
        return;
      }
      if (
        (!currentFormData?.responseInfo?.respondingParty || currentFormData?.responseInfo?.respondingParty?.length === 0) &&
        currentFormData?.responseInfo?.isResponseRequired?.code === true
      ) {
        setFormErrors.current("respondingParty", { message: t("CORE_REQUIRED_FIELD_ERROR") });
        return;
      }
    }
    if (orderType && ["WARRANT"].includes(orderType)) {
      if (!currentFormData?.bailInfo?.noOfSureties && currentFormData?.bailInfo?.isBailable?.code === true) {
        setFormErrors.current("noOfSureties", { message: t("CORE_REQUIRED_FIELD_ERROR") });
        return;
      }
      if (
        (!currentFormData?.bailInfo?.bailableAmount || currentFormData?.bailInfo?.bailableAmount?.slice(-1) === ".") &&
        currentFormData?.bailInfo?.isBailable?.code === true
      ) {
        setFormErrors.current("bailableAmount", { message: t("CS_VALID_AMOUNT_DECIMAL") });
        return;
      }
    }
    if (referenceId && ![SubmissionWorkflowState.PENDINGAPPROVAL, SubmissionWorkflowState.PENDINGREVIEW].includes(applicationDetails?.status)) {
      setShowErrorToast({
        label:
          SubmissionWorkflowState.COMPLETED === applicationDetails?.status
            ? t("SUBMISSION_ALREADY_ACCEPTED")
            : SubmissionWorkflowState.REJECTED === applicationDetails?.status
            ? t("SUBMISSION_ALREADY_REJECTED")
            : t("SUBMISSION_NO_LONGER_VALID"),
        error: true,
      });
      setShowReviewModal(false);
      setShowsignatureModal(false);
      return;
    }
    handleSaveDraft({ showReviewModal: true });
  };

  const handleClose = async () => {
    if (successModalActionSaveLabel === t("CS_COMMON_CLOSE")) {
      history.push(`/${window.contextPath}/employee/dristi/home/view-case?tab=${"Orders"}&caseId=${caseDetails?.id}&filingNumber=${filingNumber}`, {
        from: "orderSuccessModal",
      });
      localStorage.removeItem("fileStoreId");
      setShowSuccessModal(false);
      return;
    }
    if (successModalActionSaveLabel === t("ISSUE_SUMMONS_BUTTON")) {
      await handleIssueSummons(currentOrder?.additionalDetails?.formdata?.hearingDate, newHearingNumber || hearingId || hearingNumber);
      history.push(`/${window.contextPath}/employee/orders/generate-orders?filingNumber=${filingNumber}&orderNumber=${createdSummon}`);
    }
    if (successModalActionSaveLabel === t("ISSUE_NOTICE_BUTTON")) {
      await handleIssueNotice(currentOrder?.additionalDetails?.formdata?.hearingDate, newHearingNumber || hearingId || hearingNumber);
      history.push(`/${window.contextPath}/employee/orders/generate-orders?filingNumber=${filingNumber}&orderNumber=${createdNotice}`);
    }
  };

  const handleCloseSuccessModal = () => {
    history.push(`/${window.contextPath}/employee/dristi/home/view-case?tab=${"Orders"}&caseId=${caseDetails?.id}&filingNumber=${filingNumber}`, {
      from: "orderSuccessModal",
    });
    localStorage.removeItem("fileStoreId");
    setShowSuccessModal(false);
  };

  if (!filingNumber) {
    history.push("/employee/home/home-pending-task");
  }

  if (
    loader ||
    isOrdersLoading ||
    isOrdersFetching ||
    isCaseDetailsLoading ||
    isApplicationDetailsLoading ||
    !ordersData?.list ||
    isHearingLoading ||
    pendingTasksLoading ||
    isCourtIdsLoading ||
    isPublishedOrdersLoading
  ) {
    return <Loader />;
  }

  return (
    <div className="generate-orders">
      <div className="orders-list-main">
        <div className="add-order-button" onClick={handleAddOrder}>{`+ ${t("CS_ADD_ORDER")}`}</div>
        <React.Fragment>
          <style>
            {` .view-order .generate-orders .employeeCard .label-field-pair .field .field-container .component-in-front {
                border-top:1px solid #000 !important ;
                border-bottom:1px solid #000 !important ;
                border-left:1px solid #000 !important ;
                margin-top: 0px; 
          }`}
          </style>

          {formList?.map((item, index) => {
            return (
              <div className={`order-item-main ${selectedOrder === index ? "selected-order" : ""}`}>
                <h1
                  onClick={() => {
                    handleOrderChange(index);
                  }}
                  style={{ cursor: "pointer", flex: 1 }}
                >
                  {t(item?.orderType) || `${t("CS_ORDER")} ${index + 1}`}
                </h1>
                {formList?.length > 1 && (
                  <span
                    onClick={() => {
                      setDeleteOrderIndex(index);
                    }}
                    style={{ cursor: "pointer" }}
                  >
                    <CustomDeleteIcon />
                  </span>
                )}
              </div>
            );
          })}
        </React.Fragment>
      </div>
      <div className="view-order">
        {<Header className="order-header">{`${t("CS_ORDER")} ${selectedOrder + 1}`}</Header>}
        {"NO" === caseDetails?.caseDetails?.delayApplications?.formdata?.[0]?.data?.delayCondonationType?.code &&
          "NOTICE" === currentFormData?.orderType?.code &&
          "Section 223 Notice" === currentFormData?.noticeType?.code &&
          !isDCANoticeGenerated && (
            <div
              className="dca-infobox-message"
              style={{
                display: "flex",
                gap: "8px",
                backgroundColor: "#FEF4F4",
                border: "1px",
                borderColor: "#FCE8E8",
                padding: "8px",
                borderRadius: "8px",
                marginBottom: "24px",
                width: "fit-content",
              }}
            >
              <div className="dca-infobox-icon" style={{}}>
                <WarningInfoIconYellow />{" "}
              </div>
              <div className="dca-infobox-me" style={{}}>
                {t("DCA_NOTICE_NOT_SENT") + ": " + t("DCA_NOTICE_NOT_SENT_MESSAGE")}
              </div>
            </div>
          )}
        {currentFormData?.orderType?.code === "NOTICE" &&
          currentFormData?.noticeType?.code === "DCA Notice" &&
          delayCondonationData?.delayCondonationType?.code === "NO" &&
          isDelayApplicationPending && (
            <div
              className="dca-infobox-message"
              style={{
                display: "flex",
                gap: "8px",
                backgroundColor: "#FEF4F4",
                border: "1px",
                borderColor: "#FCE8E8",
                padding: "8px",
                borderRadius: "8px",
                marginBottom: "24px",
                width: "fit-content",
              }}
            >
              <div className="dca-infobox-icon" style={{}}>
                <WarningInfoIconYellow />{" "}
              </div>
              <div className="dca-infobox-me" style={{}}>
                {t("DELAY_APPLICATION_NOT_SUBMITTED")}
              </div>
            </div>
          )}
        {modifiedFormConfig && (
          <FormComposerV2
            className={"generate-orders"}
            key={`${selectedOrder}=${orderType}`}
            label={t("REVIEW_ORDER")}
            config={modifiedFormConfig}
            defaultValues={defaultValue}
            onFormValueChange={onFormValueChange}
            onSubmit={handleReviewOrderClick}
            onSecondayActionClick={handleSaveDraft}
            secondaryLabel={t("SAVE_AS_DRAFT")}
            showSecondaryLabel={true}
            cardClassName={`order-type-form-composer`}
            actionClassName={"order-type-action"}
            isDisabled={isSubmitDisabled}
          />
        )}
      </div>
      {deleteOrderIndex !== null && (
        <div className="delete-order-warning-modal">
          <OrderDeleteModal
            t={t}
            deleteOrderIndex={deleteOrderIndex}
            setDeleteOrderIndex={setDeleteOrderIndex}
            handleDeleteOrder={handleDeleteOrder}
          />
        </div>
      )}
      {showReviewModal && (
        <OrderReviewModal
          t={t}
          order={currentOrder}
          setShowReviewModal={setShowReviewModal}
          setShowsignatureModal={setShowsignatureModal}
          setOrderPdfFileStoreID={setOrderPdfFileStoreID}
          showActions={canESign}
        />
      )}
      {showsignatureModal && (
        <OrderSignatureModal
          t={t}
          order={currentOrder}
          handleIssueOrder={handleIssueOrder}
          handleGoBackSignatureModal={handleGoBackSignatureModal}
          setSignedDocumentUploadID={setSignedDocumentUploadID}
          orderPdfFileStoreID={orderPdfFileStoreID}
          saveOnsubmitLabel={"ISSUE_ORDER"}
        />
      )}
      {showSuccessModal && (
        <OrderSucessModal
          t={t}
          order={prevOrder}
          handleDownloadOrders={handleDownloadOrders}
          handleClose={handleClose}
          handleCloseSuccessModal={handleCloseSuccessModal}
          actionSaveLabel={successModalActionSaveLabel}
        />
      )}
      {showErrorToast && <Toast error={showErrorToast?.error} label={showErrorToast?.label} isDleteBtn={true} onClose={closeToast} />}
    </div>
  );
};

export default GenerateOrders;
