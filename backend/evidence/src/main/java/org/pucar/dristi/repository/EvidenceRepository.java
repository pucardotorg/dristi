package org.pucar.dristi.repository;

import lombok.extern.slf4j.Slf4j;
import org.egov.common.contract.models.Document;
import org.egov.tracer.model.CustomException;
import org.pucar.dristi.repository.querybuilder.EvidenceQueryBuilder;
import org.pucar.dristi.repository.rowmapper.*;
import org.pucar.dristi.web.models.Artifact;
import org.pucar.dristi.web.models.Comment;
import org.pucar.dristi.web.models.EvidenceSearchCriteria;
import org.pucar.dristi.web.models.Pagination;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.pucar.dristi.config.ServiceConstants.EVIDENCE_SEARCH_QUERY_EXCEPTION;

@Slf4j
@Repository
public class EvidenceRepository {

    private final EvidenceQueryBuilder queryBuilder;
    private final JdbcTemplate jdbcTemplate;
    private final EvidenceRowMapper evidenceRowMapper;
    private final DocumentRowMapper documentRowMapper;
    private final CommentRowMapper commentRowMapper;

    @Autowired
    public EvidenceRepository(
            EvidenceQueryBuilder queryBuilder,
            JdbcTemplate jdbcTemplate,
            EvidenceRowMapper evidenceRowMapper,
            DocumentRowMapper documentRowMapper,
            CommentRowMapper commentRowMapper
    ) {
        this.queryBuilder = queryBuilder;
        this.jdbcTemplate = jdbcTemplate;
        this.evidenceRowMapper = evidenceRowMapper;
        this.documentRowMapper = documentRowMapper;
        this.commentRowMapper = commentRowMapper;
    }

    public List<Artifact> getArtifacts(EvidenceSearchCriteria evidenceSearchCriteria, Pagination pagination) {
        try {
            List<Object> preparedStmtList = new ArrayList<>();
            List<Integer> preparedStmtArgList = new ArrayList<>();
            List<Object> preparedStmtListCom = new ArrayList<>();
            List<Integer> preparedStmtArgListCom = new ArrayList<>();
            List<Object> preparedStmtListDoc = new ArrayList<>();
            List<Integer> preparedStmtArgListDoc = new ArrayList<>();

            // Artifact query building
            String artifactQuery = queryBuilder.getArtifactSearchQuery(preparedStmtList,preparedStmtArgList,evidenceSearchCriteria);
            artifactQuery = queryBuilder.addOrderByQuery(artifactQuery, pagination);
            log.info("Final artifact query: {}", artifactQuery);

            if (pagination != null) {
                Integer totalRecords = getTotalCountArtifact(artifactQuery, preparedStmtList);
                log.info("Total count without pagination :: {}", totalRecords);
                pagination.setTotalCount(Double.valueOf(totalRecords));
                artifactQuery = queryBuilder.addPaginationQuery(artifactQuery, pagination, preparedStmtList,preparedStmtArgList);
            }

            if(preparedStmtList.size()!=preparedStmtArgList.size()){
                throw new CustomException(EVIDENCE_SEARCH_QUERY_EXCEPTION, "Arg and ArgType size mismatch");
            }
            List<Artifact> artifactList = jdbcTemplate.query(artifactQuery, preparedStmtList.toArray(), preparedStmtArgList.stream().mapToInt(Integer::intValue).toArray(),evidenceRowMapper);
            log.info("DB artifact list :: {}", artifactList);

            // Fetch associated comments
            List<String> artifactIds = new ArrayList<>();
            for (Artifact artifact : artifactList) {
                artifactIds.add(artifact.getId().toString());
            }
            if (artifactIds.isEmpty()) {
                return artifactList;
            }

            // Fetch associated comments
            String commentQuery = queryBuilder.getCommentSearchQuery(artifactIds, preparedStmtListCom, preparedStmtArgListCom);
            log.info("Final comment query: {}", commentQuery);
            if(preparedStmtListCom.size()!=preparedStmtArgListCom.size()){
                throw new CustomException(EVIDENCE_SEARCH_QUERY_EXCEPTION, "Arg and ArgType size mismatch for comment search");
            }
            Map<UUID, List<Comment>> commentMap = jdbcTemplate.query(commentQuery, preparedStmtListCom.toArray(), preparedStmtArgListCom.stream().mapToInt(Integer::intValue).toArray(), commentRowMapper);
            log.info("DB comment map :: {}", commentMap);

            if (commentMap != null) {
                artifactList.forEach(artifact -> {
                    artifact.setComments(commentMap.get(UUID.fromString(String.valueOf(artifact.getId()))));
                });
            }

            // Fetch associated documents
            String documentQuery = queryBuilder.getDocumentSearchQuery(artifactIds, preparedStmtListDoc, preparedStmtArgListDoc);
            log.info("Final document query: {}", documentQuery);
            if(preparedStmtListDoc.size()!=preparedStmtArgListDoc.size()){
                throw new CustomException(EVIDENCE_SEARCH_QUERY_EXCEPTION, "Arg and ArgType size mismatch for document search");
            }
            Map<UUID, Document> documentMap = jdbcTemplate.query(documentQuery, preparedStmtListDoc.toArray(), preparedStmtArgListDoc.stream().mapToInt(Integer::intValue).toArray(), documentRowMapper);
            log.info("DB document map :: {}", documentMap);

            if (documentMap != null) {
                artifactList.forEach(artifact -> {
                    artifact.setFile(documentMap.get(UUID.fromString(String.valueOf(artifact.getId()))));
                });
            }

            return artifactList;
        } catch (CustomException e) {
            log.error("Custom Exception while fetching artifact list");
            throw e;
        } catch (Exception e) {
            log.error("Error while fetching artifact list");
            throw new CustomException("ARTIFACT_SEARCH_EXCEPTION", "Error while fetching artifact list: " + e.toString());
        }
    }


    public Integer getTotalCountArtifact(String baseQuery, List<Object> preparedStmtList) {
        String countQuery = queryBuilder.getTotalCountQuery(baseQuery);
        log.info("Final count query :: {}", countQuery);
        return jdbcTemplate.queryForObject(countQuery, Integer.class, preparedStmtList.toArray());
    }
}

