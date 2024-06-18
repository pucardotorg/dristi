import { Modal } from "@egovernments/digit-ui-react-components";
import React, { useCallback } from "react";
import { useTranslation } from "react-i18next";

const Heading = (props) => {
  return <h1 className="heading-m">{props.label}</h1>;
};

function ConfirmCorrectionModal({ onCorrectionCancel, onSubmit }) {
  const { t } = useTranslation();
  const onCancel = useCallback(() => {
    onCorrectionCancel?.();
  }, [onCorrectionCancel]);
  return (
    <Modal
      actionCancelLabel={t("CS_COMMON_CANCEL")}
      actionCancelOnSubmit={onCancel}
      actionSaveOnSubmit={onSubmit}
      actionSaveLabel={t("CS_COMMON_CONFIRM")}
      formId="modal-action"
      headerBarMain={<Heading label={t("CS_CONFIRM_CORRECTION")} />}
    >
      <div style={{ padding: 24 }}>{t("CS_CORRECTION_INFO")}</div>
    </Modal>
  );
}

export default ConfirmCorrectionModal;
