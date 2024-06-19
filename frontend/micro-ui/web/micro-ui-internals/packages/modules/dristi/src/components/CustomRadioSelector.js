import { RadioButtons } from "@egovernments/digit-ui-react-components";
import React from "react";
import { ReactComponent as InfoToolTipIcon } from "../images/Vector.svg";

function CustomRadioSelector({ selectionQuestionText, t, ...RadioButtonProps }) {
  return (
    <div style={{ ...(RadioButtonProps.style ? RadioButtonProps.style : {}) }}>
      <div className="custom-radio-question-div">
        <p style={{ fontWeight: 700 }}>{t(selectionQuestionText)}</p>
        <span>
          <InfoToolTipIcon></InfoToolTipIcon>
        </span>
      </div>
      <div className="custom-radio-button-div">
        <RadioButtons
          {...RadioButtonProps}
          style={{ display: "flex", ...(RadioButtonProps.radioButtonStyle ? RadioButtonProps.radioButtonStyle : {}) }}
          selectorStyle={RadioButtonProps.selectorStyle}
          inputStyle={RadioButtonProps.inputStyle}
        />
      </div>
    </div>
  );
}

export default CustomRadioSelector;
