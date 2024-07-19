import React from "react";

export const RenderInstance = ({ value = "", t }) => {
  return (
    <React.Fragment>
      <div style={{ fontWeight: 700, fontSize: "16px", lineHeight: "18.75px", color: "#0B0C0C" }}>{t(value)}</div>
    </React.Fragment>
  );
};
