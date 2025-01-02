import React, { useEffect, useMemo, useState } from "react";
import { FileUploader } from "react-drag-drop-files";
import { UploadIcon } from "../icons/svgIndex";
import DocViewerWrapper from "../pages/employee/docViewerWrapper";
import { CardLabelError, TextInput, CloseSvg, Toast } from "@egovernments/digit-ui-react-components";
import Button from "./Button";

const CloseBtn = (props) => {
  return (
    <div
      onClick={props?.onClick}
      style={{
        height: "100%",
        display: "flex",
        alignItems: "center",
        cursor: "pointer",
      }}
    >
      <CloseSvg />
    </div>
  );
};

const DragDropComponent = ({ config, label }) => {
  return (
    <div
      style={{
        border: "1px solid #007E7E",
        display: "flex",
        alignItems: "center",
        justifyContent: "center",
        padding: "4px 8px",
        fontFamily: "Arial, sans-serif",
        fontSize: "14px",
        color: "#007E7E",
        height: "40px",
        gap: "4px",
        width: "100%",
        opacity: !config?.disable ? 1 : 0.5,
        cursor: !config?.disable ? "pointer" : "default",
      }}
    >
      <UploadIcon
        style={{
          width: "16px",
          height: "16px",
          marginRight: "4px",
          color: "#007E7E",
        }}
      />
      <span style={{ color: "#007E7E", fontWeight: 600 }}>{label}</span>
    </div>
  );
};

