import React, { useEffect, useMemo, useState } from "react";
import { FormComposerV2, Header, Loader } from "@egovernments/digit-ui-react-components";
import { useTranslation } from "react-i18next";
import { applicationTypeConfig, configsBailBond, submissionTypeConfig } from "../../configs/MakeSubmissionBailConfig";

const fieldStyle = { marginRight: 0 };

const MakeSubmissionBail = () => {
  const { t } = useTranslation();
  const [formdata, setFormdata] = useState({}); // will get all the data in formData
  const { submissionId: submissionId } = Digit.Hooks.useQueryParams(); // query paramas
  const tenantId = Digit.ULBService.getCurrentTenantId();

  const submissionType = useMemo(() => {
    return formdata?.submissionID;
  }, [formdata?.submissionID]);

  const submissionFormConfig = useMemo(() => {
    const submissionConfigKeys = {
      [submissionId]: applicationTypeConfig,
    };
    return submissionConfigKeys[submissionType] || [];
  }, [submissionType]);

  const applicationType = useMemo(() => {
    return formdata?.submissionType?.code;
  }, [formdata?.submissionType?.code]);

  const applicationFormConfig = useMemo(() => {
    const applicationConfigKeys = {
      Application: configsBailBond,
    };
    return applicationConfigKeys?.[applicationType] || [];
  }, [applicationType]);

  const modifiedFormConfig = useMemo(() => {
    return [...submissionTypeConfig, ...submissionFormConfig, ...applicationFormConfig];
  }, [submissionFormConfig, applicationFormConfig]);

  const defaultFormValue = {
    submissionID: submissionId,
  };

  const onFormValueChange = (setValue, formData, formState, reset, setError, clearErrors, trigger, getValues) => {
    console.log(formData, "callled");
    if (JSON.stringify(formData) !== JSON.stringify(formdata)) {
      setFormdata(formData);
    }
  };

  return (
    <React.Fragment>
      <div style={{ width: "906px", padding: "24px 24px 24px 40px", height: "100%" }}>
        <Header> {t("Make a Submission")}</Header>
        <FormComposerV2
          className={"bail-submission"}
          label={t("Review Submission")}
          showSecondaryLabel={true}
          secondaryLabel={t("Save As Draft")}
          config={modifiedFormConfig}
          defaultValues={defaultFormValue}
          onFormValueChange={onFormValueChange}
          // onSubmit={() => setShowReviewModal(true)}
          fieldStyle={fieldStyle}
        />
      </div>
    </React.Fragment>
  );
};

export default MakeSubmissionBail;
