import React, { useState } from "react";
import { useLocation } from "react-router-dom";
import { useTranslation } from "react-i18next";
import { Banner } from "@egovernments/digit-ui-react-components";
import { Button, InfoCard } from "@egovernments/digit-ui-components";
import { CopyIcon } from "../../../dristi/src/icons/svgIndex";

const PaymentStatus = () => {
  const { t } = useTranslation();
  const queryStrings = Digit.Hooks.useQueryParams();
  const [isResponseSuccess, setIsResponseSuccess] = useState(
    queryStrings?.isSuccess === "true" ? true : queryStrings?.isSuccess === "false" ? false : true
  );
  const { state } = useLocation();
  const [copied, setCopied] = useState({ isCopied: false, text: "Copied" });
  const [copied1, setCopied1] = useState({ isCopied: false, text: "Copied" });

  const copyToClipboard = (text) => {
    if (!navigator.clipboard) {
      fallbackCopyTextToClipboard(text);
      return;
    }
    navigator.clipboard.writeText(text);
  };

  const dataCopy = (isCopied, message, setCopied, duration = 3000) => {
    setCopied({ isCopied: isCopied, text: message });
    setTimeout(() => {
      setCopied({ isCopied: false, text: "Copied" });
    }, duration);
  };

  return (
    <div style={{ width: "60rem", margin: "auto", padding: "2rem" }}>
      <Banner
        successful={isResponseSuccess}
        message={isResponseSuccess ? "Payment Successful" : "Payment Failed"}
        info={`${state?.showID ? t("SUBMISSION_ID") : ""}`}
        whichSvg={`${isResponseSuccess ? "tick" : null}`}
      />
      {isResponseSuccess ? (
        <div>
          <div style={{ margin: "1rem 0", textAlign: "center" }}>The Summons would be sent to the relevant party.</div>
          <div style={{ background: "#f7f5f3", padding: "1rem", borderRadius: "5px", marginBottom: "1rem" }}>
            <div style={{ marginBottom: "1rem", display: "flex", justifyContent: "space-between" }}>
              <div>Case Name & ID</div>
              <div>
                <strong>Aditi vs Vikram, ID-12347283238</strong>
              </div>
            </div>
            <div style={{ display: "flex", justifyContent: "space-between", marginBottom: "1rem" }}>
              <div>Order ID 1: Order for Summons</div>
              <div style={{ display: "flex", alignItems: "center" }}>
                <strong>KA08293928392</strong>
                <button
                  onClick={() => {
                    copyToClipboard("KA08293928392");
                    dataCopy(true, "Copied", setCopied);
                  }}
                  style={{
                    display: "flex",
                    alignItems: "center",
                    backgroundColor: "#f7f5f3",
                    color: "#007e7e",
                    cursor: "pointer",
                    marginLeft: "1rem",
                  }}
                >
                  <CopyIcon />
                  <div style={{ marginLeft: "0.5rem" }}>{copied.isCopied ? copied.text : "Copy"}</div>
                </button>
              </div>
            </div>
            <div style={{ display: "flex", justifyContent: "space-between" }}>
              <div>Transaction ID</div>
              <div style={{ display: "flex", alignItems: "center" }}>
                <strong>#123456789</strong>
                <button
                  onClick={() => {
                    copyToClipboard("#123456789");
                    dataCopy(true, "Copied", setCopied1);
                  }}
                  style={{
                    display: "flex",
                    alignItems: "center",
                    backgroundColor: "#f7f5f3",
                    color: "#007e7e",
                    cursor: "pointer",
                    marginLeft: "1rem",
                  }}
                >
                  <CopyIcon />
                  <div style={{ marginLeft: "0.5rem" }}>{copied1.isCopied ? copied1.text : "Copy"}</div>
                </button>
              </div>
            </div>
          </div>
        </div>
      ) : (
        <InfoCard
          populators={{
            name: "infocard",
          }}
          variant="default"
          text={"You have a payment due of Rs 525/-. This is a mandatory step to send summons via selected delivery channel for your case."}
          label={"Note"}
          style={{ marginTop: "1.5rem" }}
        />
      )}
      <div style={{ display: "flex", gap: "1rem", marginTop: "2rem" }}>
        <Button
          variation={"secondary"}
          label={isResponseSuccess ? "Download Receipt" : "Back"}
          icon={""}
          iconFill={"#007E7E"}
          style={{ width: "100%" }}
        />
        <Button label={isResponseSuccess ? "Go to Home" : "Try again"} variation={"primary"} style={{ width: "100%" }} />
      </div>
    </div>
  );
};

export default PaymentStatus;
