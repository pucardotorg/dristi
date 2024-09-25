import { FormComposerV2 } from "@egovernments/digit-ui-react-components";
import React, { useEffect, useMemo, useRef, useState } from "react";
import { registerRespondentConfig } from "@egovernments/digit-ui-module-dristi/src/pages/citizen/FileCase/Config/resgisterRespondentConfig";
import isEqual from "lodash/isEqual";
import { checkNameValidation } from "@egovernments/digit-ui-module-dristi/src/pages/citizen/FileCase/EfilingValidationUtils";

const RegisterRespondentForm = ({ accusedRegisterFormData, setAccusedRegisterFormData, error }) => {
  const setFormErrors = useRef(null);

  const [selected, setSelected] = useState({});
  const formConfig = useMemo(() => {
    return selected?.respondentType?.code === "REPRESENTATIVE" ? registerRespondentConfig?.companyConfig : registerRespondentConfig?.individualConfig;
  }, [selected?.respondentType?.code]);

  useEffect(() => {
    for (const key in error) {
      setFormErrors.current(key, error[key]);
    }
  }, [error]);

  const onFormValueChange = (setValue, formData, formState, reset, setError, clearErrors, trigger, getValues) => {
    setFormErrors.current = setError;
    if (formData?.mobileNumber) {
      const formDataCopy = structuredClone(formData);
      for (const key in formDataCopy) {
        if (key === "mobileNumber" && Object.hasOwnProperty.call(formDataCopy, key)) {
          const oldValue = formDataCopy[key];
          let value = oldValue;
          if (typeof value === "string") {
            if (value.length > 10) {
              value = value.slice(0, 10);
            }

            let updatedValue = value.replace(/\D/g, "");
            if (updatedValue !== oldValue) {
              const element = document.querySelector(`[name="${key}"]`);
              const start = element?.selectionStart;
              const end = element?.selectionEnd;
              setValue(key, updatedValue);
              setTimeout(() => {
                element?.setSelectionRange(start, end);
              }, 0);
            }
          }
        }
      }
    }
    checkNameValidation({ formData, setValue, selected: "respondentDetails" });

    if (!isEqual(selected, formData)) {
      setSelected(formData);
      setAccusedRegisterFormData(formData);
    }
  };

  return (
    <div className="register-respondent-form">
      <FormComposerV2
        key={`form-config-${selected?.respondentType?.code}`}
        config={formConfig}
        onFormValueChange={onFormValueChange}
        defaultValues={accusedRegisterFormData}
        actionClassName="e-filing-action-bar"
        noBreakLine
      />
    </div>
  );
};

export default RegisterRespondentForm;
