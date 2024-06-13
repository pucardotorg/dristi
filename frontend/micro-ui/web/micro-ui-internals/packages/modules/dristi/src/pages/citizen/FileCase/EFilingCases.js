import React, { useCallback, useEffect, useMemo, useState } from "react";
import { useTranslation } from "react-i18next";
import { CloseSvg, FormComposerV2, Header, Loader, Toast, Button } from "@egovernments/digit-ui-react-components";
import { useHistory, useRouteMatch } from "react-router-dom/cjs/react-router-dom.min";
import { CustomAddIcon, CustomArrowDownIcon, CustomDeleteIcon } from "../../../icons/svgIndex";
import Accordion from "../../../components/Accordion";
import { sideMenuConfig } from "./Config";
import { ReactComponent as InfoIcon } from "../../../icons/info.svg";
import Modal from "../../../components/Modal";
import useSearchCaseService from "../../../hooks/dristi/useSearchCaseService";
import { DRISTIService } from "../../../services";
import EditFieldsModal from "./EditFieldsModal";
import ConfirmCourtModal from "../../../components/ConfirmCourtModal";
import { formatDate } from "./CaseType";
import { userTypeOptions } from "../registration/config";

const Heading = (props) => {
  return <h1 className="heading-m">{props.label}</h1>;
};

