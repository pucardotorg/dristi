import React, { useEffect, useRef, useState } from "react";
import PropTypes from "prop-types";

const BACKSPACE = 8;

const SingleInput = ({ isFocus, onChange, onFocus, value, inputStyle, ...rest }) => {
  const inputRef = useRef();
  useEffect(() => {
    if (isFocus) {
      inputRef.current.focus();
    }
  }, [isFocus]);

  return (
    <input
      style={{ width: "70px", ...(inputStyle ? inputStyle : {}) }}
      className="input-otp"
      maxLength={1}
      onChange={onChange}
      onFocus={onFocus}
      ref={inputRef}
      value={value ? value : ""}
      {...rest}
      type="text"
      pattern="[0-9]*"
      onInput={(e) => {
        e.target.value = e.target.value.replace(/[^0-9]/g, "");
      }}
    />
  );
};

const OTPInput = ({ inputStyle, otpInputStyles, ...props }) => {
  const [activeInput, setActiveInput] = useState(0);

  const isInputValueValid = (value) => {
    return typeof value === "string" && value.trim().length === 1;
  };

  const changeCodeAtFocus = (value) => {
    const { onChange } = props;
    const otp = getOtpValue();
    otp[activeInput] = value[0];
    const otpValue = otp.join("");
    onChange(otpValue);
  };

  const focusNextInput = () => {
    setActiveInput((activeInput) => Math.min(activeInput + 1, props.length - 1));
  };

  const focusPrevInput = () => {
    setActiveInput((activeInput) => Math.max(activeInput - 1, 0));
  };

  const getOtpValue = () => (props.value ? props.value.toString().split("") : []);

  const handleKeyDown = (event) => {
    if (event.keyCode === BACKSPACE || event.key === "Backspace") {
      event.preventDefault();
      changeCodeAtFocus("");
      focusPrevInput();
    }
  };

  function inputChange(event) {
    const { value } = event.target;
    changeCodeAtFocus(value);
    if (isInputValueValid(value)) {
      focusNextInput();
    }
  }

  const OTPStack = [];
  const otp = getOtpValue();
  for (let i = 0; i < props.length; i++) {
    OTPStack.push(
      <SingleInput
        inputStyle={inputStyle}
        key={i}
        isFocus={activeInput === i}
        onChange={inputChange}
        onKeyDown={handleKeyDown}
        onFocus={(e) => {
          setActiveInput(i);
          e.target.select();
        }}
        value={otp[i]}
      />
    );
  }

  return (
    <div style={{ backgroundColor: "none", ...otpInputStyles }} className="input-otp-wrap">
      {OTPStack}
    </div>
  );
};

OTPInput.propTypes = {
  length: PropTypes.number,
};

OTPInput.defaultProps = {
  length: 0,
};

export default OTPInput;
