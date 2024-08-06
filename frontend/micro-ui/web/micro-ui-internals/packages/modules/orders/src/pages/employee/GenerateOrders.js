import React, { useEffect, useMemo, useState } from "react";
import { useTranslation } from "react-i18next";
import { useHistory } from "react-router-dom";
import ReactTooltip from "react-tooltip";
import { Header, FormComposerV2, Toast } from "@egovernments/digit-ui-react-components";
import {
  applicationTypeConfig,
  configRejectSubmission,
  configsAssignDateToRescheduledHearing,
  configsAssignNewHearingDate,
  configsBail,
  configsCaseSettlement,
  configsCaseTransfer,
  configsCaseWithdrawal,
  configsCreateOrderWarrant,
  configsInitiateRescheduleHearingDate,
  configsIssueSummons,
  configsJudgement,
  configsOrderMandatorySubmissions,
  configsOrderSection202CRPC,
  configsOrderSubmissionExtension,
  configsOrderTranferToADR,
  configsOthers,
  configsRejectRescheduleHeadingDate,
  configsRescheduleHearingDate,
  configsScheduleHearingDate,
  configsVoluntarySubmissionStatus,
} from "../../configs/ordersCreateConfig";
import { CustomDeleteIcon } from "../../../../dristi/src/icons/svgIndex";
import OrderReviewModal from "../../pageComponents/OrderReviewModal";
import OrderSignatureModal from "../../pageComponents/OrderSignatureModal";
import OrderDeleteModal from "../../pageComponents/OrderDeleteModal";
import { ordersService } from "../../hooks/services";
import { Loader } from "@egovernments/digit-ui-components";
import OrderSucessModal from "../../pageComponents/OrderSucessModal";
import { applicationTypes } from "../../utils/applicationTypes";
import isEqual from "lodash/isEqual";
import { OrderWorkflowAction, OrderWorkflowState } from "../../utils/orderWorkflow";
import { Urls } from "../../hooks/services/Urls";
import { SubmissionWorkflowAction, SubmissionWorkflowState } from "../../utils/submissionWorkflow";
import { getAdvocates } from "../../utils/caseUtils";
import { HearingWorkflowAction } from "../../utils/hearingWorkflow";
import _ from "lodash";

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

const stateSla = {
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
  BAIL: 3,
  WARRANT: 3,
  WITHDRAWAL: 3,
  OTHERS: 3,
  APPROVE_VOLUNTARY_SUBMISSIONS: 3,
  REJECT_VOLUNTARY_SUBMISSIONS: 3,
  JUDGEMENT: 3,
};

