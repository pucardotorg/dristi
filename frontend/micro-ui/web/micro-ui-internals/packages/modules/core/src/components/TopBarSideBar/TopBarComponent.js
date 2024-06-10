import React, { useEffect, useState } from "react";
import PropTypes from "prop-types";
import { useLocation } from "react-router-dom";
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

          <div style={{ display: "flex", gap: "16px" }}>
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
