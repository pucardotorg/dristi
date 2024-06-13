import {  EmployeeModuleCard, PropertyHouse } from "@egovernments/digit-ui-react-components";
import React from "react";
import { useTranslation } from "react-i18next";

const OrderCard = () => {
 
  const { t } = useTranslation();

  const propsForModuleCard = {
    Icon: <PropertyHouse />,
    moduleName: t("Order"),
    kpis: [

    ],
    links: [
   
      {
        label: t("Order  Search"),
        link: `/${window?.contextPath}/employee/order/order-search`,

      },
      {
        label: t("Order  Create"),
        link: `/${window?.contextPath}/employee/order/order-create`,

      },
    
      {
        label: t("Order Response"),
        link: `/${window?.contextPath}/employee/order/order-response`,

      },
      
    ],
  };

  return <EmployeeModuleCard {...propsForModuleCard} />;
};

export default OrderCard;