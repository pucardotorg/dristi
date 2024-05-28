import { FormComposerV2, Header, Toast } from "@egovernments/digit-ui-react-components";
import React, { useState } from "react";
import { complainantDetailsConfig } from "./config";
import { useTranslation } from "react-i18next";

function ComplainantDetails({ params = {}, setParams = () => {} }) {
  const { t } = useTranslation();
  const [showErrorToast, setShowErrorToast] = useState(false);
  const [isDisabled, setIsDisabled] = useState(false);

  const closeToast = () => {
    setShowErrorToast(false);
  };

  const onFormValueChange = (setValue, formData, formState) => {
    if (formState?.submitCount) {
      setIsDisabled(true);
    }
  };

  const onSubmit = (data) => {};

  return (
    <div className="e-filing-card-wrapper">
      <div className="e-filing-header-content">
        <Header>{t("COMPLAINANT_DETAILS_HEADER")}</Header>
        <div className="e-filing-header-subtext">{t("COMPLAINANT_DETAILS_SUBTEXT")}</div>
      </div>
      <FormComposerV2
        config={complainantDetailsConfig}
        t={t}
        onSubmit={(props) => {
          onSubmit(props);
        }}
        isDisabled={isDisabled}
        defaultValues={params?.Terms_Conditions || {}}
        label={"CS_COMMON_SUBMIT"}
        headingStyle={{ textAlign: "center" }}
        cardStyle={{ minWidth: "100%" }}
        onFormValueChange={onFormValueChange}
        secondaryLabel={"CONTINUE"}
      ></FormComposerV2>
      {showErrorToast && <Toast error={true} label={t("ES_COMMON_SELECT_TERMS_AND_CONDITIONS")} isDleteBtn={true} onClose={closeToast} />}
    </div>
  );
}

export default ComplainantDetails;
