import { useToast } from "../../../components/Toast/useToast";
import { DRISTIService } from "../../../services";
import { userTypeOptions } from "../registration/config";
import { formatDate } from "./CaseType";

export const showDemandNoticeModal = ({
  selected,
  setValue,
  formData,
  setError,
  clearErrors,
  index,
  setServiceOfDemandNoticeModal,
  caseDetails,
  setReceiptDemandNoticeModal,
}) => {
  if (selected === "demandNoticeDetails") {
    const totalCheques = caseDetails?.caseDetails?.["chequeDetails"]?.formdata && caseDetails?.caseDetails?.["chequeDetails"]?.formdata.length;
    for (const key in formData) {
      switch (key) {
        case "dateOfService":
          if (formData?.dateOfService && new Date(formData?.dateOfService).getTime() + 15 * 24 * 60 * 60 * 1000 > new Date().getTime()) {
            setServiceOfDemandNoticeModal(true);
            setError("dateOfService", { message: " CS_SERVICE_DATE_ERROR_MSG" });
            setValue("dateOfAccrual", "");
          } else if (
            formData?.dateOfDispatch &&
            formData?.dateOfService &&
            new Date(formData?.dateOfService).getTime() < new Date(formData?.dateOfDispatch).getTime()
          ) {
            setError("dateOfService", { message: "CS_SERVICE_DATE_LEGAL_NOTICE_ERROR_MSG" });
          } else {
            clearErrors("dateOfService");
            let formattedDate = "";
            if (formData?.dateOfService) {
              const milliseconds = new Date(formData?.dateOfService).getTime() + 15 * 24 * 60 * 60 * 1000;
              const date = new Date(milliseconds);
              const year = date.getFullYear();
              const month = String(date.getMonth() + 1).padStart(2, "0");
              const day = String(date.getDate()).padStart(2, "0");
              formattedDate = `${year}-${month}-${day}`;
            }
            setValue("dateOfAccrual", formattedDate);
          }
          break;

        case "dateOfIssuance":
          if (new Date(formData?.dateOfIssuance).getTime() > new Date().getTime()) {
            setError("dateOfIssuance", { message: "CS_DATE_ERROR_MSG" });
          } else if (
            new Date(formData?.dateOfIssuance).getTime() <
            new Date(caseDetails?.caseDetails?.["chequeDetails"]?.formdata?.[totalCheques - 1]?.data?.depositDate).getTime()
          ) {
            setError("dateOfIssuance", { message: "CS_DATE_ISSUANCE_MSG_CHEQUE" });
          } else clearErrors("dateOfIssuance");
          break;

        case "dateOfDispatch":
          if (new Date(formData?.dateOfDispatch).getTime() > new Date().getTime()) {
            setError("dateOfDispatch", { message: "CS_DATE_ERROR_MSG" });
          } else if (
            formData?.dateOfDispatch &&
            formData?.dateOfIssuance &&
            new Date(formData?.dateOfIssuance).getTime() > new Date(formData?.dateOfDispatch).getTime()
          ) {
            setError("dateOfDispatch", { message: "CS_DISPATCH_DATE_ERROR_MSG" });
          } else {
            clearErrors("dateOfDispatch");
          }
          break;
        case "dateOfReply":
          if (
            formData?.dateOfService &&
            formData?.dateOfService &&
            new Date(formData?.dateOfReply).getTime() < new Date(formData?.dateOfService).getTime()
          ) {
            setError("dateOfReply", { message: "CS_REPLY_DATE_ERROR_MSG" });
          } else {
            clearErrors("dateOfReply");
          }
          break;
        case "delayApplicationType":
          if (formData?.delayApplicationType?.code === "NO") {
            setReceiptDemandNoticeModal(true);
            // setError("delayApplicationType", { message: " CS_DELAY_APPLICATION_TYPE_ERROR_MSG" });
          } else {
            clearErrors("delayApplicationType");
          }
          break;
        default:
          break;
      }
    }
  }
};

export const validateDateForDelayApplication = ({ selected, setValue, caseDetails, toast, t, history, caseId }) => {
  if (selected === "delayApplications") {
    if (
      !caseDetails?.caseDetails ||
      (caseDetails?.caseDetails && !caseDetails?.caseDetails?.["demandNoticeDetails"]?.formdata?.[0]?.data?.dateOfAccrual)
    ) {
      setValue("delayCondonationType", null);
      toast.error(t("SELECT_ACCRUAL_DATE_BEFORE_DELAY_APP"));
      setTimeout(() => {
        history.push(`?caseId=${caseId}&selected=demandNoticeDetails`);
      }, 3000);
    }
    if (
      caseDetails?.caseDetails?.["demandNoticeDetails"]?.formdata?.some(
        (data) => new Date(data?.data?.dateOfAccrual).getTime() + 30 * 24 * 60 * 60 * 1000 < new Date().getTime()
      )
    ) {
      setValue("delayCondonationType", {
        code: "NO",
        name: "NO",
        showForm: true,
        isEnabled: true,
      });
    } else if (
      caseDetails?.caseDetails?.["demandNoticeDetails"]?.formdata?.some(
        (data) => new Date(data?.data?.dateOfAccrual).getTime() + 30 * 24 * 60 * 60 * 1000 >= new Date().getTime()
      )
    ) {
      setValue("delayCondonationType", {
        code: "YES",
        name: "YES",
        showForm: false,
        isEnabled: true,
      });
    }
  }
};

export const showToastForComplainant = ({ formData, setValue, selected, setSuccessToast }) => {
  if (selected === "complainantDetails") {
    if (formData?.complainantId?.complainantId && formData?.complainantId?.verificationType && formData?.complainantId?.isFirstRender) {
      setValue("complainantId", { ...formData?.complainantId, isFirstRender: false });
      setSuccessToast((prev) => ({
        ...prev,
        showSuccessToast: true,
        successMsg: "CS_AADHAR_VERIFIED_SUCCESS_MSG",
      }));
    }
    const formDataCopy = structuredClone(formData);
    const addressDet = formDataCopy?.complainantVerification?.individualDetails?.addressDetails;
    const addressDetSelect = formDataCopy?.complainantVerification?.individualDetails?.["addressDetails-select"];
    if (!!addressDet && !!addressDetSelect) {
      setValue("addressDetails", addressDet);
      setValue("addressDetails-select", addressDetSelect);
    }
  }
};

export const checkIfscValidation = ({ formData, setValue, selected }) => {
  if (selected === "chequeDetails") {
    const formDataCopy = structuredClone(formData);
    for (const key in formDataCopy) {
      switch (key) {
        case "ifsc":
          if (Object.hasOwnProperty.call(formDataCopy, key)) {
            const oldValue = formDataCopy[key];
            let value = oldValue;

            if (typeof value === "string") {
              let updatedValue = value.toUpperCase().replace(/[^A-Z0-9]/g, "");
              if (updatedValue?.length > 11) {
                updatedValue = updatedValue.substring(0, 11);
              }

              if (updatedValue?.length < 5) {
                updatedValue = value.toUpperCase().replace(/[^A-Z]/g, "");
              }

              if (updatedValue?.length >= 5) {
                updatedValue = updatedValue.slice(0, 4).replace(/[^A-Z]/g, "") + "0" + updatedValue.slice(5);
              }

              if (updatedValue?.length === 11) {
                updatedValue = updatedValue.slice(0, 4) + "0" + updatedValue.slice(5, 11).replace(/[^A-Z0-9]/g, "");
              }

              if (updatedValue !== oldValue) {
                const element = document.querySelector(`[name="${key}"]`);
                const start = element?.selectionStart;
                const end = element?.selectionEnd;
                setValue(key, updatedValue);
                setTimeout(() => {
                  element?.setSelectionRange(start, end);
                }, 0);
              }
            }
          }
          break;
        case "chequeAmount":
          if (Object.hasOwnProperty.call(formDataCopy, key)) {
            const oldValue = formDataCopy[key];
            let value = oldValue;

            let updatedValue = value?.replace(/\D/g, "");
            if (updatedValue?.length > 12) {
              updatedValue = updatedValue.substring(0, 12);
            }

            if (updatedValue !== oldValue) {
              const element = document?.querySelector(`[name="${key}"]`);
              const start = element?.selectionStart;
              const end = element?.selectionEnd;
              setValue(key, updatedValue);
              setTimeout(() => {
                element?.setSelectionRange(start, end);
              }, 0);
            }
          }
          break;
        case "chequeNumber":
          if (Object.hasOwnProperty.call(formDataCopy, key)) {
            const oldValue = formDataCopy[key];
            let value = oldValue;

            let updatedValue = value?.replace(/\D/g, "");
            if (updatedValue?.length > 6) {
              updatedValue = updatedValue?.substring(0, 6);
            }
            if (updatedValue !== oldValue) {
              const element = document?.querySelector(`[name="${key}"]`);
              const start = element?.selectionStart;
              const end = element?.selectionEnd;
              setValue(key, updatedValue);
              setTimeout(() => {
                element?.setSelectionRange(start, end);
              }, 0);
            }
          }
          break;
        default:
          break;
      }
    }
  }
};

