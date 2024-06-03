import React, { useRef } from "react";
import { FlagIcon } from "../icons/svgIndex";
import DocViewerWrapper from "../pages/employee/docViewerWrapper";
import { EditPencilIcon } from "@egovernments/digit-ui-react-components";

const CustomReviewCardRow = ({ type, label, value, isScrutiny, data, handleOpenPopup, titleIndex, dataIndex, name, configKey, dataError }) => {
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
        <div>
          <div style={{ display: "flex", justifyContent: "space-between", alignItems: "center" }}>
            <div className="title">{`${titleIndex}. ${title}`}</div>
            {isScrutiny && (
              <div
                className="flag"
                onClick={() => {
                  handleOpenPopup(ref, configKey, name, dataIndex, "title");
                }}
                key={dataIndex}
                ref={ref}
              >
                {dataError ? <EditPencilIcon /> : <FlagIcon />}
              </div>
            )}
          </div>
          {dataError && isScrutiny && (
            <div className="scrutiny-error input">
              <FlagIcon isError={true} />
              {dataError}
            </div>
          )}
        </div>
      );
    case "text":
      const textValue = extractValue(data, value);
      return (
        <div>
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
                  handleOpenPopup(ref, configKey, name, dataIndex, value);
                }}
                key={dataIndex}
                ref={ref}
              >
                <FlagIcon />
              </div>
            )}
          </div>
          {dataError && isScrutiny && (
            <div className="scrutiny-error input">
              <FlagIcon isError={true} />
              {dataError}
            </div>
          )}
        </div>
      );

    case "amount":
      return (
        <div>
          <div className="amount">
            <div className="label">{label}</div>
            <div className="value"> {`₹${extractValue(data, value)}`} </div>
            {isScrutiny && (
              <div
                className="flag"
                onClick={() => {
                  handleOpenPopup(ref, configKey, name, dataIndex, value);
                }}
                key={dataIndex}
                ref={ref}
              >
                <FlagIcon />
              </div>
            )}
          </div>
          {dataError && isScrutiny && (
            <div className="scrutiny-error input">
              <FlagIcon isError={true} />
              {dataError}
            </div>
          )}
        </div>
      );
    case "phonenumber":
      const numbers = extractValue(data, value);
      return (
        <div>
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
                  handleOpenPopup(ref, configKey, name, dataIndex, value);
                }}
                key={dataIndex}
                ref={ref}
              >
                <FlagIcon />
              </div>
            )}
          </div>
          {dataError && isScrutiny && (
            <div className="scrutiny-error input">
              <FlagIcon isError={true} />
              {dataError}
            </div>
          )}
        </div>
      );
    case "image":
      return (
        <div>
          <div className="image">
            <div className="label">{label}</div>
            <div className="value">
              <DocViewerWrapper />
            </div>
          </div>
          {dataError && isScrutiny && (
            <div className="scrutiny-error input">
              <FlagIcon isError={true} />
              {dataError}
            </div>
          )}
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
        <div>
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
                  handleOpenPopup(ref, configKey, name, dataIndex, value);
                }}
                key={dataIndex}
              >
                <FlagIcon />
              </div>
            )}
          </div>
          {dataError && isScrutiny && (
            <div className="scrutiny-error input">
              <FlagIcon isError={true} />
              {dataError}
            </div>
          )}
        </div>
      );
    default:
      const defaulValue = extractValue(data, value);
      return (
        <div>
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
                  handleOpenPopup(ref, configKey, name, dataIndex, value);
                }}
                key={dataIndex}
              >
                <FlagIcon />
              </div>
            )}
          </div>
          <div className="scrutiny-error input">
            <FlagIcon isError={true} />
            {dataError}
          </div>
        </div>
      );
  }
};

export default CustomReviewCardRow;
