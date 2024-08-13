import React, { useState } from "react";
import { CustomArrowDownIcon } from "@egovernments/digit-ui-module-dristi/src/icons/svgIndex";
import { useTranslation } from "react-i18next";

const Accordian = ({ groupedData, caseDataDetails }) => {
  const { t } = useTranslation();
  const [isOpen, setIsOpen] = useState(false);
  const handleAccordionClick = () => {
    setIsOpen(!isOpen);
  };

  return (
    <div className="accordion-wrapper" style={{ border: "1px solid #E8E8E8", padding: 16, borderRadius: 4 }}>
      <div
        className={`accordion-title ${isOpen ? "open" : ""}`}
        style={{ cursor: "default", marginBottom: isOpen ? 16 : 0, transition: "margin-bottom 0.25s" }}
        onClick={handleAccordionClick}
      >
        <span
          style={{
            color: "#231F20",
            fontFamily: "Roboto",
            fontSize: "16px",
            fontWeight: "700",
            lineHeight: "18.75px",
            textAlign: "left",
          }}
        >{`${t("CASE_WITH_PENDING_TASKS")} (${groupedData?.length})`}</span>
        <div
          className="icon"
          style={{
            marginRight: 4,
          }}
        >
          <span className="reverse-arrow" style={{ cursor: "pointer" }} onClick={handleAccordionClick}>
            <CustomArrowDownIcon />
          </span>
        </div>
      </div>
      <div className={`accordion-item ${!isOpen ? "collapsed" : ""}`}>
        <div
          className={`accordion-item ${!isOpen ? "collapsed" : ""}`}
          style={{ overflowY: "auto", maxHeight: "40vh", paddingRight: "8px", "&::WebkitScrollbar": { width: 0 } }}
        >
          {groupedData
            .filter((data) => data !== null) // Filter out null values
            .map((data) => {
              const matchedCase = caseDataDetails.find((caseData) => caseData.filingNumber === data.filingNumber);
              return (
                <div key={data.filingNumber}>
                  {matchedCase && (
                    <div style={{ width: "100%", padding: "16px 12px", borderBottom: "1px solid #E8E8E8" }}>
                      <div style={{ fontWeight: "400", fontSize: "16px", marginBottom: "3px" }}>{matchedCase?.caseDetail?.caseTitle}</div>
                      <div
                        style={{
                          fontWeight: 400,
                          fontSize: "14px",
                          color: "#3D3C3C",
                          display: "flex",
                          justifyContent: "space-between",
                        }}
                      >
                        <div>NIA S138</div>
                        <div>PB-PT-2023</div>
                        <div style={{ color: "#9E400A" }}>{`${data?.data?.length} tasks`}</div>
                      </div>
                    </div>
                  )}
                </div>
              );
            })}
        </div>
      </div>
    </div>
  );
};

export default Accordian;
