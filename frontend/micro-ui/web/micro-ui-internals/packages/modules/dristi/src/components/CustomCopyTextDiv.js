import React, { useState } from "react";
import { CardText } from "@egovernments/digit-ui-react-components";
import { CopyIcon } from "../icons/svgIndex";

const CustomCopyTextDiv = ({ data, t, keyStyle, valueStyle }) => {
  const [copied, setCopied] = useState({ isCopied: false, text: "Copied" });
  const handleCopy = (text) => {
    navigator.clipboard.writeText(text);
  };

  const dataCopy = (isCopied, message, duration = 3000) => {
    setCopied({ isCopied: isCopied, text: message });
    setTimeout(() => {
      setCopied({ isCopied: false, text: "Copied" });
    }, duration);
  };

  return (
    <div style={{ borderRadius: "10px", backgroundColor: "#F7F5F3", padding: "10px", width: "100%" }}>
      {data.map(({ key, value, copyData = true }, index) => (
        <div key={index} style={{ display: "flex", marginBottom: "10px" }}>
          <div style={{ flex: 1 }}>
            <CardText className={"copy-key-text"} style={keyStyle}>
              {key}
            </CardText>
          </div>
          <div style={{ display: "flex", alignItems: "center", paddingLeft: "10px" }}>
            <CardText style={valueStyle}>{t(value)}</CardText>
            {copyData && (
              <button
                style={{
                  display: "flex",
                  alignItems: "center",
                  gap: "8px",
                  fontSize: "16px",
                  backgroundColor: "transparent",
                }}
                onClick={() => {
                  handleCopy(value);
                  dataCopy(true, "Copied");
                }}
              >
                <CopyIcon />
                {copied.isCopied ? copied.text : "Copy"}
              </button>
            )}
          </div>
        </div>
      ))}
    </div>
  );
};

export default CustomCopyTextDiv;
