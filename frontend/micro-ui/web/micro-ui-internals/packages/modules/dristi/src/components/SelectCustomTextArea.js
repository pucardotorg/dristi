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
        <div>
          <h1> {t(input?.textAreaHeader)}</h1>
        </div>
        <div>
          <textarea
            onChange={(data) => {
              handleChange(data, input);
            }}
            rows={5}
            className="custom-textarea-style"
          ></textarea>
        </div>
      </div>
    );
  });
}

export default SelectCustomTextArea;
