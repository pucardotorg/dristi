import React from "react";
import { Loader } from "@egovernments/digit-ui-react-components";

import { useRouteMatch, useLocation } from "react-router-dom";

const DristiCard = () => {
  return <div>Dristi - Sample </div>;
};

const Routes = ({ path, stateCode }) => {
  const location = useLocation();
  const isMobile = window.Digit.Utils.browser.isMobile();
  return (
    <div className="chart-wrapper" style={isMobile ? { marginTop: "unset" } : {}}>
      {" "}
      Dristi - Sample
    </div>
  );
};

const DristiModule = ({ stateCode, userType, tenants }) => {
  const moduleCode = "Dristi";
  const { path } = useRouteMatch();
  const language = Digit.StoreData.getCurrentLanguage();
  const { isLoading, data: store } = Digit.Services.useStore({ stateCode, moduleCode, language });

  if (isLoading) {
    return <Loader />;
  }

  Digit.SessionStorage.set("DSS_TENANTS", tenants);

  if (userType !== "citizen") {
    return <Routes path={path} stateCode={stateCode} />;
  }
  return <div>Citizen Home Page</div>
};

const componentsToRegister = {
  DristiModule,
  DristiCard,
};

export const initDristiComponents = () => {
  Object.entries(componentsToRegister).forEach(([key, value]) => {
    Digit.ComponentRegistryService.setComponent(key, value);
  });
};
