import { CloseSvg, Loader, Modal } from "@egovernments/digit-ui-react-components";
import React, { useMemo } from "react";
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
  const delayCondonation = useMemo(() => {
    const today = new Date();
    if (!caseDetails?.caseDetails?.["demandNoticeDetails"]?.formdata) {
      return null;
    }
    const dateOfAccrual = new Date(caseDetails?.caseDetails["demandNoticeDetails"]?.formdata[0]?.data?.dateOfAccrual);
    return today?.getTime() - dateOfAccrual?.getTime();
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
          delayCondonation: delayCondonation,
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
    totalAmount: chequeDetails?.totalAmount,
    scenario,
  });
  const onSubmitCase = async () => {
    try {
      const bill = await fetchBill(caseDetails?.filingNumber + `_${suffix}`, tenantId, "case-default");
      if (!bill?.Bill?.length) return;

      const paymentStatus = await openPaymentPortal(bill);
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
