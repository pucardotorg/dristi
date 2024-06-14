import { CardText, CheckBox, CollapseAndExpandGroups } from "@egovernments/digit-ui-react-components";
import React, { useEffect, useState } from "react";
function CheckboxItem({ name, checked, onToggle }) {
  return (
    <CheckBox
      onChange={onToggle}
      checked={checked}
      value={name}
      label={name}
      name={"Checkbox"}
      styles={{ alignItems: "center", textAlign: "center" }}
    />
  );
}
function DependentFields({ option, selectedValues, handleInputChange }) {
  return (
    <div style={{ display: "flex", flexDirection: "column", marginLeft: "20px" }}>
      <CardText>{option.dependentText}</CardText>
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
function DependentCheckBoxComponent({ options, onInputChange, selectedValues }) {
  const [selectedCheckboxes, setSelectedCheckboxes] = useState({});

  useEffect(() => {
    const initialSelectedCheckboxes = {};
    Object.keys(selectedValues).forEach((key) => {
      if (selectedValues[key] && Object.keys(selectedValues[key]).length > 0) {
        initialSelectedCheckboxes[key] = true;
      }
    });
    setSelectedCheckboxes(initialSelectedCheckboxes);
  }, [selectedValues]);

  const toggleCheckbox = (option) => {
    setSelectedCheckboxes((prevState) => ({
      ...prevState,
      [option]: !prevState[option],
    }));
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
      {options?.checkBoxText && <CardText>{options?.checkBoxText}</CardText>}
      <div style={{ display: "flex", flexDirection: "row", justifyContent: "space-evenly" }}>
        {options?.checkBoxes?.map((option) => (
          <div key={option?.name} style={{ display: "flex", flexDirection: "column", alignItems: "center" }}>
            <CheckboxItem name={option?.name} checked={selectedCheckboxes[option?.name] || false} onToggle={() => toggleCheckbox(option?.name)} />
            {selectedCheckboxes[option?.name] && (
              <DependentFields option={option} selectedValues={selectedValues[option?.name]} handleInputChange={handleInputChange} />
            )}
          </div>
        ))}
      </div>
    </div>
  );
}

export default DependentCheckBoxComponent;
