import { Loader } from "@egovernments/digit-ui-react-components";
import React from "react";
import { useRouteMatch } from "react-router-dom";
import CitizenApp from "./pages/citizen";
import SelectComponents from "./components/SelectComponents";
import AddressComponent from "./components/AddressComponent";

import SelectUserTypeComponent from "./components/SelectUserTypeComponent";
import CustomRadioCard from "./components/CustomRadioCard";
import AdhaarInput from "./components/AdhaarInput";
import AdvocateDetailComponent from "./components/AdvocateDetailComponent";

import Registration from "./pages/citizen/registration";
import EmployeeApp from "./pages/employee";
import DRISTICard from "./components/DRISTICard";
import Inbox from "./pages/employee/Inbox";
import Login from "./pages/citizen/Login";
import CitizenResponse from "./pages/citizen/registration/Response";
import AdvocateClerkAdditionalDetail from "./pages/citizen/registration/AdvocateClerkAdditionalDetail";
import FileCase from "./pages/citizen/FileCase";
import { CustomizedHooks } from "./hooks";
import { UICustomizations } from "./configs/UICustomizations";
import VerificationComponent from "./components/VerificationComponent";
import CustomInput from "./components/CustomInput";
import SelectBulkInputs from "./components/SelectBulkInputs";
import SelectCustomNote from "./components/SelectCustomNote";
import SelectCustomDragDrop from "./components/SelectCustomDragDrop";
import VerifyPhoneNumber from "./components/VerifyPhoneNumber";
import SelectCustomTextArea from "./components/SelectCustomTextArea";
import IdProofUploadComponent from "./components/IdProofUploadComponent";
import SelectComponentsMulti from "./components/SelectComponentsMulti";
import SelectReviewAccordion from "./components/SelectReviewAccordion";
import SelectSignature from "./components/SelectSignature";
import SelectUploadFiles from "./components/SelectUploadFiles";
import SelectUploadDocWithName from "./components/SelectUploadDocWithName";
import AdvocateNameDetails from "./components/AdvocateNameDetails";
const Digit = window?.Digit || {};

export const DRISTIModule = ({ stateCode, userType, tenants }) => {
  const { path } = useRouteMatch();
  const moduleCode = "DRISTI";
  const tenantID = tenants?.[0]?.code?.split(".")?.[0];
  const language = Digit.StoreData.getCurrentLanguage();
  const { isLoading } = Digit.Services.useStore({ stateCode, moduleCode, language });
  if (isLoading) {
    return <Loader />;
  }
  Digit.SessionStorage.set("DRISTI_TENANTS", tenants);

  if (userType === "citizen") {
    return <CitizenApp path={path} stateCode={stateCode} userType={userType} tenants={tenants} tenantId={tenantID} />;
  }
  return <EmployeeApp path={path} stateCode={stateCode} userType={userType} tenants={tenants}></EmployeeApp>;
};

const componentsToRegister = {
  SelectComponents,
  SelectComponentsMulti,
  SelectUserTypeComponent,
  DRISTIModule,
  DRISTIRegistration: Registration,
  DRISTICard,
  Inbox,
  DRISTILogin: Login,
  DRISTICitizenResponse: CitizenResponse,
  AdvocateClerkAdditionalDetail,
  FileCase,
  VerificationComponent,
  CustomInput,
  SelectBulkInputs,
  SelectCustomNote,
  SelectCustomDragDrop,
  VerifyPhoneNumber,
  SelectCustomTextArea,
  IdProofUploadComponent,
  SelectReviewAccordion,
  SelectSignature,
  CustomRadioCard,
  AddressComponent,
  AdhaarInput,
  AdvocateDetailComponent,
  SelectUploadFiles,
  SelectUploadDocWithName,
  AdvocateNameDetails,
};

const overrideHooks = () => {
  Object.keys(CustomizedHooks).forEach((ele) => {
    if (ele === "Hooks") {
      Object.keys(CustomizedHooks[ele]).forEach((hook) => {
        Object.keys(CustomizedHooks[ele][hook]).forEach((method) => {
          setupHooks(hook, method, CustomizedHooks[ele][hook][method]);
        });
      });
    } else if (ele === "Utils") {
      Object.keys(CustomizedHooks[ele]).forEach((hook) => {
        Object.keys(CustomizedHooks[ele][hook]).forEach((method) => {
          setupHooks(hook, method, CustomizedHooks[ele][hook][method], false);
        });
      });
    } else {
      Object.keys(CustomizedHooks[ele]).forEach((method) => {
        setupLibraries(ele, method, CustomizedHooks[ele][method]);
      });
    }
  });
};

/* To Overide any existing hook we need to use similar method */
const setupHooks = (HookName, HookFunction, method, isHook = true) => {
  window.Digit = window.Digit || {};
  window.Digit[isHook ? "Hooks" : "Utils"] = window.Digit[isHook ? "Hooks" : "Utils"] || {};
  window.Digit[isHook ? "Hooks" : "Utils"][HookName] = window.Digit[isHook ? "Hooks" : "Utils"][HookName] || {};
  window.Digit[isHook ? "Hooks" : "Utils"][HookName][HookFunction] = method;
};
/* To Overide any existing libraries  we need to use similar method */
const setupLibraries = (Library, service, method) => {
  window.Digit = window.Digit || {};
  window.Digit[Library] = window.Digit[Library] || {};
  window.Digit[Library][service] = method;
};

/* To Overide any existing config/middlewares  we need to use similar method */
const updateCustomConfigs = () => {
  setupLibraries("Customizations", "commonUiConfig", { ...window?.Digit?.Customizations?.commonUiConfig, ...UICustomizations });
};

export const initDRISTIComponents = () => {
  overrideHooks();
  updateCustomConfigs();
  Object.entries(componentsToRegister).forEach(([key, value]) => {
    Digit.ComponentRegistryService.setComponent(key, value);
  });
};
