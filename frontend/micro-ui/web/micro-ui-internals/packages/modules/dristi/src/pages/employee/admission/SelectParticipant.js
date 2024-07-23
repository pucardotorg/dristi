import { Button, CardLabel, SubmitBar, Toast } from "@egovernments/digit-ui-react-components";
import isEmpty from "lodash/isEmpty";
import React, { useCallback, useEffect, useState } from "react";
import DependentCheckBoxComponent from "../../../components/DependentCheckBoxComponent";

function SelectParticipant({
  config,
  setShowModal,
  modalInfo,
  setModalInfo,
  scheduleHearingParams,
  setScheduleHearingParam,
  selectedValues,
  setSelectedValues,
  handleInputChange,
  handleScheduleCase,
  t,
}) {
  const [showErrorToast, setShowErrorToast] = useState(false);

  const onSubmitSchedule = (props) => {
    const isInvalid =
      Object.keys(selectedValues).length === 0 ||
      !Object.values(selectedValues).every((value) => (value ? Object.values(value).some((innerVal) => innerVal) : false));
    if (isInvalid) {
      setShowErrorToast(true);
    } else {
      handleScheduleCase({ ...scheduleHearingParams, participant: selectedValues });
    }
  };
  const closeToast = () => {
    setShowErrorToast(false);
  };
  useEffect(() => {
    const timer = setTimeout(() => {
      closeToast();
    }, 2000);

    return () => clearTimeout(timer);
  }, [closeToast]);
  return (
    <div className="select-participants-main-div">
      <CardLabel className={"choose-participants-heading"}>{t(config?.header)}</CardLabel>
      {config?.checkBoxText && (
        <span className="participants-present">
          <h2>{t(config?.checkBoxText)}</h2>
          <span>{scheduleHearingParams?.date} ? </span>
        </span>
      )}
      <DependentCheckBoxComponent t={t} options={config} onInputChange={handleInputChange} selectedValues={selectedValues} />
      <div className="select-participants-submit-bar">
        <Button
          variation="secondary"
          onButtonClick={() => setModalInfo({ ...modalInfo, page: 0 })}
          className="primary-label-btn select-back-button"
          label={"Back"}
        ></Button>

        <SubmitBar
          variation="primary"
          onSubmit={(props) => {
            onSubmitSchedule(props);
          }}
          className="primary-label-btn select-schedule-button"
          label={"Schedule"}
        ></SubmitBar>
      </div>
      {showErrorToast && <Toast error={true} label={t("ES_COMMON_PLEASE_ENTER_ALL_MANDATORY_FIELDS")} isDleteBtn={true} onClose={closeToast} />}
    </div>
  );
}

export default SelectParticipant;
