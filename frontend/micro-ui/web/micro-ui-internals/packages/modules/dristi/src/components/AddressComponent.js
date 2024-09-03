import React, { useMemo, useState } from "react";
import { CardLabel, TextInput, CardLabelError } from "@egovernments/digit-ui-react-components";
import LocationSearch from "./LocationSearch";
import { ReactComponent as SmallInfoIcon } from "../images/smallInfoIcon.svg";

import Axios from "axios";
const getLocation = (places, code) => {
  let location = null;
  location = places?.address_components?.find((place) => {
    return place.types.includes(code);
  })?.long_name;
  return location ? location : null;
};
const AddressComponent = ({ t, config, onSelect, formData = {}, errors }) => {
  const inputs = useMemo(
    () =>
      config?.populators?.inputs || [
        {
          label: "CS_LOCATION",
          type: "LocationSearch",
          name: [],
        },
      ],
    [config?.populators?.inputs]
  );
  const [coordinateData, setCoordinateData] = useState({ callbackFunc: () => {} });

  const getLatLngByPincode = async (pincode) => {
    const key = window?.globalConfigs?.getConfig("GMAPS_API_KEY");
    const response = await Axios.get(`https://maps.googleapis.com/maps/api/geocode/json?address=${pincode}&key=${key}`);
    return response;
  };
  function setValue(value, input) {
    if (input === "pincode" && value?.length === 6) {
      getLatLngByPincode(value)
        .then((res) => {
          if (
            (res.data.results && res.data.results?.length === 0) ||
            (res.data.status === "OK" && getLocation(res.data.results[0], "country") !== "India")
          ) {
            onSelect(config.key, {
              ...formData[config.key],
              ...["state", "district", "city", "locality", "coordinates", "pincode"].reduce((res, curr) => {
                res[curr] = "";
                if (curr === "pincode") {
                  res[curr] = value;
                }
                return res;
              }, {}),
            });
          } else {
            const [location] = res.data.results;
            onSelect(config.key, {
              ...formData[config.key],
              [input]: value,
              state: getLocation(location, "administrative_area_level_1") || "",
              district: getLocation(location, "administrative_area_level_3") || "",
              city: getLocation(location, "locality") || "",
              locality: (() => {
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
              coordinates: { latitude: location.geometry.location.lat, longitude: location.geometry.location.lng },
            });
            coordinateData.callbackFunc({ lat: location.geometry.location.lat, lng: location.geometry.location.lng });
          }
        })
        .catch(() => {
          onSelect(config.key, {
            ...formData[config.key],
            ...["state", "district", "city", "locality", "coordinates", "pincode"].reduce((res, curr) => {
              res[curr] = "";
              if (curr === "pincode") {
                res[curr] = value;
              }
              return res;
            }, {}),
          });
        });
      return;
    } else if (input === "pincode") {
      onSelect(config.key, {
        ...formData[config.key],
        ...["state", "district", "city", "locality", "coordinates", "pincode"].reduce((res, curr) => {
          res[curr] = "";
          if (curr === "pincode") {
            res[curr] = value;
          }
          return res;
        }, {}),
      });
      return;
    }
    if (Array.isArray(input)) {
      onSelect(config.key, {
        ...formData[config.key],
        ...input.reduce((res, curr) => {
          res[curr] = value[curr];
          return res;
        }, {}),
      });
    } else {
      if (value.startsWith(" ")) {
        value = "";
      }
      onSelect(config.key, { ...formData[config.key], [input]: value });
    }
  }
  const checkIfValidated = (currentValue, input) => {
    const isEmpty = /^\s*$/.test(currentValue);
    return isEmpty || !currentValue.match(window?.Digit.Utils.getPattern(input.validation.patternType) || input.validation.pattern);
  };

  return (
    <div>
      {inputs
        .filter((input) => input.type === "LocationSearch")
        .map((input, index) => {
          let isFirstRender = true;

          return (
            <div className="field" key={index} style={{ alignContent: "center" }}>
              <LocationSearch
                locationStyle={{ maxWidth: "900px" }}
                position={formData?.[config.key]?.coordinates || {}}
                setCoordinateData={setCoordinateData}
                onChange={(pincode, location, coordinates = {}) => {
                  setValue(
                    {
                      pincode: formData && isFirstRender && formData[config.key] ? formData[config.key]["pincode"] : pincode || "",
                      state:
                        formData && isFirstRender && formData[config.key]
                          ? formData[config.key]["state"]
                          : getLocation(location, "administrative_area_level_1") || "",
                      district:
                        formData && isFirstRender && formData[config.key]
                          ? formData[config.key]["district"]
                          : getLocation(location, "administrative_area_level_3") || "",
                      city:
                        formData && isFirstRender && formData[config.key] ? formData[config.key]["city"] : getLocation(location, "locality") || "",
                      locality:
                        isFirstRender && formData[config.key]
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
                      buildingName: formData && isFirstRender && formData[config.key] ? formData[config.key]["buildingName"] : "",
                      doorNo: formData && isFirstRender && formData[config.key] ? formData[config.key]["doorNo"] : "",
                    },
                    input.name
                  );
                  isFirstRender = false;
                }}
              />
            </div>
          );
        })}
      <div className="user-address-map-info">
        <SmallInfoIcon></SmallInfoIcon>
        <span>{t("MOVE_PIN_ON_MAP_MESSAGE")}</span>
      </div>
      <div className="address-card-input">
        {inputs
          .filter((input) => input.type !== "LocationSearch")
          .map((input, index) => {
            let currentValue = (formData && formData[config.key] && formData[config.key][input.name]) || "";
            return (
              <React.Fragment key={index}>
                {errors[input.name] && <CardLabelError>{t(input.error)}</CardLabelError>}
                <div className="field">
                  <CardLabel className="card-label-smaller">{t(input.label)}</CardLabel>
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
                  {currentValue &&
                    currentValue.length > 0 &&
                    input.validation &&
                    !currentValue.match(window?.Digit.Utils.getPattern(input.validation.patternType) || input.validation.pattern) && (
                      <CardLabelError>{t(input.validation?.errMsg || "CORE_COMMON_INVALID")}</CardLabelError>
                    )}
                </div>
              </React.Fragment>
            );
          })}
      </div>
    </div>
  );
};

export default AddressComponent;
