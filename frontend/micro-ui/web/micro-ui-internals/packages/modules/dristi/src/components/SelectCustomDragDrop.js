import { UploadIcon } from "@egovernments/digit-ui-react-components";
import React, { useMemo } from "react";
import { FileUploader } from "react-drag-drop-files";
import { FileUploadIcon } from "../icons/svgIndex";
import { isEmptyObject } from "../Utils";
import CustomErrorTooltip from "./CustomErrorTooltip";
import RenderFileCard from "./RenderFileCard";
import { useToast } from "./Toast/useToast";

const DragDropJSX = ({ t, currentValue, error }) => {
  return (
    <React.Fragment>
      <div className={`drag-drop-container-desktop${error ? " alert-error-border" : ""}`}>
        <UploadIcon />
        <p className="drag-drop-text">
          {t("WBH_DRAG_DROP")} <text className="browse-text">{t("WBH_BULK_BROWSE_FILES")}</text>
        </p>
      </div>
      <div className="drag-drop-container-mobile">
        <div className={`file-count-class ${currentValue && currentValue.length > 0 ? "uploaded" : ""}`}>
          <h3>{currentValue && currentValue.length > 0 ? `${currentValue.length} File uploaded` : "No file selected"} </h3>
        </div>
        <div className="button-class">
          <div>
            <FileUploadIcon />
          </div>
          <h3>Upload</h3>
        </div>
      </div>
      {error && <span className="alert-error">{t(error.msg || "CORE_REQUIRED_FIELD_ERROR")}</span>}
    </React.Fragment>
  );
};

function SelectCustomDragDrop({ t, config, formData = {}, onSelect, errors, setError, clearErrors }) {
  const toast = useToast();
  const inputs = useMemo(
    () =>
      config?.populators?.inputs || [
        {
          documentHeader: "Aadhar",
          documentSubText: "subtext",
          isOptional: "CS_IS_OPTIONAL",
          infoTooltipMessage: "Tooltip",
          type: "DragDropComponent",
          uploadGuidelines: t("UPLOAD_DOC_50"),
          maxFileSize: 50,
          maxFileErrorMessage: "CS_FILE_LIMIT_50_MB",
          fileTypes: ["JPG", "PNG", "PDF"],
          isMultipleUpload: true,
        },
      ],
    [config?.populators?.inputs, t]
  );

  function setValue(value, input, isFileSizeLimitExceeded) {
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

    onSelect(config.key, isEmptyObject(updatedValue) ? null : updatedValue, { shouldValidate: isFileSizeLimitExceeded ? false : true });
  }

  const fileValidator = (file, input) => {
    // const fileType = file?.type.split("/")[1].toUpperCase();
    // if (fileType && !input.fileTypes.includes(fileType)) {
    //   return { [input?.name]: "Invalid File Type", ...uploadErrorInfo };
    // }
    if (file?.fileStore) return null;
    const maxFileSize = input?.maxFileSize * 1024 * 1024;
    return file.size > maxFileSize ? t(input?.maxFileErrorMessage) : null;
  };

  const handleChange = (file, input, index = Infinity) => {
    let currentValue = (formData && formData[config.key] && formData[config.key][input.name]) || [];
    currentValue.splice(index, 1, file);
    const maxFileSize = input?.maxFileSize * 1024 * 1024;
    if (file.size > maxFileSize) {
      setError(config.key, { message: t(input?.maxFileErrorMessage) });
    }
    setValue(currentValue, input?.name, file.size > maxFileSize);
  };

  const handleDeleteFile = (input, index) => {
    let currentValue = (formData && formData[config.key] && formData[config.key][input.name]) || [];
    currentValue.splice(index, 1);
    if (clearErrors) {
      clearErrors(config.key);
    }
    setValue(currentValue, input?.name);
  };
  return inputs.map((input) => {
    let currentValue = (formData && formData[config.key] && formData[config.key][input.name]) || [];
    let fileErrors = currentValue.map((file) => fileValidator(file, input));
    const showFileUploader = currentValue.length ? input?.isMultipleUpload : true;
    const showDocument =
      config?.isDocDependentOn && config?.isDocDependentKey
        ? formData?.[config?.isDocDependentOn]?.[config?.isDocDependentKey]
        : !input?.hideDocument;
    return (
      <React.Fragment>
        {showDocument && (
          <div className="drag-drop-visible-main">
            <div className="drag-drop-heading-main">
              {!config?.disableScrutinyHeader && (
                <div className="drag-drop-heading">
                  <h1 className="card-label custom-document-header" style={input?.documentHeaderStyle}>
                    {t(input?.documentHeader)}
                  </h1>
                  {input?.isOptional && <span style={{ color: "#77787B" }}>&nbsp;{`${t(input?.isOptional)}`}</span>}
                  <CustomErrorTooltip message={t(input?.documentHeader)} showTooltip={Boolean(input?.infoTooltipMessage)} />
                </div>
              )}
              {input.documentSubText && <p className="custom-document-sub-header">{t(input.documentSubText)}</p>}
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
                disableUploadDelete={config?.disable}
              />
            ))}

            <div className={`file-uploader-div-main ${showFileUploader ? "show-file-uploader" : ""}`}>
              <FileUploader
                disabled={config?.disable}
                handleChange={(data) => {
                  handleChange(data, input);
                }}
                name="file"
                types={input?.fileTypes}
                children={<DragDropJSX t={t} currentValue={currentValue} error={errors?.[config.key]} />}
                key={input?.name}
                onTypeError={() => {
                  toast.error(t("CS_INVALID_FILE_TYPE"));
                }}
              />
              <div className="upload-guidelines-div">{input.uploadGuidelines && <p>{t(input.uploadGuidelines)}</p>}</div>
            </div>
            {input.downloadTemplateText && input.downloadTemplateLink && (
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
            )}
            {/* {errors?.[config.key] && <CardLabelError>{t(errors[config.key]?.message || "CORE_COMMON_INVALID")}</CardLabelError>} */}
          </div>
        )}
      </React.Fragment>
    );
  });
}

export default SelectCustomDragDrop;
