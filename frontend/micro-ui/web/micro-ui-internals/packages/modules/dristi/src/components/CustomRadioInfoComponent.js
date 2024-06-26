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
    onSelect(config.key, { ...value });
  }

  return (
    <React.Fragment>
      {extractValue(formData, config.noteDependentOn) && <SelectCustomNote t={t} config={config.notes} onClick={() => {}} />}
      <CardSectionHeader style={{ margin: "5px 0px" }}>{t(config.head)}</CardSectionHeader>
      <div className="select-user-type-component">
        <React.Fragment>
          <LabelFieldPair>
            <CardLabel className="card-label-smaller" style={{ display: "flex" }}>
              {t(config.label)}
            </CardLabel>

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
