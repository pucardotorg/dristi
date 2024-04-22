import { PrivateRoute } from "@egovernments/digit-ui-react-components";
import React from "react";
import { useTranslation } from "react-i18next";
import { Switch } from "react-router-dom";
import ApplicationDetails from "./ApplicationDetails";

const EmployeeApp = ({ path, url, userType, tenants, parentRoute }) => {
  const { t } = useTranslation();

  const Inbox = Digit?.ComponentRegistryService?.getComponent("Inbox");

  return (
    <Switch>
      <React.Fragment>
        <div className="ground-container">
          <PrivateRoute exact path={`${path}/registration-requests`} component={(props) => <Inbox {...props} />} />
          <PrivateRoute exact path={`${path}/registration-requests/details/:applicationNo`} component={(props) => <ApplicationDetails {...props} />} />
        </div>
      </React.Fragment>
    </Switch>
  );
};

export default EmployeeApp;
