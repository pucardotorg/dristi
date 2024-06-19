import { CardText, CheckBox } from "@egovernments/digit-ui-react-components";
import React from "react";

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
  return (
    <div style={{ display: "flex", flexDirection: "column", marginLeft: "20px" }}>
      <CardText>{t(option.dependentText)}</CardText>
      {option.dependentFields.map((field) => (
        <div key={field}>
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
      <div style={{ display: "flex", flexDirection: "row", justifyContent: "space-evenly" }}>
        {options?.checkBoxes?.map((option) => (
          <div key={option?.name} style={{ display: "flex", flexDirection: "column", alignItems: "center" }}>
            <CheckboxItem t={t} name={t(option?.name)} checked={!!selectedValues[option?.name]} onToggle={() => toggleCheckbox(option?.name)} />
            {selectedValues[option?.name] && (
              <DependentFields t={t} option={option} selectedValues={selectedValues[option?.name]} handleInputChange={handleInputChange} />
            )}
          </div>
        ))}
      </div>
    </div>
  );
}

export default DependentCheckBoxComponent;
