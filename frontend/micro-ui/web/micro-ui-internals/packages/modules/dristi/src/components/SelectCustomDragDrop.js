import React, { useMemo, useState } from "react";
import CustomErrorTooltip from "./CustomErrorTooltip";
import { FileUploader } from "react-drag-drop-files";
import { UploadIcon } from "@egovernments/digit-ui-react-components";
import RenderFileCard from "./RenderFileCard";

function SelectCustomDragDrop({ t, config, formData = {}, onSelect }) {
  const [uploadErrorInfo, setUploadErrorInfo] = useState({});

  const inputs = useMemo(
    () =>
      config?.populators?.inputs || [
        {
          documentHeader: "Aadhar",
          documentSubText: "subtext",
          isOptional: "optional",
          infoTooltipMessage: "Tooltip",
          type: "DragDropComponent",
          uploadGuidelines: "Upload .png",
          maxFileSize: 1024 * 1024 * 50,
          maxFileErrorMessage: "CS_FILE_LIMIT_50_MB",
          fileTypes: ["JPG", "PNG", "PDF"],
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

  const fileValidator = (file, input) => {
    // const fileType = file?.type.split("/")[1].toUpperCase();
    // if (fileType && !input.fileTypes.includes(fileType)) {
    //   return { [input?.name]: "Invalid File Type", ...uploadErrorInfo };
    // }
    if (file.size > input?.maxFileSize) {
      return { ...uploadErrorInfo, [input?.name]: t(input?.maxFileErrorMessage) };
    }
    return { ...uploadErrorInfo, [input?.name]: "" };
  };

  const handleChange = (file, input) => {
    const error = fileValidator(file, input);
    setUploadErrorInfo(error);
    setValue(file, input?.name);
  };

  const handleDeleteFile = (input) => {
    setValue(null, input?.name);
  };

  const dragDropJSX = (
    <div className="drag-drop-container">
      <UploadIcon />
      <p className="drag-drop-text">
        {t("WBH_DRAG_DROP")} <text className="browse-text">{t("WBH_BULK_BROWSE_FILES")}</text>
      </p>
    </div>
  );

  return inputs.map((input) => {
    let currentValue = (formData && formData[config.key] && formData[config.key][input.name]) || "";
    return (
      <div>
        {!currentValue && (
          <div className="drag-drop-visible-main">
            <div className="drag-drop-heading-main">
              <div className="drag-drop-heading">
                <h1> {t(input?.documentHeader)}</h1>
                {input?.isOptional && <h3> {`(${t(input?.isOptional)})`}</h3>}
                <CustomErrorTooltip message={t(input?.infoTooltipMessage)} showTooltip={Boolean(input?.infoTooltipMessage)} />
              </div>
              {<p>{t(input?.documentSubText)}</p>}
            </div>
            <div className="file-uploader-div-main">
              <FileUploader
                handleChange={(data) => {
                  handleChange(data, input);
                }}
                name="file"
                types={input?.fileTypes}
                children={dragDropJSX}
                key={input?.name}
              />
            </div>
            <div className="upload-guidelines-div">
              <p>{t(input?.uploadGuidelines)}</p>
            </div>
          </div>
        )}
        {currentValue && (
          <RenderFileCard
            fileData={currentValue}
            handleChange={handleChange}
            handleDeleteFile={handleDeleteFile}
            t={t}
            uploadErrorInfo={uploadErrorInfo?.[input.name]}
            input={input}
            key={input?.name}
          />
        )}
      </div>
    );
  });
}

export default SelectCustomDragDrop;
