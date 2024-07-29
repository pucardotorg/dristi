import React from "react";
import NextHearingCard from "./NextHearingCard";
import OrderDrafts from "./OrderDrafts";
import SubmissionReview from "./SubmissionsReview";

const ExtraComponent = ({ tab, setUpdateCounter, caseData, setOrderModal, openSubmissionsViewModal }) => {
  const userRoles = Digit.UserService.getUser()?.info?.roles.map((role) => role.code);
  switch (tab) {
    case "Hearings":
    case "Overview":
      return <NextHearingCard caseData={caseData} width={"100%"} />;
    case "Orders":
      return !userRoles.includes("CITIZEN") && <OrderDrafts caseData={caseData} setOrderModal={setOrderModal} />;
    case "Submissions":
      return <SubmissionReview caseData={caseData} setUpdateCounter={setUpdateCounter} openSubmissionsViewModal={openSubmissionsViewModal} />;
    default:
      return <React.Fragment />;
  }
};

export default ExtraComponent;
