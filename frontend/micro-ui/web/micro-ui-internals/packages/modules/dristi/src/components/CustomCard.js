import { Card, CardHeader, CardLabel, CardText, SubmitBar } from "@egovernments/digit-ui-react-components";
import React from "react";

const CustomCard = ({ Icon, showNumber, label, style, onClick, subLabel, buttonLabel, className }) => {
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
      className={className}
      // onClick={onClick}
    >
      <CardLabel style={{ fontSize: "24px", fontWeight: "600", marginBottom: "0px" }}>{label}</CardLabel>
      <CardText style={{ fontSize: "14px", fontWeight: "400", marginBottom: "10px" }}> {subLabel}</CardText>

      <SubmitBar label={buttonLabel} onSubmit={onClick} />
    </Card>
  );
};

export default CustomCard;
