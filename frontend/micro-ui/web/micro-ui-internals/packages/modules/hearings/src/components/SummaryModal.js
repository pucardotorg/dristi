import React, { useEffect, useState } from "react";
import { CardText, Modal } from "@egovernments/digit-ui-react-components";
import { TextArea } from "@egovernments/digit-ui-components";
import { useTranslation } from "react-i18next";

const fieldStyle = { marginRight: 0 };

const Heading = (props) => {
  return (
    <div style={{ width: "824px", height: "28px" }}>
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

const ForwardArrowIcon = () => (
  <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" fill="currentColor" class="bi bi-arrow-right-short" viewBox="0 0 16 16">
    <path
      fill-rule="evenodd"
      d="M4 8a.5.5 0 0 1 .5-.5h5.793L8.146 5.354a.5.5 0 1 1 .708-.708l3 3a.5.5 0 0 1 0 .708l-3 3a.5.5 0 0 1-.708-.708L10.293 8.5H4.5A.5.5 0 0 1 4 8"
    />
  </svg>
);

const AlertIcon = () => {
  return (
    <svg width="20" height="20" viewBox="0 0 24 24" fill="#0F3B8C" xmlns="http://www.w3.org/2000/svg">
      <path
        d="M12 2C6.48 2 2 6.48 2 12s4.48 10 10 10 10-4.48 10-10S17.52 2 12 2zm0 18c-4.41 0-8-3.59-8-8s3.59-8 8-8 8 3.59 8 8-3.59 8-8 8z"
        fill="#0F3B8C"
      />
      <path d="M11 10h2v6h-2zm0-3h2v2h-2z" fill="#0F3B8C" />
    </svg>
  );
};

const BackBtn = ({ text }) => {
  return (
    <div style={{ display: "flex", alignItems: "center", justifyContent: "center" }}>
      <div>{text}</div>
      <div style={{ width: "24px", height: "24px", marginLeft: "4px" }}>
        <ForwardArrowIcon />
      </div>
    </div>
  );
};

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

const SummaryModal = ({ handleConfirmationModal, hearingId }) => {
  const { t } = useTranslation();
  const tenantId = window?.Digit.ULBService.getCurrentTenantId();
  const [transcript, setTranscript] = useState("");

  const { data: latestText } = Digit.Hooks.hearings.useGetHearings(
    { hearing: { tenantId } },
    { applicationNumber: "", cnrNumber: "", hearingId },
    "dristi",
    true
  );

  useEffect(() => {
    if (latestText && latestText?.HearingList?.[0]?.transcript?.[0]) {
      const hearingData = latestText?.HearingList?.[0];
      setTranscript(hearingData.transcript[0]);
    }
  }, [latestText, hearingId]);

  return (
    <div>
      {/* make this below modal same conditional as End Hearing Modal */}
      <Modal
        popupStyles={{
          height: "432px",
          maxHeight: "432px",
          width: "896px",
          position: "absolute",
          top: "50%",
          left: "50%",
          transform: "translate(-50%, -50%)",
          borderRadius: "4px",
        }}
        headerBarMainStyle={{
          padding: "0px 24px 0px 24px",
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
        actionSaveLabelStyles={{
          backgroundColor: "#BB2C2F",
          width: "150px",
          height: "40px",
          padding: " 8px 24px 8px 24px",
        }}
        headerBarMain={<Heading label={t("Confirm Hearing Transcript/ Summary")} />}
        headerBarEnd={<CloseBtn onClick={handleConfirmationModal} />}
        actionSaveLabel={<BackBtn text={t("Set Next Hearing Date")} />}
        actionCancelLabel={t("Back")}
        actionSaveOnSubmit={() => alert("clicked")} // pass the handler of next modal
        formId="modal-action"
      >
        <div style={{ height: "308px", padding: "5px 24px 16px 24px", width: "100%" }}>
          <div style={{ height: "80px", backgroundColor: "#ECF3FD", borderRadius: "4px" }}>
            <div style={{ padding: 10, display: "flex", alignItems: "center" }}>
              <div>
                <AlertIcon />
              </div>
              <div style={{ fontWeight: 700, fontSize: "16px", lineHeight: "18.75px", color: "#0A0A0A", marginLeft: "5px" }}>Please Note</div>
            </div>
            <div style={{ color: "#3D3C3C", paddingLeft: 10 }}>This summary will be visible to all parties viewing the case file.</div>
          </div>
          <CardText style={{ color: "#3D3C3C", fontSize: "16px", fontWeight: 400, lineHeight: "18.75px" }}>
            <div style={{ color: "#0A0A0A", fontWeight: 400, fontSize: "16px" }}>{t("Summary") + " "}</div>
            <div style={{ height: "165px", gap: "16px", border: "1px solid #3D3C3C", marginTop: "2px" }}>
              <TextArea
                style={{ padding: "10px", width: "100%", minHeight: "100%", fontWeight: 400, fontSize: "16px", color: "#3D3C3C" }}
                value={transcript}
              />
            </div>
          </CardText>
        </div>
      </Modal>
    </div>
  );
};

export default SummaryModal;
