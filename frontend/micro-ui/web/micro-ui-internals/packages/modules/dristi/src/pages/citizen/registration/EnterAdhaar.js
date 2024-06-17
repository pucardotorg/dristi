import { FormComposerV2, Toast } from "@egovernments/digit-ui-react-components";
import React, { useState, useEffect } from "react";
import { useHistory } from "react-router-dom/cjs/react-router-dom.min";
const EnterAdhaar = ({ t, onSelect, config, params, pathOnRefresh }) => {
  const history = useHistory();
  const [showErrorToast, setShowErrorToast] = useState(false);
  const validateFormData = (data) => {
    let isValid = true;
    if (!(data?.AdhaarInput?.aadharNumber.length === 12)) {
      isValid = false;
    }

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
  if (!params?.indentity) {
    history.push(pathOnRefresh);
  }

  return (
    <div className="enter-addhar">
      <FormComposerV2
        config={config}
        t={t}
        noBoxShadow
        inline
        label={t("GET_OTP")}
        onSecondayActionClick={() => { }}
        onSubmit={(data) => {
          if (!validateFormData(data)) {
            setShowErrorToast(!validateFormData(data));
          } else {
            onSelect(data?.AdhaarInput?.aadharNumber);
          }
          return;
        }}
        defaultValues={{ AdhaarInput: { aadharNumber: params?.adhaarNumber } }}
        submitInForm
      ></FormComposerV2>

      {showErrorToast && <Toast error={true} label={t("INVALID_AADHAAR_ERROR_MESSAGE")} isDleteBtn={true} onClose={closeToast} />}
    </div>
  );
};

export default EnterAdhaar;
