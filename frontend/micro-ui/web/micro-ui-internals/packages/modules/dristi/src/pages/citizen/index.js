import { AppContainer, BreadCrumb, PrivateRoute } from "@egovernments/digit-ui-react-components";
import React, { useState } from "react";
import { Switch, useRouteMatch } from "react-router-dom";

import { useTranslation } from "react-i18next";
import { Route, useLocation } from "react-router-dom/cjs/react-router-dom.min";
import Login from "./Login";
import CitizenHome from "./Home";

const App = () => {
  const { path } = useRouteMatch();
  const location = useLocation();
  const [isUserLoggedIn, setIsUserLoggedIn] = useState(false);
  const { t } = useTranslation();

  const Registration = Digit?.ComponentRegistryService?.getComponent("DRISTIRegistration");
  const Response = Digit?.ComponentRegistryService?.getComponent("Response");

  const dristiCrumbs = [
    {
      path: isUserLoggedIn ? `/digit-ui/citizen/dristi/home` : "",
      content: t("ES_COMMON_HOME"),
      show: location.pathname !== `/digit-ui/citizen/dristi/home`,
    },
    {
      path: isUserLoggedIn ? `${path}/home/login` : "",
      content: t("ES_COMMON_LOGIN"),
      show: location.pathname.includes("/home/login"),
    },
    {
      path: isUserLoggedIn ? `${path}/home/register` : "",
      content: t("ES_COMMON_REGISTER"),
      show: location.pathname.includes("/home/register"),
    },
    {
      path: isUserLoggedIn ? `${path}/home/user-registration` : "",
      content: t("ES_COMMON_asdf_REGISTER"),
      show: location.pathname.includes("/home/user-registration"),
    },
  ];

  return (
    <span className={"pt-citizen"}>
      <Switch>
        <AppContainer>
          <BreadCrumb crumbs={dristiCrumbs}></BreadCrumb>
          <PrivateRoute exact path={`${path}/home`}>
            <CitizenHome />
          </PrivateRoute>
          <PrivateRoute exact path={`${path}/home/user-registration`} component={Registration} />
          <PrivateRoute exact path={`${path}/response`} component={Response} />
          <Route exact path={`${path}/home/login`}>
            <Login />
          </Route>
          <Route exact path={`${path}/home/register`}>
            <Login isUserRegistered={false} />
          </Route>
        </AppContainer>
      </Switch>
    </span>
  );
};

export default App;
