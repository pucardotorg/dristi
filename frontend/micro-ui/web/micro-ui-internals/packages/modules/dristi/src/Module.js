import { Loader } from "@egovernments/digit-ui-react-components";
import React from "react";
import { useRouteMatch } from "react-router-dom";
import AddressComponent from "./components/AddressComponent";
import SelectComponents from "./components/SelectComponents";

import SelectUserTypeComponent from "./components/SelectUserTypeComponent";
import CustomRadioCard from "./components/CustomRadioCard";
import AdhaarInput from "./components/AdhaarInput";
import AdvocateDetailComponent from "./components/AdvocateDetailComponent";

import Registration from "./pages/citizen/registration";
import EmployeeApp from "./pages/employee";
import CitizenApp from "./pages/citizen";

import CustomInput from "./components/CustomInput";
import DRISTICard from "./components/DRISTICard";
import IdProofUploadComponent from "./components/IdProofUploadComponent";
import SelectBulkInputs from "./components/SelectBulkInputs";
import SelectComponentsMulti from "./components/SelectComponentsMulti";
import SelectCustomDragDrop from "./components/SelectCustomDragDrop";
import SelectCustomNote from "./components/SelectCustomNote";
import SelectCustomTextArea from "./components/SelectCustomTextArea";
import SelectReviewAccordion from "./components/SelectReviewAccordion";
import SelectSignature from "./components/SelectSignature";
import SelectUploadDocWithName from "./components/SelectUploadDocWithName";
import SelectUploadFiles from "./components/SelectUploadFiles";
import { ToastProvider } from "./components/Toast/useToast";
import VerificationComponent from "./components/VerificationComponent";
import VerifyPhoneNumber from "./components/VerifyPhoneNumber";
import { UICustomizations } from "./configs/UICustomizations";
import SelectEmptyComponent from "./components/SelectEmptyComponent";
import ScrutinyInfo from "./components/ScrutinyInfo";
import AdvocateNameDetails from "./components/AdvocateNameDetails";
import { CustomizedHooks } from "./hooks";
import FileCase from "./pages/citizen/FileCase";
import Login from "./pages/citizen/Login";
import AdvocateClerkAdditionalDetail from "./pages/citizen/registration/AdvocateClerkAdditionalDetail";
import CitizenResponse from "./pages/citizen/registration/Response";
import Inbox from "./pages/employee/Inbox";
import { useHistory } from "react-router-dom/cjs/react-router-dom.min";
import CustomRadioInfoComponent from "./components/CustomRadioInfoComponent";
import Modal from "./components/Modal";
import CustomCaseInfoDiv from "./components/CustomCaseInfoDiv";
import DocViewerWrapper from "./pages/employee/docViewerWrapper";
const Digit = window?.Digit || {};

export const DRISTIModule = ({ stateCode, userType, tenants }) => {
  const { path } = useRouteMatch();
  const history = useHistory();
  const moduleCode = ["DRISTI", "CASE"];
  const tenantID = tenants?.[0]?.code?.split(".")?.[0];
  const language = Digit.StoreData.getCurrentLanguage();
  const { isLoading } = Digit.Services.useStore({ stateCode, moduleCode, language });
  const userInfo = JSON.parse(window.localStorage.getItem("user-info"));
  if (isLoading) {
    return <Loader />;
  }
  Digit.SessionStorage.set("DRISTI_TENANTS", tenants);

  if (userType === "citizen" && userInfo?.type !== "EMPLOYEE") {
    return (
      <ToastProvider>
        <CitizenApp path={path} stateCode={stateCode} userType={userType} tenants={tenants} tenantId={tenantID} />
      </ToastProvider>
    );
  }
  if (path?.includes(`/${window?.contextPath}/citizen`)) {
    history.push(`/${window?.contextPath}/employee`);
  }
  return (
    <ToastProvider>
      <EmployeeApp path={path} stateCode={stateCode} userType={userType} tenants={tenants}></EmployeeApp>
    </ToastProvider>
  );
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
  SelectEmptyComponent,
  ScrutinyInfo,
  AdvocateNameDetails,
  CustomRadioInfoComponent,
  MODAL: Modal,
  CUSTOMCASEINFODIV: CustomCaseInfoDiv,
  DOCVIEWERWRAPPER: DocViewerWrapper,
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
