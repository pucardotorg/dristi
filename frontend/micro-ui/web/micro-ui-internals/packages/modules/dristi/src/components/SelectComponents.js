import React, { useMemo } from "react";
import { LabelFieldPair, CardLabel, TextInput, CardLabelError } from "@egovernments/digit-ui-react-components";
import LocationSearch from "./LocationSearch";

const getLocation = (places, code) => {
  let location = null;
  location = places?.address_components?.find((place) => {
    return place.types.includes(code);
  })?.long_name;
  return location ? location : null;
};
const SelectComponents = ({ t, config, onSelect, formData = {}, errors }) => {
  const inputs = useMemo(
    () =>
      config?.populators?.inputs || [
        {
          label: "CS_PIN_LOCATION",
          type: "LocationSearch",
          name: [],
        },
      ],
    [config?.populators?.inputs]
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
                    position={formData?.[config.key]?.coordinates || {}}
                    onChange={(pincode, location, coordinates = {}) => {
                      console.log(location);
                      setValue(
                        {
                          pincode: formData && formData[config.key] ? formData[config.key]["pincode"] : pincode || "",
                          state:
                            formData && formData[config.key]
                              ? formData[config.key]["state"]
                              : getLocation(location, "administrative_area_level_1") || "",
                          district:
                            formData && formData[config.key]
                              ? formData[config.key]["district"]
                              : getLocation(location, "administrative_area_level_3") || "",
                          city: formData && formData[config.key] ? formData[config.key]["city"] : getLocation(location, "locality") || "",
                          locality: formData[config.key]
                            ? formData[config.key]["locality"]
                            : (() => {
                                const plusCode = getLocation(location, "plus_code");
                                const neighborhood = getLocation(location, "neighborhood");
                                const sublocality_level_1 = getLocation(location, "sublocality_level_1");
                                const sublocality_level_2 = getLocation(location, "sublocality_level_2");
                                return [plusCode, neighborhood, sublocality_level_1, sublocality_level_2]
                                  .reduce((result, current) => {
                                    if (current) {
                                      result.push(current);
                                    }
                                    return result;
                                  }, [])
                                  .join(", ");
                              })(),
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
                  !currentValue.match(Digit.Utils.getPattern(input.validation.patternType) || input.validation.pattern) && (
                    <CardLabelError style={{ width: "100%", marginTop: "-15px", fontSize: "16px", marginBottom: "12px", color: "#FF0000" }}>
                      <span style={{ color: "#FF0000" }}> {t(input.validation?.errMsg || "CORE_COMMON_INVALID")}</span>
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
