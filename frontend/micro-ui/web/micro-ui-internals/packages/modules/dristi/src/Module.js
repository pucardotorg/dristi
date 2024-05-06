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
import CitizenResponse from "./pages/citizen/registration/Response";
import AdvocateClerkAdditionalDetail from "./pages/citizen/registration/AdvocateClerkAdditionalDetail";
import FileCase from "./pages/citizen/FileCase";
import VerificationComponent from "./components/VerificationComponent";

export const DRISTIModule = ({ stateCode, userType, tenants }) => {
  const { path, url } = useRouteMatch();
  const Digit = window?.Digit || {};
  const moduleCode = "DRISTI";
  const tenantID = tenants?.[0]?.code?.split(".")?.[0];
  const language = Digit.StoreData.getCurrentLanguage();
  const { isLoading, data: store } = Digit.Services.useStore({ stateCode, moduleCode, language });
  if (isLoading) {
    return <Loader />;
  }
  Digit.SessionStorage.set("DRISTI_TENANTS", tenants);

  if (userType === "citizen") {
    return <CitizenApp path={path} stateCode={stateCode} userType={userType} tenants={tenants} tenantId={tenantID} />;
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
  DRISTICitizenResponse: CitizenResponse,
  AdvocateClerkAdditionalDetail,
  FileCase,
  VerificationComponent,
};

export const initDRISTIComponents = () => {
  const Digit = window?.Digit || {};
  Object.entries(componentsToRegister).forEach(([key, value]) => {
    Digit.ComponentRegistryService.setComponent(key, value);
  });
};
