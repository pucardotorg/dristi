import { ArrowRightInbox } from "@egovernments/digit-ui-components";
import React from "react";
import { useHistory } from "react-router-dom/cjs/react-router-dom.min";

const ReviewCard = ({ data, userInfoType }) => {
  const history = useHistory();
  return (
    <div className="review-card-main">
      {data?.map((row) => (
        <div className="review-card">
          <div className="review-card-title-main">
            <div className="review-card-logo">{row?.logo}</div>
            <div className="review-card-title">{row?.title}</div>
          </div>
          <div className="review-card-action-main">
            <div className="review-card-action-arrow">
              <span
                onClick={() => {
                  history.push(`/${window?.contextPath}/${userInfoType}/${row?.actionLink}`);
                }}
              >
                <ArrowRightInbox />
              </span>
            </div>
          </div>
        </div>
      ))}
    </div>
  );
};

export default ReviewCard;
