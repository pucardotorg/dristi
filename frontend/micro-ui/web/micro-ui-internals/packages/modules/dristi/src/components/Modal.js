import React, { useEffect } from "react";

import { HeaderBar, PopUp, Toast } from "@egovernments/digit-ui-react-components";
import ButtonSelector from "./ButtonSelector";

const Modal = ({
  headerBarMain,
  headerBarEnd,
  popupStyles,
  children,
  actionCancelLabel,
  actionCancelOnSubmit,
  actionSaveLabel,
  actionSaveOnSubmit,
  error,
  setError,
  formId,
  isDisabled,
  hideSubmit,
  style = {},
  popupModuleMianStyles,
  headerBarMainStyle,
  isOBPSFlow = false,
  popupModuleActionBarStyles = {},
  submitTextClassName = "",
  className,
  cancelButtonBody,
}) => {
  /**
   * TODO: It needs to be done from the desgin changes
   */
  const mobileView = Digit.Utils.browser.isMobile() ? true : false;
  useEffect(() => {
    document.body.style.overflowY = "hidden";
    return () => {
      document.body.style.overflowY = "auto";
    };
  }, []);
  return (
    <PopUp>
      <div className={`popup-module ${className}`} style={popupStyles}>
        {headerBarMain && <HeaderBar main={headerBarMain} end={headerBarEnd} style={headerBarMainStyle ? headerBarMainStyle : {}} />}
        <div className="popup-module-main" style={popupModuleMianStyles ? popupModuleMianStyles : {}}>
          {children}
          <div
            className="popup-module-action-bar"
            style={
              isOBPSFlow
                ? !mobileView
                  ? { marginRight: "18px" }
                  : { position: "absolute", bottom: "5%", right: "10%", left: window.location.href.includes("employee") ? "0%" : "7%" }
                : popupModuleActionBarStyles
            }
          >
            {actionCancelLabel ? (
              <ButtonSelector
                textStyles={{ margin: "0px", color: "red" }}
                theme="border"
                label={actionCancelLabel}
                onSubmit={actionCancelOnSubmit}
                style={{ border: "1px solid red" }}
                ButtonBody={cancelButtonBody}
              />
            ) : (
              <div></div>
            )}
            {actionSaveLabel && !hideSubmit
              ? (console.log(style),
                (
                  <ButtonSelector
                    textStyles={{ margin: "0px" }}
                    label={actionSaveLabel}
                    onSubmit={actionSaveOnSubmit}
                    formId={formId}
                    isDisabled={isDisabled}
                    style={style}
                    textClassName={submitTextClassName}
                  />
                ))
              : null}
          </div>
        </div>
      </div>
      {error && <Toast label={error} onClose={() => setError(null)} error />}
    </PopUp>
  );
};

export default Modal;
