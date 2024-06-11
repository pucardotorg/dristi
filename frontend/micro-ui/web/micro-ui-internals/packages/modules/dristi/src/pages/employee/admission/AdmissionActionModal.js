import { ArrowForward, ArrowRightInbox, Banner, Card, CardText, Modal, TextArea } from "@egovernments/digit-ui-react-components";
import React, { useMemo, useState } from "react";
import { FormComposerV2 } from "@egovernments/digit-ui-react-components";

import { modalConfig, selectParticipantConfig } from "../../citizen/FileCase/Config/admissionActionConfig";
import { useHistory } from "react-router-dom/cjs/react-router-dom.min";
import CustomSubmitModal from "../../../components/CustomSubmitModal";
import ScheduleAdmission from "./ScheduleAdmission";
import SelectParticipant from "./SelectParticipant";
import { Calendar } from "react-date-range";

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
function AdmissionActionModal({ t, setShowModal, setSubmitModalInfo, submitModalInfo, modalInfo, setModalInfo }) {
  const [reasons, setReasons] = useState(null);
  const history = useHistory();
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

  const onSubmit = (props) => {
    setModalInfo({ ...modalInfo, page: 1 });
  };
  const showSuccessModal = (modalInfo) => {
    if (!modalInfo) return false;
    const { page, type } = modalInfo;
    return (page === 1 && (type === "admitCase" || type === "sendCaseBack")) || (page === 2 && type === "schedule");
  };
  const [selectedDate, setSelectedDate] = useState(new Date());

  const handleSelect = (date) => {
    setSelectedDate(date);
  };

  const renderCustomDay = (date) => {
    const isToday = date.getDate() === new Date().getDate(); // Check if the date is today
    const isWeekend = date.getDay() === 0 || date.getDay() === 6; // Check if the date is a weekend

    return (
      // <div style={{ textAlign: "center", fontSize: "14px", paddingTop: "5px" }}>
      <div>
        {date.getDate()}
        {isToday && <div style={{ fontSize: "8px", color: "#931847" }}>10 Hearings</div>}
      </div>
      //   {isWeekend && <div style={{ color: "red" }}>Weekend</div>}
      // </div>
    );
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
        </Modal>
      )}
      {modalInfo?.page == 0 && modalInfo?.type === "admitCase" && (
        <Modal
          headerBarMain={<Heading label={t(stepItems[1].headModal)} />}
          actionSaveLabel={t(stepItems[1]?.submitText)}
          headerBarEnd={<CloseBtn onClick={() => setShowModal(false)} />}
          actionSaveOnSubmit={(props) => onSubmit(props)}
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
          <ScheduleAdmission config={stepItems[2]} t={t} setShowModal={setShowModal} setModalInfo={setModalInfo} modalInfo={modalInfo} />
        </Modal>
      )}
      {modalInfo?.page == 1 && modalInfo?.type === "schedule" && (
        <Modal
          headerBarMain={<Heading label={t(stepItems[2].headModal)} />}
          headerBarEnd={<CloseBtn onClick={() => setShowModal(false)} />}
          hideSubmit={true}
        >
          <SelectParticipant config={selectParticipantConfig} setShowModal={setShowModal} modalInfo={modalInfo} setModalInfo={setModalInfo} />
        </Modal>
      )}
      {modalInfo?.showDate && (
        <Modal
          headerBarMain={<Heading label={t(stepItems[3].headModal)} />}
          headerBarEnd={<CloseBtn onClick={() => setShowModal(false)} />}
          actionSaveLabel={t("CS_COMMON_CONFIRM")}
          // actionSaveOnSubmit={onSelect}
        >
          <Calendar date={selectedDate} onChange={handleSelect} dayContentRenderer={renderCustomDay} />
        </Modal>
      )}
      {showSuccessModal(modalInfo) && (
        <Modal
          actionSaveLabel={
            <div>
              {t(submitModalInfo?.nextButtonText)}
              {submitModalInfo?.isArrow && <ArrowRightInbox />}
            </div>
          }
          actionCancelLabel={t(submitModalInfo?.backButtonText)}
          actionCancelOnSubmit={() => {
            setShowModal(false);
          }}
          className="case-types"
        >
          <CustomSubmitModal submitModalInfo={submitModalInfo} />
        </Modal>
      )}
    </div>
  );
}

export default AdmissionActionModal;
