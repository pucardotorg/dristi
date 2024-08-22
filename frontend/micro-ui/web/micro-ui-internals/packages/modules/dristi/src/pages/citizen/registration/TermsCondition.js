import { FormComposerV2, Toast } from "@egovernments/digit-ui-react-components";
import React, { useEffect, useState } from "react";

const TermsCondition = ({ t, config }) => {
  console.log("config :>> ", config);
  const [showErrorToast, setShowErrorToast] = useState(false);

  const closeToast = () => {
    setShowErrorToast(false);
  };
  useEffect(() => {
    const timer = setTimeout(() => {
      closeToast();
    }, 2000);
  });

  return (
    <div className="terms-condition">
      <FormComposerV2
        config={config}
        t={t}
        noBoxShadow
        inline
        label={t("CS_COMMON_CONTINUE")}
        onSubmit={(props) => {
          console.log("props :>> ", props);
        }}
        onFormValueChange={(setValue, formData) => {
          console.log("formDataaaaaaaaaaaaaaa :>> ", formData);
        }}
        // isDisabled={isDisabled}
        // value={params?.userType || (userTypeRegister && userTypeRegister) || {}}
        // defaultValues={params?.userType || (userTypeRegister && userTypeRegister) || {}}
        // headingStyle={{ textAlign: "center" }}
        // cardStyle={{ minWidth: "100%", padding: 20, display: "flex", flexDirection: "column" }}
        // sectionHeadStyle={{ marginBottom: "20px", fontSize: "40px" }}
        // buttonStyle={{ alignSelf: "center", minWidth: "50%" }}
      ></FormComposerV2>
      {showErrorToast && <Toast error={true} label={t("ES_COMMON_PLEASE_ENTER_ALL_MANDATORY_FIELDS")} isDleteBtn={true} onClose={closeToast} />}
    </div>
  );
};

export default TermsCondition;
