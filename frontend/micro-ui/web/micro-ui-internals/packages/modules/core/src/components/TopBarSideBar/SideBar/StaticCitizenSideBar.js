import React, { useEffect, useRef, useState } from "react";
import {
  HomeIcon,
  EditPencilIcon,
  LogoutIcon,
  Loader,
  AddressBookIcon,
  PropertyHouse,
  CaseIcon,
  CollectionIcon,
  PTIcon,
  OBPSIcon,
  PGRIcon,
  FSMIcon,
  WSICon,
  MCollectIcon,
  Phone,
  BirthIcon,
  DeathIcon,
  FirenocIcon,
} from "@egovernments/digit-ui-react-components";
import { Link, useLocation } from "react-router-dom";
import SideBarMenu from "../../../config/sidebar-menu";
import { useTranslation } from "react-i18next";
import { useHistory } from "react-router-dom";
import LogoutDialog from "../../Dialog/LogoutDialog";
import ChangeCity from "../../ChangeCity";
import { defaultImage } from "../../utils";

/* 
Feature :: Citizen Webview sidebar
*/
const Profile = ({ info, stateName, t }) => (
  <div className="profile-section">
    <div className="imageloader imageloader-loaded">
      <img className="img-responsive img-circle img-Profile" src={defaultImage} />
    </div>
    <div id="profile-name" className="label-container name-Profile">
      <div className="label-text"> {info?.name} </div>
    </div>
    <div id="profile-location" className="label-container loc-Profile">
      <div className="label-text"> {info?.mobileNumber} </div>
    </div>
    {info?.emailId && (
      <div id="profile-emailid" className="label-container loc-Profile">
        <div className="label-text"> {info.emailId} </div>
      </div>
    )}
    <div className="profile-divider"></div>
    {window.location.href.includes("/employee") &&
      !window.location.href.includes("/employee/user/login") &&
      !window.location.href.includes("employee/user/language-selection") && <ChangeCity t={t} mobileView={true} />}
  </div>
);
const IconsObject = {
  CommonPTIcon: <PTIcon className="icon" />,
  OBPSIcon: <OBPSIcon className="icon" />,
  propertyIcon: <PropertyHouse className="icon" />,
  TLIcon: <CaseIcon className="icon" />,
  PGRIcon: <PGRIcon className="icon" />,
  FSMIcon: <FSMIcon className="icon" />,
  WSIcon: <WSICon className="icon" />,
  MCollectIcon: <MCollectIcon className="icon" />,
  BillsIcon: <CollectionIcon className="icon" />,
  BirthIcon: <BirthIcon className="icon" />,
  DeathIcon: <DeathIcon className="icon" />,
  FirenocIcon: <FirenocIcon className="icon" />,
  HomeIcon: <HomeIcon className="icon" styles={{ fill: "#fff" }} />,
  EditPencilIcon: <EditPencilIcon className="icon" />,
  LogoutIcon: <LogoutIcon className="icon" styles={{ fill: "#fff" }} />,
  Phone: <Phone className="icon" style={{ fill: "#fff" }} />,
};
const StaticCitizenSideBar = ({ linkData, islinkDataLoading }) => {
  const { t } = useTranslation();
  const history = useHistory();
  const location = useLocation();
  const { pathname } = location;
  const { data: storeData, isFetched, isLoading } = Digit.Hooks.useStore.getInitData();
  const { stateInfo } = storeData || {};
  const user = Digit.UserService.getUser();
  let isMobile = window.Digit.Utils.browser.isMobile();

  const [isEmployee, setisEmployee] = useState(false);
  const [isSidebarOpen, toggleSidebar] = useState(false);
  const [showDialog, setShowDialog] = useState(false);

  const sidebarRef = useRef(null);
  const [subNav, setSubNav] = useState(false);

  useEffect(() => {
    if (isLoading) {
      return <React.Fragment></React.Fragment>;
    }

    if (sidebarRef.current && "style" in sidebarRef.current) sidebarRef.current.style.cursor = "pointer";
    collapseNav();
  }, [isLoading]);

  const expandNav = () => {
    if (sidebarRef.current && "style" in sidebarRef.current) sidebarRef.current.style.width = "260px";
    // sidebarRef.current.style.overflow = "auto";
    setSubNav(true);
  };
  const collapseNav = () => {
    if (sidebarRef.current && "style" in sidebarRef.current) {
      sidebarRef.current.style.width = "60px";
      sidebarRef.current.style.overflow = "hidden";
    }
    setSubNav(false);
  };

  const handleLogout = () => {
    toggleSidebar(false);
    setShowDialog(true);
  };
  const handleOnSubmit = () => {
    Digit.UserService.logout();
    setShowDialog(false);
  };
  const handleOnCancel = () => {
    setShowDialog(false);
  };

  if (islinkDataLoading) {
    return <Loader />;
  }

  const redirectToLoginPage = () => {
    // localStorage.clear();
    // sessionStorage.clear();
    history.push(`/${window?.contextPath}/citizen/dristi/home/login`);
  };
  const showProfilePage = () => {
    history.push(`/${window?.contextPath}/citizen/user/profile`);
  };

  let menuItems = [...SideBarMenu(t, showProfilePage, redirectToLoginPage, isEmployee)];

  menuItems = menuItems.filter((item) => item.element !== "LANGUAGE");

  const tenantId = Digit.ULBService.getCurrentTenantId();
  const MenuItem = ({ item }) => {
    const leftIconArray = item?.icon || item.icon?.type?.name;
    const leftIcon = leftIconArray ? IconsObject[leftIconArray] : IconsObject.BillsIcon;
    let itemComponent;
    if (item.type === "component") {
      itemComponent = item.action;
    } else {
      itemComponent = item.text;
    }
    const Item = () => (
      <span className="menu-item" {...item.populators} style={{ marginLeft: 0 }}>
        {leftIcon}
        <div
          className="menu-label"
          style={{ color: (!(pathname === item?.link || pathname === item?.sidebarURL || pathname.includes(item?.link)) && "#fff") || "#fff" }}
        >
          {itemComponent}
        </div>
      </span>
    );
    if (item.type === "external-link") {
      return (
        <a href={item.link} style={{ textDecoration: "none" }}>
          <Item />
        </a>
      );
    }
    if (item.type === "link") {
      return (
        <Link to={item?.link} style={{ textDecoration: "none", marginLeft: 0 }}>
          <Item />
        </Link>
      );
    }

    return <Item />;
  };
  // let profileItem;

  if (isFetched && user && user.access_token) {
    // profileItem = <Profile info={user?.info} stateName={stateInfo?.name} t={t} />;
    menuItems = menuItems.filter((item) => item?.id !== "login-btn");
    menuItems = [
      ...menuItems,
      // {
      //   text: t("EDIT_PROFILE"),
      //   element: "PROFILE",
      //   icon: "EditPencilIcon",
      //   populators: {
      //     onClick: showProfilePage,
      //   },
      // },
      {
        text: t("CORE_COMMON_LOGOUT"),
        element: "LOGOUT",
        icon: "LogoutIcon",
        populators: { onClick: handleLogout },
      },
    ];
  }
  Object.keys(linkData)
    ?.sort((x, y) => y.localeCompare(x))
    ?.map((key) => {
      if (linkData[key][0]?.sidebar === `${window.contextPath}-links`) {
        menuItems.splice(0, 0, {
          type: linkData[key][0]?.sidebarURL?.includes(window?.contextPath) ? "link" : "external-link",
          text: t(`ACTION_TEST_${Digit.Utils.locale.getTransformedLocale(key)}`),
          links: linkData[key],
          icon: linkData[key][0]?.leftIcon,
          link: linkData[key][0]?.sidebarURL,
        });
      }
    });

  return (
    <React.Fragment>
      <div className="sidebar" ref={sidebarRef} onMouseOver={expandNav} onMouseLeave={collapseNav} style={{ margin: 0, minWidth: 60, zIndex: 9998 }}>
        <div className="new-sidebar show">
          {/* {profileItem} */}
          <div className="drawer-desktop">
            {menuItems?.map((item, index) => (
              <div
                className={`sidebar-list ${
                  pathname === item?.link ||
                  pathname === item?.sidebarURL ||
                  (pathname.includes(item?.link) && pathname !== "/digit-ui/citizen/dristi/home/login") ||
                  (item.type !== "link" && pathname === "/digit-ui/citizen/dristi/home/login")
                    ? "active"
                    : ""
                }`}
                key={index}
              >
                <MenuItem item={item} />
              </div>
            ))}
          </div>
        </div>
        <div>{showDialog && <LogoutDialog onSelect={handleOnSubmit} onCancel={handleOnCancel} onDismiss={handleOnCancel}></LogoutDialog>}</div>
      </div>
    </React.Fragment>
  );
};

export default StaticCitizenSideBar;
