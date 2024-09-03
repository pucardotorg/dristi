import React from "react";
import { formatDateInMonth } from "../Utils";

const Chip = ({ label, isSelected, handleClick }) => {
  const chipStyle = {
    backgroundColor: "#FAFAFA",
    color: "#505A5F",
    border: isSelected ? "1px solid #007E7E" : "1px solid #D6D5D4",
    borderRadius: "8px",
    padding: "5px 10px",
    margin: "5px",
    cursor: "pointer",
  };

  return (
    <div style={chipStyle} onClick={() => handleClick(label)}>
      {label}
    </div>
  );
};

const CustomChooseDate = ({ data, selectedChip, handleClick, scheduleHearingParams, isSelectMulti = false }) => {
  return (
    <div style={{ display: "flex", flexDirection: "row", flexWrap: "wrap", border: "1px solid lightgrey", padding: "10px", marginBottom: "10px" }}>
      {data.map((item, index) => (
        <Chip key={index} label={item} isSelected={isSelectMulti ? selectedChip.includes(item) : selectedChip === item} handleClick={handleClick} />
      ))}
    </div>
  );
};

export default CustomChooseDate;
