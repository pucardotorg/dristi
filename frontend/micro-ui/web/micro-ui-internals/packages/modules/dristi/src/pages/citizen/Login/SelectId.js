import { Button, FormComposerV2, Toast } from "@egovernments/digit-ui-react-components";
import React, { useState } from "react";
import { Link, useHistory, useLocation } from "react-router-dom/cjs/react-router-dom.min";

function SelectId({ config, t, params, history, onSelect }) {
  const [showErrorToast, setShowErrorToast] = useState(false);

  const closeToast = () => {
    setShowErrorToast(false);
  };

  const onFormValueChange = (setValue, formData, formState, reset, setError) => {};

  if (sessionStorage.getItem("Digit.UploadedDocument") || sessionStorage.getItem("Digit.aadharNumber")) {
    sessionStorage.removeItem("Digit.UploadedDocument");
    sessionStorage.removeItem("Digit.aadharNumber");
    history.push(`/${window.contextPath}/citizen/dristi/home`);
  }
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
        label={t("CS_COMMON_CONTINUE")}
        onSecondayActionClick={() => {}}
        headingStyle={{ textAlign: "center" }}
        cardStyle={{ minWidth: "100%", padding: 20, display: "flex", flexDirection: "column", justifyContent: "center" }}
        sectionHeadStyle={{ marginBottom: "20px", fontSize: "40px" }}
        onSubmit={(props) => onSelect(props?.IdVerification?.selectIdType)}
        submitInForm
      ></FormComposerV2>
      {showErrorToast && <Toast error={true} label={t("ES_COMMON_PLEASE_ENTER_ALL_MANDATORY_FIELDS")} isDleteBtn={true} onClose={closeToast} />}
    </React.Fragment>
  );
}

export default SelectId;
