import React from "react";
import FormStep from "../../../components/FormStep";

const SelectName = ({ config, onSelect, t, isDisabled, value }) => {
  return <FormStep config={config} value={value} onSelect={onSelect} t={t} cardStyle={{ minWidth: "100%" }}></FormStep>;
};

export default SelectName;
