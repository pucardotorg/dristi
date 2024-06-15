import React from "react";
import { initLibraries } from "@egovernments/digit-ui-libraries";
import { DigitUI } from "@egovernments/digit-ui-module-core";
// import { initHRMSComponents } from "@egovernments/digit-ui-module-hrms";
import { UICustomizations } from "./Customisations/UICustomizations";
import { initUtilitiesComponents } from "@egovernments/digit-ui-module-utilities";
import {initSampleComponents} from "@egovernments/digit-ui-module-sample";
import {initPucarComponents} from "@egovernments/digit-ui-module-pucar";
import {initHearingsComponents} from "@egovernments/digit-ui-module-hearings";
import {initOrdersComponents} from "@egovernments/digit-ui-module-orders";
import {initCasesComponents} from "@egovernments/digit-ui-module-cases";
import { initSubmissionsComponents } from "@egovernments/digit-ui-module-submissions";

window.contextPath = window?.globalConfigs?.getConfig("CONTEXT_PATH");

const enabledModules = [
  "DSS",
  "NDSS",
  "Utilities",
  // "HRMS",
  "Engagement",
  "Workbench",
  "HCMWORKBENCH",
  "sample",
  "Orders",
  "Cases",
  // "Pucar",
  "Hearings",
  "Submissions"

];

const moduleReducers = (initData) => ({
  initData,
});

const initDigitUI = () => {
  window.Digit.ComponentRegistryService.setupRegistry({});
  window.Digit.Customizations = {
    PGR: {},
    commonUiConfig: UICustomizations,
  };
  // initHRMSComponents();
  initUtilitiesComponents();
  initSampleComponents();
  initSampleComponents();
  initPucarComponents();
  initHearingsComponents();
  initOrdersComponents();
  initCasesComponents();
  initSubmissionsComponents();

};

initLibraries().then(() => {
  initDigitUI();
});

function App() {
  window.contextPath = window?.globalConfigs?.getConfig("CONTEXT_PATH");
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
      defaultLanding="employee"
    />
  );
}

export default App;
