import { CardText, Modal } from "@egovernments/digit-ui-react-components";
import React, { useState } from "react";
import { useTranslation } from "react-i18next";
import { useHistory } from "react-router-dom";
import NextHearingModal from "../../components/NextHearingModal";
import SummaryModal from "../../components/SummaryModal";
import { hearingService } from "../../hooks/services";

const Heading = (props) => {
  return (
    <div style={{ width: "440px", height: "56px" }}>
      <p
        style={{
          fontWeight: 700,
          fontSize: "24px",
          lineHeight: "28.13px",
          color: "#0A0A0A",
        }}
      >
        {props.label}
      </p>
    </div>
  );
};

const Close = () => (
  <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="#0A0A0A">
    <path d="M0 0h24v24H0V0z" fill="none" />
    <path d="M19 6.41L17.59 5 12 10.59 6.41 5 5 6.41 10.59 12 5 17.59 6.41 19 12 13.41 17.59 19 19 17.59 13.41 12 19 6.41z" />
  </svg>
);

const CloseBtn = (props) => {
  return (
    <div onClick={props?.onClick} style={{ paddingTop: 10 }}>
      <div className={"icon-bg-secondary"} style={{ backgroundColor: "#ffff", cursor: "pointer" }}>
        {" "}
        <Close />{" "}
      </div>
    </div>
  );
};

const EndHearing = ({ handleEndHearingModal, hearingId, updateTranscript, hearing, transcriptText, disableTextArea, setTranscriptText }) => {
  const { t } = useTranslation();
  const [stepper, setStepper] = useState(1);
  const tenantId = window?.Digit.ULBService.getCurrentTenantId();
  const [transcript, setTranscript] = useState(transcriptText);
  const history = useHistory();

  const handleNavigate = (path) => {
    const contextPath = window?.contextPath || "";
    history.push(`/${contextPath}${path}`);
  };

  const endHearing = async (updatedTranscriptText) => {
    try {
      const updatedHearing = structuredClone(hearing);
      updatedHearing.transcript[0] = updatedTranscriptText;
      updatedHearing.workflow = updatedHearing.workflow || {};
      updatedHearing.workflow.action = "CLOSE";
      const response = await hearingService.updateHearings(
        { tenantId, hearing: updatedHearing, hearingType: "", status: "" },
        { applicationNumber: "", cnrNumber: "" }
      );
      setTranscriptText(updatedTranscriptText);
      return response;
    } catch (error) {
      console.error("Error Ending hearing:", error);
    }
  };

  const handleConfirmationModal = () => {
    handleEndHearingModal();
  };

  return (
    <div>
      {stepper === 1 && (
        <Modal
          popupStyles={{
            height: "222px",
            maxHeight: "222px",
            width: "536px",
            position: "absolute",
            top: "50%",
            left: "50%",
            transform: "translate(-50%, -50%)",
            borderRadius: "4px",
          }}
          headerBarMainStyle={{
            padding: "0px 24px 12px 24px",
          }}
          popupModuleActionBarStyles={{
            display: "flex",
            justifyContent: "flex-end",
            position: "absolute",
            right: 0,
            bottom: 0,
            width: "100%",
            borderTop: "1px solid #dbd7d2",
            padding: "10px 16px 16px 0px",
          }}
          style={{
            backgroundColor: "#BB2C2F",
            width: "150px",
            height: "40px",
            padding: " 8px 24px 8px 24px",
          }}
          headerBarMain={<Heading label={t("CONFIRM_END_HEARING")} />}
          headerBarEnd={<CloseBtn onClick={handleEndHearingModal} />}
          actionSaveLabel={t("END_HEARING")}
          actionSaveOnSubmit={() => {
            setStepper(stepper + 1);
          }}
          formId="modal-action"
        >
          <div style={{ height: "70px", padding: "5px 24px 16px 24px" }}>
            <CardText style={{ color: "#3D3C3C", fontSize: "16px", fontWeight: 400, lineHeight: "18.75px" }}>{t("END_HEARING_DISCLAIMER")}</CardText>
          </div>
        </Modal>
      )}
      {stepper === 2 && (
        <SummaryModal
          transcript={transcript}
          setTranscript={setTranscript}
          handleConfirmationModal={handleConfirmationModal}
          hearingId={hearingId}
          hearing={hearing}
          isEndHearing={true}
          disableTextArea={disableTextArea}
          onSaveSummary={(updatedTranscriptText) => {
            endHearing(updatedTranscriptText).then(() => {
              setStepper((stepper) => stepper + 1);
            });
          }}
          onCancel={() => {
            setTranscript(transcriptText);
            setStepper((stepper) => stepper - 1);
          }}
        />
      )}
      {stepper === 3 && (
        <NextHearingModal
          transcript={transcript}
          hearingId={hearingId}
          hearing={hearing}
          stepper={stepper}
          setStepper={setStepper}
          handleConfirmationModal={handleConfirmationModal}
        />
      )}
    </div>
  );
};

export default EndHearing;
