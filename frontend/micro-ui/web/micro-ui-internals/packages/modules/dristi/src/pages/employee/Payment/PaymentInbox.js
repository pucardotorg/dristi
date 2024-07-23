import { InboxSearchComposer } from "@egovernments/digit-ui-react-components";
import React from "react";
import { useTranslation } from "react-i18next";
import { paymentInboxConfig } from "./paymentInboxConfig";

const sectionsParentStyle = {
  height: "50%",
  display: "flex",
  flexDirection: "column",
  gridTemplateColumns: "20% 1fr",
  gap: "1rem",
};

function PaymentInbox() {
  const { t } = useTranslation();
  return (
    <React.Fragment>
      <div className="home-screen-wrapper payment-inbox" style={{ minHeight: "calc(100vh - 90px)", width: "100%", padding: "30px" }}>
        <div className="header-class" style={{ display: "flex", flexDirection: "column", gap: "8px" }}>
          <div className="header">{t("CS_PENDING_PAYMENT_HEADER_TEXT")}</div>
          <div className="sub-header">{t("CS_PENDING_PAYMENT_SUBHEADER_TEXT")}</div>
        </div>
        <div className="inbox-search-wrapper">
          <InboxSearchComposer customStyle={sectionsParentStyle} configs={paymentInboxConfig}></InboxSearchComposer>
        </div>
      </div>
    </React.Fragment>
  );
}

export default PaymentInbox;
