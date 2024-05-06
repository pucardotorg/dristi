import { FormComposerV2, Header, Toast } from "@egovernments/digit-ui-react-components";
import React, { useState } from "react";
import { termsAndConditionConfig } from "./config";
import { useTranslation } from "react-i18next";
import { useHistory } from "react-router-dom";

function TermsConditions({ params = {}, setParams = () => {} }) {
  const { t } = useTranslation();
  const Digit = window.Digit || {};
  const tenantId = Digit.ULBService.getCurrentTenantId();
  const history = useHistory();
  const [showErrorToast, setShowErrorToast] = useState(false);

  const closeToast = () => {
    setShowErrorToast(false);
  };

  // const onDocumentUpload = async (fileData) => {
  //   const fileUploadRes = await Digit.UploadServices.Filestorage("DRISTI", fileData, tenantId);
  //   return { file: fileUploadRes?.data, fileType: fileData.type };
  // };

  const onSubmit = (termsAndConditionData) => {
    if (!termsAndConditionData?.Terms_Conditions) {
      setShowErrorToast(t("ES_COMMON_PLEASE_ENTER_ALL_MANDATORY_FIELDS"));
      setTimeout(() => setShowErrorToast(null), 1500);
      return;
    }
    const data = params?.registrationData;
    setParams({ ...params, ...termsAndConditionData });
    const uploadedDocument = Digit?.SessionStorage?.get("UploadedDocument");
    const aadhaarNumber = Digit?.SessionStorage?.get("aadharNumber");
    const identifierId = uploadedDocument ? uploadedDocument?.filedata?.files?.[0]?.fileStoreId : aadhaarNumber;
    const identifierType = uploadedDocument ? uploadedDocument?.IdType?.code : "AADHAR";
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
          fields: [
            { key: "userType", value: data?.clientDetails?.selectUserType?.code },
            { key: "userTypeDetail", value: JSON.stringify(data?.clientDetails?.selectUserType) },
            { key: "termsAndCondition", value: termsAndConditionData?.Terms_Conditions },
          ],
        },
        clientAuditDetails: {},
        auditDetails: {},
      },
    };
    Digit.DRISTIService.postIndividualService(Individual, tenantId)
      .then(() => {
        history.push(`/digit-ui/citizen/dristi/home/response`, { response: "success", createType: data?.clientDetails?.selectUserType?.code });
      })
      .catch(() => {
        history.push(`/digit-ui/citizen/dristi/home/response`, { response: "error", createType: "LITIGANT" });
      })
      .finally(() => {
        setParams({});
      });
  };

  return (
    <div className="employee-card-wrapper">
      <div className="header-content">
        <Header>{t("CS_COMMON_TERMS_&_CONDITION")}</Header>
      </div>
      <FormComposerV2
        config={termsAndConditionConfig}
        t={t}
        onSubmit={(props) => {
          onSubmit(props);
        }}
        defaultValues={params?.Terms_Conditions || {}}
        label={"CS_COMMON_SUBMIT"}
        headingStyle={{ textAlign: "center" }}
        cardStyle={{ minWidth: "100%" }}
      ></FormComposerV2>
      {showErrorToast && <Toast error={true} label={t("ES_COMMON_SELECT_TERMS_AND_CONDITIONS")} isDleteBtn={true} onClose={closeToast} />}
    </div>
  );
}

export default TermsConditions;
