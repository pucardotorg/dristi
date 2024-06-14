import React, { useEffect, useState } from "react";
import PropTypes from "prop-types";
import isEqual from "lodash/isEqual";
import { useTranslation } from "react-i18next";
import { Card, CardHeader, CardSubHeader, CardText } from "@egovernments/digit-ui-react-components";

const RadioButtons = (props) => {
  const { t } = useTranslation();
  var selected = props.selectedOption;
  function selectOption(value) {
    //selected = value;
    props.onSelect(value);
  }
  return (
    <div style={props.style} className={`radio-wrap ${props?.additionalWrapperClass}`}>
      {props?.options?.map((option, ind) => {
        if (props?.optionsKey && !props?.isDependent) {
          return (
            <Card
              style={{
                ...props.style,
              }}
            >
              <div className="card-button-wrap" key={ind}>
                <span className="radio-btn-wrap" style={props && props.isRejected ? { pointerEvents: "none" } : {}}>
                  <input
                    className="radio-btn"
                    type="radio"
                    value={option}
                    checked={(props.isPTFlow && selected?.code === option.code) || isEqual(selected, option) ? 1 : 0}
                    onChange={() => selectOption(option)}
                    disabled={props?.disabled}
                    name={props.name}
                    ref={props.inputRef}
                  />
                  <span className="radio-btn-checkmark"></span>
                </span>
                <div className="button-label-main">
                  <CardSubHeader
                    style={{
                      marginLeft: "10px",
                      fontSize: "24px",
                      ...props.inputStyle,
                    }}
                  >
                    {t(option[props.optionsKey])}
                  </CardSubHeader>
                  <CardText
                    style={{
                      marginLeft: "10px",
                      fontSize: "16px",
                      ...props.inputStyle,
                    }}
                  >
                    {t(option["subText"])}
                  </CardText>
                </div>
              </div>
            </Card>
          );
        }
      })}
    </div>
  );
};

RadioButtons.propTypes = {
  selectedOption: PropTypes.any,
  onSelect: PropTypes.func,
  options: PropTypes.any,
  optionsKey: PropTypes.string,
  innerStyles: PropTypes.any,
  style: PropTypes.any,
};

RadioButtons.defaultProps = {};

export default RadioButtons;
