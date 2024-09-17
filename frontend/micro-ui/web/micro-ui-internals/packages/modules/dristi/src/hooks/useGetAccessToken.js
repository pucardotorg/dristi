import { useEffect } from "react";
import { Urls } from ".";
import { ServiceRequest } from "../Utils";

export const setCitizenDetail = (userObject, token, tenantId) => {
  let locale = JSON.parse(sessionStorage.getItem("Digit.initData"))?.value?.selectedLanguage;
  localStorage.setItem("Citizen.tenant-id", tenantId);
  localStorage.setItem("tenant-id", tenantId);
  localStorage.setItem("citizen.userRequestObject", JSON.stringify(userObject));
  localStorage.setItem("locale", locale);
  localStorage.setItem("Citizen.locale", locale);
  localStorage.setItem("token", token);
  localStorage.setItem("Citizen.token", token);
  localStorage.setItem("user-info", JSON.stringify(userObject));
  localStorage.setItem("Citizen.user-info", JSON.stringify(userObject));
};

const authenticate = async (details) => {
  const data = new URLSearchParams();
  Object.entries(details).forEach(([key, value]) => data.append(key, value));
  let authResponse = await ServiceRequest({
    serviceName: "authenticate",
    url: Urls.Authenticate,
    data,
    headers: {
      authorization: `Basic ${window?.globalConfigs?.getConfig("JWT_TOKEN") || "ZWdvdi11c2VyLWNsaWVudDo="}`,
      "Content-Type": "application/x-www-form-urlencoded",
    },
  });
  const invalidRoles = window?.globalConfigs?.getConfig("INVALIDROLES") || [];
  if (invalidRoles && invalidRoles.length > 0 && authResponse && authResponse?.UserRequest?.roles?.some((role) => invalidRoles.includes(role.code))) {
    throw new Error("ES_ERROR_USER_NOT_PERMITTED");
  }
  return authResponse;
};
export const getUserDetails = async (refreshToken, mobNumber = null) => {
  const mobileNumber = mobNumber ? mobNumber : window?.Digit.UserService.getUser()?.info?.mobileNumber;
  const response = await authenticate({
    username: mobileNumber,
    grant_type: "refresh_token",
    tenantId: window?.Digit.ULBService.getStateId(),
    refresh_token: refreshToken,
  });
  return response;
};
export function useGetAccessToken(key, shouldRefreshToken) {
  useEffect(() => {
    const refreshToken = window.localStorage.getItem(key);
    if (refreshToken && shouldRefreshToken) {
      getUserDetails(refreshToken).then((res) => {
        const { ResponseInfo, UserRequest: info, ...tokens } = res;
        const user = { info, ...tokens };
        localStorage.setItem("citizen.userRequestObject", user);
        window?.Digit.UserService.setUser(user);
        setCitizenDetail(user?.info, user?.access_token, window?.Digit.ULBService.getStateId());
      });
    }
  }, []);
}
