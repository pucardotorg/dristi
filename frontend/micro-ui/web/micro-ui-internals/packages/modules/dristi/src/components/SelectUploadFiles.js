import React, { useMemo, useState } from "react";
import CustomErrorTooltip from "./CustomErrorTooltip";

import { FileUploader } from "react-drag-drop-files";
import { CloseSvg, UploadIcon } from "@egovernments/digit-ui-react-components";
import RenderFileCard from "./RenderFileCard";
import MultiUploadWrapper from "./MultiUploadWrapper";
import Modal from "./Modal";
import DocViewerWrapper from "../pages/employee/docViewerWrapper";

const textAreaJSX = (t, input, handleChange) => {
  return (
    <div className="custom-text-area-main-div" style={input?.style}>
      <div className="custom-text-area-header-div">
        <h1 className={`${input?.headerClassName}`} style={{ margin: "0px" }}>
          {t(input?.textAreaHeader)}
        </h1>
        {
          <div>
            <span>
              <p className={`${input?.subHeaderClassName}`} style={{ margin: "0px" }}>
                {`${t(input?.textAreaSubHeader)}`}
                {input?.isOptional && <span style={{ color: "#77787B" }}>&nbsp;(optional)</span>}
              </p>
            </span>
          </div>
        }
      </div>
      <div>
        <textarea
          onChange={(data) => {
            handleChange(data, input);
          }}
          rows={5}
          className="custom-textarea-style"
          placeholder={input?.placeholder}
        ></textarea>
      </div>
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
  // const [onStartingPage, setOnStartingPage] = useState(true);
  const [showTextArea, setShowTextArea] = useState(true);
  const [showModal, setShowModal] = useState(false);
  const [isFileAdded, setIsFileAdded] = useState(false);
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
          isOptional: "optional",
          infoTooltipMessage: "Tooltip",
          type: "DragDropComponent",
          uploadGuidelines: "Upload .png",
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
    setValue(currentValue, input?.name);
  };

  const handleDeleteFile = (input, index) => {
    let currentValue = (formData && formData[config.key] && formData[config.key][input.name]) || [];
    currentValue.splice(index, 1);
    if (currentValue.length === 0) {
      setShowTextArea(true);
      setIsFileAdded(false);
    }
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

  const opneModal = () => {
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
    onSelect(config.key, { ...formData[config.key], [input.name]: data.target.value });
    return;
  };

  return inputs.map((input) => {
    const currentDocs = (formData && formData[config.key] && formData?.[config.key]?.["document"]) || [];
    if (input.type === "TextAreaComponent" && showTextArea) {
      return (
        <div>
          {textAreaJSX(t, input, handleTextChange)}
          {!isFileAdded && (
            <div>
              <p>want to upload a file instead?</p>
              <span onClick={opneModal} style={{ textDecoration: "underline", color: "#007E7E" }}>
                Browse in my files
              </span>
            </div>
          )}
        </div>
      );
    } else {
      const currentValue = (formData && formData[config.key] && formData[config.key][input.name]) || [];
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
                  {input?.isOptional && <h3>{`(${t(input?.isOptional)})`}</h3>}
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
              headerBarMain={<Heading label={t("UPLOAD_ID_PROOF_HEADER")} />}
              submitTextClassName={"verification-button-text-modal"}
            >
              <div>
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
                />
                {<h1>t(CS_COMMON_FILE_UPLOAD_BLURB)</h1>}
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
