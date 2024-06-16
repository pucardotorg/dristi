import { AppContainer, BreadCrumb, PrivateRoute } from "@egovernments/digit-ui-react-components";
import React from "react";
import { useTranslation } from "react-i18next";
import { Switch } from "react-router-dom";
import PucarResponse from "./PucarResponse";
import PucarCreate from "./PucarCreate";
import PucarSearch from "./PucarSearch";
const bredCrumbStyle={ maxWidth: "min-content" };
const ProjectBreadCrumb = ({ location }) => {
  const { t } = useTranslation();
  const crumbs = [
    {
      path: `/${window?.contextPath}/employee`,
      content: t("HOME"),
      show: true,
    },
    {
      path: `/${window?.contextPath}/employee`,
      content: t(location.pathname.split("/").pop()),
      show: true,
    },
  ];
  return <BreadCrumb crumbs={crumbs} spanStyle={bredCrumbStyle} />;
};

const App = ({ path, stateCode, userType, tenants }) => {
  return (
    <Switch>
      <AppContainer className="ground-container">
        <React.Fragment>
          <ProjectBreadCrumb location={location} />
        </React.Fragment>
        <PrivateRoute path={`${path}/pucar-response`} component={() => <PucarResponse></PucarResponse>} />
        <PrivateRoute path={`${path}/pucar-create`} component={() => <PucarCreate />} />
        <PrivateRoute path={`${path}/pucar-search`} component={() => <PucarSearch></PucarSearch>} />
      </AppContainer>
    </Switch>
  );
};

export default App;
