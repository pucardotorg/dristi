import {  EmployeeModuleCard, PropertyHouse } from "@egovernments/digit-ui-react-components";
import React from "react";
import { useTranslation } from "react-i18next";

const HearingsCard = () => {

  const { t } = useTranslation();

  const propsForModuleCard = {
    Icon: <PropertyHouse />,
    moduleName: t("Hearings"),
    kpis: [

    ],
    links: [
      {
        label: t("Home"),
        link: `/${window?.contextPath}/employee/hearings/home`,
      },
      {
        label: t("Inside Hearing"),
        link: `/${window?.contextPath}/employee/hearings/inside-hearing`,
      },
      {
        label: t("ADMISSION HEARING"),
        link: `/${window?.contextPath}/employee/hearings/view-hearing`,
      },

    ],
  };

  return <div />;
  // return <EmployeeModuleCard {...propsForModuleCard} />;
};

export default HearingsCard;