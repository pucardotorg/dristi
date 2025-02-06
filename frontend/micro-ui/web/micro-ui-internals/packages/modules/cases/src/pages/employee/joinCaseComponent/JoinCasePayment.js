import { InfoCard } from "@egovernments/digit-ui-components";
import React from "react";

const JoinCasePayment = ({ t, paymentCalculation, totalAmount = "46546" }) => {
  return (
    <div
      className="e-filing-payment payment-due-wrapper"
      style={{ maxHeight: "550px", display: "flex", flexDirection: "column", margin: "13px 0px" }}
    >
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
  );
};

export default JoinCasePayment;
