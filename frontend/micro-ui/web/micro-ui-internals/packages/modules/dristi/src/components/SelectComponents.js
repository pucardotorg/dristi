import React, { useMemo, useState } from "react";
import { LabelFieldPair, CardLabel, TextInput, CardLabelError, LocationSearch } from "@egovernments/digit-ui-react-components";
import { useLocation } from "react-router-dom";

const SelectComponents = ({ t, config, onSelect, formData = {}, errors }) => {
  const { pathname: url } = useLocation();
  const inputs = useMemo(
    () =>
      config?.populators?.inputs || [
        {
          label: "CS_PIN_LOCATION",
          type: "LocationSearch",
          name: "correspondenceCity",
        },
      ],
    []
  );

  function setValue(value, input) {
    onSelect(config.key, { ...formData[config.key], [input]: value });
  }

  return (
    <div>
      {inputs?.map((input, index) => {
        let currentValue = (formData && formData[config.key] && formData[config.key][input.name]) || "";
        return (
          <React.Fragment key={index}>
            {errors[input.name] && <CardLabelError>{t(input.error)}</CardLabelError>}
            <LabelFieldPair style={{ width: "100%", display: "flex" }}>
              <CardLabel className="card-label-smaller">
                {t(input.label)}
                {input.isMandatory ? <span style={{ color: "#FF0000" }}>{" * "}</span> : null}
              </CardLabel>
              <div className="field" style={{ width: "50%" }}>
                {input?.type === "LocationSearch" ? (
                  <LocationSearch
                    locationStyle={{ maxWidth: "540px" }}
                    onChange={(props) => {
                      setValue(props, input.name);
                    }}
                  />
                ) : (
                  <TextInput
                    className="field desktop-w-full"
                    key={input.name}
                    value={formData && formData[config.key] ? formData[config.key][input.name] : undefined}
                    onChange={(e) => {
                      setValue(e.target.value, input.name);
                    }}
                    disable={false}
                    defaultValue={undefined}
                    {...input.validation}
                  />
                )}
                {currentValue &&
                  currentValue.length > 0 &&
                  input.validation &&
                  !currentValue.match(Digit.Utils.getPattern(input.validation.patternType)) && (
                    <CardLabelError style={{ width: "100%", marginTop: "-15px", fontSize: "16px", marginBottom: "12px" }}>
                      {t("CORE_COMMON_APPLICANT_ADDRESS_INVALID")}
                    </CardLabelError>
                  )}
              </div>
            </LabelFieldPair>
          </React.Fragment>
        );
      })}
    </div>
  );
};

export default SelectComponents;
