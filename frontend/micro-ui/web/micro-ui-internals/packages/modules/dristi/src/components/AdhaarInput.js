import React, { useState, useRef, useEffect, useMemo } from "react";

function splitStringIntoChunks(numberString) {
  const chunks = [];
  for (let i = 0; i < numberString.length; i += 4) {
    chunks.push(numberString.slice(i, i + 4));
  }
  while (chunks.length < 3) {
    chunks.push("");
  }
  return chunks;
}

const AadhaarInput = (props) => {
  const { formData = {}, onSelect, config, t, value } = props;

  const [boxCount, setboxCount] = useState(() => setInitialboxCount());
  const [focusedInput, setFocusedInput] = useState(null);
  const inputRefs = [useRef(), useRef(), useRef()];
  const inputs = useMemo(() => config?.populators?.inputs, [config?.populators?.inputs]);

  function setInitialboxCount() {
    if (formData && formData[config.key].aadharNumber) {
      return splitStringIntoChunks(formData[config.key].aadharNumber);
    } else return ["", "", ""];
  }

  useEffect(() => {
    const handleClickOutside = (event) => {
      if (!inputRefs?.some((ref) => ref.current?.contains(event.target))) {
        if (focusedInput !== null) {
          inputRefs[focusedInput].current?.focus();
        }
      }
    };

    document.addEventListener("click", handleClickOutside);
    return () => {
      document.removeEventListener("click", handleClickOutside);
    };
  }, [focusedInput]);

  const handleChange = (key, value, index) => {
    const newInputs = [...boxCount];
    newInputs[index] = value;

    if (value.length === 4 && index < 2) {
      focusNextInput(index + 1);
    }
    setboxCount(newInputs);
    const fullValue = newInputs.join("");
    setValue(fullValue, key);
  };
  function setValue(value, input) {
    if (Array.isArray(input)) {
      onSelect(config.key, {
        ...formData[config.key],
        ...input.reduce((res, curr) => {
          res[curr] = value[curr];
          return res;
        }, {}),
      });
    } else onSelect(config.key, { ...formData[config.key], [input]: value });
  }
  const handleFocus = (index) => {
    setFocusedInput(index);
  };

  const focusNextInput = (index) => {
    if (index < inputRefs.length) {
      inputRefs[index].current.focus();
    }
  };

  const handleKeyUp = (index, e) => {
    if (e.key === "Backspace" && index > 0 && boxCount[index] === "") {
      focusPreviousInput(index - 1);
    }
  };

  const focusPreviousInput = (index) => {
    if (index >= 0) {
      inputRefs[index].current.focus();
    }
  };

  return (
    <React.Fragment>
      {inputs.map((input, index1) => {
        let currentValue = (formData && formData[config.key] && formData[config.key][input.name]) || "";
        return (
          <div className="input-otp-wrap">
            {boxCount.map((value, index) => (
              <input
                key={index}
                ref={inputRefs[index]}
                type="text"
                maxLength={4}
                onInput={(e) => {
                  const value = e.target.value.replace(/\D/, "");
                  if (value !== e.target.value) {
                    e.target.value = value;
                  }
                }}
                value={value}
                onChange={(e) => handleChange(input.name, e.target.value, index)}
                onFocus={() => handleFocus(index)}
                onKeyUp={(e) => handleKeyUp(index, e)}
                className="input-otp"
              />
            ))}
          </div>
        );
      })}
    </React.Fragment>
  );
};

export default AadhaarInput;
