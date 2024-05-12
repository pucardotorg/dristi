import React, { useMemo, useState } from "react";
import { useTranslation } from "react-i18next";
import { FormComposerV2, Header, Toast } from "@egovernments/digit-ui-react-components";
import { useHistory } from "react-router-dom/cjs/react-router-dom.min";
import { respondentconfig } from "./respondentconfig";

function RespondentDetails({ path }) {
  const [params, setParmas] = useState({});

  const Digit = window?.Digit || {};
  const { t } = useTranslation();
  const history = useHistory();
  const [showErrorToast, setShowErrorToast] = useState(false);
  const [isDisabled, setIsDisabled] = useState(false);
  const [formDataVar, setFormData] = useState({});

  const validateFormData = (data) => {
    let isValid = true;
    respondentconfig.forEach((curr) => {
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

  const isDependentEnabled = useMemo(() => {
    let result = false;
    respondentconfig.forEach((config) => {
      if (config?.body && Array.isArray(config?.body)) {
        config?.body?.forEach((bodyItem) => {
          if (bodyItem?.populators?.isDependent) {
            result = true;
          }
        });
      }
    });
    return result;
  }, []);

  const modifiedConfig = useMemo(() => {
    if (!isDependentEnabled) {
      return respondentconfig;
    }
    return respondentconfig.filter((config) => {
      const dependentKeys = config?.dependentKey;
      if (!dependentKeys) {
        return config;
      }
      let show = true;
      for (const key in dependentKeys) {
        const nameArray = dependentKeys[key];
        for (const name of nameArray) {
          console.debug(formDataVar);
          console.debug(key);
          console.debug(name);
          console.debug(formDataVar?.[key]?.[name]);
          show = show && Boolean(formDataVar?.[key]?.[name]);
        }
      }
      return show && config;
    });
  }, [formDataVar, isDependentEnabled]);
  console.debug(formDataVar);
  console.debug(modifiedConfig);

  const onSubmit = (data) => {
    if (!validateFormData(data)) {
      setShowErrorToast(!validateFormData(data));
      return;
    }
    setParmas({ ...params, registrationData: data });
    history.push(`${path}/terms-conditions`);
  };

  const closeToast = () => {
    setShowErrorToast(false);
  };

  const onFormValueChange = (setValue, formData, formState, reset, setError, clearErrors, trigger, getValues) => {
    if (JSON.stringify(formData) !== JSON.stringify(formDataVar)) {
      setFormData(formData);
    }
  };

  return (
    <div className="employee-card-wrapper">
      <div className="header-content">
        <Header>{t("CS_COMMON_RESPONDENT_DETAIL")}</Header>
      </div>
      <FormComposerV2
        label={t("CS_COMMONS_NEXT")}
        config={modifiedConfig.map((config) => {
          return {
            ...config,
            body: config.body.filter((a) => !a.hideInEmployee),
          };
        })}
        onSubmit={(props) => {
          // onSubmit(props);
          console.debug("Vaibhav");
        }}
        defaultValues={params.registrationData || {}}
        onFormValueChange={onFormValueChange}
        cardStyle={{ minWidth: "100%" }}
        isDisabled={isDisabled}
      />
      {showErrorToast && <Toast error={true} label={t("ES_COMMON_PLEASE_ENTER_ALL_MANDATORY_FIELDS")} isDleteBtn={true} onClose={closeToast} />}
    </div>
  );
}

export default RespondentDetails;
