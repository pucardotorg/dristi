import React, { useCallback, useEffect, useMemo, useState } from "react";
import CustomChooseDate from "../../../components/CustomChooseDate";
import { Button, CardText, CustomDropdown, SubmitBar, TextInput, Toast, Loader } from "@egovernments/digit-ui-react-components";
import CustomCaseInfoDiv from "../../../components/CustomCaseInfoDiv";
import { formatDateInMonth, getMDMSObj } from "../../../Utils";
import useGetStatuteSection from "../../../hooks/dristi/useGetStatuteSection";

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
  createAdmissionOrder = false,
  caseAdmittedSubmit = () => {},
  delayCondonationData,
  hearingDetails = [],
  isDelayApplicationPending,
  isDelayApplicationCompleted,
  caseDetails,
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
  const { data: hearingTypeData, isLoading } = useGetStatuteSection("Hearing", [{ name: "HearingType" }]);
  const isAdmissionHearingScheduled = useMemo(() => {
    if (!hearingDetails?.HearingList?.length) {
      return false;
    } else {
      return Boolean(hearingDetails?.HearingList?.find((hearing) => hearing?.hearingType === "ADMISSION"));
    }
  }, [hearingDetails]);
  console.log("delayCondonationData", delayCondonationData, isDelayApplicationPending, isDelayApplicationCompleted);

  const isDcaHearingScheduled = useMemo(() => {
    debugger;
    if (!hearingDetails?.HearingList?.length) {
      return false;
    } else {
      return Boolean(hearingDetails?.HearingList?.find((hearing) => hearing?.hearingType === "DELAY_CONDONATION_HEARING"));
    }
  }, [hearingDetails]);

  const hearingTypes = useMemo(() => {
    if (isDelayApplicationPending || isDelayApplicationCompleted || delayCondonationData?.isDcaSkippedInEFiling?.code === "NO") {
      if (!isDcaHearingScheduled && !isAdmissionHearingScheduled) {
        return (
          hearingTypeData?.HearingType?.filter((type) =>
            ["DELAY_CONDONATION_HEARING", "ADMISSION", "DELAY_CONDONATION_AND_ADMISSION"].includes(type?.code)
          ) || []
        );
      }
      if (!isDcaHearingScheduled && isAdmissionHearingScheduled) {
        return hearingTypeData?.HearingType?.filter((type) => !["ADMISSION", "DELAY_CONDONATION_AND_ADMISSION"].includes(type?.code)) || [];
      }
      if (isDcaHearingScheduled && !isAdmissionHearingScheduled) {
        return hearingTypeData?.HearingType?.filter((type) => ["ADMISSION"].includes(type?.code)) || [];
      }
      if (isDcaHearingScheduled && isAdmissionHearingScheduled) {
        return hearingTypeData?.HearingType || [];
      }
    } else {
      if (!isAdmissionHearingScheduled) {
        return hearingTypeData?.HearingType?.filter((type) => ["ADMISSION"].includes(type?.code)) || [];
      }
      return hearingTypeData?.HearingType || [];
    }
  }, [hearingTypeData, isDelayApplicationPending, isDelayApplicationCompleted, isAdmissionHearingScheduled, isDcaHearingScheduled]);

  const defaultHearingType = useMemo(() => {
    if (isDelayApplicationPending || isDelayApplicationCompleted || delayCondonationData?.isDcaSkippedInEFiling?.code === "NO") {
      if (!isDcaHearingScheduled) {
        return {
          id: 15,
          code: "DELAY_CONDONATION_HEARING",
          type: "DELAY_CONDONATION_HEARING",
          isactive: true,
        };
      }
      if (isDcaHearingScheduled && !isAdmissionHearingScheduled) {
        return {
          id: 5,
          type: "ADMISSION",
          isactive: true,
          code: "ADMISSION",
        };
      }
    } else {
      if (!isAdmissionHearingScheduled) {
        return {
          id: 5,
          type: "ADMISSION",
          isactive: true,
          code: "ADMISSION",
        };
      } else return null;
    }
  }, [isDelayApplicationPending, isDelayApplicationCompleted, isDcaHearingScheduled, isAdmissionHearingScheduled]);
  const closeToast = () => {
    setShowErrorToast(false);
  };
  useEffect(() => {
    const timer = setTimeout(() => {
      closeToast();
    }, 2000);

    return () => clearTimeout(timer);
  }, [closeToast]);

  const setPurpose = useCallback((props) => setPurposeValue(props), []);
  useEffect(() => {
    if (scheduleHearingParams?.purpose?.code !== defaultHearingType?.code) {
      setPurpose(defaultHearingType);
    }
  }, [setPurpose, defaultHearingType, scheduleHearingParams]);

  const handleSubmit = (props) => {
    if (!scheduleHearingParams?.date && !modalInfo?.showCustomDate) {
      setShowErrorToast(true);
    } else {
      isCaseAdmitted || createAdmissionOrder ? caseAdmittedSubmit(scheduleHearingParams) : setModalInfo({ ...modalInfo, page: 1 });
    }
  };

  const dropdownConfig = useMemo(() => {
    return {
      label: "HEARING_TYPE",
      type: "dropdown",
      name: "hearingType",
      optionsKey: "type",
      isMandatory: true,
      options: hearingTypes,
      defaultValue: defaultHearingType,
    };
  }, [hearingTypes, defaultHearingType]);

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
        <CustomDropdown
          t={t}
          onChange={(e) => {
            setPurposeValue(e, config.name);
          }}
          config={dropdownConfig}
          // defaulValue={defaultHearingType}
        ></CustomDropdown>
      ) : (
        <CustomDropdown
          t={t}
          onChange={(e) => {
            setPurposeValue(e, config.name);
          }}
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
          label={t("DOWNLOAD_CS_BACK")}
        ></Button>
        <SubmitBar
          variation="primary"
          onSubmit={handleSubmit}
          className="primary-label-btn select-participant-submit"
          disabled={isSubmitBarDisabled}
          label={
            isCaseAdmitted || createAdmissionOrder ? t("GENERATE_ORDERS_LINK") : selectedChip ? t("CS_COMMON_CONTINUE") : t("CS_SELECT_PARTICIPANT")
          }
        ></SubmitBar>
      </div>

      {showErrorToast && <Toast error={true} label={t("ES_COMMON_PLEASE_ENTER_ALL_MANDATORY_FIELDS")} isDleteBtn={true} onClose={closeToast} />}
    </div>
  );
}

export default ScheduleAdmission;