const channelTypeEnum = {
  Post: { code: "POST", type: "Post" },
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
  const history = useHistory();
  const todayDate = new Date().getTime();
  const roles = Digit.UserService.getUser()?.info?.roles;
  const canESign = roles?.some((role) => role.code === "ORDER_ESIGN");
  const setSelectedOrder = (orderIndex) => {
    _setSelectedOrder(orderIndex);
  };

  const closeToast = () => {
    setShowErrorToast(null);
  };

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

  const cnrNumber = useMemo(() => caseDetails?.cnrNumber, [caseDetails]);
  const allAdvocates = useMemo(() => getAdvocates(caseDetails), [caseDetails]);

  const complainants = useMemo(() => {
    return (
      caseDetails?.litigants
        ?.filter((item) => item?.partyType?.includes("complainant"))
        .map((item) => {
          return {
            code: item?.additionalDetails?.fullName,
            name: item?.additionalDetails?.fullName,
            uuid: allAdvocates[item?.additionalDetails?.uuid],
            individualId: item?.individualId,
          };
        }) || []
    );
  }, [caseDetails, allAdvocates]);

  const respondents = useMemo(() => {
    return (
      caseDetails?.litigants
        ?.filter((item) => item?.partyType?.includes("respondent"))
        .map((item) => {
          return {
            code: item?.additionalDetails?.fullName,
            name: item?.additionalDetails?.fullName,
            uuid: allAdvocates[item?.additionalDetails?.uuid],
          };
        }) || []
    );
  }, [caseDetails, allAdvocates]);

  const unJoinedLitigant = useMemo(() => {
    return (
      caseDetails?.additionalDetails?.respondentDetails?.formdata
        ?.filter((data) => !data?.data?.respondentVerification?.individualDetails?.individualId)
        ?.map((data) => {
          const fullName = `${data?.data?.respondentFirstName || ""}${
            data?.data?.respondentMiddleName ? " " + data?.data?.respondentMiddleName + " " : " "
          }${data?.data?.respondentLastName || ""}`.trim();
          return { code: fullName, name: fullName };
        }) || []
    );
  }, [caseDetails]);

  const {
    data: ordersData,
    refetch: refetchOrdersData,
    isLoading: isOrdersLoading,
    isFetching: isOrdersFetching,
  } = Digit.Hooks.orders.useSearchOrdersService(
    {
      tenantId,
      criteria: { filingNumber, applicationNumber: "", cnrNumber, status: OrderWorkflowState.DRAFT_IN_PROGRESS },
      pagination: { limit: 1000, offset: 0 },
    },
    { tenantId },
    filingNumber,
    Boolean(filingNumber && cnrNumber)
  );

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
    return formList.findIndex((order) => order.orderNumber === orderNumber);
  }, [formList, orderNumber]);

  const defaultOrderData = useMemo(
    () => ({
      createdDate: new Date().getTime(),
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
  }, [defaultIndex]);

  const currentOrder = useMemo(() => formList?.[selectedOrder], [formList, selectedOrder]);
  const orderType = useMemo(() => currentOrder?.orderType || {}, [currentOrder]);
  const referenceId = useMemo(() => currentOrder?.additionalDetails?.formdata?.refApplicationId, [currentOrder]);
  const hearingNumber = useMemo(() => currentOrder?.hearingNumber || currentOrder?.additionalDetails?.hearingId, [currentOrder]);

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
  const applicationDetails = useMemo(() => applicationData?.applicationList?.[0], [applicationData]);

  const hearingId = useMemo(() => currentOrder?.hearingNumber || applicationDetails?.additionalDetails?.hearingId, [applicationDetails]);
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
    Boolean(hearingId || hearingNumber)
  );
  const hearingDetails = useMemo(() => hearingsData?.HearingList?.[0], [hearingsData]);

  const modifiedFormConfig = useMemo(() => {
    const configKeys = {
      SECTION_202_CRPC: configsOrderSection202CRPC,
      MANDATORY_SUBMISSIONS_RESPONSES: configsOrderMandatorySubmissions,
      EXTENSION_OF_DOCUMENT_SUBMISSION_DATE: configsOrderSubmissionExtension,
      REFERRAL_CASE_TO_ADR: configsOrderTranferToADR,
      SCHEDULE_OF_HEARING_DATE: configsScheduleHearingDate,
      RESCHEDULE_OF_HEARING_DATE: configsRescheduleHearingDate,
      REJECTION_RESCHEDULE_REQUEST: configsRejectRescheduleHeadingDate,
      INITIATING_RESCHEDULING_OF_HEARING_DATE: configsInitiateRescheduleHearingDate,
      ASSIGNING_DATE_RESCHEDULED_HEARING: configsAssignDateToRescheduledHearing,
      ASSIGNING_NEW_HEARING_DATE: configsAssignNewHearingDate,
      CASE_TRANSFER: configsCaseTransfer,
      SETTLEMENT: configsCaseSettlement,
      SUMMONS: configsIssueSummons,
      BAIL: configsBail,
      WARRANT: configsCreateOrderWarrant,
      WITHDRAWAL: configsCaseWithdrawal,
      OTHERS: configsOthers,
      APPROVE_VOLUNTARY_SUBMISSIONS: configsVoluntarySubmissionStatus,
      REJECT_VOLUNTARY_SUBMISSIONS: configRejectSubmission,
      JUDGEMENT: configsJudgement,
    };
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
      if (orderType === "SCHEDULE_OF_HEARING_DATE") {
        orderTypeForm = orderTypeForm?.map((section) => {
          return {
            ...section,
            body: section.body.map((field) => {
              if (field.key === "namesOfPartiesRequired") {
                return {
                  ...field,
                  populators: {
                    ...field.populators,
                    options: [...complainants, ...respondents, ...unJoinedLitigant],
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
              if (field.key === "respondingParty") {
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
  }, [complainants, currentOrder, orderType, respondents, t, unJoinedLitigant]);

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

  const defaultValue = useMemo(() => {
    if (currentOrder?.orderType && !currentOrder?.additionalDetails?.formdata) {
      return {
        orderType: {
          code: currentOrder?.orderType,
          type: currentOrder?.orderType,
          name: `ORDER_TYPE_${currentOrder?.orderType}`,
        },
      };
    }
    let updatedFormdata = structuredClone(currentOrder?.additionalDetails?.formdata || {});
    if (orderType === "WITHDRAWAL") {
      if (applicationDetails?.applicationType === applicationTypes.WITHDRAWAL) {
        updatedFormdata.applicationOnBehalfOf = applicationDetails?.additionalDetails?.onBehalOfName;
        updatedFormdata.partyType = t(applicationDetails?.additionalDetails?.partyType);
        updatedFormdata.reasonForWithdrawal = t(applicationDetails?.additionalDetails?.formdata?.reasonForWithdrawal?.code);
        updatedFormdata.applicationStatus = t(applicationDetails?.status);
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
        updatedFormdata.date = formatDate(new Date(hearingDetails?.startTime));
      }
    }
    if (
      [
        "RESCHEDULE_OF_HEARING_DATE",
        "REJECTION_RESCHEDULE_REQUEST",
        "APPROVAL_RESCHEDULE_REQUEST",
        "INITIATING_RESCHEDULING_OF_HEARING_DATE",
      ].includes(orderType)
    ) {
      updatedFormdata.originalHearingDate =
        applicationDetails?.additionalDetails?.formdata?.initialHearingDate || currentOrder.additionalDetails?.formdata?.originalHearingDate || "";
    }
    return updatedFormdata;
  }, [currentOrder, orderType, applicationDetails, t, hearingDetails]);

  const onFormValueChange = (setValue, formData, formState, reset, setError, clearErrors, trigger, getValues) => {
    applyMultiSelectDropdownFix(setValue, formData, multiSelectDropdownKeys);
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
                  formData?.comments?.text || formData?.additionalComments?.text || formData?.otherDetails?.text || formData?.sentence?.text || "",
                orderType: formData?.orderType?.code,
                additionalDetails: { ...item?.additionalDetails, formdata: updatedFormData },
              };
        });
      });
    }
    if (Object.keys(formState?.errors).length) {
      setIsSubmitDisabled(true);
    } else {
      setIsSubmitDisabled(false);
    }
  };

  const updateOrder = async (order, action) => {
    try {
      return await ordersService.updateOrder({ order: { ...order, workflow: { ...order.workflow, action, documents: [{}] } } }, { tenantId });
    } catch (error) {
      return null;
    }
  };

  const createOrder = async (order) => {
    try {
      return await ordersService.createOrder({ order }, { tenantId });
    } catch (error) {}
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
    isAssignedRole = false,
    createTask = false,
    taskStatus = "CREATE_SUBMISSION",
    taskName = "",
  }) => {
    const formdata = order?.additionalDetails?.formdata;
    let create = createTask;
    let name = taskName;
    let assignees = [];
    let referenceId = order?.orderNumber;
    let assignedRole = [];
    let additionalDetails = {};
    let entityType =
      formdata?.isResponseRequired?.code === "Yes" ? "async-submission-with-response-managelifecycle" : "async-order-submission-managelifecycle";
    let status = taskStatus;
    if (order?.orderType === "MANDATORY_SUBMISSIONS_RESPONSES") {
      create = true;
      name = t("MAKE_MANDATORY_SUBMISSION");
      assignees = formdata?.submissionParty?.map((party) => party?.uuid.map((uuid) => ({ uuid }))).flat();
    }
    if (order?.orderType === "INITIATING_RESCHEDULING_OF_HEARING_DATE") {
      create = true;
      status = "OPTOUT";
      assignees = Object.values(allAdvocates)
        ?.flat()
        ?.map((uuid) => ({ uuid }));
      name = t("CHOOSE_DATES_FOR_RESCHEDULE_OF_HEARING_DATE");
      entityType = "hearing";
      const promises = assignees.map(async (assignee) => {
        return ordersService.customApiService(Urls.orders.pendingTask, {
          pendingTask: {
            name,
            entityType,
            referenceId: `MANUAL_${assignee?.uuid}_${order?.hearingNumber}`,
            status,
            assignedTo: [assignee],
            assignedRole,
            cnrNumber: cnrNumber,
            filingNumber: filingNumber,
            isCompleted: false,
            stateSla: stateSla?.[order?.orderType] * dayInMillisecond + todayDate,
            additionalDetails: { ...additionalDetails, applicationNumber: order?.additionalDetails?.formdata?.refApplicationId },
            tenantId,
          },
        });
      });
      return await Promise.all(promises);
    }
    if (order?.orderType === "SUMMONS") {
      debugger;
      assignees = [...[...new Set([...Object.keys(allAdvocates)?.flat(), ...Object.values(allAdvocates)?.flat()])]?.map((uuid) => ({ uuid }))];
      debugger;
      if (Array.isArray(order?.additionalDetails?.formdata?.SummonsOrder?.selectedChannels)) {
        entityType = "order-managelifecycle";
        const promises = order?.additionalDetails?.formdata?.SummonsOrder?.selectedChannels?.map(async (channel) => {
          if (channel?.type === "Post") {
            return ordersService.customApiService(Urls.orders.pendingTask, {
              pendingTask: {
                name: t(`MAKE_PAYMENT_FOR_SUMMONS_${channelTypeEnum?.[channel?.type]?.code}`),
                entityType,
                referenceId: `MANUAL_${orderNumber}`,
                status: `PAYMENT_PENDING_${channelTypeEnum?.[channel?.type]?.code}`,
                assignedTo: assignees,
                assignedRole,
                cnrNumber: cnrNumber,
                filingNumber: filingNumber,
                isCompleted: false,
                stateSla: stateSla?.[order?.orderType] * dayInMillisecond + todayDate,
                additionalDetails: { ...additionalDetails, applicationNumber: order?.additionalDetails?.formdata?.refApplicationId },
                tenantId,
              },
            });
          }

          return [];
        });
        return await Promise.all(promises);
      }
    }

    if (isAssignedRole) {
      assignees = [];
      assignedRole = ["JUDGE_ROLE"];
      if (order?.orderType === "SCHEDULE_OF_HEARING_DATE" && refId) {
        referenceId = refId;
        create = true;
        status = "CREATE_SUMMONS_ORDER";
        name = t("CREATE_ORDERS_FOR_SUMMONS");
        entityType = "order-managelifecycle";
        additionalDetails = { ...additionalDetails, orderType: "SUMMONS", hearingID: order?.hearingNumber };
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
          stateSla: stateSla?.[order?.orderType] * dayInMillisecond + todayDate,
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
          entityType: "order-managelifecycle",
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

  const handleApplicationAction = async (order) => {
    try {
      return await ordersService.customApiService(
        `/application/v1/update`,
        {
          application: {
            ...applicationDetails,
            workflow: {
              ...applicationDetails.workflow,
              action: ["REJECTION_RESCHEDULE_REQUEST", "REJECT_VOLUNTARY_SUBMISSIONS"].includes(order?.orderType)
                ? SubmissionWorkflowAction.REJECT
                : SubmissionWorkflowAction.APPROVE,
            },
          },
        },
        { tenantId }
      );
    } catch (error) {
      return false;
    }
  };

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

  const generateAddress = ({ pincode = "", district = "", city = "", state = "", coordinates = { longitude: "", latitude: "" }, locality = "" }) => {
    return `${locality} ${district} ${city} ${state} ${pincode ? ` - ${pincode}` : ""}`.trim();
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
    const orderFormData = orderDetails?.order?.additionalDetails?.formdata?.SummonsOrder?.party?.data;
    const selectedChannel = orderData?.additionalDetails?.formdata?.SummonsOrder?.selectedChannels;
    const respondentAddress = generateAddress({ ...orderFormData?.addressDetails?.[0]?.addressDetails });
    const respondentName = `${orderFormData?.respondentFirstName || ""}${
      orderFormData?.respondentMiddleName ? " " + orderFormData?.respondentMiddleName + " " : " "
    }${orderFormData?.respondentLastName || ""}`.trim();

    const respondentPhoneNo = orderFormData?.phonenumbers?.mobileNumber?.join(", ") || "";
    const respondentEmail = orderFormData?.emails?.email?.join(", ") || "";
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
    const complainantAddress = generateAddress({
      pincode: pincode,
      district: addressLine2,
      city: city,
      state: addressLine1,
      coordinates: {
        longitude: latitude,
        latitude: longitude,
      },
      locality: address,
    });
    const courtDetails = courtRoomData?.Court_Rooms?.find((data) => data?.code === caseDetails?.courtId);
    switch (orderType) {
      case "SUMMONS":
        payload = {
          summonDetails: {
            issueDate: orderData?.auditDetails?.lastModifiedTime,
          },
          respondentDetails: {
            name: respondentName,
            address: respondentAddress,
            phone: respondentPhoneNo,
            email: respondentEmail,
            age: "",
            gender: "",
          },
          complainantDetails: {
            name: complainantName,
            address: complainantAddress,
          },
          caseDetails: {
            title: caseDetails?.caseTitle,
            year: new Date(caseDetails).getFullYear(),
            hearingDate: new Date(orderData?.additionalDetails?.formData?.date || "").getTime(),
            judgeName: "",
            courtName: courtDetails?.name,
            courtAddress: courtDetails?.address,
            courtPhone: courtDetails?.phone,
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
          respondentDetails: {
            name: respondentName,
            address: respondentAddress,
            phone: respondentPhoneNo,
            email: respondentEmail,
            age: "",
            gender: "",
          },
          caseDetails: {
            title: caseDetails?.caseTitle,
            year: new Date(caseDetails).getFullYear(),
            hearingDate: new Date(orderData?.additionalDetails?.formData?.date || "").getTime(),
            judgeName: "",
            courtName: courtDetails?.name,
            courtAddress: courtDetails?.address,
            courtPhone: courtDetails?.phone,
          },
          deliveryChannel: {
            name: "",
            address: "",
            phone: "",
            email: "",
            status: "",
            statusChangeDate: "",
            fees: "",
            feesStatus: "",
          },
        };
        break;
      case "BAIL":
        payload = {
          respondentDetails: {
            name: respondentName,
            address: respondentAddress,
            phone: respondentPhoneNo,
            email: respondentEmail,
            age: "",
            gender: "",
          },
          caseDetails: {
            title: caseDetails?.caseTitle,
            year: new Date(caseDetails).getFullYear(),
            hearingDate: new Date(orderData?.additionalDetails?.formData?.date || "").getTime(),
            judgeName: "",
            courtName: courtDetails?.name,
            courtAddress: courtDetails?.address,
            courtPhone: courtDetails?.phone,
          },
        };
        break;
      default:
        break;
    }
    if (Object.keys(payload || {}).length > 0 && Array.isArray(selectedChannel)) {
      selectedChannel.forEach(async (item) => {
        if ("deliveryChannels" in payload) {
          payload.deliveryChannels = {
            ...payload.deliveryChannels,
            channelName: channelTypeEnum?.[item?.type]?.type,
          };
        }
        if ("deliveryChannel" in payload) {
          payload.deliveryChannel = {
            ...payload.deliveryChannel,
            channelName: channelTypeEnum?.[item?.type]?.type,
          };
        }
        await ordersService.customApiService(Urls.orders.taskCreate, {
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
            createdDate: formatDate(new Date(), "DD-MM-YYYY"),
            orderId: orderData?.id,
            filingNumber,
            cnrNumber,
            taskType: orderType,
            status: "INPROGRESS",
            tenantId,
            amount: {
              type: "FINE",
              status: "DONE",
              amount: "100",
            },
          },
          tenantId,
        });
      });
    }
  };

  const handleIssueOrder = async () => {
    try {
      setLoader(true);
      let newhearingId = "";
      setPrevOrder(currentOrder);
      if (orderType === "SCHEDULE_OF_HEARING_DATE") {
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
              hearingType: currentOrder?.additionalDetails?.formdata?.hearingPurpose?.type,
              status: true,
              attendees: [
                ...currentOrder?.additionalDetails?.formdata?.namesOfPartiesRequired.map((attendee) => {
                  return { name: attendee.name, individualId: attendee.individualId, type: "Complainant" };
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
        setCreatedHearing({ hearingId: newhearingId, startDate: currentOrder?.additionalDetails?.formdata?.hearingDate });
        await createPendingTask({ order: currentOrder, refId: newhearingId, isAssignedRole: true });
      }
      if (orderType === "RESCHEDULE_OF_HEARING_DATE") {
        await handleUpdateHearing({
          action: HearingWorkflowAction.BULK_RESCHEDULE,
          startTime: Date.parse(currentOrder?.additionalDetails?.formdata?.newHearingDate),
          endTime: Date.parse(currentOrder?.additionalDetails?.formdata?.newHearingDate),
        });
        if (currentOrder?.additionalDetails?.isReIssueSummons) {
          setCreatedHearing({
            hearingId: hearingId || hearingNumber,
            startTime: Date.parse(currentOrder?.additionalDetails?.formdata?.newHearingDate),
          });
        }
      }
      if (orderType === "INITIATING_RESCHEDULING_OF_HEARING_DATE") {
        await handleUpdateHearing({
          action: HearingWorkflowAction.RESCHEDULE,
          startTime: Date.parse(currentOrder?.additionalDetails?.formdata?.newHearingDate),
          endTime: Date.parse(currentOrder?.additionalDetails?.formdata?.newHearingDate),
        });
      }
      referenceId && (await handleApplicationAction(currentOrder));
      const orderResponse = await updateOrder(
        { ...currentOrder, ...(newhearingId && { hearingNumber: newhearingId || hearingNumber }) },
        OrderWorkflowAction.ESIGN
      );
      createPendingTask({ order: { ...currentOrder, ...(newhearingId && { hearingNumber: newhearingId || hearingNumber }) } });
      currentOrder?.additionalDetails?.formdata?.refApplicationId && closeManualPendingTask(currentOrder?.orderNumber);
      createTask(orderType, caseDetails, orderResponse);
      setLoader(false);
      setShowSuccessModal(true);
    } catch (error) {
      showErrorToast({ label: t("INTERNAL_ERROR_OCCURRED"), error: true });
      setLoader(false);
    }
  };

  const handleDeleteOrder = async () => {
    try {
      if (formList[deleteOrderIndex]?.orderNumber) {
        await updateOrder(formList[deleteOrderIndex], OrderWorkflowAction.ABANDON);
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
      (prevOrder?.orderType === "RESCHEDULE_OF_HEARING_DATE") & prevOrder?.additionalDetails?.isReIssueSummons ||
      (currentOrder?.orderType === "SCHEDULE_OF_HEARING_DATE" &&
        currentOrder?.additionalDetails?.formdata?.namesOfPartiesRequired?.some((data) => !data?.uuid))
    ) {
      return t("ISSUE_SUMMONS_BUTTON");
    }
    return t("CS_COMMON_CLOSE");
  }, [currentOrder, prevOrder?.additionalDetails?.isReIssueSummons, prevOrder?.orderType, t]);

  const handleGoBackSignatureModal = () => {
    setShowsignatureModal(false);
    setShowReviewModal(true);
  };
  const handleOrderChange = (index) => {
    setSelectedOrder(index);
  };
  const handleDownloadOrders = () => {
    // setShowSuccessModal(false);
    // history.push(`/${window.contextPath}/employee/dristi/home/view-case?tab=${"Orders"}&caseId=${caseDetails?.id}&filingNumber=${filingNumber}`, {
    //   from: "orderSuccessModal",
    // });
  };

  const handleReviewOrderClick = () => {
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

  const handleIssueSummonClick = async () => {
    try {
      const reqbody = {
        order: {
          createdDate: new Date().getTime(),
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
          hearingNumber: createdHearing?.hearingId,
          additionalDetails: {
            formdata: {
              orderType: {
                code: "SUMMONS",
                type: "SUMMONS",
                name: "ORDER_TYPE_SUMMONS",
              },
              date: createdHearing?.startDate,
            },
          },
        },
      };
      const res = await ordersService.createOrder(reqbody, { tenantId });
      await closeManualPendingTask(createdHearing?.hearingId);
      await createPendingTask({
        order: res?.order,
        isAssignedRole: true,
        createTask: true,
        taskStatus: "DRAFT_IN_PROGRESS",
        taskName: t("DRAFT_IN_PROGRESS_ISSUE_SUMMONS"),
      });
      history.push(`/${window.contextPath}/employee/orders/generate-orders?filingNumber=${filingNumber}&orderNumber=${res?.order?.orderNumber}`);
    } catch (error) {}
  };

  const handleClose = async () => {
    if (successModalActionSaveLabel === t("CS_COMMON_CLOSE")) {
      history.push(`/${window.contextPath}/employee/dristi/home/view-case?tab=${"Orders"}&caseId=${caseDetails?.id}&filingNumber=${filingNumber}`, {
        from: "orderSuccessModal",
      });
      setShowSuccessModal(false);
      return;
    }
    if (successModalActionSaveLabel === t("ISSUE_SUMMONS_BUTTON")) {
      await handleIssueSummonClick();
    }
  };

  if (!filingNumber) {
    history.push("/employee/home/home-pending-task");
  }

  if (loader || isOrdersLoading || isOrdersFetching || isCaseDetailsLoading || isApplicationDetailsLoading || !ordersData?.list || isHearingLoading) {
    return <Loader />;
  }

  return (
    <div className="generate-orders">
      <div className="orders-list-main">
        <div className="add-order-button" onClick={handleAddOrder}>{`+ ${t("CS_ADD_ORDER")}`}</div>
        <React.Fragment>
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
          showActions={canESign}
        />
      )}
      {showsignatureModal && (
        <OrderSignatureModal t={t} order={currentOrder} handleIssueOrder={handleIssueOrder} handleGoBackSignatureModal={handleGoBackSignatureModal} />
      )}
      {showSuccessModal && (
        <OrderSucessModal
          t={t}
          order={prevOrder}
          handleDownloadOrders={handleDownloadOrders}
          handleClose={handleClose}
          actionSaveLabel={successModalActionSaveLabel}
        />
      )}
      {showErrorToast && <Toast error={showErrorToast?.error} label={showErrorToast?.label} isDleteBtn={true} onClose={closeToast} />}
    </div>
  );
};

export default GenerateOrders;
