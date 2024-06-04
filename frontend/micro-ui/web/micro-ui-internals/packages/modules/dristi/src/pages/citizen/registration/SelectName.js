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

        if (typeof value === "string" && value.trim() === "" && value !== value.trim()) {
          setValue(key, value.trim());
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
        headingStyle={{ textAlign: "center" }}
        cardStyle={{ minWidth: "100%", padding: 20, display: "flex", flexDirection: "column", alignItems: "center" }}
        sectionHeadStyle={{ fontSize: "24px", lineHeight: "30px" }}
        onSubmit={(props) => onSubmit(props)}
        defaultValues={{ AdhaarInput: { aadharNumber: params?.adhaarNumber } }}
        submitInForm
        className={"registration-select-name"}
        buttonStyle={{ alignSelf: "center", minWidth: "50%" }}
      ></FormComposerV2>
    </React.Fragment>
  );
};

export default SelectName;
