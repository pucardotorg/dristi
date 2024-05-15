import React from "react";
import { useState } from "react";
import { CustomArrowDownIcon, CustomArrowUpIcon, CustomCompleteIcon, CustomSchedule } from "../icons/svgIndex";

function Accordion({ t, title, count, onClick, children }) {
  const [isOpen, setOpen] = useState(false);
  const radioCardStyle = { padding: "5px", borderRadius: "5px", alignItems: "center", display: "flex" };
  return (
    <div className="accordion-wrapper" onClick={() => onClick(title, count)}>
      <div className={`accordion-title ${isOpen ? "open" : ""}`} onClick={() => setOpen(!isOpen)}>
        <span>{t(title)}</span>
        <div style={{ gap: "5px", display: "flex", alignItems: "center", justifyContent: "space-around" }}>
          <CustomSchedule />
          <span>4m</span>
          {isOpen ? <CustomArrowUpIcon /> : <CustomArrowDownIcon />}
        </div>
      </div>
      <div className={`accordion-item ${!isOpen ? "collapsed" : ""}`}>
        <div className="accordion-content">
          {children.map((item) => (
            <div className="radio-wrap" style={item.checked ? { background: "#E8E8E8", ...radioCardStyle } : radioCardStyle}>
              {item.isCompleted ? (
                <CustomCompleteIcon />
              ) : (
                <span className="radio-btn-wrap">
                  <input className="radio-btn" type="radio" checked={item?.checked} />
                  <span className="radio-btn-checkmark"></span>
                </span>
              )}
              <label>{t(item?.label)}</label>
            </div>
          ))}
        </div>
      </div>
    </div>
  );
}

export default Accordion;
