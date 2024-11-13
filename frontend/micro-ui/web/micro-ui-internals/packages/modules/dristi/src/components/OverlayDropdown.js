import React, { useState } from "react";
import { useTranslation } from "react-i18next";
import { useHistory } from "react-router-dom";
import { CustomThreeDots, ThreeDots } from "../icons/svgIndex";

export const Context = React.createContext();

const OverlayDropdown = ({ column, row, master, module }) => {
  const { t } = useTranslation();
  const [isDropdownOpen, setIsDropdownOpen] = useState(false);
  const history = useHistory();

  const dropdownItems = Digit.Customizations[master]?.[module]?.dropDownItems?.(row) || [];

  const toggleDropdown = () => {
    setIsDropdownOpen(!isDropdownOpen);
  };

  return (
    <div style={{ position: "absolute", display: "flex", justifyContent: "center", alignItems: "center", width: "40px", height: 0 }}>
      <div
        style={{
          cursor: "pointer",
        }}
        onClick={toggleDropdown}
      >
        <ThreeDots />
      </div>

      {isDropdownOpen && (
        <ul
          style={{
            position: "absolute",
            right: 0,
            backgroundColor: "white",
            border: "1px solid #ccc",
            listStyle: "none",
            padding: 0,
            margin: 0,
            top: "10px",
            width: "200px",
            zIndex: 1000,
          }}
        >
          {dropdownItems
            .filter((item) => !item.hide)
            .map((item) => (
              <li
                key={item.id}
                style={{ padding: "10px", cursor: "pointer", color: item.disabled ? "grey" : "black" }}
                onClick={() => {
                  setIsDropdownOpen(false);
                  return !item.disabled && item.action(history, column, row);
                }}
              >
                {item.label}
              </li>
            ))}
        </ul>
      )}
    </div>
  );
};

export default OverlayDropdown;
