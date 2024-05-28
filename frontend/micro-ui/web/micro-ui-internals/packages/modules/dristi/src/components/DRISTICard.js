import { Loader } from "@egovernments/digit-ui-react-components";
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
    <div style={{ display: "flex", flexWrap: "wrap", gap: "30px", cursor: "pointer" }}>
      <CustomCard
        label={"Registration Requests"}
        Icon={<RegistrationRequestIcon />}
        style={{ width: "400px", height: "150px" }}
        showNumber={String(clerkData?.totalCount + advocateData?.totalCount) || 0}
        onClick={() => {
          history.push("/digit-ui/employee/dristi/registration-requests");
        }}
      ></CustomCard>
      <CustomCard
        label={"Cases"}
        Icon={<CasesIcon />}
        style={{ width: "400px", height: "150px", cursor: "pointer" }}
        showNumber={749}
        onClick={() => {
          // history.push("/digit-ui/employee/dristi/total-cases");
        }}
      ></CustomCard>
    </div>
  );
};

export default DRISTICard;
