import { HelpOutlineIcon, PrivateRoute } from "@egovernments/digit-ui-react-components";
import React from "react";
import { useTranslation } from "react-i18next";
import { Switch } from "react-router-dom";
import ApplicationDetails from "./ApplicationDetails";
import { useLocation } from "react-router-dom/cjs/react-router-dom.min";
import Breadcrumb from "../../components/BreadCrumb";

const EmployeeApp = ({ path, url, userType, tenants, parentRoute }) => {
  const { t } = useTranslation();
  const location = useLocation();
  const Inbox = Digit?.ComponentRegistryService?.getComponent("Inbox");

  const employeeCrumbs = [
    {
      path: `/digit-ui/employee`,
      content: t("ES_COMMON_HOME"),
      show: true,
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
          <div style={{ display: "flex", justifyContent: "space-between", alignItems: "center" }}>
            <Breadcrumb crumbs={employeeCrumbs} breadcrumbStyle={{ paddingLeft: 20 }}></Breadcrumb>
            <span style={{ display: "flex", justifyContent: "space-between", alignItems: "center", gap: "5px" }}>
              <span style={{ color: "#007E7E" }}>Help</span>
              <HelpOutlineIcon />
            </span>
          </div>
          <PrivateRoute exact path={`${path}/registration-requests`} component={(props) => <Inbox {...props} />} />
          <PrivateRoute
            exact
            path={`${path}/registration-requests/details/:applicationNo`}
            component={(props) => <ApplicationDetails {...props} />}
          />
        </div>
      </React.Fragment>
    </Switch>
  );
};

export default EmployeeApp;
