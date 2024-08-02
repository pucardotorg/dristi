import { Button, CloseSvg, InboxSearchComposer } from "@egovernments/digit-ui-react-components";
import React, { useMemo, useState } from "react";
import { useTranslation } from "react-i18next";
import Modal from "../../../dristi/src/components/Modal";
import { preHearingConfig } from "../configs/PreHearingConfig";
import { ReschedulingPurpose } from "../pages/employee/ReschedulingPurpose";

function PreHearingModal({ onCancel, hearingData, courtData }) {
  const { t } = useTranslation();
  const [purposeModalOpen, setPurposeModalOpen] = useState(false);
  const [purposeModalData, setPurposeModalData] = useState(false);

  const Heading = (props) => {
    return <h1 className="heading-m">{props.label}</h1>;
  };

  const CloseBtn = (props) => {
    return (
      <div onClick={props.onClick} style={{ height: "100%", display: "flex", alignItems: "center", paddingRight: "20px", cursor: "pointer" }}>
        <CloseSvg />
      </div>
    );
  };

  const openRescheduleModal = (caseDetails) => {
    setPurposeModalData(caseDetails);
    setPurposeModalOpen(true);
  };

  const updatedConfig = useMemo(() => {
    const configCopy = structuredClone(preHearingConfig);
    configCopy.apiDetails.requestParam = {
      ...configCopy.apiDetails.requestParam,
      fromDate: hearingData.fromDate,
      toDate: hearingData.toDate,
      slot: hearingData.slot,
    };
    configCopy.sections.searchResult.uiConfig.columns = [
      ...configCopy.sections.searchResult.uiConfig.columns.map((column) => {
        return column.label === "Actions"
          ? {
              ...column,
              openRescheduleDialog: openRescheduleModal,
            }
          : column;
      }),
    ];
    return configCopy;
  }, [hearingData.fromDate, hearingData.toDate, hearingData.slot]);

  const popUpStyle = {
    width: "70%",
    height: "fit-content",
    borderRadius: "0.3rem",
  };

  const onRescheduleAllClick = () => {
    const contextPath = window?.contextPath || "";
    window.location.href = `/${contextPath}/employee/hearings/reschedule-hearing`;
  };

  const closeFunc = () => {
    setPurposeModalOpen(false);
    setPurposeModalData({});
  };

  return (
    <Modal
      headerBarEnd={<CloseBtn onClick={onCancel} />}
      actionCancelOnSubmit={onCancel}
      actionSaveLabel={t("Reschedule All Hearings")}
      formId="modal-action"
      headerBarMain={<Heading label={t("Admission Hearings (34)")} />}
      className="pre-hearings"
      popupStyles={popUpStyle}
      popupModuleActionBarStyles={{
        display: "none",
      }}
    >
      <div style={{ minHeight: "80vh" }}>
        <InboxSearchComposer configs={updatedConfig} />
      </div>
      <div
        style={{ display: "flex", justifyContent: "space-between", alignItems: "center", padding: "1rem 0 0 0", borderTop: "1px solid lightgray" }}
      >
        <div>
          <strong>{hearingData.fromDate}</strong>, {hearingData.slot}
        </div>
        <Button
          className="border-none dristi-font-bold"
          onButtonClick={onRescheduleAllClick}
          label="Reschedule All Hearings"
          variation={"secondary"}
        />
      </div>
      {purposeModalOpen && <ReschedulingPurpose courtData={courtData} closeFunc={closeFunc} caseDetails={purposeModalData} />}
    </Modal>
  );
}

export default PreHearingModal;