export const checkNameValidation = ({ formData, setValue, selected, reset, index, formdata }) => {
  if (selected === "respondentDetails") {
    if (formData?.respondentFirstName || formData?.respondentMiddleName || formData?.respondentLastName) {
      const formDataCopy = structuredClone(formData);
      for (const key in formDataCopy) {
        if (Object.hasOwnProperty.call(formDataCopy, key)) {
          const oldValue = formDataCopy[key];
          let value = oldValue;
          if (typeof value === "string") {
            if (value.length > 100) {
              value = value.slice(0, 100);
            }

            let updatedValue = value
              .replace(/[^a-zA-Z\s]/g, "")
              .trimStart()
              .replace(/ +/g, " ")
              .toLowerCase()
              .replace(/\b\w/g, (char) => char.toUpperCase());
            if (updatedValue !== oldValue) {
              const element = document.querySelector(`[name="${key}"]`);
              const start = element?.selectionStart;
              const end = element?.selectionEnd;
              setValue(key, updatedValue);
              setTimeout(() => {
                element?.setSelectionRange(start, end);
              }, 0);
            }
          }
        }
      }
    }
  }
  if (selected === "complainantDetails" || selected === "witnessDetails") {
    if (formData?.firstName || formData?.middleName || formData?.lastName) {
      const formDataCopy = structuredClone(formData);
      for (const key in formDataCopy) {
        if (Object.hasOwnProperty.call(formDataCopy, key)) {
          const oldValue = formDataCopy[key];
          let value = oldValue;
          if (typeof value === "string") {
            if (value.length > 100) {
              value = value.slice(0, 100);
            }

            let updatedValue = value
              .replace(/[^a-zA-Z\s]/g, "")
              .trimStart()
              .replace(/ +/g, " ")
              .toLowerCase()
              .replace(/\b\w/g, (char) => char.toUpperCase());
            if (updatedValue !== oldValue) {
              const element = document.querySelector(`[name="${key}"]`);
              const start = element?.selectionStart;
              const end = element?.selectionEnd;
              setValue(key, updatedValue);
              setTimeout(() => {
                element?.setSelectionRange(start, end);
              }, 0);
            }
          }
        }
      }
    }
  }
};

export const checkDuplicateMobileEmailValidation = ({
  formData,
  setValue,
  selected,
  setError,
  clearErrors,
  formdata,
  index,
  caseDetails,
  currentDisplayIndex,
}) => {
  const complainantMobileNumbersArray =
    caseDetails?.additionalDetails?.complainantDetails?.formdata
      .filter((data) => {
        if (data?.data?.complainantVerification?.mobileNumber) {
          return true;
        } else return false;
      })
      .map((data) => {
        return data?.data?.complainantVerification?.mobileNumber;
      }) || [];
  const respondentMobileNumbersArray =
    caseDetails?.additionalDetails?.respondentDetails?.formdata
      .filter((data) => {
        if (data?.data?.phonenumbers?.mobileNumber && data?.data?.phonenumbers?.mobileNumber.length !== 0) {
          return true;
        } else return false;
      })
      .map((data) => {
        return data?.data?.phonenumbers?.mobileNumber;
      })
      .reduce((acc, curr) => acc.concat(curr), []) || [];

  const witnessMobileNumbersArray =
    caseDetails?.additionalDetails?.witnessDetails?.formdata
      .filter((data) => {
        if (data?.data?.phonenumbers?.mobileNumber && data?.data?.phonenumbers?.mobileNumber.length !== 0) {
          return true;
        } else return false;
      })
      .map((data) => {
        return data?.data?.phonenumbers?.mobileNumber;
      })
      .reduce((acc, curr) => acc.concat(curr), []) || [];

  const respondentEmailsArray =
    caseDetails?.additionalDetails?.respondentDetails?.formdata
      .filter((data) => {
        if (data?.data?.emails?.emailId && data?.data?.emails?.emailId.length !== 0) {
          return true;
        } else return false;
      })
      .map((data) => {
        return data?.data?.emails?.emailId;
      })
      .reduce((acc, curr) => acc.concat(curr), []) || [];

  const witnessEmailsArray =
    caseDetails?.additionalDetails?.witnessDetails?.formdata
      .filter((data) => {
        if (data?.data?.emails?.emailId && data?.data?.emails?.emailId.length !== 0) {
          return true;
        } else return false;
      })
      .map((data) => {
        return data?.data?.emails?.emailId;
      })
      .reduce((acc, curr) => acc.concat(curr), []) || [];

  if (selected === "respondentDetails") {
    const currentMobileNumber = formData?.phonenumbers?.textfieldValue;
    if (currentMobileNumber && complainantMobileNumbersArray.some((number) => number === currentMobileNumber)) {
      setError("phonenumbers", { mobileNumber: "RESPONDENT_MOB_NUM_CAN_NOT_BE_SAME_AS_COMPLAINANT_MOB_NUM" });
    } else if (currentMobileNumber && witnessMobileNumbersArray.some((number) => number === currentMobileNumber)) {
      setError("phonenumbers", { mobileNumber: "RESPONDENT_MOB_NUM_CAN_NOT_BE_SAME_AS_WITNESS_MOB_NUM" });
    } else if (
      formdata &&
      formdata?.length > 0 &&
      formData?.phonenumbers?.textfieldValue &&
      formData?.phonenumbers?.textfieldValue?.length === 10 &&
      formdata
        .filter((data) => data.isenabled === true)
        ?.some((data) => data?.data?.phonenumbers?.mobileNumber?.some((number) => number === formData?.phonenumbers?.textfieldValue))
    ) {
      setError("phonenumbers", { mobileNumber: "DUPLICATE_MOBILE_NUMBER_FOR_RESPONDENT" });
    } else {
      clearErrors("phonenumbers");
    }

    const currentEmail = formData?.emails?.textfieldValue;
    if (currentEmail && witnessEmailsArray.some((email) => email === currentEmail)) {
      setError("emails", { emailId: "RESPONDENT_EMAIL_CAN_NOT_BE_SAME_AS_WITNESS_EMAIL" });
    } else if (
      formdata &&
      formdata?.length > 0 &&
      formData?.emails?.textfieldValue &&
      formdata
        .filter((data) => data.isenabled === true)
        ?.some((data) => data?.data?.emails?.emailId?.some((email) => email === formData?.emails?.textfieldValue))
    ) {
      setError("emails", { emailId: "DUPLICATE_EMAIL_ID_FOR_RESPONDENT" });
    } else {
      clearErrors("emails");
    }
  }
  if (selected === "witnessDetails") {
    const currentMobileNumber = formData?.phonenumbers?.textfieldValue;
    if (currentMobileNumber && complainantMobileNumbersArray.some((number) => number === currentMobileNumber)) {
      setError("phonenumbers", { mobileNumber: "WITNESS_MOB_NUM_CAN_NOT_BE_SAME_AS_COMPLAINANT_MOB_NUM" });
    } else if (currentMobileNumber && respondentMobileNumbersArray.some((number) => number === currentMobileNumber)) {
      setError("phonenumbers", { mobileNumber: "WITNESS_MOB_NUM_CAN_NOT_BE_SAME_AS_RESPONDENT_MOB_NUM" });
    } else if (
      formdata &&
      formdata?.length > 0 &&
      formData?.phonenumbers?.textfieldValue &&
      formData?.phonenumbers?.textfieldValue?.length === 10 &&
      formdata
        .filter((data) => data.isenabled === true)
        ?.some((data) => data?.data?.phonenumbers?.mobileNumber?.some((number) => number === formData?.phonenumbers?.textfieldValue))
    ) {
      setError("phonenumbers", { mobileNumber: "DUPLICATE_MOBILE_NUMBER_FOR_WITNESS" });
    } else {
      clearErrors("phonenumbers");
    }

    const currentEmail = formData?.emails?.textfieldValue;
    if (currentEmail && respondentEmailsArray.some((email) => email === currentEmail)) {
      setError("emails", { emailId: "WITNESS_EMAIL_CAN_NOT_BE_SAME_AS_RESPONDENT_EMAIL" });
    } else if (
      formdata &&
      formdata?.length > 0 &&
      formData?.emails?.textfieldValue &&
      formdata
        .filter((data) => data.isenabled === true)
        ?.some((data) => data?.data?.emails?.emailId?.some((email) => email === formData?.emails?.textfieldValue))
    ) {
      setError("emails", { emailId: "DUPLICATE_EMAIL_ID_FOR_WITNESS" });
    } else {
      clearErrors("emails");
    }
  }
  if (selected === "complainantDetails") {
    const currentMobileNumber = formData?.complainantVerification?.mobileNumber;

    if (currentMobileNumber && respondentMobileNumbersArray.some((number) => number === currentMobileNumber)) {
      setError("complainantVerification", { mobileNumber: "COMPLAINANT_MOB_NUM_CAN_NOT_BE_SAME_AS_RESPONDENT_MOB_NUM", isDuplicateNumber: true });
    } else if (currentMobileNumber && witnessMobileNumbersArray.some((number) => number === currentMobileNumber)) {
      setError("complainantVerification", { mobileNumber: "COMPLAINANT_MOB_NUM_CAN_NOT_BE_SAME_AS_WITNESS_MOB_NUM", isDuplicateNumber: true });
    } else if (
      formdata &&
      formdata?.length > 1 &&
      formData?.complainantVerification?.mobileNumber &&
      formData?.complainantVerification?.mobileNumber?.length === 10 &&
      formdata
        .filter((data) => data.isenabled === true)
        .filter((data) => data?.displayindex !== currentDisplayIndex)
        ?.some((data, idx) => idx !== index && data?.data?.complainantVerification?.mobileNumber === formData?.complainantVerification?.mobileNumber)
    ) {
      setError("complainantVerification", { mobileNumber: "DUPLICATE_MOBILE_NUMBER_FOR_COMPLAINANT", isDuplicateNumber: true });
    } else {
      clearErrors("complainantVerification");
    }
  }
};

