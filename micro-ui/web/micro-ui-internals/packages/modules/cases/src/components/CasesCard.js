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

      }
    ],
  };

  return <EmployeeModuleCard {...propsForModuleCard} />;
};

export default CasesCard;