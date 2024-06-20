import { Button, CardText, EditPencilIcon, TextArea } from "@egovernments/digit-ui-react-components";
import React, { Fragment, useEffect, useMemo, useRef, useState } from "react";
import { useHistory } from "react-router-dom";
import {
  ChequeDetailsIcon,
  CustomArrowDownIcon,
  DebtLiabilityIcon,
  DemandDetailsNoticeIcon,
  FlagIcon,
  PrayerSwornIcon,
  RespondentDetailsIcon,
} from "../icons/svgIndex";
import CustomPopUp from "./CustomPopUp";
import CustomReviewCard from "./CustomReviewCard";
import ImageModal from "./ImageModal";

function SelectReviewAccordion({ t, config, onSelect, formData = {}, errors, formState, control, setError }) {
  const roles = Digit.UserService.getUser()?.info?.roles;
  const isScrutiny = useMemo(() => roles.some((role) => role.code === "CASE_REVIEWER"), [roles]);
  const isJudge = useMemo(() => roles.some((role) => role.code === "CASE_APPROVER"), [roles]);

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
  const imagePopupInfo = useMemo(() => {
    return formData?.scrutinyMessage?.imagePopupInfo;
  }, [formData]);

  const isPopupOpen = useMemo(() => {
    return popupInfo?.configKey === config.key;
  }, [config.key, popupInfo]);
  const [deletePopup, setDeletePopup] = useState(false);
  const inputs = useMemo(
    () =>
      config?.populators?.inputs || [
        {
          label: "CS_LOCATION",
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
  const handleOpenPopup = (e, configKey, name, index = null, fieldName, inputlist = [], fileName = null) => {
    setValue(
      "scrutinyMessage",
      {
        name,
        index,
        fieldName,
        configKey,
        inputlist,
        fileName,
      },
      "popupInfo"
    );
  };

  const handleClickImage = (e, configKey, name, index = null, fieldName, data, inputlist = []) => {
    setValue(
      "scrutinyMessage",
      {
        name,
        index,
        fieldName,
        configKey,
        data,
        inputlist,
      },
      "imagePopupInfo"
    );
  };

  const handleClosePopup = () => {
    setScrutinyError("");
    setValue("scrutinyMessage", null, "popupInfo");
  };

  const handleCloseImageModal = () => {
    setScrutinyError("");
    setValue("scrutinyMessage", null, "imagePopupInfo");
  };

  const handleDeleteError = () => {
    const { name, configKey, index, fieldName, inputlist } = popupInfo;
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
      let fieldObj = { [fieldName]: { FSOError: "" } };
      inputlist.forEach((key) => {
        fieldObj[key] = { FSOError: "" };
      });
      currentMessage.form[index] = {
        ...currentMessage.form[index],
        ...fieldObj,
      };
    }
    setDeletePopup(false);
    setScrutinyError("");
    setValue(config.key, currentMessage, name);
    setValue("scrutinyMessage", null, "popupInfo");
  };

  const handleAddError = () => {
    const trimmedError = scrutinyError.trim();
    if (!trimmedError) {
      return;
    }
    const { name, configKey, index, fieldName, inputlist, fileName } = popupInfo;
    let fieldObj = { [fieldName]: { FSOError: trimmedError } };
    inputlist.forEach((key) => {
      fieldObj[key] = { FSOError: trimmedError, fileName };
    });
    let currentMessage =
      formData && formData[configKey] && formData[config.key]?.[name]
        ? { ...formData[config.key]?.[name] }
        : {
            scrutinyMessage: "",
            form: inputs.find((item) => item.name === name)?.data?.map(() => ({})),
          };
    if (index == null) {
      currentMessage.scrutinyMessage = { FSOError: trimmedError, fileName };
    } else {
      currentMessage.form[index] = {
        ...(currentMessage?.form?.[index] || {}),
        ...fieldObj,
      };
    }
    setValue(config.key, currentMessage, name);
    setValue("scrutinyMessage", { popupInfo: null, imagePopupInfo: null }, ["popupInfo", "imagePopupInfo"]);
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
                  {(!isScrutiny || sectionError) && !isJudge && (
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
                  {!sectionError && isScrutiny && sectionValue && (
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
                {sectionError && isScrutiny && (
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
                        handleClickImage={handleClickImage}
                        formData={formData}
                        input={input}
                        dataErrors={dataErrors}
                        configKey={config.key}
                        titleHeading={titleHeading}
                      />
                    );
                  })}
              </div>
            );
          })}
        </div>
      </div>
      {isPopupOpen && (
        <CustomPopUp anchorRef={popupAnchor.current} popupstyle={{ left: "50%", top: "50%", transform: "translate(-50%, -50%)" }}>
          <Fragment>
            <div>{t("CS_ERROR_DESCRIPTION")}</div>
            <TextArea
              value={scrutinyError}
              onChange={(e) => {
                const { value } = e.target;
                setScrutinyError(value);
              }}
              maxlength="255"
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
              <Button
                label={!defaultError ? t("CS_COMMON_CANCEL") : t("CS_COMMON_DELETE")}
                onButtonClick={() => {
                  if (!defaultError) {
                    handleDeleteError();
                  } else {
                    setDeletePopup(true);
                  }
                }}
              />
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
      {deletePopup && (
        <CustomPopUp
          anchorRef={popupAnchor.current}
          popupstyle={{ minWidth: "400px", maxWidth: "400px", left: "50%", top: "50%", transform: "translate(-50%, -50%)" }}
        >
          <Fragment>
            <div>{t("CS_DELETE_COMMENT")}</div>
            <CardText>{t("CS_DELETE_HEADER")}</CardText>

            <div
              style={{
                display: "flex",
                justifyContent: "space-between",
                alignItems: "center",
                gap: "20px",
              }}
            >
              <Button
                label={t("CS_COMMON_CANCEL")}
                onButtonClick={() => {
                  setDeletePopup(false);
                }}
              />
              <Button label={t("CS_COMMON_DELETE")} onButtonClick={handleDeleteError} />
            </div>
          </Fragment>
        </CustomPopUp>
      )}
      {imagePopupInfo && (
        <ImageModal
          imageInfo={imagePopupInfo}
          t={t}
          anchorRef={popupAnchor}
          handleOpenPopup={handleOpenPopup}
          handleCloseModal={() => {
            handleCloseImageModal();
          }}
        />
      )}
    </div>
  );
}

export default SelectReviewAccordion;
