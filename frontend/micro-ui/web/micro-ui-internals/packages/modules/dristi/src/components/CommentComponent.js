import React from "react";

function CommentComponent({ key, comment }) {
  return (
    <div className="comment-body" key={key}>
      <div className="name-logo">
        <div className="comment-avatar">
          <span>{comment?.additionalDetails?.author?.[0]}</span>
        </div>
      </div>
      <div className="comment-details">
        <h3 className="comment-header" style={{ marginTop: "5px" }}>
          {comment?.additionalDetails?.author}
          <span className="times-stamp" style={{ color: "#77787B", marginLeft: "10px" }}>
            {comment?.additionalDetails?.timestamp}{" "}
          </span>
        </h3>
        <p className="comment-text">{comment?.comment}</p>
      </div>
    </div>
  );
}

export default CommentComponent;
