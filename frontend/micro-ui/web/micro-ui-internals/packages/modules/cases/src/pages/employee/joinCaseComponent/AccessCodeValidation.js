import { InfoCard } from "@egovernments/digit-ui-components";
import CustomCaseInfoDiv from "@egovernments/digit-ui-module-dristi/src/components/CustomCaseInfoDiv";
import { CardLabel, CardLabelError, LabelFieldPair, TextInput } from "@egovernments/digit-ui-react-components";
import React, { useMemo } from "react";

const AccessCodeValidation = ({ t, caseDetails, validationCode, setValidationCode, setIsDisabled, errors, setErrors }) => {
  const caseInfo = useMemo(() => {
    if (caseDetails?.caseCategory) {
      return [
        {
          key: "CS_CASE_NAME",
          value: caseDetails?.caseTitle,
        },
        {
          key: "CS_CASE_ID",
          value: caseDetails?.cnrNumber,
        },
        {
          key: "CS_FILING_NUMBER",
          value: caseDetails?.filingNumber,
        },
        {
          key: "CASE_NUMBER",
          value: caseDetails?.cmpNumber,
        },
        {
          key: "CASE_CATEGORY",
          value: caseDetails?.caseCategory,
        },
      ];
    }
    return [];
  }, [caseDetails]);

  return (
    <div className="enter-validation-code">
      <CustomCaseInfoDiv t={t} data={caseInfo?.slice(0, 4)} column={4} />
      <InfoCard
        variant={"default"}
        label={t("PLEASE_NOTE")}
        additionalElements={{}}
        inline
        text={t("SIX_DIGIT_CODE_INFO")}
        textStyle={{}}
        className={`custom-info-card`}
        style={{ width: "712px" }}
      />
      <LabelFieldPair className="case-label-field-pair">
        <div className="join-case-tooltip-wrapper">
          <CardLabel className="case-input-label">{`${t("ENTER_SECRET_CODE")}`}</CardLabel>
        </div>
        <div style={{ width: "100%", maxWidth: "960px" }}>
          <TextInput
            style={{ width: "100%" }}
            type={"text"}
            name="validationCode"
            value={validationCode}
            onChange={(e) => {
              let val = e.target.value;
              val = val.substring(0, 6);
              val = val.replace(/\D/g, "");
              setValidationCode(val);
              if (val.length === 6) {
                setIsDisabled(false);
              } else {
                setIsDisabled(true);
              }

              setErrors({
                ...errors,
                validationCode: undefined,
              });
            }}
            autoFocus={true}
          />
          {errors?.validationCode && <CardLabelError> {t(errors?.validationCode?.message)} </CardLabelError>}
          {}
        </div>
      </LabelFieldPair>
    </div>
  );
};

export default AccessCodeValidation;
