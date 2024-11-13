import React, { useEffect, useMemo, useState } from "react";
import CustomCard from "../../../components/CustomCard";
import { Loader } from "@egovernments/digit-ui-react-components";
import ApplicationAwaitingPage from "./ApplicationAwaitingPage";
import TakeUserToRegistration from "./TakeUserToRegistration";
import { userTypeOptions } from "../registration/config";
import { useHistory } from "react-router-dom/cjs/react-router-dom.min";
import { CaseInProgressIcon, ClosedCasesIcon, FileCaseIcon, JoinCaseIcon, MyHearingsIcon, PendingActionsIcon } from "../../../icons/svgIndex";
import Home from "./litigantHome";
import { useGetAccessToken } from "../../../hooks/useGetAccessToken";

function CitizenHome({ tenantId, setHideBack }) {
  const Digit = window?.Digit || {};
  const token = window.localStorage.getItem("token");
  const isUserLoggedIn = Boolean(token);
  const history = useHistory();
  const moduleCode = "DRISTI";
  const userInfo = JSON.parse(window.localStorage.getItem("user-info"));
  const [isFetching, setIsFetching] = useState(true);
  const [isFetchingAdvoacte, setIsFetchingAdvocate] = useState(true);

  const { data, isLoading, refetch } = Digit.Hooks.dristi.useGetIndividualUser(
    {
      Individual: {
        userUuid: [userInfo?.uuid],
      },
    },
    { tenantId, limit: 1000, offset: 0 },
    moduleCode,
    "HOME",
    userInfo?.uuid && isUserLoggedIn
  );

  const cardIcons = [
    { Icon: <FileCaseIcon />, label: "File a Case", path: "/digit-ui/citizen/dristi/home/file-case" },
    { Icon: <CaseInProgressIcon />, label: "Case in Progress", path: "/digit-ui/employee/citizen/dristi/case-progress" },
    { Icon: <MyHearingsIcon />, label: "My hearing", path: "/digit-ui/employee/citzen/dristi/my-hearings" },
    { Icon: <JoinCaseIcon />, label: "Join a case", path: "/digit-ui/employee/citizen/dristi/join-case" },
    { Icon: <ClosedCasesIcon />, label: "Closed Cases", path: "/digit-ui/employee/citizen/dristi/closed-cases" },
    { Icon: <PendingActionsIcon />, label: "Pending Actions", path: "/digit-ui/employee/citizen/dristi/pending-actions" },
  ];

  const individualId = useMemo(() => data?.Individual?.[0]?.individualId, [data?.Individual]);
  const userType = useMemo(() => data?.Individual?.[0]?.additionalFields?.fields?.find((obj) => obj.key === "userType")?.value, [data?.Individual]);
  const { data: searchData, isLoading: isSearchLoading, refetch: refetchAdvocateClerk } = Digit.Hooks.dristi.useGetAdvocateClerk(
    {
      criteria: [{ individualId }],
      tenantId,
    },
    { tenantId },
    moduleCode,
    Boolean(isUserLoggedIn && individualId && userType !== "LITIGANT"),
    userType === "ADVOCATE" ? "/advocate/v1/_search" : "/advocate/clerk/v1/_search"
  );
  useEffect(() => {
    refetch().then(() => {
      refetchAdvocateClerk().then(() => {
        setIsFetchingAdvocate(false);
      });
      setIsFetching(false);
    });
  }, []);

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
  const isRejected = useMemo(() => {
    return (
      userType !== "LITIGANT" &&
      Array.isArray(searchResult) &&
      searchResult?.length > 0 &&
      searchResult?.[0]?.isActive === false &&
      searchResult?.[0]?.status === "INACTIVE"
    );
  }, [searchResult, userType]);

  const userHasIncompleteRegistration = !individualId || isRejected;
  const registrationIsDoneApprovalIsPending = individualId && isApprovalPending && !isRejected;
  useEffect(() => {
    setHideBack(userHasIncompleteRegistration || registrationIsDoneApprovalIsPending);
    return () => {
      setHideBack(false);
    };
  }, [userHasIncompleteRegistration, registrationIsDoneApprovalIsPending, setHideBack]);

  useEffect(() => {
    setHideBack(userHasIncompleteRegistration || registrationIsDoneApprovalIsPending);
    return () => {
      setHideBack(false);
    };
  }, [userHasIncompleteRegistration, registrationIsDoneApprovalIsPending, setHideBack]);

  useGetAccessToken("citizen.refresh-token", individualId && !isApprovalPending && !isRejected);

  if (isLoading || isSearchLoading || isFetching || isFetchingAdvoacte) {
    return <Loader />;
  }

  return (
    <div
      style={{
        display: "flex",
        flexWrap: "wrap",
        gap: "30px",
        cursor: "pointer",
        justifyContent: "space-evenly",
        width: "100%",
      }}
    >
      {individualId && !isApprovalPending && !isRejected && (
        // cardIcons.map((card, index) => {
        //   return (
        //     <CustomCard
        //       key={index}
        //       label={card.label}
        //       Icon={card.Icon}
        //       style={{ width: "400px", height: "150px" }}
        //       onClick={() => {
        //         if (card.label === "File a Case") {
        //           history.push(card.path);
        //         }
        //       }}
        //     ></CustomCard>
        //   );
        // })}
        <Home />
      )}
      {registrationIsDoneApprovalIsPending && <ApplicationAwaitingPage individualId={individualId} />}
      {userHasIncompleteRegistration && (
        <TakeUserToRegistration
          message={isRejected ? "CS_REJECT_MESSAGE" : "CS_REGISTRATION_MESSAGE"}
          isRejected={isRejected}
          data={data}
          userType={searchResult?.[0]?.additionalDetails?.userType}
        />
      )}
    </div>
  );
}

export default CitizenHome;
