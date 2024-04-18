import { AppContainer, BreadCrumb, PrivateRoute } from "@egovernments/digit-ui-react-components";
import React, { useState } from "react";
import { Switch, useRouteMatch } from "react-router-dom";

import { useTranslation } from "react-i18next";
import { Route, useHistory, useLocation } from "react-router-dom/cjs/react-router-dom.min";
import CitizenHome from "./Home";
import LandingPage from "./Home/LandingPage";

const App = ({ stateCode }) => {
  const { path } = useRouteMatch();
  const location = useLocation();
  const [isUserLoggedIn, setIsUserLoggedIn] = useState(true);
  const { t } = useTranslation();
  const history = useHistory();
  const Registration = Digit?.ComponentRegistryService?.getComponent("DRISTIRegistration");
  const Response = Digit?.ComponentRegistryService?.getComponent("DRISTICitizenResponse");
  const Login = Digit?.ComponentRegistryService?.getComponent("DRISTILogin");

  const hideHomeCrumb = [`${path}/home`, `${path}/landing-page`];
  const dristiCrumbs = [
    {
      path: isUserLoggedIn ? `${path}/home` : "",
      content: t("ES_COMMON_HOME"),
      show: !hideHomeCrumb.includes(location.pathname),
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
  const whiteListedRoutes = [
    `${path}/landing-page`,
    `${path}/home/response`,
    `${path}/home/register`,
    `${path}/home/login/otp`,
    `${path}/home/login`,
    `${path}/home/login/id-verification`,
    `${path}/home/login/id-verification`,
    `${path}/home/login/aadhar-otp`,
    `${path}/home/user-registration`,
  ];

  if (!isUserLoggedIn && !whiteListedRoutes.includes(location.pathname)) {
    history.push(`${path}/landing-page`);
  }
  return (
    <span className={"pt-citizen"}>
      <Switch>
        <AppContainer style={{ minWidth: "100%" }}>
          <BreadCrumb crumbs={dristiCrumbs} breadcrumbStyle={{ paddingLeft: 20 }}></BreadCrumb>
          <PrivateRoute exact path={`${path}/home`}>
            <CitizenHome />
          </PrivateRoute>
          <PrivateRoute exact path={`${path}/home/user-registration`} component={Registration} />
          <PrivateRoute exact path={`${path}/response`} component={Response} />
          <Route path={`${path}/home/login`}>
            <Login stateCode={stateCode} />
          </Route>
          <Route path={`${path}/home/register`}>
            <Login stateCode={stateCode} isUserRegistered={false} />
          </Route>
          <Route path={`${path}/home/response`}>
            <Response />
          </Route>
          <Route path={`${path}/landing-page`}>
            <LandingPage />
          </Route>
        </AppContainer>
      </Switch>
    </span>
  );
};

export default App;
