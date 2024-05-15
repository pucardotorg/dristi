import { FormComposerV2 } from "@egovernments/digit-ui-react-components";
import React from "react";
import { useHistory } from "react-router-dom/cjs/react-router-dom.min";

const SelectUserType = ({ config, t, params = {}, setParams = () => {} }) => {
  console.log(params);
  const Digit = window.Digit || {};
  const tenantId = Digit.ULBService.getCurrentTenantId();
  const history = useHistory();

  const onSubmit = (userType) => {
    // const uploadedDocument = Digit?.SessionStorage?.get("UploadedDocument");
    // const aadhaarNumber = Digit?.SessionStorage?.get("aadharNumber");
    // const identifierId = uploadedDocument ? uploadedDocument?.filedata?.files?.[0]?.fileStoreId : aadhaarNumber;
    // const identifierType = uploadedDocument ? uploadedDocument?.IdType?.code : "AADHAR";
    const data = params;
    const userTypeSelcted = userType?.selectUserType?.code;
    console.log(userType?.selectUserType?.code);
    setParams({ ...params, userType });
    let Individual = {
      Individual: {
        tenantId: tenantId,
        name: {
          givenName: data?.name?.firstName,
          familyName: data?.name?.lastName,
          otherNames: data?.name?.name,
        },
        userDetails: {
          username: Digit.UserService.getUser()?.info?.userName,
          roles: data?.selectUserType?.role
            ? [
                {
                  code: "CITIZEN",
                  name: "Citizen",
                  tenantId: tenantId,
                },
                {
                  code: data?.userType?.selectUserType?.role,
                  name: data?.userType?.selectUserType?.role,
                  tenantId: tenantId,
                },
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
            doorNo: data?.address?.addressDetails?.doorNo,
            latitude: data?.address?.addressDetails?.coordinates?.latitude,
            longitude: data?.address?.addressDetails?.coordinates?.longitude,
            city: data?.address?.addressDetails?.city,
            pincode: data?.address?.addressDetails?.pincode,
            district: data?.address?.addressDetails?.district,
          },
        ],
        identifiers: [
          {
            identifierType: data?.indentity?.code,
            identifierId: data?.adhaarNumber,
          },
        ],
        isSystemUser: true,
        skills: [],
        additionalFields: {
          fields: [
            { key: "userType", value: userTypeSelcted },
            { key: "userTypeDetail", value: JSON.stringify(userType?.selectUserType) },
            { key: "termsAndCondition", value: true },
          ],
        },
        clientAuditDetails: {},
        auditDetails: {},
      },
    };
    setParams({
      ...params,
      Individual: {
        ...Individual,
      },
      userType: {
        ...userType,
      },
    });
    if (userTypeSelcted === "LITIGANT") {
      const aadhaarNumber = Digit?.SessionStorage?.get("aadharNumber");

      console.log(data?.name?.firstName, "dfvf");

      console.log(Individual, "INDIIII");
      Digit.DRISTIService.postIndividualService(Individual, tenantId)
        .then(() => {
          console.log("then1");
          history.push(`/digit-ui/citizen/dristi/home/response`, {
            response: "success",
            createType: data?.userType?.selectUserType?.code,
          });
        })
        .catch(() => {
          console.log("catch1");
          history.push(`/digit-ui/citizen/dristi/home/response`, { response: "error" });
        })
        .finally(() => {
          setParams({});
        });
    } else {
      console.log("else");
      history.push(`/digit-ui/citizen/dristi/home/registration/additional-details`);
    }
  };
  if (!params?.indentity) {
    history.push("/digit-ui/citizen/dristi/home/login");
  }
  return (
    <FormComposerV2
      config={config}
      t={t}
      noBoxShadow
      inline
      label={t("CS_COMMON_CONTINUE")}
      onSubmit={(data) => onSubmit(data?.clientDetails)}
      headingStyle={{ textAlign: "center" }}
      cardStyle={{ minWidth: "100%", padding: 20, display: "flex", flexDirection: "column" }}
      sectionHeadStyle={{ marginBottom: "20px", fontSize: "40px" }}
      submitInForm
    ></FormComposerV2>
  );
};

export default SelectUserType;
