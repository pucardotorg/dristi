import { FormComposerV2, Toast } from "@egovernments/digit-ui-react-components";
import React, { useRef, useState } from "react";
import { useTranslation } from "react-i18next";
import { useHistory } from "react-router-dom";
import { advocateClerkConfig } from "./config";

const headerStyle = {
  fontFamily: "Roboto",
  fontSize: "24px",
  fontWeight: 700,
  lineHeight: "30px",
  textAlign: "center",
  color: "#0b0c0c",
  margin: 0,
  width: "100%",
};

const subHeaderStyle = {
  margin: 0,
  fontFamily: "Roboto",
  fontSize: "14px",
  fontWeight: 400,
  lineHeight: "21px",
  textAlign: "center",
  color: "#505a5f",
};

function AdvocateClerkAdditionalDetail({ params, setParams, path, config, pathOnRefresh }) {
  const { t } = useTranslation();
  const Digit = window.Digit || {};
  const history = useHistory();
  const [showErrorToast, setShowErrorToast] = useState(false);
  const [isDisabled, setIsDisabled] = useState(false);
  const setFormErrors = useRef(null);

  const tenantId = Digit.ULBService.getCurrentTenantId();
  const closeToast = () => {
    setShowErrorToast(false);
  };

  const getUserForAdvocateUUID = async (barRegistrationNumber) => {
    const advocateDetail = await window?.Digit.DRISTIService.searchAdvocateClerk("/advocate/v1/_search", {
      criteria: [
        {
          barRegistrationNumber: barRegistrationNumber,
        },
      ],
      tenantId,
    });
    return advocateDetail;
  };

  const validateFormData = (data) => {
    let isValid = true;
    advocateClerkConfig.forEach((curr) => {
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
  const onFormValueChange = (setValue, formData, formState, reset, setError, clearErrors, trigger, getValues) => {
    setFormErrors.current = setError;
    const formDataCopy = structuredClone(formData);
    for (const key in formDataCopy) {
      if (Object.hasOwnProperty.call(formDataCopy, key) && key === "clientDetails") {
        if (typeof formDataCopy?.clientDetails?.barRegistrationNumber === "string") {
          const clientValue = formDataCopy.clientDetails;
          let oldValue = clientValue.barRegistrationNumber || "";
          let value = oldValue.toUpperCase();
          const updatedValue = value.replace(/[^A-Z0-9\/]/g, "");
          if (updatedValue !== oldValue) {
            clientValue.barRegistrationNumber = updatedValue;
            setValue(key, clientValue, { shouldValidate: true });
          }
        }
      }
    }

    let isDisabled = false;
    advocateClerkConfig.forEach((curr) => {
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
        if (input?.name == "barRegistrationNumber" && formData?.clientDetails?.barRegistrationNumber?.length < input?.validation?.minlength) {
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

  const onSubmit = async (formData) => {
    if (!validateFormData(formData)) {
      setShowErrorToast(!validateFormData(formData));
      return;
    }

    if (formData?.clientDetails?.barRegistrationNumber) {
      const advocateDetail = await getUserForAdvocateUUID(formData?.clientDetails?.barRegistrationNumber);
      if (advocateDetail?.advocates[0]?.responseList?.length !== 0) {
        setFormErrors.current("barRegistrationNumber", { message: t("DUPLICATE_BAR_REGISTRATION") });
        return;
      }
    }
    setParams({
      ...params,
      formData: formData,
    });
    if (!params?.Individual?.[0]?.individualId) history.push(`/digit-ui/citizen/dristi/home/registration/terms-condition`);
  };
  if (!params?.IndividualPayload) {
    history.push(pathOnRefresh);
  }
  return (
    <div className="advocate-additional-details">
      <div className="id-verificatin-header">
        <p className="vefifcation-header" style={headerStyle}>
          {t("CORE_ADVOCATE_VERFICATION")}
        </p>
        <p className="vefifcation-sub-header" style={subHeaderStyle}>
          {t("CORE_ADVOCATE_AUTHENTICITY_TEXT")}
        </p>
        <p className="vefifcation-sub-header" style={{ ...subHeaderStyle, paddingBottom: "40px" }}>
          {t("CORE_ADVOCATE_DETAILS_TEXT")}
        </p>
      </div>
      <FormComposerV2
        config={advocateClerkConfig}
        t={t}
        onSubmit={(props) => {
          onSubmit(props);
        }}
        isDisabled={isDisabled}
        label={"CS_COMMON_CONTINUE"}
        defaultValues={{ ...params?.registrationData } || {}}
        submitInForm
        onFormValueChange={onFormValueChange}
      ></FormComposerV2>

      {showErrorToast && <Toast error={true} label={t("ES_COMMON_PLEASE_ENTER_ALL_MANDATORY_FIELDS")} isDleteBtn={true} onClose={closeToast} />}
    </div>
  );
}

export default AdvocateClerkAdditionalDetail;
