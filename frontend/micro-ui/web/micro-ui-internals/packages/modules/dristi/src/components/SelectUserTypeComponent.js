import React, { useMemo } from "react";
import {
  LabelFieldPair,
  CardLabel,
  TextInput,
  CardLabelError,
  CustomDropdown,
  UploadFileComposer,
  MultiUploadWrapper,
  CitizenInfoLabel,
} from "@egovernments/digit-ui-react-components";
import { useLocation } from "react-router-dom";
import { Controller, useForm } from "react-hook-form";

const SelectUserTypeComponent = ({ t, config, onSelect, formData = {}, errors }) => {
  const newConfig = useMemo(() => {}, [formData]);
  const { pathname: url } = useLocation();
  const { register, control } = useForm();
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

  function setValue(value, input) {
    onSelect(config.key, { ...formData[config.key], [input]: value });
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
                    {input.isMandatory ? " * " : null}
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
                        console.log(e, "dropdown");
                        setValue(e, input.name);
                      }}
                      config={input}
                      errorStyle={errors?.[input.name]}
                    />
                  )}
                  {input?.type === "documentUpload" && (
                    <Controller
                      name={`${input.name}`}
                      control={control}
                      render={({ onChange, ref, value = [] }) => {
                        function getFileStoreData(filesData) {
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
                          onChange(numberOfFiles > 0 ? filesData : []);
                          setValue(numberOfFiles > 0 ? filesData : [], input.name);
                        }
                        return (
                          <MultiUploadWrapper
                            t={t}
                            module="works"
                            tenantId={Digit.ULBService.getCurrentTenantId()}
                            getFormState={getFileStoreData}
                            showHintBelow={input?.showHintBelow ? true : false}
                            setuploadedstate={value || []}
                            allowedFileTypesRegex={input.allowedFileTypes}
                            allowedMaxSizeInMB={input.allowedMaxSizeInMB || "5"}
                            hintText={input.hintText}
                            maxFilesAllowed={input.maxFilesAllowed || "1"}
                            extraStyleName={{ padding: "0.5rem" }}
                            customClass={input?.customClass}
                            customErrorMsg={input?.errorMessage}
                            containerStyles={{ ...input?.containerStyles }}
                          />
                        );
                      }}
                    />
                  )}
                  {input?.type === "text" && (
                    <TextInput
                      className="field desktop-w-full"
                      key={input.name}
                      value={formData && formData[config.key] ? formData[config.key][input.name] : undefined}
                      onChange={(e) => {
                        setValue(e.target.value, input.name);
                      }}
                      disable={false}
                      defaultValue={undefined}
                      {...input.validation}
                    />
                  )}

                  {input.hasBreakPoint && <div style={{ margin: 20, textAlign: "center", width: "100%", maxWidth: 540 }}>{"( Or )"}</div>}
                  {currentValue &&
                    currentValue.length > 0 &&
                    !["documentUpload", "radioButton"].includes(input.type) &&
                    !currentValue.match(Digit.Utils.getPattern(input.validation.patternType)) && (
                      <CardLabelError style={{ width: "100%", marginTop: "-15px", fontSize: "16px", marginBottom: "12px" }}>
                        {t("CORE_COMMON_APPLICANT_ADDRESS_INVALID")}
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
