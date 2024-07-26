import { Button as ActionButton } from "@egovernments/digit-ui-components";
import { Button, Header, InboxSearchComposer, Loader, Menu, Toast } from "@egovernments/digit-ui-react-components";
import React, { useEffect, useMemo, useState } from "react";
import { useTranslation } from "react-i18next";
import { useHistory, useRouteMatch } from "react-router-dom";
import useSearchCaseService from "../../../hooks/dristi/useSearchCaseService";
import { CustomThreeDots } from "../../../icons/svgIndex";
import { CaseWorkflowState } from "../../../Utils/caseWorkflow";
import ViewCaseFile from "../scrutiny/ViewCaseFile";
import { TabSearchconfig } from "./AdmittedCasesConfig";
import CaseOverview from "./CaseOverview";
import EvidenceModal from "./EvidenceModal";
import ExtraComponent from "./ExtraComponent";
import "./tabs.css";
import { SubmissionWorkflowState } from "../../../Utils/submissionWorkflow";
import { OrderWorkflowState } from "../../../Utils/orderWorkflow";
import ScheduleHearing from "./ScheduleHearing";
import ViewAllOrderDrafts from "./ViewAllOrderDrafts";
import PublishedOrderModal from "./PublishedOrderModal";

const defaultSearchValues = {
  individualName: "",
  mobileNumber: "",
  IndividualID: "",
};

