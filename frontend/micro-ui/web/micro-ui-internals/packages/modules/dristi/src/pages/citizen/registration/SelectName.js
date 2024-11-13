import { FormComposerV2, Toast } from "@egovernments/digit-ui-react-components";
import React, { useEffect, useState } from "react";

const SelectName = ({ config, t, onSubmit, isDisabled, params, history, value, isUserLoggedIn }) => {
  const [showErrorToast, setShowErrorToast] = useState(false);

  const closeToast = () => {
    setShowErrorToast(false);
  };

  useEffect(() => {
    const timer = setTimeout(() => {
      closeToast();
    }, 2000);

    return () => clearTimeout(timer);
  }, [closeToast]);

  if (!params?.mobileNumber && !isUserLoggedIn) {
    history.push("/digit-ui/citizen/dristi/home/login");
  }

  const onFormValueChange = (setValue, formData, formState) => {
    const formDataCopy = structuredClone(formData);
    for (const key in formDataCopy) {
      if (Object.hasOwnProperty.call(formDataCopy, key)) {
        const oldValue = formDataCopy[key];
        let value = oldValue;
        if (typeof value === "string") {
          let updatedValue = value
            .replace(/[^a-zA-Z\s]/g, "")
            .trimStart()
            .replace(/ +/g, " ");
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
  };

  return (
    <React.Fragment>
      <FormComposerV2
        config={config}
        t={t}
        noBoxShadow
        inline={false}
        label={t("CORE_COMMON_CONTINUE")}
        onSecondayActionClick={() => {}}
        onFormValueChange={onFormValueChange}
        onSubmit={(props) => onSubmit(props)}
        defaultValues={params?.name || {}}
        submitInForm
        className={"registration-select-name"}
      ></FormComposerV2>
    </React.Fragment>
  );
};

export default SelectName;
