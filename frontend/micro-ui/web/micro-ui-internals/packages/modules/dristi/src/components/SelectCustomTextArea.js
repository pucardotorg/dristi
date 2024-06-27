import { CardLabelError } from "@egovernments/digit-ui-react-components";
import React, { useMemo } from "react";

function SelectCustomTextArea({ t, config, formData = {}, onSelect, errors }) {
  const inputs = useMemo(
    () =>
      config?.populators?.inputs || [
        {
          textAreaHeader: "custom note",
          textAreaSubHeader: "please provide some more details.",
          isOptional: false,
        },
      ],
    [config?.populators?.inputs]
  );

  function setValue(value, input) {
    if (Array.isArray(input)) {
      onSelect(
        config.key,
        {
          ...formData[config.key],
          ...input.reduce((res, curr) => {
            res[curr] = value[curr];
            return res;
          }, {}),
        },
        { shouldValidate: true }
      );
    } else onSelect(config.key, { ...formData[config.key], [input]: value }, { shouldValidate: true });
  }

  const handleChange = (event, input) => {
    const newText = event.target.value;
    setValue(newText, input?.name);
  };

  return inputs.map((input) => {
    return (
      <div className="custom-text-area-main-div" style={input?.style}>
        <div className="custom-text-area-header-div">
          {input.textAreaHeader && (
            <h1 className={`custom-text-area-header ${input?.headerClassName}`} style={{ margin: "0px" }}>
              {t(input?.textAreaHeader)}
            </h1>
          )}
          {
            <span>
              <p className={`custom-sub-header ${input?.subHeaderClassName}`} style={{ margin: "0px" }}>
                {`${t(input?.textAreaSubHeader)}`}
                {input?.isOptional && <span style={{ color: "#77787B" }}>&nbsp;(optional)</span>}
              </p>
            </span>
          }
        </div>
        <textarea
          value={formData?.[config.key]?.[input.name]}
          onChange={(data) => {
            handleChange(data, input);
          }}
          rows={5}
          maxLength={400}
          className={`custom-textarea-style${errors[config.key] ? " alert-error-border" : ""}`}
          placeholder={t(input?.placeholder)}
          disabled={config.disable}
        ></textarea>
        {errors[config.key] && <CardLabelError>{t(errors[config.key].msg || "CORE_REQUIRED_FIELD_ERROR")}</CardLabelError>}
      </div>
    );
  });
}

export default SelectCustomTextArea;
