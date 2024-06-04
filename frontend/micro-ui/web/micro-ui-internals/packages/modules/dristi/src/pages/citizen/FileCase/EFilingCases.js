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
import { generateUUID } from "../../../Utils";

function EFilingCases({ path }) {
  const [params, setParmas] = useState({});
  const Digit = window?.Digit || {};
  const { t } = useTranslation();
  const history = useHistory();
  const [showErrorToast, setShowErrorToast] = useState(false);
  const [isDisabled, setIsDisabled] = useState(false);
  const [formdata, setFormdata] = useState([{ isenabled: true, data: {}, displayindex: 0 }]);
  const [{ setFormErrors, resetFormData }, setState] = useState({
    setFormErrors: () => {},
    resetFormData: () => {},
  });
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

  const caseDetails = useMemo(
    () => ({
      ...caseData?.criteria?.[0]?.responseList?.[0],
      additionalDetails: {
        complaintDetails: [
          {
            isenabled: true,
            data: {
              complainantType: {
                code: "INDIVIDUAL",
                name: "Individual",
                showCompanyDetails: false,
                commonFields: true,
                isEnabled: true,
              },
              "addressDetails-select": {
                pincode: "500032",
                state: "Telangana",
                district: "Rangareddy",
                city: "Kondapur",
                locality: "F84X+6P6",
              },
              complainantId: true,
              firstName: "fgnf",
              middleName: "",
              lastName: "hff",
              complainantVerification: {
                mobileNumber: "9304619513",
                otpNumber: "123456",
                individualDetails: "IND-2024-04-30-000018",
                isUserVerified: true,
              },
              addressDetails: {
                pincode: "500032",
                state: "Telangana",
                district: "Rangareddy",
                city: "Kondapur",
                coordinates: {
                  longitude: 78.3500765,
                  latitude: 17.4549784,
                },
                locality: "F84X+6P6",
                uuid: "0a03dfb7-44bd-456b-8402-bc0b28c0a571",
              },
            },
            displayindex: 0,
          },
        ],
        respondentDetails: [
          {
            isenabled: true,
            data: {
              respondentType: {
                code: "INDIVIDUAL",
                name: "Individual",
                showCompanyDetails: false,
                commonFields: true,
                isEnabled: true,
              },
              firstName: "dfdfg",
              lastName: "dfgdfg",
              phonenumbers: {
                textfieldValue: "",
                mobileNumber: ["7546456456"],
              },
              emails: {
                textfieldValue: "",
                emailId: ["sdfsdf@sdfsdf.dfg"],
              },
              addressDetails: [
                {
                  id: "0e5bc8e8-702c-46c8-bf9b-e000636ba5b8",
                  addressDetails: {
                    pincode: "500032",
                    state: "Telangana",
                    district: "Rangareddy",
                    city: "Kondapur",
                    coordinates: {
                      longitude: 78.3500765,
                      latitude: 17.4549784,
                    },
                    locality: "F84X+6P6",
                    doorNo: "dfgdgdg",
                  },
                },
              ],
              condonationFileUpload: {
                document: [
                  {
                    documentType: "application/pdf",
                    fileStore: "f37cdcea-a594-49fd-92c4-b16221127ebd",
                    documentName: "npm.pdf",
                  },
                ],
              },
            },
            displayindex: 0,
          },
        ],
      },
    }),
    [caseData]
  );
  useEffect(() => {
    setParentOpen(sideMenuConfig.findIndex((parent) => parent.children.some((child) => child.key === selected)));
  }, [selected]);

  useEffect(() => {
    const data = caseDetails?.additionalDetails?.[selected] ||
      caseDetails?.caseDetails?.[selected] || [{ isenabled: true, data: {}, displayindex: 0 }];
    setFormdata(data);
  }, [selected, caseDetails]);

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
              if (body?.addUUID) {
                body.uuid = index;
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
          setValue();
          return i === index
            ? {
                ...item,
                data: formData,
              }
            : item;
        })
      );
      // setIsFirstRender(false);
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
    if (!!resetFormData) {
      resetFormData();
    }
    setIsOpen(false);
    history.push(`?caseId=${caseId}&selected=${key}`);
  };

  const onDocumentUpload = async (fileData, filename) => {
    const fileUploadRes = await Digit.UploadServices.Filestorage("DRISTI", fileData, tenantId);
    return { file: fileUploadRes?.data, fileType: fileData.type, filename };
  };
  // const validateData = (data) => {
  //   let isValid = true;
  //   formConfig.forEach((config) => {
  //     config?.body?.forEach((body) => {
  //       if (body?.type === "component") {
  //         body?.populators?.inputs?.forEach((input) => {
  //           if (input?.isMandatory) {
  //             if (input?.validation) {
  //               if (input?.validation?.isArray) {
  //                 formdata?.forEach((data) => {
  //                   if (!data?.data?.[body.key]?.[input.name] || formdata?.[body.key]?.[input.name]?.length === 0) {
  //                     isValid = false;
  //                     // setFormErrors(body.key, { [input.name]: "Please Enter the mandatory field" });
  //                   } else {
  //                     // setFormErrors(body.key, { [input.name]: "" });
  //                   }
  //                 });
  //               } else {
  //                 formdata?.forEach((data) => {
  //                   if (!data?.data?.[body.key]?.[input.name]) {
  //                     isValid = false;
  //                     // setFormErrors(body.key, { [input.name]: "Please Enter the mandatory field" });
  //                   } else {
  //                     // setFormErrors(body.key, { [input.name]: "Please Enter the mandatory field" });
  //                   }
  //                 });
  //               }
  //             }
  //           }
  //         });
  //       }
  //     });
  //   });
  //   return isValid;
  // };
  const createIndividualUser = async (data, documentData, verificationType) => {
    const identifierId = documentData ? documentData?.filedata?.files?.[0]?.fileStoreId : data?.data;
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
          givenName: data?.userDetails?.firstName,
          familyName: data?.userDetails?.lastName,
          otherNames: data?.userDetails?.middleName,
        },
        userDetails: {
          username: Digit.UserService.getUser()?.info?.userName,
          roles: [
            {
              code: "CITIZEN",
              name: "Citizen",
              tenantId: tenantId,
            },
            ...["CASE_CREATOR", "CASE_EDITOR", "CASE_VIEWER"]?.map((role) => ({
              code: role,
              name: role,
              tenantId: tenantId,
            })),
          ],

          type: Digit.UserService.getUser()?.info?.type,
        },
        userUuid: Digit.UserService.getUser()?.info?.uuid,
        userId: Digit.UserService.getUser()?.info?.id,
        mobileNumber: Digit.UserService.getUser()?.info?.mobileNumber,
        address: [
          {
            tenantId: tenantId,
            type: "PERMANENT",
            doorNo: data?.addressDetails?.doorNo,
            latitude: data?.addressDetails?.coordinates?.latitude,
            longitude: data?.addressDetails?.coordinates?.longitude,
            city: data?.addressDetails?.city,
            pincode: data?.addressDetails?.pincode,
            addressLine1: data?.addressDetails?.state,
            addressLine2: data?.addressDetails?.district,
            buildingName: data?.addressDetails?.buildingName,
            landmark: data?.addressDetails?.locality,
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
            { key: "userType", value: data?.clientDetails?.selectUserType?.code },
            { key: "userTypeDetail", value: JSON.stringify(data?.clientDetails?.selectUserType) },
            // { key: "termsAndCondition", value: termsAndConditionData?.Terms_Conditions },
            // { key: "identifierIdDetails", value: JSON.stringify(identifierIdDetails) },
          ],
        },
        clientAuditDetails: {},
        auditDetails: {},
      },
    };
    return await Digit.DRISTIService.postIndividualService(Individual, tenantId);
  };

  const onSubmit = async (props, index) => {
    // if (!validateData(props, index)) {
    //   return null;
    // }
    const data = {};
    if (selected === "complaintDetails") {
      const litigants = [];
      formdata.forEach(async (data, index) => {
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
              const documentData = await onDocumentUpload(
                data?.data?.complainantId?.complainantId?.complainantId?.ID_Proof?.[0]?.[1]?.file,
                data?.data?.complainantId?.complainantId?.complainantId?.ID_Proof?.[0]?.[0]
              );
              debugger;
              await createIndividualUser(data?.data, documentData, data?.data?.complainantId?.complainantId?.verificationType);
            }
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
      data.additionalDetails = { ...caseDetails.additionalDetails, complaintDetails: formdata };
    }
    if (selected === "respondentDetails") {
      const newFormData = await Promise.all(
        formdata.map(async (data) => {
          let documentData = [];
          if (data?.data?.condonationFileUpload?.document) {
            documentData = await Promise.all(
              data?.data?.condonationFileUpload?.document?.map(async (document) => {
                return await onDocumentUpload(document, document.name).then((data) => {
                  return {
                    documentType: data.fileType,
                    fileStore: data.file?.files?.[0]?.fileStoreId,
                    documentName: data.filename,
                  };
                });
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
      data.additionalDetails = { ...caseDetails.additionalDetails, respondentDetails: newFormData };
    }
    if (selected === "chequeDetails") {
      const documentData = {};
      const newFormData = await Promise.all(
        formdata.map(async (data) => {
          if (data?.data?.bouncedChequeFileUpload?.document) {
            documentData.bouncedChequeFileUpload = await Promise.all(
              data?.data?.bouncedChequeFileUpload?.document?.map(async (document) => {
                return await onDocumentUpload(document, document.name).then((data) => {
                  return {
                    documentType: data.fileType,
                    fileStore: data.file?.files?.[0]?.fileStoreId,
                    documentName: data.filename,
                  };
                });
              })
            );
          }
          if (data?.data?.depositChequeFileUpload?.document) {
            documentData.depositChequeFileUpload = await Promise.all(
              data?.data?.depositChequeFileUpload?.document?.map(async (document) => {
                return await onDocumentUpload(document, document.name).then((data) => {
                  return {
                    documentType: data.fileType,
                    fileStore: data.file?.files?.[0]?.fileStoreId,
                    documentName: data.filename,
                  };
                });
              })
            );
          }
          if (data?.data?.returnMemoFileUpload?.document) {
            documentData.returnMemoFileUpload = await Promise.all(
              data?.data?.returnMemoFileUpload?.document?.map(async (document) => {
                return await onDocumentUpload(document, document.name).then((data) => {
                  return {
                    documentType: data.fileType,
                    fileStore: data.file?.files?.[0]?.fileStoreId,
                    documentName: data.filename,
                  };
                });
              })
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
      data.caseDetails = { ...caseDetails.caseDetails, chequeDetails: newFormData };
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
              <div key={selected} className="form-wrapper-d">
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
                  key={selected}
                  label={t("CS_COMMON_CONTINUE")}
                  config={config}
                  onSubmit={(data) => onSubmit(data, index)}
                  onSecondayActionClick={onSaveDraft}
                  defaultValues={
                    caseDetails?.additionalDetails?.[selected]?.[index]?.data ||
                    caseDetails?.caseDetails?.[selected]?.[index]?.data ||
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
