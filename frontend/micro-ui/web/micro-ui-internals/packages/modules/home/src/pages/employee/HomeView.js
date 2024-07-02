import { useTranslation } from "react-i18next";
import React, { useState, useEffect, useMemo } from "react";
import { useHistory } from "react-router-dom";
import { Button, Header, InboxSearchComposer } from "@egovernments/digit-ui-react-components";
import { TabLitigantSearchConfig, TabSearchconfig, userTypeOptions } from "../../configs/HearingsHomeConfig";
import UpcomingHearings from "../../components/UpComingHearing";
import { Loader } from "@egovernments/digit-ui-react-components";
import TasksComponent from "../../components/TaskComponent";

const fieldStyle = { marginRight: 0 };
const defaultSearchValues = {
  individualName: "",
  mobileNumber: "",
  IndividualID: "",
};

const HomeView = () => {
  const history = useHistory();
  const { t } = useTranslation();
  const token = window.localStorage.getItem("token");
  const isUserLoggedIn = Boolean(token);
  const [defaultValues, setDefaultValues] = useState(defaultSearchValues);
  const [config, setConfig] = useState(TabLitigantSearchConfig?.TabSearchConfig?.[0]);
  const [tabData, setTabData] = useState(
    TabLitigantSearchConfig?.TabSearchConfig?.map((configItem, index) => ({
      key: index,
      label: configItem.label,
      active: index === 0 ? true : false,
    }))
  );
  const [taskType, setTaskType] = useState({ code: "case", name: "Case" });

  const tenantId = window?.Digit.ULBService.getCurrentTenantId();
  const userInfo = JSON.parse(window.localStorage.getItem("user-info"));
  const userInfoType = useMemo(() => (userInfo.type === "CITIZEN" ? "citizen" : "employee"), [userInfo.type]);
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
  const individualId = individualData?.Individual?.[0]?.individualId;

  const userType = useMemo(() => individualData?.Individual?.[0]?.additionalFields?.fields?.find((obj) => obj.key === "userType")?.value, [
    individualData?.Individual,
  ]);

  const { data: searchData, isLoading: isSearchLoading } = window?.Digit.Hooks.dristi.useGetAdvocateClerk(
    {
      criteria: [{ individualId }],
      tenantId,
    },
    {},
    individualId,
    userType,
    "/advocate/advocate/v1/_search"
  );

  if (userType === "ADVOCATE" && searchData) {
    const advocateBarRegNumber = searchData?.advocates?.[0]?.responseList?.[0]?.barRegistrationNumber;
    if (advocateBarRegNumber) {
      window?.Digit.SessionStorage.set("isAdvocateAndApproved", true);
    } else {
      window?.Digit.SessionStorage.set("isAdvocateAndApproved", false);
    }
  }

  const userTypeDetail = useMemo(() => {
    return userTypeOptions.find((item) => item.code === userType) || {};
  }, [userType]);

  const searchResult = useMemo(() => {
    return searchData?.[`${userTypeDetail?.apiDetails?.requestKey}s`];
  }, [searchData, userTypeDetail?.apiDetails?.requestKey]);

  const advocateId = useMemo(() => {
    return searchResult?.[0]?.responseList?.[0]?.id;
  }, [searchResult]);

  useEffect(() => {
    setDefaultValues(defaultSearchValues);
  }, []);

  const additionalDetails = useMemo(() => {
    return {
      ...(advocateId
        ? {
            searchKey: "filingNumber",
            defaultFields: true,
            advocateId: advocateId,
          }
        : {
            searchKey: "filingNumber",
            defaultFields: true,
            litigantId: individualId,
          }),
    };
  }, [advocateId, individualId]);

  const onTabChange = (n) => {
    setTabData((prev) => prev.map((i, c) => ({ ...i, active: c === n ? true : false })));
    setConfig(TabLitigantSearchConfig?.TabSearchConfig?.[n]);
  };

  const handleNavigate = () => {
    const contextPath = window?.contextPath || "";
    history.push(`/${contextPath}/${userInfoType}/hearings/view-hearing`);
  };
  const JoinCaseHome = Digit?.ComponentRegistryService?.getComponent("JoinCaseHome");

  if (isLoading || isFetching || isSearchLoading) {
    return <Loader />;
  }
  return (
    <div className="home-view-hearing-container">
      <div className="left-side">
        <UpcomingHearings handleNavigate={handleNavigate} />
        <div className="content-wrapper">
          <div className="header-class">
            <div className="header">{t("CS_YOUR_CASE")}</div>
            <div className="button-field" style={{ width: "50%" }}>
              <JoinCaseHome t={t} />
              <Button
                className={"tertiary-button-selector"}
                label={t("FILE_A_CASE")}
                labelClassName={"tertiary-label-selector"}
                onButtonClick={() => {
                  history.push("/digit-ui/citizen/dristi/home/file-case");
                }}
              />
            </div>
          </div>
          <div className="inbox-search-wrapper pucar-hearing-home">
            <InboxSearchComposer
              configs={{ ...config, ...additionalDetails }}
              defaultValues={defaultValues}
              showTab={true}
              tabData={tabData}
              onTabChange={onTabChange}
              additionalConfig={{
                resultsTable: {
                  onClickRow: (props) => {
                    history.push(`/${window?.contextPath}/citizen/dristi/home/file-case/case?caseId=${props?.original?.id}`);
                  },
                },
              }}
            />
          </div>
        </div>
      </div>
      <div className="right-side">
        <TasksComponent taskType={taskType} setTaskType={setTaskType} />
      </div>
    </div>
  );
};
export default HomeView;
