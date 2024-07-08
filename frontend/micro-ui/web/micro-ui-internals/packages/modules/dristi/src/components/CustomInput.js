import { Button, CardLabel, RemoveableTag, TextInput } from "@egovernments/digit-ui-react-components";
import React from "react";
import { useForm } from "react-hook-form";

function CustomInput({ onChange, value, isDisabled, componentInFront, config, _defaultValues = {}, canAdd, handleAdd, handleRemove, chipList, t }) {
  const { register, watch, errors, handleSubmit } = useForm({
    defaultValues: _defaultValues,
  });
  const isDisable = isDisabled ? true : config.canDisable && Object.keys(errors).filter((i) => errors[i]).length;
  const inputs = config?.inputs;

  return inputs.map((input) => {
    return (
      <div style={{ width: "100%" }}>
        <CardLabel>{t(input.label)}</CardLabel>
        <div style={{ display: "flex", justifyContent: "left", gap: "20px" }}>
          <div style={{ display: "flex", width: "100%" }}>
            {componentInFront ? <span className="citizen-card-input citizen-card-input--front">{componentInFront}</span> : null}
            <TextInput
              value={"99999"}
              // onChange={onChange}
              prefix={""}
              name={input.name}
              minlength={input?.validation?.minLength}
              maxlength={input?.validation?.maxLength}
              validation={input?.validation}
              ValidationRequired={input?.validation}
              title={input?.validation?.title}
              disable={input?.disable ? input?.disable : false}
              // textInputStyle={{ flex: 1 }}
              // inputStyle={{ flex: 1, width: "100%" }}
              // style={{ width: "100%" }}
              inputRef={register(input?.validation)}
              isMandatory={errors[input?.name]}
            />
          </div>
          <Button
            label={"Add"}
            style={{ alignItems: "center" }}
            isDisabled={!canAdd}
            onButtonClick={() => {
              handleAdd(value);
            }}
          />
        </div>
        {chipList?.length > 0 ? (
          <div className="tag-container" style={{ width: "100%" }}>
            {chipList?.length > 0 &&
              chipList?.map((value, index) => {
                return (
                  <RemoveableTag
                    extraStyles={{
                      closeIconStyles: { fill: "#3D3C3C" },
                      tagStyles: { background: "#E8E8E8", textAlign: "center" },
                      textStyles: { display: "flex", alignItems: "center" },
                    }}
                    key={index}
                    text={value}
                    onClick={() => {
                      handleRemove(value);
                    }}
                  />
                );
              })}
          </div>
        ) : null}
      </div>
    );
  });
}

export default CustomInput;
