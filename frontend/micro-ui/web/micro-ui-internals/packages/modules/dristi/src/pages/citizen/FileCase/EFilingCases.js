import React, { useEffect, useMemo, useState } from "react";
import { useTranslation } from "react-i18next";
import { CloseSvg, FormComposerV2, Header, Loader, Toast } from "@egovernments/digit-ui-react-components";
import { useHistory } from "react-router-dom/cjs/react-router-dom.min";
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

function EFilingCases({ path }) {
  const [params, setParmas] = useState({});
  const Digit = window?.Digit || {};
  const { t } = useTranslation();
  const history = useHistory();
  const [showErrorToast, setShowErrorToast] = useState(false);
  const [isDisabled, setIsDisabled] = useState(false);
  const [formdata, setFormdata] = useState([{ isenabled: true, data: {}, displayindex: 0 }]);
  const [{ setFormErrors, resetFormData }, setState] = useState({ setFormErrors: () => {}, resetFormData: () => {} });
  const urlParams = new URLSearchParams(window.location.search);
  const selected = urlParams.get("selected") || sideMenuConfig?.[0]?.children?.[0]?.key;
  const caseId = urlParams.get("caseId");
  const tenantId = window?.Digit.ULBService.getCurrentTenantId();
  const [parentOpen, setParentOpen] = useState(sideMenuConfig.findIndex((parent) => parent.children.some((child) => child.key === selected)));
  const [openConfigurationModal, setOpenConfigurationModal] = useState(false);
  const [openConfirmCourtModal, setOpenConfirmCourtModal] = useState(false);
  const { data: caseData, isLoading } = useSearchCaseService(
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

  const nextSelected = useMemo(() => {
    const index = getAllKeys.indexOf(selected);
    if (index !== -1 && index + 1 < getAllKeys.length) {
      return getAllKeys[index + 1];
    } else {
      return null;
    }
  }, [getAllKeys, selected]);

  const caseDetails = useMemo(() => caseData?.criteria?.[0]?.responseList?.[0], [caseData]);
  useEffect(() => {
    setParentOpen(sideMenuConfig.findIndex((parent) => parent.children.some((child) => child.key === selected)));
  }, [selected]);

  const accordion = useMemo(() => {
    return sideMenuConfig.map((parent, pIndex) => ({
      ...parent,
      isOpen: pIndex === parentOpen,
      children: parent.children.map((child, cIndex) => ({
        ...child,
        checked: child.key === selected,
      })),
    }));
  }, [parentOpen, selected]);

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
      return formdata.map(() => formConfig);
    }
    return formdata.map(({ data }) => {
      let disableConfigFields = [];
      return formConfig
        .filter((config) => {
          config.body.forEach((body) => {
            if ("disableConfigFields" in body && "disableConfigKey" in body && "key" in body) {
              if (!!data?.[body.key]?.[body.disableConfigKey]) {
                disableConfigFields = [...disableConfigFields, ...body.disableConfigFields];
              }
            }
          });
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
                          disable: true,
                          isDisabled: true,
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
              };
            }),
          };
        });
    });
  }, [isDependentEnabled, formdata, formConfig]);

  const activeForms = useMemo(() => {
    return formdata.filter((item) => item.isenabled === true).length;
  }, [formdata]);

  const handleAddForm = () => {
    setFormdata([...formdata, { isenabled: true, data: {}, displayindex: activeForms }]);
  };

  const handleDeleteForm = (index) => {
    const newArray = formdata.map((item, i) => ({ ...item, isenabled: index === i ? false : item.isenabled, displayindex: i < index ? i : i - 1 }));
    setFormdata(newArray);
  };

  const closeToast = () => {
    setShowErrorToast(false);
  };

  const onFormValueChange = (setValue, formData, formState, reset, setError, clearErrors, trigger, getValues, index) => {
    if (JSON.stringify(formData) !== JSON.stringify(formdata[index].data)) {
      setFormdata(
        formdata.map((item, i) => {
          return i === index ? { ...item, data: formData } : item;
        })
      );
    }
    if (!setFormErrors) {
      setState((prev) => ({
        ...prev,
        setFormErrors: setError,
        resetFormData: reset,
      }));
    }
  };

  const handleAccordionClick = (index) => {
    setParentOpen(index);
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
    setIsOpen(false);
    !!resetFormData && resetFormData();
    history.push(`?caseId=${caseId}&selected=${key}`);
  };

  const onDocumentUpload = async (fileData, filename) => {
    const fileUploadRes = await Digit.UploadServices.Filestorage("DRISTI", fileData, tenantId);
    return { file: fileUploadRes?.data, fileType: fileData.type, filename };
  };

  const validateData = (data) => {
    let isValid = true;
    formConfig.forEach((config) => {
      config?.body?.forEach((body) => {
        if (body?.type === "component") {
          body?.populators?.inputs?.forEach((input) => {
            if (input?.isMandatory) {
              if (input?.validation) {
                if (input?.validation?.isArray) {
                  formdata?.forEach((data) => {
                    if (!data?.data?.[body.key]?.[input.name] || formdata?.[body.key]?.[input.name]?.length === 0) {
                      isValid = false;
                      // setFormErrors(body.key, { [input.name]: "Please Enter the mandatory field" });
                    } else {
                      // setFormErrors(body.key, { [input.name]: "" });
                    }
                  });
                } else {
                  formdata?.forEach((data) => {
                    if (!data?.data?.[body.key]?.[input.name]) {
                      isValid = false;
                      // setFormErrors(body.key, { [input.name]: "Please Enter the mandatory field" });
                    } else {
                      // setFormErrors(body.key, { [input.name]: "Please Enter the mandatory field" });
                    }
                  });
                }
              }
            }
          });
        }
      });
    });
    return isValid;
  };

  const onSubmit = async (props, index) => {
    // if (!validateData(props, index)) {
    //   return null;
    // }
    const data = {};
    let isMandatoryFields = false;
    if (selected === "complaintDetails") {
      const litigants = [];
      formdata.forEach((data, index) => {
        if (data?.data?.complainantVerification?.individualDetails) {
          litigants.push({
            tenantId,
            caseId: caseDetails?.id,
            partyCategory: data?.data?.complainantType?.code,
            individualId: data?.data?.complainantVerification?.individualDetails,
            partyType: index === 0 ? "complainant.primary" : "complainant.additional",
          });
        } else {
          if (data?.data?.complainantId?.complainantId) {
            if (data?.data?.complainantId?.complainantId?.verificationType !== "AADHAR") {
            }
            // debugger;
          } else {
            // setShowErrorToast(true);
            // return;
          }
        }
      });

      const representatives = [...caseDetails?.representatives]?.map((representative) => ({
        ...representative,
        caseId: caseDetails?.id,
        representing: [...litigants],
      }));
      data.litigants = litigants;
      data.representatives = representatives;
    }
    if (selected === "respondentDetails") {
      let documentData = {};
      const newFormData = await Promise.all(
        formdata.map(async (data) => {
          if (data?.data?.condonationFileUpload?.document) {
            await onDocumentUpload(data?.data?.condonationFileUpload?.document?.[0], data?.data?.condonationFileUpload?.document?.[0]?.name).then(
              (data) => {
                documentData = {
                  documentType: data.fileType,
                  fileStore: data.file?.files?.[0]?.fileStoreId,
                  documentName: data.filename,
                };
              }
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
      data.additionalDetails = { respondentDetails: newFormData };
    }
    if (selected === "chequeDetails") {
      const documentData = {};
      const newFormData = await Promise.all(
        formdata.map(async (data) => {
          if (data?.data?.bouncedChequeFileUpload?.document) {
            await onDocumentUpload(data?.data?.bouncedChequeFileUpload?.document?.[0], data?.data?.bouncedChequeFileUpload?.document?.[0]?.name).then(
              (data) => {
                documentData.bouncedChequeFileUpload = {
                  documentType: data.fileType,
                  fileStore: data.file?.files?.[0]?.fileStoreId,
                  documentName: data.filename,
                };
              }
            );
          }
          if (data?.data?.depositChequeFileUpload?.document) {
            await onDocumentUpload(data?.data?.depositChequeFileUpload?.document?.[0], data?.data?.depositChequeFileUpload?.document?.[0]?.name).then(
              (data) => {
                documentData.depositChequeFileUpload = {
                  documentType: data.fileType,
                  fileStore: data.file?.files?.[0]?.fileStoreId,
                  documentName: data.filename,
                };
              }
            );
          }
          if (data?.data?.returnMemoFileUpload?.document) {
            await onDocumentUpload(data?.data?.returnMemoFileUpload?.document?.[0], data?.data?.returnMemoFileUpload?.document?.[0]?.name).then(
              (data) => {
                documentData.returnMemoFileUpload = {
                  documentType: data.fileType,
                  fileStore: data.file?.files?.[0]?.fileStoreId,
                  documentName: data.filename,
                };
              }
            );
          }
          return {
            ...data,
            data: {
              ...data.data,
              ...documentData,
            },
          };
        })
      );
      data.caseDetails = { chequeDetails: newFormData };
    }
    if (selected === "addSignature") {
      setOpenConfirmCourtModal(true);
      return;
    }
    DRISTIService.caseUpdateService({ cases: { ...caseDetails, ...data, filingDate: formatDate(new Date()) }, tenantId }, tenantId);
    history.push(`?caseId=${caseId}&selected=${nextSelected}`);
  };
  const onSaveDraft = (props) => {
    setParmas({ ...params, [pageConfig.key]: formdata });
    setFormdata([{ isenabled: true, data: {}, displayindex: 0 }]);
  };

  const onSubmitCase = (data) => {
    console.debug(data);
    setOpenConfirmCourtModal(false);
  };

  const [isOpen, setIsOpen] = useState(false);
  if (isLoading) {
    return <Loader />;
  }
  return (
    <div className="file-case">
      <div className="file-case-side-stepper">
        <div className="side-stepper-info">
          <div className="header">
            <InfoIcon />
            <span>You are filing a case</span>
          </div>
          <p>
            Under
            <span className="act-name"> S-138, Negotiable Instrument Act</span> In
            <span className="place-name"> Kollam S 138 Special Court</span>
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
            <p>{t(pageConfig.subtext || "Please provide the necessary details")}</p>
          </div>
          {modifiedFormConfig.map((config, index) => {
            return formdata[index].isenabled ? (
              <div key={index} className="form-wrapper-d">
                {pageConfig?.addFormText && (
                  <div className="form-item-name">
                    <h1>{`${pageConfig?.formItemName} ${formdata[index]?.displayindex + 1}`}</h1>
                    {(activeForms > 1 || pageConfig?.isOptional) && (
                      <span
                        style={{ cursor: "pointer" }}
                        onClick={() => {
                          handleDeleteForm(index);
                        }}
                      >
                        <CustomDeleteIcon />
                      </span>
                    )}
                  </div>
                )}
                <FormComposerV2
                  label={t("CS_COMMON_CONTINUE")}
                  config={config}
                  onSubmit={(data) => onSubmit(data, index)}
                  onSecondayActionClick={onSaveDraft}
                  defaultValues={formdata[index]?.data}
                  onFormValueChange={(setValue, formData, formState, reset, setError, clearErrors, trigger, getValues) => {
                    onFormValueChange(setValue, formData, formState, reset, setError, clearErrors, trigger, getValues, index);
                  }}
                  cardStyle={{ minWidth: "100%" }}
                  isDisabled={isDisabled}
                  cardClassName={`e-filing-card-form-style ${pageConfig.className}`}
                  secondaryLabel={t("CS_SAVE_DRAFT")}
                  showSecondaryLabel={true}
                  actionClassName="e-filing-action-bar"
                  noBreakLine
                />
              </div>
            ) : null;
          })}
          {pageConfig?.addFormText && (
            <div onClick={handleAddForm} className="add-new-form">
              <CustomAddIcon />
              <span>{t(pageConfig.addFormText)}</span>
            </div>
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
        </div>
      </div>
      {openConfirmCourtModal && <ConfirmCourtModal setOpenConfirmCourtModal={setOpenConfirmCourtModal} t={t} onSubmitCase={onSubmitCase} />}
    </div>
  );
}

export default EFilingCases;
