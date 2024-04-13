import React, { useMemo } from "react";
import { LabelFieldPair, CardLabel, TextInput, CardLabelError, LocationSearch } from "@egovernments/digit-ui-react-components";
import { useLocation } from "react-router-dom";

const SearchLocationAddress = ({ t, config, onSelect, formData = {}, userType, register, errors }) => {
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

  function validate(value, input) {
    setError(!input.populators.validation.pattern.test(value));
  }
  return (
    <div>
      {inputs?.map((input, index) => {
        let currentValue = (formData && formData[config.key] && formData[config.key][input.name]) || "";
        return (
          <React.Fragment key={index}>
            {errors[input.name] && <CardLabelError>{t(input.error)}</CardLabelError>}
            <LabelFieldPair>
              <CardLabel className="card-label-smaller">
                {t(input.label)}
                {input.isMandatory ? " * " : null}
              </CardLabel>
              <div className="field">
                {input?.type === "LocationSearch" ? (
                  <LocationSearch
                    onChange={(props) => {
                      console.log(props);
                      setValue(props);
                    }}
                  />
                ) : (
                  <TextInput
                    className="field desktop-w-full"
                    key={input.name}
                    value={formData && formData[config.key] ? formData[config.key][input.name] : undefined}
                    onChange={(e) => {
                      setValue(e.target.value, input.name, validate(e.target.value, input));
                    }}
                    disable={false}
                    defaultValue={undefined}
                    onBlur={(e) => validate(e.target.value, input)}
                    {...input.validation}
                  />
                )}
                {currentValue && currentValue.length > 0 && !currentValue.match(Digit.Utils.getPattern("Address")) && (
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

export default SearchLocationAddress;
