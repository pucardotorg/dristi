import React, { useEffect, useMemo, useState } from "react";
import { Button, CardText, CustomDropdown, SubmitBar, TextInput, Toast, Modal, Loader, Banner } from "@egovernments/digit-ui-react-components";
import { formatDateInMonth } from "../../utils";
import { useTranslation } from "react-i18next";
import { useHistory, useLocation } from "react-router-dom/cjs/react-router-dom.min";
import useSearchCaseService from "@egovernments/digit-ui-module-dristi/src/hooks/dristi/useSearchCaseService";
import { HomeService, Urls } from "../../hooks/services";
import { InfoCard } from "@egovernments/digit-ui-components";

const hearingTypeOptions = [{}];

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

function ScheduleNextHearing({
  config = {
    headModal: "Set Next Hearing Date",
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
  const getSuggestedDates = (dateResponse) => {
    const dates = [];
    if (dateResponse?.Hearings?.[0]?.suggestedDates && dateResponse?.Hearings?.[0]?.availableDates) {
      return dateResponse?.Hearings?.[0]?.suggestedDates;
    }
    return [];
  };

  const getAvailableDates = (dateResponse) => {
    const dates = [];
    if (dateResponse?.Hearings?.[0]?.suggestedDates && dateResponse?.Hearings?.[0]?.availableDates) {
      return dateResponse?.Hearings?.[0]?.availableDates;
    }
    return [];
  };

  const extractUnitValue = (OptOutLimit) => {
    const configArray = OptOutLimit?.["SCHEDULER-CONFIG"]?.config;
    if (Array.isArray(configArray)) {
      const configItem = configArray.find((item) => item.identifier === "OPT_OUT_SELECTION_LIMIT");
      return configItem ? configItem.unit : null;
    }

    return null;
  };

  const fetchBasicUserInfo = async () => {
    const individualData = await window?.Digit.DRISTIService.searchIndividualUser(
      {
        Individual: {
          userUuid: [caseDetails?.auditDetails?.createdBy],
        },
      },
      { tenantId, limit: 1000, offset: 0 },
      "",
      caseDetails?.auditDetails?.createdBy
    );

    return individualData?.Individual?.[0]?.individualId;
  };

  const { filingNumber, status, hearingId } = Digit.Hooks.useQueryParams();
  const [modalInfo, setModalInfo] = useState(null);
  const [selectedChip, setSelectedChip] = React.useState(null);
  const [showErrorToast, setShowErrorToast] = useState(false);
  const [scheduleHearingParams, setScheduleHearingParam] = useState({ purpose: "Admission Purpose" });
  const [isSubmitDisabled, setIsSubmitDisabled] = useState(false);
  const [sucessOptOut, setSucessOptOut] = useState(false);
  const [OptOutLimitValue, setOptOutLimitValue] = useState(null);
  const location = useLocation();
  const referenceId = location?.state?.state?.params?.referenceId;

  const CustomCaseInfoDiv = Digit.ComponentRegistryService.getComponent("CustomCaseInfoDiv") || <React.Fragment></React.Fragment>;
  const CustomChooseDate = Digit.ComponentRegistryService.getComponent("CustomChooseDate") || <React.Fragment></React.Fragment>;
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

  const { data: applicationData } = Digit.Hooks.submissions.useSearchSubmissionService(
    {
      criteria: {
        filingNumber: filingNumber,
        tenantId: tenantId,
        applicationType: "RE_SCHEDULE",
        status: "COMPLETED",
      },
      tenantId,
    },
    {},
    "",
    true
  );

  const { data: dateResponse } = Digit.Hooks.home.useSearchReschedule(
    {
      SearchCriteria: {
        tenantId: Digit.ULBService.getCurrentTenantId(),
        rescheduledRequestId: [referenceId],
      },
    },
    { limit: 1, offset: 0 },
    "",
    !!referenceId
  );

  const nextFourDates = getSuggestedDates(dateResponse);
  const availableDates = getAvailableDates(dateResponse);

  const { data: OptOutLimit, isLoading: loading } = Digit.Hooks.useCustomMDMS(
    Digit.ULBService.getStateId(),
    "SCHEDULER-CONFIG",
    [
      {
        name: "config",
      },
    ],
    {
      cacheTime: 0,
    }
  );

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

  const handleClose = () => {
    history.goBack();
  };

  const handleSubmit = async (data) => {
    if (status !== "OPTOUT") {
      const dateArr = formatDateInMonth(new Date(data?.date))
        ?.split(" ")
        .map((date, i) => (i === 0 ? date.slice(0, date.length - 2) : date));
      const date = new Date(dateArr.join(" "));
      const reqBody = {
        order: {
          createdDate: new Date().getTime(),
          tenantId,
          cnrNumber,
          hearingNumber: applicationData.applicationList[0]?.hearingId,
          filingNumber: filingNumber,
          statuteSection: {
            tenantId,
          },
          orderType: "ASSIGNING_DATE_RESCHEDULED_HEARING",
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
              newHearingDate: `${dateArr[2]}-${date.getMonth() < 9 ? `0${date.getMonth() + 1}` : date.getMonth() + 1}-${
                dateArr[0] < 9 ? `0${dateArr[0]}` : dateArr[0]
              }`,
              hearingPurpose: data.purpose,
              orderType: {
                code: "ASSIGNING_DATE_RESCHEDULED_HEARING",
                type: "ASSIGNING_DATE_RESCHEDULED_HEARING",
                name: "ORDER_TYPE_ASSIGNING_DATE_RESCHEDULED_HEARING",
              },
            },
          },
        },
      };

      setIsSubmitDisabled(true);
      HomeService.customApiService(Urls.orderCreate, reqBody, { tenantId })
        .then(async (res) => {
          await HomeService.customApiService(Urls.pendingTask, {
            pendingTask: {
              name: "Create Order for rescheduling the hearing",
              entityType: "order-default",
              referenceId: `MANUAL_${referenceId}`,
              status: "RESCHEDULE_HEARING",
              assignedTo: [],
              assignedRole: [],
              cnrNumber: cnrNumber,
              filingNumber: filingNumber,
              isCompleted: true,
              stateSla: null,
              additionalDetails: {},
              tenantId,
            },
          });
          history.push(`/${window.contextPath}/employee/orders/generate-orders?filingNumber=${filingNumber}&orderNumber=${res.order.orderNumber}`);
          setIsSubmitDisabled(false);
        })
        .catch((err) => {
          setIsSubmitDisabled(false);
        });
    } else if (status && status === "OPTOUT") {
      const individualId = await fetchBasicUserInfo();
      setIsSubmitDisabled(true);
      HomeService.customApiService(
        Urls.submitOptOutDates,
        {
          OptOut: {
            tenantId: tenantId,
            individualId: individualId,
            caseId: filingNumber,
            rescheduleRequestId: referenceId,
            judgeId: "super",
            optOutDates: selectedChip,
          },
        },
        {}
      )
        .then(async () => {
          await HomeService.customApiService(Urls.pendingTask, {
            pendingTask: {
              name: "Completed",
              entityType: "order-default",
              referenceId: `MANUAL_${referenceId}`,
              status: "DRAFT_IN_PROGRESS",
              assignedTo: [],
              assignedRole: [],
              cnrNumber: cnrNumber,
              filingNumber: filingNumber,
              isCompleted: true,
              stateSla: null,
              additionalDetails: {},
              tenantId,
            },
          });
          setIsSubmitDisabled(false);
          setSucessOptOut(true);
        })
        .catch((err) => {
          setIsSubmitDisabled(false);
        });
    }
  };

  useEffect(() => {
    if (OptOutLimit) setOptOutLimitValue(extractUnitValue(OptOutLimit));
  }, [OptOutLimit]);

  if (isLoading) {
    return <Loader />;
  }

  return (
    <Modal
      headerBarMain={<Heading label={status === "OPTOUT" ? t("SELECT_OPT_OUT_DATES") : t(config.headModal)} />}
      headerBarEnd={<CloseBtn onClick={handleClose} />}
      hideSubmit={true}
      popupStyles={{
        maxWidth: "650px",
        width: "100%",
      }}
    >
      <div className="schedule-admission-main">
        {shortCaseInfo && <CustomCaseInfoDiv t={t} data={shortCaseInfo} style={{ marginTop: "24px" }} />}

        {status === "OPTOUT" && Array.isArray(selectedChip) && selectedChip.length > 0 && (
          <InfoCard
            className="payment-status-info-card"
            headerWrapperClassName="payment-status-info-header"
            populators={{
              name: "infocard",
            }}
            variant="default"
            text={"The date for the next hearing will be decided based on the selections made below."}
            label={"Please Note"}
            style={{ marginTop: "1.5rem" }}
            textStyle={{
              color: "#3D3C3C",
              margin: "0.5rem 0",
            }}
          />
        )}
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
              isSelectMulti={false}
              enabledData={availableDates}
            />
          </div>
        )}

        <div className="action-button-schedule-admission">
          <Button variation="secondary" onButtonClick={handleClose} className="primary-label-btn back-from-schedule" label={"Close"}></Button>
          <SubmitBar
            variation="primary"
            onSubmit={() => handleSubmit(scheduleHearingParams)}
            className="primary-label-btn select-participant-submit"
            label={status === "OPTOUT" ? "Done" : t("GENERATE_ORDERS_LINK")}
            disabled={
              (status === "OPTOUT" && Array.isArray(selectedChip) && selectedChip.length === 0) ||
              (status !== "OPTOUT" && !scheduleHearingParams?.date) ||
              isSubmitDisabled
            }
          ></SubmitBar>
        </div>

        {showErrorToast && <Toast error={true} label={t("ES_COMMON_PLEASE_ENTER_ALL_MANDATORY_FIELDS")} isDleteBtn={true} onClose={closeToast} />}
      </div>
    </Modal>
  );
}

export default ScheduleNextHearing;
