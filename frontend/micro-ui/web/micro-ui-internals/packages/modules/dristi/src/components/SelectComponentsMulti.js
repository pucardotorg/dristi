import React, { useState } from "react";
import { LabelFieldPair, CardLabel, TextInput, CardLabelError, Button } from "@egovernments/digit-ui-react-components";
import Axios from "axios";
import LocationComponent from "./LocationComponent";
import { ReactComponent as CrossIcon } from "../images/cross.svg";

function generateUUID() {
  return "xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx".replace(/[xy]/g, function (c) {
    const r = (Math.random() * 16) | 0;
    const v = c === "x" ? r : (r & 0x3) | 0x8;
    return v.toString(16);
  });
}

const selectCompMultiConfig = {
  type: "component",
  component: "SelectComponents",
  key: "addressDetails",
  withoutLabel: true,
  populators: {
    inputs: [
      { label: "CS_PIN_LOCATION", type: "LocationSearch", name: ["pincode", "state", "district", "city", "coordinates", "locality"] },
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
        },
        isMandatory: true,
      },
      //   {
      //     label: "LOCALITY",
      //     type: "text",
      //     name: "locality",
      //     validation: {
      //       isRequired: true,
      //     },
      //     isMandatory: true,
      //   },
      {
        label: "ADDRESS",
        type: "text",
        name: "doorNo",
        validation: {
          errMsg: "ADDRESS_DOOR_NO_INVALID",
          pattern: /^[^\$\"'<>?~`!@$%^={}\[\]*:;“”‘’]{2,50}$/i,
          isRequired: true,
          minlength: 2,
          title: "",
        },
        isMandatory: true,
      },
    ],
    validation: {},
  },
};

const SelectComponentsMulti = ({ t, config, onSelect, formData, errors }) => {
  const [locationData, setLocationData] = useState([{ id: generateUUID() }]);
  //   const locationData = new Proxy(_locationData, {
  //     set: (target, prop, value, receiver) => {
  //       // Passthrough to the target object
  //       console.trace({ prop, value });
  //       return Reflect.set(target, prop, value, receiver);
  //     },
  //   });
  const handleAdd = () => {
    setLocationData([...(locationData || []), { id: generateUUID() }]);
    onSelect(config.key, [...(locationData || []), { id: generateUUID() }]);
  };

  const handleDeleteLocation = (index) => {
    const currentFormData = structuredClone(locationData);
    currentFormData.splice(index, 1);
    setLocationData(currentFormData);
    onSelect(config.key, currentFormData);
  };

  const onChange = (key, value, index) => {
    // const currentFormData = new Proxy(structuredClone(_locationData), {
    //   set: (target, prop, value, receiver) => {
    //     // Passthrough to the target object
    //     console.trace({ prop, value });
    //     return Reflect.set(target, prop, value, receiver);
    //   },
    // });
    const currentFormData = structuredClone(locationData);
    const newCurr = Array.isArray(currentFormData) && currentFormData.map((val) => (val.id === index ? { ...val, addressDetails: value } : val));
    console.debug(Array.isArray(currentFormData));
    if (Array.isArray(currentFormData)) {
      onSelect(config?.key, newCurr);
      setLocationData(newCurr);
    } else {
      onSelect(config?.key, [{ addressDetails: value }]);
      setLocationData([{ addressDetails: value }]);
    }
  };

  console.log("loc", locationData);

  return (
    <div>
      {locationData.map((data, index) => (
        <div>
          <div style={{ display: "flex", justifyContent: "space-between" }}>
            <h1> {`WITNESS'S_LOCATION ${index}`}</h1>
            <span onClick={() => handleDeleteLocation(index)} style={locationData.length === 1 ? { display: "none" } : {}}>
              <CrossIcon></CrossIcon>
            </span>
          </div>
          <LocationComponent
            t={t}
            config={selectCompMultiConfig}
            locationFormData={structuredClone(data)}
            onLocationSelect={(key, value) => {
              onChange(key, value, data.id);
            }}
            errors={{}}
            key={`location-${data.addressDetails?.coordinates.latitude || 0}-${data.addressDetails?.coordinates.longitude}`}
            //   mapIndex={index}
            mapIndex={data.id}
          ></LocationComponent>
        </div>
      ))}
      {/* {!Array.isArray(locationData) && (
        <LocationComponent
          t={t}
          config={selectCompMultiConfig}
          locationFormData={formData?.[config.key]?.["addressDetails"] || {}}
          onLocationSelect={(key, value) => {
            onChange(key, value, 0);
          }}
          errors={{}}
        ></LocationComponent>
      )} */}
      {
        <Button
          label={"Add Location"}
          style={{ alignItems: "center" }}
          onButtonClick={() => {
            handleAdd();
          }}
        />
      }
    </div>
  );
};

export default SelectComponentsMulti;
