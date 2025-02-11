import { BackButton, Dropdown, FormComposer, FormComposerV2, Loader, Toast } from "@egovernments/digit-ui-react-components";
import PropTypes from "prop-types";
import React, { useEffect, useState, useMemo } from "react";
import { useHistory } from "react-router-dom";
import Background from "../../../components/Background";
import Header from "../../../components/Header";

/* set employee details to enable backward compatiable */
const setEmployeeDetail = (userObject, token) => {
  let locale = JSON.parse(sessionStorage.getItem("Digit.locale"))?.value || Digit.Utils.getDefaultLanguage();
  localStorage.setItem("Employee.tenant-id", userObject?.tenantId);
  localStorage.setItem("tenant-id", userObject?.tenantId);
  localStorage.setItem("citizen.userRequestObject", JSON.stringify(userObject));
  localStorage.setItem("locale", locale);
  localStorage.setItem("Employee.locale", locale);
  localStorage.setItem("token", token);
  localStorage.setItem("Employee.token", token);
  localStorage.setItem("user-info", JSON.stringify(userObject));
  localStorage.setItem("Employee.user-info", JSON.stringify(userObject));
};

const Login = ({ config: propsConfig, t, isDisabled, tenantsData, isTenantsDataLoading }) => {
  const { data: cities, isLoading } = Digit.Hooks.useTenants();
  const { data: storeData, isLoading: isStoreLoading } = Digit.Hooks.useStore.getInitData();
  const { stateInfo } = storeData || {};
  const [user, setUser] = useState(null);
  const [showToast, setShowToast] = useState(null);
  const [disable, setDisable] = useState(false);
  const [prevDistrict, setPrevDistrict] = useState(null);

  const history = useHistory();
  // const getUserType = () => "EMPLOYEE" || Digit.UserService.getType();

  useEffect(() => {
    if (!user) {
      return;
    }
    localStorage.setItem("citizen.userRequestObject", user);
    const filteredRoles = user?.info?.roles?.filter((role) => role.tenantId === Digit.SessionStorage.get("Employee.tenantId"));
    if (user?.info?.roles?.length > 0) user.info.roles = filteredRoles;
    Digit.UserService.setUser(user);
    setEmployeeDetail(user?.info, user?.access_token);
    let redirectPath = `/${window?.contextPath}/employee`;

    /* logic to redirect back to same screen where we left off  */
    if (window?.location?.href?.includes("from=")) {
      redirectPath = decodeURIComponent(window?.location?.href?.split("from=")?.[1]) || `/${window?.contextPath}/employee`;
    }

    /*  RAIN-6489 Logic to navigate to National DSS home incase user has only one role [NATADMIN]*/
    if (user?.info?.roles && user?.info?.roles?.length > 0 && user?.info?.roles?.every((e) => e.code === "NATADMIN")) {
      redirectPath = `/${window?.contextPath}/employee/dss/landing/NURT_DASHBOARD`;
    }
    /*  RAIN-6489 Logic to navigate to National DSS home incase user has only one role [NATADMIN]*/
    if (user?.info?.roles && user?.info?.roles?.length > 0 && user?.info?.roles?.every((e) => e.code === "STADMIN")) {
      redirectPath = `/${window?.contextPath}/employee/dss/landing/home`;
    }
    const userInfo = JSON.parse(window.localStorage.getItem("user-info"));
    const userType = userInfo?.type === "CITIZEN" ? "citizen" : "employee";
    function hasPostManagerRole() {
      return userInfo.roles.some((userRole) => userRole.name === "POST_MANAGER");
    }
    if (hasPostManagerRole()) {
      redirectPath = `/${window?.contextPath}/${userType}/orders/tracking`;
    }
    history.replace(redirectPath);
  }, [user]);

  const onLogin = async (data) => {
    // if (!data.city) {
    //   alert("Please Select City!");
    //   return;
    // }
    setDisable(true);

    const requestData = {
      ...data,
      courtroom: data?.courtroom?.code,
      district: data?.district?.code,
      userType: "EMPLOYEE",
    };

    requestData.tenantId = data?.city?.code || Digit.ULBService.getStateId();
    delete requestData.city;
    try {
      const { UserRequest: info, ...tokens } = await Digit.UserService.authenticate(requestData);
      Digit.SessionStorage.set("Employee.tenantId", info?.tenantId);
      setUser({ info, ...tokens });
    } catch (err) {
      setShowToast(
        err?.response?.data?.error_description ||
          (err?.message === "ES_ERROR_USER_NOT_PERMITTED" && t("ES_ERROR_USER_NOT_PERMITTED")) ||
          t("INVALID_LOGIN_CREDENTIALS")
      );
      setTimeout(closeToast, 5000);
    }
    setDisable(false);
  };

  const closeToast = () => {
    setShowToast(null);
  };

  const onForgotPassword = () => {
    history.push(`/${window?.contextPath}/employee/user/forgot-password`);
  };

  const { mode } = Digit.Hooks.useQueryParams();

  const [config, setConfig] = useState([{ body: propsConfig?.inputs }]);

  const { data: commonMasterData, isLoading: isCommonMasterDataLoading } = Digit.Hooks.useCustomMDMS(
    Digit.ULBService.getStateId(),
    "common-masters",
    [{ name: "CourtEstablishment" }, { name: "Court_Rooms" }],
    {
      select: (data) => data,
    }
  );

  const getFilteredCourtRoom = (district) => {
    const courtEstablishmnets = commonMasterData?.["common-masters"]?.CourtEstablishment;
    const courtRoom = commonMasterData?.["common-masters"]?.Court_Rooms;
    if (!district) return courtRoom;
    const filteredCourtEstablishmnets = courtEstablishmnets.filter((ce) => ce.district === district);
    const filteredCourtRoom = courtRoom.filter((court) => filteredCourtEstablishmnets.some((ce) => ce.code === court.establishment));
    return filteredCourtRoom;
  };
  const getModifiedConfig = (district) => {
    const newPopulators = {
      name: "courtroom",
      optionsKey: "name",
      error: "ERR_HRMS_INVALID_COURT_ROOM",
      options: getFilteredCourtRoom(district?.code),
    };
    let modifiedConfig = config;
    modifiedConfig[0].body[3].populators = newPopulators;
    return modifiedConfig;
  };

  const onFormValueChange = (setValue, formData, formState) => {
    if (formData?.district !== prevDistrict) {
      setConfig(getModifiedConfig(formData?.district));
      setValue("courtroom", "");
    }
    setPrevDistrict((prev) => formData?.district);
  };

  return isLoading || isStoreLoading || isCommonMasterDataLoading ? (
    <Loader />
  ) : (
    <Background>
      <div className="employeeBackbuttonAlign">
        <BackButton variant="white" style={{ borderBottom: "none" }} />
      </div>

      <FormComposerV2
        onSubmit={onLogin}
        isDisabled={isDisabled || disable}
        onFormValueChange={onFormValueChange}
        noBoxShadow
        inline
        submitInForm
        config={config}
        label={propsConfig.texts.submitButtonLabel}
        // secondaryActionLabel={propsConfig.texts.secondaryButtonLabel}
        onSecondayActionClick={onForgotPassword}
        heading={propsConfig.texts.header}
        className="loginFormStyleEmployee"
        cardSubHeaderClassName="loginCardSubHeaderClassName"
        cardClassName="loginCardClassName"
        buttonClassName="buttonClassName"
      >
        <Header tenantsData={tenantsData} />
      </FormComposerV2>
      {showToast && <Toast error={true} label={t(showToast)} onClose={closeToast} />}
      <div className="employee-login-home-footer" style={{ backgroundColor: "unset" }}>
        <img
          alt="Powered by DIGIT"
          src={window?.globalConfigs?.getConfig?.("DIGIT_FOOTER_BW")}
          style={{ cursor: "pointer" }}
          onClick={() => {
            window.open(window?.globalConfigs?.getConfig?.("DIGIT_HOME_URL"), "_blank").focus();
          }}
        />{" "}
      </div>
    </Background>
  );
};

Login.propTypes = {
  loginParams: PropTypes.any,
};

Login.defaultProps = {
  loginParams: null,
};

export default Login;
