import { Dropdown, Hamburger } from "@egovernments/digit-ui-react-components";
import React from "react";
import { useHistory, useLocation } from "react-router-dom";
import ChangeCity from "../ChangeCity";
import ChangeLanguage from "../ChangeLanguage";
import TopBarComponent from "./TopBarComponent";
const TextToImg = (props) => (
  <span className="user-img-txt" onClick={props.toggleMenu} title={props.name}>
    {props?.name?.[0]?.toUpperCase()}
  </span>
);
const TopBar = ({
  t,
  stateInfo,
  toggleSidebar,
  isSidebarOpen,
  handleLogout,
  userDetails,
  CITIZEN,
  cityDetails,
  mobileView,
  userOptions,
  handleUserDropdownSelection,
  logoUrl,
  showLanguageChange = true,
}) => {
  const [profilePic, setProfilePic] = React.useState(null);

  React.useEffect(async () => {
    const tenant = Digit.ULBService.getCurrentTenantId();
    const uuid = userDetails?.info?.uuid;
    if (uuid) {
      const usersResponse = await Digit.UserService.userSearch(tenant, { uuid: [uuid] }, {});
      if (usersResponse && usersResponse.user && usersResponse.user.length) {
        const userDetails = usersResponse.user[0];
        const thumbs = userDetails?.photo?.split(",");
        setProfilePic(thumbs?.at(0));
      }
    }
  }, [profilePic !== null, userDetails?.info?.uuid]);

  const CitizenHomePageTenantId = Digit.ULBService.getCitizenCurrentTenant(true);

  let history = useHistory();
  const { pathname } = useLocation();

  const conditionsToDisableNotificationCountTrigger = () => {
    if (Digit.UserService?.getUser()?.info?.type === "EMPLOYEE") return false;
    if (Digit.UserService?.getUser()?.info?.type === "CITIZEN") {
      if (!CitizenHomePageTenantId) return false;
      else return true;
    }
    return false;
  };

  const { data: { unreadCount: unreadNotificationCount } = {}, isSuccess: notificationCountLoaded } = Digit.Hooks.useNotificationCount({
    tenantId: CitizenHomePageTenantId,
    config: {
      enabled: conditionsToDisableNotificationCountTrigger(),
    },
  });

  const updateSidebar = () => {
    if (!Digit.clikOusideFired) {
      toggleSidebar(true);
    } else {
      Digit.clikOusideFired = false;
    }
  };

  function onNotificationIconClick() {
    history.push(`/${window?.contextPath}/citizen/engagement/notifications`);
  }

  const urlsToDisableNotificationIcon = (pathname) =>
    !!Digit.UserService?.getUser()?.access_token
      ? false
      : [`/${window?.contextPath}/citizen/select-language`, `/${window?.contextPath}/citizen/select-location`].includes(pathname);

  if (CITIZEN) {
    return (
      <TopBarComponent
        img={stateInfo?.logoUrlWhite}
        isMobile={true}
        toggleSidebar={updateSidebar}
        logoUrl={stateInfo?.logoUrlWhite}
        onLogout={handleLogout}
        userDetails={userDetails}
        notificationCount={unreadNotificationCount < 99 ? unreadNotificationCount : 99}
        notificationCountLoaded={notificationCountLoaded}
        cityOfCitizenShownBesideLogo={t(CitizenHomePageTenantId)}
        onNotificationIconClick={onNotificationIconClick}
        hideNotificationIconOnSomeUrlsWhenNotLoggedIn={true}
        hideChangeLangOnSomeUrlsWhenNotLoggedIn={location.pathname.includes("/select-language") ? true : false}
        changeLanguage={
          !mobileView ? (
            <ChangeLanguage
              style={{
                display: "flex",
                alignItems: "center",
                justifyContent: "center",
              }}
              dropdownClassName={"change-language-citizen"}
              dropdown={true}
            />
          ) : null
        }
        userOptions={userOptions}
        handleUserDropdownSelection={handleUserDropdownSelection}
        TextToImg={TextToImg}
        mobileView={mobileView}
      />
    );
  }
  const loggedin = userDetails?.access_token ? true : false;

  //checking for custom topbar components
  const CustomEmployeeTopBar = Digit.ComponentRegistryService?.getComponent("CustomEmployeeTopBar");

  if (CustomEmployeeTopBar) {
    return (
      <CustomEmployeeTopBar
        {...{
          t,
          stateInfo,
          toggleSidebar,
          isSidebarOpen,
          handleLogout,
          userDetails,
          CITIZEN,
          cityDetails,
          mobileView,
          userOptions,
          handleUserDropdownSelection,
          logoUrl,
          showLanguageChange,
          loggedin,
        }}
      />
    );
  }
  return (
    <div className="topbar">
      <div className="hambuger-back-wrapper">
        {mobileView ? <Hamburger handleClick={toggleSidebar} color="#9E9E9E" /> : null}

        <img
          className="city"
          id="topbar-logo"
          style={{ display: "flex", alignItems: "center", height: "40px", minWidth: "20px" }}
          src={
            "https://s3-alpha-sig.figma.com/img/1d0d/d20b/17bba4d1a5b09a0840e516227bbe9364?Expires=1717977600&Key-Pair-Id=APKAQ4GOSFWCVNEHN3O4&Signature=kCzBVvYwPETCwHpSAAaIFB1MZQisvQ1avryik4YbCCH0HDVfJp1dxdl8UNPrDgf34YYKgyA~nNbNeGJ8N9eurZz3JbjCzdLdJiGnJL9ANqQgAOdZV4c9TAroinpq9RhTdTOjJg2U4f00PCM8jYTErO2WvNGAlXx~SqSBIj~SuiwYR2Xf2mozObFIDAjyW2gyDa6HbVLPkdINrnDJnm73S3HrqdL3q6DZkYEmEYd7qtZW0XJ3ywZBbXEuRnhumX6Ylf9RTzLdJqlfP5WaQDwjdxhQTNmIUjBYMqaF-SWqR31gZWISkKjK3uXdtfjAQBW2CYzMu-n~Alh9FwD3dx-SZQ__" ||
            "https://cdn.jsdelivr.net/npm/@egovernments/digit-ui-css@1.0.7/img/m_seva_white_logo.png"
          }
          alt="mSeva"
        />
        <img
          className="city"
          id="topbar-logo"
          style={{ display: "flex", alignItems: "center", height: "40px" }}
          src={
            "https://s3-alpha-sig.figma.com/img/726d/3e39/0de38f6dc850f12fe24f8ef1b9d734a6?Expires=1717977600&Key-Pair-Id=APKAQ4GOSFWCVNEHN3O4&Signature=bdTffHkNhRtbRg7AJmVXJMEpUkC07JA~tyhPaNFaR72o1Vvf0LzLTJRajIwPGt3tryH4jRUYqpxfRqMOL-n2mEvB77r0XgMScymYc1sdeVjkFAqkpMpFkmkzbQ5Vg8w22--v3~rMHrILGXNT09pS8mCquyvUDy73qGwHcUzULXgNVyCoN1NRPEpI5AddOTqO7B2ZCq1n6RMm8trCnIYppfA8sKGxc843tlPQd0dUZc1KO-unKJ41AutHfGqLUsu9-NY-s~aXFwXLsx3BkNjm8xK3hu0euJgPAEPQq941hZFm6hMizxra5RLFvyP8yk~iJg-TTZ0VSdxLltkFK2IW~g__" ||
            "https://cdn.jsdelivr.net/npm/@egovernments/digit-ui-css@1.0.7/img/m_seva_white_logo.png"
          }
          alt="mSeva"
        />
      </div>
      <div className="RightMostTopBarOptions">
        {/* {loggedin &&
          (cityDetails?.city?.ulbGrade ? (
            <p className="ulb" style={mobileView ? { fontSize: "14px", display: "inline-block" } : {}}>
              {t(cityDetails?.i18nKey).toUpperCase()}{" "}
              {t(`ULBGRADE_${cityDetails?.city?.ulbGrade.toUpperCase().replace(" ", "_").replace(".", "_")}`).toUpperCase()}
            </p>
          ) : (
            <img className="state" src={logoUrl} />
          ))}
        {!loggedin && (
          <p className="ulb" style={mobileView ? { fontSize: "14px", display: "inline-block" } : {}}>
            {t(`MYCITY_${stateInfo?.code?.toUpperCase()}_LABEL`)} {t(`MYCITY_STATECODE_LABEL`)}
          </p>
        )} */}
        {!mobileView && (
          <div className={mobileView ? "right" : "flex-right right column-gap-15"} style={!loggedin ? { width: "80%" } : {}}>
            {/* <div className="left">
              {!window.location.href.includes("employee/user/login") && !window.location.href.includes("employee/user/language-selection") && (
                <ChangeCity dropdown={true} t={t} />
              )}
            </div> */}
            <div className="left">{showLanguageChange && <ChangeLanguage dropdown={true} />}</div>
            {userDetails?.access_token && (
              <div className="left">
                <Dropdown
                  option={userOptions}
                  optionKey={"name"}
                  select={handleUserDropdownSelection}
                  showArrow={true}
                  freeze={true}
                  style={mobileView ? { right: 0 } : {}}
                  optionCardStyles={{ overflow: "revert", display: "table", marginRight: '50px' }}
                  topbarOptionsClassName={"topbarOptionsClassName"}
                  customSelector={
                    profilePic == null ? (
                      <TextToImg name={userDetails?.info?.name || userDetails?.info?.userInfo?.name || "Employee"} />
                    ) : (
                      <img src={profilePic} style={{ height: "48px", width: "48px", borderRadius: "50%" }} />
                    )
                  }
                />
              </div>
            )}
            {/* <img className="state" src={logoUrl} /> */}
          </div>
        )}
      </div>
    </div>
  );
};

export default TopBar;
