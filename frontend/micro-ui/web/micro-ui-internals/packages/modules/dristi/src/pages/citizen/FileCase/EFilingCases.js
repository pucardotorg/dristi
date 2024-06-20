import { Button, CloseSvg, FormComposerV2, Header, Loader, Toast } from "@egovernments/digit-ui-react-components";
import React, { useCallback, useEffect, useMemo, useRef, useState } from "react";
import { useTranslation } from "react-i18next";
import { useHistory } from "react-router-dom/cjs/react-router-dom.min";
import { CaseWorkflowAction, CaseWorkflowState } from "../../../Utils/caseWorkflow";
import Accordion from "../../../components/Accordion";
import ConfirmCourtModal from "../../../components/ConfirmCourtModal";
import Modal from "../../../components/Modal";
import { useToast } from "../../../components/Toast/useToast";
import useSearchCaseService from "../../../hooks/dristi/useSearchCaseService";
import { ReactComponent as InfoIcon } from "../../../icons/info.svg";
import { CustomAddIcon, CustomArrowDownIcon, CustomDeleteIcon, RightArrow } from "../../../icons/svgIndex";
import { DRISTIService } from "../../../services";
import { formatDate } from "./CaseType";
import { sideMenuConfig } from "./Config";
import EditFieldsModal from "./EditFieldsModal";
import {
  checkIfscValidation,
  checkNameValidation,
  checkOnlyCharInCheque,
  chequeDateValidation,
  complainantValidation,
  delayApplicationValidation,
  demandNoticeFileValidation,
  prayerAndSwornValidation,
  respondentValidation,
  showDemandNoticeModal,
  showToastForComplainant,
  signatureValidation,
  updateCaseDetails,
  validateDateForDelayApplication,
  chequeDetailFileValidation,
  advocateDetailsFileValidation,
} from "./EfilingValidationUtils";
import ConfirmCorrectionModal from "../../../components/ConfirmCorrectionModal";

function isEmptyValue(value) {
  if (!value) {
    return true;
  } else if (Array.isArray(value) || typeof value === "object") {
    return Object.keys(value).length === 0;
  } else if (typeof value === "string") {
    return value.trim().length === 0;
  } else {
    return false;
  }
}

const extractValue = (data, key) => {
  if (!key.includes(".")) {
    return data[key];
  }
  const keyParts = key.split(".");
  let value = data;
  keyParts.forEach((part) => {
    if (value && value.hasOwnProperty(part)) {
      value = value[part];
    } else {
      value = undefined;
    }
  });
  return value;
};

const Heading = (props) => {
  return <h1 className="heading-m">{props.label}</h1>;
};

const selectedArray = [
  "complaintDetails",
  "respondentDetails",
  "chequeDetails",
  "debtLiabilityDetails",
  "demandNoticeDetails",
  "delayApplications",
  "witnessDetails",
  "prayerSwornStatement",
  "advocateDetails",
];

const getTotalCountFromSideMenuConfig = (sideMenuConfig, selected) => {
  const countObj = { mandatory: 0, optional: 0 };
  for (let i = 0; i < sideMenuConfig.length; i++) {
    const childArray = sideMenuConfig[i]?.children;
    for (let j = 0; j < childArray.length; j++) {
      if (childArray[j].key === selected) {
        countObj.mandatory = childArray[j]?.initialMandatoryFieldCount;
        countObj.optional = childArray[j]?.initialOptionalFieldCount;
      }
    }
  }
  return countObj;
};

