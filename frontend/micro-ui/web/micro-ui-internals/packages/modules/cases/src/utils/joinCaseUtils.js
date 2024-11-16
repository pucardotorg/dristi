import { userTypeOptions } from "@egovernments/digit-ui-module-dristi/src/pages/citizen/registration/config";
import { DRISTIService } from "@egovernments/digit-ui-module-dristi/src/services";
import { CASEService } from "../hooks/services";
import { getUserDetails } from "@egovernments/digit-ui-module-dristi/src/hooks/useGetAccessToken";

const TYPE_REGISTER = { type: "register" };
const TYPE_LOGIN = { type: "login" };
const DEFAULT_USER = "digit-user";

const sendOtp = async (data, tenantId) => {
  try {
    const res = await window?.Digit.UserService.sendOtp(data, tenantId);
    return [res, null];
  } catch (err) {
    return [null, err];
  }
};

export const selectMobileNumber = async (mobileNumber, tenantId) => {
  const data = {
    mobileNumber: mobileNumber,
    tenantId: tenantId,
    userType: Digit.UserService.getType(),
  };
  const [res, err] = await sendOtp({ otp: { ...data, ...TYPE_LOGIN } });
  if (!err) {
    return { response: res, isRegistered: true };
  } else {
    const [res, err] = await sendOtp({ otp: { ...data, name: DEFAULT_USER, ...TYPE_REGISTER } });
    return { response: res, isRegistered: false };
  }
};

export const selectOtp = async (isUserRegistered, mobileNumber, otp, tenantId, name) => {
  try {
    if (isUserRegistered) {
      const requestData = {
        username: mobileNumber,
        password: otp,
        tenantId: tenantId,
        userType: Digit.UserService.getType(),
      };
      const { ResponseInfo, UserRequest: info, ...tokens } = await Digit.UserService.authenticate(requestData);

      if (window?.globalConfigs?.getConfig("ENABLE_SINGLEINSTANCE")) {
        info.tenantId = Digit.ULBService.getStateId();
      }
      return { info: info, ...tokens };
    } else if (!isUserRegistered) {
      const requestData = {
        name: name || DEFAULT_USER,
        username: mobileNumber,
        otpReference: otp,
        tenantId: tenantId,
      };

      const { ResponseInfo, UserRequest: info, ...tokens } = await Digit.UserService.registerUser(requestData, tenantId);
      if (window?.globalConfigs?.getConfig("ENABLE_SINGLEINSTANCE")) {
        info.tenantId = Digit.ULBService.getStateId();
      }
      localStorage.setItem(`temp-refresh-token-${mobileNumber}`, tokens?.refresh_token);
      return { info: info, ...tokens };
    }
  } catch (err) {
    console.error("err :>> ", err);
  }
};

export const createRespondentIndividualUser = async (data, documentData, tenantId) => {
  const identifierId = documentData?.fileStoreId;
  const identifierIdDetails = documentData
    ? {
        fileStoreId: identifierId,
        filename: documentData?.filename,
        documentType: documentData?.fileType,
      }
    : {};
  const identifierType = documentData?.identifierType;
  let Individual = {
    Individual: {
      tenantId: tenantId,
      name: {
        givenName: data?.firstName,
        familyName: data?.lastName,
        otherNames: data?.middleName,
      },
      userDetails: {
        username: data?.userDetails?.username,
        type: data?.userDetails?.type,
        roles: [
          {
            code: "CITIZEN",
            name: "Citizen",
            tenantId: tenantId,
          },
          ...[
            "CASE_CREATOR",
            "CASE_EDITOR",
            "CASE_VIEWER",
            "DEPOSITION_CREATOR",
            "DEPOSITION_VIEWER",
            "APPLICATION_CREATOR",
            "APPLICATION_VIEWER",
            "HEARING_VIEWER",
            "ORDER_VIEWER",
            "SUBMISSION_CREATOR",
            "SUBMISSION_RESPONDER",
            "SUBMISSION_DELETE",
            "TASK_VIEWER",
            "CASE_RESPONDER",
            "HEARING_ACCEPTOR",
            "PENDING_TASK_CREATOR",
          ]?.map((role) => ({
            code: role,
            name: role,
            tenantId: tenantId,
          })),
        ],
      },
      userUuid: data?.userDetails?.userUuid,
      userId: data?.userDetails?.userId,
      mobileNumber: data?.userDetails?.mobileNumber,
      address: [
        {
          tenantId: data?.addressDetails?.[0]?.tenantId,
          type: "PERMANENT",
          doorNo: data?.addressDetails?.[0]?.addressDetails?.doorNo,
          latitude: data?.addressDetails?.[0]?.addressDetails?.coordinates?.latitude,
          longitude: data?.addressDetails?.[0]?.addressDetails?.coordinates?.longitude,
          city: data?.addressDetails?.[0]?.addressDetails?.city,
          pincode: data?.addressDetails?.[0]?.addressDetails?.pincode,
          addressLine1: data?.addressDetails?.[0]?.addressDetails?.state,
          addressLine2: data?.addressDetails?.[0]?.addressDetails?.district,
          buildingName: data?.addressDetails?.[0]?.addressDetails?.buildingName,
          street: data?.addressDetails?.[0]?.addressDetails?.locality,
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
          { key: "termsAndCondition", value: true },
          { key: "identifierIdDetails", value: JSON.stringify(identifierIdDetails) },
        ],
      },
      clientAuditDetails: {},
      auditDetails: {},
    },
  };
  const response = await window?.Digit.DRISTIService.postIndividualService(Individual, tenantId);
  const refreshToken = window.localStorage.getItem(`temp-refresh-token-${response?.Individual?.mobileNumber}`);
  window.localStorage.removeItem(`temp-refresh-token-${response?.Individual?.mobileNumber}`);
  if (refreshToken) {
    await getUserDetails(refreshToken, response?.Individual?.mobileNumber);
  }
  return response;
};

export const updateCaseDetails = async (caseDetails, tenantId, action) => {
  return DRISTIService.caseUpdateService(
    {
      cases: {
        ...caseDetails,
        linkedCases: caseDetails?.linkedCases ? caseDetails?.linkedCases : [],
        workflow: {
          ...caseDetails?.workflow,
          action,
        },
      },
      tenantId,
    },
    tenantId
  );
};

export const submitJoinCase = async (data) => {
  let res;
  try {
    res = await CASEService.joinCaseService(data, {});
    return [res, undefined];
  } catch (err) {
    return [res, err];
  }
};

export const searchIndividualUserWithUuid = async (uuid, tenantId) => {
  const individualData = await window?.Digit.DRISTIService.searchIndividualUser(
    {
      Individual: {
        userUuid: [uuid],
      },
    },
    { tenantId, limit: 1000, offset: 0 },
    "",
    uuid
  );
  return individualData;
};

export const getFullName = (seperator, ...strings) => {
  return strings.filter(Boolean).join(seperator);
};
