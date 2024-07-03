package org.pucar.dristi.repository.querybuilder;
import lombok.extern.slf4j.Slf4j;
import org.egov.tracer.model.CustomException;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static org.pucar.dristi.config.ServiceConstants.*;

@Component
@Slf4j
public class EvidenceQueryBuilder {

    private static final String BASE_ARTIFACT_QUERY = " SELECT art.id as id, art.tenantId as tenantId, art.artifactNumber as artifactNumber, " +
            "art.evidenceNumber as evidenceNumber, art.externalRefNumber as externalRefNumber, art.caseId as caseId, " +
            "art.application as application, art.hearing as hearing, art.orders as orders, art.mediaType as mediaType, " +
            "art.artifactType as artifactType, art.sourceType as sourceType, art.sourceID as sourceID, art.sourceName as sourceName, art.applicableTo as applicableTo, " +
            "art.createdDate as createdDate, art.isActive as isActive, art.isEvidence as isEvidence, art.status as status, art.description as description, " +
            "art.artifactDetails as artifactDetails, art.additionalDetails as additionalDetails, art.createdBy as createdBy, " +
            "art.lastModifiedBy as lastModifiedBy, art.createdTime as createdTime, art.lastModifiedTime as lastModifiedTime ";

    private static final String DOCUMENT_SELECT_QUERY = "SELECT doc.id as id, doc.fileStore as fileStore, doc.documentUid as documentUid, " +
            "doc.documentType as documentType, doc.artifactId as artifactId, doc.additionalDetails as additionalDetails ";

    private static final String COMMENT_SELECT_QUERY = "SELECT com.id as id, com.tenantId as tenantId, com.artifactId as artifactId, " +
            "com.individualId as individualId, com.comment as comment, com.isActive as isActive, com.additionalDetails as additionalDetails, " +
            "com.createdBy as createdBy, com.lastModifiedBy as lastModifiedBy, com.createdTime as createdTime, com.lastModifiedTime as lastModifiedTime ";

    private static final String FROM_ARTIFACTS_TABLE = " FROM dristi_evidence_artifact art";
    private static final String FROM_DOCUMENTS_TABLE = " FROM dristi_evidence_document doc";
    private static final String FROM_COMMENTS_TABLE = " FROM dristi_evidence_comment com";
    private static final String ORDERBY_CREATEDTIME = " ORDER BY art.createdTime DESC ";

    public String getArtifactSearchQuery(List<Object> preparedStmtList,String id, String caseId, String application, String hearing, String order, String sourceId, String sourceName, String artifactNumber) {
        try {
            StringBuilder query = new StringBuilder(BASE_ARTIFACT_QUERY);
            query.append(FROM_ARTIFACTS_TABLE);
            boolean firstCriteria = true; // To check if it's the first criteria

            firstCriteria =addArtifactCriteria(id, query, preparedStmtList, firstCriteria, "art.id = ?");
            firstCriteria =addArtifactCriteria(caseId, query, preparedStmtList, firstCriteria, "art.caseId = ?");
            firstCriteria =addArtifactCriteria(application, query, preparedStmtList, firstCriteria, "art.application = ?");
            firstCriteria =addArtifactCriteria(hearing, query, preparedStmtList, firstCriteria, "art.hearing = ?");
            firstCriteria =addArtifactCriteria(order, query, preparedStmtList, firstCriteria, "art.orders = ?");
            firstCriteria =addArtifactCriteria(sourceId, query, preparedStmtList, firstCriteria, "art.sourceId = ?");
            firstCriteria =addArtifactCriteria(sourceName, query, preparedStmtList, firstCriteria, "art.sourceName = ?");
            addArtifactCriteria(artifactNumber, query, preparedStmtList, firstCriteria, "art.artifactNumber = ?");

            query.append(ORDERBY_CREATEDTIME);

            return query.toString();
        } catch (Exception e) {
            log.error("Error while building artifact search query", e);
            throw new CustomException(EVIDENCE_SEARCH_QUERY_EXCEPTION, "Error occurred while building the artifact search query: " + e.toString());
        }
    }
    private boolean addArtifactCriteria(String criteria, StringBuilder query, List<Object> preparedStmtList, boolean firstCriteria, String criteriaClause) {
        if (criteria != null && !criteria.isEmpty()) {
            addClauseIfRequired(query, firstCriteria);
            query.append(criteriaClause);
            preparedStmtList.add(criteria);
            firstCriteria = false;
        }
        return firstCriteria;
    }


    public String getDocumentSearchQuery(List<String> ids, List<Object> preparedStmtList) {
        try {
            StringBuilder query = new StringBuilder(DOCUMENT_SELECT_QUERY);
            query.append(FROM_DOCUMENTS_TABLE);
            if (!ids.isEmpty()) {
                query.append(" WHERE doc.artifactId IN (")
                        .append(ids.stream().map(id -> "?").collect(Collectors.joining(",")))
                        .append(")");
                preparedStmtList.addAll(ids);
            }

            return query.toString();
        } catch (Exception e) {
            log.error("Error while building document search query");
            throw new CustomException("DOCUMENT_SEARCH_QUERY_EXCEPTION", "Error occurred while building the query: " + e.toString());
        }
    }



    public String getCommentSearchQuery(List<String> artifactIds, List<Object> preparedStmtList) {
        try {
            StringBuilder query = new StringBuilder(COMMENT_SELECT_QUERY);
            query.append(FROM_COMMENTS_TABLE);

            if (artifactIds != null && !artifactIds.isEmpty()) {
                query.append(" WHERE com.artifactId IN (");
                for (int i = 0; i < artifactIds.size(); i++) {
                    query.append("?");
                    preparedStmtList.add(artifactIds.get(i));
                    if (i < artifactIds.size() - 1) {
                        query.append(",");
                    }
                }
                query.append(")");
            }

            return query.toString();
        } catch (Exception e) {
            log.error("Error while building comment search query", e);
            throw new CustomException(COMMENT_SEARCH_QUERY_EXCEPTION, "Error occurred while building the comment search query: " + e.toString());
        }
    }


    private void addClauseIfRequired(StringBuilder query, boolean isFirstCriteria) {
        if (isFirstCriteria) {
            query.append(" WHERE ");
        } else {
            query.append(" AND ");
        }
    }
}