import React from "react";
import { Button, CardLabel, CardSectionHeader, CardSubHeader, CardText, FormComposerV2, Toast } from "@egovernments/digit-ui-react-components";
import { CopyIcon } from "../icons/svgIndex";

const CustomCopyTextDiv = ({ data, t }) => {
  const handleCopy = (text) => {
    navigator.clipboard.writeText(text);
  };

  return (
    <div style={{ borderRadius: "10px", backgroundColor: "#F7F5F3", padding: "10px", width: "100%" }}>
      {data.map(({ key, value }, index) => (
        <div key={index} style={{ display: "flex", marginBottom: "10px" }}>
          <div style={{ flex: 1 }}>
            <CardText>{t(key)}</CardText>
          </div>
          <div style={{ display: "flex", alignItems: "center", paddingLeft: "10px" }}>
            <CardText>{t(value)}</CardText>

            <Button
              variation="secondary"
              onButtonClick={() => handleCopy(value)}
              className="primary-label-btn"
              icon={<CopyIcon />}
              style={{ border: "none", backgroundColor: "#F7F5F3" }}
              label={"Copy"}
            ></Button>
          </div>
        </div>
      ))}
    </div>
  );
};

export default CustomCopyTextDiv;
