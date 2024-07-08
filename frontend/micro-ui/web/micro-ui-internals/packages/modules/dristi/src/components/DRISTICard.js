import { Card, Header, Loader } from "@egovernments/digit-ui-react-components";
import React from "react";
import CustomCard from "./CustomCard";
import { useHistory } from "react-router-dom/cjs/react-router-dom.min";
import JudgeScreen from "../pages/employee/Judge/JudgeScreen";

const DRISTICard = () => {
  const Digit = window?.Digit || {};
  const history = useHistory();
  const tenantId = Digit.ULBService.getCurrentTenantId();
  const roles = Digit.UserService.getUser()?.info?.roles;
  const isJudge = roles.some((role) => role.code === "CASE_APPROVER");
  const isNyayMitra = ["CASE_CREATOR", "CASE_EDITOR", "CASE_VIEWER", "ADVOCATE_APPROVER", "ADVOCATE_CLERK_APPROVER"].reduce((res, curr) => {
    if (!res) return res;
    res = roles.some((role) => role.code === curr);
    return res;
  }, true);
  const advocateClerkRequestBody = {
    inbox: {
      processSearchCriteria: {
        businessService: ["advocateclerk"],
        moduleName: "Advocate Clerk Service",
        tenantId: "pg",
      },
      moduleSearchCriteria: {
        tenantId: "pg",
      },
      tenantId: "pg",
      limit: 10,
      offset: 0,
    },
  };

  const advocateRequestBody = {
    inbox: {
      processSearchCriteria: {
        businessService: ["advocate"],
        moduleName: "Advocate services",
        tenantId: "pg",
      },
      moduleSearchCriteria: {
        tenantId: "pg",
      },
      tenantId: "pg",
      limit: 10,
      offset: 0,
    },
  };

  const { data: clerkData, isLoading: isLoadingClerk } = Digit.Hooks.dristi.useGetAdvocateClientServices(
    "/inbox/v2/_search",
    advocateClerkRequestBody,
    tenantId,
    "clerk",
    {},
    true,
    true
  );
  const { data: advocateData, isLoading: isLoadingAdvocate } = Digit.Hooks.dristi.useGetAdvocateClientServices(
    "/inbox/v2/_search",
    advocateRequestBody,
    tenantId,
    "advocate",
    {},
    true,
    true
  );

  if (isLoadingAdvocate || isLoadingClerk) {
    return <Loader />;
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
                    // showNumber={String(clerkData?.clerks?.length + advocateData?.advocates?.length) || 35}
                    onClick={() => {
                      history.push("/digit-ui/employee/dristi/registration-requests");
                    }}
                  />
                  <CustomCard
                    label={"View Cases"}
                    subLabel={"Explore cases and support on-going case queries"}
                    buttonLabel={"View Cases"}
                    className="custom-card-style"
                    // showNumber={749}
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
