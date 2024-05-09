import React, { useMemo, useRef, useState } from "react";
import CustomErrorTooltip from "./CustomErrorTooltip";
import { FileUploader } from "react-drag-drop-files";
import { UploadIcon } from "@egovernments/digit-ui-react-components";
import RenderFileCard from "./RenderFileCard";

function SelectCustomDragDrop({ t, config, formData = {}, onSelect }) {
  const [uploadErrorInfo, setUploadErrorInfo] = useState({});
  const fileInputRef = useRef();

  const fileData = useMemo(() => {
    return formData?.[config?.key] || {};
  }, [config?.key, formData]);

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
    const fileType = file?.type.split("/")[1].toUpperCase();
    if (fileType && !input.fileTypes.includes(fileType)) {
      setUploadErrorInfo({ [input?.name]: "Invalid File Type", ...uploadErrorInfo });
      return false;
    }
    if (file.size > input?.maxFileSize) {
      setUploadErrorInfo({ [input?.name]: "Your file exceeded the 50mb limit.", ...uploadErrorInfo });
      return false;
    }
    setUploadErrorInfo({ [input?.name]: "", ...uploadErrorInfo });
    return true;
  };

  const handleChange = (data, input, isReupload = false) => {
    const file = isReupload ? data.target.files[0] : data;
    const isFileValid = fileValidator(file, input);
    if (isFileValid) {
      setValue(file, input?.name);
    }
  };

  const handleDeleteFile = (input) => {
    setValue(null, input?.name);
  };

  const handleReupload = () => {
    fileInputRef.current.click();
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
            fileInputRef={fileInputRef}
            handleChange={handleChange}
            handleDeleteFile={handleDeleteFile}
            handleReupload={handleReupload}
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
