import React from "react";
import { WarningInfoIconYellow } from "../icons/svgIndex";

function WarningTextComponent({ t, label, styles, iconStyles, labelStyles }) {
  return (
    <div
      className="dca-infobox-message"
      style={{
        display: "flex",
        gap: "8px",
        backgroundColor: "#FEF4F4",
        border: "1px",
        borderColor: "#FCE8E8",
        padding: "8px",
        borderRadius: "8px",
        marginTop: "10px",
        ...styles,
      }}
    >
      <div className="dca-infobox-icon" style={iconStyles}>
        <WarningInfoIconYellow />{" "}
      </div>
      <div className="dca-infobox-me" style={{ labelStyles }}>
        {label}
      </div>
    </div>
  );
}

export default WarningTextComponent;
