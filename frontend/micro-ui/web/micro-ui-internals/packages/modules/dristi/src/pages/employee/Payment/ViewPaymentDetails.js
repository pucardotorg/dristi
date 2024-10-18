import { Loader, SubmitBar, ActionBar, CustomDropdown, CardLabel, LabelFieldPair, TextInput } from "@egovernments/digit-ui-react-components";
import React, { useEffect, useMemo, useState } from "react";
import { useTranslation } from "react-i18next";
import { useHistory } from "react-router-dom";
import useSearchCaseService from "../../../hooks/dristi/useSearchCaseService";
import { useToast } from "../../../components/Toast/useToast";
import { DRISTIService } from "../../../services";
import { Urls } from "../../../hooks";
import CustomCopyTextDiv from "../../../components/CustomCopyTextDiv";
import { extractFeeMedium, getFilteredPaymentData, getTaskType } from "../../../Utils";

const paymentTaskType = {
  TASK_SUMMON: "task-summons",
  TASK_NOTICE: "task-notice",
  TASK_WARRANT: "task-warrant",
  TASK_SUMMON_ADVANCE_CARRYFORWARD: "TASK_SUMMON_ADVANCE_CARRYFORWARD",
  TASK_NOTICE_ADVANCE_CARRYFORWARD: "TASK_NOTICE_ADVANCE_CARRYFORWARD",
  ORDER_MANAGELIFECYCLE: "order-default",
  SUMMON_WARRANT_STATUS: "SUMMON_WARRANT_STATUS",
  NOTICE_STATUS: "NOTICE_STATUS",
  ASYNC_ORDER_SUBMISSION_MANAGELIFECYCLE: "application-order-submission-default",
  PAYMENT_PENDING_POST: "PAYMENT_PENDING_POST",
  PAYMENT_PENDING_EMAIL: "PAYMENT_PENDING_EMAIL",
  PAYMENT_PENDING_SMS: "PAYMENT_PENDING_SMS",
};

const paymentOptionConfig = {
  label: "CS_MODE_OF_PAYMENT",
  type: "dropdown",
  name: "selectIdTypeType",
  optionsKey: "name",
  validation: {},
  isMandatory: true,
  mdmsConfig: {
    masterName: "OfflinePaymentMode",
    moduleName: "case",
    select: "(data) => {return data['case'].OfflinePaymentMode?.map((item) => {return item;});}",
  },
  styles: {
    width: "100%",
    maxWidth: "100%",
  },
};

const handleTaskSearch = async (businessService, consumerCodeWithoutSuffix, tenantId) => {
  if (["task-summons", "task-notice", "task-warrant"].includes(businessService)) {
    const {
      list: [tasksData],
    } = await Digit.HearingService.searchTaskList({
      criteria: {
        tenantId: tenantId,
        taskNumber: consumerCodeWithoutSuffix,
      },
    });
    return { tasksData };
  }
  return {};
};

