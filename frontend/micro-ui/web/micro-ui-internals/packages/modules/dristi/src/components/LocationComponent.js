import React, { useCallback, useEffect, useMemo, useState } from "react";
import { LabelFieldPair, CardLabel, TextInput, CardLabelError, RadioButtons } from "@egovernments/digit-ui-react-components";
import LocationSearch, { defaultCoordinates } from "./LocationSearch";
import Axios from "axios";

const getLocation = (places, code) => {
  let location = null;
  location = places?.address_components?.find((place) => {
    return place.types.includes(code);
  })?.long_name;
  return location ? location : null;
};

const getLocality = (location) => {
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
};

const LocationComponent = ({
  t,
  config,
  onLocationSelect,
  locationFormData,
  errors,
  setError,
  clearErrors,
  mapIndex,
  disable = false,
  isAutoFilledDisabled = false,
}) => {
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

  const parseCoordinates = useCallback(() => {
    let coordinates = locationFormData?.[config.key]?.coordinates || {};
    coordinates = {
      latitude: parseFloat(coordinates?.latitude) || 0,
      longitude: parseFloat(coordinates?.longitude) || 0,
    };
    return coordinates;
  }, [locationFormData, config?.key]);

  const getFieldValue = useCallback(
    (isFirstRender, coordinates, field, defaultValue = "", location) => {
      const isDefaultCoordinates =
        parseFloat(coordinates?.latitude) === defaultCoordinates?.lat && parseFloat(coordinates?.longitude) === defaultCoordinates?.lng;
      if (!locationFormData.hasOwnProperty("addressDetails")) return "";

      // this check is to set error when user enters invalid pincode and save draft
      // and after visiting different page and comes to the same page-> error should be set.
      if (isDefaultCoordinates && locationFormData?.addressDetails?.pincode) {
        getLatLngByPincode(locationFormData?.addressDetails?.pincode)
          .then((res) => {
            if (
              (res.data.results && (res.data.results?.length === 0 || !res.data.results?.[0]?.hasOwnProperty("postcode_localities"))) ||
              (res.data.status === "OK" && getLocation(res.data.results[0], "country") !== "India")
            ) {
              setError("pincode", { message: "ADDRESS_PINCODE_INVALID" });
            }
          })
          .catch(() => {
            console.error("error in getting lng, lat from pincode");
          });
      }

      if (locationFormData?.[config?.key]) {
        return locationFormData[config?.key]?.[field];
      }
      return defaultValue;
    },
    [locationFormData, config?.key]
  );

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
            (res.data.results && (res.data.results?.length === 0 || !res.data.results?.[0]?.hasOwnProperty("postcode_localities"))) ||
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
            setError("pincode", { message: "ADDRESS_PINCODE_INVALID" });
          } else {
            const [location] = res.data.results;
            onLocationSelect(config.key, {
              ...locationFormData[config.key],
              [input]: value,
              state: getLocation(location, "administrative_area_level_1") || "",
              district: getLocation(location, "administrative_area_level_3") || "",
              city: getLocation(location, "locality") || "",
              locality: getLocality(location),
              coordinates: { latitude: location.geometry.location.lat, longitude: location.geometry.location.lng },
            });
            coordinateData.callbackFunc({ lat: location.geometry.location.lat, lng: location.geometry.location.lng });
            clearErrors("pincode");
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
      clearErrors("pincode");
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
            {/* {errors[input.name] && <CardLabelError>{t(input.error)}</CardLabelError>} */}
            <LabelFieldPair>
              <CardLabel className="card-label-smaller">
                {t(input.label)}
                <span>{input?.showOptional && ` ${t("CS_IS_OPTIONAL")}`}</span>
              </CardLabel>
              <div className="field">
                {input?.type === "LocationSearch" && mapIndex ? (
                  <LocationSearch
                    locationStyle={{}}
                    position={parseCoordinates()}
                    setCoordinateData={setCoordinateData}
                    index={mapIndex}
                    isAutoFilledDisabled={isAutoFilledDisabled}
                    onChange={(pincode, location, coordinates = {}) => {
                      setValue(
                        {
                          pincode: getFieldValue(isFirstRender, coordinates, "pincode", pincode || ""),
                          state: getFieldValue(isFirstRender, coordinates, "state", getLocation(location, "administrative_area_level_1") || ""),
                          district: getFieldValue(isFirstRender, coordinates, "district", getLocation(location, "administrative_area_level_3") || ""),
                          city: getFieldValue(isFirstRender, coordinates, "city", getLocation(location, "locality") || ""),
                          locality: getFieldValue(isFirstRender, coordinates, "locality", getLocality(location), location),
                          coordinates,
                        },
                        input.name
                      );
                      isFirstRender = false;
                    }}
                    disable={input.isDisabled || disable}
                  />
                ) : input?.type === "Radio" ? (
                  <RadioButtons
                    style={{ display: "flex", justifyContent: "flex-start", gap: "3rem", ...input.styles }}
                    selectedOption={currentValue}
                    options={input?.options}
                    optionsKey={"code"}
                    innerStyles={{ justifyContent: "start" }}
                    onSelect={(value) => {
                      setValue(value, input?.name);
                    }}
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
                  !currentValue.match(window?.Digit.Utils.getPattern(input.validation.patternType) || input.validation.pattern) &&
                  !errors.hasOwnProperty("pincode") && (
                    <CardLabelError style={{ width: "100%", marginTop: "-15px", fontSize: "16px", marginBottom: "12px", color: "#FF0000" }}>
                      <span style={{ color: "#FF0000" }}> {t(input.validation?.errMsg || "CORE_COMMON_INVALID")}</span>
                    </CardLabelError>
                  )}
                {errors?.pincode && input?.name === "pincode" && (
                  <CardLabelError>
                    <span style={{ color: "#ff0000" }}>{t(errors?.pincode?.message)}</span>
                  </CardLabelError>
                )}
                {errors[input?.name] && (
                  <CardLabelError>
                    <span style={{ color: "#ff0000" }}>{t(errors[input?.name]?.message)}</span>
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
