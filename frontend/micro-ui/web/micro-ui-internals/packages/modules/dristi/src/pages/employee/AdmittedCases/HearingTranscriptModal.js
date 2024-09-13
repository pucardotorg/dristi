import { CloseSvg, TextArea } from "@egovernments/digit-ui-components";
import React, { useEffect } from "react";
import Modal from "../../../components/Modal";
import { SubmitBar } from "@egovernments/digit-ui-react-components";

const formatDate = (epochTime) => {
  const date = new Date(epochTime);
  const day = String(date.getDate()).padStart(2, "0");
  const month = String(date.getMonth() + 1).padStart(2, "0");
  const year = date.getFullYear();
  return `${day}/${month}/${year}`;
};

function HearingTranscriptModal({ t, hearing, setShowHearingTranscriptModal }) {
  const Heading = () => {
    return (
      <h1 className="heading-m">{`${t(hearing?.hearingType)} ${
        ["NBW_HEARING", "82_83_HEARING"].includes(hearing?.hearingType) ? "" : "Hearing"
      } - ${formatDate(hearing?.startTime)}`}</h1>
    );
  };

  const CloseBtn = (props) => {
    return (
      <div
        onClick={() => setShowHearingTranscriptModal(false)}
        style={{ height: "100%", display: "flex", alignItems: "center", paddingRight: "20px", cursor: "pointer" }}
      >
        <CloseSvg />
      </div>
    );
  };

  return (
    <Modal
      headerBarMain={<Heading />}
      headerBarEnd={<CloseBtn />}
      actionCancelLabel={null}
      actionCancelOnSubmit={() => {}}
      actionSaveLabel={null}
      hideSubmit={true}
      actionSaveOnSubmit={() => {}}
      popupStyles={{ minHeight: "50vh", width: "70vw" }}
      className={"view-hearing-transcript-modal"}
    >
      <div>
        <h2 className="transcript-header">{t("HEARING_TRANSCRIPT_SUMMARY_HEADING")}</h2>
        <TextArea style={{ width: "100%", height: "25vh", border: "solid 1px #3d3c3c", resize: "none" }} value={hearing?.transcript || ""} />
      </div>
      <div className="submit-bar-div">
        <SubmitBar
          variation="primary"
          onSubmit={() => setShowHearingTranscriptModal(false)}
          className="primary-label-btn"
          label={t("CS_COMMON_CANCEL")}
        ></SubmitBar>
      </div>
    </Modal>
  );
}

export default HearingTranscriptModal;
