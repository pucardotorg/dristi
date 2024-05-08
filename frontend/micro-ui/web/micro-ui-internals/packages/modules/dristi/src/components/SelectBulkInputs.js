import { Button, CardLabel, RemoveableTag, TextInput } from "@egovernments/digit-ui-react-components";
import React, { useMemo, useState } from "react";

function SelectBulkInputs({ t, config, onSelect, formData = {}, errors }) {
  const [canAdd, setCanAdd] = useState(false);
  const inputs = useMemo(
    () =>
      config?.populators?.inputs || [
        {
          label: "CS_PIN_LOCATION",
          type: "LocationSearch",
          name: [],
        },
      ],
    [config?.populators?.inputs]
  );

  function setValue(value, input) {
    if (Array.isArray(input)) {
      onSelect(config.key, {
        ...formData[config.key],
        ...input.reduce((res, curr) => {
          res[curr] = value[curr];
          return res;
        }, {}),
      });
    } else onSelect(config.key, { ...formData[config.key], [input]: value });
  }

  const handleAdd = (value, input) => {
    const temp = formData[config.key]?.values || [];
    setValue({ values: [...temp, value], [input.name]: "" }, ["values", [input.name]]);
  };

  const handleRemove = (value) => {
    const temp = formData[config.key]?.values || [];
    setValue(
      temp.filter((item) => item !== value),
      "values"
    );
  };

  const chipList = useMemo(() => formData[config.key]?.values || [], [config.key, formData]);

  const onChange = (event, input) => {
    const { value } = event.target;
    const temp = formData[config.key]?.values || [];
    if (
      ((input?.validation?.minLength ? value.length < input?.validation?.minLength : false) ||
        (input?.validation?.maxLength ? value.length > input?.validation?.maxLength : false) ||
        (input?.validation?.pattern ? !input?.validation?.pattern?.test(value) : false)) &&
      canAdd
    ) {
      setCanAdd(false);
    }
    if (
      (input?.validation?.minLength ? value.length >= input?.validation?.minLength : true) &&
      (input?.validation?.maxLength ? value.length <= input?.validation?.maxLength : true) &&
      (input?.validation?.pattern ? input?.validation?.pattern?.test(value) : true) &&
      !temp.includes(value)
    ) {
      setCanAdd(true);
    }
    setValue(value, input.name);
  };

  return inputs.map((input) => {
    let currentValue = (formData && formData[config.key] && formData[config.key][input.name]) || "";
    return (
      <div style={{ width: "100%" }}>
        <CardLabel>{t(input.label)}</CardLabel>
        <div style={{ display: "flex", justifyContent: "left", gap: "20px" }}>
          <div style={{ display: "flex", width: "100%" }}>
            {input?.componentInFront ? <span className="citizen-card-input citizen-card-input--front">{input?.componentInFront}</span> : null}
            <TextInput
              value={currentValue}
              onChange={(event) => {
                onChange(event, input);
              }}
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
              //   inputRef={register(input?.validation)}
              isMandatory={errors[input?.name]}
            />
          </div>
          <Button
            label={"Add"}
            style={{ alignItems: "center" }}
            isDisabled={!canAdd}
            onButtonClick={() => {
              handleAdd(currentValue, input);
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

export default SelectBulkInputs;
