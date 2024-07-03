import { Button as ActionButton } from "@egovernments/digit-ui-components";
import { Header, InboxSearchComposer, Menu } from "@egovernments/digit-ui-react-components";
import React, { useEffect, useMemo, useState } from "react";
import { useTranslation } from "react-i18next";
import { useHistory } from "react-router-dom/cjs/react-router-dom.min";
import OrderReviewModal from "../../../../../orders/src/pageComponents/OrderReviewModal";
import ViewCaseFile from "../scrutiny/ViewCaseFile";
import { TabSearchconfig } from "./AdmittedCasesConfig";
import CaseOverview from "./CaseOverview";
import EvidenceModal from "./EvidenceModal";
import ExtraComponent from "./ExtraComponent";
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
  const [show, setShow] = useState(false);
  const [comment, setComment] = useState("");
  const user = localStorage.getItem("user-info");
  const userRoles = JSON.parse(user).roles.map((role) => role.code);
  const [documentSubmission, setDocumentSubmission] = useState();
  const tenantId = window?.Digit.ULBService.getCurrentTenantId();
  const [showReviewModal, setShowReviewModal] = useState(false);
  const [currentOrder, setCurrentOrder] = useState();
  const [showMenu, setShowMenu] = useState(false);
  const history = useHistory();

  const docSetFunc = (docObj) => {
    setDocumentSubmission(docObj);
    console.log(docObj);
    setShow(true);
  };

  const orderSetFunc = (order) => {
    setCurrentOrder(order);
    setShowReviewModal(true);
  };

  const handleTakeAction = () => {
    setShowMenu(!showMenu);
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
        : tabConfig.label === "Orders"
        ? {
            ...tabConfig,
            apiDetails: {
              ...tabConfig.apiDetails,
              requestBody: {
                ...tabConfig.apiDetails.requestBody,
                criteria: {
                  filingNumber: filingNumber,
                  tenantId: tenantId,
                },
              },
            },
            sections: {
              ...tabConfig.sections,
              searchResult: {
                ...tabConfig.sections.searchResult,
                uiConfig: {
                  ...tabConfig.sections.searchResult.uiConfig,
                  columns: tabConfig.sections.searchResult.uiConfig.columns.map((column) => {
                    return column.label === "Order Type"
                      ? {
                          ...column,
                          clickFunc: orderSetFunc,
                        }
                      : column;
                  }),
                },
              },
            },
          }
        : tabConfig.label === "Submissions"
        ? {
            ...tabConfig,
            apiDetails: {
              ...tabConfig.apiDetails,
              requestBody: {
                ...tabConfig.apiDetails.requestBody,
                criteria: {
                  filingNumber: filingNumber,
                  tenantId: tenantId,
                },
              },
            },
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

  console.log(config, tabData, "Admitted Cases Config");

  const onTabChange = (n) => {
    setTabData((prev) => prev.map((i, c) => ({ ...i, active: c === n ? true : false }))); //setting tab enable which is being clicked
    setConfig(newTabSearchConfig?.TabSearchconfig?.[n]); // as per tab number filtering the config
  };

  const handleSelect = (option) => {
    console.log(option);

    if (option == "Generate Order / Home") {
      history.push(`/${window.contextPath}/employee/orders/generate-orders?filingNumber=${filingNumber}`);
    }
  };

  return (
    <React.Fragment>
      <div style={{ display: "flex", justifyContent: "space-between", alignItems: "center", padding: "10px" }}>
        <Header styles={{ fontSize: "32px", marginTop: "40px" }}>{t(title)}</Header>
        <div className="evidence-header-wrapper">
          <div className="evidence-hearing-header" style={{ background: "transparent" }}>
            <div className="evidence-actions">
              <ActionButton
                variation={"primary"}
                label={"Take Action"}
                icon={showMenu ? "ExpandLess" : "ExpandMore"}
                isSuffix={true}
                onClick={handleTakeAction}
              ></ActionButton>
              {showMenu && (
                <Menu
                  options={["Generate Order / Home", "Schedule Hearing", "Refer to ADR", "Abate Case"]}
                  onSelect={(option) => handleSelect(option)}
                ></Menu>
              )}
            </div>
          </div>
        </div>
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
      <ExtraComponent tab={config.label} />
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
      {showReviewModal && (
        <OrderReviewModal
          t={t}
          order={currentOrder}
          setShowReviewModal={setShowReviewModal}
          setShowsignatureModal={() => {}}
          handleSaveDraft={() => {}}
          showActions={false}
        />
      )}
    </React.Fragment>
  );
};

export default AdmittedCases;
