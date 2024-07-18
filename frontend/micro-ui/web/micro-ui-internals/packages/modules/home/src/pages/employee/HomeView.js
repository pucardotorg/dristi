import { useTranslation } from "react-i18next";
import React, { useState, useEffect, useMemo, useCallback } from "react";
import { useHistory } from "react-router-dom";
import { Button, InboxSearchComposer } from "@egovernments/digit-ui-react-components";
import { TabLitigantSearchConfig, rolesToConfigMapping, userTypeOptions } from "../../configs/HomeConfig";
import UpcomingHearings from "../../components/UpComingHearing";
import { Loader } from "@egovernments/digit-ui-react-components";
import TasksComponent from "../../components/TaskComponent";
import { useLocation } from "react-router-dom/cjs/react-router-dom.min";
import { HomeService } from "../../hooks/services";
import LitigantHomePage from "./LitigantHomePage";

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
  const [callRefetch, SetCallRefetch] = useState(false);
  const [tabConfig, setTabConfig] = useState(TabLitigantSearchConfig);
  const [onRowClickData, setOnRowClickData] = useState({ url: "", params: [] });
  const [taskType, setTaskType] = useState({ code: "case", name: "Case" });
  const roles = useMemo(() => Digit.UserService.getUser()?.info?.roles, [Digit.UserService]);
  const tenantId = useMemo(() => window?.Digit.ULBService.getCurrentTenantId(), []);
  const userInfo = JSON.parse(window.localStorage.getItem("user-info"));
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
    {},
    individualId,
    userType,
    "/advocate/advocate/v1/_search"
  );

  const userTypeDetail = useMemo(() => {
    return userTypeOptions.find((item) => item.code === userType) || {};
  }, [userType]);

  const searchResult = useMemo(() => {
    return searchData?.[`${userTypeDetail?.apiDetails?.requestKey}s`];
  }, [searchData, userTypeDetail?.apiDetails?.requestKey]);

  const advocateId = useMemo(() => {
    return searchResult?.[0]?.responseList?.[0]?.id;
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
                pagination: { offSet: 0, limit: 1 },
              },
            ],
          });
          const totalCount = response?.criteria?.[0]?.pagination?.totalCount;
          return {
            key: index,
            label: totalCount ? `${configItem.label} (${totalCount})` : `${configItem.label} (0)`,
            active: index === 0 ? true : false,
          };
        }) || []
      );
      setTabData(updatedTabData);
    },
    [additionalDetails, tenantId]
  );

  useEffect(() => {
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
  }, [additionalDetails, getTotalCountForTab, roles, state, tenantId]);

  // calling case api for tab's count
  useEffect(() => {
    (async function () {
      if (userType) {
        setIsFetchCaseLoading(true);
        const caseData = await HomeService.customApiService("/case/case/v1/_search", {
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
    history.push(`/${contextPath}/${userInfoType}/hearings/view-hearing`);
  };
  const JoinCaseHome = Digit?.ComponentRegistryService?.getComponent("JoinCaseHome");

  const refreshInbox = () => {
    SetCallRefetch(true);
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
      const statusArray = ["CASE_ADMITTED", "ADMISSION_HEARING_SCHEDULED", "PAYMENT_PENDING", "UNDER_SCRUTINY", "PENDING_ADMISSION"];
      if (statusArray.includes(row?.original?.status)) {
        if (row?.original?.status === "CASE_ADMITTED") {
          history.push(
            `/${window?.contextPath}/${userInfoType}/dristi/home/view-case?caseId=${row?.original?.id}&filingNumber=${row?.original?.filingNumber}&tab=Overview`
          );
        } else if (row?.original?.status === "ADMISSION_HEARING_SCHEDULED") {
          history.push(
            `/${window?.contextPath}/${userInfoType}/dristi/admission?caseId=${row?.original?.id}&filingNumber=${row?.original?.filingNumber}`
          );
        } else {
          history.push(
            `/${window?.contextPath}/${userInfoType}/dristi/home/view-case?caseId=${row?.original?.id}&filingNumber=${row?.original?.filingNumber}&tab=Complaints`
          );
        }
      }
    }
  };

  if (isLoading || isFetching || isSearchLoading || isFetchCaseLoading) {
    return <Loader />;
  }

  if (isUserLoggedIn && !individualId && userInfoType === "citizen") {
    history.push(`/${window?.contextPath}/${userInfoType}/dristi/landing-page`);
  }

  return (
    <div className="home-view-hearing-container">
      {individualId && userType && userInfoType === "citizen" && !caseDetails ? (
        <LitigantHomePage />
      ) : (
        <React.Fragment>
          <div className="left-side">
            <UpcomingHearings handleNavigate={handleNavigate} />
            <div className="content-wrapper">
              <div className="header-class">
                <div className="header">{t("CS_YOUR_CASE")}</div>
                {individualId && userType && userInfoType === "citizen" && (
                  <div className="button-field" style={{ width: "50%" }}>
                    <React.Fragment>
                      <JoinCaseHome refreshInbox={refreshInbox} t={t} />
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
          <div className="right-side">
            <TasksComponent
              taskType={taskType}
              setTaskType={setTaskType}
              isLitigant={Boolean(individualId && userType && userInfoType === "citizen")}
              uuid={userInfo?.uuid}
              userInfoType={userInfoType}
            />
          </div>
        </React.Fragment>
      )}
    </div>
  );
};
export default HomeView;
