import { CardText, CheckBox } from "@egovernments/digit-ui-react-components";
import React, { Fragment } from "react";

function CheckboxItem({ name, checked, onToggle, t }) {
  return (
    <CheckBox
      onChange={onToggle}
      checked={checked}
      value={t(name)}
      label={name}
      name={"Checkbox"}
      styles={{ alignItems: "center", textAlign: "center" }}
    />
  );
}

function DependentFields({ t, option, selectedValues, handleInputChange }) {
  console.log("option1", option);
  return (
    <div style={{ display: "flex", flexDirection: "column", margin: "10px 0px" }}>
      <span style={{ display: "flex", flexDirection: "row", gap: "5px", alignItems: "center" }}>
        <h2>{t("SELECT_ONE_OR_MORE")}</h2>
        <span style={{ fontWeight: "700" }}>{t(option.dependentText)}</span>
      </span>
      <div style={{ display: "flex", flexDirection: "row", gap: "30px", maxHeight: "40px" }}>
        {option?.dependentFields.map((field) => (
          <div key={field} style={{ display: "flex", flexDirection: "row", gap: "40px", justifyContent: "flex-start" }}>
            <label>
              <CheckBox
                value={field}
                label={field}
                name={"Checkbox"}
                checked={selectedValues?.[field] || false}
                onChange={(e) => handleInputChange(e, option?.name)}
                styles={{ alignItems: "center", textAlign: "center" }}
              />
            </label>
          </div>
        ))}
      </div>
    </div>
  );
}

function DependentCheckBoxComponent({ t, options, onInputChange, selectedValues }) {
  const toggleCheckbox = (option) => {
    const updatedValues = {
      ...selectedValues,
      [option]: selectedValues[option] ? null : {},
    };
    onInputChange(updatedValues);
  };

  const handleInputChange = (e, option) => {
    const { value, checked } = e.target;
    const newSelectedValues = {
      ...selectedValues,
      [option]: { ...selectedValues[option], [value]: checked },
    };
    onInputChange(newSelectedValues);
  };

  return (
    <div className="select-checkbox-dependent">
      <div className="select-checkbox-dependent-child">
        <div className="select-between-compl-or-resp" style={{ display: "flex", flexDirection: "row", gap: "30px", maxHeight: "40px" }}>
          {options?.checkBoxes?.map((option) => (
            <div key={option?.name} style={{ display: "flex", flexDirection: "column", alignItems: "center" }}>
              <CheckboxItem t={t} name={t(option?.name)} checked={!!selectedValues[option?.name]} onToggle={() => toggleCheckbox(option?.name)} />
            </div>
          ))}
        </div>
        <div className="compl-resp-combined-div">
          {options?.checkBoxes?.map((option) => (
            <Fragment>
              {selectedValues[option?.name] && (
                <DependentFields t={t} option={option} selectedValues={selectedValues[option?.name]} handleInputChange={handleInputChange} />
              )}
            </Fragment>
          ))}
        </div>
      </div>
    </div>
  );
}

export default DependentCheckBoxComponent;
