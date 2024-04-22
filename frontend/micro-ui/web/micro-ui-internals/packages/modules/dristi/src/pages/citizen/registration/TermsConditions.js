import { FormComposerV2, Header, Toast } from "@egovernments/digit-ui-react-components";
import React, { useState } from "react";
import { termsAndConditionConfig } from "./config";

function TermsConditions({ config, t }) {
  const { t } = useTranslation();
  const Digit = window.Digit || {};
  const tenantId = Digit.ULBService.getCurrentTenantId();
  const history = useHistory();
  const [showErrorToast, setShowErrorToast] = useState(false);
  const token = window.localStorage.getItem("token");
  const isUserLoggedIn = Boolean(token);
  const moduleCode = "DRISTI";
  const userInfo = JSON.parse(window.localStorage.getItem("user-info"));
  const { data, isLoading } = Digit.Hooks.dristi.useGetIndividualUser(
    {
      Individual: {
        userUuid: [userInfo?.uuid],
      },
    },
    { tenantId, limit: 1000, offset: 0 },
    moduleCode,
    userInfo?.uuid && isUserLoggedIn
  );
  const individualId = data?.Individual?.[0]?.individualId;

  if (isLoading) {
    return <Loader />;
  }

  if (Boolean(individualId)) {
    history.push(`${window?.contextPath}/dristi/home`);
  }
  const closeToast = () => {
    setShowErrorToast(false);
  };

  const onSubmit = (data) => {
    console.log("data", data);

    const uploadedDocument = Digit?.SessionStorage?.get("UploadedDocument");
    const aadhaarNumber = Digit?.SessionStorage?.get("aadharNumber");
    const identifierId = uploadedDocument ? uploadedDocument?.filedata?.files?.[0]?.fileStoreId : aadhaarNumber;
    const identifierType = uploadedDocument ? uploadedDocument?.IdType?.code : "ADHAAR";
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
              code: "USER_REGISTER",
              name: "USER_REGISTER",
              description: "USER_REGISTER",
              tenantId: tenantId,
            },
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
            doorNo: data?.addressDetails?.doorNo,
            latitude: data?.addressDetails?.coordinates?.latitude,
            longitude: data?.addressDetails?.coordinates?.longitude,
            city: data?.addressDetails?.city,
            pincode: data?.addressDetails?.pincode,
            district: data?.addressDetails?.district,
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
          fields: [{ key: "userType", value: data?.clientDetails?.selectUserType?.code }],
        },
        clientAuditDetails: {},
        auditDetails: {},
      },
    };
    Digit.DRISTIService.postIndividualService(Individual, tenantId)
      .then((individualRes) => {
        if (data?.clientDetails?.selectUserType?.apiDetails && data?.clientDetails?.selectUserType?.apiDetails?.serviceName) {
          onDocumentUpload(data?.clientDetails?.barCouncilId[0][1]?.file).then((document) => {
            console.log("document", document);
            const requestBody = {
              [data?.clientDetails?.selectUserType?.apiDetails?.requestKey]: [
                {
                  tenantId: tenantId,
                  individualId: individualRes?.Individual?.individualId,
                  isActive: true,
                  workflow: {
                    action: "REGISTER",
                    comments: `Applying for ${data?.clientDetails?.selectUserType?.apiDetails?.requestKey} registration`,
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
                    stateOfRegistration: data?.clientDetails?.stateOfRegistration?.name,
                    username:
                      data?.userDetails?.firstName && data?.userDetails?.lastName
                        ? `${data?.userDetails?.firstName} ${data?.userDetails?.lastName}`
                        : "",
                  },
                  ...data?.clientDetails?.selectUserType?.apiDetails?.AdditionalFields?.reduce((res, curr) => {
                    res[curr] = data?.clientDetails[curr];
                    return res;
                  }, {}),
                },
              ],
            };
            Digit.DRISTIService.complainantService(data?.clientDetails?.selectUserType?.apiDetails?.serviceName, requestBody, tenantId, true, {
              roles: [
                {
                  name: "Citizen",
                  code: "CITIZEN",
                  tenantId: "pg",
                },
                {
                  name: "USER_REGISTER",
                  code: "USER_REGISTER",
                  tenantId: "pg",
                },
              ],
            })
              .then(() => {
                history.push(`/digit-ui/citizen/dristi/home/response`, "success");
              })
              .catch(() => {
                history.push(`/digit-ui/citizen/dristi/home/response`, "error");
              });
          });
        } else history.push(`/digit-ui/citizen/dristi/home/response`, "success");
      })
      .catch(() => {
        history.push(`/digit-ui/citizen/dristi/home/response`, "error");
      });
  };

  return (
    <div className="employee-card-wrapper">
      <div className="header-content">
        <Header>{t("CS_COMMON_TERMS_&_CONDITION")}</Header>
      </div>
      <FormComposerV2
        config={termsAndConditionConfig.map((config) => {
          return {
            ...config,
            body: config.body.filter((a) => !a.hideInEmployee),
          };
        })}
        t={t}
        onSubmit={(props) => {
          onSubmit();
        }}
        noBoxShadow
        inline
        label={"CS_COMMON_SUBMIT"}
        description={"Description"}
        headingStyle={{ textAlign: "center" }}
        cardStyle={{ minWidth: "100%", paddingRight: "90vh" }}
        className="employeeForgotPassword"
      ></FormComposerV2>
      {showErrorToast && <Toast error={true} label={t("ES_COMMON_PLEASE_ENTER_ALL_MANDATORY_FIELDS")} isDleteBtn={true} onClose={closeToast} />}
    </div>
  );
}

export default TermsConditions;
