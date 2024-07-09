import React, { useContext, useMemo, useState } from "react";
import Modal from "../../../dristi/src/components/Modal";
import { Button, CloseSvg, InboxSearchComposer } from "@egovernments/digit-ui-react-components";
import { useTranslation } from "react-i18next";
import { DataContext } from "./DataContext";
import { preHearingConfig } from "../configs/PreHearingConfig";

function PreHearingModal({ onCancel }) {
  const { t } = useTranslation();
  const { hearingData } = useContext(DataContext);
  const [config, setConfig] = useState(preHearingConfig);

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

  const updatedConfig = useMemo(() => {
    const configCopy = structuredClone(preHearingConfig);
    configCopy.apiDetails.requestParam = {
      ...configCopy.apiDetails.requestParam,
      fromDate: hearingData.fromDate,
      toDate: hearingData.toDate,
      slot: hearingData.slot,
    };
    setConfig(configCopy);
  }, [preHearingConfig, hearingData?.fromDate, , hearingData.toDate, hearingData?.slot]);

  const popUpStyle = {
    width: "70%",
    height: "fit-content",
    borderRadius: "0.3rem",
  };

  const onRescheduleAllClick = () => {
    const contextPath = window?.contextPath || "";
    window.location.href = `/${contextPath}/employee/hearings/reschedule-hearing`;
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
      <div style={{ minHeight: "35rem" }}>
        <InboxSearchComposer configs={config} />
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
    </Modal>
  );
}

export default PreHearingModal;
