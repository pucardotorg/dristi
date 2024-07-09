import { Button as ActionButton } from "@egovernments/digit-ui-components";
import { Button, Header, InboxSearchComposer, Menu } from "@egovernments/digit-ui-react-components";
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
import { CaseWorkflowAction } from "../../../../../orders/src/utils/caseWorkflow";
import { ordersService } from "../../../../../orders/src/hooks/services";

const defaultSearchValues = {
  individualName: "",
  mobileNumber: "",
  IndividualID: "",
};

const AdmittedCases = ({ isJudge = true }) => {
  const { t } = useTranslation();
  const urlParams = new URLSearchParams(window.location.search);
  const filingNumber = urlParams.get("filingNumber");
  const cnrNumber = urlParams.get("cnrNumber");
  const title = urlParams.get("title");
  const caseId = urlParams.get("caseId");
  const [show, setShow] = useState(false);
  const [comment, setComment] = useState("");
  const userRoles = Digit.UserService.getUser()?.info?.roles.map((role) => role.code);
  const [documentSubmission, setDocumentSubmission] = useState();
  const tenantId = window?.Digit.ULBService.getCurrentTenantId();
  const [showReviewModal, setShowReviewModal] = useState(false);
  const [currentOrder, setCurrentOrder] = useState();
  const [showMenu, setShowMenu] = useState(false);
  const history = useHistory();
  const showDownLoadCaseFIle = userRoles.includes("CITIZEN");
  const showMakeSubmission = userRoles.includes("APPLICATION_CREATOR");
  const docSetFunc = (docObj) => {
    setDocumentSubmission(docObj);
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
        : tabConfig.label === "Hearings"
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
          }
        : tabConfig.label === "Documents"
        ? {
            ...tabConfig,
            apiDetails: {
              ...tabConfig.apiDetails,
              requestBody: {
                ...tabConfig.apiDetails.requestBody,
                criteria: {
                  caseId: caseId,
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
                    return column.label === "File" || column.label === "Document Type"
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
                cnrNumber,
                applicationNumber: "",
              },
            },
          };
    });
  }, [caseId, cnrNumber, filingNumber, tenantId]);

  const newTabSearchConfig = {
    ...TabSearchconfig,
    TabSearchconfig: configList,
  };

  const [defaultValues, setDefaultValues] = useState(defaultSearchValues); // State to hold default values for search fields
  const [config, setConfig] = useState(newTabSearchConfig?.TabSearchconfig?.[0]); // initially setting first index config as default from jsonarray
  const [tabData, setTabData] = useState(
    newTabSearchConfig?.TabSearchconfig?.map((configItem, index) => ({ key: index, label: configItem.label, active: index === 0 ? true : false }))
  ); // setting number of tab component and making first index enable as default
  const [updateCounter, setUpdateCounter] = useState(0);
  useEffect(() => {
    // Set default values when component mounts
    setDefaultValues(defaultSearchValues);
  }, []);

  const onTabChange = (n) => {
    setTabData((prev) => prev.map((i, c) => ({ ...i, active: c === n ? true : false }))); //setting tab enable which is being clicked
    setConfig(newTabSearchConfig?.TabSearchconfig?.[n]); // as per tab number filtering the config
  };

  const formatDate = (date) => {
    const day = String(date.getDate()).padStart(2, "0");
    const month = String(date.getMonth() + 1).padStart(2, "0");
    const year = date.getFullYear();
    return `${day}-${month}-${year}`;
  };

  const handleMakeSubmission = () => {
    history.push(`/digit-ui/citizen/submissions/submissions-create?filingNumber=${filingNumber}`);
  };

  const handleSelect = (option) => {
    if (option === "GENERATE_ORDER_HOME") {
      const reqbody = {
        order: {
          createdDate: formatDate(new Date()),
          tenantId,
          cnrNumber,
          filingNumber: filingNumber,
          statuteSection: {
            tenantId,
          },
          orderType: "Bail",
          status: "",
          isActive: true,
          workflow: {
            action: CaseWorkflowAction.SAVE_DRAFT,
            comments: "Creating order",
            assignes: null,
            rating: null,
            documents: [{}],
          },
          documents: [],
          additionalDetails: {},
        },
      };
      ordersService
        .createOrder(reqbody, { tenantId })
        .then(() => {
          history.push(`/${window.contextPath}/employee/orders/generate-orders?filingNumber=${filingNumber}`);
        })
        .catch((err) => {});
    }
  };

  return (
    <React.Fragment>
      <div style={{ display: "flex", justifyContent: "space-between", alignItems: "center", padding: "10px" }}>
        <Header styles={{ fontSize: "32px", marginTop: "40px" }}>{t(title)}</Header>
        <div style={{ display: "flex", gap: 20, justifyContent: "space-between", alignItems: "center" }}>
          {showDownLoadCaseFIle && <Button variation={"outlined"} label={t("DOWNLOAD_CASE_FILE")} />}
          {showMakeSubmission && <Button label={t("MAKE_SUBMISSION")} onButtonClick={handleMakeSubmission} />}
        </div>
        {isJudge && (
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
                    options={
                      userRoles.includes("ORDER_CREATOR") || userRoles.includes("SUPERUSER") || userRoles.includes("EMPLOYEE")
                        ? ["GENERATE_ORDER_HOME", "Schedule Hearing", "Refer to ADR", "Abate Case"]
                        : ["Schedule Hearing", "Refer to ADR", "Abate Case"]
                    }
                    onSelect={(option) => handleSelect(option)}
                  ></Menu>
                )}
              </div>
            </div>
          </div>
        )}
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
      <ExtraComponent setUpdateCounter={setUpdateCounter} tab={config.label} />
      <div className="inbox-search-wrapper">
        {/* Pass defaultValues as props to InboxSearchComposer */}
        <InboxSearchComposer
          key={`${config.label}-${updateCounter}`}
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
          modalType={tabData.filter((tab) => tab.active)[0].label}
          setUpdateCounter={setUpdateCounter}
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
