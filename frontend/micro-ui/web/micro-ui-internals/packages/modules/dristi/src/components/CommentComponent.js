import React from "react";

function CommentComponent({ key, comment }) {
  return (
    <div className="comment-body" key={key}>
      <div className="name-logo">
        <div className="comment-avatar">
          <span>{comment?.author[0]}</span>
        </div>
      </div>
      <div className="comment-details">
        <h3 className="comment-header" style={{ marginTop: "5px" }}>
          {comment?.author}
          <span className="times-stamp" style={{ color: "#77787B", marginLeft: "10px" }}>
            {comment?.timestamp}{" "}
          </span>
        </h3>
        <p className="comment-text">{comment?.text}</p>
      </div>
    </div>
  );
}

export default CommentComponent;
