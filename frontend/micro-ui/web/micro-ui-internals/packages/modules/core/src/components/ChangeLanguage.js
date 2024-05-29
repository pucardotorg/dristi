import { CustomButton, Dropdown } from "@egovernments/digit-ui-react-components";
import React, { useState } from "react";

const GlobalIcon = () => (
  <svg width="16" height="16" viewBox="0 0 16 16" fill="none" xmlns="http://www.w3.org/2000/svg">
    <path
      d="M8 16C6.89333 16 5.85333 15.79 4.88 15.37C3.90667 14.95 3.06 14.38 2.34 13.66C1.62 12.94 1.05 12.0933 0.63 11.12C0.21 10.1467 0 9.10667 0 8C0 6.89333 0.21 5.85333 0.63 4.88C1.05 3.90667 1.62 3.06 2.34 2.34C3.06 1.62 3.90667 1.05 4.88 0.63C5.85333 0.21 6.89333 0 8 0C9.10667 0 10.1467 0.21 11.12 0.63C12.0933 1.05 12.94 1.62 13.66 2.34C14.38 3.06 14.95 3.90667 15.37 4.88C15.79 5.85333 16 6.89333 16 8C16 9.10667 15.79 10.1467 15.37 11.12C14.95 12.0933 14.38 12.94 13.66 13.66C12.94 14.38 12.0933 14.95 11.12 15.37C10.1467 15.79 9.10667 16 8 16ZM8 14.4C9.78667 14.4 11.3 13.78 12.54 12.54C13.78 11.3 14.4 9.78667 14.4 8C14.4 7.90667 14.3967 7.81 14.39 7.71C14.3833 7.61 14.38 7.52667 14.38 7.46C14.3133 7.84667 14.1333 8.16667 13.84 8.42C13.5467 8.67333 13.2 8.8 12.8 8.8H11.2C10.76 8.8 10.3833 8.64333 10.07 8.33C9.75667 8.01667 9.6 7.64 9.6 7.2V6.4H6.4V4.8C6.4 4.36 6.55667 3.98333 6.87 3.67C7.18333 3.35667 7.56 3.2 8 3.2H8.8C8.8 2.89333 8.88333 2.62333 9.05 2.39C9.21667 2.15667 9.42 1.96667 9.66 1.82C9.39333 1.75333 9.12333 1.7 8.85 1.66C8.57667 1.62 8.29333 1.6 8 1.6C6.21333 1.6 4.7 2.22 3.46 3.46C2.22 4.7 1.6 6.21333 1.6 8H5.6C6.48 8 7.23333 8.31333 7.86 8.94C8.48667 9.56667 8.8 10.32 8.8 11.2V12H6.4V14.2C6.66667 14.2667 6.93 14.3167 7.19 14.35C7.45 14.3833 7.72 14.4 8 14.4Z"
      fill="#0A0A0A"
    />
  </svg>
);
const ChangeLanguage = (prop) => {
  const isDropdown = prop.dropdown || false;
  const { data: storeData, isLoading } = Digit.Hooks.useStore.getInitData();
  const { languages, stateInfo } = storeData || {};
  const selectedLanguage = Digit.StoreData.getCurrentLanguage();
  const [selected, setselected] = useState(selectedLanguage);
  const handleChangeLanguage = (language) => {
    setselected(language.value);
    Digit.LocalizationService.changeLanguage(language.value, stateInfo.code);
  };

  if (isLoading) return null;

  if (isDropdown) {
    return (
      <div style={prop.style}>
        <Dropdown
          option={languages}
          selected={languages.find((language) => language.value === selectedLanguage)}
          optionKey={"label"}
          select={handleChangeLanguage}
          freeze={true}
          customSelector={
            <label
              className={`cp ${prop.dropdownClassName}`}
              style={{ color: "black", display: "flex", gap: "4px", alignItems: "center", height: "40px" }}
            >
              <span style={{ display: "flex", alignItems: "center" }}>
                <GlobalIcon />
              </span>
              <span>
                {languages
                  .find((language) => language.value === selected)
                  .label?.substring(0, 2)
                  ?.toUpperCase()}
              </span>
            </label>
          }
        />
      </div>
    );
  } else {
    return (
      <React.Fragment>
        <div style={{ marginBottom: "5px" }}>Language</div>
        <div className="language-selector">
          {languages.map((language, index) => (
            <div className="language-button-container" key={index}>
              <CustomButton
                selected={language.value === selected}
                text={language.label}
                onClick={() => handleChangeLanguage(language)}
              ></CustomButton>
            </div>
          ))}
        </div>
      </React.Fragment>
    );
  }
};

export default ChangeLanguage;
