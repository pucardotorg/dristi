import React from "react";
import { FlagIcon } from "../icons/svgIndex";

function FlagBox({ t, judgeObj }) {
  return (
    <div key={"ErrorAccordion"} className="accordion-wrapper">
      <div className={`open total-error-count`}>
        <span style={{ color: "#BB2C2F" }}>
          {" "}
          <FlagIcon isError={true} />
          {`${t("JUDGE_REMARKS")}`}
        </span>
        <div style={{ padding: "10px", color: "#3D3C3C", alignItems: "center" }}>
          <label> {judgeObj?.comment}</label>
        </div>
      </div>
    </div>
  );
}

export default FlagBox;
