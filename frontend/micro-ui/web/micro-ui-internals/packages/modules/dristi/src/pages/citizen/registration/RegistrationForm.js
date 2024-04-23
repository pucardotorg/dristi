import React, { useState } from "react";
import { newConfig } from "./config";
import { FormComposerV2, Header, Toast } from "@egovernments/digit-ui-react-components";
import { useTranslation } from "react-i18next";
import { useHistory } from "react-router-dom";

const RegistrationForm = ({ setParams, params, path }) => {
  const { t } = useTranslation();
  const history = useHistory();
  const [showErrorToast, setShowErrorToast] = useState(false);

  const validateFormData = (data) => {
    let isValid = true;
    newConfig.forEach((curr) => {
      if (!isValid) return;
      if (!(curr.body[0].key in data) || !data[curr.body[0].key]) {
        isValid = false;
      }
      curr.body[0].populators.inputs.forEach((input) => {
        if (!isValid) return;
        if (Array.isArray(input.name)) return;
        if (
          input.isDependentOn &&
          data[curr.body[0].key][input.isDependentOn] &&
          !Boolean(
            input.dependentKey[input.isDependentOn].reduce((res, current) => {
              if (!res) return res;
              res = data[curr.body[0].key][input.isDependentOn][current];
              if (
                Array.isArray(data[curr.body[0].key][input.isDependentOn][current]) &&
                data[curr.body[0].key][input.isDependentOn][current].length === 0
              ) {
                res = false;
              }
              return res;
            }, true)
          )
        ) {
          return;
        }
        if (Array.isArray(data[curr.body[0].key][input.name]) && data[curr.body[0].key][input.name].length === 0) {
          isValid = false;
        }
        if (input?.isMandatory && !(input.name in data[curr.body[0].key])) {
          isValid = false;
        }
      });
    });
    return isValid;
  };

  const onSubmit = (data) => {
    console.log("data", data);
    if (!validateFormData(data)) {
      setShowErrorToast(!validateFormData(data));
      return;
    }
    setParams({ ...params, registrationData: data });
    history.push(`${path}/terms-conditions`);
  };

  const closeToast = () => {
    setShowErrorToast(false);
  };

  return (
    <div className="employee-card-wrapper">
      <div className="header-content">
        <Header>{t("CS_COMMON_REGISTRATION_DETAIL")}</Header>
      </div>
      <FormComposerV2
        label={t("CS_COMMONS_NEXT")}
        config={newConfig.map((config) => {
          return {
            ...config,
            body: config.body.filter((a) => !a.hideInEmployee),
          };
        })}
        onSubmit={(props) => {
          onSubmit(props);
        }}
        defaultValues={params.registrationData || {}}
        cardStyle={{ minWidth: "100%" }}
      />
      {showErrorToast && <Toast error={true} label={t("ES_COMMON_PLEASE_ENTER_ALL_MANDATORY_FIELDS")} isDleteBtn={true} onClose={closeToast} />}
    </div>
  );
};

export default RegistrationForm;
