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
import SummonsOrderComponent from "./components/SummonsOrderComponent";

export const OrdersModule = ({ stateCode, userType, tenants }) => {
  const { path } = useRouteMatch();
  const moduleCode = ["orders", "common", "workflow"];
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
  SummonsOrderComponent,
  OrderWorkflowActionEnum: OrderWorkflowAction,
  OrderWorkflowStateEnum: OrderWorkflowState,
  OrdersService: ordersService,
  OrderReviewModal,
};

export const initOrdersComponents = () => {
  overrideHooks();
  updateCustomConfigs();
  Object.entries(componentsToRegister).forEach(([key, value]) => {
    Digit.ComponentRegistryService.setComponent(key, value);
  });
};
