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
    for (const key in formData) {
      switch (key) {
        case "dateOfService":
          if (formData?.dateOfService && new Date(formData?.dateOfService).getTime() + 15 * 24 * 60 * 60 * 1000 > new Date().getTime()) {
            setServiceOfDemandNoticeModal(true);
            setError("dateOfService", { message: " CS_SERVICE_DATE_ERROR_MSG" });
            setValue("dateOfAccrual", "");
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
            new Date(caseDetails?.caseDetails?.["chequeDetails"]?.formdata?.[index]?.data?.depositDate).getTime()
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
      setValue("delayApplicationType", {
        code: "NO",
        name: "NO",
        showForm: false,
        isEnabled: false,
      });
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
      setValue("delayApplicationType", {
        code: "NO",
        name: "NO",
        showForm: true,
        isVerified: true,
        hasBarRegistrationNo: true,
        isEnabled: true,
      });
    } else if (
      caseDetails?.caseDetails?.["demandNoticeDetails"]?.formdata?.some(
        (data) => new Date(data?.data?.dateOfAccrual).getTime() + 30 * 24 * 60 * 60 * 1000 >= new Date().getTime()
      )
    ) {
      setValue("delayApplicationType", {
        code: "YES",
        name: "YES",
        showForm: false,
        isEnabled: true,
      });
    }
  }
};

