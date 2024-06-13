import {  EmployeeModuleCard, PropertyHouse } from "@egovernments/digit-ui-react-components";
import React from "react";
import { useTranslation } from "react-i18next";

const HearingCard = () => {
 
  const { t } = useTranslation();

  const propsForModuleCard = {
    Icon: <PropertyHouse />,
    moduleName: t("Hearing"),
    kpis: [

    ],
    links: [
   
      {
        label: t("Hearing  Search"),
        link: `/${window?.contextPath}/employee/hearing/hearing-search`,

      },
      {
        label: t("Hearing  Create"),
        link: `/${window?.contextPath}/employee/hearing/hearing-create`,

      },
    
      {
        label: t("Hearing Response"),
        link: `/${window?.contextPath}/employee/hearing/hearing-response`,

      },
      
    ],
  };

  return <EmployeeModuleCard {...propsForModuleCard} />;
};

export default HearingCard;