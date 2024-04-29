import React from "react";
import { initLibraries } from "@egovernments/digit-ui-libraries";
import {
  DigitUI,
  initCoreComponents,
} from "@egovernments/digit-ui-module-core";
import { UICustomizations } from "./Customisations/UICustomizations";
import { initWorkbenchComponents } from "@egovernments/digit-ui-module-workbench";
import { initDRISTIComponents } from "@egovernments/digit-ui-module-dristi";

window.contextPath = window?.globalConfigs?.getConfig("CONTEXT_PATH");

const enabledModules = ["DRISTI"];

const moduleReducers = (initData) => ({
  initData,
});

const initDigitUI = () => {
  window.Digit.ComponentRegistryService.setupRegistry({});
  initCoreComponents();
  initWorkbenchComponents();
  initDRISTIComponents();

  window.Digit.Customizations = {
    PGR: {},
    commonUiConfig: UICustomizations,
  };
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
