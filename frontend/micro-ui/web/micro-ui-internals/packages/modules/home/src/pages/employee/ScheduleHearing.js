import React, { useMemo, useState } from "react";
import { Button, CardText, CustomDropdown, SubmitBar, TextInput, Toast, Modal, Loader } from "@egovernments/digit-ui-react-components";
import { formatDateInMonth } from "../../utils";
import { useTranslation } from "react-i18next";
import { useHistory } from "react-router-dom/cjs/react-router-dom.min";
import useSearchCaseService from "@egovernments/digit-ui-module-dristi/src/hooks/dristi/useSearchCaseService";
import { HomeService, Urls } from "../../hooks/services";

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

const Heading = (props) => {
  return <h1 className="heading-m">{props.label}</h1>;
};

const Close = () => (
  <svg width="24" height="24" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
    <g clip-path="url(#clip0_4124_3214)">
      <path d="M19 6.41L17.59 5L12 10.59L6.41 5L5 6.41L10.59 12L5 17.59L6.41 19L12 13.41L17.59 19L19 17.59L13.41 12L19 6.41Z" fill="#0A0A0A" />
    </g>
    <defs>
      <clipPath id="clip0_4124_3214">
        <rect width="24" height="24" fill="white" />
      </clipPath>
    </defs>
  </svg>
);

const CloseBtn = (props) => {
  return (
    <div style={{ padding: "10px", cursor: "pointer" }} onClick={props.onClick}>
      <Close />
    </div>
  );
};

const customDateConfig = {
  headModal: "CS_SELECT_CUSTOM_DATE",
  label: "CS_HEARINGS_SCHEDULED",
  showBottomBar: true,
  buttonText: "CS_COMMON_CONFIRM",
};

