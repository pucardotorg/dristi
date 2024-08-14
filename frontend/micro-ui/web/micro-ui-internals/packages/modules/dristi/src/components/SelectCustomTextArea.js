import React, { useMemo, useState } from "react";
import CustomErrorTooltip from "./CustomErrorTooltip";
import { FileUploader } from "react-drag-drop-files";
import { UploadIcon } from "@egovernments/digit-ui-react-components";
import RenderFileCard from "./RenderFileCard";

function SelectCustomTextArea({ t, config, formData = {}, onSelect }) {
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
      onSelect(config.key, {
        ...formData[config.key],
        ...input.reduce((res, curr) => {
          res[curr] = value[curr];
          return res;
        }, {}),
      });
    } else onSelect(config.key, { ...formData[config.key], [input]: value });
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
          className="custom-textarea-style"
          placeholder={t(input?.placeholder)}
          disabled={config.disable}
        ></textarea>
      </div>
    );
  });
}

export default SelectCustomTextArea;
