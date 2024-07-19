import React from 'react';
import { useHistory } from 'react-router-dom';
import { useState } from 'react';
import {
  Dropdown,
  CardLabel,
  Modal,
  Button,
  CardText,
} from '@egovernments/digit-ui-react-components';
import { useTranslation } from 'react-i18next';

const Heading = (props) => {
  return <h1 className="heading-m">{props.label}</h1>;
};

const Close = () => (
  <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="#FFFFFF">
    <path d="M0 0h24v24H0V0z" fill="none" />
    <path d="M19 6.41L17.59 5 12 10.59 6.41 5 5 6.41 10.59 12 5 17.59 6.41 19 12 13.41 17.59 19 19 17.59 13.41 12 19 6.41z" />
  </svg>
);

const CloseBtn = (props) => {
  return (
    <div onClick={props?.onClick} style={props?.isMobileView ? { padding: 5 } : null}>
      {props?.isMobileView ? (
        <CloseSvg />
      ) : (
        <div className={"icon-bg-secondary"} style={{ backgroundColor: "#505A5F" }}>
          {" "}
          <Close />{" "}
        </div>
      )}
    </div>
  );
};

const RescheduleHearing = ({ onSelect, onCancel, onDismiss , rescheduleAll}) => {
  const { t } = useTranslation();
  const history = useHistory();
  const mobileDeviceWidth = 780;
  const [isMobileView, setIsMobileView] = useState(window.innerWidth <= mobileDeviceWidth);
  const onResize = () => {
    if (window.innerWidth <= mobileDeviceWidth) {
      if (!isMobileView) {
        setIsMobileView(true);
      }
    } else {
      if (isMobileView) {
        setIsMobileView(false);
      }
    }
  };
  console.log(rescheduleAll,"reschedule")

  React.useEffect(() => {
    window.addEventListener('resize', onResize);
    return () => {
      window.removeEventListener('resize', onResize);
    };
  }, [isMobileView]);

  let a = "FSM-2018-22-3-001";
  let b = "John vs Doe";
  let c = "NIA S";

  const handleNavigate = (path) => {
    const contextPath = window?.contextPath || ''; 
    history.push(`/${contextPath}${path}`);
  };

  return isMobileView ? (
    <Modal
      popupStyles={{
        height: "250px",
        maxHeight: "250px",
        width: "400px",
        position: "absolute",
        top: "50%",
        left: "50%",
        transform: "translate(-50%, -50%)",
      }}
      popupModuleActionBarStyles={{
        display: "flex",
        flex: 1,
        justifyContent: "flex-start",
        width: "100%",
        position: "absolute",
        left: 0,
        bottom: 0,
        padding: "18px",
      }}
      style={{
        flex: 1,
      }}
      popupModuleMianStyles={{
        padding: "18px",
      }}
      headerBarMain={<Heading label={t("RESCHEDULE HEARING")} />}
      headerBarEnd={<CloseBtn onClick={onDismiss} isMobileView={isMobileView} />}
      actionCancelLabel={t("BACK")}
      actionCancelOnSubmit={onCancel}
      actionSaveLabel={t("Generate Order")}
      actionSaveOnSubmit={onSelect}
      formId="modal-action"
    >
      <div>
        <CardText style={{ margin: 0 }}>{t("RESCHEDULE_HEARING_CONFIRMATION_MESSAGE") + " "}</CardText>
      </div>
    </Modal>
  ) : (
    <Modal
      popupModuleMianStyles={{}}
      headerBarMain={<Heading label={t("RESCHEDULE HEARING HEADER")} />}
      headerBarEnd={<CloseBtn onClick={onDismiss} isMobileView={false} />}
      actionCancelLabel={t("Back")}
      actionCancelOnSubmit={onCancel}
      actionSaveLabel={t("Generate Order")}
      actionSaveOnSubmit={onSelect}
      formId="modal-action"
    >
      <div>
        <CardText style={{ marginBottom: "20px", color: "#0B0C0C", textAlign: "center", fontSize: "18px" }}>
          {t("RESCHEDULE_HEARING_CONFIRMATION_MESSAGE") + " "}
          <strong>{t("RESCHEDULE_HEARING_MESSAGE")}</strong>
        </CardText>
      </div>
      {rescheduleAll?(
          <div>
          <div key="5">Hearing Type</div>
          <div key="6">No. of Hearings</div>
          <div key="7">Initial Hearing Date</div>
          <div key="8">Initial Time Slot</div>
          </div>
      ):(
        <div>
          <div key="1">Case Number: {t(`${a}`)}</div>
          <div key="2">Case Name: {t(`${b}`)}</div>
          <div key="3">Case Type: {t(`${c}`)}</div>
        </div>
      )}
      <CardLabel style={{ width: "16rem", marginBottom: "0px" }}>Purpose Of Reschedule Hearing</CardLabel>
      <Dropdown
        style={{ width: "100%" }}
        option={[]}
        optionKey={"code"}
        select={(value) => {}}
      />
    </Modal>
  );
};

export default RescheduleHearing;