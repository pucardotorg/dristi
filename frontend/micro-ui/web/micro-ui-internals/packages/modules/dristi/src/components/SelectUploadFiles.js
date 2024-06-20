import React, { useMemo, useState } from "react";
import CustomErrorTooltip from "./CustomErrorTooltip";

import { FileUploader } from "react-drag-drop-files";
import { CloseSvg, UploadIcon } from "@egovernments/digit-ui-react-components";
import RenderFileCard from "./RenderFileCard";
import MultiUploadWrapper from "./MultiUploadWrapper";
import Modal from "./Modal";
import DocViewerWrapper from "../pages/employee/docViewerWrapper";

const textAreaJSX = (value, t, input, handleChange) => {
  return (
    <div className="custom-text-area-main-div" style={input?.style}>
      <div className="custom-text-area-header-div">
        <h1 className={`text-area-upload-header ${input?.headerClassName}`} style={{ margin: "0px" }}>
          {t(input?.textAreaHeader)}
        </h1>
        {
          <div>
            <span>
              <p className={`text-area-upload-sub-header ${input?.subHeaderClassName}`} style={{ margin: "0px" }}>
                {`${t(input?.textAreaSubHeader)}`}
                {input?.isOptional && <span style={{ color: "#77787B" }}>&nbsp;{t("CS_IS_OPTIONAL")}</span>}
              </p>
            </span>
          </div>
        }
      </div>

      <textarea
        value={value}
        onChange={(data) => {
          handleChange(data, input);
        }}
        rows={5}
        className="custom-textarea-style"
        placeholder={input?.placeholder}
      ></textarea>
    </div>
  );
};

const CloseBtn = (props) => {
  return (
    <div onClick={props?.onClick} style={{ height: "100%", display: "flex", alignItems: "center", paddingRight: "20px", cursor: "pointer" }}>
      <CloseSvg />
    </div>
  );
};

const Heading = (props) => {
  return <h1 className="heading-m">{props.label}</h1>;
};

