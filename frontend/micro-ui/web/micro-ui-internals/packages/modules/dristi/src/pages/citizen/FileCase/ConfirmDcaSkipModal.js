import { CloseSvg, Modal } from "@egovernments/digit-ui-react-components";
import React from "react";

function ConfirmDcaSkipModal({ t, setFormDataValue, setShowConfirmDcaSkipModal, prevIsDcaSkipped, setPrevIsDcaSkipped }) {
  const CloseBtn = (props) => {
    return (
      <div onClick={props?.onClick} style={{ height: "100%", display: "flex", alignItems: "center", paddingRight: "20px", cursor: "pointer" }}>
        <CloseSvg />
      </div>
    );
  };
  const Heading = (props) => {
    return <h1 className="heading-m">{props.label}</h1>;
  };
  const onCancel = () => {
    setFormDataValue("isDcaSkippedInEFiling", {
      code: "NO",
      name: "NO",
      showDcaFileUpload: true,
    }); // If user cancels confirmation for skipping DCA submission, file upload added in form.
    setShowConfirmDcaSkipModal(false);
  };
  const onSubmit = () => {
    setShowConfirmDcaSkipModal(false);
    setFormDataValue("isDcaSkippedInEFiling", {
      code: "YES",
      name: "YES",
      showDcaFileUpload: false,
    }); // If user confirms skipping DCA submission, remove file upload from form.
    // setShowConfirmDcaSkipModal(false);
  };
  return (
    <Modal
      headerBarEnd={<CloseBtn onClick={onCancel} />}
      actionCancelLabel={t("CS_COMMON_CANCEL")}
      actionCancelOnSubmit={onCancel}
      actionSaveLabel={t("CS_COMMON_CONFIRM")}
      actionSaveOnSubmit={onSubmit}
      formId="modal-action"
      headerBarMain={<Heading label={t("CONFIRM_SKIPPING_DCA")} />}
      className="case-types"
    >
      <div style={{ paddingTop: "10px", paddingBottom: "10px" }}>
        <p>{t("YOUR_CASE_REQUIRES_DCA_RECOMMENDED_TO_SUBMIT_ONE")}</p>
        <p>{t("ARE_YOU_SURE_TO_SKIP")}</p>
      </div>
    </Modal>
  );
}

export default ConfirmDcaSkipModal;
