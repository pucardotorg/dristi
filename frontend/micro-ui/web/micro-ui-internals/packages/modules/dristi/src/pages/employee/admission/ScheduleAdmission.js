import React, { useContext, useEffect, useState } from "react";
import CustomChooseDate from "../../../components/CustomChooseDate";
import { Button, CardLabel, CardText, DateRange, EventCalendar, SubmitBar, TextInput, Toast } from "@egovernments/digit-ui-react-components";
import { Link } from "react-router-dom/cjs/react-router-dom.min";
import CustomCaseInfoDiv from "../../../components/CustomCaseInfoDiv";

function ScheduleAdmission({
  config,
  t,
  setShowModal,
  setModalInfo,
  modalInfo,
  selectedDate,
  selectedChip,
  setSelectedChip,
  handleChipClick,
  dateSelected,
  setDateSelected,
  showCustomDateModal,
  selectedCustomDate,
  setPurposeValue,
  setScheduleHearingParam,
  scheduleHearingParams,
  submitModalInfo,
}) {
  const getNextNDates = (n) => {
    const today = new Date();
    const monthNames = ["January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"];

    const formatWithSuffix = (day) => {
      if (day > 3 && day < 21) return `${day}th`;
      switch (day % 10) {
        case 1:
          return `${day}st`;
        case 2:
          return `${day}nd`;
        case 3:
          return `${day}rd`;
        default:
          return `${day}th`;
      }
    };

    const datesArray = [];

    for (let i = 1; i <= n; i++) {
      const nextDate = new Date(today);
      nextDate.setDate(today.getDate() + i);
      const day = formatWithSuffix(nextDate.getDate());
      const month = monthNames[nextDate.getMonth()];
      const year = nextDate.getFullYear();
      datesArray.push(`${day} ${month} ${year}`);
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

  const date = new Date(selectedCustomDate);

  const options = { day: "2-digit", month: "2-digit", year: "2-digit" };
  const formattedDate = date.toLocaleDateString("en-GB", options).replace(/\//g, "/");

  const handleSubmit = (props) => {
    if (!dateSelected && !modalInfo?.showCustomDate) {
      setShowErrorToast(true);
    } else {
      setScheduleHearingParam({ ...scheduleHearingParams, date: selectedCustomDate || dateSelected });
      setModalInfo({ ...modalInfo, page: 1 });
    }
  };

  return (
    <div>
      {dateSelected && <CustomCaseInfoDiv data={submitModalInfo?.shortCaseInfo} />}

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
          <CardText>Select a date</CardText>
          <CustomChooseDate
            data={nextFourDates}
            selectedChip={selectedChip}
            onChipClick={handleChipClick}
            dateSelected={dateSelected}
            setDateSelected={setDateSelected}
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
          label={dateSelected ? "Continue" : "Select Participants"}
        ></SubmitBar>
      </div>
      {modalInfo?.showCustomDate ? (
        <h3>
          {formattedDate}{" "}
          <span style={{ color: "#007E7E" }} onClick={() => showCustomDateModal()}>
            {String(t("Select Another Date"))}
          </span>
        </h3>
      ) : (
        <h3>
          {t("Dates don’t work?")}{" "}
          <span style={{ color: "#007E7E" }} onClick={() => showCustomDateModal()}>
            {String(t("Select Custom Date"))}
          </span>
        </h3>
      )}
      {showErrorToast && <Toast error={true} label={t("ES_COMMON_PLEASE_ENTER_ALL_MANDATORY_FIELDS")} isDleteBtn={true} onClose={closeToast} />}
    </div>
  );
}

export default ScheduleAdmission;
