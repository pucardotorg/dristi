import { AppContainer } from "@egovernments/digit-ui-react-components";
import React from "react";
import { Route, Switch, useRouteMatch } from "react-router-dom/cjs/react-router-dom.min";

function FileCase({ t }) {
  const { path } = useRouteMatch();

  return (
    <div className="citizen-form-wrapper" style={{ minWidth: "100%" }}>
      <Switch>
        <AppContainer>
          <Route path={`${path}`} exact>
            {/* <CaseType t={t} /> */}
          </Route>
        </AppContainer>
      </Switch>
    </div>
  );
}

export default FileCase;
