import { CitizenHomeCard, PTIcon, Loader } from "@egovernments/digit-ui-react-components";
import React, { useEffect } from "react";
import { useTranslation } from "react-i18next";
import { useRouteMatch } from "react-router-dom";
import CitizenApp from "./pages/citizen";
import SelectComponents from "./components/SelectComponents";
import SelectUserTypeComponent from "./components/SelectUserTypeComponent";
import Registration from "./pages/citizen/registration";
import EmployeeApp from "./pages/employee";
import DRISTICard from "./components/DRISTICard";
import Inbox from "./pages/employee/Inbox";
import Login from "./pages/citizen/Login";

export const DRISTIModule = ({ stateCode, userType, tenants }) => {
  const { path, url } = useRouteMatch();

  const moduleCode = "DRISTI";
  const language = Digit.StoreData.getCurrentLanguage();
  const { isLoading, data: store } = Digit.Services.useStore({ stateCode, moduleCode, language });
  if (isLoading) {
    return <Loader />;
  }
  Digit.SessionStorage.set("DRISTI_TENANTS", tenants);

  if (userType === "citizen") {
    return <CitizenApp path={path} stateCode={stateCode} userType={userType} tenants={tenants} />;
  }
  return <EmployeeApp path={path} stateCode={stateCode} userType={userType} tenants={tenants}></EmployeeApp>;
};

const componentsToRegister = {
  SelectComponents,
  SelectUserTypeComponent,
  DRISTIModule,
  DRISTIRegistration: Registration,
  DRISTICard,
  Inbox,
  DRISTILogin: Login,
};

export const initDRISTIComponents = () => {
  Object.entries(componentsToRegister).forEach(([key, value]) => {
    Digit.ComponentRegistryService.setComponent(key, value);
  });
};
