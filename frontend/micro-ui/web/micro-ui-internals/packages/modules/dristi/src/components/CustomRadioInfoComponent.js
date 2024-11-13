import React from "react";
import { LabelFieldPair, CardLabel, CardLabelError, CustomDropdown, CardSectionHeader } from "@egovernments/digit-ui-react-components";
import SelectCustomNote from "./SelectCustomNote";

const extractValue = (data, key) => {
  if (!key.includes(".")) {
    return data[key];
  }
  const keyParts = key.split(".");
  let value = data;
  keyParts.forEach((part) => {
    if (value && value.hasOwnProperty(part)) {
      value = value[part];
    } else {
      value = undefined;
    }
  });
  return value;
};

const CustomRadioInfoComponent = ({ t, config, onSelect, formData = {}, errors, formState, control, setError }) => {
  function setValue(value, name) {
    onSelect(config.key, { ...value }, { shouldValidate: true });
  }

  return (
    <React.Fragment>
      {config.noteDependentOnValue
        ? extractValue(formData, config.noteDependentOn) === config.noteDependentOnValue && (
            <SelectCustomNote t={t} config={config.notes} onClick={() => {}} />
          )
        : extractValue(formData, config.noteDependentOn) && <SelectCustomNote t={t} config={config.notes} onClick={() => {}} />}
      <CardSectionHeader style={{ margin: "5px 0px" }}>{t(config.head)}</CardSectionHeader>
      <div className="select-user-type-component">
        <React.Fragment>
          <LabelFieldPair>
            {!config?.disableScrutinyHeader && (
              <CardLabel className="card-label-smaller" style={{ display: "flex", ...config?.labelStyles }}>
                {t(config.label)}
              </CardLabel>
            )}

            <div className="field">
              <CustomDropdown
                t={t}
                label={config?.populators?.label}
                type={"radio"}
                value={formData?.[config.key] || {}}
                onChange={(e) => {
                  setValue(e, config.name);
                }}
                config={config.populators}
                errorStyle={errors?.[config.name]}
                disable={config?.disable}
              />
              {errors[config.name] && <CardLabelError>{t(config.error)}</CardLabelError>}
            </div>
          </LabelFieldPair>
        </React.Fragment>
      </div>
    </React.Fragment>
  );
};

export default CustomRadioInfoComponent;
