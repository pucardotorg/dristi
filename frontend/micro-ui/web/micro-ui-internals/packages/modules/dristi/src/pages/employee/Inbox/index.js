import React, { useState } from "react";
import { useTranslation } from "react-i18next";

import { CustomDropdown, Header, InboxSearchComposer, Loader } from "@egovernments/digit-ui-react-components";
import { useHistory } from "react-router-dom/cjs/react-router-dom.min";

const Digit = window?.Digit || {};
const configAdvocate = {
  label: "ES_COMMON_INBOX",
  type: "inbox",
  apiDetails: {
    serviceName: "/advocate/advocate/v1/_search",
    requestParam: {},
    requestBody: {
      applicationNumber: "",
      tenantId: "pg",
      status: ["INWORKFLOW"],
    },
    minParametersForSearchForm: 1,
    masterName: "commonUiConfig",
    moduleName: "registrationRequestsConfig",
    searchFormJsonPath: "requestBody",
  },
  sections: {
    search: {
      uiConfig: {
        headerStyle: null,
        type: "registration-requests-table-search",
        primaryLabel: "ES_COMMON_SEARCH",
        secondaryLabel: "ES_COMMON_CLEAR_SEARCH",
        minReqFields: 1,
        defaultValues: {
          applicationNumber: "",
        },
        fields: [
          {
            label: "Application No",
            type: "text",
            isMandatory: false,
            disable: false,
            populators: {
              name: "applicationNumber",
              error: "BR_PATTERN_ERR_MSG",
              validation: {
                pattern: {},
                minlength: 2,
              },
            },
          },
        ],
      },
      label: "",
      children: {},
      show: true,
    },
    searchResult: {
      label: "",
      uiConfig: {
        columns: [
          {
            label: "Application No",
            jsonPath: "applicationNumber",
            additionalCustomization: true,
          },
          {
            label: "User Name",
            jsonPath: "additionalDetails",
            additionalCustomization: true,
          },
          {
            label: "User Type",
            jsonPath: "usertype",
            additionalCustomization: true,
          },
          {
            label: "Date Created",
            jsonPath: "auditDetails.createdTime",
            additionalCustomization: true,
          },
          {
            label: "Due Since (no of days)",
            jsonPath: "dueSince",
            additionalCustomization: true,
          },
          { label: "Action", jsonPath: "individualId", additionalCustomization: true },
        ],
        enableGlobalSearch: false,
        enableColumnSort: true,
        resultsJsonPath: "advocates",
      },
      children: {},
      show: true,
    },
  },
  additionalSections: {},
};

const configClerk = {
  label: "ES_COMMON_INBOX",
  type: "inbox",
  apiDetails: {
    serviceName: "/advocate/clerk/v1/_search",
    requestParam: {},
    requestBody: {
      applicationNumber: "",
      tenantId: "pg",
      status: ["INWORKFLOW"],
    },
    minParametersForSearchForm: 1,
    masterName: "commonUiConfig",
    moduleName: "registrationRequestsConfig",
    searchFormJsonPath: "requestBody",
  },
  sections: {
    search: {
      uiConfig: {
        headerStyle: null,
        type: "registration-requests-table-search",
        primaryLabel: "ES_COMMON_SEARCH",
        secondaryLabel: "ES_COMMON_CLEAR_SEARCH",
        minReqFields: 1,
        defaultValues: {
          applicationNumber: "",
        },
        fields: [
          {
            label: "Application No",
            type: "text",
            isMandatory: false,
            disable: false,
            populators: {
              name: "applicationNumber",
              error: "BR_PATTERN_ERR_MSG",
              validation: {
                pattern: {},
                minlength: 2,
              },
            },
          },
        ],
      },
      label: "",
      children: {},
      show: true,
    },
    searchResult: {
      label: "",
      uiConfig: {
        columns: [
          {
            label: "Application No",
            jsonPath: "applicationNumber",
            additionalCustomization: true,
          },
          {
            label: "User Name",
            jsonPath: "additionalDetails",
            additionalCustomization: true,
          },
          {
            label: "User Type",
            jsonPath: "usertype",
            additionalCustomization: true,
          },
          {
            label: "Date Created",
            jsonPath: "auditDetails.createdTime",
            additionalCustomization: true,
          },
          {
            label: "Due Since (no of days)",
            jsonPath: "dueSince",
            additionalCustomization: true,
          },
          { label: "Action", jsonPath: "individualId", additionalCustomization: true },
        ],
        enableGlobalSearch: false,
        enableColumnSort: true,
        resultsJsonPath: "clerks",
      },
      children: {},
      show: true,
    },
  },
  additionalSections: {},
};

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
  const [pageSize, setPageSize] = useState(10);
  const [pageOffset, setPageOffset] = useState(0);
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

  const dropdownConfig = {
    label: "CS_ID_TYPE",
    type: "dropdown",
    name: "selectIdTypeType",
    optionsKey: "name",
    validation: {},
    isMandatory: true,
    options: [
      {
        code: "advocate",
        name: "Advocate",
      },
      {
        code: "clerk",
        name: "Clerk",
      },
    ],
    styles: {
      width: "200px",
    },
  };

  return (
    <React.Fragment>
      <div>
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
        <p>{}</p>
        <div className="inbox-search-wrapper">
          {type === "clerk" && <InboxSearchComposer customStyle={sectionsParentStyle} configs={configClerk}></InboxSearchComposer>}
          {type === "advocate" && <InboxSearchComposer customStyle={sectionsParentStyle} configs={configAdvocate}></InboxSearchComposer>}
        </div>
      </div>
    </React.Fragment>
  );
};

export default Inbox;
