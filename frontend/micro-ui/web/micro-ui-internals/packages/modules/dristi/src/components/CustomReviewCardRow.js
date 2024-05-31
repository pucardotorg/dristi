import React, { useRef } from "react";
import { FlagIcon } from "../icons/svgIndex";
import DocViewerWrapper from "../pages/employee/docViewerWrapper";

const CustomReviewCardRow = ({ type, label, value, isScrutiny, data, handleOpenPopup, titleIndex, index }) => {
  const ref = useRef();
  const extractValue = (data, key) => {
    if (!key.includes(".")) {
      return data[key];
    }
    const keyParts = key.split(".");
    let value = data;
    keyParts.forEach((part) => {
      if (value && value.hasOwnProperty(part)) {
        value = value[part];
      } else {
        value = undefined;
      }
    });
    return value;
  };

  switch (type) {
    case "title":
      let title = "";
      if (Array.isArray(value)) {
        title = value.map((key) => extractValue(data, key)).join(" ");
      } else {
        title = extractValue(data, value);
      }
      return (
        <div style={{ display: "flex", justifyContent: "space-between", alignItems: "center" }}>
          <div className="title">{`${titleIndex}. ${title}`}</div>
          {isScrutiny && (
            <div
              className="flag"
              onClick={() => {
                handleOpenPopup(ref);
              }}
              key={index}
            >
              <FlagIcon />
            </div>
          )}
        </div>
      );
    case "text":
      const textValue = extractValue(data, value);
      return (
        <div className="text">
          <div className="label">{label}</div>
          <div className="value">
            {Array.isArray(textValue) && textValue.map((text) => <div> {text} </div>)}
            {!Array.isArray(textValue) && textValue}
          </div>
          {isScrutiny && (
            <div
              className="flag"
              onClick={() => {
                handleOpenPopup(ref);
              }}
              key={index}
            >
              <FlagIcon />
            </div>
          )}
        </div>
      );
    case "amount":
      return (
        <div className="amount">
          <div className="label">{label}</div>
          <div className="value"> {`₹${extractValue(data, value)}`} </div>
          {isScrutiny && (
            <div
              className="flag"
              onClick={() => {
                handleOpenPopup(ref);
              }}
              key={index}
            >
              <FlagIcon />
            </div>
          )}
        </div>
      );
    case "phonenumber":
      const numbers = extractValue(data, value);
      return (
        <div className="phone-number">
          <div className="label">{label}</div>
          <div className="value">
            {Array.isArray(numbers) && numbers.map((number) => <div> {`+91-${number}`} </div>)}
            {!Array.isArray(numbers) && `+91-${numbers}`}
          </div>
          {isScrutiny && (
            <div
              className="flag"
              onClick={() => {
                handleOpenPopup(ref);
              }}
              key={index}
            >
              <FlagIcon />
            </div>
          )}
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
      const addressDetails = extractValue(data, value);
      let address = [""];
      if (Array.isArray(addressDetails)) {
        address = addressDetails.map(({ addressDetails }) => {
          return `${addressDetails?.locality}, ${addressDetails?.city}, ${addressDetails?.district}, ${addressDetails?.state} - ${addressDetails?.pincode}`;
        });
      } else {
        address = [
          `${addressDetails?.locality}, ${addressDetails?.city}, ${addressDetails?.district}, ${addressDetails?.state} - ${addressDetails?.pincode}`,
        ];
      }

      return (
        <div className="address">
          <div className="label">{label}</div>
          <div>
            {address.map((item) => (
              <div className="value">{item}</div>
            ))}
          </div>
          {isScrutiny && (
            <div
              className="flag"
              onClick={() => {
                handleOpenPopup(ref);
              }}
              key={index}
            >
              <FlagIcon />
            </div>
          )}
        </div>
      );
    default:
      const defaulValue = extractValue(data, value);
      return (
        <div className="text">
          <div className="label">{label}</div>
          <div className="value">
            {Array.isArray(defaulValue) && defaulValue.map((text) => <div> {text} </div>)}
            {!Array.isArray(defaulValue) && defaulValue}
          </div>
          {isScrutiny && (
            <div
              className="flag"
              onClick={() => {
                handleOpenPopup(ref);
              }}
              key={index}
            >
              <FlagIcon />
            </div>
          )}
        </div>
      );
  }
};

export default CustomReviewCardRow;
