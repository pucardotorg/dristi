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
import useSearchCaseService from "../hooks/dristi/useSearchCaseService";
import { CaseWorkflowState } from "../Utils/caseWorkflow";
import ReactTooltip from "react-tooltip";

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

function SelectReviewAccordion({ t, config, onSelect, formData = {}, errors, formState, control, setError }) {
  const roles = Digit.UserService.getUser()?.info?.roles;
  const isScrutiny = useMemo(() => roles.some((role) => role.code === "CASE_REVIEWER"), [roles]);
  const isJudge = useMemo(() => roles.some((role) => role.code === "CASE_APPROVER"), [roles]);
  const isPrevScrutiny = config?.isPrevScrutiny || false;
  const [isOpen, setOpen] = useState(true);
  const history = useHistory();
  const urlParams = new URLSearchParams(window.location.search);
  const caseId = urlParams.get("caseId");
  const [scrutinyError, setScrutinyError] = useState("");
  const [showImageModal, setShowImageModal] = useState({ openModal: false, imageInfo: {} });
  const popupAnchor = useRef();
  const tenantId = window?.Digit.ULBService.getCurrentTenantId();

  const { data: caseData } = useSearchCaseService(
    {
      criteria: [
        {
          caseId: caseId,
        },
      ],
      tenantId,
    },
    {},
    "dristi",
    caseId,
    caseId
  );

  const caseDetails = useMemo(
    () => ({
      ...caseData?.criteria?.[0]?.responseList?.[0],
    }),
    [caseData]
  );

  const state = useMemo(() => caseDetails?.status, [caseDetails]);

  const isCaseReAssigned = useMemo(() => state === CaseWorkflowState.CASE_RE_ASSIGNED, [state]);
  const isDraftInProgress = state === CaseWorkflowState.DRAFT_IN_PROGRESS;

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

  const handleClickImage = (e, configKey, name, index = null, fieldName, data, inputlist = [], dataError = {}) => {
    setValue(
      "scrutinyMessage",
      {
        name,
        index,
        fieldName,
        configKey,
        data,
        inputlist,
        dataError,
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
  let showFlagIcon = isScrutiny ? true : false;
  return (
    <div className="accordion-wrapper" onClick={() => {}}>
      <div className={`accordion-title ${isOpen ? "open" : ""}`} onClick={() => setOpen(!isOpen)}>
        <span>
          {config?.number}. {t(config?.label)}
        </span>
        <span className="reverse-arrow">
          <CustomArrowDownIcon />
        </span>
      </div>
      <div className={`accordion-item ${!isOpen ? "collapsed" : ""}`}>
        <div className="accordion-content">
          {inputs.map((input, index) => {
            const sectionValue = formData && formData[config.key] && formData[config.key]?.[input.name];
            const sectionError = sectionValue?.scrutinyMessage?.FSOError;
            const prevSectionError = input?.prevErrors?.scrutinyMessage?.FSOError;
            let bgclassname = sectionError && isScrutiny ? "error" : "";
            bgclassname = sectionError && isCaseReAssigned ? "preverror" : bgclassname;
            const sectionErrorClassname = sectionError === prevSectionError ? "prevsection" : "section";
            if (isPrevScrutiny) {
              showFlagIcon = prevSectionError ? true : false;
              bgclassname = prevSectionError ? "preverror" : "";
            }
            if (input?.disableScrutiny) {
              showFlagIcon = false;
            }
            return (
              <div className={`content-item ${bgclassname}`}>
                <div className="item-header">
                  <div className="header-left">
                    {input?.icon && <Icon icon={input?.icon} />}
                    <span>{t(input?.label)}</span>
                  </div>
                  {!isScrutiny && !isJudge && (isCaseReAssigned || isDraftInProgress) && (
                    <div
                      className="header-right"
                      onClick={(e) => {
                        history.push(`?caseId=${caseId}&selected=${input?.key}`);
                      }}
                    >
                      <EditPencilIcon />
                    </div>
                  )}
                  {showFlagIcon && input?.data?.length > 0 && (
                    <div
                      style={{ cursor: "pointer" }}
                      onClick={(e) => {
                        handleOpenPopup(e, config.key, input?.name);
                      }}
                      key={index}
                    >
                      {sectionError ? (
                        <React.Fragment>
                          <span style={{ color: "#77787B", position: "relative" }} data-tip data-for={`Click`}>
                            {" "}
                            <EditPencilIcon />
                          </span>
                          <ReactTooltip id={`Click`} place="bottom" content={t("CS_CLICK_TO_EDIT") || ""}>
                            {t("CS_CLICK_TO_EDIT")}
                          </ReactTooltip>
                        </React.Fragment>
                      ) : (
                        <FlagIcon />
                      )}
                    </div>
                  )}
                </div>
                {sectionError && isScrutiny && (
                  <div className={`scrutiny-error ${sectionErrorClassname}`}>
                    {prevSectionError === sectionError ? (
                      <span style={{ color: "#4d83cf", fontWeight: 300 }}>{t("CS_PREVIOUS_ERROR")}</span>
                    ) : (
                      <FlagIcon isError={true} />
                    )}
                    {sectionError}
                  </div>
                )}
                {Array.isArray(input.data) &&
                  input.data.map((item, index) => {
                    const dataErrors = sectionValue?.form?.[index];
                    const prevDataErrors = input?.prevErrors?.form?.[index] || {};
                    const titleHeading = input.name === "chequeDetails" ? true : false;
                    const updatedConfig = input?.config?.filter((config) => {
                      if (!config?.dependentOn || !config?.dependentValue) {
                        return true;
                      } else {
                        if (extractValue(item.data, config?.dependentOn) === config?.dependentValue) {
                          return true;
                        }
                        return false;
                      }
                    });
                    return (
                      <CustomReviewCard
                        isScrutiny={isScrutiny}
                        config={updatedConfig}
                        titleIndex={index + 1}
                        data={item?.data}
                        key={index}
                        dataIndex={index}
                        t={t}
                        handleOpenPopup={handleOpenPopup}
                        handleClickImage={handleClickImage}
                        setShowImageModal={setShowImageModal}
                        formData={formData}
                        input={input}
                        dataErrors={dataErrors}
                        prevDataErrors={prevDataErrors}
                        configKey={config.key}
                        titleHeading={titleHeading}
                        isPrevScrutiny={isPrevScrutiny}
                        isCaseReAssigned={isCaseReAssigned}
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
              maxlength={config.textAreaMaxLength || "255"}
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
                    handleClosePopup();
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
      {(imagePopupInfo || showImageModal.openModal) && (
        <ImageModal
          imageInfo={showImageModal.openModal ? showImageModal.imageInfo : imagePopupInfo}
          t={t}
          anchorRef={popupAnchor}
          showFlag={showImageModal.openModal ? false : true}
          handleOpenPopup={!showImageModal.openModal && handleOpenPopup}
          handleCloseModal={() => {
            if (showImageModal.openModal) {
              setShowImageModal({ showImageModal: false, imageInfo: {} });
            } else handleCloseImageModal();
          }}
          isPrevScrutiny={isPrevScrutiny}
          disableScrutiny={false}
        />
      )}
    </div>
  );
}

export default SelectReviewAccordion;
