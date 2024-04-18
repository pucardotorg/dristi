import React, { useEffect, useMemo, useState } from "react";
import {
  LabelFieldPair,
  CardLabel,
  TextInput,
  CardLabelError,
  CustomDropdown,
  MultiUploadWrapper,
  CitizenInfoLabel,
  Toast,
} from "@egovernments/digit-ui-react-components";
import { useLocation } from "react-router-dom";
import { Controller } from "react-hook-form";

const SelectUserTypeComponent = ({ t, config, onSelect, formData = {}, errors, formState, control }) => {
  const { pathname: url } = useLocation();
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
    []
  );

  function setValue(value, name, input) {
    if (input && input?.clearFields && value) {
      if (input?.clearFieldsType && formData[config.key]) {
        Object.keys(input?.clearFields).forEach((ele) => {
          if (ele in input?.clearFieldsType && input?.clearFieldsType[ele] === "documentUpload" && formData[config.key][ele].length > 0) {
            const [fileData] = formData[config.key][ele];
            setRemoveFile(fileData[1]);
          }
        });
      }
      onSelect(config.key, { ...formData[config.key], [name]: value, ...input.clearFields });
    } else onSelect(config.key, { ...formData[config.key], [name]: value });
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
    setValue(numberOfFiles > 0 ? filesData : [], input.name);
  }
  return (
    <div>
      {inputs?.map((input, index) => {
        let currentValue = (formData && formData[config.key] && formData[config.key][input.name]) || "";
        const showDependentFields =
          Boolean(input.isDependentOn) && !Boolean(formData && formData[config.key])
            ? false
            : Boolean(formData && formData[config.key] && formData[config.key][input.isDependentOn])
            ? formData && formData[config.key] && formData[config.key][input.isDependentOn][input.dependentKey]
            : true;
        return (
          <React.Fragment key={index}>
            {errors[input.name] && <CardLabelError>{t(input.error)}</CardLabelError>}

            {showDependentFields && (
              <LabelFieldPair style={{ width: "100%", display: "flex" }}>
                {input?.type !== "infoBox" && (
                  <CardLabel className="card-label-smaller">
                    {t(input.label)}
                    {input.isMandatory ? <span style={{ color: "#FF0000" }}>{" * "}</span> : null}
                  </CardLabel>
                )}
                <div className="field" style={{ width: "50%" }}>
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
                    <MultiUploadWrapper
                      t={t}
                      module="works"
                      tenantId={Digit.ULBService.getCurrentTenantId()}
                      getFormState={(fileData) => getFileStoreData(fileData, input)}
                      showHintBelow={input?.showHintBelow ? true : false}
                      setuploadedstate={[]}
                      allowedFileTypesRegex={input.allowedFileTypes}
                      allowedMaxSizeInMB={input.allowedMaxSizeInMB || "5"}
                      hintText={input.hintText}
                      maxFilesAllowed={input.maxFilesAllowed || "1"}
                      extraStyleName={{ padding: "0.5rem" }}
                      customClass={input?.customClass}
                      customErrorMsg={input?.errorMessage}
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
                      {...input.validation}
                    />
                  )}

                  {input.hasBreakPoint && <div style={{ margin: 20, textAlign: "center", width: "100%", maxWidth: 540 }}>{"( Or )"}</div>}
                  {currentValue &&
                    currentValue.length > 0 &&
                    !["documentUpload", "radioButton"].includes(input.type) &&
                    input.validation &&
                    !currentValue.match(Digit.Utils.getPattern(input.validation.patternType)) && (
                      <CardLabelError style={{ width: "100%", marginTop: "-15px", fontSize: "16px", marginBottom: "12px" }}>
                        <span style={{ color: "#FF0000" }}> {t("CORE_COMMON_INVALID")}</span>
                      </CardLabelError>
                    )}
                </div>
              </LabelFieldPair>
            )}
            {input?.type === "infoBox" && (
              <CitizenInfoLabel
                style={{ maxWidth: "100%", height: "90px", padding: "0 8px" }}
                textStyle={{ margin: 8 }}
                iconStyle={{ margin: 0 }}
                info={t("ES_COMMON_INFO")}
                text={t(input?.bannerLabel)}
                className="doc-banner"
              ></CitizenInfoLabel>
            )}
          </React.Fragment>
        );
      })}
    </div>
  );
};

export default SelectUserTypeComponent;
