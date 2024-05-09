import React, { useState } from "react";
import { useTranslation } from "react-i18next";
import { FormComposerV2, Header, Toast } from "@egovernments/digit-ui-react-components";
import { useHistory } from "react-router-dom/cjs/react-router-dom.min";
import { respondentconfig } from "./respondentconfig";

function RespondentDetails({ path }) {
  const [params, setParmas] = useState({});
  const [canAdd, setCanAdd] = useState(false);

  const Digit = window?.Digit || {};
  const { t } = useTranslation();
  const history = useHistory();
  const [showErrorToast, setShowErrorToast] = useState(false);
  const [isDisabled, setIsDisabled] = useState(false);

  const validateFormData = (data) => {
    let isValid = true;
    respondentconfig.forEach((curr) => {
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
    history.push(`${path}/terms-conditions`);
  };

  const closeToast = () => {
    setShowErrorToast(false);
  };

  const onFormValueChange = (setValue, formData, formState) => {
    let isDisabled = false;
    respondentconfig.forEach((curr) => {
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

  const handleMobileChange = (event) => {
    const { value } = event.target;
    setParmas({ ...params, mobileNumber: value });
    const temp = params?.mobileNumbers || [];
    if (value.length !== 10 && canAdd) {
      setCanAdd(false);
    }
    if (value.length === 10 && !temp.includes(value)) {
      setCanAdd(true);
    }
  };

  const handleAddMobileNumber = (mobileNumber) => {
    const temp = params?.mobileNumbers || [];
    setParmas({ ...params, mobileNumbers: [...temp, mobileNumber], mobileNumber: "" });
  };

  const handleDeleteMobileNumber = (mobileNumber) => {
    setParmas({ ...params, mobileNumbers: params?.mobileNumbers.filter((mobile) => mobile !== mobileNumber) });
  };

  const config = {
    texts: {
      header: "CS_LOGIN_PROVIDE_MOBILE_NUMBER",
      cardText: "CS_LOGIN_TEXT",
      nextText: "CS_COMMONS_NEXT",
      submitBarLabel: "CS_COMMONS_NEXT",
    },
    inputs: [
      {
        label: "CORE_COMMON_PHONE_NUMBER",
        type: "text",
        name: "mobileNumber",
        error: "ERR_HRMS_INVALID_MOB_NO",
        validation: {
          required: true,
          minLength: 10,
          maxLength: 10,
          pattern: /^[6-9]\d{9}$/,
        },
      },
    ],
  };

  return (
    <div className="employee-card-wrapper">
      <div className="header-content">
        <Header>{t("CS_COMMON_RESPONDENT_DETAIL")}</Header>
      </div>
      <FormComposerV2
        label={t("CS_COMMONS_NEXT")}
        config={respondentconfig.map((config) => {
          return {
            ...config,
            body: config.body.filter((a) => !a.hideInEmployee),
          };
        })}
        onSubmit={(props) => {
          // onSubmit(props);
          console.debug("Vaibhav");
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

export default RespondentDetails;
