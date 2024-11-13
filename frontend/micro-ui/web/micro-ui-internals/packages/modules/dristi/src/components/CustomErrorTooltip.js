import React from "react";
import { InfoToolTipIcon } from "../icons/svgIndex";
import { InfoIcon } from "../icons/svgIndex";

const CustomErrorTooltip = ({ message, showTooltip, icon }) => {
  if (!showTooltip) {
    return null;
  }

  return (
    <div className="custom-error-tooltip" style = {{ position:"relative"}}>
      <span>{icon ? <InfoIcon /> : <InfoToolTipIcon />}</span>
      <div className="custom-error-tooltip-message" style={{ ...(!message && { border: "none" }),
      position:"absolute",
      whiteSpace: "unset",
width: "max-content",
maxWidth: "25vw",
position: "absolute",
top: "100%",
left: "100%",
bottom: "unset",
right: "unset",
backgroundColor:"white"
 }}>
        {message}
      </div>
    </div>
  );
};

export default CustomErrorTooltip;
