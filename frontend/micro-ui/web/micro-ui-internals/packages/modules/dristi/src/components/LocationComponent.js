import React, { useEffect, useMemo, useState } from "react";
import { LabelFieldPair, CardLabel, TextInput, CardLabelError } from "@egovernments/digit-ui-react-components";
import LocationSearch from "./LocationSearch";
import Axios from "axios";

const getLocation = (places, code) => {
  let location = null;
  location = places?.address_components?.find((place) => {
    return place.types.includes(code);
  })?.long_name;
  return location ? location : null;
};
const LocationComponent = ({ t, config, onLocationSelect, locationFormData, errors, mapIndex, disable = false }) => {
  const [coordinateData, setCoordinateData] = useState({ callbackFunc: () => {} });
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

  const getLatLngByPincode = async (pincode) => {
    const response = await Axios.get(
      `https://maps.googleapis.com/maps/api/geocode/json?address=${pincode}&key=AIzaSyAASfCFja6YxwDzEAzhHFc8B-17TNTCV0g`
    );
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
            onLocationSelect(config.key, {
              ...locationFormData?.[config.key],
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
            onLocationSelect(config.key, {
              ...locationFormData[config.key],
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
          onLocationSelect(config.key, {
            ...locationFormData[config.key],
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
      onLocationSelect(config.key, {
        ...locationFormData[config.key],
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
      onLocationSelect(config.key, {
        ...locationFormData[config.key],
        ...input.reduce((res, curr) => {
          res[curr] = value[curr];
          return res;
        }, {}),
      });
    } else onLocationSelect(config.key, { ...locationFormData[config.key], [input]: value });
  }

  return (
    <div>
      {inputs?.map((input) => {
        let currentValue = (locationFormData && locationFormData[config.key] && locationFormData[config.key][input.name]) || "";
        let isFirstRender = true;
        return (
          <React.Fragment key={input.label}>
            {errors[input.name] && <CardLabelError>{t(input.error)}</CardLabelError>}
            <LabelFieldPair>
              <CardLabel className="card-label-smaller">
                {t(input.label)}
                <span>{input?.showOptional && ` ${t("CS_IS_OPTIONAL")}`}</span>
              </CardLabel>
              <div className="field">
                {input?.type === "LocationSearch" && mapIndex ? (
                  <LocationSearch
                    locationStyle={{}}
                    position={locationFormData?.[config.key]?.coordinates || {}}
                    setCoordinateData={setCoordinateData}
                    index={mapIndex}
                    onChange={(pincode, location, coordinates = {}) => {
                      setValue(
                        {
                          pincode:
                            locationFormData && isFirstRender && locationFormData?.[config.key]
                              ? locationFormData[config.key]["pincode"]
                              : pincode || "",
                          state:
                            locationFormData && isFirstRender && locationFormData?.[config.key]
                              ? locationFormData[config.key]["state"]
                              : getLocation(location, "administrative_area_level_1") || "",
                          district:
                            locationFormData && isFirstRender && locationFormData?.[config.key]
                              ? locationFormData[config.key]["district"]
                              : getLocation(location, "administrative_area_level_3") || "",
                          city:
                            locationFormData && isFirstRender && locationFormData?.[config.key]
                              ? locationFormData[config.key]["city"]
                              : getLocation(location, "locality") || "",
                          locality:
                            isFirstRender && locationFormData?.[config.key]
                              ? locationFormData[config.key]["locality"]
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
                      isFirstRender = false;
                    }}
                    disable={input.isDisabled || disable}
                  />
                ) : (
                  <TextInput
                    className="field desktop-w-full"
                    value={locationFormData && locationFormData[config.key] ? locationFormData[config.key][input.name] : undefined}
                    onChange={(e) => {
                      setValue(e.target.value, input.name);
                    }}
                    disable={input.isDisabled || disable}
                    defaultValue={undefined}
                    {...input.validation}
                  />
                )}
                {currentValue &&
                  currentValue.length > 0 &&
                  input.validation &&
                  !currentValue.match(window?.Digit.Utils.getPattern(input.validation.patternType) || input.validation.pattern) && (
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

export default LocationComponent;
