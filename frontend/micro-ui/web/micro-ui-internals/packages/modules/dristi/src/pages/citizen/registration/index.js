import React, { useEffect, useMemo, useState } from "react";
import { AppContainer, Loader, Toast } from "@egovernments/digit-ui-react-components";
import { Route, Switch, useHistory, useRouteMatch } from "react-router-dom";
import AdvocateClerkAdditionalDetail from "./AdvocateClerkAdditionalDetail";
import SelectUserType from "./SelectUserType";
import { newConfig } from "./config";
import { useTranslation } from "react-i18next";
import SelectName from "./SelectName";
import SelectOtp from "../Login/SelectOtp";
import SelectUserAddress from "./SelectUserAddress";
import SelectMobileNumber from "../Login/SelectMobileNumber";
import SelectId from "../Login/SelectId";
import EnterAdhaar from "./EnterAdhaar";
import { useLocation } from "react-router-dom/cjs/react-router-dom.min";

const TYPE_REGISTER = { type: "register" };
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
const Registration = ({ stateCode }) => {
  const Digit = window.Digit || {};
  const tenantId = Digit.ULBService.getCurrentTenantId();
  const history = useHistory();
  const { path } = useRouteMatch();
  const token = window.localStorage.getItem("token");
  const isUserLoggedIn = Boolean(token);
  const moduleCode = "DRISTI";
  const [newParams, setNewParams] = useState({});
  const [canSubmitNo, setCanSubmitNo] = useState(true);
  const [isUserRegistered, setIsUserRegistered] = useState(true);
  const [canSubmitOtp, setCanSubmitOtp] = useState(true);
  const getUserType = () => Digit.UserService.getType();
  const { t } = useTranslation();
  const location = useLocation();
  const DEFAULT_USER = "digit-user";
  const [user, setUser] = useState(null);
  const [isOtpValid, setIsOtpValid] = useState(true);
  const searchParams = Digit.Hooks.useQueryParams();
  const [canSubmitAadharOtp, setCanSubmitAadharOtp] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    if (!user) {
      return;
    }
    Digit.SessionStorage.set("citizen.userRequestObject", user);
    Digit.UserService.setUser(user);
    setCitizenDetail(user?.info, user?.access_token, stateCode);
    const redirectPath = location.state?.from || `${path}/user-name`;
    // routeToAdditionalDetail(user?.info);
    if (!Digit.ULBService.getCitizenCurrentTenant(true)) {
      const idVerificationUrl = `${path}/user-name`;
      history.push(`${path}/user-name`);
    } else {
      history.push(redirectPath);
    }
  }, [user]);
  const stepItems = useMemo(() =>
    newConfig.map(
      (step) => {
        const texts = {};
        for (const key in step.texts) {
          texts[key] = t(step.texts[key]);
        }
        return { ...step, texts };
      },
      [newConfig]
    )
  );
  const getFromLocation = (state, searchParams) => {
    return state?.from || searchParams?.from;
  };
  const userInfo = JSON.parse(window.localStorage.getItem("user-info"));
  const { data, isLoading, refetch, isFetching } = Digit.Hooks.dristi.useGetIndividualUser(
    {
      Individual: {
        userUuid: [userInfo?.uuid],
      },
    },
    { tenantId, limit: 1000, offset: 0 },
    moduleCode,
    "",
    userInfo?.uuid && isUserLoggedIn
  );
  const individualId = data?.Individual?.[0]?.individualId;

  if (Boolean(individualId)) {
    history.push(`/${window?.contextPath}/citizen/dristi/home`);
  }
  const handleAadharOtpChange = (aadharOtp) => {
    setNewParams({ ...newParams, aadharOtp });
  };

  const selectMobileNumber = async (mobileNumber) => {
    setCanSubmitNo(false);
    setNewParams({ ...newParams, ...mobileNumber });
    const data = {
      ...mobileNumber,
      tenantId: stateCode,
      userType: getUserType(),
    };
    const [res, err] = await sendOtp({ otp: { ...data, ...TYPE_REGISTER } });
    if (!err) {
      setCanSubmitNo(true);
      history.replace(`${path}/otp`, { from: getFromLocation(location.state, searchParams) });
      return;
    } else {
      setError(t("ES_ERROR_USER_ALREADY_REGISTERED"));
      setCanSubmitNo(true);
    }
  };
  const selectOtp = async () => {
    try {
      setIsOtpValid(true);
      setCanSubmitOtp(false);
      const { mobileNumber, otp, name } = newParams;
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
      // history.push(`${path}/user-name`)
    } catch (err) {
      setCanSubmitOtp(true);
      setIsOtpValid(false);
      const { otp, ...temp } = newParams;
      setNewParams(temp);
    }
  };
  const handleOtpChange = (otp) => {
    setNewParams({ ...newParams, otp });
  };
  const handleAdhaarChange = (adhaarNumber) => {
    setNewParams({ ...newParams, adhaarNumber });
    history.replace(`${path}/aadhar-otp`);
  };
  const resendOtp = async () => {
    const { mobileNumber } = newParams;
    const data = {
      mobileNumber,
      tenantId: stateCode,
      userType: getUserType(),
    };
    if (!isUserRegistered) {
      const [res, err] = await sendOtp({ otp: { ...data, ...TYPE_REGISTER } });
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
  const handleMobileChange = (event) => {
    const { value } = event.target;
    setNewParams({ ...newParams, mobileNumber: value?.replace(/[^0-9]/g, "") });
    setIsUserRegistered(true);
  };
  const selectName = async (name) => {
    setNewParams({ ...newParams, name });
    history.replace(`${path}/user-address`);
  };

  const onAadharOtpSelect = () => {
    setCanSubmitAadharOtp(false);
    history.replace(`${path}/user-type`);
    setCanSubmitAadharOtp(true);
  };
  const handleAddressSave = (address) => {
    setNewParams({ ...newParams, address });
    history.push(`${path}/id-verification`);
  };
  const handleIdentitySave = (indentity) => {
    setNewParams({ ...newParams, indentity });
    history.push(`${path}/enter-adhaar`);
  };
  const handleUserTypeSave = (userType) => {
    setNewParams({ ...newParams, userType });
    history.push(`/digit-ui/citizen/dristi/home/response`);
  };

  if (isLoading || isFetching) {
    return <Loader />;
  }
  console.log(newParams);
  return (
    <div className="citizen-form-wrapper" style={{ minWidth: "100%" }}>
      <Switch>
        <AppContainer>
          <Route path={`${path}/additional-details`}>
            <AdvocateClerkAdditionalDetail setParams={setNewParams} params={newParams} path={path} config={stepItems[9]} />
          </Route>
          <Route path={`${path}/mobile-number`}>
            <SelectMobileNumber
              onSelect={selectMobileNumber}
              config={stepItems[5]}
              mobileNumber={newParams.mobileNumber || ""}
              onMobileChange={handleMobileChange}
              canSubmit={canSubmitNo}
              isRegister={true}
              t={t}
              path={path}
            />
          </Route>
          <Route path={`${path}/otp`}>
            <SelectOtp
              config={{
                ...stepItems[4],
                texts: { ...stepItems[4].texts, cardText: `${stepItems[4].texts.cardText} ${newParams.mobileNumber || ""}` },
              }}
              onOtpChange={handleOtpChange}
              onResend={resendOtp}
              onSelect={selectOtp}
              otp={newParams.otp}
              error={isOtpValid}
              canSubmit={canSubmitOtp}
              params={newParams}
              t={t}
            />
          </Route>
          <Route path={`${path}/user-name`}>
            <SelectName config={stepItems[3]} t={t} onSelect={selectName} params={newParams} history={history} />
          </Route>
          <Route path={`${path}/user-address`}>
            <SelectUserAddress config={[stepItems[1]]} t={t} params={newParams} onSelect={handleAddressSave} />
          </Route>
          <Route path={`${path}/id-verification`}>
            <SelectId t={t} config={[stepItems[6]]} params={newParams} history={history} onSelect={handleIdentitySave} />
          </Route>

          <Route path={`${path}/enter-adhaar`}>
            <EnterAdhaar t={t} config={[stepItems[7]]} onSelect={handleAdhaarChange} params={newParams} />
          </Route>
          <Route path={`${path}/aadhar-otp`}>
            <SelectOtp
              config={{
                ...stepItems[8],
                texts: { ...stepItems[4].texts, cardText: `${stepItems[4].texts.cardText} ${newParams.mobileNumber || ""}` },
              }}
              onOtpChange={handleAadharOtpChange}
              onResend={resendOtp}
              onSelect={onAadharOtpSelect}
              otp={newParams.aadharOtp}
              error={isOtpValid}
              canSubmit={canSubmitAadharOtp}
              params={newParams}
              path={`${path}/user-type`}
              t={t}
            />
          </Route>
          <Route path={`${path}/user-type`}>
            <SelectUserType config={[stepItems[2]]} t={t} setParams={setNewParams} params={newParams} onSelect={handleUserTypeSave} />
          </Route>

          {error && <Toast error={true} label={error} onClose={() => setError(null)} />}
        </AppContainer>
      </Switch>
    </div>
  );
};

export default Registration;
