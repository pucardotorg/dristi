import { FormComposerV2, Toast } from "@egovernments/digit-ui-react-components";
import React, { useEffect, useState } from "react";
import { useHistory } from "react-router-dom/cjs/react-router-dom.min";
import { getUserDetails, setCitizenDetail } from "../../../hooks/useGetAccessToken";

const SelectUserType = ({ config, t, params = {}, setParams = () => {}, pathOnRefresh, userTypeRegister }) => {
  const Digit = window.Digit || {};
  const tenantId = Digit.ULBService.getCurrentTenantId();
  const history = useHistory();
  const [showErrorToast, setShowErrorToast] = useState(false);
  const [showUsename, setshowUsename] = useState(false);

  const closeToast = () => {
    setShowErrorToast(false);
  };
  useEffect(() => {
    const timer = setTimeout(() => {
      closeToast();
    }, 2000);

    return () => clearTimeout(timer);
  }, [closeToast]);
  const validateFormData = (data) => {
    let isValid = true;
    config.forEach((curr) => {
      if (!isValid) return;
      if (!(curr.body[0].key in data) || !data[curr.body[0].key]) {
        isValid = false;
      }
      curr.body[0].populators.inputs.forEach((input) => {
        if (!isValid) return;
        if (Array.isArray(input.name)) return;
        if (input.disableMandatoryFieldFor) {
          if (input.disableMandatoryFieldFor.some((field) => !data[curr.body[0].key][field]) && data[curr.body[0].key][input.name]) {
            if (Array.isArray(data[curr.body[0].key][input.name]) && data[curr.body[0].key][input.name].length === 0) {
              isValid = false;
            }
            if ((input?.isMandatory && !(input.name in data[curr.body[0].key])) || !data[curr.body[0].key][input.name]) {
              isValid = false;
            }
            return;
          } else {
            if (
              (input?.isMandatory && !(input.name in data[curr.body[0].key])) ||
              (!data[curr.body[0].key][input.name] && !input.disableMandatoryFieldFor.some((field) => data[curr.body[0].key][field]))
            ) {
              isValid = false;
            }
          }
          return;
        } else {
          if (Array.isArray(data[curr.body[0].key][input.name]) && data[curr.body[0].key][input.name].length === 0) {
            isValid = false;
          }
          if (input?.isMandatory && !(input.name in data[curr.body[0].key])) {
            isValid = false;
          }
        }
      });
    });
    return isValid;
  };

  const [isDisabled, setIsDisabled] = useState(false);
  const onFormValueChange = (setValue, formData, formState) => {
    let isDisabled = false;
    config.forEach((curr) => {
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
  const onSubmit = (userType) => {
    const data = params;
    const userTypeSelcted = userType?.clientDetails?.selectUserType?.code;
    const uploadedDocument = Digit?.SessionStorage?.get("UploadedDocument");
    const aadhaarNumber = Digit?.SessionStorage?.get("aadharNumber");
    const identifierId = uploadedDocument ? uploadedDocument?.filedata?.files?.[0]?.fileStoreId : data?.adhaarNumber;
    const identifierIdDetails = uploadedDocument
      ? {
        fileStoreId: identifierId,
        filename: uploadedDocument?.filename,
      }
      : {};
    const identifierType = uploadedDocument ? uploadedDocument?.IdType?.code : "AADHAR";
    setParams({ ...params, userType });
    let Individual = {
      Individual: {
        tenantId: tenantId,
        name: {
          givenName: data?.name?.firstName,
          familyName: data?.name?.lastName,
          otherNames: data?.name?.middleName,
        },
        userDetails: {
          username: Digit.UserService.getUser()?.info?.userName,
          roles: userType?.clientDetails?.selectUserType?.role
            ? [
              {
                code: "CITIZEN",
                name: "Citizen",
                tenantId: tenantId,
              },
              ...userType?.clientDetails?.selectUserType?.role?.map((role) => ({
                code: role,
                name: role,
                tenantId: tenantId,
              })),
            ]
            : [
              {
                code: "CITIZEN",
                name: "Citizen",
                tenantId: tenantId,
              },
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
            latitude: data?.address?.addressDetails?.coordinates?.latitude,
            longitude: data?.address?.addressDetails?.coordinates?.longitude,
            city: data?.address?.addressDetails?.city,
            pincode: data?.address?.addressDetails?.pincode,
            addressLine1: data?.address?.addressDetails?.state,
            addressLine2: data?.address?.addressDetails?.district,
            street: data?.address?.addressDetails?.locality,
            doorNo: data?.address?.addressDetails?.doorNo,
            buildingName: data?.address?.addressDetails?.buildingName,
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
            { key: "userType", value: userTypeSelcted },
            { key: "userTypeDetail", value: JSON.stringify(userType?.clientDetails?.selectUserType) },
            { key: "termsAndCondition", value: true },
            { key: "identifierIdDetails", value: JSON.stringify(identifierIdDetails) },
          ],
        },

        clientAuditDetails: {},
        auditDetails: {},
      },
    };
    setParams({
      ...params,
      IndividualPayload: {
        ...Individual,
      },
      userType: {
        ...userType,
      },
    });
    if ((userTypeSelcted === "LITIGANT" || userTypeSelcted === "ADVOCATE_CLERK") && !data?.Individual?.[0]?.individualId) {
      Digit.DRISTIService.postIndividualService(Individual, tenantId)
        .then((result) => {
          if (userType?.clientDetails?.selectUserType?.apiDetails && userType?.clientDetails?.selectUserType?.apiDetails?.serviceName && result) {
            const requestBody = {
              [userType?.clientDetails?.selectUserType?.apiDetails?.requestKey]: {
                tenantId: tenantId,
                individualId: result?.Individual?.individualId,
                isActive: false,
                workflow: {
                  action: "REGISTER",
                  comments: `Applying for ${userType?.clientDetails?.selectUserType?.apiDetails?.requestKey} registration`,
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
                  username: data?.name?.firstName + " " + data?.name?.lastName,
                  userType: userType,
                },
                ...userType?.clientDetails?.selectUserType?.apiDetails?.AdditionalFields?.reduce((res, curr) => {
                  res[curr] = "DEFAULT_VALUE";
                  return res;
                }, {}),
              },
            };
            Digit.DRISTIService.advocateClerkService(userType?.clientDetails?.selectUserType?.apiDetails?.serviceName, requestBody, tenantId, true, {
              roles: [
                {
                  name: "Citizen",
                  code: "CITIZEN",
                  tenantId: tenantId,
                },
              ],
            })
              .then(() => {
                setshowUsename(true);

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
              })
              .finally(() => {
                setParams({});
              });
          } else {
            history.push(`/digit-ui/citizen/dristi/home/response`, {
              response: "success",
              createType: data?.userType?.clientDetails?.selectUserType?.code,
            });
          }
        })
        .catch(() => {
          history.push(`/digit-ui/citizen/dristi/home/response`, { response: "error" });
        })
        .finally(() => {
          setshowUsename(true);
          setParams({});
        });
    } else if (userTypeSelcted === "ADVOCATE_CLERK" && data?.Individual?.[0]?.individualId) {
      if (userType?.clientDetails?.selectUserType?.apiDetails && userType?.clientDetails?.selectUserType?.apiDetails?.serviceName) {
        const requestBody = {
          [userType?.clientDetails?.selectUserType?.apiDetails?.requestKey]: {
            tenantId: tenantId,
            individualId: data?.Individual?.[0]?.individualId,
            isActive: false,
            workflow: {
              action: "REGISTER",
              comments: `Applying for ${userType?.clientDetails?.selectUserType?.apiDetails?.requestKey} registration`,
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
              username: data?.name?.firstName + " " + data?.name?.lastName,
              userType: userType,
            },
            ...userType?.clientDetails?.selectUserType?.apiDetails?.AdditionalFields?.reduce((res, curr) => {
              res[curr] = "DEFAULT_VALUE";
              return res;
            }, {}),
          },
        };
        Digit.DRISTIService.advocateClerkService(userType?.clientDetails?.selectUserType?.apiDetails?.serviceName, requestBody, tenantId, true, {
          roles: [
            {
              name: "Citizen",
              code: "CITIZEN",
              tenantId: tenantId,
            },
          ],
        })
          .then(() => {
            setshowUsename(true);

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
          })
          .finally(() => {
            setParams({});
          });
      } else {
        history.push(`/digit-ui/citizen/dristi/home/response`, {
          response: "success",
          createType: data?.userType?.clientDetails?.selectUserType?.code,
        });
      }
    } else {
      history.push(`/digit-ui/citizen/dristi/home/registration/additional-details`);
    }
  };
  if (!params?.indentity && showUsename == false && !params?.Individual?.[0]?.additionalFields) {
    history.push(pathOnRefresh);
  }
  return (
    <div className="select-user">
      <FormComposerV2
        config={config}
        t={t}
        noBoxShadow
        inline
        label={t("CS_COMMON_CONTINUE")}
        onSubmit={(props) => {
          if (!validateFormData(props)) {
            setShowErrorToast(!validateFormData(props));
          } else {
            onSubmit(props);
          }
          return;
        }}
        onFormValueChange={onFormValueChange}
        isDisabled={isDisabled}
        value={params?.userType || (userTypeRegister && userTypeRegister) || {}}
        defaultValues={params?.userType || (userTypeRegister && userTypeRegister) || {}}
        headingStyle={{ textAlign: "center" }}
        cardStyle={{ minWidth: "100%", padding: 20, display: "flex", flexDirection: "column" }}
        sectionHeadStyle={{ marginBottom: "20px", fontSize: "40px" }}
        buttonStyle={{ alignSelf: "center", minWidth: "50%" }}
        submitInForm
      ></FormComposerV2>
      {showErrorToast && <Toast error={true} label={t("ES_COMMON_PLEASE_ENTER_ALL_MANDATORY_FIELDS")} isDleteBtn={true} onClose={closeToast} />}
    </div>
  );
};

export default SelectUserType;
