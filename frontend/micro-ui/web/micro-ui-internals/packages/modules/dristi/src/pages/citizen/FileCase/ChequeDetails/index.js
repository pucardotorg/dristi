import { FormComposerV2, Header, Toast } from "@egovernments/digit-ui-react-components";
import React, { useCallback, useState } from "react";
import { useTranslation } from "react-i18next";
import { chequeDetailsConfig } from "./config";

function ChequeDetails() {
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

  const onSubmit = useCallback((data) => {
    // console.log(data);
  });

  return (
    <div className="e-filing-card-wrapper cheque-details">
      <div className="e-filing-header-content">
        <Header>{t("CHEQUE_DETAILS_HEADER")}</Header>
        <div className="e-filing-header-subtext">{t("CHEQUE_DETAILS_SUBTEXT")}</div>
      </div>
      <div className="e-filing-form-container">
        <h2>Cheque 1</h2>
        <FormComposerV2
          config={chequeDetailsConfig}
          t={t}
          onSubmit={onSubmit}
          isDisabled={isDisabled}
          label={"CS_COMMON_SUBMIT"}
          headingStyle={{ textAlign: "center" }}
          cardStyle={{ minWidth: "100%" }}
          onFormValueChange={onFormValueChange}
          secondaryLabel={"CONTINUE"}
          inline
        />
      </div>

      {showErrorToast && <Toast error={true} label={t("ES_COMMON_SELECT_TERMS_AND_CONDITIONS")} isDleteBtn={true} onClose={closeToast} />}
    </div>
  );
}

export default ChequeDetails;
