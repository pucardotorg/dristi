import { FormComposerV2, Header, Loader, Toast } from "@egovernments/digit-ui-react-components";
import React, { useMemo, useState } from "react";
import { useLocation, Redirect } from "react-router-dom";
import useSearchCaseService from "../../../hooks/dristi/useSearchCaseService";
import { CustomArrowDownIcon } from "../../../icons/svgIndex";
import { reviewCaseFileFormConfig } from "../../citizen/FileCase/Config/reviewcasefileconfig";

function ViewCaseFile({ t }) {
  const [isDisabled, setIsDisabled] = useState(false);
  const location = useLocation();
  const searchParams = new URLSearchParams(location.search);
  const caseId = searchParams.get("caseId");
  const tenantId = window?.Digit.ULBService.getCurrentTenantId();

  const [showErrorToast, setShowErrorToast] = useState(false);
  const onSubmit = () => {};
  const onSaveDraft = () => {};
  const onFormValueChange = (setValue, formData, formState, reset, setError, clearErrors, trigger, getValues, index) => {};
  const closeToast = () => {
    setShowErrorToast(false);
  };

  const { data: caseFetchResponse, refetch: refetchCaseData, isLoading } = useSearchCaseService(
    {
      criteria: [
        {
          caseId: caseId,
        },
      ],
      tenantId,
    },
    {},
    "dristi",
    caseId,
    Boolean(caseId)
  );

  const caseData = useMemo(() => caseFetchResponse?.criteria?.[0]?.responseList?.[0] || null, [caseFetchResponse]);

  const formConfig = useMemo(() => {
    if (!caseData) return null;
    return [
      ...reviewCaseFileFormConfig.map((form) => {
        return {
          ...form,
          body: form.body.map((section) => {
            return {
              ...section,
              populators: {
                ...section.populators,
                inputs: section.populators.inputs?.map((input) => {
                  delete input.data;
                  return {
                    ...input,
                    data: caseData.additionalDetails[input.key]?.formdata,
                  };
                }),
              },
            };
          }),
        };
      }),
    ];
  }, [reviewCaseFileFormConfig, caseData]);

  if (!caseId) {
    return <Redirect to="cases" />;
  }

  if (isLoading) {
    return <Loader />;
  }

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
            config={formConfig}
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
