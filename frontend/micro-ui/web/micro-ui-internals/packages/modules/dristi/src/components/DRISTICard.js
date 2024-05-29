import { Loader } from "@egovernments/digit-ui-react-components";
import React from "react";
import CustomCard from "./CustomCard";
import { useHistory } from "react-router-dom/cjs/react-router-dom.min";
import { CasesIcon, RegistrationRequestIcon } from "../icons/svgIndex";

const DRISTICard = () => {
  const Digit = window?.Digit || {};
  const history = useHistory();
  const tenantId = Digit.ULBService.getCurrentTenantId();
  const requestBody = {
    applicationNumber: "",
    tenantId: "pg",
    status: ["INWORKFLOW"],
  };
  const { data: clerkData, isLoading: isLoadingClerk } = Digit.Hooks.dristi.useGetAdvocateClientServices(
    "/advocate/clerk/v1/_search",
    requestBody,
    tenantId,
    "clerk",
    {},
    true,
    true
  );
  const { data: advocateData, isLoading: isLoadingAdvocate } = Digit.Hooks.dristi.useGetAdvocateClientServices(
    "/advocate/advocate/v1/_search",
    requestBody,
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
        showNumber={String(clerkData?.clerks?.length + advocateData?.advocates?.length) || 35}
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
          history.push("/digit-ui/employee/dristi/cases");
        }}
      ></CustomCard>
    </div>
  );
};

export default DRISTICard;
