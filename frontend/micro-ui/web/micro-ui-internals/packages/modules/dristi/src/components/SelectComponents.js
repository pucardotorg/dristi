import React, { useMemo, useState } from "react";
import { LabelFieldPair, CardLabel, TextInput, CardLabelError, LocationSearch } from "@egovernments/digit-ui-react-components";
import { useLocation } from "react-router-dom";

const getLocation = (places, code) => {
  let location = null;
  location = places?.address_components?.find((place) => {
    return place.types.includes(code);
  })?.long_name;
  return location ? location : null;
};
const SelectComponents = ({ t, config, onSelect, formData = {}, errors }) => {
  const { pathname: url } = useLocation();
  const inputs = useMemo(
    () =>
      config?.populators?.inputs || [
        {
          label: "CS_PIN_LOCATION",
          type: "LocationSearch",
          name: [],
        },
      ],
    []
  );

  function setValue(value, input) {
    if (Array.isArray(input)) {
      onSelect(config.key, {
        ...formData[config.key],
        ...input.reduce((res, curr) => {
          res[curr] = value[curr];
          return res;
        }, {}),
      });
    } else onSelect(config.key, { ...formData[config.key], [input]: value });
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
                    onChange={(pincode, location, coordinates) => {
                      console.log(location);
                      setValue(
                        {
                          pincode,
                          state: getLocation(location, "administrative_area_level_1"),
                          district: getLocation(location, "administrative_area_level_3"),
                          city: getLocation(location, "locality"),
                          coordinates,
                        },
                        input.name
                      );
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
                    disable={input.isDisabled}
                    defaultValue={undefined}
                    {...input.validation}
                  />
                )}
                {currentValue &&
                  currentValue.length > 0 &&
                  input.validation &&
                  !currentValue.match(Digit.Utils.getPattern(input.validation.patternType)) && (
                    <CardLabelError style={{ width: "100%", marginTop: "-15px", fontSize: "16px", marginBottom: "12px", color: "#FF0000" }}>
                      <span style={{ color: "#FF0000" }}> {t(input.validation?.title || "CORE_COMMON_INVALID")}</span>
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
