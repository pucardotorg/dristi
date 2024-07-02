import { Button, Header, InboxSearchComposer } from "@egovernments/digit-ui-react-components";
import React, { useEffect, useMemo, useState } from "react";
import { useTranslation } from "react-i18next";
import ViewCaseFile from "../scrutiny/ViewCaseFile";
import { TabSearchconfig } from "./AdmittedCasesConfig";
import CaseOverview from "./CaseOverview";
import EvidenceModal from "./EvidenceModal";
import "./tabs.css";
const fieldStyle = { marginRight: 0 };

const defaultSearchValues = {
  individualName: "",
  mobileNumber: "",
  IndividualID: "",
};

const AdmittedCases = () => {
  const { t } = useTranslation();
  const searchParams = new URLSearchParams(location.search);
  const filingNumber = searchParams.get("filingNumber");
  const cnr = searchParams.get("cnr");
  const title = searchParams.get("title");
  const caseId = searchParams.get("caseId");
  console.log(TabSearchconfig?.TabSearchconfig);
  const [show, setShow] = useState(false);
  const [comment, setComment] = useState("");
  const user = localStorage.getItem("user-info");
  const userRoles = JSON.parse(user).roles.map((role) => role.code);
  const [documentSubmission, setDocumentSubmission] = useState();

  const docSetFunc = (docObj) => {
    setDocumentSubmission(docObj);
    console.log(docObj);
    setShow(true);
  };

  const configList = useMemo(() => {
    return TabSearchconfig?.TabSearchconfig.map((tabConfig) => {
      return tabConfig.label === "Parties"
        ? {
            ...tabConfig,
            apiDetails: {
              ...tabConfig.apiDetails,
              requestBody: {
                ...tabConfig.apiDetails.requestBody,
                criteria: [
                  {
                    filingNumber: filingNumber,
                  },
                ],
              },
            },
          }
        : tabConfig.label === "Submissions"
        ? {
            ...tabConfig,
            sections: {
              ...tabConfig.sections,
              searchResult: {
                ...tabConfig.sections.searchResult,
                uiConfig: {
                  ...tabConfig.sections.searchResult.uiConfig,
                  columns: tabConfig.sections.searchResult.uiConfig.columns.map((column) => {
                    return column.label === "Document" || column.label === "Submission Name"
                      ? {
                          ...column,
                          clickFunc: docSetFunc,
                        }
                      : column;
                  }),
                },
              },
            },
          }
        : {
            ...tabConfig,
            apiDetails: {
              ...tabConfig.apiDetails,
              requestParam: {
                ...tabConfig.apiDetails?.requestParam,
                filingNumber: filingNumber,
                cnrNumber: cnr,
                applicationNumber: "",
              },
            },
          };
    });
  }, [filingNumber]);
  console.log(configList);
  const newTabSearchConfig = {
    ...TabSearchconfig,
    TabSearchconfig: configList,
  };
  console.log(newTabSearchConfig);

  console.log(filingNumber);
  const [defaultValues, setDefaultValues] = useState(defaultSearchValues); // State to hold default values for search fields
  const [config, setConfig] = useState(newTabSearchConfig?.TabSearchconfig?.[0]); // initially setting first index config as default from jsonarray
  const [tabData, setTabData] = useState(
    newTabSearchConfig?.TabSearchconfig?.map((configItem, index) => ({ key: index, label: configItem.label, active: index === 0 ? true : false }))
  ); // setting number of tab component and making first index enable as default
  useEffect(() => {
    // Set default values when component mounts
    setDefaultValues(defaultSearchValues);
  }, []);

  const onTabChange = (n) => {
    setTabData((prev) => prev.map((i, c) => ({ ...i, active: c === n ? true : false }))); //setting tab enable which is being clicked
    setConfig(newTabSearchConfig?.TabSearchconfig?.[n]); // as per tab number filtering the config
  };

  return (
    <React.Fragment>
      <div style={{ display: "flex", gap: "20px" }}>
        <Header styles={{ fontSize: "32px", marginTop: "40px" }}>{t(title)}</Header>
        <Button className={"generate-order"} label={"Generate Order / Notice"} />
      </div>
      <div className="search-tabs-container">
        <div>
          {tabData?.map((i, num) => (
            <button
              className={i?.active === true ? "search-tab-head-selected" : "search-tab-head"}
              onClick={() => {
                onTabChange(num);
              }}
            >
              {t(i?.label)}
            </button>
          ))}
        </div>
      </div>
      <div className="inbox-search-wrapper">
        {/* Pass defaultValues as props to InboxSearchComposer */}
        <InboxSearchComposer
          configs={config}
          defaultValues={defaultValues}
          showTab={false}
          tabData={tabData}
          onTabChange={onTabChange}
        ></InboxSearchComposer>
      </div>
      {tabData.filter((tab) => tab.label === "Overview")[0].active && (
        <div>
          <CaseOverview />
        </div>
      )}
      {tabData.filter((tab) => tab.label === "Complaints")[0].active && (
        <div>
          <ViewCaseFile t={t} showHeader={false} />
        </div>
      )}
      {show && (
        <EvidenceModal
          documentSubmission={documentSubmission}
          show={show}
          setShow={setShow}
          comment={comment}
          setComment={setComment}
          userRoles={userRoles}
        />
      )}
    </React.Fragment>
  );
};

export default AdmittedCases;
