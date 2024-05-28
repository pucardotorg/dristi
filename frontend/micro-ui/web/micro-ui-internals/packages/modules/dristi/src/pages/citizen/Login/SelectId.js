import { FormComposerV2, Toast } from "@egovernments/digit-ui-react-components";
import React, { useState } from "react";

function SelectId({ config, t, params, history, onSelect }) {
  const [showErrorToast, setShowErrorToast] = useState(false);
  const [isDisabled, setIsDisabled] = useState(false);

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
  const closeToast = () => {
    setShowErrorToast(false);
  };

  const validateFormData = (data) => {
    let isValid = true;
    if (!data?.IdVerification?.selectIdType) {
      isValid = false;
    }

    return isValid;
  };

  if (!params?.address) {
    history.push("/digit-ui/citizen/dristi/home/login");
  }
  return (
    <React.Fragment>
      <FormComposerV2
        config={config}
        t={t}
        noBoxShadow
        inline
        isDisabled={isDisabled}
        label={t("CS_COMMON_CONTINUE")}
        onSecondayActionClick={() => {}}
        headingStyle={{ textAlign: "center" }}
        cardStyle={{ minWidth: "100%", padding: 20, display: "flex", flexDirection: "column", justifyContent: "center" }}
        sectionHeadStyle={{ marginBottom: "20px", fontSize: "40px" }}
        onFormValueChange={onFormValueChange}
        defaultValues={params?.indentity || {}}
        buttonStyle={{ alignSelf: "center", minWidth: "50%" }}
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
      {showErrorToast && <Toast error={true} label={t("ES_COMMON_PLEASE_ENTER_ALL_MANDATORY_FIELDS")} isDleteBtn={true} onClose={closeToast} />}
    </React.Fragment>
  );
}

export default SelectId;
