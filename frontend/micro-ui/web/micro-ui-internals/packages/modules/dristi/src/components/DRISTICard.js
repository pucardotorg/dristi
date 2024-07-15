import { Card, Header } from "@egovernments/digit-ui-react-components";
import React, { useMemo } from "react";
import CustomCard from "./CustomCard";
import { useHistory } from "react-router-dom/cjs/react-router-dom.min";
import JudgeScreen from "../pages/employee/Judge/JudgeScreen";

const DRISTICard = () => {
  const Digit = window?.Digit || {};
  const history = useHistory();
  const roles = Digit.UserService.getUser()?.info?.roles;
  const isJudge = useMemo(() => roles.some((role) => role.code === "CASE_APPROVER"), [roles]);
  const isScrutiny = useMemo(() => roles.some((role) => role.code === "CASE_REVIEWER"), [roles]);
  const isCourtOfficer = useMemo(() => roles.some((role) => role.code === "HEARING_CREATOR"), [roles]);
  const isNyayMitra = ["CASE_CREATOR", "CASE_EDITOR", "CASE_VIEWER", "ADVOCATE_APPROVER", "ADVOCATE_CLERK_APPROVER"].reduce((res, curr) => {
    if (!res) return res;
    res = roles.some((role) => role.code === curr);
    return res;
  }, true);

  if (isScrutiny || isJudge || isCourtOfficer) {
    history.push("/digit-ui/employee/home/home-pending-task");
  }

  let roleType = isJudge ? "isJudge" : "default";
  return (
    <React.Fragment>
      {(() => {
        switch (roleType) {
          case "isJudge":
            return (
              <div className={"file-case-main"}>
                <JudgeScreen path={`/digit-ui/employee/dristi`} />
              </div>
            );
          default:
            return (
              <Card className="main-card-home">
                <Header className="main-card-header">{"What do you wish to do?"}</Header>
                <div className="main-inner-div">
                  <CustomCard
                    label={"View Registrations"}
                    subLabel={"Review new platform registration requests from Advocates"}
                    buttonLabel={"View Requests"}
                    className="custom-card-style"
                    onClick={() => {
                      history.push("/digit-ui/employee/dristi/registration-requests");
                    }}
                  />
                  <CustomCard
                    label={"View Cases"}
                    subLabel={"Explore cases and support on-going case queries"}
                    buttonLabel={"View Cases"}
                    className="custom-card-style"
                    onClick={() => {
                      isNyayMitra ? history.push("/digit-ui/employee/dristi/pending-payment-inbox") : history.push("/digit-ui/employee/dristi/cases");
                    }}
                  />
                </div>
              </Card>
            );
        }
      })()}
    </React.Fragment>
  );
};

export default DRISTICard;
