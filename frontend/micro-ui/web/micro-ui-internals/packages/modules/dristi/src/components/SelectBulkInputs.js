import { CardLabelError } from "@egovernments/digit-ui-components";
import { Button, CardLabel, RemoveableTag, TextInput } from "@egovernments/digit-ui-react-components";
import React, { useMemo, useState } from "react";

function SelectBulkInputs({ t, config, onSelect, formData = {}, errors }) {
  const [enableAdd, setEnableAdd] = useState(false);
  const inputs = useMemo(
    () =>
      config?.populators?.inputs || [
        {
          label: "CS_LOCATION",
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
    const temp = formData[config.key]?.[input.name] || [];
    setValue({ textfieldValue: "", [input.name]: [...temp, value] }, [[input.name], "textfieldValue"]);
    setEnableAdd(false);
  };

  const handleRemove = (value, input) => {
    const temp = formData[config.key]?.[input.name] || [];
    setValue(
      temp.filter((item) => item !== value),
      input.name
    );
  };

  const onChange = (event, input) => {
    const { value } = event.target;
    const temp = formData[config.key]?.[input.name] || [];
    if (
      ((input?.validation?.minLength ? value.length < input?.validation?.minLength : false) ||
        (input?.validation?.maxLength ? value.length > input?.validation?.maxLength : false) ||
        (input?.validation?.pattern ? !input?.validation?.pattern?.test(value) : false)) &&
      enableAdd
    ) {
      setEnableAdd(false);
    }
    if (
      (input?.validation?.minLength ? value.length >= input?.validation?.minLength : true) &&
      (input?.validation?.maxLength ? value.length <= input?.validation?.maxLength : true) &&
      (input?.validation?.pattern ? input?.validation?.pattern?.test(value) : true) &&
      !temp.includes(value)
    ) {
      setEnableAdd(true);
    }
    setValue(input?.validation?.isNumber ? value?.replace(/[^0-9]/g, "") : value, "textfieldValue");
  };

  return inputs.map((input) => {
    const currentValue = (formData && formData[config.key] && formData[config.key].textfieldValue) || "";
    const chipList = (formData && formData[config.key] && formData[config.key][input.name]) || "";
    return (
      <div className={`bulk-input-class ${input.className || ""}`} style={{ width: "100%" }}>
        {!config?.disableScrutinyHeader && <h3 className="bulk-input-header">{t(input.label)}</h3>}
        <div className="bulk-input-main">
          <div className="input-main">
            {input?.componentInFront ? <span className="citizen-card-input citizen-card-input--front">{input?.componentInFront}</span> : null}
            <TextInput
              value={currentValue}
              onChange={(event) => {
                onChange(event, input);
              }}
              name={input.name}
              minlength={input?.validation?.minLength}
              maxlength={input?.validation?.maxLength}
              // validation={input?.validation}
              // ValidationRequired={input?.validation}
              title={input?.validation?.title}
              disable={input?.disable || config?.disable}
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
            isDisabled={!enableAdd || errors?.[config?.key]?.[input.name]}
            onButtonClick={() => {
              handleAdd(currentValue, input);
            }}
          />
        </div>
        {errors?.[config?.key]?.[input.name] && (
          <CardLabelError className={errors?.[config?.key]?.[input.name] && "error-text"} style={{ margin: 0 }}>
            {t(errors?.[config?.key]?.[input.name] && errors?.[config?.key]?.[input.name])}
          </CardLabelError>
        )}
        {chipList?.length > 0 ? (
          <div className="tag-container" style={{ width: "100%" }}>
            {chipList?.length > 0 &&
              chipList?.map((value, index) => {
                return (
                  <RemoveableTag
                    disabled={config?.disable || input?.disabled || input.isDisabled}
                    extraStyles={{
                      closeIconStyles: { fill: "#3D3C3C" },
                      tagStyles: { background: "#E8E8E8", textAlign: "center", maxWidth: "100%" },
                      textStyles: { display: "flex", alignItems: "center" },
                    }}
                    key={index}
                    text={value}
                    onClick={() => {
                      handleRemove(value, input);
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
