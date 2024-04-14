import { CitizenHomeCard, PTIcon, Loader } from "@egovernments/digit-ui-react-components";
import React, { useEffect } from "react";
import { useTranslation } from "react-i18next";
import { useRouteMatch } from "react-router-dom";
import CitizenApp from "./pages/citizen";
import SelectComponents from "./components/SelectComponents";
import SelectUserTypeComponent from "./components/SelectUserTypeComponent";
import Registration from "./pages/citizen/registration";
import EmployeeApp from "./pages/employee";
import DristiCard from "./components/DristiCard";
import Inbox from "./pages/employee/Inbox";

export const DristiModule = ({ stateCode, userType, tenants }) => {
  const { path, url } = useRouteMatch();

  const moduleCode = "Dristi";
  const language = Digit.StoreData.getCurrentLanguage();
  const { isLoading, data: store } = Digit.Services.useStore({ stateCode, moduleCode, language });

  if (isLoading) {
    return <Loader />;
  }
  Digit.SessionStorage.set("Dristi_TENANTS", tenants);

  if (userType === "citizen") {
    return <CitizenApp path={path} stateCode={stateCode} userType={userType} tenants={tenants} />;
  }
  return <EmployeeApp path={path} stateCode={stateCode} userType={userType} tenants={tenants}></EmployeeApp>;
};

export const DristiLinks = ({ matchPath, userType }) => {
  const { t } = useTranslation();

  const links = [
    {
      link: `${matchPath}/home/user-registration`,
      i18nKey: t("CS_COMMON_USER_REGISTRATION"),
    },
  ];

  return <CitizenHomeCard header={t("CS_USER_REGISTRATION")} links={links} Icon={() => <PTIcon className="fill-path-primary-main" />} />;
};

const componentsToRegister = {
  SelectComponents,
  SelectUserTypeComponent,
  DristiLinks,
  DristiModule,
  DristiRegistration: Registration,
  DristiCard,
  Inbox,
};

export const initDristiComponents = () => {
  Object.entries(componentsToRegister).forEach(([key, value]) => {
    Digit.ComponentRegistryService.setComponent(key, value);
  });
};
