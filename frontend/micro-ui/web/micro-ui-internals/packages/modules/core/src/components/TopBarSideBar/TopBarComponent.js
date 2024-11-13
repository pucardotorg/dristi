import React, { useEffect, useState } from "react";
import PropTypes from "prop-types";
import { useLocation, useHistory } from "react-router-dom";
// import BackButton from "./BackButton";
import { Dropdown, Hamburger, NotificationBell } from "@egovernments/digit-ui-react-components";

const TopBarComponent = ({
  img,
  isMobile,
  logoUrl,
  onLogout,
  toggleSidebar,
  ulb,
  userDetails,
  notificationCount,
  notificationCountLoaded,
  cityOfCitizenShownBesideLogo,
  onNotificationIconClick,
  hideNotificationIconOnSomeUrlsWhenNotLoggedIn,
  changeLanguage,
  hideChangeLangOnSomeUrlsWhenNotLoggedIn = false,
  userOptions,
  handleUserDropdownSelection,
  mobileView,
  profilePic,
  TextToImg,
}) => {
  const { pathname } = useLocation();
  const history = useHistory();
  const token = window.localStorage.getItem("token");
  const isUserLoggedIn = Boolean(token);
  // const showHaburgerorBackButton = () => {
  //   if (pathname === "/digit-ui/citizen" || pathname === "/digit-ui/citizen/" || pathname === "/digit-ui/citizen/select-language") {
  //     return <Hamburger handleClick={toggleSidebar} />;
  //   } else {
  //     return <BackButton className="top-back-btn" />;
  //   }
  // };
  return (
    <div className="navbar" style={{ zIndex: "999" }}>
      <div className="center-container back-wrapper">
        <div className="hambuger-back-wrapper">
          {isMobile && <Hamburger handleClick={toggleSidebar} />}

          <div
            style={{ display: "flex", gap: "16px", cursor: "pointer" }}
            onClick={() => {
              const pathUnwind = pathname.split("/").slice(0, 3).join("/") + (isUserLoggedIn ? "/home/home-pending-task" : "/dristi");
              history.push(pathUnwind);
            }}
          >
            <img
              className="city"
              id="topbar-logo"
              style={{ display: "flex", alignItems: "center", height: "40px", minWidth: "20px" }}
              src={
                "https://pucarfilestore.blob.core.windows.net/pucar-filestore/kl/17bba4d1a5b09a0840e516227bbe9364.png" ||
                "https://cdn.jsdelivr.net/npm/@egovernments/digit-ui-css@1.0.7/img/m_seva_white_logo.png"
              }
              alt="mSeva"
            />
            <img
              className="city"
              id="topbar-logo"
              style={{ display: "flex", alignItems: "center", height: "40px" }}
              src={
                "https://pucarfilestore.blob.core.windows.net/pucar-filestore/kl/Crafting_ON_24x7_final%20(1).png" ||
                "https://cdn.jsdelivr.net/npm/@egovernments/digit-ui-css@1.0.7/img/m_seva_white_logo.png"
              }
              alt="mSeva"
            />
          </div>
          <h3>{cityOfCitizenShownBesideLogo}</h3>
        </div>

        <div className="RightMostTopBarOptions">
          {!hideChangeLangOnSomeUrlsWhenNotLoggedIn ? changeLanguage : null}
          {!hideNotificationIconOnSomeUrlsWhenNotLoggedIn ? (
            <div className="EventNotificationWrapper" onClick={onNotificationIconClick}>
              {notificationCountLoaded && notificationCount ? (
                <span>
                  <p>{notificationCount}</p>
                </span>
              ) : null}
              <NotificationBell />
            </div>
          ) : null}
          {userDetails?.access_token && (
            <div className="left">
              <Dropdown
                option={userOptions}
                optionKey={"name"}
                select={handleUserDropdownSelection}
                showArrow={true}
                freeze={true}
                style={mobileView ? { right: 0 } : {}}
                optionCardStyles={{ overflow: "revert", display: "table" }}
                topbarOptionsClassName={"topbarOptionsClassNameCitizen"}
                customSelector={
                  profilePic == null ? (
                    <TextToImg name={userDetails?.info?.name || userDetails?.info?.userInfo?.name || "Citizen"} />
                  ) : (
                    <img src={profilePic} alt="profile" style={{ height: "48px", width: "48px", borderRadius: "50%", color: "#5B2448" }} />
                  )
                }
              />
            </div>
          )}
        </div>
      </div>
    </div>
  );
};

TopBarComponent.propTypes = {
  img: PropTypes.string,
};

TopBarComponent.defaultProps = {
  img: undefined,
};

export default TopBarComponent;
