import { BackButton, HelpOutlineIcon, PrivateRoute } from "@egovernments/digit-ui-react-components";
import React from "react";
import { useTranslation } from "react-i18next";
import { Switch } from "react-router-dom";
import ApplicationDetails from "./ApplicationDetails";
import { useLocation } from "react-router-dom/cjs/react-router-dom.min";
import CaseFileAdmission from "./admission/CaseFileAdmission";
import Home from "./home";

const EmployeeApp = ({ path, url, userType, tenants, parentRoute }) => {
  const { t } = useTranslation();
  const location = useLocation();
  const Inbox = Digit?.ComponentRegistryService?.getComponent("Inbox");
  const hideHomeCrumb = [`${path}/cases`];
  const employeeCrumbs = [
    {
      path: `/digit-ui/employee`,
      content: t("ES_COMMON_HOME"),
      show: !hideHomeCrumb.includes(location.pathname),
      isLast: false,
    },
    {
      path: `${path}/registration-requests`,
      content: t("ES_REGISTRATION_REQUESTS"),
      show: location.pathname.includes("/registration-requests"),
      isLast: !location.pathname.includes("/details"),
    },
    {
      path: ``,
      content: t("ES_APPLICATION_DETAILS"),
      show: location.pathname.includes("/registration-requests/details"),
      isLast: true,
    },
  ];
  return (
    <Switch>
      <React.Fragment>
        <div className="ground-container">
          {!location.pathname.endsWith("/registration-requests") && (
            <div className="back-button-home">
              <BackButton />

              {/* <Breadcrumb crumbs={employeeCrumbs} breadcrumbStyle={{ paddingLeft: 20 }}></Breadcrumb> */}
              <span style={{ display: "flex", justifyContent: "right", gap: "5px" }}>
                <span style={{ color: "#f47738" }}>Help</span>
                <HelpOutlineIcon />
              </span>
            </div>
          )}
          <PrivateRoute exact path={`${path}/registration-requests`} component={(props) => <Inbox {...props} />} />
          <PrivateRoute exact path={`${path}/registration-requests/details`} component={(props) => <ApplicationDetails {...props} />} />
          <div className={location.pathname.endsWith("employee/dristi/cases") ? "file-case-main" : ""}>
            <PrivateRoute exact path={`${path}/cases`} component={(props) => <Home {...props} t={t} />} />
          </div>
          <div className={location.pathname.endsWith("employee/dristi/cases") ? "file-case-main" : ""}>
            <PrivateRoute exact path={`${path}/admission`} component={(props) => <CaseFileAdmission {...props} t={t} />} />
          </div>
        </div>
      </React.Fragment>
    </Switch>
  );
};

export default EmployeeApp;
