import { CloseSvg, Loader, Toast } from "@egovernments/digit-ui-react-components";
import React, { useCallback, useEffect, useMemo, useState } from "react";
import { InfoCard } from "@egovernments/digit-ui-components";
import { useHistory } from "react-router-dom/cjs/react-router-dom.min";
import useSearchCaseService from "@egovernments/digit-ui-module-dristi/src/hooks/dristi/useSearchCaseService";
import { useToast } from "@egovernments/digit-ui-module-dristi/src/components/Toast/useToast";
import { DRISTIService } from "@egovernments/digit-ui-module-dristi/src/services";
import usePaymentProcess from "../hooks/usePaymentProcess";
import { useTranslation } from "react-i18next";
import { useLocation } from "react-router-dom/cjs/react-router-dom.min";
import { Urls } from "@egovernments/digit-ui-module-dristi/src/hooks";
import { getSuffixByBusinessCode } from "../utils";
import Modal from "@egovernments/digit-ui-module-dristi/src/components/Modal";
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

function EfilingPaymentBreakdown({ setShowModal, header, subHeader }) {
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
  const path = "";
  const [toastMsg, setToastMsg] = useState(null);
  const [isCaseLocked, setIsCaseLocked] = useState(false);
  const [payOnlineButtonTitle, setPayOnlineButtonTitle] = useState("CS_BUTTON_PAY_ONLINE_SOMEONE_PAYING");
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
    `dristi-${caseId}`,
    caseId,
    Boolean(caseId)
  );

  const caseDetails = useMemo(
    () => ({
      ...caseData?.criteria?.[0]?.responseList?.[0],
    }),
    [caseData]
  );
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
          isDelayCondonation: isDelayCondonation,
          filingNumber: caseDetails?.filingNumber,
        },
      ],
    },
    {},
    "dristi",
    Boolean(chequeDetails?.totalAmount && chequeDetails.totalAmount !== "0")
  );

  const suffix = useMemo(() => getSuffixByBusinessCode(paymentTypeData, "case-default"), [paymentTypeData]);

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
    consumerCode: caseDetails?.filingNumber + `_${suffix}`,
    service: "case-default",
    path,
    caseDetails,
    totalAmount: totalAmount,
    scenario,
  });

  const fetchCaseLockStatus = useCallback(async () => {
    try {
      const status = await DRISTIService.getCaseLockStatus(
        {},
        {
          uniqueId: caseDetails?.filingNumber,
          tenantId: tenantId,
        }
      );
      setIsCaseLocked(status?.Lock?.isLocked);
    } catch (error) {
      console.error("Error fetching case lock status", error);
    }
  });

  useEffect(() => {
    if (caseDetails?.filingNumber) {
      fetchCaseLockStatus();
    }
  }, [caseDetails?.filingNumber]);

  const onSubmitCase = async () => {
    try {
      const bill = await fetchBill(caseDetails?.filingNumber + `_${suffix}`, tenantId, "case-default");
      if (!bill?.Bill?.length) {
        showToast("success", t("CS_NO_PENDING_PAYMENT"), 50000);
        setIsCaseLocked(true);
        setPayOnlineButtonTitle("CS_BUTTON_PAY_ONLINE_NO_PENDING_PAYMENT");
        return;
      }

      const caseLockStatus = await DRISTIService.getCaseLockStatus(
        {},
        {
          uniqueId: caseDetails?.filingNumber,
          tenantId: tenantId,
        }
      );
      if (caseLockStatus?.Lock?.isLocked) {
        setIsCaseLocked(true);
        showToast("success", t("CS_CASE_LOCKED_BY_ANOTHER_USER"), 50000);
        setPayOnlineButtonTitle("CS_BUTTON_PAY_ONLINE_SOMEONE_PAYING");
        return;
      }

      await DRISTIService.setCaseLock({ Lock: { uniqueId: caseDetails?.filingNumber, tenantId: tenantId, lockType: "PAYMENT" } }, {});

      const paymentStatus = await openPaymentPortal(bill);
      await DRISTIService.setCaseUnlock({}, { uniqueId: caseDetails?.filingNumber, tenantId: tenantId });
      const success = Boolean(paymentStatus);

      const receiptData = {
        header: "CS_HEADER_FOR_E_FILING_PAYMENT",
        subHeader: "CS_SUBHEADER_TEXT_FOR_E_FILING_PAYMENT",
        isArrow: false,
        showTable: true,
        caseInfo: [
          { key: "Mode of Payment", value: "Online", copyData: false },
          { key: "Amount", value: totalAmount, copyData: false },
          { key: "Transaction ID", value: caseDetails?.filingNumber, copyData: true },
        ],
        showCopytext: true,
      };

      if (success) {
        await DRISTIService.customApiService(Urls.dristi.pendingTask, {
          pendingTask: {
            name: "Pending Payment",
            entityType: "case-default",
            referenceId: `MANUAL_${caseDetails?.filingNumber}`,
            status: "PENDING_PAYMENT",
            cnrNumber: null,
            filingNumber: caseDetails?.filingNumber,
            isCompleted: true,
            stateSla: null,
            additionalDetails: {},
            tenantId,
          },
        });
        const fileStoreId = await DRISTIService.fetchBillFileStoreId({}, { billId: bill?.Bill?.[0]?.id, tenantId });
        if (fileStoreId) {
          history.push(`e-filing-payment-response`, {
            state: { success: true, receiptData, fileStoreId: fileStoreId?.Document?.fileStore },
          });
        }
      } else {
        history.push(`e-filing-payment-response`, {
          state: { success: false, receiptData, caseId },
        });
      }
    } catch (error) {
      toast.error(t("CS_PAYMENT_ERROR"));
      console.error(error);
    }
  };

  if (isLoading || isPaymentLoading || isPaymentTypeLoading) {
    return <Loader />;
  }
  const showToast = (type, message, duration = 5000) => {
    setToastMsg({ key: type, action: message });
    setTimeout(() => {
      setToastMsg(null);
    }, duration);
  };
  return (
    <div className="e-filing-payment">
      <Modal
        headerBarEnd={<CloseBtn onClick={onCancel} />}
        actionSaveLabel={t("CS_PAY_ONLINE")}
        formId="modal-action"
        actionSaveOnSubmit={() => onSubmitCase()}
        titleSaveButton={isCaseLocked ? t(payOnlineButtonTitle) : ""}
        isDisabled={paymentLoader || isCaseLocked}
        headerBarMain={<Heading label={t("CS_PAY_TO_FILE_CASE")} />}
      >
        <div className="payment-due-wrapper" style={{ maxHeight: "550px", display: "flex", flexDirection: "column", margin: "13px 0px" }}>
          <InfoCard
            variant={"default"}
            label={t("CS_COMMON_NOTE")}
            style={{ backgroundColor: "#ECF3FD", marginBottom: "8px" }}
            additionalElements={[
              <div style={{ display: "flex", alignItems: "center", gap: 4 }}>
                <span>{t("PLEASE_ALLOW_POPUP_PAYMENT")}</span>
              </div>,
            ]}
            inline
            textStyle={{}}
            className={"adhaar-verification-info-card"}
          />
          <div className="payment-due-text" style={{ fontSize: "18px" }}>
            {`${t("CS_DUE_PAYMENT")} `}
            <span style={{ fontWeight: 700 }}>Rs {totalAmount}/-.</span>
            {` ${t("CS_MANDATORY_STEP_TO_FILE_CASE")}`}
          </div>
          <div className="payment-calculator-wrapper" style={{ display: "flex", flexDirection: "column", maxHeight: "150px", overflowY: "auto" }}>
            {paymentCalculation
              .filter((item) => !item.isTotalFee)
              .map((item) => (
                <div
                  style={{
                    display: "flex",
                    justifyContent: "space-between",
                    alignItems: "center",
                    paddingRight: "16px",
                  }}
                >
                  <span>{item.key}</span>
                  <span>
                    {item.currency} {parseFloat(item.value).toFixed(2)}
                  </span>
                </div>
              ))}
          </div>
          <div className="payment-calculator-wrapper" style={{ display: "flex", flexDirection: "column" }}>
            {paymentCalculation
              .filter((item) => item.isTotalFee)
              .map((item) => (
                <div
                  style={{
                    display: "flex",
                    justifyContent: "space-between",
                    alignItems: "center",
                    borderTop: "1px solid #BBBBBD",
                    fontSize: "16px",
                    fontWeight: "700",
                    paddingTop: "12px",
                    paddingRight: paymentCalculation.length > 6 ? "28px" : "16px",
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
              style={{ backgroundColor: "#ECF3FD" }}
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
      {toastMsg && (
        <Toast error={toastMsg.key === "error"} label={t(toastMsg.action)} onClose={() => setToastMsg(null)} style={{ maxWidth: "500px" }} />
      )}
    </div>
  );
}

export default EfilingPaymentBreakdown;
