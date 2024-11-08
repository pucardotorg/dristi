import React from "react";
import { initLibraries } from "@egovernments/digit-ui-libraries";
import {
  DigitUI,
  initCoreComponents,
} from "@egovernments/digit-ui-module-core";
import { UICustomizations } from "./Customisations/UICustomizations";
import { initDRISTIComponents } from "@egovernments/digit-ui-module-dristi";
import { initOrdersComponents } from "@egovernments/digit-ui-module-orders";
import { initSubmissionsComponents } from "@egovernments/digit-ui-module-submissions";
import { initHearingsComponents } from "@egovernments/digit-ui-module-hearings";
import { initCasesComponents } from "@egovernments/digit-ui-module-cases";
import { initHomeComponents } from "@egovernments/digit-ui-module-home";

import "@egovernments/dristi-ui-css/dist/index.min.css";

window.contextPath =
  window?.globalConfigs?.getConfig("CONTEXT_PATH") || "digit-ui";

const enabledModules = [
  "DRISTI",
  "Submissions",
  "Orders",
  "Hearings",
  "Cases",
  "Home",
];

const moduleReducers = (initData) => ({
  initData,
});

const initDigitUI = () => {
  window.Digit.ComponentRegistryService.setupRegistry({});
  initCoreComponents();
  initDRISTIComponents();
  initOrdersComponents();
  initHearingsComponents();
  initCasesComponents();
  initSubmissionsComponents();
  initHomeComponents();
};

initLibraries().then(() => {
  initDigitUI();
});

function App() {
  window.contextPath =
    window?.globalConfigs?.getConfig("CONTEXT_PATH") || "digit-ui";
  const stateCode =
    window.globalConfigs?.getConfig("STATE_LEVEL_TENANT_ID") ||
    process.env.REACT_APP_STATE_LEVEL_TENANT_ID;
  if (!stateCode) {
    return <h1>stateCode is not defined</h1>;
  }
  return (
    <DigitUI
      stateCode={stateCode}
      enabledModules={enabledModules}
      moduleReducers={moduleReducers}
      // defaultLanding="employee"
    />
  );
}

export default App;
