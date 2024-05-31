import { EditPencilIcon } from "@egovernments/digit-ui-react-components";
import React, { useMemo, useState } from "react";
import {
  ChequeDetailsIcon,
  CustomArrowDownIcon,
  DebtLiabilityIcon,
  DemandDetailsNoticeIcon,
  FlagIcon,
  PrayerSwornIcon,
  RespondentDetailsIcon,
} from "../icons/svgIndex";
import CustomReviewCard from "./CustomReviewCard";
import { useHistory } from "react-router-dom/cjs/react-router-dom.min";

function SelectReviewAccordion({ t, config, onSelect, formData = {}, errors, formState, control, setError }) {
  const roles = Digit.UserService.getUser()?.info?.roles;
  // const isScrutiny = roles.some((role) => role.code === "CASE_REVIEWER");
  const isScrutiny = false;
  const [isOpen, setOpen] = useState(true);
  const history = useHistory();
  const urlParams = new URLSearchParams(window.location.search);
  const caseId = urlParams.get("caseId");
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
        <span>{config?.label}</span>
        <span className="reverse-arrow">
          <CustomArrowDownIcon />
        </span>
      </div>
      <div className={`accordion-item ${!isOpen ? "collapsed" : ""}`}>
        <div className="accordion-content">
          {inputs.map((input) => (
            <div className="content-item">
              <div className="item-header">
                <div className="header-left">
                  {input?.icon && <Icon icon={input?.icon} />}
                  <span>{t(input?.label)}</span>
                </div>
                {!isScrutiny && (
                  <div
                    className="header-right"
                    onClick={() => {
                      history.push(`?caseId=${caseId}&selected=${input?.key}`);
                    }}
                  >
                    <EditPencilIcon />
                  </div>
                )}
                {isScrutiny && (
                  <div style={{ cursor: "pointer" }}>
                    <FlagIcon style={{ fill: "blue" }} />
                  </div>
                )}
              </div>
              {Array.isArray(input.data) &&
                input.data.map((item, index) => (
                  <CustomReviewCard isScrutiny={isScrutiny} config={input.config} index={index + 1} data={item?.data} />
                ))}
            </div>
          ))}
        </div>
      </div>
    </div>
  );
}

export default SelectReviewAccordion;
