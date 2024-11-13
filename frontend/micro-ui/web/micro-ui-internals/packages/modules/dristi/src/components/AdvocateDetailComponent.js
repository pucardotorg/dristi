import React, { useMemo, useState } from "react";
import { CardLabel, TextInput, CardLabelError } from "@egovernments/digit-ui-react-components";
import MultiUploadWrapper from "./MultiUploadWrapper";
import DocViewerWrapper from "../pages/employee/docViewerWrapper";

const AdvocateDetailComponent = ({ t, config, onSelect, formData = {}, errors, clearErrors }) => {
  const [removeFile, setRemoveFile] = useState();
  const [showDoc, setShowDoc] = useState(false);
  const tenantId = window?.Digit.ULBService.getCurrentTenantId();
  const [fileStoreId, setFileStoreID] = useState();
  const [fileName, setFileName] = useState();
  const Digit = window.Digit || {};
  const inputs = useMemo(
    () =>
      config?.populators?.inputs || [
        {
          label: "CS_LOCATION",
          type: "LocationSearch",
          name: "correspondenceCity",
        },
      ],
    [config?.populators?.inputs]
  );
  const onDocumentUpload = async (fileData, filename) => {
    const fileUploadRes = await Digit.UploadServices.Filestorage("DRISTI", fileData, tenantId);
    return { file: fileUploadRes?.data, fileType: fileData.type, filename };
  };
  function setValue(value, name, input) {
    if (errors?.[name]) {
      clearErrors();
    }
    if (input && input?.clearFields && value) {
      if (input?.clearFieldsType && formData[config.key]) {
        Object.keys(input?.clearFields).forEach((ele) => {
          if (ele in input?.clearFieldsType && input?.clearFieldsType[ele] === "documentUpload" && formData[config.key][ele]?.length > 0) {
            const [fileData] = formData[config.key][ele];
            setRemoveFile(fileData[1]);
          }
        });
      }
      onSelect(config.key, { ...formData[config.key], [name]: value, ...input.clearFields }, { shouldValidate: true });
    } else onSelect(config.key, { ...formData[config.key], [name]: value }, { shouldValidate: true });
  }
  function getFileStoreData(filesData, input) {
    const numberOfFiles = filesData.length;
    let finalDocumentData = [];
    if (numberOfFiles > 0) {
      filesData.forEach((value) => {
        finalDocumentData.push({
          fileName: value?.[0],
          fileStoreId: value?.[1]?.fileStoreId,
          documentType: value?.[1]?.file?.type,
        });
      });
    }

    numberOfFiles > 0
      ? onDocumentUpload(filesData[0][1]?.file, filesData[0][0]).then((document) => {
          setFileName(filesData[0][0]);

          setFileStoreID(document.file?.files?.[0]?.fileStoreId);
          setShowDoc(true);
        })
      : setShowDoc(false);
    setValue(numberOfFiles > 0 ? filesData : [], input.name, input);
  }

  const showDocument = useMemo(() => {
    return (
      <div>
        <div className="documentDetails_row_items" style={{ display: "flex", flexDirection: "column", alignItems: "center" }}>
          <DocViewerWrapper fileStoreId={fileStoreId} tenantId={tenantId} displayFilename={fileName} />
        </div>
      </div>
    );
  }, [fileStoreId, tenantId, fileName]);

  return (
    <React.Fragment>
      {inputs?.map((input, index) => {
        let currentValue = (formData && formData[config.key] && formData[config.key][input.name]) || "";
        const showDependentFields =
          Boolean(input.isDependentOn) && !Boolean(formData && formData[config.key])
            ? false
            : Boolean(formData && formData[config.key] && formData[config.key][input.isDependentOn])
            ? formData &&
              formData[config.key] &&
              Array.isArray(input.dependentKey[input.isDependentOn]) &&
              input.dependentKey[input.isDependentOn].reduce((res, curr) => {
                if (!res) return res;
                res = formData[config.key][input.isDependentOn][curr];
                return res;
              }, true)
            : true;
        return (
          <React.Fragment key={index}>
            <div className={`${input?.type}`} style={{ width: "100%" }}>
              {input?.type !== "infoBox" && (
                <CardLabel className="card-label-smaller" style={{ width: "100%", fontSize: "16px" }}>
                  {t(input.label)}
                </CardLabel>
              )}
              <div className="field" style={{ width: "100%" }}>
                {input?.type === "documentUpload" && (
                  <MultiUploadWrapper
                    t={t}
                    module="works"
                    tenantId={window?.Digit.ULBService.getCurrentTenantId()}
                    getFormState={(fileData) => getFileStoreData(fileData, input)}
                    showHintBelow={input?.showHintBelow ? true : false}
                    setuploadedstate={formData?.[config.key]?.[input.name] || []}
                    allowedFileTypesRegex={input.allowedFileTypes}
                    allowedMaxSizeInMB={input.allowedMaxSizeInMB || "5"}
                    hintText={input.hintText}
                    maxFilesAllowed={input.maxFilesAllowed || "1"}
                    extraStyleName={{ padding: "0.5rem" }}
                    customClass={input?.customClass}
                    containerStyles={{ ...input?.containerStyles }}
                    requestSpecifcFileRemoval={removeFile}
                  />
                )}
                {input?.type === "text" && (
                  <TextInput
                    className="field desktop-w-full"
                    key={input.name}
                    value={formData && formData[config.key] ? formData[config.key][input.name] : undefined}
                    onChange={(e) => {
                      setValue(e.target.value, input.name, input);
                    }}
                    disable={input.isDisabled}
                    defaultValue={undefined}
                    isRequired={input.validation.isRequired}
                    pattern={input.validation.pattern}
                    errMsg={input.validation.errMsg}
                    maxlength={input.validation.maxlength}
                    minlength={input.validation.minlength}
                    style={{ minWidth: "500px" }}
                  />
                )}

                {currentValue &&
                  currentValue.length > 0 &&
                  !["documentUpload", "radioButton"].includes(input.type) &&
                  input.validation &&
                  !currentValue.match(window?.Digit.Utils.getPattern(input.validation.patternType) || input.validation.pattern) && (
                    <CardLabelError style={{ width: "100%", marginTop: "5px", fontSize: "16px", marginBottom: "12px" }}>
                      <span style={{ color: "#FF0000" }}> {t(input.validation?.errMsg || "INVALID_BAR_REG_NUMBER")}</span>
                    </CardLabelError>
                  )}
              </div>
            </div>
            {errors[input.name] && (
              <CardLabelError style={{ color: "#FF0000", marginTop: "5px", fontSize: "14px" }}>
                {errors[input.name]?.message ? errors[input.name]?.message : t(errors[input.name]) || t(input.error)}
                {t(input.error)}
              </CardLabelError>
            )}
            {/* )} */}
          </React.Fragment>
        );
      })}
      {showDoc && showDocument}
    </React.Fragment>
  );
};

export default AdvocateDetailComponent;
