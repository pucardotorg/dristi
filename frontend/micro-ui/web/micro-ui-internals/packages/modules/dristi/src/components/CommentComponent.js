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
        <h3 className="comment-header">
          {comment?.author} <br />
          <span className="times-stamp">{comment?.timestamp} </span>
        </h3>
        <p className="comment-text">{comment?.text}</p>
      </div>
    </div>
  );
}

export default CommentComponent;
