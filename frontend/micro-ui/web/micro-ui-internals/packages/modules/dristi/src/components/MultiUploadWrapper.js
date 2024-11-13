import React, { useEffect, useReducer, useState } from "react";
import UploadFile from "./UploadFile";

const displayError = ({ t, error, name }, customErrorMsg) => (
  <span style={{ display: "flex", flexDirection: "column" }}>
    <div className="validation-error">{customErrorMsg ? t(customErrorMsg) : error}</div>
    <div className="validation-error" style={{ marginTop: 0 }}></div>
  </span>
);

const fileValidationStatus = (file, regex, maxSize, t, notSupportedError, maxFileErrorMessage) => {
  const updatedRegex = typeof regex === "string" ? new RegExp(regex.replace("/i", "").replace("/(.*?)", "(.*?)")) : regex;
  const fileNameParts = file?.name.split(".");
  const extension = fileNameParts.pop().toLowerCase();
  const fileNameWithoutExtension = fileNameParts.join(".");
  const newFileName = `${fileNameWithoutExtension}.${extension}`;
  file = new File([file], newFileName, {
    type: file?.type,
    lastModified: file?.lastModified,
  });
  const status = { valid: true, name: file?.name?.substring(0, 15), error: "" };
  if (!file) return;

  if (!updatedRegex.test(file.name) && file.size / 1024 / 1024 > maxSize) {
    status.valid = false;
    status.error = t(`NOT_SUPPORTED_FILE_TYPE_AND_FILE_SIZE_EXCEEDED_5MB`);
  }

  if (!updatedRegex.test(file.name)) {
    status.valid = false;
    status.error = t(notSupportedError ? notSupportedError : `NOT_SUPPORTED_FILE_TYPE`);
  }

  if (file.size / 1024 / 1024 > maxSize) {
    status.valid = false;
    status.error = t(maxFileErrorMessage ? maxFileErrorMessage : `FILE_SIZE_EXCEEDED_5MB`);
  }

  return status;
};
const checkIfAllValidFiles = (files, regex, maxSize, t, maxFilesAllowed, state, notSupportedError, maxFileErrorMessage) => {
  if (!files.length || !regex || !maxSize) return [{}, false];

  const uploadedFiles = state.length + 1;
  if (maxFilesAllowed && files.length > maxFilesAllowed)
    return [[{ valid: false, name: files[0]?.name?.substring(0, 15), error: t(`FILE_LIMIT_EXCEEDED`) }], true];

  const messages = [];
  let isInValidGroup = false;
  for (let file of files) {
    const fileStatus = fileValidationStatus(file, regex, maxSize, t, notSupportedError, maxFileErrorMessage);
    if (!fileStatus.valid) {
      isInValidGroup = true;
    }
    messages.push(fileStatus);
  }

  return [messages, isInValidGroup];
};

