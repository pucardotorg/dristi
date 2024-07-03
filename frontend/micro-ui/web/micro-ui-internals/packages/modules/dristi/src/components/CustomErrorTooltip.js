import React from "react";
import { InfoToolTipIcon } from "../icons/svgIndex";

const CustomErrorTooltip = ({ message, showTooltip }) => {
  if (!showTooltip) {
    return null;
  }

  return (
    <div className="custom-error-tooltip">
      <span>
        <InfoToolTipIcon />
      </span>
      <div className="custom-error-tooltip-message" style={{ ...(!message && { border: "none" }) }}>
        {message}
      </div>
    </div>
  );
};

export default CustomErrorTooltip;
