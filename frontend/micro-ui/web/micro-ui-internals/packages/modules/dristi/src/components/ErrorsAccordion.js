import React, { useState } from "react";
import { CustomArrowDownIcon, CustomArrowUpIcon } from "../icons/svgIndex";

function ErrorsAccordion({ handlePageChange, pages, t, showConfirmModal, totalErrorCount, handleGoToPage, selected }) {
  const [isOpen, setIsOpen] = useState(false);
  const handleAccordionClick = () => {
    // disabling accordion Functionality as per the requirements
    // setIsOpen((prev) => {
    //   return !prev;
    // });
  };
  const index = pages.findIndex((page) => page.key === selected);
  const resultIndex = index === -1 ? pages.length : index + 1;

  const hangelGoToNext = () => {
    if (resultIndex < pages.length) {
      handleGoToPage(pages[resultIndex]?.key);
    }
  };
  const handleGoToPrev = () => {
    if (selected === pages[resultIndex - 1]?.key) {
      if (resultIndex > 1) {
        handleGoToPage(pages[resultIndex - 2]?.key);
      }
    } else {
      handleGoToPage(pages[resultIndex - 1]?.key);
    }
  };

  return (
    <div key={"ErrorAccordion"} className="accordion-wrapper">
      <div className={`accordion-title ${isOpen ? "open" : ""} total-error-count`} style={{ cursor: "default" }} onClick={handleAccordionClick}>
        <span style={{ color: "#BB2C2F" }}>{`${totalErrorCount} ${t("CS_ERRORS_MARKED")}`}</span>
        <div className="icon">
          <span className="reverse-arrow" style={{ cursor: "pointer" }} onClick={hangelGoToNext}>
            <CustomArrowDownIcon />
          </span>
          <span>{`${resultIndex}/${pages.length}`}</span>
          <span className="reverse-arrow" style={{ cursor: "pointer" }} onClick={handleGoToPrev}>
            <CustomArrowUpIcon />
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
