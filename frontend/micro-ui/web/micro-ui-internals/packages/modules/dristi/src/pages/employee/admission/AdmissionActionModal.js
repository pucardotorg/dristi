import { CardText, Modal, Toast } from "@egovernments/digit-ui-react-components";
import React, { useCallback, useEffect, useMemo, useState } from "react";
import { FormComposerV2 } from "@egovernments/digit-ui-react-components";

import { modalConfig } from "../../citizen/FileCase/Config/admissionActionConfig";
import { useHistory } from "react-router-dom/cjs/react-router-dom.min";
import CustomSubmitModal from "../../../components/CustomSubmitModal";
import ScheduleAdmission from "./ScheduleAdmission";
import SelectParticipant from "./SelectParticipant";
import CustomCalendar from "../../../components/CustomCalendar";
import { WhiteRightArrow } from "../../../icons/svgIndex";
import { formatDateInMonth } from "../../../Utils";

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
function AdmissionActionModal({
  t,
  setShowModal,
  setSubmitModalInfo,
  submitModalInfo,
  modalInfo,
  setModalInfo,
  path,
  handleSendCaseBack,
  handleAdmitCase,
  handleScheduleCase,
  updatedConfig,
  tenantId,
  // hearingDetails,
  handleScheduleNextHearing,
  disabled,
  filingNumber,
  isCaseAdmitted = false,
}) {
  const history = useHistory();
  const [showErrorToast, setShowErrorToast] = useState(false);
  const [label, setLabel] = useState(false);

  const closeToast = () => {
    setShowErrorToast(false);
  };

  useEffect(() => {
    const timer = setTimeout(() => {
      closeToast();
    }, 2000);

    return () => clearTimeout(timer);
  }, [closeToast]);

  const stepItems = useMemo(() => {
    return modalConfig.map((step) => {
      const texts = {};
      for (const key in step?.texts) {
        texts[key] = t(step?.texts[key]);
      }
      return { ...step, texts };
    });
  }, [t]);

  const [scheduleHearingParams, setScheduleHearingParam] = useState({ purpose: "Admission Purpose" });

  const onSubmit = (props, wordLimit) => {
    const words = props?.commentForLitigant?.trim()?.split(/\s+/);
    if (!props?.commentForLitigant) {
      setShowErrorToast(true);
      setLabel("ES_COMMON_PLEASE_ENTER_ALL_MANDATORY_FIELDS");
      return;
    }
    if (words?.length >= wordLimit) {
      setShowErrorToast(true);
      setLabel(`ES_WORD_COUNT_LIMIT ${wordLimit}`);
    } else {
      handleSendCaseBack(props);
    }
  };
  const showSuccessModal = (modalInfo) => {
    if (!modalInfo) return false;
    const { page, type } = modalInfo;
    return (page === 1 && (type === "admitCase" || type === "sendCaseBack")) || (page === 2 && type === "schedule");
  };
  const [selectedCustomDate, setSelectedCustomDate] = useState(new Date());

  const handleSelect = (date) => {
    setScheduleHearingParam({ ...scheduleHearingParams, date: formatDateInMonth(date) });
    setSelectedCustomDate(date);
  };
  const onCalendarConfirm = () => {
    setModalInfo({ ...modalInfo, page: 0, showDate: false, showCustomDate: true });
    setSelectedChip(null);
  };
  const [selectedChip, setSelectedChip] = React.useState(null);

  const setPurposeValue = (value, input) => {
    setScheduleHearingParam({ ...scheduleHearingParams, purpose: value.code });
  };

  const showCustomDateModal = () => {
    setModalInfo({ ...modalInfo, showDate: true });
  };
  const [selectedValues, setSelectedValues] = useState({});

  const handleInputChange = (values) => {
    setSelectedValues(values);
  };
  const handleClickDate = (label) => {
    const newSelectedChip = selectedChip === label ? null : label;
    setSelectedChip(newSelectedChip);
    setScheduleHearingParam({
      ...scheduleHearingParams,
      date: newSelectedChip,
    });
  };

  return (
    <React.Fragment>
      {modalInfo?.page == 0 && modalInfo?.type === "sendCaseBack" && (
        <Modal
          headerBarMain={<Heading label={t(stepItems[0].headModal)} />}
          headerBarEnd={<CloseBtn onClick={() => setShowModal(false)} />}
          hideSubmit={true}
          popmoduleClassName={"send-case-back-modal"}
        >
          <FormComposerV2
            config={[stepItems[0]]}
            t={t}
            noBoxShadow
            inline={false}
            label={t("CORE_COMMON_SEND")}
            onSecondayActionClick={() => {
              setShowModal(false);
            }}
            // onFormValueChange={onFormValueChange}
            headingStyle={{ textAlign: "center" }}
            cardStyle={{ minWidth: "100%", padding: 20, display: "flex", flexDirection: "column", alignItems: "center" }}
            onSubmit={(props) => onSubmit(props, stepItems[0]?.wordCount)}
            submitInForm
            // className={"registration-select-name"}
            secondaryActionLabel={t("CORE_LOGOUT_CANCEL")}
            buttonStyle={{ alignSelf: "center", minWidth: "50%" }}
            actionClassName="e-filing-action-bar"
          ></FormComposerV2>
          {showErrorToast && <Toast error={true} label={t(label)} isDleteBtn={true} onClose={closeToast} />}
        </Modal>
      )}
      {modalInfo?.page == 0 && modalInfo?.type === "admitCase" && (
        <Modal
          headerBarMain={<Heading label={t(stepItems[1].headModal)} />}
          actionSaveLabel={t(stepItems[1]?.submitText)}
          headerBarEnd={<CloseBtn onClick={() => setShowModal(false)} />}
          actionSaveOnSubmit={(props) => handleAdmitCase(props)}
        >
          <CardText>{t(stepItems[1]?.text)}</CardText>
        </Modal>
      )}

      {modalInfo?.page == 0 && modalInfo?.type === "schedule" && (
        <Modal
          headerBarMain={<Heading label={t(stepItems[2].headModal)} />}
          headerBarEnd={<CloseBtn onClick={() => setShowModal(false)} />}
          hideSubmit={true}
          popupStyles={{ width: "917px" }}
        >
          <ScheduleAdmission
            config={stepItems[2]}
            t={t}
            setShowModal={setShowModal}
            setModalInfo={setModalInfo}
            modalInfo={modalInfo}
            selectedChip={selectedChip}
            setSelectedChip={setSelectedChip}
            showCustomDateModal={showCustomDateModal}
            setPurposeValue={setPurposeValue}
            scheduleHearingParams={scheduleHearingParams}
            setScheduleHearingParam={setScheduleHearingParam}
            submitModalInfo={submitModalInfo}
            handleClickDate={handleClickDate}
            disabled={disabled}
            isCaseAdmitted={isCaseAdmitted}
          />
        </Modal>
      )}
      {modalInfo?.page == 1 && modalInfo?.type === "schedule" && (
        <Modal
          headerBarMain={<Heading label={t(stepItems[2].headModal)} />}
          headerBarEnd={<CloseBtn onClick={() => setShowModal(false)} />}
          hideSubmit={true}
          popmoduleClassName={"schedule-admission-select-participants-modal"}
        >
          <SelectParticipant
            config={updatedConfig}
            setShowModal={setShowModal}
            modalInfo={modalInfo}
            setModalInfo={setModalInfo}
            scheduleHearingParams={scheduleHearingParams}
            setScheduleHearingParam={setScheduleHearingParam}
            handleInputChange={handleInputChange}
            selectedValues={selectedValues}
            setSelectedValues={setSelectedValues}
            handleScheduleCase={handleScheduleCase}
            setShowErrorToast={setShowErrorToast}
            t={t}
          />
          {showErrorToast && <Toast error={true} label={t("ES_COMMON_PLEASE_ENTER_ALL_MANDATORY_FIELDS")} isDleteBtn={true} onClose={closeToast} />}
        </Modal>
      )}
      {modalInfo?.showDate && (
        <Modal
          headerBarMain={<Heading label={t(stepItems[3].headModal)} />}
          headerBarEnd={<CloseBtn onClick={() => setModalInfo({ ...modalInfo, page: 0, showDate: false, showCustomDate: false })} />}
          // actionSaveLabel={t("CS_COMMON_CONFIRM")}
          hideSubmit={true}
          popmoduleClassName={"custom-date-selector-modal"}

          // actionSaveOnSubmit={onSelect}
        >
          <CustomCalendar
            config={stepItems[3]}
            t={t}
            onCalendarConfirm={onCalendarConfirm}
            handleSelect={handleSelect}
            selectedCustomDate={selectedCustomDate}
            tenantId={tenantId}
          />
        </Modal>
      )}
      {showSuccessModal(modalInfo) && (
        <Modal
          actionSaveLabel={
            <div>
              {t(submitModalInfo?.nextButtonText)}
              {submitModalInfo?.isArrow && <WhiteRightArrow />}
            </div>
          }
          actionCancelLabel={t(submitModalInfo?.backButtonText)}
          actionCancelOnSubmit={() => {
            history.push(`/employee`);
          }}
          actionSaveOnSubmit={() => {
            if (submitModalInfo?.nextButtonText === "SCHEDULE_NEXT_HEARING") {
              handleScheduleNextHearing();
            } else {
              history.push(`/employee`);
            }
          }}
          className="case-types"
          formId="modal-action"
        >
          <CustomSubmitModal submitModalInfo={submitModalInfo} t={t} />
        </Modal>
      )}
    </React.Fragment>
  );
}

export default AdmissionActionModal;
