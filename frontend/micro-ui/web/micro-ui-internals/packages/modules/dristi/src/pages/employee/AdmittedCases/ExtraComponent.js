import React from "react";
import NextHearingCard from "./NextHearingCard";
import OrderDrafts from "./OrderDrafts";
import SubmissionReview from "./SubmissionsReview";

const ExtraComponent = ({ tab, setUpdateCounter, caseData }) => {
  switch (tab) {
    case "Hearings":
    case "Overview":
      return <NextHearingCard caseData={caseData} width={tab === "Overview" ? "70%" : "100%"} />;
    case "Orders":
      return <OrderDrafts caseData={caseData} />;
    case "Submissions":
      return <SubmissionReview caseData={caseData} setUpdateCounter={setUpdateCounter} />;
    default:
      return <React.Fragment />;
  }
};

export default ExtraComponent;
