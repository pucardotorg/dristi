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
        const value = formDataCopy[key];
        if (typeof value === "string") {
          const updatedValue = value.trimStart().replace(/ +/g, " ");
          if (updatedValue !== value) {
            setValue(key, updatedValue);
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
