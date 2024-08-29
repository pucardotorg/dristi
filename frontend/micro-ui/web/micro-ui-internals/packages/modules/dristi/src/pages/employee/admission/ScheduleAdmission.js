import React, { useContext, useEffect, useState } from "react";
import CustomChooseDate from "../../../components/CustomChooseDate";
import {
  Button,
  CardLabel,
  CardText,
  CustomDropdown,
  DateRange,
  EventCalendar,
  SubmitBar,
  TextInput,
  Toast,
} from "@egovernments/digit-ui-react-components";
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
  disabled = true,
  isCaseAdmitted = false,
  isSubmitBarDisabled = false,
  caseAdmittedSubmit = () => {},
}) {
  // const getNextNDates = (n) => {
  //   const today = new Date();
  //   const datesArray = [];

  //   for (let i = 1; i <= n; i++) {
  //     const nextDate = new Date(today);
  //     nextDate.setDate(today.getDate() + i);
  //     datesArray.push(formatDateInMonth(nextDate));
  //   }

  //   return datesArray;
  // };

  const [nextFiveDates, setNextFiveDates] = useState([]);
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
      isCaseAdmitted ? caseAdmittedSubmit(scheduleHearingParams) : setModalInfo({ ...modalInfo, page: 1 });
    }
  };

  const hearingTypeOptions = [
    {
      id: 1,
      type: "EVIDENCE",
      isactive: true,
      code: "EVIDENCE",
    },
    {
      id: 2,
      type: "ADMIN",
      isactive: true,
      code: "ADMIN",
    },
    {
      id: 3,
      type: "82_83_HEARING",
      isactive: true,
      code: "82_83_HEARING",
    },
    {
      id: 4,
      type: "NBW_HEARING",
      isactive: true,
      code: "NBW_HEARING",
    },
    {
      id: 5,
      type: "ADMISSION",
      isactive: true,
      code: "ADMISSION",
    },
    {
      id: 6,
      type: "PLEA",
      isactive: true,
      code: "PLEA",
    },
    {
      id: 7,
      type: "ARGUMENTS",
      isactive: true,
      code: "ARGUMENTS",
    },
    {
      id: 8,
      type: "JUDGEMENT",
      isactive: true,
      code: "JUDGEMENT",
    },
    {
      id: 9,
      type: "SENTENCE",
      isactive: true,
      code: "SENTENCE",
    },
    {
      id: 10,
      type: "BAIL",
      isactive: true,
      code: "BAIL",
    },
    {
      id: 11,
      type: "OTHERS",
      isactive: true,
      code: "OTHERS",
    },
  ];

  const dropdownConfig = {
    label: "HEARING_TYPE",
    type: "dropdown",
    name: "hearingType",
    optionsKey: "type",
    isMandatory: true,
    options: hearingTypeOptions,
  };

  function dateToEpoch(date) {
    return Math.floor(new Date(date).getTime());
  }

  const { data: dateResponse } = window?.Digit.Hooks.dristi.useJudgeAvailabilityDates(
    {
      SearchCriteria: {
        tenantId: Digit.ULBService.getCurrentTenantId(),
        fromDate: dateToEpoch(new Date(new Date().setDate(new Date().getDate() + 1))),
      },
    },
    {},
    "",
    true
  );

  const convertAvailableDatesToDateObjects = (availableDates) => {
    return availableDates.map((dateInfo) => ({
      ...dateInfo,
      date: new Date(dateInfo.date / 1),
    }));
  };

  const getNextNDates = (n, availableDates) => {
    const datesArray = [];

    for (let i = 0; i < n; i++) {
      if (i < availableDates.length) {
        const dateObject = availableDates[i].date;
        datesArray.push(formatDateInMonth(dateObject));
      } else {
        break;
      }
    }
    return datesArray;
  };

  useEffect(() => {
    if (dateResponse?.AvailableDates) {
      const availableDatesWithDateObjects = convertAvailableDatesToDateObjects(dateResponse.AvailableDates);
      const nextDates = getNextNDates(5, availableDatesWithDateObjects);
      setNextFiveDates(nextDates);
    }
  }, [dateResponse]);

  return (
    <div className="schedule-admission-main">
      {selectedChip && <CustomCaseInfoDiv t={t} data={submitModalInfo?.shortCaseInfo} style={{ marginTop: "24px" }} />}

      <CardText className="card-label-smaller">{t(config.label)}</CardText>
      {!isCaseAdmitted ? (
        <TextInput
          value={scheduleHearingParams?.purpose}
          className="field desktop-w-full"
          name={`${config.name}`}
          onChange={(e) => {
            setPurposeValue(e.target.value, config.name);
          }}
          disabled={disabled}
        />
      ) : (
        <CustomDropdown
          t={t}
          defaulValue={hearingTypeOptions[4]}
          onChange={(e) => {
            setPurposeValue(e, config.name);
            console.log(e);
          }}
          // value={userType}
          config={dropdownConfig}
        ></CustomDropdown>
      )}
      {!modalInfo?.showCustomDate && (
        <div>
          <CardText>{t("CS_SELECT_DATE")}</CardText>
          <CustomChooseDate
            data={nextFiveDates}
            selectedChip={selectedChip}
            handleClick={handleClickDate}
            scheduleHearingParams={scheduleHearingParams}
          />
        </div>
      )}

      {modalInfo?.showCustomDate ? (
        <h3>
          {scheduleHearingParams?.date}{" "}
          <span style={{ color: "#007E7E", fontWeight: "500" }} onClick={() => showCustomDateModal()}>
            {String(t("SELECT_ANOTHER_DATE"))}
          </span>
        </h3>
      ) : (
        <span className="select-custom-dates-main">
          <h3>{t("DATE_DONT_WORK")}</h3>
          <span className="select-custom-dates-child" onClick={() => showCustomDateModal()}>
            {String(t("CS_SELECT_CUSTOM_DATE"))}
          </span>
        </span>
      )}

      <div className="action-button-schedule-admission">
        <Button
          variation="secondary"
          onButtonClick={() => {
            modalInfo?.showCustomDate ? setModalInfo({ ...modalInfo, page: 0, showDate: false, showCustomDate: false }) : setShowModal(false);
          }}
          className="primary-label-btn back-from-schedule"
          label={"Back"}
        ></Button>
        <SubmitBar
          variation="primary"
          onSubmit={handleSubmit}
          className="primary-label-btn select-participant-submit"
          disabled={isSubmitBarDisabled}
          label={isCaseAdmitted ? t("GENERATE_ORDERS_LINK") : selectedChip ? t("CS_COMMON_CONTINUE") : t("CS_SELECT_PARTICIPANT")}
        ></SubmitBar>
      </div>

      {showErrorToast && <Toast error={true} label={t("ES_COMMON_PLEASE_ENTER_ALL_MANDATORY_FIELDS")} isDleteBtn={true} onClose={closeToast} />}
    </div>
  );
}

export default ScheduleAdmission;