export const showToastForComplainant = ({ formData, setValue, selected, setSuccessToast }) => {
  if (selected === "complaintDetails") {
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
    setValue("addressDetails", addressDet);
    setValue("addressDetails-select", addressDetSelect);
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
  if (selected === "complaintDetails" || selected === "witnessDetails") {
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

export const checkDuplicateMobileEmailValidation = ({ formData, setValue, selected, setError, clearErrors, formdata, index, caseDetails }) => {
  if (selected === "respondentDetails" || selected === "witnessDetails") {
    const respondentMobileNUmbers = formData?.phonenumbers?.textfieldValue;
    const complainantMobileNumber = caseDetails?.additionalDetails?.complaintDetails?.formdata?.[0]?.data?.complainantVerification?.mobileNumber;
    if (respondentMobileNUmbers && respondentMobileNUmbers && respondentMobileNUmbers === complainantMobileNumber) {
      setError("phonenumbers", { mobileNumber: "RESPONDENT_MOB_NUM_CAN_NOT_BE_SAME_AS_COMPLAINANT_MOB_NUM" });
    } else if (
      formdata &&
      formdata?.length > 1 &&
      formData?.phonenumbers?.textfieldValue &&
      formData?.phonenumbers?.textfieldValue?.length === 10 &&
      formdata?.some((data) => data?.data?.phonenumbers?.mobileNumber?.some((number) => number === formData?.phonenumbers?.textfieldValue))
    ) {
      setError("phonenumbers", { mobileNumber: "DUPLICATE_MOBILE_NUMBER" });
    } else {
      clearErrors("phonenumbers");
    }

    if (
      formdata &&
      formdata?.length > 1 &&
      formData?.emails?.textfieldValue &&
      formdata?.some((data) => data?.data?.emails?.emailId?.some((number) => number === formData?.emails?.textfieldValue))
    ) {
      setError("emails", { emailId: "DUPLICATE_EMAILS" });
    } else {
      clearErrors("emails");
    }
  }
  if (selected === "complaintDetails") {
    if (
      formdata &&
      formdata?.length > 1 &&
      formData?.complainantVerification?.mobileNumber &&
      formData?.complainantVerification?.mobileNumber?.length === 10 &&
      formdata?.some(
        (data, idx) => idx !== index && data?.data?.complainantVerification?.mobileNumber === formData?.complainantVerification?.mobileNumber
      )
    ) {
      setError("complainantVerification", { mobileNumber: "DUPLICATE_MOBILE_NUMBER" });
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
  } else if (selected == "debtLiabilityDetails") {
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

export const respondentValidation = ({ t, formData, selected, caseDetails, setShowErrorToast, toast }) => {
  if (selected === "respondentDetails") {
    const formDataCopy = structuredClone(formData);
    if ("inquiryAffidavitFileUpload" in formDataCopy) {
      if (
        formData?.addressDetails?.some(
          (address) =>
            address?.addressDetails?.pincode !== caseDetails?.additionalDetails?.["complaintDetails"]?.formdata?.[0]?.data?.addressDetails?.pincode
        ) &&
        !Object.keys(formData?.inquiryAffidavitFileUpload?.document || {}).length
      ) {
        setShowErrorToast(true);
        return true;
      } else {
        return false;
      }
    }
  } else {
    return false;
  }
};

export const demandNoticeFileValidation = ({ formData, selected, setShowErrorToast }) => {
  if (selected === "demandNoticeDetails") {
    const formDataCopy = structuredClone(formData);
    if ("SelectCustomDragDrop" in formDataCopy) {
      if (
        ["legalDemandNoticeFileUpload", "proofOfDispatchFileUpload"].some((data) => !Object.keys(formData?.SelectCustomDragDrop?.[data] || {}).length)
      ) {
        setShowErrorToast(true);
        return true;
      } else if (
        formData?.proofOfService?.code === "YES" &&
        ["proofOfAcknowledgmentFileUpload"].some((data) => !Object.keys(formData?.SelectCustomDragDrop?.[data] || {}).length)
      ) {
        setShowErrorToast(true);
        return true;
      } else {
        return false;
      }
    }
  } else {
    return false;
  }
};

export const chequeDetailFileValidation = ({ formData, selected, setShowErrorToast }) => {
  if (selected === "chequeDetails") {
    if (
      ["bouncedChequeFileUpload", "depositChequeFileUpload", "returnMemoFileUpload"].some(
        (data) => !Object.keys(formData?.[data]?.document || {}).length
      )
    ) {
      setShowErrorToast(true);
      return true;
    } else {
      return false;
    }
  } else {
    return false;
  }
};

export const advocateDetailsFileValidation = ({ formData, selected, setShowErrorToast }) => {
  if (selected === "advocateDetails") {
    if (
      formData?.isAdvocateRepresenting?.code === "YES" &&
      ["vakalatnamaFileUpload"].some((data) => !Object.keys(formData?.[data]?.document || {}).length)
    ) {
      setShowErrorToast(true);
      return true;
    } else {
      return false;
    }
  } else {
    return false;
  }
};

export const complainantValidation = ({ formData, t, caseDetails, selected, setShowErrorToast, toast }) => {
  if (selected === "complaintDetails") {
    const formDataCopy = structuredClone(formData);
    if (formData?.complainantType?.code === "REPRESENTATIVE" && "companyDetailsUpload" in formDataCopy) {
      if (!Object.keys(formData?.companyDetailsUpload?.document || {}).length) {
        setShowErrorToast(true);
        return true;
      }
    }
    if (!formData?.complainantId?.complainantId) {
      setShowErrorToast(true);
      return true;
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

export const signatureValidation = ({ formData, selected, setShowErrorToast, setErrorMsg }) => {
  if (selected === "addSignature") {
    if (
      !(
        formData?.advocatesignature &&
        Object.keys(formData?.advocatesignature)?.length > 0 &&
        formData?.litigentsignature &&
        Object.keys(formData?.litigentsignature)?.length > 0
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
      formData?.delayApplicationType?.code === "NO" &&
      (!formData?.condonationFileUpload || (formData?.condonationFileUpload && !formData?.condonationFileUpload?.document.length > 0))
    ) {
      toast.error(t("ES_COMMON_PLEASE_ENTER_ALL_MANDATORY_FIELDS"));
      return true;
    }
  } else {
    return false;
  }
};

export const prayerAndSwornValidation = ({ t, formData, selected, setShowErrorToast, setErrorMsg, toast }) => {
  if (selected === "prayerSwornStatement") {
    if (
      !Object.keys(formData?.memorandumOfComplaint)?.length > 0 ||
      !Object.keys(formData?.prayerForRelief)?.length > 0 ||
      (!("document" in formData?.memorandumOfComplaint) &&
        "text" in formData?.memorandumOfComplaint &&
        !formData?.memorandumOfComplaint?.text.length > 0) ||
      (!("text" in formData?.memorandumOfComplaint) &&
        "document" in formData?.memorandumOfComplaint &&
        !formData?.memorandumOfComplaint?.document.length > 0) ||
      (!("document" in formData?.prayerForRelief) && "text" in formData?.prayerForRelief && !formData?.prayerForRelief?.text.length > 0) ||
      (!("text" in formData?.prayerForRelief) && "document" in formData?.prayerForRelief && !formData?.prayerForRelief?.document.length > 0)
    ) {
      toast.error(t("ES_COMMON_PLEASE_ENTER_ALL_MANDATORY_FIELDS"));
      return true;
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
  const identifierType = documentData ? data?.complainantId?.complainantId?.complainantId?.selectIdTypeType?.code : "AADHAR";
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
  if (selected === "complaintDetails") {
    let litigants = [];
    const complainantVerification = {};
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
            if (data?.data?.complainantId?.complainantId && isCompleted === true) {
              if (data?.data?.complainantId?.verificationType !== "AADHAR") {
                const documentData = await onDocumentUpload(
                  data?.data?.complainantId?.complainantId?.complainantId?.ID_Proof?.[0]?.[1]?.file,
                  data?.data?.complainantId?.complainantId?.complainantId?.ID_Proof?.[0]?.[0],
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

    const newFormData = await Promise.all(
      formdata
        .filter((item) => item.isenabled)
        .map(async (data, index) => {
          let documentData = [];
          const idProof = {
            complainantId: { complainantId: { complainantId: {} } },
          };
          const individualDetails = {};
          if (data?.data?.complainantId?.complainantId?.complainantId?.ID_Proof?.[0]?.[1]?.file) {
            const uploadedData = await onDocumentUpload(
              data?.data?.complainantId?.complainantId?.complainantId?.ID_Proof?.[0]?.[1]?.file,
              data?.data?.complainantId?.complainantId?.complainantId?.ID_Proof?.[0]?.[0],
              tenantId
            );
            idProof.complainantId.complainantId.complainantId = {
              ID_Proof: [
                [
                  data?.data?.complainantId?.complainantId?.complainantId?.ID_Proof?.[0]?.[0],
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
              individualDetails: {
                ...data?.data?.complainantVerification?.individualDetails,
                individualDetails,
              },
              complainantVerification: {
                ...data?.data?.complainantVerification,
                ...complainantVerification[index],
                isUserVerified: !!data?.data?.complainantId?.complainantId && data?.data?.complainantVerification?.mobileNumber,
              },
              ...(data?.data?.complainantId?.complainantId?.complainantId?.ID_Proof?.[0]?.[1]?.file && idProof),
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
      complaintDetails: {
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
          let documentData = [];
          if (data?.data?.inquiryAffidavitFileUpload?.document) {
            documentData = await Promise.all(
              data?.data?.inquiryAffidavitFileUpload?.document?.map(async (document) => {
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
                document: documentData,
              },
            },
          };
        })
    );
    data.additionalDetails = {
      ...caseDetails.additionalDetails,
      respondentDetails: {
        formdata: newFormData,
        isCompleted: isCompleted === "PAGE_CHANGE" ? caseDetails.additionalDetails?.[selected]?.isCompleted : isCompleted,
      },
    };
  }
  if (selected === "chequeDetails") {
    const infoBoxData = { header: "CS_COMMON_NOTE", data: ["CS_CHEQUE_RETURNED_INSUFFICIENT_FUND"] };
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
    const debtDocumentData = { debtLiabilityFileUpload: {} };
    const newFormData = await Promise.all(
      formdata
        .filter((item) => item.isenabled)
        .map(async (data) => {
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
    data.additionalDetails = {
      ...caseDetails.additionalDetails,
      witnessDetails: {
        formdata: formdata.filter((item) => item.isenabled),
        isCompleted: isCompleted === "PAGE_CHANGE" ? caseDetails.additionalDetails?.[selected]?.isCompleted : isCompleted,
      },
    };
  }
  if (selected === "demandNoticeDetails") {
    const newFormData = await Promise.all(
      formdata
        .filter((item) => item.isenabled)
        .map(async (data) => {
          const documentData = {};
          if (
            data?.data?.SelectCustomDragDrop &&
            typeof data?.data?.SelectCustomDragDrop === "object" &&
            Object.keys(data?.data?.SelectCustomDragDrop).length > 0
          ) {
            documentData.SelectCustomDragDrop = await Object.keys(data?.data?.SelectCustomDragDrop).reduce(async (res, curr) => {
              const result = await res;
              result[curr] = await Promise.all(
                data?.data?.SelectCustomDragDrop?.[curr]?.map(async (document) => {
                  if (document) {
                    const uploadedData = await onDocumentUpload(document, document.name, tenantId);
                    return {
                      documentType: uploadedData.fileType || document?.documentType,
                      fileStore: uploadedData.file?.files?.[0]?.fileStoreId || document?.fileStore,
                      documentName: uploadedData.filename || document?.documentName,
                      fileName: pageConfig?.selectDocumentName?.[curr],
                    };
                  }
                })
              );
              return result;
            }, Promise.resolve({}));
          }
          return {
            ...data,
            data: {
              ...data.data,
              SelectCustomDragDrop: {
                ...data?.data?.SelectCustomDragDrop,
                ...documentData.SelectCustomDragDrop,
              },
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
          const documentData = { SelectUploadDocWithName: [], prayerForRelief: {}, memorandumOfComplaint: {} };
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
          if (
            data?.data?.SelectCustomDragDrop &&
            typeof data?.data?.SelectCustomDragDrop === "object" &&
            Object.keys(data?.data?.SelectCustomDragDrop).length > 0
          ) {
            documentData.SelectCustomDragDrop = await Object.keys(data?.data?.SelectCustomDragDrop).reduce(async (res, curr) => {
              const result = await res;
              result[curr] = await Promise.all(
                data?.data?.SelectCustomDragDrop?.[curr]?.map(async (document) => {
                  if (document) {
                    const uploadedData = await onDocumentUpload(document, document.name, tenantId);
                    return {
                      documentType: uploadedData.fileType || document?.documentType,
                      fileStore: uploadedData.file?.files?.[0]?.fileStoreId || document?.fileStore,
                      documentName: uploadedData.filename || document?.documentName,
                      fileName: pageConfig?.selectDocumentName?.[curr],
                    };
                  }
                })
              );
              return result;
            }, Promise.resolve({}));
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
              SelectCustomDragDrop: {
                ...data?.data?.SelectCustomDragDrop,
                ...documentData.SelectCustomDragDrop,
              },
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
                  barRegistrationNumber: item?.barRegistrationNumber,
                  advocateName: item?.advocateName,
                  advocateId: item?.advocateId,
                  barRegistrationNumberOriginal: data?.data?.advocateBarRegNumberWithName?.[0]?.barRegistrationNumberOriginal,
                };
              }),
              advocateName: data?.data?.advocateBarRegNumberWithName?.[0]?.advocateName,
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
  setErrorCaseDetails({
    ...caseDetails,
    litigants: !caseDetails?.litigants ? [] : caseDetails?.litigants,
    ...data,
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