export const checkOnlyCharInCheque = ({ formData, setValue, selected }) => {
  if (selected === "chequeDetails") {
    if (formData?.chequeSignatoryName || formData?.bankName || formData?.name) {
      const formDataCopy = structuredClone(formData);
      for (const key in formDataCopy) {
        if (Object.hasOwnProperty.call(formDataCopy, key)) {
          const oldValue = formDataCopy[key];
          let value = oldValue;
          if (key === "chequeSignatoryName" || key === "name") {
            if (typeof value === "string") {
              if (value.length > 100) {
                value = value.slice(0, 100);
              }

              let updatedValue = value
                .replace(/[^a-zA-Z\s]/g, "")
                .trimStart()
                .replace(/ +/g, " ")
                .toLowerCase()
                .replace(/\b\w/g, (char) => char.toUpperCase());
              if (updatedValue !== oldValue) {
                const element = document.querySelector(`[name="${key}"]`);
                const start = element?.selectionStart;
                const end = element?.selectionEnd;
                setValue(key, updatedValue);
                setTimeout(() => {
                  element?.setSelectionRange(start, end);
                }, 0);
              }
            }
          } else if (key === "bankName") {
            if (typeof value === "string") {
              if (value.length > 200) {
                value = value.slice(0, 200);
              }

              let updatedValue = value
                .replace(/[^a-zA-Z0-9 ]/g, "")
                .trimStart()
                .replace(/ +/g, " ")
                .toLowerCase()
                .replace(/\b\w/g, (char) => char.toUpperCase());
              if (updatedValue !== oldValue) {
                const element = document.querySelector(`[name="${key}"]`);
                const start = element?.selectionStart;
                const end = element?.selectionEnd;
                setValue(key, updatedValue);
                setTimeout(() => {
                  element?.setSelectionRange(start, end);
                }, 0);
              }
            }
          }
        }
      }
    }
  } else if (selected === "debtLiabilityDetails") {
    if (formData?.totalAmount) {
      const formDataCopy = structuredClone(formData);
      for (const key in formDataCopy) {
        if (Object.hasOwnProperty.call(formDataCopy, key) && key === "totalAmount") {
          const oldValue = formDataCopy[key];
          let value = oldValue;
          if (typeof value === "string") {
            if (value.length > 12) {
              value = value.slice(0, 12);
            }

            if (value !== oldValue) {
              const element = document.querySelector(`[name="${key}"]`);
              const start = element?.selectionStart;
              const end = element?.selectionEnd;
              setValue(key, value);
              setTimeout(() => {
                element?.setSelectionRange(start, end);
              }, 0);
            }
          }
        }
      }
    }
  }
};

export const respondentValidation = ({
  setErrorMsg,
  t,
  formData,
  selected,
  caseDetails,
  setShowErrorToast,
  toast,
  setFormErrors,
  clearFormDataErrors,
}) => {
  if (selected === "respondentDetails") {
    const formDataCopy = structuredClone(formData);
    if ("inquiryAffidavitFileUpload" in formDataCopy) {
      if (
        formData?.addressDetails?.some(
          (address) =>
            (address?.addressDetails?.pincode !==
              caseDetails?.additionalDetails?.["complainantDetails"]?.formdata?.[0]?.data?.addressDetails?.pincode &&
              caseDetails?.additionalDetails?.["complainantDetails"]?.formdata?.[0]?.data?.complainantType?.code === "INDIVIDUAL") ||
            (address?.addressDetails?.pincode !==
              caseDetails?.additionalDetails?.["complainantDetails"]?.formdata?.[0]?.data?.addressCompanyDetails?.pincode &&
              caseDetails?.additionalDetails?.["complainantDetails"]?.formdata?.[0]?.data?.complainantType?.code === "REPRESENTATIVE")
        ) &&
        !Object.keys(formData?.inquiryAffidavitFileUpload?.document || {}).length
      ) {
        setFormErrors("inquiryAffidavitFileUpload", { type: "required", msg: "" });
        setShowErrorToast(true);
        return true;
      }
    }

    if (
      formData?.respondentType?.code === "REPRESENTATIVE" &&
      ["companyDetailsUpload"].some((data) => !Object.keys(formData?.[data]?.document || {}).length)
    ) {
      setFormErrors("companyDetailsUpload", { type: "required" });
      setShowErrorToast(true);
      return true;
    } else {
      return false;
    }
  }

  const respondentMobileNUmbers = formData?.phonenumbers?.textfieldValue;
  const complainantMobileNumber = caseDetails?.additionalDetails?.complainantDetails?.formdata?.[0]?.data?.complainantVerification?.mobileNumber;
  if (
    formData &&
    formData?.phonenumbers?.textfieldValue &&
    formData?.phonenumbers?.textfieldValue?.length === 10 &&
    respondentMobileNUmbers &&
    respondentMobileNUmbers &&
    respondentMobileNUmbers === complainantMobileNumber
  ) {
    toast.error(t("RESPONDENT_MOB_NUM_CAN_NOT_BE_SAME_AS_COMPLAINANT_MOB_NUM"));
    setFormErrors("phonenumbers", { mobileNumber: "RESPONDENT_MOB_NUM_CAN_NOT_BE_SAME_AS_COMPLAINANT_MOB_NUM" });
    return true;
  }
  // else if (
  //   formData &&
  //   formData?.phonenumbers?.textfieldValue &&
  //   formData?.phonenumbers?.textfieldValue?.length === 10 &&
  //   formData?.phonenumbers?.mobileNumber?.some((number) => number === formData?.phonenumbers?.textfieldValue)
  // ) {
  //   toast.error("DUPLICATE_MOBILE_NUMBER");
  //   // setFormErrors("phonenumbers", { mobileNumber: "DUPLICATE_MOBILE_NUMBER" });
  //   return true;
  // }
  else {
    clearFormDataErrors("phonenumbers");
    return false;
  }
};

