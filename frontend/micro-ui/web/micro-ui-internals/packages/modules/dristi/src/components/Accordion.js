import React from "react";
import { CustomArrowDownIcon, CustomArrowUpIcon, CustomCompleteIcon, CustomSchedule } from "../icons/svgIndex";

function Accordion({ t, title, handlePageChange, handleAccordionClick, children, parentIndex, isOpen, }) {
  return (
    <div className="accordion-wrapper">
      <div className={`accordion-title ${isOpen ? "open" : ""}`} onClick={handleAccordionClick}>
        <span>{`${parentIndex + 1}. ${t(title)}`}</span>
        <div className="icon">
          <CustomSchedule />
          <span>4m</span>
          <CustomArrowDownIcon />
        </div>
      </div>
      <div className={`accordion-item ${!isOpen ? "collapsed" : ""}`}>
        <div className="accordion-content">
          {children.map((item, index) => (
            <div
              className="radio-wrap"
              style={item.checked ? { background: "#E8E8E8", color: "#3D3C3C", borderRadius: "0px" } : { color: "#77787B" }}
              onClick={() => {
                handlePageChange(parentIndex, index);
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
