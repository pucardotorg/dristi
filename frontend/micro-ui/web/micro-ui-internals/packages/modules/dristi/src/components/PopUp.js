import React from "react";

const PopUp = ({ className, popUpStyleMain, ...props }) => {
  return (
    <div className={`popup-wrap ${className}`} style={popUpStyleMain}>
      {props.children}
    </div>
  );
};

export default PopUp;
