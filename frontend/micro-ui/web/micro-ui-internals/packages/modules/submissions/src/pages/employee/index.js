import { AppContainer, BreadCrumb, PrivateRoute } from "@egovernments/digit-ui-react-components";
import React, { useMemo } from "react";
import { useTranslation } from "react-i18next";
import { Switch } from "react-router-dom";
import SubmissionsResponse from "./SubmissionsResponse";
import SubmissionsCreate from "./SubmissionsCreate";
import SubmissionsSearch from "./SubmissionsSearch";
import { useHistory } from "react-router-dom/cjs/react-router-dom.min";
const bredCrumbStyle = { maxWidth: "min-content" };

const ProjectBreadCrumb = ({ location }) => {
  const userInfo = Digit?.UserService?.getUser()?.info;
  let userType = "employee";
  if (userInfo) {
    userType = userInfo?.type === "CITIZEN" ? "citizen" : "employee";
  }
  const { t } = useTranslation();
  const crumbs = [
    {
      path: `/${window?.contextPath}/${userType}/home/home-pending-task`,
      content: t("HOME"),
      show: true,
    },
    {
      path: `/${window?.contextPath}/${userType}`,
      content: t(location.pathname.split("/").pop()),
      show: true,
    },
  ];
  return <BreadCrumb crumbs={crumbs} spanStyle={bredCrumbStyle} />;
};

const App = ({ path, stateCode, userType, tenants }) => {
  const history = useHistory();
  const Digit = useMemo(() => window?.Digit || {}, []);
  const userInfo = Digit?.UserService?.getUser()?.info;
  const hasCitizenRoute = useMemo(() => path?.includes(`/${window?.contextPath}/citizen`), [path]);
  const isCitizen = useMemo(() => Boolean(Digit?.UserService?.getUser()?.info?.type === "CITIZEN"), [Digit]);

  if (isCitizen && !hasCitizenRoute && Boolean(userInfo)) {
    history.push(`/${window?.contextPath}/citizen/home/home-pending-task`);
  } else if (!isCitizen && hasCitizenRoute && Boolean(userInfo)) {
    history.push(`/${window?.contextPath}/employee/home/home-pending-task`);
  }
  return (
    <Switch>
      <AppContainer className="ground-container submission-main">
        <React.Fragment>
          <ProjectBreadCrumb location={window.location} />
        </React.Fragment>
        <PrivateRoute path={`${path}/submissions-response`} component={() => <SubmissionsResponse></SubmissionsResponse>} />
        <PrivateRoute path={`${path}/submissions-create`} component={() => <SubmissionsCreate path={path} />} />
        <PrivateRoute path={`${path}/submissions-search`} component={() => <SubmissionsSearch></SubmissionsSearch>} />
      </AppContainer>
    </Switch>
  );
};

export default App;
