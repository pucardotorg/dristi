import { FormComposerV2, Header, Toast } from "@egovernments/digit-ui-react-components";
import React, { useMemo, useState } from "react";
import { CustomArrowDownIcon } from "../../../icons/svgIndex";
import { reviewCaseFileFormConfig } from "../../citizen/FileCase/Config/reviewcasefileconfig";

function CaseFileScrutiny({ t }) {
  const [isDisabled, setIsDisabled] = useState(false);
  const [showErrorToast, setShowErrorToast] = useState(false);
  const [formdata, setFormdata] = useState({ isenabled: true, data: {}, displayindex: 0 });

  const countSectionErrors = (section) => {
    let count = 0;

    if (section?.scrutinyMessage && section?.scrutinyMessage?.FSOError) {
      count++;
    }

    Object.keys(section)?.forEach((key) => {
      if (section[key]) {
        if (section[key]?.scrutinyMessage) {
          count++;
        }
        section[key]?.form?.forEach((item) => {
          Object.keys(item)?.forEach((field) => {
            if (item[field]?.FSOError) {
              count++;
            }
          });
        });
      }
    });

    return count;
  };

  const scrutinyErrorCount = useMemo(() => {
    const errorCount = {};
    for (const key in formdata?.data) {
      if (typeof formdata.data[key] === "object" && formdata.data[key] !== null) {
        if (!errorCount[key]) {
          errorCount[key] = 0;
        }
        errorCount[key] += countSectionErrors(formdata.data[key]);
      }
    }
    return errorCount;
  }, [formdata]);

  //error count for each section
  const onSubmit = () => {};
  const onSaveDraft = () => {};
  const onFormValueChange = (setValue, formData, formState, reset, setError, clearErrors, trigger, getValues) => {
    if (JSON.stringify(formData) !== JSOformDataformDataN.stringify(formdata.data)) {
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
          <div className="accordion-wrapper">{`Litigent Details`}</div>
          <div className="accordion-wrapper">{`Case Specific Details`}</div>
          <div className="accordion-wrapper">{`Additional Details`}</div>
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
            label={t("CS_REGISTER_CASE")}
            config={reviewCaseFileFormConfig}
            onSubmit={onSubmit}
            onSecondayActionClick={onSaveDraft}
            defaultValues={{}}
            onFormValueChange={onFormValueChange}
            cardStyle={{ minWidth: "100%" }}
            isDisabled={isDisabled}
            cardClassName={`e-filing-card-form-style review-case-file`}
            secondaryLabel={t("CS_SAVE_COMMENTS")}
            showSecondaryLabel={true}
            actionClassName="e-filing-action-bar"
          />

          {showErrorToast && <Toast error={true} label={t("ES_COMMON_PLEASE_ENTER_ALL_MANDATORY_FIELDS")} isDleteBtn={true} onClose={closeToast} />}
        </div>
      </div>
    </div>
  );
}

export default CaseFileScrutiny;
