import { EmployeeModuleCard, PropertyHouse } from "@egovernments/digit-ui-react-components";
import React from "react";
import { useTranslation } from "react-i18next";

const CasesCard = () => {

  const { t } = useTranslation();

  const propsForModuleCard = {
    Icon: <PropertyHouse />,
    moduleName: t("Cases"),
    kpis: [

    ],
    links: [
      {
        label: t("Join Case"),
        link: `/${window?.contextPath}/employee/cases/join-case`,
      },
      {
        label: t("Join Case litigant"),
        link: `/${window?.contextPath}/employee/cases/join-case-litigant`,
      },
      {
        label: t("Advocate vakalath"),
        link: `/${window?.contextPath}/employee/cases/advocate-vakalath`,
      },
      {
        label: t("Advocate payment"),
        link: `/${window?.contextPath}/employee/cases/advocate-payment`,
      },
      {
        label: t("Advocate join-case"),
        link: `/${window?.contextPath}/employee/cases/advocate-join-case`,
      }
    
      
    ],
  };

  return <div />;
  // return <EmployeeModuleCard {...propsForModuleCard} />;
};

export default CasesCard;