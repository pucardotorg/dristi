import { useTranslation } from "react-i18next";
import React, { useState, useEffect, useMemo, useCallback } from "react";
import { useHistory } from "react-router-dom";
import { Button, InboxSearchComposer, CardLabel, CardLabelError, LabelFieldPair, TextInput } from "@egovernments/digit-ui-react-components";
import { InfoCard } from "@egovernments/digit-ui-components";

import { rolesToConfigMapping, userTypeOptions } from "../../configs/HomeConfig";
import UpcomingHearings from "../../components/UpComingHearing";
import { Loader } from "@egovernments/digit-ui-react-components";
import TasksComponent from "../../components/TaskComponent";
import { useLocation } from "react-router-dom/cjs/react-router-dom.min";
import { HomeService, Urls } from "../../hooks/services";
import LitigantHomePage from "./LitigantHomePage";
import { TabLitigantSearchConfig } from "../../configs/LitigantHomeConfig";
import ReviewCard from "../../components/ReviewCard";
import { InboxIcon, DocumentIcon } from "../../../homeIcon";
import { Link } from "react-router-dom";
import isEqual from "lodash/isEqual";
import CustomStepperSuccess from "@egovernments/digit-ui-module-orders/src/components/CustomStepperSuccess";
import { uploadResponseDocumentConfig } from "@egovernments/digit-ui-module-dristi/src/pages/citizen/FileCase/Config/resgisterRespondentConfig";
import UploadIdType from "@egovernments/digit-ui-module-dristi/src/pages/citizen/registration/UploadIdType";
import DocumentModal from "@egovernments/digit-ui-module-orders/src/components/DocumentModal";
import { submitJoinCase, updateCaseDetails } from "../../../../cases/src/utils/joinCaseUtils";
import CustomErrorTooltip from "@egovernments/digit-ui-module-dristi/src/components/CustomErrorTooltip";
import { DRISTIService } from "@egovernments/digit-ui-module-dristi/src/services";
import { Urls as PendingsUrls } from "@egovernments/digit-ui-module-dristi/src/hooks";

const defaultSearchValues = {
  filingNumber: "",
  caseType: "NIA S138",
};

