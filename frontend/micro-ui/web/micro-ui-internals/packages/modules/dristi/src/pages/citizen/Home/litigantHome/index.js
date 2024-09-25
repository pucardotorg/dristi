import { Button, InboxSearchComposer, Loader } from "@egovernments/digit-ui-react-components";
import React, { useMemo, useState } from "react";
import { useTranslation } from "react-i18next";
import { useHistory } from "react-router-dom";
import { useRouteMatch } from "react-router-dom/cjs/react-router-dom.min";
import JoinCaseHome from "../../../../../../cases/src/pages/employee/JoinCaseHome";
import { userTypeOptions } from "../../registration/config";
import { litigantInboxConfig } from "./litigantInboxConfig";

const sectionsParentStyle = {
  height: "50%",
  display: "flex",
  flexDirection: "column",
  gridTemplateColumns: "20% 1fr",
  gap: "1rem",
};

function Home() {
  const { t } = useTranslation();
  const history = useHistory();
  const { path } = useRouteMatch();
  const token = window.localStorage.getItem("token");
  const isUserLoggedIn = Boolean(token);
  const moduleCode = "DRISTI";
  const tenantId = window?.Digit.ULBService.getCurrentTenantId();
  const userInfo = JSON.parse(window.localStorage.getItem("user-info"));
  const { data: individualData, isLoading, isFetching } = window?.Digit.Hooks.dristi.useGetIndividualUser(
    {
      Individual: {
        userUuid: [userInfo?.uuid],
      },
    },
    { tenantId, limit: 1000, offset: 0 },
    moduleCode,
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

  const [callRefetch, SetCallRefetch] = useState(false);
  const [caseDetails, setCaseDetails] = useState(null);
  const [selectedParty, setSelectedParty] = useState({});

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

  if (isLoading || isFetching || isSearchLoading) {
    return <Loader />;
  } else {
    history.push(`/${window?.contextPath}/citizen/home/home-pending-task`);
  }

  const refreshInbox = () => {
    SetCallRefetch(true);
  };

  return (
    <React.Fragment>
      <div className="home-screen-wrapper" style={{ minHeight: "calc(100vh - 90px)", width: "100%", padding: "30px" }}>
        <div className="header-class">
          <div className="header">{t("CS_YOUR_CASE")}</div>
          <div className="button-field" style={{ width: "50%" }}>
            <JoinCaseHome refreshInbox={refreshInbox} updateCase={setCaseDetails} updateSelectedParty={setSelectedParty} />
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
        <div className="inbox-search-wrapper">
          <InboxSearchComposer
            key={`InboxSearchComposer-${callRefetch}`}
            customStyle={sectionsParentStyle}
            configs={{
              ...litigantInboxConfig,
              additionalDetails: {
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
              },
            }}
            additionalConfig={{
              resultsTable: {
                onClickRow: (props) => {
                  const statusArray = ["CASE_ADMITTED", "ADMISSION_HEARING_SCHEDULED", "PENDING_PAYMENT", "UNDER_SCRUTINY", "PENDING_ADMISSION"];
                  statusArray.includes(props?.original?.status)
                    ? history.push(`${path}/view-case?caseId=${props.original.id}&filingNumber=${props.original.filingNumber}&tab=Overview`)
                    : history.push(`${path}/file-case/case?caseId=${props?.original?.id}`);
                },
              },
            }}
          ></InboxSearchComposer>
        </div>
      </div>
    </React.Fragment>
  );
}

export default Home;
