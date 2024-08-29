import { AppContainer, BreadCrumb, PrivateRoute } from "@egovernments/digit-ui-react-components";
import React, { useMemo } from "react";
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
import PaymentStatus from "../../../../orders/src/components/PaymentStatus";
import ScheduleNextHearing from "./ScheduleNextHearing";
import DashboardPage from "./Dashboard";
import { useHistory } from "react-router-dom/cjs/react-router-dom.min";
const bredCrumbStyle = { maxWidth: "min-content" };

const ProjectBreadCrumb = ({ location }) => {
  const userInfo = window?.Digit?.UserService?.getUser()?.info;
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
  const Digit = useMemo(() => window?.Digit || {}, []);
  const SummonsAndWarrantsModal = Digit.ComponentRegistryService.getComponent("SummonsAndWarrantsModal") || <React.Fragment></React.Fragment>;
  const ReIssueSummonsModal = Digit.ComponentRegistryService.getComponent("ReIssueSummonsModal") || <React.Fragment></React.Fragment>;
  const PaymentForSummonModal = Digit.ComponentRegistryService.getComponent("PaymentForSummonModal") || <React.Fragment></React.Fragment>;
  const PaymentForSummonModalSMSAndEmail = Digit.ComponentRegistryService.getComponent("PaymentForSummonModalSMSAndEmail") || (
    <React.Fragment></React.Fragment>
  );

  const history = useHistory();
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
      <AppContainer className="ground-container">
        <PrivateRoute path={`${path}/hearings-response`} component={() => <HearingsResponse></HearingsResponse>} />
        <PrivateRoute path={`${path}/inside-hearing`} component={() => <InsideHearingMainPage />} />
        <PrivateRoute path={`${path}/home-pending-task/e-filing-payment-response`} component={() => <EFilingPaymentRes></EFilingPaymentRes>} />
        <PrivateRoute path={`${path}/home-pending-task`} component={() => <HomeView></HomeView>} />

        <PrivateRoute path={`${path}/dashboard`} component={() => <DashboardPage></DashboardPage>} />
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
        <PrivateRoute
          path={`${path}/home-pending-task/sms-payment-modal`}
          component={() => <PaymentForSummonModalSMSAndEmail></PaymentForSummonModalSMSAndEmail>}
        />
        <PrivateRoute
          path={`${path}/home-pending-task/email-payment-modal`}
          component={() => <PaymentForSummonModalSMSAndEmail></PaymentForSummonModalSMSAndEmail>}
        />
        <PrivateRoute path={`${path}/post-payment-screen`} component={() => <PaymentStatus></PaymentStatus>} />
        <PrivateRoute path={`${path}/view-hearing`} component={() => <ViewHearing></ViewHearing>} />
        <PrivateRoute path={`${path}/home-popup`} component={() => <HomePopUp></HomePopUp>} />
        <PrivateRoute path={`${path}/home-pending-task/home-schedule-hearing`} component={() => <ScheduleHearing />} />
        <PrivateRoute path={`${path}/home-pending-task/home-set-next-hearing`} component={() => <ScheduleNextHearing />} />
      </AppContainer>
    </Switch>
  );
};

export default App;
