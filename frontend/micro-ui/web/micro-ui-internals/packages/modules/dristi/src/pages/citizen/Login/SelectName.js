import React from "react";
import FormStep from "../../../components/FormStep";

const SelectName = ({ config, onSelect, t, isDisabled }) => {
  return <FormStep config={config} onSelect={onSelect} t={t} isDisabled={isDisabled} cardStyle={{ minWidth: "100%" }}></FormStep>;
};

export default SelectName;
