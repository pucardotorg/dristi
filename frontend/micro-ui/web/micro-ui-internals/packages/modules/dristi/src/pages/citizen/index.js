import { AppContainer, HelpOutlineIcon, Loader, PrivateRoute } from "@egovernments/digit-ui-react-components";
import React, { useMemo } from "react";
import { Switch, useRouteMatch } from "react-router-dom";

import { useTranslation } from "react-i18next";
import { Route, useHistory, useLocation } from "react-router-dom/cjs/react-router-dom.min";
import CitizenHome from "./Home";
import LandingPage from "./Home/LandingPage";
import ApplicationDetails from "../employee/ApplicationDetails";
import BreadCrumb from "../../components/BreadCrumb";

const App = ({ stateCode, tenantId }) => {
  const Digit = window?.Digit || {};
  const { path } = useRouteMatch();
  const location = useLocation();
  const { t } = useTranslation();
  const history = useHistory();
  const Registration = Digit?.ComponentRegistryService?.getComponent("DRISTIRegistration");
  const Response = Digit?.ComponentRegistryService?.getComponent("DRISTICitizenResponse");
  const Login = Digit?.ComponentRegistryService?.getComponent("DRISTILogin");
  const FileCase = Digit?.ComponentRegistryService?.getComponent("FileCase");
  const token = window.localStorage.getItem("token");
  const isUserLoggedIn = Boolean(token);

  const moduleCode = "DRISTI";
  const userInfo = JSON.parse(window.localStorage.getItem("user-info"));
  if (isUserLoggedIn && userInfo) {
    const user = {
      access_token: token,
      info: userInfo,
    };
    Digit.UserService.setUser(user);
  }
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

  const individualId = useMemo(() => data?.Individual?.[0]?.individualId, [data?.Individual]);

  const hideHomeCrumb = [`${path}/home`];
  const citizenCrumbs = [
    {
      path: isUserLoggedIn ? `${path}/home` : "",
      content: t("ES_COMMON_HOME"),
      show: !hideHomeCrumb.includes(location.pathname),
    },
    {
      path: !isUserLoggedIn ? `${path}/home/login` : "",
      content: t("ES_COMMON_LOGIN"),
      show: location.pathname.includes("/home/login"),
      isLast: !location.pathname.includes("/login"),
    },
    {
      path: isUserLoggedIn ? `${path}/home/login/user-name` : "",
      content: t("ES_COMMON_USER_NAME"),
      show: location.pathname.includes("/home/login/user-name"),
      isLast: true,
    },
    {
      path: isUserLoggedIn ? `${path}/home/register` : "",
      content: t("ES_COMMON_REGISTER"),
      show: location.pathname.includes("/home/register"),
      isLast: true,
    },
    {
      path: isUserLoggedIn ? `${path}/home/registration` : "",
      content: t("ES_COMMON_REGISTER"),
      show: location.pathname.includes("/home/registration"),
      isLast: !location.pathname.includes("/registration"),
    },
    // {
    //   path: isUserLoggedIn ? `${path}/home/registration/additional-details` : "",
    //   content: t("ES_COMMON_USER_ADDITIONAL_DETAILS"),
    //   show: location.pathname.includes("/home/registration/additional-details"),
    //   isLast: !location.pathname.includes("/registration"),
    // },
    {
      path: isUserLoggedIn ? `${path}/home/registration/additional-details/terms-conditions` : "",
      content: t("ES_COMMON_USER_TERMS_AND_CONDITIONS"),
      show: location.pathname.includes("/home/registration/additional-details/terms-conditions"),
      isLast: true,
    },
    {
      path: isUserLoggedIn ? `${path}/home/registration/terms-conditions` : "",
      content: t("ES_COMMON_USER_TERMS_AND_CONDITIONS"),
      show: location.pathname.includes("/home/registration/terms-conditions"),
      isLast: true,
    },
  ];
  const whiteListedRoutes = [
    `${path}/home/response`,
    `${path}/home/register`,
    `${path}/home/register/otp`,
    `${path}/home/login/otp`,
    `${path}/home/login`,
    `${path}/home/registration/user-name`,
    `${path}/home/registration/user-type`,
    `${path}/home/registration/user-address`,
    `${path}/home/registration/user-number`,
    `${path}/home/registration/otp`,
    `${path}/home/registration/mobile-number`,
    `${path}/home/registration/id-verification`,
    `${path}/home/registration/enter-adhaar`,
    `${path}/home/registration/aadhar-otp`,
    `${path}/home/registration/additional-details`,
  ];

  if (!isUserLoggedIn && !whiteListedRoutes.includes(location.pathname)) {
    history.push(`${path}/home/login`);
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
          <div style={{ display: "flex", justifyContent: "align-right", alignItems: "center" }}>
            {/* <BreadCrumb crumbs={citizenCrumbs} breadcrumbStyle={{ paddingLeft: 20 }}></BreadCrumb> */}
            {!hideHomeCrumb.includes(location.pathname) && (
              <span style={{ display: "flex", justifyContent: "align-right", alignItems: "center", gap: "5px" }}>
                <span style={{ color: "#f47738" }}>Help</span>
                <HelpOutlineIcon />
              </span>
            )}
          </div>
          <PrivateRoute exact path={`${path}/home`}>
            <CitizenHome tenantId={tenantId} />
          </PrivateRoute>
          <PrivateRoute exact path={`${path}/home/application-details`} component={(props) => <ApplicationDetails {...props} />} />
          <PrivateRoute exact path={`${path}/response`} component={Response} />
          <PrivateRoute path={`${path}/home/file-case`}>
            <FileCase t={t}></FileCase>
          </PrivateRoute>
          <Route path={`${path}/home/login`}>
            <Login stateCode={stateCode} />
          </Route>
          <Route path={`${path}/home/register`}>
            <Login stateCode={stateCode} isUserRegistered={false} />
          </Route>
          <Route path={`${path}/home/registration`}>
            <Registration stateCode={stateCode} />
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
