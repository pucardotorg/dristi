import isEqual from "lodash/isEqual";
import React, { useEffect, useMemo, useState } from "react";
import { generateUUID } from "../Utils";
import { ReactComponent as CrossIcon } from "../images/cross.svg";
import Button from "./Button";
import LocationComponent from "./LocationComponent";
import { CaseWorkflowState } from "../Utils/caseWorkflow";

const selectCompMultiConfig = {
  type: "component",
  key: "addressDetails",
  withoutLabel: true,
  populators: {
    inputs: [
      { label: "CS_LOCATION", type: "LocationSearch", name: ["pincode", "state", "district", "city", "coordinates", "locality"] },
      {
        label: "PINCODE",
        type: "text",
        name: "pincode",
        validation: {
          minlength: 6,
          maxlength: 6,
          patternType: "Pincode",
          pattern: "[0-9]+",
          max: "9999999",
          errMsg: "ADDRESS_PINCODE_INVALID",
          isRequired: true,
          title: "",
        },
        isMandatory: true,
      },
      {
        label: "STATE",
        type: "text",
        name: "state",
        validation: {
          isRequired: true,
          pattern: /^[^{0-9}^\$\"<>?\\\\~!@#$%^()+={}\[\]*,/_:;“”‘’]{1,50}$/i,
          errMsg: "CORE_COMMON_APPLICANT_STATE_INVALID",
          patternType: "Name",
          title: "",
        },
        isMandatory: true,
      },
      {
        label: "DISTRICT",
        type: "text",
        name: "district",
        validation: {
          isRequired: true,
          pattern: /^[^{0-9}^\$\"<>?\\\\~!@#$%^()+={}\[\]*,/_:;“”‘’]{1,50}$/i,
          errMsg: "CORE_COMMON_APPLICANT_DISTRICT_INVALID",
          patternType: "Name",
          title: "",
        },
        isMandatory: true,
      },
      {
        label: "CITY/TOWN",
        type: "text",
        name: "city",
        validation: {
          isRequired: true,
          patternType: "Name",
          errMsg: "CORE_COMMON_APPLICANT_CITY_INVALID",
        },
        isMandatory: true,
      },
      {
        label: "ADDRESS",
        type: "text",
        name: "locality",
        validation: {
          isRequired: true,
          minlength: 2,
          maxlength: 256,
          pattern: /^[^\$\"<>?\\\\~`!@$%^()={}\[\]*:;“”‘’]{2,256}$/i,
          errMsg: "CORE_COMMON_APPLICANT_ADDRESS_INVALID",
        },
        isMandatory: true,
      },
    ],
    validation: {},
  },
};

const SelectComponentsMulti = ({ t, config, onSelect, formData, errors, setError, clearErrors }) => {
  const [locationData, setLocationData] = useState([formData?.[config?.key] ? formData?.[config?.key] : { id: generateUUID() }]);
  console.log("formData", formData, errors);

  useEffect(() => {
    if (
      Array.isArray(formData?.[config?.key]) &&
      locationData?.length === 1 &&
      !locationData?.[0]?.addressDetails &&
      !isEqual(locationData, formData?.[config?.key])
    ) {
      setLocationData(formData?.[config?.key]);
    } else {
      setLocationData(locationData);
    }
  }, [formData]);

  const addressLabel = useMemo(() => {
    return formData?.respondentType?.code;
  }, [formData?.respondentType]);

  const handleAdd = () => {
    setLocationData((locationData) => {
      const updatedLocationData = [...(locationData || []), { id: generateUUID() }];
      onSelect(config.key, updatedLocationData);
      return updatedLocationData;
    });
  };

  const handleDeleteLocation = (locationId) => {
    setLocationData((locationData) => {
      const currentFormData = locationData.filter((data) => data.id !== locationId);
      onSelect(config.key, currentFormData);
      return currentFormData;
    });
  };

  const onChange = (key, value, locationId) => {
    setLocationData((locationData) => {
      const locationsCopy = structuredClone(locationData);
      const updatedLocations = locationsCopy.map((data) => (data.id === locationId ? { ...data, addressDetails: value } : data));

      onSelect(config?.key, updatedLocations);
      return updatedLocations;
    });
  };

  return (
    <div>
      {Array.isArray(locationData) &&
        locationData?.[0]?.id &&
        locationData.map((data, index) => (
          <div key={data.id}>
            <div style={{ display: "flex", gap: "4px", justifyContent: "space-between", alignItems: "center" }}>
              <b>
                <h1>{` ${
                  addressLabel == "INDIVIDUAL"
                    ? t("CS_RESPONDENT_ADDRESS_DETAIL")
                    : addressLabel == "REPRESENTATIVE"
                    ? t("CS_COMPANY_LOCATION")
                    : config?.formType == "Witness"
                    ? t("CS_COMMON_ADDRESS_WITNESS")
                    : t("CS_COMMON_ADDRESS_DETAIL")
                } ${index + 1}`}</h1>
              </b>
              <span
                onClick={() => {
                  if (!config?.disable) {
                    handleDeleteLocation(data.id);
                  }
                }}
                style={locationData.length === 1 ? { display: "none" } : {}}
              >
                <CrossIcon></CrossIcon>
              </span>
            </div>
            <LocationComponent
              t={t}
              config={selectCompMultiConfig}
              locationFormData={data}
              onLocationSelect={(key, value) => {
                onChange(key, value, data.id);
              }}
              errors={errors}
              setError={setError}
              clearErrors={clearErrors}
              mapIndex={data.id}
              disable={config?.disable}
              isAutoFilledDisabled={true}
            ></LocationComponent>
          </div>
        ))}
      {!(config?.removeAddLocationButton === true) && (
        <Button
          isDisabled={config?.disable || (config?.state && config?.state !== CaseWorkflowState.DRAFT_IN_PROGRESS)}
          className={"add-location-btn"}
          label={"Add Location"}
          style={{ alignItems: "center", margin: "10px 0px" }}
          onButtonClick={() => {
            handleAdd();
          }}
        />
      )}
    </div>
  );
};

export default SelectComponentsMulti;
