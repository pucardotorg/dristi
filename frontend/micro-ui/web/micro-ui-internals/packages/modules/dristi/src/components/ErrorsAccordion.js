import React, { useState } from "react";
import { CustomArrowDownIcon } from "../icons/svgIndex";

function ErrorsAccordion({ handlePageChange, pages, t, showConfirmModal, totalErrorCount }) {
  const [isOpen, setIsOpen] = useState(false);
  const handleAccordionClick = () => {
    setIsOpen((prev) => {
      return !prev;
    });
  };
  return (
    <div key={"ErrorAccordion"} className="accordion-wrapper">
      <div className={`accordion-title ${isOpen ? "open" : ""} total-error-count`} onClick={handleAccordionClick}>
        <span style={{ color: "#BB2C2F" }}>{`${totalErrorCount} ${t("CS_ERRORS_MARKED")}`}</span>
        <div className="icon">
          <span className="reverse-arrow">
            <CustomArrowDownIcon />
          </span>
        </div>
      </div>
      <div className={`accordion-item ${!isOpen ? "collapsed" : ""}`}>
        <div className={`accordion-item ${!isOpen ? "collapsed" : ""}`}>
          {pages?.map((item, index) => (
            <div
              style={{ padding: "10px", color: "#BB2C2F", cursor: "pointer", display: "flex", alignItems: "center", justifyContent: "space-between" }}
              onClick={() => {
                handlePageChange(item?.key, !showConfirmModal);
              }}
              key={index}
            >
              <label>{t(item?.label)}</label>
              <labal> {`${item?.errorCount || 0} ${t("CS_ERRORS")}`} </labal>
            </div>
          ))}
        </div>
      </div>
    </div>
  );
}

export default ErrorsAccordion;
