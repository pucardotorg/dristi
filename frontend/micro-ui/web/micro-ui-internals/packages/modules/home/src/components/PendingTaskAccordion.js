import { CustomArrowUpIcon } from "@egovernments/digit-ui-module-dristi/src/icons/svgIndex";
import React, { useState } from "react";
// import { CustomArrowDownIcon, CustomArrowUpIcon } from "../icons/svgIndex";

function PendingTaskAccordion({
  handlePageChange,
  pendingTasks,
  t,
  showConfirmModal,
  totalCount,
  handleGoToPage,
  selected,
  accordionHeader = "Complete this week",
  accordionKey = "accordion",
  isHighlighted = false,
  isAccordionOpen = false,
}) {
  const [isOpen, setIsOpen] = useState(isAccordionOpen);
  const handleAccordionClick = () => {
    setIsOpen(!isOpen);
  };

  return (
    <div key={accordionKey} className="accordion-wrapper" style={{ border: "1px solid #E8E8E8", padding: 16, borderRadius: 4 }}>
      <div
        className={`accordion-title ${isOpen ? "open" : ""}`}
        style={{ cursor: "default", marginBottom: isOpen ? 16 : 0, transition: "margin-bottom 0.25s" }}
        onClick={handleAccordionClick}
      >
        <span
          style={{
            color: isHighlighted ? "#9E400A" : "black",
            fontFamily: "Roboto",
            fontSize: "16px",
            fontWeight: "700",
            lineHeight: "18.75px",
            textAlign: "left",
          }}
        >{`${t(accordionHeader)}${totalCount ? ` (${totalCount})` : ""}`}</span>
        <div
          className="icon"
          style={{
            marginRight: 4,
          }}
        >
          <span className="reverse-arrow" style={{ cursor: "pointer" }} onClick={handleAccordionClick}>
            <CustomArrowUpIcon />
          </span>
        </div>
      </div>
      <div className={`accordion-item ${!isOpen ? "collapsed" : ""}`}>
        <div className={`accordion-item ${!isOpen ? "collapsed" : ""}`}>
          {pendingTasks?.map((item) => (
            <div className={`task-item ${item?.due === "Due today" && "due-today"}`} key={item?.filingNumber}>
              <input type="checkbox" />
              <div className="task-details" style={{ display: "flex", flexDirection: "column", gap: 8, marginLeft: 8 }}>
                <span className="task-title">
                  {item?.actionName} : {item?.caseTitle}
                </span>
                <span className="task-info">
                  {item?.caseType} - {item?.filingNumber} - {item?.due}
                </span>
              </div>
            </div>
          ))}
        </div>
      </div>
    </div>
  );
}

export default PendingTaskAccordion;
