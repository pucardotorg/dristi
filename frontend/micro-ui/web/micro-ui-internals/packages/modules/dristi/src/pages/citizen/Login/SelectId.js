import { FormComposerV2, Toast } from "@egovernments/digit-ui-react-components";
import React, { useEffect, useState } from "react";

function SelectId({ config, t, params, history, onSelect, pathOnRefresh }) {
  const [showErrorToast, setShowErrorToast] = useState(false);
  const [isDisabled, setIsDisabled] = useState(false);
  const closeToast = () => {
    setShowErrorToast(false);
  };
  useEffect(() => {
    const timer = setTimeout(() => {
      closeToast();
    }, 2000);

    return () => clearTimeout(timer);
  }, [closeToast]);
  const onFormValueChange = (setValue, formData, formState) => {
    let isDisabled = false;
    config.forEach((curr) => {
      if (isDisabled) return;
      if (!(curr.body[0].key in formData) || !formData[curr.body[0].key]) {
        return;
      }
      curr.body[0].populators.inputs.forEach((input) => {
        if (isDisabled) return;
        if (Array.isArray(input.name)) return;
        if (
          formData[curr.body[0].key][input.name] &&
          formData[curr.body[0].key][input.name].length > 0 &&
          !["documentUpload", "radioButton"].includes(input.type) &&
          input.validation &&
          !formData[curr.body[0].key][input.name].match(Digit.Utils.getPattern(input.validation.patternType) || input.validation.pattern)
        ) {
          isDisabled = true;
        }
        if (Array.isArray(formData[curr.body[0].key][input.name]) && formData[curr.body[0].key][input.name].length === 0) {
          isDisabled = true;
        }
      });
    });
    if (isDisabled) {
      setIsDisabled(isDisabled);
    } else {
      setIsDisabled(false);
    }
  };

  const validateFormData = (data) => {
    let isValid = true;
    if (!data?.IdVerification?.selectIdType) {
      isValid = false;
    }

    return isValid;
  };

  if (!params?.address) {
    history.push(pathOnRefresh);
  }
  return (
    <div className="id-verification">
      <FormComposerV2
        config={config}
        t={t}
        noBoxShadow
        inline
        isDisabled={isDisabled}
        label={t("CS_COMMON_CONTINUE")}
        onSecondayActionClick={() => { }}
        onFormValueChange={onFormValueChange}
        defaultValues={params?.indentity || {}}
        value={params?.indentity || {}}
        onSubmit={(props) => {
          if (!validateFormData(props)) {
            setShowErrorToast(!validateFormData(props));
          } else {
            onSelect(props);
          }
          return;
        }}
        submitInForm
      ></FormComposerV2>
      {
        showErrorToast &&
        <Toast error={true} label={t("ID_NOT_SELECTED_ERROR_MESSAGE")} isDleteBtn={true} onClose={closeToast} />
      }
    </div>
  );
}

export default SelectId;