function ScheduleHearing({
  config = {
    headModal: "CS_SCHEDULE_HEARING",
  },
  submitModalInfo = {
    shortCaseInfo: [
      {
        key: "CASE_NUMBER",
        value: "",
      },
      {
        key: "COURT_NAME",
        value: "Kerala City Criminal Court",
      },
      {
        key: "CASE_TYPE",
        value: "NIA S138",
      },
    ],
  },
  disabled = true,
  isCaseAdmitted = false,
  showPurposeOfHearing = false,
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

  const nextFourDates = getNextNDates(5);
  const [modalInfo, setModalInfo] = useState(null);
  const [selectedChip, setSelectedChip] = React.useState(null);
  const [showErrorToast, setShowErrorToast] = useState(false);
  const [scheduleHearingParams, setScheduleHearingParam] = useState({ purpose: "Admission Purpose" });
  const [selectedCustomDate, setSelectedCustomDate] = useState(new Date());
  const [isSubmitDisabled, setIsSubmitDisabled] = useState(false);

  const CustomCaseInfoDiv = Digit.ComponentRegistryService.getComponent("CustomCaseInfoDiv") || <React.Fragment></React.Fragment>;
  const CustomChooseDate = Digit.ComponentRegistryService.getComponent("CustomChooseDate") || <React.Fragment></React.Fragment>;
  const CustomCalendar = Digit.ComponentRegistryService.getComponent("CustomCalendar") || <React.Fragment></React.Fragment>;
  const { filingNumber, status } = Digit.Hooks.useQueryParams();
  const userInfo = JSON.parse(window.localStorage.getItem("user-info"));
  const userInfoType = useMemo(() => (userInfo?.type === "CITIZEN" ? "citizen" : "employee"), [userInfo]);
  const OrderWorkflowAction = Digit.ComponentRegistryService.getComponent("OrderWorkflowActionEnum") || {};
  const { t } = useTranslation();
  const history = useHistory();
  const shortCaseInfo = useMemo(() => {
    if (submitModalInfo?.shortCaseInfo) {
      return submitModalInfo?.shortCaseInfo.map((data) => {
        if (data.key === "CASE_NUMBER") {
          data.value = filingNumber || "";
        }
        return data;
      });
    }
    return null;
  }, [filingNumber, submitModalInfo?.shortCaseInfo]);

  const tenantId = window?.Digit.ULBService.getCurrentTenantId();

  const { data: caseData, isLoading } = useSearchCaseService(
    {
      criteria: [
        {
          filingNumber: filingNumber,
        },
      ],
      tenantId,
    },
    {},
    "dristi",
    filingNumber,
    filingNumber
  );
  const caseDetails = useMemo(() => caseData?.criteria[0]?.responseList[0], [caseData]);
  const cnrNumber = useMemo(() => caseDetails?.cnrNumber, [caseDetails]);

  const closeToast = () => {
    setShowErrorToast(false);
  };

  const setPurposeValue = (value, input) => {
    setScheduleHearingParam({ ...scheduleHearingParams, purpose: isCaseAdmitted ? value : value.code });
  };

  const handleClickDate = (label) => {
    const newSelectedChip = selectedChip === label ? null : label;
    setSelectedChip(newSelectedChip);
    setScheduleHearingParam({
      ...scheduleHearingParams,
      date: newSelectedChip,
    });
  };

  const showCustomDateModal = () => {
    setModalInfo({ ...modalInfo, showDate: true });
  };

  const onCalendarConfirm = () => {
    setModalInfo({ ...modalInfo, page: 0, showDate: false, showCustomDate: true });
    setSelectedChip(null);
  };

  const handleSelect = (date) => {
    setScheduleHearingParam({ ...scheduleHearingParams, date: formatDateInMonth(date) });
    setSelectedCustomDate(date);
  };

  const handleClose = () => {
    history.push(`/${window?.contextPath}/${userInfoType}/home/home-pending-task`, { taskType: { code: "case", name: "Case" } });
  };

  const handleSubmit = (data) => {
    const dateArr = data.date.split(" ").map((date, i) => (i === 0 ? date.slice(0, date.length - 2) : date));
    const date = new Date(dateArr.join(" "));
    const reqBody = {
      order: {
        createdDate: new Date().getTime(),
        tenantId,
        cnrNumber,
        filingNumber: filingNumber,
        statuteSection: {
          tenantId,
        },
        orderType: "SCHEDULE_OF_HEARING_DATE",
        status: "",
        isActive: true,
        workflow: {
          action: OrderWorkflowAction.SAVE_DRAFT,
          comments: "Creating order",
          assignes: null,
          rating: null,
          documents: [{}],
        },
        documents: [],
        additionalDetails: {
          formdata: {
            hearingDate: `${dateArr[2]}-${date.getMonth() < 9 ? `0${date.getMonth() + 1}` : date.getMonth() + 1}-${
              dateArr[0] < 9 ? `0${dateArr[0]}` : dateArr[0]
            }`,
            hearingPurpose: data.purpose,
            orderType: {
              code: "SCHEDULE_OF_HEARING_DATE",
              type: "SCHEDULE_OF_HEARING_DATE",
              name: "ORDER_TYPE_SCHEDULE_OF_HEARING_DATE",
            },
          },
        },
      },
    };
    if (status !== "OPTOUT") {
      setIsSubmitDisabled(true);
      HomeService.customApiService(Urls.orderCreate, reqBody, { tenantId })
        .then(async (res) => {
          await HomeService.customApiService(Urls.pendingTask, {
            pendingTask: {
              name: "Schedule Hearing",
              entityType: "case",
              referenceId: `MANUAL_${caseDetails?.filingNumber}`,
              status: "SCHEDULE_HEARING",
              assignedTo: [],
              assignedRole: ["JUDGE_ROLE"],
              cnrNumber: null,
              filingNumber: caseDetails?.filingNumber,
              isCompleted: true,
              additionalDetails: {},
              tenantId,
            },
          });
          history.push(`/${window.contextPath}/employee/orders/generate-orders?filingNumber=${filingNumber}&orderNumber=${res.order.orderNumber}`);
          setIsSubmitDisabled(false);
        })
        .catch((err) => {
          setIsSubmitDisabled(false);
          console.log("err", err);
        });
    } else if (status && status === "OPTOUT") {
      handleClose();
    }
  };

  if (isLoading) {
    return <Loader />;
  }

  return (
    <Modal
      headerBarMain={<Heading label={t(config.headModal)} />}
      headerBarEnd={<CloseBtn onClick={handleClose} />}
      hideSubmit={true}
      popupStyles={{
        maxWidth: "650px",
        width: "100%",
      }}
    >
      <div className="schedule-admission-main">
        {shortCaseInfo && <CustomCaseInfoDiv t={t} data={shortCaseInfo} style={{ marginTop: "24px" }} />}

        {config.label && <CardText className="card-label-smaller">{t(config.label)}</CardText>}
        {!isCaseAdmitted ? (
          showPurposeOfHearing ? (
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
            <React.Fragment></React.Fragment>
          )
        ) : (
          <CustomDropdown
            t={t}
            defaulValue={hearingTypeOptions[4]}
            onChange={(e) => {
              setPurposeValue(e, config.name);
              console.log(e);
            }}
            config={dropdownConfig}
          ></CustomDropdown>
        )}
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
          <Button variation="secondary" onButtonClick={handleClose} className="primary-label-btn back-from-schedule" label={"Close"}></Button>
          <SubmitBar
            variation="primary"
            onSubmit={() => handleSubmit(scheduleHearingParams)}
            className="primary-label-btn select-participant-submit"
            label={t("GENERATE_ORDERS_LINK")}
            disabled={!scheduleHearingParams?.date || isSubmitDisabled}
          ></SubmitBar>
        </div>

        {showErrorToast && <Toast error={true} label={t("ES_COMMON_PLEASE_ENTER_ALL_MANDATORY_FIELDS")} isDleteBtn={true} onClose={closeToast} />}
        {modalInfo?.showDate && (
          <Modal
            headerBarMain={<Heading label={t(customDateConfig.headModal)} />}
            headerBarEnd={<CloseBtn onClick={() => setModalInfo({ ...modalInfo, page: 0, showDate: false, showCustomDate: false })} />}
            hideSubmit={true}
            popmoduleClassName={"custom-date-selector-modal"}
          >
            <CustomCalendar
              config={customDateConfig}
              t={t}
              onCalendarConfirm={onCalendarConfirm}
              handleSelect={handleSelect}
              selectedCustomDate={selectedCustomDate}
              tenantId={tenantId}
            />
          </Modal>
        )}
      </div>
    </Modal>
  );
}

export default ScheduleHearing;
