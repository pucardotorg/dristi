import { FormComposerV2, Header, Toast } from "@egovernments/digit-ui-react-components";
import React, { useState } from "react";
import { CustomArrowDownIcon } from "../../../icons/svgIndex";
import { reviewCaseFileFormConfig } from "../../citizen/FileCase/Config/reviewcasefileconfig";

function ViewCaseFile({ t }) {
  const [isDisabled, setIsDisabled] = useState(false);
  const [showErrorToast, setShowErrorToast] = useState(false);
  const onSubmit = () => { };
  const onSaveDraft = () => { };
  const onFormValueChange = (setValue, formData, formState, reset, setError, clearErrors, trigger, getValues, index) => { };
  const closeToast = () => {
    setShowErrorToast(false);
  };

  return (
    <div className="file-case">
      <div className="file-case-side-stepper">
        <div className="file-case-select-form-section">
          <div className="accordion-wrapper">Litigent Details</div>
          <div className="accordion-wrapper">Case Specific Details</div>
          <div className="accordion-wrapper">Additional Details</div>
        </div>
      </div>
      <div className="file-case-form-section">
        <div className="employee-card-wrapper">
          <div className="header-content">
            <div className="header-details">
              <Header>{t("Review Case")}</Header>
              <div className="header-icon" onClick={() => { }}>
                <CustomArrowDownIcon />
              </div>
            </div>
          </div>
          <FormComposerV2
            label={t("CS_COMMON_CONTINUE")}
            config={reviewCaseFileFormConfig}
            onSubmit={onSubmit}
            onSecondayActionClick={onSaveDraft}
            defaultValues={{}}
            onFormValueChange={(setValue, formData, formState, reset, setError, clearErrors, trigger, getValues) => {
              onFormValueChange(setValue, formData, formState, reset, setError, clearErrors, trigger, getValues);
            }}
            cardStyle={{ minWidth: "100%" }}
            isDisabled={isDisabled}
            cardClassName={`e-filing-card-form-style review-case-file`}
            secondaryLabel={t("CS_SAVE_DRAFT")}
            showSecondaryLabel={true}
            actionClassName="e-filing-action-bar"
          />

          {showErrorToast && <Toast error={true} label={t("ES_COMMON_PLEASE_ENTER_ALL_MANDATORY_FIELDS")} isDleteBtn={true} onClose={closeToast} />}
        </div>
      </div>
    </div>
  );
}

export default ViewCaseFile;
