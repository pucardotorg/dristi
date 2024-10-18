import React, { useMemo, useState } from "react";
import { Button, Loader } from "@egovernments/digit-ui-react-components";
import { useTranslation } from "react-i18next";
import { useHistory } from "react-router-dom/cjs/react-router-dom.min";
import ApplicationInfoComponent from "../../components/ApplicationInfoComponent";
import DocumentModal from "../../components/DocumentModal";
import { formatDate } from "../../../../hearings/src/utils";
import usePaymentProcess from "../../../../home/src/hooks/usePaymentProcess";
import { DRISTIService } from "@egovernments/digit-ui-module-dristi/src/services";
import { ordersService } from "../../hooks/services";
import { Urls } from "../../hooks/services/Urls";
import { useEffect } from "react";
import { paymentType } from "../../utils/paymentType";
import { extractFeeMedium, getTaskType } from "@egovernments/digit-ui-module-dristi/src/Utils";
import { getSuffixByDeliveryChannel } from "../../utils";

const submitModalInfo = {
  header: "CS_HEADER_FOR_SUMMON_POST",
  subHeader: "CS_SUBHEADER_TEXT_FOR_Summon_POST",
  caseInfo: [
    {
      key: "Case Number",
      value: "FSM-2019-04-23-898898",
    },
  ],
  isArrow: false,
  showTable: true,
};

const orderTypeEnum = {
  SUMMONS: "Summons",
  NOTICE: "Notice",
  WARRANT: "Warrant",
};

const PaymentForSummonComponent = ({ infos, links, feeOptions, orderDate, paymentLoader, channelId, formattedChannelId, orderType }) => {
  const { t } = useTranslation();
  const CustomErrorTooltip = window?.Digit?.ComponentRegistryService?.getComponent("CustomErrorTooltip");
  const [selectedOption, setSelectedOption] = useState({});

  const getDateWithMonthName = (orderDate) => {
    let today = new Date();

    today.setDate(today.getDate() - 15);

    // Array of month names
    const monthNames = ["Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"];

    let dd = String(today.getDate()).padStart(2, "0");
    let mm = monthNames[today.getMonth()];
    let yyyy = today.getFullYear();

    let formattedDate = `${dd} ${mm} ${yyyy}`;

    return formattedDate; // Output: formatted date 15 days ago with month name
  };

  return (
    <div className="payment-for-summon">
      <p style={{ marginTop: "0px", marginBottom: "0px" }}>
        {t("MAKE_PAYMENT_IN_ORDER_TO_SEND_FOLLOWING")} {orderTypeEnum?.[orderType]} via {formattedChannelId}.
      </p>
      <ApplicationInfoComponent infos={infos} links={links} />
      {channelId && feeOptions[channelId]?.length > 0 && (
        <div className="summon-payment-action-table">
          {feeOptions[channelId]?.map((action, index) => (
            <div className={`${index === 0 ? "header-row" : "action-row"}`} key={index}>
              <div className="payment-label">{t(action?.label)}</div>
              <div className="payment-amount">{action?.action !== "offline-process" && action?.amount ? `Rs. ${action?.amount}/-` : "-"}</div>
              <div className="payment-action">
                {!action?.isCompleted &&
                  (index === 0 ? (
                    t(action?.action)
                  ) : action?.action !== "offline-process" ? (
                    <Button label={t(action.action)} onButtonClick={action.onClick} isDisabled={paymentLoader} />
                  ) : (
                    <p className="offline-process-text">
                      This is an offline process. <span className="learn-more-text">Learn More</span>
                    </p>
                  ))}
                {action?.isCompleted && <p>{t("PAYMEND_ALREADY_COMPLETED")}</p>}
              </div>
            </div>
          ))}
        </div>
      )}
    </div>
  );
};

