import { EditPencilIcon } from "@egovernments/digit-ui-react-components";
import React, { useEffect, useMemo, useRef, useState } from "react";
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
  const isScrutiny = roles.some((role) => role.code === "CASE_REVIEWER");
  const [isOpen, setOpen] = useState(true);
  const history = useHistory();
  const urlParams = new URLSearchParams(window.location.search);
  const caseId = urlParams.get("caseId");
  const [isPopupOpen, setIsPopupOpen] = useState(false);
  const [popupInfo, setPopupInfo] = useState({ position: { top: 0, left: 0 }, name: "", index: null });
  const [scrutinyError, setScrutinyError] = useState("");
  const ref = useRef();
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

  function setValue(value, input) {
    if (Array.isArray(input)) {
      onSelect(config.key, {
        ...formData[config.key],
        ...input.reduce((res, curr) => {
          res[curr] = value[curr];
          return res;
        }, {}),
      });
    } else onSelect(config.key, { ...formData[config.key], [input]: value });
  }

  useEffect(() => {
    const names = inputs.map((item) => item.name);
    const values = inputs.reduce((acc, item) => {
      acc[item.name] = { scrutinyMessage: "", form: item.data.map(() => ({})) };
      return acc;
    }, {});
    setValue(values, names);
  }, [inputs]);

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
  const handleOpenPopup = (clickref, name, index = null, fieldName) => {
    if (clickref.current) {
      const rect = clickref.current.getBoundingClientRect();
      setPopupInfo({
        position: {
          top: 0,
          left: 0,
        },
        name,
        index,
        fieldName,
      });
    }
    setIsPopupOpen(true);
  };

  const handleClosePopup = () => {
    setScrutinyError("");
    setIsPopupOpen(false);
  };

  const handleAddError = () => {
    let currentValue =
      formData && formData[config.key]
        ? { ...formData[config.key]?.[popupInfo.name] }
        : {
            scrutinyMessage: "",
            form: inputs.find((item) => item.name === popupInfo.name)?.data?.map(() => ({})),
          };
    if (popupInfo.index == null) {
      currentValue.scrutinyMessage = scrutinyError;
    } else {
      currentValue.form[popupInfo.index] = { ...currentValue.form[popupInfo.index], [popupInfo.fieldName]: scrutinyError };
    }
    setValue(currentValue, popupInfo.name);
    setIsPopupOpen(false);
    setScrutinyError("");
  };
  console.debug(formData);
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
          {inputs.map((input, index) => (
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
                  <div
                    ref={ref}
                    style={{ cursor: "pointer" }}
                    onClick={() => {
                      handleOpenPopup(ref, input?.name);
                    }}
                    key={index}
                  >
                    <FlagIcon style={{ fill: "blue" }} />
                  </div>
                )}
              </div>
              {Array.isArray(input.data) &&
                input.data.map((item, index) => (
                  <CustomReviewCard
                    isScrutiny={isScrutiny}
                    config={input.config}
                    titleIndex={index + 1}
                    data={item?.data}
                    key={index}
                    index={index}
                    isPopupOpen={isPopupOpen}
                    popupInfo={popupInfo}
                    t={t}
                    handleClosePopup={handleClosePopup}
                    handleAddError={handleAddError}
                    handleOpenPopup={handleOpenPopup}
                    formData={formData}
                    input={input}
                    scrutinyError={scrutinyError}
                    setScrutinyError={setScrutinyError}
                  />
                ))}
            </div>
          ))}
        </div>
      </div>
    </div>
  );
}

export default SelectReviewAccordion;
