import { AppContainer } from "@egovernments/digit-ui-react-components";
import React from "react";
import { Route, Switch, useRouteMatch } from "react-router-dom/cjs/react-router-dom.min";
import CaseType from "./CaseType";
import RespondentDetails from "./RespondentDetails";
import DelayApplication from "./DelayApplication";
import ComplainantDetails from "./ComplainantDetails";
import EFilingCases from "./EFilingCases";
import WitnessDetails from "./WitnessDetails";

function FileCase({ t }) {
  const { path } = useRouteMatch();

  return (
    <div className="citizen-form-wrapper" style={{ minWidth: "100%" }}>
      <Switch>
        <AppContainer>
          <Route path={`${path}`} exact>
            <CaseType t={t} />
          </Route>
          {/* <Route path={`${path}/respondent-details`} exact>
            <RespondentDetails />
          </Route> */}
          <Route path={`${path}/delay-application`} exact>
            <DelayApplication />
          </Route>
          <Route path={`${path}/case`} exact>
            <EFilingCases t={t} />
          </Route>
          <Route path={`${path}/witness-details`} exact>
            <WitnessDetails />
          </Route>
        </AppContainer>
      </Switch>
    </div>
  );
}

export default FileCase;
