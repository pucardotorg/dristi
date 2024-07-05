import { CardText, Modal, CloseSvg } from "@egovernments/digit-ui-react-components";
import React, { useState } from "react";
import { useTranslation } from "react-i18next";
import { useHistory } from "react-router-dom";
import { hearingService } from "../../hooks/services";
import SummaryModal from "../../components/SummaryModal";

const fieldStyle = { marginRight: 0 };

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

const EndHearing = ({ handleEndHearingModal, hearingId, hearing }) => {
  const { t } = useTranslation();
  const tenantId = window?.Digit.ULBService.getCurrentTenantId();
  const [openConfirmationModal, setOpenConfirmationModal] = useState(false);

  const updateHearing = async () => {
    try {
      await hearingService.updateHearing({ tenantId, hearing, hearingType: "", status: "" }, { applicationNumber: "", cnrNumber: "" });
    } catch (error) {
      console.error("Error updating hearing:", error);
    }
  };

  const handleConfirmationModal = () => {
    updateHearing();
    setOpenConfirmationModal(!openConfirmationModal);
    if (openConfirmationModal === true) {
      handleEndHearingModal();
    }
  };

  return (
    <div>
      {!openConfirmationModal ? (
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
          headerBarMain={<Heading label={t("Are you sure you wish to end this hearing?")} />}
          headerBarEnd={<CloseBtn onClick={handleEndHearingModal} />}
          actionSaveLabel={t("End Hearing")}
          actionSaveOnSubmit={handleConfirmationModal}
          formId="modal-action"
        >
          <div style={{ height: "70px", padding: "5px 24px 16px 24px" }}>
            <CardText style={{ color: "#3D3C3C", fontSize: "16px", fontWeight: 400, lineHeight: "18.75px" }}>
              {t("This action cannot be reversed. All parties will be removed from this hearing.") + " "}
            </CardText>
          </div>
        </Modal>
      ) : (
        <SummaryModal handleConfirmationModal={handleConfirmationModal} hearingId={hearingId} />
      )}
    </div>
  );
};

export default EndHearing;
