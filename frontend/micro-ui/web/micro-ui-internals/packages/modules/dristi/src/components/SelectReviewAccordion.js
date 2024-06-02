import { Button, EditPencilIcon, TextArea } from "@egovernments/digit-ui-react-components";
import React, { Fragment, useEffect, useMemo, useRef, useState } from "react";
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
import CustomPopUp from "./CustomPopUp";

function SelectReviewAccordion({ t, config, onSelect, formData = {}, errors, formState, control, setError }) {
  const roles = Digit.UserService.getUser()?.info?.roles;
  const isScrutiny = true || roles.some((role) => role.code === "CASE_REVIEWER");
  const [isOpen, setOpen] = useState(true);
  const history = useHistory();
  const urlParams = new URLSearchParams(window.location.search);
  const caseId = urlParams.get("caseId");
  const [isPopupOpen, setIsPopupOpen] = useState(true);
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
  const handleOpenPopup = (clickref, configKey, name, index = null, fieldName) => {
    console.debug({ name, index, fieldName, configKey });
    setPopupInfo({
      position: {
        top: 0,
        left: 0,
      },
      name,
      index,
      fieldName,
      configKey,
    });
    setIsPopupOpen(true);
  };

  const handleClosePopup = () => {
    setScrutinyError("");
    setIsPopupOpen(false);
  };

  const handleAddError = () => {
    const { name, configKey, index, fieldName } = popupInfo;
    let currentMessage =
      formData && formData[configKey]
        ? { ...formData[config.key]?.[name] }
        : {
            scrutinyMessage: "",
            form: inputs.find((item) => item.name === name)?.data?.map(() => ({})),
          };
    if (index == null) {
      currentMessage.scrutinyMessage = { FSOError: scrutinyError };
    } else {
      currentMessage.form[index] = {
        ...currentMessage.form[index],
        [fieldName]: { FSOError: scrutinyError },
      };
    }
    setValue(currentMessage, name);
    setIsPopupOpen(false);
    setScrutinyError("");
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
          {inputs.map((input, index) => {
            let sectionValue = formData && formData[config.key] && formData[config.key]?.[input.name];
            return (
              <div className="content-item">
                <div className="item-header">
                  <div className="header-left">
                    {input?.icon && <Icon icon={input?.icon} />}
                    <span>{t(input?.label)}</span>
                  </div>
                  {(!isScrutiny || sectionValue?.scrutinyMessage?.FSOError) && (
                    <div
                      className="header-right"
                      onClick={() => {
                        if (!isScrutiny) {
                          history.push(`?caseId=${caseId}&selected=${input?.key}`);
                        } else {
                          handleOpenPopup(ref, config.key, input?.name);
                        }
                      }}
                    >
                      <EditPencilIcon />
                    </div>
                  )}
                  {!sectionValue?.scrutinyMessage?.FSOError && isScrutiny && (
                    <div
                      ref={ref}
                      style={{ cursor: "pointer" }}
                      onClick={() => {
                        handleOpenPopup(ref, config.key, input?.name);
                      }}
                      key={index}
                    >
                      <FlagIcon style={{ fill: "blue" }} />
                    </div>
                  )}
                </div>
                {sectionValue?.scrutinyMessage?.FSOError && (
                  <div
                    style={{
                      display: "flex",
                      padding: "5px",
                      borderRadius: "10px",
                      background: "#FCE8E8",
                      justifyContent: "flex-start",
                      alignItems: "center",
                      gap: "20px",
                      width: "50%",
                    }}
                  >
                    <FlagIcon isError={true} />
                    {sectionValue?.scrutinyMessage?.FSOError}
                  </div>
                )}
                {Array.isArray(input.data) &&
                  input.data.map((item, index) => {
                    const dataErrors = sectionValue?.form?.[index];
                    return (
                      <CustomReviewCard
                        isScrutiny={isScrutiny}
                        config={input.config}
                        titleIndex={index + 1}
                        data={item?.data}
                        key={index}
                        dataIndex={index}
                        t={t}
                        handleOpenPopup={handleOpenPopup}
                        formData={formData}
                        input={input}
                        dataErrors={dataErrors}
                        configKey={config.key}
                      />
                    );
                  })}
              </div>
            );
          })}
        </div>
      </div>
      {isPopupOpen && (
        <CustomPopUp position={popupInfo?.position}>
          <Fragment>
            <div>{t("CS_ERROR_DESCRIPTION")}</div>
            <TextArea
              value={scrutinyError}
              onChange={(e) => {
                const { value } = e.target;
                setScrutinyError(value);
              }}
            ></TextArea>
            <div style={{ display: "flex", justifyContent: "space-between", alignItems: "center", gap: "20px" }}>
              <Button label={t("CS_COMMON_DELETE")} onButtonClick={handleClosePopup} />
              <Button
                label={t("CS_COMMON_UPDATE")}
                onButtonClick={() => {
                  handleAddError();
                }}
              />
            </div>
          </Fragment>
        </CustomPopUp>
      )}
    </div>
  );
}

export default SelectReviewAccordion;
