import React, { useMemo, useState } from "react";
import { LabelFieldPair, CardLabel, TextInput, CardLabelError, CustomDropdown } from "@egovernments/digit-ui-react-components";
import MultiUploadWrapper from "./MultiUploadWrapper";
import CitizenInfoLabel from "./CitizenInfoLabel";
import { CardText } from "@egovernments/digit-ui-components";
import useInterval from "../hooks/useInterval";
import DocViewerWrapper from "../pages/employee/docViewerWrapper";
const TYPE_REGISTER = { type: "register" };
const TYPE_LOGIN = { type: "login" };
const DEFAULT_USER = "digit-user";

const SelectUserTypeComponent = ({ t, config, onSelect, formData = {}, errors, formState, control, setError }) => {
  const [removeFile, setRemoveFile] = useState();
  const [timeLeft, setTimeLeft] = useState(10);
  const [showDoc, setShowDoc] = useState(false);
  const tenantId = window?.Digit.ULBService.getCurrentTenantId();
  const [fileStoreId, setFileStoreID] = useState();
  const [fileName, setFileName] = useState();
  // const [isUserRegistered, setIsUserRegistered] = useState(true);
  const getUserType = () => window?.Digit.UserService.getType();
  const stateCode = window?.Digit.ULBService.getStateId();
  const Digit = window.Digit || {};
  const onDocumentUpload = async (fileData, filename) => {
    const fileUploadRes = await Digit.UploadServices.Filestorage("DRISTI", fileData, tenantId);
    return { file: fileUploadRes?.data, fileType: fileData.type, filename };
  };
  useInterval(
    () => {
      setTimeLeft(timeLeft - 1);
    },
    timeLeft > 0 ? 1000 : null
  );
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
        onSelect(config.key, { ...formData[config.key], [name]: value });
        return;
      }
      onSelect(config.key, { ...formData[config.key], [name]: value, ...input.clearFields }, { shouldValidate: true });
    } else onSelect(config.key, { ...formData[config.key], [name]: value }, { shouldValidate: true });

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
    numberOfFiles > 0
      ? onDocumentUpload(filesData[0][1]?.file, filesData[0][0]).then((document) => {
          const newFileStoreId = document.file?.files?.[0]?.fileStoreId;

          // Update filesData with the new fileStoreId
          filesData[0][1].fileStoreId = {
            fileStoreId: newFileStoreId,
          };
          setFileName(filesData[0][0]);

          setFileStoreID(document.file?.files?.[0]?.fileStoreId);
          setShowDoc(true);
          // Set the updated filesData after setting the fileStoreId
          setValue(filesData, input.name, input);
        })
      : setShowDoc(false);
    setValue(numberOfFiles > 0 ? filesData : [], input.name, input);
  }

  const checkIfAadharValidationNotSuccessful = (currentValue, input) => {
    if (!input?.checkAadharVerification) {
      return !currentValue.match(window?.Digit.Utils.getPattern(input.validation.patternType) || input.validation.pattern);
    }
    let isValidated = true;
    const ifOnlyNumeric = /^\d*$/.test(currentValue);
    if (!ifOnlyNumeric) {
      isValidated = false;
    }
    return !isValidated;
  };

  const sendOtp = async (data) => {
    try {
      const res = await window?.Digit.UserService.sendOtp(data, stateCode);
      return [res, null];
    } catch (err) {
      return [null, err];
    }
  };

  const resendOtp = async (input) => {
    const data = {
      mobileNumber: formData[config.key]?.[input?.mobileNoKey],
      tenantId: stateCode,
      userType: getUserType(),
    };
    const [res, err] = await sendOtp({ otp: { ...data, ...TYPE_LOGIN } });
    if (!err) {
      return;
    } else {
      const [res, err] = await sendOtp({ otp: { ...data, name: DEFAULT_USER, ...TYPE_REGISTER } });
      return;
    }
  };

  const showUploadedDocument = useMemo(() => {
    return (
      <div>
        <div className="documentDetails_row_items" style={{ display: "flex", flexDirection: "column", alignItems: "center" }}>
          <DocViewerWrapper fileStoreId={fileStoreId} tenantId={tenantId} displayFilename={fileName} />
        </div>
      </div>
    );
  }, [fileStoreId, tenantId, fileName]);

  return (
    <div className="select-user-type-component">
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
            {showDependentFields && (
              <LabelFieldPair>
                {!config?.disableScrutinyHeader && (
                  <CardLabel className="card-label-smaller" style={{ display: "flex" }}>
                    {t(input.label) +
                      `${
                        input?.hasMobileNo
                          ? formData[config.key]?.[input?.mobileNoKey]
                            ? input?.isMobileSecret
                              ? input?.mobileCode
                                ? ` ${input?.mobileCode}-******${formData[config.key]?.[input?.mobileNoKey]?.substring(6)}`
                                : ` ${formData[config.key]?.[input?.mobileNoKey]?.substring(6)}`
                              : ` ${formData[config.key]?.[input?.mobileNoKey]}`
                            : ""
                          : ""
                      }`}
                  </CardLabel>
                )}
                <div className="field">
                  {["radioButton", "dropdown"].includes(input?.type) && (
                    <CustomDropdown
                      t={t}
                      label={input?.label}
                      type={input?.type === "radioButton" ? "radio" : "dropdown"}
                      value={formData && formData[config.key] ? formData[config.key][input.name] : input?.allowMultiSelect ? [] : undefined}
                      onChange={(e) => {
                        setValue(e, input.name, input);
                      }}
                      config={input}
                      errorStyle={errors?.[input.name]}
                    />
                  )}
                  {["date"].includes(input?.type) && (
                    <TextInput
                      className="field desktop-w-full"
                      key={input.name}
                      type={"date"}
                      value={formData && formData[config.key] ? formData[config.key][input.name] : undefined}
                      onChange={(e) => {
                        setValue(e.target.value, input.name, input);
                      }}
                      min={input?.validation?.min}
                      disable={input.isDisabled}
                      style={{ paddingRight: "3px" }}
                      defaultValue={undefined}
                      errorStyle={errors?.[input.name]}
                      customIcon={input?.customIcon}
                      {...input.validation}
                    />
                  )}
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
                  {showDoc && input?.type === "documentUpload" && showUploadedDocument}
                  {input?.type === "text" && (
                    <TextInput
                      className="field desktop-w-full"
                      key={input.name}
                      value={formData && formData[config.key] ? formData[config.key][input.name] : undefined}
                      onChange={(e) => {
                        let updatedValue = e.target.value;
                        if (input.validation && input.validation?.isNumber) {
                          updatedValue = /^\d*$/.test(updatedValue?.[updatedValue?.length - 1])
                            ? updatedValue
                            : updatedValue?.slice(0, updatedValue?.length - 1);
                        }
                        if (input.validation && input.validation?.isDecimal && input.validation?.regex) {
                          updatedValue = input.validation?.regex.test(updatedValue) ? updatedValue : updatedValue?.slice(0, updatedValue?.length - 1);
                        }
                        setValue(updatedValue, input.name, input);
                      }}
                      errorStyle={errors?.[input.name]}
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
                    checkIfAadharValidationNotSuccessful(currentValue, input) && (
                      <CardLabelError style={{ width: "100%", fontSize: "12px", marginBottom: "12px" }}>
                        <span style={{ color: "#FF0000" }}> {t(input.validation?.errMsg || "CORE_COMMON_INVALID")}</span>
                      </CardLabelError>
                    )}

                  {errors[input.name] && (
                    <CardLabelError style={{ width: "70%", marginLeft: "30%", fontSize: "12px" }}>
                      {errors[input.name]?.message ? errors[input.name]?.message : t(errors[input.name]) || t(input.error)}
                    </CardLabelError>
                  )}
                </div>
              </LabelFieldPair>
            )}

            {input?.type === "infoBox" && (
              <CitizenInfoLabel
                style={{ maxWidth: "100%", padding: "0 8px 10px 8px" }}
                textStyle={{ margin: 8 }}
                iconStyle={{ margin: 0 }}
                info={t("ES_COMMON_INFO")}
                text={t(input?.bannerLabel)}
                className="doc-banner"
                children={t("CS_AADHAR_NUMBER_INPUT_INFO")}
              ></CitizenInfoLabel>
            )}
            {input?.hasResendOTP && (
              <React.Fragment>
                {timeLeft > 0 ? (
                  <CardText>{`${t("CS_RESEND_ANOTHER_OTP")} ${timeLeft} ${t("CS_RESEND_SECONDS")}`}</CardText>
                ) : (
                  <p className="card-text" onClick={() => resendOtp(input)} style={{ backgroundColor: "#fff", color: "#007E7E", cursor: "pointer" }}>
                    {t("CS_RESEND_OTP")}
                  </p>
                )}
              </React.Fragment>
            )}
          </React.Fragment>
        );
      })}
    </div>
  );
};

export default SelectUserTypeComponent;
