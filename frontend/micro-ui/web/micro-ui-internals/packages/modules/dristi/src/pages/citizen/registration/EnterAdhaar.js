import { FormComposerV2, Toast } from "@egovernments/digit-ui-react-components";
import React, { useState, useEffect } from "react";
import { useHistory } from "react-router-dom/cjs/react-router-dom.min";
const EnterAdhaar = ({ t, onSelect, config, params }) => {
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
    history.push("/digit-ui/citizen/dristi/home/login");
  }
  return (
    <React.Fragment>
      <FormComposerV2
        config={config}
        t={t}
        noBoxShadow
        inline
        label={t("GET_OTP")}
        onSecondayActionClick={() => {}}
        headingStyle={{ textAlign: "center" }}
        cardStyle={{ minWidth: "100%", padding: 20, display: "flex", flexDirection: "column", alignItems: "center" }}
        sectionHeadStyle={{ marginBottom: "20px", fontSize: "40px" }}
        onSubmit={(data) => {
          if (!validateFormData(data)) {
            setShowErrorToast(!validateFormData(data));
          } else {
            onSelect(data?.AdhaarInput?.aadharNumber);
          }
          return;
        }}
        defaultValues={params?.aadharNumber || {}}
        submitInForm
        buttonStyle={{ alignSelf: "center", minWidth: "50%" }}
      ></FormComposerV2>

      {showErrorToast && <Toast error={true} label={t("ES_COMMON_PLEASE_ENTER_ALL_MANDATORY_FIELDS")} isDleteBtn={true} onClose={closeToast} />}
    </React.Fragment>
  );
};

export default EnterAdhaar;
