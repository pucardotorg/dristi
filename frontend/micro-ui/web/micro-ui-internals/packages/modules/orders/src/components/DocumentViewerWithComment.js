import React from "react";
import { useTranslation } from "react-i18next";
import CommentBox from "./CommentBox/CommentBox";
import ApplicationInfoComponent from "./ApplicationInfoComponent";

const DocumentViewerWithComment = ({ infos, links, documents, showCommentSection, comments, showAddNewComment, onAddComment }) => {
  const { t } = useTranslation();
  const DocViewerWrapper = Digit?.ComponentRegistryService?.getComponent("DocViewerWrapper");

  return (
    <div className="document-viewer-with-comment">
      <div className={`application-details ${!showCommentSection && "without-comment"}`}>
        <React.Fragment>
          {infos && <ApplicationInfoComponent infos={infos} links={links} />}
          {documents &&
            documents?.map((docs, index) => (
              <React.Fragment>
                {docs && (
                  <div className="application-view" key={index}>
                    <DocViewerWrapper
                      key={docs.fileStore}
                      fileStoreId={docs.fileStore}
                      // displayFilename={docs.fileName}
                      tenantId={Digit.ULBService.getCurrentTenantId()}
                      docWidth="100%"
                      docHeight="unset"
                      showDownloadOption={false}
                      // documentName={docs.fileName}
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
