import React, { useEffect } from "react";

import ButtonSelector from "./ButtonSelector";
import { HeaderBar, PopUp, Toast } from "@egovernments/digit-ui-react-components";

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
  submitTextClassName = {},
  className
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
        <HeaderBar main={headerBarMain} end={headerBarEnd} style={headerBarMainStyle ? headerBarMainStyle : {}} />
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
              <ButtonSelector textStyles={{ margin: "0px" }} theme="border" label={actionCancelLabel} onSubmit={actionCancelOnSubmit} style={style} />
            ) : (
              <div></div>
            )}
            {!hideSubmit ? (
              <ButtonSelector
                textStyles={{ margin: "0px" }}
                label={actionSaveLabel}
                onSubmit={actionSaveOnSubmit}
                formId={formId}
                isDisabled={isDisabled}
                style={style}
                textClassName={submitTextClassName}
              />
            ) : null}
          </div>
        </div>
      </div>
      {error && <Toast label={error} onClose={() => setError(null)} error />}
    </PopUp>
  );
};

export default Modal;
