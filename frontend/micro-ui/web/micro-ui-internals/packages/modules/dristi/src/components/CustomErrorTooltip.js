// ErrorTooltip.js
import React from "react";
import { ReactComponent as InfoToolTipIcon } from "../images/Vector.svg";

const CustomErrorTooltip = ({ message, showTooltip }) => {
  if (!showTooltip) {
    return null;
  }

  return (
    <div className="custom-error-tooltip">
      <span>
        <InfoToolTipIcon></InfoToolTipIcon>
      </span>
      <div className="custom-error-tooltip-message" style={{ ...(!message && { border: "none" }) }}>
        {message}
      </div>
    </div>
  );
};

export default CustomErrorTooltip;
