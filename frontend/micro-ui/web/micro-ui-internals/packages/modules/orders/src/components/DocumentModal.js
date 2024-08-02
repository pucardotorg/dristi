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
    return config?.steps[step]?.isDisabled || false;
  }, [config?.steps, step]);

  const actionSaveOnSubmit = () => {
    if (config?.isStepperModal[step] && config?.steps[step]?.actionCancelOnSubmit) {
      config?.steps[step]?.actionCancelOnSubmit();
    } else if (config?.actionSaveOnSubmit) config?.actionSaveOnSubmit();
    if (step + 1 <= config?.steps?.length) setStep(step + 1);
  };

  const actionCancelOnSubmit = () => {
    if (config?.isStepperModal[step] && config?.steps[step]?.actionCancelOnSubmit) {
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
        config?.steps[step]?.type !== "success" && (
          <Heading heading={config?.isStepperModal ? config?.steps[step]?.heading || config?.heading : config?.heading} />
        )
      }
      className={config.steps[step]?.type === "document" ? "custom-modal-stepper" : "add-signature-modal"}
      isDisabled={isDisabled}
    >
      {config?.isStepperModal ? config?.steps[step]?.modalBody || config?.modalBody : config?.modalBody}
    </Modal>
  );
};

export default DocumentModal;