const PaymentForSummonModalSMSAndEmail = ({ path }) => {
  const history = useHistory();
  const { filingNumber, taskNumber } = Digit.Hooks.useQueryParams();
  const tenantId = Digit.ULBService.getCurrentTenantId();
  const [caseId, setCaseId] = useState();
  const { t } = useTranslation();
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

  useEffect(() => {
    if (caseData) {
      const id = caseData?.criteria?.[0]?.responseList?.[0]?.id;
      if (id) {
        console.log(id, "id");
        setCaseId(id); // Set the caseId in state
      } else {
        console.error("caseId is undefined or not available");
      }
    }
  }, [caseData]);

  const caseDetails = useMemo(() => {
    return caseData?.criteria?.[0]?.responseList?.[0];
  }, [caseData]);

  const isCaseAdmitted = useMemo(() => caseDetails?.status === "CASE_ADMITTED", [caseDetails]);

  const mockSubmitModalInfo = useMemo(
    () =>
      isCaseAdmitted
        ? submitModalInfo
        : {
            ...submitModalInfo,
            header: "CS_HEADER_FOR_NOTICE_POST",
            subHeader: "CS_SUBHEADER_TEXT_FOR_NOTICE_POST",
          },
    [isCaseAdmitted]
  );

  const { data: tasksData } = Digit.Hooks.hearings.useGetTaskList(
    {
      criteria: {
        tenantId: tenantId,
        taskNumber: taskNumber,
      },
    },
    {},
    filingNumber,
    Boolean(filingNumber)
  );

  const filteredTasks = useMemo(() => tasksData?.list, [tasksData]);

  const { data: orderData, isloading: isOrdersLoading } = Digit.Hooks.orders.useSearchOrdersService(
    { tenantId, criteria: { id: filteredTasks?.[0]?.orderId } },
    { tenantId },
    filteredTasks?.[0]?.orderId,
    Boolean(filteredTasks?.[0]?.orderId)
  );
  const orderDetails = useMemo(() => orderData?.list?.[0] || {}, [orderData]);
  const summonsPincode = useMemo(() => filteredTasks?.[0]?.taskDetails?.respondentDetails?.address?.pincode, [filteredTasks]);
  const channelId = useMemo(() => extractFeeMedium(filteredTasks?.[0]?.taskDetails?.deliveryChannels?.channelName || ""), [filteredTasks]);

  const orderType = useMemo(() => orderDetails?.orderType, [orderDetails?.orderType]);

  const { data: hearingsData } = Digit.Hooks.hearings.useGetHearings(
    {
      hearing: { tenantId },
      criteria: {
        tenantID: tenantId,
        filingNumber: filingNumber,
        hearingId: orderDetails?.hearingNumber,
      },
    },
    { applicationNumber: "", cnrNumber: "" },
    orderDetails?.hearingNumber,
    Boolean(orderDetails?.hearingNumber)
  );

  const getBusinessService = (orderType) => {
    const businessServiceMap = {
      SUMMONS: paymentType.TASK_SUMMON,
      WARRANT: paymentType.TASK_WARRANT,
      NOTICE: paymentType.TASK_NOTICE,
    };
    return businessServiceMap?.[orderType];
  };

  const businessService = useMemo(() => getBusinessService(orderType), [orderType]);

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

  const suffix = useMemo(() => getSuffixByDeliveryChannel(paymentTypeData, channelId, businessService), [
    businessService,
    channelId,
    paymentTypeData,
  ]);

  const { data: breakupResponse, isLoading: isSummonsBreakUpLoading } = Digit.Hooks.dristi.useSummonsPaymentBreakUp(
    {
      Criteria: [
        {
          channelId: channelId,
          receiverPincode: summonsPincode,
          tenantId: tenantId,
          Id: taskNumber,
          taskType: getTaskType(businessService),
        },
      ],
    },
    {},
    `breakup-response-${summonsPincode}${channelId}${taskNumber}${businessService}`,
    Boolean(filteredTasks && channelId && orderType && taskNumber && businessService)
  );

  const courtFeeAmount = useMemo(() => breakupResponse?.Calculation?.[0]?.breakDown.find((data) => data?.type === "Court Fee")?.amount, [
    breakupResponse,
  ]);

  const { openPaymentPortal, paymentLoader } = usePaymentProcess({
    tenantId,
    consumerCode: `${taskNumber}_${suffix}`,
    service: businessService,
    caseDetails,
    totalAmount: courtFeeAmount,
  });

  const status = useMemo(() => {
    if (channelId === "SMS") {
      return paymentType.PAYMENT_PENDING_SMS;
    } else if (channelId === "EMAIL") {
      return paymentType.PAYMENT_PENDING_EMAIL;
    } else if (channelId === "POLICE") {
      return paymentType.PAYMENT_PENDING_POLICE;
    } else {
      return paymentType.PAYMENT_PENDING_POST;
    }
  }, [channelId]);

  const { data: billResponse, isLoading: isBillLoading } = Digit.Hooks.dristi.useBillSearch(
    {},
    {
      tenantId,
      consumerCode: `${taskNumber}_${suffix}`,
      service: businessService,
    },
    `billResponse-${businessService}${taskNumber}`,
    Boolean(taskNumber && businessService)
  );

  const feeOptions = useMemo(() => {
    const onPayOnline = async () => {
      try {
        if (!billResponse?.Bill?.length) {
          console.log("Bill not found");
          return null;
        }
        const billPaymentStatus = await openPaymentPortal(billResponse);
        if (!billPaymentStatus) {
          console.log("Payment canceled or failed", taskNumber);
          return;
        }
        const resfileStoreId = await DRISTIService.fetchBillFileStoreId({}, { billId: billResponse?.Bill?.[0]?.id, tenantId });
        const fileStoreId = resfileStoreId?.Document?.fileStore;
        const postPaymenScreenObj = {
          state: {
            success: Boolean(fileStoreId),
            receiptData: {
              ...mockSubmitModalInfo,
              caseInfo: [
                {
                  key: "Case Name & ID",
                  value: caseDetails?.caseTitle + "," + caseDetails?.filingNumber,
                  copyData: false,
                },
                {
                  key: "ORDER ID",
                  value: orderDetails?.orderNumber,
                  copyData: false,
                },
                {
                  key: "Transaction ID",
                  value: taskNumber,
                  copyData: true,
                },
              ],
              isArrow: false,
              showTable: true,
              showCopytext: true,
            },
            fileStoreId: fileStoreId || "",
          },
        };
        if (fileStoreId) {
          await ordersService.customApiService(Urls.orders.pendingTask, {
            pendingTask: {
              name: orderType === "WARRANT" ? "PAYMENT_PENDING_FOR_WARRANT" : `MAKE_PAYMENT_FOR_${orderType}_POST`,
              entityType: paymentType.ASYNC_ORDER_SUBMISSION_MANAGELIFECYCLE,
              referenceId: `MANUAL_${taskNumber}`,
              status: status,
              assignedTo: [],
              assignedRole: [],
              cnrNumber: filteredTasks?.[0]?.cnrNumber,
              filingNumber: filingNumber,
              isCompleted: true,
              stateSla: "",
              additionalDetails: {},
              tenantId,
            },
          });
        }
        history.push(`/${window?.contextPath}/citizen/home/post-payment-screen`, postPaymenScreenObj);
      } catch (error) {
        console.error(error);
      }
    };

    return {
      EMAIL: [
        {
          label: "Fee Type",
          amount: "Amount",
          action: "Actions",
        },
        {
          label: "Court Fees",
          amount: courtFeeAmount,
          action: "Pay Online",
          onClick: onPayOnline,
        },
      ],
      SMS: [
        {
          label: "Fee Type",
          amount: "Amount",
          action: "Actions",
        },
        {
          label: "Court Fees",
          amount: courtFeeAmount,
          action: "Pay Online",
          onClick: onPayOnline,
        },
      ],
      POLICE: [
        {
          label: "Fee Type",
          amount: "Amount",
          action: "Actions",
        },
        {
          label: "Court Fees",
          amount: courtFeeAmount,
          action: "Pay Online",
          onClick: onPayOnline,
        },
      ],
    };
  }, [
    courtFeeAmount,
    billResponse,
    openPaymentPortal,
    tenantId,
    mockSubmitModalInfo,
    caseDetails?.caseTitle,
    caseDetails?.filingNumber,
    orderDetails?.orderNumber,
    taskNumber,
    history,
    orderType,
    status,
    filteredTasks,
    filingNumber,
  ]);

  const infos = useMemo(() => {
    const name =
      [
        orderDetails?.additionalDetails?.formdata?.[orderType === "SUMMONS" ? "SummonsOrder" : "noticeOrder"]?.party?.data?.firstName,
        orderDetails?.additionalDetails?.formdata?.[orderType === "SUMMONS" ? "SummonsOrder" : "noticeOrder"]?.party?.data?.lastName,
      ]
        ?.filter(Boolean)
        ?.join(" ") ||
      (orderType === "WARRANT" && orderDetails?.additionalDetails?.formdata?.warrantFor);

    const task = filteredTasks?.[0];
    const taskDetails = task?.taskDetails;
    const deliveryChannel = taskDetails?.deliveryChannels?.channelName || "";

    let contactDetail = "";
    if (deliveryChannel === "Email") {
      contactDetail = taskDetails?.respondentDetails?.email || "Not provided";
    } else if (deliveryChannel === "SMS") {
      contactDetail = taskDetails?.respondentDetails?.phone || "Not provided";
    } else if (deliveryChannel === "Police") {
      contactDetail = taskDetails?.respondentDetails?.phone || "Not provided";
    }

    return [
      { key: "Issued to", value: name },
      { key: "Next Hearing Date", value: formatDate(new Date(hearingsData?.HearingList?.[0]?.startTime)) },
      {
        key: "Delivery Channel",
        value: deliveryChannel ? `${deliveryChannel} (${contactDetail})` : "Not available",
      },
    ];
  }, [orderDetails?.additionalDetails?.formdata, orderType, filteredTasks, hearingsData?.HearingList]);

  const orderDate = useMemo(() => {
    return hearingsData?.HearingList?.[0]?.startTime;
  }, [hearingsData?.HearingList]);

  const links = useMemo(() => {
    const onViewOrderClick = () => {
      console.log(caseId, "caseID");
      history.push(
        `/${window.contextPath}/citizen/dristi/home/view-case?caseId=${caseData?.criteria?.[0]?.responseList?.[0]?.id}&filingNumber=${filingNumber}&tab=Orders`
      );
    };
    return [{ text: "View order", link: "", onClick: onViewOrderClick }];
  }, [caseData?.criteria, caseId, filingNumber, history]);

  const paymentForSummonModalConfig = useMemo(() => {
    const handleClose = () => {
      if (paymentLoader === false) {
        history.goBack();
      }
    };
    const formattedChannelId =
      channelId === "SMS" ? "SMS" : channelId ? channelId?.charAt(0)?.toUpperCase() + channelId?.slice(1)?.toLowerCase() : "";

    return {
      handleClose: handleClose,
      heading: {
        label: `Payment for ${orderTypeEnum?.[orderType]} via ${formattedChannelId}`,
      },
      isStepperModal: false,
      modalBody: (
        <PaymentForSummonComponent
          infos={infos}
          links={links}
          feeOptions={feeOptions}
          orderDate={orderDate}
          paymentLoader={paymentLoader}
          channelId={channelId}
          formattedChannelId={formattedChannelId}
          isCaseAdmitted={isCaseAdmitted}
          orderType={orderType}
        />
      ),
    };
  }, [channelId, feeOptions, history, infos, isCaseAdmitted, links, orderDate, orderType, paymentLoader]);
  if (isOrdersLoading || isPaymentTypeLoading || isSummonsBreakUpLoading || isBillLoading) {
    return <Loader />;
  }
  return <DocumentModal config={paymentForSummonModalConfig} />;
};

export default PaymentForSummonModalSMSAndEmail;
