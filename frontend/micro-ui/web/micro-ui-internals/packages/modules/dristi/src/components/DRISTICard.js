import { RegistrationRequestIcon, CasesIcon } from "@egovernments/digit-ui-react-components";
import React from "react";
import CustomCard from "./CustomCard";
import { useHistory } from "react-router-dom/cjs/react-router-dom.min";

const DRISTICard = () => {
  const history = useHistory();
  return (
    <div style={{ display: "flex", flexWrap: "wrap", gap: "30px", cursor: "pointer" }}>
      <CustomCard
        label={"Registration Requests"}
        Icon={<RegistrationRequestIcon />}
        style={{ width: "400px", height: "150px" }}
        showNumber={35}
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