function EFilingCases({ path }) {
  const [params, setParmas] = useState({});
  const { t } = useTranslation();
  const toast = useToast();
  const history = useHistory();
  const [showErrorToast, setShowErrorToast] = useState(false);
  const [isDisabled, setIsDisabled] = useState(false);
  const setFormErrors = useRef(null);
  const resetFormData = useRef(null);
  const setFormDataValue = useRef(null);
  const clearFormDataErrors = useRef(null);

  const urlParams = new URLSearchParams(window.location.search);
  const selected = urlParams.get("selected") || sideMenuConfig?.[0]?.children?.[0]?.key;
  const caseId = urlParams.get("caseId");
  const [formdata, setFormdata] = useState(selected === "witnessDetails" ? [{}] : [{ isenabled: true, data: {}, displayindex: 0 }]);
  const [errorCaseDetails, setErrorCaseDetails] = useState(null);
  const tenantId = window?.Digit.ULBService.getCurrentTenantId();
  const [parentOpen, setParentOpen] = useState(sideMenuConfig.findIndex((parent) => parent.children.some((child) => child.key === selected)));

  const [openConfigurationModal, setOpenConfigurationModal] = useState(false);
  const [openConfirmCourtModal, setOpenConfirmCourtModal] = useState(false);
  const [openConfirmCorrectionModal, setOpenConfirmCorrectionModal] = useState(false);
  const [receiptDemandNoticeModal, setReceiptDemandNoticeModal] = useState(false);
  const [serviceOfDemandNoticeModal, setServiceOfDemandNoticeModal] = useState(false);
  const [confirmDeleteModal, setConfirmDeleteModal] = useState(false);
  const [showConfirmMandatoryModal, setShowConfirmMandatoryModal] = useState(false);
  const [showConfirmOptionalModal, setShowConfirmOptionalModal] = useState(false);
  const [errorMsg, setErrorMsg] = useState("");
  const homepagePath = "/digit-ui/citizen/dristi/home";

  const [{ showSuccessToast, successMsg }, setSuccessToast] = useState({
    showSuccessToast: false,
    successMsg: "",
  });
  const [deleteFormIndex, setDeleteFormIndex] = useState(null);
  const setFieldsRemainingInitially = () => {
    const array = [];
    for (let i = 0; i < selectedArray.length; i++) {
      const selected = selectedArray[i];
      array.push({
        selectedPage: selected,
        mandatoryTotalCount: getTotalCountFromSideMenuConfig(sideMenuConfig, selected)?.mandatory,
        optionalTotalCount: getTotalCountFromSideMenuConfig(sideMenuConfig, selected)?.optional,
      });
    }
    return array;
  };
  const [fieldsRemaining, setFieldsRemaining] = useState(() => setFieldsRemainingInitially());

  const checkAndGetMandatoryFieldLeftPages = useMemo(() => {
    const mandatoryRemainingPages = fieldsRemaining.filter((page) => page.mandatoryTotalCount !== 0) || [];
    return mandatoryRemainingPages;
  }, [fieldsRemaining]);

  const checkAndGetOptionalFieldLeftPages = useMemo(() => {
    const optionalRemainingPages = fieldsRemaining.filter((page) => page.optionalTotalCount !== 0) || [];
    return optionalRemainingPages;
  }, [fieldsRemaining]);

  const mandatoryFieldsLeftTotalCount = useMemo(() => {
    let count = 0;
    for (let i = 0; i < fieldsRemaining.length; i++) {
      count = count + fieldsRemaining[i].mandatoryTotalCount;
    }
    return count;
  }, [fieldsRemaining]);

  const optionalFieldsLeftTotalCount = useMemo(() => {
    let count = 0;
    for (let i = 0; i < fieldsRemaining.length; i++) {
      count = count + fieldsRemaining[i].optionalTotalCount;
    }
    return count;
  }, [fieldsRemaining]);

  const showMandatoryFieldsRemainingModal = useMemo(() => {
    if (selected === "reviewCaseFile" || selected === "addSignature") {
      if (mandatoryFieldsLeftTotalCount > 0) {
        setShowConfirmMandatoryModal(true);
        return true;
      } else return false;
    }
    return false;
  }, [selected, mandatoryFieldsLeftTotalCount]);

  const showOptionalFieldsRemainingModal = useMemo(() => {
    if (selected === "reviewCaseFile" || selected === "addSignature") {
      if (checkAndGetOptionalFieldLeftPages.length !== 0) {
        setShowConfirmOptionalModal(true);
        return true;
      } else return false;
    }
    return false;
  }, [selected, checkAndGetOptionalFieldLeftPages]);

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
      <div className="delete-warning-text">
        <h3>{`${t("CONFIRM_DELETE_FIRST_HALF")} ${pageName?.toLowerCase()} ${t("CONFIRM_DELETE_SECOND_HALF")}`}</h3>
      </div>
    );
  }, []);

  const mandatoryFieldsRemainingText = useCallback(() => {
    return (
      <div>
        <h3>{t("ENSURE_ALL_MANDATORY_ARE_FILLED")}</h3>
      </div>
    );
  }, []);

  const optionalFieldsRemainingText = useCallback((count) => {
    return (
      <div>
        <h3>{`${t("MORE_INFO_HELPS_FIRST_HALF")} ${count} ${t("MORE_INFO_HELPS_SECOND_HALF")}`}</h3>
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

  const state = useMemo(() => caseDetails?.status, [caseDetails]);

  const isCaseReAssigned = state === CaseWorkflowState.CASE_RE_ASSIGNED;
  const isDisableAllFieldsMode = !(state === CaseWorkflowState.CASE_RE_ASSIGNED || state === CaseWorkflowState.DRAFT_IN_PROGRESS);
  const isDraftInProgress = state === CaseWorkflowState.DRAFT_IN_PROGRESS;

  useEffect(() => {
    setParentOpen(sideMenuConfig.findIndex((parent) => parent.children.some((child) => child.key === selected)));
  }, [selected]);

  useEffect(() => {
    if (Object.keys(caseDetails).length !== 0) {
      const fieldsRemainingCopy = structuredClone(fieldsRemaining);
      const additionalDetailsArray = ["complaintDetails", "respondentDetails", "witnessDetails", "prayerSwornStatement", "advocateDetails"];
      const caseDetailsArray = ["chequeDetails", "debtLiabilityDetails", "demandNoticeDetails", "delayApplications"];
      for (const key of additionalDetailsArray) {
        if (caseDetails?.additionalDetails?.[key]) {
          const index = fieldsRemainingCopy.findIndex((fieldsRemainingCopy) => fieldsRemainingCopy.selectedPage === key);
          fieldsRemainingCopy[index] = setMandatoryAndOptionalRemainingFields(caseDetails?.additionalDetails?.[key]?.formdata, key);
        }
      }
      for (const key of caseDetailsArray) {
        if (caseDetails?.caseDetails?.[key]) {
          const index = fieldsRemainingCopy.findIndex((fieldsRemainingCopy) => fieldsRemainingCopy.selectedPage === key);
          fieldsRemainingCopy[index] = setMandatoryAndOptionalRemainingFields(caseDetails?.caseDetails?.[key]?.formdata, key);
        }
      }
      setFieldsRemaining(fieldsRemainingCopy);
    }
  }, [caseDetails]);

  useEffect(() => {
    const data =
      caseDetails?.additionalDetails?.[selected]?.formdata ||
      caseDetails?.caseDetails?.[selected]?.formdata ||
      (selected === "witnessDetails" ? [{}] : [{ isenabled: true, data: {}, displayindex: 0 }]);
    setFormdata(data);
  }, [selected, caseDetails]);

  const closeToast = () => {
    setShowErrorToast(false);
    setErrorMsg("");
    setSuccessToast((prev) => ({
      ...prev,
      showSuccessToast: false,
      successMsg: "",
    }));
  };

  useEffect(() => {
    let timer;
    if (showErrorToast || showSuccessToast) {
      timer = setTimeout(() => {
        closeToast();
      }, 2000);
    }
    return () => clearTimeout(timer);
  }, [showErrorToast, showSuccessToast]);

  const getDefaultValues = useCallback(
    (index) => {
      if (isCaseReAssigned && errorCaseDetails) {
        return (
          errorCaseDetails?.additionalDetails?.[selected]?.formdata?.[index]?.data ||
          errorCaseDetails?.caseDetails?.[selected]?.formdata?.[index]?.data ||
          formdata[index]?.data
        );
      }

      return (
        caseDetails?.additionalDetails?.[selected]?.formdata?.[index]?.data ||
        caseDetails?.caseDetails?.[selected]?.formdata?.[index]?.data ||
        formdata[index]?.data
      );
    },
    [caseDetails?.additionalDetails, caseDetails?.caseDetails, formdata, selected]
  );

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
    let modifiedFormData = formdata;
    if (!isDependentEnabled) {
      modifiedFormData = modifiedFormData.map(() => {
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
        if (selected === "witnessDetails") {
          return formConfig.map((config) => {
            return {
              ...config,
              body: config?.body?.map((body) => {
                return {
                  ...body,
                  labelChildren: body?.labelChildren === "optional" ? <span style={{ color: "#77787B" }}>&nbsp;{`${t("CS_IS_OPTIONAL")}`}</span> : "",
                };
              }),
            };
          });
        }
        return formConfig;
      });
      if (!isCaseReAssigned) {
        return modifiedFormData;
      }
    }
    return modifiedFormData.map(({ data }, index) => {
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
              if (Array.isArray(data?.[key]?.[name]) && data?.[key]?.[name]?.length === 0) {
                show = false;
              } else show = show && Boolean(data?.[key]?.[name]);
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

              if (body?.labelChildren === "optional" && Object.keys(caseDetails?.additionalDetails?.scrutiny?.data || {}).length === 0) {
                body.labelChildren = <span style={{ color: "#77787B" }}>&nbsp;{`${t("CS_IS_OPTIONAL")}`}</span>;
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
                      if (selected === "respondentDetails") {
                        if (
                          Array.isArray(data?.addressDetails) &&
                          data?.addressDetails?.some(
                            (address) =>
                              address?.addressDetails?.pincode !==
                                caseDetails?.additionalDetails?.["complaintDetails"]?.formdata?.[0]?.data?.addressDetails?.pincode &&
                              body?.key === "inquiryAffidavitFileUpload"
                          )
                        ) {
                          delete input.isOptional;
                          return {
                            ...input,
                          };
                        } else {
                          return {
                            ...input,
                            isOptional: "CS_IS_OPTIONAL",
                          };
                        }
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
          const scrutinyObj = caseDetails?.additionalDetails?.scrutiny?.data || {};
          const scrutiny = {};
          Object.keys(scrutinyObj).forEach((item) => {
            Object.keys(scrutinyObj[item]).forEach((key) => {
              scrutiny[key] = scrutinyObj[item][key];
            });
          });
          let updatedBody = [];
          if (Object.keys(scrutinyObj).length > 0) {
            updatedBody = config.body
              .map((formComponent) => {
                let key = formComponent.key || formComponent.populators?.name;
                if (formComponent.type === "component" && formComponent.component === "SelectCustomDragDrop") {
                  key = formComponent.key + "." + formComponent.populators?.inputs?.[0]?.name;
                }
                const modifiedFormComponent = structuredClone(formComponent);
                if (modifiedFormComponent?.labelChildren === "optional") {
                  modifiedFormComponent.labelChildren = <span style={{ color: "#77787B" }}>&nbsp;{`${t("CS_IS_OPTIONAL")}`}</span>;
                }
                modifiedFormComponent.disable = true;
                if (scrutiny?.[selected] && key in scrutiny?.[selected]?.form?.[index]) {
                  modifiedFormComponent.disable = false;
                  modifiedFormComponent.withoutLabel = true;
                  return [
                    {
                      type: "component",
                      component: "ScrutinyInfo",
                      key: `${key}Scrutiny`,
                      label: modifiedFormComponent.label,
                      populators: {
                        scrutinyMessage: scrutiny?.[selected].form[index][key].FSOError,
                      },
                    },
                    modifiedFormComponent,
                  ];
                }
                return modifiedFormComponent;
              })
              .flat();
          } else {
            updatedBody = config.body.map((formComponent) => {
              return formComponent;
            });
          }
          return {
            ...config,
            body: updatedBody,
          };
        });
    });
  }, [isDependentEnabled, formdata, selected, formConfig, caseDetails.additionalDetails, caseDetails?.caseDetails, t]);

  const activeForms = useMemo(() => {
    return formdata.filter((item) => item.isenabled === true).length;
  }, [formdata]);

  const handleAddForm = () => {
    setFormdata([...formdata, { isenabled: true, data: {}, displayindex: activeForms }]);
  };

  // const handleDeleteForm = (index) => {
  //   const newArray = formdata.map((item, i) => ({
  //     ...item,
  //     isenabled: index === i ? false : item.isenabled,
  //     displayindex: i < index ? item.displayindex : i === index ? -Infinity : item.displayindex - 1,
  //   }));
  //   setConfirmDeleteModal(true);
  //   setFormdata(newArray);
  // };

  const onFormValueChange = (setValue, formData, formState, reset, setError, clearErrors, trigger, getValues, index) => {
    if (formData.advocateBarRegNumberWithName?.[0] && !formData.advocateBarRegNumberWithName[0].modified) {
      setValue("advocateBarRegNumberWithName", [
        {
          ...formData.advocateBarRegNumberWithName[0],
          modified: true,
          barRegistrationNumber: formData.advocateBarRegNumberWithName[0].barRegistrationNumberOriginal,
        },
      ]);
    }
    checkIfscValidation({ formData, setValue, selected });
    checkNameValidation({ formData, setValue, selected, formdata, index, reset });
    checkOnlyCharInCheque({ formData, setValue, selected });
    if (JSON.stringify(formData) !== JSON.stringify(formdata[index].data)) {
      chequeDateValidation({ formData, setError, clearErrors, selected });
      showDemandNoticeModal({
        setValue,
        formData,
        setError,
        clearErrors,
        index,
        caseDetails,
        selected,
        setReceiptDemandNoticeModal,
        setServiceOfDemandNoticeModal,
      });
      validateDateForDelayApplication({ setValue, caseDetails, selected, toast, t, history, caseId });
      showToastForComplainant({ formData, setValue, selected, setSuccessToast });
      setFormdata(
        formdata.map((item, i) => {
          return i === index
            ? {
                ...item,
                data: formData,
              }
            : item;
        })
      );
    }

    setFormErrors.current = setError;
    resetFormData.current = reset;
    setFormDataValue.current = setValue;
    clearFormDataErrors.current = clearErrors;

    // if (formState?.submitCount && !Object.keys(formState?.errors).length && formState?.isSubmitSuccessful) {
    //   setIsDisabled(true);
    // }
  };

  const handleAccordionClick = (index) => {
    setParentOpen((prevParentOpen) => (prevParentOpen === index ? -1 : index));
  };

  const setMandatoryAndOptionalRemainingFields = (currentPageData, currentSelected) => {
    let totalMandatoryLeft = 0;
    let totalOptionalLeft = 0;

    if (currentPageData.length === 0) {
      // this case is specially for witness details page (which is optional),
      // so there might not be any witness at all.
      totalMandatoryLeft = 0;
      totalOptionalLeft = 1;
    } else {
      for (let i = 0; i < currentPageData.length; i++) {
        const currentIndexData = currentPageData[i];
        const currentPageMandatoryFields = [];
        const currentPageOptionalFields = [];
        let currentPage = {};
        for (const obj of sideMenuConfig) {
          const foundPage = obj?.children.find((o) => o?.key === currentSelected);
          if (foundPage) {
            currentPage = foundPage;
            break;
          }
        }

        currentPageMandatoryFields.push(...(currentPage?.mandatoryFields || []));
        currentPageOptionalFields.push(...(currentPage?.optionalFields || []));

        const currentPageMandatoryDependentFields = (currentPage?.dependentMandatoryFields || [])
          .filter((obj) => {
            return currentIndexData?.data?.[obj?.dependentOn]?.[obj?.dependentOnKey] === true;
          })
          .map((obj) => {
            return obj?.field;
          });
        currentPageMandatoryFields.push(...currentPageMandatoryDependentFields);

        const currentPageOptionalDependentFields = (currentPage?.dependentOptionalFields || [])
          .filter((obj) => {
            return currentIndexData?.data?.[obj?.dependentOn]?.[obj?.dependentOnKey] === true;
          })
          .map((obj) => {
            return obj?.field;
          });
        currentPageOptionalFields.push(...currentPageOptionalDependentFields);

        if (currentPageMandatoryFields.length !== 0) {
          for (let i = 0; i < currentPageMandatoryFields.length; i++) {
            const value = extractValue(currentIndexData?.data, currentPageMandatoryFields[i]);
            const isValueEmpty = isEmptyValue(value);
            if (isValueEmpty) {
              totalMandatoryLeft++;
            }
          }
        }

        if ("ifMultipleAddressLocations" in currentPage) {
          const arrayValue = currentIndexData?.data[currentPage?.ifDataKeyHasValueAsArray?.dataKey] || [];
          for (let i = 0; i < arrayValue.length; i++) {
            const mandatoryFields = currentPage?.ifDataKeyHasValueAsArray?.mandatoryFields || [];
            for (let j = 0; j < mandatoryFields.length; j++) {
              const value = extractValue(arrayValue[i], mandatoryFields[j]);
              const isValueEmpty = isEmptyValue(value);
              if (isValueEmpty) {
                totalMandatoryLeft++;
              }
            }
          }
        }

        if ("anyOneOfTheseMandatoryFields" in currentPage) {
          const fieldsArray = currentPage.anyOneOfTheseMandatoryFields;
          for (let i = 0; i < fieldsArray.length; i++) {
            const currentChildArray = fieldsArray[i];
            let count = 0;
            for (let j = 0; j < currentChildArray.length; j++) {
              const value = extractValue(currentIndexData?.data, currentChildArray[j]);
              const isValueEmpty = isEmptyValue(value);
              if (isValueEmpty) {
                count++;
              }
            }
            if (count === 2) {
              totalMandatoryLeft++;
            }
          }
        }

        if (currentPageOptionalFields.length !== 0) {
          let optionalLeft = 0;
          for (let i = 0; i < currentPageOptionalFields.length; i++) {
            const value = extractValue(currentIndexData?.data, currentPageOptionalFields[i]);
            const isValueEmpty = isEmptyValue(value);
            if (isValueEmpty) {
              optionalLeft++;
            }
          }
          totalOptionalLeft += optionalLeft;
        }
      }
    }
    const obj = {
      selectedPage: currentSelected,
      mandatoryTotalCount: totalMandatoryLeft,
      optionalTotalCount: totalOptionalLeft,
    };
    return obj;
  };
  const onSubmit = async (action) => {
    if (isDisableAllFieldsMode) {
      history.push(homepagePath);
    }
    if (!Array.isArray(formdata)) {
      return;
    }
    if (
      formdata
        .filter((data) => data.isenabled)
        .some((data) => respondentValidation({ t, formData: data?.data, caseDetails, selected, setShowErrorToast, toast }))
    ) {
      return;
    }
    if (formdata.filter((data) => data.isenabled).some((data) => demandNoticeFileValidation({ formData: data?.data, selected, setShowErrorToast }))) {
      return;
    }
    if (formdata.filter((data) => data.isenabled).some((data) => chequeDetailFileValidation({ formData: data?.data, selected, setShowErrorToast }))) {
      return;
    }
    if (
      formdata.filter((data) => data.isenabled).some((data) => advocateDetailsFileValidation({ formData: data?.data, selected, setShowErrorToast }))
    ) {
      return;
    }
    if (
      formdata
        .filter((data) => data.isenabled)
        .some((data) => complainantValidation({ formData: data?.data, t, caseDetails, selected, setShowErrorToast, toast }))
    ) {
      return;
    }
    if (
      formdata
        .filter((data) => data.isenabled)
        .some((data) => delayApplicationValidation({ formData: data?.data, t, caseDetails, selected, setShowErrorToast, toast }))
    ) {
      return;
    }
    if (
      formdata
        .filter((data) => data.isenabled)
        .some((data) => prayerAndSwornValidation({ t, formData: data?.data, selected, setShowErrorToast, setErrorMsg, toast }))
    ) {
      return;
    }
    if (
      formdata
        .filter((data) => data.isenabled)
        .some((data) => signatureValidation({ formData: data?.data, selected, setShowErrorToast, setErrorMsg }))
    ) {
      return;
    }
    if (selected === "addSignature" && isCaseReAssigned && !openConfirmCorrectionModal) {
      return setOpenConfirmCorrectionModal(true);
    }

    if (selected === "addSignature" && isDraftInProgress) {
      setOpenConfirmCourtModal(true);
    } else {
      updateCaseDetails({
        isCompleted: true,
        caseDetails: isCaseReAssigned && errorCaseDetails ? errorCaseDetails : caseDetails,
        formdata,
        pageConfig,
        selected,
        setIsDisabled,
        tenantId,
        setFormDataValue: setFormDataValue.current,
        action,
        setErrorCaseDetails,
      })
        .then(() => {
          if (resetFormData.current) {
            resetFormData.current();
            setIsDisabled(false);
          }
          return refetchCaseData().then(() => {
            const caseData =
              caseDetails?.additionalDetails?.[nextSelected]?.formdata ||
              caseDetails?.caseDetails?.[nextSelected]?.formdata ||
              (nextSelected === "witnessDetails" ? [{}] : [{ isenabled: true, data: {}, displayindex: 0 }]);
            setFormdata(caseData);
            setIsDisabled(false);
            if (action === CaseWorkflowAction.EDIT_CASE) {
              return history.push(`/${window.contextPath}/citizen/dristi/home`);
            }
            history.push(`?caseId=${caseId}&selected=${nextSelected}`);
          });
        })
        .catch(() => {
          history.push(`?caseId=${caseId}&selected=${nextSelected}`);
          setIsDisabled(false);
        });
    }
  };

  const onSaveDraft = (props) => {
    setParmas({ ...params, [pageConfig.key]: formdata });
    updateCaseDetails({ caseDetails, formdata, pageConfig, selected, setIsDisabled, tenantId })
      .then(() => {
        refetchCaseData().then(() => {
          const caseData = caseDetails?.additionalDetails?.[nextSelected]?.formdata ||
            caseDetails?.caseDetails?.[nextSelected]?.formdata || [{ isenabled: true, data: {}, displayindex: 0 }];
          setFormdata(caseData);
          setIsDisabled(false);
        });
      })
      .catch(() => {
        setIsDisabled(false);
      })
      .finally(() => {
        toast.success("Successfully Saved Draft");
      });
  };

  const onErrorCorrectionSubmit = () => {
    setOpenConfirmCorrectionModal(false);
    onSubmit(CaseWorkflowAction.EDIT_CASE);
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
    if (resetFormData.current) {
      resetFormData.current();
      setIsDisabled(false);
    }
    setIsOpen(false);

    updateCaseDetails({ isCompleted: "PAGE_CHANGE", caseDetails, formdata, pageConfig, selected, setIsDisabled, tenantId })
      .then(() => {
        if (!isCaseReAssigned) {
          refetchCaseData().then(() => {
            const caseData =
              caseDetails?.additionalDetails?.[nextSelected]?.formdata ||
              caseDetails?.caseDetails?.[nextSelected]?.formdata ||
              (nextSelected === "witnessDetails" ? [{}] : [{ isenabled: true, data: {}, displayindex: 0 }]);
            setFormdata(caseData);
            setIsDisabled(false);
          });
        }
      })
      .catch(() => {
        setIsDisabled(false);
      });

    history.push(`?caseId=${caseId}&selected=${key}`);
  };

  const onSubmitCase = async (data) => {
    setOpenConfirmCourtModal(false);
    await DRISTIService.caseUpdateService(
      {
        cases: {
          ...caseDetails,
          caseTitle: `${caseDetails?.additionalDetails?.complaintDetails?.formdata?.[0]?.data?.firstName} ${caseDetails?.additionalDetails?.complaintDetails?.formdata?.[0]?.data?.lastName} VS ${caseDetails?.additionalDetails?.respondentDetails?.formdata?.[0]?.data?.respondentFirstName} ${caseDetails?.additionalDetails?.respondentDetails?.formdata?.[0]?.data?.respondentLastName}`,
          filingDate: formatDate(new Date()),
          workflow: {
            ...caseDetails?.workflow,
            action: "SUBMIT_CASE",
          },
        },
        tenantId,
      },
      tenantId
    );
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

  const takeUserToRemainingMandatoryFieldsPage = () => {
    const firstPageInTheListWhichHasMandatoryFieldsLeft = checkAndGetMandatoryFieldLeftPages?.[0];
    const selectedPage = firstPageInTheListWhichHasMandatoryFieldsLeft?.selectedPage;
    history.push(`?caseId=${caseId}&selected=${selectedPage}`);
    setShowConfirmMandatoryModal(false);
  };

  const takeUserToRemainingOptionalFieldsPage = () => {
    const firstPageInTheListWhichHasOptionalFieldsLeft = checkAndGetOptionalFieldLeftPages?.[0];
    const selectedPage = firstPageInTheListWhichHasOptionalFieldsLeft?.selectedPage;
    history.push(`?caseId=${caseId}&selected=${selectedPage}`);
    setShowConfirmOptionalModal(false);
  };
  if (isDisableAllFieldsMode && selected !== "reviewCaseFile" && caseDetails) {
    history.push(`?caseId=${caseId}&selected=reviewCaseFile`);
  }
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
              <Header>
                {`${t(pageConfig.header)}`}
                {pageConfig?.showOptionalInHeader && <span style={{ color: "#77787B", fontWeight: 100 }}>&nbsp;(optional)</span>}
              </Header>
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
                    {(activeForms > 1 || t(pageConfig?.formItemName) === "Witness" || pageConfig?.isOptional) && (
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
                  label={selected === "addSignature" ? t("CS_SUBMIT_CASE") : isDisableAllFieldsMode ? t("CS_GO_TO_HOME") : t("CS_COMMON_CONTINUE")}
                  config={config}
                  onSubmit={() => onSubmit("SAVE_DRAFT", index)}
                  onSecondayActionClick={onSaveDraft}
                  defaultValues={getDefaultValues(index)}
                  onFormValueChange={(setValue, formData, formState, reset, setError, clearErrors, trigger, getValues) => {
                    onFormValueChange(setValue, formData, formState, reset, setError, clearErrors, trigger, getValues, index);
                  }}
                  cardStyle={{ minWidth: "100%" }}
                  cardClassName={`e-filing-card-form-style ${pageConfig.className}`}
                  secondaryLabel={t("CS_SAVE_DRAFT")}
                  showSecondaryLabel={isDraftInProgress}
                  actionClassName="e-filing-action-bar"
                  className={`${pageConfig.className} ${getFormClassName()}`}
                  noBreakLine
                  submitIcon={<RightArrow />}
                />
              </div>
            ) : null;
          })}
          {confirmDeleteModal && (
            <Modal
              headerBarMain={<Heading label={t("Are you sure?")} />}
              headerBarEnd={<CloseBtn onClick={() => setConfirmDeleteModal(false)} />}
              actionCancelLabel="Cancel"
              actionCancelOnSubmit={() => setConfirmDeleteModal(false)}
              actionSaveLabel={`Remove ${t(pageConfig?.formItemName)}`}
              children={deleteWarningText(`${t(pageConfig?.formItemName).toLowerCase()}`)}
              actionSaveOnSubmit={handleConfirmDeleteForm}
              className={"confirm-delete-modal"}
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
                      litigants: !caseDetails?.litigants ? [] : caseDetails?.litigants,
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
                setFormDataValue.current?.("delayApplicationType", {
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
                      litigants: !caseDetails?.litigants ? [] : caseDetails?.litigants,
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
          {showMandatoryFieldsRemainingModal && showConfirmMandatoryModal && (
            <Modal
              headerBarMain={<Heading label={`${mandatoryFieldsLeftTotalCount} ${t("MANDATORY_FIELDS_REMAINING")}`} />}
              headerBarEnd={<CloseBtn onClick={() => takeUserToRemainingMandatoryFieldsPage()} />}
              actionSaveLabel={t("CONTINUE_FILLING")}
              children={mandatoryFieldsRemainingText()}
              actionSaveOnSubmit={() => takeUserToRemainingMandatoryFieldsPage()}
            ></Modal>
          )}
          {showOptionalFieldsRemainingModal && showConfirmOptionalModal && !mandatoryFieldsLeftTotalCount && !isDisableAllFieldsMode && (
            <Modal
              headerBarMain={<Heading label={t("TIPS_FOR_STRONGER_CASE")} />}
              headerBarEnd={<CloseBtn onClick={() => takeUserToRemainingOptionalFieldsPage()} />}
              actionCancelLabel={t("SKIP_AND_CONTINUE")}
              actionCancelOnSubmit={() => setShowConfirmOptionalModal(false)}
              actionSaveLabel={t("FILL_NOW")}
              children={optionalFieldsRemainingText(optionalFieldsLeftTotalCount)}
              actionSaveOnSubmit={() => takeUserToRemainingOptionalFieldsPage()}
            ></Modal>
          )}
          {pageConfig?.addFormText && (
            <Button
              variation="secondary"
              onButtonClick={handleAddForm}
              className="add-new-form"
              icon={<CustomAddIcon />}
              label={t(pageConfig.addFormText)}
              isDisabled={!isDraftInProgress}
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
          {showErrorToast && (
            <Toast
              error={true}
              label={t(errorMsg ? errorMsg : "ES_COMMON_PLEASE_ENTER_ALL_MANDATORY_FIELDS")}
              isDleteBtn={true}
              onClose={closeToast}
            />
          )}
          {showSuccessToast && <Toast label={t(successMsg)} isDleteBtn={true} onClose={closeToast} />}
        </div>
      </div>
      {openConfirmCourtModal && <ConfirmCourtModal setOpenConfirmCourtModal={setOpenConfirmCourtModal} t={t} onSubmitCase={onSubmitCase} />}
      {openConfirmCorrectionModal && (
        <ConfirmCorrectionModal onCorrectionCancel={() => setOpenConfirmCorrectionModal(false)} onSubmit={onErrorCorrectionSubmit} />
      )}
      {isDisabled && (
        <div
          style={{
            width: "100vw",
            height: "100vh",
            zIndex: "9999",
            position: "fixed",
            right: "0",
            display: "flex",
            top: "0",
            background: "rgb(234 234 245 / 50%)",
            alignItems: "center",
            justifyContent: "center",
          }}
        >
          <Loader />
        </div>
      )}
    </div>
  );
}

export default EFilingCases;
