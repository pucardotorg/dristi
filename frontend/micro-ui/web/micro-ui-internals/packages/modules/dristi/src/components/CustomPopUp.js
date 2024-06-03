import React from "react";
import ReactDOM from "react-dom";

function CustomPopUp({ children, onClose, position, anchorRef }) {
  if (!anchorRef) return null;
  return ReactDOM.createPortal(
    <div className="pop-up-class" onClick={onClose}>
      <div className="popup" onClick={(e) => e.stopPropagation()}>
        {onClose && <span className="close" onClick={onClose}></span>}
        {children}
      </div>
    </div>,
    anchorRef
  );
}

export default CustomPopUp;
