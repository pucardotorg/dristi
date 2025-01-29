import React, { useState } from "react";

function BoxComplainant({ t, config, onSelect, formData, errors, setError, clearErrors }) {
  return (
    <div
      style={{
        display: "flex",
        justifyContent: "space-between",
        alignItems: "center",
        marginBottom: "20px",
        height: "20px",
      }}
    >
      <h1 style={{ fontSize: "18px", fontWeight: "bold" }}> Complainant {formData?.boxComplainant?.index + 1}</h1>
      <h1 style={{ fontSize: "18px" }}> {formData?.boxComplainant?.firstName}</h1>
    </div>
  );
}

export default BoxComplainant;
