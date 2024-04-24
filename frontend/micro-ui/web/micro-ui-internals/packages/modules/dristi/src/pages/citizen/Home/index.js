import React, { useMemo } from "react";
import CustomCard from "../../../components/CustomCard";
import {
  CaseInProgressIcon,
  ClosedCasesIcon,
  JoinCaseIcon,
  Loader,
  MyHearingsIcon,
  PendingActionsIcon,
} from "@egovernments/digit-ui-react-components";
import ApplicationAwaitingPage from "./ApplicationAwaitingPage";
import TakeUserToRegistration from "./TakeUserToRegistration";
import { userTypeOptions } from "../registration/config";

function CitizenHome({ tenantId }) {
  const Digit = window?.Digit || {};
  const token = window.localStorage.getItem("token");
  const isUserLoggedIn = Boolean(token);
  const moduleCode = "DRISTI";
  const userInfo = JSON.parse(window.localStorage.getItem("user-info"));
  const { data, isLoading } = Digit.Hooks.dristi.useGetIndividualUser(
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
    { Icon: <MyHearingsIcon />, label: "File a Case", path: "/digit-ui/employee/citizen/dristi/my-hearings" },
    { Icon: <CaseInProgressIcon />, label: "Case in Progress", path: "/digit-ui/employee/citizen/dristi/case-progress" },
    { Icon: <MyHearingsIcon />, label: "My hearing", path: "/digit-ui/employee/citzen/dristi/my-hearings" },
    { Icon: <JoinCaseIcon />, label: "Join a case", path: "/digit-ui/employee/citizen/dristi/join-case" },
    { Icon: <ClosedCasesIcon />, label: "Closed Cases", path: "/digit-ui/employee/citizen/dristi/closed-cases" },
    { Icon: <PendingActionsIcon />, label: "Pending Actions", path: "/digit-ui/employee/citizen/dristi/pending-actions" },
  ];

  const individualId = useMemo(() => data?.Individual?.[0]?.individualId, [data?.Individual]);
  const userType = useMemo(() => data?.Individual?.[0]?.additionalFields?.fields?.find((obj) => obj.key === "userType")?.value, [data?.Individual]);

  const { data: searchData, isLoading: isSearchLoading } = Digit.Hooks.dristi.useGetAdvocateClerk(
    {
      criteria: [{ individualId }],
      tenantId,
    },
    {},
    moduleCode,
    Boolean(isUserLoggedIn && individualId && userType !== "LITIGANT"),
    userType === "ADVOCATE" ? "/advocate/advocate/v1/_search" : "/advocate/clerk/v1/_search"
  );

  const userTypeDetail = useMemo(() => {
    return userTypeOptions.find((item) => item.code === userType) || {};
  }, [userType]);

  const isApprovalPending = useMemo(() => {
    const searchResult = searchData?.[userTypeDetail?.apiDetails?.requestKey];
    return (
      userType !== "LITIGANT" &&
      Array.isArray(searchResult) &&
      searchResult.length > 0 &&
      searchResult[0]?.isActive === false &&
      searchResult[0]?.status !== "INACTIVE"
    );
  }, [searchData, userType, userTypeDetail?.apiDetails?.requestKey]);

  if (isLoading || isSearchLoading) {
    return <Loader />;
  }

  return (
    <div style={{ display: "flex", flexWrap: "wrap", gap: "30px", cursor: "pointer", justifyContent: "space-evenly" }}>
      {individualId &&
        !isApprovalPending &&
        cardIcons.map((card) => {
          return (
            <CustomCard
              label={card.label}
              Icon={card.Icon}
              style={{ width: "400px", height: "150px" }}
              onClick={() => {
                // history.push(card.path);
              }}
            ></CustomCard>
          );
        })}
      {individualId && isApprovalPending && <ApplicationAwaitingPage individualId={individualId} />}
      {!individualId && <TakeUserToRegistration />}
    </div>
  );
}

export default CitizenHome;
