import { Loader } from "@egovernments/digit-ui-react-components";
import React from "react";
import { useRouteMatch } from "react-router-dom";
import { default as EmployeeApp } from "./pages/employee";
import CasesCard from "./components/CasesCard";
import { overrideHooks, updateCustomConfigs } from "./utils";
import JoinCaseHome from "./pages/employee/JoinCaseHome";

export const CasesModule = ({ stateCode, userType, tenants }) => {
  const { path, url } = useRouteMatch();
  console.log(path);
  const tenantId = Digit.ULBService.getCurrentTenantId();
  const moduleCode = ["case", "common", "workflow"];
  const language = Digit.StoreData.getCurrentLanguage();
  const { isLoading, data: store } = Digit.Services.useStore({
    stateCode,
    moduleCode,
    language,
  });

  if (isLoading) {
    return <Loader />;
  }
  return <EmployeeApp path={path} stateCode={stateCode} userType={userType} tenants={tenants} />;
};

const componentsToRegister = {
  CasesModule,
  CasesCard,
  JoinCaseHome,
};

export const initCasesComponents = () => {
  overrideHooks();
  updateCustomConfigs();
  Object.entries(componentsToRegister).forEach(([key, value]) => {
    Digit.ComponentRegistryService.setComponent(key, value);
  });
};
