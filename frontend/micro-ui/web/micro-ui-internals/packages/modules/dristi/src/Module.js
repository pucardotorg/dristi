import { CitizenHomeCard, PTIcon, Loader } from "@egovernments/digit-ui-react-components";
import React, { useEffect } from "react";
import { useTranslation } from "react-i18next";
import { useRouteMatch } from "react-router-dom";
import CitizenApp from "./pages/citizen";
import SearchLocationAddress from "./components/SearchLocationAddress";
import Registration from "./pages/citizen/registration";

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
  return <React.Fragment></React.Fragment>;
};

export const DristiLinks = ({ matchPath, userType }) => {
  const { t } = useTranslation();

  const links = [
    {
      link: `${matchPath}/user-registration`,
      i18nKey: t("CS_COMMON_USER_REGISTRATION"),
    },
  ];

  return <CitizenHomeCard header={t("CS_USER_REGISTRATION")} links={links} Icon={() => <PTIcon className="fill-path-primary-main" />} />;
};

const componentsToRegister = {
  SearchLocationAddress,
  DristiLinks,
  DristiModule,
  DristiRegistration: Registration,
};

export const initDristiComponents = () => {
  Object.entries(componentsToRegister).forEach(([key, value]) => {
    Digit.ComponentRegistryService.setComponent(key, value);
  });
};
