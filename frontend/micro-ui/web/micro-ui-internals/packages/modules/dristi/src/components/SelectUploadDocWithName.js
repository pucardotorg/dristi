import React, { useMemo, useState } from "react";
import { TextInput } from "@egovernments/digit-ui-react-components";
import { FileUploader } from "react-drag-drop-files";
import RenderFileCard from "./RenderFileCard";
import { ReactComponent as DeleteFileIcon } from "../images/delete.svg";

import { UploadIcon } from "@egovernments/digit-ui-react-components";
import { CustomAddIcon } from "../icons/svgIndex";
import Button from "./Button";
import { CaseWorkflowState } from "../Utils/caseWorkflow";
import { DRISTIService } from "../services";

function SelectUploadDocWithName({ t, config, formData = {}, onSelect }) {
  const [documentData, setDocumentData] = useState(formData?.[config.key] ? formData?.[config.key] : []);
  const tenantId = Digit.ULBService.getCurrentTenantId();
  const { caseId } = window?.Digit.Hooks.useQueryParams();

  const inputs = useMemo(
    () =>
      config?.populators?.inputs || [
        {
          label: "label",
          type: "text",
          name: "docName",
          validation: {
            pattern: /^[^{0-9}^\$\"<>?\\\\~!@#$%^()+={}\[\]*,/_:;“”‘’]{1,50}$/i,
            errMsg: "CORE_COMMON_DOCUMENT_NAME_INVALID",
            title: "",
            patternType: "Name",
            isRequired: true,
          },
          isMandatory: true,
        },
        {
          isMandatory: true,
          name: "document",
          documentHeader: "header",
          type: "DragDropComponent",
          maxFileSize: 50,
          maxFileErrorMessage: "CS_FILE_LIMIT_50_MB",
          fileTypes: ["JPG", "PDF"],
          isMultipleUpload: false,
        },
      ],
    [config?.populators?.inputs]
  );

  const dragDropJSX = (
    <div className="drag-drop-container-desktop">
      <UploadIcon />
      <p className="drag-drop-text">
        {t("WBH_DRAG_DROP")} <text className="browse-text">{t("WBH_BULK_BROWSE_FILES")}</text>
      </p>
    </div>
  );

  const handleFileChange = (file, input, index) => {
    let currentDocumentDataCopy = structuredClone(documentData);
    let currentDataObj = currentDocumentDataCopy[index];
    currentDataObj.document = [file];
    currentDocumentDataCopy.splice(index, 1, currentDataObj);
    setDocumentData(currentDocumentDataCopy);
    onSelect(config.key, currentDocumentDataCopy);
  };

  const handleDeleteFile = (index) => {
    let currentDocumentDataCopy = structuredClone(documentData);
    let currentDataObj = currentDocumentDataCopy[index];
    currentDataObj.document = [];
    currentDocumentDataCopy.splice(index, 1, currentDataObj);
    setDocumentData(currentDocumentDataCopy);
    onSelect(config.key, currentDocumentDataCopy);
  };

  const fileValidator = (file, input) => {
    const maxFileSize = input?.maxFileSize * 1024 * 1024;
    if (file.length > 0) {
      return file[0].size > maxFileSize ? t(input?.maxFileErrorMessage) : null;
    } else return null;
  };

  const handleAddDocument = () => {
    const documentDataCopy = structuredClone(documentData);
    const dataObject = {
      docName: "",
      document: [],
    };
    documentDataCopy.push(dataObject);
    setDocumentData(documentDataCopy);
    onSelect(config.key, documentDataCopy);
  };

  const handleDeleteDocument = async (index) => {
    let currentDocumentDataCopy = structuredClone(documentData);
    if (currentDocumentDataCopy?.[index].document?.[0]?.artifactId)
      await DRISTIService.createEvidence({
        artifact: {
          artifactType: "DOCUMENTARY",
          sourceType: "COMPLAINANT",
          caseId: caseId,
          tenantId,
          artifactId: currentDocumentDataCopy?.[index].document?.[0]?.artifactId,
          comments: [],
          workflow: {
            action: "ABANDON",
          },
        },
      });
    currentDocumentDataCopy.splice(index, 1);
    setDocumentData(currentDocumentDataCopy);
    onSelect(config.key, currentDocumentDataCopy);
  };

  const handleOnTextChange = (value, input, index) => {
    let currentDocumentDataCopy = structuredClone(documentData);
    let currentDataObj = currentDocumentDataCopy[index];
    currentDataObj[input.name] = value;
    currentDocumentDataCopy.splice(index, 1, currentDataObj);
    setDocumentData(currentDocumentDataCopy);
    onSelect(config.key, currentDocumentDataCopy);
  };

  return (
    <div className="file-uploader-with-name">
      {documentData.length > 0 &&
        documentData.map((data, index) => {
          return (
            <div key={index} className="file-uploader-with-name-sub">
              <div className="file-uploader-with-name-header">
                <h1>{`${t("DOCUMENT_NUMBER_HEADING")} ${index + 1}`}</h1>
                <span
                  onClick={() => {
                    if (!config?.disable) {
                      handleDeleteDocument(index);
                    }
                  }}
                  style={{ cursor: "pointer" }}
                >
                  <DeleteFileIcon />
                </span>
              </div>
              <div className="drag-drop-visible-main-with-custom-name">
                {inputs.map((input) => {
                  let currentValue = data && data[input.name];
                  if (input.type === "text") {
                    return (
                      <div className="file-name-field">
                        <h1>{t("DOCUMENT_NAME_TITLE")}</h1>
                        <TextInput
                          className="field desktop-w-full"
                          key={input?.name}
                          value={currentValue}
                          onChange={(e) => {
                            handleOnTextChange(e.target.value, input, index);
                          }}
                          disable={input?.isDisabled || config?.disable}
                          defaultValue={undefined}
                          {...input?.validation}
                        />
                      </div>
                    );
                  } else {
                    let fileErrors = fileValidator(currentValue, input);
                    const showFileUploader = currentValue.length ? input?.isMultipleUpload : true;
                    return (
                      <div className="drag-drop-visible-main">
                        <div className="drag-drop-heading-main">
                          <div className="drag-drop-heading">
                            <span>
                              <h2 className="card-label document-header">{t(input?.documentHeader)}</h2>
                            </span>
                          </div>
                        </div>
                        {currentValue.length > 0 && (
                          <RenderFileCard
                            fileData={currentValue[0]}
                            handleChange={(data) => {
                              handleFileChange(data, input, index);
                            }}
                            handleDeleteFile={() => handleDeleteFile(index)}
                            t={t}
                            uploadErrorInfo={fileErrors}
                            input={input}
                            disableUploadDelete={config?.disable}
                          />
                        )}
                        {showFileUploader && (
                          <div className={`file-uploader-div-main ${showFileUploader ? "show-file-uploader" : ""}`}>
                            <FileUploader
                              handleChange={(data) => {
                                handleFileChange(data, input, index);
                              }}
                              name="file"
                              types={input?.fileTypes}
                              children={dragDropJSX}
                              key={input?.name}
                              // disabled={config?.disable}
                            />
                            <div className="upload-guidelines-div">{input.uploadGuidelines && <p>{t(input.uploadGuidelines)}</p>}</div>
                          </div>
                        )}
                      </div>
                    );
                  }
                })}
              </div>
            </div>
          );
        })}
      <Button
        isDisabled={config?.disable || (config?.state && config?.state !== CaseWorkflowState.DRAFT_IN_PROGRESS)}
        variation="secondary"
        onButtonClick={handleAddDocument}
        className="add-new-document"
        icon={<CustomAddIcon />}
        label={t("ADD_DOCUMENT")}
        labelClassName="add-new-document-label"
      />
      {/* {<span onClick={handleAddDocument}> + {t("ADD_DOCUMENT")}</span>} */}
    </div>
  );
}

export default SelectUploadDocWithName;
