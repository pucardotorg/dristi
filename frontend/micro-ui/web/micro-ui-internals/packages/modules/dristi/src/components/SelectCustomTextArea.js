import { CardLabelError } from "@egovernments/digit-ui-react-components";
import React, { useEffect, useMemo, useState } from "react";
import { isEmptyObject } from "../Utils";
import isEqual from "lodash/isEqual";

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

  const [formdata, setFormData] = useState(formData);

  useEffect(() => {
    if (!isEqual(formdata, formData)) {
      setFormData(formData);
    }
  }, [formData]);

  function setValue(value, input) {
    let updatedValue = {
      ...formData[config.key],
    };

    if (Array.isArray(input)) {
      updatedValue = {
        ...updatedValue,
        ...input.reduce((res, curr) => {
          res[curr] = value[curr];
          return res;
        }, {}),
      };
    } else {
      updatedValue[input] = value;
    }

    if (!value) {
      updatedValue = null;
    }

    setFormData((prevData) => ({
      ...prevData,
      [config.key]: {
        ...prevData[config.key],
        [input]: value,
      },
    }));

    onSelect(config.key, isEmptyObject(updatedValue) ? null : updatedValue, { shouldValidate: true });
  }

  const handleChange = (event, input) => {
    let newText = event.target.value.trimStart().replace(/ +/g, " ");
    if (typeof config?.populators?.validation?.pattern === "object") {
      newText = newText.replace(config?.populators?.validation?.pattern, "");
    }
    setValue(newText, input?.name);
  };

  return inputs.map((input) => {
    return (
      <div className="custom-text-area-main-div" style={input?.style}>
        <div className="custom-text-area-header-div">
          {input.textAreaHeader && (
            <h1 className={`custom-text-area-header ${input?.headerClassName}`} style={{ margin: "0px 0px 8px", ...input.textAreaStyle }}>
              {t(input?.textAreaHeader)}
            </h1>
          )}
          {!config?.disableScrutinyHeader && (
            <span>
              <p className={`custom-sub-header ${input?.subHeaderClassName}`} style={{ margin: "0px 0px 8px" }}>
                {`${t(input?.textAreaSubHeader)}`}
                {input?.isOptional && <span style={{ color: "#77787B" }}>&nbsp;(optional)</span>}
              </p>
            </span>
          )}
        </div>
        <textarea
          value={formdata?.[config.key]?.[input.name]}
          onChange={(data) => {
            handleChange(data, input);
          }}
          rows={5}
          maxLength={400}
          className={`custom-textarea-style${errors[config.key] ? " alert-error-border" : ""}`}
          placeholder={t(input?.placeholder)}
          disabled={config.disable}
        ></textarea>
        {errors[config.key] && <CardLabelError style={input?.errorStyle}>{t(errors[config.key].msg || "CORE_REQUIRED_FIELD_ERROR")}</CardLabelError>}
      </div>
    );
  });
}

export default SelectCustomTextArea;
