import React, { useState } from "react";
import { useTranslation } from "react-i18next";

import { CustomDropdown, Header, InboxSearchComposer, Loader } from "@egovernments/digit-ui-react-components";
import { useHistory } from "react-router-dom/cjs/react-router-dom.min";
import { dropdownConfig, newConfigAdvocate, newConfigClerk } from "./config";

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
          {type === "clerk" && <InboxSearchComposer customStyle={sectionsParentStyle} configs={newConfigClerk}></InboxSearchComposer>}
          {type === "advocate" && <InboxSearchComposer customStyle={sectionsParentStyle} configs={newConfigAdvocate}></InboxSearchComposer>}
        </div>
      </div>
    </React.Fragment>
  );
};

export default Inbox;
