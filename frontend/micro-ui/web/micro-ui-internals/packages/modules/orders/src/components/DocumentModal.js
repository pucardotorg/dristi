import { CloseSvg } from "@egovernments/digit-ui-react-components";
import React, { useMemo, useState } from "react";
const CloseBtn = (props) => {
  return (
    <div onClick={props?.onClick} style={{ height: "100%", display: "flex", alignItems: "center", paddingRight: "20px", cursor: "pointer" }}>
      <CloseSvg />
    </div>
  );
};
const Heading = ({ heading }) => {
  return (
    <div className="evidence-title">
      <h1 className="heading-m">{heading.label}</h1>
      <h3 className={heading.isStatusRed ? "status-false" : "status"}>{heading?.status}</h3>
    </div>
  );
};
const DocumentModal = ({ config }) => {
  const Modal = window?.Digit?.ComponentRegistryService?.getComponent("Modal");
  const [step, setStep] = useState(0);
  // const [isDisabled, setIsDisabled] = useState(false);

  const isDisabled = useMemo(() => {
    if (config?.isStepperModal) return config?.steps[step]?.isDisabled;
    return config?.isDisabled;
  }, [config?.isDisabled, config?.isStepperModal, config?.steps, step]);

  const actionSaveOnSubmit = () => {
    if (config?.isStepperModal && config?.steps[step]?.actionCancelOnSubmit) {
      config?.steps[step]?.actionCancelOnSubmit();
    } else if (config?.actionSaveOnSubmit) config?.actionSaveOnSubmit();
    if (step + 1 <= config?.steps?.length) setStep(step + 1);
  };

  const actionCancelOnSubmit = () => {
    if (config?.isStepperModal && config?.steps[step]?.actionCancelOnSubmit) {
      config?.steps[step]?.actionCancelOnSubmit();
    } else if (config?.actionCancelOnSubmit) config?.actionCancelOnSubmit();
    if (step - 1 >= 0) setStep(step - 1);
  };

  return (
    <Modal
      headerBarEnd={<CloseBtn onClick={config?.isStepperModal ? config?.steps[step]?.handleClose || config?.handleClose : config?.handleClose} />}
      actionSaveLabel={config?.isStepperModal ? config?.steps[step]?.actionSaveLabel || config?.actionSaveLabel : config?.actionSaveLabel}
      actionSaveOnSubmit={actionSaveOnSubmit}
      hideSubmit={config?.isStepperModal ? config?.steps[step]?.hideSubmit || config?.hideSubmit : config?.hideSubmit}
      actionCancelLabel={config?.isStepperModal ? config?.steps[step]?.actionCancelLabel || config?.actionCancelLabel : config?.actionCancelLabel}
      actionCancelOnSubmit={actionCancelOnSubmit}
      formId="modal-action"
      headerBarMain={
        config?.isStepperModal && config?.steps[step]?.type !== "success" ? (
          <Heading heading={config?.steps[step]?.heading || config?.heading} />
        ) : (
          <Heading heading={config?.heading} />
        )
      }
      className={
        config?.isStepperModal
          ? config.steps[step]?.type === "document"
            ? "custom-modal-stepper"
            : "add-signature-modal"
          : config?.type === "document"
          ? "custom-modal-stepper"
          : "add-signature-modal"
      }
      isDisabled={isDisabled}
    >
      {config?.isStepperModal ? config?.steps[step]?.modalBody || config?.modalBody : config?.modalBody}
    </Modal>
  );
};

export default DocumentModal;
