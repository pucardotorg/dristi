import { AppContainer, BreadCrumb, PrivateRoute } from "@egovernments/digit-ui-react-components";
import React from "react";
import { useTranslation } from "react-i18next";
import { Switch } from "react-router-dom";
import CasesResponse from "./CasesResponse";
import JoinCaseHome from "./JoinCaseHome";
import AdvocateRegistration from "./advocateRegistration";
const bredCrumbStyle = { maxWidth: "min-content" };
import SearchCase from "./SearchCase";
import AdvocateMain from "../advocate/AdvocateMain";
import Vakalath from "../advocate/Vakalath";
import AdvocateEsign from "../advocate/AdvocateEsign";
import AdvocatePayment from "../advocate/AdovactePayment";
import AdvocateJoinCase from "../advocate/AdvocateJoinCase";
import AdvocateJoinSucess from "../advocate/AdvocateJoinSucess";
import CaseAndFilingSearch from "./CaseAndFilingSearch";
import LitigantSucess from "./LitigantSuccess";

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
  const { t } = useTranslation();
  return (
    <Switch>
      <AppContainer className="ground-container">
        <React.Fragment>
          <ProjectBreadCrumb location={location} />
        </React.Fragment>
        <PrivateRoute path={`${path}/cases-response`} component={() => <CasesResponse></CasesResponse>} />
        <PrivateRoute path={`${path}/join-case`} component={() => <JoinCaseHome t={t} />} />
        <PrivateRoute path={`${path}/join-case-litigant`} component={() => <AdvocateRegistration></AdvocateRegistration>} />
        <PrivateRoute path={`${path}/search-case`} component={() => <SearchCase />} />
        <PrivateRoute path={`${path}/join-case-advocate`} component={() => <AdvocateMain />} />
        <PrivateRoute path={`${path}/advocate-vakalath`} component={() => <Vakalath />} />
        <PrivateRoute path={`${path}/advocate-esign`} component={() => <AdvocateEsign />} />
        <PrivateRoute path={`${path}/advocate-payment`} component={() => <AdvocatePayment />} />
        <PrivateRoute path={`${path}/advocate-join-case`} component={() => <AdvocateJoinCase />} />
        <PrivateRoute path={`${path}/advocate-join-success`} component={() => <AdvocateJoinSucess />} />
        <PrivateRoute path={`${path}/case-filing-search`} component={() => <CaseAndFilingSearch></CaseAndFilingSearch>} />
        <PrivateRoute path={`${path}/litigant-success`} component={() => <LitigantSucess></LitigantSucess>} />
      </AppContainer >
    </Switch >
  );
};

export default App;
