import { AppContainer, BreadCrumb, PrivateRoute } from "@egovernments/digit-ui-react-components";
import React from "react";
import { useTranslation } from "react-i18next";
import { Switch } from "react-router-dom";
import HearingsResponse from "./HearingsResponse";
import InsideHearingMainPage from "./InsideHearingMainPage";
import HomeView from "./HomeView";
import ViewHearing from "./ViewHearing";
import HomePopUp from "./HomePopUp";
import EfilingPaymentBreakdown from "../../components/EfilingPaymentDropdown";
import EFilingPaymentRes from "../../components/EfilingPaymentRes";
import ScheduleHearing from "./ScheduleHearing";
const bredCrumbStyle = { maxWidth: "min-content" };
const userInfo = JSON.parse(window.localStorage.getItem("user-info"));
let userType = "employee";
if (userInfo) {
  userType = userInfo?.type === "CITIZEN" ? "citizen" : "employee";
}
const ProjectBreadCrumb = ({ location }) => {
  const { t } = useTranslation();
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
  const SummonsAndWarrantsModal = Digit.ComponentRegistryService.getComponent("SummonsAndWarrantsModal") || <React.Fragment></React.Fragment>;
  const ReIssueSummonsModal = Digit.ComponentRegistryService.getComponent("ReIssueSummonsModal") || <React.Fragment></React.Fragment>;
  const PaymentForSummonModal = Digit.ComponentRegistryService.getComponent("PaymentForSummonModal") || <React.Fragment></React.Fragment>;

  return (
    <Switch>
      <AppContainer className="ground-container">
        <PrivateRoute path={`${path}/hearings-response`} component={() => <HearingsResponse></HearingsResponse>} />
        <PrivateRoute path={`${path}/inside-hearing`} component={() => <InsideHearingMainPage />} />
        <PrivateRoute path={`${path}/e-filing-payment-response`} component={() => <EFilingPaymentRes></EFilingPaymentRes>} />
        <PrivateRoute path={`${path}/home-pending-task`} component={() => <HomeView></HomeView>} />
        <PrivateRoute
          path={`${path}/home-pending-task/e-filing-payment-breakdown`}
          component={() => <EfilingPaymentBreakdown></EfilingPaymentBreakdown>}
        />
        <PrivateRoute
          path={`${path}/home-pending-task/summons-warrants-modal`}
          component={() => <SummonsAndWarrantsModal></SummonsAndWarrantsModal>}
        />
        <PrivateRoute path={`${path}/home-pending-task/reissue-summons-modal`} component={() => <ReIssueSummonsModal></ReIssueSummonsModal>} />
        <PrivateRoute path={`${path}/home-pending-task/post-payment-modal`} component={() => <PaymentForSummonModal></PaymentForSummonModal>} />
        <PrivateRoute path={`${path}/view-hearing`} component={() => <ViewHearing></ViewHearing>} />
        <PrivateRoute path={`${path}/home-popup`} component={() => <HomePopUp></HomePopUp>} />
        <PrivateRoute path={`${path}/home-pending-task/home-schedule-hearing`} component={() => <ScheduleHearing />} />
      </AppContainer>
    </Switch>
  );
};

export default App;
