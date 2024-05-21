import { EditPencilIcon } from "@egovernments/digit-ui-react-components";
import React, { useMemo, useState } from "react";
import { RespondentDetailsIcon } from "../icons/svgIndex";
import CustomReviewCard from "./CustomReviewCard";

function SelectReviewAccordion({ t, config, onSelect, formData = {}, errors, formState, control, setError }) {
  const [isOpen, setOpen] = useState(true);
  console.debug(config);
  const inputs = useMemo(
    () =>
      config?.populators?.inputs || [
        {
          label: "CS_PIN_LOCATION",
          type: "LocationSearch",
          name: [],
        },
      ],
    [config?.populators?.inputs]
  );
  console.debug(inputs);
  const Icon = ({ icon }) => {
    switch (icon) {
      case "RespondentDetailsIcon":
        return RespondentDetailsIcon;
      default:
        return RespondentDetailsIcon;
    }
  };

  const data = { name: "Sheetal Arora", phone: "9834178901", id: "uri", address: "4601E, Gatade Plot, Pandharpur" };
  const config1 = [
    { type: "title", value: "name" },
    { type: "phonenumber", label: "Phone Number", value: "phone" },
    { type: "image", label: "ID Proof", value: "id" },
    { type: "address", label: "Address", value: "address" },
  ];

  return (
    <div className="accordion-wrapper" onClick={() => {}}>
      <div className={`accordion-title ${isOpen ? "open" : ""}`} onClick={() => setOpen(!isOpen)}>
        {config?.label}
      </div>
      <div className={`accordion-item ${!isOpen ? "collapsed" : ""}`}>
        <div className="accordion-content">
          {inputs.map((input) => (
            <div>
              <div style={{ display: "flex", alignItems: "center", justifyContent: "space-between" }}>
                <div style={{ display: "flex", alignItems: "center", justifyContent: "flex-start", gap: "20px" }}>
                  <Icon icon={"RespondentDetailsIcon"} />
                  {input?.label}
                </div>
                <EditPencilIcon />
              </div>
              {Array.isArray(input.data) && input.data.map((item, index) => <CustomReviewCard config={input.config} index={index + 1} data={item} />)}
              {!Array.isArray(input.data) && <CustomReviewCard config={input.config} data={input.data} />}
            </div>
          ))}
        </div>
      </div>
    </div>
  );
}

export default SelectReviewAccordion;
