import { CardLabel, CardLabelError, CloseSvg, FormComposerV2, LabelFieldPair, Modal, TextInput } from "@egovernments/digit-ui-react-components";
import React, { useMemo, useState } from "react";
import Button from "./Button";
import { verifyMobileNoConfig } from "../configs/component";

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

function VerifyPhoneNumber({ t, config, onSelect, formData = {}, errors, setError }) {
  const [{ showModal }, setState] = useState({
    showModal: false,
  });

  const handleCloseModal = () => {
    setState((prev) => ({
      ...prev,
      showModal: false,
    }));
  };
  return (
    <React.Fragment>
      <LabelFieldPair style={{ width: "100%", display: "flex", alignItem: "center" }}>
        <CardLabel className="card-label-smaller">{t(config.label)}</CardLabel>
      </LabelFieldPair>
      <div style={{ display: "flex", justifyContent: "space-between", gap: 24 }}>
        <div className="field user-details-form-style" style={{ display: "flex", width: "100%" }}>
          {config?.componentInFront ? <span className="citizen-card-input citizen-card-input--front">{config?.componentInFront}</span> : null}
          <TextInput
            value={formData?.[config.key]?.[config.name]}
            name={config.name}
            minlength={config?.validation?.minLength}
            maxlength={config?.validation?.maxLength}
            validation={config?.validation}
            ValidationRequired={config?.validation}
            title={config?.validation?.title}
            disable={config?.disable ? config?.disable : false}
            // inputRef={register(config?.validation)}
            isMandatory={errors[config?.name]}
            onChange={(e) => {
              const { value } = e.target;
              if (value?.length >= config?.validation?.minLength && !value.match(config?.validation?.pattern)) {
                setError(config.key, { [config?.name]: config?.error });
              } else {
                setError(config.key, { [config?.name]: "" });
              }
              onSelect(config?.key, { ...formData?.[config.key], [config?.name]: value });
            }}
          />
        </div>
        <Button
          label={"VERIFY_OTP"}
          style={{ alignItems: "center" }}
          className={"secondary-button-selector"}
          labelClassName={"secondary-label-selector"}
          isDisabled={
            !formData?.[config.key]?.[config.name] ||
            errors?.[config?.key]?.[config.name] ||
            formData?.[config.key]?.[config.name]?.length < config?.validation?.minLength ||
            formData?.[config.key]?.[config.name]?.length > config?.validation?.maxLength
          }
          onButtonClick={() => {
            // handleAdd(value);
            setState((prev) => ({
              ...prev,
              showModal: true,
            }));
          }}
        />
      </div>
      <CardLabelError>{t(errors?.[config?.key]?.[config.name] ? "VERIFY_PHONE_ERROR_TEXT" : "VERIFY_PHONE_DEFAULT_TEXT")}</CardLabelError>
      {showModal && (
        <Modal
          headerBarEnd={<CloseBtn onClick={handleCloseModal} isMobileView={true} />}
          // actionCancelLabel={page === 0 ? t("CORE_LOGOUT_CANCEL") : null}
          actionCancelOnSubmit={() => {}}
          actionSaveLabel={t("ADD")}
          actionSaveOnSubmit={() => {}}
          formId="modal-action"
          headerBarMain={<Heading label={t("UPLOAD_ID_PROOF_HEADER")} />}
          submitTextClassName={"verification-button-text-modal"}
        >
          <div>
            <FormComposerV2
              config={verifyMobileNoConfig}
              t={t}
              onSubmit={(props) => {
                // onSubmit(props);
              }}
              cardClassName={"form-composer-id-proof-card"}
              // isDisabled={isDisabled}
              // defaultValues={{ Terms_Conditions: null }}
              inline
              // label={"CS_COMMON_SUBMIT"}
              headingStyle={{ textAlign: "center" }}
              cardStyle={{ minWidth: "100%" }}
              // onFormValueChange={onFormValueChange}
            ></FormComposerV2>
          </div>
        </Modal>
      )}
    </React.Fragment>
  );
}

export default VerifyPhoneNumber;
