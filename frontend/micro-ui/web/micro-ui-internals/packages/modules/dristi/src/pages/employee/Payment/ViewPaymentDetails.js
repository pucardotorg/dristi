import { Loader, SubmitBar, ActionBar, CustomDropdown, CardLabel, LabelFieldPair, TextInput, Toast } from "@egovernments/digit-ui-react-components";
import React, { useMemo, useState } from "react";
import { useTranslation } from "react-i18next";
import { useHistory } from "react-router-dom";
import useSearchCaseService from "../../../hooks/dristi/useSearchCaseService";
import { useToast } from "../../../components/Toast/useToast";

const paymentCalculation = [
  { key: "Amount Due", value: 600, currency: "Rs" },
  { key: "Court Fees", value: 400, currency: "Rs" },
  { key: "Advocate Fees", value: 1000, currency: "Rs" },
  { key: "Total Fees", value: 2000, currency: "Rs", isTotalFee: true },
];

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
    code: "POSTAL_ORDER",
    i18nKey: "Stamps",
  },
];

const paymentOptionConfig = {
  label: "CS_MODE_OF_PAYMENT",
  type: "dropdown",
  name: "selectIdTypeType",
  optionsKey: "i18nKey",
  validation: {},
  isMandatory: true,
  options: paymentOption,
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
      businessService: "case",
    },
    {
      enabled: Boolean(tenantId && caseDetails?.filingNumber),
    }
  );
  const bill = paymentDetails?.Bill ? paymentDetails?.Bill[0] : {};

  const onSubmitCase = async () => {
    if (!Object.keys(bill || {}).length) {
      toast.error(t("CS_BILL_NOT_AVAILABLE"));
      history.push(`/${window?.contextPath}/employee/dristi/pending-payment-inbox`);
      return;
    }
    try {
      const receiptData = await window?.Digit.PaymentService.createReciept(tenantId, {
        Payment: {
          paymentDetails: [
            {
              businessService: "case",
              billId: bill.id,
              totalDue: bill?.totalAmount,
              totalAmountPaid: bill?.totalAmount || 2000,
            },
          ],
          tenantId,
          paymentMode: modeOfPayment.code,
          paidBy: "PAY_BY_OWNER",
          mobileNumber: caseDetails?.additionalDetails?.payerMobileNo || "",
          payerName: payer,
          totalAmountPaid: 2000,
          ...(additionDetails && { transactionNumber: additionDetails }),
          instrumentNumber: additionDetails,
          instrumentDate: new Date().getTime(),
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
        },
      });
    } catch (err) {
      history.push(`/${window?.contextPath}/employee/dristi/pending-payment-inbox/response`, { state: { success: false } });
    }
  };

  if (isCaseSearchLoading || isFetchBillLoading) {
    return <Loader />;
  }
  return (
    <React.Fragment>
      <div className="home-screen-wrapper" style={{ minHeight: "calc(100vh - 90px)", width: "100%", padding: "30px" }}>
        <div className="header-class" style={{ display: "flex", flexDirection: "column", gap: "8px" }}>
          <div className="header">{t("CS_RECORD_PAYMENT_HEADER_TEXT")}</div>
          <div className="sub-header">{t("CS_RECORD_PAYMENT_SUBHEADER_TEXT")}</div>
        </div>
        <div className="payment-calculator-wrapper">
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
                {item.currency} {item.value}
              </span>
            </div>
          ))}
        </div>
        <div style={{ marginTop: 40 }}>
          <div className="payment-case-name">{`${t("CS_CASE_ID")}: ${caseDetails?.filingNumber}`}</div>
          <div className="payment-case-detail-wrapper" style={{ maxHeight: 400 }}>
            <LabelFieldPair>
              <CardLabel>{`${t("CORE_COMMON_PAYER")}`}</CardLabel>
              <TextInput
                t={t}
                style={{ width: "50%" }}
                type={"text"}
                isMandatory={false}
                name="name"
                value={payer}
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
                defaulValue={paymentOption[0]}
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
              !payer ||
              Object.keys(!modeOfPayment ? {} : modeOfPayment).length === 0 ||
              (["CHEQUE", "DD"].includes(modeOfPayment?.code) ? additionDetails.length !== 6 : false)
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