export const demandNoticeFileValidation = ({ formData, selected, setShowErrorToast, setFormErrors }) => {
  if (selected === "demandNoticeDetails") {
    for (const key of ["legalDemandNoticeFileUpload", "proofOfDispatchFileUpload"]) {
      if (!(key in formData) || formData[key].document?.length === 0) {
        setFormErrors(key, { type: "required" });
        setShowErrorToast(true);
        return true;
      }
    }

    if (formData?.proofOfService?.code === "YES" && formData?.["proofOfAcknowledgmentFileUpload"]?.document.length === 0) {
      setFormErrors("proofOfAcknowledgmentFileUpload", { type: "required" });
      setShowErrorToast(true);
      return true;
    }
    if (formData?.proofOfReply?.code === "YES" && formData?.["proofOfReplyFileUpload"]?.document.length === 0) {
      setFormErrors("proofOfReplyFileUpload", { type: "required" });
      setShowErrorToast(true);
      return true;
    }
  } else {
    return false;
  }
};

export const chequeDetailFileValidation = ({ formData, selected, setShowErrorToast, setFormErrors }) => {
  if (selected === "chequeDetails") {
    for (const key of ["bouncedChequeFileUpload", "depositChequeFileUpload", "returnMemoFileUpload"]) {
      if (!(key in formData) || formData[key]?.document?.length === 0 || !formData[key] || Object.keys(formData[key] || {}).length === 0) {
        setFormErrors(key, { type: "required" });
        setShowErrorToast(true);
        return true;
      }
    }
  } else {
    return false;
  }
};

export const advocateDetailsFileValidation = ({ formData, selected, setShowErrorToast, setFormErrors }) => {
  if (selected === "advocateDetails") {
    if (
      formData?.isAdvocateRepresenting?.code === "YES" &&
      ["vakalatnamaFileUpload"].some((data) => !Object.keys(formData?.[data]?.document || {}).length)
    ) {
      setFormErrors("vakalatnamaFileUpload", { type: "required" });
      setShowErrorToast(true);
      return true;
    } else {
      return false;
    }
  } else {
    return false;
  }
};

export const complainantValidation = ({ formData, t, caseDetails, selected, setShowErrorToast, toast, setFormErrors, clearFormDataErrors }) => {
  if (selected === "complainantDetails") {
    const formDataCopy = structuredClone(formData);
    if (formData?.complainantType?.code === "REPRESENTATIVE" && "companyDetailsUpload" in formDataCopy) {
      if (!Object.keys(formData?.companyDetailsUpload?.document || {}).length) {
        setFormErrors("companyDetailsUpload", { type: "required" });
        setShowErrorToast(true);
        return true;
      }
    }
    if (!formData?.complainantId?.complainantId) {
      setShowErrorToast(true);
      return true;
    }

    if (!formData?.complainantVerification?.mobileNumber || !formData?.complainantVerification?.otpNumber) {
      setShowErrorToast(true);
      setFormErrors("complainantVerification", { mobileNumber: "PLEASE_VERIFY_YOUR_PHONE_NUMBER" });
      return true;
    } else {
      clearFormDataErrors("complainantVerification");
    }
    const respondentData = caseDetails?.additionalDetails?.respondentDetails;
    const complainantMobileNumber = formData?.complainantVerification?.mobileNumber;
    if (respondentData) {
      const respondentMobileNumbers = respondentData?.formdata?.[0]?.data?.phonenumbers?.mobileNumber;
      if (respondentMobileNumbers && complainantMobileNumber) {
        for (let i = 0; i < respondentMobileNumbers.length; i++) {
          if (respondentMobileNumbers[i] === complainantMobileNumber) {
            toast.error(t("CHANGE_RESPONDENT_MOBILE_NUMBER_REGISTERED"));
            return true;
          }
        }
      }
    }
  } else {
    return false;
  }
};

export const signatureValidation = ({ formData, selected, setShowErrorToast, setErrorMsg, caseDetails }) => {
  if (selected === "addSignature") {
    let index = 0;
    if (
      !(
        Object.keys(formData || {}).length > 0 &&
        Object.keys(formData).reduce((res, curr) => {
          if (!res) return res;
          else {
            res = Boolean(
              caseDetails?.[curr]?.reduce((result, current) => {
                if (!result) return result;
                result = Boolean(formData?.[curr]?.[`${current?.name} ${index}`]);
                ++index;
                return result;
              }, true) &&
                formData[curr] &&
                Object.keys(formData[curr])?.length > 0
            );
            index = 0;
            return res;
          }
        }, true)
      )
    ) {
      setShowErrorToast(true);
      setErrorMsg("CS_PLEASE_ADD_SIGNATURE_BEFORE_SUBMIT");
      return true;
    }
  } else {
    setErrorMsg("");
    return false;
  }
};

export const chequeDateValidation = ({ selected, formData, setError, clearErrors }) => {
  if (selected === "chequeDetails") {
    for (const key in formData) {
      switch (key) {
        case "issuanceDate":
          if (new Date(formData?.issuanceDate).getTime() > new Date().getTime()) {
            setError("issuanceDate", { message: "CS_DATE_ERROR_MSG" });
          } else {
            clearErrors("issuanceDate");
          }
          break;
        case "depositDate":
          if (
            formData?.depositDate &&
            formData?.issuanceDate &&
            new Date(formData?.issuanceDate).getTime() > new Date(formData?.depositDate).getTime()
          ) {
            setError("depositDate", { message: "CS_DEPOSIT_DATE_ERROR_MSG" });
          } else if (selected === "chequeDetails" && new Date(formData?.depositDate).getTime() > new Date().getTime()) {
            setError("depositDate", { message: "CS_DATE_ERROR_MSG" });
          } else {
            clearErrors("depositDate");
          }
          break;
        default:
          break;
      }
    }
  }
};

export const delayApplicationValidation = ({ t, formData, selected, setShowErrorToast, setErrorMsg, toast }) => {
  if (selected === "delayApplications") {
    if (
      formData?.delayCondonationType?.code === "NO" &&
      (!formData?.condonationFileUpload || (formData?.condonationFileUpload && !formData?.condonationFileUpload?.document.length > 0))
    ) {
      toast.error(t("ES_COMMON_PLEASE_ENTER_ALL_MANDATORY_FIELDS"));
      return true;
    }
  } else {
    return false;
  }
};

export const prayerAndSwornValidation = ({ t, formData, selected, setShowErrorToast, setErrorMsg, toast, setFormErrors, clearFormDataErrors }) => {
  if (selected === "prayerSwornStatement") {
    let hasError = false;
    if (
      !Object.keys(formData?.memorandumOfComplaint)?.length > 0 ||
      !Object.keys(formData?.prayerForRelief)?.length > 0 ||
      (!("document" in formData?.memorandumOfComplaint) &&
        "text" in formData?.memorandumOfComplaint &&
        !formData?.memorandumOfComplaint?.text.length > 0) ||
      (!("text" in formData?.memorandumOfComplaint) &&
        "document" in formData?.memorandumOfComplaint &&
        !formData?.memorandumOfComplaint?.document.length > 0)
    ) {
      toast.error(t("ES_COMMON_PLEASE_ENTER_ALL_MANDATORY_FIELDS"));
      setFormErrors("memorandumOfComplaint", { type: "required" });
      hasError = true;
    }
    if (
      (!("document" in formData?.prayerForRelief) && "text" in formData?.prayerForRelief && !formData?.prayerForRelief?.text.length > 0) ||
      (!("text" in formData?.prayerForRelief) && "document" in formData?.prayerForRelief && !formData?.prayerForRelief?.document.length > 0)
    ) {
      toast.error(t("ES_COMMON_PLEASE_ENTER_ALL_MANDATORY_FIELDS"));
      setFormErrors("prayerForRelief", { type: "required" });
      hasError = true;
    }
    if ("SelectUploadDocWithName" in formData && Array.isArray(formData?.SelectUploadDocWithName)) {
      let index = 0;
      for (const key of formData?.SelectUploadDocWithName) {
        if (!key?.document || key.document?.length === 0) {
          setFormErrors("SelectUploadDocWithName", { message: "ES_COMMON_PLEASE_ENTER_ALL_MANDATORY_FIELDS", documentIndex: index });
          setShowErrorToast(true);
          hasError = true;
        } else {
          clearFormDataErrors("SelectUploadDocWithName");
        }
        index = index++;
      }
      return hasError;
    }
  } else {
    return false;
  }
};

