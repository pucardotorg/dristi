import React from "react";
import { CloseIcon } from "../icons/svgIndex";

const RemoveableTag = ({ text, onClick, extraStyles, disabled = false }) => (
  <div className="tag" style={extraStyles ? extraStyles?.tagStyles : {}}>
    <span className="text" style={extraStyles ? extraStyles?.textStyles : {}}>
      {text}
    </span>
    <span onClick={disabled ? null : onClick}>
      <CloseIcon className="close" style={extraStyles ? extraStyles?.closeIconStyles : {}} />
    </span>
  </div>
);

export default RemoveableTag;
