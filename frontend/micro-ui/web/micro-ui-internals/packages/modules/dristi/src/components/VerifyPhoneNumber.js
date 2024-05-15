import { CardLabel, LabelFieldPair, TextInput } from "@egovernments/digit-ui-react-components";
import React, { useMemo } from "react";
import Button from "./Button";

function VerifyPhoneNumber({ t, config, onSelect, formData = {}, errors }) {
  return (
    <React.Fragment>
      <LabelFieldPair style={{ width: "100%", display: "flex", alignItem: "center" }}>
        <CardLabel style={{ fontWeight: 700 }} className="card-label-smaller">
          {t(config.label)}
        </CardLabel>
      </LabelFieldPair>
      <div>
        <div style={{ display: "flex", width: "100%" }}>
          {config?.componentInFront ? <span className="citizen-card-input citizen-card-input--front">{config?.componentInFront}</span> : null}
          <TextInput
            value={formData?.[config.name]}
            name={config.name}
            minlength={config?.validation?.minLength}
            maxlength={config?.validation?.maxLength}
            validation={config?.validation}
            ValidationRequired={config?.validation}
            title={config?.validation?.title}
            disable={config?.disable ? config?.disable : false}
            // inputRef={register(config?.validation)}
            isMandatory={errors[config?.name]}
          />
        </div>
        <Button
          label={"VERIFY_OTP"}
          style={{ alignItems: "center" }}
          className={"secondary-button-selector"}
          labelClassName={"secondary-label-selector"}
          //   isDisabled={!canAdd}
          onButtonClick={() => {
            // handleAdd(value);
          }}
        />
      </div>
    </React.Fragment>
  );
}

export default VerifyPhoneNumber;