function SelectUploadFiles({ t, config, formData = {}, onSelect }) {
  const checkIfTextPresent = () => {
    if (!formData) {
      return true;
    }
    if (Object.keys(formData).length === 0) {
      return true;
    }
    if (Object.keys(formData?.[config.key] || {}).length === 0) {
      return true;
    } else if (formData?.[config.key]?.text) {
      return true;
    } else if (formData && !formData?.[config.key]?.text && formData?.[config.key]?.document && formData?.[config.key]?.document.length === 0) {
      return true;
    } else {
      return false;
    }
  };

  const checkIfFilesPresent = () => {
    if (formData && formData?.[config.key]?.document && formData?.[config.key]?.document.length !== 0) {
      return true;
    } else return false;
  };
  const [showTextArea, setShowTextArea] = useState(checkIfTextPresent);
  const [showModal, setShowModal] = useState(false);
  const [isFileAdded, setIsFileAdded] = useState(checkIfFilesPresent);
  const [isAddButtonDisabled, setIsAddButtonDisabled] = useState(true);

  const inputs = useMemo(
    () =>
      config?.populators?.inputs || [
        {
          name: "text",
          textAreaHeader: "Custom note",
          textAreaSubHeader: "Please provide some more info.",
          type: "TextAreaComponent",
          isOptional: false,
        },
        {
          name: "document",
          documentHeader: "Aadhar",
          documentSubText: "subtext",
          isOptional: "CS_IS_OPTIONAL",
          infoTooltipMessage: "Tooltip",
          label: "Title",
          type: "DragDropComponent",
          uploadGuidelines: t("UPLOAD_DOC_50"),
          maxFileSize: 50,
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
    } else {
      onSelect(config.key, { ...formData[config.key], [input]: value });
    }
  }

  const fileValidator = (file, input) => {
    const maxFileSize = input?.maxFileSize * 1024 * 1024;
    return file.size > maxFileSize ? t(input?.maxFileErrorMessage) : null;
  };

  const handleChange = (file, input, index = Infinity) => {
    let currentValue = (formData && formData[config.key] && formData[config.key][input.name]) || [];
    currentValue.splice(index, 1, file);
    // setValue(currentValue, input?.name);
    onSelect(config.key, { ...formData[config.key], [input?.name]: currentValue });
  };

  const handleDeleteFile = (input, index) => {
    let currentValue = (formData && formData[config.key] && formData[config.key][input.name]) || [];
    currentValue.splice(index, 1);
    if (currentValue.length === 0) {
      setShowTextArea(true);
      setIsFileAdded(false);
    }
    onSelect(config.key, { ...formData[config.key], [input?.name]: currentValue });
  };

  const dragDropJSX = (
    <div className="drag-drop-container">
      <UploadIcon />
      <p className="drag-drop-text">
        {t("WBH_DRAG_DROP")} <text className="browse-text">{t("WBH_BULK_BROWSE_FILES")}</text>
      </p>
    </div>
  );

  const openModal = () => {
    setShowModal(true);
    onSelect(config.key, { ...formData[config.key], document: [] });
  };

  const handleCloseModal = () => {
    setShowModal(false);
  };

  const handleAddFileInModal = (data) => {
    setShowTextArea(false);
    setShowModal(false);
    setIsFileAdded(true);
    const dataCopy = structuredClone(formData[config.key]);
    if ("text" in dataCopy) {
      delete dataCopy.text;
    }
    onSelect(config.key, dataCopy);
  };

  function getFileStoreData(filesData, input) {
    const numberOfFiles = filesData.length;
    const fileArray = [];
    fileArray.push(filesData?.[0]?.[1]?.file);
    if (numberOfFiles > 0) {
      setIsAddButtonDisabled(false);
    }
    onSelect(config.key, { ...formData[config.key], [input.name]: numberOfFiles > 0 ? fileArray : [] });
  }

  const handleTextChange = (data, input) => {
    const formDataCopy = structuredClone(formData);
    delete formDataCopy[config.key]?.document;
    onSelect(config.key, { ...formDataCopy[config.key], [input.name]: data.target.value });
    return;
  };

  return inputs.map((input) => {
    const currentDocs = (formData && formData[config.key] && formData?.[config.key]?.["document"]) || [];
    if (input.type === "TextAreaComponent" && showTextArea) {
      return (
        <div className="text-area-and-upload">
          {textAreaJSX(formData?.[config.key]?.text || "", t, input, handleTextChange)}
          {!isFileAdded && (
            <div className="text-upload-file-main">
              <p>{t("CS_WANT_TO_UPLOAD")}</p>
              <span onClick={openModal} style={{ textDecoration: "underline", color: "#007E7E" }}>
                {t("WBH_BULK_BROWSE_FILES")}
              </span>
            </div>
          )}
        </div>
      );
    }
    if (input.type === "DragDropComponent") {
      const currentValue = (formData && formData[config.key] && formData[config.key].document) || [];
      let fileErrors = [];
      if (currentValue.length > 0) {
        fileErrors = currentValue.map((file) => fileValidator(file, input));
      }
      const showFileUploader = currentValue.length ? input?.isMultipleUpload : false;
      return (
        <div>
          {isFileAdded && (
            <div className="drag-drop-visible-main">
              <div className="drag-drop-heading-main">
                <div className="drag-drop-heading">
                  <h2 className="card-label">{t(input?.documentHeader)}</h2>
                  {input?.isOptional && <h3>{t(input?.isOptional)}</h3>}
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
                <div className={`file-uploader-div-main show-file-uploader select-UploadFiles`}>
                  <div className="file-uploader">
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
            </div>
          )}
          {showModal && (
            <Modal
              headerBarEnd={<CloseBtn onClick={handleCloseModal} isMobileView={true} />}
              actionCancelOnSubmit={() => {}}
              actionSaveLabel={t("ADD")}
              actionSaveOnSubmit={(data) => handleAddFileInModal(data)}
              // hideSubmit={false}
              isDisabled={isAddButtonDisabled}
              formId="modal-action"
              headerBarMain={<Heading label={t("UPLOAD_FILE")} />}
              submitTextClassName={"verification-button-text-modal"}
            >
              <div>
                {<h1>{t(input.label)}</h1>}

                <MultiUploadWrapper
                  t={t}
                  module="works"
                  getFormState={(fileData) => getFileStoreData(fileData, input)}
                  showHintBelow={input?.showHintBelow ? true : false}
                  setuploadedstate={formData?.[config.key]?.[input.name] || []}
                  allowedMaxSizeInMB={"5"}
                  maxFilesAllowed={input.maxFilesAllowed || "1"}
                  extraStyleName={{ padding: "0.5rem" }}
                  customClass={input?.customClass}
                  containerStyles={{ ...input?.containerStyles }}
                  allowedFileTypesRegex={input.allowedFileTypes}
                  noteMsg={input?.noteMsg}
                  notSupportedError={input?.notSupportedError}
                  maxFileErrorMessage={input?.maxFileErrorMessage}
                />
                {Array.isArray(formData?.[config.key]?.[input.name]) && formData?.[config.key]?.[input.name].length > 0 && (
                  <DocViewerWrapper
                    selectedDocs={[formData?.[config.key]?.[input.name][0]]}
                    showDownloadOption={false}
                    docViewerCardClassName={"doc-viewer-card-style"}
                  ></DocViewerWrapper>
                )}
              </div>
            </Modal>
          )}
        </div>
      );
    }
  });
}

export default SelectUploadFiles;
