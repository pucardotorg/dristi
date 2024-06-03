import React, { useState } from "react";
import { FormComposerV2, Header, Toast } from "@egovernments/digit-ui-react-components";
import { CustomArrowDownIcon } from "../../../icons/svgIndex";
import { reviewCaseFileFormConfig } from "../../citizen/FileCase/Config/reviewcasefileconfig";

function CaseViewFile({ t }) {
  const [isDisabled, setIsDisabled] = useState(false);
  const [showErrorToast, setShowErrorToast] = useState(false);
  const [formdata, setFormdata] = useState({ isenabled: true, data: {}, displayindex: 0 });
  const onSubmit = () => {};
  const onSaveDraft = () => {};
  const onFormValueChange = (setValue, formData, formState, reset, setError, clearErrors, trigger, getValues) => {
    if (JSON.stringify(formData) !== JSON.stringify(formdata.data)) {
      setFormdata((prev) => {
        return { ...prev, data: formData };
      });
    }
  };
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
              <div className="header-icon" onClick={() => {}}>
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
            onFormValueChange={onFormValueChange}
            cardStyle={{ minWidth: "100%" }}
            isDisabled={isDisabled}
            cardClassName={`e-filing-card-form-style`}
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

export default CaseViewFile;
