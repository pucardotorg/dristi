import { InboxSearchComposer } from "@egovernments/digit-ui-react-components";
import React, { useCallback, useEffect, useMemo, useState } from "react";
import { useTranslation } from "react-i18next";
import { paymentTabInboxConfig } from "./paymentInboxConfig";
import { useHistory } from "react-router-dom/cjs/react-router-dom.min";
import { DRISTIService } from "../../../services";

const sectionsParentStyle = {
  height: "50%",
  display: "flex",
  flexDirection: "column",
  gridTemplateColumns: "20% 1fr",
  gap: "1rem",
};

function PaymentInbox() {
  const { t } = useTranslation();
  const roles = Digit.UserService.getUser()?.info?.roles;
  const history = useHistory();
  const [config, setConfig] = useState(paymentTabInboxConfig?.TabSearchConfig?.[0]);
  const [tabData, setTabData] = useState(
    paymentTabInboxConfig?.TabSearchConfig?.map((configItem, index) => ({
      key: index,
      label: configItem.label,
      active: index === 0 ? true : false,
    }))
  );

  const isNyayMitra = roles.some((role) => role.code === "NYAY_MITRA_ROLE");

  const tenantId = useMemo(() => window?.Digit.ULBService.getCurrentTenantId(), []);

  const getTotalCountForTab = useCallback(
    async function (tabConfig) {
      const updatedTabData = await Promise.all(
        tabConfig?.TabSearchConfig?.map(async (configItem, index) => {
          const response = await DRISTIService.customApiService(configItem?.apiDetails?.serviceName, {
            inbox: {
              tenantId,
              processSearchCriteria: {
                ...configItem?.apiDetails?.requestBody?.inbox?.processSearchCriteria,
                tenantId,
              },
              moduleSearchCriteria: {
                ...configItem?.apiDetails?.requestBody?.inbox?.moduleSearchCriteria,
                tenantId,
              },
              offset: 0,
              limit: 1,
            },
          });
          const totalCount = response?.totalCount;
          return {
            key: index,
            label: totalCount ? `${t(configItem.label)} (${totalCount})` : `${t(configItem.label)} (0)`,
            active: index === 0 ? true : false,
          };
        }) || []
      );
      setTabData(updatedTabData);
    },
    [tenantId]
  );

  useEffect(() => {
    getTotalCountForTab(paymentTabInboxConfig);
  }, [getTotalCountForTab, tenantId]);

  const onTabChange = (n) => {
    setTabData((prev) => prev.map((i, c) => ({ ...i, active: c === n ? true : false })));
    setConfig(paymentTabInboxConfig?.TabSearchConfig?.[n]);
  };

  if (!isNyayMitra) {
    history.push(`/${window?.contextPath}/employee/home/home-pending-task`);
  }

  return (
    <React.Fragment>
      <div className="home-screen-wrapper payment-inbox" style={{ minHeight: "calc(100vh - 90px)", width: "100%", padding: "30px" }}>
        <div className="header-class" style={{ display: "flex", flexDirection: "column", gap: "8px" }}>
          <div className="header">{t("NYAY_MITRA_PAYMENTS")}</div>
        </div>
        <div className="inbox-search-wrapper">
          <InboxSearchComposer
            customStyle={sectionsParentStyle}
            configs={config}
            showTab={true}
            tabData={tabData}
            onTabChange={onTabChange}
          ></InboxSearchComposer>{" "}
        </div>
      </div>
    </React.Fragment>
  );
}

export default PaymentInbox;
