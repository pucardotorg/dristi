import React, { useEffect, useMemo, useState } from "react";
import { CardLabel, TextInput, CardLabelError, CustomDropdown } from "@egovernments/digit-ui-react-components";
import MultiUploadWrapper from "../../../dristi/src/components/MultiUploadWrapper";

const CloseBtn = () => {
  return (
    <svg width="16" height="18" viewBox="0 0 16 18" fill="none" xmlns="http://www.w3.org/2000/svg">
      <path
        d="M3 18C2.45 18 1.97917 17.8042 1.5875 17.4125C1.19583 17.0208 1 16.55 1 16V3H0V1H5V0H11V1H16V3H15V16C15 16.55 14.8042 17.0208 14.4125 17.4125C14.0208 17.8042 13.55 18 13 18H3ZM13 3H3V16H13V3ZM5 14H7V5H5V14ZM9 14H11V5H9V14Z"
        fill="#C62326"
      />
    </svg>
  );
};

const AddSubmissionDocument = ({ t, config, onSelect, formData = {}, errors }) => {
  const DocViewerWrapper = window?.Digit?.ComponentRegistryService?.getComponent("DOCVIEWERWRAPPER");
  const tenantId = window?.Digit.ULBService.getCurrentTenantId();
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
  const [formInstances, setFormInstances] = useState([{}]);

  const addAnotherForm = () => {
    setFormInstances([...formInstances, {}]);
  };

  const updateFormData = (updatedFormInstances) => {
    onSelect(config.key, {
      ...formData[config.key],
      submissionDocuments: updatedFormInstances.map((instance) => instance[config.key]),
    });
  };

  const deleteForm = (index) => {
    const updatedFormInstances = [...formInstances];
    updatedFormInstances.splice(index, 1);
    setFormInstances(updatedFormInstances);
    updateFormData(updatedFormInstances);
  };

  const onDocumentUpload = async (fileData, filename) => {
    const fileUploadRes = await Digit.UploadServices.Filestorage("DRISTI", fileData, tenantId);
    return { file: fileUploadRes?.data, fileType: fileData.type, filename };
  };

  function setValue(value, name, input, index) {
    const updatedFormInstances = [...formInstances];
    if (!updatedFormInstances[index][config.key]) {
      updatedFormInstances[index][config.key] = {};
    }
    updatedFormInstances[index][config.key][name] = value;

    setFormInstances(updatedFormInstances);
    updateFormData(updatedFormInstances);
  }

  const getFileStoreData = async (filesData, input, index) => {
    const numberOfFiles = filesData.length;
    let documents = [];
    if (numberOfFiles > 0) {
      const documentres = await Promise.all([onDocumentUpload(filesData[0][1]?.file, filesData[0][0])]);
      documentres.forEach((res) => {
        documents.push({
          documentType: res?.fileType,
          fileStore: res?.file?.files?.[0]?.fileStoreId,
          additionalDetails: { name: res?.filename },
        });
      });

      const file = {
        documentType: documentres[0]?.fileType,
        fileStore: documentres[0]?.file?.files?.[0]?.fileStoreId,
        additionalDetails: {
          name: documentres[0]?.filename,
        },
      };
      setValue(file, input.name, input, index);
    } else {
      setValue(null, input.name, input, index);
    }
  };

  const showDocument = (doc) => {
    return (
      <div>
        <div className="documentDetails_row_items" style={{ display: "flex", flexDirection: "column", alignItems: "center" }}>
          <DocViewerWrapper fileStoreId={doc?.fileStore} tenantId={tenantId} displayFilename={doc?.additionalDetails?.name} />
        </div>
      </div>
    );
  };

  const memoizedDocuments = useMemo(() => {
    return formInstances.map((formInstance, index) => {
      return formInstance.submissionDocuments?.document && showDocument(formInstance.submissionDocuments.document);
    });
  }, [formInstances, tenantId]);

  return (
    <React.Fragment>
      {formInstances.map((formInstance, index) => (
        <div key={index}>
          <div style={{ display: "flex", justifyContent: "space-between", marginBottom: "10px" }}>
            <p style={{ fontSize: "24px", fontWeight: 700 }}>{`Submission Document (${index + 1})`}</p>
            {formInstances.length > 1 && (
              <button type="button" style={{ background: "none" }} onClick={() => deleteForm(index)}>
                <CloseBtn />
              </button>
            )}
          </div>

          {inputs?.map((input, idx) => {
            let currentValue = (formData[index] && formData[index][config.key] && formData[index][config.key][input.name]) || "";
            const showDependentFields =
              Boolean(input.isDependentOn) && !Boolean(formData[index] && formData[index][config.key])
                ? false
                : Boolean(formData[index] && formData[index][config.key] && formData[index][config.key][input.isDependentOn])
                ? formData[index] &&
                  formData[index][config.key] &&
                  Array.isArray(input.dependentKey[input.isDependentOn]) &&
                  input.dependentKey[input.isDependentOn].reduce((res, curr) => {
                    if (!res) return res;
                    res = formData[index][config.key][input.isDependentOn][curr];
                    return res;
                  }, true)
                : true;
            return (
              <React.Fragment key={idx}>
                {errors[input.name] && <CardLabelError>{t(input.error)}</CardLabelError>}
                <div className={`${input?.type}`}>
                  {input?.type !== "infoBox" && <CardLabel>{t(input.label)}</CardLabel>}
                  <div style={{ marginBottom: "20px", marginTop: "10px" }}>
                    {input?.type === "documentUpload" && (
                      <MultiUploadWrapper
                        t={t}
                        module="works"
                        tenantId={window?.Digit.ULBService.getCurrentTenantId()}
                        getFormState={(fileData) => getFileStoreData(fileData, input, index)}
                        showHintBelow={input?.showHintBelow ? true : false}
                        setuploadedstate={formData?.[config.key]?.[input.name] || []}
                        allowedFileTypesRegex={input.allowedFileTypes}
                        allowedMaxSizeInMB={input.allowedMaxSizeInMB || "5"}
                        hintText={input.hintText}
                        maxFilesAllowed={input.maxFilesAllowed || "1"}
                        extraStyleName={{ padding: "0.5rem" }}
                        customClass={input?.customClass}
                        containerStyles={{ ...input?.containerStyles }}
                      />
                    )}
                    {input?.type === "text" && (
                      <TextInput
                        className="field desktop-w-full"
                        key={input.name}
                        value={
                          formInstances[index]?.submissionDocuments?.documentTitle ? formInstances[index]?.submissionDocuments.documentTitle : ""
                        }
                        onChange={(e) => {
                          setValue(e.target.value, input.name, input, index);
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
                    {input?.type === "dropdown" && (
                      <CustomDropdown
                        label={input.name}
                        type={input.type}
                        value={formInstances[index]?.submissionDocuments?.documentType}
                        onChange={(e) => {
                          setValue(e, input.name, input, index);
                        }}
                        config={input.populators}
                      />
                    )}
                    {currentValue &&
                      currentValue.length > 0 &&
                      !["documentUpload", "radioButton"].includes(input.type) &&
                      input.validation &&
                      !currentValue.match(window?.Digit.Utils.getPattern(input.validation.patternType) || input.validation.pattern) && (
                        <CardLabelError>
                          <span style={{ color: "#FF0000" }}> {t(input.validation?.errMsg || "INVALID_BAR_REG_NUMBER")}</span>
                        </CardLabelError>
                      )}
                  </div>
                </div>
              </React.Fragment>
            );
          })}
          {formInstances.length > 0 && memoizedDocuments[index]}
        </div>
      ))}
      <button type="button" onClick={addAnotherForm} style={{ background: "none", fontSize: "16px", fontWeight: 700, color: "#007E7E" }}>
        {formInstances.length < 1 ? `+ ${t("Add Submission Documents")}` : `+ ${t("Add another")}`}
      </button>
    </React.Fragment>
  );
};

export default AddSubmissionDocument;
