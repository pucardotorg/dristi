import { FormComposerV2, Header, Toast } from "@egovernments/digit-ui-react-components";
import React, { useState } from "react";
import { useTranslation } from "react-i18next";
import { useHistory } from "react-router-dom";
import { advocateClerkConfig } from "./config";
import DocViewerWrapper from "../../employee/docViewerWrapper";

function AdvocateClerkAdditionalDetail({ params, setParams, path, config }) {
  const { t } = useTranslation();
  const Digit = window.Digit || {};
  const history = useHistory();
  const [showErrorToast, setShowErrorToast] = useState(false);
  const [isDisabled, setIsDisabled] = useState(false);
  const tenantId = Digit.ULBService.getCurrentTenantId();
  console.log(params);
  const closeToast = () => {
    setShowErrorToast(false);
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
  const onFormValueChange = (setValue, formData, formState) => {
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
      });
    });
    if (isDisabled) {
      setIsDisabled(isDisabled);
    } else {
      setIsDisabled(false);
    }
  };
  const onDocumentUpload = async (fileData) => {
    const fileUploadRes = await Digit.UploadServices.Filestorage("DRISTI", fileData, tenantId);
    return { file: fileUploadRes?.data, fileType: fileData.type };
  };
  const uploadedDocument = Digit?.SessionStorage?.get("UploadedDocument");
  const onSubmit = (formData) => {
    if (!validateFormData(formData)) {
      setShowErrorToast(!validateFormData(formData));
      return;
    }
    const data = params?.userType;
    const Individual = params?.Individual;
    console.log(formData);
    const oldData = params;
    const aadhaarNumber = Digit?.SessionStorage?.get("aadharNumber");
    const identifierId = uploadedDocument ? uploadedDocument?.filedata?.files?.[0]?.fileStoreId : aadhaarNumber;
    const identifierType = uploadedDocument ? uploadedDocument?.IdType?.code : "AADHAR";
    Digit.DRISTIService.postIndividualService(Individual, tenantId)
      .then((result) => {
        if (
          data?.selectUserType?.apiDetails &&
          data?.selectUserType?.apiDetails?.serviceName &&
          result &&
          data?.selectUserType?.role === "ADVOCATE_ROLE"
        ) {
          onDocumentUpload(formData?.clientDetails?.barCouncilId[0][1]?.file, formData?.clientDetails?.barCouncilId[0][0]).then((document) => {
            const requestBody = {
              [data?.selectUserType?.apiDetails?.requestKey]: [
                {
                  tenantId: tenantId,
                  individualId: result?.Individual?.individualId,
                  isActive: false,
                  workflow: {
                    action: "REGISTER",
                    comments: `Applying for ${data?.selectUserType?.apiDetails?.requestKey} registration`,
                    documents: [
                      {
                        id: null,
                        documentType: document.fileType,
                        fileStore: document.file?.files?.[0]?.fileStoreId,
                        documentUid: "",
                        additionalDetails: {},
                      },
                    ],
                    assignes: [],
                    rating: null,
                  },
                  documents: [
                    {
                      id: null,
                      documentType: document.fileType,
                      fileStore: document.file?.files?.[0]?.fileStoreId,
                      documentUid: "",
                      additionalDetails: {},
                    },
                  ],
                  additionalDetails: {
                    username: oldData?.name?.firstName + " " + oldData?.name?.name,
                  },
                  ...data?.selectUserType?.apiDetails?.AdditionalFields?.reduce((res, curr) => {
                    res[curr] = formData?.clientDetails?.barRegistrationNumber;
                    return res;
                  }, {}),
                },
              ],
            };
            Digit.DRISTIService.advocateClerkService(data?.selectUserType?.apiDetails?.serviceName, requestBody, tenantId, true, {
              roles: [
                {
                  name: "Citizen",
                  code: "CITIZEN",
                  tenantId: tenantId,
                },
              ],
            })
              .then(() => {
                history.push(`/digit-ui/citizen/dristi/home/response`, {
                  response: "success",
                  createType: data?.selectUserType?.code,
                });
              })
              .catch(() => {
                history.push(`/digit-ui/citizen/dristi/home/response`, { response: "error" });
              });
          });
        } else {
          const requestBody = {
            [data?.selectUserType?.apiDetails?.requestKey]: [
              {
                tenantId: tenantId,
                individualId: result?.Individual?.individualId,
                isActive: false,
                workflow: {
                  action: "REGISTER",
                  comments: `Applying for ${data?.selectUserType?.apiDetails?.requestKey} registration`,
                  documents: [
                    {
                      id: null,
                      documentType: null,
                      fileStore: null,
                      documentUid: "",
                      additionalDetails: {},
                    },
                  ],
                  assignes: [],
                  rating: null,
                },
                documents: [
                  {
                    id: null,
                    documentType: null,
                    fileStore: null,
                    documentUid: "",
                    additionalDetails: {},
                  },
                ],
                additionalDetails: {
                  username: data?.userDetails?.firstName + " " + data?.userDetails?.lastName,
                },
                ...data?.selectUserType?.apiDetails?.AdditionalFields?.reduce((res, curr) => {
                  res[curr] = "DEFAULT_VALUE";
                  return res;
                }, {}),
              },
            ],
          };
          Digit.DRISTIService.advocateClerkService(data?.selectUserType?.apiDetails?.serviceName, requestBody, tenantId, true, {
            roles: [
              {
                name: "Citizen",
                code: "CITIZEN",
                tenantId: tenantId,
              },
            ],
          })
            .then(() => {
              history.push(`/digit-ui/citizen/dristi/home/response`, {
                response: "success",
                createType: data?.selectUserType?.code,
              });
            })
            .catch(() => {
              history.push(`/digit-ui/citizen/dristi/home/response`, { response: "error", createType: "LITIGANT" });
            });
        }
      })
      .catch(() => {
        history.push(`/digit-ui/citizen/dristi/home/response`, { response: "error", createType: "LITIGANT" });
      })
      .finally(() => {
        setParams({});
      });
    // setParams({
    //   ...params,
    //   ...data,
    // });
    console.log(params, formData);
    // history.push(`${path}/additional-details/terms-conditions`);
  };
  console.debug(advocateClerkConfig[0]);
  if (!params?.Individual) {
    history.push("/digit-ui/citizen/dristi/home/login");
  }
  return (
    <div className="employee-card-wrapper">
      <FormComposerV2
        config={advocateClerkConfig}
        t={t}
        onSubmit={(props) => {
          onSubmit(props);
        }}
        isDisabled={isDisabled}
        label={"CS_COMMONS_NEXT"}
        headingStyle={{ textAlign: "center" }}
        sectionHeadStyle={{ fontSize: "30px" }}
        defaultValues={{ ...params?.registrationData } || {}}
        cardStyle={{ minWidth: "100%", padding: 20, display: "flex", flexDirection: "column", alignItems: "center" }}
        submitInForm
        onFormValueChange={onFormValueChange}
        buttonStyle={{ margin: "20px", minWidth: "500px" }}
      ></FormComposerV2>

      {showErrorToast && <Toast error={true} label={t("ES_COMMON_PLEASE_ENTER_ALL_MANDATORY_FIELDS")} isDleteBtn={true} onClose={closeToast} />}
    </div>
  );
}

export default AdvocateClerkAdditionalDetail;
