import { Banner, CardLabel, CloseSvg, Loader, Modal } from "@egovernments/digit-ui-react-components";
import React, { useMemo, useState, useEffect } from "react";
import Button from "@egovernments/digit-ui-module-dristi/src/components/Button";
import { InfoCard } from "@egovernments/digit-ui-components";
import { Link, useHistory } from "react-router-dom/cjs/react-router-dom.min";
import CustomCaseInfoDiv from "@egovernments/digit-ui-module-dristi/src/components/CustomCaseInfoDiv";
import useSearchCaseService from "@egovernments/digit-ui-module-dristi/src/hooks/dristi/useSearchCaseService";
import { useToast } from "@egovernments/digit-ui-module-dristi/src/components/Toast/useToast";
import { DRISTIService } from "@egovernments/digit-ui-module-dristi/src/services";
import usePaymentProcess from "../hooks/usePaymentProcess";
import { useTranslation } from "react-i18next";
import { useLocation } from "react-router-dom/cjs/react-router-dom.min";
import { Urls } from "@egovernments/digit-ui-module-dristi/src/hooks";
import { getSuffixByBusinessCode, getTaxPeriodByBusinessService } from "../utils";

const mockSubmitModalInfo = {
  header: "CS_HEADER_FOR_E_FILING_PAYMENT",
  subHeader: "CS_SUBHEADER_TEXT_FOR_E_FILING_PAYMENT",
  caseInfo: [
    {
      key: "Case Number",
      value: "FSM-2019-04-23-898898",
    },
  ],
  isArrow: false,
  showTable: true,
};

const CloseBtn = (props) => {
  return (
    <div onClick={props?.onClick} style={{ height: "100%", display: "flex", alignItems: "center", paddingRight: "20px", cursor: "pointer" }}>
      <CloseSvg />
    </div>
  );
};

const Heading = (props) => {
  return <h1 className="heading-m">{props.label}</h1>;
};

