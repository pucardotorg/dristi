import React from "react";

export const OrderName = ({ rowData, colData, value = "" }) => {
  return (
    <div
      style={{
        fontWeight: "bold",
        textDecoration: "underline",
        cursor: "pointer",
      }}
      onClick={() => colData?.clickFunc(rowData)}
    >
      {value}
    </div>
  );
};
