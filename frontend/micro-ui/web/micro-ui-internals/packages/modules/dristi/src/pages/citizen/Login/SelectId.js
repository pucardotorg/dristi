import { FormComposerV2, Toast } from "@egovernments/digit-ui-react-components";
import React, { useState } from "react";
import { useHistory, useLocation } from "react-router-dom/cjs/react-router-dom.min";

function SelectId({ config, t, onAadharChange, onDocumentUpload }) {
  const [showErrorToast, setShowErrorToast] = useState(false);
  const history = useHistory();
  const validateFormData = (data) => {
    let isValid = true;
    config.forEach((curr) => {
      if (!isValid) return;
      if (!(curr.body[0].key in data) || !data[curr.body[0].key]) {
        isValid = false;
      }
      curr.body[0].populators.inputs.forEach((input) => {
        if (!isValid) return;
        if (Array.isArray(input.name)) return;
        if (input.disableMandatoryFieldFor) {
          if (input.disableMandatoryFieldFor.some((field) => !data[curr.body[0].key][field]) && data[curr.body[0].key][input.name]) {
            if (Array.isArray(data[curr.body[0].key][input.name]) && data[curr.body[0].key][input.name].length === 0) {
              isValid = false;
            }
            if ((input?.isMandatory && !(input.name in data[curr.body[0].key])) || !data[curr.body[0].key][input.name]) {
              isValid = false;
            }
            return;
          } else {
            if (
              (input?.isMandatory && !(input.name in data[curr.body[0].key])) ||
              (!data[curr.body[0].key][input.name] && !input.disableMandatoryFieldFor.some((field) => data[curr.body[0].key][field]))
            ) {
              isValid = false;
            }
          }
          return;
        } else {
          if (Array.isArray(data[curr.body[0].key][input.name]) && data[curr.body[0].key][input.name].length === 0) {
            isValid = false;
          }
          if (input?.isMandatory && !(input.name in data[curr.body[0].key])) {
            isValid = false;
          }
        }
      });
    });
    return isValid;
  };

  const closeToast = () => {
    setShowErrorToast(false);
  };

  const onFormValueChange = (setValue, formData, formState, reset, setError) => {};

  if (sessionStorage.getItem("Digit.UploadedDocument") || sessionStorage.getItem("Digit.aadharNumber")) {
    sessionStorage.removeItem("Digit.UploadedDocument");
    sessionStorage.removeItem("Digit.aadharNumber");
    history.push(`/${window.contextPath}/citizen/dristi/home`);
  }

  return (
    <React.Fragment>
      <FormComposerV2
        config={config}
        t={t}
        onSubmit={(data) => {
          console.log(data);
          if (!validateFormData(data)) {
            setShowErrorToast(!validateFormData(data));
          } else if (data?.SelectUserTypeComponent?.aadharNumber) {
            onAadharChange(data?.SelectUserTypeComponent?.aadharNumber);
          } else {
            onDocumentUpload(
              data?.SelectUserTypeComponent?.ID_Proof[0][0],
              data?.SelectUserTypeComponent?.ID_Proof[0][1]?.file,
              data?.SelectUserTypeComponent?.selectIdTypeType
            );
          }
          return;
        }}
        noBoxShadow
        inline
        label={"Next"}
        // onFormValueChange={onFormValueChange}
        onSecondayActionClick={() => {}}
        headingStyle={{ textAlign: "center" }}
        cardStyle={{ minWidth: "100%", padding: 20, display: "flex", flexDirection: "column" }}
        sectionHeadStyle={{ marginBottom: "20px", fontSize: "40px" }}
      ></FormComposerV2>
      {showErrorToast && <Toast error={true} label={t("ES_COMMON_PLEASE_ENTER_ALL_MANDATORY_FIELDS")} isDleteBtn={true} onClose={closeToast} />}
    </React.Fragment>
  );
}

export default SelectId;
