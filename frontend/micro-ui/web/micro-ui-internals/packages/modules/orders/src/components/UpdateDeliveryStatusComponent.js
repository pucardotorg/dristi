import { CardLabel, Dropdown, LabelFieldPair, TextInput } from "@egovernments/digit-ui-react-components";
import React, { useEffect, useState } from "react";
import ApplicationInfoComponent from "./ApplicationInfoComponent";

const UpdateDeliveryStatusComponent = ({ t, infos, links, handleSubmitButtonDisable }) => {
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

  useEffect(() => {
    if (selectedDelievery && date) handleSubmitButtonDisable(false);
    else handleSubmitButtonDisable(true);
  }, [selectedDelievery, date]);

  return (
    <div className="update-delivery-status">
      <LabelFieldPair className="case-label-field-pair">
        <CardLabel className="case-input-label">{`${t("Update Delivery Status")}`}</CardLabel>
        <Dropdown t={t} option={deliveryOptions} selected={selectedDelievery} optionKey={"value"} select={(e) => setSelectedDelievery(e)} />
      </LabelFieldPair>
      {selectedDelievery && (
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
      )}

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

      <ApplicationInfoComponent infos={infos} links={links} />
    </div>
  );
};

export default UpdateDeliveryStatusComponent;
