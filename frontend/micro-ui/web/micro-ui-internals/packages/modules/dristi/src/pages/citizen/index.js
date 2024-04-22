import { AppContainer, BreadCrumb, HelpLineIcon, HelpOutlineIcon, Loader, PrivateRoute } from "@egovernments/digit-ui-react-components";
import React from "react";
import { Switch, useRouteMatch } from "react-router-dom";

import { useTranslation } from "react-i18next";
import { Route, useHistory, useLocation } from "react-router-dom/cjs/react-router-dom.min";
import CitizenHome from "./Home";
import LandingPage from "./Home/LandingPage";
import ApplicationDetails from "../employee/ApplicationDetails";
import { Link } from "react-router-dom/cjs/react-router-dom";

const App = ({ stateCode, tenantId }) => {
  const Digit = window?.Digit || {};
  const { path } = useRouteMatch();
  const location = useLocation();
  const { t } = useTranslation();
  const history = useHistory();
  const Registration = Digit?.ComponentRegistryService?.getComponent("DRISTIRegistration");
  const Response = Digit?.ComponentRegistryService?.getComponent("DRISTICitizenResponse");
  const Login = Digit?.ComponentRegistryService?.getComponent("DRISTILogin");
  const token = window.localStorage.getItem("token");
  const isUserLoggedIn = Boolean(token);

  const moduleCode = "DRISTI";
  const userInfo = JSON.parse(window.localStorage.getItem("user-info"));

  const { data, isLoading, refetch } = Digit.Hooks.dristi.useGetIndividualUser(
    {
      Individual: {
        userUuid: [userInfo?.uuid],
      },
    },
    { tenantId, limit: 1000, offset: 0 },
    moduleCode,
    "",
    userInfo?.uuid && isUserLoggedIn
  );
  const individualId = data?.Individual?.[0]?.individualId;
  const userType = data?.Individual?.[0]?.additionalFields?.fields?.find((obj) => obj.key === "userType")?.value;
  const isApprovalPending = userType !== "LITIGANT";

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
      isLast: true,
    },
    {
      path: isUserLoggedIn ? `${path}/home/register` : "",
      content: t("ES_COMMON_REGISTER"),
      show: location.pathname.includes("/home/register"),
      isLast: !location.pathname.includes("/user-registration"),
    },
    {
      path: isUserLoggedIn ? `${path}/home/register/user-registration` : "",
      content: t("ES_COMMON_USER_REGISTER"),
      show: location.pathname.includes("/home/register/user-registration"),
      isLast: true,
    },
  ];
  const whiteListedRoutes = [
    `${path}/landing-page`,
    `${path}/home/response`,
    `${path}/home/register`,
    `${path}/home/register/otp`,
    `${path}/home/login/otp`,
    `${path}/home/login`,
  ];

  if (!isUserLoggedIn && !whiteListedRoutes.includes(location.pathname)) {
    history.push(`${path}/landing-page`);
  }

  if (individualId && whiteListedRoutes.includes(location.pathname)) {
    history.push(`${path}/home`);
  }

  if (isUserLoggedIn && !location.pathname.includes(`${path}/home`)) {
    history.push(`${path}/home`);
  }

  if (isLoading) {
    return <Loader />;
  }

  return (
    <span className={"pt-citizen"}>
      <Switch>
        <AppContainer style={{ minWidth: "100%" }}>
          <div style={{ display: "flex", justifyContent: "space-between", alignItems: "center" }}>
            <BreadCrumb crumbs={dristiCrumbs} breadcrumbStyle={{ paddingLeft: 20 }}></BreadCrumb>
            {!hideHomeCrumb.includes(location.pathname) && (
              <span style={{ display: "flex", justifyContent: "space-between", alignItems: "center", gap: "5px" }}>
                <Link target="_blank" style={{ color: "#f47738" }}>
                  Help
                </Link>
                <HelpOutlineIcon />
              </span>
            )}
          </div>
          <PrivateRoute exact path={`${path}/home`}>
            <CitizenHome tenantId={tenantId} individualId={individualId} isApprovalPending={isApprovalPending} refetch={refetch} />
          </PrivateRoute>
          <PrivateRoute exact path={`${path}/home/register/user-registration`} component={Registration} refetch={refetch} />
          <PrivateRoute exact path={`${path}/home/application-details`} component={(props) => <ApplicationDetails {...props} />} />
          <PrivateRoute exact path={`${path}/response`} component={Response} />
          <Route path={`${path}/home/login`}>
            <Login stateCode={stateCode} />
          </Route>
          <Route path={`${path}/home/register`}>
            <Login stateCode={stateCode} isUserRegistered={false} />
          </Route>
          <Route path={`${path}/home/response`}>
            <Response refetch={refetch} />
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
