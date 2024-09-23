import { AppContainer, BreadCrumb, PrivateRoute } from "@egovernments/digit-ui-react-components";
import React, { useMemo } from "react";
import { useTranslation } from "react-i18next";
import { Switch } from "react-router-dom";
import AdjournHearing from "./AdjournHearing";
import MonthlyCalendar from "./CalendarView";
import EndHearing from "./EndHearing";
import InsideHearingMainPage from "./InsideHearingMainPage";
import { useHistory } from "react-router-dom/cjs/react-router-dom.min";

const bredCrumbStyle = { maxWidth: "min-content" };

const ProjectBreadCrumb = ({ location }) => {
  const { t } = useTranslation();
  const userInfo = window?.Digit?.UserService?.getUser?.()?.info;
  const userType = useMemo(() => (userInfo?.type === "CITIZEN" ? "citizen" : "employee"), [userInfo?.type]);
  const crumbs = [
    {
      path: `/${window?.contextPath}/${userType}/home/home-pending-task`,
      content: t("ES_COMMON_HOME"),
      show: true,
    },
    {
      path: `/${window?.contextPath}/${userType}`,
      content: t(location.pathname.split("/").filter(Boolean).pop()),
      show: true,
    },
  ];
  return <BreadCrumb crumbs={crumbs} spanStyle={bredCrumbStyle} style={{ color: "rgb(0, 126, 126)" }} />;
};

const App = ({ path }) => {
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
      <AppContainer className="ground-container hearing-action-block">
        <React.Fragment>
          <ProjectBreadCrumb location={window.location} />
        </React.Fragment>
        <PrivateRoute path={`${path}/inside-hearing`} component={() => <InsideHearingMainPage />} />
        <PrivateRoute path={`${path}/end-hearing`} component={() => <EndHearing />} />
        <PrivateRoute path={`${path}/adjourn-hearing`} component={() => <AdjournHearing />} />
        <PrivateRoute exact path={`${path}/`} component={() => <MonthlyCalendar />} />
      </AppContainer>
    </Switch>
  );
};

export default App;
