import { FormComposerV2, Toast } from "@egovernments/digit-ui-react-components";
import React, { useEffect, useState } from "react";
import { useHistory, useLocation } from "react-router-dom/cjs/react-router-dom.min";

function UploadIdType({ config, t, onAadharChange, onDocumentUpload, params, pathOnRefresh, isAdvocateUploading, onFormValueChange }) {
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

  useEffect(() => {
    const timer = setTimeout(() => {
      closeToast();
    }, 2000);

    return () => clearTimeout(timer);
  }, [closeToast]);

  if (!isAdvocateUploading && !params?.indentity) {
    history.push(pathOnRefresh);
  }
  return (
    <div className="advocate-additional-details upload-id">
      <FormComposerV2
        config={config}
        t={t}
        onSubmit={(data) => {
          if (isAdvocateUploading) {
            return;
          }
          if (!validateFormData(data)) {
            setShowErrorToast(!validateFormData(data));
          } else if (data?.SelectUserTypeComponent?.aadharNumber) {
            onAadharChange(data?.SelectUserTypeComponent?.aadharNumber);
          } else {
            onDocumentUpload(
              data?.SelectUserTypeComponent?.ID_Proof[0][0],
              data?.SelectUserTypeComponent?.ID_Proof[0][1]?.file,
              data?.SelectUserTypeComponent?.selectIdType
            );
          }
          return;
        }}
        onFormValueChange={onFormValueChange}
        noBoxShadow
        inline
        label={!isAdvocateUploading ? "CS_COMMON_CONTINUE" : ""}
        // onFormValueChange={onFormValueChange}
        onSecondayActionClick={() => {}}
        submitInForm={!isAdvocateUploading}
      ></FormComposerV2>
      {showErrorToast && <Toast error={true} label={t("ES_COMMON_PLEASE_ENTER_ALL_MANDATORY_FIELDS")} isDleteBtn={true} onClose={closeToast} />}
    </div>
  );
}
export default UploadIdType;
