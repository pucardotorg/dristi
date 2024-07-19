import React, { useState } from "react";
import Modal from "./Modal";
import { CheckBox, CloseSvg, TextArea } from "@egovernments/digit-ui-react-components";

function ConfirmSubmissionAction({ t, type, setShowConfirmationModal, handleAction }) {
  const [generateOrder, setGenerateOrder] = useState(false);
  const CloseBtn = (props) => {
    return (
      <div onClick={props?.onClick} style={{ height: "100%", display: "flex", alignItems: "center", paddingRight: "20px", cursor: "pointer" }}>
        <CloseSvg />
      </div>
    );
  };
  const Heading = (props) => {
    return (
      <div className="evidence-title">
        <h1 className="heading-m">{props.label}</h1>
      </div>
    );
  };

  const checkBoxLabel = type === "reject" ? t("GENERATE_ORDER_FOR_REJECTION") : t("GENERATE_ORDER_FOR_ACCEPTANCE");
  const header = type === "reject" ? t("REJECT_SUBMISSION_HEADER") : t("ACCEPT_SUBMISSION_HEADER");
  const actionSaveLabel = generateOrder
    ? type === "reject"
      ? t("GENERATE_REJECTION_ORDER")
      : t("GENERATE_ACCEPTANCE_ORDER")
    : type === "reject"
    ? t("REJECT_SUBMISSION")
    : t("ACCEPT_SUBMISSION");

  return (
    <Modal
      headerBarEnd={
        <CloseBtn
          onClick={() => {
            setShowConfirmationModal(null);
          }}
        />
      }
      headerBarMain={<Heading label={header} />}
      actionCancelLabel={"CS_COMMON_BACK"}
      actionSaveLabel={actionSaveLabel}
      actionCancelOnSubmit={() => {
        setShowConfirmationModal(null);
      }}
      actionSaveOnSubmit={() => {
        handleAction(generateOrder);
      }}
    >
      <div>
        <div style={{ marginTop: 10 }}>{t("REJECT_ACCEPT_SUBMISSION_TEXT")}</div>
        {!generateOrder && type === "reject" && <TextArea name={t("PURPOSE_OF_REJECTION")} />}
        <div>
          <CheckBox
            onChange={() => {
              setGenerateOrder((prev) => !prev);
            }}
            label={checkBoxLabel}
          />
        </div>
      </div>
    </Modal>
  );
}

export default ConfirmSubmissionAction;
