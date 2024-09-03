import { Loader, SubmitBar, ActionBar, CustomDropdown, CardLabel, LabelFieldPair, TextInput } from "@egovernments/digit-ui-react-components";
import React, { useMemo, useState } from "react";
import { useTranslation } from "react-i18next";
import { useHistory } from "react-router-dom";
import useSearchCaseService from "../../../hooks/dristi/useSearchCaseService";
import { useToast } from "../../../components/Toast/useToast";
import { DRISTIService } from "../../../services";
import { Urls } from "../../../hooks";

const paymentOption = [
  {
    code: "CASH",
    i18nKey: "Cash",
  },
  {
    code: "CHEQUE",
    i18nKey: "Cheque",
  },
  {
    code: "DD",
    i18nKey: "Demand Draft",
  },
  {
    code: "STAMP",
    i18nKey: "Stamp",
  },
];

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
    width: "50%",
  },
};

const ViewPaymentDetails = ({ location, match }) => {
  const { t } = useTranslation();
  const history = useHistory();
  const tenantId = window?.Digit.ULBService.getCurrentTenantId();
  const [payer, setPayer] = useState("");
  const [modeOfPayment, setModeOfPayment] = useState(null);
  const [additionDetails, setAdditionalDetails] = useState("");
  const toast = useToast();
  const [isDisabled, setIsDisabled] = useState(false);
  const { caseId, filingNumber } = window?.Digit.Hooks.useQueryParams();

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

  const caseDetails = useMemo(
    () => ({
      ...caseData?.criteria?.[0]?.responseList?.[0],
    }),
    [caseData]
  );
  const { data: paymentDetails, isLoading: isFetchBillLoading } = Digit.Hooks.useFetchBillsForBuissnessService(
    {
      tenantId: tenantId,
      consumerCode: caseDetails?.filingNumber,
      businessService: "case-default",
    },
    {
      enabled: Boolean(tenantId && caseDetails?.filingNumber),
    }
  );

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
        },
      ],
    },
    {},
    "dristi",
    Boolean(chequeDetails?.totalAmount && chequeDetails.totalAmount !== "0")
  );
  const totalAmount = useMemo(() => {
    const totalAmount = calculationResponse?.Calculation?.[0]?.totalAmount || 0;
    return parseFloat(totalAmount).toFixed(2);
  }, [calculationResponse?.Calculation]);
  const paymentCalculation = useMemo(() => {
    const breakdown = calculationResponse?.Calculation?.[0]?.breakDown || [];
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
  }, [calculationResponse?.Calculation]);
  const payerName = useMemo(() => caseDetails?.additionalDetails?.payerName, [caseDetails?.additionalDetails?.payerName]);
  const bill = paymentDetails?.Bill ? paymentDetails?.Bill[0] : {};
  const { data: demandResponse, isLoading: demandCreateLoading } = Digit.Hooks.dristi.useCreateDemand(
    {
      Demands: [
        {
          tenantId,
          consumerCode: caseDetails?.filingNumber,
          consumerType: "case-default",
          businessService: "case-default",
          taxPeriodFrom: Date.now().toString(),
          taxPeriodTo: Date.now().toString(),
          demandDetails: [
            {
              taxHeadMasterCode: "CASE_ADVANCE_CARRYFORWARD",
              taxAmount: 4,
              collectionAmount: 0,
            },
          ],
        },
      ],
    },
    {},
    "dristi",
    Boolean(paymentDetails?.Bill?.length === 0 && caseDetails?.filingNumber)
  );

  const onSubmitCase = async () => {
    setIsDisabled(true);
    const regenerateBill = await DRISTIService.callFetchBill(
      {},
      { consumerCode: caseDetails?.filingNumber, tenantId, businessService: "case-default" }
    );
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
              businessService: "case-default",
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
          totalAmountPaid: totalAmount,
          instrumentNumber: additionDetails,
          instrumentDate: new Date().getTime(),
        },
      });
      await DRISTIService.customApiService(Urls.dristi.pendingTask, {
        pendingTask: {
          name: "Pending Payment",
          entityType: "case-default",
          referenceId: `MANUAL_${caseDetails?.filingNumber}`,
          status: "PAYMENT_PENDING",
          cnrNumber: null,
          filingNumber: caseDetails?.filingNumber,
          isCompleted: true,
          stateSla: null,
          additionalDetails: {},
          tenantId,
        },
      });
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

  if (isCaseSearchLoading || isFetchBillLoading || isPaymentLoading || demandCreateLoading) {
    return <Loader />;
  }
  return (
    <React.Fragment>
      <div className="home-screen-wrapper" style={{ minHeight: "calc(100vh - 90px)", width: "100%", padding: "30px" }}>
        <div className="header-class" style={{ display: "flex", flexDirection: "column", gap: "8px" }}>
          <div className="header">{t("CS_RECORD_PAYMENT_HEADER_TEXT")}</div>
          <div className="sub-header">{t("CS_RECORD_PAYMENT_SUBHEADER_TEXT")}</div>
        </div>
        <div className="payment-calculator-wrapper" style={{ maxHeight: "400px" }}>
          {paymentCalculation.map((item) => (
            <div
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
        <div style={{ marginTop: 40, marginBottom: "150px" }}>
          <div className="payment-case-name">{`${t("CS_CASE_ID")}: ${caseDetails?.filingNumber}`}</div>
          <div className="payment-case-detail-wrapper" style={{ maxHeight: "350px" }}>
            <LabelFieldPair>
              <CardLabel>{`${t("CORE_COMMON_PAYER")}`}</CardLabel>
              <TextInput
                t={t}
                style={{ width: "50%" }}
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
