import { Card } from "@egovernments/digit-ui-react-components";
import React from "react";
import DocViewerWrapper from "../pages/employee/docViewerWrapper";
import { FlagIcon } from "../icons/svgIndex";

function CustomReviewCard({ index, config, data }) {
  const Getrow = ({ type, label, value }) => {
    switch (type) {
      case "title":
        return <div className="title">{`${index}. ${data[value]}`}</div>;
      case "text":
        return (
          <div className="text">
            <div className="label">{label}</div>
            <div className="value"> {data[value]} </div>
            <div className="flag">
              <FlagIcon />
            </div>
          </div>
        );
      case "amount":
        return (
          <div className="amount">
            <div className="label">{label}</div>
            <div className="value"> {`₹${data[value]}`} </div>
            <div className="flag">
              <FlagIcon />
            </div>
          </div>
        );
      case "phonenumber":
        return (
          <div className="phone-number">
            <div className="label">{label}</div>
            <div className="value"> {`+91-${data[value]}`} </div>
            <div className="flag">
              <FlagIcon />
            </div>
          </div>
        );
      case "image":
        return (
          <div className="image">
            <div className="label">{label}</div>
            <div className="value">
              <DocViewerWrapper />
            </div>
          </div>
        );
      case "address":
        return (
          <div className="address">
            <div className="label">{label}</div>
            <div className="value">{data[value]}</div>
            <div className="flag">
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
    <div className="item-body">
      {config.map((item, index) => (
        <Getrow type={item.type} label={item?.label} value={item.value} key={index} />
      ))}
    </div>
  );
}

export default CustomReviewCard;
