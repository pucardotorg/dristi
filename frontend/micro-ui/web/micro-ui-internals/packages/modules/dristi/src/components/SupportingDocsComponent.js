import React, { useMemo, useState } from "react";
import SelectMultiUpload from "./SelectMultiUpload";
import { CardLabelError, TextInput, CustomDropdown, Header } from "@egovernments/digit-ui-react-components";

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
const SupportingDocsComponent = ({ t, config, onSelect, formData = {}, errors, setError, clearErrors }) => {
  const [formInstances, setFormInstances] = useState(formData?.[config?.key] || [{}]);
  const disable = config?.disable;

  const inputs = useMemo(
    () =>
      config?.populators?.inputs || [
        {
          isMandatory: true,
          key: "documentType",
          type: "dropdown",
          label: "DOCUMENT_TYPE",
          populators: {
            name: "documentType",
            optionsKey: "code",
            error: "CORE_REQUIRED_FIELD_ERROR",
            styles: { maxWidth: "100%" },
            required: true,
            isMandatory: true,
            mdmsConfig: {
              moduleName: "Submission",
              masterName: "SubmissionDocumentType",
              select: "(data) => {return data['Submission'].SubmissionDocumentType?.map((item) => {return item;});}",
            },
            customStyle: { display: "flex", flexDirection: "column", alignItems: "flex-start" },
          },
        },
      ],
    [config?.populators?.inputs]
  );

  const addAnotherForm = () => {
    const newFormInstances = [...formInstances, {}];
    setFormInstances(newFormInstances);
    updateFormData(newFormInstances);
  };

  const updateFormData = (updatedFormInstances) => {
    onSelect(
      config.key,
      updatedFormInstances.map((instance) => instance[config.key] || {})
    );
  };


  const deleteForm = (index) => {
    const updatedFormInstances = [...formInstances];
    updatedFormInstances.splice(index, 1);
    setFormInstances(updatedFormInstances);
    updateFormData(updatedFormInstances);
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

  function uploadedDocs(value, inputDocs, name, index) {
    const updatedFormInstances = [...formInstances];

    if (!updatedFormInstances[index][config.key]) {
      updatedFormInstances[index][config.key] = {};
    }
    updatedFormInstances[index][config.key][value] = inputDocs;
    setFormInstances(updatedFormInstances);
    updateFormData(updatedFormInstances);
  }

  return (
    <React.Fragment>
      <style>
        {`
          .text-Input .text-input-width {
          max-width : none
          }
          `}
      </style>
      {config.header && <Header>{t(config.header)}</Header>}
      {formInstances.map((formInstance, formIndex) => (
        <div key={formIndex}>
          <div style={{ display: "flex", justifyContent: "space-between", marginBottom: "10px" }}>
            <p style={{ fontSize: "24px", fontWeight: 700 }}>{`${t(config?.name)} (${formIndex + 1})`}</p>
            {formInstances.length > 1 && !disable && (
              <button type="button" style={{ background: "none" }} onClick={() => deleteForm(formIndex)}>
                <CloseBtn />
              </button>
            )}
          </div>

          {inputs?.map((input, inputIndex) => {
            const obj = formInstances?.[formIndex]?.[config?.key] ? formInstances[formIndex]?.[config?.key] : formInstances[formIndex];

            return (
              <React.Fragment key={inputIndex}>
                {input?.type === "text" && (
                  <div className="text-Input">
                    <div style={{ marginBottom: "8px" }}>{t(input.label)} {input?.isOptional && <span style={{ color: "#77787B" }}>&nbsp;{t("CS_IS_OPTIONAL")}</span>}</div>
                    <TextInput
                      t={t}
                      className="field desktop-w-full"
                      key={input?.key}
                      value={obj?.[input?.name] ? obj?.[input?.name] : ""}
                      onChange={(e) => {
                        setValue(e.target.value, input.key, input, formIndex);
                      }}
                      disable={input?.isDisabled}
                      isRequired={input?.validation?.isRequired}
                      pattern={input?.validation?.pattern}
                      errMsg={input?.validation?.errMsg}
                      maxlength={input?.validation?.maxlength}
                      minlength={input?.validation?.minlength}
                    />
                  </div>
                )}
                {input?.component === "SelectMultiUpload" && (
                  <SelectMultiUpload
                    config={input}
                    t={t}
                    formData={formInstances[formIndex]?.[config?.key]}
                    onSelect={(value, inputDocs) => uploadedDocs(value, inputDocs, input.key, formIndex)}
                    errors={errors}
                  />
                )}
                {input?.type === "dropdown" && (
                  <div>
                    <div style={{ marginBottom: "8px" }}>{t(input.label)}</div>
                    <CustomDropdown
                      t={t}
                      label={input.key}
                      type={input.type}
                      value={obj?.[input?.key] ? obj?.[input?.key] : {}}
                      onChange={(e) => {
                        setValue(e, input.key, input, formIndex);
                      }}
                      key={input.key}
                      config={input.populators}
                    />
                  </div>
                )}
                {errors[`${input?.key}_${formIndex}`] && (
                  <CardLabelError style={{ width: "70%", marginLeft: "30%", fontSize: "12px" }}>
                    {errors[`${input?.key}_${formIndex}`]?.message ? errors[`${input?.key}_${formIndex}`]?.message : t(errors[`${input?.key}_${formIndex}`]) || t(input.error)}
                  </CardLabelError>
                )}
              </React.Fragment>
            );
          })}
        </div>
      ))}
      {!disable && (
        <button type="button" onClick={addAnotherForm} style={{ background: "none", fontSize: "16px", fontWeight: 700, color: "#007E7E" }}>
          {formInstances.length < 1 ? `+ ${t("ADD_SUBMISSION_DOCUMENTS")}` : `+ ${t("ADD_ANOTHER")}`}
        </button>
      )}
    </React.Fragment>
  );
};

export default SupportingDocsComponent;
