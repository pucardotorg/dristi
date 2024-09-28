import React, { useMemo, useState } from "react";
import { Modal, Button, CardText, RadioButtons, CardLabel, LabelFieldPair } from "@egovernments/digit-ui-react-components";
import { useTranslation } from "react-i18next";
import { useHistory } from "react-router-dom/cjs/react-router-dom.min";
import { InfoCard } from "@egovernments/digit-ui-components";
import ApplicationInfoComponent from "../../components/ApplicationInfoComponent";
import DocumentModal from "../../components/DocumentModal";
import { formatDate } from "../../../../hearings/src/utils";
import usePaymentProcess from "../../../../home/src/hooks/usePaymentProcess";
import { DRISTIService } from "@egovernments/digit-ui-module-dristi/src/services";
import { ordersService } from "../../hooks/services";
import { Urls } from "../../hooks/services/Urls";
import { useEffect } from "react";
import { paymentType } from "../../utils/paymentType";
import { taskService } from "../../hooks/services";

const modeOptions = [
  { label: "E-Post (3-5 days)", value: "e-post" },
  { label: "Registered Post (10-15 days)", value: "registered-post" },
];

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

const PaymentForSummonComponent = ({ infos, links, feeOptions, orderDate, paymentLoader, orderType }) => {
  const { t } = useTranslation();
  const CustomErrorTooltip = window?.Digit?.ComponentRegistryService?.getComponent("CustomErrorTooltip");

  const [selectedOption, setSelectedOption] = useState({});

  const getDateWithMonthName = (orderDate) => {
    let today = new Date();
    today.setDate(today.getDate() + 15);
    const monthNames = ["Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"];
    let dd = String(today.getDate()).padStart(2, "0");
    let mm = monthNames[today.getMonth()];
    let yyyy = today.getFullYear();
    let formattedDate = `${dd} ${mm} ${yyyy}`;
    return formattedDate; // Output: formatted date 15 days from now with month name
  };

  return (
    <div className="payment-for-summon">
      <InfoCard
        variant={"warning"}
        label={"Complete in 2 days"}
        additionalElements={[
          <p>
            {t(orderType === "SUMMONS" ? "SUMMON_DELIVERY_NOTE" : "NOTICE_DELIVERY_NOTE")}{" "}
            <span style={{ fontWeight: "bold" }}>{getDateWithMonthName(orderDate)}</span> {t("ON_TIME_DELIVERY")}
          </p>,
        ]}
        inline
        textStyle={{}}
        className={`custom-info-card warning`}
      />
      <ApplicationInfoComponent infos={infos} links={links} />
      <LabelFieldPair className="case-label-field-pair">
        <div className="join-case-tooltip-wrapper">
          <CardLabel className="case-input-label">{t("Select preferred mode of post to pay")}</CardLabel>
          <CustomErrorTooltip message={t("Select date")} showTooltip={true} icon />
        </div>
        <RadioButtons
          additionalWrapperClass="mode-of-post-pay"
          options={modeOptions}
          selectedOption={selectedOption}
          optionsKey={"label"}
          onSelect={(value) => setSelectedOption(value)}
          disabled={paymentLoader}
        />
      </LabelFieldPair>
      {selectedOption?.value && (
        <div className="summon-payment-action-table">
          {feeOptions[selectedOption?.value]?.map((action, index) => (
            <div className={`${index === 0 ? "header-row" : "action-row"}`}>
              <div className="payment-label">{t(action?.label)}</div>
              <div className="payment-amount">{action?.action !== "offline-process" && action?.amount ? `Rs. ${action?.amount}/-` : "-"}</div>
              <div className="payment-action">
                {index === 0 ? (
                  t(action?.action)
                ) : action?.action !== "offline-process" ? (
                  <Button label={t(action.action)} onButtonClick={action.onClick} isDisabled={paymentLoader} />
                ) : (
                  <p className="offline-process-text">
                    {t("THIS_OFFLINE_TEXT")} <span className="learn-more-text">{t("LEARN_MORE")}</span>
                  </p>
                )}
              </div>
            </div>
          ))}
        </div>
      )}
    </div>
  );
};

