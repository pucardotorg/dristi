import React from "react";
import NextHearingCard from "./NextHearingCard";
import OrderDrafts from "./OrderDrafts";
import SubmissionReview from "./SubmissionsReview";

const ExtraComponent = ({ tab, setUpdateCounter }) => {
  switch (tab) {
    case "Hearings":
    case "Overview":
      return <NextHearingCard width={tab === "Overview" ? "70%" : "100%"} />;
    case "Orders":
      return <OrderDrafts />;
    case "Submissions":
      return <SubmissionReview setUpdateCounter={setUpdateCounter} />;
    default:
      return <React.Fragment />;
  }
};

export default ExtraComponent;
