import React from "react";

import { EmptyStateIcon } from "@egovernments/digit-ui-module-dristi/src/icons/svgIndex";

function EmptyStates({ heading, message }) {
  return (
    <div
      style={{
        marginLeft: "auto",
        marginRight: "auto",
        width: "100%",
        display: "flex",
        flexDirection: "column",
        alignItems: "center",
        justifyContent: "center",
        backgroundColor: "#fffaf6",
        padding: "20px 20px 0px 20px",
        borderRadius: "8px",
      }}
    >
      <div>
        <EmptyStateIcon />
      </div>
      <p style={{ color: "#5f5f5f", fontSize: "1.3rem", fontWeight: "bold", maxWidth: "450px", margin: "10px 0 5px 0" }}>{heading} </p>
      <p style={{ color: "#6e6e6e", fontSize: "0.9rem", textAlign: "center", maxWidth: "450px", margin: "5px 0", lineHeight: "1.7" }}>{message} </p>
    </div>
  );
}

export default EmptyStates;
