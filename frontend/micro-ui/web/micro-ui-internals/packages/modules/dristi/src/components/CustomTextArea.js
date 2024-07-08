import React from "react";
import PropTypes from "prop-types";

const CustomTextArea = (props) => {
  const handleChange = (event) => {
    const newText = event.target.value;
    props.onTextChange(newText);
  };
  return (
    <div style={props.style}>
      <div>
        <h2>{props.info}</h2>
      </div>
      <div>
        <textarea
          placeholder={props.placeholder}
          name={props.name}
          ref={props.inputRef}
          style={props.textAreaStyle}
          id={props.id}
          value={props.value}
          onChange={handleChange}
          className={`${props.disable && "disabled"} ${props?.className ? props?.className : ""}`}
          minLength={props.minlength}
          maxLength={props.maxlength}
          autoComplete="off"
          rows={props.rows}
          cols={props.cols}
          disabled={props.disabled}
          onResize={"none"}
          pattern={props?.validation && props.ValidationRequired ? props?.validation?.pattern : props.pattern}
        ></textarea>
        {<p className="cell-text">{props.hintText}</p>}
      </div>
    </div>
  );
};

CustomTextArea.propTypes = {
  userType: PropTypes.string,
  name: PropTypes.string.isRequired,
  ref: PropTypes.func,
  value: PropTypes.string,
  onChange: PropTypes.func,
  id: PropTypes.string,
  info: PropTypes.string,
};

CustomTextArea.defaultProps = {
  ref: undefined,
  onChange: undefined,
};

export default CustomTextArea;
