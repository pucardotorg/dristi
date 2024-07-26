package org.pucar.dristi.repository.querybuilder;
import lombok.extern.slf4j.Slf4j;
import org.egov.tracer.model.CustomException;
import org.pucar.dristi.web.models.EvidenceSearchCriteria;
import org.pucar.dristi.web.models.Pagination;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

import static org.pucar.dristi.config.ServiceConstants.*;

@Component
@Slf4j
public class EvidenceQueryBuilder {

    private static final String BASE_ARTIFACT_QUERY = " SELECT art.id as id, art.tenantId as tenantId, art.artifactNumber as artifactNumber, " +
            "art.evidenceNumber as evidenceNumber, art.externalRefNumber as externalRefNumber, art.caseId as caseId, " +
            "art.application as application, art.filingNumber as filingNumber, art.hearing as hearing, art.orders as orders, art.mediaType as mediaType, " +
            "art.artifactType as artifactType, art.sourceType as sourceType, art.sourceID as sourceID, art.sourceName as sourceName, art.applicableTo as applicableTo, " +
            "art.comments as comments, art.file as file, art.createdDate as createdDate, art.isActive as isActive, art.isEvidence as isEvidence, art.status as status, art.description as description, " +
            "art.artifactDetails as artifactDetails, art.additionalDetails as additionalDetails, art.createdBy as createdBy, " +
            "art.lastModifiedBy as lastModifiedBy, art.createdTime as createdTime, art.lastModifiedTime as lastModifiedTime ";

    private  static  final String TOTAL_COUNT_QUERY = "SELECT COUNT(*) FROM ({baseQuery}) total_result";
    private static final String DEFAULT_ORDERBY_CLAUSE = " ORDER BY art.createdtime DESC ";
    private static final String ORDERBY_CLAUSE = " ORDER BY art.{orderBy} {sortingOrder} ";
    private static final String FROM_ARTIFACTS_TABLE = " FROM dristi_evidence_artifact art";
    public String getArtifactSearchQuery(List<Object> preparedStmtList, EvidenceSearchCriteria criteria) {
        try {
            StringBuilder query = new StringBuilder(BASE_ARTIFACT_QUERY);
            query.append(FROM_ARTIFACTS_TABLE);
            boolean firstCriteria = true; // To check if it's the first criteria

            // Extract fields from EvidenceSearchCriteria
            UUID owner = criteria.getOwner();
            String artifactType = criteria.getArtifactType();
            Boolean evidenceStatus = criteria.getEvidenceStatus();
            String id = criteria.getId();
            String caseId = criteria.getCaseId();
            String application = criteria.getApplicationNumber();
            String filingNumber = criteria.getFilingNumber();
            String hearing = criteria.getHearing();
            String order = criteria.getOrder();
            String sourceId = criteria.getSourceId();
            String sourceName = criteria.getSourceName();
            String artifactNumber = criteria.getArtifactNumber();

            // Build the query using the extracted fields
            firstCriteria = addArtifactCriteria(id, query, preparedStmtList, firstCriteria, "art.id = ?");
            firstCriteria = addArtifactCriteria(caseId, query, preparedStmtList, firstCriteria, "art.caseId = ?");
            firstCriteria = addArtifactCriteria(application, query, preparedStmtList, firstCriteria, "art.application = ?");
            firstCriteria = addArtifactCriteria(artifactType, query, preparedStmtList, firstCriteria, "art.artifactType = ?");
            firstCriteria = addArtifactCriteria(evidenceStatus, query, preparedStmtList, firstCriteria, "art.isEvidence = ?");
            firstCriteria = addArtifactCriteria(filingNumber, query, preparedStmtList, firstCriteria, "art.filingNumber = ?");
            firstCriteria = addArtifactCriteria(hearing, query, preparedStmtList, firstCriteria, "art.hearing = ?");
            firstCriteria = addArtifactCriteria(order, query, preparedStmtList, firstCriteria, "art.orders = ?");
            firstCriteria = addArtifactCriteria(sourceId, query, preparedStmtList, firstCriteria, "art.sourceId = ?");
            firstCriteria = addArtifactCriteria(owner != null ? owner.toString() : null, query, preparedStmtList, firstCriteria, "art.createdBy = ?");
            firstCriteria = addArtifactCriteria(sourceName, query, preparedStmtList, firstCriteria, "art.sourceName = ?");
            addArtifactPartialCriteria(artifactNumber, query, preparedStmtList, firstCriteria, "art.artifactNumber");

            return query.toString();
        } catch (Exception e) {
            log.error("Error while building artifact search query", e);
            throw new CustomException(EVIDENCE_SEARCH_QUERY_EXCEPTION, "Error occurred while building the artifact search query: " + e.toString());
        }
    }

    boolean addArtifactPartialCriteria(String criteria, StringBuilder query, List<Object> preparedStmtList, boolean firstCriteria, String criteriaClause) {
        if (criteria != null && !criteria.isEmpty()) {
            addClauseIfRequired(query, firstCriteria);
            query.append(criteriaClause).append(" LIKE ?");
            preparedStmtList.add("%" + criteria + "%"); // Add wildcard characters for partial match
            firstCriteria = false;
        }
        return firstCriteria;
    }
    boolean addArtifactCriteria(String criteria, StringBuilder query, List<Object> preparedStmtList, boolean firstCriteria, String criteriaClause) {
        if (criteria != null && !criteria.isEmpty()) {
            addClauseIfRequired(query, firstCriteria);
            query.append(criteriaClause);
            preparedStmtList.add(criteria);
            firstCriteria = false;
        }
        return firstCriteria;
    }
    boolean     addArtifactCriteria(Boolean criteria, StringBuilder query, List<Object> preparedStmtList, boolean firstCriteria, String criteriaClause) {
        if (criteria != null) {
            addClauseIfRequired(query, firstCriteria);
            query.append(criteriaClause);
            preparedStmtList.add(criteria);
            firstCriteria = false;
        }
        return firstCriteria;
    }

    public String getTotalCountQuery(String baseQuery) {
        return TOTAL_COUNT_QUERY.replace("{baseQuery}", baseQuery);
    }
    public String addOrderByQuery(String query, Pagination pagination) {
        if (pagination == null || pagination.getSortBy() == null || pagination.getOrder() == null) {
            return query + DEFAULT_ORDERBY_CLAUSE;
        } else {
            query = query + ORDERBY_CLAUSE;
        }
        return query.replace("{orderBy}", pagination.getSortBy()).replace("{sortingOrder}", pagination.getOrder().name());
    }

    public String addPaginationQuery(String query, Pagination pagination, List<Object> preparedStatementList) {
        preparedStatementList.add(pagination.getLimit());
        preparedStatementList.add(pagination.getOffSet());
        return query + " LIMIT ? OFFSET ?";
    }


    private void addClauseIfRequired(StringBuilder query, boolean isFirstCriteria) {
        if (isFirstCriteria) {
            query.append(" WHERE ");
        } else {
            query.append(" AND ");
        }
    }
}