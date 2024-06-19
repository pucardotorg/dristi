import React, { useEffect, useState } from "react";
import DependentCheckBoxComponent from "../../../components/DependentCheckBoxComponent";
import { Button, CardHeader, CardLabel, CardText, SubmitBar, Toast } from "@egovernments/digit-ui-react-components";
import { formatDateInMonth } from "../../../Utils";

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
  const isObjectEmpty = (obj) => {
    return Object.keys(obj).length === 0;
  };
  const isEmpty = isObjectEmpty(selectedValues);
  const onSubmitSchedule = (props) => {
    if (isEmpty) {
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
    <div>
      <CardLabel>{t(config?.header)}</CardLabel>
      {config?.checkBoxText && (
        <CardText>
          {t(config?.checkBoxText)}
          {scheduleHearingParams?.date} ?
        </CardText>
      )}
      <DependentCheckBoxComponent t={t} options={config} onInputChange={handleInputChange} selectedValues={selectedValues} />
      <div className="action-button-application">
        <Button
          variation="secondary"
          onButtonClick={() => setModalInfo({ ...modalInfo, page: 0 })}
          className="primary-label-btn"
          label={"Back"}
        ></Button>

        <SubmitBar
          variation="primary"
          onSubmit={(props) => {
            onSubmitSchedule(props);
          }}
          className="primary-label-btn"
          label={"Schedule"}
        ></SubmitBar>
      </div>
      {showErrorToast && <Toast error={true} label={t("ES_COMMON_PLEASE_ENTER_ALL_MANDATORY_FIELDS")} isDleteBtn={true} onClose={closeToast} />}
    </div>
  );
}

export default SelectParticipant;