const HomeView = () => {
  const history = useHistory();
  const location = useLocation();
  const { state } = location;
  const { t } = useTranslation();
  const Digit = window.Digit || {};
  const token = window.localStorage.getItem("token");
  const isUserLoggedIn = Boolean(token);
  const [defaultValues, setDefaultValues] = useState(defaultSearchValues);
  const [config, setConfig] = useState(TabLitigantSearchConfig?.TabSearchConfig?.[0]);
  const [caseDetails, setCaseDetails] = useState(null);
  const [isFetchCaseLoading, setIsFetchCaseLoading] = useState(false);
  const [tabData, setTabData] = useState(
    TabLitigantSearchConfig?.TabSearchConfig?.map((configItem, index) => ({
      key: index,
      label: configItem.label,
      active: index === 0 ? true : false,
    }))
  );
  const [callRefetch, setCallRefetch] = useState(false);
  const [tabConfig, setTabConfig] = useState(TabLitigantSearchConfig);
  const [onRowClickData, setOnRowClickData] = useState({ url: "", params: [] });
  const [taskType, setTaskType] = useState(state?.taskType || {});
  const [caseType, setCaseType] = useState(state?.caseType || {});

  const [showSubmitResponseModal, setShowSubmitResponseModal] = useState(false);
  const [responsePendingTask, setResponsePendingTask] = useState({});

  const roles = useMemo(() => Digit.UserService.getUser()?.info?.roles, [Digit.UserService]);
  const isJudge = useMemo(() => roles?.some((role) => role?.code === "JUDGE_ROLE"), [roles]);
  const isCourtRoomRole = useMemo(() => roles?.some((role) => role?.code === "COURT_ADMIN"), [roles]);
  const isNyayMitra = roles.some((role) => role.code === "NYAY_MITRA_ROLE");
  const tenantId = useMemo(() => window?.Digit.ULBService.getCurrentTenantId(), []);
  const userInfo = Digit?.UserService?.getUser()?.info;
  const userInfoType = useMemo(() => (userInfo?.type === "CITIZEN" ? "citizen" : "employee"), [userInfo]);
  const { data: individualData, isLoading, isFetching } = window?.Digit.Hooks.dristi.useGetIndividualUser(
    {
      Individual: {
        userUuid: [userInfo?.uuid],
      },
    },
    { tenantId, limit: 1000, offset: 0 },
    "Home",
    "",
    userInfo?.uuid && isUserLoggedIn
  );
  const individualId = useMemo(() => individualData?.Individual?.[0]?.individualId, [individualData]);

  const userType = useMemo(() => individualData?.Individual?.[0]?.additionalFields?.fields?.find((obj) => obj.key === "userType")?.value, [
    individualData?.Individual,
  ]);
  const { data: searchData, isLoading: isSearchLoading } = Digit?.Hooks?.dristi?.useGetAdvocateClerk(
    {
      criteria: [{ individualId }],
      tenantId,
    },
    { tenantId },
    individualId,
    Boolean(isUserLoggedIn && individualId && userType !== "LITIGANT"),
    userType === "ADVOCATE" ? "/advocate/v1/_search" : "/advocate/clerk/v1/_search"
  );

  const refreshInbox = () => {
    setCallRefetch(!callRefetch);
  };

  const userTypeDetail = useMemo(() => {
    return userTypeOptions.find((item) => item.code === userType) || {};
  }, [userType]);

  const searchResult = useMemo(() => {
    return searchData?.[`${userTypeDetail?.apiDetails?.requestKey}s`]?.[0]?.responseList;
  }, [searchData, userTypeDetail?.apiDetails?.requestKey]);

  const isApprovalPending = useMemo(() => {
    return (
      userType !== "LITIGANT" &&
      Array.isArray(searchResult) &&
      searchResult?.length > 0 &&
      searchResult?.[0]?.isActive === false &&
      searchResult?.[0]?.status !== "INACTIVE"
    );
  }, [searchResult, userType]);

  const advocateId = useMemo(() => {
    return searchResult?.[0]?.id;
  }, [searchResult]);

  const additionalDetails = useMemo(() => {
    return {
      ...(advocateId
        ? {
            searchKey: "filingNumber",
            defaultFields: true,
            advocateId: advocateId,
          }
        : individualId
        ? {
            searchKey: "filingNumber",
            defaultFields: true,
            litigantId: individualId,
          }
        : {}),
    };
  }, [advocateId, individualId]);

  useEffect(() => {
    setDefaultValues(defaultSearchValues);
  }, []);

  useEffect(() => {
    state && state.taskType && setTaskType(state.taskType);
  }, [state]);

  const { isLoading: isOutcomeLoading, data: outcomeTypeData } = Digit.Hooks.useCustomMDMS(
    Digit.ULBService.getStateId(),
    "case",
    [{ name: "OutcomeType" }],
    {
      select: (data) => {
        return (data?.case?.OutcomeType || []).flatMap((item) => {
          return item?.judgementList?.length > 0 ? item.judgementList : [item?.outcome];
        });
      },
    }
  );

  const getTotalCountForTab = useCallback(
    async function (tabConfig) {
      const updatedTabData = await Promise.all(
        tabConfig?.TabSearchConfig?.map(async (configItem, index) => {
          const response = await HomeService.customApiService(configItem?.apiDetails?.serviceName, {
            tenantId,
            criteria: [
              {
                ...configItem?.apiDetails?.requestBody?.criteria?.[0],
                ...defaultSearchValues,
                ...additionalDetails,
                ...(configItem?.apiDetails?.requestBody?.criteria[0]["outcome"] && {
                  outcome: outcomeTypeData,
                }),
                pagination: { offSet: 0, limit: 1 },
              },
            ],
          });
          const totalCount = response?.criteria?.[0]?.pagination?.totalCount;
          return {
            key: index,
            label: totalCount ? `${t(configItem.label)} (${totalCount})` : `${t(configItem.label)} (0)`,
            active: index === 0 ? true : false,
          };
        }) || []
      );
      setTabData(updatedTabData);
    },
    [additionalDetails, outcomeTypeData, tenantId]
  );

  useEffect(() => {
    if (!(isLoading && isFetching && isSearchLoading && isFetchCaseLoading && isOutcomeLoading)) {
      if (state?.role && rolesToConfigMapping?.find((item) => item[state.role])[state.role]) {
        const rolesToConfigMappingData = rolesToConfigMapping?.find((item) => item[state.role]);
        const tabConfig = rolesToConfigMappingData.config;
        const rowClickData = rolesToConfigMappingData.onRowClickRoute;
        setOnRowClickData(rowClickData);
        setConfig(tabConfig?.TabSearchConfig?.[0]);
        setTabConfig(tabConfig);
        getTotalCountForTab(tabConfig);
      } else {
        const rolesToConfigMappingData =
          rolesToConfigMapping?.find((item) =>
            item.roles?.reduce((res, curr) => {
              if (!res) return res;
              res = roles.some((role) => role.code === curr);
              return res;
            }, true)
          ) || TabLitigantSearchConfig;
        const tabConfig = rolesToConfigMappingData?.config;
        const rowClickData = rolesToConfigMappingData?.onRowClickRoute;
        setOnRowClickData(rowClickData);
        setConfig(tabConfig?.TabSearchConfig?.[0]);
        setTabConfig(tabConfig);
        getTotalCountForTab(tabConfig);
      }
    }
  }, [additionalDetails, getTotalCountForTab, isOutcomeLoading, isFetchCaseLoading, isFetching, isLoading, isSearchLoading, roles, state, tenantId]);

  // calling case api for tab's count
  useEffect(() => {
    (async function () {
      if (userType) {
        setIsFetchCaseLoading(true);
        const caseData = await HomeService.customApiService(Urls.caseSearch, {
          tenantId,
          criteria: [
            {
              ...(advocateId ? { advocateId } : { litigantId: individualId }),

              pagination: { offSet: 0, limit: 1 },
            },
          ],
        });
        setCaseDetails(caseData?.criteria?.[0]?.responseList?.[0]);
        setIsFetchCaseLoading(false);
      }
    })();
  }, [advocateId, individualId, tenantId, userType]);

  const onTabChange = (n) => {
    setTabData((prev) => prev.map((i, c) => ({ ...i, active: c === n ? true : false })));
    setConfig(tabConfig?.TabSearchConfig?.[n]);
  };

  const handleNavigate = () => {
    const contextPath = window?.contextPath || "";
    history.push(`/${contextPath}/${userInfoType}/hearings/`);
  };
  const JoinCaseHome = Digit?.ComponentRegistryService?.getComponent("JoinCaseHome");

  const getRedirectUrl = (status, caseId, filingNumber) => {
    const contextPath = window?.contextPath;
    const userType = userInfoType;
    const baseUrl = `/${contextPath}/${userType}/dristi/home/view-case`;
    const params = `caseId=${caseId}&filingNumber=${filingNumber}`;

    switch (status) {
      case "UNDER_SCRUTINY":
        return userType === "employee" ? `/${contextPath}/${userType}/dristi/cases?${params}` : `${baseUrl}?${params}&tab=Complaint`;
      case "ADMISSION_HEARING_SCHEDULED":
        return `${baseUrl}?${params}&tab=Complaint`;
      case "PENDING_REGISTRATION":
        return userType === "employee" ? `/${contextPath}/${userType}/dristi/admission?${params}` : `${baseUrl}?${params}&tab=Complaint`;
      case "PENDING_E-SIGN":
        return `/${contextPath}/${userType}/dristi/home/file-case/sign-complaint?caseId=${caseId}`;
      case "PENDING_E-SIGN-2":
        return `/${contextPath}/${userType}/dristi/home/file-case/sign-complaint?caseId=${caseId}`;
      case "PENDING_RE_E-SIGN":
        return `/${contextPath}/${userType}/dristi/home/file-case/case?caseId=${caseId}&selected=addSignature`;
      default:
        return `${baseUrl}?${params}&tab=Overview`;
    }
  };

  const onRowClick = (row) => {
    const searchParams = new URLSearchParams();
    if (
      onRowClickData?.urlDependentOn && onRowClickData?.urlDependentValue && Array.isArray(onRowClickData?.urlDependentValue)
        ? onRowClickData?.urlDependentValue.includes(row.original[onRowClickData?.urlDependentOn])
        : row.original[onRowClickData?.urlDependentOn] === onRowClickData?.urlDependentValue
    ) {
      onRowClickData.params.forEach((item) => {
        searchParams.set(item?.key, row.original[item?.value]);
      });
      history.push(`/${window?.contextPath}/${userInfoType}${onRowClickData?.dependentUrl}?${searchParams.toString()}`);
    } else if (onRowClickData?.url) {
      onRowClickData.params.forEach((item) => {
        searchParams.set(item?.key, row.original[item?.value]);
      });
      history.push(`/${window?.contextPath}/${userInfoType}${onRowClickData?.url}?${searchParams.toString()}`);
    } else {
      const statusArray = [
        "PENDING_REGISTRATION",
        "CASE_ADMITTED",
        "ADMISSION_HEARING_SCHEDULED",
        "PENDING_PAYMENT",
        "UNDER_SCRUTINY",
        "PENDING_ADMISSION",
        "PENDING_E-SIGN",
        "PENDING_E-SIGN-2",
        "PENDING_RE_E-SIGN",
        "PENDING_ADMISSION_HEARING",
        "PENDING_NOTICE",
        "PENDING_RESPONSE",
        "UNDER_SCRUTINY",
      ];
      if (statusArray.includes(row?.original?.status)) {
        history.push(getRedirectUrl(row?.original?.status, row?.original?.id, row?.original?.filingNumber));
      }
    }
  };

  if (isLoading || isFetching || isSearchLoading || isFetchCaseLoading) {
    return <Loader />;
  }

  if (isUserLoggedIn && !individualId && userInfoType === "citizen") {
    history.push(`/${window?.contextPath}/${userInfoType}/dristi/landing-page`);
  }

  if (isNyayMitra) {
    history.push(`/${window?.contextPath}/employee`);
  }

  const data = [
    {
      logo: <InboxIcon />,
      title: t("REVIEW_SUMMON_NOTICE_WARRANTS_TEXT"),
      actionLink: "orders/Summons&Notice",
    },
    {
      logo: <DocumentIcon />,
      title: t("VIEW_ISSUED_ORDERS"),
      actionLink: "",
    },
  ];

  return (
    <div className="home-view-hearing-container">
      {individualId && userType && userInfoType === "citizen" && !caseDetails ? (
        <LitigantHomePage
          isApprovalPending={isApprovalPending}
          setShowSubmitResponseModal={setShowSubmitResponseModal}
          setResponsePendingTask={setResponsePendingTask}
        />
      ) : (
        <React.Fragment>
          <div className="left-side" style={{ width: individualId && userType && userInfoType === "citizen" && !caseDetails ? "100vw" : "70vw" }}>
            <div className="home-header-wrapper">
              <UpcomingHearings handleNavigate={handleNavigate} attendeeIndividualId={individualId} userInfoType={userInfoType} t={t} />
              {isJudge && (
                <div className="hearingCard" style={{ backgroundColor: "#ECF3FD" }}>
                  <Link to={`/${window.contextPath}/employee/home/dashboard`} style={{ color: "#007e7e", fontWeight: 700, textDecoration: "none" }}>
                    Open Dashboard
                  </Link>
                </div>
              )}
              {isCourtRoomRole && <ReviewCard data={data} userInfoType={userInfoType} />}
            </div>
            <div className="content-wrapper">
              <div className="header-class">
                <div className="header">{t("CS_YOUR_CASE")}</div>
                {individualId && userType && userInfoType === "citizen" && (
                  <div className="button-field" style={{ width: "50%" }}>
                    <React.Fragment>
                      <JoinCaseHome
                        refreshInbox={refreshInbox}
                        setShowSubmitResponseModal={setShowSubmitResponseModal}
                        setResponsePendingTask={setResponsePendingTask}
                      />
                      <Button
                        className={"tertiary-button-selector"}
                        label={t("FILE_A_CASE")}
                        labelClassName={"tertiary-label-selector"}
                        onButtonClick={() => {
                          history.push("/digit-ui/citizen/dristi/home/file-case");
                        }}
                      />
                    </React.Fragment>
                  </div>
                )}
              </div>
              <div className="inbox-search-wrapper pucar-home home-view">
                <InboxSearchComposer
                  key={`InboxSearchComposer-${callRefetch}`}
                  configs={{
                    ...config,
                    ...{
                      additionalDetails: {
                        ...config?.additionalDetails,
                        ...additionalDetails,
                      },
                    },
                  }}
                  defaultValues={defaultValues}
                  showTab={true}
                  tabData={tabData}
                  onTabChange={onTabChange}
                  additionalConfig={{
                    resultsTable: {
                      onClickRow: onRowClick,
                    },
                  }}
                />
              </div>
            </div>
          </div>
        </React.Fragment>
      )}
      <div className="right-side" style={{ width: individualId && userType && userInfoType === "citizen" && !caseDetails ? "0vw" : "30vw" }}>
        <TasksComponent
          taskType={taskType}
          setTaskType={setTaskType}
          caseType={caseType}
          setCaseType={setCaseType}
          isLitigant={Boolean(individualId && userType && userInfoType === "citizen")}
          uuid={userInfo?.uuid}
          userInfoType={userInfoType}
          joinCaseResponsePendingTask={responsePendingTask}
          joinCaseShowSubmitResponseModal={showSubmitResponseModal}
          setJoinCaseShowSubmitResponseModal={setShowSubmitResponseModal}
          hideTaskComponent={individualId && userType && userInfoType === "citizen" && !caseDetails}
        />
      </div>
    </div>
  );
};
export default HomeView;
