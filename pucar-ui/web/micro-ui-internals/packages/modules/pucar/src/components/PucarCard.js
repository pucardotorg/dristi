import {  EmployeeModuleCard, PropertyHouse } from "@egovernments/digit-ui-react-components";
import React from "react";
import { useTranslation } from "react-i18next";

const PucarCard = () => {
 
  const { t } = useTranslation();

  const propsForModuleCard = {
    Icon: <PropertyHouse />,
    moduleName: t("Pucar"),
    kpis: [

    ],
    links: [
   
      {
        label: t("Pucar  Search"),
        link: `/${window?.contextPath}/employee/pucar/pucar-search`,

      },
      {
        label: t("Pucar  Create"),
        link: `/${window?.contextPath}/employee/pucar/pucar-create`,

      },
    
      {
        label: t("Pucar Response"),
        link: `/${window?.contextPath}/employee/pucar/pucar-response`,

      },
      
    ],
  };

  return <EmployeeModuleCard {...propsForModuleCard} />;
};

export default PucarCard;