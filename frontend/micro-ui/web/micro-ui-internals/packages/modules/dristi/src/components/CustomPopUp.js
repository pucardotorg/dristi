import React from "react";

function CustomPopUp({ children, onClose, position }) {
  const popupStyle = {
    top: `${position.top}px`,
    left: `${position.left}px`,
  };

  return (
    <div className="pop-up-class" onClick={onClose}>
      <div className="popup" style={popupStyle} onClick={(e) => e.stopPropagation()}>
        {onClose && <span className="close" onClick={onClose}></span>}
        {children}
      </div>
    </div>
  );
}

export default CustomPopUp;
