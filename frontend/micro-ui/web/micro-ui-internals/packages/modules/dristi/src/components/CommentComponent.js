import React from "react";
import { FileIcon } from "@egovernments/digit-ui-react-components";

function CommentComponent({ key, comment }) {
  return (
    <div className="comment-body" key={key}>
      <div className="name-logo">
        <div className="comment-avatar">
          <span>{comment?.author?.[0]}</span>
          <span>{comment?.additionalDetails?.author?.[0]}</span>
        </div>
      </div>
      <div className="comment-details">
        <h3 className="comment-header" style={{ marginTop: "5px" }}>
          {comment?.author}
          {comment?.additionalDetails?.author}
          <span className="times-stamp" style={{ color: "#77787B", marginLeft: "10px" }}>
            {comment?.timestamp} {comment?.additionalDetails?.timestamp}{" "}
          </span>
        </h3>
        <p className="comment-text">{comment?.text}</p>
        <p className="comment-text">{comment?.comment}</p>
        {comment?.additionalDetails?.commentDocumentId && (
          <div
            style={{
              border: "1px solid #bbbbbd",
              color: "#505A5F",
              display: "flex",
              alignItems: "center",
              padding: "10px",
              borderRadius: "5px",
              width: "300px",
              fontWeight: "bold",
              gap: "7px",
              marginTop: "10px",
              cursor: "pointer",
            }}
            onClick={() => {
              window.open(
                `/filestore/v1/files/id?tenantId=kl&fileStoreId=${comment?.additionalDetails?.commentDocumentId}`,
                "_blank",
                "noopener,noreferrer"
              );
            }}
          >
            <FileIcon />
            <span style={{ fontWeight: "bold" }}>{comment?.additionalDetails?.commentDocumentName || "Attached File"}</span>
          </div>
        )}
      </div>
    </div>
  );
}

export default CommentComponent;
