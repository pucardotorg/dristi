import React from "react";
import PropTypes from "prop-types";

const Comment = ({ key, comment }) => {
  return (
    <div className="comment-body" key={key}>
      <div className="name-logo">
        <div className="comment-avatar">
          <span>{comment?.author?.[0]}</span>
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
};

Comment.propTypes = {
  key: PropTypes.oneOfType([PropTypes.string, PropTypes.number]).isRequired,
  comment: PropTypes.shape({
    author: PropTypes.string.isRequired,
    timestamp: PropTypes.oneOfType([PropTypes.string, PropTypes.instanceOf(Date)]).isRequired,
    text: PropTypes.string.isRequired,
  }).isRequired,
};

export default Comment;
