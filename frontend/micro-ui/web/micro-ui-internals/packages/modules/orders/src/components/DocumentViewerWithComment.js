import React from "react";
import { useTranslation } from "react-i18next";
import CommentBox from "./CommentBox/CommentBox";

const DocumentViewerWithComment = ({ infos, documents, showCommentSection, comments, showAddNewComment, onAddComment }) => {
  const { t } = useTranslation();
  const DocViewerWrapper = window?.Digit?.ComponentRegistryService?.getComponent("DocViewerWrapper");

  return (
    <div className="document-viewer-with-comment">
      <div className={`application-details ${!showCommentSection && "without-comment"}`}>
        <React.Fragment>
          <div className="application-info">
            {infos &&
              infos?.map((info, index) => (
                <div className="info-row" key={index}>
                  <div className="info-key">
                    <h3>{t(info?.key)}</h3>
                  </div>
                  <div className="info-value">
                    <h3>{t(info?.value)}</h3>
                  </div>
                </div>
              ))}
          </div>
          {documents &&
            documents?.map((docs, index) => (
              <React.Fragment>
                {docs && (
                  <div className="application-view" key={index}>
                    <DocViewerWrapper
                      key={docs.fileStoreId}
                      fileStoreId={docs.fileStoreId}
                      displayFilename={docs.fileName}
                      tenantId={Digit.ULBService.getCurrentTenantId()}
                      docWidth="100%"
                      docHeight="unset"
                      showDownloadOption={false}
                      documentName={docs.fileName}
                    />
                  </div>
                )}
              </React.Fragment>
            ))}
        </React.Fragment>
      </div>
      {showCommentSection && <CommentBox comments={comments} onAddComment={onAddComment} showAddNewComment={showAddNewComment} />}
    </div>
  );
};

export default DocumentViewerWithComment;
