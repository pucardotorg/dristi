import React, { useMemo } from "react";
import CustomErrorTooltip from "./CustomErrorTooltip";
import { FileUploader } from "react-drag-drop-files";
import { UploadIcon } from "@egovernments/digit-ui-react-components";
import RenderFileCard from "./RenderFileCard";

function SelectCustomDragDrop({ t, config, formData = {}, onSelect }) {
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
          isMultipleUpload: true,
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
    const maxFileSize = input?.maxFileSize * 1024 * 1024;
    return file.size > maxFileSize ? t(input?.maxFileErrorMessage) : null;
  };

  const handleChange = (file, input, index = Infinity) => {
    let currentValue = (formData && formData[config.key] && formData[config.key][input.name]) || [];
    currentValue.splice(index, 1, file);
    setValue(currentValue, input?.name);
  };

  const handleDeleteFile = (input, index) => {
    let currentValue = (formData && formData[config.key] && formData[config.key][input.name]) || [];
    currentValue.splice(index, 1);
    setValue(currentValue, input?.name);
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
    let currentValue = (formData && formData[config.key] && formData[config.key][input.name]) || [];
    let fileErrors = currentValue.map((file) => fileValidator(file, input));
    const showFileUploader = currentValue.length ? input?.isMultipleUpload : true;
    return (
      <div className="drag-drop-visible-main">
        <div className="drag-drop-heading-main">
          <div className="drag-drop-heading">
            <h1> {t(input?.documentHeader)}</h1>
            {input?.isOptional && <h3> {`(${t(input?.isOptional)})`}</h3>}
            <CustomErrorTooltip message={t(input?.infoTooltipMessage)} showTooltip={Boolean(input?.infoTooltipMessage)} />
          </div>
          {<p>{t(input?.documentSubText)}</p>}
        </div>
        {currentValue.map((file, index) => (
          <RenderFileCard
            key={`${input?.name}${index}`}
            index={index}
            fileData={file}
            handleChange={handleChange}
            handleDeleteFile={handleDeleteFile}
            t={t}
            uploadErrorInfo={fileErrors[index]}
            input={input}
          />
        ))}
        {showFileUploader && (
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
            <div className="upload-guidelines-div">
              <p>{t(input?.uploadGuidelines)}</p>
            </div>
          </div>
        )}
        <div style={{ display: "flex", alignItems: "center", justifyContent: "flex-start", gap: "20px" }}>
          {input?.downloadTemplateText && t(input?.downloadTemplateText)}
          {input?.downloadTemplateLink && (
            <a
              href={input?.downloadTemplateLink}
              target="_blank"
              rel="noreferrer"
              style={{
                display: "flex",
                color: "#9E400A",
                textDecoration: "none",
                width: 250,
                whiteSpace: "nowrap",
                overflow: "hidden",
                textOverflow: "ellipsis",
              }}
            >
              {t("CS__DOWNLOAD_TEMPLATE")}
            </a>
          )}
        </div>
      </div>
    );
  });
}

export default SelectCustomDragDrop;
