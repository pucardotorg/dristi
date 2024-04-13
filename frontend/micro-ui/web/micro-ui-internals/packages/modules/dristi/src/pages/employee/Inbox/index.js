import React, { useState, useCallback } from "react";
import { useTranslation } from "react-i18next";

import { Header, InboxSearchComposer, Loader } from "@egovernments/digit-ui-react-components";

import { useQuery } from "react-query";

const Digit = window?.Digit || {};
const config = {
  label: "ES_COMMON_INBOX",
  type: "inbox",
  apiDetails: {
    serviceName: "/inbox/v2/_search",
    requestParam: {},
    requestBody: {
      inbox: {
        processSearchCriteria: {
          businessService: ["PQM"],
          moduleName: "pqm",
          tenantId: "pg.citya",
        },
        moduleSearchCriteria: {
          plantCodes: ["PURI_FSTP"],
          tenantId: "pg.citya",
        },
        tenantId: "pg.citya",
      },
    },
    minParametersForSearchForm: 1,
    tableFormJsonPath: "requestBody.inbox",
    masterName: "commonUiConfig",
    moduleName: "registrationRequestsConfig",
    filterFormJsonPath: "requestBody.inbox.moduleSearchCriteria",
    searchFormJsonPath: "requestBody.inbox.moduleSearchCriteria",
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
          testIds: "",
        },
        fields: [
          {
            label: "Application No",
            type: "text",
            isMandatory: false,
            disable: false,
            populators: {
              name: "testIds",
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
            jsonPath: "applicationNo",
            additionalCustomization: true,
          },
          {
            label: "User Name",
            jsonPath: "username",
          },
          {
            label: "User Type",
            jsonPath: "usertype",
          },
          {
            label: "Date Created",
            jsonPath: "datecreated",
          },
          {
            label: "Due Since (no of days)",
            jsonPath: "dueSince",
          },
          { label: "Action", jsonPath: "apply", additionalCustomization: true },
        ],
        enableGlobalSearch: false,
        enableColumnSort: true,
        resultsJsonPath: "items",
      },
      children: {},
      show: true,
    },
  },
  additionalSections: {},
};

const Inbox = ({ tenants, parentRoute }) => {
  const { t } = useTranslation();
  Digit.SessionStorage.set("ENGAGEMENT_TENANTS", tenants);
  const tenantId = Digit.ULBService.getCurrentTenantId();
  const [pageSize, setPageSize] = useState(10);
  const [pageOffset, setPageOffset] = useState(0);
  const [searchParams, setSearchParams] = useState({
    eventStatus: [],
    range: {
      startDate: null,
      endDate: new Date(""),
      title: "",
    },
    ulb: tenants?.find((tenant) => tenant?.code === tenantId),
  });
  let isMobile = window.Digit.Utils.browser.isMobile();
  const [data, setData] = useState([]);
  const { isLoading } = data;

  if (isLoading) {
    return <Loader />;
  }
  return (
    <React.Fragment>
      <div>
        <Header>{t("Registration-Requests")}</Header>
        <p>{}</p>
        <div className="inbox-search-wrapper">
          <InboxSearchComposer configs={config}></InboxSearchComposer>
        </div>
      </div>
    </React.Fragment>
  );
};

export default Inbox;
