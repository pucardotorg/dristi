import { FormComposerV2, Toast } from "@egovernments/digit-ui-react-components";
import React, { useState } from "react";
import { useTranslation } from "react-i18next";
import { useHistory } from "react-router-dom";
import { advocateClerkConfig } from "./config";
import { getUserDetails, setCitizenDetail } from "../../../hooks/useGetAccessToken";

function AdvocateClerkAdditionalDetail({ params, setParams, path, config, pathOnRefresh }) {
  const { t } = useTranslation();
  const Digit = window.Digit || {};
  const history = useHistory();
  const [showErrorToast, setShowErrorToast] = useState(false);
  const [isDisabled, setIsDisabled] = useState(false);
  const [showSuccess, setShowSuccess] = useState(false);

  const tenantId = Digit.ULBService.getCurrentTenantId();
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
            setValue(key, clientValue);
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
  const onDocumentUpload = async (fileData) => {
    const fileUploadRes = await Digit.UploadServices.Filestorage("DRISTI", fileData, tenantId);
    return { file: fileUploadRes?.data, fileType: fileData.type };
  };

  const onSubmit = (formData) => {
    if (!validateFormData(formData)) {
      setShowErrorToast(!validateFormData(formData));
      return;
    }
    const data = params?.userType?.clientDetails;
    const Individual = params?.IndividualPayload ? params?.IndividualPayload : { Individual: params?.Individual?.[0] };
    const oldData = params;
    if (!params?.Individual?.[0]?.individualId) {
      Digit.DRISTIService.postIndividualService(Individual, tenantId)
        .then((result) => {
          if (
            data?.selectUserType?.apiDetails &&
            data?.selectUserType?.apiDetails?.serviceName &&
            result &&
            data?.selectUserType?.role[0] === "ADVOCATE_ROLE"
          ) {
            onDocumentUpload(formData?.clientDetails?.barCouncilId[0][1]?.file, formData?.clientDetails?.barCouncilId[0][0]).then((document) => {
              const requestBody = {
                [data?.selectUserType?.apiDetails?.requestKey]: {
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
                        additionalDetails: {
                          fileName: formData?.clientDetails?.barCouncilId[0][0],
                        },
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
                      additionalDetails: {
                        fileName: formData?.clientDetails?.barCouncilId[0][0],
                      },
                    },
                  ],
                  additionalDetails: {
                    username: oldData?.name?.firstName + " " + oldData?.name?.lastName,
                    userType: params?.userType,
                  },
                  ...data?.selectUserType?.apiDetails?.AdditionalFields?.reduce((res, curr) => {
                    res[curr] = formData?.clientDetails?.[curr];
                    return res;
                  }, {}),
                },
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
                  setShowSuccess(true);
                  const refreshToken = window.localStorage.getItem("citizen.refresh-token");
                  if (refreshToken) {
                    getUserDetails(refreshToken).then((res) => {
                      const { ResponseInfo, UserRequest: info, ...tokens } = res;
                      const user = { info, ...tokens };
                      window?.Digit.SessionStorage.set("citizen.userRequestObject", user);
                      window?.Digit.UserService.setUser(user);
                      setCitizenDetail(user?.info, user?.access_token, window?.Digit.ULBService.getStateId());
                      history.push(`/${window?.contextPath}/citizen/dristi/home`);
                    });
                  }
                })
                .catch(() => {
                  history.push(`/digit-ui/citizen/dristi/home/response`, { response: "error" });
                });
            });
          }
        })
        .catch(() => {
          history.push(`/digit-ui/citizen/dristi/home/response`, { response: "error", createType: "LITIGANT" });
        })
        .finally(() => {
          setShowSuccess(true);

          setParams({});
        });
      setParams({
        ...params,
        ...formData,
      });
    } else if (params?.Individual?.[0]?.individualId) {
      if (data?.selectUserType?.apiDetails && data?.selectUserType?.apiDetails?.serviceName && data?.selectUserType?.role[0] === "ADVOCATE_ROLE") {
        onDocumentUpload(formData?.clientDetails?.barCouncilId[0][1]?.file, formData?.clientDetails?.barCouncilId[0][0]).then((document) => {
          const requestBody = {
            [data?.selectUserType?.apiDetails?.requestKey]: {
              tenantId: tenantId,
              individualId: params?.Individual?.[0]?.individualId,
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
                    additionalDetails: {
                      fileName: formData?.clientDetails?.barCouncilId[0][0],
                    },
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
                  additionalDetails: {
                    fileName: formData?.clientDetails?.barCouncilId[0][0],
                  },
                },
              ],
              additionalDetails: {
                username: oldData?.name?.firstName + " " + oldData?.name?.lastName,
                userType: params?.userType,
              },
              ...data?.selectUserType?.apiDetails?.AdditionalFields?.reduce((res, curr) => {
                res[curr] = formData?.clientDetails?.[curr];
                return res;
              }, {}),
            },
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
              setShowSuccess(true);
              const refreshToken = window.localStorage.getItem("citizen.refresh-token");
              if (refreshToken) {
                getUserDetails(refreshToken).then((res) => {
                  const { ResponseInfo, UserRequest: info, ...tokens } = res;
                  const user = { info, ...tokens };
                  window?.Digit.SessionStorage.set("citizen.userRequestObject", user);
                  window?.Digit.UserService.setUser(user);
                  setCitizenDetail(user?.info, user?.access_token, window?.Digit.ULBService.getStateId());
                  history.push(`/${window?.contextPath}/citizen/dristi/home`);
                });
              }
            })
            .catch(() => {
              history.push(`/digit-ui/citizen/dristi/home/response`, { response: "error" });
            });
        });
      }
    }
  };
  if (!params?.IndividualPayload && showSuccess == false) {
    history.push(pathOnRefresh);
  }
  return (
    <div className="advocate-additional-details">
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