function EFilingCases({ path }) {
  const [params, setParmas] = useState({});
  const Digit = window?.Digit || {};
  const { t } = useTranslation();
  const history = useHistory();
  const [showErrorToast, setShowErrorToast] = useState(false);
  const [isDisabled, setIsDisabled] = useState(false);
  const [{ setFormErrors, resetFormData, setFormDataValue }, setState] = useState({
    setFormErrors: null,
    resetFormData: null,
    setFormDataValue: null,
  });
  const urlParams = new URLSearchParams(window.location.search);
  const selected = urlParams.get("selected") || sideMenuConfig?.[0]?.children?.[0]?.key;
  const caseId = urlParams.get("caseId");
  const [formdata, setFormdata] = useState(selected === "witnessDetails" ? [{}] : [{ isenabled: true, data: {}, displayindex: 0 }]);
  const tenantId = window?.Digit.ULBService.getCurrentTenantId();
  const [parentOpen, setParentOpen] = useState(sideMenuConfig.findIndex((parent) => parent.children.some((child) => child.key === selected)));

  const [openConfigurationModal, setOpenConfigurationModal] = useState(false);
  const [openConfirmCourtModal, setOpenConfirmCourtModal] = useState(false);
  const [receiptDemandNoticeModal, setReceiptDemandNoticeModal] = useState(false);
  const [serviceOfDemandNoticeModal, setServiceOfDemandNoticeModal] = useState(false);
  const [confirmDeleteModal, setConfirmDeleteModal] = useState(false);
  const [{ showSuccessToast, successMsg }, setSuccessToast] = useState({
    showSuccessToast: false,
    successMsg: "",
  });
  const [deleteFormIndex, setDeleteFormIndex] = useState(null);

  const { data: caseData, refetch: refetchCaseData, isLoading } = useSearchCaseService(
    {
      criteria: [
        {
          caseId: caseId,
        },
      ],
      tenantId,
    },
    {},
    "dristi",
    caseId,
    caseId
  );

  const getAllKeys = useMemo(() => {
    const keys = [];
    sideMenuConfig.forEach((parent) => {
      parent.children.forEach((child) => {
        keys.push(child.key);
      });
    });
    return keys;
  }, []);

  const deleteWarningText = useCallback((pageName) => {
    return (
      <div>
        <h3>{`This will permanently delete all the details entered for this ${pageName}. This action cannot be undone.`}</h3>
      </div>
    );
  }, []);

  const nextSelected = useMemo(() => {
    const index = getAllKeys.indexOf(selected);
    if (index !== -1 && index + 1 < getAllKeys.length) {
      return getAllKeys[index + 1];
    } else {
      return null;
    }
  }, [getAllKeys, selected]);

  const caseDetails = useMemo(
    () => ({
      ...caseData?.criteria?.[0]?.responseList?.[0],
    }),
    [caseData]
  );
  useEffect(() => {
    setParentOpen(sideMenuConfig.findIndex((parent) => parent.children.some((child) => child.key === selected)));
  }, [selected]);

  useEffect(() => {
    const data =
      caseDetails?.additionalDetails?.[selected]?.formdata ||
      caseDetails?.caseDetails?.[selected]?.formdata ||
      (selected === "witnessDetails" ? [{}] : [{ isenabled: true, data: {}, displayindex: 0 }]);
    setFormdata(data);
  }, [selected, caseDetails]);

  const accordion = useMemo(() => {
    return sideMenuConfig.map((parent, pIndex) => ({
      ...parent,
      isOpen: pIndex === parentOpen,
      children: parent.children.map((child, cIndex) => ({
        ...child,
        checked: child.key === selected,
        isCompleted: caseDetails?.additionalDetails?.[child.key]?.isCompleted || caseDetails?.caseDetails?.[child.key]?.isCompleted,
      })),
    }));
  }, [caseDetails, parentOpen, selected]);

  const pageConfig = useMemo(() => {
    return sideMenuConfig.find((parent) => parent.children.some((child) => child.key === selected))?.children?.find((child) => child.key === selected)
      ?.pageConfig;
  }, [selected]);

  const formConfig = useMemo(() => {
    return pageConfig?.formconfig;
  }, [pageConfig?.formconfig]);

  if (!getAllKeys.includes(selected) || !formConfig) {
    history.push(`?caseId=${caseId}&selected=${getAllKeys[0]}`);
  }

  const confirmModalConfig = useMemo(() => {
    return pageConfig?.confirmmodalconfig;
  }, [pageConfig?.confirmmodalconfig]);

  const CloseBtn = (props) => {
    return (
      <div onClick={props?.onClick} style={{ height: "100%", display: "flex", alignItems: "center", paddingRight: "20px", cursor: "pointer" }}>
        <CloseSvg />
      </div>
    );
  };

  const isDependentEnabled = useMemo(() => {
    let result = false;
    formConfig.forEach((config) => {
      if (config?.body && Array.isArray(config?.body)) {
        config?.body?.forEach((bodyItem) => {
          if (bodyItem?.populators?.isDependent) {
            result = true;
          }
        });
      }
    });
    return result;
  }, [formConfig]);

  const modifiedFormConfig = useMemo(() => {
    if (!isDependentEnabled) {
      return formdata.map(() => {
        if (selected === "reviewCaseFile") {
          return formConfig.map((config) => {
            return {
              ...config,
              body: config?.body?.map((body) => {
                return {
                  ...body,
                  populators: {
                    inputs: body?.populators?.inputs?.map((input) => {
                      return {
                        ...input,
                        data: caseDetails?.additionalDetails?.[input?.key]?.formdata || caseDetails?.caseDetails?.[input?.key]?.formdata || {},
                      };
                    }),
                  },
                };
              }),
            };
          });
        }
        if (selected === "addSignature") {
          return formConfig.map((config) => {
            return {
              ...config,
              body: config?.body?.map((body) => {
                return {
                  ...body,
                  populators: {
                    inputs: body?.populators?.inputs?.map((input) => {
                      return {
                        ...input,
                        data:
                          input.key === "advocateDetails"
                            ? [
                                {
                                  name:
                                    caseDetails?.additionalDetails?.[input.key]?.formdata?.[0]?.data?.advocateBarRegNumberWithName?.[0]?.advocateName,
                                },
                              ] || [{ name: "" }]
                            : caseDetails?.additionalDetails?.[input.key]?.formdata?.map((data) => ({
                                name: `${data?.data?.firstName || ""} ${data?.data?.middleName || ""} ${data?.data?.lastName || ""}`,
                              })),
                      };
                    }),
                  },
                };
              }),
            };
          });
        }
        return formConfig;
      });
    }

    return formdata.map(({ data }, index) => {
      let disableConfigFields = [];
      formConfig.forEach((config) => {
        config.body.forEach((body) => {
          if ("disableConfigFields" in body && "disableConfigKey" in body && "key" in body) {
            if (!!data?.[body.key]?.[body.disableConfigKey]) {
              disableConfigFields = [...disableConfigFields, ...body.disableConfigFields];
            }
          }
        });
      });
      return formConfig
        .filter((config) => {
          const dependentKeys = config?.dependentKey;
          if (!dependentKeys) {
            return config;
          }
          let show = true;
          for (const key in dependentKeys) {
            const nameArray = dependentKeys[key];
            for (const name of nameArray) {
              show = show && Boolean(data?.[key]?.[name]);
            }
          }
          return show && config;
        })
        .map((config) => {
          return {
            ...config,
            body: config?.body.map((body) => {
              if (body?.addUUID && body?.uuid !== index) {
                body.uuid = index;
                body.isUserVerified = disableConfigFields.some((field) => {
                  return field === body?.key;
                });
              }
              if (selected === "delayApplications") {
                if (
                  caseDetails?.caseDetails?.["demandNoticeDetails"]?.formdata?.some(
                    (data) => new Date(data?.data?.dateOfAccrual).getTime() + 30 * 24 * 60 * 60 * 1000 < new Date().getTime()
                  ) &&
                  body?.key === "delayApplicationType"
                ) {
                  body.disable = true;
                }
              }
              if ("inputs" in body?.populators && Array.isArray(body?.populators.inputs)) {
                return {
                  ...body,
                  populators: {
                    inputs: body?.populators.inputs.map((input) => {
                      if (
                        disableConfigFields.some((field) => {
                          if (Array.isArray(input?.name)) return field === input?.key;
                          return field === input?.name;
                        })
                      ) {
                        return {
                          ...input,
                          disable: input?.shouldBeEnabled ? false : true,
                          isDisabled: input?.shouldBeEnabled ? false : true,
                        };
                      }
                      return {
                        ...input,
                      };
                    }),
                  },
                };
              } else if ("populators" in body) {
                return {
                  ...body,
                  disable: disableConfigFields.some((field) => field === body?.populators?.name),
                };
              }
              return {
                ...body,
                disable: disableConfigFields.some((field) => field === body?.name),
              };
            }),
          };
        })
        .map((config) => {
          const { scrutiny } = caseDetails.additionalDetails || { scrutiny: {} };
          // const scrutiny = {
          //   complaintDetails: {
          //     scrutinyMessage: "",
          //     form: [
          //       {
          //         firstName: "Name does not match",
          //         lastname: "Doest not match",
          //       },
          //       {},
          //     ],
          //   },
          //   respondentDetails: {
          //     scrutinyMessage: "",
          //     form: [{}, {}],
          //   },
          // };
          const updatedBody = config.body
            .map((formComponent) => {
              const key = formComponent.key || formComponent.populators?.name;
              const modifiedFormComponent = structuredClone(formComponent);
              if (scrutiny?.[selected]) modifiedFormComponent.disable = true;
              if (scrutiny?.[selected] && key in scrutiny?.[selected]?.form?.[index]) {
                modifiedFormComponent.disable = false;
                modifiedFormComponent.withoutLabel = true;
                return [
                  {
                    type: "component",
                    component: "ScrutinyInfo",
                    key: "firstNameScrutiny",
                    populators: {
                      scrutinyMessage: scrutiny?.[selected].form[index][key],
                    },
                  },
                  modifiedFormComponent,
                ];
              }
              return modifiedFormComponent;
            })
            .flat();
          return {
            ...config,
            body: updatedBody,
          };
        });
    });
  }, [isDependentEnabled, formdata, selected, formConfig, caseDetails.additionalDetails, caseDetails?.caseDetails]);

  const activeForms = useMemo(() => {
    return formdata.filter((item) => item.isenabled === true).length;
  }, [formdata]);

  const handleAddForm = () => {
    setFormdata([...formdata, { isenabled: true, data: {}, displayindex: activeForms }]);
  };

  const handleDeleteForm = (index) => {
    const newArray = formdata.map((item, i) => ({
      ...item,
      isenabled: index === i ? false : item.isenabled,
      displayindex: i < index ? item.displayindex : i === index ? -Infinity : item.displayindex - 1,
    }));
    setConfirmDeleteModal(true);
    setFormdata(newArray);
  };

  const closeToast = () => {
    setShowErrorToast(false);
    setSuccessToast((prev) => ({
      ...prev,
      showSuccessToast: false,
      successMsg: "",
    }));
  };

  const chequeDateValidation = (formData, setError, clearErrors) => {
    if (selected === "chequeDetails") {
      for (const key in formData) {
        switch (key) {
          case "issuanceDate":
            if (new Date(formData?.issuanceDate).getTime() > new Date().getTime()) {
              setError("issuanceDate", { message: " CS_DATE_ERROR_MSG" });
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
              setError("depositDate", { message: " CS_DEPOSIT_DATE_ERROR_MSG" });
            } else if (selected === "chequeDetails" && new Date(formData?.depositDate).getTime() > new Date().getTime()) {
              setError("depositDate", { message: " CS_DATE_ERROR_MSG" });
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

  const showDemandNoticeModal = (setValue, formData, setError, clearErrors) => {
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
              const milliseconds = new Date(formData?.dateOfService).getTime() + 15 * 24 * 60 * 60 * 1000;
              const date = new Date(milliseconds);
              const year = date.getFullYear();
              const month = String(date.getMonth() + 1).padStart(2, "0");
              const day = String(date.getDate()).padStart(2, "0");
              const formattedDate = `${year}-${month}-${day}`;
              setValue("dateOfAccrual", formattedDate);
            }
            break;

          case "dateOfIssuance":
            if (new Date(formData?.dateOfIssuance).getTime() > new Date().getTime()) {
              setError("dateOfIssuance", { message: " CS_DATE_ERROR_MSG" });
            } else {
              clearErrors("dateOfIssuance");
            }
            break;

          case "dateOfDispatch":
            if (new Date(formData?.dateOfDispatch).getTime() > new Date().getTime()) {
              setError("dateOfDispatch", { message: " CS_DATE_ERROR_MSG" });
            } else if (
              formData?.dateOfDispatch &&
              formData?.dateOfIssuance &&
              new Date(formData?.dateOfIssuance).getTime() > new Date(formData?.dateOfDispatch).getTime()
            ) {
              setError("dateOfDispatch", { message: " CS_DISPATCH_DATE_ERROR_MSG" });
            } else {
              clearErrors("dateOfDispatch");
            }
            break;
          case "delayApplicationType":
            if (formData?.delayApplicationType?.code === "NO") {
              setReceiptDemandNoticeModal(true);
              setError("delayApplicationType", { message: " CS_DELAY_APPLICATION_TYPE_ERROR_MSG" });
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

  const validateDateForDelayApplication = (setValue) => {
    if (selected === "delayApplications") {
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

  const showToastForComplainant = (formData) => {
    if (selected === "complaintDetails") {
      if (formData?.complainantId?.complainantId && formData?.complainantId?.verificationType) {
        setSuccessToast((prev) => ({
          ...prev,
          showSuccessToast: true,
          successMsg: "CS_AADHAR_VERIFIED_SUCCESS_MSG",
        }));
      }
    }
  };
  const checkIfscValidation = (formData, setValue) => {
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
  const checkNameValidation = (formData, setValue) => {
    if (selected === "complaintDetails" || selected === "respondentDetails") {
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
  const onFormValueChange = (setValue, formData, formState, reset, setError, clearErrors, trigger, getValues, index) => {
    checkIfscValidation(formData, setValue);
    checkNameValidation(formData, setValue);
    if (JSON.stringify(formData) !== JSON.stringify(formdata[index].data)) {
      chequeDateValidation(formData, setError, clearErrors);
      showDemandNoticeModal(setValue, formData, setError, clearErrors);
      validateDateForDelayApplication(setValue);
      showToastForComplainant(formData);
      setFormdata(
        formdata.map((item, i) => {
          setValue();
          return i === index
            ? {
                ...item,
                data: formData,
              }
            : item;
        })
      );
    }
    if (!setFormErrors) {
      setState((prev) => ({
        ...prev,
        setFormErrors: setError,
        resetFormData: reset,
        setFormDataValue: setValue,
      }));
    }
    if (formState?.submitCount && !Object.keys(formState?.errors).length && formState?.isSubmitSuccessful) {
      setIsDisabled(true);
    }
  };

  const handleAccordionClick = (index) => {
    setParentOpen((prevParentOpen) => (prevParentOpen === index ? -1 : index));
  };
  const onDocumentUpload = async (fileData, filename) => {
    if (fileData?.fileStore) return fileData;
    const fileUploadRes = await Digit.UploadServices.Filestorage("DRISTI", fileData, tenantId);
    return { file: fileUploadRes?.data, fileType: fileData.type, filename };
  };

  const createIndividualUser = async (data, documentData) => {
    const identifierId = documentData ? documentData?.filedata?.files?.[0]?.fileStoreId : data?.complainantId?.complainantId;
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
    const response = await Digit.DRISTIService.postIndividualService(Individual, tenantId);
    return response;
  };

  const updateCaseDetails = async (isCompleted, key) => {
    const data = {};
    if (selected === "complaintDetails") {
      const litigants = await Promise.all(
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
              if (data?.data?.complainantId?.complainantId) {
                if (data?.data?.complainantId?.verificationType !== "AADHAR") {
                  const documentData = await onDocumentUpload(
                    data?.data?.complainantId?.complainantId?.complainantId?.ID_Proof?.[0]?.[1]?.file,
                    data?.data?.complainantId?.complainantId?.complainantId?.ID_Proof?.[0]?.[0]
                  );
                  const Individual = await createIndividualUser(data?.data, documentData);
                  return {
                    tenantId,
                    caseId: caseDetails?.id,
                    partyCategory: data?.data?.complainantType?.code,
                    individualId: Individual?.Individual?.individualId,
                    partyType: index === 0 ? "complainant.primary" : "complainant.additional",
                  };
                } else {
                  const Individual = await createIndividualUser(data?.data);
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
          .map(async (data) => {
            let documentData = [];
            if (data?.data?.companyDetailsUpload?.document) {
              documentData = await Promise.all(
                data?.data?.companyDetailsUpload?.document?.map(async (document) => {
                  if (document) {
                    const uploadedData = await onDocumentUpload(document, document.name);
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
                },
              },
            };
          })
      );
      const representatives = [...(caseDetails?.representatives ? caseDetails?.representative : [])]
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
            if (data?.data?.condonationFileUpload?.document) {
              documentData = await Promise.all(
                data?.data?.condonationFileUpload?.document?.map(async (document) => {
                  if (document) {
                    const uploadedData = await onDocumentUpload(document, document.name);
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
                condonationFileUpload: {
                  ...data?.data?.condonationFileUpload,
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
      const documentData = {
        bouncedChequeFileUpload: {},
        depositChequeFileUpload: {},
        returnMemoFileUpload: {},
      };
      const infoBoxData = { header: "CS_COMMON_NOTE", data: ["CS_CHEQUE_RETURNED_INSUFFICIENT_FUND"] };
      const newFormData = await Promise.all(
        formdata
          .filter((item) => item.isenabled)
          .map(async (data) => {
            if (data?.data?.bouncedChequeFileUpload?.document) {
              documentData.bouncedChequeFileUpload.document = await Promise.all(
                data?.data?.bouncedChequeFileUpload?.document?.map(async (document) => {
                  if (document) {
                    const uploadedData = await onDocumentUpload(document, document.name);
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
                    const uploadedData = await onDocumentUpload(document, document.name);
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
                    const uploadedData = await onDocumentUpload(document, document.name);
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
                    const uploadedData = await onDocumentUpload(document, document.name);
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
      const documentData = {};
      const newFormData = await Promise.all(
        formdata
          .filter((item) => item.isenabled)
          .map(async (data) => {
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
                      const uploadedData = await onDocumentUpload(document, document.name);
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
      const condonationDocumentData = { condonationFileUpload: {} };
      const newFormData = await Promise.all(
        formdata
          .filter((item) => item.isenabled)
          .map(async (data) => {
            if (data?.data?.condonationFileUpload?.document) {
              condonationDocumentData.condonationFileUpload.document = await Promise.all(
                data?.data?.condonationFileUpload?.document?.map(async (document) => {
                  if (document) {
                    const uploadedData = await onDocumentUpload(document, document.name);
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
      const documentData = { SelectUploadDocWithName: [], prayerForRelief: {}, memorandumOfComplaint: {} };
      const infoBoxData = { header: "", data: "" };
      const newFormData = await Promise.all(
        formdata
          .filter((item) => item.isenabled)
          .map(async (data) => {
            if (data?.data?.SelectUploadDocWithName) {
              documentData.SelectUploadDocWithName = await Promise.all(
                data?.data?.SelectUploadDocWithName?.map(async (docWithNameData) => {
                  if (!docWithNameData?.document[0]?.fileStore) {
                    const document = await onDocumentUpload(docWithNameData?.document[0], docWithNameData?.document[0]?.name).then(async (data) => {
                      const evidenceData = await DRISTIService.createEvidence({
                        artifact: {
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
                    });
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
                      const uploadedData = await onDocumentUpload(document, document.name);
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
            if (data?.data?.memorandumOfComplaint?.document) {
              documentData.memorandumOfComplaint.document = await Promise.all(
                data?.data?.memorandumOfComplaint?.document?.map(async (document) => {
                  if (document) {
                    const uploadedData = await onDocumentUpload(document, document.name);
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
            if (data?.data?.prayerForRelief?.document) {
              documentData.prayerForRelief.document = await Promise.all(
                data?.data?.prayerForRelief?.document?.map(async (document) => {
                  if (document) {
                    const uploadedData = await onDocumentUpload(document, document.name);
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
              debugger;
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
      const vakalatnamaDocumentData = { vakalatnamaFileUpload: {} };
      const newFormData = await Promise.all(
        formdata
          .filter((item) => item.isenabled)
          .map(async (data) => {
            if (data?.data?.vakalatnamaFileUpload?.document) {
              vakalatnamaDocumentData.vakalatnamaFileUpload.document = await Promise.all(
                data?.data?.vakalatnamaFileUpload?.document?.map(async (document) => {
                  if (document) {
                    const uploadedData = await onDocumentUpload(document, document.name);
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
                  };
                }),
                advocateName: data?.data?.advocateBarRegNumberWithName?.[0]?.advocateName,
                barRegistrationNumber: data?.data?.advocateBarRegNumberWithName?.[0]?.barRegistrationNumber,
              },
            };
          })
      );
      const representatives = formdata
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
      data.representatives = [...representatives];
      data.additionalDetails = {
        ...caseDetails.additionalDetails,
        advocateDetails: {
          formdata: newFormData,
          isCompleted: isCompleted === "PAGE_CHANGE" ? caseDetails.additionalDetails?.[selected]?.isCompleted : isCompleted,
        },
      };
    }
    return DRISTIService.caseUpdateService(
      {
        cases: {
          ...caseDetails,
          ...data,
          linkedCases: caseDetails?.linkedCases ? caseDetails?.linkedCases : [],
          filingDate: formatDate(new Date()),
          workflow: {
            ...caseDetails?.workflow,
            action: "SAVE_DRAFT",
          },
        },
        tenantId,
      },
      tenantId
    );
  };

  const onSubmit = async () => {
    if (selected === "addSignature") {
      setOpenConfirmCourtModal(true);
    } else {
      updateCaseDetails(true).then(() => {
        if (!!resetFormData) {
          resetFormData();
        }
        refetchCaseData().then(() => {
          const caseData =
            caseDetails?.additionalDetails?.[nextSelected]?.formdata ||
            caseDetails?.caseDetails?.[nextSelected]?.formdata ||
            (nextSelected === "witnessDetails" ? [{}] : [{ isenabled: true, data: {}, displayindex: 0 }]);
          setFormdata(caseData);
          history.push(`?caseId=${caseId}&selected=${nextSelected}`);
          setIsDisabled(false);
        });
      });
    }
  };

  const onSaveDraft = (props) => {
    setParmas({ ...params, [pageConfig.key]: formdata });
    updateCaseDetails().then(() => {
      refetchCaseData().then(() => {
        const caseData = caseDetails?.additionalDetails?.[nextSelected]?.formdata ||
          caseDetails?.caseDetails?.[nextSelected]?.formdata || [{ isenabled: true, data: {}, displayindex: 0 }];
        setFormdata(caseData);
      });
    });
  };

  const handlePageChange = (key, isConfirm) => {
    if (key === selected) {
      return;
    }
    if (!isConfirm) {
      setOpenConfigurationModal(key);
      return;
    }
    setParmas({ ...params, [pageConfig.key]: formdata });
    setFormdata([{ isenabled: true, data: {}, displayindex: 0 }]);
    if (!!resetFormData) {
      resetFormData();
      setIsDisabled(false);
    }
    setIsOpen(false);
    updateCaseDetails("PAGE_CHANGE").then(() => {
      refetchCaseData().then(() => {
        const caseData =
          caseDetails?.additionalDetails?.[nextSelected]?.formdata ||
          caseDetails?.caseDetails?.[nextSelected]?.formdata ||
          (nextSelected === "witnessDetails" ? [{}] : [{ isenabled: true, data: {}, displayindex: 0 }]);
        setFormdata(caseData);
      });
    });
    history.push(`?caseId=${caseId}&selected=${key}`);
  };

  const onSubmitCase = (data) => {
    setOpenConfirmCourtModal(false);
    history.push(`${path}/e-filing-payment?caseId=${caseId}`);
  };

  const getFormClassName = useCallback(() => {
    if (formdata && formdata?.[0]?.data?.advocateBarRegNumberWithName?.[0]?.isDisable) {
      return "disable-form";
    }

    if (selected === "delayApplications" && formdata?.[0]?.data?.delayApplicationType?.code) {
      return "disable-form";
    }
    return "";
  }, [formdata, selected]);

  const handleConfirmDeleteForm = () => {
    const index = deleteFormIndex;
    const newArray = formdata.map((item, i) => ({
      ...item,
      isenabled: index === i ? false : item.isenabled,
      displayindex: i < index ? item.displayindex : i === index ? -Infinity : item.displayindex - 1,
    }));
    setConfirmDeleteModal(false);
    setFormdata(newArray);
  };

  const [isOpen, setIsOpen] = useState(false);
  if (isLoading) {
    return <Loader />;
  }

  const caseType = {
    cateogry: "Criminal",
    act: "Negotiable Instrument Act",
    section: "138",
    courtName: "Kollam S 138 Special Court",
  };

  return (
    <div className="file-case">
      <div className="file-case-side-stepper">
        <div className="side-stepper-info">
          <div className="header">
            <InfoIcon />
            <span>
              <b>{t("CS_YOU_ARE_FILING_A_CASE")}</b>
            </span>
          </div>
          <p>
            {t("CS_UNDER")} <a href="#" className="act-name">{`S-${caseType.section}, ${caseType.act}`}</a> {t("CS_IN")}
            <span className="place-name">{` ${caseType.courtName}.`}</span>
          </p>
        </div>
        {isOpen && (
          <Modal
            headerBarEnd={
              <CloseBtn
                onClick={() => {
                  setIsOpen(false);
                }}
              />
            }
            hideSubmit={true}
            className={"case-types"}
          >
            <div style={{ padding: "8px 16px" }}>
              {accordion.map((item, index) => (
                <Accordion
                  t={t}
                  title={item.title}
                  handlePageChange={handlePageChange}
                  handleAccordionClick={() => {
                    handleAccordionClick(index);
                  }}
                  key={index}
                  children={item.children}
                  parentIndex={index}
                  isOpen={item.isOpen}
                />
              ))}
            </div>
          </Modal>
        )}

        <div className="file-case-select-form-section">
          {accordion.map((item, index) => (
            <Accordion
              t={t}
              title={item.title}
              handlePageChange={handlePageChange}
              handleAccordionClick={() => {
                handleAccordionClick(index);
              }}
              key={index}
              children={item.children}
              parentIndex={index}
              isOpen={item.isOpen}
              showConfirmModal={confirmModalConfig ? true : false}
            />
          ))}
        </div>
      </div>

      <div className="file-case-form-section">
        <div className="employee-card-wrapper">
          <div className="header-content">
            <div className="header-details">
              <Header>{t(pageConfig.header)}</Header>
              <div
                className="header-icon"
                onClick={() => {
                  setIsOpen(true);
                }}
              >
                <CustomArrowDownIcon />
              </div>
            </div>
            <p>{t(pageConfig.subtext || "")}</p>
          </div>
          {modifiedFormConfig.map((config, index) => {
            return formdata[index].isenabled ? (
              <div key={`${selected}-${index}`} className="form-wrapper-d">
                {pageConfig?.addFormText && (
                  <div className="form-item-name">
                    <h1>{`${t(pageConfig?.formItemName)} ${formdata[index]?.displayindex + 1}`}</h1>
                    {(activeForms > 1 || pageConfig?.isOptional) && (
                      <span
                        style={{ cursor: "pointer" }}
                        onClick={() => {
                          setConfirmDeleteModal(true);
                          setDeleteFormIndex(index);
                        }}
                      >
                        <CustomDeleteIcon />
                      </span>
                    )}
                  </div>
                )}
                <FormComposerV2
                  key={selected}
                  label={selected === "addSignature" ? t("CS_SUBMIT_CASE") : t("CS_COMMON_CONTINUE")}
                  config={config}
                  onSubmit={(data) => onSubmit(data, index)}
                  onSecondayActionClick={onSaveDraft}
                  defaultValues={
                    caseDetails?.additionalDetails?.[selected]?.formdata?.[index]?.data ||
                    caseDetails?.caseDetails?.[selected]?.formdata?.[index]?.data ||
                    formdata[index]?.data
                  }
                  onFormValueChange={(setValue, formData, formState, reset, setError, clearErrors, trigger, getValues) => {
                    onFormValueChange(setValue, formData, formState, reset, setError, clearErrors, trigger, getValues, index);
                  }}
                  cardStyle={{ minWidth: "100%" }}
                  isDisabled={isDisabled}
                  cardClassName={`e-filing-card-form-style ${pageConfig.className}`}
                  secondaryLabel={t("CS_SAVE_DRAFT")}
                  showSecondaryLabel={true}
                  actionClassName="e-filing-action-bar"
                  className={`${pageConfig.className} ${getFormClassName()}`}
                  noBreakLine
                />
              </div>
            ) : null;
          })}
          {confirmDeleteModal && (
            <Modal
              // hideSubmit={true}
              headerBarMain={<Heading label={t("Are you sure?")} />}
              headerBarEnd={<CloseBtn onClick={() => setConfirmDeleteModal(false)} />}
              actionCancelLabel="Cancel"
              actionCancelOnSubmit={() => setConfirmDeleteModal(false)}
              actionSaveLabel={`Remove ${pageConfig?.formItemName}`}
              children={deleteWarningText(`${pageConfig?.formItemName}`)}
              actionSaveOnSubmit={handleConfirmDeleteForm}
            ></Modal>
          )}
          {receiptDemandNoticeModal && (
            <Modal
              headerBarMain={<Heading label={t("CS_IMPORTANT_NOTICE")} />}
              headerBarEnd={
                <CloseBtn
                  onClick={() => {
                    setReceiptDemandNoticeModal(false);
                  }}
                />
              }
              actionCancelLabel={t("CS_CANCEL_E_FILING")}
              actionCancelOnSubmit={async () => {
                await DRISTIService.caseUpdateService(
                  {
                    cases: {
                      ...caseDetails,
                      filingDate: formatDate(new Date()),
                      workflow: {
                        ...caseDetails?.workflow,
                        action: "DELETE_DRAFT",
                      },
                    },
                    tenantId,
                  },
                  tenantId
                );
                setReceiptDemandNoticeModal(false);
                history.push(`/${window?.contextPath}/citizen/dristi/home`);
              }}
              actionSaveLabel={t("CS_NOT_PAID_FULL")}
              children={<div style={{ padding: "16px 0" }}>{t("CS_NOT_PAID_FULL_TEXT")}</div>}
              actionSaveOnSubmit={async () => {
                setFormDataValue("delayApplicationType", {
                  code: "YES",
                  name: "YES",
                  showForm: false,
                  isEnabled: true,
                });
                setReceiptDemandNoticeModal(false);
              }}
            ></Modal>
          )}
          {serviceOfDemandNoticeModal && (
            <Modal
              headerBarMain={<Heading label={t("CS_IMPORTANT_NOTICE")} />}
              headerBarEnd={
                <CloseBtn
                  onClick={() => {
                    setServiceOfDemandNoticeModal(false);
                  }}
                />
              }
              actionCancelOnSubmit={() => setServiceOfDemandNoticeModal(false)}
              actionSaveLabel={t("CS_SAVE_DRAFT")}
              children={<div style={{ padding: "16px 0" }}>{t("CS_SAVE_AS_DRAFT_TEXT")}</div>}
              actionSaveOnSubmit={async () => {
                await DRISTIService.caseUpdateService(
                  {
                    cases: {
                      ...caseDetails,
                      filingDate: formatDate(new Date()),
                      workflow: {
                        ...caseDetails?.workflow,
                        action: "SAVE_DRAFT",
                      },
                    },
                    tenantId,
                  },
                  tenantId
                );
                setReceiptDemandNoticeModal(false);
                history.push(`/${window?.contextPath}/citizen/dristi/home`);
              }}
            ></Modal>
          )}
          {pageConfig?.addFormText && (
            <Button
              variation="secondary"
              onButtonClick={handleAddForm}
              className="add-new-form"
              icon={<CustomAddIcon />}
              label={t(pageConfig.addFormText)}
            ></Button>
          )}
          {openConfigurationModal && (
            <EditFieldsModal
              t={t}
              config={confirmModalConfig}
              setOpenConfigurationModal={setOpenConfigurationModal}
              selected={openConfigurationModal}
              handlePageChange={handlePageChange}
            />
          )}
          {showErrorToast && <Toast error={true} label={t("ES_COMMON_PLEASE_ENTER_ALL_MANDATORY_FIELDS")} isDleteBtn={true} onClose={closeToast} />}
          {showSuccessToast && <Toast label={t(successMsg)} isDleteBtn={true} onClose={closeToast} />}
        </div>
      </div>
      {openConfirmCourtModal && <ConfirmCourtModal setOpenConfirmCourtModal={setOpenConfirmCourtModal} t={t} onSubmitCase={onSubmitCase} />}
    </div>
  );
}

export default EFilingCases;
