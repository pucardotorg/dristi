import React, { useEffect, useState } from "react";
import PropTypes from "prop-types";
import { useLocation } from "react-router-dom";
// import BackButton from "./BackButton";
import { Hamburger, NotificationBell } from "@egovernments/digit-ui-react-components";

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
}) => {
  const { pathname } = useLocation();

  // const showHaburgerorBackButton = () => {
  //   if (pathname === "/digit-ui/citizen" || pathname === "/digit-ui/citizen/" || pathname === "/digit-ui/citizen/select-language") {
  //     return <Hamburger handleClick={toggleSidebar} />;
  //   } else {
  //     return <BackButton className="top-back-btn" />;
  //   }
  // };
  return (
    <div className="navbar">
      <div className="center-container back-wrapper">
        <div className="hambuger-back-wrapper">
          {isMobile && <Hamburger handleClick={toggleSidebar} />}
          <img
            className="city"
            id="topbar-logo"
            src={img || "https://cdn.jsdelivr.net/npm/@egovernments/digit-ui-css@1.0.7/img/m_seva_white_logo.png"}
            alt="mSeva"
          />
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
