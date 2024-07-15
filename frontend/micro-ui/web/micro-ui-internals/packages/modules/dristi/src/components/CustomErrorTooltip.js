import React from "react";
import { InfoToolTipIcon } from "../icons/svgIndex";
import { InfoIcon } from "../icons/svgIndex";

const CustomErrorTooltip = ({ message, showTooltip, icon }) => {
  if (!showTooltip) {
    return null;
  }

  return (
    <div className="custom-error-tooltip">
      <span>{!icon ? <InfoIcon /> : <InfoIcon />}</span>
      <div className="custom-error-tooltip-message" style={{ ...(!message && { border: "none" }) }}>
        {message}
      </div>
    </div>
  );
};

export default CustomErrorTooltip;
