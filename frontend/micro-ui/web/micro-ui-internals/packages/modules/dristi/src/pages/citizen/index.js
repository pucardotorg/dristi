import { BackButton, Loader, PrivateRoute, Toast } from "@egovernments/digit-ui-react-components";
import React, { useMemo, useState } from "react";
import { useTranslation } from "react-i18next";
import { Switch, useRouteMatch } from "react-router-dom";
import { Route, useHistory, useLocation } from "react-router-dom/cjs/react-router-dom.min";
import { useToast } from "../../components/Toast/useToast";
import AdmittedCases from "../employee/AdmittedCases/AdmittedCase";
import ApplicationDetails from "../employee/ApplicationDetails";
import CitizenHome from "./Home";
import LandingPage from "./Home/LandingPage";
import { userTypeOptions } from "./registration/config";
import Breadcrumb from "../../components/BreadCrumb";

const App = ({ stateCode, tenantId, result, fileStoreId }) => {
  const [hideBack, setHideBack] = useState(false);
  const { toastMessage, toastType, closeToast } = useToast();
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
  if (individualId && !localStorage.getItem(individualId)) {
    localStorage.setItem("individualId", individualId);
  }

  const userType = useMemo(() => data?.Individual?.[0]?.additionalFields?.fields?.find((obj) => obj.key === "userType")?.value, [data?.Individual]);
  const { data: searchData, isLoading: isSearchLoading } = Digit.Hooks.dristi.useGetAdvocateClerk(
    {
      criteria: [{ individualId }],
      tenantId,
    },
    { tenantId },
    moduleCode,
    Boolean(isUserLoggedIn && individualId && userType !== "LITIGANT"),
    userType === "ADVOCATE" ? "/advocate/advocate/v1/_search" : "/advocate/clerk/v1/_search"
  );

  const userTypeDetail = useMemo(() => {
    return userTypeOptions.find((item) => item.code === userType) || {};
  }, [userType]);

  const searchResult = useMemo(() => {
    return searchData?.[`${userTypeDetail?.apiDetails?.requestKey}s`]?.[0]?.responseList;
  }, [searchData, userTypeDetail?.apiDetails?.requestKey]);

  const isRejected = useMemo(() => {
    return (
      userType !== "LITIGANT" &&
      Array.isArray(searchResult) &&
      searchResult?.length > 0 &&
      searchResult?.[0]?.isActive === false &&
      searchResult?.[0]?.status === "INACTIVE"
    );
  }, [searchResult, userType]);
  const hideHomeCrumb = [`${path}/home`];

  const citizenCrumb = [
    {
      path: `/digit-ui/citizen/home/home-pending-task`,
      content: t("ES_COMMON_HOME"),
      show: !hideHomeCrumb.includes(location.pathname),
      isLast: false,
    },
    {
      path: `${path}/view-case`,
      content: t("VIEW_CASE"),
      show: location.pathname.includes("/view-case"),
      isLast: true,
    },
  ];

  const whiteListedRoutes = [
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
    `${path}/home/registration/upload-id`,
    `${path}/home/registration/terms-condition`,
  ];
  const registerScreenRoute = [`${path}/home/login`, `${path}/home/registration/mobile-number`, `${path}/home/registration/otp`];
  const eSignWindowObject = localStorage.getItem("eSignWindowObject");
  const retrievedObject = JSON.parse(eSignWindowObject);
  if (!isUserLoggedIn && !whiteListedRoutes.includes(location.pathname)) {
    history.push(`${path}/home/login`);
  }
  if (!isRejected && individualId && whiteListedRoutes.includes(location.pathname)) {
    history.push(`${path}/home`);
  }

  if (isUserLoggedIn && !location.pathname.includes(`${path}/home`)) {
    history.push(`${path}/home`);
  }
  if (isUserLoggedIn && registerScreenRoute.includes(location.pathname)) {
    history.push(`${path}/home/registration/user-name`);
  }
  if (result) {
    localStorage.setItem("isSignSuccess", result);
  }
  if (fileStoreId) {
    console.log(fileStoreId, "fileStoreId");
    localStorage.setItem("fileStoreId", fileStoreId);
  }
  if (isUserLoggedIn && retrievedObject) {
    history.push(`${retrievedObject?.path}${retrievedObject?.param}`);
    localStorage.removeItem("eSignWindowObject");
  }
  if (isLoading) {
    return <Loader />;
  }

  return (
    <div className={"pt-citizen"}>
      <Switch>
        <React.Fragment>
          {!hideBack && !(location.pathname.includes("/login") || location.pathname.includes("/registration/user-name") || individualId) && (
            <div className="back-button-home">
              <BackButton />
            </div>
          )}
          {location.pathname.includes("/view-case") && <Breadcrumb crumbs={citizenCrumb} breadcrumbStyle={{ paddingLeft: 20 }}></Breadcrumb>}

          {userType !== "LITIGANT" && (
            <PrivateRoute exact path={`${path}/home/application-details`} component={(props) => <ApplicationDetails {...props} />} />
          )}
          <PrivateRoute exact path={`${path}/response`} component={Response} />
          <div
            className={
              location.pathname.includes("/file-case")
                ? location.pathname.includes("/file-case/e-filing-payment")
                  ? "file-case-main payment-wrapper"
                  : "file-case-main"
                : ""
            }
          >
            <PrivateRoute path={`${path}/home/file-case`}>
              <FileCase t={t}></FileCase>
            </PrivateRoute>
          </div>

          <PrivateRoute exact path={`${path}/home/view-case`} component={(props) => <AdmittedCases isJudge={false} />} />
          <div
            className={
              location.pathname.includes("/response") ||
              location.pathname.includes("/login") ||
              location.pathname.includes("/registration") ||
              location.pathname.endsWith("/home")
                ? `user-registration`
                : ""
            }
          >
            <PrivateRoute exact path={`${path}/home`}>
              <CitizenHome tenantId={tenantId} setHideBack={setHideBack} />
            </PrivateRoute>
            <Route path={`${path}/home/login`}>
              <Login stateCode={stateCode} />
            </Route>
            <Route path={`${path}/home/registration`}>
              <Registration stateCode={stateCode} />
            </Route>
            <Route path={`${path}/home/response`}>
              <Response refetch={refetch} setHideBack={setHideBack} />
            </Route>
          </div>
          <Route path={`${path}/home/register`}>
            <Login stateCode={stateCode} isUserRegistered={false} />
          </Route>

          <Route path={`${path}/landing-page`}>
            <LandingPage />
          </Route>
        </React.Fragment>
      </Switch>
      {toastMessage && (
        <Toast
          style={{ right: 24, left: "unset" }}
          label={toastMessage}
          onClose={closeToast}
          {...(toastType !== "success" && { [toastType]: true })}
        />
      )}
    </div>
  );
};

export default App;
