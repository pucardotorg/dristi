import React from "react";
import { Banner, CardLabel } from "@egovernments/digit-ui-react-components";
import Modal from "./Modal";

function SuccessModal({ t, onCancel, onSubmit, bannerMessage, actionSaveLabel, actionCancelLabel, type, data }) {
  return (
    <Modal
      actionCancelLabel={t(actionCancelLabel)}
      actionCancelOnSubmit={onCancel}
      actionSaveLabel={t(actionSaveLabel)}
      actionSaveOnSubmit={onSubmit}
      formId="modal-action"
      className="case-types"
    >
      <div style={{ padding: 20 }}>
        <Banner
          whichSvg={"tick"}
          successful={true}
          message={t(bannerMessage)}
          headerStyles={{ fontSize: "32px" }}
          style={{ minWidth: "100%", marginTop: "10px" }}
        ></Banner>
        {["caseSendBackSuccess", "caseRegisterSuccess"].includes(type) && (
          <div>
            <div style={{ display: "flex", justifyContent: "space-between", alignItems: "center" }}>
              <p>{t("CS_CASE_ID")}</p>
              <p>{data.caseId}</p>
            </div>
            <div style={{ display: "flex", justifyContent: "space-between", alignItems: "center" }}>
              <p>{t("CS_CASE_NAME")}</p>
              <p>{data.caseName}</p>
            </div>
            <div style={{ display: "flex", justifyContent: "space-between", alignItems: "center" }}>
              <p>{t("CS_ERRORS_MARKED")}</p>
              <p>{data.errorsMarked}</p>
            </div>
          </div>
        )}
      </div>
    </Modal>
  );
}

export default SuccessModal;
