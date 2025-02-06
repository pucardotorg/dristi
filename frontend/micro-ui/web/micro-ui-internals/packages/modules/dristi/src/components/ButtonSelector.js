import React from "react";
import PropTypes from "prop-types";

const ButtonSelector = (props) => {
  let theme = "selector-button-primary";
  switch (props.theme) {
    case "border":
      theme = "selector-button-border";
      break;
    default:
      theme = "selector-button-primary";
      break;
  }
  return (
    <button
      className={props.isDisabled ? `selector-button-primary-disabled ${props.className}` : `${theme} ${props.className}`}
      type={props.type || "submit"}
      form={props.formId}
      onClick={props.onSubmit}
      disabled={props.isDisabled}
      style={props.style ? props.style : null}
      title={props.title ? props.title : null}
    >
      {props?.label && (
        <h2 style={{ ...props?.textStyles, ...{ width: "100%" } }} className={props?.textClassName}>
          {props.label}
        </h2>
      )}
      {props.ButtonBody ? props.ButtonBody : ""}
    </button>
  );
};

ButtonSelector.propTypes = {
  /**
   * ButtonSelector content
   */
  label: PropTypes.string.isRequired,
  /**
   * button border theme
   */
  theme: PropTypes.string,
  /**
   * click handler
   */
  onSubmit: PropTypes.func,
  /**
   * CustomBody
   */
  ButtonBody: PropTypes.any,
  /**
   * ButtonSelector className
   */
  className: PropTypes.string,
};

ButtonSelector.defaultProps = {
  label: "",
  theme: "",
  onSubmit: undefined,
  ButtonBody: undefined,
  className: "",
};

export default ButtonSelector;
