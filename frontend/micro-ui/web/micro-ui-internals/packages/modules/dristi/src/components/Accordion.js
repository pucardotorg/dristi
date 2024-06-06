import React from "react";
import { CustomArrowDownIcon, CustomCompleteIcon, CustomSchedule } from "../icons/svgIndex";

function Accordion({ t, title, handlePageChange, handleAccordionClick, children, parentIndex, isOpen, showConfirmModal }) {
  return (
    <div key={parentIndex} className="accordion-wrapper">
      <div className={`accordion-title ${isOpen ? "open" : ""}`} onClick={handleAccordionClick}>
        <span>{`${parentIndex + 1}. ${t(title)}`}</span>
        <div className="icon">
          <CustomSchedule />
          <span style={{ paddingRight: "8px" }}>4m</span>
          <span className="reverse-arrow">
            <CustomArrowDownIcon />
          </span>
        </div>
      </div>
      <div className={`accordion-item ${!isOpen ? "collapsed" : ""}`}>
        <div className="accordion-content">
          {children.map((item) => (
            <div
              className="radio-wrap"
              style={item.checked ? { background: "#E8E8E8", color: "#3D3C3C", borderRadius: "0px" } : { color: "#77787B" }}
              onClick={() => {
                handlePageChange(item.key, !showConfirmModal);
              }}
            >
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
