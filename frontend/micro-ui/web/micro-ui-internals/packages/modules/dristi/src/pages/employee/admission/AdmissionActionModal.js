import { ArrowForward, ArrowRightInbox, Banner, Card, CardText, Modal, TextArea, Toast } from "@egovernments/digit-ui-react-components";
import React, { useEffect, useMemo, useState } from "react";
import { FormComposerV2 } from "@egovernments/digit-ui-react-components";

import { modalConfig, selectParticipantConfig } from "../../citizen/FileCase/Config/admissionActionConfig";
import { useHistory } from "react-router-dom/cjs/react-router-dom.min";
import CustomSubmitModal from "../../../components/CustomSubmitModal";
import ScheduleAdmission from "./ScheduleAdmission";
import SelectParticipant from "./SelectParticipant";
import { Calendar } from "react-date-range";
import CustomCalendar from "../../../components/CustomCalendar";
import { WhiteRightArrow } from "../../../icons/svgIndex";

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
    <div style={{ padding: "10px" }} onClick={props.onClick}>
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
}) {
  const [reasons, setReasons] = useState(null);
  const history = useHistory();
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
  const stepItems = useMemo(() =>
    modalConfig.map(
      (step) => {
        const texts = {};
        for (const key in step.texts) {
          texts[key] = t(step.texts[key]);
        }
        return { ...step, texts };
      },
      [modalConfig]
    )
  );
  const [scheduleHearingParams, setScheduleHearingParam] = useState({ purpose: "Admission Purpose" });
  const [sendCaseBack, setSendCaseBack] = useState({});

  const onSubmit = (props) => {
    if (!props?.commentForLitigant) {
      setShowErrorToast(true);
    } else {
      handleSendCaseBack(props);
    }
  };
  const showSuccessModal = (modalInfo) => {
    if (!modalInfo) return false;
    const { page, type } = modalInfo;
    return (page === 1 && (type === "admitCase" || type === "sendCaseBack")) || (page === 2 && type === "schedule");
  };
  // const onCalendarConfirm = () => {};
  const [selectedCustomDate, setSelectedCustomDate] = useState(new Date());

  const handleSelect = (date) => {
    setSelectedCustomDate(date);
  };
  const onCalendarConfirm = () => {
    setModalInfo({ ...modalInfo, page: 0, showDate: false, showCustomDate: true });
    setDateSelected(false);
  };
  const [selectedChip, setSelectedChip] = React.useState(null);

  const setPurposeValue = (value, input) => {
    setScheduleHearingParam({ ...scheduleHearingParams, purpose: value });
  };
  const handleChipClick = (chipLabel) => {
    setSelectedChip(chipLabel);
  };
  const [dateSelected, setDateSelected] = useState(false);

  const showCustomDateModal = () => {
    setModalInfo({ ...modalInfo, showDate: true });
  };
  const [selectedValues, setSelectedValues] = useState({});

  const handleInputChange = (values) => {
    setSelectedValues(values);
  };
  return (
    <div>
      {modalInfo?.page == 0 && modalInfo?.type === "sendCaseBack" && (
        <Modal
          headerBarMain={<Heading label={t(stepItems[0].headModal)} />}
          headerBarEnd={<CloseBtn onClick={() => setShowModal(false)} />}
          hideSubmit={true}
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
            onSubmit={(props) => onSubmit(props)}
            submitInForm
            // className={"registration-select-name"}
            secondaryActionLabel={t("CORE_LOGOUT_CANCEL")}
            buttonStyle={{ alignSelf: "center", minWidth: "50%" }}
            actionClassName="e-filing-action-bar"
          ></FormComposerV2>
          {showErrorToast && <Toast error={true} label={t("ES_COMMON_PLEASE_ENTER_ALL_MANDATORY_FIELDS")} isDleteBtn={true} onClose={closeToast} />}
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
        >
          <ScheduleAdmission
            config={stepItems[2]}
            t={t}
            setShowModal={setShowModal}
            setModalInfo={setModalInfo}
            modalInfo={modalInfo}
            selectedCustomDate={selectedCustomDate}
            selectedChip={selectedChip}
            setSelectedChip={setSelectedChip}
            handleChipClick={handleChipClick}
            dateSelected={dateSelected}
            setDateSelected={setDateSelected}
            showCustomDateModal={showCustomDateModal}
            setPurposeValue={setPurposeValue}
            scheduleHearingParams={scheduleHearingParams}
            setScheduleHearingParam={setScheduleHearingParam}
            submitModalInfo={submitModalInfo}
          />
        </Modal>
      )}
      {modalInfo?.page == 1 && modalInfo?.type === "schedule" && (
        <Modal
          headerBarMain={<Heading label={t(stepItems[2].headModal)} />}
          headerBarEnd={<CloseBtn onClick={() => setShowModal(false)} />}
          hideSubmit={true}
        >
          <SelectParticipant
            config={selectParticipantConfig}
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

          // actionSaveOnSubmit={onSelect}
        >
          <CustomCalendar
            config={stepItems[3]}
            t={t}
            onCalendarConfirm={onCalendarConfirm}
            handleSelect={handleSelect}
            selectedCustomDate={selectedCustomDate}
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
            setShowModal(false);
          }}
          actionSaveOnSubmit={() => history.push(`${path}/admission`)}
          className="case-types"
          formId="modal-action"
        >
          <CustomSubmitModal submitModalInfo={submitModalInfo} />
        </Modal>
      )}
    </div>
  );
}

export default AdmissionActionModal;