export const createIndividualUser = async ({ data, documentData, tenantId }) => {
  const identifierId = documentData
    ? documentData?.fileStore
      ? documentData?.fileStore
      : documentData?.file?.files?.[0]?.fileStoreId
    : data?.complainantId?.complainantId;
  const identifierIdDetails = documentData
    ? {
        fileStoreId: identifierId,
        filename: documentData?.filename,
        documentType: documentData?.fileType,
      }
    : {};
  const identifierType = documentData ? data?.complainantId?.complainantId?.selectIdTypeType?.code : "AADHAR";
  let Individual = {
    Individual: {
      tenantId: tenantId,
      name: {
        givenName: data?.firstName,
        familyName: data?.lastName,
        otherNames: data?.middleName,
      },
      userDetails: {
        username: data?.complainantVerification?.userDetails?.userName,
        roles: [
          {
            code: "CITIZEN",
            name: "Citizen",
            tenantId: tenantId,
          },
          ...["CASE_CREATOR", "CASE_EDITOR", "CASE_VIEWER", "DEPOSITION_CREATOR", "DEPOSITION_EDITOR", "DEPOSITION_VIEWER"]?.map((role) => ({
            code: role,
            name: role,
            tenantId: tenantId,
          })),
        ],

        type: data?.complainantVerification?.userDetails?.type,
      },
      userUuid: data?.complainantVerification?.userDetails?.uuid,
      userId: data?.complainantVerification?.userDetails?.id,
      mobileNumber: data?.complainantVerification?.userDetails?.mobileNumber,
      address: [
        {
          tenantId: tenantId,
          type: "PERMANENT",
          latitude: data?.addressDetails?.coordinates?.latitude,
          longitude: data?.addressDetails?.coordinates?.longitude,
          city: data?.addressDetails?.city,
          pincode: data?.addressDetails?.pincode,
          addressLine1: data?.addressDetails?.state,
          addressLine2: data?.addressDetails?.district,
          street: data?.addressDetails?.locality,
        },
      ],
      identifiers: [
        {
          identifierType: identifierType,
          identifierId: identifierId,
        },
      ],
      isSystemUser: true,
      skills: [],
      additionalFields: {
        fields: [
          { key: "userType", value: userTypeOptions?.[0]?.code },
          { key: "userTypeDetail", value: JSON.stringify(userTypeOptions) },
          { key: "identifierIdDetails", value: JSON.stringify(identifierIdDetails) },
        ],
      },
      clientAuditDetails: {},
      auditDetails: {},
    },
  };
  const response = await window?.Digit.DRISTIService.postIndividualService(Individual, tenantId);
  return response;
};

const onDocumentUpload = async (fileData, filename, tenantId) => {
  if (fileData?.fileStore) return fileData;
  const fileUploadRes = await window?.Digit.UploadServices.Filestorage("DRISTI", fileData, tenantId);
  return { file: fileUploadRes?.data, fileType: fileData.type, filename };
};

