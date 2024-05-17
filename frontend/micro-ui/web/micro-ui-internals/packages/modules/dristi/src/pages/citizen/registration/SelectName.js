import React from "react";
import FormStep from "../../../components/FormStep";

const SelectName = ({ config, onSelect, t, isDisabled, params, history }) => {
  if (!params?.mobileNumber) {
    history.push("/digit-ui/citizen/dristi/home/login");
  }
  return (
    <div style={{ minWidth: "100%", display: "flex", justifyContent: "center" }}>
      <FormStep config={config} onSelect={(props) => onSelect(props)} t={t} isDisabled={isDisabled} cardStyle={{ minWidth: "100%" }}></FormStep>
    </div>
  );
};

export default SelectName;
