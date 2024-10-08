import React, { Fragment } from "react";
import { CheckSvg } from "@egovernments/digit-ui-react-components";
import PropTypes from "prop-types";
import { useTranslation } from "react-i18next";
const CheckBox = ({
  onChange,
  label,
  value,
  disable,
  ref,
  checked,
  inputRef,
  pageType,
  labeStyle,
  index,
  isLabelFirst,
  customLabelMarkup,
  ...props
}) => {
  const { t } = useTranslation();
  const Digit = window?.Digit || {};
  const userType = pageType || Digit.SessionStorage.get("userType");
  let wrkflwStyle = props.styles;
  if (isLabelFirst) {
    return (
      <div className="checkbox-wrap" style={wrkflwStyle ? wrkflwStyle : {}}>
        <p style={labeStyle ? labeStyle : null}> {index + 1}.</p>
        <p className="label" style={{ maxWidth: "80%", marginLeft: "10px" }}>
          {label}
        </p>
        <div>
          <input
            type="checkbox"
            className={userType === "employee" ? "input-emp" : ""}
            onChange={onChange}
            style={{ cursor: "pointer", left: "90%" }}
            value={value || label}
            {...props}
            ref={inputRef}
            disabled={disable}
            checked={checked}
          />
          <p className={userType === "employee" ? "custom-checkbox-emp" : "custom-checkbox"} style={disable ? { opacity: 0.5 } : { left: "90%" }}>
            <CheckSvg />
          </p>
        </div>
      </div>
    );
  } else {
    return (
      <div className="checkbox-wrap" style={wrkflwStyle ? wrkflwStyle : {}}>
        <div>
          <input
            type="checkbox"
            className={userType === "employee" ? "input-emp" : ""}
            onChange={onChange}
            style={{ cursor: "pointer" }}
            value={value || label}
            {...props}
            ref={inputRef}
            disabled={disable}
            // {(checked ? (checked = { checked }) : null)}
            checked={checked}
          />
          <p
            className={userType === "employee" ? "custom-checkbox-emp" : "custom-checkbox"}
            style={disable ? { opacity: 0.5 } : props?.checkboxWidth ? { ...props?.checkboxWidth } : null}
          >
            {/* <img src={check} alt="" /> */}
            <CheckSvg />
          </p>
        </div>
        <p className="label" style={labeStyle ? labeStyle : null}>
          {customLabelMarkup ? (
            <>
              <p>{t("COMMON_CERTIFY_ONE")}</p>
              <br />
              <p>
                <b> {t("ES_COMMON_NOTE")}</b>
                {t("COMMON_CERTIFY_TWO")}
              </p>
            </>
          ) : (
            label
          )}
        </p>
      </div>
    );
  }
};

CheckBox.propTypes = {
  /**
   * CheckBox content
   */
  label: PropTypes.string.isRequired,
  /**
   * onChange func
   */
  onChange: PropTypes.func,
  /**
   * input ref
   */
  ref: PropTypes.func,
  userType: PropTypes.string,
};

CheckBox.defaultProps = {};

export default CheckBox;
