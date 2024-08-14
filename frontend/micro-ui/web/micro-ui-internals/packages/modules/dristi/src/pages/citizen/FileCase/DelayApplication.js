import React, { useMemo, useState } from "react";
// import { delayAppConfig } from "./delayAppConfig";
import { useTranslation } from "react-i18next";
import { useHistory } from "react-router-dom/cjs/react-router-dom.min";
import { FormComposerV2, Toast } from "@egovernments/digit-ui-react-components";
import { mainConfig } from "./delayAppConfig";
import { configRadioOptions } from "./delayAppConfig";

function DelayApplication() {
  const Digit = window?.Digit || {};
  const { t } = useTranslation();
  const history = useHistory();
  const [showErrorToast, setShowErrorToast] = useState(false);
  const [isDisabled, setIsDisabled] = useState(false);
  const [params, setParmas] = useState({ isEligible: true });
  const [selectedConfigName, setSelectedConfigName] = useState("delayAppConfig");

  const validateFormData = (data) => {
    let isValid = true;
    mainConfig
      .filter((config) => config.configName === selectedConfigName)
      ?.value.forEach((curr) => {
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
    if (!validateFormData(data)) {
      setShowErrorToast(!validateFormData(data));
      return;
    }
    setParmas({ ...params, registrationData: data });
    // history.push(`${path}/`);  update this later.
  };

  const closeToast = () => {
    setShowErrorToast(false);
  };

  const setEligibility = (formData) => {
    const ifEligible = formData?.eligibleForDelayApplicationType?.selectEligibilityType.code === "NO" ? true : false;
    if (ifEligible !== params?.isEligible) {
      setParmas({ isEligible: ifEligible });
    }
  };

  const onFormValueChange = (setValue, formData, formState) => {
    let isDisabled = false;
    mainConfig
      .filter((config) => config.configName === selectedConfigName)[0]
      ?.value.forEach((curr) => {
        if (isDisabled) return;
        if (!(curr.body[0].key in formData) || !formData[curr.body[0].key]) {
          return;
        }
        curr.body[0].populators.inputs.forEach((input) => {
          if (isDisabled) return;
          if (Array.isArray(input.name)) return;
          if (
            formData[curr.body[0].key][input.name] &&
            formData[curr.body[0].key][input.name].length > 0 &&
            !["documentUpload", "radioButton"].includes(input.type) &&
            input.validation &&
            !formData[curr.body[0].key][input.name].match(Digit.Utils.getPattern(input.validation.patternType) || input.validation.pattern)
          ) {
            isDisabled = true;
          }
          if (Array.isArray(formData[curr.body[0].key][input.name]) && formData[curr.body[0].key][input.name].length === 0) {
            isDisabled = true;
          }
        });
      });
    if (isDisabled) {
      setIsDisabled(isDisabled);
    } else {
      setIsDisabled(false);
    }
    setEligibility(formData);
  };

  const getModifiedConfig = () => {
    const currentConfig = mainConfig.filter((config) => config?.configName === selectedConfigName)[0];
    if (currentConfig?.configName === "delayAppConfig") {
      if (params?.isEligible === true) {
        return currentConfig?.value;
      } else {
        return currentConfig?.value.slice(0, 1);
      }
    }
    return currentConfig?.value;
  };

  const getConfig = useMemo(
    () =>
      getModifiedConfig().map((config) => {
        return {
          ...config,
          body: config.body.filter((a) => !a.hideInEmployee),
        };
      }),
    [selectedConfigName, params?.isEligible]
  );

  const handleOptionChange = (event) => {
    setSelectedConfigName(event.target.value);
  };
  return (
    <div>
      {configRadioOptions.map((option) => (
        <label key={option.name}>
          <input type="radio" value={option?.configName} checked={selectedConfigName === option?.configName} onChange={handleOptionChange} />
          {option?.name}
        </label>
      ))}
      {
        <FormComposerV2
          config={getConfig}
          onSubmit={(props) => {
            // onSubmit(props);
          }}
          defaultValues={params.registrationData || {}}
          onFormValueChange={onFormValueChange}
          // cardStyle={{ minWidth: "100%" }}
          isDisabled={isDisabled}
        />
      }
      {showErrorToast && <Toast error={true} label={t("ES_COMMON_PLEASE_ENTER_ALL_MANDATORY_FIELDS")} isDleteBtn={true} onClose={closeToast} />}
    </div>
  );
}

export default DelayApplication;