export const updateCaseDetails = async ({
  isCompleted,
  setIsDisabled,
  tenantId,
  caseDetails,
  selected,
  formdata,
  pageConfig,
  setFormDataValue,
  action = "SAVE_DRAFT",
  setErrorCaseDetails = () => {},
}) => {
  const data = {};
  setIsDisabled(true);
  if (selected === "complainantDetails") {
    let litigants = [];
    const complainantVerification = {};
    if (isCompleted === true) {
      litigants = await Promise.all(
        formdata
          .filter((item) => item.isenabled)
          .map(async (data, index) => {
            if (data?.data?.complainantVerification?.individualDetails) {
              return {
                tenantId,
                caseId: caseDetails?.id,
                partyCategory: data?.data?.complainantType?.code,
                individualId: data?.data?.complainantVerification?.individualDetails?.individualId,
                partyType: index === 0 ? "complainant.primary" : "complainant.additional",
              };
            } else {
              if (data?.data?.complainantId?.complainantId && data?.data?.complainantVerification?.isUserVerified) {
                if (data?.data?.complainantId?.verificationType !== "AADHAR") {
                  const documentData = await onDocumentUpload(
                    data?.data?.complainantId?.complainantId?.ID_Proof?.[0]?.[1]?.file,
                    data?.data?.complainantId?.complainantId?.ID_Proof?.[0]?.[0],
                    tenantId
                  );
                  !!setFormDataValue &&
                    setFormDataValue("complainantVerification", {
                      individualDetails: {
                        document: [documentData],
                      },
                    });
                  const Individual = await createIndividualUser({ data: data?.data, documentData, tenantId });

                  const addressLine1 = Individual?.Individual?.address[0]?.addressLine1 || "Telangana";
                  const addressLine2 = Individual?.Individual?.address[0]?.addressLine2 || "Rangareddy";
                  const buildingName = Individual?.Individual?.address[0]?.buildingName || "";
                  const street = Individual?.Individual?.address[0]?.street || "";
                  const city = Individual?.Individual?.address[0]?.city || "";
                  const pincode = Individual?.Individual?.address[0]?.pincode || "";
                  const latitude = Individual?.Individual?.address[0]?.latitude || "";
                  const longitude = Individual?.Individual?.address[0]?.longitude || "";
                  const doorNo = Individual?.Individual?.address[0]?.doorNo || "";

                  const address = `${doorNo ? doorNo + "," : ""} ${buildingName ? buildingName + "," : ""} ${street}`.trim();

                  complainantVerification[index] = {
                    individualDetails: {
                      document: [documentData],
                      individualId: Individual?.Individual?.individualId,
                      "addressDetails-select": {
                        pincode: pincode,
                        district: addressLine2,
                        city: city,
                        state: addressLine1,
                        locality: address,
                      },
                      addressDetails: {
                        pincode: pincode,
                        district: addressLine2,
                        city: city,
                        state: addressLine1,
                        coordinates: {
                          longitude: latitude,
                          latitude: longitude,
                        },
                        locality: address,
                      },
                    },
                    userDetails: null,
                  };
                  return {
                    tenantId,
                    caseId: caseDetails?.id,
                    partyCategory: data?.data?.complainantType?.code,
                    individualId: Individual?.Individual?.individualId,
                    partyType: index === 0 ? "complainant.primary" : "complainant.additional",
                  };
                } else {
                  const Individual = await createIndividualUser({ data: data?.data, tenantId });

                  const addressLine1 = Individual?.Individual?.address[0]?.addressLine1 || "Telangana";
                  const addressLine2 = Individual?.Individual?.address[0]?.addressLine2 || "Rangareddy";
                  const buildingName = Individual?.Individual?.address[0]?.buildingName || "";
                  const street = Individual?.Individual?.address[0]?.street || "";
                  const city = Individual?.Individual?.address[0]?.city || "";
                  const pincode = Individual?.Individual?.address[0]?.pincode || "";
                  const latitude = Individual?.Individual?.address[0]?.latitude || "";
                  const longitude = Individual?.Individual?.address[0]?.longitude || "";
                  const doorNo = Individual?.Individual?.address[0]?.doorNo || "";

                  const address = `${doorNo ? doorNo + "," : ""} ${buildingName ? buildingName + "," : ""} ${street}`.trim();
                  complainantVerification[index] = {
                    individualDetails: {
                      document: null,
                      individualId: Individual?.Individual?.individualId,
                      "addressDetails-select": {
                        pincode: pincode,
                        district: addressLine2,
                        city: city,
                        state: addressLine1,
                        locality: address,
                      },
                      addressDetails: {
                        pincode: pincode,
                        district: addressLine2,
                        city: city,
                        state: addressLine1,
                        coordinates: {
                          longitude: latitude,
                          latitude: longitude,
                        },
                        locality: address,
                      },
                    },
                    userDetails: null,
                  };
                  return {
                    tenantId,
                    caseId: caseDetails?.id,
                    partyCategory: data?.data?.complainantType?.code,
                    individualId: Individual?.Individual?.individualId,
                    partyType: index === 0 ? "complainant.primary" : "complainant.additional",
                  };
                }
              }
              return {};
            }
          })
      );
    }

    const newFormData = await Promise.all(
      formdata
        .filter((item) => item.isenabled)
        .map(async (data, index) => {
          let documentData = [];
          const idProof = {
            complainantId: { complainantId: { complainantId: {} } },
          };
          const individualDetails = {};
          if (data?.data?.complainantId?.complainantId?.ID_Proof?.[0]?.[1]?.file) {
            const uploadedData = await onDocumentUpload(
              data?.data?.complainantId?.complainantId?.ID_Proof?.[0]?.[1]?.file,
              data?.data?.complainantId?.complainantId?.ID_Proof?.[0]?.[0],
              tenantId
            );
            idProof.complainantId.complainantId.complainantId = {
              ID_Proof: [
                [
                  data?.data?.complainantId?.complainantId?.ID_Proof?.[0]?.[0],
                  {
                    file: {
                      documentType: uploadedData.fileType || uploadedData?.documentType,
                      fileStore: uploadedData.file?.files?.[0]?.fileStoreId || uploadedData?.fileStore,
                      documentName: uploadedData.filename || uploadedData?.documentName,
                    },
                    fileStoreId: uploadedData?.file?.files?.[0]?.fileStoreId || uploadedData?.fileStore,
                  },
                ],
              ],
            };
            individualDetails.document = [uploadedData];
          }
          if (data?.data?.companyDetailsUpload?.document) {
            documentData = await Promise.all(
              data?.data?.companyDetailsUpload?.document?.map(async (document) => {
                if (document) {
                  const uploadedData = await onDocumentUpload(document, document.name, tenantId);
                  return {
                    documentType: uploadedData.fileType || document?.documentType,
                    fileStore: uploadedData.file?.files?.[0]?.fileStoreId || document?.fileStore,
                    documentName: uploadedData.filename || document?.documentName,
                    fileName: "Company documents",
                  };
                }
              })
            );
          }
          return {
            ...data,
            data: {
              ...data.data,
              companyDetailsUpload: {
                ...data?.data?.companyDetailsUpload,
                document: documentData,
              },
              complainantVerification: {
                ...data?.data?.complainantVerification,
                ...complainantVerification[index],
                isUserVerified: Boolean(data?.data?.complainantVerification?.mobileNumber && data?.data?.complainantVerification?.otpNumber),
              },
              ...(data?.data?.complainantId?.complainantId?.ID_Proof?.[0]?.[1]?.file && idProof),
            },
          };
        })
    );
    const representatives = (caseDetails?.representatives ? [...caseDetails?.representatives] : [])
      ?.filter((representative) => representative?.advocateId)
      .map((representative) => ({
        ...representative,
        caseId: caseDetails?.id,
        representing: representative?.advocateId ? [...litigants] : [],
      }));
    data.litigants = [...litigants].map((item, index) => ({
      ...(caseDetails.litigants?.[index] ? caseDetails.litigants?.[index] : {}),
      ...item,
    }));
    data.representatives = [...representatives];
    data.additionalDetails = {
      ...caseDetails.additionalDetails,
      complainantDetails: {
        formdata: newFormData,
        isCompleted: isCompleted === "PAGE_CHANGE" ? caseDetails.additionalDetails?.[selected]?.isCompleted : isCompleted,
      },
    };
  }
  if (selected === "respondentDetails") {
    const newFormData = await Promise.all(
      formdata
        .filter((item) => item.isenabled)
        .map(async (data) => {
          const documentData = {
            inquiryAffidavitFileUpload: [],
            companyDetailsUpload: [],
          };
          if (
            data?.data?.inquiryAffidavitFileUpload?.document &&
            Array.isArray(data?.data?.inquiryAffidavitFileUpload?.document) &&
            data?.data?.inquiryAffidavitFileUpload?.document.length > 0
          ) {
            documentData.inquiryAffidavitFileUpload = await Promise.all(
              data?.data?.inquiryAffidavitFileUpload?.document?.map(async (document) => {
                if (document) {
                  const uploadedData = await onDocumentUpload(document, document.name, tenantId);
                  return {
                    documentType: uploadedData.fileType || document?.documentType,
                    fileStore: uploadedData.file?.files?.[0]?.fileStoreId || document?.fileStore,
                    documentName: uploadedData.filename || document?.documentName,
                    fileName: "Affidavit documents",
                  };
                }
              })
            );
          }
          if (
            data?.data?.companyDetailsUpload?.document &&
            Array.isArray(data?.data?.companyDetailsUpload?.document) &&
            data?.data?.companyDetailsUpload?.document.length > 0
          ) {
            documentData.companyDetailsUpload = await Promise.all(
              data?.data?.companyDetailsUpload?.document?.map(async (document) => {
                if (document) {
                  const uploadedData = await onDocumentUpload(document, document.name, tenantId);
                  return {
                    documentType: uploadedData.fileType || document?.documentType,
                    fileStore: uploadedData.file?.files?.[0]?.fileStoreId || document?.fileStore,
                    documentName: uploadedData.filename || document?.documentName,
                    fileName: "Company documents",
                  };
                }
              })
            );
          }
          return {
            ...data,
            data: {
              ...data.data,
              inquiryAffidavitFileUpload: {
                ...data?.data?.inquiryAffidavitFileUpload,
                document: documentData.inquiryAffidavitFileUpload,
              },
              companyDetailsUpload: {
                ...data?.data?.companyDetailsUpload,
                document: documentData.companyDetailsUpload,
              },
            },
          };
        })
    );
    const newFormDataCopy = structuredClone(newFormData);
    for (let i = 0; i < newFormDataCopy.length; i++) {
      const obj = newFormDataCopy[i];
      if (obj?.data?.phonenumbers) {
        obj.data.phonenumbers.textfieldValue = "";
      }
      if (obj?.data?.emails) {
        obj.data.emails.textfieldValue = "";
      }
    }
    data.additionalDetails = {
      ...caseDetails.additionalDetails,
      respondentDetails: {
        formdata: newFormDataCopy,
        isCompleted: isCompleted === "PAGE_CHANGE" ? caseDetails.additionalDetails?.[selected]?.isCompleted : isCompleted,
      },
    };
  }
  if (selected === "chequeDetails") {
    const infoBoxData = { header: "CS_YOU_HAVE_CONFIRMED", scrutinyHeader: "CS_COMPLAINANT_HAVE_CONFIRMED", data: ["CS_CHEQUE_RETURNED_INSUFFICIENT_FUND"] };
    const newFormData = await Promise.all(
      formdata
        .filter((item) => item.isenabled)
        .map(async (data) => {
          const documentData = {
            bouncedChequeFileUpload: {},
            depositChequeFileUpload: {},
            returnMemoFileUpload: {},
          };
          if (data?.data?.bouncedChequeFileUpload?.document) {
            documentData.bouncedChequeFileUpload.document = await Promise.all(
              data?.data?.bouncedChequeFileUpload?.document?.map(async (document) => {
                if (document) {
                  const uploadedData = await onDocumentUpload(document, document.name, tenantId);
                  return {
                    documentType: uploadedData.fileType || document?.documentType,
                    fileStore: uploadedData.file?.files?.[0]?.fileStoreId || document?.fileStore,
                    documentName: uploadedData.filename || document?.documentName,
                    fileName: pageConfig?.selectDocumentName?.["bouncedChequeFileUpload"],
                  };
                }
              })
            );
          }
          if (data?.data?.depositChequeFileUpload?.document) {
            documentData.depositChequeFileUpload.document = await Promise.all(
              data?.data?.depositChequeFileUpload?.document?.map(async (document) => {
                if (document) {
                  const uploadedData = await onDocumentUpload(document, document.name, tenantId);
                  return {
                    documentType: uploadedData.fileType || document?.documentType,
                    fileStore: uploadedData.file?.files?.[0]?.fileStoreId || document?.fileStore,
                    documentName: uploadedData.filename || document?.documentName,
                    fileName: pageConfig?.selectDocumentName?.["depositChequeFileUpload"],
                  };
                }
              })
            );
          }
          if (data?.data?.returnMemoFileUpload?.document) {
            documentData.returnMemoFileUpload.document = await Promise.all(
              data?.data?.returnMemoFileUpload?.document?.map(async (document) => {
                if (document) {
                  const uploadedData = await onDocumentUpload(document, document.name, tenantId);
                  return {
                    documentType: uploadedData.fileType || document?.documentType,
                    fileStore: uploadedData.file?.files?.[0]?.fileStoreId || document?.fileStore,
                    documentName: uploadedData.filename || document?.documentName,
                    fileName: pageConfig?.selectDocumentName?.["returnMemoFileUpload"],
                  };
                }
              })
            );
          }

          if (
            data?.data?.depositDate &&
            data?.data?.issuanceDate &&
            new Date(data?.data?.issuanceDate).getTime() + 6 * 30 * 24 * 60 * 60 * 1000 > new Date(data?.data?.depositDate).getTime()
          ) {
            infoBoxData.data.splice(0, 0, "CS_SIX_MONTH_BEFORE_DEPOSIT_TEXT");
          }

          return {
            ...data,
            data: {
              ...data.data,
              ...documentData,
              infoBoxData,
            },
          };
        })
    );
    data.caseDetails = {
      ...caseDetails.caseDetails,
      chequeDetails: {
        formdata: newFormData,
        isCompleted: isCompleted === "PAGE_CHANGE" ? caseDetails.caseDetails?.[selected]?.isCompleted : isCompleted,
      },
    };
  }
  if (selected === "debtLiabilityDetails") {
    const newFormData = await Promise.all(
      formdata
        .filter((item) => item.isenabled)
        .map(async (data) => {
          const debtDocumentData = { debtLiabilityFileUpload: {} };
          if (data?.data?.debtLiabilityFileUpload?.document) {
            debtDocumentData.debtLiabilityFileUpload.document = await Promise.all(
              data?.data?.debtLiabilityFileUpload?.document?.map(async (document) => {
                if (document) {
                  const uploadedData = await onDocumentUpload(document, document.name, tenantId);
                  return {
                    documentType: uploadedData.fileType || document?.documentType,
                    fileStore: uploadedData.file?.files?.[0]?.fileStoreId || document?.fileStore,
                    documentName: uploadedData.filename || document?.documentName,
                    fileName: pageConfig?.selectDocumentName?.["debtLiabilityFileUpload"],
                  };
                }
              })
            );
          }
          return {
            ...data,
            data: {
              ...data.data,
              ...debtDocumentData,
            },
          };
        })
    );
    data.caseDetails = {
      ...caseDetails.caseDetails,
      debtLiabilityDetails: {
        formdata: newFormData,
        isCompleted: isCompleted === "PAGE_CHANGE" ? caseDetails.caseDetails?.[selected]?.isCompleted : isCompleted,
      },
    };
  }
  if (selected === "witnessDetails") {
    const newFormDataCopy = structuredClone(formdata.filter((item) => item.isenabled));
    for (let i = 0; i < newFormDataCopy.length; i++) {
      const obj = newFormDataCopy[i];
      if (obj?.data?.phonenumbers) {
        obj.data.phonenumbers.textfieldValue = "";
      }
      if (obj?.data?.emails) {
        obj.data.emails.textfieldValue = "";
      }
    }
    data.additionalDetails = {
      ...caseDetails.additionalDetails,
      witnessDetails: {
        formdata: newFormDataCopy,
        isCompleted: isCompleted === "PAGE_CHANGE" ? caseDetails.additionalDetails?.[selected]?.isCompleted : isCompleted,
      },
    };
  }
  if (selected === "demandNoticeDetails") {
    const newFormData = await Promise.all(
      formdata
        .filter((item) => item.isenabled)
        .map(async (data) => {
          const demandNoticeDocumentData = {
            legalDemandNoticeFileUpload: {},
            proofOfDispatchFileUpload: {},
            proofOfAcknowledgmentFileUpload: {},
            proofOfReplyFileUpload: {},
          };

          const fileUploadKeys = Object.keys(demandNoticeDocumentData).filter((key) => data?.data?.[key]?.document);

          await Promise.all(
            fileUploadKeys.map(async (key) => {
              if (data?.data?.[key]?.document) {
                demandNoticeDocumentData[key].document = await Promise.all(
                  data?.data?.[key]?.document?.map(async (document) => {
                    if (document) {
                      const uploadedData = await onDocumentUpload(document, document.name, tenantId);
                      return {
                        documentType: uploadedData.fileType || document?.documentType,
                        fileStore: uploadedData.file?.files?.[0]?.fileStoreId || document?.fileStore,
                        documentName: uploadedData.filename || document?.documentName,
                        fileName: pageConfig?.selectDocumentName?.[key],
                      };
                    }
                  })
                );
              }
            })
          ).catch(console.debug);
          return {
            ...data,
            data: {
              ...data.data,
              ...demandNoticeDocumentData,
            },
          };
        })
    );
    data.caseDetails = {
      ...caseDetails.caseDetails,
      demandNoticeDetails: {
        formdata: newFormData,
        isCompleted: isCompleted === "PAGE_CHANGE" ? caseDetails.caseDetails?.[selected]?.isCompleted : isCompleted,
      },
    };
  }

  if (selected === "delayApplications") {
    const newFormData = await Promise.all(
      formdata
        .filter((item) => item.isenabled)
        .map(async (data) => {
          const condonationDocumentData = { condonationFileUpload: {} };
          if (data?.data?.condonationFileUpload?.document) {
            condonationDocumentData.condonationFileUpload.document = await Promise.all(
              data?.data?.condonationFileUpload?.document?.map(async (document) => {
                if (document) {
                  const uploadedData = await onDocumentUpload(document, document.name, tenantId);
                  return {
                    documentType: uploadedData.fileType || document?.documentType,
                    fileStore: uploadedData.file?.files?.[0]?.fileStoreId || document?.fileStore,
                    documentName: uploadedData.filename || document?.documentName,
                    fileName: pageConfig?.selectDocumentName?.["condonationFileUpload"],
                  };
                }
              })
            );
          }
          return {
            ...data,
            data: {
              ...data.data,
              ...condonationDocumentData,
            },
          };
        })
    );
    data.caseDetails = {
      ...caseDetails.caseDetails,
      delayApplications: {
        formdata: newFormData,
        isCompleted: isCompleted === "PAGE_CHANGE" ? caseDetails.caseDetails?.[selected]?.isCompleted : isCompleted,
      },
    };
  }
  if (selected === "prayerSwornStatement") {
    const infoBoxData = { header: "", data: "" };
    const newFormData = await Promise.all(
      formdata
        .filter((item) => item.isenabled)
        .map(async (data) => {
          const documentData = { SelectUploadDocWithName: [], prayerForRelief: {}, memorandumOfComplaint: {}, swornStatement: {} };
          if (data?.data?.SelectUploadDocWithName) {
            documentData.SelectUploadDocWithName = await Promise.all(
              data?.data?.SelectUploadDocWithName?.map(async (docWithNameData) => {
                if (docWithNameData?.document?.[0] && !docWithNameData?.document?.[0]?.fileStore) {
                  const document = await onDocumentUpload(docWithNameData?.document[0], docWithNameData?.document[0]?.name, tenantId).then(
                    async (data) => {
                      const evidenceData = await DRISTIService.createEvidence({
                        artifact: {
                          artifactType: "complainant",
                          caseId: caseDetails?.id,
                          tenantId,
                          comments: [],
                          file: {
                            documentType: data.fileType || data?.documentType,
                            fileStore: data.file?.files?.[0]?.fileStoreId || data?.fileStore,
                            additionalDetails: {
                              name: docWithNameData?.docName,
                            },
                          },
                          workflow: {
                            action: "TYPE DEPOSITION",
                            documents: [
                              {
                                documentType: data.fileType,
                                fileName: data.fileName,
                                fileStoreId: data.file?.files?.[0]?.fileStoreId,
                              },
                            ],
                          },
                        },
                      });
                      return [
                        {
                          documentType: data.fileType || data?.documentType,
                          fileStore: data.file?.files?.[0]?.fileStoreId || data?.fileStore,
                          documentName: data.filename || data?.documentName,
                          artifactId: evidenceData?.artifact?.id,
                          fileName: docWithNameData?.document[0]?.name,
                        },
                      ];
                    }
                  );
                  return {
                    document: document,
                    docName: docWithNameData?.docName,
                  };
                } else {
                  return docWithNameData;
                }
              })
            );
          }
          if (data?.data?.swornStatement?.document) {
            documentData.swornStatement.document = await Promise.all(
              data?.data?.swornStatement?.document?.map(async (document) => {
                if (document) {
                  const uploadedData = await onDocumentUpload(document, document.name, tenantId);
                  return {
                    documentType: uploadedData.fileType || document?.documentType,
                    fileStore: uploadedData.file?.files?.[0]?.fileStoreId || document?.fileStore,
                    documentName: uploadedData.filename || document?.documentName,
                    fileName: pageConfig?.selectDocumentName?.["swornStatement"],
                  };
                }
              })
            );
          }
          if (data?.data?.memorandumOfComplaint?.document && data?.data?.memorandumOfComplaint?.document.length > 0) {
            documentData.memorandumOfComplaint.document = await Promise.all(
              data?.data?.memorandumOfComplaint?.document?.map(async (document) => {
                if (document) {
                  const uploadedData = await onDocumentUpload(document, document.name, tenantId);
                  return {
                    documentType: uploadedData.fileType || document?.documentType,
                    fileStore: uploadedData.file?.files?.[0]?.fileStoreId || document?.fileStore,
                    documentName: uploadedData.filename || document?.documentName,
                    fileName: pageConfig?.selectDocumentName?.["memorandumOfComplaint"],
                  };
                }
              })
            );
          } else if (data?.data?.memorandumOfComplaint?.text) {
            documentData.memorandumOfComplaint.text = data?.data?.memorandumOfComplaint?.text;
          }
          if (data?.data?.prayerForRelief?.document && data?.data?.prayerForRelief?.document.length > 0) {
            documentData.prayerForRelief.document = await Promise.all(
              data?.data?.prayerForRelief?.document?.map(async (document) => {
                if (document) {
                  const uploadedData = await onDocumentUpload(document, document.name, tenantId);
                  return {
                    documentType: uploadedData.fileType || document?.documentType,
                    fileStore: uploadedData.file?.files?.[0]?.fileStoreId || document?.fileStore,
                    documentName: uploadedData.filename || document?.documentName,
                    fileName: pageConfig?.selectDocumentName?.["prayerForRelief"],
                  };
                }
              })
            );
          } else if (data?.data?.prayerForRelief?.text) {
            documentData.prayerForRelief.text = data?.data?.prayerForRelief?.text;
          }

          if (["MAYBE", "YES"].includes(data?.data?.prayerAndSwornStatementType?.code)) {
            infoBoxData.header = "CS_RESOLVE_WITH_ADR";
            if (data?.data?.caseSettlementCondition?.text) {
              infoBoxData.data = data?.data?.caseSettlementCondition?.text;
            }
          }
          return {
            ...data,
            data: {
              ...data.data,
              ...documentData,
              infoBoxData,
            },
          };
        })
    );
    data.additionalDetails = {
      ...caseDetails.additionalDetails,
      prayerSwornStatement: {
        formdata: newFormData,
        isCompleted: isCompleted === "PAGE_CHANGE" ? caseDetails.additionalDetails?.[selected]?.isCompleted : isCompleted,
      },
    };
  }
  if (selected === "advocateDetails") {
    const newFormData = await Promise.all(
      formdata
        .filter((item) => item.isenabled)
        .map(async (data) => {
          const vakalatnamaDocumentData = { vakalatnamaFileUpload: {} };
          if (data?.data?.vakalatnamaFileUpload?.document) {
            vakalatnamaDocumentData.vakalatnamaFileUpload.document = await Promise.all(
              data?.data?.vakalatnamaFileUpload?.document?.map(async (document) => {
                if (document) {
                  const uploadedData = await onDocumentUpload(document, document.name, tenantId);
                  return {
                    documentType: uploadedData.fileType || document?.documentType,
                    fileStore: uploadedData.file?.files?.[0]?.fileStoreId || document?.fileStore,
                    documentName: uploadedData.filename || document?.documentName,
                    fileName: pageConfig?.selectDocumentName?.["vakalatnamaFileUpload"],
                  };
                }
              })
            );
          }
          return {
            ...data,
            data: {
              ...data.data,
              ...vakalatnamaDocumentData,
              advocateBarRegNumberWithName: data?.data?.advocateBarRegNumberWithName?.map((item) => {
                return {
                  ...item,
                  barRegistrationNumber: item?.barRegistrationNumber,
                  advocateName: item?.advocateName,
                  advocateId: item?.advocateId,
                  barRegistrationNumberOriginal: data?.data?.advocateBarRegNumberWithName?.[0]?.barRegistrationNumberOriginal,
                };
              }),
              advocateName: data?.data?.advocateBarRegNumberWithName?.[0]?.advocateName,
              advocateId: data?.data?.advocateBarRegNumberWithName?.[0]?.advocateId,
              barRegistrationNumber: data?.data?.advocateBarRegNumberWithName?.[0]?.barRegistrationNumber,
              barRegistrationNumberOriginal: data?.data?.advocateBarRegNumberWithName?.[0]?.barRegistrationNumberOriginal,
            },
          };
        })
    );
    let representatives = [];
    if (formdata?.filter((item) => item.isenabled).some((data) => data?.data?.isAdvocateRepresenting?.code === "YES")) {
      representatives = formdata
        .filter((item) => item.isenabled)
        .map((data, index) => {
          return {
            ...(caseDetails.representatives?.[index] ? caseDetails.representatives?.[index] : {}),
            caseId: caseDetails?.id,
            representing: data?.data?.advocateBarRegNumberWithName?.[0]?.advocateId
              ? [
                  ...(caseDetails?.litigants && Array.isArray(caseDetails?.litigants)
                    ? caseDetails?.litigants?.map((data, key) => ({
                        ...(caseDetails.representatives?.[index]?.representing?.[key]
                          ? caseDetails.representatives?.[index]?.representing?.[key]
                          : {}),
                        tenantId,
                        caseId: data?.caseId,
                        partyCategory: data?.partyCategory,
                        individualId: data?.individualId,
                        partyType: data?.partyType,
                      }))
                    : []),
                ]
              : [],
            advocateId: data?.data?.advocateBarRegNumberWithName?.[0]?.advocateId,
            tenantId,
          };
        });
    }
    data.representatives = [...representatives];
    data.additionalDetails = {
      ...caseDetails.additionalDetails,
      advocateDetails: {
        formdata: newFormData,
        isCompleted: isCompleted === "PAGE_CHANGE" ? caseDetails.additionalDetails?.[selected]?.isCompleted : isCompleted,
      },
    };
  }
  if (selected === "reviewCaseFile") {
    data.additionalDetails = {
      ...caseDetails.additionalDetails,
      reviewCaseFile: {
        formdata: formdata,
        isCompleted: isCompleted === "PAGE_CHANGE" ? caseDetails.caseDetails?.[selected]?.isCompleted : isCompleted,
      },
    };
  }
  const caseTitle =
    caseDetails?.caseTitle ||
    (caseDetails?.additionalDetails?.complainantDetails?.formdata?.[0]?.data?.firstName &&
      caseDetails?.additionalDetails?.respondentDetails?.formdata?.[0]?.data?.respondentFirstName &&
      `${caseDetails?.additionalDetails?.complainantDetails?.formdata?.[0]?.data?.firstName} ${
        caseDetails?.additionalDetails?.complainantDetails?.formdata?.[0]?.data?.lastName || ""
      } VS ${caseDetails?.additionalDetails?.respondentDetails?.formdata?.[0]?.data?.respondentFirstName} ${
        caseDetails?.additionalDetails?.respondentDetails?.formdata?.[0]?.data?.respondentLastName || ""
      }`);
  setErrorCaseDetails({
    ...caseDetails,
    litigants: !caseDetails?.litigants ? [] : caseDetails?.litigants,
    ...data,
    caseTitle,
    linkedCases: caseDetails?.linkedCases ? caseDetails?.linkedCases : [],
    filingDate: caseDetails.filingDate,
    workflow: {
      ...caseDetails?.workflow,
      action: action,
    },
  });
  return DRISTIService.caseUpdateService(
    {
      cases: {
        ...caseDetails,
        caseTitle,
        litigants: !caseDetails?.litigants ? [] : caseDetails?.litigants,
        ...data,
        linkedCases: caseDetails?.linkedCases ? caseDetails?.linkedCases : [],
        filingDate: formatDate(new Date()),
        workflow: {
          ...caseDetails?.workflow,
          action: action,
        },
      },
      tenantId,
    },
    tenantId
  );
};
