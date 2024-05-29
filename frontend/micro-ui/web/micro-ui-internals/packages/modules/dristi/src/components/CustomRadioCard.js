import React, { useMemo } from "react";
import { LabelFieldPair, CardLabelError, CardText, CardHeader } from "@egovernments/digit-ui-react-components";
import RadioButtons from "./RadioButton";

const CustomRadioCard = ({ t, config, onSelect, formData = {}, errors }) => {
  const Digit = window.Digit || {};
  const inputs = useMemo(() => config?.populators?.inputs, [config?.populators?.inputs]);

  function setValue(value, name, input) {
    onSelect(config.key, { ...formData[config.key], [name]: value });
  }
  return (
    <div>
      {inputs?.map((input, index) => {
        let currentValue = (formData && formData[config.key] && formData[config.key][input.name]) || "";
        const showDependentFields =
          Boolean(input.isDependentOn) && !Boolean(formData && formData[config.key])
            ? false
            : Boolean(formData && formData[config.key] && formData[config.key][input.isDependentOn])
            ? formData &&
              formData[config.key] &&
              Array.isArray(input.dependentKey[input.isDependentOn]) &&
              input.dependentKey[input.isDependentOn].reduce((res, curr) => {
                if (!res) return res;
                res = formData[config.key][input.isDependentOn][curr];
                return res;
              }, true)
            : true;
        return (
          <React.Fragment key={index}>
            {errors[input.name] && <CardLabelError>{t(input.error)}</CardLabelError>}

            {showDependentFields && (
              <LabelFieldPair style={{ width: "100%", display: "flex", flexDirection: "column", alignItems: "center" }}>
                <CardHeader style={{ fontSize: "30px" }} className="card-label-smaller">
                  {t(input.label)}
                </CardHeader>

                <CardText style={{ fontWeight: 200 }} className="card-label-smaller">
                  {t(input.subLabel)}
                </CardText>

                <div className="field" style={{ width: "50%" }}>
                  <RadioButtons
                    style={{ display: "flex", justifyContent: "flex-start", gap: "3rem", ...input.styles }}
                    options={input?.options || []}
                    key={input.name}
                    optionsKey={input?.optionsKey}
                    value={formData && formData[config.key] ? formData[config.key][input.name] : undefined}
                    onSelect={(e) => {
                      setValue(e, input.name, input);
                    }}
                    selectedOption={formData && formData[config.key] ? formData[config.key][input.name] : undefined}
                    defaultValue={formData && formData[config.key] ? formData[config.key][input.name] : undefined}
                    t={t}
                    errorStyle={errors?.[input.name]}
                  />

                  {currentValue &&
                    currentValue.length > 0 &&
                    !["documentUpload", "radioButton"].includes(input.type) &&
                    input.validation &&
                    !currentValue.match(Digit.Utils.getPattern(input.validation.patternType) || input.validation.pattern) && (
                      <CardLabelError style={{ width: "100%", marginTop: "-15px", fontSize: "16px", marginBottom: "12px" }}>
                        <span style={{ color: "#FF0000" }}> {t(input.validation?.errMsg || "CORE_COMMON_INVALID")}</span>
                      </CardLabelError>
                    )}
                </div>
              </LabelFieldPair>
            )}
          </React.Fragment>
        );
      })}
    </div>
  );
};

export default CustomRadioCard;
