import React, { useState } from "react";
import { CardText } from "@egovernments/digit-ui-react-components";
import { CopyIcon } from "../icons/svgIndex";

const CustomCopyTextDiv = ({ data, t, keyStyle, valueStyle, textWrapperStyle }) => {
  const [copiedIndex, setCopiedIndex] = useState(null); // Track the index of the copied item

  const handleCopy = (text) => {
    navigator.clipboard.writeText(text);
  };

  const dataCopy = (index, duration = 3000) => {
    setCopiedIndex(index); // Set the copied index
    setTimeout(() => {
      setCopiedIndex(null); // Reset after the duration
    }, duration);
  };

  return (
    <div style={{ borderRadius: "10px", backgroundColor: "#F7F5F3", padding: "10px", width: "100%" }}>
      {data.map(({ key, value, copyData = true }, index) => (
        <div key={index} style={{ display: "flex", marginBottom: "10px" }}>
          <div style={{ flex: 1, ...textWrapperStyle }}>
            <CardText className={"copy-key-text"} style={keyStyle}>
              {t(key)}
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
                  dataCopy(index); // Pass the current index to the dataCopy function
                }}
              >
                <CopyIcon />
                {copiedIndex === index ? "Copied" : "Copy"} {/* Show "Copied" only for the clicked item */}
              </button>
            )}
          </div>
        </div>
      ))}
    </div>
  );
};

export default CustomCopyTextDiv;
