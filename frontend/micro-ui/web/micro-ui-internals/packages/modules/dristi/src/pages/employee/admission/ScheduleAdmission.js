import React, { useContext, useEffect, useState } from "react";
import CustomChooseDate from "../../../components/CustomChooseDate";
import { Button, CardLabel, CardText, DateRange, EventCalendar, SubmitBar, TextInput, Toast } from "@egovernments/digit-ui-react-components";
import CustomCaseInfoDiv from "../../../components/CustomCaseInfoDiv";
import { formatDateInMonth } from "../../../Utils";

function ScheduleAdmission({
  config,
  t,
  setShowModal,
  setModalInfo,
  modalInfo,
  selectedChip,
  showCustomDateModal,
  setPurposeValue,
  setScheduleHearingParam,
  scheduleHearingParams,
  submitModalInfo,
  handleClickDate,
}) {
  const getNextNDates = (n) => {
    const today = new Date();

    const datesArray = [];

    for (let i = 1; i <= n; i++) {
      const nextDate = new Date(today);
      nextDate.setDate(today.getDate() + i);
      datesArray.push(formatDateInMonth(nextDate));
    }

    return datesArray;
  };

  const nextFourDates = getNextNDates(3);
  const [showErrorToast, setShowErrorToast] = useState(false);
  const closeToast = () => {
    setShowErrorToast(false);
  };
  useEffect(() => {
    const timer = setTimeout(() => {
      closeToast();
    }, 2000);

    return () => clearTimeout(timer);
  }, [closeToast]);

  const handleSubmit = (props) => {
    if (!scheduleHearingParams?.date && !modalInfo?.showCustomDate) {
      setShowErrorToast(true);
    } else {
      setModalInfo({ ...modalInfo, page: 1 });
    }
  };
  return (
    <div>
      {selectedChip && <CustomCaseInfoDiv t={t} data={submitModalInfo?.shortCaseInfo} style={{ marginTop: "24px" }} />}

      <CardText className="card-label-smaller">{t(config.label)}</CardText>
      <TextInput
        value={scheduleHearingParams?.purpose}
        className="field desktop-w-full"
        name={`${config.name}`}
        onChange={(e) => {
          setPurposeValue(e.target.value, config.name);
        }}
        disabled={true}
      />
      {!modalInfo?.showCustomDate && (
        <div>
          <CardText>{t("CS_SELECT_DATE")}</CardText>
          <CustomChooseDate
            data={nextFourDates}
            selectedChip={selectedChip}
            handleClick={handleClickDate}
            scheduleHearingParams={scheduleHearingParams}
          />
        </div>
      )}

      <div className="action-button-application">
        <Button
          variation="secondary"
          onButtonClick={() => {
            modalInfo?.showCustomDate ? setModalInfo({ ...modalInfo, page: 0, showDate: false, showCustomDate: false }) : setShowModal(false);
          }}
          className="primary-label-btn"
          label={"Back"}
        ></Button>
        <SubmitBar
          variation="primary"
          onSubmit={handleSubmit}
          className="primary-label-btn"
          label={selectedChip ? t("CS_COMMON_CONTINUE") : t("CS_SELECT_PARTICIPANT")}
        ></SubmitBar>
      </div>
      {modalInfo?.showCustomDate ? (
        <h3>
          {scheduleHearingParams?.date}{" "}
          <span style={{ color: "#007E7E" }} onClick={() => showCustomDateModal()}>
            {String(t("SELECT_ANOTHER_DATE"))}
          </span>
        </h3>
      ) : (
        <h3>
          {t("DATE_DONT_WORK")}{" "}
          <span style={{ color: "#007E7E" }} onClick={() => showCustomDateModal()}>
            {String(t("CS_SELECT_CUSTOM_DATE"))}
          </span>
        </h3>
      )}
      {showErrorToast && <Toast error={true} label={t("ES_COMMON_PLEASE_ENTER_ALL_MANDATORY_FIELDS")} isDleteBtn={true} onClose={closeToast} />}
    </div>
  );
}

export default ScheduleAdmission;
