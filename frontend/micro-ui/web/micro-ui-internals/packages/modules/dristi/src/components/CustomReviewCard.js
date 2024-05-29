import { Card } from "@egovernments/digit-ui-react-components";
import React from "react";
import DocViewerWrapper from "../pages/employee/docViewerWrapper";
import { FlagIcon } from "../icons/svgIndex";

function CustomReviewCard({ index, config, data }) {
  const Getrow = ({ type, label, value }) => {
    switch (type) {
      case "title":
        return <div style={{ padding: "5px" }}>{`${index}. ${data[value]}`}</div>;
      case "text":
        return (
          <div style={{ display: "flex", alignItems: "center", justifyContent: "space-between", padding: "5px" }}>
            <div style={{ width: "40%" }}>{label}</div>
            <div style={{ flex: 1, display: "flex", justifyContent: "flex-start" }}> {data[value]} </div>
            <div style={{ cursor: "pointer" }}>
              <FlagIcon />
            </div>
          </div>
        );
      case "amount":
        return (
          <div style={{ display: "flex", alignItems: "center", justifyContent: "space-between", padding: "5px", background: "#F9FAFB" }}>
            <div style={{ width: "40%" }}>{label}</div>
            <div style={{ flex: 1, display: "flex", justifyContent: "flex-start" }}> {`₹${data[value]}`} </div>
            <div style={{ cursor: "pointer" }}>
              <FlagIcon />
            </div>
          </div>
        );
      case "phonenumber":
        return (
          <div style={{ display: "flex", alignItems: "center", justifyContent: "space-between", padding: "5px", background: "#F9FAFB" }}>
            <div style={{ width: "40%" }}>{label}</div>
            <div style={{ flex: 1, display: "flex", justifyContent: "flex-start" }}> {`+91-${data[value]}`} </div>
            <div style={{ cursor: "pointer" }}>
              <FlagIcon />
            </div>
          </div>
        );
      case "image":
        return (
          <div style={{ padding: "5px", background: "#F9FAFB" }}>
            <div>{label}</div>
            <DocViewerWrapper />
          </div>
        );
      case "address":
        return (
          <div style={{ display: "flex", alignItems: "center", justifyContent: "space-between", padding: "5px", background: "#F9FAFB" }}>
            <div style={{ width: "40%" }}>{label}</div>
            <div style={{ flex: 1, display: "flex", justifyContent: "flex-start" }}>{data[value]}</div>
            <div style={{ cursor: "pointer" }}>
              <FlagIcon />
            </div>
          </div>
        );
      default:
        return (
          <div style={{ display: "flex", alignItems: "center", justifyContent: "space-between", padding: "5px" }}>
            <div style={{ width: "40%" }}>{label}</div>
            <div style={{ flex: 1, display: "flex", justifyContent: "flex-start" }}> {data[value]} </div>
            <div style={{ cursor: "pointer" }}>
              <FlagIcon />
            </div>
          </div>
        );
    }
  };

  return (
    <Card>
      {config.map((item, index) => (
        <Getrow type={item.type} label={item?.label} value={item.value} key={index} />
      ))}
    </Card>
  );
}

export default CustomReviewCard;
