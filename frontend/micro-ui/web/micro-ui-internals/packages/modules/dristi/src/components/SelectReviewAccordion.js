import { EditPencilIcon } from "@egovernments/digit-ui-react-components";
import React, { useMemo, useState } from "react";
import { ChequeDetailsIcon, DebtLiabilityIcon, DemandDetailsNoticeIcon, PrayerSwornIcon, RespondentDetailsIcon } from "../icons/svgIndex";
import CustomReviewCard from "./CustomReviewCard";
import { useHistory } from "react-router-dom/cjs/react-router-dom.min";

function SelectReviewAccordion({ t, config, onSelect, formData = {}, errors, formState, control, setError }) {
  const [isOpen, setOpen] = useState(true);
  const history = useHistory();
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
  const Icon = ({ icon }) => {
    switch (icon) {
      case "RespondentDetailsIcon":
        return <RespondentDetailsIcon />;
      case "ComplainantDetailsIcon":
        return <RespondentDetailsIcon />;
      case "ChequeDetailsIcon":
        return <ChequeDetailsIcon />;
      case "DebtLiabilityIcon":
        return <DebtLiabilityIcon />;
      case "DemandDetailsNoticeIcon":
        return <DemandDetailsNoticeIcon />;
      case "PrayerSwornIcon":
        return <PrayerSwornIcon />;
      case "WitnessDetailsIcon":
        return <RespondentDetailsIcon />;
      case "AdvocateDetailsIcon":
        return <DemandDetailsNoticeIcon />;
      default:
        return <RespondentDetailsIcon />;
    }
  };

  return (
    <div className="accordion-wrapper" onClick={() => {}}>
      <div className={`accordion-title ${isOpen ? "open" : ""}`} onClick={() => setOpen(!isOpen)}>
        {config?.label}
      </div>
      <div className={`accordion-item ${!isOpen ? "collapsed" : ""}`}>
        <div className="accordion-content">
          {inputs.map((input) => (
            <div style={{ paddingTop: "20px" }}>
              <div style={{ display: "flex", alignItems: "center", justifyContent: "space-between" }}>
                <div
                  style={{
                    display: "flex",
                    alignItems: "center",
                    justifyContent: "flex-start",
                    gap: "20px",
                    paddingTop: "10px",
                    paddingBottom: "10px",
                  }}
                >
                  {input?.icon && <Icon icon={input?.icon} />}
                  {input?.label}
                </div>
                <div
                  style={{ cursor: "pointer" }}
                  onClick={() => {
                    history.push(`?selected=${input?.key}`);
                  }}
                >
                  <EditPencilIcon />
                </div>
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
