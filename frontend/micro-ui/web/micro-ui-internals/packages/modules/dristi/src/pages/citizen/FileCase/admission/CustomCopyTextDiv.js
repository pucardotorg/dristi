import React from "react";
import { CardLabel, CardSectionHeader, CardSubHeader, CardText, FormComposerV2, Toast } from "@egovernments/digit-ui-react-components";
import { useTranslation } from "react-i18next";

const CustomCopyTextDiv = ({ data }) => {
  const { t } = useTranslation();
  const handleCopy = (text) => {
    navigator.clipboard.writeText(text);
  };

  return (
    <div style={{ borderRadius: "10px", backgroundColor: "#F7F5F3", padding: "10px", width: "100%" }}>
      {data.map(({ key, value, showCopy }, index) => (
        <div key={index} style={{ display: "flex", marginBottom: "10px" }}>
          <div style={{ flex: 1 }}>
            <CardText>{t(key)}</CardText>
          </div>
          <div style={{ display: "flex", alignItems: "center", paddingLeft: "10px" }}>
            <CardText>{value}</CardText>
            {showCopy && <button onClick={() => handleCopy(value)}>Copy</button>}
          </div>
        </div>
      ))}
    </div>
  );
};

export default CustomCopyTextDiv;
