import { Loader } from "@egovernments/digit-ui-react-components";
import React from "react";
import { useRouteMatch } from "react-router-dom";
import OrdersCard from "./components/OrdersCard";
import DeliveryChannels from "./pageComponents/DeliveryChannels";
import { default as EmployeeApp } from "./pages/employee";
import { overrideHooks, updateCustomConfigs } from "./utils";
import { OrderWorkflowAction, OrderWorkflowState } from "./utils/orderWorkflow";
import { ordersService } from "./hooks/services";
import OrderReviewModal from "./pageComponents/OrderReviewModal";
import AddSubmissionDocument from "./components/AddSubmissionDocument";
import CustomInfo from "./components/CustomInfo";
import SummonsOrderComponent from "./components/SummonsOrderComponent";
import ReIssueSummonsModal from "./components/ReIssueSummonsModal";
import PaymentForSummonModal from "./pages/employee/PaymentForSummonModal";
import PaymentForSummonModalSMSAndEmail from "./pages/employee/PaymentForSummonModalSMSAndEmail";
import SBIEpostPayment from "./pages/employee/SBIEpostPayment";

export const OrdersModule = ({ stateCode, userType, tenants }) => {
  const { path } = useRouteMatch();
  const moduleCode = ["orders", "hearings", "common", "case", "workflow"];
  const language = Digit.StoreData.getCurrentLanguage();
  const { isLoading } = Digit.Services.useStore({
    stateCode,
    moduleCode,
    language,
  });

  if (isLoading) {
    return <Loader />;
  }
  return <EmployeeApp path={path} stateCode={stateCode} userType={userType} tenants={tenants} />;
};

const componentsToRegister = {
  OrdersModule,
  OrdersCard,
  DeliveryChannels,
  OrderWorkflowActionEnum: OrderWorkflowAction,
  OrderWorkflowStateEnum: OrderWorkflowState,
  OrdersService: ordersService,
  OrderReviewModal,
  AddSubmissionDocument,
  CustomInfo,
  SummonsOrderComponent,
  ReIssueSummonsModal,
  PaymentForSummonModal,
  PaymentForSummonModalSMSAndEmail,
  SBIEpostPayment,
};

export const initOrdersComponents = () => {
  overrideHooks();
  updateCustomConfigs();
  Object.entries(componentsToRegister).forEach(([key, value]) => {
    Digit.ComponentRegistryService.setComponent(key, value);
  });
};
