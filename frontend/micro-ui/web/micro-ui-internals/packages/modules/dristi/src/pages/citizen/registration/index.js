import React from "react";
import { newConfig } from "./config";
import { FormComposerV2, Header } from "@egovernments/digit-ui-react-components";
import { useTranslation } from "react-i18next";

const Registration = () => {
  const { t } = useTranslation();
  return (
    <div className="employee-card-wrapper">
      <div className="header-content">
        <Header>{t("CS_COMMON_REGISTRATION_DETAIL")}</Header>
      </div>
      <FormComposerV2
        label={t("CS_COMMON_SUBMIT")}
        config={newConfig.map((config) => {
          return {
            ...config,
            body: config.body.filter((a) => !a.hideInEmployee),
          };
        })}
        onSubmit={(props) => {
          console.debug(props);
        }}
      />
    </div>
  );
};

export default Registration;
