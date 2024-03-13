import { HRIcon, EmployeeModuleCard, AttendanceIcon, PropertyHouse } from "@egovernments/digit-ui-react-components";
import React from "react";
import { useTranslation } from "react-i18next";

const SampleCard = () => {
 
  const { t } = useTranslation();

  const propsForModuleCard = {
    Icon: <PropertyHouse />,
    moduleName: t("Sample"),
    kpis: [

    ],
    links: [
   
     
      {
        label: t("Individual Search"),
        link: `/${window?.contextPath}/employee/sample/search-individual`,

      },
      {
        label: t("Individual Create"),
        link: `/${window?.contextPath}/employee/sample/create-individual`,

      },
      {
        label: t("Sample Create"),
        link: `/${window?.contextPath}/employee/sample/sample-create`,

      },
      {
        label: t("Sample Search"),
        link: `/${window?.contextPath}/employee/sample/sample-search`,

      },
      {
        label: t("Sample View"),
        link: `/${window?.contextPath}/employee/sample/sample-view?tenantId=ddd&estimateNumber=ssss`,

      },
      
    ],
  };

  return <EmployeeModuleCard {...propsForModuleCard} />;
};

export default SampleCard;