const ViewPaymentDetails = ({ location, match }) => {
  const { t } = useTranslation();
  const todayDate = new Date().getTime();
  const dayInMillisecond = 24 * 3600 * 1000;
  const history = useHistory();
  const tenantId = window?.Digit.ULBService.getCurrentTenantId();
  const [payer, setPayer] = useState("");
  const [modeOfPayment, setModeOfPayment] = useState(null);
  const [additionDetails, setAdditionalDetails] = useState("");
  const toast = useToast();
  const [isDisabled, setIsDisabled] = useState(false);
  const { caseId, filingNumber, consumerCode, businessService, paymentType } = window?.Digit.Hooks.useQueryParams();
  const ordersService = Digit.ComponentRegistryService.getComponent("OrdersService") || {};

  const consumerCodeWithoutSuffix = consumerCode.split("_")[0];
  const [tasksData, setTasksData] = useState(null);

  const { data: caseData, isLoading: isCaseSearchLoading } = useSearchCaseService(
    {
      criteria: [
        caseId
          ? {
              caseId,
            }
          : {
              filingNumber,
            },
      ],
      tenantId,
    },
    {},
    "dristi",
    caseId || filingNumber,
    caseId || filingNumber
  );

  useEffect(() => {
    const fetchTaskData = async () => {
      const { tasksData = {} } = await handleTaskSearch(businessService, consumerCodeWithoutSuffix, tenantId);
      setTasksData(tasksData);
    };
    fetchTaskData();
  }, [businessService, consumerCode, consumerCodeWithoutSuffix, tenantId]);
  const summonsPincode = useMemo(() => tasksData?.taskDetails?.respondentDetails?.address?.pincode, [tasksData]);
  const channelId = useMemo(() => extractFeeMedium(tasksData?.taskDetails?.deliveryChannels?.channelName || ""), [tasksData]);

  const caseDetails = useMemo(
    () => ({
      ...caseData?.criteria?.[0]?.responseList?.[0],
    }),
    [caseData]
  );
  const { data: paymentDetails, isLoading: isFetchBillLoading } = Digit.Hooks.useFetchBillsForBuissnessService(
    {
      tenantId: tenantId,
      consumerCode: consumerCode,
      businessService: businessService,
    },
    {
      enabled: Boolean(tenantId && caseDetails?.filingNumber),
    }
  );
  const { data: BillResponse, isLoading: isBillLoading } = Digit.Hooks.dristi.useBillSearch(
    {},
    {
      tenantId,
      consumerCode,
      service: businessService,
    },
    `summons-warrant-notice-bill-${consumerCodeWithoutSuffix}`,
    Boolean(businessService && consumerCodeWithoutSuffix)
  );
  const currentBillDetails = useMemo(() => BillResponse?.Bill?.[0], [BillResponse]);

  const { data: ePostBillResponse, isLoading: isEPOSTBillLoading } = Digit.Hooks.dristi.useBillSearch(
    {},
    {
      tenantId,
      consumerCode: `${consumerCodeWithoutSuffix}_POST_PROCESS`,
      service: businessService,
    },
    `${consumerCodeWithoutSuffix}_POST_PROCESS`,
    Boolean(consumerCodeWithoutSuffix && businessService)
  );

  const isDeliveryPartnerPaid = useMemo(() => (ePostBillResponse?.Bill?.[0]?.status ? ePostBillResponse?.Bill?.[0]?.status === "PAID" : true), [
    ePostBillResponse,
  ]);

  const delayCondonation = useMemo(() => {
    const today = new Date();
    if (!caseDetails?.caseDetails?.["demandNoticeDetails"]?.formdata) {
      return null;
    }
    const dateOfAccrual = new Date(caseDetails?.caseDetails["demandNoticeDetails"]?.formdata[0]?.data?.dateOfAccrual);
    return today?.getTime() - dateOfAccrual?.getTime();
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

  const { data: calculationResponse, isLoading: isPaymentLoading } = Digit.Hooks.dristi.usePaymentCalculator(
    {
      EFillingCalculationCriteria: [
        {
          checkAmount: chequeDetails?.totalAmount,
          numberOfApplication: 1,
          tenantId: tenantId,
          caseId: caseId,
          delayCondonation: delayCondonation,
        },
      ],
    },
    {},
    "dristi",
    Boolean(chequeDetails?.totalAmount && chequeDetails.totalAmount !== "0" && paymentType?.toLowerCase()?.includes("case"))
  );

  const { data: breakupResponse, isLoading: isSummonsBreakUpLoading } = Digit.Hooks.dristi.useSummonsPaymentBreakUp(
    {
      Criteria: [
        {
          channelId: channelId,
          receiverPincode: summonsPincode,
          tenantId: tenantId,
          Id: consumerCodeWithoutSuffix,
          taskType: getTaskType(businessService),
        },
      ],
    },
    {},
    "dristi" + channelId,
    Boolean(!paymentType?.toLowerCase()?.includes("application") && !paymentType?.toLowerCase()?.includes("case") && tasksData && channelId)
  );
  const courtFeeBreakup = useMemo(() => breakupResponse?.Calculation?.[0]?.breakDown?.filter((data) => data?.type === "Court Fee"), [
    breakupResponse?.Calculation,
  ]);
  const totalAmount = useMemo(() => {
    const totalAmount = calculationResponse?.Calculation?.[0]?.totalAmount || currentBillDetails?.totalAmount || 0;
    return parseFloat(totalAmount).toFixed(2);
  }, [calculationResponse?.Calculation, currentBillDetails]);

  const paymentCalculation = useMemo(() => {
    const breakdown = calculationResponse?.Calculation?.[0]?.breakDown || courtFeeBreakup || [];
    const updatedCalculation = breakdown.map((item) => ({
      key: item?.type,
      value: item?.amount,
      currency: "Rs",
    }));

    updatedCalculation.push({
      key: "Total amount",
      value: totalAmount,
      currency: "Rs",
      isTotalFee: true,
    });

    return updatedCalculation;
  }, [calculationResponse?.Calculation, courtFeeBreakup, totalAmount]);
  const payerName = useMemo(() => caseDetails?.additionalDetails?.payerName, [caseDetails?.additionalDetails?.payerName]);
  const bill = paymentDetails?.Bill ? paymentDetails?.Bill[0] : null;

  const onSubmitCase = async () => {
    const consumerCodeWithoutSuffix = consumerCode.split("_")[0];
    let taskFilingNumber = "";
    let taskHearingNumber = "";
    let taskOrderType = "";
    if (["task-notice", "task-summons", "task-warrant"].includes(businessService)) {
      const {
        list: [{ orderNumber, hearingNumber, orderType }],
      } = await ordersService.searchOrder({
        criteria: {
          tenantId: tenantId,
          id: tasksData?.orderId,
        },
      });
      taskHearingNumber = hearingNumber || "";
      taskOrderType = orderType || "";
      taskFilingNumber = tasksData?.filingNumber || caseDetails?.filingNumber;
    }

    const referenceId = consumerCodeWithoutSuffix;
    setIsDisabled(true);
    const regenerateBill = await DRISTIService.callFetchBill({}, { consumerCode: consumerCode, tenantId, businessService: businessService });
    const billFetched = regenerateBill?.Bill ? regenerateBill?.Bill[0] : {};
    if (!Object.keys(bill || regenerateBill || {}).length) {
      toast.error(t("CS_BILL_NOT_AVAILABLE"));
      history.push(`/${window?.contextPath}/employee/dristi/pending-payment-inbox`);
      return;
    }
    try {
      const receiptData = await window?.Digit.PaymentService.createReciept(tenantId, {
        Payment: {
          paymentDetails: [
            {
              businessService: businessService,
              billId: billFetched.id,
              totalDue: billFetched.totalAmount,
              totalAmountPaid: billFetched.totalAmount,
            },
          ],
          tenantId,
          paymentMode: modeOfPayment.code,
          paidBy: "PAY_BY_OWNER",
          mobileNumber: caseDetails?.additionalDetails?.payerMobileNo || "",
          payerName: payer || payerName,
          totalAmountPaid: billFetched.totalAmount || totalAmount,
          instrumentNumber: additionDetails,
          instrumentDate: new Date().getTime(),
        },
      });
      if (isDeliveryPartnerPaid) {
        await DRISTIService.customApiService(Urls.dristi.pendingTask, {
          pendingTask: {
            name: "Pending Payment",
            entityType: businessService,
            referenceId: `MANUAL_${referenceId}`,
            status: "PENDING_PAYMENT",
            cnrNumber: null,
            filingNumber: caseDetails?.filingNumber || taskFilingNumber,
            isCompleted: true,
            stateSla: null,
            additionalDetails: {},
            tenantId,
          },
        });
      }

      if (["task-notice", "task-summons", "task-warrant"].includes(businessService) && isDeliveryPartnerPaid) {
        await DRISTIService.customApiService(Urls.dristi.pendingTask, {
          pendingTask: {
            name: taskOrderType === "SUMMONS" ? "Show Summon-Warrant Status" : "Show Notice Status",
            entityType: paymentTaskType.ORDER_MANAGELIFECYCLE,
            referenceId: taskHearingNumber,
            status: taskOrderType === "SUMMONS" ? paymentTaskType.SUMMON_WARRANT_STATUS : paymentTaskType.NOTICE_STATUS,
            assignedTo: [],
            assignedRole: ["JUDGE_ROLE"],
            cnrNumber: caseDetails?.cnrNumber,
            filingNumber: filingNumber,
            isCompleted: false,
            stateSla: 3 * dayInMillisecond + todayDate,
            additionalDetails: {
              hearingId: taskHearingNumber,
            },
            tenantId,
          },
        });
      }
      history.push(`/${window?.contextPath}/employee/dristi/pending-payment-inbox/response`, {
        state: {
          success: true,
          receiptData: {
            caseInfo: [
              {
                key: "Mode of Payment",
                value: receiptData?.Payments?.[0]?.paymentMode,
                copyData: false,
              },
              {
                key: "Amount",
                value: receiptData?.Payments?.[0]?.totalAmountPaid,
                copyData: false,
              },
              {
                key: "Transaction ID",
                value: receiptData?.Payments?.[0]?.transactionNumber,
                copyData: true,
              },
            ],
            isArrow: false,
            showTable: true,
            showCopytext: true,
          },
          amount: totalAmount,
          fileStoreId: "c162c182-103f-463e-99b6-18654ed7a5b1",
        },
      });
      setIsDisabled(false);
    } catch (err) {
      history.push(`/${window?.contextPath}/employee/dristi/pending-payment-inbox/response`, {
        state: { success: false, amount: totalAmount },
      });
      setIsDisabled(false);
    }
  };

  const orderModalInfo = useMemo(
    () => ({
      caseInfo: [
        {
          key: t("CS_CASE_ID"),
          value: caseDetails?.filingNumber,
          copyData: false,
        },
        {
          key: t("NYAY_PAYMENT_TYPE"),
          value: paymentType,
          copyData: false,
        },
      ],
    }),
    [caseDetails?.filingNumber, paymentType, t]
  );

  if (isCaseSearchLoading || isFetchBillLoading || isPaymentLoading || isBillLoading || isEPOSTBillLoading || isSummonsBreakUpLoading) {
    return <Loader />;
  }
  return (
    <React.Fragment>
      <div className="home-screen-wrapper" style={{ minHeight: "calc(100vh - 90px)", width: "100%", padding: "30px" }}>
        <div className="header-class" style={{ display: "flex", flexDirection: "column", gap: "8px" }}>
          <div className="header">{t("CS_RECORD_PAYMENT_HEADER_TEXT")}</div>
          <div className="sub-header">{t("CS_RECORD_PAYMENT_SUBHEADER_TEXT")}</div>
        </div>
        <div style={{ display: "flex", flexDirection: "row-reverse", gap: 40, justifyContent: "space-between", width: "100%" }}>
          <div className="payment-calculator-wrapper" style={{ width: "33%" }}>
            {getFilteredPaymentData(paymentType, paymentCalculation, bill)?.map((item) => (
              <div
                key={item.key}
                style={{
                  display: "flex",
                  justifyContent: "space-between",
                  alignItems: "center",
                  borderTop: item.isTotalFee && "1px solid #BBBBBD",
                  paddingTop: item.isTotalFee && "20px",
                }}
              >
                <span>{item.key}</span>
                <span>
                  {item.currency} {parseFloat(item.value).toFixed(2)}
                </span>
              </div>
            ))}
          </div>
          <div style={{ width: "63%" }}>
            <div>
              {/* {`${t("CS_CASE_ID")}: ${caseDetails?.filingNumber}`} */}
              <CustomCopyTextDiv
                t={t}
                keyStyle={{ margin: "8px 0px" }}
                valueStyle={{ margin: "8px 0px", fontWeight: 700 }}
                data={orderModalInfo?.caseInfo}
              />
            </div>
            <div className="payment-case-detail-wrapper" style={{ maxHeight: "350px" }}>
              <LabelFieldPair>
                <CardLabel>{`${t("CORE_COMMON_PAYER")}`}</CardLabel>
                <TextInput
                  t={t}
                  style={{ width: "100%" }}
                  textInputStyle={{ width: "100%", maxWidth: "100%" }}
                  type={"text"}
                  isMandatory={false}
                  name="name"
                  disable={true}
                  value={payerName}
                  onChange={(e) => {
                    const { value } = e.target;
                    let updatedValue = value
                      .replace(/[^a-zA-Z\s]/g, "")
                      .trimStart()
                      .replace(/ +/g, " ")
                      .toLowerCase()
                      .replace(/\b\w/g, (char) => char.toUpperCase());
                    setPayer(updatedValue);
                  }}
                />
              </LabelFieldPair>
              <LabelFieldPair style={{ alignItems: "flex-start", fontSize: "16px", fontWeight: 400 }}>
                <CardLabel>{t(paymentOptionConfig.label)}</CardLabel>
                <CustomDropdown
                  label={paymentOptionConfig.label}
                  t={t}
                  onChange={(e) => {
                    setModeOfPayment(e);
                    setAdditionalDetails("");
                  }}
                  value={modeOfPayment}
                  config={paymentOptionConfig}
                ></CustomDropdown>
              </LabelFieldPair>
              {(modeOfPayment?.code === "CHEQUE" || modeOfPayment?.code === "DD") && (
                <LabelFieldPair style={{ alignItems: "flex-start", fontSize: "16px", fontWeight: 400 }}>
                  <CardLabel>{t(modeOfPayment?.code === "CHEQUE" ? t("Cheque number") : t("Demand Draft number"))}</CardLabel>
                  <TextInput
                    t={t}
                    style={{ width: "50%" }}
                    type={"text"}
                    isMandatory={false}
                    name="name"
                    value={additionDetails}
                    onChange={(e) => {
                      const { value } = e.target;

                      let updatedValue = value?.replace(/\D/g, "");
                      if (updatedValue?.length > 6) {
                        updatedValue = updatedValue?.substring(0, 6);
                      }

                      setAdditionalDetails(updatedValue);
                    }}
                  />
                </LabelFieldPair>
              )}
            </div>
          </div>
        </div>
        <ActionBar>
          <SubmitBar
            label={t("CS_GENERATE_RECEIPT")}
            disabled={
              Object.keys(!modeOfPayment ? {} : modeOfPayment).length === 0 ||
              (["CHEQUE", "DD"].includes(modeOfPayment?.code) ? additionDetails.length !== 6 : false) ||
              isDisabled
            }
            onSubmit={() => {
              onSubmitCase();
            }}
          />
        </ActionBar>
      </div>
    </React.Fragment>
  );
};

export default ViewPaymentDetails;
