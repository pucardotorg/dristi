import React, { useMemo, useState } from "react";
import { LabelFieldPair, CardLabel, TextInput, CardLabelError, CustomDropdown } from "@egovernments/digit-ui-react-components";
import MultiUploadWrapper from "./MultiUploadWrapper";
import DocViewerWrapper from "../pages/employee/docViewerWrapper";

const IdProofUploadComponent = ({ t, config, onSelect, formData = {}, errors, formState, control, setError }) => {
  const [removeFile, setRemoveFile] = useState();
  const inputs = useMemo(
    () =>
      config?.populators?.inputs || [
        {
          label: "CS_PIN_LOCATION",
          type: "LocationSearch",
          name: "correspondenceCity",
        },
      ],
    [config?.populators?.inputs]
  );

  function setValue(value, name, input) {
    if (input && input?.clearFields && value) {
      if (input?.clearFieldsType && formData[config.key]) {
        Object.keys(input?.clearFields).forEach((ele) => {
          if (ele in input?.clearFieldsType && input?.clearFieldsType[ele] === "documentUpload" && formData[config.key][ele]?.length > 0) {
            const [fileData] = formData[config.key][ele];
            setRemoveFile(fileData[1]);
          }
        });
      }
      if (input?.type && input.type === "documentUpload" && value?.length === 0) {
        onSelect(config.key, { ...formData[config.key], [name]: [] });
        return;
      }
      onSelect(config.key, { ...formData[config.key], [name]: value, ...input.clearFields });
    } else onSelect(config.key, { ...formData[config.key], [name]: value });

    // if (
    //   value &&
    //   typeof value === "string" &&
    //   !value?.match(window?.Digit.Utils.getPattern(input.validation.patternType) || input.validation.pattern)
    // ) {
    //   setError(config.key, { ...formData[config.key], [name]: value });
    // }
  }
  function getFileStoreData(filesData, input) {
    const numberOfFiles = filesData.length;
    let finalDocumentData = [];
    if (numberOfFiles > 0) {
      filesData.forEach((value) => {
        finalDocumentData.push({
          fileName: value?.[0],
          fileStoreId: value?.[1]?.fileStoreId?.fileStoreId,
          documentType: value?.[1]?.file?.type,
        });
      });
    }
    setValue(numberOfFiles > 0 ? filesData : [], input.name, input);
  }
  return (
    <div>
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
            {errors[input.name] && <CardLabelError>{t(input.error)}</CardLabelError>}

            {showDependentFields && (
              <LabelFieldPair>
                <CardLabel className="card-label-smaller">{t(input.label)}</CardLabel>
                <div className="field">
                  {["radioButton", "dropdown"].includes(input?.type) && (
                    <CustomDropdown
                      t={t}
                      label={input?.label}
                      type={input?.type === "radioButton" && "radio"}
                      value={formData && formData[config.key] ? formData[config.key][input.name] : undefined}
                      onChange={(e) => {
                        setValue(e, input.name, input);
                      }}
                      config={input}
                      errorStyle={errors?.[input.name]}
                    />
                  )}
                  {input?.type === "documentUpload" && (
                    <React.Fragment>
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
                      {Array.isArray(formData?.[config.key]?.[input.name]) && formData?.[config.key]?.[input.name].length > 0 && (
                        <DocViewerWrapper
                          selectedDocs={[formData?.[config.key]?.[input.name][0][1]?.file]}
                          showDownloadOption={false}
                          docViewerCardClassName={"doc-viewer-card-style"}
                        ></DocViewerWrapper>
                      )}
                    </React.Fragment>
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
                      {...input.validation}
                    />
                  )}

                  {input.hasBreakPoint && <div style={{ margin: 20, textAlign: "center", width: "100%", maxWidth: 540 }}>{"( Or )"}</div>}
                  {currentValue &&
                    currentValue.length > 0 &&
                    !["documentUpload", "radioButton"].includes(input.type) &&
                    input.validation &&
                    !currentValue.match(window?.Digit.Utils.getPattern(input.validation.patternType) || input.validation.pattern) && (
                      <CardLabelError style={{ width: "100%", marginTop: "-15px", fontSize: "16px", marginBottom: "12px" }}>
                        <span style={{ color: "#FF0000" }}> {t(input.validation?.errMsg || "CORE_COMMON_INVALID")}</span>
                      </CardLabelError>
                    )}
                </div>
              </LabelFieldPair>
            )}
          </React.Fragment>
        );
      })}
    </div>
  );
};

export default IdProofUploadComponent;