const AdmittedCases = ({ isJudge = true }) => {
  const { t } = useTranslation();
  const { path } = useRouteMatch();
  const urlParams = new URLSearchParams(window.location.search);
  const caseId = urlParams.get("caseId");
  const activeTab = urlParams.get("tab") || "Overview";
  const filingNumber = urlParams.get("filingNumber");
  const [show, setShow] = useState(false);
  const userRoles = Digit.UserService.getUser()?.info?.roles.map((role) => role.code);
  const [documentSubmission, setDocumentSubmission] = useState();
  const tenantId = window?.Digit.ULBService.getCurrentTenantId();
  const [showOrderReviewModal, setShowOrderReviewModal] = useState(false);
  const [currentOrder, setCurrentOrder] = useState();
  const [showMenu, setShowMenu] = useState(false);
  const [toast, setToast] = useState(false);
  const [orderDraftModal, setOrderDraftModal] = useState(false);
  const [draftOrderList, setDraftOrderList] = useState([]);
  const history = useHistory();
  const isCitizen = userRoles.includes("CITIZEN");
  const OrderWorkflowAction = Digit.ComponentRegistryService.getComponent("OrderWorkflowActionEnum") || {};
  const ordersService = Digit.ComponentRegistryService.getComponent("OrdersService") || {};
  const OrderReviewModal = Digit.ComponentRegistryService.getComponent("OrderReviewModal") || {};

  const { data: caseData, isLoading } = useSearchCaseService(
    {
      criteria: [
        {
          caseId: caseId,
        },
      ],
      tenantId,
    },
    {},
    "dristi",
    caseId,
    caseId
  );
  const caseDetails = useMemo(() => caseData?.criteria[0]?.responseList[0], [caseData]);
  const cnrNumber = useMemo(() => caseDetails?.cnrNumber, [caseDetails]);

  const statue = useMemo(
    () =>
      caseDetails?.statutesAndSections[0]?.sections[0]
        ? `${caseDetails?.statutesAndSections[0]?.sections[0]
            ?.split(" ")
            ?.map((splitString) => splitString.charAt(0))
            ?.join("")} ${caseDetails?.statutesAndSections[0]?.subsections[0]}`
        : "",
    [caseDetails?.statutesAndSections]
  );

  const caseRelatedData = useMemo(
    () => ({
      caseId,
      filingNumber,
      cnrNumber,
      title: caseDetails?.caseTitle || "",
      stage: caseDetails?.stage,
      case: caseDetails,
      statue: statue,
    }),
    [caseDetails, caseId, cnrNumber, filingNumber, statue]
  );

  const showMakeSubmission = useMemo(() => {
    return (
      userRoles.includes("APPLICATION_CREATOR") &&
      [CaseWorkflowState.CASE_ADMITTED, CaseWorkflowState.ADMISSION_HEARING_SCHEDULED].includes(caseDetails?.status)
    );
  }, [userRoles, caseDetails]);

  const openDraftModal = (orderList) => {
    setDraftOrderList(orderList);
    setOrderDraftModal(true);
  };

  const handleTakeAction = () => {
    setShowMenu(!showMenu);
    setShowOtherMenu(false);
  };

  const configList = useMemo(() => {
    const docSetFunc = (docObj) => {
      const applicationNumber = docObj?.[0]?.applicationList?.applicationNumber;
      const status = docObj?.[0]?.applicationList?.status;
      if (isCitizen) {
        if (
          [SubmissionWorkflowState.PENDINGPAYMENT, SubmissionWorkflowState.PENDINGESIGN, SubmissionWorkflowState.PENDINGSUBMISSION].includes(status)
        ) {
          /// if createdBy is same user as logged in
          history.push(`/digit-ui/citizen/submissions/submissions-create?filingNumber=${filingNumber}&applicationNumber=${applicationNumber}`);
        } else {
          /// if user only has respondant then open the modal
          setDocumentSubmission(docObj);
          setShow(true);
        }
      } else {
        if (
          ![SubmissionWorkflowState.PENDINGPAYMENT, SubmissionWorkflowState.PENDINGESIGN, SubmissionWorkflowState.PENDINGSUBMISSION].includes(status)
        ) {
          setDocumentSubmission(docObj);
          setShow(true);
        }
      }
    };

    const orderSetFunc = (order) => {
      if (isCitizen) {
        // for citizen, only those orders should be visible which are published
        setCurrentOrder(order);
        setShowOrderReviewModal(true);
      } else {
        if (order?.status === OrderWorkflowState.DRAFT_IN_PROGRESS) {
          history.push(`/${window.contextPath}/employee/orders/generate-orders?filingNumber=${filingNumber}&orderNumber=${order?.orderNumber}`);
        } else {
          setCurrentOrder(order);
          setShowOrderReviewModal(true);
        }
      }
    };

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
        : tabConfig.label === "History"
        ? {
            ...tabConfig,
            apiDetails: {
              ...tabConfig.apiDetails,
              requestBody: {
                ...tabConfig.apiDetails.requestBody,
                filingNumber: filingNumber,
                tenantId: tenantId,
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
  }, [caseId, cnrNumber, filingNumber, history, isCitizen, tenantId]);

  const newTabSearchConfig = {
    ...TabSearchconfig,
    TabSearchconfig: configList,
  };

  const indexOfActiveTab = newTabSearchConfig?.TabSearchconfig?.findIndex((tabData) => tabData.label === activeTab);

  const [defaultValues, setDefaultValues] = useState(defaultSearchValues); // State to hold default values for search fields
  const config = useMemo(() => {
    return newTabSearchConfig?.TabSearchconfig?.[indexOfActiveTab];
  }, [indexOfActiveTab, newTabSearchConfig?.TabSearchconfig]); // initially setting first index config as default from jsonarray

  const tabData = useMemo(() => {
    return newTabSearchConfig?.TabSearchconfig?.map((configItem, index) => ({
      key: index,
      label: configItem.label,
      active: index === indexOfActiveTab ? true : false,
    }));
  }, [indexOfActiveTab, newTabSearchConfig?.TabSearchconfig]); // setting number of tab component and making first index enable as default
  const [updateCounter, setUpdateCounter] = useState(0);
  const [toastDetails, setToastDetails] = useState({});
  const [showOtherMenu, setShowOtherMenu] = useState(false);
  const [showScheduleHearingModal, setShowScheduleHearingModal] = useState(false);

  const isTabDisabled = useMemo(() => {
    return caseDetails?.status !== "CASE_ADMITTED" && caseDetails?.status !== "ADMISSION_HEARING_SCHEDULED" && config?.label !== "Complaint";
  }, [caseDetails?.status, config?.label]);

  useEffect(() => {
    if (history?.location?.state?.from && history?.location?.state?.from === "orderSuccessModal") {
      showToast(true);
      setToastDetails({
        isError: false,
        message: "ORDER_SUCCESSFULLY_ISSUED",
      });
    }
  }, [history.location]);

  useEffect(() => {
    // Set default values when component mounts
    setDefaultValues(defaultSearchValues);
  }, []);

  const onTabChange = (n) => {
    history.replace(`${path}?caseId=${caseId}&filingNumber=${filingNumber}&tab=${newTabSearchConfig?.TabSearchconfig?.[n].label}`);
    // urlParams.set("tab", newTabSearchConfig?.TabSearchconfig?.[n].label);
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
    if (option === t("SCHEDULE_HEARING")) {
      openHearingModule();
      return;
    } else if (option === t("REFER_TO_ADR")) {
      const reqBody = {
        order: {
          createdDate: formatDate(new Date()),
          tenantId,
          cnrNumber,
          filingNumber: filingNumber,
          statuteSection: {
            tenantId,
          },
          orderType: "REFERRAL_CASE_TO_ADR",
          status: "",
          isActive: true,
          workflow: {
            action: OrderWorkflowAction.SAVE_DRAFT,
            comments: "Creating order",
            assignes: null,
            rating: null,
            documents: [{}],
          },
          documents: [],
          additionalDetails: {
            formdata: {
              orderType: {
                id: 4,
                type: "REFERRAL_CASE_TO_ADR",
                isactive: true,
                code: "REFERRAL_CASE_TO_ADR",
                name: "ORDER_TYPE_REFERRAL_CASE_TO_ADR",
              },
            },
          },
        },
      };
      ordersService
        .createOrder(reqBody, { tenantId })
        .then((res) => {
          history.push(`/${window.contextPath}/employee/orders/generate-orders?filingNumber=${filingNumber}&orderNumber=${res.order.orderNumber}`, {
            caseId: caseId,
            tab: activeTab,
          });
        })
        .catch((err) => {});
      return;
    }
    history.push(`/${window.contextPath}/employee/orders/generate-orders?filingNumber=${filingNumber}`, { caseId: caseId, tab: "Orders" });
  };

  const showToast = (details, duration = 5000) => {
    setToast(true);
    setToastDetails(details);
    setTimeout(() => {
      setToast(false);
      // history.replace(history.location.pathname + history.location.search, { from: "" });
    }, duration);
  };

  const handleDownload = () => {
    setShowOrderReviewModal(false);
  };
  const handleRequestLabel = () => {
    setShowOrderReviewModal(false);
  };
  const handleSubmitDocument = () => {
    setShowOrderReviewModal(false);
  };

  const openHearingModule = () => {
    setShowScheduleHearingModal(true);
  };

  const caseAdmittedSubmit = (data) => {
    const dateArr = data.date.split(" ").map((date, i) => (i === 0 ? date.slice(0, date.length - 2) : date));
    const date = new Date(dateArr.join(" "));
    const reqBody = {
      order: {
        createdDate: formatDate(new Date()),
        tenantId,
        cnrNumber,
        filingNumber: filingNumber,
        statuteSection: {
          tenantId,
        },
        orderType: "SCHEDULE_OF_HEARING_DATE",
        status: "",
        isActive: true,
        workflow: {
          action: OrderWorkflowAction.SAVE_DRAFT,
          comments: "Creating order",
          assignes: null,
          rating: null,
          documents: [{}],
        },
        documents: [],
        additionalDetails: {
          formdata: {
            hearingDate: `${dateArr[2]}-${date.getMonth() < 9 ? `0${date.getMonth() + 1}` : date.getMonth() + 1}-${dateArr[0]}`,
            hearingPurpose: data.purpose,
            orderType: {
              id: 7,
              code: "SCHEDULE_OF_HEARING_DATE",
              type: "SCHEDULE_OF_HEARING_DATE",
              isactive: true,
              name: "ORDER_TYPE_SCHEDULE_OF_HEARING_DATE",
            },
          },
        },
      },
    };
    ordersService
      .createOrder(reqBody, { tenantId })
      .then((res) => {
        history.push(`/${window.contextPath}/employee/orders/generate-orders?filingNumber=${filingNumber}&orderNumber=${res.order.orderNumber}`);
      })
      .catch((err) => {});
  };

  if (isLoading) {
    return <Loader />;
  }

  return (
    <div style={{ position: "absolute", width: "100%" }}>
      <div style={{ position: "sticky", top: "72px", width: "100%", height: "100%", zIndex: 150, background: "white" }}>
        <div style={{ display: "flex", justifyContent: "space-between", alignItems: "center", padding: "10px" }}>
          <div style={{ display: "flex", alignItems: "center", gap: "5px" }}>
            <Header styles={{ fontSize: "32px", marginTop: "10px" }}>{caseDetails?.caseTitle || ""}</Header>
            {statue && (
              <React.Fragment>
                <hr className="vertical-line" />
                <div className="sub-details-text">{statue}</div>
              </React.Fragment>
            )}
            <hr className="vertical-line" />
            <div className="sub-details-text">{caseDetails?.stage}</div>
            <hr className="vertical-line" />
            <div className="sub-details-text">Code: {caseData.criteria[0].responseList[0].accessCode}</div>
          </div>
          <div className="make-submission-action" style={{ display: "flex", gap: 20, justifyContent: "space-between", alignItems: "center" }}>
            {isCitizen && <Button variation={"outlined"} label={t("DOWNLOAD_CASE_FILE")} />}
            {showMakeSubmission && <Button label={t("MAKE_SUBMISSION")} onButtonClick={handleMakeSubmission} />}
          </div>
          {isJudge && (
            <div className="judge-action-block" style={{ display: "flex" }}>
              <div className="evidence-header-wrapper">
                <div className="evidence-hearing-header" style={{ background: "transparent" }}>
                  <div className="evidence-actions" style={{ ...(isTabDisabled ? { pointerEvents: "none" } : {}) }}>
                    <ActionButton
                      variation={"primary"}
                      label={t("TAKE_ACTION_LABEL")}
                      icon={showMenu ? "ExpandLess" : "ExpandMore"}
                      isSuffix={true}
                      onClick={handleTakeAction}
                      className={"take-action-btn-class"}
                    ></ActionButton>
                    {showMenu && (
                      <Menu
                        options={
                          userRoles.includes("ORDER_CREATOR")
                            ? [t("GENERATE_ORDER_HOME"), t("SCHEDULE_HEARING"), t("REFER_TO_ADR")]
                            : [t("SCHEDULE_HEARING"), t("REFER_TO_ADR")]
                        }
                        onSelect={(option) => handleSelect(option)}
                      ></Menu>
                    )}
                  </div>
                </div>
              </div>
              <div className="evidence-header-wrapper">
                <div className="evidence-hearing-header" style={{ background: "transparent" }}>
                  <div className="evidence-actions">
                    <div
                      className="custom-icon-wrapper"
                      onClick={() => {
                        setShowOtherMenu((prev) => !prev);
                        setShowMenu(false);
                      }}
                    >
                      <CustomThreeDots />
                      {showOtherMenu && (
                        <Menu
                          options={[t("DOWNLOAD_CASE_FILE")]}
                          // onSelect={() => {}}
                        ></Menu>
                      )}
                    </div>
                  </div>
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
                disabled={isTabDisabled}
              >
                {t(i?.label)}
              </button>
            ))}
          </div>
        </div>
      </div>
      <ExtraComponent caseData={caseRelatedData} setUpdateCounter={setUpdateCounter} tab={config?.label} setOrderModal={openDraftModal} />
      {config?.label !== "Overview" && config?.label !== "Complaints" && (
        <div style={{ width: "100%", background: "white", padding: "10px", display: "flex", justifyContent: "space-between" }}>
          <div style={{ fontWeight: 700, fontSize: "24px", lineHeight: "28.8px" }}>{t(`All_${config?.label.toUpperCase()}_TABLE_HEADER`)}</div>
          {userRoles.includes("ORDER_CREATOR") && config?.label === "Orders" && (
            <div
              onClick={() => handleSelect(t("GENERATE_ORDER_HOME"))}
              style={{ fontWeight: 500, fontSize: "16px", lineHeight: "20px", color: "#0A5757", cursor: "pointer" }}
            >
              {t("GENERATE_ORDERS_LINK")}
            </div>
          )}
          {userRoles.includes("ORDER_CREATOR") && config?.label === "Submissions" && (
            <div
              // onClick={() => handleSelect(t("GENERATE_ORDER_HOME"))}
              style={{ fontWeight: 500, fontSize: "16px", lineHeight: "20px", color: "#0A5757", cursor: "pointer" }}
            >
              {t("REQUEST_DOCUMENTS_LINK")}
            </div>
          )}
          {isCitizen && config?.label === "Submissions" && (
            <div
              onClick={handleMakeSubmission}
              style={{ fontWeight: 500, fontSize: "16px", lineHeight: "20px", color: "#0A5757", cursor: "pointer" }}
            >
              {t("MAKE_SUBMISSION")}
            </div>
          )}
        </div>
      )}
      <div className="inbox-search-wrapper">
        {/* Pass defaultValues as props to InboxSearchComposer */}
        <InboxSearchComposer
          key={`${config?.label}-${updateCounter}`}
          configs={config}
          defaultValues={defaultValues}
          showTab={false}
          tabData={tabData}
          onTabChange={onTabChange}
        ></InboxSearchComposer>
      </div>
      {tabData.filter((tab) => tab.label === "Overview")[0].active && (
        <div className="case-overview-wrapper">
          <CaseOverview
            handleDownload={handleDownload}
            handleRequestLabel={handleRequestLabel}
            handleSubmitDocument={handleSubmitDocument}
            caseData={caseRelatedData}
            setUpdateCounter={setUpdateCounter}
            showToast={showToast}
          />
        </div>
      )}
      {tabData.filter((tab) => tab.label === "Complaints")[0].active && (
        <div className="view-case-file-wrapper">
          <ViewCaseFile t={t} inViewCase={true} />
        </div>
      )}
      {show && (
        <EvidenceModal
          documentSubmission={documentSubmission}
          show={show}
          setShow={setShow}
          userRoles={userRoles}
          modalType={tabData.filter((tab) => tab.active)[0].label}
          setUpdateCounter={setUpdateCounter}
          showToast={showToast}
          caseData={caseRelatedData}
        />
      )}
      {showOrderReviewModal && (
        <PublishedOrderModal
          t={t}
          order={currentOrder}
          setShowReviewModal={setShowOrderReviewModal}
          handleDownload={handleDownload}
          handleRequestLabel={handleRequestLabel}
          handleSubmitDocument={handleSubmitDocument}
        />
      )}

      {showScheduleHearingModal && (
        <ScheduleHearing
          setUpdateCounter={setUpdateCounter}
          showToast={showToast}
          tenantId={tenantId}
          caseData={caseRelatedData}
          setShowModal={setShowScheduleHearingModal}
          caseAdmittedSubmit={caseAdmittedSubmit}
        />
      )}

      {showScheduleHearingModal && (
        <ScheduleHearing
          setUpdateCounter={setUpdateCounter}
          showToast={showToast}
          tenantId={tenantId}
          caseData={caseRelatedData}
          setShowModal={setShowScheduleHearingModal}
          caseAdmittedSubmit={caseAdmittedSubmit}
        />
      )}
      {orderDraftModal && <ViewAllOrderDrafts t={t} setShow={setOrderDraftModal} draftOrderList={draftOrderList} filingNumber={filingNumber} />}
      {toast && toastDetails && (
        <Toast error={toastDetails?.isError} label={t(toastDetails?.message)} onClose={() => setToast(false)} style={{ maxWidth: "670px" }} />
      )}
    </div>
  );
};

export default AdmittedCases;
