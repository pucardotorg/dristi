import { Card, CardHeader, Header, Loader } from "@egovernments/digit-ui-react-components";
import React from "react";
import CustomCard from "./CustomCard";
import { useHistory } from "react-router-dom/cjs/react-router-dom.min";
import { CasesIcon, RegistrationRequestIcon } from "../icons/svgIndex";

const DRISTICard = () => {
  const Digit = window?.Digit || {};
  const history = useHistory();
  const tenantId = Digit.ULBService.getCurrentTenantId();
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

  return (
    <div style={{ alignContent: "center", width: "100%" }}>
      <Card className="main-card-home">
        <Header className="main-card-header">{"What do you wish to do?"}</Header>
        <div className="main-inner-div">
          <CustomCard
            label={"View Registrations"}
            subLabel={"Review new platform registration requests from Advocates"}
            buttonLabel={"View Requests"}
            Icon={<RegistrationRequestIcon />}
            className="custom-card-style"
            showNumber={String(clerkData?.clerks?.length + advocateData?.advocates?.length) || 35}
            onClick={() => {
              history.push("/digit-ui/employee/dristi/registration-requests");
            }}
          ></CustomCard>
          <CustomCard
            label={"View Cases"}
            subLabel={"Explore cases and support on-going case queries"}
            buttonLabel={"View Cases"}
            Icon={<CasesIcon />}
            className="custom-card-style"
            showNumber={749}
            onClick={() => {
              // history.push("/digit-ui/employee/dristi/total-cases");
            }}
          ></CustomCard>
        </div>
      </Card>
    </div>
  );
};

export default DRISTICard;
