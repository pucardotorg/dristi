import React, { useEffect, useState } from "react";
import { useTranslation } from "react-i18next";

import { CustomDropdown, Header, InboxSearchComposer, Loader, Toast } from "@egovernments/digit-ui-react-components";
import { useHistory } from "react-router-dom/cjs/react-router-dom.min";
import { dropdownConfig, newconfigAdvocate, newconfigClerk } from "./config";

const Digit = window?.Digit || {};

const sectionsParentStyle = {
  height: "50%",
  display: "flex",
  flexDirection: "column",
  gridTemplateColumns: "20% 1fr",
  gap: "1rem",
};

const Inbox = ({ tenants, parentRoute }) => {
  const { t } = useTranslation();
  Digit.SessionStorage.set("ENGAGEMENT_TENANTS", tenants);
  const tenantId = Digit.ULBService.getCurrentTenantId();
  let isMobile = window.Digit.Utils.browser.isMobile();
  const [data, setData] = useState([]);
  const history = useHistory();
  const urlParams = new URLSearchParams(window.location.search);
  const type = urlParams.get("type") || "advocate";
  const actions = urlParams.get("actions");
  const [message, setMessage] = useState(null);
  useEffect(() => {
    if (actions) {
      setMessage(actions === "APPROVE" ? t("ES_USER_APPROVED") : actions === "ERROR" ? t("ES_API_ERROR") : t("ES_USER_REJECTED"));
      setTimeout(() => {
        history.push(`/digit-ui/employee/dristi/registration-requests`);
      }, 3000);
    }
    return;
  }, [actions]);

  const defaultType = { code: type, name: type?.charAt(0)?.toUpperCase() + type?.slice(1) };
  const [{ userType }, setSearchParams] = useState({
    eventStatus: [],
    range: {
      startDate: null,
      endDate: new Date(""),
      title: "",
    },
    userType: defaultType,
    ulb: tenants?.find((tenant) => tenant?.code === tenantId),
  });
  const { isLoading } = data;

  if (isLoading) {
    return <Loader />;
  }

  return (
    <React.Fragment>
      <div style={{ paddingLeft: "20px" }}>
        <div style={{ display: "flex", justifyContent: "space-between" }}>
          <Header>{t("Registration-Requests")}</Header>
          <CustomDropdown
            t={t}
            defaulValue={defaultType}
            onChange={(e) => {
              history.push(`?type=${e.code}`);
              setSearchParams((prev) => {
                return {
                  ...prev,
                  userType: e,
                };
              });
            }}
            value={userType}
            config={dropdownConfig}
          ></CustomDropdown>
        </div>
        <div className="inbox-search-wrapper">
          {type === "clerk" && <InboxSearchComposer customStyle={sectionsParentStyle} configs={newconfigClerk}></InboxSearchComposer>}
          {type === "advocate" && <InboxSearchComposer customStyle={sectionsParentStyle} configs={newconfigAdvocate}></InboxSearchComposer>}
        </div>
        {message && (
          <Toast error={message === t("ES_API_ERROR") || message === t("ES_USER_REJECTED")} label={message} onClose={() => setMessage(null)} />
        )}
      </div>
    </React.Fragment>
  );
};

export default Inbox;
