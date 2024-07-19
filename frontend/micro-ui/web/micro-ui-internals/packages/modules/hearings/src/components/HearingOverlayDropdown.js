import React, { useState } from "react";
import { useTranslation } from "react-i18next";
import { useHistory } from "react-router-dom";

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
    <div style={{ position: "relative" }}>
      {/* Three dots or any other trigger */}
      <div
        style={{
          top: "0",
          right: "0",
          backgroundColor: "white",
          border: "1px solid #ccc",
          listStyle: "none",
          padding: "5px",
          cursor: "pointer",
          zIndex: "1000",
          width: "20px",
          textAlign: "center",
        }}
        onClick={toggleDropdown}
      >
        {/* You can use any icon or three dots image here */}
        <span>...</span>
      </div>

      {isDropdownOpen && (
        <ul
          style={{
            position: "absolute",
            top: "100%",
            right: 0,
            backgroundColor: "white",
            border: "1px solid #ccc",
            listStyle: "none",
            padding: 0,
            margin: 0,
            width: "200px",
            zIndex: 1000,
          }}
        >
          {dropdownItems
            .filter((item) => !item.hide)
            .map((item) => (
              <li
                key={item.id}
                style={{ padding: "10px", cursor: "pointer" }}
                onClick={() => {
                  setIsDropdownOpen(false);
                  return item.action(history);
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
