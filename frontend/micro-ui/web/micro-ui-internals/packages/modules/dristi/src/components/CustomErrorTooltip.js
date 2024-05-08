// ErrorTooltip.js
import React from "react";
import { ReactComponent as InfoToolTipIcon } from "../images/info_tooltip.svg";

const CustomErrorTooltip = ({ message, visible }) => {
  if (!visible) {
    return null;
  }

  return (
    <div className="custom-error-tooltip">
      <span>
        <InfoToolTipIcon></InfoToolTipIcon>
      </span>
      <div className="custom-error-tooltip-message">{message}</div>
    </div>
  );
};

export default CustomErrorTooltip;
