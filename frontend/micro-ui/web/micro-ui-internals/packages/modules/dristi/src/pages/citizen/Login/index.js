import { AppContainer, Toast } from "@egovernments/digit-ui-react-components";
import React, { useEffect, useMemo, useState } from "react";
import { useTranslation } from "react-i18next";
import { Route, Switch, useHistory, useLocation, useRouteMatch } from "react-router-dom";
import { loginSteps } from "./config";
import SelectMobileNumber from "./SelectMobileNumber";
import SelectOtp from "./SelectOtp";
import SelectId from "./SelectId";
import SelectName from "./SelectName";
import { userTypeOptions } from "../registration/config";
const TYPE_REGISTER = { type: "register" };
const TYPE_LOGIN = { type: "login" };
const DEFAULT_USER = "digit-user";

/* set citizen details to enable backward compatiable */
const setCitizenDetail = (userObject, token, tenantId) => {
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

function getRedirectionUrl(status) {
  switch (status) {
    case "isNotRegistered":
      return `/${window?.contextPath}/citizen/dristi/home/login/id-verification`;
    case "isNotApproved":
      return `/${window?.contextPath}/citizen/dristi/home/isNotApproved`;
    case "isApproved":
      return `/${window?.contextPath}/citizen/dristi/home`;
    case "isNotLoggedIn":
      return `/${window?.contextPath}/citizen/dristi/home/login`;
    case "isRegistered":
      return `/${window?.contextPath}/citizen/dristi/home/login`;
    default:
      return `/${window?.contextPath}/citizen/dristi/home/login/id-verification`;
  }
}

const DEFAULT_REDIRECT_URL = getRedirectionUrl("isNotRegistered");

const getFromLocation = (state, searchParams) => {
  return state?.from || searchParams?.from || DEFAULT_REDIRECT_URL;
};

const Login = ({ stateCode }) => {
  const Digit = window.Digit || {};
  const { t } = useTranslation();
  const location = useLocation();
  const { path, url } = useRouteMatch();
  const history = useHistory();
  const [user, setUser] = useState(null);
  const [error, setError] = useState(null);
  const [isOtpValid, setIsOtpValid] = useState(true);
  const [tokens, setTokens] = useState(null);
  const [params, setParmas] = useState({});
  const [errorTO, setErrorTO] = useState(null);
  const searchParams = Digit.Hooks.useQueryParams();
  const [canSubmitName, setCanSubmitName] = useState(false);
  const [canSubmitOtp, setCanSubmitOtp] = useState(true);
  const [canSubmitAadharOtp, setCanSubmitAadharOtp] = useState(true);
  const [canSubmitNo, setCanSubmitNo] = useState(true);
  const [isUserRegistered, setIsUserRegistered] = useState(true);
  const userMobileNUmber = Digit.UserService.getUser()?.info?.mobileNumber;
  useEffect(() => {
    let errorTimeout;
    if (error) {
      if (errorTO) {
        clearTimeout(errorTO);
        setErrorTO(null);
      }
      errorTimeout = setTimeout(() => {
        setError("");
      }, 5000);
      setErrorTO(errorTimeout);
    }
    return () => {
      errorTimeout && clearTimeout(errorTimeout);
    };
  }, [error]);

  useEffect(() => {
    if (!user) {
      return;
    }
    Digit.SessionStorage.set("citizen.userRequestObject", user);
    Digit.UserService.setUser(user);
    if (params.isRememberMe) {
      localStorage.setItem("refresh-token", user?.refresh_token);
    }
    localStorage.setItem("citizen.refresh-token", user?.refresh_token);
    setCitizenDetail(user?.info, user?.access_token, stateCode);
    const redirectPath = location.state?.from || DEFAULT_REDIRECT_URL;
    // routeToAdditionalDetail(user?.info);
    if (!Digit.ULBService.getCitizenCurrentTenant(true)) {
      const homeUrl = `/${window?.contextPath}/citizen/dristi/home`;
      const idVerificationUrl = `/${window?.contextPath}/citizen/dristi/home/login/id-verification`;
      history.replace(isUserRegistered ? homeUrl : idVerificationUrl, {
        redirectBackTo: redirectPath,
      });
    } else {
      history.replace(redirectPath);
    }
  }, [user]);

  const stepItems = useMemo(() =>
    loginSteps.map(
      (step) => {
        const texts = {};
        for (const key in step.texts) {
          texts[key] = t(step.texts[key]);
        }
        return { ...step, texts };
      },
      [loginSteps]
    )
  );

  const getUserType = () => Digit.UserService.getType();

  const handleOtpChange = (otp) => {
    setParmas({ ...params, otp });
  };
  const handleAadharOtpChange = (aadharOtp) => {
    setParmas({ ...params, aadharOtp });
  };

  const handleMobileChange = (event) => {
    const { value } = event.target;
    setParmas({ ...params, mobileNumber: value?.replace(/[^0-9]/g, "") });
    setIsUserRegistered(true);
  };

  const selectMobileNumber = async (mobileNumber) => {
    setCanSubmitNo(false);
    setParmas({ ...params, ...mobileNumber });
    const data = {
      ...mobileNumber,
      tenantId: stateCode,
      userType: getUserType(),
    };
    if (isUserRegistered) {
      const [res, err] = await sendOtp({ otp: { ...data, ...TYPE_LOGIN } });
      if (!err) {
        setCanSubmitNo(true);
        history.replace(`${path}/otp`, { from: getFromLocation(location.state, searchParams), role: location.state?.role });
        return;
      } else {
        setCanSubmitNo(true);
        // setError("MOBILE_NUMBER_NOT_REGISTERED");
        // setTimeout(() => history.replace(getRedirectionUrl("isNotLoggedIn")), 3000);
        setIsUserRegistered(false);
        history.replace(`${path}/user-name`, { from: getFromLocation(location.state, searchParams) });
      }
    } else {
      const [res, err] = await sendOtp({ otp: { ...data, ...TYPE_REGISTER } });
      if (!err) {
        setCanSubmitNo(true);
        history.replace(`${path}/otp`, { from: getFromLocation(location.state, searchParams) });
        return;
      } else {
        setCanSubmitNo(true);
        // setError("MOBILE_NUMBER_ALREADY_REGISTERED");
        // setTimeout(() => history.replace(getRedirectionUrl("isRegistered")), 3000);
      }
    }
  };

  const selectName = async (name) => {
    const data = {
      ...params,
      tenantId: stateCode,
      userType: getUserType(),
      ...name,
    };
    setParmas({ ...params, ...name });
    setCanSubmitName(true);
    const [res, err] = await sendOtp({ otp: { ...data, ...TYPE_REGISTER } });
    if (res) {
      setCanSubmitName(false);
      history.replace(`${path}/otp`, { from: getFromLocation(location.state, searchParams) });
    } else {
      setCanSubmitName(false);
    }
  };

  const selectOtp = async () => {
    try {
      setIsOtpValid(true);
      setCanSubmitOtp(false);
      const { mobileNumber, otp, name } = params;
      if (isUserRegistered) {
        const requestData = {
          username: mobileNumber,
          password: otp,
          tenantId: stateCode,
          userType: getUserType(),
        };
        const { ResponseInfo, UserRequest: info, ...tokens } = await Digit.UserService.authenticate(requestData);

        if (location.state?.role) {
          const roleInfo = info.roles.find((userRole) => userRole.code === location.state.role);
          if (!roleInfo || !roleInfo.code) {
            setError(t("ES_ERROR_USER_NOT_PERMITTED"));
            setTimeout(() => history.replace(DEFAULT_REDIRECT_URL), 5000);
            return;
          }
        }
        if (window?.globalConfigs?.getConfig("ENABLE_SINGLEINSTANCE")) {
          info.tenantId = Digit.ULBService.getStateId();
        }

        setUser({ info, ...tokens });
      } else if (!isUserRegistered) {
        const requestData = {
          name: name || DEFAULT_USER,
          username: mobileNumber,
          otpReference: otp,
          tenantId: stateCode,
        };

        const { ResponseInfo, UserRequest: info, ...tokens } = await Digit.UserService.registerUser(requestData, stateCode);

        if (window?.globalConfigs?.getConfig("ENABLE_SINGLEINSTANCE")) {
          info.tenantId = Digit.ULBService.getStateId();
        }

        setUser({ info, ...tokens });
      }
    } catch (err) {
      setCanSubmitOtp(true);
      setIsOtpValid(false);
      const { otp, ...temp } = params;
      setParmas(temp);
    }
  };

  const resendOtp = async () => {
    const { mobileNumber } = params;
    const data = {
      mobileNumber,
      tenantId: stateCode,
      userType: getUserType(),
    };
    if (!isUserRegistered) {
      const [res, err] = await sendOtp({ otp: { ...data, ...TYPE_REGISTER } });
    } else if (isUserRegistered) {
      const [res, err] = await sendOtp({ otp: { ...data, ...TYPE_LOGIN } });
    }
  };

  const sendOtp = async (data) => {
    try {
      const res = await Digit.UserService.sendOtp(data, stateCode);
      return [res, null];
    } catch (err) {
      return [null, err];
    }
  };

  const handleRememberMeChange = () => {
    setParmas({ ...params, isRememberMe: !params.isRememberMe });
  };

  const onAadharChange = (aadharNumber) => {
    Digit.SessionStorage.set("aadharNumber", aadharNumber);
    Digit.SessionStorage.del("UploadedDocument");
    history.push(`${path}/aadhar-otp`);
  };
  const onDocumentUpload = async (filename, filedata, IdType) => {
    const fileUploadRes = await Digit.UploadServices.Filestorage("DRISTI", filedata, Digit.ULBService.getStateId());
    Digit.SessionStorage.set("UploadedDocument", { filedata: fileUploadRes?.data, IdType, filename });
    Digit.SessionStorage.del("aadharNumber");
    history.push(`/digit-ui/citizen/dristi/home/registration`);
  };
  const onAadharOtpSelect = () => {
    setCanSubmitAadharOtp(false);
    history.replace(`/${window?.contextPath}/citizen/dristi/home/registration`);
    setCanSubmitAadharOtp(true);
  };

  return (
    <div className="citizen-form-wrapper" style={{ minWidth: "100%" }}>
      <Switch>
        <AppContainer>
          <Route path={`${path}`} exact>
            <SelectMobileNumber
              onSelect={selectMobileNumber}
              config={stepItems[0]}
              mobileNumber={params.mobileNumber || ""}
              onMobileChange={handleMobileChange}
              canSubmit={canSubmitNo}
              showRegisterLink={isUserRegistered && !location.state?.role}
              t={t}
              isRememberMe={params?.isRememberMe || false}
              handleRememberMeChange={handleRememberMeChange}
            />
          </Route>
          <Route path={`${path}/user-name`}>
            <SelectName t={t} config={stepItems[1]} onSelect={selectName} />
          </Route>
          <Route path={`${path}/otp`}>
            <SelectOtp
              config={{ ...stepItems[2], texts: { ...stepItems[2].texts, cardText: `${stepItems[2].texts.cardText} ${params.mobileNumber || ""}` } }}
              onOtpChange={handleOtpChange}
              onResend={resendOtp}
              onSelect={selectOtp}
              otp={params.otp}
              error={isOtpValid}
              canSubmit={canSubmitOtp}
              t={t}
            />
          </Route>
          <Route path={`${path}/id-verification`}>
            <SelectId t={t} config={[stepItems[3]]} onAadharChange={onAadharChange} onDocumentUpload={onDocumentUpload} />
          </Route>
          <Route path={`${path}/aadhar-otp`}>
            <SelectOtp
              config={{
                ...stepItems[4],
                texts: { ...stepItems[4].texts, cardText: `${stepItems[4].texts.cardText}` },
              }}
              onOtpChange={handleAadharOtpChange}
              onResend={resendOtp}
              onSelect={onAadharOtpSelect}
              otp={params.aadharOtp}
              error={isOtpValid}
              canSubmit={canSubmitAadharOtp}
              t={t}
            />
          </Route>
          {error && <Toast error={true} label={error} onClose={() => setError(null)} />}
        </AppContainer>
      </Switch>
    </div>
  );
};

export default Login;
