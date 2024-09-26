import { userTypeOptions } from "@egovernments/digit-ui-module-dristi/src/pages/citizen/registration/config";
import { DRISTIService } from "@egovernments/digit-ui-module-dristi/src/services";
import { CASEService } from "../hooks/services";

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
      return { info: info, ...tokens };
    }
  } catch (err) {
    // setCanSubmitOtp(true);
    // setOtpError(err?.response?.data?.error_description === "Account locked" ? t("MAX_RETRIES_EXCEEDED") : t("CS_INVALID_OTP"));
    // setParmas((prev) => ({
    //   ...prev,
    //   otp: "",
    // }));
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
            "HEARING_ACCEPTOR"
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
      address: data?.addressDetails,
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
  // const refreshToken = window.localStorage.getItem(`temp-refresh-token-${data?.complainantVerification?.userDetails?.mobileNumber}`);
  // window.localStorage.removeItem(`temp-refresh-token-${data?.complainantVerification?.userDetails?.mobileNumber}`);
  // if (refreshToken) {
  //   await getUserDetails(refreshToken, data?.complainantVerification?.userDetails?.mobileNumber);
  // }
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
