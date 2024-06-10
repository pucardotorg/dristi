import { CustomDropdown, InboxSearchComposer } from "@egovernments/digit-ui-react-components";
import React, { useEffect, useState } from "react";
import { useTranslation } from "react-i18next";
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
  const history = useHistory();
  const urlParams = new URLSearchParams(window.location.search);
  const type = urlParams.get("type") || "advocate";
  const actions = urlParams.get("actions");

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

  return (
    <React.Fragment>
      <div className="registration-requests">
        <div className="header-class">
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
      </div>
    </React.Fragment>
  );
};

export default Inbox;
