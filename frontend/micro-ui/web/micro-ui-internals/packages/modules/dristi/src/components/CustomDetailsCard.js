import { Card } from "@egovernments/digit-ui-react-components";
import React from "react";

const CustomDetailsCard = ({ header, subtext, serialNumber, subnote, style }) => {
  return (
    <Card
      style={{
        display: "flex",
        justifyContent: "space-evenly",
        width: "400px",
        height: "100px",
        flexDirection: "row",
        alignItems: "flex-start",
        ...style,
      }}
    >
      {serialNumber && (
        <div style={{ width: "10%" }}>
          <p>{serialNumber}</p>
        </div>
      )}
      <div style={{ width: "90%" }}>
        {header && <p>{header}</p>}
        {subtext && <p>{subtext}</p>}
        {subnote && <p>{subnote}</p>}
      </div>
    </Card>
  );
};

export default CustomDetailsCard;
