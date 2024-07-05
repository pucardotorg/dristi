// ErrorTooltip.js
import React from "react";
import { ReactComponent as InfoToolTipIcon } from "../images/Vector.svg";
import { InfoIcon } from "../icons/svgIndex";

const CustomErrorTooltip = ({ message, showTooltip, icon }) => {
  if (!showTooltip) {
    return null;
  }

  return (
    <div className="custom-error-tooltip">
      <span>{!icon ? <InfoToolTipIcon></InfoToolTipIcon> : <InfoIcon />}</span>
      <div className="custom-error-tooltip-message" style={{ ...(!message && { border: "none" }) }}>
        {message}
      </div>
    </div>
  );
};

export default CustomErrorTooltip;
