import { Card } from "@egovernments/digit-ui-react-components";
import React from "react";

const CustomCard = ({ Icon, showNumber, label, style = { width: "400px", height: "150px" }, onClick }) => {
  return (
    <Card
      style={{
        position: "relative",
        display: "flex",
        flexDirection: "column",
        alignItems: "center",
        justifyContent: "space-evenly",
        ...style,
      }}
      onClick={onClick}
    >
      {showNumber && (
        <div style={{ display: "flex", justifyContent: "flex-end", width: "100%" }}>
          <span
            style={{
              padding: "5px",
              paddingRight: "10px",
              paddingLeft: "10px",
              background: "#D4351C",
              color: "white",
              borderRadius: "20px",
              textAlign: "center",
            }}
          >
            {showNumber}
          </span>
        </div>
      )}
      {Icon ? Icon : null}
      <h3>{label}</h3>
    </Card>
  );
};

export default CustomCard;