function EfilingPaymentBreakdown({ setShowModal, header, subHeader, submitModalInfo = mockSubmitModalInfo, amount = 2000 }) {
  const { t } = useTranslation();
  const location = useLocation();
  const history = useHistory();
  const onCancel = () => {
    if (!paymentLoader) {
      history.goBack();
      setShowPaymentModal(false);
    }
  };
  const tenantId = window?.Digit.ULBService.getCurrentTenantId();
  const params = location?.state.state.params;
  const caseId = params?.caseId;
  const toast = useToast();
  const scenario = "EfillingCase";
  const userInfo = JSON.parse(window.localStorage.getItem("user-info"));
  const userInfoType = useMemo(() => (userInfo?.type === "CITIZEN" ? "citizen" : "employee"), [userInfo]);
  const path = "";

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

  const { data: caseData, isLoading } = useSearchCaseService(
    {
      criteria: [
        {
          caseId: caseId,
        },
      ],
      tenantId,
    },
    {},
    "dristi",
    caseId,
    caseId
  );

  const caseDetails = useMemo(
    () => ({
      ...caseData?.criteria?.[0]?.responseList?.[0],
    }),
    [caseData]
  );

  // check for partial Liability
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
  const { data: billResponse, isLoading: isBillLoading } = Digit.Hooks.dristi.useBillSearch(
    {},
    { tenantId, consumerCode: caseDetails?.filingNumber, service: "case-default" },
    "dristi",
    Boolean(caseDetails?.filingNumber)
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

  const { fetchBill, openPaymentPortal, paymentLoader, showPaymentModal, setShowPaymentModal } = usePaymentProcess({
    tenantId,
    consumerCode: caseDetails?.filingNumber,
    service: "case-default",
    path,
    caseDetails,
    totalAmount: chequeDetails?.totalAmount,
    mockSubmitModalInfo,
    scenario,
  });
  const onSubmitCase = async () => {
    try {
      const suffix = getSuffixByBusinessCode(paymentTypeData, "case-default");
      if (billResponse?.Bill?.length === 0) {
        const taxPeriod = getTaxPeriodByBusinessService(taxPeriodData, "case-default");
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
                  taxAmount: 4,
                  collectionAmount: 0,
                },
              ],
            },
          ],
        });
      }
      const bill = await fetchBill(caseDetails?.filingNumber + `_${suffix}`, tenantId, "case-default");
      if (bill?.Bill?.length) {
        const paymentStatus = await openPaymentPortal(bill);
        if (paymentStatus) {
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
          const fileStoreId = await DRISTIService.fetchBillFileStoreId({}, { billId: bill?.Bill?.[0]?.id, tenantId });
          fileStoreId &&
            history.push(`e-filing-payment-response`, {
              state: {
                success: true,
                receiptData: {
                  ...mockSubmitModalInfo,
                  caseInfo: [
                    {
                      key: "Mode of Payment",
                      value: "Online",
                      copyData: false,
                    },
                    {
                      key: "Amount",
                      value: totalAmount,
                      copyData: false,
                    },
                    {
                      key: "Transaction ID",
                      value: caseDetails?.filingNumber,
                      copyData: true,
                    },
                  ],
                  isArrow: false,
                  showTable: true,
                  showCopytext: true,
                },
                fileStoreId: fileStoreId?.Document?.fileStore,
              },
            });
        } else {
          history.push(`e-filing-payment-response`, {
            state: {
              success: false,
              receiptData: {
                ...mockSubmitModalInfo,
                caseInfo: [
                  {
                    key: "Mode of Payment",
                    value: "Online",
                    copyData: false,
                  },
                  {
                    key: "Amount",
                    value: totalAmount,
                    copyData: false,
                  },
                  {
                    key: "Transaction ID",
                    value: caseDetails?.filingNumber,
                    copyData: true,
                  },
                ],
                isArrow: false,
                showTable: true,
                showCopytext: true,
              },
              caseId: caseId,
            },
          });
        }
      }
    } catch (error) {
      toast.error(t("CS_PAYMENT_ERROR"));
      console.error(error);
    }
  };

  if (isLoading || isPaymentLoading || isBillLoading) {
    return <Loader />;
  }
  return (
    <div className="e-filing-payment">
      <Modal
        headerBarEnd={<CloseBtn onClick={onCancel} />}
        actionSaveLabel={t("CS_PAY_ONLINE")}
        formId="modal-action"
        actionSaveOnSubmit={() => onSubmitCase()}
        isDisabled={paymentLoader}
        headerBarMain={<Heading label={t("CS_PAY_TO_FILE_CASE")} />}
      >
        <div className="payment-due-wrapper" style={{ display: "flex", flexDirection: "column" }}>
          <div className="payment-due-text" style={{ fontSize: "18px" }}>
            {`${t("CS_DUE_PAYMENT")} `}
            <span style={{ fontWeight: 700 }}>Rs {totalAmount}/-.</span>
            {` ${t("CS_MANDATORY_STEP_TO_FILE_CASE")}`}
          </div>
          <div className="payment-calculator-wrapper" style={{ display: "flex", flexDirection: "column" }}>
            {paymentCalculation.map((item) => (
              <div
                style={{
                  display: "flex",
                  justifyContent: "space-between",
                  alignItems: "center",
                  borderTop: item.isTotalFee && "1px solid #BBBBBD",
                  fontSize: item.isTotalFee && "16px",
                  fontWeight: item.isTotalFee && "700",
                  paddingTop: item.isTotalFee && "12px",
                }}
              >
                <span>{item.key}</span>
                <span>
                  {item.currency} {parseFloat(item.value).toFixed(2)}
                </span>
              </div>
            ))}
          </div>
          <div>
            <InfoCard
              variant={"default"}
              label={t("CS_COMMON_NOTE")}
              style={{ margin: "100px 0 0 0", backgroundColor: "#ECF3FD" }}
              additionalElements={[
                <div style={{ display: "flex", alignItems: "center", gap: 4 }}>
                  <span>{t("CS_OFFLINE_PAYMENT_STEP_TEXT")}</span>
                </div>,
              ]}
              inline
              textStyle={{}}
              className={"adhaar-verification-info-card"}
            />
          </div>
        </div>
      </Modal>
    </div>
  );
}

export default EfilingPaymentBreakdown;
