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
    code: "PAY_BY_OWNER",
    i18nKey: "PT_PAY_BY_OWNER",
    name: "I am making the payment as the owner/ consumer of the service",
  },
  {
    code: "PAY_BEHALF_OWNER",
    i18nKey: "PT_PAY_BEHALF_OWNER",
    name: "I am making the payment on behalf of the owner/ consumer of the service",
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
      toast.error("CS_BILL_NOT_AVAILABLE");
      history.push(`/${window?.contextPath}/employee/dristi/pending-payment-inbox`);
      return;
    }
    try {
      await window?.Digit.PaymentService.createReciept(tenantId, {
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
          paymentMode: "CASH",
          paidBy: modeOfPayment?.code,
          mobileNumber: caseDetails?.additionalDetails?.payerMobileNo || "",
          payerName: payer,
          totalAmountPaid: 2000,
        },
      });
      history.push(`/${window?.contextPath}/employee/dristi/pending-payment-inbox/response`, { state: { success: true } });
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
          <div className="payment-case-detail-wrapper">
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
                }}
                value={modeOfPayment}
                config={paymentOptionConfig}
              ></CustomDropdown>
            </LabelFieldPair>
          </div>
        </div>
        <ActionBar>
          <SubmitBar
            label={t("CS_GENERATE_RECEIPT")}
            disabled={!payer || Object.keys(!modeOfPayment ? {} : modeOfPayment).length === 0}
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
