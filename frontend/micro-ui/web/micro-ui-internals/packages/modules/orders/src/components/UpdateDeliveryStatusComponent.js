import { CardLabel, Dropdown, LabelFieldPair, TextInput } from "@egovernments/digit-ui-react-components";
import React, { useState } from "react";
import { Link } from "react-router-dom/cjs/react-router-dom.min";

const UpdateDeliveryStatusComponent = ({ t, infos }) => {
  const [selectedDelievery, setSelectedDelievery] = useState({});
  const [date, setDate] = useState("");
  const [remarks, setRemarks] = useState("");
  const deliveryOptions = [
    {
      key: "SENT",
      value: "Sent",
    },
    {
      key: "DELIVERED",
      value: "Delivered",
    },
    {
      key: "NOT_DELIVERED",
      value: "Not Delivered",
    },
    {
      key: "SIGNED",
      value: "Signed",
    },
  ];
  return (
    <div className="update-delivery-status">
      <LabelFieldPair className="case-label-field-pair">
        <CardLabel className="case-input-label">{`${t("Update Delivery Status")}`}</CardLabel>
        <Dropdown t={t} option={deliveryOptions} selected={selectedDelievery} optionKey={"value"} select={(e) => setSelectedDelievery(e)} />
      </LabelFieldPair>
      <LabelFieldPair className="case-label-field-pair">
        <CardLabel className="case-input-label">{`${t("Update Delivery Status")}`}</CardLabel>
        <TextInput
          value={date}
          type={"date"}
          name={"delivery-date"}
          onChange={(e) => {
            setDate(e?.target?.value);
            console.log("date :>> ", e.target.value);
          }}
        />
      </LabelFieldPair>
      <LabelFieldPair className="case-label-field-pair">
        <CardLabel className="case-input-label">{`${t("Remarks (optional)")}`}</CardLabel>
        <TextInput
          value={remarks}
          type={"text"}
          name={"remarks"}
          onChange={(e) => {
            setRemarks(e?.target?.value);
            console.log("remarks :>> ", e.target.value);
          }}
        />
      </LabelFieldPair>
      <div className="application-info">
        {true && (
          <Link className="review-summon-order" to={{ pathname: `` }}>
            {"View Order"}
          </Link>
        )}
        {infos &&
          infos?.map((info, index) => (
            <div className="info-row" key={index}>
              <div className="info-key">
                <h3>{t(info?.key)}</h3>
              </div>
              <div className="info-value">
                <h3>{t(info?.value)}</h3>
              </div>
            </div>
          ))}
      </div>
    </div>
  );
};

export default UpdateDeliveryStatusComponent;