// can use react hook form to set validations @neeraj-egov
const MultiUploadWrapper = ({
  t,
  module = "PGR",
  tenantId = window?.Digit.ULBService.getStateId(),
  getFormState,
  requestSpecifcFileRemoval,
  extraStyleName = "",
  setuploadedstate = [],
  showHintBelow,
  hintText,
  allowedFileTypesRegex = /(.*?)(jpg|jpeg|webp|aif|png|image|pdf|msword|openxmlformats-officedocument|xls|xlsx|openxmlformats-officedocument|wordprocessingml|document|spreadsheetml|sheet|ms-excel)$/i,
  allowedMaxSizeInMB = 5,
  acceptFiles = "image/*, .jpg, .jpeg, .webp, .aif, .png, .image, .pdf, .msword, .openxmlformats-officedocument, .dxf, .xlsx, .xls, application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
  maxFilesAllowed,
  customClass = "",
  customErrorMsg,
  containerStyles,
  noteMsg,
  notSupportedError,
  maxFileErrorMessage,
  displayName,
  disable,
}) => {
  const FILES_UPLOADED = "FILES_UPLOADED";
  const TARGET_FILE_REMOVAL = "TARGET_FILE_REMOVAL";

  const [fileErrors, setFileErrors] = useState([]);
  const [enableButton, setEnableButton] = useState(true);

  const uploadMultipleFiles = (state, payload) => {
    const { files, fileStoreIds } = payload;
    const filesData = Array.from(files);
    const newUploads = filesData?.map((file, index) => {
      if (file?.name) {
        const fileNameParts = file?.name.split(".");
        const extension = fileNameParts.pop().toLowerCase();
        const fileNameWithoutExtension = fileNameParts.join(".");
        const newFileName = `${fileNameWithoutExtension}.${extension}`;
        file = new File([file], newFileName, {
          type: file?.type,
          lastModified: file?.lastModified,
        });
        return [newFileName, { file, fileStoreId: fileStoreIds[index] }];
      } else {
        return filesData?.map((file, index) => [file.name, { file, fileStoreId: fileStoreIds[index] }]);
      }
    });
    return [...newUploads];
  };

  const removeFile = (state, payload) => {
    const __indexOfItemToDelete = state.findIndex((e) => e[1].fileStoreId.fileStoreId === payload.fileStoreId.fileStoreId);
    const mutatedState = state.filter((e, index) => index !== __indexOfItemToDelete);
    setFileErrors([]);
    return [...mutatedState];
  };

  const uploadReducer = (state, action) => {
    switch (action.type) {
      case FILES_UPLOADED:
        return uploadMultipleFiles(state, action.payload);
      case TARGET_FILE_REMOVAL:
        return removeFile(state, action.payload);
      default:
        break;
    }
  };

  const [state, dispatch] = useReducer(uploadReducer, [...setuploadedstate]);

  const onUploadMultipleFiles = async (e) => {
    setEnableButton(false);
    setFileErrors([]);
    const files = Array.from(e.target.files);

    if (!files.length) return;
    const [validationMsg, error] = checkIfAllValidFiles(
      files,
      allowedFileTypesRegex,
      allowedMaxSizeInMB,
      t,
      maxFilesAllowed,
      state,
      notSupportedError,
      maxFileErrorMessage
    );

    if (!error) {
      try {
        // API call commented for now
        // const { data: { files: fileStoreIds } = {} } = await Digit.UploadServices.MultipleFilesStorage(module, e.target.files, tenantId)
        setEnableButton(true);
        return dispatch({ type: FILES_UPLOADED, payload: { files: e.target.files, fileStoreIds: [1] } });
      } catch (err) {
        setEnableButton(true);
      }
    } else {
      setFileErrors(validationMsg);
      setEnableButton(true);
      return dispatch({ type: FILES_UPLOADED, payload: { files: [], fileStoreIds: [1] } });
    }
  };

  useEffect(() => getFormState(state), [state]);

  useEffect(() => {
    if (requestSpecifcFileRemoval) {
      dispatch({ type: TARGET_FILE_REMOVAL, payload: requestSpecifcFileRemoval });
    }
  }, [requestSpecifcFileRemoval]);

  return (
    <div style={containerStyles}>
      <UploadFile
        onUpload={(e) => onUploadMultipleFiles(e)}
        removeTargetedFile={(fileDetailsData) => dispatch({ type: TARGET_FILE_REMOVAL, payload: fileDetailsData })}
        uploadedFiles={state}
        multiple={true}
        showHintBelow={showHintBelow}
        hintText={hintText}
        extraStyleName={extraStyleName}
        onDelete={() => {
          setFileErrors([]);
        }}
        accept={acceptFiles}
        customClass={customClass}
        enableButton={enableButton || !disable}
        disabled={!enableButton || disable}
        displayName={displayName}
      />
      <span className="error-msg" style={{ display: "flex" }}>
        {fileErrors.length ? (
          fileErrors.map(({ valid, name, type, size, error }) => (valid ? null : displayError({ t, error, name }, customErrorMsg)))
        ) : (
          <h1 style={{ fontSize: "12px" }}>{t(noteMsg ? noteMsg : "CS_DOCUMENT_UPLOAD_BLURB")}</h1>
        )}
      </span>
    </div>
  );
};

export default MultiUploadWrapper;
