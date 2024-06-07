import { AppContainer, Toast } from "@egovernments/digit-ui-react-components";
import React, { useEffect, useMemo, useState } from "react";
import { useTranslation } from "react-i18next";
import { Route, Switch, useHistory, useLocation, useRouteMatch } from "react-router-dom";
import { loginSteps } from "./config";
import SelectMobileNumber from "./SelectMobileNumber";
import SelectOtp from "./SelectOtp";

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
      return `/${window?.contextPath}/citizen/dristi/home/registration/user-name`;
    case "isNotApproved":
      return `/${window?.contextPath}/citizen/dristi/home/isNotApproved`;
    case "isApproved":
      return `/${window?.contextPath}/citizen/dristi/home`;
    case "isNotLoggedIn":
      return `/${window?.contextPath}/citizen/dristi/home/login`;
    case "isRegistered":
      return `/${window?.contextPath}/citizen/dristi/home/login`;
    default:
      return `/${window?.contextPath}/citizen/dristi/home/registration/user-name`;
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
  const token = window.localStorage.getItem("token");
  const isUserLoggedIn = Boolean(token);
  const [isOtpValid, setIsOtpValid] = useState(true);
  const [tokens, setTokens] = useState(null);
  const [params, setParmas] = useState({});
  const [errorTO, setErrorTO] = useState(null);
  const searchParams = Digit.Hooks.useQueryParams();
  const [canSubmitOtp, setCanSubmitOtp] = useState(true);
  const [canSubmitNo, setCanSubmitNo] = useState(true);
  const [isUserRegistered, setIsUserRegistered] = useState(true);
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
    if (!Digit.ULBService.getCitizenCurrentTenant(true)) {
      const homeUrl = `/${window?.contextPath}/citizen/dristi/home`;
      const idVerificationUrl = `/${window?.contextPath}/citizen/dristi/home/registration/user-name`;
      history.push(isUserRegistered ? homeUrl : idVerificationUrl, {
        redirectBackTo: redirectPath,
      });
    } else {
      history.push(redirectPath);
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

  const handleMobileChange = (event) => {
    const { value } = event.target;
    setParmas({ ...params, mobileNumber: value?.replace(/[^0-9]/g, ""), name: "" });
    setIsUserRegistered(true);
  };

  const selectMobileNumber = async (mobileNumber) => {
    setIsOtpValid(true);
    setCanSubmitNo(false);
    setParmas({ ...params, ...mobileNumber });
    const data = {
      ...mobileNumber,
      tenantId: stateCode,
      userType: getUserType(),
    };
    const [res, err] = await sendOtp({ otp: { ...data, ...TYPE_LOGIN } });
    if (!err) {
      setCanSubmitNo(true);
      history.push(`${path}/otp`);
      return;
    } else {
      setCanSubmitNo(true);
      setIsUserRegistered(false);
      setError(t("ES_ERROR_USER_NOT_PERMITTED"));
      history.replace(`${path}/login`, { from: getFromLocation(location.state, searchParams) });
    }
  };

  const selectOtp = async () => {
    try {
      setParmas({ ...params, otp: "" });

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
      setParmas((prev) => ({
        ...prev,
        otp: "",
      }));
    }
  };

  const resendOtp = async () => {
    setIsOtpValid(true);
    setParmas({ ...params, otp: "" });
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
  return (
    <div className="citizen-form-wrapper">
      <Switch>
        <React.Fragment>
          <Route path={`${path}`} exact>
            <SelectMobileNumber
              onSelect={selectMobileNumber}
              config={stepItems[0]}
              mobileNumber={params.mobileNumber || ""}
              onMobileChange={handleMobileChange}
              canSubmit={canSubmitNo}
              isUserLoggedIn={isUserLoggedIn}
              showRegisterLink={isUserRegistered && !location.state?.role}
              t={t}
            />
          </Route>
          <Route path={`${path}/otp`}>
            <SelectOtp
              cardText={`${stepItems[2].texts.cardText}`}
              mobileNumber={params.mobileNumber || ""}
              onOtpChange={handleOtpChange}
              onResend={resendOtp}
              onSelect={selectOtp}
              setIsOtpValid={setIsOtpValid}
              otp={params.otp}
              error={isOtpValid}
              canSubmit={canSubmitOtp}
              params={params}
              setParams={setParmas}
              t={t}
              path={`${path}/login`}
            />
          </Route>

          {error && <Toast error={true} label={error} onClose={() => setError(null)} />}
        </React.Fragment>
      </Switch>
    </div>
  );
};

export default Login;
