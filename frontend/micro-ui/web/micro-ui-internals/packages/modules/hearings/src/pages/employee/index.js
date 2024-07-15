import { AppContainer, BreadCrumb, PrivateRoute } from "@egovernments/digit-ui-react-components";
import React, { useMemo } from "react";
import { useTranslation } from "react-i18next";
import { Switch } from "react-router-dom";
import HearingsResponse from "./HearingsResponse";
import InsideHearingMainPage from "./InsideHearingMainPage";
import GenerateOrders from "./GenerateOrders";
import AddParty from "./AddParty";
import AdjournHearing from "./AdjournHearing";
import EndHearing from "./EndHearing";
import Orders from "./Orders";
import Submission from "./Submission";
import CaseHistory from "./CaseHistory";
import Parties from "./Parties";
import HomeViewHearing from "./HomeViewHearing";
import ViewHearing from "./ViewHearing";
import RescheduleHearing from "./ReSchedulHearing";
import ViewTranscript from "./ViewTranscript";
import ViewWitnessDeposition from "./ViewWitnessDeposition";
import ViewPendingTask from "./ViewPendingTask";
import HearingPopup from "./HearingPopUp";
import InsideHearing from "./InsideHearing";
import ViewCase from "./ViewCase";
import MonthlyCalendar from "./CalendarView";
import { DataProvider } from "../../components/DataContext";
const bredCrumbStyle = { maxWidth: "min-content" };
const ProjectBreadCrumb = ({ location }) => {
  const { t } = useTranslation();
  const userInfo = JSON.parse(window.localStorage.getItem("user-info"));
  const userType = useMemo(() => (userInfo.type === "CITIZEN" ? "citizen" : "employee"), [userInfo.type]);
  const crumbs = [
    {
      path: `/${window?.contextPath}/${userType}/home/home-pending-task`,
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
          <ProjectBreadCrumb location={window.location} />
        </React.Fragment>
        <PrivateRoute path={`${path}/hearings-response`} component={() => <HearingsResponse></HearingsResponse>} />
        <PrivateRoute path={`${path}/inside-hearing`} component={() => <InsideHearingMainPage />} />
        <PrivateRoute path={`${path}/generate-orders`} component={() => <GenerateOrders />} />
        <PrivateRoute path={`${path}/end-hearing`} component={() => <EndHearing />} />
        {/* <PrivateRoute path={`${path}/add-party`} component={() => <AddParty />} /> */}
        <PrivateRoute path={`${path}/adjourn-hearing`} component={() => <AdjournHearing />} />
        <PrivateRoute path={`${path}/orders`} component={() => <Orders />} />
        <PrivateRoute path={`${path}/parties`} component={() => <Parties />} />
        <PrivateRoute path={`${path}/case-history`} component={() => <CaseHistory />} />
        <PrivateRoute path={`${path}/home`} component={() => <HomeViewHearing></HomeViewHearing>} />
        <PrivateRoute path={`${path}/view-hearing`} component={() => <ViewHearing></ViewHearing>} />
        <PrivateRoute path={`${path}/hearing-popup`} component={() => <HearingPopup></HearingPopup>} />
        <PrivateRoute path={`${path}/inside-hearings`} component={() => <InsideHearing></InsideHearing>} />
        <PrivateRoute path={`${path}/view-case`} component={() => <ViewCase></ViewCase>} />
        {/* <PrivateRoute path={`${path}/add-party`} component={() => <AddParty></AddParty>} /> */}
        <PrivateRoute path={`${path}/view-transcript`} component={() => <ViewTranscript></ViewTranscript>} />
        <PrivateRoute path={`${path}/view-witness-deposition`} component={() => <ViewWitnessDeposition></ViewWitnessDeposition>} />
        <PrivateRoute path={`${path}/view-pending-task`} component={() => <ViewPendingTask></ViewPendingTask>} />
        <PrivateRoute path={`${path}/submission`} component={() => <Submission></Submission>} />
        <PrivateRoute
          exact
          path={`${path}/calendar`}
          component={() => (
            <DataProvider>
              <MonthlyCalendar />
            </DataProvider>
          )}
        />
      </AppContainer>
    </Switch>
  );
};

export default App;
