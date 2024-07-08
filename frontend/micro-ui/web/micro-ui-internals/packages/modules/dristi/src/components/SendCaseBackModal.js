import React from "react";
import Modal from "./Modal";
import { CloseSvg } from "@egovernments/digit-ui-react-components";
import SelectCustomNote from "./SelectCustomNote";
import { useHistory } from "react-router-dom/cjs/react-router-dom.min";

function SendCaseBackModal({ totalErrors, onCancel, onSubmit, t, heading, type, actionCancelLabel, actionSaveLabel, handleCloseModal }) {
  const Heading = (props) => {
    return <h1 className="heading-m">{props.label}</h1>;
  };
  const history = useHistory();
  const CloseBtn = (props) => {
    return (
      <div onClick={props?.onClick} style={{ height: "100%", display: "flex", alignItems: "center", paddingRight: "20px", cursor: "pointer" }}>
        <CloseSvg />
      </div>
    );
  };
  const subtexts = {
    registerCase: t("CS_NO_ERROR_MARKED"),
    sendCaseBack: `${t("CS_YOU_HAVE_MARKED")} ${totalErrors} ${t("CS_COMMON_ERRORS")} ${t("CS_IN_THIS_FILE")}`,
    sendCaseBackPotential: `${t("CS_CONFIRMING")} ${totalErrors} ${t("CS_POTENTIAL_ERRORS")} ${t("CS_FLAGGED_BY_SYSTEM")}`,
  };
  const isDisabled = (type === "sendCaseBack" || type === "sendCaseBackPotential") && totalErrors === 0;
  const nodeConfig = {
    populators: {
      inputs: [
        {
          infoHeader: "CS_COMMON_NOTE",
          infoText: "SCRUTINY_SEND_CASE_NOTE",
          infoTooltipMessage: "Tooltip",
          type: "InfoComponent",
          linkText: "VIEW_CHECKLIST",
        },
      ],
    },
  };
  return (
    <Modal
      headerBarEnd={
        <CloseBtn
          onClick={() => {
            handleCloseModal ? handleCloseModal() : onCancel();
          }}
        />
      }
      actionCancelLabel={t(actionCancelLabel)}
      actionCancelOnSubmit={onCancel}
      actionSaveLabel={t(actionSaveLabel)}
      actionSaveOnSubmit={onSubmit}
      formId="modal-action"
      isDisabled={isDisabled}
      headerBarMain={<Heading label={t(heading)} />}
      className="case-types"
    >
      <div style={{ padding: "16px 24px" }}>
        <SelectCustomNote
          config={nodeConfig}
          t={t}
          onClick={() => {
            handleCloseModal ? handleCloseModal() : onCancel();
          }}
        />
      </div>
      <p style={{ padding: 20 }}>{subtexts[type]}</p>
    </Modal>
  );
}

export default SendCaseBackModal;
