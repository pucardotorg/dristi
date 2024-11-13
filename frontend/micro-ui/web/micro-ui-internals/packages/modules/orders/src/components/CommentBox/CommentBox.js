import React, { useState } from "react";
import PropTypes from "prop-types";
import { TextInput } from "@egovernments/digit-ui-react-components";
import Comment from "./Comment";
import { useTranslation } from "react-i18next";

const CommentBox = ({ comments = [], onAddComment, showAddNewComment }) => {
  const { t } = useTranslation();
  const [newComment, setNewComment] = useState("");
  const RightArrow = window?.Digit?.ComponentRegistryService?.getComponent("RightArrow");
  const user = Digit.UserService.getUser()?.info?.name;

  const handleInputChange = (event) => {
    setNewComment(event.target.value);
  };

  const handleAddComment = () => {
    if (newComment.trim() !== "") {
      onAddComment({
        author: user,
        timeStamp: new Date().toISOString().split("T")[0],
        text: newComment,
      });
      setNewComment("");
    }
  };
  return (
    <div className="application-comment">
      <div className="comment-section">
        <h1 className="comment-xyzoo">{t("DOC_COMMENTS")}</h1>
        <div className="comment-main">
          {comments?.map((comment, index) => (
            <Comment key={index} comment={comment} />
          ))}
        </div>
      </div>
      {showAddNewComment && (
        <div className="comment-send">
          <div className="comment-input-wrapper">
            <TextInput placeholder={"Type here..."} value={newComment} onChange={handleInputChange} />
            <div className="send-comment-btn" onClick={handleAddComment}>
              <RightArrow />
            </div>
          </div>
        </div>
      )}
    </div>
  );
};

CommentBox.propTypes = {
  comments: PropTypes.arrayOf(
    PropTypes.shape({
      id: PropTypes.oneOfType([PropTypes.string, PropTypes.number]).isRequired,
      text: PropTypes.string.isRequired,
    })
  ),
  onAddComment: PropTypes.func.isRequired,
  showAddNewComment: PropTypes.bool,
};

export default CommentBox;
