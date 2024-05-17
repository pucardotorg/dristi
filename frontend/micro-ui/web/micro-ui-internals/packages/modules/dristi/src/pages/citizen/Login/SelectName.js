import React from "react";
import FormStep from "../../../components/FormStep";
import { useHistory } from "react-router-dom/cjs/react-router-dom.min";

const SelectName = ({ config, onSelect, t, isDisabled, value, path, params }) => {
  const history = useHistory();
  const token = window.localStorage.getItem("token");
  const isUserLoggedIn = Boolean(token);
  if (isUserLoggedIn) {
    history.push(`/${window.contextPath}/citizen/dristi/home`);
  }
  if (!isUserLoggedIn && !params?.mobileNumber) {
    history.push(path);
  }
  return <FormStep config={config} value={value} onSelect={onSelect} t={t} cardStyle={{ minWidth: "100%" }}></FormStep>;
};

export default SelectName;
