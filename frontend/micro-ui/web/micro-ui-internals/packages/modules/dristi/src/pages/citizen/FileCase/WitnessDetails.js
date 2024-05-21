import React, { useState } from "react";
import { useTranslation } from "react-i18next";
import { FormComposerV2, Header, Toast } from "@egovernments/digit-ui-react-components";
import { useHistory } from "react-router-dom/cjs/react-router-dom.min";
import { witnessConfig } from "./Config/witnessConfig";

function WitnessDetails() {
  const [params, setParmas] = useState({});
  const [canAdd, setCanAdd] = useState(false);

  const Digit = window?.Digit || {};
  const { t } = useTranslation();
  const history = useHistory();
  const [showErrorToast, setShowErrorToast] = useState(false);
  const [isDisabled, setIsDisabled] = useState(false);

  const validateFormData = (data) => {
    let isValid = true;
    witnessConfig.forEach((curr) => {
      if (!isValid) return;
      if (!(curr.body[0].key in data) || !data[curr.body[0].key]) {
        isValid = false;
      }
      curr.body[0].populators.inputs.forEach((input) => {
        if (!isValid) return;
        if (Array.isArray(input.name)) return;
        if (
          input.isDependentOn &&
          data[curr.body[0].key][input.isDependentOn] &&
          !Boolean(
            input.dependentKey[input.isDependentOn].reduce((res, current) => {
              if (!res) return res;
              res = data[curr.body[0].key][input.isDependentOn][current];
              if (
                Array.isArray(data[curr.body[0].key][input.isDependentOn][current]) &&
                data[curr.body[0].key][input.isDependentOn][current].length === 0
              ) {
                res = false;
              }
              return res;
            }, true)
          )
        ) {
          return;
        }
        if (Array.isArray(data[curr.body[0].key][input.name]) && data[curr.body[0].key][input.name].length === 0) {
          isValid = false;
        }
        if (input?.isMandatory && !(input.name in data[curr.body[0].key])) {
          isValid = false;
        }
      });
    });
    return isValid;
  };

  const onSubmit = (data) => {
    if (!validateFormData(data)) {
      setShowErrorToast(!validateFormData(data));
      return;
    }
    setParmas({ ...params, registrationData: data });
    // history.push(`${path}/terms-conditions`);
  };

  const closeToast = () => {
    setShowErrorToast(false);
  };

  const onFormValueChange = (setValue, formData, formState) => {
    let isDisabled = false;
    witnessConfig.forEach((curr) => {
      if (isDisabled) return;
      if (!(curr.body[0].key in formData) || !formData[curr.body[0].key]) {
        return;
      }
      curr.body[0].populators.inputs.forEach((input) => {
        if (isDisabled) return;
        if (Array.isArray(input.name)) return;
        if (
          formData[curr.body[0].key][input.name] &&
          formData[curr.body[0].key][input.name].length > 0 &&
          !["documentUpload", "radioButton"].includes(input.type) &&
          input.validation &&
          !formData[curr.body[0].key][input.name].match(Digit.Utils.getPattern(input.validation.patternType) || input.validation.pattern)
        ) {
          isDisabled = true;
        }
        if (Array.isArray(formData[curr.body[0].key][input.name]) && formData[curr.body[0].key][input.name].length === 0) {
          isDisabled = true;
        }
      });
    });
    if (isDisabled) {
      setIsDisabled(isDisabled);
    } else {
      setIsDisabled(false);
    }
  };

  // const handleMobileChange = (event) => {
  //   const { value } = event.target;
  //   setParmas({ ...params, mobileNumber: value });
  //   const temp = params?.mobileNumbers || [];
  //   if (value.length !== 10 && canAdd) {
  //     setCanAdd(false);
  //   }
  //   if (value.length === 10 && !temp.includes(value)) {
  //     setCanAdd(true);
  //   }
  // };

  // const handleAddMobileNumber = (mobileNumber) => {
  //   const temp = params?.mobileNumbers || [];
  //   setParmas({ ...params, mobileNumbers: [...temp, mobileNumber], mobileNumber: "" });
  // };

  // const handleDeleteMobileNumber = (mobileNumber) => {
  //   setParmas({ ...params, mobileNumbers: params?.mobileNumbers.filter((mobile) => mobile !== mobileNumber) });
  // };

  return (
    <div>
      <div className="header-content">
        <Header>{`${t("CS_COMMON_WITNESS_DETAIL")} (optional)`}</Header>
      </div>
      <FormComposerV2
        label={t("CS_COMMONS_NEXT")}
        config={witnessConfig.map((config) => {
          return {
            ...config,
            body: config.body.filter((a) => !a.hideInEmployee),
          };
        })}
        onSubmit={(props) => {
          // onSubmit(props);
        }}
        defaultValues={params.registrationData || {}}
        onFormValueChange={onFormValueChange}
        cardStyle={{ minWidth: "100%" }}
        isDisabled={isDisabled}
      />
      {showErrorToast && <Toast error={true} label={t("ES_COMMON_PLEASE_ENTER_ALL_MANDATORY_FIELDS")} isDleteBtn={true} onClose={closeToast} />}
    </div>
  );
}

export default WitnessDetails;
