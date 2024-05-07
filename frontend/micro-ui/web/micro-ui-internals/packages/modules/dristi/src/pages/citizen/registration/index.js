import React, { useState } from "react";
import { AppContainer, Loader } from "@egovernments/digit-ui-react-components";
import { Route, Switch, useHistory, useRouteMatch } from "react-router-dom";
import RegistrationForm from "./RegistrationForm";
import TermsConditions from "./TermsConditions";
import AdvocateClerkAdditionalDetail from "./AdvocateClerkAdditionalDetail";

const Registration = () => {
  const Digit = window.Digit || {};
  const tenantId = Digit.ULBService.getCurrentTenantId();
  const history = useHistory();
  const { path } = useRouteMatch();
  const [params, setParams] = useState({ registrationData: { userDetails: {} } });
  const token = window.localStorage.getItem("token");
  const isUserLoggedIn = Boolean(token);
  const moduleCode = "DRISTI";
  const userInfo = JSON.parse(window.localStorage.getItem("user-info"));
  const { data, isLoading } = Digit.Hooks.dristi.useGetIndividualUser(
    {
      Individual: {
        userUuid: [userInfo?.uuid],
      },
    },
    { tenantId, limit: 1000, offset: 0 },
    moduleCode,
    "",
    userInfo?.uuid && isUserLoggedIn
  );
  const individualId = data?.Individual?.[0]?.individualId;

  if (isLoading) {
    return <Loader />;
  }

  if (Boolean(individualId)) {
    history.push(`/${window?.contextPath}/citizen/dristi/home`);
  }

  return (
    <div className="citizen-form-wrapper" style={{ minWidth: "100%" }}>
      <Switch>
        <AppContainer>
          <Route exact path={`${path}`}>
            <RegistrationForm setParams={setParams} params={params} path={path} />
          </Route>
          <Route exact path={`${path}/additional-details`}>
            <AdvocateClerkAdditionalDetail setParams={setParams} params={params} />
          </Route>
          <Route exact path={`${path}/terms-conditions`}>
            <TermsConditions setParams={setParams} params={params} />
          </Route>
        </AppContainer>
      </Switch>
    </div>
  );
};

export default Registration;
