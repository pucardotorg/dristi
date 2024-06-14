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
import ImageModal from "./ImageModal";

function SelectReviewAccordion({ t, config, onSelect, formData = {}, errors, formState, control, setError }) {
  const roles = Digit.UserService.getUser()?.info?.roles;
  const isScrutiny = roles.some((role) => role.code === "CASE_REVIEWER");
  const [isOpen, setOpen] = useState(true);
  const [isImageModal, setIsImageModal] = useState(false);
  const history = useHistory();
  const urlParams = new URLSearchParams(window.location.search);
  const caseId = urlParams.get("caseId");
  const [scrutinyError, setScrutinyError] = useState("");
  const popupAnchor = useRef();
  const popupInfo = useMemo(() => {
    return formData?.scrutinyMessage?.popupInfo;
  }, [formData]);

  const isPopupOpen = useMemo(() => {
    return popupInfo?.configKey === config.key;
  }, [config.key, popupInfo]);

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

  const defaultError = useMemo(() => {
    if (!popupInfo) {
      return "";
    }
    const { name = null, configKey = null, index = null, fieldName = null } = popupInfo;
    return fieldName
      ? formData?.[configKey]?.[name]?.form?.[index]?.[fieldName]?.FSOError
      : formData?.[configKey]?.[name]?.scrutinyMessage?.FSOError || "";
  }, [formData, popupInfo]);

  useEffect(() => {
    if (isPopupOpen && popupInfo) {
      setScrutinyError(defaultError);
    }
  }, [defaultError, isPopupOpen, popupInfo]);

  function setValue(configkey, value, input) {
    if (Array.isArray(input)) {
      onSelect(configkey, {
        ...formData[configkey],
        ...input.reduce((res, curr) => {
          res[curr] = value[curr];
          return res;
        }, {}),
      });
    } else onSelect(configkey, { ...formData[configkey], [input]: value });
  }

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
  const handleOpenPopup = (e, configKey, name, index = null, fieldName) => {
    if (e) {
      popupAnchor.current = e.currentTarget;
    }
    setValue(
      "scrutinyMessage",
      {
        name,
        index,
        fieldName,
        configKey,
      },
      "popupInfo"
    );
  };
  const handleClosePopup = () => {
    setScrutinyError("");
    setValue("scrutinyMessage", null, "popupInfo");
  };

  const handleDeleteError = () => {
    const { name, configKey, index, fieldName } = popupInfo;
    let currentMessage =
      formData && formData[configKey]
        ? { ...formData[config.key]?.[name] }
        : {
            scrutinyMessage: "",
            form: inputs.find((item) => item.name === name)?.data?.map(() => ({})),
          };
    if (index == null) {
      currentMessage.scrutinyMessage = { FSOError: "" };
    } else {
      currentMessage.form[index] = {
        ...currentMessage.form[index],
        [fieldName]: { FSOError: "" },
      };
    }
    setScrutinyError("");
    setValue(config.key, currentMessage, name);
    setValue("scrutinyMessage", null, "popupInfo");
  };

  const handleAddError = () => {
    const trimmedError = scrutinyError.trim();
    if (!trimmedError) {
      return;
    }
    const { name, configKey, index, fieldName } = popupInfo;
    let currentMessage =
      formData && formData[configKey] && formData[config.key]?.[name]
        ? { ...formData[config.key]?.[name] }
        : {
            scrutinyMessage: "",
            form: inputs.find((item) => item.name === name)?.data?.map(() => ({})),
          };
    if (index == null) {
      currentMessage.scrutinyMessage = { FSOError: trimmedError };
    } else {
      currentMessage.form[index] = {
        ...(currentMessage?.form?.[index] || {}),
        [fieldName]: { FSOError: trimmedError },
      };
    }
    setValue(config.key, currentMessage, name);
    setValue("scrutinyMessage", null, "popupInfo");
    setScrutinyError("");
  };
  return (
    <div className="accordion-wrapper" onClick={() => {}}>
      <div className={`accordion-title ${isOpen ? "open" : ""}`} onClick={() => setOpen(!isOpen)}>
        <span>{t(config?.label)}</span>
        <span className="reverse-arrow">
          <CustomArrowDownIcon />
        </span>
      </div>
      <div className={`accordion-item ${!isOpen ? "collapsed" : ""}`}>
        <div className="accordion-content">
          {inputs.map((input, index) => {
            const sectionValue = formData && formData[config.key] && formData[config.key]?.[input.name];
            const sectionError = sectionValue?.scrutinyMessage?.FSOError;
            return (
              <div className={`content-item ${sectionError && isScrutiny && "error"}`}>
                <div className="item-header">
                  <div className="header-left">
                    {input?.icon && <Icon icon={input?.icon} />}
                    <span>{t(input?.label)}</span>
                  </div>
                  {(!isScrutiny || sectionError) && (
                    <div
                      className="header-right"
                      onClick={(e) => {
                        if (!isScrutiny) {
                          history.push(`?caseId=${caseId}&selected=${input?.key}`);
                        } else {
                          handleOpenPopup(e, config.key, input?.name);
                        }
                      }}
                    >
                      <EditPencilIcon />
                    </div>
                  )}
                  {!sectionError && isScrutiny && (
                    <div
                      style={{ cursor: "pointer" }}
                      onClick={(e) => {
                        handleOpenPopup(e, config.key, input?.name);
                      }}
                      key={index}
                    >
                      <FlagIcon />
                    </div>
                  )}
                </div>
                {sectionError && (
                  <div className="scrutiny-error section">
                    <FlagIcon isError={true} />
                    {sectionError}
                  </div>
                )}
                {Array.isArray(input.data) &&
                  input.data.map((item, index) => {
                    const dataErrors = sectionValue?.form?.[index];
                    const titleHeading = input.name === "chequeDetails" ? true : false;
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
                        titleHeading={titleHeading}
                        setIsImageModal={setIsImageModal}
                      />
                    );
                  })}
              </div>
            );
          })}
        </div>
      </div>
      {isPopupOpen && (
        <CustomPopUp anchorRef={popupAnchor.current} popupstyle={{ left: -345 }}>
          <Fragment>
            <div>{t("CS_ERROR_DESCRIPTION")}</div>
            <TextArea
              value={scrutinyError}
              onChange={(e) => {
                const { value } = e.target;
                setScrutinyError(value);
              }}
              style={{ minWidth: "300px", maxWidth: "300px", maxHeight: "150px", minHeight: "50px" }}
            ></TextArea>
            <div
              style={{
                display: "flex",
                justifyContent: "space-between",
                alignItems: "center",
                gap: "20px",
              }}
            >
              <Button label={!defaultError ? t("CS_COMMON_CANCEL") : t("CS_COMMON_DELETE")} onButtonClick={handleDeleteError} />
              <Button
                label={!defaultError ? t("CS_MARK_ERROR") : defaultError === scrutinyError ? t("CS_COMMON_CANCEL") : t("CS_COMMON_UPDATE")}
                onButtonClick={() => {
                  if (defaultError === scrutinyError) {
                    handleClosePopup();
                  } else {
                    handleAddError();
                  }
                }}
              />
            </div>
          </Fragment>
        </CustomPopUp>
      )}
      {isImageModal && (
        <ImageModal
          imageInfo={isImageModal}
          t={t}
          handleOpenPopup={handleOpenPopup}
          handleCloseModal={() => {
            setIsImageModal(false);
          }}
        />
      )}
    </div>
  );
}

export default SelectReviewAccordion;