const PaymentForSummonModal = ({ path }) => {
  const history = useHistory();
  const { filingNumber, taskNumber } = Digit.Hooks.useQueryParams();
  const tenantId = Digit.ULBService.getCurrentTenantId();
  const [caseId, setCaseId] = useState();

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
    Boolean(filingNumber)
  );

  const isCaseAdmitted = useMemo(() => caseData?.criteria?.[0]?.responseList?.[0]?.status === "CASE_ADMITTED", [caseData]);

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

  const onViewOrderClick = () => {
    console.log(caseId, "caseID");
    history.push(
      `/${window.contextPath}/citizen/dristi/home/view-case?caseId=${caseData?.criteria?.[0]?.responseList?.[0]?.id}&filingNumber=${filingNumber}&tab=Orders`
    );
  };

  console.log("caseData :>> ", caseData?.criteria?.[0]?.responseList?.[0]?.id);
  const todayDate = new Date().getTime();
  const dayInMillisecond = 24 * 3600 * 1000;

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

  const orderType = useMemo(() => orderData?.list?.[0]?.orderType, [orderData]);

  const { data: hearingsData } = Digit.Hooks.hearings.useGetHearings(
    {
      hearing: { tenantId },
      criteria: {
        tenantID: tenantId,
        filingNumber: filingNumber,
        hearingId: orderData?.list?.[0]?.hearingNumber,
      },
    },
    { applicationNumber: "", cnrNumber: "" },
    orderData?.list?.[0]?.hearingNumber,
    Boolean(orderData?.list?.[0]?.hearingNumber)
  );

  const consumerCode = useMemo(() => {
    return filteredTasks?.[0]?.taskNumber ? `${filteredTasks?.[0]?.taskNumber}_POST_COURT` : undefined;
  }, [filteredTasks]);

  const { fetchBill, openPaymentPortal, paymentLoader, showPaymentModal, setShowPaymentModal, billPaymentStatus } = usePaymentProcess({
    tenantId,
    consumerCode: consumerCode,
    service: orderType === "SUMMONS" ? paymentType.TASK_SUMMON : paymentType.TASK_NOTICE,
    caseDetails,
    totalAmount: "4",
  });

  const { data: courtBillResponse, isLoading: isCourtBillLoading } = Digit.Hooks.dristi.useBillSearch(
    {},
    {
      tenantId,
      consumerCode: `${filteredTasks?.[0]?.taskNumber}_POST_COURT`,
      service: orderType === "SUMMONS" ? paymentType.TASK_SUMMON : paymentType.TASK_NOTICE,
    },
    "dristi",
    Boolean(filteredTasks?.[0]?.taskNumber)
  );
  const { data: ePostBillResponse, isLoading: isEPOSTBillLoading } = Digit.Hooks.dristi.useBillSearch(
    {},
    {
      tenantId,
      consumerCode: `${filteredTasks?.[0]?.taskNumber}_POST_PROCESS`,
      service: orderType === "SUMMONS" ? paymentType.TASK_SUMMON : paymentType.TASK_NOTICE,
    },
    "dristi",
    Boolean(filteredTasks?.[0]?.taskNumber)
  );

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

  const onPayOnline = async () => {
    console.log("clikc");
    try {
      // if (courtBillResponse?.Bill?.length === 0) {
      //   await DRISTIService.createDemand({
      //     Demands: [
      //       {
      //         tenantId,
      //         consumerCode: `${filteredTasks?.[0]?.taskNumber}_POST_COURT`,
      //         consumerType: paymentType.TASK_SUMMON,
      //         businessService: paymentType.TASK_SUMMON,
      //         taxPeriodFrom: Date.now().toString(),
      //         taxPeriodTo: Date.now().toString(),
      //         demandDetails: [
      //           {
      //             taxHeadMasterCode: paymentType.TASK_SUMMON_ADVANCE_CARRYFORWARD,
      //             taxAmount: 4,
      //             collectionAmount: 0,
      //           },
      //         ],
      //       },
      //     ],
      //   });
      // }
      // const bill = await fetchBill(`${filteredTasks?.[0]?.taskNumber}_POST_COURT`, tenantId, paymentType.TASK_SUMMON);
      // if (bill?.Bill?.length) {
      //   const billPaymentStatus = await openPaymentPortal(bill);
      //   console.log(billPaymentStatus);
      //   if (billPaymentStatus === true) {
      //     console.log("YAAAYYYYY");
      // const fileStoreId = await DRISTIService.fetchBillFileStoreId({}, { billId: bill?.Bill?.[0]?.id, tenantId });

      const updatedTask = {
        ...filteredTasks?.[0], // Keep all the existing properties
        taskType: orderType === "SUMMONS" ? "SUMMONS" : "NOTICE", // Change the taskType to SUMMON
        workflow: {
          // Add the workflow object with the desired action
          action: "MAKE_PAYMENT",
        },
      };

      await taskService
        .updateTask(
          {
            task: updatedTask,
            tenantId: tenantId,
          },
          {}
        )
        .then(() => {
          return Promise.all([
            ordersService.customApiService(Urls.orders.pendingTask, {
              pendingTask: {
                name: orderType === "SUMMONS" ? "Show Summon-Warrant Status" : "Show Notice Status",
                entityType: paymentType.ORDER_MANAGELIFECYCLE,
                referenceId: hearingsData?.HearingList?.[0]?.hearingId,
                status: orderType === "SUMMONS" ? paymentType.SUMMON_WARRANT_STATUS : paymentType.NOTICE_STATUS,
                assignedTo: [],
                assignedRole: ["JUDGE_ROLE"],
                cnrNumber: filteredTasks?.[0]?.cnrNumber,
                filingNumber: filingNumber,
                isCompleted: false,
                stateSla: 3 * dayInMillisecond + todayDate,
                additionalDetails: {
                  hearingId: hearingsData?.list?.[0]?.hearingId,
                },
                tenantId,
              },
            }),
            ordersService.customApiService(Urls.orders.pendingTask, {
              pendingTask: {
                name: orderType === "SUMMONS" ? `MAKE_PAYMENT_FOR_SUMMONS_POST` : `MAKE_PAYMENT_FOR_NOTICE_POST`,
                entityType: paymentType.ASYNC_ORDER_SUBMISSION_MANAGELIFECYCLE,
                referenceId: `MANUAL_${taskNumber}`,
                status: paymentType.PAYMENT_PENDING_POST,
                assignedTo: [],
                assignedRole: [],
                cnrNumber: filteredTasks?.[0]?.cnrNumber,
                filingNumber: filingNumber,
                isCompleted: true,
                stateSla: "",
                additionalDetails: {},
                tenantId,
              },
            }),
          ]);
        });

      // fileStoreId &&
      history.push(`/${window?.contextPath}/citizen/home/post-payment-screen`, {
        state: {
          success: true,
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
                value: orderData?.list?.[0]?.orderNumber,
                copyData: false,
              },
              {
                key: "Transaction ID",
                value: filteredTasks?.[0]?.taskNumber,
                copyData: true,
              },
            ],
            isArrow: false,
            showTable: true,
            showCopytext: true,
          },
          fileStoreId: "fileStoreId?.Document?.fileStore",
        },
      });
      //   } else {
      //     console.log("NAAAYYYYY");
      //     history.push(`/${window?.contextPath}/citizen/home/post-payment-screen`, {
      //       state: {
      //         success: false,
      //         receiptData: {
      //           ...mockSubmitModalInfo,
      //           caseInfo: [
      //             {
      //               key: "Case Name & ID",
      //               value: caseDetails?.caseTitle + "," + caseDetails?.filingNumber,
      //               copyData: false,
      //             },
      //             {
      //               key: "ORDER ID",
      //               value: orderData?.list?.[0]?.orderNumber,
      //               copyData: false,
      //             },
      //             {
      //               key: "Transaction ID",
      //               value: filteredTasks?.[0]?.taskNumber,
      //               copyData: true,
      //             },
      //           ],
      //           isArrow: false,
      //           showTable: true,
      //           showCopytext: true,
      //         },
      //         caseId: caseDetails?.filingNumber,
      //       },
      //     });
      //   }
      // }
    } catch (error) {
      console.error(error);
    }
  };

  const onPayOnlineSBI = async () => {
    try {
      if (ePostBillResponse?.Bill?.length === 0) {
        await DRISTIService.createDemand({
          Demands: [
            {
              tenantId,
              consumerCode: `${filteredTasks?.[0]?.taskNumber}_POST_PROCESS`,
              consumerType: orderType === "SUMMONS" ? paymentType.TASK_SUMMON : paymentType.TASK_NOTICE,
              businessService: orderType === "SUMMONS" ? paymentType.TASK_SUMMON : paymentType.TASK_NOTICE,
              taxPeriodFrom: Date.now().toString(),
              taxPeriodTo: Date.now().toString(),
              demandDetails: [
                {
                  taxHeadMasterCode:
                    orderType === "SUMMONS" ? paymentType.TASK_SUMMON_ADVANCE_CARRYFORWARD : paymentType.TASK_NOTICE_ADVANCE_CARRYFORWARD,
                  taxAmount: 4,
                  collectionAmount: 0,
                },
              ],
            },
          ],
        });
      }
      const bill = await fetchBill(
        `${filteredTasks?.[0]?.taskNumber}_POST_PROCESS`,
        tenantId,
        orderType === "SUMMONS" ? paymentType.TASK_SUMMON : paymentType.TASK_NOTICE
      );
      history.push(`/${window?.contextPath}/citizen/home/sbi-epost-payment`, {
        state: {
          billData: bill,
          serviceNumber: filteredTasks?.[0]?.taskNumber,
          businessService: orderType === "SUMMONS" ? paymentType.TASK_SUMMON : paymentType.TASK_NOTICE,
          caseDetails: caseDetails,
          consumerCode: `${filteredTasks?.[0]?.taskNumber}_POST_PROCESS`,
          orderData: orderData,
          filteredTasks: filteredTasks,
          filingNumber: filingNumber,
        },
      });
    } catch (error) {
      console.error(error);
    }
  };

  const feeOptions = useMemo(() => {
    const taskAmount = filteredTasks?.[0]?.amount?.amount || 0;

    return {
      "e-post": [
        {
          label: "Fee Type",
          amount: "Amount",
          action: "Actions",
        },
        { label: "Court Fees", amount: taskAmount, action: "Pay Online", onClick: onPayOnline },
        { label: "Delivery Partner Fee", amount: taskAmount, action: "Pay Online", onClick: onPayOnlineSBI },
      ],
      "registered-post": [
        {
          label: "Fee Type",
          amount: "Amount",
          action: "Actions",
        },
        { label: "Court Fees", amount: taskAmount, action: "Pay Online", onClick: onPayOnline },
        { label: "Delivery Partner Fee", amount: taskAmount, action: "offline-process", onClick: onPayOnline },
      ],
    };
  }, [filteredTasks, onPayOnline]);

  const handleClose = () => {
    if (paymentLoader === false) {
      history.goBack();
    }
  };

  const infos = useMemo(() => {
    const name = [
      orderData?.list?.[0]?.additionalDetails?.formdata?.[orderType === "SUMMONS" ? "SummonsOrder" : "noticeOrder"]?.party?.data?.firstName,
      orderData?.list?.[0]?.additionalDetails?.formdata?.[orderType === "SUMMONS" ? "SummonsOrder" : "noticeOrder"]?.party?.data?.lastName,
    ]
      ?.filter(Boolean)
      ?.join(" ");
    const addressDetails =
      orderData?.list?.[0]?.additionalDetails?.formdata?.[orderType === "SUMMONS" ? "SummonsOrder" : "noticeOrder"]?.party?.data?.addressDetails?.[0]
        ?.addressDetails;
    console.log("addressDetails :>> ", addressDetails);
    return [
      { key: "Issued to", value: name },
      { key: "Next Hearing Date", value: formatDate(new Date(hearingsData?.HearingList?.[0]?.startTime)) },
      {
        key: "Delivery Channel",
        value: `Post (${addressDetails?.locality}, ${addressDetails?.city}, ${addressDetails?.district}, ${addressDetails?.state}, ${addressDetails?.pincode})`,
      },
    ];
  }, [hearingsData?.HearingList, orderData?.list]);

  const orderDate = useMemo(() => {
    return hearingsData?.HearingList?.[0]?.startTime;
  }, [hearingsData?.HearingList]);

  const links = useMemo(() => {
    return [{ text: "View order", link: "", onClick: onViewOrderClick }];
  }, [caseData]);

  const paymentForSummonModalConfig = useMemo(() => {
    return {
      handleClose: handleClose,
      heading: { label: `Payment for ${orderType === "SUMMONS" ? "Summons" : "Notice"} via post` },
      isStepperModal: false,
      modalBody: (
        <PaymentForSummonComponent
          infos={infos}
          links={links}
          feeOptions={feeOptions}
          orderDate={orderDate}
          paymentLoader={paymentLoader}
          isCaseAdmitted={isCaseAdmitted}
          orderType={orderType}
        />
      ),
    };
  }, [feeOptions, infos, links]);

  return <DocumentModal config={paymentForSummonModalConfig} />;
};

export default PaymentForSummonModal;
