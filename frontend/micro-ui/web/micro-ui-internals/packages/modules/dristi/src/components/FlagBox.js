import React from "react";
import { FlagIcon } from "../icons/svgIndex";

function FlagBox({ t, judgeObj }) {
  return (
    <div key={"ErrorAccordion"} className="accordion-wrapper">
      <div className={`open total-error-count-judge`}>
        <div className="error-flag-icon-div">
          <FlagIcon isError={true} />
          <h2> {`${t("JUDGE_REMARKS")}`}</h2>
        </div>
        <div style={{ padding: "10px 10px 0px 0px", color: "#3D3C3C", alignItems: "center" }}>
          <label> {judgeObj?.comment}</label>
        </div>
      </div>
    </div>
  );
}

export default FlagBox;
