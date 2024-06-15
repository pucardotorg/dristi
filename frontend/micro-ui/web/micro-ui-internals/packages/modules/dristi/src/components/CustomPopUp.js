import React from "react";
import ReactDOM from "react-dom";

function CustomPopUp({ children, onClose, popupstyle, anchorRef }) {
  if (!anchorRef) return null;
  return ReactDOM.createPortal(
    <div className="pop-up-class" style={{ position: { top: 0, right: 0 } }} onClick={onClose}>
      <div className="popup" style={popupstyle} onClick={(e) => e.stopPropagation()}>
        {onClose && <span className="close" onClick={onClose}></span>}
        {children}
      </div>
    </div>,
    anchorRef
  );
}

export default CustomPopUp;
