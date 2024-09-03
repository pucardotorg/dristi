import { Card, CardLabel, CardText, SubmitBar } from "@egovernments/digit-ui-react-components";
import React from "react";

const CustomCard = ({ Icon, label, style, onClick, subLabel, buttonLabel, className }) => {
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
      onClick={() => {
        if (Icon) {
          onClick();
        }
      }}
    >
      <CardLabel style={{ fontSize: "24px", fontWeight: "600", marginBottom: "5px" }}>{label}</CardLabel>
      <CardText style={{ fontSize: "14px", fontWeight: "400", marginBottom: "10px" }}> {subLabel}</CardText>
      {buttonLabel && <SubmitBar label={buttonLabel} onSubmit={onClick} />}
      {Icon ? Icon : null}
    </Card>
  );
};

export default CustomCard;
