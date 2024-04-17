import React from "react";
import CustomCard from "../../../components/CustomCard";
import { CaseInProgressIcon, ClosedCasesIcon, JoinCaseIcon, MyHearingsIcon, PendingActionsIcon } from "@egovernments/digit-ui-react-components";
import { useHistory } from "react-router-dom/cjs/react-router-dom.min";
import ApplicationAwaitingPage from "./ApplicationAwaitingPage";
import TakeUserToRegistration from "./TakeUserToRegistration";

function CitizenHome() {
  const history = useHistory();
  const isApplicationAwaiting = false;
  const isUserNotRegistered = false;
  const cardIcons = [
    { Icon: <MyHearingsIcon />, label: "File a Case", path: "/digit-ui/employee/citizen/dristi/my-hearings" },
    { Icon: <CaseInProgressIcon />, label: "Case in Progress", path: "/digit-ui/employee/citizen/dristi/case-progress" },
    { Icon: <MyHearingsIcon />, label: "My hearing", path: "/digit-ui/employee/citzen/dristi/my-hearings" },
    { Icon: <JoinCaseIcon />, label: "Join a case", path: "/digit-ui/employee/citizen/dristi/join-case" },
    { Icon: <ClosedCasesIcon />, label: "Closed Cases", path: "/digit-ui/employee/citizen/dristi/closed-cases" },
    { Icon: <PendingActionsIcon />, label: "Pending Actions", path: "/digit-ui/employee/citizen/dristi/pending-actions" },
  ];

  return (
    <div style={{ display: "flex", flexWrap: "wrap", gap: "30px", cursor: "pointer", justifyContent: "space-evenly" }}>
      {!isApplicationAwaiting && !isUserNotRegistered && cardIcons.map((card) => {
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
      {isApplicationAwaiting && !isUserNotRegistered && <ApplicationAwaitingPage /> }
      {!isApplicationAwaiting && isUserNotRegistered && <TakeUserToRegistration /> }

    </div>
  );
}

export default CitizenHome;
