import { EmployeeModuleCard, PropertyHouse } from "@egovernments/digit-ui-react-components";
import React from "react";
import { useTranslation } from "react-i18next";

const HomeCard = () => {
  const { t } = useTranslation();

  const propsForModuleCard = {
    Icon: <PropertyHouse />,
    moduleName: t("Hearings"),
    kpis: [],
    links: [
      {
        label: t("Home"),
        link: `/${window?.contextPath}/employee/home/home-pending-task`,
      },
      {
        label: t("Inside Hearing"),
        link: `/${window?.contextPath}/employee/home/inside-hearing`,
      },
      {
        label: t("ADMISSION HEARING"),
        link: `/${window?.contextPath}/employee/home/view-hearing`,
      },
    ],
  };

  return <div />;
  // return <EmployeeModuleCard {...propsForModuleCard} />;
};

export default HomeCard;