const SelectMultiUpload = ({ t, config, onSelect, formData = {}, errors, setError, clearErrors }) => {
  const [showErrorToast, setShowErrorToast] = useState(null);
  const tenantId = Digit.ULBService.getCurrentTenantId();

  const closeToast = () => {
    setShowErrorToast(null);
  };

  const inputs = useMemo(
    () =>
      config?.populators?.inputs || [
        {
          name: "uploadedDocs",
          isMandatory: true,
          textAreaHeader: "CS_DOCUMENT",
          fileTypes: ["JPG", "PDF", "PNG", "JPEG"],
          uploadGuidelines: "UPLOAD_DOC_50",
          maxFileSize: 50,
          maxFileErrorMessage: "CS_FILE_LIMIT_50_MB",
          textAreaStyle: {
            fontSize: "16px",
            fontWeight: 400,
            marginBottom: "8px",
          },
        },
      ],
    [config?.populators?.inputs]
  );

  useEffect(() => {
    if (showErrorToast) {
      const timer = setTimeout(() => {
        setShowErrorToast(null);
      }, 2000);
      return () => clearTimeout(timer);
    }
  }, [showErrorToast]);

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

  function handleAddFiles(data, input, currentValue) {
    const maxFileSize = input?.maxFileSize * 1024 * 1024;
    if (data.size > maxFileSize) {
      setShowErrorToast({ label: t("FILE_SIZE_EXCEEDS"), error: true });
      return;
    }

    const updatedDocuments = [...currentValue, data];
    setValue(updatedDocuments, input?.name);
  }

  const handleRemoveFile = (file, index, currentValue, input) => {
    const updatedValue = currentValue.filter((item, idx) => idx !== index);
    setValue(updatedValue, input?.name);
  };

  return inputs?.map((input) => {
    const currentValue = formData?.[config.key]?.[input?.name] || [];

    return (
      <React.Fragment>
        <style>
          {`
            .file-uploader {
              display: flex;
              align-items: center;
              justify-content: space-between;
            }

            .file-uploader .text-input.text-input-width {
              max-width: calc(100% - 138px);
              flex: 1;
            }

            .file-uploader input[type="file"] {
              width: 100%;
            }

            .file-uploader label {
              width: 130px;
              display: flex;
              align-items: center;
              justify-content: center;
            }

            .file-uploader-div-main .toast-success.error h2 {
              color: white !important;
            }
        `}
        </style>
        <div className={`file-uploader-div-main show-file-uploader select-UploadFiles`}>
          {input.textAreaHeader && (
            <h1 className={`custom-text-area-header ${input?.headerClassName}`} style={{ margin: "0px 0px 8px", ...input.textAreaStyle }}>
              {t(input?.textAreaHeader)}
            </h1>
          )}
          <div className="file-uploader" style={{ display: "flex", alignItems: "center", justifyContent: "space-between" }}>
            <TextInput
              disable={true}
              value={currentValue?.length > 0 ? `${currentValue?.length} Documents(s) Uploaded` : "No Document(s) Uploaded"}
              style={{
                border: "1px solid black",
                fontSize: "14px",
                color: "#888",
                margin: "0px",
              }}
            />
            {!config?.disable && (
              <FileUploader
                disabled={config?.disable}
                handleChange={(data) => handleAddFiles(data, input, currentValue)}
                name="file"
                types={input?.fileTypes}
                children={<DragDropComponent config={config} label={currentValue?.length > 0 ? t("UPLOAD_MORE") : t("UPLOAD")} />}
                key={input?.name}
              />
            )}
          </div>
          <div className="upload-guidelines-div">
            {input?.fileTypes && input?.maxFileSize ? (
              <p>
                {`${t("CS_COMMON_CHOOSE_FILE")} ${
                  input?.fileTypes.length > 1
                    ? `${input?.fileTypes
                        .slice(0, -1)
                        .map((type) => `.${type.toLowerCase()}`)
                        .join(", ")} ${t("CS_COMMON_OR")} .${input?.fileTypes[input?.fileTypes.length - 1].toLowerCase()}`
                    : `.${input?.fileTypes[0].toLowerCase()}`
                }. ${t("CS_MAX_UPLOAD")} ${input.maxFileSize}MB`}
              </p>
            ) : (
              <p>{input.uploadGuidelines}</p>
            )}
          </div>
          <div
            style={{
              width: "100%",
              overflowX: "auto",
              display: "flex",
              gap: "10px",
              position: "relative",
            }}
          >
            {currentValue?.map((file, index) => (
              <div key={(file?.additionalDetails?.name || file?.name) + index} style={{ position: "relative", display: "inline-block" }}>
                <DocViewerWrapper
                  fileStoreId={file?.fileStore}
                  selectedDocs={[file]}
                  showDownloadOption={false}
                  docViewerCardClassName="doc-viewer-card-style"
                  style={{ flexShrink: 0 }}
                  tenantId={tenantId}
                />
                <p style={{ marginTop: "10px", fontSize: "14px", color: "#888" }}>{file?.additionalDetails?.name || file?.name}</p>

                {!config?.disable && (
                  <Button
                    key={(file?.additionalDetails?.name || file?.name) + index}
                    onButtonClick={() => {
                      handleRemoveFile(file, index, currentValue, input);
                    }}
                    children={<CloseBtn />}
                    label=""
                    style={{
                      position: "absolute",
                      top: "5px",
                      right: "5px",
                      background: "none",
                      color: "#FF0000",
                      fontSize: "16px",
                      width: "20px",
                      height: "20px",
                      display: "flex",
                      alignItems: "center",
                      justifyContent: "center",
                      cursor: "pointer",
                      padding: "0",
                      border: "none",
                      boxShadow: "none",
                    }}
                  />
                )}
              </div>
            ))}
          </div>
          {errors[input?.name] && (
            <CardLabelError style={{ width: "70%", marginLeft: "30%", fontSize: "12px" }}>
              {errors[input.name]?.message ? errors[input.name]?.message : t(errors[input.name]) || t(input.error)}
            </CardLabelError>
          )}
          {showErrorToast && <Toast error={showErrorToast?.error} label={showErrorToast?.label} isDleteBtn={true} onClose={closeToast} />}
        </div>
      </React.Fragment>
    );
  });
};

export default SelectMultiUpload;
