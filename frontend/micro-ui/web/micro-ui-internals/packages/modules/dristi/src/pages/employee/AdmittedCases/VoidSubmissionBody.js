import { CardLabel, CardLabelError, LabelFieldPair, TextInput } from "@egovernments/digit-ui-react-components";
import React, { useState } from "react";

const voidMainStyle = { width: "536px", padding: "16px 24px", display: "flex", flexDirection: "column", gap: "20px" };

const pStyle = {
  fontFamily: "Roboto",
  fontSize: "16px",
  fontWeight: 400,
  lineHeight: "24px",
  textAlign: "left",
  color: "#0A0A0A",
  margin: "0px",
};

const VoidSubmissionBody = ({ t, documentSubmission, setVoidReason, voidReason, disabled }) => {
  const [errors, setErrors] = useState({});
  return (
    <div className="void-submission-main" style={voidMainStyle}>
      {"view_reason_for_voiding" !== documentSubmission?.[0]?.itemType && (
        <p className="void-submission-message" style={pStyle}>
          {t("MARK_VOID_SUBMISSION_MESSAGE")}
        </p>
      )}
      <LabelFieldPair className="case-label-field-pair">
        <CardLabel className="case-input-label">{`${t("REASON_FOR_NOT_CONSIDERATION")}`}</CardLabel>
        <div style={{ width: "100%", maxWidth: "960px" }}>
          <textarea
            value={voidReason}
            onChange={(e) => {
              let input = e.target.value;
              setVoidReason(input);
            }}
            rows={5}
            className="custom-textarea-style"
            placeholder={t("TYPE_HERE_PLACEHOLDER")}
            disabled={disabled}
          ></textarea>
          {errors?.voidReason && <CardLabelError> {t(errors?.voidReason?.message)} </CardLabelError>}
          {}
        </div>
      </LabelFieldPair>
    </div>
  );
};

export default VoidSubmissionBody;
