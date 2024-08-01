import { Button } from "@egovernments/digit-ui-components";
import React from "react";

const HearingSideCard = ({ hearingId, caseId, filingNumber }) => {
  const handleNavigate = (path, extraSearchParams) => {
    const userInfo = JSON.parse(window.localStorage.getItem("user-info"));
    const userType = userInfo.type === "CITIZEN" ? "citizen" : "employee";
    const searchParams = new URLSearchParams({ hearingId, caseId, filingNumber, ...extraSearchParams });
    window.open(`${window.location.origin}/${window.contextPath}/${userType}/${path}?${searchParams.toString()}`, "_blank");
  };

  return (
    <div className="hearing-side-card">
      <div className="hearing-details">
        <div className="hearing-title">Case Details</div>
        <div className="hearing-buttons">
          <Button
            label={"Orders"}
            variation={"default"}
            style={{ width: "100%", justifyContent: "center" }}
            iconFill={"#1C1B1F"}
            onClick={() => handleNavigate("dristi/home/view-case", { tab: "Orders" })}
          />
          <Button
            label={"Submissions"}
            variation={"default"}
            style={{ width: "100%", justifyContent: "center" }}
            icon={""}
            iconFill={"#1C1B1F"}
            onClick={() => handleNavigate("dristi/home/view-case", { tab: "Submissions" })}
          />
          <Button
            label={"Case History"}
            variation={"default"}
            style={{ width: "100%", justifyContent: "center" }}
            icon={""}
            iconFill={"#1C1B1F"}
            onClick={() => handleNavigate("dristi/home/view-case", { tab: "History" })}
          />
          <Button
            label={"Parties"}
            variation={"default"}
            style={{ width: "100%", justifyContent: "center" }}
            icon={"Person"}
            iconFill={"#1C1B1F"}
            onClick={() => handleNavigate("dristi/home/view-case", { tab: "Parties" })}
          />
        </div>
      </div>
    </div>
  );
};
export default HearingSideCard;
